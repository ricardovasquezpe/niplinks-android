package com.nippylinks.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import com.nippylinks.android.classes.TransparentProgressDialog;
import com.nippylinks.android.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevincampo on 17/03/17.
 */

public class AsynkTask extends AsyncTask<String,Void,String> { 
    public TransparentProgressDialog progress;
    public Activity currentActivity;

    public AsynkTask(Activity activity) {
        progress = new TransparentProgressDialog(activity);
        currentActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        progress.show();
    }

    protected String doInBackground(String... url) {
        try {
            return utils.connectToServer(url[0], url[1], url[2], url[3]);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}