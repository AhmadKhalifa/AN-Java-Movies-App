package com.android.nanodegree.moviesapp.model;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public enum QueryType {

    MOST_POPULAR ("popular"),
    TOP_RATED ("top_rated"),
    FAVORITES("favorites");

    private final String mValue;

    QueryType(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }
}