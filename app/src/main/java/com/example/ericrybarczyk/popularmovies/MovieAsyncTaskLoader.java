package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<Movie> {

    private Movie cachedMovie;
    private String apiKey;
    private int movieId;

    public MovieAsyncTaskLoader(@NonNull Context context, int movieId) {
        super(context);
        apiKey = (new ApiKeyUtil(context.getResources())).getApiKey();
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (cachedMovie != null) {
            deliverResult(cachedMovie);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Movie loadInBackground() {
        MovieService movieService = new MovieService(apiKey);
        return movieService.getMovie(movieId);
    }

    @Override
    public void deliverResult(@Nullable Movie data) {
        cachedMovie = data;
        super.deliverResult(data);
    }
}
