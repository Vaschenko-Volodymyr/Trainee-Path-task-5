package com.traineepath.volodymyrvashchenko.trainee_path_task_5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {

    private static final String TAG = LoadImage.class.getSimpleName();

    private ImageView mImage;
    private Context mContext;
    private int mMaxHeight;
    private int mMaxWidth;
    private boolean mDoneWithoutExceptions = false;

    public LoadImage(ImageView image, Context context, int maxHeight, int maxWidth) {
        mImage = image;
        mContext = context;
        mMaxHeight = maxHeight;
        mMaxWidth = maxWidth;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, ">> Method: onPreExecute()");
        super.onPreExecute();
        Log.v(TAG, "<< Method: onPreExecute()");
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.v(TAG, ">> Method: doInBackground()");
        Bitmap bitmap = decodeSampledBitmapFromUrl(params[0], mMaxHeight, mMaxWidth);
        Log.v(TAG, "<< Method: doInBackground()");
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        Log.v(TAG, ">> Method: onPostExecute()");
        Log.v(TAG, "thread is stopped - " + isCancelled());
        if (isCancelled()) return;
        if (image != null) {
            mImage.setImageBitmap(image);
            MainActivity.dismissDialog();
            if (getLoadedState()) {
                Toast.makeText(mContext,
                        R.string.loading_success,
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(mContext,
                        R.string.loading_error,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        Log.v(TAG, "<< Method: onPostExecute()");
    }

    private boolean getLoadedState() {
        return mDoneWithoutExceptions;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight && width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromUrl(String ulr, int reqHeight, int reqWidth) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream((InputStream) new URL(ulr).getContent());
        } catch (IOException e) {
            // Just ignore exception
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        try {
            mDoneWithoutExceptions = true;
            return BitmapFactory.decodeStream((InputStream) new URL(ulr).getContent(), null, options);
        } catch (IOException e) {
            mDoneWithoutExceptions = false;
            return null;
        }
    }
}