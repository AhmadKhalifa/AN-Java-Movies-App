package com.android.nanodegree.moviesapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.nanodegree.moviesapp.MoviesApp;

/**
 * Created by Khalifa on 3/24/2018.
 *
 */
public final class ConnectionChecker implements BaseConnectionChecker {

    @Override
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = ((ConnectivityManager) MoviesApp
                .getApplication()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo info = null;
        if (connectivityManager != null) {
            info = connectivityManager.getActiveNetworkInfo();
        }
        return info != null && info.isConnectedOrConnecting();
    }
}
