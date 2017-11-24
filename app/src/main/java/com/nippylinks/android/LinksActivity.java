package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nippylinks.android.utils.utils;

public class LinksActivity extends Activity {

    private ImageView img_back;
    private TextView tv_back;
    private TextView tv_addLink;

    private Button btn_add_cust_link;
    private Button btn_pick_provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        img_back          = (ImageView) findViewById(R.id.img_back);
        tv_addLink        = (TextView)  findViewById(R.id.tv_addLink);
        tv_back           = (TextView) findViewById(R.id.tv_back);
        btn_pick_provider = (Button) findViewById(R.id.btn_pick_provider);
        btn_add_cust_link = (Button) findViewById(R.id.btn_add_cust_link);

        utils.ChangeFont(tv_back,utils.RELAWAY_LIGHT_FONT);
        utils.ChangeFont(tv_addLink,utils.RELAWAY_MEDIUM_FONT);
        utils.ChangeFont(btn_add_cust_link,utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(btn_pick_provider,utils.RELAWAY_REGULAR_FONT);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(LinksActivity.this, DashboardActivity.class);
                utils.goToOtherActivityRight(LinksActivity.this,DashboardActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(LinksActivity.this, DashboardActivity.class);
                utils.goToOtherActivityRight(LinksActivity.this,DashboardActivity.class);
            }
        });

        btn_pick_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(LinksActivity.this, ProvidersActivity.class);
                utils.goToOtherActivityLeft(LinksActivity.this,ProvidersActivity.class);
            }
        });

        btn_add_cust_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToOtherACtivity(LinksActivity.this, CustomLinksActivity.class);
                utils.goToOtherActivityLeft(LinksActivity.this,CustomLinksActivity.class);
            }
        });
        utils.changeButtonBG(btn_add_cust_link,getResources().getString(R.color.colorCompBtnContinue));
        utils.changeButtonBG(btn_pick_provider,getResources().getString(R.color.colorCompBtnContinue));
    }

    public void goToOtherACtivity(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
