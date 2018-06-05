package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.model.MovieTrailer;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

import java.util.List;

public class MovieTrailerListAsyncTaskLoader extends AsyncTaskLoader<List<MovieTrailer>> {

    private String apiKey;
    private List<MovieTrailer> cachedTrailers;
    int movieId;

    public MovieTrailerListAsyncTaskLoader(@NonNull Context context, int movieId) {
        super(context);
        apiKey = (new ApiKeyUtil(context.getResources())).getApiKey();
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (cachedTrailers != null) {
            deliverResult(cachedTrailers);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<MovieTrailer> loadInBackground() {
        MovieService movieService = new MovieService(apiKey);
        return movieService.getTrailers(this.movieId);
    }

    @Override
    public void deliverResult(@Nullable List<MovieTrailer> data) {
        cachedTrailers = data;
        super.deliverResult(data);
    }
}
