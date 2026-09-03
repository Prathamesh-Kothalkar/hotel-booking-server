package dev.prathamesh.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.HotelModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController{
	
	@Autowired
	HotelService hotelService;
	
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Hotels";
	}
	
	@GetMapping("")
	public List<HotelModel> getHotels(){
		return hotelService.getHotels();
	}
	
	
	@PostMapping("")
	public HotelModel create(@RequestBody HotelModel hotel) {
		return hotelService.createHotel(hotel);
	}
	
	@GetMapping("/{id}/rooms")
	public List<RoomModel> getRoomsByHotelId(@PathVariable Long id){
		return hotelService.getRoomsByHotelId(id);
	}
	
	
}