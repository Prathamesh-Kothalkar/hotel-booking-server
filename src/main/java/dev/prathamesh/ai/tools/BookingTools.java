package dev.prathamesh.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import dev.prathamesh.model.BookingModel;
import dev.prathamesh.service.BookingService;

@Component
public class BookingTools {

    private final BookingService bookingService;

    public BookingTools(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Tool(description = "Get the current status and details of a hotel booking by its booking ID")
    public BookingStatusResult getBookingStatus(
            @ToolParam(description = "The booking ID to look up") Long bookingId) {

        BookingModel booking = bookingService.getBookingById(bookingId);

        return new BookingStatusResult(
                booking.getBookingId(),
                booking.getStatus().name(),
                booking.getCheckInDate().toString(),
                booking.getCheckOutDate().toString(),
                booking.getTotalAmount()
        );
    }

    public record BookingStatusResult(
            Long bookingId,
            String status,
            String checkInDate,
            String checkOutDate,
            java.math.BigDecimal totalAmount
    ) {}
}