package com.yalin.wallpaper.hexshader.base.p000a.p001a;

import android.opengl.GLES20;
import android.util.Log;

public class C0004d {
    public static void m10a(String str) {
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError != 0) {
                Log.e("Utils", new StringBuilder(String.valueOf(str)).append(": glError ").append(glGetError).toString());
            } else {
                return;
            }
        }
    }
}
