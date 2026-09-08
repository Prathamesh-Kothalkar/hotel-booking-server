package dev.prathamesh.ai.tools;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.prathamesh.ai.dto.BookingStatusResult;
import dev.prathamesh.ai.guardrail.PendingConfirmationStore;
import dev.prathamesh.model.BookingModel;
import dev.prathamesh.service.BookingService;
import dev.prathamesh.service.UserService;
import dev.prathamesh.types.BookingRequest;
import dev.prathamesh.types.BookingStatus;

@Component
public class BookingTools {

    private final BookingService bookingService;
    private final UserService userService;
    private final PendingConfirmationStore confirmationStore;

    public BookingTools(BookingService bookingService, UserService userService, PendingConfirmationStore confirmationStore) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.confirmationStore=confirmationStore;
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
    	
        BookingModel booking = bookingService.getBookingById(bookingId,currentUserId());
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

    @Tool(description = "Start the process of cancelling a booking. Shows the booking details and " +
            "returns a confirmation reference. Does NOT actually cancel anything yet — " +
            "you must call confirmCancelBooking after the user explicitly confirms.")
		public String requestCancelBooking(
		@ToolParam(description = "The booking ID to cancel") Long bookingId) {
		
			BookingModel booking = bookingService.getBookingById(bookingId,currentUserId());
			assertOwnership(booking, currentUserId());
			
			if (booking.getStatus() == BookingStatus.CANCELLED) {
			return "This booking is already cancelled.";
			}
			
			String token = confirmationStore.create(currentUserId(), bookingId);
			
			return String.format(
			"Booking #%d: %s to %s, total ₹%s. Ask the user to confirm cancellation. " +
			"If they say yes, call confirmCancelBooking with confirmationToken=%s",
			booking.getBookingId(), booking.getCheckInDate(), booking.getCheckOutDate(),
			booking.getTotalAmount(), token);
		}
		
		@Tool(description = "Actually cancels a booking. Only call this after the user has explicitly " +
		            "confirmed, using the confirmationToken from a prior requestCancelBooking call.")
		public BookingStatusResult confirmCancelBooking(
		@ToolParam(description = "The confirmation token from requestCancelBooking") String confirmationToken) {
		
			Long bookingId = confirmationStore.consume(confirmationToken, currentUserId());
			BookingModel booking = bookingService.cancelBookingId(bookingId, currentUserId());
			return toResult(booking);
		}
}
	