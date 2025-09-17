package com.AmrFawry.MovieAPI.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RatingDTO {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    public @NotNull(message = "Movie ID is required") Long getMovieId() {
        return movieId;
    }

    public void setMovieId(@NotNull(message = "Movie ID is required") Long movieId) {
        this.movieId = movieId;
    }

    public @NotNull(message = "Rating is required") @Min(value = 1, message = "Rating must be at least 1") @Max(value = 5, message = "Rating cannot be more than 5") Integer getRating() {
        return rating;
    }

    public void setRating(@NotNull(message = "Rating is required") @Min(value = 1, message = "Rating must be at least 1") @Max(value = 5, message = "Rating cannot be more than 5") Integer rating) {
        this.rating = rating;
    }
}
