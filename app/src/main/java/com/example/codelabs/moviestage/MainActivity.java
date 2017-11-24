package com.example.codelabs.moviestage;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,MovieAdapter.GridItemClickListener {
    String mSortField = "popularity.desc";
    List<Movie> mMovieList = new ArrayList<>();
    MovieAdapter mMovieAdapter;
    RecyclerView mMovieRecyclerView;
    Intent mPositionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);}

   String builtUrl = MovieUtils.buildUrl(mSortField);

        if (networkInfo != null && networkInfo.isConnected()) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("urlString", builtUrl);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        } else {
//            mAuthorText.setText("");
//            mTitleText.setText(R.string.no_network);
        }


    }


    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this,args.getString("urlString"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {



        //ImageView tv = (ImageView)findViewById(R.id.tv_test);
        mMovieList = MovieUtils.parseJson(data);
        //String url = mMovieList.get(1).getmImageUrl();
        //Log.d("URL",url);
        mMovieAdapter = new MovieAdapter(this,mMovieList,this);
        mMovieRecyclerView.setAdapter(mMovieAdapter);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this,2));


        mMovieAdapter.notifyDataSetChanged();
        Log.d("AURL",Integer.toString(mMovieList.size()));

        for(int i=0;i<mMovieList.size();i++){
            Log.d("LIstxxx",mMovieList.get(i).getmImageUrl());
        }



    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onItemClick(int clickedItemIndex) {
//
//        mPositionIntent = new Intent(MainActivity.this,MovieDetailActivity.class);
//        mPositionIntent.putExtra(MovieDetailActivity.EXTRA_POSITION,clickedItemIndex);
//        startActivity(mPositionIntent);
        if(Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // Current Thread is Main Thread.
            Toast mToast;
        mToast = Toast.makeText(this,"In main thread",Toast.LENGTH_SHORT);
      mToast.show();
        }

    }
}
