package com.android.nanodegree.moviesapp.mvp.movieDetails;

import com.android.nanodegree.moviesapp.model.Movie;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
interface IMovieDetailPresenter {

    void loadMovieTrailers(String movieId);

    void loadMovieReviews(String movieId);

    void isFavoriteMovie(String movieId);

    void addToFavorites(Movie movie);

    void removeFromFavorites(Movie movie);
}
