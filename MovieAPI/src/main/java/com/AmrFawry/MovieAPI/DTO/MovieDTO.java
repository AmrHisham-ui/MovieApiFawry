package com.AmrFawry.MovieAPI.DTO;

import lombok.*;

public class MovieDTO {
    private Long id;
    private String title;
    private String imdbId;
    private String year;
    private String director;
    private String plot;
    private String poster;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}