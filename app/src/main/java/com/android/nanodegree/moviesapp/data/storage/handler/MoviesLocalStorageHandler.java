package com.android.nanodegree.moviesapp.data.storage.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 4/14/2018.
 *
 */
public interface MoviesLocalStorageHandler {

    Cursor getMoviesCursor(@NonNull QueryType queryType);

    boolean isFavoriteMovie(@NonNull String movieId);

    void addMovieToFavorites(@NonNull ContentValues values);

    void removeMovieFromFavorites(@NonNull String movieId);

    void cacheMovies(@NonNull QueryType queryType,@NonNull ContentValues[] moviesContentValues);

    void clearTable(@NonNull QueryType queryType);

    Cursor loadFavoriteMoviesIds();
}
