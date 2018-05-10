package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private String movieApiKey;


    public MovieService(Context context) {
        Resources resources = context.getResources();
        XmlResourceParser parser = resources.getXml(R.xml.keys);

        // TODO - figure out how to do read the xml file

        parser.close();
    }

    public List<String> getMovies() {

        ArrayList<String> movies = new ArrayList<>();
        movies.add("Star Wars");
        movies.add("Star Trek");
        movies.add("Space Balls");
        movies.add("The Matrix");
        movies.add("The Load Of The Rings");
        movies.add("Iron Man");
        movies.add("The Martian");
        movies.add("The Hunt For Red October");
        movies.add("Patriot Games");
        movies.add("Guardians Of The Galaxy");
        movies.add("Point Break");
        movies.add("When Harry Met Sally");
        movies.add("Goldeneye");
        movies.add("Deadpool");
        movies.add("Contact");
        movies.add("The Terminator");
        movies.add("Alien");
        movies.add("The Abyss");
        movies.add("Spider Man");
        movies.add("The Wizard Of Oz");

        return movies;
    }
}
