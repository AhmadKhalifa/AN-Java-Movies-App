package com.android.nanodegree.moviesapp;

import android.app.Application;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class MoviesApp extends Application {

    private static MoviesApp sMoviesApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sMoviesApp = this;
    }

    public static MoviesApp getApplication() {
        return sMoviesApp;
    }

    public static String getStringRes(int stringResId) {
        return sMoviesApp.getString(stringResId);
    }
}
