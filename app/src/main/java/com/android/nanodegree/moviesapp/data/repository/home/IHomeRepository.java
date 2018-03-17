package com.android.nanodegree.moviesapp.data.repository.home;

import com.android.nanodegree.moviesapp.data.repository.IRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface IHomeRepository extends IRepository {

    List<Movie> loadMovies(QueryType queryType) throws Throwable;
}
