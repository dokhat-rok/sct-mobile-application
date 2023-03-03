package com.sct.mobile.application.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sct.mobile.application.model.enums.SharedName;

import java.util.EnumSet;

public class SharedDataUtil {

    private static SharedPreferences sharedPreferences;

    public static void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getString(String key){
        return sharedPreferences.getString(key, "");
    }

    public static void setString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void deleteString(String key){
        setString(key, "");
    }

    public static void deleteAll(){
        EnumSet.allOf(SharedName.class).forEach(n -> SharedDataUtil.deleteString(n.getLabel()));
    }
}
