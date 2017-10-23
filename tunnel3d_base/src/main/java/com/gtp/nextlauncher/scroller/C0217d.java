package com.gtp.nextlauncher.scroller;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/* compiled from: ScreenScroller */
public class C0217d extends C0216c {
    static final /* synthetic */ boolean ae = (!C0217d.class.desiredAssertionStatus());
    protected int f1005A;
    protected int f1006B;
    protected int f1007C;
    protected int f1008D;
    protected int f1009E;
    protected int f1010F;
    protected boolean f1011G;
    protected int f1012H;
    protected int f1013I;
    protected int f1014J;
    protected int f1015K;
    protected int f1016L;
    protected int f1017M;
    protected int f1018N;
    protected int f1019O;
    protected int f1020P;
    protected int f1021Q;
    protected int f1022R;
    protected int f1023S;
    protected int f1024T;
    protected int f1025U;
    boolean f1026V;
    boolean f1027W;
    protected boolean f1028X;
    protected boolean f1029Y;
    protected Drawable f1030Z;
    protected Bitmap aa;
    protected Paint ab;
    protected int ac;
    protected int ad;
    protected C0219b f1031m;
    protected int f1032n;
    protected C0215e f1033o;
    protected Interpolator f1034p;
    protected Interpolator f1035q;
    protected int f1036r;
    protected int f1037s;
    protected int f1038t;
    protected int f1039u;
    protected int f1040v;
    protected float f1041w;
    protected float f1042x;
    protected float f1043y;
    protected float f1044z;

    public /* bridge */ /* synthetic */ boolean mo144g() {
        return super.mo144g();
    }

    public /* bridge */ /* synthetic */ float mo146j() {
        return super.mo146j();
    }

    public C0217d(C0215e c0215e) {
        this(c0215e, null);
    }

    public C0217d(C0215e c0215e, C0219b c0219b) {
        this.f1044z = 0.5f;
        this.f1005A = 1;
        this.f1008D = 1;
        this.f1020P = 1000;
        this.f1021Q = 500;
        this.f1026V = true;
        this.f1027W = true;
        this.ac = 49;
        if (ae || c0215e != null) {
            this.f1035q = C0216c.f990l;
            this.f1034p = this.f1035q;
            this.f1033o = c0215e;
            if (c0219b == null) {
                c0219b = new C0219b();
            }
            this.f1031m = c0219b;
            return;
        }
        throw new AssertionError();
    }

    public void m1274c(int i, int i2) {
        mo142f();
        if (!(this.f1006B == i && this.f1007C == i2) && i > 0 && i2 > 0) {
            this.f1006B = i;
            this.f1007C = i2;
            m1294t();
            m1287m();
        }
    }

    public void mo143f(int i) {
        mo142f();
        if (i != f996f) {
            f995e = 0;
            if (i == 0) {
                this.f1033o.scrollBy(0, -this.f1033o.getScrollY());
            } else {
                this.f1033o.scrollBy(-this.f1033o.getScrollX(), 0);
            }
            f996f = i;
            m1287m();
        }
    }

    protected void m1287m() {
        f995e = 0;
        if (f996f == 0) {
            this.f1008D = this.f1006B;
            this.f1033o.scrollBy(-this.f1033o.getScrollX(), 0);
        } else {
            this.f1008D = this.f1007C;
            this.f1033o.scrollBy(0, -this.f1033o.getScrollY());
        }
        int i = this.f1005A;
        this.f1005A = -1;
        m1280g(i);
    }

    public void m1280g(int i) {
        float f = 0.0f;
        mo142f();
        if (this.f1005A != i && i > 0) {
            this.f1005A = i;
            this.f1043y = this.f1005A > 0 ? 1.0f / ((float) this.f1005A) : 0.0f;
            this.f1038t = this.f1008D * (this.f1005A - 1);
            this.f1040v = this.f1008D * this.f1005A;
            if (this.f1040v > 0) {
                f = 1.0f / ((float) this.f1040v);
            }
            this.f1042x = f;
            float f2 = this.f1044z;
            this.f1044z = -1.0f;
            mo148a(f2);
        }
    }

    public void mo148a(float f) {
        float f2 = 0.0f;
        mo142f();
        if (this.f1044z != f) {
            this.f1044z = Math.max(0.0f, Math.min(f, 0.5f));
            this.f1036r = Math.max(-((int) (((float) this.f1008D) * f)), (-this.f1008D) / 2);
            this.f1037s = Math.min(this.f1038t + ((int) (((float) this.f1008D) * f)), (this.f1038t + (this.f1008D / 2)) - 1);
            this.f1037s = Math.max(this.f1036r, this.f1037s);
            if (this.f1037s > this.f1036r) {
                f2 = 1.0f / ((float) (this.f1037s - this.f1036r));
            }
            this.f1041w = f2;
            mo149a(mo147a() * this.f1008D);
        }
    }

    protected void mo149a(int i) {
        this.f1039u = f995e;
        f995e = i;
        if (f995e != this.f1039u) {
            if (f996f == 0) {
                this.f1033o.scrollBy(f995e - this.f1039u, 0);
            } else {
                this.f1033o.scrollBy(0, f995e - this.f1039u);
            }
            this.f1033o.mo132a(f995e, this.f1039u);
            int i2 = this.f1009E;
            this.f1009E = mo153b(f995e);
            if (this.f1009E != i2) {
                this.f1033o.mo135b(this.f1009E, i2);
            }
        }
    }

    public void m1263a(Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = C0216c.f990l;
        }
        this.f1034p = interpolator;
        this.f1035q = this.f1034p;
    }

    public void m1282h(int i) {
        this.f1020P = Math.max(1, i);
    }

    protected void mo154b(int i, int i2) {
        Interpolator interpolator = this.f1035q;
        if (f995e < 0 || i < 0) {
            i = 0;
            i2 = this.f1021Q;
            interpolator = C0216c.f989k;
        } else if (f995e >= this.f1038t || i >= this.f1005A) {
            i = this.f1005A - 1;
            i2 = this.f1021Q;
            interpolator = C0216c.f989k;
        }
        mo150a(i, i2, interpolator);
    }

    protected int mo157d(int i) {
        return Math.max(0, Math.min(i, this.f1005A - 1));
    }

    protected void mo150a(int i, int i2, Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = C0216c.f990l;
        }
        this.f1034p = interpolator;
        this.f1010F = mo157d(i);
        int i3 = (this.f1010F * this.f1008D) - f995e;
        if (i3 == 0 && mo146j() == 0.0f) {
            f997g = 0;
            this.f1033o.mo131a(mo147a());
            return;
        }
        if (!(this.f1032n == 0 || this.f1034p == f989k)) {
            i2 = Math.min(i2, m1276d(i3, this.f1032n));
            this.f1032n = 0;
        }
        m1240a(f995e, i3, i2);
        this.f1033o.mo137d();
        this.f1033o.postInvalidate();
    }

    public final Interpolator m1288n() {
        return this.f1035q;
    }

    public final int m1289o() {
        return this.f1006B;
    }

    public final int m1290p() {
        return this.f1007C;
    }

    public final int m1291q() {
        return this.f1008D;
    }

    public int mo147a() {
        return this.f1010F;
    }

    public final int m1292r() {
        return (this.f1009E * this.f1008D) - f995e;
    }

    protected int mo153b(int i) {
        return ((this.f1008D / 2) + i) / this.f1008D;
    }

    public boolean m1293s() {
        return this.f1028X;
    }

    public boolean mo139a(MotionEvent motionEvent, int i) {
        this.f1018N = (int) motionEvent.getX();
        this.f1019O = (int) motionEvent.getY();
        int i2 = f996f == 0 ? this.f1018N : this.f1019O;
        int i3 = this.f1017M - i2;
        this.f1017M = i2;
        switch (i) {
            case 0:
                this.f1031m.m1308a();
                this.f1031m.m1311a(motionEvent);
                this.f1013I = this.f1017M;
                this.f1014J = this.f1018N;
                this.f1015K = this.f1019O;
                this.f1016L = f995e;
                this.f1012H = this.f1009E;
                if (f997g == 1) {
                    this.f1033o.mo134b();
                }
                if (f997g != 0) {
                    f997g = 2;
                    break;
                }
                break;
            case 1:
            case 3:
                float b;
                this.f1031m.m1311a(motionEvent);
                this.f1031m.m1309a(1000);
                if (f996f == 0) {
                    b = this.f1031m.m1312b();
                } else {
                    b = this.f1031m.m1313c();
                }
                this.f1032n = (int) b;
                if (this.f1032n <= 500 || this.f1013I >= i2) {
                    if (this.f1032n < -500 && this.f1013I > i2) {
                        mo154b(this.f1012H + 1, this.f1020P);
                        break;
                    }
                    this.f1032n = 500;
                    mo154b(mo153b(f995e), this.f1020P);
                    break;
                }
                mo154b(this.f1012H - 1, this.f1020P);
                break;
            case 2:
                this.f1031m.m1311a(motionEvent);
                mo141e(i3);
                break;
            default:
                return false;
        }
        return true;
    }

    protected int mo156c(int i) {
        return Math.max(this.f1036r - f995e, Math.min(i / 2, this.f1037s - f995e));
    }

    protected void mo141e(int i) {
        int i2 = f995e + i;
        if (i2 < 0 || i2 >= this.f1038t) {
            i = mo156c(i);
        }
        if (i != 0) {
            if (f997g == 0) {
                m1250h();
                this.f1033o.mo136c();
            }
            mo149a(f995e + i);
        }
    }

    protected void mo140b(float f) {
        float interpolation = this.f1034p.getInterpolation(f);
        int round = m1246e() ? f993c : f992b + Math.round(((float) f994d) * interpolation);
        boolean z = !m1246e() && interpolation > 1.0f;
        this.f1011G = z;
        mo149a(round);
        if (m1244d()) {
            this.f1033o.mo131a(mo147a());
        }
        this.f1033o.postInvalidate();
    }

    protected void mo145i() {
        this.f1033o.postInvalidate();
    }

    public void mo142f() {
        if (f997g == 1) {
            super.mo142f();
            mo140b(1.0f);
        }
    }

    public void mo151a(Drawable drawable) {
        this.f1030Z = drawable;
        this.aa = null;
        if (this.f1030Z != null) {
            this.f1022R = this.f1030Z.getIntrinsicWidth();
            this.f1023S = this.f1030Z.getIntrinsicHeight();
            this.f1030Z.setBounds(0, 0, this.f1022R, this.f1023S);
            m1294t();
            if (this.f1030Z instanceof BitmapDrawable) {
                this.aa = ((BitmapDrawable) this.f1030Z).getBitmap();
            }
        }
    }

    public void m1264a(Boolean bool) {
        this.f1026V = bool;
    }

    protected void m1294t() {
        this.f1025U = ((this.f1024T + this.f1023S) - this.f1007C) / 2;
    }

    public void m1284i(int i) {
        if (this.f1027W || i == this.ac) {
            int min = Math.min(i, this.ac);
            if (this.ad != min) {
                this.ad = min;
                if (min <= 0) {
                    m1263a(f990l);
                } else {
                    m1263a(new OvershootInterpolator(C0217d.m1257k(min)));
                }
            }
        }
    }

    public void m1286j(int i) {
        this.ac = Math.max(0, Math.min(i, 49));
        m1284i(this.ac);
    }

    public void m1271b(boolean z) {
        this.f1029Y = z;
    }

    void m1295u() {
    }

    public int mo152b() {
        int i = this.f1009E;
        if (m1292r() > 0) {
            i--;
        }
        if (i < 0 || i >= this.f1005A) {
            return -1;
        }
        return i;
    }

    public int mo155c() {
        int i = this.f1009E;
        int r = m1292r();
        if (r == 0) {
            return -1;
        }
        if (r < 0) {
            i++;
        }
        if (i < 0 || i >= this.f1005A) {
            return -1;
        }
        return i;
    }

    protected int m1276d(int i, int i2) {
        return (int) Math.abs((((this.f1034p.getInterpolation(1.0E-6f) * 1000000.0f) * ((float) i)) * 1000.0f) / ((float) i2));
    }

    public static void m1256a(C0215e c0215e, boolean z) {
        if (c0215e != null) {
            C0217d a = c0215e.mo130a();
            if (a == null || a.m1293s() != z) {
                C0217d c0218a = z ? new C0218a(c0215e) : new C0217d(c0215e);
                c0215e.mo133a(c0218a);
                if (a != null) {
                    C0217d.m1255a(a, c0218a);
                    a.m1295u();
                }
            }
        }
    }

    private static void m1255a(C0217d c0217d, C0217d c0217d2) {
        c0217d2.f1010F = c0217d.mo147a();
        c0217d2.f1024T = c0217d.f1024T;
        c0217d2.f1044z = c0217d.f1044z;
        c0217d2.f1005A = c0217d.f1005A;
        c0217d2.f996f = c0217d.f996f;
        c0217d2.m1274c(c0217d.f1006B, c0217d.f1007C);
        c0217d2.m1263a(c0217d.m1288n());
        c0217d2.m1282h(c0217d.f1020P);
        c0217d2.mo138a(c0217d.f999i);
        c0217d2.mo151a(c0217d.f1030Z);
        c0217d2.m1271b(c0217d.f1029Y);
        c0217d2.m1286j(c0217d.ac);
        c0217d2.m1264a(Boolean.valueOf(c0217d.f1026V));
    }

    private static float m1257k(int i) {
        float[] fArr = new float[]{0.0f, 1.1652954f, 1.7015402f, 2.1642938f, 2.5923889f, 3.0f, 3.3940518f, 3.7784798f, 4.155745f, 4.5274878f, 4.8948593f};
        int max = Math.max(0, Math.min(i, 49));
        int i2 = max / 5;
        return ((fArr[i2 + 1] - fArr[i2]) * ((((float) max) / 5.0f) - ((float) i2))) + fArr[i2];
    }
}
