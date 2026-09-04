package dev.prathamesh.service;


import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.prathamesh.model.HotelModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.repository.HotelRepo;
import dev.prathamesh.repository.RoomRepo;
import dev.prathamesh.specification.RoomSpecification;
import dev.prathamesh.types.CreateRoomRequest;
import dev.prathamesh.types.RoomSearchRequest;

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
	

    public Page<RoomModel> searchRooms(
            RoomSearchRequest request,
            Pageable pageable) {

        validate(request);

        Specification<RoomModel> specification =
                RoomSpecification.search(request);

        return roomRepo.findAll(
                specification,
                pageable
        );
    }
	
	
	private void validate(RoomSearchRequest request) {

        if (request.getCheckIn() != null &&
            request.getCheckOut() != null) {

            if (!request.getCheckIn()
                    .isBefore(request.getCheckOut())) {

                throw new IllegalArgumentException(
                    "Check-in date must be before check-out date"
                );
            }
        }

        if (request.getGuests() != null &&
            request.getGuests() < 1) {

            throw new IllegalArgumentException(
                "Guests must be at least 1"
            );
        }

        if (request.getMinPrice() != null &&
            request.getMaxPrice() != null) {

            if (request.getMinPrice()
                    .compareTo(request.getMaxPrice()) > 0) {

                throw new IllegalArgumentException(
                    "Minimum price cannot exceed maximum price"
                );
            }
        }
    }
	
	
}