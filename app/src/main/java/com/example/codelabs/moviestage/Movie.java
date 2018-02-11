package com.example.codelabs.moviestage;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.codelabs.moviestage.Utils.MovieUtils;

import java.util.List;

/**
 * Created by varshaa on 23-11-2017.
 */

public class Movie implements Parcelable {

    //Parcel creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mImageUrl;
    private String mOriginalTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mId;
    private String mTrailerUrl;
    private String mReviewUrl;
    private int mByteArraySize;
    //private boolean offlineFlag;

    public byte[] getmRawImage() {
        return mRawImage;
    }

    private byte[] mRawImage;
    private List<Trailer> mTrailer;
    private List<Review> mReview;

    //public boolean isOfflineFlag() {
        //return offlineFlag;
    //}

    public Movie(String mImageUrl, String mOriginalTitle, String mOverview, String mReleaseDate, String mVoteAverage, String mId, int mByteArraySize, byte[] mRawImage /*boolean offlineFlag*/) {
        this.mImageUrl = mImageUrl;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
        this.mId = mId;
        this.mByteArraySize = mByteArraySize;
        this.mRawImage = mRawImage;
        //this.offlineFlag = offlineFlag;



    }

    public List<Trailer> getmTrailer() {
        return mTrailer;
    }
    public List<Review> getmReview() {
        return mReview;
    }
    public String getmId() {
        return mId;
    }
    public String getmTrailerUrl() {
        return MovieUtils.movieBuildUrl(MovieUtils.VIDEO, mId);
    }
    public String getmReviewUrl() {
        return MovieUtils.movieBuildUrl(MovieUtils.REVIEWS, mId);
    }







    // De-parcel object
    public Movie(Parcel in) {
        mImageUrl = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readString();
        mId = in.readString();
        mTrailerUrl = in.readString();
        mReviewUrl = in.readString();
        mByteArraySize = in.readInt();
        mRawImage = new byte[mByteArraySize];
        in.readByteArray(mRawImage);

    }

    public String getmImageUrl() {
        return MovieUtils.buildImageUrl(mImageUrl);
    }

    public String getmOriginalTitle() {
        if (mOriginalTitle != null) {
            return mOriginalTitle;
        } else {
            return "Title Not Found";
        }


    }

    public String getmOverview() {


        if (mOverview != null) {
            return mOverview;
        } else {
            return "Overview Not Found";
        }
    }

    public String getmReleaseDate() {
        if (mReleaseDate != null) {
            if(StarterActivity.offlinePref)
            {
                return mReleaseDate;
            }
            else {
                return MovieUtils.formatDate(mReleaseDate);
            }
        }
        else
            return "NA";
    }

    public String getmVoteAverage() {
        if (mVoteAverage != null) {
            if(StarterActivity.offlinePref) {
                return mVoteAverage;
            } else {
                return mVoteAverage + "/10";
            }
        }
        else
            return "NA";
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageUrl);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
        dest.writeString(mId);
        dest.writeString(mTrailerUrl);
        dest.writeString(mReviewUrl);
        if(StarterActivity.offlinePref || getmRawImage() != null) {
       dest.writeInt(mRawImage.length);
         dest.writeByteArray(mRawImage);
      }
   else
        {
            dest.writeInt(13);
            byte[] k =  {69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98};
            mRawImage = k;
            dest.writeByteArray(mRawImage);
        }


    }
}
