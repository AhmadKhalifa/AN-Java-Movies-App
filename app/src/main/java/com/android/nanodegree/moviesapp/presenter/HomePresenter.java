package com.android.nanodegree.moviesapp.presenter;

import android.util.Log;

import com.android.nanodegree.moviesapp.data.repository.home.IHomeRepository;
import com.android.nanodegree.moviesapp.data.repository.home.local.BaseLocalHomeRepository;
import com.android.nanodegree.moviesapp.data.repository.home.remote.BaseRemoteHomeRepository;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.util.ConnectionChecker;
import com.android.nanodegree.moviesapp.util.RxWrapper;
import com.android.nanodegree.moviesapp.view.Error;
import com.android.nanodegree.moviesapp.view.HomeView;

import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class HomePresenter extends BasePresenter<HomeView> implements IHomePresenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private BaseLocalHomeRepository mLocalRepository;
    private BaseRemoteHomeRepository mRemoteRepository;

    public HomePresenter(BaseLocalHomeRepository localRepository,
                         BaseRemoteHomeRepository remoteRepository) {
        Log.i(TAG, "HomePresenter");
        Log.d(TAG, "localRepository: " + localRepository);
        Log.d(TAG, "remoteRepository: " + remoteRepository);
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @Override
    public void getMovies(final QueryType queryType) {
        Log.i(TAG, "getMovies");
        Log.d(TAG, "query type: " + queryType);
        loadMoviesLocally(queryType);
    }

    private void loadMoviesLocally(QueryType queryType) {
        Log.i(TAG, "loadMoviesLocally");
        Log.d(TAG, "query type: " + queryType);
        loadMovies(queryType, mLocalRepository);
    }

    private void loadMoviesRemotely(QueryType queryType) {
        Log.i(TAG, "loadMoviesRemotely");
        Log.d(TAG, "query type: " + queryType);
        loadMovies(queryType, mRemoteRepository);
    }

    private void loadMovies(final QueryType queryType,
                            final IHomeRepository homeRepository) {
        Log.i(TAG, "loadMovies");
        Log.d(TAG, "query type: " + queryType);
        Log.d(TAG, "homeRepository " + homeRepository);
        if (getView() != null && queryType != null && homeRepository != null) {
            final boolean isRequestingDataRemotely =
                    homeRepository instanceof BaseRemoteHomeRepository;
            if (isRequestingDataRemotely && !ConnectionChecker.isNetworkAvailable()) {
                getView().onLoadMoviesFailure(Error.NO_INTERNET_CONNECTION);
                return;
            }
            performActionAsync(
                    new Action<List<Movie>>() {
                        @Override
                        public List<Movie> run() throws Throwable {
                            Log.i(TAG, "loadMovies.run");
                            return homeRepository.loadMovies(queryType);
                        }
                    },
                    new RxWrapper.Callback<List<Movie>>() {
                        @Override
                        public void onSuccess(List<Movie> movies) {
                            if (movies != null && !movies.isEmpty()) {
                                Log.i(TAG, "cacheMovies.onSuccess: Movies loaded successfully!");
                                Log.d(TAG, "Movies: " + movies);
                                if (isRequestingDataRemotely) {
                                    getView().onRemoteMoviesRetrieved(queryType, movies);
                                    cacheMovies(queryType, movies);
                                } else {
                                    getView().onLocalMoviesRetrieved(queryType, movies);
                                }
                            } else {
                                onError(null);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            Log.e(TAG, "loadMovies.onError: Error loading Movies.");
                            Log.d(TAG, "error: " + error);
                            if (isRequestingDataRemotely) {
                                getView().onLoadMoviesFailure(Error.NON_STABLE_CONNECTION);
                            } else {
                                loadMoviesRemotely(queryType);
                            }
                        }

                        @Override
                        public void onFinish() {
                            Log.i(TAG, "loadMovies.onFinish");
                        }
                    }
            );
        }
    }

    private void cacheMovies(final QueryType queryType, final List<Movie> movies) {
        Log.i(TAG, "cacheMovies: " + movies);
        performActionAsync(
                new Action<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        Log.i(TAG, "cacheMovies.run");
                        return mLocalRepository.cacheMovies(queryType, movies);
                    }
                },
                new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        Log.d(TAG, "cacheMovies.onSuccess: Movies cached? " + success);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "cacheMovies.onError: Error caching Movies.");
                        Log.d(TAG, "error: " + error);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "cacheMovies.onFinish");
                    }
                }
        );
    }
}
