package com.gtp.nextlauncher.scroller;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;

/* compiled from: CycloidScreenScroller */
class C0218a extends C0217d {
    boolean f1045a;

    static final int m1296a(int i, int i2) {
        if (i < 0) {
            return i + i2;
        }
        if (i >= i2) {
            return i - i2;
        }
        return i;
    }

    public C0218a(C0215e c0215e) {
        super(c0215e);
        this.f1045a = true;
        f1028X = true;
    }

    public void mo148a(float f) {
        if (f1040v > 0 && f1044z != 0.5f) {
            mo142f();
            f1044z = 0.5f;
            f1036r = (-f1008D) / 2;
            f1037s = f1040v + f1036r;
            f1041w= f1037s > f1036r ? 1.0f / ((float) (f1037s - f1036r)) : 0.0f;
            mo149a(mo147a() * f1008D);
        }
    }

    protected void mo149a(int i) {
        if (f1005A > 1) {
            if (i < f1036r) {
                i += f1040v;
            } else if (i >= f1037s) {
                i -= f1040v;
            }
        }
        super.mo149a(i);
    }

    protected int mo153b(int i) {
        int b = super.mo153b(i);
        return (b < 0 || b >= f1005A) ? 0 : b;
    }

    protected int mo156c(int i) {
        if (f1005A < 2) {
            return super.mo156c(i);
        }
        int i2 = f995e + i;
        if (i2 < f1036r) {
            return i + f1040v;
        }
        if (i2 >= f1037s) {
            return i - f1040v;
        }
        return i;
    }

    protected void mo154b(int i, int i2) {
        mo150a(i, i2, f1035q);
    }

    protected int mo157d(int i) {
        return i;
    }

    public void mo150a(int i, int i2, Interpolator interpolator) {
        if (i > f1009E && (i - f1009E) * 2 > f1005A) {
            i -= f1005A;
        } else if (i < f1009E && (f1009E - i) * 2 > f1005A) {
            i += f1005A;
        }
        super.mo150a(i, i2, interpolator);
    }

    public int mo147a() {
        return C0218a.m1296a(f1010F, f1005A);
    }

    public void mo151a(Drawable drawable) {
        super.mo151a(drawable);
        if (this.aa != null) {
            this.ab = new Paint();
            this.f1045a = true;
            return;
        }
        this.ab = null;
        this.f1045a = false;
    }

    public int mo152b() {
        int i = f1009E;
        if (m1292r() > 0) {
            i--;
        }
        int a = C0218a.m1296a(i, f1005A);
        if (f1005A >= 2 || a == i) {
            return a;
        }
        return -1;
    }

    public int mo155c() {
        int i = f1009E;
        int r = m1292r();
        if (r == 0) {
            return -1;
        }
        if (r < 0) {
            i++;
        }
        r = C0218a.m1296a(i, f1005A);
        if (f1005A >= 2 || r == i) {
            return r;
        }
        return -1;
    }
}
