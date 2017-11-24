package com.nippylinks.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nippylinks.android.classes.Provider;
import com.nippylinks.android.listadapter.ProviderListAdapter;
import com.nippylinks.android.utils.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProvidersActivity extends Activity {

    private ImageView img_back;
    private TextView tv_back;
    private Button btn_save;
    private TextView tv_title;
    private TextView tv_agreement;

    AsynkTask asyncTask;

    UserSessionManager session;

    ListView listView;

    ProviderListAdapter providerAdapter;

    int idSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        session = new UserSessionManager(getBaseContext());

        img_back       = (ImageView) findViewById(R.id.img_back);
        tv_back        = (TextView)  findViewById(R.id.tv_back);
        btn_save       = (Button)    findViewById(R.id.btn_save);
        tv_title       = (TextView)  findViewById(R.id.tv_title);
        tv_agreement   = (TextView) findViewById(R.id.tv_agreement);

        utils.ChangeFont(tv_back, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_title, utils.RELAWAY_MEDIUM_FONT);
        utils.ChangeFont(btn_save, utils.RELAWAY_REGULAR_FONT);
        utils.ChangeFont(tv_agreement, utils.RELAWAY_REGULAR_FONT);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(ProvidersActivity.this,LinksActivity.class);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(ProvidersActivity.this,LinksActivity.class);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.goToOtherActivityRight(ProvidersActivity.this,DashboardActivity.class);
            }
        });

        populateListProviders();
    }

    public void populateListProviders() {
        AsynkTask async = new AsynkTask(this) {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray jsonArrayProviders = responseObj.getJSONArray("data");

                    listView = (ListView)findViewById(R.id.listViewProviders);
                    providerAdapter = new ProviderListAdapter(this.currentActivity, R.layout.providers_list_item);
                    listView.setAdapter(providerAdapter);
                    listView.setDividerHeight(0);
                    registerForContextMenu(listView);
                    int count = 0;
                    int height=0;
                    while (count < jsonArrayProviders.length()){
                        JSONObject object = jsonArrayProviders.getJSONObject(count);
                        int favorite = (object.get("favorite").equals(null))?0:object.getInt("favorite");
                        Provider prov = new Provider(object.getInt("id"), object.getString("name"), object.getString("color"), favorite, object.getInt("assigned"), object.getString("login_url"), null, null, null, null, null, null);
                        providerAdapter.add(prov);
                        View childView = listView.getAdapter().getView(count, null, listView);
                        childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        height= childView.getMeasuredHeight()+height+4;
                        count++;
                    }

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int pos, long id) {
                            openContextMenu(listView);
                            return true;
                        }
                    });

                    listView.getLayoutParams().height=height;
                    listView.requestLayout();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Provider provSel = (Provider) listView.getItemAtPosition(position);
                            if(provSel.getAssigned() != 1){
                                Intent intent = new Intent(ProvidersActivity.this, SelectProviderActivity.class);
                                intent.putExtra("color_prov_select", provSel.getColor());
                                intent.putExtra("name_prov_select", provSel.getName());
                                intent.putExtra("url_prov_select", provSel.getLogin_url());
                                intent.putExtra("id_prov_select", provSel.getId());
                                startActivity(intent);
                            }else{
                                idSelected = provSel.getId();
                                view.performLongClick();
                                registerForContextMenu(view);
                            }
                        }
                    });
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        async.execute(utils.API_BASE_URL + utils.API_PROVIDERS_URL, utils.TYPE_GET, null, session.getSessionToken());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("The provider already exist in your list");
        menu.add(0, 1, 0, "Remove");
        menu.add(0, 2, 0, "Not Now");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                removeProviderFromMyList(idSelected);
                break;
        }

        return false;
    }

    public void removeProviderFromMyList(int idProvider){
        try {
            JSONObject paramsObj = new JSONObject();
            paramsObj.put("provider_id", idProvider);

            AsynkTask sec = new AsynkTask(this) {
                @Override
                public void onPostExecute(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        if(Boolean.parseBoolean(responseObj.get("status").toString())){
                            providerAdapter.clear();
                            providerAdapter.notifyDataSetChanged();
                            populateListProviders();
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
}