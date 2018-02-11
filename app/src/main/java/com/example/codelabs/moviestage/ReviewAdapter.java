package com.example.codelabs.moviestage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by varshaa on 21-01-2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mcontext;
    private List<Review> mReviewList;
    public ReviewAdapter(Context mcontext, List<Review> mReviewList) {
        this.mcontext = mcontext;
        this.mReviewList = mReviewList;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View reviewViewHolder = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ReviewViewHolder(reviewViewHolder);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String author = mReviewList.get(position).getAuthor();
        String labelText = (String.valueOf(author.charAt(0)).toUpperCase());
        holder.labelTextView.setText(labelText);
        holder.authorTextView.setText(author);
        holder.contentTextView.setText(mReviewList.get(position).getContent());
        holder.contentTextView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        if (mReviewList != null)
            return mReviewList.size();
        else
            return 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView labelTextView = (TextView) itemView.findViewById(R.id.tv_label);
        TextView authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
        TextView contentTextView = (TextView) itemView.findViewById(R.id.tv_content);


        public ReviewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
