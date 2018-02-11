package com.example.codelabs.moviestage;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.example.codelabs.moviestage.data.MovieDbHelper;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>, MovieAdapter.GridItemClickListener {
    private static final String URL_STRING = "urlString";
    private static final String SORT_FIELD_POPULARITY = "popularity.desc";
    private static final String SORT_FIELD_RATING = "vote_average.desc";
    private static final String SORT_FIELD_KEY = "sort_field";
    private static final String TITLE_KEY = "title_field";
    private String mTitle;
    private List<Movie> mMovieList = new ArrayList<>();
    private MovieAdapter mMovieAdapter;
    private RecyclerView mMovieRecyclerView;
    private Intent mPositionIntent;
    private String[] mBuiltUrl = new String[1] ;
    private String mSortOrder;





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mMovieAdapter.clear();
        switch (id) {

            case R.id.sortby_populatity:
                mSortOrder = SORT_FIELD_POPULARITY;
                mTitle = getString(R.string.popularity_title);

                break;
            case R.id.sortby_rating:
                mSortOrder = SORT_FIELD_RATING;
                mTitle = getString(R.string.rating_title);
                break;
            default:
                mSortOrder = SORT_FIELD_POPULARITY;
                mTitle = getString(R.string.popularity_title);
                break;
        }
        //load movies based on popularity or rating according to the selection
        loadMovie(mSortOrder);
        //set the corresponding title [POPULAR MOVIES or TOP RATED MOVIES]
        this.setTitle(mTitle);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby, menu);
        Stetho.initializeWithDefaults(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent backIntent = new Intent(MainActivity.this,StarterActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backIntent);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //persist sortorder and title in a bundle so that the same can be restored when the device is rotated
        outState.putString(SORT_FIELD_KEY, mSortOrder);
        outState.putString(TITLE_KEY, mTitle);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SQLiteDatabase dba1;
        MovieDbHelper mh = new MovieDbHelper(this);
        dba1=mh.getReadableDatabase();
        Log.d("krishna",Integer.toString(dba1.getVersion()));
        //If bundle contains data use the same
        if (savedInstanceState != null) {
            loadMovie(savedInstanceState.getString(SORT_FIELD_KEY));
            this.setTitle(savedInstanceState.getString(TITLE_KEY));
        }
        //If bundle is empty load default
        else {
            mSortOrder = SORT_FIELD_POPULARITY;
            mTitle = getString(R.string.popularity_title);
            this.setTitle(mTitle);
            loadMovie(mSortOrder);

        }
//


    }

    //Using a loader hit the appropriate endpoint so as to fetch data in the background



    @Override
    public void onItemClick(int clickedItemIndex) {
        //when a poster is clicked, get the movie object corresponding to that
        Movie movie = mMovieList.get(clickedItemIndex);
        //create an explicit intent to pass this to Moviedetail activity
        mPositionIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        /*As the Movie class implements parcelable, the movie object corresponding to the clicked position is passed
        as such to MovieDetail Activity*/
        mPositionIntent.putExtra(MovieDetailActivity.EXTRA_POSITION, movie);
        startActivity(mPositionIntent);
    }

    //This function takes sortorder as the parameter and loads the movies accordingly
    private void loadMovie(String sortOrder) {
        //Connectivity check
        if (checkConnectivity()) {
            //Initializing the loader/
            if (getSupportLoaderManager().getLoader(0) != null) {
                getSupportLoaderManager().initLoader(0, null, this);
            }
            mBuiltUrl[0] = MovieUtils.discoverBuildUrl(sortOrder);
            Bundle queryBundle = new Bundle();
            queryBundle.putStringArray(URL_STRING, mBuiltUrl);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
//            String k = MovieUtils.movieBuildUrl(MovieUtils.REVIEWS,MovieUtils.id);
//            Log.d("RAMANUJA",k);
        } else {
            //if there is no connectivity shows an alert and exits
            AlertDialog.Builder alertBuilder = new
                    AlertDialog.Builder(MainActivity.this);
            alertBuilder.setTitle("No internet connection");
            alertBuilder.setMessage("An internet connection is required for this app to function.");
            alertBuilder.setPositiveButton("OK", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });

            alertBuilder.show();


        }

    }

    private boolean checkConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, args.getStringArray(URL_STRING));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

            mMovieRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            mMovieList = MovieUtils.parseJson(data.get(0));
            mMovieAdapter = new MovieAdapter(this, mMovieList, this);
            mMovieRecyclerView.setAdapter(mMovieAdapter);
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mMovieAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}
