package com.example.ericrybarczyk.popularmovies;

import android.net.Uri;
import android.util.Log;

import com.example.ericrybarczyk.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MovieService {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String QUERY_API_KEY = "api_key";
    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_MOVIE_ID = "id";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_POSTER = "poster_path";
    private static final String JSON_KEY_BACKDROP = "backdrop_path";
    private static final String JSON_KEY_OVERVIEW = "overview";
    private static final String JSON_KEY_RELEASE_DATE = "release_date";
    private static final String JSON_KEY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String JSON_KEY_USER_RATING = "vote_average";
    private static final String TAG = MovieService.class.getName();

    private String movieApiKey;


    public MovieService(String movieApiKey) {
        this.movieApiKey = movieApiKey;
    }


    protected List<Movie> getMovies(String sortPreference) {
        String rawMovieData = null;
        ArrayList<Movie> movies = new ArrayList<>();

        URL movieServiceUrl = buildUrl(movieApiKey, sortPreference);

        try {
            rawMovieData = getMovieData(movieServiceUrl);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        JSONObject fullMovieCollection;
        JSONArray movieResults;
        try {
            fullMovieCollection = new JSONObject(rawMovieData);
            movieResults = fullMovieCollection.getJSONArray(JSON_KEY_RESULTS);

            for (int i = 0; i < movieResults.length(); i++) {
                JSONObject jsonMovie = new JSONObject(movieResults.getString(i));
                int movieId = jsonMovie.getInt(JSON_KEY_MOVIE_ID);
                String title = jsonMovie.getString(JSON_KEY_TITLE);
                String posterPath = MOVIE_IMAGE_BASE_URL + jsonMovie.getString(JSON_KEY_POSTER);
                String backdropPath = jsonMovie.getString(JSON_KEY_BACKDROP);
                String overview = jsonMovie.getString(JSON_KEY_OVERVIEW);
                Date releaseDate = (new SimpleDateFormat(JSON_KEY_DATE_FORMAT, Locale.getDefault()))
                        .parse(jsonMovie.getString(JSON_KEY_RELEASE_DATE));
                double userRating = jsonMovie.getDouble(JSON_KEY_USER_RATING);

                Movie movie = new Movie(movieId, title, posterPath, backdropPath, overview, releaseDate, userRating);

                movies.add(movie);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return movies;
    }


    protected Movie getMovie(int movieId) {

        URL movieServiceUrl = buildUrl(movieApiKey, String.valueOf(movieId));

        try {
            String rawMovieData = getMovieData(movieServiceUrl);
            JSONObject jsonMovie = new JSONObject(rawMovieData);
            String title = jsonMovie.getString(JSON_KEY_TITLE);
            String posterPath = MOVIE_IMAGE_BASE_URL + jsonMovie.getString(JSON_KEY_POSTER);
            String backdropPath = jsonMovie.getString(JSON_KEY_BACKDROP);
            String overview = jsonMovie.getString(JSON_KEY_OVERVIEW);
            Date releaseDate = (new SimpleDateFormat(JSON_KEY_DATE_FORMAT, Locale.getDefault()))
                    .parse(jsonMovie.getString(JSON_KEY_RELEASE_DATE));
            double userRating = jsonMovie.getDouble(JSON_KEY_USER_RATING);

            return new Movie(movieId, title, posterPath, backdropPath, overview, releaseDate, userRating);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        // return a default movie object to represent error condition in a graceful way
        return getErrorMovie();
    }


    // TODO - review how I use this and make sure it is as I intended for error conditions
    public Movie getErrorMovie() {
        int id = -1;
        String title = "Unknown Movie";
        String img = "missing.jpg"; // TODO - figure out how to handle this in Picasso
        String backdrop = "missing.jpg";
        String overview = "The requested movie could not be located.";
        Date relDate = new Date();
        double userRating = 0.0;
        return new Movie(id, title, img, backdrop, overview, relDate, userRating);
    }


    private URL buildUrl(String apiKey, String apiPath) {

        Uri uri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(apiPath)
                .appendQueryParameter(QUERY_API_KEY, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }




    /*
        This method is directly derived from method getResponseFromHttpUrl(URL url) from
        ud851-Exercises-student/Lesson02-GitHub-Repo-Search/T02.04-Exercise-ConnectingToTheInternet/
            app/src/main/java/com/example/android/datafrominternet/utilities/NetworkUtils.java
    */
    private String getMovieData(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
            throw e; // TODO - make sure this is an acceptable way to rethrow the exception after logging it
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw e; // TODO - make sure this is an acceptable way to rethrow the exception after logging it
        }
        finally {
            urlConnection.disconnect();
        }
    }

}

