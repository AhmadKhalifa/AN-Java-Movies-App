package com.android.nanodegree.moviesapp.mvp.home;


import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.mvp.MVPView;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface HomeView extends MVPView {

    void onMoviesRetrieved(QueryType queryType, List<Movie> movies, boolean isRemotelyLoaded);

    void onFavoriteMoviesIdsRetrieved(HashSet<Long> favoriteMoviesIdsSet);

    void onAddToFavoritesSuccess(Movie movie);

    void onRemoveFromFavoritesSuccess(Movie movie);
}
