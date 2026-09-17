package dev.prathamesh.ai.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import dev.prathamesh.ai.dto.EnhancedChatResponse;
import dev.prathamesh.ai.dto.EnhancedChatResponse.BookingConfirmation;
import dev.prathamesh.ai.dto.EnhancedChatResponse.RoomResult;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.repository.RoomRepo;

/**
 * Service to enrich LLM responses with structured data for better frontend rendering.
 * Parses text responses and extracts structured information.
 */
@Service
public class ResponseEnricher {

    private final RoomRepo roomRepo;

    public ResponseEnricher(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    /**
     * Enriches a plain text response with structured data
     */
    public EnhancedChatResponse enrich(String textResponse) {
        EnhancedChatResponse response = new EnhancedChatResponse(textResponse);

        // Detect response type and parse accordingly
        if (isSearchResultsResponse(textResponse)) {
            response.setResponseType("SEARCH_RESULTS");
            response.setSearchResults(parseSearchResults(textResponse));
            response.setSuggestedActions(List.of("Filter by price", "See more options", "Different dates"));
        } 
        else if (isBookingConfirmationResponse(textResponse)) {
            response.setResponseType("BOOKING_CONFIRMATION");
            response.setBookingConfirmation(parseBookingConfirmation(textResponse));
            response.setSuggestedActions(List.of("View all bookings", "Search again", "Download receipt"));
        }
        else if (isRoomSelectionResponse(textResponse)) {
            response.setResponseType("ROOM_SELECTED");
            response.setSuggestedActions(List.of("Proceed with booking", "View other rooms"));
        }
        else if (isQuestionResponse(textResponse)) {
            response.setResponseType("QUESTION");
            response.setSuggestedActions(generateContextualActions(textResponse));
        }
        else {
            response.setResponseType("TEXT");
        }

        return response;
    }

    /**
     * Detects if response contains room search results
     */
    private boolean isSearchResultsResponse(String response) {
        return response.toLowerCase().contains("rooms available") 
            || response.toLowerCase().contains("room id")
            || response.toLowerCase().contains("hotel")
            || (response.contains("₹") && response.contains("night"));
    }

    /**
     * Detects if response is a booking confirmation
     */
    private boolean isBookingConfirmationResponse(String response) {
        return response.toLowerCase().contains("booking has been successfully confirmed")
            || response.toLowerCase().contains("booking id")
            || response.toLowerCase().contains("status: confirmed");
    }

    /**
     * Detects if response indicates room selection
     */
    private boolean isRoomSelectionResponse(String response) {
        return response.toLowerCase().contains("selected") 
            && response.toLowerCase().contains("room id");
    }

    /**
     * Detects if response is asking a question
     */
    private boolean isQuestionResponse(String response) {
        return response.contains("?") && response.length() < 300;
    }

    /**
     * Parses room search results from text response
     * Pattern: Room ID: X - Type: Y - Beds: Z - Price: ₹ABC
     */
    private List<RoomResult> parseSearchResults(String response) {
        List<RoomResult> results = new ArrayList<>();

        // Pattern to match room entries
        // Matches: Room ID: 4 - Type: Deluxe ... Price: ₹3,000
        Pattern pattern = Pattern.compile(
            "Room ID[:\\s]*([\\d]+)[^\\n]*" +
            "Type[:\\s]*([^\\n,-]+)[^\\n]*" +
            "Price[:\\s]*₹([\\d,]+)",
            Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(response);
        int index = 0;

        while (matcher.find() && index < 5) { // Limit to 5 results
            try {
                Long roomId = Long.parseLong(matcher.group(1).trim());
                String roomType = matcher.group(2).trim();
                Double price = Double.parseDouble(matcher.group(3).replaceAll(",", ""));

                // Try to fetch actual room details from database for more accurate info
                RoomModel roomModel = roomRepo.findById(roomId).orElse(null);

                RoomResult result = new RoomResult();
                result.setRoomId(roomId);
                result.setRoomType(roomType);
                result.setPrice(price);

                if (roomModel != null) {
                    result.setHotelName(roomModel.getHotel().getName());
                    result.setLocation(roomModel.getHotel().getLocation());
                    result.setNoOfBeds(roomModel.getNoOfBeds());
                    result.setRating(roomModel.getHotel().getRating());
                    result.setStatus(roomModel.getStatus().toString());
                    result.setImages(roomModel.getImages());
                } else {
                    // Fallback values if room not found
                    result.setHotelName("Hotel");
                    result.setLocation("Pune");
                    result.setNoOfBeds(1);
                    result.setRating(BigDecimal.valueOf(4.5));
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

    /**
     * Parses booking confirmation details from text response
     * Pattern: Booking ID: X, Hotel: Y, Check-in: Z, Total: ₹ABC
     */
    private BookingConfirmation parseBookingConfirmation(String response) {
        BookingConfirmation confirmation = new BookingConfirmation();

        // Extract Booking ID
        Pattern bookingIdPattern = Pattern.compile("Booking ID[:\\s]*([\\d]+)", Pattern.CASE_INSENSITIVE);
        Matcher idMatcher = bookingIdPattern.matcher(response);
        if (idMatcher.find()) {
            confirmation.setBookingId(Long.parseLong(idMatcher.group(1)));
        }

        // Extract Hotel Name
        Pattern hotelPattern = Pattern.compile("Hotel[:\\s]*([^\\n,-]+)", Pattern.CASE_INSENSITIVE);
        Matcher hotelMatcher = hotelPattern.matcher(response);
        if (hotelMatcher.find()) {
            confirmation.setHotel(hotelMatcher.group(1).trim());
        }

        // Extract Room Type
        Pattern roomTypePattern = Pattern.compile("Room[:\\s]*(?:ID[:\\s]*[\\d]+)?[^\\n]*?([A-Za-z\\s]+(?:Deluxe|Standard|Premium))", Pattern.CASE_INSENSITIVE);
        Matcher roomTypeMatcher = roomTypePattern.matcher(response);
        if (roomTypeMatcher.find()) {
            confirmation.setRoomType(roomTypeMatcher.group(1).trim());
        }

        // Extract Check-in
        Pattern checkInPattern = Pattern.compile("Check-in[:\\s]*([^\\n,-]+)", Pattern.CASE_INSENSITIVE);
        Matcher checkInMatcher = checkInPattern.matcher(response);
        if (checkInMatcher.find()) {
            confirmation.setCheckInDate(checkInMatcher.group(1).trim());
        }

        // Extract Check-out
        Pattern checkOutPattern = Pattern.compile("Check-out[:\\s]*([^\\n,-]+)", Pattern.CASE_INSENSITIVE);
        Matcher checkOutMatcher = checkOutPattern.matcher(response);
        if (checkOutMatcher.find()) {
            confirmation.setCheckOutDate(checkOutMatcher.group(1).trim());
        }

        // Extract Number of Guests
        Pattern guestPattern = Pattern.compile("Guests[:\\s]*([\\d]+)", Pattern.CASE_INSENSITIVE);
        Matcher guestMatcher = guestPattern.matcher(response);
        if (guestMatcher.find()) {
            confirmation.setNumGuests(Integer.parseInt(guestMatcher.group(1)));
        }

        // Extract Total Amount
        Pattern amountPattern = Pattern.compile("₹([\\d,]+)", Pattern.CASE_INSENSITIVE);
        Matcher amountMatcher = amountPattern.matcher(response);
        if (amountMatcher.find()) {
            confirmation.setTotalAmount(Double.parseDouble(amountMatcher.group(1).replaceAll(",", "")));
        }

        // Extract Status
        Pattern statusPattern = Pattern.compile("Status[:\\s]*([A-Z]+)", Pattern.CASE_INSENSITIVE);
        Matcher statusMatcher = statusPattern.matcher(response);
        if (statusMatcher.find()) {
            confirmation.setStatus(statusMatcher.group(1).toUpperCase());
        } else {
            confirmation.setStatus("CONFIRMED");
        }

        return confirmation;
    }

    /**
     * Generates contextual actions based on the response content
     */
    private List<String> generateContextualActions(String response) {
        List<String> actions = new ArrayList<>();

        if (response.contains("guests")) {
            actions.add("Confirm number of guests");
        }
        if (response.contains("date")) {
            actions.add("Change check-in date");
            actions.add("Change check-out date");
        }
        if (response.contains("price") || response.contains("budget")) {
            actions.add("Set price filter");
        }
        if (response.contains("location") || response.contains("city")) {
            actions.add("Change location");
        }

        if (actions.isEmpty()) {
            actions = List.of("Continue", "Search again");
        }

        return actions;
    }
}