package com.yalin.component2;


import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.yalin.style.engine.GLWallpaperServiceProxy;

/**
 * @author jinyalin
 * @since 2017/7/24.
 */

public class MyGLWallpaperService extends GLWallpaperServiceProxy {
    public MyGLWallpaperService(Context host) {
        super(host);
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new MyEngineGL();
    }

    private class MyEngineGL extends GLActiveEngine {
        MyRenderer renderer;

        public MyEngineGL() {
            super();
            // handle prefs, other initialization
            renderer = new MyRenderer();
            setRenderer(renderer);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }

        public void onDestroy() {
            super.onDestroy();
            if (renderer != null) {
                renderer.release();
            }
            renderer = null;
        }
    }
}
