package com.android.nanodegree.moviesapp.data.repository.movies.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.data.storage.sql.handler.MoviesDatabaseHandler;
import com.android.nanodegree.moviesapp.data.storage.sql.helper.DatabaseHelper;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class SQLiteLocalMoviesRepository extends BaseLocalMoviesRepository {

    @Override
    @SuppressWarnings("all")
    public List<Movie> loadMovies(QueryType queryType) throws Exception {
        if (queryType == null) {
            throw new NullPointerException("Query type cannot be null");
        }
        Cursor moviesCursor = DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .getMoviesCursor(queryType);
        List<Movie> movies = new ArrayList<>();
        MoviesDatabaseHandler.MoviesCursorWrapper cursorWrapper =
                new MoviesDatabaseHandler.MoviesCursorWrapper(moviesCursor);
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
            throw new NullPointerException("Query type cannot be null");
        }
        DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .cacheMovies(queryType, getMoviesContentValuesArray(movies));
    }

    @Override
    public boolean isFavoriteMovie(String movieId) throws Exception {
        if (TextUtils.isEmpty(movieId)) {
            throw new IllegalStateException("Movie id cannot be null or empty");
        }
        return DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .isFavoriteMovie(movieId);
    }

    @Override
    public void addMovieToFavorites(Movie movie) throws Exception {
        if (movie == null) {
            throw new NullPointerException("Movie cannot be null");
        }
        DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .addMovieToFavorites(getMovieContentValue(movie));
    }

    @Override
    public void removeMovieFromFavorites(String movieId) throws Exception {
        if (TextUtils.isEmpty(movieId)) {
            throw new NullPointerException("Movie id cannot be null or empty");
        }
        DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .removeMovieFromFavorites(movieId);
    }

    @Override
    @SuppressWarnings("all")
    public HashSet<Long> loadFavoriteMoviesIds() {
        HashSet<Long> favoriteMoviesIds = new HashSet<>();
        Cursor favoriteMoviesIdsCursor = DatabaseHelper
                .getInstance()
                .getMoviesDatabaseHandler()
                .loadFavoriteMoviesIds();
        MoviesDatabaseHandler.MoviesIdsCursorWrapper cursorWrapper =
                new MoviesDatabaseHandler.MoviesIdsCursorWrapper(favoriteMoviesIdsCursor);
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
