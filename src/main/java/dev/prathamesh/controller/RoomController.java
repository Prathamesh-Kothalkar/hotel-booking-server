package dev.prathamesh.controller;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.RoomModel;
import dev.prathamesh.service.RoomService;
import dev.prathamesh.types.CreateRoomRequest;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController{
	
	@Autowired
	RoomService roomService;
	
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Rooms";
	}
	
	@PostMapping("")
	public RoomModel createRoom(@RequestBody CreateRoomRequest room) {
		return roomService.createRoom(room);
	}
	
	@GetMapping("/{id}")
	public RoomModel getRoomById(@PathVariable Long id) {
		return roomService.getRoomById(id);
	}
	
	@GetMapping("/search")
	public List<RoomModel> searchRooms(

	        @RequestParam(required = false)
	        String location,

	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate checkIn,

	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate checkOut,

	        @RequestParam(required = false)
	        Short guests
	) {

	    return roomService.getRoomsByQuery(
	            location,
	            checkIn,
	            checkOut,
	            guests
	    );
	}
	
}