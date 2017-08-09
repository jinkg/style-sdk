package com.yalin.wallpaper.boids;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ProfileLoader {
    private static final String TAG = "ProfileLoader";

    private static final Map<String, Profile> profiles = new HashMap<>();

    public static void init(Context context) {
        loadProfiles();
    }

    public static Profile getProfile(String key) {
        return profiles.get(key);
    }

    public static String[] getProfileNames() {
        return profiles.keySet().toArray(new String[0]);
    }

    private static void loadProfiles() {
        Profile profile = getDefaultProfile();
        profiles.put(profile.name, profile);
    }

    private static Profile getDefaultProfile() {
        Profile profile = new Profile();
        profile.name = "Default";
        profile.FLOCK_SIZE = 500;
        profile.RANDOMIZE_COLORS = false;
        profile.MIN_SEED = -500;
        profile.MAX_SEED = 500;
        profile.MIN_SIZE = 1000;
        profile.MAX_SIZE = 100000;
        profile.MAX_VELOCITY = 10;
        profile.RANGE = 200.0f;
        profile.AVOID_RADIUS = 50.0f;
        profile.NEIGHBORS = 10;
        profile.REBOUND_VELOCITY = 0.01f;
        profile.SIZE_SCALE = 750;
        profile.FLEE_TIME = 300;
        profile.SCALE_V1 = 0.75f;
        profile.SCALE_V2 = 0.01f;
        profile.SCALE_V3 = 1;
        profile.SCALE_V4 = 1.0f;
        profile.SCALE_V5 = 0.0001f;
        profile.SCALE_V6 = 1.0f;

        profile.BONUS_V1 = 100;
        profile.BONUS_V2 = 100;
        profile.BONUS_V3 = 100;
        profile.BONUS_V4 = 100;
        profile.BONUS_V5 = 100;
        profile.BONUS_V6 = 100;
        profile.BONUS_V7 = 100;
        profile.BONUS_VELOCITY = 100;

        profile.MIN_X = -800;
        profile.MAX_X = 800;
        profile.MIN_Y = -800;
        profile.MAX_Y = 800;
        profile.MIN_Z = -800;
        profile.MAX_Z = 800;
        profile.MIN_ALPHA = 0.01f;
        return profile;
    }

    public static JSONObject storeProfile(Profile profile) {

        JSONObject obj = new JSONObject();
        Field fields[] = profile.getClass().getFields();

        for (Field f : fields) {
            String name = f.getName();

            try {
                obj.put(name, f.get(profile));
            } catch (JSONException e) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Could not store field " + name + ": " + e, e);
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Could not store field " + name + ": " + e, e);
            }
        }

        return obj;
    }

    public static void saveProfile(Profile profile,
                                   SharedPreferences preferences) {
        String json = storeProfile(profile).toString();

        if (BuildConfig.DEBUG) Log.d(TAG, "saving profile to preferences: " + json);

        Editor editor = preferences.edit();
        editor.putString("profile", json);
        editor.commit();
    }

    public static void updateProfile(Profile profile,
                                     SharedPreferences preferences) {

        try {
            Field fields[] = profile.getClass().getFields();

            for (Field field : fields) {
                String field_name = field.getName();

                if (preferences.contains(field_name))
                    updateProfile(profile, preferences, field_name);
            }
        } catch (Throwable e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Could not update profile: " + e, e);
        }

    }

    public static void updateProfile(Profile profile,
                                     SharedPreferences preferences,
                                     String key) {

        try {
            Field field = profile.getClass().getField(key.toUpperCase());
            String type_name = field.getType().getName();

            try {
                if (BuildConfig.DEBUG) Log.d(TAG, "Setting field: " + key);

                if (type_name.equals("java.lang.String")) {
                    field.set(profile,
                            preferences.getString(key, (String) field.get(profile)));
                } else if (type_name.equals("float")) {
                    field.setFloat(profile,
                            (float) preferences.getInt(key,
                                    (int) field.getFloat(profile)));
                } else if (type_name.equals("int")) {
                    field.setInt(profile, preferences.getInt(key, field.getInt(profile)));
                } else if (type_name.equals("long")) {
                    field.setLong(profile, (long) preferences.getInt(key, (int) field.getLong(profile)));
                } else if (type_name.equals("boolean")) {
                    field.setBoolean(profile, preferences.getBoolean(key, field.getBoolean(profile)));
                } else {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "Unknown Type " + type_name + " in field " + key);
                }

            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Error setting field " + key + ": " + e, e);
            }

        } catch (NoSuchFieldException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "No Such Field " + key + ": " + e, e);
        }
    }
}

