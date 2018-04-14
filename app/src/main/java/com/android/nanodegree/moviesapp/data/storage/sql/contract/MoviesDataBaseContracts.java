package com.android.nanodegree.moviesapp.data.storage.sql.contract;

import android.provider.BaseColumns;

/**
 * Created by Khalifa on 25/03/2018.
 *
 */
public class MoviesDataBaseContracts {

    public interface BaseMoviesEntry extends BaseColumns {
        String COL_TITLE = "title";
        String COL_RELEASE_DATE = "release_date";
        String COL_POSTER_URL = "poster_url";
        String COL_BACKDROP_URL = "backdrop_url";
        String COL_OVERVIEW = "overview";
        String COL_VOTE_AVERAGE = "vote_average";
    }

    static String getCreatingQuery(String tableName) {
        return String.format("CREATE TABLE IF NOT EXISTS %s (", tableName) +
                String.format("%s TEXT PRIMARY KEY, ", BaseMoviesEntry._ID) +
                String.format("%s TEXT, ", BaseMoviesEntry.COL_TITLE) +
                String.format("%s TEXT, ", BaseMoviesEntry.COL_RELEASE_DATE) +
                String.format("%s TEXT, ", BaseMoviesEntry.COL_POSTER_URL) +
                String.format("%s TEXT, ", BaseMoviesEntry.COL_BACKDROP_URL) +
                String.format("%s TEXT, ", BaseMoviesEntry.COL_OVERVIEW) +
                String.format("%s TEXT);", BaseMoviesEntry.COL_VOTE_AVERAGE);
    }

    static String getDroppingQuery(String tableName) {
        return ("DROP TABLE IF EXISTS " + tableName + ";");
    }
    
    public static class CachedPopularMoviesEntry implements BaseMoviesEntry {

        public static final String TABLE_NAME = "popular";

        public static String getCreatingQuery() {
            return MoviesDataBaseContracts.getCreatingQuery(TABLE_NAME);
        }

        public static String getDroppingQuery() {
            return MoviesDataBaseContracts.getDroppingQuery(TABLE_NAME);
        }
    }

    public static class CachedTopRatedMoviesEntry implements BaseMoviesEntry {

        public static final String TABLE_NAME = "top_rated";

        public static String getCreatingQuery() {
            return MoviesDataBaseContracts.getCreatingQuery(TABLE_NAME);
        }

        public static String getDroppingQuery() {
            return MoviesDataBaseContracts.getDroppingQuery(TABLE_NAME);
        }
    }

    public static class FavoriteMoviesEntry implements BaseMoviesEntry {

        public static final String TABLE_NAME = "favorites";

        public static String getCreatingQuery() {
            return MoviesDataBaseContracts.getCreatingQuery(TABLE_NAME);
        }

        public static String getDroppingQuery() {
            return MoviesDataBaseContracts.getDroppingQuery(TABLE_NAME);
        }
    }
}
