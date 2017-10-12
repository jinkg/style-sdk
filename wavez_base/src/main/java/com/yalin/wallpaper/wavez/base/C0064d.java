package com.yalin.wallpaper.wavez.base;

import android.util.Log;
import com.badlogic.gdx.Gdx;

public class C0064d {
    public static boolean f152a;
    public static int f153b;
    public static int f154c;
    public static int f155d = 0;

    public static void m177a(Boolean bool) {
        f152a = bool;
        Log.v("WorldSize", "Landscape: " + String.valueOf(f152a));
    }

    public static void m175a(int i) {
        f155d = i;
    }

    public static void m176a(int i, int i2) {
        int i3 = f152a ? i : i2;
        if (!f152a) {
            i2 = i;
        }
        int a = Gdx.graphics.getWidth();
        int b = Gdx.graphics.getHeight();
        if (f152a) {
            if (a < b) {
                a = Gdx.graphics.getHeight();
                b = Gdx.graphics.getWidth();
            }
        } else if (a > b) {
            a = Gdx.graphics.getHeight();
            b = Gdx.graphics.getWidth();
        }
        float f = ((float) i3) / ((float) a);
        float f2 = ((float) i2) / ((float) b);
        if (f > f2) {
            f153b = i3;
            f154c = (int) (((float) b) * f);
        } else {
            f153b = (int) (((float) a) * f2);
            f154c = i2;
        }
        Log.v("WorldSize", String.valueOf(f153b));
        Log.v("WorldSize", String.valueOf(f154c));
    }

    public static int m174a() {
        return f153b;
    }

    public static int m178b() {
        return f154c;
    }

    public static int m179c() {
        return Math.max(f153b, f154c);
    }
}
