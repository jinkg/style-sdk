package com.yalin.wallpaper.feather;

import android.os.Build.VERSION;
import android.util.Log;

import com.badlogic1.gdx.Gdx;

public class C0030d {
    public static Boolean f139a;
    public static int f140b;
    public static int f141c;
    public static float f142d;
    public static int f143e = 0;

    public static void m134a(Boolean bool) {
        f139a = bool;
        Log.v("WorldSize", "Landscape: " + String.valueOf(f139a));
    }

    public static void m132a(int i) {
        f143e = i;
    }

    public static void m133a(int i, int i2) {
        int i3 = f139a ? i : i2;
        if (!f139a) {
            i2 = i;
        }
        int e = Gdx.graphics.getWidth();
        int f = Gdx.graphics.getHeight();
        if (f139a) {
            if (e < f) {
                e = Gdx.graphics.getHeight();
                f = Gdx.graphics.getWidth();
            }
        } else if (e > f) {
            e = Gdx.graphics.getHeight();
            f = Gdx.graphics.getWidth();
        }
        int parseInt = Integer.parseInt(VERSION.SDK);
        if (parseInt == 11 || parseInt == 12 || parseInt == 13) {
            if (f139a) {
                if (e == 1232) {
                    e = 1280;
                }
                if (f == 800) {
                    f = 752;
                }
            } else {
                if (e == 752) {
                    e = 800;
                }
                if (f == 1280) {
                    f = 1232;
                }
            }
        }
        float f2 = ((float) i3) / ((float) e);
        float f3 = ((float) i2) / ((float) f);
        if (f2 > f3) {
            f140b = i3;
            f141c = (int) (((float) f) * f2);
        } else {
            f140b = (int) (((float) e) * f3);
            f141c = i2;
        }
        Log.v("WorldSize", String.valueOf(f140b));
        Log.v("WorldSize", String.valueOf(f141c));
        f142d = f140b < f141c ? 30.0f : 15.0f;
    }

    public static int m131a() {
        return f140b;
    }

    public static int m135b() {
        return f141c;
    }

    public static int m136c() {
        return Math.max(f140b, f141c);
    }
}
