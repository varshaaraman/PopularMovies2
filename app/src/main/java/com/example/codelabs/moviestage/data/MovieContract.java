package com.example.codelabs.moviestage.data;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;



public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.codelabs.moviestage";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POPULARMOVIES = "popularmovies";
    private MovieContract() {}


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULARMOVIES).build();;
        public final static String TABLE_NAME = "popularmovies";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE ="title";
        public final static String COLUMN_STATUS = "status";
        public final static String COLUMN_RELEASEDATE="release_date";
        public final static String COLUMN_RATING="rating";
        public final static String COLUMN_OVERVIEW="overview";
        public final static String COLUMN_POSTER="poster";





    }
}