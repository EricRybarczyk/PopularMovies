package com.example.ericrybarczyk.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_KEY_MOVIE_ID = "com.example.ericrybarczyk.popularmovies.movie_id"; // TODO - probably move this to resource file for access from multiple activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tempTextView = findViewById(R.id.movie_id_tv);

        Intent starter = getIntent();
        if (starter != null) {
            if (starter.hasExtra(INTENT_EXTRA_KEY_MOVIE_ID)) {
                int movieId = starter.getIntExtra(INTENT_EXTRA_KEY_MOVIE_ID, 0);
                tempTextView.setText(String.valueOf(movieId));
            }
        }

    }
}
