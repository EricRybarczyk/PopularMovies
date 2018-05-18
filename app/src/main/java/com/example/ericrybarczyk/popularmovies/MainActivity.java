package com.example.ericrybarczyk.popularmovies;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.utils.ApiKeyUtil;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    /// TODO - clean up unused constants

    private static final int MOVIE_LOADER = 529;
    private static final String INTENT_EXTRA_KEY_MOVIE_ID = "com.example.ericrybarczyk.popularmovies.movie_id"; // TODO - probably move this to resource file for access from multiple activities
    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_LIST_TYPE_KEY = "requestedMovieListType";
    private static final String MOVIE_API_POPULAR = "popular";
    private static final String MOVIE_API_TOP = "top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android.support.v4.app.LoaderManager
        //getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);


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


    private void loadMovieData(boolean refresh) {

        Bundle listTypeBundle = new Bundle();
        listTypeBundle.putString(MOVIE_LIST_TYPE_KEY, MOVIE_API_POPULAR);  //TODO - implement preference for which movie list to get - TOP or POPULAR

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

                return movieService.getMovies();
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


//    private String getApiKey() {
//        InputStream inputStream = getResources().openRawResource(R.raw.apikey);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        String result;
//        int c;
//
//        try {
//            c = inputStream.read();
//            while (c != -1) {
//                outputStream.write(c);
//                c = inputStream.read();
//            }
//            inputStream.close();
//            result = outputStream.toString();
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//            result = "undefined";
//        }
//
//        return result;
//    }

    @Override
    public void onClick(int movieId) {
        Class destination = DetailActivity.class;
        Intent intentToStart = new Intent(this, destination);
        intentToStart.putExtra(INTENT_EXTRA_KEY_MOVIE_ID, movieId);
        startActivity(intentToStart);
    }
}
