package com.example.codelabs.moviestage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    ImageView mthumbnailImageView;
    TextView mOriginalTitleTextView;
    TextView mOverviewTextView;
    TextView mReleaseDateTextView;
    TextView mVoterAverageTextView ;
    List<Movie> movieList;
    String mSortField = "popularity.desc";
    public static final String EXTRA_POSITION = "clickedPosition";
    Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent mainIntent = getIntent();
        int clickedPosition = mainIntent.getIntExtra(EXTRA_POSITION,1);
        //mToast.makeText(this,Integer.toString(clickedPosition),Toast.LENGTH_SHORT).show();



        mthumbnailImageView = (ImageView)findViewById(R.id.iv_thumbnail);
        Picasso.with(this).load(movieList.get(clickedPosition).getmImageUrl()).into(mthumbnailImageView);
        mOriginalTitleTextView = (TextView)findViewById(R.id.tv_title);
        mOriginalTitleTextView.setText(movieList.get(clickedPosition).getmOriginalTitle());
        mOverviewTextView = (TextView)findViewById(R.id.tv_synopsis);
        mOverviewTextView.setText(movieList.get(clickedPosition).getmOverview());
        mReleaseDateTextView = (TextView)findViewById(R.id.tv_releasedate);
        mReleaseDateTextView.setText(movieList.get(clickedPosition).getmReleaseDate());
        mVoterAverageTextView = (TextView)findViewById(R.id.tv_rating);
        mVoterAverageTextView.setText(movieList.get(clickedPosition).getmVoteAverage());



    }


}
