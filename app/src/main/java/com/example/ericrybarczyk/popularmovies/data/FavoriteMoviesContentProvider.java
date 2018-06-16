package com.example.ericrybarczyk.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;

public class FavoriteMoviesContentProvider extends ContentProvider {

    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIE_FOR_ID = 101;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private static final String TAG = FavoriteMoviesContentProvider.class.getSimpleName();

    private FavoriteMoviesDbHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteMoviesDbHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String[] movieDataFields = new String[] {
                FavoriteMoviesContract.FavoriteMoviesEntry._ID,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_IMAGE_PATH_REMOTE,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE
        };
        int match = URI_MATCHER.match(uri);
        Cursor result;

        switch (match) {
            case FAVORITE_MOVIES:
                result = db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        movieDataFields,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_MOVIE_FOR_ID:
                // URI: content://<authority>/FAVORITE_MOVIES/#
                String id = uri.getPathSegments().get(1);
                String where = FavoriteMoviesContract.FavoriteMoviesEntry._ID + "=?";
                String[] args = new String[]{id};
                result = db.query(
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        movieDataFields,
                        where,
                        args,
                        null,
                        null,
                        null);
                break;
                default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        try {
            result.setNotificationUri(Objects.requireNonNull(this.getContext()).getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        Uri result;

        switch (match) {
            case FAVORITE_MOVIES:
                long id = db.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    result = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri.toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        try {
            Objects.requireNonNull(this.getContext()).getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int deleteCount;
        switch (match) {
            case FAVORITE_MOVIE_FOR_ID:
                // URI: content://<authority>/FAVORITE_MOVIES/#
                String id = uri.getPathSegments().get(1);
                String where = "_id=?";
                String[] args = new String[]{id};
                deleteCount = db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, where, args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_FOR_ID);
        return matcher;
    }
}
