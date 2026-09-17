package dev.prathamesh.ai.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import dev.prathamesh.ai.dto.EnhancedChatResponse;
import dev.prathamesh.ai.dto.EnhancedChatResponse.RoomResult;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.repository.RoomRepo;

/**
 * Parser for room search result responses
 */
@Component
public class SearchResultsResponseParser implements ResponseParser {

    private final RoomRepo roomRepo;
    private static final int PRIORITY = 100; // Higher priority

    public SearchResultsResponseParser(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    @Override
    public boolean canHandle(String response) {
        String lower = response.toLowerCase();
        return lower.contains("rooms available") 
            || lower.contains("room id")
            || (lower.contains("hotel") && response.contains("₹"));
    }

    @Override
    public EnhancedChatResponse parse(String response) {
        EnhancedChatResponse result = new EnhancedChatResponse(response);
        result.setResponseType("SEARCH_RESULTS");
        result.setSearchResults(parseRoomResults(response));
        result.setSuggestedActions(List.of(
            "Filter by price",
            "See more options",
            "Different dates"
        ));
        return result;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * Parse room results from text response
     * Matches patterns like: Room ID: 4 - Type: Deluxe - Price: ₹3,000
     */
    private List<RoomResult> parseRoomResults(String response) {
        List<RoomResult> results = new ArrayList<>();

        // Pattern to extract room data
        Pattern pattern = Pattern.compile(
            "Room ID[:\\s]*([\\d]+)[^\\n]*?" +
            "Type[:\\s]*([^\\n,-]+)[^\\n]*?" +
            "(?:Bed[s]?[:\\s]*([\\d]+)[^\\n]*?)?" +
            "Price[:\\s]*₹([\\d,]+)",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(response);
        int index = 0;

        while (matcher.find() && index < 5) { // Limit to 5 results
            try {
                Long roomId = Long.parseLong(matcher.group(1).trim());
                String roomType = matcher.group(2).trim();
                Integer beds = null;
                if (matcher.group(3) != null) {
                    beds = Integer.parseInt(matcher.group(3).trim());
                }
                Double price = Double.parseDouble(matcher.group(4).replaceAll(",", ""));

                // Fetch actual room details from database
                RoomModel roomModel = roomRepo.findById(roomId).orElse(null);

                RoomResult result = new RoomResult();
                result.setRoomId(roomId);
                result.setRoomType(roomType);
                result.setPrice(price);

                if (roomModel != null) {
                    result.setHotelName(roomModel.getHotel().getName());
                    result.setLocation(roomModel.getHotel().getLocation());
                    result.setNoOfBeds(beds != null ? beds : roomModel.getNoOfBeds());
                    result.setRating(roomModel.getHotel().getRating());
                    result.setStatus(roomModel.getStatus().toString());
                    result.setImages(roomModel.getImages());
                } else {
                    // Fallback values
                    result.setHotelName("Hotel");
                    result.setLocation("Pune");
                    result.setNoOfBeds(beds != null ? beds : 1);
                    result.setRating(BigDecimal.valueOf(4.5));
                    result.setStatus("AVAILABLE");
                }

                results.add(result);
                index++;
            } catch (NumberFormatException e) {
                // Skip malformed entries
                continue;
            }
        }

        return results;
    }
}