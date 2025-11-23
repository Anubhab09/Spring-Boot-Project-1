package com.anubhab09.user_service.service;

import com.anubhab09.user_service.config.jwt.JwtService;
import com.anubhab09.user_service.dto.AuthResponse;
import com.anubhab09.user_service.dto.LoginRequest;
import com.anubhab09.user_service.dto.RegisterRequest;
import com.anubhab09.user_service.model.Role;
import com.anubhab09.user_service.model.User;
import com.anubhab09.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request){
        if(repository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exusts!");
        }
        if(repository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already used");
        }
        Role role = (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN"))
                ? Role.ADMIN
                : Role.USER;

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        repository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    // Login user
    public AuthResponse login(LoginRequest request) {
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
