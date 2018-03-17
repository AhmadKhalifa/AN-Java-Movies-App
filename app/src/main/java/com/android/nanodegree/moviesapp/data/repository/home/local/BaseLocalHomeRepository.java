package com.android.nanodegree.moviesapp.data.repository.home.local;

import com.android.nanodegree.moviesapp.data.repository.BaseLocalRepository;
import com.android.nanodegree.moviesapp.data.repository.home.IHomeRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseLocalHomeRepository
        extends BaseLocalRepository implements IHomeRepository {

    public abstract List<Movie> loadFavoriteMovies();

    public abstract boolean cacheMovies(QueryType queryType, List<Movie> movies);
}
