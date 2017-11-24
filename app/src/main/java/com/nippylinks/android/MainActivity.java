package com.nippylinks.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import com.nippylinks.android.utils.utils;

/**
 * Created by kevincampo on 16/03/17.
 */

public class MainActivity extends Activity{

    public static AssetManager assetsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.assetsManager = this.getAssets();
        FontsOverride.setDefaultFont(this, "MONOSPACE", utils.RELAWAY_REGULAR_FONT);
        Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
        finish();
        startActivity(intent);
    }

    public static AssetManager getInstanceAsset(){
        return MainActivity.assetsManager;
    }
}
