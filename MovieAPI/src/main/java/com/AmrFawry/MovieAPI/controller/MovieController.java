package com.AmrFawry.MovieAPI.controller;

import com.AmrFawry.MovieAPI.DTO.MovieDTO;
import com.AmrFawry.MovieAPI.DTO.RatingDTO;
import com.AmrFawry.MovieAPI.Provider.OmdbApiClient;
import com.AmrFawry.MovieAPI.Services.MovieService;
import com.AmrFawry.MovieAPI.Services.RatingService;
import com.AmrFawry.MovieAPI.entity.Movie;
import com.AmrFawry.MovieAPI.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final RatingService ratingService;
    private final OmdbApiClient omdbApiClient;
    @Autowired
    public MovieController(MovieService movieService, RatingService ratingService, OmdbApiClient omdbApiClient) {
        this.movieService = movieService;
        this.ratingService = ratingService;
        this.omdbApiClient = omdbApiClient;
    }

    /**
     * Admin or authenticated users can view all movies in the database (paginated).
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies.getContent());
    }

    /**
     * Admins can search movies by title (paginated).
     */
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Movie>> searchMovies(
            @RequestParam String title,
            Pageable pageable) {
        Page<Movie> movies = movieService.searchMovies(title, pageable);
        return ResponseEntity.ok(movies);
    }
        @GetMapping("/search/external")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<MovieDTO>> searchExternalMovies(@RequestParam String title) {
            List<MovieDTO> movies = omdbApiClient.searchMovies(title);
            return ResponseEntity.ok(movies);
        }

    /**
     * Admin can add a movie using IMDbID.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> addMovie(@RequestParam String imdbId) {
        Movie movie = movieService.addMovie(imdbId);
        return ResponseEntity.ok(movie);
    }

    /**
     * Admin can remove a movie by IMDbID.
     */
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeMovie(@RequestParam String imdbId) {
        movieService.removeMovie(imdbId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Admin can delete a movie by its ID.
     */
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Admins can add movies in batch using a list of IMDb IDs.
     */
    @PostMapping("/add/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Movie>> addMoviesBatch(@RequestBody List<String> imdbIds) {
        List<Movie> movies = movieService.addMoviesBatch(imdbIds);
        return ResponseEntity.ok(movies);
    }

    /**
     * Admins can delete multiple movies by their IDs.
     */
    @DeleteMapping("/remove/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMoviesBatch(@RequestBody List<Long> ids) {
        movieService.deleteMoviesBatch(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Regular users can view movie details by movie ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    /**
     * Regular users or admins can search for movies from the OMDB API.
     */
    @GetMapping("/search-api")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MovieDTO>> searchMoviesFromApi(@RequestParam String searchTerm) {
        List<MovieDTO> movies = movieService.searchMoviesFromApi(searchTerm);
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/addrating")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Rating> rateMovie(@RequestBody RatingDTO ratingDTO, Authentication authentication) {
        Rating rating = ratingService.addOrUpdateRating(authentication, ratingDTO);
        return ResponseEntity.ok(rating);
    }

    /**
     * Get the average rating of a movie.
     */
    @GetMapping("/average/{movieId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long movieId) {
        double averageRating = ratingService.getAverageRating(movieId);
        return ResponseEntity.ok(averageRating);
    }
}

