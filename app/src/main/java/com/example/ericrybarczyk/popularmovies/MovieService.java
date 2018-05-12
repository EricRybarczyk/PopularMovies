package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieService {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_API_POPULAR = "popular";
    private static final String MOVIE_API_TOP = "top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String TAG = MovieService.class.getName();

    private URL movieServiceUrl;
    private String movieData;
    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }


    public MovieService(Context context) {

        String movieApiKey = getApiKey(context); // TODO - set the API Key value
        movieServiceUrl = buildUrl(movieApiKey);

    }


    protected List<String> getMovies() throws IOException {

        new MovieServiceQueryTask().execute(movieServiceUrl);


        ArrayList<String> movies = new ArrayList<>();

        //String movieJson = getMovieData(movieServiceUrl);




        movies.add("Star Wars");
        movies.add("Star Trek");
        movies.add("Space Balls");
        movies.add("The Matrix");
        movies.add("The Load Of The Rings");
        movies.add("Iron Man");
        movies.add("The Martian");
        movies.add("The Hunt For Red October");
        movies.add("Patriot Games");
        movies.add("Guardians Of The Galaxy");
        movies.add("Point Break");
        movies.add("When Harry Met Sally");
        movies.add("Goldeneye");
        movies.add("Deadpool");
        movies.add("Contact");
        movies.add("The Terminator");
        movies.add("Alien");
        movies.add("The Abyss");
        movies.add("Spider Man");
        movies.add("The Wizard Of Oz");

        return movies;
    }


    protected class MovieServiceQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            try {
                String result = getMovieData(urls[0]);
                return result;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            movieData = s;
        }
    }


    private URL buildUrl(String apiKey) {
        Uri uri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(MOVIE_API_POPULAR)
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

    private String getApiKey(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.apikey);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result;
        int c;

        try {
            c = inputStream.read();
            while (c != -1) {
                outputStream.write(c);
                c = inputStream.read();
            }
            inputStream.close();
            result = outputStream.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            result = "undefined";
        }

        return result;
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
            throw e;
        }
        finally {
            urlConnection.disconnect();
        }
    }

}

