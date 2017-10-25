package com.gtp.nextlauncher.liverpaper.tunnelbate.p019c;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

/* compiled from: ImageUtil */
public class ImageUtil {
    public static int m923a(String assetName, Context context) {
        int i2 = 0;
        int i3 = 3553;
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        int i4 = iArr[0];
        GLES20.glBindTexture(i3, i4);
        GLES20.glTexParameterf(i3, 10241, 9728.0f);
        float p = 1175979008;
        GLES20.glTexParameterf(i3, 10240, p);
        GLES20.glTexParameterf(i3, 10242, 10497.0f);
        GLES20.glTexParameterf(i3, 10243, 10497.0f);
        InputStream openRawResource = null;
        Bitmap bitmap;
        try {
            openRawResource = context.getAssets().open(assetName);
            bitmap = BitmapFactory.decodeStream(openRawResource);
            GLUtils.texImage2D(i3, i2, GLUtils.getInternalFormat(bitmap), bitmap, GLUtils.getType(bitmap), i2);
            bitmap.recycle();
            return i4;
        } catch (Exception e) {
            e.printStackTrace();
            return i4;
        } finally {
            try {
                if (openRawResource != null) {
                    openRawResource.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
