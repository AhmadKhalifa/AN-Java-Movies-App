package com.android.nanodegree.moviesapp.mvp.home;

import com.android.nanodegree.moviesapp.data.repository.movies.IMoviesRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.local.BaseLocalMoviesRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.remote.BaseRemoteMoviesRepository;
import com.android.nanodegree.moviesapp.exception.NoInternetConnectionException;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;
import com.android.nanodegree.moviesapp.mvp.BasePresenter;
import com.android.nanodegree.moviesapp.mvp.MVPView;
import com.android.nanodegree.moviesapp.util.RxWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class HomePresenter extends BasePresenter<HomeView> implements IHomePresenter {

    private BaseLocalMoviesRepository mLocalRepository;
    private BaseRemoteMoviesRepository mRemoteRepository;

    private HashSet<Long> mFavoriteMoviesIds;

    public HomePresenter(BaseLocalMoviesRepository localRepository,
                         BaseRemoteMoviesRepository remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
        loadFavoriteMoviesIdsFromLocalRepository();
    }


    @Override
    public void loadMovies(QueryType queryType) {
        loadMovies(queryType, false);
    }

    @Override
    public void loadMovies(final QueryType queryType, boolean forceLoadRemotely) {
        if (forceLoadRemotely) {
            loadMoviesRemotely(queryType);
        } else {
            loadMoviesLocally(queryType);
        }
    }

    @Override
    public void loadFavoriteMoviesIds() {
        if (mFavoriteMoviesIds != null && isViewAttached()) {
            getView().onFavoriteMoviesIdsRetrieved(mFavoriteMoviesIds);
        } else {
            loadFavoriteMoviesIdsFromLocalRepository();
        }
    }

    @Override
    public void loadFavoriteMoviesIds(boolean forceReadFromDb) {
        if (forceReadFromDb) {
            loadFavoriteMoviesIdsFromLocalRepository();
        } else {
            loadFavoriteMoviesIds();
        }
    }

    @Override
    public void addToFavorites(final Movie movie) {
        if (isViewAttached() && movie != null) {
            performActionAsync(
                    new Action<Void>() {
                        @Override
                        public Void run() throws Throwable {
                            mLocalRepository.addMovieToFavorites(movie);
                            return null;
                        }
                    },
                    new Callback<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            mFavoriteMoviesIds.add(movie.getId());
                            if (isViewAttached()) {
                                getView().onAddToFavoritesSuccess(movie);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(MVPView.Error.ERROR_ADDING_MOVIE_TO_FAVORITES);
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }
            );
        }
    }

    @Override
    public void removeFromFavorites(final Movie movie) {
        if (isViewAttached() && movie != null) {
            performActionAsync(
                    new Action<Void>() {
                        @Override
                        public Void run() throws Throwable {
                            mLocalRepository.removeMovieFromFavorites(
                                    String.valueOf(movie.getId())
                            );
                            return null;
                        }
                    },
                    new Callback<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            mFavoriteMoviesIds.remove(movie.getId());
                            if (isViewAttached()) {
                                getView().onRemoveFromFavoritesSuccess(movie);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(
                                        MVPView.Error.ERROR_REMOVING_MOVIE_FROM_FAVORITES
                                );
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }
            );
        }
    }

    private void loadFavoriteMoviesIdsFromLocalRepository() {
        performActionAsync(
                new Action<HashSet<Long>>() {
                    @Override
                    public HashSet<Long> run() throws Throwable {
                        return mLocalRepository.loadFavoriteMoviesIds();
                    }
                },
                new Callback<HashSet<Long>>() {
                    @Override
                    public void onSuccess(HashSet<Long> favoriteMoviesIds) {
                        if (favoriteMoviesIds != null) {
                            mFavoriteMoviesIds = favoriteMoviesIds;
                            if (isViewAttached()) {
                                getView().onFavoriteMoviesIdsRetrieved(mFavoriteMoviesIds);
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        throw new IllegalStateException("Error loading favorite movies ids");
                    }

                    @Override
                    public void onFinish() {

                    }
                }
        );
    }

    private void loadMoviesLocally(QueryType queryType) {
        loadMovies(queryType, mLocalRepository);
    }

    private void loadMoviesRemotely(QueryType queryType) {
        loadMovies(queryType, mRemoteRepository);
    }

    private void loadMovies(final QueryType queryType,
                            final IMoviesRepository moviesRepository) {
        if (queryType == QueryType.FAVORITES &&
                !(moviesRepository instanceof BaseLocalMoviesRepository)) {
            throw new UnsupportedOperationException(
                    "Favorite movies are only stored in the local repository"
            );
        }
        if (isViewAttached() && queryType != null && moviesRepository != null) {
            final boolean isRequestingDataRemotely =
                    moviesRepository instanceof BaseRemoteMoviesRepository;
            performActionAsync(
                    new Action<List<Movie>>() {
                        @Override
                        public List<Movie> run() throws Throwable {
                            return moviesRepository.loadMovies(queryType);
                        }
                    },
                    new RxWrapper.Callback<List<Movie>>() {
                        @Override
                        public void onSuccess(List<Movie> movies) {
                            if (movies != null && !movies.isEmpty()) {
                                if (isRequestingDataRemotely) {
                                    if (isViewAttached()) {
                                        getView().onMoviesRetrieved(
                                                queryType,
                                                movies,
                                                true
                                        );
                                    }
                                    cacheMovies(queryType, movies);
                                } else if (isViewAttached()) {
                                    getView().onMoviesRetrieved(
                                            queryType,
                                            movies,
                                            false
                                    );
                                }
                            } else if (queryType == QueryType.FAVORITES && isViewAttached()){
                                getView().onMoviesRetrieved(
                                        queryType,
                                        new ArrayList<Movie>(),
                                        false
                                );
                            } else {
                                onError(null);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isRequestingDataRemotely) {
                                if (error instanceof NoInternetConnectionException && isViewAttached()) {
                                    getView().onError(MVPView.Error.NO_INTERNET_CONNECTION);
                                } else if (isViewAttached()) {
                                    getView().onError(MVPView.Error.NON_STABLE_CONNECTION);
                                }
                            } else if (queryType == QueryType.FAVORITES) {
                                if (isViewAttached()) {
                                    getView().onError(MVPView.Error.ERROR_LOADING_FAVORITE_MOVIES);
                                }
                            } else {
                                loadMoviesRemotely(queryType);
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }
            );
        }
    }

    private void cacheMovies(final QueryType queryType, final List<Movie> movies) {
        performActionAsync(
                new Action<Void>() {
                    @Override
                    public Void run() throws Throwable {
                        mLocalRepository.cacheMovies(queryType, movies);
                        return null;
                    }
                },
                new Callback<Void>() {
                    @Override
                    public void onSuccess(Void success) {

                    }

                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onFinish() {

                    }
                }
        );
    }
}
