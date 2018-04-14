package com.android.nanodegree.moviesapp.data.storage.sql.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.nanodegree.moviesapp.MoviesApp;
import com.android.nanodegree.moviesapp.data.storage.handler.MoviesSQLiteDatabaseHandler;

/**
 * Created by Khalifa on 25/03/2018.
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int MOVIES_DATABASE_HANDLER_INDEX = 0;

    private static DatabaseHelper sInstance;

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "MoviesAppDB.db";
    private SQLiteDatabase mDatabase;
    private DatabaseHandler[] mDatabaseHandlers;

    public static DatabaseHelper getInstance() {
        if (sInstance == null) {
            synchronized (DatabaseHelper.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseHelper(
                            MoviesApp.getApplication()
                    );
                }
            }
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDatabaseHandlers = new DatabaseHandler[] {
                new MoviesSQLiteDatabaseHandler()
        };
    }

    public SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (DatabaseHandler databaseHandler : mDatabaseHandlers) {
            databaseHandler.onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        for (DatabaseHandler handler : mDatabaseHandlers) {
            handler.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        }
    }

    public MoviesSQLiteDatabaseHandler getMoviesDatabaseHandler() {
        return (MoviesSQLiteDatabaseHandler) mDatabaseHandlers[MOVIES_DATABASE_HANDLER_INDEX];
    }

    public static abstract class DatabaseHandler {

        protected abstract void onCreate(SQLiteDatabase sqLiteDatabase);

        protected abstract void onUpgrade(SQLiteDatabase sqLiteDatabase,
                                          int oldVersion,
                                          int newVersion);
    }
}
