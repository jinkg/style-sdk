package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;

/* compiled from: MatrixState */
public class MatrixState {
    public static FloatBuffer f806a;
    public static Stack<float[]> f807b = new Stack<>();
    private static final float[] f808c = new float[16];
    private static float[] f809d = new float[16];
    private static float[] f810e;
    private static final float[] f811f = new float[16];
    private static final float[] f812g = new float[16];

    public static void m1079a() {
        f810e = new float[16];
        Matrix.setRotateM(f810e, 0, 0.0f, 1.0f, 0.0f, 0.0f);
    }

    public static void m1085b() {
        f807b.push(f810e.clone());
    }

    public static void m1087c() {
        f810e = f807b.pop();
    }

    public static void m1080a(float f, float f2, float f3) {
        Matrix.translateM(f810e, 0, f, f2, f3);
    }

    public static void m1081a(float f, float f2, float f3, float f4) {
        Matrix.rotateM(f810e, 0, f, f2, f3, f4);
    }

    public static void m1086b(float f, float f2, float f3) {
        Matrix.scaleM(f810e, 0, f, f2, f3);
    }

    public static void m1083a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        Matrix.setLookAtM(f809d, 0, f, f2, f3, f4, f5, f6, f7, f8, f9);
        float[] fArr = new float[]{f, f2, f3};
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(12);
        allocateDirect.order(ByteOrder.nativeOrder());
        f806a = allocateDirect.asFloatBuffer();
        f806a.put(fArr);
        f806a.position(0);
    }

    public static void m1082a(float f, float f2, float f3, float f4, float f5, float f6) {
        Matrix.frustumM(f808c, 0, f, f2, f3, f4, f5, f6);
    }

    public static float[] m1088d() {
        Matrix.multiplyMM(f811f, 0, f809d, 0, f810e, 0);
        Matrix.multiplyMM(f812g, 0, f808c, 0, f811f, 0);
        return f812g;
    }

    public static float[] m1089e() {
        float[] fArr = new float[16];
        Matrix.invertM(fArr, 0, f809d, 0);
        return fArr;
    }

    public static float[] m1084a(float[] fArr) {
        float[] r0 = new float[4];
        Matrix.multiplyMV(r0, 0, MatrixState.m1089e(), 0, new float[]{fArr[0], fArr[1], fArr[2], 1.0f}, 0);
        return new float[]{r0[0], r0[1], r0[2]};
    }
}
