package com.yalin.wallpaper.forest;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

import com.yalin.wallpaper.forest.core.gl.GLWallpaperService;

public abstract class OpenGLES2WallpaperService extends GLWallpaperService {

    public OpenGLES2WallpaperService(Context host) {
        super(host);
    }

    private class OpenGLES2Engine extends GLEngine {

        OpenGLES2Engine() {
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            if (((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                    .getDeviceConfigurationInfo().reqGlEsVersion >= 131072) {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(OpenGLES2WallpaperService.this.getNewRenderer());
            } else {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(OpenGLES2WallpaperService.this.getNewRenderer());
            }
        }
    }

    protected abstract Renderer getNewRenderer();

    public Engine onCreateEngine() {
        return new OpenGLES2Engine();
    }
}
