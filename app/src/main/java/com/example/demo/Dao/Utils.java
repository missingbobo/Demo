package com.example.demo.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.example.demo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private final static String PRENAME = "data";

    public static void saveToken(String token, Activity context) {
        SharedPreferences spf = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
        spf.edit().putString("token", token).commit();

    }

    public static String getToken(Activity context) {
        SharedPreferences spf = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
        return spf.getString("token", "");
    }

    public static String[] getAvatar(Activity context) {
        String[] avatars = new String[5];
        String content = getStringFromRaw(context, R.raw.avatar);
        try {
            JSONObject obj = new JSONObject(content);
            JSONArray arry = obj.optJSONArray("items");
            for(int i = 0;i<arry.length();i++){
                String temple = arry.getString(i);
                avatars[i] = temple;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return avatars;
    }

    public static String getStringFromRaw(Context context, int id) {
        String content;
        Resources resources = context.getResources();
        InputStream is = null;
        try {
            is = resources.openRawResource(id);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            content = new String(buffer);
            return content;
        } catch (IOException e) {
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    return "";
                }
            }
        }
    }
}
