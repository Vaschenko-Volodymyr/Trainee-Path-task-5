package com.traineepath.volodymyrvashchenko.trainee_path_task_5;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnPostExecutable {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mLoadedImage;
    private Button mLoad;
    private ProgressDialog mProgress;
    private CheckBox mLoadBigImage;
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
        mLoadBigImage = (CheckBox) findViewById(R.id.load_big);
        setLoadBigImageClickListener();

        mLoader = new LoadImage(MainActivity.this,
                mProgress,
                mLoadedImage.getHeight(),
                mLoadBigImage.getWidth());
    }

    private void setLoadBigImageClickListener() {
        mLoadBigImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLoadedImage.setImageBitmap(null);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "Method: onResume()");
        super.onResume();
        if (mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "Method: onPause()");
        super.onPause();
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        mLoader.cancel(true);
        Log.v(TAG, "AsyncTask is canceled");
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "Method: onDestroy()");
        super.onDestroy();
    }

    private void setLoadOnClickListener() {
        Log.v(TAG, "Method: setLoadOnClickListener()");
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mLoadBigImage.isChecked() ?
                        getResources().getString(R.string.big_image_url) :
                        getResources().getString(R.string.small_image_url);
                try {
                        if (!mLoader.getStatus().equals(AsyncTask.Status.FINISHED)) {
                            Log.v(TAG, "Loading image was not finished, start loading..");
                            mLoader.execute(url);
                        } else {
                            onLoadCompletedToast();
                        }
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.retry),
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
            if (mLoader.getLoadedState()) {
                Toast.makeText(this,
                        getResources().getString(R.string.too_big),
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.loading_error),
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void onLoadCompletedToast() {
        Log.v(TAG, "Loading image was finished, don't need to load again");
        Toast.makeText(MainActivity.this,
                getResources().getString(R.string.successful_text),
                Toast.LENGTH_SHORT)
                .show();
    }
}
