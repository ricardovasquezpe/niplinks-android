package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.utils.utils;

public class SignUpProfileActivity extends Activity {

    private Button btn_continue;
    private TextView tv_back;
    private ImageView img_back;
    public String email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_profile);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_back      = (TextView) findViewById(R.id.tv_back);
        img_back     = (ImageView) findViewById(R.id.img_back);

        utils.ChangeFont(btn_continue,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_back,utils.RELAWAY_REGULAR_FONT);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(SignUpProfileActivity.this, SignUpPinActivity.class);
                utils.goToOtherActivityLeft(SignUpProfileActivity.this,SignUpPinActivity.class);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(SignUpProfileActivity.this, SignUpEmailActivity.class);
                //utils.goToOtherActivityRight(SignUpProfileActivity.this,SignUpEmailActivity.class);
                onBackPressed();
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(SignUpProfileActivity.this, SignUpEmailActivity.class);
                //utils.goToOtherActivityRight(SignUpProfileActivity.this,SignUpEmailActivity.class);
                onBackPressed();
            }
        });
        utils.changeButtonBG(btn_continue,getResources().getString(R.color.colorCompBtnContinue));
    }

    @Override
    //On back pressed works only if the last activity called wasn't destroyed by finish()
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
