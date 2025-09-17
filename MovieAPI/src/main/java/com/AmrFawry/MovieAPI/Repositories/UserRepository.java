package com.AmrFawry.MovieAPI.Repositories;

import com.AmrFawry.MovieAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by username - changed to return Optional
    Optional<User> findByUsername(String username);

    // Check if a user exists by username
    boolean existsByUsername(String username);

    // Find a user by email - changed to return Optional
    Optional<User> findByEmail(String email);



}

