package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ericrybarczyk.popularmovies.model.MovieReview;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

import java.util.List;

public class MovieReviewListAsyncTaskLoader extends AsyncTaskLoader<List<MovieReview>> {


    private String apiKey;
    private List<MovieReview> cachedReviews;
    int movieId;

    public MovieReviewListAsyncTaskLoader(@NonNull Context context, int movieId) {
        super(context);
        apiKey = (new ApiKeyUtil(context.getResources())).getApiKey();
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (cachedReviews != null) {
            deliverResult(cachedReviews);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<MovieReview> loadInBackground() {
        MovieService movieService = new MovieService(apiKey);
        return movieService.getReviews(movieId);
    }

    @Override
    public void deliverResult(@Nullable List<MovieReview> data) {
        cachedReviews = data;
        super.deliverResult(data);
    }
}
