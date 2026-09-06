package dev.prathamesh.ai.tools;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.prathamesh.ai.dto.BookingStatusResult;
import dev.prathamesh.model.BookingModel;
import dev.prathamesh.service.BookingService;
import dev.prathamesh.service.UserService;
import dev.prathamesh.types.BookingRequest;

@Component
public class BookingTools {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingTools(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // Single source of truth for "who is actually asking" — never trust the LLM for this.
    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void assertOwnership(BookingModel booking, Long requestingUserId) {
        if (!booking.getUser().getUserId().equals(requestingUserId)) {
            throw new AccessDeniedException("You do not have permission to access this booking");
        }
    }

    private BookingStatusResult toResult(BookingModel booking) {
        return new BookingStatusResult(
                booking.getBookingId(),
                booking.getStatus().name(),
                booking.getCheckInDate().toString(),
                booking.getCheckOutDate().toString(),
                booking.getTotalAmount()
        );
    }

    @Tool(description = "Get the current status and details of a hotel booking by its booking ID. " +
                         "Only works for bookings belonging to the current user.")
    public BookingStatusResult getBookingStatus(
            @ToolParam(description = "The booking ID to look up") Long bookingId) {

        BookingModel booking = bookingService.getBookingById(bookingId);
        assertOwnership(booking, currentUserId());
        return toResult(booking);
    }

    @Tool(description = "Get all bookings for the current user")
    public List<BookingStatusResult> getAllBooking() {
        List<BookingModel> bookings = userService.getAllBookings(currentUserId());
        return bookings.stream().map(this::toResult).toList();
    }

    @Tool(description = "Book a hotel room for the current user for the specified check-in date, " +
                         "check-out date, and number of guests")
    public BookingStatusResult createBooking(
            @ToolParam(description = "The room id to book") Long roomId,
            @ToolParam(description = "Check-in date in YYYY-MM-DD format") LocalDate checkIn,
            @ToolParam(description = "Check-out date in YYYY-MM-DD format") LocalDate checkOut,
            @ToolParam(description = "Number of guests") Short numGuest) {

        BookingRequest req = new BookingRequest();
        req.setUserId(currentUserId());   // never from the LLM
        req.setRoomId(roomId);
        req.setCheckInDate(checkIn);
        req.setCheckOutDate(checkOut);
        req.setNumGuests(numGuest);

        BookingModel booking = bookingService.bookRoom(req);
        return toResult(booking);
    }

    @Tool(description = "Cancel a booking by its booking ID. Only works for bookings belonging to the current user.")
    public BookingStatusResult cancelBooking(
            @ToolParam(description = "The booking ID to cancel") Long id) {

        BookingModel booking = bookingService.cancelBookingId(id, currentUserId());
        return toResult(booking);
    }
}