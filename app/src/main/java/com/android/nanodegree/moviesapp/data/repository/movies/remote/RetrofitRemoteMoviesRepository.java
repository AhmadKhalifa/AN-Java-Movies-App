package com.android.nanodegree.moviesapp.data.repository.movies.remote;

import com.android.nanodegree.moviesapp.data.response.MoviesResponse;
import com.android.nanodegree.moviesapp.data.response.ReviewsResponse;
import com.android.nanodegree.moviesapp.data.response.TrailersResponse;
import com.android.nanodegree.moviesapp.data.repository.movies.MovieService;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.util.BaseConnectionChecker;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class RetrofitRemoteMoviesRepository extends BaseRemoteMoviesRepository {

    public RetrofitRemoteMoviesRepository(BaseConnectionChecker connectionChecker,
                                          String apiKey,
                                          String baseUrl) {
        super(connectionChecker, apiKey, baseUrl);
    }

    @Override
    public List<Movie> loadMovies(QueryType queryType) throws Exception {
        try {
            MoviesResponse moviesResponse =
                    execute(create(MovieService.class).loadMovies(queryType.toString(), mApiKey));
            if (moviesResponse == null) {
                throw new Exception("Error getting movies");
            }
            return moviesResponse.getMovies();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception(throwable);
        }
    }

    @Override
    public List<Movie.Trailer> loadMovieTrailers(String movieId) throws Exception {
        try {
            TrailersResponse trailersResponse =
                    execute(create(MovieService.class).loadMovieTrailers(movieId, mApiKey));
            if (trailersResponse == null) {
                throw new Exception("Error getting movie trailers");
            }
            return trailersResponse.getTrailers();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception(throwable);
        }
    }

    @Override
    public List<Movie.Review> loadMovieReviews(String movieId) throws Exception {
        try {
            ReviewsResponse reviewsResponse =
                    execute(create(MovieService.class).loadMovieReviews(movieId, mApiKey));
            if (reviewsResponse == null) {
                throw new Exception("Error getting movies reviews");
            }
            return reviewsResponse.getReviews();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception(throwable);
        }
    }
}
