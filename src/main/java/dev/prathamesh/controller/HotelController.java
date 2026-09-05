package dev.prathamesh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity.ok("Hello From Hotels");
    }

    @GetMapping
    public ResponseEntity<List<HotelModel>> getHotels() {

        List<HotelModel> hotels = hotelService.getHotels();

        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    public ResponseEntity<HotelModel> create(
            @RequestBody HotelModel hotel) {

        HotelModel createdHotel = hotelService.createHotel(hotel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomModel>> getRoomsByHotelId(
            @PathVariable Long id) {

        List<RoomModel> rooms = hotelService.getRoomsByHotelId(id);

        return ResponseEntity.ok(rooms);
    }
}