package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_API_POPULAR = "popular";
    private static final String MOVIE_API_TOP = "top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String TAG = MovieService.class.getName();

    private final String movieApiKey;
    private URL movieServiceUrl;


    public MovieService(Context context) {

        movieApiKey = getApiKey(context); // TODO - set the API Key value

        movieServiceUrl = buildUrl();
    }

    private URL buildUrl() {
        Uri uri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(MOVIE_API_POPULAR)
                .appendQueryParameter(QUERY_API_KEY, movieApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    private String getApiKey(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.apikey);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result;
        int c;

        try {
            c = inputStream.read();
            while (c != -1) {
                outputStream.write(c);
                c = inputStream.read();
            }
            inputStream.close();
            result = outputStream.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            result = "undefined";
        }

        return result;
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
