package com.yalin.component1;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

/**
 * @author jinyalin
 * @since 2017/7/24.
 */

public class AdvanceGLWallpaperService extends WallpaperServiceProxy {
    public AdvanceGLWallpaperService(Context host) {
        super(host);
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new AdvanceEngine();
    }

    private class AdvanceEngine extends ActiveEngine {
        AdvanceRenderer renderer;

        public AdvanceEngine() {
            super();
            renderer = new AdvanceRenderer(AdvanceGLWallpaperService.this);
            setRenderer(renderer);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            // Add touch events
            setTouchEventsEnabled(true);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            renderer.onTouchEvent(event);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (renderer != null) {
                renderer.release();
            }
            renderer = null;
        }
    }

    public void pickWallpaper(Class target) {

    }
}
