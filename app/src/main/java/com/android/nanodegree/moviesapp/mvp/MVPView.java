package com.android.nanodegree.moviesapp.mvp;

import com.android.nanodegree.moviesapp.R;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public interface MVPView {

    enum Error {

        GENERAL_ERROR(R.string.general_error),
        NO_INTERNET_CONNECTION(R.string.no_internet_connection),
        NON_STABLE_CONNECTION(R.string.non_stable_connection),
        ERROR_LOADING_FAVORITE_MOVIES(R.string.error_loading_favorite_movies),
        ERROR_LOADING_MOVIE_TRAILERS(R.string.error_loading_movie_trailers),
        ERROR_LOADING_MOVIE_REVIEWS(R.string.error_loading_movie_reviews),
        ERROR_ADDING_MOVIE_TO_FAVORITES(R.string.error_adding_movie_to_favorites),
        ERROR_REMOVING_MOVIE_FROM_FAVORITES(R.string.error_removing_movie_from_favorites),
        ERROR_GETTING_MOVIE_FAVORITE_STATE(R.string.error_getting_movie_favorite_state);

        private int mStringResId;

        Error(int stringResId) {
            mStringResId = stringResId;
        }

        public int getStringResId() {
            return mStringResId;
        }
    }

    void onError(Error error);
}
