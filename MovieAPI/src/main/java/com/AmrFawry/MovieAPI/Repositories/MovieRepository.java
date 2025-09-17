package com.AmrFawry.MovieAPI.Repositories;

import com.AmrFawry.MovieAPI.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByImdbId(String imdbId);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
