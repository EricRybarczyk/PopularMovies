package com.example.ericrybarczyk.popularmovies;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity  {

    // TODO - TMDb Attribution - https://www.themoviedb.org/faq/api - https://www.themoviedb.org/about/logos-attribution
    /*
    What are the attribution requirements?
        You shall use the TMDb logo to identify your use of the TMDb APIs. You shall place the following notice prominently on your application:
        "This product uses the TMDb API but is not endorsed or certified by TMDb." Any use of the TMDb logo in your application shall be
        less prominent than the logo or mark that primarily describes the application and your use of the TMDb logo
        shall not imply any endorsement by TMDb. When attributing TMDb, the attribution must be within your application's "About" or "Credits" type section.

        When using a TMDb logo, we require you to use one of our approved images. https://www.themoviedb.org/about/logos-attribution
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
