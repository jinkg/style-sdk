package com.yalin.wallpaper.hexshader.base.p000a.p002b;

public class C0006b {
    public static final float f5a = C0006b.m15a("0000");
    public static final float f6b = C0006b.m15a("ffff");
    public static final float f7c = C0006b.m15a("000f");
    public static final float f8d = C0006b.m15a("f00f");
    public static final float f9e = C0006b.m15a("0f0f");
    public static final float f10f = C0006b.m15a("00ff");
    public static final float f11g = C0006b.m15a("0fff");
    public static final float f12h = C0006b.m15a("f0ff");
    public static final float f13i = C0006b.m15a("ff0f");
    public static final float f14j = C0006b.m15a("777f");

    public static float m12a(float f) {
        return f < 0.0f ? 0.0f : f > 1.0f ? 1.0f : f;
    }

    public static float m13a(float f, float f2, float f3, float f4) {
        return C0006b.m14a((int) (C0006b.m12a(f) * 255.0f),
                (int) (C0006b.m12a(f2) * 255.0f), (int) (C0006b.m12a(f3) * 255.0f),
                (int) (C0006b.m12a(f4) * 255.0f));
    }

    public static float m14a(int i, int i2, int i3, int i4) {
        return Float.intBitsToFloat((((i4 << 24) | (i3 << 16)) | (i2 << 8)) | i);
    }

    public static float m15a(String str) {
        switch (str.length()) {
            case 3:
                return C0006b.m13a(((float) Integer.valueOf(str.substring(0, 1), 16)) / 15.0f,
                        ((float) Integer.valueOf(str.substring(1, 2), 16)) / 15.0f,
                        ((float) Integer.valueOf(str.substring(2, 3), 16)) / 15.0f, 1.0f);
            case 4:
                return C0006b.m13a(((float) Integer.valueOf(str.substring(0, 1), 16)) / 15.0f,
                        ((float) Integer.valueOf(str.substring(1, 2), 16)) / 15.0f,
                        ((float) Integer.valueOf(str.substring(2, 3), 16)) / 15.0f,
                        ((float) Integer.valueOf(str.substring(3, 4), 16)) / 15.0f);
            case 6:
                return C0006b.m13a(((float) Integer.valueOf(str.substring(0, 2), 16)) / 255.0f,
                        ((float) Integer.valueOf(str.substring(2, 4), 16)) / 255.0f,
                        ((float) Integer.valueOf(str.substring(4, 6), 16)) / 255.0f, 1.0f);
            case 8:
                return C0006b.m13a(((float) Integer.valueOf(str.substring(0, 2), 16)) / 255.0f,
                        ((float) Integer.valueOf(str.substring(2, 4), 16)) / 255.0f,
                        ((float) Integer.valueOf(str.substring(4, 6), 16)) / 255.0f,
                        ((float) Integer.valueOf(str.substring(6, 8), 16)) / 255.0f);
            default:
                return f7c;
        }
    }
}
