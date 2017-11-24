package com.nippylinks.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nippylinks.android.classes.TransparentProgressDialog;
import com.nippylinks.android.utils.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CreateCustomLinkActivity extends Activity {

    private TextView tv_back;
    private ImageView img_back;

    private TextView tv_not_see;
    String TAG = this.getClass().getSimpleName();

    private int nro_clicks = 0;
    private String path_username = "";
    private String path_password = "";
    private String path_button   = "";

    private String cl_name;
    private String cl_login_url;
    private String cl_username;
    private String cl_password;
    public RelativeLayout relativeLayout;
    public Button layout_button;
    public TextView tv_message;
    public RelativeLayout relativeLayout1;
    public Button layout_button1;
    public RelativeLayout relativeLayout2;
    public Button layout_button2;
    public boolean clickEnabled = false;

    WebView wv;

    AsynkTask asyncTask;

    UserSessionManager session;

    private TransparentProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_link);
        session = new UserSessionManager(getBaseContext());
        progress = new TransparentProgressDialog(this);

        img_back   = (ImageView) findViewById(R.id.img_back);
        tv_back    = (TextView) findViewById(R.id.tv_back);
        tv_not_see = (TextView) findViewById(R.id.tv_not_see);

        relativeLayout  =(RelativeLayout)findViewById(R.id.messageLayout);
        layout_button   =(Button)findViewById(R.id.message_layout_button);
        tv_message      =(TextView)findViewById(R.id.tv_message);
        layout_button1  =(Button)findViewById(R.id.message_layout_button1);
        relativeLayout1 =(RelativeLayout)findViewById(R.id.messageLayout1);
        layout_button2  =(Button)findViewById(R.id.message_layout_button2);
        relativeLayout2 =(RelativeLayout)findViewById(R.id.messageLayout2);

        utils.ChangeFont(tv_back, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_not_see, utils.RELAWAY_MEDIUM_FONT);
        utils.changeButtonBG(layout_button,getResources().getString(R.color.colorCompBtnContinue));
        utils.changeButtonBG(layout_button1,getResources().getString(R.color.colorCompBtnContinue));
        utils.changeButtonBG(layout_button2,getResources().getString(R.color.colorCompBtnContinue));
        progress.show();

        Bundle extras = getIntent().getExtras();
        cl_login_url = extras.getString("url_custom_link");
        cl_name      = extras.getString("name_custom_link");
        cl_username  = extras.getString("username_custom_link");
        cl_password  = extras.getString("password_custom_link");

        wv = (WebView) findViewById(R.id.MyWebView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.clearCache(true);
        wv.addJavascriptInterface(new WebAppInterface(this), "Android");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                injectJS();
                String js = "javascript: var allInputs = document.getElementsByTagName('input'); for (var i = 0, len = allInputs.length; i < len; ++i) { allInputs[i].readOnly = true;}";
                wv.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        LayoutVisibility(0);
                    }
                });
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CreateCustomLinkActivity.this, "URL error", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            private void injectJS() {
                try {
                    InputStream inputStream = getAssets().open("script.js");
                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    inputStream.close();
                    String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    wv.loadUrl("javascript:(function() {" +
                            "var parent = document.getElementsByTagName('head').item(0);" +
                            "var script = document.createElement('script');" +
                            "script.type = 'text/javascript';" +
                            "script.innerHTML = window.atob('" + encoded + "');" +
                            "parent.appendChild(script)" +
                            "})()");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        wv.loadUrl(cl_login_url);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_not_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }


    public void LayoutVisibility(int numClicks){
        clickEnabled = false;
        switch (numClicks) {
            case 1:
                relativeLayout1.setVisibility(View.VISIBLE);
                layout_button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickEnabled = true;
                        relativeLayout1.setVisibility(View.GONE);
                    }
                });
                break;
            case 2:
                relativeLayout2.setVisibility(View.VISIBLE);
                layout_button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickEnabled = true;
                        relativeLayout2.setVisibility(View.GONE);
                    }
                });
                break;
            default:
                layout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickEnabled = true;
                        relativeLayout.setVisibility(View.GONE);
                    }
                });
                relativeLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void getClick(String path) {
            if(clickEnabled){
                nro_clicks++;
                if(nro_clicks == 1){
                    path_username = path.replaceAll("\"", "'");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutVisibility(nro_clicks);
                        }
                    });
                }else if(nro_clicks == 2){
                    path_password = path.replaceAll("\"", "'");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutVisibility(nro_clicks);
                        }
                    });
                }else if(nro_clicks == 3){
                    path_button = path.replaceAll("\"", "'");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveCustomLink();
                        }
                    });
                }
            }
        }
    }

    public void saveCustomLink(){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("name", cl_name);
            paramsObj.put("login_url", cl_login_url);
            paramsObj.put("username_selector", path_username);
            paramsObj.put("password_selector", path_password);
            paramsObj.put("submit_selector", path_button);
            paramsObj.put("username", cl_username);
            paramsObj.put("password", cl_password);

            AsynkTask async = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            utils.goToOtherActivityLeft(CreateCustomLinkActivity.this, DashboardActivity.class);
                        }
                        progress.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            async.execute(utils.API_BASE_URL + utils.API_MY_CUSTOM_LINKS_URL, utils.TYPE_PUT, utils.constructStringParams(paramsObj), session.getSessionToken());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Info");
        alertDialog.setMessage("Please logout in this site before adding it");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
