package com.example.ericrybarczyk.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;
import com.example.ericrybarczyk.popularmovies.utils.NetworkChecker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static final int MOVIE_LOADER = 529;
    private static final String MOVIE_LIST_TYPE_KEY = "requestedMovieListType";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter movieAdapter;
    private String sortPreference;

    @BindView(R.id.rv_movies) protected RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getPreferences();

        if (!sortPreference.equals(getString(R.string.pref_sort_favorite_value))){
            if (!NetworkChecker.isNetworkConnected(this)) {
                Log.e(TAG, NetworkChecker.getNoNetworkLogMessage(this));
                NetworkChecker.getNoNetworkToastMessage(this).show();
                return;
            }
        }

        // set up the RecyclerView and layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, this, sortPreference);
        recyclerView.setAdapter(movieAdapter);

        loadMovieData(false);
    }

    private void getPreferences() {
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
            Log.d(TAG, getString(R.string.log_message_restart_loader));
        } else {
            loaderManager.initLoader(MOVIE_LOADER, listTypeBundle, this);
            Log.d(TAG, getString(R.string.log_message_init_loader));
        }

    }


    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieListAsyncTaskLoader(this, sortPreference);
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
        intentToStart.putExtra(MovieAppConstants.KEY_MOVIE_ID, movieId);
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
