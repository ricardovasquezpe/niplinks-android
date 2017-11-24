package com.nippylinks.android.listadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nippylinks.android.LoginLinkActivity;
import com.nippylinks.android.R;
import com.nippylinks.android.classes.Provider;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kevincampo on 20/03/17.
 */

public class MyProviderListAdapter extends ArrayAdapter {

    List list = new ArrayList();
    List listBack = new ArrayList();
    private Context mContext;

    public List<ProviderHolder> providerHolders = new ArrayList<>();

    public MyProviderListAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    public void add(Provider object){
        super.add(object);
        list.add(object);
        listBack.add(object);
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        setupImageLoader();
        View row;
        row = convertView;
        ProviderHolder providerHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.my_providers_list_item, parent, false);
            providerHolder = new ProviderHolder();
            providerHolder.provider_item      = (Button)    row.findViewById(R.id.provider_item);
            providerHolder.icon_favorite      = (ImageView) row.findViewById(R.id.icon_favorite);
            providerHolder.provider_radio     = (ImageView) row.findViewById(R.id.provider_radio);
            row.setTag(providerHolder);
        }else{
            providerHolder = (ProviderHolder)row.getTag();
        }

        int starImage = mContext.getResources().getIdentifier("@drawable/star", null, mContext.getPackageName());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(starImage)
                .showImageOnFail(starImage)
                .showImageOnLoading(starImage).build();

        Provider providers = (Provider) this.getItem(position);
        providerHolder.provider_item.setText(providers.getName());
        //providerHolder.provider_item.setBackgroundColor(Color.parseColor("#"+providers.getColor()));
        int round = 15;
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(new float[]{round,round,round,round,round,round,round,round},null,null));
        sd.setTint(Color.parseColor("#"+providers.getColor()));
        providerHolder.provider_item.setBackground(sd);
        providerHolder.provider_radio.setId(providers.getId());
        providerHolder.provider_radio.setTag(providers);
        if(providers.getFavorite() == 1){
            imageLoader.displayImage("drawable://"+R.drawable.star, providerHolder.icon_favorite, options);
        }

        providerHolder.provider_item.setTag(providers);
        providerHolder.provider_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginLinkActivity.class);
                Provider provSelec = (Provider) v.getTag();
                intent.putExtra("id_select", provSelec.getId());
                intent.putExtra("name_select", provSelec.getName());
                intent.putExtra("url_select", provSelec.getLogin_url());
                intent.putExtra("username_select", provSelec.getUsername());
                intent.putExtra("password_select", provSelec.getPassword());
                intent.putExtra("us_field_select", provSelec.getUsernameField());
                intent.putExtra("pass_field_select", provSelec.getPasswordField());
                intent.putExtra("btn_field_select", provSelec.getButtonField());
                intent.putExtra("form_field_select", provSelec.getFormField());
                mContext.startActivity(intent);
            }
        });

        providerHolders.add(providerHolder);

        return row;
    }

    public static class ProviderHolder{
        public Button provider_item;
        public ImageView provider_radio;
        public ImageView icon_favorite;
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                list = listBack;
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    results.count  = list.size();
                    results.values = list;
                } else {
                    List resultsData = new ArrayList();
                    String searchStr = constraint.toString().toUpperCase();
                    for (Object s : list){
                        Provider prov = (Provider) s;
                        if (prov.getName().toLowerCase().indexOf(searchStr.toLowerCase())>=0) resultsData.add(s);
                    }
                    results.count  = resultsData.size();
                    results.values = resultsData;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void sortBy(int opc){
        if(opc == 1){ //Favorite
            Collections.sort(list, comparatorFavorite);
        }else{ //Name
            Collections.sort(list, comparatorAbc);
        }
        super.notifyDataSetChanged();
    }

    Comparator<Object> comparatorFavorite = new Comparator<Object>() {
        @Override
        public int compare(Object left, Object right) {
            return Integer.valueOf(((Provider) right).getFavorite()).compareTo(((Provider) left).getFavorite());
        }
    };

    Comparator<Object> comparatorAbc = new Comparator<Object>() {
        @Override
        public int compare(Object left, Object right) {
            return ((Provider) left).getName().compareToIgnoreCase(((Provider) right).getName());
        }
    };

    public void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}