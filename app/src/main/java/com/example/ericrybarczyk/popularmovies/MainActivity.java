package com.example.ericrybarczyk.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    MovieAdapter movieAdapter;
    MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up the movie service
        this.movieService = new MovieService(this);

        // set up the RecyclerView and layout
        // TODO - butterknife
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(movieService);
        recyclerView.setAdapter(movieAdapter);

    }
}
