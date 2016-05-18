package com.traineepath.volodymyrvashchenko.trainee_path_task_5;

import android.app.ProgressDialog;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String LOGGED_STATE = "logged";
    private static final String LOAD_BIG_IMAGE = "big";

    private static ProgressDialog sProgressDialog;

    private ImageView mLoadedImage;
    private Button mLoadButton;
    private CheckBox mImageSizeCheckbox;
    private LoadImage mLoaderTask;

    private boolean mIsFirstTimeLogged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, ">> Method: onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadedImage = (ImageView) findViewById(R.id.loaded_image);
        mLoadButton = (Button) findViewById(R.id.load);
        setLoadOnClickListener();

        sProgressDialog = new ProgressDialog(this);
        mImageSizeCheckbox = (CheckBox) findViewById(R.id.load_big);
        setLoadBigImageClickListener();

        if (savedInstanceState != null) {
            mIsFirstTimeLogged = savedInstanceState.getBoolean(LOGGED_STATE);
            mImageSizeCheckbox.setChecked(savedInstanceState.getBoolean(LOAD_BIG_IMAGE));
        }

        Log.v(TAG, "<< Method: onCreate()");
    }

    @Override
    protected void onResume() {
        Log.v(TAG, ">> Method: onResume()");
        super.onResume();
        createLoader();
        if (!mIsFirstTimeLogged) {
            Log.v(TAG, "Method: onResume(), Invoke onClick on button");
            mLoadButton.callOnClick();
        }
        Log.v(TAG, "<< Method: onResume()");
    }

    @Override
    protected void onPause() {
        Log.v(TAG, ">> Method: onPause()");
        super.onPause();
        if (sProgressDialog.isShowing()) {
            dismissDialog();
        }
        mLoaderTask.cancel(true);
        Log.v(TAG, "Method: onPause(), thread is canceled - " + mLoaderTask.isCancelled());
        Log.v(TAG, "<< Method: onPause()");
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(LOGGED_STATE, mIsFirstTimeLogged);
        savedInstanceState.putBoolean(LOAD_BIG_IMAGE, mImageSizeCheckbox.isChecked());
    }

    public static void dismissDialog() {
        sProgressDialog.dismiss();
    }

    private void createLoader() {
        mLoaderTask = new LoadImage(mLoadedImage,
                getApplicationContext(),
                mLoadedImage.getHeight(),
                mImageSizeCheckbox.getWidth());
    }

    private void setLoadBigImageClickListener() {
        mImageSizeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                createLoader();
                mLoadedImage.setImageBitmap(null);
            }
        });
    }

    private void setLoadOnClickListener() {
        Log.v(TAG, ">> Method: setLoadOnClickListener()");
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFirstTimeLogged = false;
                sProgressDialog.setMessage(getResources().getString(R.string.progress_message));
                sProgressDialog.show();
                String url = mImageSizeCheckbox.isChecked() ?
                        getResources().getString(R.string.big_image_url) :
                        getResources().getString(R.string.small_image_url);
                try {
                    if (!mLoaderTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                        Log.v(TAG, "Loading image was not finished, start loading..");
                        mLoaderTask.execute(url);
                    } else {
                        onLoadCompletedToast();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this,
                            R.string.retry,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        Log.v(TAG, "<< Method: setLoadOnClickListener()");
    }

    private void onLoadCompletedToast() {
        Log.v(TAG, "Loading image was finished, don't need to load again");
        sProgressDialog.dismiss();
        Toast.makeText(MainActivity.this,
                R.string.already_loaded,
                Toast.LENGTH_SHORT)
                .show();
    }
}
