package com.android.nanodegree.moviesapp.data.repository.movies.remote;

import com.android.nanodegree.moviesapp.data.repository.BaseRemoteRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.IMoviesRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.util.BaseConnectionChecker;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseRemoteMoviesRepository
        extends BaseRemoteRepository implements IMoviesRepository {

    final String mApiKey;

    public BaseRemoteMoviesRepository(BaseConnectionChecker connectionChecker,
                                      String apiKey,
                                      String baseUrl){
        super(connectionChecker, baseUrl);
        mApiKey = apiKey;
    }

    public abstract List<Movie.Trailer> loadMovieTrailers(String movieId) throws Exception;

    public abstract List<Movie.Review> loadMovieReviews(String movieId) throws Exception;
}
