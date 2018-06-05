package com.example.ericrybarczyk.popularmovies;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.example.ericrybarczyk.popularmovies.model.MovieReview;
import com.example.ericrybarczyk.popularmovies.model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
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

class MovieService {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String QUERY_API_KEY = "api_key";
    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_MOVIE_ID = "id";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_POSTER = "poster_path";
    private static final String JSON_KEY_OVERVIEW = "overview";
    private static final String JSON_KEY_RELEASE_DATE = "release_date";
    private static final String JSON_KEY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String JSON_KEY_USER_RATING = "vote_average";

    private static final String API_PATH_REVIEWS_SUFFIX = "reviews";
    private static final String JSON_KEY_REVIEWS_RESULTS = "results";
    private static final String JSON_KEY_REVIEWS_ID = "id";
    private static final String JSON_KEY_REVIEWS_AUTHOR = "author";
    private static final String JSON_KEY_REVIEWS_CONTENT = "content";

    private static final String API_PATH_TRAILERS_SUFFIX = "videos";
    private static final String JSON_KEY_TRAILERS_RESULTS = "results";
    private static final String JSON_KEY_TRAILERS_ID = "id";
    private static final String JSON_KEY_TRAILERS_KEY = "key";
    private static final String JSON_KEY_TRAILERS_NAME = "name";
    private static final String JSON_KEY_TRAILERS_SITE = "site";
    private static final String JSON_KEY_TRAILERS_TYPE = "type";

    private static final String TAG = MovieService.class.getName();

    private final String movieApiKey;


    public MovieService(String movieApiKey) {
        this.movieApiKey = movieApiKey;
    }


    List<Movie> getMovies(String sortPreference) {
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
                String overview = jsonMovie.getString(JSON_KEY_OVERVIEW);
                Date releaseDate = (new SimpleDateFormat(JSON_KEY_DATE_FORMAT, Locale.getDefault()))
                        .parse(jsonMovie.getString(JSON_KEY_RELEASE_DATE));
                double userRating = jsonMovie.getDouble(JSON_KEY_USER_RATING);

                Movie movie = new Movie(movieId, title, posterPath, overview, releaseDate, userRating);

                movies.add(movie);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return movies;
    }

    // gets a specific movie including reviews and trailers
    Movie getMovie(int movieId) {

        URL movieServiceUrl = buildUrl(movieApiKey, String.valueOf(movieId));

        try {
            JSONObject jsonMovie = new JSONObject(getMovieData(movieServiceUrl));
            String title = jsonMovie.getString(JSON_KEY_TITLE);
            String posterPath = MOVIE_IMAGE_BASE_URL + jsonMovie.getString(JSON_KEY_POSTER);
            String overview = jsonMovie.getString(JSON_KEY_OVERVIEW);
            Date releaseDate = (new SimpleDateFormat(JSON_KEY_DATE_FORMAT, Locale.getDefault()))
                    .parse(jsonMovie.getString(JSON_KEY_RELEASE_DATE));
            double userRating = jsonMovie.getDouble(JSON_KEY_USER_RATING);

            return new Movie(movieId, title, posterPath, overview, releaseDate, userRating);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        // return a default movie object to represent error condition in a graceful way
        return getErrorMovie();
    }

    // get trailers for the movie
    public List<MovieTrailer> getTrailers(int movieId) {

        List<MovieTrailer> trailers = new ArrayList<>();
        URL trailersUrl = buildMoreDetailsUrl(movieApiKey, API_PATH_TRAILERS_SUFFIX, movieId);

        try {
            JSONObject jsonTrailersResult = new JSONObject(getMovieData(trailersUrl));
            JSONArray jsonTrailers = jsonTrailersResult.getJSONArray(JSON_KEY_TRAILERS_RESULTS);
            for (int i = 0; i < jsonTrailers.length(); i++) {
                JSONObject jsonTrailer = new JSONObject(jsonTrailers.getString(i));
                String id = jsonTrailer.getString(JSON_KEY_TRAILERS_ID);
                String key = jsonTrailer.getString(JSON_KEY_TRAILERS_KEY);
                String name = jsonTrailer.getString(JSON_KEY_TRAILERS_NAME);
                String site = jsonTrailer.getString(JSON_KEY_TRAILERS_SITE);
                String type = jsonTrailer.getString(JSON_KEY_TRAILERS_TYPE);
                trailers.add(new MovieTrailer(id, key, name, site, type));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // return collection will be empty which is acceptable for an error condition
        }
        return trailers;
    }

    // get reviews for the movie
    public List<MovieReview> getReviews(int movieId) {

        List<MovieReview> reviews = new ArrayList<>();
        URL reviewsUrl = buildMoreDetailsUrl(movieApiKey, API_PATH_REVIEWS_SUFFIX, movieId);
        try {
            JSONObject jsonReviewsResult = new JSONObject(getMovieData(reviewsUrl));
            JSONArray jsonReviews = jsonReviewsResult.getJSONArray(JSON_KEY_REVIEWS_RESULTS);
            for (int i = 0; i < jsonReviews.length(); i++) {
                JSONObject jsonReview = new JSONObject(jsonReviews.getString(i));
                String id = jsonReview.getString(JSON_KEY_REVIEWS_ID);
                String author = jsonReview.getString(JSON_KEY_REVIEWS_AUTHOR);
                String content = jsonReview.getString(JSON_KEY_REVIEWS_CONTENT);
                reviews.add(new MovieReview(id, author, content));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // return collection will be empty which is acceptable for an error condition
        }
        return reviews;
    }


    // this method works for the main movie listing calls, and also for the main call for a single movie
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

    // this methos works for the calls to get trailers and reviews for a specific movie
    private URL buildMoreDetailsUrl(String apiKey, String apiPath, int movieId) {

        Uri uri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
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
            throw e;
        }
        catch (Exception e) {
            Log.e(TAG, e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
        finally {
            urlConnection.disconnect();
        }
    }


    private Movie getErrorMovie() {
        int id = -1;
        String title = Resources.getSystem().getString(R.string.error_movie_title);
        String img = Resources.getSystem().getString(R.string.error_movie_poster);
        String overview = Resources.getSystem().getString(R.string.error_movie_description);
        Date relDate = new Date();
        double userRating = 0.0;
        return new Movie(id, title, img, overview, relDate, userRating);
    }

}

