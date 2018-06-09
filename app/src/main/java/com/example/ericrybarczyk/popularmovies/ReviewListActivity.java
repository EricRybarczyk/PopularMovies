package com.example.ericrybarczyk.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.MovieReview;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieReview>> {

    private static final int REVIEWS_LOADER = 5150;
    private static final String TAG = ReviewListActivity.class.getSimpleName();
    private int movieId;

    @BindView(R.id.movie_title_value) protected TextView movieTitle;
    @BindView(R.id.reviews_viewpager) protected ViewPager reviewPager;
    @BindView(R.id.page_indicator) protected TabLayout pageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        movieId = -1;
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(MovieAppConstants.KEY_MOVIE_ID)) {
            movieId = intentThatStartedThisActivity.getIntExtra(MovieAppConstants.KEY_MOVIE_ID, -1);
        }
        if (intentThatStartedThisActivity.hasExtra(MovieAppConstants.KEY_MOVIE_TITLE)) {
            movieTitle.setText(intentThatStartedThisActivity.getStringExtra(MovieAppConstants.KEY_MOVIE_TITLE));
        }

        loadReviews(movieId);
    }

    private void loadReviews(int movieId) {
        // TODO - do I need this bundle?  Do I need the same bundle thing in MainActivity equivalent to this method?
//        Bundle bundle = new Bundle();
//        bundle.putInt(MovieAppConstants.KEY_MOVIE_ID, movieId);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(REVIEWS_LOADER, null, this);
        Log.d(TAG, "loadMovieData - initLoader");

        // TODO ?? make this work like MainActivity.loadMovieDetail() - do I need the refresh thing to restartLoader() vs initLoader() ??
    }

    @NonNull
    @Override
    public Loader<List<MovieReview>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieReviewListAsyncTaskLoader(this, this.movieId); // TODO - eval for memory leak, should I use application context instead? context.getApplicationContext()
    }

    // CREDIT for how to use the TabLayout as a paging indicator: https://stackoverflow.com/a/38459310/798642
    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieReview>> loader, List<MovieReview> data) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, data); // TODO - eval for memory leak, should I use application context instead? context.getApplicationContext()
        reviewPager.setAdapter(reviewAdapter);
        pageIndicator.setupWithViewPager(reviewPager, true);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieReview>> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
}
