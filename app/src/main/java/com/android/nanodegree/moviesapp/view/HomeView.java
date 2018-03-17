package com.android.nanodegree.moviesapp.view;


import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface HomeView extends MVPView {

    void onLocalMoviesRetrieved(QueryType queryType, List<Movie> cachedMovies);

    void onRemoteMoviesRetrieved(QueryType queryType, List<Movie> remoteMovies);

    void onLoadMoviesFailure(Error error);
}
