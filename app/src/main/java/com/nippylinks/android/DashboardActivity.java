package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.nippylinks.android.classes.CustomLink;
import com.nippylinks.android.classes.Provider;
import com.nippylinks.android.listadapter.CustomLinkListAdapter;
import com.nippylinks.android.listadapter.MyProviderListAdapter;
import com.nippylinks.android.utils.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DashboardActivity extends Activity {

    private TextView tv_edit;
    private TextView tv_sortby;
    private TextView tv_filter;
    private TextView tv_providers;
    private TextView tv_custom_links;

    private EditText et_search;

    private ImageView ico_plus;
    private ImageView ico_setting;

    private ArrayList<String> providerList = new ArrayList<String>();

    private Boolean edit_touch   = false;
    private Boolean filter_touch = false;

    UserSessionManager session;

    MyProviderListAdapter myProviderAdapter;
    CustomLinkListAdapter customLinksAdapter;

    int idSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new UserSessionManager(getBaseContext());

        tv_edit         = (TextView) findViewById(R.id.tv_edit);
        tv_sortby       = (TextView) findViewById(R.id.tv_sortby);
        tv_filter       = (TextView) findViewById(R.id.tv_filter);
        tv_providers    = (TextView) findViewById(R.id.tv_providers);
        tv_custom_links = (TextView) findViewById(R.id.tv_custom_links);
        et_search       = (EditText) findViewById(R.id.et_search);
        ico_plus        = (ImageView) findViewById(R.id.ico_plus);
        ico_setting     = (ImageView) findViewById(R.id.ico_setting);
        utils.ChangeFont(tv_edit,utils.RELAWAY_LIGHT_FONT);
        utils.ChangeFont(tv_filter,utils.RELAWAY_SEMIBOLD_FONT);
        utils.ChangeFont(tv_sortby,utils.RELAWAY_MEDIUM_FONT);
        utils.ChangeFont(et_search,utils.RELAWAY_LIGHT_FONT);
        utils.ChangeFont(tv_providers,utils.RELAWAY_SEMIBOLD_FONT);
        utils.ChangeFont(tv_custom_links,utils.RELAWAY_SEMIBOLD_FONT);

        ico_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityLeft(DashboardActivity.this,LinksActivity.class);
            }
        });
        ico_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityLeft(DashboardActivity.this,SettingsActivity.class);
            }
        });

        tv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filter_touch){
                    tv_filter.setText("Favorite");
                    myProviderAdapter.sortBy(1);
                    filter_touch = false;
                }else{
                    tv_filter.setText("ABC");
                    myProviderAdapter.sortBy(2);
                    filter_touch = true;
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                DashboardActivity.this.myProviderAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_touch){
                    tv_edit.setText("Edit");
                    for(int i=0; i< myProviderAdapter.providerHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(myProviderAdapter.providerHolders.get(i).provider_item).leftMargin(0).setDuration(300).start();
                        myProviderAdapter.providerHolders.get(i).provider_radio.setVisibility(View.INVISIBLE);
                    }
                    for(int i=0; i< customLinksAdapter.customLinkHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(customLinksAdapter.customLinkHolders.get(i).btn_name).leftMargin(0).setDuration(300).start();
                        customLinksAdapter.customLinkHolders.get(i).custom_link_radio.setVisibility(View.INVISIBLE);
                    }
                    edit_touch = false;
                }else{
                    tv_edit.setText("Done");
                    int leftMargin=convertDpToPixel(45,DashboardActivity.this);
                    for(int i=0; i< myProviderAdapter.providerHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(myProviderAdapter.providerHolders.get(i).provider_item).leftMargin(leftMargin).setDuration(300).start();
                        myProviderAdapter.providerHolders.get(i).provider_radio.setVisibility(View.VISIBLE);
                        myProviderAdapter.providerHolders.get(i).provider_radio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.performLongClick();
                            }
                        });
                        registerForContextMenu(myProviderAdapter.providerHolders.get(i).provider_radio);
                    }
                    for(int i=0; i< customLinksAdapter.customLinkHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(customLinksAdapter.customLinkHolders.get(i).btn_name).leftMargin(leftMargin).setDuration(300).start();
                        customLinksAdapter.customLinkHolders.get(i).custom_link_radio.setVisibility(View.VISIBLE);
                        customLinksAdapter.customLinkHolders.get(i).custom_link_radio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.performLongClick();
                            }
                        });
                        registerForContextMenu(customLinksAdapter.customLinkHolders.get(i).custom_link_radio);
                    }
                    edit_touch = true;
                }
            }
        });

        populateMyDashboard();
    }

    public void populateMyDashboard() {
        AsynkTask async = new AsynkTask(this) {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response).getJSONObject("data");

                    JSONArray jsonArrayProviders    = responseObj.getJSONArray("providers");
                    JSONArray jsonArrayCustonmLinks = responseObj.getJSONArray("customlinks");

                    //PROVIDERS//
                    final ListView listView = (ListView)findViewById(R.id.listViewMyProviders);
                    myProviderAdapter = new MyProviderListAdapter(this.currentActivity, R.layout.my_providers_list_item);
                    listView.setAdapter(myProviderAdapter);
                    listView.setDividerHeight(0);

                    int height=0;
                    int count = 0;
                    while (count < jsonArrayProviders.length()){
                        JSONObject object = jsonArrayProviders.getJSONObject(count);
                        if(object.getInt("assigned") == 1){
                            String form = (object.get("openform_selector").equals(null))?"":object.getString("openform_selector");
                            Provider prov = new Provider(object.getInt("id"), object.getString("name"), object.getString("color"), object.getInt("favorite"), object.getInt("assigned"), object.getString("login_url"),
                                    object.getString("username"), object.getString("password"), object.getString("username_selector"), object.getString("password_selector"), object.getString("submit_selector"), form);
                            myProviderAdapter.add(prov);
                            View childView = listView.getAdapter().getView(count, null, listView);
                            childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            height+= childView.getMeasuredHeight();
                        }
                        providerList.add(jsonArrayProviders.getString(count));
                        count++;
                    }

                    //int baseHeight = convertDpToPixel(height,DashboardActivity.this);
                    listView.getLayoutParams().height=(height);
                    listView.requestLayout();

                    //CUSTOM LINKS//
                    ListView listView1 = (ListView)findViewById(R.id.listViewCustomLinks);
                    customLinksAdapter = new CustomLinkListAdapter(this.currentActivity, R.layout.custom_links_list_item);
                    listView1.setAdapter(customLinksAdapter);
                    listView1.setDividerHeight(0);

                    int count1 = 0;
                    height=0;

                    while (count1 < jsonArrayCustonmLinks.length()){
                        JSONObject object = jsonArrayCustonmLinks.getJSONObject(count1);
                        String form = (object.get("openform_selector").equals(null))?"":object.getString("openform_selector");
                        CustomLink cus = new CustomLink(object.getInt("id"), object.getString("name"), null, object.getString("login_url"),
                                object.getString("username"), object.getString("password"), object.getString("username_selector"), object.getString("password_selector"), object.getString("submit_selector"), form);
                        customLinksAdapter.add(cus);
                        View childView = listView1.getAdapter().getView(count1, null, listView1);
                        childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        height+= childView.getMeasuredHeight();
                        count1++;
                    }
                    //baseHeight = convertDpToPixel(height,DashboardActivity.this);
                    listView1.getLayoutParams().height=(height);
                    listView1.requestLayout();
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        async.execute(utils.API_BASE_URL + utils.API_MY_PROVIDERS_URL, utils.TYPE_GET, null, session.getSessionToken());
    }

    public void refreshMyProviders(){
        AsynkTask async = new AsynkTask(this) {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response).getJSONObject("data");
                    JSONArray jsonArrayProviders    = responseObj.getJSONArray("providers");

                    final ListView listView = (ListView)findViewById(R.id.listViewMyProviders);
                    myProviderAdapter = new MyProviderListAdapter(this.currentActivity, R.layout.my_providers_list_item);
                    listView.setAdapter(myProviderAdapter);
                    listView.setDividerHeight(0);

                    int height=0;
                    int count = 0;
                    while (count < jsonArrayProviders.length()){
                        JSONObject object = jsonArrayProviders.getJSONObject(count);
                        if(object.getInt("assigned") == 1){
                            String form = (object.get("openform_selector").equals(null))?"":object.getString("openform_selector");
                            Provider prov = new Provider(object.getInt("id"), object.getString("name"), object.getString("color"), object.getInt("favorite"), object.getInt("assigned"), object.getString("login_url"),
                                    object.getString("username"), object.getString("password"), object.getString("username_selector"), object.getString("password_selector"), object.getString("submit_selector"), form);
                            myProviderAdapter.add(prov);
                            View childView = listView.getAdapter().getView(count, null, listView);
                            childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            height+= childView.getMeasuredHeight();
                        }
                        providerList.add(jsonArrayProviders.getString(count));
                        count++;
                    }

                    int baseHeight = convertDpToPixel(height,DashboardActivity.this);
                    listView.getLayoutParams().height=(height);
                    listView.requestLayout();

                    for(int i=0; i< customLinksAdapter.customLinkHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(customLinksAdapter.customLinkHolders.get(i).btn_name).leftMargin(0).setDuration(300).start();
                        customLinksAdapter.customLinkHolders.get(i).custom_link_radio.setVisibility(View.INVISIBLE);
                    }
                    edit_touch = false;
                    tv_edit.setText("Edit");
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        async.execute(utils.API_BASE_URL + utils.API_MY_PROVIDERS_URL, utils.TYPE_GET, null, session.getSessionToken());
    }

    public void refreshCustomLinks(){
        AsynkTask async = new AsynkTask(this) {
            @Override
            public void onPostExecute(String response) {
                try{
                    JSONObject responseObj = new JSONObject(response).getJSONObject("data");
                    JSONArray jsonArrayCustonmLinks = responseObj.getJSONArray("customlinks");

                    ListView listView1 = (ListView)findViewById(R.id.listViewCustomLinks);
                    customLinksAdapter = new CustomLinkListAdapter(this.currentActivity, R.layout.custom_links_list_item);
                    listView1.setAdapter(customLinksAdapter);
                    listView1.setDividerHeight(0);

                    int count1 = 0;
                    int height=0;

                    while (count1 < jsonArrayCustonmLinks.length()){
                        JSONObject object = jsonArrayCustonmLinks.getJSONObject(count1);
                        String form = (object.get("openform_selector").equals(null))?"":object.getString("openform_selector");
                        CustomLink cus = new CustomLink(object.getInt("id"), object.getString("name"), null, object.getString("login_url"),
                                object.getString("username"), object.getString("password"), object.getString("username_selector"), object.getString("password_selector"), object.getString("submit_selector"), form);
                        customLinksAdapter.add(cus);
                        View childView = listView1.getAdapter().getView(count1, null, listView1);
                        childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        height+= childView.getMeasuredHeight();
                        count1++;
                    }
                    int baseHeight = convertDpToPixel(height,DashboardActivity.this);
                    listView1.getLayoutParams().height=(height);
                    listView1.requestLayout();
                    for(int i=0; i< myProviderAdapter.providerHolders.size(); i++){
                        ViewPropertyObjectAnimator.animate(myProviderAdapter.providerHolders.get(i).provider_item).leftMargin(0).setDuration(300).start();
                        myProviderAdapter.providerHolders.get(i).provider_radio.setVisibility(View.INVISIBLE);
                    }
                    edit_touch = false;
                    tv_edit.setText("Edit");
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        async.execute(utils.API_BASE_URL + utils.API_MY_PROVIDERS_URL, utils.TYPE_GET, null, session.getSessionToken());
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(Provider.class.isInstance(v.getTag())){
            Provider provSelec = (Provider) v.getTag();
            if(provSelec.getFavorite() == 0){
                menu.add(0, 1, 0, "Mark as favorite");
            }else{
                menu.add(0, 2, 0, "Mark as unfavorite");
            }
            menu.add(0, 3, 0, "Remove");
            menu.add(0, 4, 0, "Cancel");
            idSelected = provSelec.getId();
        }else{
            CustomLink customSelec = (CustomLink) v.getTag();
            menu.add(0, 5, 0, "Remove");
            menu.add(0, 4, 0, "Cancel");
            idSelected = customSelec.getId();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                markUnmarkFavoriteProvider(idSelected, 1);
                break;
            case 2:
                markUnmarkFavoriteProvider(idSelected, 0);
                break;
            case 3:
                removeProvider(idSelected);
                break;
            case 5:
                removeCustomLink(idSelected);
                break;
        }

        return false;
    }

    public void markUnmarkFavoriteProvider(int idProvider, int opc){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("provider_id", idProvider);
            paramsObj.put("favorite", opc);

            AsynkTask sec = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            myProviderAdapter.clear();
                            myProviderAdapter.notifyDataSetChanged();
                            refreshMyProviders();
                        }
                        progress.dismiss();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
            sec.execute(utils.API_BASE_URL + utils.API_PROVIDERS_URL, utils.TYPE_POST, utils.constructStringParams(paramsObj), session.getSessionToken());

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void removeProvider(int idProvider){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("provider_id", idProvider);

            AsynkTask sec = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            myProviderAdapter.clear();
                            myProviderAdapter.notifyDataSetChanged();
                            refreshMyProviders();
                        }
                        progress.dismiss();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };

            sec.execute(utils.API_BASE_URL + utils.API_PROVIDERS_URL, utils.TYPE_DELETE, utils.constructStringParams(paramsObj), session.getSessionToken());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void removeCustomLink(int customLink){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("id", customLink);

            AsynkTask sec = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            customLinksAdapter.clear();
                            customLinksAdapter.notifyDataSetChanged();
                            refreshCustomLinks();
                        }
                        progress.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            sec.execute(utils.API_BASE_URL + utils.API_MY_CUSTOM_LINKS_URL, utils.TYPE_DELETE, utils.constructStringParams(paramsObj), session.getSessionToken());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}