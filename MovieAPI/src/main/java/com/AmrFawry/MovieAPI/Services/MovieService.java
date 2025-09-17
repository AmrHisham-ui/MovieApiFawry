package com.AmrFawry.MovieAPI.Services;

import com.AmrFawry.MovieAPI.DTO.MovieDTO;
import com.AmrFawry.MovieAPI.Provider.OmdbApiClient;
import com.AmrFawry.MovieAPI.Repositories.MovieRepository;
import com.AmrFawry.MovieAPI.entity.Movie;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final OmdbApiClient omdbApiClient;
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    @Autowired
    public MovieService(MovieRepository movieRepository, OmdbApiClient omdbApiClient) {
        this.movieRepository = movieRepository;
        this.omdbApiClient = omdbApiClient;
    }

    public List<MovieDTO> searchMoviesFromApi(String searchTerm) {
        return omdbApiClient.searchMovies(searchTerm);
    }
@Transactional
    public Movie addMovie(String imdbId) {
        // Check if movie already exists
    logger.info("Adding movie with IMDB ID: {}", imdbId);
        if (movieRepository.findByImdbId(imdbId).isPresent()) {
            throw new RuntimeException("Movie already exists in database");
        }

        // Get details from OMDB
        MovieDTO movieDTO = omdbApiClient.getMovieDetails(imdbId);

        // Save to database
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setImdbId(movieDTO.getImdbId());
        movie.setYear(movieDTO.getYear());
        movie.setDirector(movieDTO.getDirector());
        movie.setPlot(movieDTO.getPlot());
        movie.setPoster(movieDTO.getPoster());
    logger.info("Movie added successfully: {}", movie.getTitle());
        return movieRepository.save(movie);
    }
@Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> searchMovies(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    public void removeMovie(String imdbId) {
        Movie movie = movieRepository.findByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found with IMDbID: " + imdbId));
        movieRepository.delete(movie);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }
@Transactional
    public List<Movie> addMoviesBatch(List<String> imdbIds) {
        return imdbIds.stream()
                .map(this::addMovie)
                .toList();
    }
@Transactional
    public void deleteMoviesBatch(List<Long> ids) {
        movieRepository.deleteAllById(ids);
    }
}