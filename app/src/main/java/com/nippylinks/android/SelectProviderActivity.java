package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nippylinks.android.utils.utils;

import org.json.JSONObject;

public class SelectProviderActivity extends Activity {

    private ImageView img_back;
    private TextView tv_back;
    private TextView tv_lets;
    private EditText et_email;
    private EditText et_password;
    private Button btn_continue;

    private RelativeLayout rl_select_provider;

    private int idSelectProvider;

    AsynkTask asyncTask;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_provider);
        session = new UserSessionManager(getBaseContext());

        img_back     = (ImageView) findViewById(R.id.img_back);
        tv_back      = (TextView)  findViewById(R.id.tv_back);
        tv_lets      = (TextView)  findViewById(R.id.tv_lets);
        et_email     = (EditText)  findViewById(R.id.et_email);
        et_password  = (EditText)  findViewById(R.id.et_password);
        btn_continue = (Button)    findViewById(R.id.btn_continue);

        rl_select_provider = (RelativeLayout) findViewById(R.id.rl_select_provider);

        utils.ChangeFont(tv_back,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_lets,utils.RELAWAY_SEMIBOLD_FONT);
        utils.ChangeFont(et_email,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(et_password,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(btn_continue,utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOtherACtivity(SelectProviderActivity.this, ProvidersActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOtherACtivity(SelectProviderActivity.this, ProvidersActivity.class);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProviderToMyList();
            }
        });

        Bundle extras = getIntent().getExtras();
        rl_select_provider.setBackgroundColor(Color.parseColor("#"+extras.getString("color_prov_select")));
        tv_lets.setText(extras.getString("name_prov_select"));
        idSelectProvider = extras.getInt("id_prov_select");
    }

    public void addProviderToMyList(){
        if(validate()){
            try {
                final String email    = et_email.getText().toString();
                final String password = et_password.getText().toString();
                JSONObject paramsObj = new JSONObject();
                paramsObj.put("provider_id", idSelectProvider);

                AsynkTask async = new AsynkTask(this) {
                    @Override
                    public void onPostExecute(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            if(Boolean.parseBoolean(responseObj.get("status").toString())){
                                JSONObject paramsObj = new JSONObject();
                                paramsObj.put("username", email);
                                paramsObj.put("password", password);
                                paramsObj.put("provider_id", idSelectProvider);

                                asyncTask = new AsynkTask(this.currentActivity);
                                response = asyncTask.execute(utils.API_BASE_URL + utils.API_PROVIDERS_URL, utils.TYPE_POST, utils.constructStringParams(paramsObj), session.getSessionToken()).get();
                                responseObj = new JSONObject(response);
                                if(Boolean.parseBoolean(responseObj.get("status").toString())){
                                    goToOtherACtivity(SelectProviderActivity.this, DashboardActivity.class);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                async.execute(utils.API_BASE_URL + utils.API_PROVIDERS_URL, utils.TYPE_PUT, utils.constructStringParams(paramsObj), session.getSessionToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validate(){
        boolean valid = true;

        String email     = et_email.getText().toString();
        String password  = et_password.getText().toString();

        if(email.isEmpty()){
            et_email.setError("Invalid email Address");
            valid = false;
        }else{
            et_email.setError(null);
        }

        if(password.isEmpty()){
            et_password.setError("Password is missing");
            valid = false;
        }else{
            et_password.setError(null);
        }

        return valid;
    }

    public void goToOtherACtivity(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
