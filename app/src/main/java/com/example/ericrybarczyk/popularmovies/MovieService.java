package com.example.ericrybarczyk.popularmovies;

import android.net.Uri;
import android.util.Log;

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

public class MovieService {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String MOVIE_API_POPULAR = "popular";
    private static final String MOVIE_API_TOP = "top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String TAG = MovieService.class.getName();

    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_MOVIE_ID = "id";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_POSTER = "poster_path";
    private static final String JSON_KEY_BACKDROP = "backdrop_path";
    private static final String JSON_KEY_OVERVIEW = "overview";
    private static final String JSON_KEY_RELEASE_DATE = "release_date";
    private static final String JSON_KEY_DATE_FORMAT = "yyyy-MM-dd";

    private URL movieServiceUrl;


    public MovieService(String movieApiKey) {
        movieServiceUrl = buildUrl(movieApiKey);
    }


    protected List<Movie> getMovies() {
        String rawMovieData = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            rawMovieData = getMovieData(this.movieServiceUrl);
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

                Movie movie = new Movie(movieId, title, posterPath, backdropPath, overview, releaseDate);

                movies.add(movie);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


//        int id = 1977;
//        String title = "Star Wars";
//        String img = "image.jpg";
//        String backdrop = "backdrop.jpg";
//        String overview = "the best ever";
//        Date relDate = new Date();
//        Movie dummyMovie = new Movie(id, title, img, backdrop, overview, relDate);
//
//        movies.add(dummyMovie);

        return movies;
    }



// TODO - implement preference for which api path to call (popular, or top)
    private URL buildUrl(String apiKey) {
        Uri uri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(MOVIE_API_TOP)
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
            Log.e(TAG, e.getMessage());
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }


//new MovieServiceQueryTask().execute(movieServiceUrl);

//    protected class MovieServiceQueryTask extends AsyncTask<URL, Void, String> {
//        @Override
//        protected String doInBackground(URL... urls) {
//            try {
//                String result = getMovieData(urls[0]);
//                return result;
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            movieData = s;
//        }
//    }

}

