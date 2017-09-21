package com.yalin.wallpaper.hexshader.base.p000a.p001a;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class C0003c {
    private int f4a = m7b();

    public C0003c(Bitmap bitmap) {
        m9a();
        GLUtils.texImage2D(3553, 0, bitmap, 0);
    }

    public static void m6a(int i, int i2) {
        GLES20.glTexParameteri(3553, 10241, i);
        GLES20.glTexParameteri(3553, 10240, i2);
    }

    private int m7b() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    public static void m8b(int i, int i2) {
        GLES20.glTexParameteri(3553, 10242, i);
        GLES20.glTexParameteri(3553, 10243, i2);
    }

    public void m9a() {
        GLES20.glBindTexture(3553, this.f4a);
    }
}
