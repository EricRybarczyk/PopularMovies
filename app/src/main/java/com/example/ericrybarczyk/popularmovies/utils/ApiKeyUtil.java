package com.example.ericrybarczyk.popularmovies.utils;

import android.content.res.Resources;
import android.util.Log;

import com.example.ericrybarczyk.popularmovies.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApiKeyUtil {

    private Resources resources;

    private static final String TAG = ApiKeyUtil.class.getSimpleName();

    public ApiKeyUtil(Resources resources) {
        this.resources = resources;
    }

    public String getApiKey() {
        InputStream inputStream = resources.openRawResource(R.raw.apikey);
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
}
