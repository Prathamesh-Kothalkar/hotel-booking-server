package dev.prathamesh.service;


import java.util.List;

import org.springframework.stereotype.Service;

import dev.prathamesh.expection.ResourceNotFoundException;
import dev.prathamesh.model.BookingModel;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.BookingRepo;
import dev.prathamesh.repository.RefundRepo;
import dev.prathamesh.repository.UserRepo;


@Service
public class UserService{

	
    private final UserRepo userRepo;
	private final BookingRepo bookingRepo;
	private final RefundRepo refundRepo;
	
	UserService(UserRepo userRepo,BookingRepo bookingRepo, RefundRepo refundRepo){
        this.userRepo = userRepo;
        this.bookingRepo=bookingRepo;
        this.refundRepo=refundRepo;
    }
	
	public UserModel createUser(UserModel u) {
		return userRepo.save(u);
	}
	
	public List<UserModel> getUsers(){
		return userRepo.findAll();
	}
	
	public UserModel getUserById(Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	public List<BookingModel> getAllBookings(Long id){
		if(userRepo.findById(id).orElse(null)==null) {
			throw new ResourceNotFoundException("User not Found with :- "+id);
		}
		return bookingRepo.findByUserId(id);
	}
	
	public List<RefundModel> getAllRefunds(Long id){
		if(!userRepo.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id :- "+id);
		}
		
		return refundRepo.findByUserId(id);
	}
}