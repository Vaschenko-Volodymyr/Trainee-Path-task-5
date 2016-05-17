package com.traineepath.volodymyrvashchenko.trainee_path_task_5;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnPostExecutable {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mLoadedImage;

    private Button mLoad;
    private ProgressDialog mProgress;

    private LoadImage mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Method: onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadedImage = (ImageView) findViewById(R.id.loaded_image);
        mLoad = (Button) findViewById(R.id.load);
        setLoadOnClickListener();

        mProgress = new ProgressDialog(this);
        mLoader = new LoadImage(MainActivity.this, mProgress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    private void setLoadOnClickListener() {
        Log.v(TAG, "Method: setLoadOnClickListener()");
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLoader.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    mLoader.execute(getResources().getString(R.string.url));
                } else {
                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.successful_text),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        Log.v(TAG, "Method: onPostExecute()");
        if (bitmap != null) {
            mLoadedImage.setImageBitmap(bitmap);
            mProgress.dismiss();
        } else {
            mProgress.dismiss();
            Toast.makeText(this,
                    getResources().getString(R.string.loading_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
