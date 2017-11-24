package com.nippylinks.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.nippylinks.android.utils.utils;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "RELAWAY_REGULAR", utils.RELAWAY_REGULAR_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_MEDIUM", utils.RELAWAY_MEDIUM_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_LIGHT", utils.RELAWAY_LIGHT_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_SEMIBOLD", utils.RELAWAY_SEMIBOLD_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_THIN", utils.RELAWAY_THIN_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_BOLD", utils.RELAWAY_BOLD_FONT);
        FontsOverride.setDefaultFont(this, "RELAWAY_BLACK", utils.RELAWAY_BLACK_FONT);

        setContentView(R.layout.activity_splash_screen);
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
