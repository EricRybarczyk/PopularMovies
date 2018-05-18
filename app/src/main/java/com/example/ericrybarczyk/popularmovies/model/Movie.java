package com.example.ericrybarczyk.popularmovies.model;

import java.util.Date;

// TODO - refactor to a separate 'models' package

public class Movie {

    private int id;
    private String title;
    private String imagePath;
    private String relativeBackdropPath;
    private String overview;
    private double userRating;
    private Date releaseDate;

    public Movie(int id, String title, String imagePath, String relativeBackdropPath, String overview, Date releaseDate, double userRating) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.relativeBackdropPath = relativeBackdropPath;
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

    public String getRelativeBackdropPath() {
        return relativeBackdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() { return userRating; }
}
