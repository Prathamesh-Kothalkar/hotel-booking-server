package dev.prathamesh.service;


import java.util.List;

import org.springframework.stereotype.Service;

import dev.prathamesh.expection.ResourceNotFoundException;
import dev.prathamesh.model.BookingModel;
import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.BookingRepo;
import dev.prathamesh.repository.UserRepo;


@Service
public class UserService{

	
    private final UserRepo userRepo;
	private final BookingRepo bookingRepo;
	
	UserService(UserRepo userRepo,BookingRepo bookingRepo){
        this.userRepo = userRepo;
        this.bookingRepo=bookingRepo;
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
}