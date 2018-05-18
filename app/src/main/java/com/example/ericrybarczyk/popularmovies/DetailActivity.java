package com.example.ericrybarczyk.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int MOVIE_LOADER = 5291;
    private static final String INTENT_EXTRA_KEY_MOVIE_ID = "com.example.ericrybarczyk.popularmovies.movie_id"; // TODO - probably move this to resource file for access from multiple activities
    private static final String BUNDLE_KEY_MOVIE_ID = "movie_id";
    private static final String TAG = MovieService.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent starter = getIntent();
        if (starter != null) {
            int movieId;
            if (starter.hasExtra(INTENT_EXTRA_KEY_MOVIE_ID)) {
                movieId = starter.getIntExtra(INTENT_EXTRA_KEY_MOVIE_ID, 0);
            } else {
                Log.e(TAG, "Missing expected data: " + INTENT_EXTRA_KEY_MOVIE_ID);
                movieId = -1;
            }

            loadMovieDetail(movieId, false);
        }
    }

    private void loadMovieDetail(int movieId, boolean refresh) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_MOVIE_ID, movieId);

        LoaderManager loaderManager = getSupportLoaderManager();

        if (refresh) {
            loaderManager.restartLoader(MOVIE_LOADER, bundle, this);
            Log.d(TAG, "loadMovieDetail - restartLoader");
        } else {
            loaderManager.initLoader(MOVIE_LOADER, bundle, this);
            Log.d(TAG, "loadMovieDetail - initLoader");
        }

    }

    @SuppressLint("StaticFieldLeak") // TODO - figure out how to do this correctly instead of SuppressLint - solve the root problem
    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Movie>(this) {

            private Movie cachedMovie;
            int movieId = args.getInt(BUNDLE_KEY_MOVIE_ID);

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
                MovieService movieService = new MovieService(new ApiKeyUtil(getResources()).getApiKey());
                return movieService.getMovie(movieId);
            }

            @Override
            public void deliverResult(@Nullable Movie data) {
                cachedMovie = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

        // TODO: Butterknife
        TextView tempTextView = findViewById(R.id.movie_id_placeholder);
        ImageView imageView = findViewById(R.id.movie_image);

        tempTextView.setText(String.valueOf(data.getId())); // TODO - remove this later

        // TODO - improve Picasso use - use error() and placeholder()
        Picasso.with(this)
                .load(data.getImagePath())
                .into(imageView);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
}
