package  com.example.codelabs.moviestage;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.MovieUtils;
import com.example.codelabs.moviestage.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapterOffline extends CursorAdapter {
    //private GridItemClickListener mListener;


    public MovieAdapterOffline(Context context, Cursor c ) {
        super(context, c, 0 /* flags */);
//
   }



    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //return null;
        return LayoutInflater.from(context).inflate(R.layout.list_item_offline, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView posterImage = (ImageView) view.findViewById(R.id.iv_poster_offline);
        //TextView lentOnTextView = (TextView) view.findViewById(R.id.lent_on);


//        int nameColumnIndex = cursor.getColumnIndex(LendEntry.COLUMN_NAME);
//        int lentToColumnIndex = cursor.getColumnIndex(LendEntry.COLUMN_LENT_TO);
        int posterImageColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);

        // Read the Lend attributes from the Cursor for the current Lend
//        String LendName  = cursor.getString(nameColumnIndex);
//        String LentTo = cursor.getString(lentToColumnIndex);
//        String LentOn = cursor.getString(lentOnColumnIndex);
        byte[] byteArray  = cursor.getBlob(posterImageColumnIndex);
        Log.d("govi",Integer.toString(byteArray.length));
//        for(int i =0 ; i < byteArray.length ; i++)
//        {
//            Log.d("blobby",Byte.toString(byteArray[i]));
//        }

        // Update the TextViews with the attributes for the current Lend
        Bitmap posterBitmap = MovieUtils.convertByteArrayToBitmap(context,byteArray);
        Log.d("bittumappu",posterBitmap.toString());
       if(posterImage != null)
          posterImage.setImageBitmap(posterBitmap);
        else
           Log.d("posterimage","nullama");
    }




}





