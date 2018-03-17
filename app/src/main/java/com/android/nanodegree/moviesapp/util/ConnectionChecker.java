package com.android.nanodegree.moviesapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.nanodegree.moviesapp.MoviesApp;

/**
 * Created by Khalifa on 3/17/2018.
 * This class is important for checking the internet connectivity before invoking any network-
 * related calls.
 */
public final class ConnectionChecker {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConnectionChecker() {}

    /**
     * This method checks the internet connection and returns true if the device is connected
     * to the internet, false otherwise.
     * @return true if device is connected to the internet, false otherwise.
     */
    public static boolean isNetworkAvailable(){
        NetworkInfo info = ((ConnectivityManager) MoviesApp.getApplication().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
