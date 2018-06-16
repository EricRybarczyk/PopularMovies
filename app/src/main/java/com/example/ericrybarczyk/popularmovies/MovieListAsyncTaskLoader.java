package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.ericrybarczyk.popularmovies.data.DateConverter;
import com.example.ericrybarczyk.popularmovies.data.FavoriteMoviesContract;
import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

import java.util.ArrayList;
import java.util.List;

class MovieListAsyncTaskLoader extends AsyncTaskLoader<List<Movie>>{

    private List<Movie> cachedMovies;
    private final String apiKey;
    private final String sortPreference;
    private static final String TAG = MovieListAsyncTaskLoader.class.getSimpleName();

    MovieListAsyncTaskLoader(@NonNull Context context, String sortPreference) {
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
        List<Movie> data;
        if (sortPreference.equals(getContext().getResources().getString(R.string.pref_sort_favorite_value))) {
            Cursor cursor = null;
            Uri uri = (FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI);
            data = new ArrayList<>();

            try {
                cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                assert cursor != null;
                while (cursor.moveToNext()) {
                    data.add(
                        new Movie(
                            cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_IMAGE_PATH_REMOTE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)),
                            DateConverter.toDate((long)cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE))),
                            cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING))
                        )
                    );
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }

        } else {
            MovieService movieService = new MovieService(apiKey);
            data = movieService.getMovies(sortPreference);
        }
        return data;
    }

    @Override
    public void deliverResult(@Nullable List<Movie> data) {
        cachedMovies = data;
        super.deliverResult(data);
    }
}
