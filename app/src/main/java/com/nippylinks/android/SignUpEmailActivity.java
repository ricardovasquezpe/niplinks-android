package com.nippylinks.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.utils.utils;

public class SignUpEmailActivity extends Activity {

    private ImageView img_back;
    private TextView tv_lets;
    private TextView tv_what;
    private Button btn_continue;
    private TextView tv_back;
    private EditText et_email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);

        img_back          = (ImageView) findViewById(R.id.img_back);
        tv_back           = (TextView)  findViewById(R.id.tv_back);
        tv_lets           = (TextView)  findViewById(R.id.tv_lets);
        tv_what           = (TextView)  findViewById(R.id.tv_what);
        btn_continue      = (Button)    findViewById(R.id.btn_continue);
        et_email_address  = (EditText)  findViewById(R.id.et_email_address);

        utils.ChangeFont(tv_back, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_lets, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_what, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(btn_continue, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(et_email_address,utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //utils.goToOtherActivityRight(SignUpEmailActivity.this, LoginActivity.class);
                onBackPressed();
            }
        });    

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //utils.goToOtherActivityRight(SignUpEmailActivity.this, LoginActivity.class);
                onBackPressed();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityLeft(SignUpEmailActivity.this, SignUpProfileActivity.class);
            }
        });

        utils.changeButtonBG(btn_continue,getResources().getString(R.color.colorCompBtnContinue));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
