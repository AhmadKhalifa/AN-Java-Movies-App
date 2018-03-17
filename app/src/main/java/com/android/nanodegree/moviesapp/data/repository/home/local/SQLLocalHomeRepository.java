package com.android.nanodegree.moviesapp.data.repository.home.local;

import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class SQLLocalHomeRepository extends BaseLocalHomeRepository {

    @Override
    public List<Movie> loadMovies(QueryType queryType) throws Throwable {
        return null;
    }

    @Override
    public List<Movie> loadFavoriteMovies() {
        return null;
    }

    @Override
    public boolean cacheMovies(QueryType queryType, List<Movie> movies) {
        return false;
    }
}
