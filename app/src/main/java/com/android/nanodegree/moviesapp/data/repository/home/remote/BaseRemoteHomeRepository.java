package com.android.nanodegree.moviesapp.data.repository.home.remote;

import com.android.nanodegree.moviesapp.data.repository.BaseRemoteRepository;
import com.android.nanodegree.moviesapp.data.repository.home.IHomeRepository;
import com.android.nanodegree.moviesapp.util.Constants;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public abstract class BaseRemoteHomeRepository
        extends BaseRemoteRepository implements IHomeRepository {

    private static final String BASE_URL = Constants.THE_MOVIE_DB_API_BASE_URL;

    String mApiKey;

    BaseRemoteHomeRepository(String apiKey) {
        super(BASE_URL);
        mApiKey = apiKey;
    }
}
