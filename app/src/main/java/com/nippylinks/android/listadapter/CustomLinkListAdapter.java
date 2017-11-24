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
import android.widget.ImageView;

import com.nippylinks.android.LoginLinkActivity;
import com.nippylinks.android.R;
import com.nippylinks.android.classes.CustomLink;
import com.nippylinks.android.utils.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevincampo on 20/03/17.
 */

public class CustomLinkListAdapter extends ArrayAdapter {

    public List<CustomLinkHolder> customLinkHolders = new ArrayList<>();

    List list = new ArrayList();
    private Context mContext;

    public CustomLinkListAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    public void add(CustomLink object){
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
        View row;
        row = convertView;
        CustomLinkHolder customLinkHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.custom_links_list_item, parent, false);
            customLinkHolder = new CustomLinkHolder();

            customLinkHolder.btn_name          = (Button) row.findViewById(R.id.custom_link_item);
            customLinkHolder.custom_link_radio = (ImageView) row.findViewById(R.id.custom_link_radio);
            row.setTag(customLinkHolder);
        }else{
            customLinkHolder = (CustomLinkHolder)row.getTag();
        }

        CustomLink customLink = (CustomLink) this.getItem(position);
        customLinkHolder.custom_link_radio.setTag(customLink);
        customLinkHolder.btn_name.setTag(customLink);
        utils.changeButtonBG(customLinkHolder.btn_name, "#0500F5");

        customLinkHolder.btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginLinkActivity.class);
                CustomLink customLink = (CustomLink) v.getTag();
                intent.putExtra("id_select", customLink.getId());
                intent.putExtra("name_select", customLink.getName());
                intent.putExtra("url_select", customLink.getLogin_url());
                intent.putExtra("username_select", customLink.getUsername());
                intent.putExtra("password_select", customLink.getPassword());
                intent.putExtra("us_field_select", customLink.getUsernameField());
                intent.putExtra("pass_field_select", customLink.getPasswordField());
                intent.putExtra("btn_field_select", customLink.getButtonField());
                intent.putExtra("form_field_select", customLink.getFormField());
                mContext.startActivity(intent);
            }
        });
        customLinkHolder.btn_name.setText(customLink.getName());
        customLinkHolders.add(customLinkHolder);

        return row;
    }

    public class CustomLinkHolder{
        public Button btn_name;
        public ImageView custom_link_radio;
    }
}
