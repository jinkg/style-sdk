package com.yalin.wallpaper.hexshader_galaxy;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yalin.wallpaper.hexshader.base.hexshaders.HexRenderer;
import com.yalin.wallpaper.hexshader.base.p004c.BaseWallpaperService;
import com.yalin.wallpaper.hexshader.base.p004c.C0017d;


public class LiveWallpaper extends BaseWallpaperService {
    private HexRenderer f56a;

    public LiveWallpaper(Context host) {
        super(host);
    }

    public C0017d mo2a(GLSurfaceView gLSurfaceView) {
        Config config = new Config(this);
        gLSurfaceView.setEGLContextClientVersion(2);
        this.f56a = new HexRenderer(getAssets(), config.getType(),
                (float) ((config.getDetailLevel() * 16) + 16),
                (((float) config.getTimeScale()) * 0.2f) + 0.2f);
        gLSurfaceView.setRenderer(this.f56a);
        return this.f56a;
    }
}
