package com.example.ericrybarczyk.popularmovies;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static final int MOVIE_LOADER = 529;
    private static final String MOVIE_LIST_TYPE_KEY = "requestedMovieListType";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private String sortPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPreferences();

        // set up the RecyclerView and layout
        // TODO - butterknife
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, this);
        recyclerView.setAdapter(movieAdapter);

        loadMovieData(false);
    }

    private void setPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortPreference = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular_value));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void loadMovieData(boolean refresh) {

        Bundle listTypeBundle = new Bundle();
        listTypeBundle.putString(MOVIE_LIST_TYPE_KEY, sortPreference);

        LoaderManager loaderManager = getSupportLoaderManager();

        if (refresh) {
            loaderManager.restartLoader(MOVIE_LOADER, listTypeBundle, this);
            Log.d(TAG, "loadMovieData - restartLoader");
        } else {
            loaderManager.initLoader(MOVIE_LOADER, listTypeBundle, this);
            Log.d(TAG, "loadMovieData - initLoader");
        }

    }


    @SuppressLint("StaticFieldLeak") // TODO - figure out how to do this correctly instead of SuppressLint - solve the root problem
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            private List<Movie> cachedMovies;

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

                MovieService movieService = new MovieService(new ApiKeyUtil(getResources()).getApiKey());

                return movieService.getMovies(sortPreference);
            }

            @Override
            public void deliverResult(@Nullable List<Movie> data) {
                cachedMovies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        movieAdapter.setMovieData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        // not doing anything here, just required by LoaderCallback<> interface
    }
    

    @Override
    public void onClick(int movieId) {
        Class destination = DetailActivity.class;
        Intent intentToStart = new Intent(this, destination);
        intentToStart.putExtra(MovieAppConstants.INTENT_EXTRA_KEY_MOVIE_ID, movieId);
        startActivity(intentToStart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            sortPreference = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular_value));
            loadMovieData(true);
        }
    }
}
