package com.android.nanodegree.moviesapp.mvp.movieDetails;

import android.text.TextUtils;

import com.android.nanodegree.moviesapp.data.repository.movies.local.BaseLocalMoviesRepository;
import com.android.nanodegree.moviesapp.data.repository.movies.remote.BaseRemoteMoviesRepository;
import com.android.nanodegree.moviesapp.exception.NoInternetConnectionException;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.mvp.BasePresenter;
import com.android.nanodegree.moviesapp.mvp.MVPView;

import java.util.List;

/**
 * Created by Khalifa on 03/04/2018.
 *
 */
public class MovieDetailsPresenter extends BasePresenter<MovieDetailsView>
        implements IMovieDetailPresenter {

    private BaseLocalMoviesRepository mLocalRepository;
    private BaseRemoteMoviesRepository mRemoteRepository;

    public MovieDetailsPresenter(BaseLocalMoviesRepository localRepository,
                         BaseRemoteMoviesRepository remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }
    
    @Override
    public void loadMovieTrailers(final String movieId) {
        if (isViewAttached() && !TextUtils.isEmpty(movieId)) {
            performActionAsync(
                    new Action<List<Movie.Trailer>>() {
                        @Override
                        public List<Movie.Trailer> run() throws Throwable {
                            return mRemoteRepository.loadMovieTrailers(movieId);
                        }
                    },
                    new Callback<List<Movie.Trailer>>() {

                        @Override
                        public void onSuccess(List<Movie.Trailer> trailers) {
                            if (trailers != null && isViewAttached()) {
                                getView().onTrailersRetrieved(trailers);
                            } else {
                                onError(null);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(
                                        error instanceof NoInternetConnectionException ?
                                                MVPView.Error.NO_INTERNET_CONNECTION :
                                                MVPView.Error.ERROR_LOADING_MOVIE_TRAILERS
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

    @Override
    public void loadMovieReviews(final String movieId) {
        if (isViewAttached() && !TextUtils.isEmpty(movieId)) {
            performActionAsync(
                    new Action<List<Movie.Review>>() {
                        @Override
                        public List<Movie.Review> run() throws Throwable {
                            return mRemoteRepository.loadMovieReviews(movieId);
                        }
                    },
                    new Callback<List<Movie.Review>>() {

                        @Override
                        public void onSuccess(List<Movie.Review> reviews) {
                            if (reviews != null && isViewAttached()) {
                                getView().onReviewsRetrieved(reviews);
                            } else {
                                onError(null);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(
                                        error instanceof NoInternetConnectionException ?
                                                MVPView.Error.NO_INTERNET_CONNECTION :
                                                MVPView.Error.ERROR_LOADING_MOVIE_REVIEWS
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

    @Override
    public void isFavoriteMovie(final String movieId) {
        if (isViewAttached() && !TextUtils.isEmpty(movieId)) {
            performActionAsync(
                    new Action<Boolean>() {
                        @Override
                        public Boolean run() throws Throwable {
                            return mLocalRepository.isFavoriteMovie(movieId);
                        }
                    },
                    new Callback<Boolean>() {

                        @Override
                        public void onSuccess(Boolean isFavoriteMovie) {
                            if (isViewAttached()) {
                                getView().onMovieFavoriteStateRetrieved(isFavoriteMovie);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(MVPView.Error.ERROR_GETTING_MOVIE_FAVORITE_STATE);
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
                            if (isViewAttached()) {
                                getView().onRemoveFromFavoritesSuccess(movie);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            if (isViewAttached()) {
                                getView().onError(MVPView.Error.ERROR_REMOVING_MOVIE_FROM_FAVORITES);
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }
            );
        }
    }
}
