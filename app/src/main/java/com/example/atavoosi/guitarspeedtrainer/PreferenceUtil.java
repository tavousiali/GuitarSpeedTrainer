package com.example.atavoosi.guitarspeedtrainer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by A.Tavoosi on 16/10/2016.
 */
//public class PreferenceUtil extends PreferenceActivity {
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public int GetPref(String key) {
//        return prefs.getInt(key, 40);
//    }
//
//    public void SavePref(String key, int value) {
//        SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = shared.edit();
//        editor.putInt(key, value);
//        editor.apply();
//    }
//}
public class PreferenceUtil {

    public static void savePreferences(Activity activity, String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readPreferences(Activity activity, String key, String defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getString(key, defaultValue);
    }

    public static void savePreferences(Activity activity, String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int readPreferences(Activity activity, String key, int defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getInt(key, defaultValue);
    }
}