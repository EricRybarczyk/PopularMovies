package com.example.ericrybarczyk.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;
import com.example.ericrybarczyk.popularmovies.utils.NetworkChecker;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int MOVIE_LOADER = 5291;
    private static final String BUNDLE_KEY_MOVIE_ID = "movie_id";
    private static final String TAG = MovieService.class.getName();


    @BindView(R.id.movie_image) protected ImageView imageView;
    @BindView(R.id.rating_stars) protected RatingBar ratingBar;
    @BindView(R.id.release_date_value) protected TextView releaseDate;
    @BindView(R.id.movie_title_value) protected TextView movieTitle;
    @BindView(R.id.movie_overview_value) protected TextView movieOverview;
    @BindView(R.id.rating_description) protected TextView ratingDescription;
    @BindView(R.id.rating_label) protected TextView ratingLabel;
    @BindView(R.id.release_date_label) protected TextView releaseDateLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (!NetworkChecker.isNetworkConnected(this)) {
            Log.e(TAG, "No network available");
            NetworkChecker.getNoNetworkToastMessage(this).show();
            return;
        }


        Intent starter = getIntent();
        if (starter != null) {
            int movieId;
            if (starter.hasExtra(MovieAppConstants.INTENT_EXTRA_KEY_MOVIE_ID)) {
                movieId = starter.getIntExtra(MovieAppConstants.INTENT_EXTRA_KEY_MOVIE_ID, 0);
            } else {
                Log.e(TAG, "Missing expected data: " + MovieAppConstants.INTENT_EXTRA_KEY_MOVIE_ID);
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
            final int movieId = args.getInt(BUNDLE_KEY_MOVIE_ID);

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
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {

        // see if we got the error movie object back
        if (data.getId() == -1) {
            movieTitle.setText(R.string.error_movie_title);
            movieOverview.setText(R.string.error_movie_description);
            imageView.setVisibility(View.GONE);
            ratingLabel.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            releaseDateLabel.setVisibility(View.GONE);
            releaseDate.setVisibility(View.GONE);
            return;
        }

        ratingBar.setRating((float)(data.getUserRating() / 2));
        releaseDate.setText(new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(data.getReleaseDate()));
        movieTitle.setText(data.getTitle());
        movieOverview.setText(data.getOverview());
        String ratingString = (new DecimalFormat("0.##")).format( (data.getUserRating() / 2) );
        String ratingDescriptionText = getString(R.string.rating_description_text, ratingString);
        ratingDescription.setText(ratingDescriptionText); // N.n out of 5 stars

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int maxImageWidth = (displayMetrics.widthPixels / 2);
        imageView.setMaxWidth(maxImageWidth);

        Picasso.with(this)
                .load(data.getImagePath())
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.placeholder_movie_black_18dp)
                .into(imageView);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
}
