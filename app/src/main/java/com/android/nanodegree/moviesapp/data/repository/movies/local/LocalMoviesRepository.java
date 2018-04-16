package com.android.nanodegree.moviesapp.data.repository.movies.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.android.nanodegree.moviesapp.data.storage.handler.MoviesLocalStorageHandler;
import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.data.storage.handler.MoviesSQLiteDatabaseHandler;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class LocalMoviesRepository extends BaseLocalMoviesRepository {

    private final MoviesLocalStorageHandler mMoviesLocalStorageHandler;

    public LocalMoviesRepository(MoviesLocalStorageHandler moviesLocalStorageHandler) {
        if (moviesLocalStorageHandler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        mMoviesLocalStorageHandler = moviesLocalStorageHandler;
    }

    @Override
    @SuppressWarnings("all")
    public List<Movie> loadMovies(QueryType queryType) throws Exception {
        if (queryType == null) {
            throw new IllegalArgumentException("Query type cannot be null");
        }
        Cursor moviesCursor = mMoviesLocalStorageHandler.getMoviesCursor(queryType);
        List<Movie> movies = new ArrayList<>();
        MoviesSQLiteDatabaseHandler.MoviesCursorWrapper cursorWrapper =
                new MoviesSQLiteDatabaseHandler.MoviesCursorWrapper(moviesCursor);
        try {
            if (cursorWrapper.moveToFirst()) {
                do {
                    movies.add(cursorWrapper.getMovie());
                } while (cursorWrapper.moveToNext());
            }
        } finally {
            cursorWrapper.close();
        }
        return movies;
    }

    @Override
    public void cacheMovies(QueryType queryType, List<Movie> movies) throws Exception {
        if (queryType == null) {
            throw new IllegalArgumentException("Query type cannot be null");
        }
        mMoviesLocalStorageHandler.clearTable(queryType);
        mMoviesLocalStorageHandler.cacheMovies(queryType, getMoviesContentValuesArray(movies));
    }

    @Override
    public boolean isFavoriteMovie(String movieId) throws Exception {
        if (TextUtils.isEmpty(movieId)) {
            throw new IllegalArgumentException("Movie id cannot be null or empty");
        }
        return mMoviesLocalStorageHandler.isFavoriteMovie(movieId);
    }

    @Override
    public void addMovieToFavorites(Movie movie) throws Exception {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        mMoviesLocalStorageHandler.addMovieToFavorites(getMovieContentValue(movie));
    }

    @Override
    public void removeMovieFromFavorites(String movieId) throws Exception {
        if (TextUtils.isEmpty(movieId)) {
            throw new IllegalArgumentException("Movie id cannot be null or empty");
        }
        mMoviesLocalStorageHandler.removeMovieFromFavorites(movieId);
    }

    @Override
    @SuppressWarnings("all")
    public HashSet<Long> loadFavoriteMoviesIds() {
        HashSet<Long> favoriteMoviesIds = new HashSet<>();
        Cursor favoriteMoviesIdsCursor = mMoviesLocalStorageHandler.loadFavoriteMoviesIds();
        MoviesSQLiteDatabaseHandler.MoviesIdsCursorWrapper cursorWrapper =
                new MoviesSQLiteDatabaseHandler.MoviesIdsCursorWrapper(favoriteMoviesIdsCursor);
        try {
            if (cursorWrapper.moveToFirst()) {
                do {
                    favoriteMoviesIds.add(Long.parseLong(cursorWrapper.getMovieId()));
                } while (cursorWrapper.moveToNext());
            }
        } finally {
            cursorWrapper.close();
        }
        return favoriteMoviesIds;
    }

    private ContentValues getMovieContentValue(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry._ID,
                String.valueOf(movie.getId())
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_TITLE,
                movie.getTitle()
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_RELEASE_DATE,
                movie.getReleaseDate()
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_POSTER_URL,
                movie.getPosterPath()
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_BACKDROP_URL,
                movie.getBackdropPath()
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_OVERVIEW,
                movie.getOverview()
        );
        contentValues.put(
                MoviesDataBaseContracts.BaseMoviesEntry.COL_VOTE_AVERAGE,
                String.valueOf(movie.getVoteAverage())
        );
        return contentValues;
    }

    private ContentValues[] getMoviesContentValuesArray(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return new ContentValues[0];
        }
        ContentValues[] contentValues = new ContentValues[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            contentValues[i] = getMovieContentValue(movies.get(i));
        }
        return contentValues;
    }
}
