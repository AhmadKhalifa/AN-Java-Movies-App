package com.android.nanodegree.moviesapp.data.repository.movies;

import com.android.nanodegree.moviesapp.data.response.MoviesResponse;
import com.android.nanodegree.moviesapp.data.response.ReviewsResponse;
import com.android.nanodegree.moviesapp.data.response.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface MovieService {

    @GET("/3/movie/{query_type}")
    Call<MoviesResponse> loadMovies(@Path("query_type") String queryType,
                                    @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/videos")
    Call<TrailersResponse> loadMovieTrailers(@Path("movie_id") String movieId,
                                             @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewsResponse> loadMovieReviews(@Path("movie_id") String movieId,
                                           @Query("api_key") String apiKey);
}
