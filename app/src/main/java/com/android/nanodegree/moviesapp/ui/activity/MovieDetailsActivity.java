package com.android.nanodegree.moviesapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.ui.fragment.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity
        implements MovieDetailsFragment.OnFragmentInteractionListener {

    private static final String KEY_MOVIE = "key_movie";

    private MovieDetailsFragment mMovieDetailsFragment;

    private Movie mMovie;

    public static void startActivity(Context context, Movie movie) {
        if (context != null && movie != null) {
            context.startActivity(
                    new Intent(context, MovieDetailsActivity.class)
                    .putExtra(KEY_MOVIE, movie)
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_MOVIE) &&
                savedInstanceState.getParcelable(KEY_MOVIE) != null) {
            mMovie = savedInstanceState.getParcelable(KEY_MOVIE);
        } else if (getIntent() != null &&
                getIntent().hasExtra(KEY_MOVIE) &&
                getIntent().getParcelableExtra(KEY_MOVIE) != null) {
            mMovie = getIntent().getParcelableExtra(KEY_MOVIE);
        } else {
            throw new IllegalStateException("Invalid movie value");
        }
        if (mMovie != null) {
            setTitle(mMovie.getTitle());
            if (getSupportFragmentManager().findFragmentByTag(MovieDetailsFragment.TAG) == null) {
                getSupportFragmentManager().beginTransaction().add(
                        R.id.fl_fragment_placeholder,
                        getMovieDetailsFragment(),
                        MovieDetailsFragment.TAG
                ).commit();
            }
        } else {
            throw new IllegalStateException("Invalid movie value");
        }
    }

    private MovieDetailsFragment getMovieDetailsFragment() {
        if (mMovieDetailsFragment == null) {
            mMovieDetailsFragment =
                    (MovieDetailsFragment) getSupportFragmentManager()
                            .findFragmentByTag(MovieDetailsFragment.TAG);
        }
        if (mMovieDetailsFragment == null) {
            mMovieDetailsFragment = MovieDetailsFragment.newInstance(mMovie);
        }
        return mMovieDetailsFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(KEY_MOVIE, mMovie);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onTrailerClick(Movie.Trailer trailer) {
        if (trailer != null && !TextUtils.isEmpty(trailer.getVideoUrl())) {
            Uri videoUri = Uri.parse(trailer.getVideoUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
