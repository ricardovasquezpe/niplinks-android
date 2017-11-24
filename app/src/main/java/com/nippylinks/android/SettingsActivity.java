package com.nippylinks.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nippylinks.android.utils.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class SettingsActivity extends Activity {

    private TextView tv_back;
    private TextView tv_settings;
    private TextView tv_username;
    private TextView tv_email;
    private ImageView img_back;
    private TextView tv_info;
    private TextView tv_reset;
    private TextView tv_username_header;
    private TextView tv_password_header;

    private Button btn_reset;

    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new UserSessionManager(getBaseContext());

        img_back    = (ImageView) findViewById(R.id.img_back);
        tv_back     = (TextView) findViewById(R.id.tv_back);
        tv_settings = (TextView) findViewById(R.id.tv_settings);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_email    = (TextView) findViewById(R.id.tv_email);
        btn_reset   = (Button) findViewById(R.id.btn_reset);
        tv_info     = (TextView) findViewById(R.id.tv_info);
        tv_reset    = (TextView) findViewById(R.id.tv_reset);
        tv_username_header = (TextView) findViewById(R.id.tv_username_header);
        tv_password_header = (TextView) findViewById(R.id.tv_password_header);

        utils.ChangeFont(tv_settings,utils.RELAWAY_MEDIUM_FONT);
        utils.ChangeFont(tv_back,utils.RELAWAY_LIGHT_FONT);
        utils.ChangeFont(tv_info,utils.RELAWAY_SEMIBOLD_FONT);
        utils.ChangeFont(tv_reset,utils.RELAWAY_SEMIBOLD_FONT);
        utils.ChangeFont(btn_reset,utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            goToOtherACtivity(SettingsActivity.this, DashboardActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            goToOtherACtivity(SettingsActivity.this, DashboardActivity.class);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        getBasicInfo();
    }

    public void getBasicInfo(){
        AsynkTask sec = new AsynkTask(this) {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONObject dataObj = responseObj.getJSONObject("data");

                    tv_username.setText(dataObj.getString("username"));
                    tv_email.setText((dataObj.getString(("email"))));
                    progress.dismiss();

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        sec.execute(utils.API_BASE_URL + utils.API_MY_USER, utils.TYPE_GET, null, session.getSessionToken());
    }


    public void resetPassword(){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("email", tv_email.getText());

            AsynkTask sec = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            session.logoutUser();
                            utils.goToOtheActivityFinish(SettingsActivity.this,LoginActivity.class);
                        }

                        progress.dismiss();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };

            sec.execute(utils.API_BASE_URL + utils.API_MY_PASS, utils.TYPE_POST, utils.constructStringParams(paramsObj), session.getSessionToken());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void goToOtherACtivity(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
