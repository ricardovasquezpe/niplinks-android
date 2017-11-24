package com.nippylinks.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.classes.TransparentProgressDialog;
import com.nippylinks.android.utils.utils;

import java.io.InputStream;

public class LoginLinkActivity extends Activity {

    private TextView tv_back;
    private ImageView img_back;

    WebView wv;
    AsynkTask asyncTask;
    UserSessionManager session;
    TransparentProgressDialog progress;

    String url;
    String username;
    String password;
    String username_field;
    String password_field;
    String button_field;
    String form_field;

    boolean loadPage = true;

    String samePage = "";
    int pageLoadsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_link);

        session = new UserSessionManager(getBaseContext());
        progress = new TransparentProgressDialog(this);

        Bundle extras = getIntent().getExtras();
        url            = extras.getString("url_select");
        username       = extras.getString("username_select");
        password       = extras.getString("password_select");
        username_field = extras.getString("us_field_select");
        password_field = extras.getString("pass_field_select");
        button_field   = extras.getString("btn_field_select");
        form_field     = extras.getString("form_field_select");

        img_back = (ImageView) findViewById(R.id.img_back);
        tv_back  = (TextView) findViewById(R.id.tv_back);
        utils.ChangeFont(tv_back, utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(LoginLinkActivity.this, DashboardActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(LoginLinkActivity.this, DashboardActivity.class);
            }
        });

        progress.show();

        wv = (WebView) findViewById(R.id.MyWebViewLogin);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.clearCache(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setInitialScale(1);
        wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        wv.addJavascriptInterface(new WebAppInterface(this), "Android");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(wv.getProgress() == 100 && loadPage){
                    loadPage = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            injectJS();
                        }
                    }, 100);
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                }
            }

            private void injectJS() {
                try {
                    InputStream inputStream = getAssets().open("scriptlogin.js");
                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    inputStream.close();
                    String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    String init = "javascript: "  +
                                    " var username  = '"+username+"'; " +
                                    "var password   = '"+password+"'; " +
                                    "var us_field   =  \""+username_field+"\"; " +
                                    "var pass_field = \"" + password_field + "\"; " +
                                    "var btn_field  = \"" + button_field + "\"; " +
                                    "var form_field = \"" + form_field + "\"; " +
                                    "(function() {" +
                                    "var parent = document.getElementsByTagName('head').item(0);" +
                                    "var script = document.createElement('script');" +
                                    "script.type = 'text/javascript';" +
                                    "script.innerHTML = window.atob('" + encoded + "');" +
                                    "parent.appendChild(script)" +
                                    "})()";
                    wv.loadUrl(init);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        wv.loadUrl(url);
    }

    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void someError(String error) {
            onBackPressed();
        }
    }
}
