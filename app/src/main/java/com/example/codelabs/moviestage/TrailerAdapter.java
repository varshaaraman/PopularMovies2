package com.example.codelabs.moviestage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codelabs.moviestage.Trailer;
import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.VideoViewHolder> {
    Context mContext;
    List<Trailer> mTrailerList;
    YouTubeThumbnailView youTubeThumbnailView;
    private final int UNINITIALIZED = 1;
    private final int INITIALIZING = 2;
    private final int INITIALIZED = 3;
    String videoId;
    private int blackColor = Color.parseColor("#FF000000");
    private int transparentColor = Color.parseColor("#00000000");
    public TrailerAdapter(Context mContext, List<Trailer> mTrailerList) {
        this.mContext = mContext;
        this.mTrailerList = mTrailerList;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public YouTubeThumbnailView ytThubnailView = null;
        public ImageView ivYtLogo = null;
        public TextView tvTitle = null;
        public FloatingActionButton fab = null;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ytThubnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.yt_thumbnail);
            ivYtLogo = (ImageView) itemView.findViewById(R.id.iv_yt_logo);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            fab = (FloatingActionButton) itemView.findViewById(R.id.fab_share);
            ivYtLogo.setOnClickListener(this);
            fab.setOnClickListener(this);
            initialize();
        }

        public void initialize(){
            ivYtLogo.setBackgroundColor(blackColor);
            ytThubnailView.setTag(R.id.initialize, INITIALIZING);
            ytThubnailView.setTag(R.id.thumbnailloader, null);
            ytThubnailView.setTag(R.id.videoid, "");
            ytThubnailView.initialize("AIzaSyBr98uf-4DyhgZNkQ9FZqC8tgiflCwkUbc", new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    Toast.makeText(mContext,"success",Toast.LENGTH_LONG).show();
                    ytThubnailView.setTag(R.id.initialize, INITIALIZED);
                    ytThubnailView.setTag(R.id.thumbnailloader, youTubeThumbnailLoader);
                    youTubeThumbnailLoader.setVideo(mTrailerList.get(getAdapterPosition()).getTrailerId());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String loadedVideoId) {
                            Toast.makeText(mContext,"thumbnailloaded",Toast.LENGTH_LONG).show();
                            String currentVideoId = (String) ytThubnailView.getTag(R.id.videoid);
                            if(currentVideoId.equals(loadedVideoId)) {
                                ivYtLogo.setBackgroundColor(transparentColor);
                            }
                            else{
                                ivYtLogo.setBackgroundColor(blackColor);
                            }
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            Toast.makeText(mContext,"Failure",Toast.LENGTH_LONG).show();
                            ivYtLogo.setBackgroundColor(blackColor);
                        }
                    });

                   videoId = (String) ytThubnailView.getTag(R.id.videoid);
                    if(videoId != null && !videoId.isEmpty()){
                        youTubeThumbnailLoader.setVideo(videoId);
                    }
                }


                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Toast.makeText(mContext,"initfailure :(",Toast.LENGTH_LONG).show();
                    ytThubnailView.setTag(R.id.initialize, UNINITIALIZED);
                    ivYtLogo.setBackgroundColor(blackColor);
                }
            });
        }


        @Override
        public void onClick(View v) {
            //Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, MovieUtils.YOUTUBE_API_KEY, VideoID[getLayoutPosition()]);
            //ctx.startActivity(intent);
            int id = v.getId();
            int clickedPosition = getAdapterPosition();
            String youtubeURL = MovieUtils.buildTrailerUrl(mTrailerList.get(clickedPosition).getKey());
            switch(id)
            {
                case R.id.iv_yt_logo :

                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext,
                            MovieUtils.YOUTUBE_API_KEY,
                            mTrailerList.get(clickedPosition).getKey(),//video id
                            100,     //after this time, video will start automatically
                            true,               //autoplay or not
                            false);             //lightbox mode or not; show the video in a small box
                    mContext.startActivity(intent);
                    break;
                case R.id.fab_share :
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey!!Please have a look" + "    " + youtubeURL + "   " + "-Sent from MovieApp");
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share link using"));
                    break;

            }





        }
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        final Trailer e = mTrailerList.get(position);
        final VideoViewHolder videoViewHolder =  holder;
        videoViewHolder.tvTitle.setText(e.name);
        videoViewHolder.ivYtLogo.setVisibility(View.VISIBLE);
        videoViewHolder.ytThubnailView.setTag(R.id.videoid, e.getKey());
        videoViewHolder.ivYtLogo.setBackgroundColor(blackColor);

        int state = (int) videoViewHolder.ytThubnailView.getTag(R.id.initialize);

        if(state == UNINITIALIZED){
            videoViewHolder.initialize();
        }
        else if(state == INITIALIZED){
            YouTubeThumbnailLoader loader = (YouTubeThumbnailLoader) videoViewHolder.ytThubnailView.getTag(R.id.thumbnailloader);
            loader.setVideo(e.getTrailerId());
        }
    }




    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }






}


