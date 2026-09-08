package dev.prathamesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import dev.prathamesh.model.BookingModel;
import dev.prathamesh.service.BookingService;
import dev.prathamesh.types.BookingRequest;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity
                .ok("Hello From Booking");
    }

    @PostMapping
    public ResponseEntity<BookingModel> createBooking(
            @RequestBody BookingRequest bookingRequest,Authentication auth) {
    	Long userId = (Long) auth.getPrincipal();
    	bookingRequest.setUserId(userId);
        BookingModel booking = bookingService.bookRoom(bookingRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(booking);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingModel> cancelBooking(
            @PathVariable Long id,Authentication auth) {
    	Long userId = (Long) auth.getPrincipal();
        BookingModel booking = bookingService.cancelBookingId(id,userId);

        return ResponseEntity
                .ok(booking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingModel> getBookingById(
            @PathVariable Long id,Authentication auth) {
    	Long userId=(Long) auth.getPrincipal();
        BookingModel booking = bookingService.getBookingById(id,userId);

        return ResponseEntity
                .ok(booking);
    }
}