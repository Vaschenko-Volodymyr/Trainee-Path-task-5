package com.traineepath.volodymyrvashchenko.trainee_path_task_5;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {

    private static final String TAG = LoadImage.class.getSimpleName();

    private ProgressDialog mProgress;
    private MainActivity mActivity;
    private int mMaxHeight;
    private int mMaxWidth;

    private boolean mDoneWithoutExceptions = false;

    public LoadImage(MainActivity activity, ProgressDialog progressDialog, int maxHeight, int maxWidth) {
        mActivity = activity;
        mProgress = progressDialog;
        mMaxHeight = maxHeight;
        mMaxWidth = maxWidth;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "Method: onPreExecute()");
        super.onPreExecute();
        mProgress.setMessage(mActivity.getResources().getString(R.string.progress_message));
        mProgress.show();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.v(TAG, "Method: doInBackground()");
        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            bitmap = BitmapFactory.decodeStream(
                    (InputStream) new URL(params[0]).getContent(), null, options);
            if (options.outHeight * options.outWidth >= mMaxHeight * mMaxWidth) {
                Log.v(TAG, "Image is too big");
                return decodeSampledBitmapFromUrl(params[0], mMaxHeight, mMaxWidth);
            } else {
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
            }
            mDoneWithoutExceptions = true;
        } catch (IOException e) {
            mDoneWithoutExceptions = false;
        }
        return bitmap;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

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
            return BitmapFactory.decodeStream((InputStream) new URL(ulr).getContent());
        } catch (IOException e) {
            return null;
        }
    }

    public void onPostExecute(Bitmap image) {
        mActivity.onPostExecute(image);
    }

    public boolean getLoadedState() {
        return mDoneWithoutExceptions;
    }
}