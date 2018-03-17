package com.android.nanodegree.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class Movie {

    @SerializedName("id")
    private long mId;

    @SerializedName("vote_average")
    private double mVoteAverage;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("backdrop_path")
    private String mBackdropPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
    private String mReleaseDate;


}
