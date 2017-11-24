package com.example.codelabs.moviestage;

import android.util.Log;

import com.example.codelabs.moviestage.Utils.MovieUtils;
/**
 * Created by varshaa on 23-11-2017.
 */

public class Movie {

    private String mImageUrl;
    private String mOriginalTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mVoteAverage;

    public Movie(String mImageUrl, String mOriginalTitle, String mOverview, String mReleaseDate, String mVoteAverage) {
        this.mImageUrl = mImageUrl;
        this.mOriginalTitle = mOriginalTitle;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
        this.mOverview = mOverview;
    }

    public String getmImageUrl() {
        Log.d("rurl",mImageUrl);
        return MovieUtils.buildImageUrl(mImageUrl);
    }


    public String getmOriginalTitle() {
        return mOriginalTitle;
    }


    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {

        return MovieUtils.formatDate(mReleaseDate);
    }

    public String getmVoteAverage() {
        return mVoteAverage + "/10";
    }


}





