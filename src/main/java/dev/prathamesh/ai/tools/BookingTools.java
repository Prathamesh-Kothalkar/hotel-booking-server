package dev.prathamesh.ai.tools;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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
        this.userService=userService;
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
    
    @Tool(description = "Get the all details of a hotel booking by user id")
    public List<BookingStatusResult> getAllBooking(@ToolParam(description = "The User id for lookup") Long userId){
    	List<BookingModel> bookings=userService.getAllBookings(userId);
    	
    	return bookings.stream().map((booking)->new BookingStatusResult(
    			booking.getBookingId(),
                booking.getStatus().name(),
                booking.getCheckInDate().toString(),
                booking.getCheckOutDate().toString(),
                booking.getTotalAmount()
    			)).toList();
    }
    
    @Tool(description = "Book a hotel room for a user for the specified check-in date, check-out date, and number of guests")
    public BookingStatusResult createBooking(
    			@ToolParam(description = "The user id who want to book a room") Long userId,
    			@ToolParam(description = "The room id which need to book") Long roomId,
    			@ToolParam(description = "Check-in date in YYYY-MM-DD format") LocalDate checkIn,
    			@ToolParam(description = "Check-out date in YYYY-MM-DD format") LocalDate checkOut,
    			@ToolParam(description = "Number of guest,person,members") Short numGuest
    		) {
    	
    		BookingRequest req=new BookingRequest(userId,roomId,checkIn,checkOut,numGuest);
    		BookingModel booking=bookingService.bookRoom(req);
    		
    		return new BookingStatusResult(
                    booking.getBookingId(),
                    booking.getStatus().name(),
                    booking.getCheckInDate().toString(),
                    booking.getCheckOutDate().toString(),
                    booking.getTotalAmount()
            );
    }
    
}