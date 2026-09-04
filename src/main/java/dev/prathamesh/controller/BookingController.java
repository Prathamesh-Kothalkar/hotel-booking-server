package dev.prathamesh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.BookingModel;
import dev.prathamesh.service.BookingService;
import dev.prathamesh.types.BookingRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/bookings")

public class BookingController{
	@Autowired
	BookingService bookingService;

    
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Booking";
	}
	
	@PostMapping()
	public BookingModel createBooking(@RequestBody BookingRequest bookingRequest) {
		return bookingService.bookRoom(bookingRequest);
	}
	
	@PostMapping("/{id}/cancel")
	public BookingModel cancelOrderByBooking(@PathVariable Long id) {
		return bookingService.cancelBookingId(id);
	}
	
	@GetMapping("/{id}")
	public BookingModel getBookingById(@PathVariable Long id) {
		return bookingService.getBookingById(id);
	}
	
}