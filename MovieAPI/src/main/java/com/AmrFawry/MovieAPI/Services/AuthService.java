package com.AmrFawry.MovieAPI.Services;

import com.AmrFawry.MovieAPI.DTO.UserDTO;
import com.AmrFawry.MovieAPI.config.JwtUtil;
import com.AmrFawry.MovieAPI.entity.User;
import com.AmrFawry.MovieAPI.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> registerUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUsername() == null ||
                userDTO.getPassword() == null || userDTO.getRole() == null) {
            System.out.println("❌ Missing required fields!");
            return ResponseEntity.badRequest().body(createErrorResponse("Username, password, and role are required"));
        }

        if (userService.existsByUsername(userDTO.getUsername())) {
            System.out.println("❌ Username already exists: " + userDTO.getUsername());
            return ResponseEntity.badRequest().body(createErrorResponse("Username already exists"));
        }

        System.out.println("🔑 Encoding password...");
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(getEncoder().encode(userDTO.getPassword()));
        newUser.setRole(userDTO.getRole());
        newUser.setEmail(userDTO.getEmail());

        try {
            System.out.println("📝 Saving user to DB...");
            userService.saveUser(newUser);
            System.out.println("✅ User registered successfully!");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createSuccessResponse("User registered successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error during user registration: " + e.getMessage()));
        }
    }

    private BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    // Helper method to create error response
    private Object createErrorResponse(String message) {
        return new ApiResponse(false, message);
    }

    // Helper method to create success response
    private Object createSuccessResponse(String message) {
        return new ApiResponse(true, message);
    }

    public ResponseEntity login(UserDTO loginRequest) {
        try {
            // Validate the user credentials against the database
            User user = userService.findByUsername(loginRequest.getUsername());

            if (user == null) {
                throw new RuntimeException("Exception");
            }

            boolean isValidPassword = getEncoder().matches(loginRequest.getPassword(), user.getPassword());

            if (isValidPassword) {
                // Generate JWT token
                String token = jwtUtil.generateToken(user);

                // Return token in response
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("username", user.getUsername());
                response.put("role", user.getRole());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error during authentication: " + e.getMessage()));
        }
    }
}
