package com.gtp.nextlauncher.liverpaper.tunnelbate.p019c;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/* compiled from: ShaderUtil */
public class C0158f {
    public static int m925a(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e("ES20_ERROR", "Could not compile shader " + i + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(glCreateShader));
                GLES20.glDeleteShader(glCreateShader);
                return 0;
            }
        }
        return glCreateShader;
    }

    public static int m926a(String str, String str2) {
        int a = C0158f.m925a(35633, str);
        if (a == 0) {
            return 0;
        }
        int a2 = C0158f.m925a(35632, str2);
        if (a2 == 0) {
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, a);
//            C0158f.m928a("glAttachShader");
            GLES20.glGetError();
            GLES20.glAttachShader(glCreateProgram, a2);
//            C0158f.m928a("glAttachShader");
            GLES20.glGetError();
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(glCreateProgram));
                GLES20.glDeleteProgram(glCreateProgram);
                return 0;
            }
        }
        return glCreateProgram;
    }

    public static void m928a(String str) {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            Log.e("ES20_ERROR", str + ": glError " + glGetError);
            throw new RuntimeException(str + ": glError " + glGetError);
        }
    }

    public static String m927a(String str, Resources resources) {
        String replaceAll;
        Exception exception;
        try {
            InputStream open = resources.getAssets().open(str);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int read = open.read();
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(read);
            }
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            open.close();
            String str2 = new String(toByteArray, "UTF-8");
            try {
                replaceAll = str2.replaceAll("\\r\\n", "\n");
            } catch (Exception e) {
                exception = e;
                replaceAll = str2;
                exception.printStackTrace();
                return replaceAll;
            }
        } catch (Exception e2) {
            Exception exception2 = e2;
            replaceAll = null;
            exception = exception2;
            exception.printStackTrace();
            return replaceAll;
        }
        return replaceAll;
    }
}
