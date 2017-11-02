package com.livewallpaper.datareader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.badlogic.gdx.utils.Disposable;
import com.livewallpaper.koipond.KoiPondService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashSet;

public class SharedPreferenceHelper implements Disposable {
    private static SharedPreferenceHelper sInstance = null;
    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(KoiPondService.getContext());

    public static SharedPreferenceHelper getInstance() {
        if (sInstance == null) {
            sInstance = new SharedPreferenceHelper();
        }
        return sInstance;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.sharedPreferences.getBoolean(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public int getInteger(String key, int defValue) {
        return this.sharedPreferences.getInt(key, defValue);
    }

    public void putInteger(String key, int value) {
        Editor editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key, String defValue) {
        return this.sharedPreferences.getString(key, defValue);
    }

    public void putString(String key, String value) {
        Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putStringSet(String key, LinkedHashSet<String> newValue) {
        Editor editor = this.sharedPreferences.edit();
        if (newValue == null) {
            editor.remove(key).commit();
        } else {
            editor.putString(key, new JSONArray(newValue).toString()).commit();
        }
    }

    public LinkedHashSet<String> getStringSet(String key, LinkedHashSet<String> defValue) {
        String str = this.sharedPreferences.getString(key, null);
        Editor editor = this.sharedPreferences.edit();
        if (str == null) {
            return new LinkedHashSet(defValue);
        }
        try {
            JSONArray jsonArray = new JSONArray(str);
            LinkedHashSet<String> result = new LinkedHashSet();
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getString(i));
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            editor.remove(key).commit();
            return new LinkedHashSet(defValue);
        }
    }

    public void dispose() {
        sInstance = null;
    }
}
