package com.example.codelabs.moviestage;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.example.codelabs.moviestage.data.MovieContract;
import com.example.codelabs.moviestage.data.MovieDbHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {
    boolean checked ;
    //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    private SharedPreferences mPreferences;
    public static final String SHARED_PREF_FILE = "com.example.codelabs.moviestage";
    SQLiteDatabase dba ;
    public static final String EXTRA_POSITION = "clickedPosition";
    private static final String URL_STRING = "urlString";
    private ImageView mthumbnailImageView;
    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoterAverageTextView;
    private Movie mMovieObject;
    private CheckBox mFavoriteCheck;
    private String[] mBuiltUrl = new String[2];
    private List<Review> mReviewList;
    private List<Trailer> mTrailerList;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;
    private byte[] byteArray;
    private ScrollView mReviewScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        Intent mainIntent = getIntent();
        //get the movie object from the intent
        mMovieObject = mainIntent.getParcelableExtra(EXTRA_POSITION);
        mthumbnailImageView = (ImageView) findViewById(R.id.iv_thumbnail);
        //Load the image as a target with Picasso so as to generate a color swatch
        //This in turn is used to set the background and textcolor based on the color of the poster
        Picasso.with(this).load(mMovieObject.getmImageUrl()).error(R.drawable.art_themoviedb).into(new Target() {
            @Override

            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                assert mthumbnailImageView != null;
                mthumbnailImageView.setImageBitmap(bitmap);
                Palette.from(bitmap)
                        .generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                                if (vibrantSwatch == null) {
                                    return;
                                }

                                if (mutedSwatch == null) {
                                    return;
                                }
                                mOriginalTitleTextView.setTextColor(vibrantSwatch.getTitleTextColor());
                                mOriginalTitleTextView.setBackgroundColor(vibrantSwatch.getRgb());
                                mOverviewTextView.setTextColor(vibrantSwatch.getBodyTextColor());
                                GradientDrawable bgShape = (GradientDrawable) mVoterAverageTextView.getBackground();
                                bgShape.setColor(mutedSwatch.getRgb());
                                mOverviewTextView.setBackgroundColor(vibrantSwatch.getRgb());
                                mVoterAverageTextView.setTextColor(mutedSwatch.getTitleTextColor());
                                mReleaseDateTextView.setTextColor(mutedSwatch.getRgb());
                            }
                        });
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byteArray = bos.toByteArray();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }


        });

        //Initializing the loader
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, MovieDetailActivity.this);}

        mBuiltUrl[0] = MovieUtils.movieBuildUrl(MovieUtils.REVIEWS,mMovieObject.getmId());
        mBuiltUrl[1] = MovieUtils.movieBuildUrl(MovieUtils.VIDEO,mMovieObject.getmId());
        Bundle queryBundle = new Bundle();
        queryBundle.putStringArray(URL_STRING, mBuiltUrl);
        getSupportLoaderManager().restartLoader(0, queryBundle, this);

        //populate the respective fields from the movie object
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOriginalTitleTextView.setText(mMovieObject.getmOriginalTitle());
        mOverviewTextView = (TextView) findViewById(R.id.tv_synopsis);
        mOverviewTextView.setText(mMovieObject.getmOverview());
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_releasedate);
        mReleaseDateTextView.setText(mMovieObject.getmReleaseDate());
        mVoterAverageTextView = (TextView) findViewById(R.id.tv_rating);
        mVoterAverageTextView.setText(mMovieObject.getmVoteAverage());
        mFavoriteCheck=(CheckBox)findViewById(R.id.favorite);
        if(mPreferences.getBoolean(mMovieObject.getmOriginalTitle(),FALSE))
        {
            mFavoriteCheck.setChecked(TRUE);
        }
        else
        {
            mFavoriteCheck.setChecked(FALSE);
        }

    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, args.getStringArray(URL_STRING));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_recyclerview);
        mReviewScrollView = (ScrollView) findViewById(R.id.sv_trailer);
        try {
            mReviewList = MovieUtils.parseReviewJson(data.get(0));
            mTrailerList = MovieUtils.parseTrailerJson(data.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mReviewRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                mReviewScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        mReviewAdapter = new ReviewAdapter(MovieDetailActivity.this, mReviewList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewAdapter.notifyDataSetChanged();
        Log.d("narayana",Integer.toString(mReviewList.size()) + Integer.toString(mTrailerList.size()));
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trialer_recyclerview);
//        mTrailerAdapter = new TrailerAdapter(MovieDetailActivity.this);
//        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
//        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mTrailerAdapter.notifyDataSetChanged();
        mTrailerAdapter = new TrailerAdapter(MovieDetailActivity.this, mTrailerList
        );
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerAdapter.notifyDataSetChanged();
//        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.list);
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(linearLayoutManager);

    }


    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        checked = ((CheckBox) view).isChecked();
        //Test();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(mMovieObject.getmOriginalTitle(),checked);
        preferencesEditor.commit();

        // Check which checkbox was clicked
                if (checked)
                {

                    //button++;

                    Toast.makeText(this,"FAVORITE!!!" + Boolean.toString(checked),Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    // Put the task description and selected mPriority into the ContentValues
                    contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,mMovieObject.getmOriginalTitle() );
                    contentValues.put(MovieContract.MovieEntry.COLUMN_STATUS,1);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RATING,mMovieObject.getmVoteAverage());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE,mMovieObject.getmReleaseDate());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,mMovieObject.getmOverview());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER,byteArray);
                    
                    // Insert the content values via a ContentResolver
                    //ContentValues contentValues = new ContentValues();
                    // Put the task description and selected mPriority into the ContentValues
                    //contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,mMovieObject.getmOriginalTitle() );
                    //contentValues.put(MovieContract.MovieEntry.COLUMN_STATUS,mPreferences.getBoolean(mMovieObject.getmOriginalTitle(),FALSE) );
                    String[] selectionColumn = {MovieContract.MovieEntry._ID,MovieContract.MovieEntry.COLUMN_TITLE};
                    String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
                    String[] selectionArgs = {mMovieObject.getmOriginalTitle()};
                    Boolean isDuplicate = duplicateValidation(contentValues,selectionColumn,selectionClause,selectionArgs);
                    if(isDuplicate)
                    {
                        deleteRows();

                    }

                    //Cursor c = dba.query(MovieContract.MovieEntry.TABLE_NAME, selectionColumn,selectionClause,selectionArgs,null,null,null);
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                    // Display the URI that's returned with a Toast
                    // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
                    if(uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }

                    // Finish activity (this returns back to MainActivity)
                    //finish();

                }
                else
                {
                    deleteRows();


                }



    }
    public Boolean duplicateValidation(ContentValues values,String[] selectionColumn,String selectionClause,String[] selectionArgs)
    {
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this);
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        //= {MovieContract.MovieEntry._ID,MovieContract.MovieEntry.COLUMN_TITLE};
        //String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
        //= {mMovieObject.getmOriginalTitle()};
        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, selectionColumn,selectionClause,selectionArgs,null,null,null);
        if(c.getCount() == 0)
            return false;
        else
            return true;


    }

    public void deleteRows()
    {
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this);
        dba = mMovieDbHelper.getReadableDatabase();
        String[] selectionColumn = {MovieContract.MovieEntry._ID,MovieContract.MovieEntry.COLUMN_TITLE};
        String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
        String[] selectionArgs = {mMovieObject.getmOriginalTitle()};


        String[] id = new String[1000];
        Cursor c = dba.query(MovieContract.MovieEntry.TABLE_NAME,selectionColumn,selectionClause,selectionArgs,null,null,null);
        String coln[] = c.getColumnNames();
        for(int i=0;i<coln.length;i++ )
        {
            Log.d("govindaa","col -" + i + "-" + coln[i]);
        }
        while(c.moveToNext())
        {
            for(int i =0;i < c.getCount();i++) {
                id[i] = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));
                Uri uriD = MovieContract.MovieEntry.CONTENT_URI;
                uriD = uriD.buildUpon().appendPath(id[i]).build();
                int del = getContentResolver().delete(uriD,null,null);
                Toast.makeText(this,"ILLAYE :("  + "del :" + del, Toast.LENGTH_LONG).show();

            }
        }


    }

//    public static int updateContent(ContentResolver resolver, Uri uri, String selectionArg) {
//        ContentValues conValues = new ContentValues();
//        String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
//        String[] selectionArgs = {selectionArg};
//        conValues.put(MovieContract.MovieEntry.COLUMN_STATUS,0);
//        int rowsUpdated = resolver.update(uri, conValues, selectionClause, selectionArgs);
//        return rowsUpdated;
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//       preferencesEditor.putBoolean(mMovieObject.getmOriginalTitle(),checked);
//        preferencesEditor.commit();
//    }
   //h//

    public void Test()
    {
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this);
        dba = mMovieDbHelper.getReadableDatabase();
        String[] selectionColumn = {MovieContract.MovieEntry._ID,MovieContract.MovieEntry.COLUMN_TITLE,MovieContract.MovieEntry.COLUMN_OVERVIEW,MovieContract.MovieEntry.COLUMN_RATING,
                                    MovieContract.MovieEntry.COLUMN_RELEASEDATE};
        //String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
        //String[] selectionArgs = {mMovieObject.getmOriginalTitle()};

        //String[] id = new String[100];
        //String rating = "";
        Cursor c = dba.query(MovieContract.MovieEntry.TABLE_NAME,selectionColumn,null,null,null,null,null);
        while(c.moveToNext())
        {
            for(int i =0;i < c.getCount();i++) {
                String rating = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASEDATE));
                //Uri uriD = MovieContract.MovieEntry.CONTENT_URI;
                //uriD = uriD.buildUpon().appendPath(id[i]).build();
                //int del = getContentResolver().delete(uriD,null,null);
                Toast.makeText(this,"ILLAYE :("  + "del :" + rating, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
