package com.yalin.wallpaper.hexshader.base.p003b;

import android.os.SystemClock;

public class C0010b {
    private C0011c f20a;
    private int f21b;
    private int f22c;
    private long f23d;
    private long f24e;
    private float f25f;
    private long f26g;
    private String f27h;

    public C0010b() {
        this(null);
    }

    public C0010b(C0011c c0011c) {
        this(c0011c, 3000);
    }

    public C0010b(C0011c c0011c, int i) {
        this.f20a = null;
        this.f22c = 0;
        this.f25f = 0.0f;
        this.f26g = 0;
        this.f27h = "";
        this.f20a = c0011c;
        this.f21b = i;
        this.f23d = SystemClock.elapsedRealtime();
    }

    public void m25a() {
        this.f24e = SystemClock.elapsedRealtime();
    }

    public String m26b() {
        this.f22c++;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.f23d;
        this.f26g += elapsedRealtime - this.f24e;
        if (j > ((long) this.f21b)) {
            this.f25f = ((float) (this.f22c * 1000)) / ((float) j);
            this.f23d = elapsedRealtime;
            this.f27h = String.format("%4.1f %d", Float.valueOf(this.f25f),
                    Long.valueOf(this.f26g / ((long) this.f22c)));
            this.f22c = 0;
            this.f26g = 0;
            if (this.f20a != null) {
                this.f20a.mo4a(this);
            }
        }
        return this.f27h;
    }
}
