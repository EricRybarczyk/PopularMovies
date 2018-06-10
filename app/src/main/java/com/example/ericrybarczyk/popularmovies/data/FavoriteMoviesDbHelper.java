package com.example.ericrybarczyk.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ericrybarczyk.popularmovies.data.FavoriteMoviesContract.*;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritemovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITEMOVIES_TABLE = "CREATE TABLE " +
                FavoriteMoviesEntry.TABLE_NAME + " (" +
                FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_IMAGE_PATH_LOCAL + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_IMAGE_PATH_REMOTE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_RELEASE_DATE + " INT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_FAVORITE_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITEMOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple approach for this app, drop table on schema version change
        // a real app would need to migrate existing user data to the new schema version
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
