package com.android.nanodegree.moviesapp.data.response;

import com.android.nanodegree.moviesapp.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class MoviesResponse {

    @SerializedName("results")
    private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }
}
