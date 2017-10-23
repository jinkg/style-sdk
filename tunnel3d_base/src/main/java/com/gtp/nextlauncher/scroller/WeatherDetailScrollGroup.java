package com.gtp.nextlauncher.scroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class WeatherDetailScrollGroup extends FrameLayout implements C0215e {
    int f980a;
    int f981b;
    private int f982c = 600;
    private Point f983d;
    private boolean f984e;
    private int f985f;
    private C0217d f986g = null;
    private int f987h = 0;
    private C0161g f988i = null;

    public WeatherDetailScrollGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        m1225f();
    }

    private void m1225f() {
        this.f984e = false;
        this.f983d = new Point();
        this.f985f = 15;
        this.f986g = new C0217d(this);
        this.f986g.m1286j(0);
        this.f986g.m1282h(this.f982c);
        this.f986g.mo143f(0);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int i5 = i4 - i2;
        int paddingLeft2 = (i3 - i) - (getPaddingLeft() + getPaddingRight());
        this.f980a = paddingLeft2;
        i5 -= getPaddingTop() + getPaddingBottom();
        this.f981b = i5;
        int childCount = getChildCount();
        for (int i6 = 0; i6 < childCount; i6++) {
            getChildAt(i6).layout(paddingLeft, paddingTop, paddingLeft + paddingLeft2, paddingTop + i5);
            paddingLeft += paddingLeft2;
        }
    }

    public void computeScroll() {
        this.f986g.mo144g();
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.f986g.m1244d()) {
            super.dispatchDraw(canvas);
            return;
        }
        int r = this.f986g.m1292r();
        int b = this.f986g.mo152b();
        int c = this.f986g.mo155c();
        int q = this.f986g.m1291q();
        if (r > 0) {
            r -= q;
        }
        if (r == 0) {
            m1224a(canvas, b, r);
            return;
        }
        m1224a(canvas, b, r);
        m1224a(canvas, c, r + q);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.f984e = false;
            this.f983d.set((int) motionEvent.getX(), (int) motionEvent.getY());
            this.f986g.mo139a(motionEvent, motionEvent.getAction());
            return super.onInterceptTouchEvent(motionEvent);
        } else if (motionEvent.getAction() == 2) {
            if (!this.f984e && Math.abs(motionEvent.getX() - ((float) this.f983d.x)) > ((float) this.f985f)) {
                this.f984e = true;
            }
            return this.f984e;
        } else if (motionEvent.getAction() != 1) {
            return super.onInterceptTouchEvent(motionEvent);
        } else {
            this.f986g.mo139a(motionEvent, motionEvent.getAction());
            return this.f984e;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.f986g.mo139a(motionEvent, motionEvent.getAction());
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.f986g.m1274c(i - (getPaddingLeft() + getPaddingRight()), i2 - (getPaddingTop() + getPaddingBottom()));
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void mo136c() {
        if (this.f988i != null) {
            this.f988i.mo96a(this, this.f987h);
        }
    }

    public void mo137d() {
        if (this.f988i != null) {
            this.f988i.mo94a();
        }
    }

    public void mo132a(int i, int i2) {
        if (this.f988i != null) {
            this.f988i.mo95a(i, i2);
        }
    }

    public void mo135b(int i, int i2) {
        this.f987h = i;
        if (this.f988i != null) {
            this.f988i.mo97b(this, this.f987h);
        }
    }

    public void mo131a(int i) {
        this.f987h = i;
        if (this.f988i != null) {
            this.f988i.mo98c(this, this.f987h);
        }
    }

    public void m1238e() {
        this.f986g.m1280g(getChildCount());
    }

    public C0217d mo130a() {
        return this.f986g;
    }

    public void mo133a(C0217d c0217d) {
        this.f986g = c0217d;
    }

    public void mo134b() {
    }

    public void m1229a(Canvas canvas, int i) {
        if (this.f986g.mo146j() != 0.0f) {
            this.f986g.mo138a(false);
        }
        View childAt = getChildAt(i);
        if (childAt != null && !this.f986g.m1244d()) {
            childAt.draw(canvas);
        }
    }

    public void m1234b(int i) {
        this.f986g.mo150a(i, this.f982c, null);
    }

    public void m1231a(C0161g c0161g) {
        this.f988i = c0161g;
    }

    private void m1224a(Canvas canvas, int i, int i2) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int k = this.f986g.m1253k();
        int o = this.f986g.m1289o();
        int p = this.f986g.m1290p();
        canvas.save();
        if (this.f986g.m1254l() == 0) {
            canvas.translate((float) (k + i2), 0.0f);
        } else {
            canvas.translate(0.0f, (float) (k + i2));
        }
        canvas.clipRect(paddingLeft, paddingTop, paddingLeft + o, paddingTop + p);
        canvas.translate((float) paddingLeft, (float) paddingTop);
        m1229a(canvas, i);
        canvas.translate((float) (-paddingLeft), (float) (-paddingTop));
        canvas.restore();
    }

    public void m1232a(boolean z) {
        C0217d.m1256a((C0215e) this, z);
    }
}
