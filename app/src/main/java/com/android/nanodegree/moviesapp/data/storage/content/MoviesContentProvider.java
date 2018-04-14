package com.android.nanodegree.moviesapp.data.storage.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.data.storage.sql.contract.MoviesDataBaseContracts;
import com.android.nanodegree.moviesapp.data.storage.sql.helper.DatabaseHelper;

public class MoviesContentProvider extends ContentProvider {

    private static final String PROVIDER_NAME =
            "com.android.nanodegree.moviesapp.data.MoviesContentProvider";
    private static final String MOST_POPULAR_URL =
            "content://" + PROVIDER_NAME + "/mostPopularMovies";
    private static final String TOP_RATED_URL =
            "content://" + PROVIDER_NAME + "/topRatedMovies";
    private static final String FAVORITES_URL =
            "content://" + PROVIDER_NAME + "/favoriteMovies";

    public static final Uri MOST_POPULAR_CONTENT_URI = Uri.parse(MOST_POPULAR_URL);
    public static final Uri TOP_RATED_CONTENT_URI = Uri.parse(TOP_RATED_URL);
    public static final Uri FAVORITES_CONTENT_URI = Uri.parse(FAVORITES_URL);

    private static final int MOST_POPULAR_MOVIES = 1;
    private static final int TOP_RATED_MOVIES = 2;
    private static final int FAVORITE_MOVIES = 3;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(PROVIDER_NAME, "mostPopularMovies", MOST_POPULAR_MOVIES);
        sUriMatcher.addURI(PROVIDER_NAME, "topRatedMovies", TOP_RATED_MOVIES);
        sUriMatcher.addURI(PROVIDER_NAME, "favoriteMovies", FAVORITE_MOVIES);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String tableName;
        Uri tableContentUri;
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                tableContentUri = MOST_POPULAR_CONTENT_URI;
                break;
            case TOP_RATED_MOVIES:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                tableContentUri = TOP_RATED_CONTENT_URI;
                break;
            case FAVORITE_MOVIES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                tableContentUri = FAVORITES_CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = DatabaseHelper.getInstance().getDatabase().insert(
                tableName,
                null,
                values
        );
        if (id > 0) {
            Uri insertedRowUri = ContentUris.withAppendedId(tableContentUri, id);
            MoviesApp
                    .getApplication()
                    .getContentResolver()
                    .notifyChange(insertedRowUri, null);
            return insertedRowUri;
        } else {
            throw new SQLiteException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                break;
            case TOP_RATED_MOVIES:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                break;
            case FAVORITE_MOVIES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        SQLiteDatabase database = DatabaseHelper.getInstance().getDatabase();
        database.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues movieValues : values) {
                if (database.insert(tableName, null, movieValues) != -1) {
                    rowsInserted++;
                }
            }
            if (rowsInserted == values.length) {
                database.setTransactionSuccessful();
            } else {
                throw new SQLiteException("Error caching all movies.");
            }
        } finally {
            database.endTransaction();
        }
        return rowsInserted;
    }

    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                break;
            case TOP_RATED_MOVIES:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                break;
            case FAVORITE_MOVIES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = DatabaseHelper.getInstance().getDatabase().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor != null) {
            cursor.setNotificationUri(MoviesApp.getApplication().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                break;
            case TOP_RATED_MOVIES:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                break;
            case FAVORITE_MOVIES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = DatabaseHelper
                .getInstance()
                .getDatabase()
                .delete(tableName, selection, selectionArgs);
        MoviesApp.getApplication().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                tableName = MoviesDataBaseContracts.CachedPopularMoviesEntry.TABLE_NAME;
                break;
            case TOP_RATED_MOVIES:
                tableName = MoviesDataBaseContracts.CachedTopRatedMoviesEntry.TABLE_NAME;
                break;
            case FAVORITE_MOVIES:
                tableName = MoviesDataBaseContracts.FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = DatabaseHelper
                .getInstance()
                .getDatabase()
                .update(tableName, values, selection, selectionArgs);
        MoviesApp.getApplication().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOST_POPULAR_MOVIES:
                return "MOST_POPULAR_MOVIES";
            case TOP_RATED_MOVIES:
                return "TOP_RATED_MOVIES";
            case FAVORITE_MOVIES:
                return "FAVORITE_MOVIES";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
