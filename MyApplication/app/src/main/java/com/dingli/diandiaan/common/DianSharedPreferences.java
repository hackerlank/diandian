package com.dingli.diandiaan.common;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/7.
 */
public class DianSharedPreferences {

        private final SharedPreferences sharedPreferences;
      public DianSharedPreferences(){
          sharedPreferences= PreferenceManager.getDefaultSharedPreferences(DianApplication.getInstance());
      }
    public boolean saveString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }
    public String getStringValue(String key) {
        return getStringValue(key, "");
    }
    public String getStringValue(String key, String defValue) {
        String string = sharedPreferences.getString(key, defValue);
        if (TextUtils.isEmpty(string))
            return defValue;
        return string;
    }
    public boolean saveInt(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    public int getIntValue(String key) {
        return getIntValue(key, -10);
    }
    public int getIntValue(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }
    public boolean saveBoolean(String key, boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBooleanValue(String key) {
        return getBooleanValue(key, true);
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean saveFloat(String key, Float value) {
        return sharedPreferences.edit().putFloat(key, value).commit();
    }
    public Float getFloatValue(String key, Float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public boolean saveLong(String key, long value) {
        return sharedPreferences.edit().putLong(key, value).commit();
    }

    public boolean saveContentValues(ContentValues values) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, Object> value : values.valueSet()) {
            editor.putString(value.getKey(), value.getValue().toString());
        }
        return editor.commit();
    }
}
