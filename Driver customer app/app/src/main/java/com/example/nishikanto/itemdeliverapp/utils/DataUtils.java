package com.example.nishikanto.itemdeliverapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataUtils {

    private static final String TAG = DataUtils.class.getSimpleName();
    private static Context context;

    public final static String APP_PREF = "app_pref";


    public DataUtils(Context context){
        this.context = context;
    }

    public void setStr(String key, String value) {
        Log.d(TAG, "setStr: "+ key+"+"+value);
        SharedPreferences prefs = context.getSharedPreferences(APP_PREF,0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREF, 0);
        return prefs.getString(key,"");
    }


}
