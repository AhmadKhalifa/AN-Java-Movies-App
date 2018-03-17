package com.android.nanodegree.moviesapp.data.repository.home.remote;

import com.android.nanodegree.moviesapp.data.payload.response.MoviesResponse;
import com.android.nanodegree.moviesapp.data.repository.home.HomeService;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class RetrofitRemoteHomeRepository extends BaseRemoteHomeRepository {

    public RetrofitRemoteHomeRepository(String apiKey) {
        super(apiKey);
    }

    @Override
    public List<Movie> loadMovies(QueryType queryType) throws Throwable {
        try {
            MoviesResponse moviesResponse =
                    execute(create(HomeService.class).loadMovies(queryType.toString(), mApiKey));
            if (moviesResponse == null) {
                throw new Exception("Error getting movies");
            }
            return moviesResponse.getMovies();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}
