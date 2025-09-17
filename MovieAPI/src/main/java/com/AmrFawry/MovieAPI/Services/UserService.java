package com.AmrFawry.MovieAPI.Services;

import com.AmrFawry.MovieAPI.Repositories.UserRepository;
import com.AmrFawry.MovieAPI.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to save or register a new user
    public User saveUser(User user) {
        // Encode the password before saving
        return userRepository.save(user);
    }

    // Method to find a user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

//    // Method to validate user credentials
//    public boolean validateUser(String username, String rawPassword) {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Debugging output
//            System.out.println("✅ User found: " + username);
//            System.out.println("🔐 Stored password hash: " + user.getPassword());
//            System.out.println("🔑 Raw password entered: " + rawPassword);
//
//            // Check if password matches
//            boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
//            System.out.println("✅ Password match result: " + passwordMatches);
//
//            return passwordMatches;
//        } else {
//            System.out.println("❌ User not found: " + username);
//            return false;
//        }
//    }

    // Method to check if a username already exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}