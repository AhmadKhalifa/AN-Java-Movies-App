package com.android.nanodegree.moviesapp.data.storage.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.data.storage.sql.helper.DatabaseHelper;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 25/03/2018.
 *
 */
public class MoviesSQLiteDatabaseHandler extends DatabaseHelper.DatabaseHandler
        implements MoviesLocalStorageHandler {

    @Override
    protected void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.CachedPopularMoviesEntry.getCreatingQuery()
        );
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.CachedTopRatedMoviesEntry.getCreatingQuery()
        );
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.FavoriteMoviesEntry.getCreatingQuery()
        );
    }

    @Override
    protected void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.CachedPopularMoviesEntry.getDroppingQuery()
        );
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.CachedTopRatedMoviesEntry.getDroppingQuery()
        );
        sqLiteDatabase.execSQL(
                MoviesDataBaseContracts.FavoriteMoviesEntry.getDroppingQuery()
        );
        onCreate(sqLiteDatabase);
    }

    @Override
    public Cursor getMoviesCursor(@NonNull QueryType queryType) {
        return DatabaseHelper.getInstance().getDatabase().query(
                queryType.toString(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public boolean isFavoriteMovie(@NonNull String movieId) {
        Cursor cursor =  DatabaseHelper.getInstance().getDatabase().query(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                null,
                MoviesDataBaseContracts.BaseMoviesEntry._ID + "=?",
                new String[] {movieId},
                null,
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
        if (DatabaseHelper.getInstance().getDatabase().insertWithOnConflict(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE) == -1) {
            throw new SQLiteException("Error adding movie to favorites");
        }
    }

    @Override
    public void removeMovieFromFavorites(@NonNull String movieId) {
        if (DatabaseHelper.getInstance().getDatabase().delete(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                MoviesDataBaseContracts.BaseMoviesEntry._ID + "=?",
                new String[] {movieId}) == 0) {
            throw new SQLiteException("Error removing movie from favorites");
        }
    }

    @Override
    public void cacheMovies(@NonNull QueryType queryType,
                            @NonNull ContentValues[] moviesContentValues) {
        String tableName = queryType.toString();
        SQLiteDatabase moviesDatabase = DatabaseHelper.getInstance().getDatabase();
        moviesDatabase.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues movieValues : moviesContentValues) {
                if (moviesDatabase.insert(tableName, null, movieValues) != -1) {
                    rowsInserted++;
                }
            }
            if (rowsInserted == moviesContentValues.length) {
                moviesDatabase.setTransactionSuccessful();
            } else {
                throw new SQLiteException("Error caching all movies.");
            }
        } finally {
            moviesDatabase.endTransaction();
        }
    }

    @Override
    public void clearTable(@NonNull QueryType queryType) {
        if (!TextUtils.isEmpty(queryType.toString())) {
            DatabaseHelper.getInstance().getDatabase().delete(
                    queryType.toString(),
                    null,
                    null
            );
        }
    }

    @Override
    public Cursor loadFavoriteMoviesIds() {
        return DatabaseHelper.getInstance().getDatabase().query(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                new String[]{MoviesDataBaseContracts.BaseMoviesEntry._ID},
                null,
                null,
                null,
                null,
                null
        );
    }

    public static class MoviesIdsCursorWrapper extends CursorWrapper {

        public MoviesIdsCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public String getMovieId() {
            return getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry._ID));
        }
    }

    public static class MoviesCursorWrapper extends CursorWrapper {

        public MoviesCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Movie getMovie() {
            return new Movie.MovieBuilder()
                    .id(Long.valueOf(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry._ID))))
                    .title(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_TITLE)))
                    .releaseDate(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_RELEASE_DATE)))
                    .posterTitle(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_POSTER_URL)))
                    .backdropPath(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_BACKDROP_URL)))
                    .overview(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_OVERVIEW)))
                    .voteAverage(Double.valueOf(
                            getString(getColumnIndex(MoviesDataBaseContracts.BaseMoviesEntry.COL_VOTE_AVERAGE))))
                    .build();
        }
    }
}
