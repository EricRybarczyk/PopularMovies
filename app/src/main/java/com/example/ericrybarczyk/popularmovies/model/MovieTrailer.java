package com.example.ericrybarczyk.popularmovies.model;

public class MovieTrailer {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public MovieTrailer(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    public String getId() { return id; }

    public String getKey() { return key; }

    public String getName() { return name; }

    public String getSite() { return site; }

    public String getType() { return type; }
}
