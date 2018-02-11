package com.example.codelabs.moviestage.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.codelabs.moviestage.MainActivity;
import com.example.codelabs.moviestage.data.MovieContract.MovieEntry;

import static android.content.Context.MODE_PRIVATE;
import static com.example.codelabs.moviestage.MovieDetailActivity.SHARED_PREF_FILE;
import static com.example.codelabs.moviestage.data.MovieContract.MovieEntry.TABLE_NAME;


public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "movies.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 108;

    /**
     * Constructs a new instance of {@link MovieDbHelper}.
     *
     * @param context of the app
     */

    private  Context mContext;



    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Lends table
        Log.d("MovieDbHelperTag","In create");
        String SQL_CREATE_MOVIES_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_TITLE + " TEXT, "
                + MovieEntry.COLUMN_STATUS + " INTEGER NOT NULL DEFAULT 0, "
                + MovieEntry.COLUMN_RELEASEDATE + " TEXT, "
                + MovieEntry.COLUMN_RATING + " TEXT, "
                + MovieEntry.COLUMN_OVERVIEW + " TEXT, "
                + MovieEntry.COLUMN_POSTER + " BLOB " +
                ");";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        Log.d("MovieDbHelperTag","In Upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        mContext.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE).edit().clear().apply();
        onCreate(db);

    }
}