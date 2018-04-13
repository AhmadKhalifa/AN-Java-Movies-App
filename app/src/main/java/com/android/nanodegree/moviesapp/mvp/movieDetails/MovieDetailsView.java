package com.android.nanodegree.moviesapp.mvp.movieDetails;

import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.mvp.MVPView;

import java.util.List;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
public interface MovieDetailsView extends MVPView {

    void onTrailersRetrieved(List<Movie.Trailer> trailers);

    void onReviewsRetrieved(List<Movie.Review> reviews);

    void onAddToFavoritesSuccess(Movie movie);

    void onRemoveFromFavoritesSuccess(Movie movie);

    void onMovieFavoriteStateRetrieved(boolean isFavoriteMovie);
}
