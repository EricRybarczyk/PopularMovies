package com.example.ericrybarczyk.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericrybarczyk.popularmovies.data.DateConverter;
import com.example.ericrybarczyk.popularmovies.data.FavoriteMoviesContract;
import com.example.ericrybarczyk.popularmovies.data.FavoriteMoviesDbHelper;
import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.FontManager;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;
import com.example.ericrybarczyk.popularmovies.utils.NetworkChecker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, View.OnClickListener {

    private static final int MOVIE_LOADER = 5291;
    private static final String TAG = MovieService.class.getName();
    private Movie loadedMovie;
    private boolean isFavorite;

    @BindView(R.id.movie_image) protected ImageView moviePosterImage;
    @BindView(R.id.rating_stars) protected RatingBar ratingBar;
    @BindView(R.id.release_date_value) protected TextView releaseDate;
    @BindView(R.id.movie_title_value) protected TextView movieTitle;
    @BindView(R.id.movie_overview_value) protected TextView movieOverview;
    @BindView(R.id.rating_description) protected TextView ratingDescription;
    @BindView(R.id.rating_label) protected TextView ratingLabel;
    @BindView(R.id.release_date_label) protected TextView releaseDateLabel;
    @BindView(R.id.trailers_text_icon) protected TextView trailersTextIcon;
    @BindView(R.id.trailers_text_value) protected TextView trailersLabel;
    @BindView(R.id.reviews_text_icon) protected TextView reviewsTextIcon;
    @BindView(R.id.reviews_text_value) protected TextView reviewsLabel;
    @BindView(R.id.favorite_text_icon) protected TextView favoriteTextIcon;
    @BindView(R.id.favorite_text_value) protected TextView favoriteLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent starter = getIntent();
        if (starter != null) {
            int movieId;
            if (starter.hasExtra(MovieAppConstants.KEY_MOVIE_ID)) {
                movieId = starter.getIntExtra(MovieAppConstants.KEY_MOVIE_ID, MovieAppConstants.ERROR_MOVIE_ID);
            } else {
                Log.e(TAG, getString(R.string.log_missing_expected_data) + MovieAppConstants.KEY_MOVIE_ID);
                movieId = MovieAppConstants.ERROR_MOVIE_ID;
            }

            this.isFavorite = movieIsFavorite(movieId);

            if (this.isFavorite) {
                loadFavoriteMovie(movieId);
            } else {
                if (!NetworkChecker.isNetworkConnected(this)) {
                    Log.e(TAG, NetworkChecker.getNoNetworkLogMessage(this));
                    NetworkChecker.getNoNetworkToastMessage(this).show();
                    return;
                }
                loadMovieDetail(movieId, false);
            }

            // trailers and reviews widget setup
            trailersTextIcon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));
            reviewsTextIcon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));

            // adjust appearance if current movie is marked as user favorite
            setFavoriteIndicator();

            setClickHandlers();
        }
    }


    private void setClickHandlers() {
        trailersTextIcon.setOnClickListener(this);
        trailersLabel.setOnClickListener(this);
        reviewsTextIcon.setOnClickListener(this);
        reviewsLabel.setOnClickListener(this);
        favoriteTextIcon.setOnClickListener(this);
        favoriteLabel.setOnClickListener(this);
    }

    private void loadMovieDetail(int movieId, boolean refresh) {
        Bundle bundle = new Bundle();
        bundle.putInt(MovieAppConstants.KEY_MOVIE_ID, movieId);

        LoaderManager loaderManager = getSupportLoaderManager();

        if (refresh) {
            loaderManager.restartLoader(MOVIE_LOADER, bundle, this);
            Log.d(TAG, getString(R.string.log_message_restart_loader));
        } else {
            loaderManager.initLoader(MOVIE_LOADER, bundle, this);
            Log.d(TAG, getString(R.string.log_message_init_loader));
        }
    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        int movieId = args.getInt(MovieAppConstants.KEY_MOVIE_ID);
        return new MovieAsyncTaskLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {

        loadedMovie = data; // set a reference for the click event handlers

        // see if we got the error movie object back
        if (data.getId() == MovieAppConstants.ERROR_MOVIE_ID) {
            setErrorMovieDisplay();
            return;
        }

        displayMovieData(data);
    }

    private void displayMovieData(Movie movie) {

        ratingBar.setRating((float)(movie.getUserRating() / 2));
        releaseDate.setText(new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(movie.getReleaseDate()));
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        String ratingString = (new DecimalFormat("0.##")).format( (movie.getUserRating() / 2) );
        String ratingDescriptionText = getString(R.string.rating_description_text, ratingString);
        ratingDescription.setText(ratingDescriptionText); // N.n out of 5 stars

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int maxImageWidth = (displayMetrics.widthPixels / 2);
        moviePosterImage.setMaxWidth(maxImageWidth);

        if (this.isFavorite) {
            // GET IMAGE FILE FROM FILE SYSTEM
            String filename = MovieAppConstants.LOCAL_POSTER_PREFIX + String.valueOf(loadedMovie.getId());
            File imageFile = new File(getFilesDir(), filename);
            Picasso.with(this)
                    .load(imageFile)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.placeholder_movie_black_18dp)
                    .into(moviePosterImage);
        } else {
            Picasso.with(this)
                    .load(movie.getImagePath())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.placeholder_movie_black_18dp)
                    .into(moviePosterImage);
        }

    }

    private void setErrorMovieDisplay() {
        movieTitle.setText(R.string.error_movie_title);
        movieOverview.setText(R.string.error_movie_description);
        moviePosterImage.setVisibility(View.GONE);
        ratingLabel.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
        releaseDateLabel.setVisibility(View.GONE);
        releaseDate.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }

    private void loadFavoriteMovie(int movieId) {
        Cursor cursor = null;
        Movie data = null;
        Uri uri = (FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI)
                .buildUpon().appendPath(String.valueOf(movieId)).build();
        try {
            cursor = this.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            if (cursor.moveToFirst()) {
                // create the movie from the database
                data = new Movie(
                        cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_IMAGE_PATH_REMOTE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)),
                        DateConverter.toDate((long)cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE))),
                        cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            setErrorMovieDisplay();
            Toast.makeText(this, R.string.error_favorite_movie_fail, Toast.LENGTH_LONG).show();
            return;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if (data != null) {
            this.loadedMovie = data;
            displayMovieData(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trailers_text_icon:
            case R.id.trailers_text_value:
                Class trailersActivity = TrailerListActivity.class;
                Intent trailersIntent = new Intent(this, trailersActivity);
                trailersIntent.putExtra(MovieAppConstants.KEY_MOVIE_ID, loadedMovie.getId());
                trailersIntent.putExtra(MovieAppConstants.KEY_MOVIE_TITLE, loadedMovie.getTitle());
                startActivity(trailersIntent);
                break;
            case R.id.reviews_text_icon:
            case R.id.reviews_text_value:
                Class reviewsActivity = ReviewListActivity.class;
                Intent reviewsIntent = new Intent(this, reviewsActivity);
                reviewsIntent.putExtra(MovieAppConstants.KEY_MOVIE_ID, loadedMovie.getId());
                reviewsIntent.putExtra(MovieAppConstants.KEY_MOVIE_TITLE, loadedMovie.getTitle());
                startActivity(reviewsIntent);
                break;
            case R.id.favorite_text_icon:
            case R.id.favorite_text_value:
                toggleFavorite();
                break;
        }
    }

    private void setFavoriteIndicator() {
        if (this.isFavorite) {
            favoriteTextIcon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_SOLID));
            favoriteTextIcon.setTextColor(ContextCompat.getColor(this, R.color.colorFavoriteIcon));
            favoriteLabel.setTextColor(ContextCompat.getColor(this, R.color.colorFavoriteLabel));
        } else {
            favoriteTextIcon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME_REGULAR));
            favoriteTextIcon.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            favoriteLabel.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private boolean movieIsFavorite(int movieId) {
        boolean isFavorite = false;
        int evalId;
        Cursor cursor = null;
        Uri uri = (FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI)
                .buildUpon()
                .appendPath(String.valueOf(movieId)).build();

        try {
            cursor = this.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            while (cursor.moveToNext()) {
                evalId = cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry._ID));
                if (evalId == movieId) {
                    isFavorite = true;
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return isFavorite;
    }

    private void toggleFavorite() {
        String filename = MovieAppConstants.LOCAL_POSTER_PREFIX + String.valueOf(loadedMovie.getId());
        if (this.isFavorite) {
            Uri uri = (FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI)
                    .buildUpon().appendPath(String.valueOf(this.loadedMovie.getId())).build();
            this.getContentResolver().delete(uri, null, null);
            File imageFile = new File(getFilesDir(), filename);
            boolean deleted = imageFile.delete();
            if (!deleted) {
                Log.e(TAG, getString(R.string.error_image_delete) + filename);
            }
        } else {
            Picasso.with(this)
                    .load(this.loadedMovie.getImagePath()) // expect image is cached
                    .into(getLocalImageTarget(filename));
            ContentValues values = new ContentValues();
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry._ID, this.loadedMovie.getId());
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, this.loadedMovie.getTitle());
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_IMAGE_PATH_REMOTE, this.loadedMovie.getImagePath());
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, this.loadedMovie.getOverview());
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING, this.loadedMovie.getUserRating());
            values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, DateConverter.toTimestamp(this.loadedMovie.getReleaseDate()));
            this.getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, values);
        }
        // state is saved, so flip the local indicator and update the display
        this.isFavorite = (!this.isFavorite);
        setFavoriteIndicator();
    }

    private Target getLocalImageTarget(final String filename) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(() -> {
                    try {
                        File imageFile = new File(getFilesDir(), filename);
                        if (imageFile.exists()) {
                            boolean successOverwrite = imageFile.createNewFile();
                            if (!successOverwrite) {
                                Log.e(TAG, getString(R.string.error_image_overwrite));
                            }
                        }
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, getString(R.string.error_image_save) + " - " +  filename + " - " + e.getMessage(), e);
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(TAG, getString(R.string.error_image_save));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // don't need to do anything here, just required by Picasso's Target interface
            }
        };
    }

}
