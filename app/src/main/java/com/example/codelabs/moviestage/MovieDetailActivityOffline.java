package com.example.codelabs.moviestage;

/**
 * Created by varshaa on 06-02-2018.
 */

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
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class MovieDetailActivityOffline extends AppCompatActivity {
    boolean checked ;
    public static final String EXTRA_POSITION_OFFLINE = "clickedPositionoffline";
    private ImageView mthumbnailImageView;
    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoterAverageTextView;
    private Movie mMovieObject;
    private RecyclerView mTrailerRecyclerView;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_offline);

        Intent mainIntent = getIntent();
        mMovieObject = mainIntent.getParcelableExtra(EXTRA_POSITION_OFFLINE);
        mthumbnailImageView = (ImageView) findViewById(R.id.iv_thumbnail);
        //Load the image as a target with Picasso so as to generate a color swatch
        //This in turn is used to set the background and textcolor based on the color of the poster
        Bitmap posterBitmapOffline = MovieUtils.convertByteArrayToBitmap(MovieDetailActivityOffline.this,mMovieObject.getmRawImage());
        mthumbnailImageView.setImageBitmap(posterBitmapOffline);
        Palette.from(posterBitmapOffline)
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



        //populate the respective fields from the movie object
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOriginalTitleTextView.setText(mMovieObject.getmOriginalTitle());
        mOverviewTextView = (TextView) findViewById(R.id.tv_synopsis);
        mOverviewTextView.setText(mMovieObject.getmOverview());
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_releasedate);
        mReleaseDateTextView.setText(mMovieObject.getmReleaseDate());
        mVoterAverageTextView = (TextView) findViewById(R.id.tv_rating);
        mVoterAverageTextView.setText(mMovieObject.getmVoteAverage());

    }

}
