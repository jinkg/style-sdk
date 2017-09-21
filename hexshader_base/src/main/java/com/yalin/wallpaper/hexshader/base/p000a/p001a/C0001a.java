package com.yalin.wallpaper.hexshader.base.p000a.p001a;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.yalin.wallpaper.hexshader.base.p000a.C0000a;


public class C0001a extends C0000a {
    public C0001a(Context context, Renderer renderer) {
        super(context, renderer);
    }

    protected void setupRenderer(Renderer renderer) {
        setEGLContextClientVersion(2);
        setRenderer(renderer);
        setRenderMode(0);
    }
}
