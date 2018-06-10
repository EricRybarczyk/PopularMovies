package com.example.ericrybarczyk.popularmovies.data;

import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_PATH_REMOTE = "image_path_remote";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_FAVORITE_DATE = "favorite_date";

    }

}
