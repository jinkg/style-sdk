package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public abstract class SimpleGLWallpaperService extends WallpaperService {

    public class GLEngine extends Engine {
        public final GLSurfaceView glSurface;

        public GLEngine() {
            super();
            this.glSurface = new GLSurfaceView(SimpleGLWallpaperService.this) {
                public SurfaceHolder getHolder() {
                    return GLEngine.this.getSurfaceHolder();
                }
            };
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            this.glSurface.surfaceCreated(holder);
        }

        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                this.glSurface.onResume();
            } else {
                this.glSurface.onPause();
            }
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.glSurface.surfaceChanged(holder, format, width, height);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            this.glSurface.surfaceDestroyed(holder);
        }
    }
}
