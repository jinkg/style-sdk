package com.yalin.wallpaper.galaxy.base;

import android.util.Log;

import com.badlogic.gdx.Gdx;

public class C0365c {
    public static boolean f818a;
    public static int f819b;
    public static int f820c;
    public static int f821d = 0;

    public static void m1607a(Boolean bool) {
        f818a = bool;
        Log.v("WorldSize", "Landscape: " + String.valueOf(f818a));
    }

    public static void m1605a(int i) {
        f821d = i;
    }

    public static void m1606a(int i, int i2) {
        int i3 = f818a ? i : i2;
        if (!f818a) {
            i2 = i;
        }
        int a = Gdx.graphics.getWidth();
        int b = Gdx.graphics.getHeight();
        if (f818a) {
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
            f819b = i3;
            f820c = (int) (((float) b) * f);
        } else {
            f819b = (int) (((float) a) * f2);
            f820c = i2;
        }
        Log.v("WorldSize", String.valueOf(f819b));
        Log.v("WorldSize", String.valueOf(f820c));
    }

    public static int m1604a() {
        return f819b;
    }

    public static int m1608b() {
        return f820c;
    }

    public static int m1609c() {
        return Math.max(f819b, f820c);
    }
}
