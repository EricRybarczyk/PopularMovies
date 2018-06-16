package com.example.ericrybarczyk.popularmovies.model;

public class MovieReview {
    private final String id;
    private final String author;
    private final String content;

    public MovieReview(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

}
