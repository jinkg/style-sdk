package com.yalin.wallpaper.rain3d;

public class C0871f extends C0867c {
    public boolean f2955g;
    public float f2956h;
    public float f2957i;
    public float f2958j;
    public float f2959k;
    private float[] f2960l;

    public C0871f() {
        this(1.0f, 1.0f, 1, 1);
    }

    public C0871f(float f, float f2, float f3, float f4, float f5, float f6) {
        this(f, f2, 1, 1);
        this.f2957i = f3;
        this.f2958j = f4;
        this.f2959k = f5;
//        this.c = this.c;
        this.f2956h = f6;
    }

    public C0871f(float f, float f2, int i, int i2) {
        this.f2955g = true;
        this.f2960l = new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        float[] fArr = new float[(((i + 1) * (i2 + 1)) * 3)];
        short[] sArr = new short[(((i + 1) * (i2 + 1)) * 6)];
        float f3 = f / -2.0f;
        float f4 = f2 / -2.0f;
        float f5 = f / ((float) i);
        float f6 = f2 / ((float) i2);
        int i3 = 0;
        int i4 = 0;
        short s = (short) (i + 1);
        int i5 = 0;
        while (i5 < i2 + 1) {
            int i6 = i4;
            i4 = 0;
            while (i4 < i + 1) {
                fArr[i3] = (((float) i4) * f5) + f3;
                fArr[i3 + 1] = (((float) i5) * f6) + f4;
                fArr[i3 + 2] = 0.0f;
                i3 += 3;
                int i7 = ((i + 1) * i5) + i4;
                if (i5 < i2 && i4 < i) {
                    sArr[i6] = (short) i7;
                    sArr[i6 + 1] = (short) (i7 + 1);
                    sArr[i6 + 2] = (short) (i7 + s);
                    sArr[i6 + 3] = (short) (i7 + 1);
                    sArr[i6 + 4] = (short) ((i7 + 1) + s);
                    sArr[i6 + 5] = (short) (((i7 + 1) + s) - 1);
                    i6 += 6;
                }
                i4++;
            }
            i5++;
            i4 = i6;
        }
        m4162a(sArr);
        m4161a(fArr);
        m4164b(this.f2960l);
    }
}
