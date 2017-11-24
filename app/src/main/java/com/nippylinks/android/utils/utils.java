package com.nippylinks.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import com.nippylinks.android.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nippylinks.android.R;
import com.nippylinks.android.UserSessionManager;
import android.webkit.URLUtil;

/**
 * Created by kevincampo on 15/03/17.
 */

public class utils {
    public static final String RELAWAY_REGULAR_FONT     = "fonts/Raleway-Regular.ttf";
    public static final String RELAWAY_THIN_FONT        = "fonts/Raleway-Thin.ttf";
    public static final String RELAWAY_LIGHT_FONT       = "fonts/Raleway-Light.ttf";
    public static final String RELAWAY_BOLD_FONT        = "fonts/Raleway-Bold.ttf";
    public static final String RELAWAY_SEMIBOLD_FONT    = "fonts/Raleway-SemiBold.ttf";
    public static final String RELAWAY_BLACK_FONT       = "fonts/Raleway-Black.ttf";
    public static final String RELAWAY_MEDIUM_FONT      = "fonts/Raleway-Medium.ttf";
    public static final String RELAWAY_EXTRA_LIGHT_FONT = "fonts/Raleway-ExtraLight.ttf";

    public static final String API_KEY              = "0gg0c4owkkw084o884ok0cg4ogkswcgk4kk0wgcg";
    public static final String API_BASE_URL         = "http://nippylinks.ninthcoast.com/api/";
    public static final String API_LOGIN_URL        = "auth/";
    public static final String API_MY_PROVIDERS_URL = "providers/my_mixedlist/";
    public static final String API_PROVIDERS_URL    = "providers/my/";
    public static final String API_MY_CUSTOM_LINKS_URL = "providers/my_customlinks/";
    public static final String API_MY_USER          = "users/me/";
    public static final String API_MY_PASS          = "auth/reset_password";

    public static final String TYPE_POST = "POST";
    public static final String TYPE_GET  = "GET";
    public static final String TYPE_PUT  = "PUT";
    public static final String TYPE_DELETE  = "DELETE";

    public static void ChangeFont(TextView element, String font){
        Typeface face = Typeface.createFromAsset(MainActivity.getInstanceAsset(), font);
        element.setTypeface(face);
    }

    public static String connectToServer(String myurl, String type, String query, String access_token) throws IOException {
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        int len = 8000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty ("X-API-KEY", API_KEY);
            if(access_token != null && !access_token.isEmpty()){
                conn.setRequestProperty ("x-access-token", access_token);
            }
            conn.setRequestMethod(type);
            conn.setDoInput(true);

            if(type == TYPE_POST || type == TYPE_PUT || type == TYPE_DELETE){
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }
            conn.connect();

            return readIt(conn.getInputStream(), len);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        /*Reader reader = null;
        reader = new InputStreamReader(stream);
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);*/

        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    public static String constructStringParams(JSONObject params){
        Iterator<?> keys = params.keys();
        Uri.Builder builder = new Uri.Builder();
        try {
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                String value = params.get(key).toString();
                builder.appendQueryParameter(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = builder.build().getEncodedQuery();
        return query;
    }

    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    public static void goToOtherActivityLeft(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        clase1.startActivity(intent);
        if (clase1 instanceof Activity) {
            ((Activity) clase1).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    public static void goToOtherActivityRight(Context clase1, Class clase2){
        Intent intent = new Intent(clase1, clase2);
        clase1.startActivity(intent);
        if (clase1 instanceof Activity) {
            ((Activity) clase1).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public static void goToOtheActivityFinish(Context clase1, Class clase2)
    {
        Intent intent = new Intent(clase1, clase2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        clase1.startActivity(intent);
        if (clase1 instanceof Activity) {
            ((Activity) clase1).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public static void goToOtherActivityFinishAllPrevious(Context clase1, Class clase2)
    {
        Intent intent = new Intent(clase1, clase2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        clase1.startActivity(intent);
        if (clase1 instanceof Activity) {
            ((Activity) clase1).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    public static String formatURL(String url){
        if(!URLUtil.isHttpsUrl(url) && !URLUtil.isHttpUrl(url)){
            url = "http://" + url;
        }
        return url;
    }

    public static void changeButtonBG(Button button,String color) {
        int round = 15;
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(new float[]{round,round,round,round,round,round,round,round},null,null));
        //sd.setTint(Color.parseColor("#"+customLink.getColor()));
        sd.setTint(Color.parseColor(color));
        button.setBackground(sd);
    }
}
