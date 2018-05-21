package com.example.ericrybarczyk.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.ericrybarczyk.popularmovies.R;

public class NetworkChecker {

    // directly adapted from https://stackoverflow.com/a/4009133/798642 as recommended by Udacity for this project
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnected());
    }

    public static Toast getNoNetworkToastMessage(Context context) {
        return Toast.makeText(context, R.string.error_message_no_network, Toast.LENGTH_LONG);
    }

}
