package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.utils.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CustomLinksActivity extends Activity {

    private TextView tv_back;
    private ImageView img_back;
    private TextView tv_custom_links;
    private EditText et_name;
    private EditText et_url;
    private EditText et_userName;
    private EditText et_password;
    private Button btn_continue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_links);

        img_back            = (ImageView) findViewById(R.id.img_back);
        tv_back             = (TextView) findViewById(R.id.tv_back);
        tv_custom_links     = (TextView) findViewById(R.id.tv_custom_links);
        et_name             = (EditText) findViewById(R.id.et_name);
        et_url              = (EditText) findViewById(R.id.et_url);
        et_userName         = (EditText) findViewById(R.id.et_username);
        et_password         = (EditText) findViewById(R.id.et_password);
        btn_continue        = (Button) findViewById(R.id.btn_continue);

        utils.ChangeFont(tv_back,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_custom_links,utils.RELAWAY_MEDIUM_FONT);
        utils.ChangeFont(et_name,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(et_url,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(et_userName,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(et_password,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(btn_continue,utils.RELAWAY_REGULAR_FONT);
        et_url.setText("http://");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(CustomLinksActivity.this,LinksActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(CustomLinksActivity.this,LinksActivity.class);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Intent intent = new Intent(CustomLinksActivity.this, CreateCustomLinkActivity.class);
                    intent.putExtra("name_custom_link", et_name.getText().toString());
                    intent.putExtra("url_custom_link", addSecurityToURL(et_url.getText().toString()));
                    intent.putExtra("username_custom_link", et_userName.getText().toString());
                    intent.putExtra("password_custom_link", et_password.getText().toString());
                    startActivity(intent);
                }
            }
        });

        utils.changeButtonBG(btn_continue,getResources().getString(R.color.colorCompBtnContinue));
    }

    public boolean validate(){
        boolean valid = true;

        String name      = et_name.getText().toString();
        String url       = et_url.getText().toString();
        String username  = et_userName.getText().toString();
        String password  = et_password.getText().toString();

        if(name.isEmpty()){
            et_name.setError("Name is missing");
            valid = false;
        }else{
            et_name.setError(null);
        }

        if(url.isEmpty() || !utils.isValidUrl(url)){
            et_url.setError("Enter a valid url address");
            valid = false;
        }else{
            et_url.setError(null);
        }

        if(username.isEmpty()){
            et_userName.setError("Username is missing");
            valid = false;
        }else{
            et_userName.setError(null);
        }

        if(password.isEmpty()){
            et_password.setError("Password is missing");
            valid = false;
        }else{
            et_password.setError(null);
        }

        return valid;
    }

    public String addSecurityToURL(String url){
        String modifiedURLString = "";
        try{
            if (!url.startsWith("https://") && !url.startsWith("http://")){
                modifiedURLString = "https://" + url;
            }else{
                modifiedURLString = url;
            }
            AsyncCaller as = new AsyncCaller();
            String res = as.execute(modifiedURLString).get();

            if(res.equals("0")) {
                String[] parts = modifiedURLString.split("https://");
                modifiedURLString =  "http://" + parts[1];

            }
        }catch (Exception e){}

        return modifiedURLString;
    }

    public void goToOtherACtivity(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}

class AsyncCaller extends AsyncTask<String, Void, String>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            URL myUrl = new URL(params[0]);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(100);
            connection.connect();
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}