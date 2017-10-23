package com.gtp.nextlauncher.liverpaper.tunnelbate.p019c;

import android.view.MotionEvent;

/* compiled from: FastVelocityTracker */
public class C0155c {
    final float[] f596a = new float[10];
    final float[] f597b = new float[10];
    final long[] f598c = new long[10];
    float f599d;
    float f600e;
    int f601f;

    public void m922a(MotionEvent motionEvent) {
        int historySize = motionEvent.getHistorySize();
        int i = this.f601f + 1;
        this.f601f = i;
        if (i >= 10) {
            this.f601f = 0;
        }
        for (i = 0; i < historySize; i++) {
            this.f596a[this.f601f] = motionEvent.getHistoricalX(i);
            this.f597b[this.f601f] = motionEvent.getHistoricalY(i);
            this.f598c[this.f601f] = motionEvent.getHistoricalEventTime(i);
            int i2 = this.f601f + 1;
            this.f601f = i2;
            if (i2 >= 10) {
                this.f601f = 0;
            }
        }
        this.f596a[this.f601f] = motionEvent.getX();
        this.f597b[this.f601f] = motionEvent.getY();
        this.f598c[this.f601f] = motionEvent.getEventTime();
    }

    public void m921a(int i, float f) {
        float f2;
        int i2;
        int i3;
        float[] fArr = this.f596a;
        float[] fArr2 = this.f597b;
        long[] jArr = this.f598c;
        int i4 = this.f601f;
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
        this.f600e = f2 < 0.0f ? Math.max(f2, -f) : Math.min(f2, f);
        this.f599d = f5 < 0.0f ? Math.max(f5, -f) : Math.min(f5, f);
    }

    public float m920a() {
        return this.f600e;
    }
}
