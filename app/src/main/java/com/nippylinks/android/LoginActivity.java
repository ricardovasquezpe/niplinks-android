package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;

import com.nippylinks.android.utils.utils;
import android.view.View;
import android.widget.Toast;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    private EditText input_email;
    private EditText input_password;
    private Button   btn_login;
    private Button   btn_signup;
    private Drawable drw_error;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getBaseContext());
        if(session.isUserLoggedIn()){
            //goToOtherActivityFinish(LoginActivity.this, DashboardActivity.class);
            utils.goToOtheActivityFinish(LoginActivity.this,DashboardActivity.class);
        }
        input_email    = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login      = (Button)   findViewById(R.id.btn_login);
        //btn_signup     = (Button)   findViewById(R.id.btn_signup);
        int round = 15;
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(new float[]{round,round,round,round,round,round,round,round},null,null));
        sd.setTint(ContextCompat.getColor(getBaseContext(),R.color.colorCompBtnLogin));
        btn_login.setBackground(sd);

        ShapeDrawable sd1 = new ShapeDrawable(new RoundRectShape(new float[]{round,round,round,round,round,round,round,round},null,null));
        sd1.setTint(ContextCompat.getColor(getBaseContext(),R.color.colorCompBtnSignUp));
        //btn_signup.setBackground(sd1);
        //ic_error=(Drawable)

        utils.ChangeFont(btn_login, utils.RELAWAY_REGULAR_FONT);
        //utils.ChangeFont(btn_signup, utils.RELAWAY_REGULAR_FONT);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        /*btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityLeft(LoginActivity.this,SignUpEmailActivity.class);
                //goToOtherActivity(LoginActivity.this, SignUpEmailActivity.class);
            }
        });*/

        drw_error = getResources().getDrawable(R.mipmap.ic_error_lightblue1);
        drw_error.setBounds(0, 0, drw_error.getIntrinsicWidth()/2, drw_error.getIntrinsicHeight()/2);
    }

    public void login(){
        if(this.validate()){
            try {
                final String email     = input_email.getText().toString();
                final String password  = input_password.getText().toString();
                JSONObject paramsObj = new JSONObject();
                paramsObj.put("email", email);
                paramsObj.put("password", password);

                AsynkTask async = new AsynkTask(this) {
                    @Override
                    public void onPostExecute(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);

                            if (Boolean.parseBoolean(responseObj.get("status").toString())) {
                                session.createUserLoginSession(email, password, responseObj.get("access_token").toString());
                                //goToOtherActivityFinishAllPrevious(LoginActivity.this, DashboardActivity.class);
                                utils.goToOtherActivityFinishAllPrevious(LoginActivity.this, DashboardActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect Login", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                };
                async.execute(utils.API_BASE_URL + utils.API_LOGIN_URL, utils.TYPE_POST, utils.constructStringParams(paramsObj), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validate(){
        boolean valid = true;

        String email     = input_email.getText().toString();
        String password  = input_password.getText().toString();

        if(email.isEmpty() || !utils.isValidEmail(email)){
            input_email.setError("Invalid email Address",drw_error);
            valid = false;
        }else{
            input_email.setError(null);
        }

        if(password.isEmpty() || password.length() < 8){
            input_password.setError("Password should be at least 8 characters",drw_error);
            valid = false;
        }else{
            input_password.setError(null);
        }

        return valid;
    }

    public void goToOtherActivity(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void goToOtherActivityFinish(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        finish();
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void goToOtherActivityFinishAllPrevious(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
