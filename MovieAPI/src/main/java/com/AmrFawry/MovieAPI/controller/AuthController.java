package com.AmrFawry.MovieAPI.controller;

import com.AmrFawry.MovieAPI.DTO.UserDTO;
import com.AmrFawry.MovieAPI.Services.AuthService;
import com.AmrFawry.MovieAPI.Services.UserService;
import com.AmrFawry.MovieAPI.config.JwtUtil;
import com.AmrFawry.MovieAPI.entity.User;
import com.AmrFawry.MovieAPI.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        return authService.registerUser(userDTO);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginRequest) {
        return authService.login(loginRequest);
    }
}