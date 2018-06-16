package com.example.ericrybarczyk.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.ericrybarczyk.popularmovies.R;

public class NetworkChecker {

    /*  This method was directly adapted from https://stackoverflow.com/a/4009133/798642
        as recommended by Udacity for this project.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // I prefer the affirmative (is) method name prefix
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnected());
    }

    public static Toast getNoNetworkToastMessage(Context context) {
        return Toast.makeText(context, R.string.error_no_network, Toast.LENGTH_LONG);
    }

    public static String getNoNetworkLogMessage(Context context) {
        return context.getString(R.string.log_message_no_network);
    }

}
