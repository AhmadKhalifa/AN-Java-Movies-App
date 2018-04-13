package com.android.nanodegree.moviesapp.exception;

/**
 * Created by Khalifa on 24/03/2018.
 *
 */
public class NoInternetConnectionException extends Exception {

    public NoInternetConnectionException() {
        super("Error connecting to the internet.");
    }
}
