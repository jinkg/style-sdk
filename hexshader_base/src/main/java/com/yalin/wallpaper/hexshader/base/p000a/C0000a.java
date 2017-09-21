package com.yalin.wallpaper.hexshader.base.p000a;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yalin.wallpaper.hexshader.base.p000a.p002b.C0007c;


public class C0000a extends GLSurfaceView {
    private C0007c f0a = new C0007c();

    public C0000a(Context context, Renderer renderer) {
        super(context);
        setupRenderer(renderer);
    }

    public void onPause() {
        this.f0a.m20a();
    }

    public void onResume() {
        this.f0a.m21a((GLSurfaceView) this);
    }

    protected void setupRenderer(Renderer renderer) {
        setRenderer(renderer);
        setRenderMode(0);
    }
}
