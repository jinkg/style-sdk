package com.yalin.wallpaper.hexshader.base.p000a.p001a;

import android.opengl.GLES20;
import android.util.Log;

public abstract class C0002b {
    private int f1a;
    private int f2b;
    private int f3c;

    public C0002b(String str) {
        String[] split = str.split("====");
        this.f1a = m1a(split[0], split[1]);
    }

    private int m0a(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e("ShaderProgram", "Could not compile shader " + (i == 35633 ? "vertex" : "fragment") + ":");
                Log.e("ShaderProgram", GLES20.glGetShaderInfoLog(glCreateShader));
                GLES20.glDeleteShader(glCreateShader);
                return 0;
            }
        }
        Log.e("ShaderProgram", "Could not compile shader " + i + ":");
        return glCreateShader;
    }

    private int m1a(String str, String str2) {
        this.f2b = m0a(35633, str);
        if (this.f2b == 0) {
            return 0;
        }
        this.f3c = m0a(35632, str2);
        if (this.f3c != 0) {
            int glCreateProgram = GLES20.glCreateProgram();
            if (glCreateProgram == 0) {
                return 0;
            }
            GLES20.glAttachShader(glCreateProgram, this.f2b);
            C0004d.m10a("glAttachVertexShader");
            GLES20.glAttachShader(glCreateProgram, this.f3c);
            C0004d.m10a("glAttachFragmentShader");
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] == 1) {
                return glCreateProgram;
            }
            Log.e("ShaderProgram", "Could not link program: ");
            Log.e("ShaderProgram", GLES20.glGetProgramInfoLog(glCreateProgram));
            GLES20.glDeleteProgram(glCreateProgram);
            GLES20.glDeleteShader(this.f3c);
            this.f3c = 0;
            GLES20.glDeleteShader(this.f2b);
            this.f2b = 0;
            return 0;
        }
        GLES20.glDeleteShader(this.f2b);
        return 0;
    }

    public static void m2b() {
        GLES20.glReleaseShaderCompiler();
    }

    protected int m3a(String str) {
        return GLES20.glGetUniformLocation(this.f1a, str);
    }

    public void m4a() {
        GLES20.glUseProgram(this.f1a);
    }

    protected int m5b(String str) {
        return GLES20.glGetAttribLocation(this.f1a, str);
    }
}
