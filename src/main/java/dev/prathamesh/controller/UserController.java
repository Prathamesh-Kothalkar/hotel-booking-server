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

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(
            @PathVariable Long id) {

        UserModel user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingModel>> getAllBookings(
            @PathVariable Long id) {

        List<BookingModel> bookings = userService.getAllBookings(id);

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}/refunds")
    public ResponseEntity<List<RefundModel>> getAllRefunds(
            @PathVariable Long id) {

        List<RefundModel> refunds = userService.getAllRefunds(id);

        return ResponseEntity.ok(refunds);
    }

    @PostMapping
    public ResponseEntity<UserModel> create(
            @RequestBody UserModel user) {

        UserModel createdUser = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }
}