package com.rishabh.ecommerce.controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishabh.ecommerce.dto.AuthResponse;
import com.rishabh.ecommerce.dto.UserRequest;
import com.rishabh.ecommerce.dto.UserResponse;
import com.rishabh.ecommerce.services.JwtService;
import com.rishabh.ecommerce.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        String jwtToken = jwtService.generateToken(userResponse.getUsername());
        AuthResponse authResponse = AuthResponse.builder()
                .jwtToken(jwtToken)
                .username(userResponse.getUsername())
                .expiresAt(LocalDateTime.now().plusHours(6))
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String jwtToken = jwtService.generateToken(request.getUsername());
        AuthResponse authResponse = AuthResponse.builder()
                .jwtToken(jwtToken)
                .username(request.getUsername())
                .expiresAt(LocalDateTime.now().plusHours(6))
                .build();
        return ResponseEntity.ok(authResponse);
    }
}
