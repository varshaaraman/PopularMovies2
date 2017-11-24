package com.example.codelabs.moviestage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by varshaa on 23-11-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    Context mcontext;
    List<Movie> mMovieList;
    GridItemClickListener mListener;

    public MovieAdapter(Context mcontext, List<Movie> mMovieList, GridItemClickListener mListener) {
        this.mcontext = mcontext;
        this.mMovieList = mMovieList;
        this.mListener=mListener;
    }

    public interface GridItemClickListener {
        void onItemClick(int clickedItemIndex);
    }
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View movieViewHolder = inflater.inflate(R.layout.list_item,parent,false);
        return new MovieViewHolder(movieViewHolder);

    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        Picasso.with(mcontext).load(mMovieList.get(position).getmImageUrl()).into(holder.movieImageView);
        //holder.movieImageView.s;
       // holder.movieImageView.setText(mMovieList.get(position).getmImageUrl());

    }

    @Override
    public int getItemCount() {
        if(mMovieList!=null)
            return mMovieList.size();
        else
            return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);}
        ImageView movieImageView = (ImageView)itemView.findViewById(R.id.iv_poster);
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListener.onItemClick(clickedPosition);

        }
        }




    }

