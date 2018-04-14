package com.android.nanodegree.moviesapp.data.storage.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.data.storage.content.MoviesContentProvider;
import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 4/14/2018.
 *
 */
public class MoviesContentProviderHandler implements MoviesLocalStorageHandler {

    @Override
    public Cursor getMoviesCursor(@NonNull QueryType queryType) {
        return MoviesApp.getApplication().getContentResolver().query(
                getTableUri(queryType),
                null,
                null,
                null,
                null,
                null

        );
    }

    @Override
    public boolean isFavoriteMovie(@NonNull String movieId) {
        Cursor cursor = MoviesApp.getApplication().getContentResolver().query(
                MoviesContentProvider.FAVORITES_CONTENT_URI,
                null,
                MoviesDataBaseContracts.BaseMoviesEntry._ID + "=?",
                new String[] {movieId},
                null,
                null

        );
        boolean isFavoriteMovie = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return isFavoriteMovie;
    }

    @Override
    public void addMovieToFavorites(@NonNull ContentValues values) {
        MoviesApp.getApplication().getContentResolver().insert(
                MoviesContentProvider.FAVORITES_CONTENT_URI,
                values
        );
    }

    @Override
    public void removeMovieFromFavorites(@NonNull String movieId) {
        MoviesApp.getApplication().getContentResolver().delete(
                MoviesContentProvider.FAVORITES_CONTENT_URI,
                MoviesDataBaseContracts.BaseMoviesEntry._ID + "=?",
                new String[] {movieId}
        );
    }

    @Override
    public void cacheMovies(@NonNull QueryType queryType,
                            @NonNull ContentValues[] moviesContentValues) {
        MoviesApp.getApplication().getContentResolver().bulkInsert(
                getTableUri(queryType),
                moviesContentValues
        );
    }

    @Override
    public void clearTable(@NonNull QueryType queryType) {
        MoviesApp.getApplication().getContentResolver().delete(
                getTableUri(queryType),
                null,
                null
        );
    }

    @Override
    public Cursor loadFavoriteMoviesIds() {
        return MoviesApp.getApplication().getContentResolver().query(
                MoviesContentProvider.FAVORITES_CONTENT_URI,
                new String[]{MoviesDataBaseContracts.BaseMoviesEntry._ID},
                null,
                null,
                null,
                null

        );
    }

    private Uri getTableUri(QueryType queryType) {
        Uri tableUri;
        switch (queryType) {
            case MOST_POPULAR:
                tableUri = MoviesContentProvider.MOST_POPULAR_CONTENT_URI;
                break;
            case TOP_RATED:
                tableUri = MoviesContentProvider.TOP_RATED_CONTENT_URI;
                break;
            case FAVORITES:
                tableUri = MoviesContentProvider.FAVORITES_CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Invalid query type value");
        }
        return tableUri;
    }
}
