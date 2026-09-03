package dev.prathamesh.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.prathamesh.model.HotelModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.repository.HotelRepo;
import dev.prathamesh.repository.RoomRepo;
import dev.prathamesh.types.CreateRoomRequest;

@Service
public class RoomService{
	private final RoomRepo roomRepo;
	private final HotelRepo hotelRepo;
	
	public RoomService(RoomRepo roomRepo,HotelRepo hotelRepo) {
		this.roomRepo=roomRepo;
		this.hotelRepo=hotelRepo;
	}
	
	//create;
	public RoomModel createRoom(CreateRoomRequest request) {
		 HotelModel hotel = hotelRepo.findById(request.getHotelId())
	                .orElseThrow(() -> new RuntimeException("Hotel not found"));

	        RoomModel room = new RoomModel();

	        room.setHotel(hotel);
	        room.setRoomType(request.getRoomType());
	        room.setNoOfBeds(request.getNoOfBeds());
	        room.setPrice(request.getPrice());
	        room.setStatus(request.getStatus());
	        room.setImages(request.getImages());

	        return roomRepo.save(room);
	}
	
	public RoomModel getRoomById(Long id) {
		return roomRepo.findById(id).orElse(null);
	}
	
	public List<RoomModel> getRoomsByQuery(
	        String location,
	        LocalDate checkIn,
	        LocalDate checkOut,
	        Short guests) {

	    // Both checkIn and checkOut must be provided together, or neither
	    if ((checkIn == null) != (checkOut == null)) {
	        throw new IllegalArgumentException(
	                "Both checkIn and checkOut must be provided together"
	        );
	    }

	    if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
	        throw new IllegalArgumentException(
	                "Check-out date must be after check-in date"
	        );
	    }

	    if (guests != null && guests <= 0) {
	        throw new IllegalArgumentException(
	                "Guests must be greater than 0"
	        );
	    }

	    return roomRepo.searchAvailableRooms(
	            location,
	            checkIn,
	            checkOut,
	            guests
	    );
	}
}