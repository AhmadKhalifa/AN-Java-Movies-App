package com.android.nanodegree.moviesapp.data.repository.home;

import com.android.nanodegree.moviesapp.data.payload.response.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface HomeService {

    @GET("/3/movie/{query_type}")
    Call<MoviesResponse> loadMovies(@Path("query_type") String queryType,
                                    @Query("api_key") String apiKey);
}
