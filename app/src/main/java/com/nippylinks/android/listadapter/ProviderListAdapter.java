package com.nippylinks.android.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.nippylinks.android.R;
import com.nippylinks.android.classes.Provider;
import com.nippylinks.android.utils.utils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevincampo on 20/03/17.
 */

public class ProviderListAdapter extends ArrayAdapter {

    List list = new ArrayList();
    private Context mContext;

    public ProviderListAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    public void add(Provider object){
        super.add(object);
        list.add(object);
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
            row = layoutInflater.inflate(R.layout.providers_list_item, parent, false);
            providerHolder = new ProviderHolder();

            providerHolder.btn_name      = (Button)    row.findViewById(R.id.provider_item);
            providerHolder.icon_selected = (ImageView) row.findViewById(R.id.icon_selected);
            row.setTag(providerHolder);
        }else{
            providerHolder = (ProviderHolder)row.getTag();
        }

        int starImage = mContext.getResources().getIdentifier("@drawable/green_mark", null, mContext.getPackageName());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(starImage)
                .showImageOnFail(starImage)
                .showImageOnLoading(starImage).build();

        Provider providers = (Provider) this.getItem(position);
        providerHolder.btn_name.setText(providers.getName());
        int round = 15;
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(new float[]{round,round,round,round,round,round,round,round},null,null));
        utils.changeButtonBG(providerHolder.btn_name,"#"+providers.getColor());
        if(providers.getAssigned() == 1){
            imageLoader.displayImage("drawable://"+R.drawable.green_mark, providerHolder.icon_selected, options);
        }
        return row;
    }

    static class ProviderHolder{
        Button btn_name;
        ImageView icon_selected;
    }

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