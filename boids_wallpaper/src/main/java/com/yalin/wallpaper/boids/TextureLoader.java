package com.yalin.wallpaper.boids;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class TextureLoader {
    private static final String TAG = "TextureLoader";

    private static Context context;
    private static Map<String, Texture> cache;

    public static void init(Context context) {
        TextureLoader.context = context;
        TextureLoader.cache = new HashMap<>();
    }

    public static Texture get(String key) {
        Texture rv = TextureLoader.cache.get(key);

        if (rv == null) {
            rv = openTexture(key);
            cache.put(key, rv);
        }

        return rv;
    }

    private static Texture openTexture(String key) {
        AssetManager asset_manager = context.getAssets();

        Texture rv = null;

        try {
            String texture_path = "textures/" + key + ".png";

            if (BuildConfig.DEBUG) Log.d(TAG, "Opening texture: " + texture_path);

            InputStream in = asset_manager.open(texture_path);
            rv = new Texture(key, BitmapFactory.decodeStream(in));
        } catch (IOException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Could not open texture: " + key);
        }

        return rv;
    }

}
