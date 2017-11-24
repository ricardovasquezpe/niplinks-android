package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.utils.utils;

public class SignUpPinActivity extends Activity {

    private ImageView img_back;
    private TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_pin);

        img_back = (ImageView) findViewById(R.id.img_back);
        tv_back  = (TextView) findViewById(R.id.tv_back);

        utils.ChangeFont(tv_back,utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(SignUpPinActivity.this, SignUpProfileActivity.class);
                //utils.goToOtherActivityRight(SignUpPinActivity.this,SignUpProfileActivity.class);
                onBackPressed();
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(SignUpPinActivity.this, SignUpProfileActivity.class);
                //utils.goToOtherActivityRight(SignUpPinActivity.this,SignUpProfileActivity.class);
                onBackPressed();
            }
        });
    }

    @Override
    //On back pressed works only if the last activity called wasn't destroyed by finish()
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
