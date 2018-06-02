package com.example.ericrybarczyk.popularmovies.model;

import java.util.Date;
import java.util.List;

public class Movie {

    private final int id;
    private final String title;
    private final String imagePath;
    private final String overview;
    private final double userRating;
    private final Date releaseDate;
    private List<MovieReview> reviews;
    private List<MovieTrailer> trailers;

    public Movie(int id, String title, String imagePath, String overview, Date releaseDate, double userRating) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() { return userRating; }

    public List<MovieReview> getReviews() { return reviews; }

    public void setReviews(List<MovieReview> reviews) { this.reviews = reviews; }

    public List<MovieTrailer> getTrailers() { return trailers; }

    public void setTrailers(List<MovieTrailer> trailers) { this.trailers = trailers; }
}
