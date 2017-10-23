package com.gtp.nextlauncher.scroller;

import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/* compiled from: MScroller */
abstract class C0216c {
    protected static final Interpolator f989k = new C0220f();
    protected static final Interpolator f990l = new OvershootInterpolator(0.0f);
    private long f991a;
    protected int f992b;
    protected int f993c;
    protected int f994d;
    protected int f995e;
    protected int f996f;
    protected int f997g;
    protected float f998h;
    protected boolean f999i = false;
    protected long f1000j;
    private int f1001m;
    private float f1002n;
    private int f1003o;
    private float f1004p;

    C0216c() {
    }

    public final boolean m1244d() {
        return this.f997g == 0;
    }

    protected final boolean m1246e() {
        return this.f1003o >= this.f1001m;
    }

    public final int m1239a(long j) {
        if (this.f991a != -1) {
            return (int) (j - this.f991a);
        }
        this.f991a = j;
        return 0;
    }

    public void mo142f() {
        if (this.f997g == 1) {
            this.f997g = 0;
        }
    }

    public boolean mo144g() {
        long currentAnimationTimeMillis;
        int i;
        switch (this.f997g) {
            case 1:
                currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                this.f1003o = m1239a(currentAnimationTimeMillis);
                if (this.f1003o >= this.f1001m) {
                    this.f1003o = this.f1001m;
                    if (!this.f999i || this.f998h <= 0.0f) {
                        this.f997g = 0;
                    }
                }
                if (this.f999i && currentAnimationTimeMillis >= this.f1000j) {
                    i = (int) (currentAnimationTimeMillis - this.f1000j);
                    this.f1000j = currentAnimationTimeMillis;
                    this.f998h = Math.max(0.0f, this.f998h - (((float) i) / 200.0f));
                    mo145i();
                }
                this.f1004p = ((float) this.f1003o) * this.f1002n;
                mo140b(this.f1004p);
                return true;
            case 2:
                if (!this.f999i || this.f998h >= 1.0f) {
                    return false;
                }
                currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                i = (int) (currentAnimationTimeMillis - this.f1000j);
                this.f1000j = currentAnimationTimeMillis;
                this.f998h = Math.min(1.0f, (((float) i) / 200.0f) + this.f998h);
                mo145i();
                return true;
            default:
                return false;
        }
    }

    protected void m1240a(int i, int i2, int i3) {
        this.f997g = 1;
        this.f1004p = 0.0f;
        this.f1001m = i3;
        this.f991a = -1;
        this.f992b = i;
        this.f994d = i2;
        this.f993c = i + i2;
        this.f1002n = 1.0f / ((float) this.f1001m);
        this.f1000j = AnimationUtils.currentAnimationTimeMillis() + 100;
    }

    protected void mo141e(int i) {
    }

    protected void m1250h() {
        if (this.f997g == 0) {
            this.f1000j = AnimationUtils.currentAnimationTimeMillis();
        }
        this.f997g = 2;
    }

    protected void mo140b(float f) {
    }

    protected void mo145i() {
    }

    public boolean mo139a(MotionEvent motionEvent, int i) {
        return false;
    }

    public void mo138a(boolean z) {
        this.f999i = z;
    }

    public float mo146j() {
        if (!this.f999i) {
            return 0.0f;
        }
        float f = 1.0f - this.f998h;
        return 1.0f - (f * f);
    }

    public final int m1253k() {
        return this.f995e;
    }

    public void mo143f(int i) {
        this.f996f = i;
    }

    public final int m1254l() {
        return this.f996f;
    }
}
