package com.example.codelabs.moviestage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.example.codelabs.moviestage.data.MovieContract;
import com.example.codelabs.moviestage.data.MovieDbHelper;
import com.facebook.stetho.Stetho;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 04-02-2018.
 */

public class MainActivityOffline extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

     public static final int INV_LOADER = 0;
    public MovieAdapterOffline mMovieAdapterOffline;

    private String mTitle;
    private List<Movie> mMovieListOffline = new ArrayList<>();
    private MovieAdapter mMovieAdapter;
    private RecyclerView mMovieRecyclerView;
    private ProgressBar mLoadingIndicator;
    private Intent mPositionIntent;


    //private String[] mBuiltUrl = new String[1] ;
    //private String mSortOrder;
    SQLiteDatabase dba;
    private byte[] mByteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stetho.initializeWithDefaults(this);
        super.onCreate(savedInstanceState);

        //View emptyView = findViewById(R.id.empty_list_view);
        setContentView(R.layout.activity_main_offline);
        //mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        SQLiteDatabase dba1;
        MovieDbHelper mh = new MovieDbHelper(this);
        dba1=mh.getReadableDatabase();
        GridView offlineListView = (GridView) findViewById(R.id.listview_offline);
        //View emptyView = findViewById(R.id.empty_view);
        //lendListView.setEmptyView(emptyView);
        Log.d("krishna",Integer.toString(dba1.getVersion()));
        mMovieListOffline = MovieUtils.offlineResults(MainActivityOffline.this);
        mMovieAdapterOffline = new MovieAdapterOffline(this, null);
        ViewGroup parentGroup = (ViewGroup)offlineListView.getParent();
        View empty = this.getLayoutInflater().inflate(R.layout.activity_empty,
                parentGroup,
                false);
        parentGroup.setBackground(getResources().getDrawable(R.drawable.art_poster_collage_3,null));

        parentGroup.addView(empty);
        offlineListView.setEmptyView(empty);
        offlineListView.setAdapter(mMovieAdapterOffline);

        offlineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivityOffline.this,Integer.toString(position),Toast.LENGTH_SHORT).show();
                final int mposition = mMovieAdapterOffline.getCursor().getPosition();
                if(mMovieListOffline.size() > 0 && mposition < mMovieListOffline.size())
                {
                Movie movie = mMovieListOffline.get(mposition);
                //create an explicit intent to pass this to Moviedetail activity
                Toast.makeText(MainActivityOffline.this,Integer.toString(mposition) + "-" + Integer.toString(mMovieListOffline.size())+"-"+Integer.toString(mMovieListOffline.indexOf(movie))+movie.getmOverview(),Toast.LENGTH_SHORT).show();
                mPositionIntent = new Intent(MainActivityOffline.this, MovieDetailActivityOffline.class);
        /*As the Movie class implements parcelable, the movie object corresponding to the clicked position is passed
        as such to MovieDetail Activity*/
                mPositionIntent.putExtra(MovieDetailActivityOffline.EXTRA_POSITION_OFFLINE, movie);
                startActivity(mPositionIntent);
                }

                else
                {
                    Toast.makeText(MainActivityOffline.this,Integer.toString(position),Toast.LENGTH_SHORT).show();
                }


            }
        });
        getSupportLoaderManager().initLoader(INV_LOADER,null,this);
    }

    //Using a loader hit the appropriate endpoint so as to fetch data in the background






    //This function takes sortorder as the parameter and loads the movies accordingly

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {

                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_POSTER,
                MovieContract.MovieEntry.COLUMN_RELEASEDATE,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_STATUS };
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapterOffline.swapCursor(data);
        //progressDialog = new Dialog(this);



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapterOffline.swapCursor(null);
    }

}



