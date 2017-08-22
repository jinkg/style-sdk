package com.yalin.wallpaper.forest;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.yalin.wallpaper.forest.core.ForestRenderer;


public class ForestService extends OpenGLES2WallpaperService {
    private static Context CONTEXT;

    public ForestService(Context host) {
        super(host);
        CONTEXT = host;
    }

    public static Context context() {
        return CONTEXT;
    }

    protected Renderer getNewRenderer() {
        return new ForestRenderer(this);
    }
}
