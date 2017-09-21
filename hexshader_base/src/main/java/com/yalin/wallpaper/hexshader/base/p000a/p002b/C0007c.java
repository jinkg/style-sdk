package com.yalin.wallpaper.hexshader.base.p000a.p002b;

import android.opengl.GLSurfaceView;
import android.os.Handler;

public class C0007c {
    public GLSurfaceView f15a;
    public int f16b;
    public Handler f17c = new Handler();
    public Runnable f18d = new C0008d(this);

    public void m20a() {
        this.f17c.removeCallbacks(this.f18d);
    }

    public void m21a(GLSurfaceView gLSurfaceView) {
        m22a(gLSurfaceView, 33);
    }

    public void m22a(GLSurfaceView gLSurfaceView, int i) {
        this.f15a = gLSurfaceView;
        this.f16b = i;
        this.f17c.postDelayed(this.f18d, (long) i);
    }
}
