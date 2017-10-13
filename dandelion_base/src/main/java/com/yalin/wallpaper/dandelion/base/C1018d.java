package com.yalin.wallpaper.dandelion.base;

import android.graphics.Point;
import android.util.Log;

import com.badlogic.gdx.Gdx;

public class C1018d {
    public static boolean f2709a;
    public static int f2710b;
    public static int f2711c;
    public static int f2712d = 0;

    public static void m5172a(Boolean bool) {
        f2709a = bool;
        Log.v("WorldSize", "Landscape: " + String.valueOf(f2709a));
    }

    public static void m5170a(int i) {
        f2712d = i;
    }

    public static void m5171a(int i, int i2) {
        int i3 = f2709a ? i : i2;
        if (!f2709a) {
            i2 = i;
        }
        int a = Gdx.graphics.getWidth();
        int b = Gdx.graphics.getHeight();
        if (f2709a) {
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
            f2710b = i3;
            f2711c = (int) (((float) b) * f);
        } else {
            f2710b = (int) (((float) a) * f2);
            f2711c = i2;
        }
        Log.v("WorldSize", String.valueOf(f2710b));
        Log.v("WorldSize", String.valueOf(f2711c));
    }

    public static int m5168a() {
        return f2710b;
    }

    public static int m5173b() {
        return f2711c;
    }

    public static Point m5169a(int i, int i2, int i3, int i4) {
        float f = ((float) i4) / ((float) i3);
        float f2 = ((float) i2) / ((float) i);
        Point point = new Point(0, 0);
        if (f <= f2) {
            point.x = i3;
            point.y = (int) (((float) i3) * f2);
        } else if (f > f2) {
            point.x = (int) (((float) i4) / f2);
            point.y = i4;
        }
        return point;
    }
}
