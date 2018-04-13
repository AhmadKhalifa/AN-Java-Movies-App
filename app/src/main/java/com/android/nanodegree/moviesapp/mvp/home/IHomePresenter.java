package com.android.nanodegree.moviesapp.mvp.home;

import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
interface IHomePresenter {

    void loadMovies(QueryType queryType);

    void loadMovies(QueryType queryType, boolean forceLoadRemotely);

    void loadFavoriteMoviesIds();

    void loadFavoriteMoviesIds(boolean forceReadFromDb);

    void addToFavorites(Movie movie);

    void removeFromFavorites(Movie movie);
}
