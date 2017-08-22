package com.yalin.wallpaper.forest.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.yalin.wallpaper.forest.ForestService;

public class Textures {
    public static int CLOUD;
    public static int RAINBOW;

    public static void loadAll() {
        CLOUD = load("cloud9-64x16.png");
        RAINBOW = load("rainbow-128x32.png");
    }

    private static int load(String filename) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] != 0) {
            Bitmap bitmap = getBitmapFromAsset(filename);
            if (bitmap != null) {
                GLES20.glBindTexture(3553, textureHandle[0]);
                GLES20.glTexParameteri(3553, 10241, 9984);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 10497);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLUtils.texImage2D(3553, 0, bitmap, 0);
                GLES20.glGenerateMipmap(3553);
                bitmap.recycle();
            }
        }
        if (textureHandle[0] != 0) {
            return textureHandle[0];
        }
        throw new RuntimeException("Error loading texture.");
    }

    public static void use(int texture) {
        GLES20.glBindTexture(3553, texture);
    }

    public static Bitmap getBitmapFromAsset(String filename) {
        try {
            return BitmapFactory.decodeStream(ForestService.context().getAssets().open(filename));
        } catch (Throwable e) {
            return null;
        }
    }
}
