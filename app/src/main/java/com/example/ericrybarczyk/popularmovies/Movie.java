package com.example.ericrybarczyk.popularmovies;

import java.util.Date;

public class Movie {

    private int id;
    private String title;
    private String imagePath;
    private String relativeBackdropPath;
    private String overview;
    private Date releaseDate;

    public Movie(int id, String title, String imagePath, String relativeBackdropPath, String overview, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
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
}
