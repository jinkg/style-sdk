package com.yalin.component2;


import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.yalin.style.engine.WallpaperServiceProxy;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

/**
 * @author jinyalin
 * @since 2017/7/24.
 */

public class MyGLWallpaperService extends WallpaperServiceProxy {
    public MyGLWallpaperService(Context host) {
        super(host);
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new MyEngine();
    }

    private class MyEngine extends ActiveEngine {
        MyRenderer renderer;

        public MyEngine() {
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
