package com.AmrFawry.MovieAPI.Provider;

import com.AmrFawry.MovieAPI.DTO.MovieDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class OmdbApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    public OmdbApiClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<MovieDTO> searchMovies(String searchTerm) {
        String url = apiUrl + "?apikey=" + apiKey + "&s=" + searchTerm;
        String response = restTemplate.getForObject(url, String.class);

        List<MovieDTO> movies = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode searchResults = root.get("Search");

            if (searchResults != null && searchResults.isArray()) {
                for (JsonNode result : searchResults) {
                    MovieDTO movie = new MovieDTO();
                    movie.setTitle(result.get("Title").asText());
                    movie.setImdbId(result.get("imdbID").asText());
                    movie.setYear(result.get("Year").asText());
                    movie.setPoster(result.get("Poster").asText());
                    movies.add(movie);
                }
            }
        } catch (Exception e) {
            // Handle exception
            System.out.println("Error parsing response: " + e.getMessage());
        }

        return movies;
    }

    public MovieDTO getMovieDetails(String imdbId) {
        String url = apiUrl + "?apikey=" + apiKey + "&i=" + imdbId;
        String response = restTemplate.getForObject(url, String.class);

        MovieDTO movie = new MovieDTO();

        try {
            JsonNode root = objectMapper.readTree(response);

            movie.setTitle(root.get("Title").asText());
            movie.setImdbId(root.get("imdbID").asText());
            movie.setYear(root.get("Year").asText());
            movie.setDirector(root.get("Director").asText());
            movie.setPlot(root.get("Plot").asText());
            movie.setPoster(root.get("Poster").asText());
        } catch (Exception e) {
            // Handle exception
            throw new RuntimeException("Error connecting to OMDB API", e);
        }

        return movie;
    }


}