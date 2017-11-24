package com.example.codelabs.moviestage;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.codelabs.moviestage.Utils.MovieUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by varshaa on 23-11-2017.
 */

public class MovieLoader extends AsyncTaskLoader<String> {
    private  String mQueryURL;
    public MovieLoader(Context context,String queryURL) {
        super(context);
        mQueryURL = queryURL;
    }

    @Override
    public String loadInBackground() {
        try {
            return MovieUtils.getResponseFromHttpUrl(mQueryURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "IOException";
    }

    @Override
    protected void onStartLoading() {
        forceLoad();;
    }

}
