package com.example.ericrybarczyk.popularmovies;

import java.util.Date;

public class Movie {

    private int id;
    private String title;
    private String relativeImagePath;
    private String relativeBackdropPath;
    private String overview;
    private Date releaseDate;

    public Movie(int id, String title, String relativeImagePath, String relativeBackdropPath, String overview, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.relativeImagePath = relativeImagePath;
        this.relativeBackdropPath = relativeBackdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRelativeImagePath() {
        return relativeImagePath;
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
}
