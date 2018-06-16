package com.example.ericrybarczyk.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.MovieReview;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;
import com.example.ericrybarczyk.popularmovies.utils.NetworkChecker;

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

        // trailers require network connection
        if (!NetworkChecker.isNetworkConnected(this)) {
            Log.e(TAG, NetworkChecker.getNoNetworkLogMessage(this));
            NetworkChecker.getNoNetworkToastMessage(this).show();
            return;
        }

        loadReviews();
    }

    private void loadReviews() {
        getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, this);
        Log.d(TAG, getString(R.string.log_message_init_loader));
    }

    @NonNull
    @Override
    public Loader<List<MovieReview>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieReviewListAsyncTaskLoader(this, this.movieId);
    }

    // CREDIT for how to use the TabLayout as a paging indicator: https://stackoverflow.com/a/38459310/798642
    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieReview>> loader, List<MovieReview> data) {
        if (data.isEmpty()) {
            data.add(new MovieReview(
                    MovieAppConstants.NO_REVIEW_ID,
                    MovieAppConstants.NO_REVIEW_AUTHOR,
                    getString(R.string.error_movie_no_reviews_message)));
        }
        ReviewAdapter reviewAdapter = new ReviewAdapter(data);
        reviewPager.setAdapter(reviewAdapter);
        if (data.size() > 1) {
            pageIndicator.setupWithViewPager(reviewPager, true);
        } else {
            pageIndicator.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieReview>> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
}
