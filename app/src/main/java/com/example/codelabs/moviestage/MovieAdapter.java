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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context mcontext;
    private List<Movie> mMovieList;
    private GridItemClickListener mListener;
    //private ShareItemClickListener mShareItemClickListener;

    public MovieAdapter(Context mcontext, List<Movie> mMovieList, GridItemClickListener mListener) {
            this.mcontext = mcontext;
        this.mMovieList = mMovieList;
        this.mListener = mListener;
        //this.mShareItemClickListener = mShareItemClickListener;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View movieViewHolder = inflater.inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(movieViewHolder);

    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        Picasso.with(mcontext).load(mMovieList.get(position).getmImageUrl()).error(R.drawable.art_themoviedb).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null)
            return mMovieList.size();
        else
            return 0;
    }

    public void clear() {
        if (mMovieList != null) {
            int size = this.mMovieList.size();
            this.mMovieList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public interface GridItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView movieImageView = (ImageView) itemView.findViewById(R.id.iv_poster);

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListener.onItemClick(clickedPosition);

        }
    }


}

