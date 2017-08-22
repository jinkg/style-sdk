package com.yalin.wallpaper.forest;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    public static boolean keyEquals(String key, int resId) {
        return key.equals(ForestService.context().getString(resId));
    }

    public static boolean keyAnyOf(String key, int... resIds) {
        for (int keyEquals : resIds) {
            if (keyEquals(key, keyEquals)) {
                return true;
            }
        }
        return false;
    }

    public static boolean getBoolean(int keyId, int defaultId) {
        return getBoolean(ForestService.context().getString(keyId), defaultId);
    }

    public static boolean getBoolean(String key, int defaultId) {
        return get(key, ForestService.context().getResources().getBoolean(defaultId));
    }

    public static float getFloat(int keyId, int defaultId) {
        return getFloat(ForestService.context().getString(keyId), defaultId);
    }

    public static float getFloat(String key, int defaultId) {
        return get(key, Float.valueOf(ForestService.context().getString(defaultId)));
    }

    public static int getInt(int keyId, int defaultId) {
        return getInt(ForestService.context().getString(keyId), defaultId);
    }

    public static int getInt(String key, int defaultId) {
        return get(key, ForestService.context().getResources().getInteger(defaultId));
    }

    public static long getLong(int keyId, int defaultId) {
        return getLong(ForestService.context().getString(keyId), defaultId);
    }

    public static long getLong(String key, int defaultId) {
        return get(key, Long.valueOf(ForestService.context().getString(defaultId)));
    }

    public static String getString(int keyId, int defaultId) {
        return getString(ForestService.context().getString(keyId), defaultId);
    }

    public static String getString(String key, int defaultId) {
        return get(key, ForestService.context().getString(defaultId));
    }

    public static boolean get(String key, boolean dflt) {
        return getSp().getBoolean(key, dflt);
    }

    public static float get(String key, float dflt) {
        return getSp().getFloat(key, dflt);
    }

    public static int get(String key, int dflt) {
        return getSp().getInt(key, dflt);
    }

    public static long get(String key, long dflt) {
        return getSp().getLong(key, dflt);
    }

    public static String get(String key, String dflt) {
        return getSp().getString(key, dflt);
    }

    private static SharedPreferences getSp() {
        return PreferenceManager.getDefaultSharedPreferences(ForestService.context());
    }
}
