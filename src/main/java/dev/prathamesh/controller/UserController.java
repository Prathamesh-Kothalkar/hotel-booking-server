package dev.prathamesh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.BookingModel;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.model.UserModel;
import dev.prathamesh.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity.ok("Hello From Users");
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserModel> getUserById(
           Authentication auth) {
    	Long userId = (Long) auth.getPrincipal();
        UserModel user = userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/my/bookings")
    public ResponseEntity<List<BookingModel>> getAllBookings(
Authentication auth) {
    	Long userId = (Long) auth.getPrincipal();
        List<BookingModel> bookings = userService.getAllBookings(userId);

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/my/refunds")
    public ResponseEntity<List<RefundModel>> getAllRefunds(
    		Authentication auth) {
    	Long userId = (Long) auth.getPrincipal();
        List<RefundModel> refunds = userService.getAllRefunds(userId);

        return ResponseEntity.ok(refunds);
    }

}