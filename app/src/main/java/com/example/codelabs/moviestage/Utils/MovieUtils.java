package com.example.codelabs.moviestage.Utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.codelabs.moviestage.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by varshaa on 23-11-2017.
 */

public class MovieUtils {
    private static  final String CLASS_TAG = MovieUtils.class.getSimpleName();
    private static final String API_KEY = "23edb5946ec7fbcc0ac6f1d515ac6e71";
    final static String DISCOVER_BASE_URL="https://api.themoviedb.org/3/discover/movie";
    final static String PARAM_SORTBY = "sort_by";
    final static String PARAM_APIKEY="api_key";
    final static String IMAGE_BASE_URL="http://image.tmdb.org/t/p/";
    final static String POSTER_SIZE="w185";

    public static String buildUrl(String sortby) {
        Uri builtUri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SORTBY, sortby)
                .appendQueryParameter(PARAM_APIKEY, API_KEY)
                .build();

        String url = builtUri.toString();


        return url;
    }

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

    public static List<Movie> parseJson(String movieJSON)
    {
        if(TextUtils.isEmpty(movieJSON))
        {
            return  null;
        }

        List<Movie> movieList = new ArrayList<>();
        try{
            JSONObject jsonResponse = new JSONObject(movieJSON);
            JSONArray resultArray = jsonResponse.getJSONArray("results");
            for(int i=0;i<resultArray.length();i++){
                JSONObject currentResult = resultArray.getJSONObject(i);
                String currentImageUrl = currentResult.getString("poster_path");
                String currentOriginalTitle = currentResult.getString("original_title");
                String currentOverview = currentResult.getString("overview");
                String currentReleaseDate = currentResult.getString("release_date");
                String currentVoterRating = currentResult.getString("vote_average");
                Movie movieObject = new Movie(currentImageUrl,currentOriginalTitle,currentOverview,currentReleaseDate,currentVoterRating);
                movieList.add(movieObject);
            }



        }

        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
      return movieList;
    }

    public static  String buildImageUrl(String relativeUrl){
        String subStringUrl = relativeUrl.substring(1);
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendPath(POSTER_SIZE).appendPath(subStringUrl).build();
        String url = builtUri.toString();
        return url;



    }
    public static String formatDate(String dateString)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        return formattedDate;
    }



}
