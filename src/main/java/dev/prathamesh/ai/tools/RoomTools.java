package dev.prathamesh.ai.tools;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import dev.prathamesh.model.RoomModel;
import dev.prathamesh.service.RoomService;
import dev.prathamesh.types.RoomSearchRequest;
import dev.prathamesh.types.RoomSearchResponse;

@Component
public class RoomTools {

    private final RoomService roomService;

    public RoomTools(RoomService roomService) {
        this.roomService = roomService;
    }

    @Tool(description = "Search for available hotel rooms by location, dates, guests, and price range. " +
                         "Returns up to 5 matching rooms sorted by price.")
    public List<RoomSearchResponse> searchAvailableRooms(
            @ToolParam(description = "City or area to search in, e.g. Pune", required = false) String location,
            @ToolParam(description = "Check-in date in YYYY-MM-DD format", required = false) LocalDate checkIn,
            @ToolParam(description = "Check-out date in YYYY-MM-DD format", required = false) LocalDate checkOut,
            @ToolParam(description = "Number of guests", required = false) Short guests,
            @ToolParam(description = "Minimum price per night", required = false) BigDecimal minPrice,
            @ToolParam(description = "Maximum price per night", required = false) BigDecimal maxPrice) {

        RoomSearchRequest request = new RoomSearchRequest();
        request.setLocation(location);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setGuests(guests);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);

        Page<RoomModel> results = roomService.searchRooms(request, PageRequest.of(0, 5));

        return results.getContent().stream()
                .map(this::toResponse)
                .toList();
    }

    private RoomSearchResponse toResponse(RoomModel room) {
        RoomSearchResponse response = new RoomSearchResponse();
        response.setRoomId(room.getRoomId());
        response.setRoomType(room.getRoomType());
        response.setNoOfBeds(room.getNoOfBeds());
        response.setPrice(room.getPrice());
        response.setStatus(room.getStatus());
        response.setHotelName(room.getHotel().getName());
        response.setLocation(room.getHotel().getLocation());
        response.setRating(room.getHotel().getRating());
        response.setImages(room.getImages());
        return response;
    }
}