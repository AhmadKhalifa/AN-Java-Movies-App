package com.android.nanodegree.moviesapp.data.storage.sql.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.data.storage.sql.helper.DatabaseHelper;
import com.android.nanodegree.moviesapp.model.Movie;
import com.android.nanodegree.moviesapp.model.QueryType;

/**
 * Created by Khalifa on 25/03/2018.
 *
 */
public class MoviesDatabaseHandler extends DatabaseHelper.DatabaseHandler {

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

    public Cursor getMoviesCursor(QueryType queryType) throws Exception {
        String tableName;
        switch (queryType) {
            case MOST_POPULAR:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                break;
            case TOP_RATED:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                break;
            case FAVORITES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new NullPointerException("Table name cannot be null");
        }
        return DatabaseHelper.getInstance().getDatabase().query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public boolean isFavoriteMovie(String movieId) throws Exception {
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

    public void addMovieToFavorites(ContentValues values) throws Exception {
        if (DatabaseHelper.getInstance().getDatabase().insertWithOnConflict(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE) == -1) {
            throw new Exception("Error adding movie to favorites");
        }
    }

    public void removeMovieFromFavorites(String movieId) throws Exception {
        if (DatabaseHelper.getInstance().getDatabase().delete(
                MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME,
                MoviesDataBaseContracts.BaseMoviesEntry._ID + "=?",
                new String[] {movieId}) == 0) {
            throw new Exception("Error removing movie from favorites");
        }
    }

    public void cacheMovies(QueryType queryType, ContentValues[] moviesContentValues)
            throws Exception {
        String tableName;
        switch (queryType) {
            case MOST_POPULAR:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                clearTable(tableName);
                break;
            case TOP_RATED:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                clearTable(tableName);
                break;
            default:
                throw new NullPointerException("Query type cannot be null");
        }
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
                throw new Exception("Error caching all movies.");
            }
        } finally {
            moviesDatabase.endTransaction();
        }
    }

    private void clearTable(String tableName) throws Exception {
        if (!TextUtils.isEmpty(tableName)) {
            DatabaseHelper.getInstance().getDatabase().delete(
                    tableName,
                    null,
                    null
            );
        }
    }

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
