package dev.prathamesh.service;


import java.util.List;

import org.springframework.stereotype.Service;

import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.UserRepo;


@Service
public class UserService{

	
    private final UserRepo userRepo;
	
	
	UserService(UserRepo userRepo){
        this.userRepo = userRepo;

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
}