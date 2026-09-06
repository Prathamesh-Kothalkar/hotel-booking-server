package dev.prathamesh.controller;

import dev.prathamesh.expection.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.UserRepo;
import dev.prathamesh.security.JwtService;
import dev.prathamesh.types.AuthRequest;
import dev.prathamesh.types.AuthResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        UserModel user = new UserModel();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepo.save(user);

        String token = jwtService.generateToken(user.getUserId(), user.getEmail());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        UserModel user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new dev.prathamesh.expection.ResourceNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUserId(), user.getEmail());
        return new AuthResponse(token);
    }
}