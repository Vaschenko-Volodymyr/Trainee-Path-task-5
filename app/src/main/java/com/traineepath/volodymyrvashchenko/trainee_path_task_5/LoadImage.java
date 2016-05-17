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

    public LoadImage(MainActivity activity, ProgressDialog progressDialog) {
        mActivity = activity;
        mProgress = progressDialog;
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
        Bitmap mBitmap = null;
        try {
            mBitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
        } catch (IOException e) {
            // Just ignore exception
        }
        return mBitmap;
    }

    public void onPostExecute(Bitmap image) {
        mActivity.onPostExecute(image);
    }
}