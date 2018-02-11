package com.example.codelabs.moviestage;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.codelabs.moviestage.Utils.MovieUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by varshaa on 23-11-2017.
 */
//Loader class for populating the movie data in mainActivity

public class MovieLoader extends AsyncTaskLoader<List<String>> {
    private String[] mQueryURL;
    private List<String> mJsonResponse = new ArrayList<>();

    public MovieLoader(Context context, String[] queryURL) {
        super(context);
        mQueryURL = queryURL;
    }

    @Override
    public List<String> loadInBackground() {
        for (int i = 0; i < mQueryURL.length; i++) {
            try {
                mJsonResponse.add(MovieUtils.getResponseFromHttpUrl(mQueryURL[i]));

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return mJsonResponse;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
