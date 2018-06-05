package com.example.ericrybarczyk.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.MovieTrailer;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieTrailer>> {

    private static final int TRAILER_LOADER = 8675309;
    private static final String TAG = TrailerListActivity.class.getSimpleName();
    int movieId;
    @BindView(R.id.temp_test_text) protected TextView tempTextView;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_list);
        ButterKnife.bind(this);
        movieId = -1;

        // TODO - remove this temp stuff
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(MovieAppConstants.KEY_MOVIE_ID)) {
            movieId = intentThatStartedThisActivity.getIntExtra(MovieAppConstants.KEY_MOVIE_ID, -1);
        }
        try {
            tempTextView.setText(String.valueOf(movieId));
        } catch (Exception e) {
            Log.e(TrailerListActivity.class.getSimpleName(), e.getMessage());
        } // TODO - remove above temp stuff
        

    }

    private void loadTrailers(int movieId) {
        // TODO - do I need this bundle?  Do I need the same bundle thing in MainActivity equivalent to this method?
        Bundle bundle = new Bundle();
        bundle.putInt(MovieAppConstants.KEY_MOVIE_ID, movieId);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(TRAILER_LOADER, null, this);
        Log.d(TAG, "loadMovieData - initLoader");

        // TODO ?? make this work like MainActivity.loadMovieDetail() - do I need the refresh thing to restartLoader() vs initLoader() ??
    }

    @NonNull
    @Override
    public Loader<List<MovieTrailer>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieTrailerListAsyncTaskLoader(this, this.movieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieTrailer>> loader, List<MovieTrailer> data) {
        // TODO - need an adapter to bind the result data
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieTrailer>> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
}
