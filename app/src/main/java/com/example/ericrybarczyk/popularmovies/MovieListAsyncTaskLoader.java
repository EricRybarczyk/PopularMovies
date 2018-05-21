package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

import java.util.List;

public class MovieListAsyncTaskLoader extends AsyncTaskLoader<List<Movie>>{

    private List<Movie> cachedMovies;
    private String apiKey;
    private String sortPreference;

    public MovieListAsyncTaskLoader(@NonNull Context context, String sortPreference) {
        super(context);
        apiKey = (new ApiKeyUtil(context.getResources())).getApiKey();
        this.sortPreference = sortPreference;
    }

    @Override
    protected void onStartLoading() {
        if (cachedMovies != null) {
            deliverResult(cachedMovies);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        MovieService movieService = new MovieService(apiKey);
        return movieService.getMovies(sortPreference);
    }

    @Override
    public void deliverResult(@Nullable List<Movie> data) {
        cachedMovies = data;
        super.deliverResult(data);
    }
}
