package com.android.nanodegree.moviesapp.data.response;

import com.android.nanodegree.moviesapp.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
public class TrailersResponse {

    @SerializedName("results")
    private List<Movie.Trailer> mTrailers;

    public List<Movie.Trailer> getTrailers() {
        return mTrailers;
    }
}
