package com.example.codelabs.moviestage.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.codelabs.moviestage.MainActivity;
import com.example.codelabs.moviestage.MainActivityOffline;
import com.example.codelabs.moviestage.Movie;
import com.example.codelabs.moviestage.R;
import com.example.codelabs.moviestage.Review;
import com.example.codelabs.moviestage.Trailer;
import com.example.codelabs.moviestage.data.MovieContract;
import com.example.codelabs.moviestage.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by varshaa on 23-11-2017.
 */

//Utility class which performs actions like building URl, connecting and fetching data etc.
public class MovieUtils {
    private final static String DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String PARAM_SORTBY = "sort_by";
    private final static String PARAM_APIKEY = "api_key";
    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String POSTER_SIZE = "w185";
    public final static String VIDEO = "videos";
    public final static String REVIEWS = "reviews";
    public final static String MAIN = "main";
    public final static String id = "211672";
    public final static  String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    public final static  String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi";
    private final static String PARAM_V = "v";




    public static final String YOUTUBE_API_KEY = "AIzaSyDth1QRWz6K66BFjp_LHFG_7JZrIqjgbzI";

    private static final String API_KEY = "23edb5946ec7fbcc0ac6f1d515ac6e71";

    //Builds the URL to be hit from the base url by dynamically appending parameters
    public static String discoverBuildUrl(String sortby) {
        Uri builtUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SORTBY, sortby)
                .appendQueryParameter(PARAM_APIKEY, API_KEY)
                .build();
        return builtUri.toString();
    }
    public static String movieBuildUrl(String subject,String id) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(subject)
                .appendQueryParameter(PARAM_APIKEY, API_KEY)
                .build();
        return builtUri.toString();
    }


    //Hits the appropriate endpoint and fetches the JSON data as string
    public static String getResponseFromHttpUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Converts the raw string to JSON and parses the same. Returns a list of movie objects built by parsing the JSON
    public static List<Movie> parseJson(String movieJSON) {
        List<Trailer> currentTrailerList = new ArrayList<>();
        List<Review> currentReviewList = new ArrayList<>();
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(movieJSON);
            JSONArray resultArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentResult = resultArray.getJSONObject(i);
                String currentMovieId = currentResult.getString("id");
                //String currentTrailerUrl = movieBuildUrl(VIDEO,currentMovieId);
//                try {
//                    currentTrailerList = parseTrailerJson(trailerUrl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //String currentReviewUrl = movieBuildUrl(REVIEWS,currentMovieId);
//                try {
//                   currentReviewList = parseReviewJson(reviewUrl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                String currentImageUrl = currentResult.getString("poster_path");
                String currentOriginalTitle = currentResult.getString("original_title");
                String currentOverview = currentResult.getString("overview");
                String currentReleaseDate = currentResult.getString("release_date");
                String currentVoterRating = currentResult.getString("vote_average");
                Movie movieObject = new Movie(currentImageUrl, currentOriginalTitle, currentOverview, currentReleaseDate, currentVoterRating,currentMovieId,0,null);
                movieList.add(movieObject);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public static List<Trailer> parseTrailerJson(String trailerJson) throws IOException {
        if (TextUtils.isEmpty(trailerJson)) {
            return null;
        }
        List<Trailer> trailerList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(trailerJson);
            JSONArray trailerResultArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < trailerResultArray.length(); i++) {
                JSONObject currentResult = trailerResultArray.getJSONObject(i);
                String currentId = currentResult.getString("id");
                String currentKey = currentResult.getString("key");
                String currentName = currentResult.getString("name");
                Trailer trailerObject = new Trailer(currentId, currentKey, currentName);
                trailerList.add(trailerObject);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    public static List<Review> parseReviewJson(String reviewJson) throws IOException {
        if (TextUtils.isEmpty(reviewJson)) {
            return null;
        }
        List<Review> reviewList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(reviewJson);
            JSONArray reviewResultArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < reviewResultArray.length(); i++) {
                JSONObject currentResult = reviewResultArray.getJSONObject(i);
                String currentId = currentResult.getString("id");
                String currentAuthor = currentResult.getString("author");
                String currentContent = currentResult.getString("content");
                Review reviewObject = new Review(currentId, currentAuthor, currentContent);
                reviewList.add(reviewObject);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    //Utility function for buling the url for the movie poster
    public static String buildImageUrl(String relativeUrl) {
        String subStringUrl = relativeUrl.substring(1);
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendPath(POSTER_SIZE).appendPath(subStringUrl).build();
        return builtUri.toString();
    }

    public static String buildTrailerUrl(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon().appendQueryParameter(PARAM_V, key)
                .build();
        return builtUri.toString();
    }

    public static String buildThumbnailUrl(String thumbNailId) {
        Uri builtUri = Uri.parse(YOUTUBE_THUMBNAIL_URL).buildUpon().appendPath(thumbNailId).appendPath("0.jpg")
                .build();
        return builtUri.toString();
    }

    //Utility function for formating date
    public static String formatDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd-MM-yyyy",Locale.US).format(date);
    }

    public static Bitmap convertByteArrayToBitmap(Context ctx,byte[] bytes) {
        Bitmap bitmapImage;
        if(bytes != null)
        {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
            bitmapImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            bitmapImage.compress(Bitmap.CompressFormat.JPEG,50/100,byteArrayOutputStream);
        }
        else
            bitmapImage = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.art_themoviedb);
        return bitmapImage;
    }
  public static List<Movie> offlineResults(Context context) {
      MovieDbHelper mMovieDbHelper = new MovieDbHelper(context);
      SQLiteDatabase dba;
      dba = mMovieDbHelper.getReadableDatabase();
      List<Movie> mMovieListOffline = new ArrayList<>();
      String[] selectionColumn = {MovieContract.MovieEntry._ID,
              MovieContract.MovieEntry.COLUMN_TITLE,
              MovieContract.MovieEntry.COLUMN_POSTER,
              MovieContract.MovieEntry.COLUMN_RELEASEDATE,
              MovieContract.MovieEntry.COLUMN_RATING,
              MovieContract.MovieEntry.COLUMN_OVERVIEW,
              MovieContract.MovieEntry.COLUMN_STATUS};
      //String selectionClause = MovieContract.MovieEntry.COLUMN_TITLE +  "= ?";
      //String[] selectionArgs = {mMovieObject.getmOriginalTitle()};

      //String[] id = new String[100];
      //String rating = "";
      Cursor data = dba.query(MovieContract.MovieEntry.TABLE_NAME, selectionColumn, null, null, null, null, null);

              //Uri uriD = MovieContract.MovieEntry.CONTENT_URI;
              //uriD = uriD.buildUpon().appendPath(id[i]).build();
              //int del = getContentResolver().delete(uriD,null,null)
              int originalTitleColumnIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
      Log.d("lalalaaa", Integer.toString(originalTitleColumnIndex));
              int overviewColumnIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
              int releaseDateColumnIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASEDATE);
              int voteAverage = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
              int posterImageColumnIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
              if(data != null && data.getCount() >0) {
                  while (data.moveToNext()) {
                      Log.d("sadagopa", "ipo whileaamae");

              //Log.d("kuresaa", currentTitle);
                String currentTitle = data.getString(originalTitleColumnIndex);
                String currentOverview = data.getString(overviewColumnIndex);
                String currentReleaseDate = data.getString(releaseDateColumnIndex);
                String currentVoterAverage = data.getString(voteAverage);
                byte[] currentColumnPoster = data.getBlob(posterImageColumnIndex);

                 //String currentTitle = "data.getString";
                Movie currentMovie = new Movie(null,currentTitle,currentOverview, currentReleaseDate,
                                           currentVoterAverage, null,byteArraySize(currentColumnPoster), currentColumnPoster);
                mMovieListOffline.add(currentMovie);


//
//
                  }
                  Log.d("column", Integer.toString(originalTitleColumnIndex));
              }
              else
              {
                  Log.d("sadagopa", "errrorrame");
              }

             return mMovieListOffline;
          }

          public static int byteArraySize(byte[] inputByteArray)
          {
              int mByteArraySize;
              if(inputByteArray != null) {
                  mByteArraySize = inputByteArray.length;
              }
              else
                  mByteArraySize = 0 ;
              return mByteArraySize;
          }
          public static void deletePreferances(Context context)
          {

          }

      }




