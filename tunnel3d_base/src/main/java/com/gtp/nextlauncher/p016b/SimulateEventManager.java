package com.gtp.nextlauncher.p016b;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.widget.ImageView;

import com.gtp.nextlauncher.liverpaper.tunnelbate.TunnelRender;

import java.util.EmptyStackException;
import java.util.Stack;

/* compiled from: SimulateEventManager */
public class SimulateEventManager {
    String f497a;
    private C0141g f498b;
    private C0141g f499c;
    private C0142h f500d;
    private C0137b f501e;
    private C0138c f502f;
    private C0138c f503g;
    private C0140e f504h;
    private C0140e f505i;
    private C0139d f506j;
    public Stack<C0136f> f507k;
    public TunnelRender f508l;
    public GLSurfaceView f509m;
    public ImageView f510n;
    public ImageView f511o;
    public ImageView f512p;

    public Stack<C0136f> m814a() {
        return this.f507k;
    }

    public C0137b m815b() {
        if (this.f501e == null) {
            this.f501e = new C0137b(this);
        }
        this.f501e.m826a();
        return this.f501e;
    }

    public C0142h m817c() {
        if (this.f500d == null) {
            this.f500d = new C0142h(this, 500);
            this.f500d.m853a(new float[]{240.0f, 400.0f});
        }
        return this.f500d;
    }

    public C0138c m813a(boolean z) {
        float e = (float) (((double) ((float) this.f508l.m1056e())) * 0.75d);
        float d = (float) (((double) ((float) this.f508l.m1055d())) * 0.25d);
        if (z) {
            if (this.f502f == null) {
                this.f502f = new C0138c(this, true, new float[]{e, 0.0f, 0.0f}, new float[]{d, 0.0f, 0.0f});
            }
            this.f502f.m831a();
            return this.f502f;
        }
        if (this.f503g == null) {
            this.f503g = new C0138c(this, false, new float[]{d, 0.0f, 0.0f}, new float[]{e, 0.0f, 0.0f});
        }
        this.f503g.m831a();
        return this.f503g;
    }

    public C0141g m816b(boolean z) {
        long j = z ? -3000 : 3000;
        if (z) {
            if (this.f498b == null) {
                this.f498b = new C0141g(this, j, z);
            }
            this.f498b.m847a();
            return this.f498b;
        }
        if (this.f499c == null) {
            this.f499c = new C0141g(this, j, false);
        }
        this.f498b.m847a();
        return this.f499c;
    }

    public void m818d() {
        C0136f c0136f = null;
        try {
            c0136f = this.f507k.pop();
        } catch (EmptyStackException e) {
            m809e();
            c0136f = this.f507k.pop();
        }
        this.f508l.m1047a(c0136f);
        c0136f.mo88b(SystemClock.uptimeMillis());
    }

    public void m809e() {
        Stack<C0136f> a = m814a();
        C0141g b = m816b(true);
        C0141g b2 = m816b(false);
        C0142h c = m817c();
        C0137b b3 = m815b();
        C0138c a2 = m813a(true);
        C0138c a3 = m813a(false);
        C0140e c2 = m805c(true);
        C0140e c3 = m805c(false);
        C0139d f = m811f();
        a.push(c3);
        a.push(b3);
        a.push(c);
        a.push(f);
        a.push(c2);
        a.push(b2);
        a.push(a3);
        a.push(f);
        a.push(b);
        a.push(a2);
        a.push(f);
    }

    private C0139d m811f() {
        if (this.f506j == null) {
            this.f506j = new C0139d(this);
        }
        return this.f506j;
    }

    private C0140e m805c(boolean z) {
        if (z) {
            if (this.f504h == null) {
                this.f504h = new C0140e(this, z);
            }
            return this.f504h;
        }
        if (this.f505i == null) {
            this.f505i = new C0140e(this, z);
        }
        return this.f505i;
    }
}
