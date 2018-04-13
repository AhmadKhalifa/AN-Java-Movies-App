package com.android.nanodegree.moviesapp.data.repository.movies;

import com.android.nanodegree.moviesapp.data.repository.IRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface IMoviesRepository extends IRepository {

    List<Movie> loadMovies(QueryType queryType) throws Exception;
}
