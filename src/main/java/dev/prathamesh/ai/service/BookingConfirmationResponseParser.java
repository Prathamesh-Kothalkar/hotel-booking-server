package dev.prathamesh.ai.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import dev.prathamesh.ai.dto.EnhancedChatResponse;
import dev.prathamesh.ai.dto.EnhancedChatResponse.BookingConfirmation;

/**
 * Parser for booking confirmation responses
 */
@Component
public class BookingConfirmationResponseParser implements ResponseParser {

    private static final int PRIORITY = 110; // Highest priority

    @Override
    public boolean canHandle(String response) {
        String lower = response.toLowerCase();
        return lower.contains("booking has been successfully confirmed")
            || lower.contains("booking id")
            || (lower.contains("confirmed") && lower.contains("check-in"));
    }

    @Override
    public EnhancedChatResponse parse(String response) {
        EnhancedChatResponse result = new EnhancedChatResponse(response);
        result.setResponseType("BOOKING_CONFIRMATION");
        result.setBookingConfirmation(parseBookingConfirmation(response));
        result.setSuggestedActions(List.of(
            "View all bookings",
            "Search again",
            "Download receipt"
        ));
        return result;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * Parse booking confirmation details from response text
     * Matches patterns like: Booking ID: 17, Hotel: X, Check-in: Y, Total: ₹Z
     */
    private BookingConfirmation parseBookingConfirmation(String response) {
        BookingConfirmation confirmation = new BookingConfirmation();

        // Extract Booking ID
        Long bookingId = extractLong(response, "Booking ID[:\\s]*([\\d]+)");
        if (bookingId != null) {
            confirmation.setBookingId(bookingId);
        }

        // Extract Hotel Name
        String hotel = extractString(response, "Hotel[:\\s]*([^\\n,-]+)");
        if (hotel != null) {
            confirmation.setHotel(hotel);
        }

        // Extract Room Type
        String roomType = extractString(response, "Room[:\\s]*(?:ID[:\\s]*[\\d]+)?[^\\n]*?([A-Za-z\\s]+(?:Deluxe|Standard|Premium))");
        if (roomType != null) {
            confirmation.setRoomType(roomType);
        }

        // Extract Room ID
        Integer roomId = extractInteger(response, "Room ID[:\\s]*([\\d]+)");
        if (roomId != null) {
            confirmation.setRoomId(roomId);
        }

        // Extract Check-in Date
        String checkIn = extractString(response, "Check-in[:\\s]*([^\\n,-]+)");
        if (checkIn != null) {
            confirmation.setCheckInDate(checkIn);
        }

        // Extract Check-out Date
        String checkOut = extractString(response, "Check-out[:\\s]*([^\\n,-]+)");
        if (checkOut != null) {
            confirmation.setCheckOutDate(checkOut);
        }

        // Extract Number of Guests
        Integer guests = extractInteger(response, "Guests[:\\s]*([\\d]+)");
        if (guests != null) {
            confirmation.setNumGuests(guests);
        }

        // Extract Total Amount (handles both ₹3,000 and 3000 formats)
        Double amount = extractDouble(response, "(?:Total Amount|Total|Amount)[:\\s]*₹?([\\d,]+)");
        if (amount != null) {
            confirmation.setTotalAmount(amount);
        }

        // Extract Status
        String status = extractString(response, "Status[:\\s]*([A-Z]+)");
        if (status != null) {
            confirmation.setStatus(status);
        } else {
            confirmation.setStatus("CONFIRMED");
        }

        return confirmation;
    }

    // ========== Helper Methods ==========

    private Long extractLong(String text, String pattern) {
        try {
            String value = extractString(text, pattern);
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer extractInteger(String text, String pattern) {
        try {
            String value = extractString(text, pattern);
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double extractDouble(String text, String pattern) {
        try {
            String value = extractString(text, pattern);
            return value != null ? Double.parseDouble(value.replaceAll(",", "")) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractString(String text, String pattern) {
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            if (m.find()) {
                return m.group(1).trim();
            }
        } catch (Exception e) {
            // Silently fail and return null
        }
        return null;
    }
}