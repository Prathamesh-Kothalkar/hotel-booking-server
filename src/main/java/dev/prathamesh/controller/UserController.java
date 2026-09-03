package dev.prathamesh.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.UserModel;
import dev.prathamesh.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController{
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Users";
	}
	
	@GetMapping("/{id}")
	public UserModel getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
	
	@PostMapping("")
	public UserModel create(@RequestBody UserModel user) {
		return userService.createUser(user);
	}
	
}