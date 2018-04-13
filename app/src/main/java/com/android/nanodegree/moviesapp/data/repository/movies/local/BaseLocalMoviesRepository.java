package com.android.nanodegree.moviesapp.data.repository.movies.local;

import com.android.nanodegree.moviesapp.data.repository.BaseLocalRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.IMoviesRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseLocalMoviesRepository
        extends BaseLocalRepository implements IMoviesRepository {

    public abstract void cacheMovies(QueryType queryType, List<Movie> movies) throws Exception;

    public abstract boolean isFavoriteMovie(String movieId) throws Exception;

    public abstract void addMovieToFavorites(Movie movie) throws Exception;

    public abstract void removeMovieFromFavorites(String movieId) throws Exception;

    public abstract HashSet<Long> loadFavoriteMoviesIds();
}
