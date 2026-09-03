package dev.prathamesh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.prathamesh.model.HotelModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.repository.HotelRepo;
import dev.prathamesh.repository.RoomRepo;

@Service
public class HotelService{
	private HotelRepo hotelRepo;
	private RoomRepo roomRepo;
	
	public HotelService(HotelRepo hotelRepo,RoomRepo roomRepo){
		this.hotelRepo=hotelRepo;
		this.roomRepo=roomRepo;
	}
	
	//create Hotel
	public HotelModel createHotel(HotelModel hotel) {
		return hotelRepo.save(hotel);
	}
	
	public List<HotelModel> getHotels(){
		return hotelRepo.findAll();
	}
	
	public HotelModel getHotelById(Long id) {
		return hotelRepo.findById(id).orElse(null);
	}
	
	public List<RoomModel> getRoomsByHotelId(Long hotelId) {

        // Optional but recommended:
        if (!hotelRepo.existsById(hotelId)) {
            throw new RuntimeException("Hotel not found with id: " + hotelId);
        }

        return roomRepo.findByHotel_hotel_id(hotelId);
    }
	
	
}