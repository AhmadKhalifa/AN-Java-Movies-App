package com.android.nanodegree.moviesapp.data.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class BaseRemoteRepository {

    private Map<Class<?>, Object> mServicesMap;

    private final String mBaseUrl;

    public BaseRemoteRepository(String baseUrl) {
        mBaseUrl = baseUrl;
        mServicesMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected <T> T create(Class<T> clazz) {
        T service;
        if (mServicesMap.containsKey(clazz)) {
            service = (T) mServicesMap.get(clazz);
        } else {
            service = retrofit().create(clazz);
            mServicesMap.put(clazz, service);
        }
        return service;
    }

    protected <T> T execute(Call<T> call) throws Throwable {
        Response<T> response = call.execute();
        if (!response.isSuccessful()) {
            //handle service exceptions
            throw new Throwable(response.message());
        }
        return response.body();
    }

    private Retrofit retrofit() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(2 * 60, TimeUnit.SECONDS)
                .connectTimeout(2 * 60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
