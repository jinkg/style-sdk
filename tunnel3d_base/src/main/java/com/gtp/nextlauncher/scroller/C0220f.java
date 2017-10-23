package com.gtp.nextlauncher.scroller;

import android.view.animation.Interpolator;

/* compiled from: ViscousFluidInterpolater */
public class C0220f implements Interpolator {
    static float f1052a;

    static {
        f1052a = 1.0f;
        f1052a = 1.0f / C0220f.m1314a(1.0f);
    }

    public static float m1314a(float f) {
        float f2 = 8.0f * f;
        if (f2 < 1.0f) {
            f2 -= 1.0f - ((float) Math.exp((double) (-f2)));
        } else {
            f2 = ((1.0f - ((float) Math.exp((double) (1.0f - f2)))) * 0.63212055f) + 0.36787945f;
        }
        return f2 * f1052a;
    }

    public float getInterpolation(float f) {
        return C0220f.m1314a(f);
    }
}
