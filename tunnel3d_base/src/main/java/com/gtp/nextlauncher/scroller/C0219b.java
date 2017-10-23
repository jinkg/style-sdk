package com.gtp.nextlauncher.scroller;

import android.view.MotionEvent;

/* compiled from: FastVelocityTracker */
public class C0219b {
    final float[] f1046a = new float[10];
    final float[] f1047b = new float[10];
    final long[] f1048c = new long[10];
    float f1049d;
    float f1050e;
    int f1051f;

    public void m1308a() {
        long[] jArr = this.f1048c;
        for (int i = 0; i < 10; i++) {
            jArr[i] = Long.MIN_VALUE;
        }
    }

    public void m1311a(MotionEvent motionEvent) {
        int historySize = motionEvent.getHistorySize();
        int i = this.f1051f + 1;
        this.f1051f = i;
        if (i >= 10) {
            this.f1051f = 0;
        }
        for (i = 0; i < historySize; i++) {
            this.f1046a[this.f1051f] = motionEvent.getHistoricalX(i);
            this.f1047b[this.f1051f] = motionEvent.getHistoricalY(i);
            this.f1048c[this.f1051f] = motionEvent.getHistoricalEventTime(i);
            int i2 = this.f1051f + 1;
            this.f1051f = i2;
            if (i2 >= 10) {
                this.f1051f = 0;
            }
        }
        this.f1046a[this.f1051f] = motionEvent.getX();
        this.f1047b[this.f1051f] = motionEvent.getY();
        this.f1048c[this.f1051f] = motionEvent.getEventTime();
    }

    public void m1309a(int i) {
        m1310a(i, Float.MAX_VALUE);
    }

    public void m1310a(int i, float f) {
        float f2;
        int i2;
        int i3;
        float[] fArr = this.f1046a;
        float[] fArr2 = this.f1047b;
        long[] jArr = this.f1048c;
        int i4 = this.f1051f;
        if (jArr[i4] != Long.MIN_VALUE) {
            f2 = (float) (jArr[i4] - 200);
            i2 = ((i4 + 10) - 1) % 10;
            i3 = i4;
            while (((float) jArr[i2]) >= f2 && i2 != i4) {
                i3 = i2;
                i2 = ((i2 + 10) - 1) % 10;
            }
        } else {
            i3 = i4;
        }
        float f3 = fArr[i3];
        float f4 = fArr2[i3];
        long j = jArr[i3];
        f2 = 0.0f;
        float f5 = 0.0f;
        i2 = (((i4 - i3) + 10) % 10) + 1;
        if (i2 > 3) {
            i2--;
        }
        int i5 = 1;
        while (i5 < i2) {
            float f6;
            int i6 = (i3 + i5) % 10;
            int i7 = (int) (jArr[i6] - j);
            if (i7 == 0) {
                f6 = f2;
                f2 = f5;
            } else {
                f6 = ((fArr[i6] - f3) / ((float) i7)) * ((float) i);
                if (f2 != 0.0f) {
                    f6 = (f6 + f2) * 0.5f;
                }
                f2 = ((fArr2[i6] - f4) / ((float) i7)) * ((float) i);
                if (f5 != 0.0f) {
                    f2 = (f2 + f5) * 0.5f;
                }
            }
            i5++;
            f5 = f2;
            f2 = f6;
        }
        this.f1050e = f2 < 0.0f ? Math.max(f2, -f) : Math.min(f2, f);
        this.f1049d = f5 < 0.0f ? Math.max(f5, -f) : Math.min(f5, f);
    }

    public float m1312b() {
        return this.f1050e;
    }

    public float m1313c() {
        return this.f1049d;
    }
}
