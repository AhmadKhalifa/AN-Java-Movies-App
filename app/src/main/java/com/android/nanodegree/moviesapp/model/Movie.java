package com.android.nanodegree.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class Movie implements Parcelable {

    private static final String IMAGE_WIDTH = "w780";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/" + IMAGE_WIDTH + "/";

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

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel movieParcel) {
                    return new Movie(movieParcel);
                }

        @Override
        public Movie[] newArray(int size) {
                    return new Movie[size];
                }
    };

    public Movie() {

    }

    protected Movie(Parcel movieParcel) {
        mId = movieParcel.readLong();
        mVoteAverage = movieParcel.readDouble();
        mTitle = movieParcel.readString();
        mPosterPath = movieParcel.readString();
        mBackdropPath = movieParcel.readString();
        mOverview = movieParcel.readString();
        mReleaseDate = movieParcel.readString();
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public String getPosterPathUrl() {
        return IMAGE_BASE_URL + mPosterPath;
    }

    public String getBackdropPathUrl() {
        return IMAGE_BASE_URL + mBackdropPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    @Override
    public boolean equals(Object otherMovie) {
        return otherMovie instanceof Movie && ((Movie) otherMovie).mId == mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel movieParcel, int flags) {
        movieParcel.writeLong(mId);
        movieParcel.writeDouble(mVoteAverage);
        movieParcel.writeString(mTitle);
        movieParcel.writeString(mPosterPath);
        movieParcel.writeString(mBackdropPath);
        movieParcel.writeString(mOverview);
        movieParcel.writeString(mReleaseDate);
    }

    public static class MovieBuilder {

        private long mId;
        private double mVoteAverage;
        private String mTitle;
        private String mPosterPath;
        private String mBackdropPath;
        private String mOverview;
        private String mReleaseDate;

        public MovieBuilder id(long id) {
            mId = id;
            return this;
        }

        public MovieBuilder voteAverage(double voteAverage) {
            mVoteAverage = voteAverage;
            return this;
        }

        public MovieBuilder title(String title) {
            mTitle = title;
            return this;
        }

        public MovieBuilder posterTitle(String posterTitle) {
            mPosterPath = posterTitle;
            return this;
        }

        public MovieBuilder backdropPath(String backdropPath) {
            mBackdropPath = backdropPath;
            return this;
        }

        public MovieBuilder overview(String overview) {
            mOverview = overview;
            return this;
        }

        public MovieBuilder releaseDate(String releaseDate) {
            mReleaseDate = releaseDate;
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.mId = mId;
            movie.mVoteAverage = mVoteAverage;
            movie.mTitle = mTitle;
            movie.mPosterPath = mPosterPath;
            movie.mBackdropPath = mBackdropPath;
            movie.mOverview = mOverview;
            movie.mReleaseDate = mReleaseDate;
            return movie;
        }
    }

    public static class Trailer implements Parcelable {

        private static final String YOUTUBE_VIDEO_URL =
                "https://www.youtube.com/watch?v=%s";

        private static final String THUMBNAIL_IMAGE_URL =
                "https://img.youtube.com/vi/%s/mqdefault.jpg";

        @SerializedName("name")
        private String mName;

        @SerializedName("key")
        private String mKey;

        Trailer(Parcel in) {
            mName = in.readString();
            mKey = in.readString();
        }

        public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mName);
            parcel.writeString(mKey);
        }

        public String getName() {
            return mName;
        }

        public String getVideoUrl() {
            return String.format(YOUTUBE_VIDEO_URL, mKey);
        }

        public String getThumbnailImageUrl() {
            return String.format(THUMBNAIL_IMAGE_URL, mKey);
        }
    }

    public static class Review implements Parcelable {

        @SerializedName("author")
        private String mAuthor;

        @SerializedName("content")
        private String mContent;

        Review(Parcel in) {
            mAuthor = in.readString();
            mContent = in.readString();
        }

        public static final Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mAuthor);
            parcel.writeString(mContent);
        }

        public String getAuthor() {
            return mAuthor;
        }

        public String getContent() {
            return mContent;
        }
    }
}
