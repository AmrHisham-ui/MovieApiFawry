package com.AmrFawry.MovieAPI.Repositories;

import com.AmrFawry.MovieAPI.entity.Movie;
import com.AmrFawry.MovieAPI.entity.Rating;
import com.AmrFawry.MovieAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndMovie(User user, Movie movie);

    // Query to calculate the average rating for a movie
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Optional<Double> findAverageRatingByMovieId(Long movieId);
}
