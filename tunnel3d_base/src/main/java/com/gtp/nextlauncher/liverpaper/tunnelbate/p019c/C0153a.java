package com.gtp.nextlauncher.liverpaper.tunnelbate.p019c;

import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.C0184e;

/* compiled from: CalculateUtil */
public class C0153a {
    public static float[] m916a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = ((((f4 / 2.0f) - f2) * 2.0f) * f6) / f4;
        float f10 = f8 / f7;
        f10 = -f7;
        float[] a = C0184e.m1084a(new float[]{(((f - (f3 / 2.0f)) * 2.0f) * f5) / f3, f9, f10});
        return new float[]{a[0], a[1], a[2]};
    }

    public static float getStarSpeed() {
        return 1;
    }

    public static float getAutoRunSpeed() {
        return 1;
    }

    public static float getSensorSpeed() {
        return 1 * 0.5f;
    }

    public static float m919d() {
        return 0.08f;
    }
}
