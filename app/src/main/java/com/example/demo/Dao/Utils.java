package com.example.demo.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    private final static String PRENAME = "data";
    public static void saveToken(String token, Activity context){
        SharedPreferences spf = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
        spf.edit().putString("token",token).commit();

    }
    public static String getToken(Activity context){
        SharedPreferences spf = context.getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
        return spf.getString("token","");
    }
}
