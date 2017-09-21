package com.yalin.wallpaper.hexshader.base.p003b;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class C0009a {
    public static String m23a(String str, AssetManager assetManager) {
        InputStream closeable = null;
        try {
            closeable = assetManager.open(str);
            String b = C0013e.m33b(closeable);
            C0013e.m31a(closeable);
            return b;
        } catch (Throwable e) {
            C0013e.m31a(closeable);
            return null;
        }
    }

    public static Bitmap m24b(String str, AssetManager assetManager) {
        InputStream closeable = null;
        try {
            closeable = assetManager.open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(closeable);
            C0013e.m31a(closeable);
            return decodeStream;
        } catch (Throwable e) {
            C0013e.m31a(closeable);
            return null;
        }
    }
}
