package com.yalin.wallpaper.super_hexagon;

import android.content.Context;
import android.content.SharedPreferences;

import com.yalin.style.engine.GLWallpaperServiceProxy;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class HexagonService extends GLWallpaperServiceProxy {
    public static final String SHARED_PREFS_NAME = "hexagonwallpaper_settings";
    public SharedPreferences preferences;

    public int prefs_colors;
    public int prefs_perspective;
    public int prefs_zoom;
    public int prefs_pulse;
    public int prefs_rotation;
    public int prefs_walls;
    public int prefs_cursor;

    public HexagonService(Context host) {
        super(host);
    }

    public Engine onCreateEngine() {
        return new MyEngine(this);
    }

    private class MyEngine extends GLActiveEngine {
        HexagonRenderer renderer;
        HexagonService service;

        public MyEngine(HexagonService s) {
            super();
            // handle prefs, other initialization
            renderer = new HexagonRenderer(s);
            service = s;
            setRenderer(renderer);
            setRenderMode(RENDERMODE_CONTINUOUSLY);

            initParams();
        }

        public void onDestroy() {
            super.onDestroy();
            if (renderer != null) {
                renderer.release();
            }
            renderer = null;
        }

        public void initParams() {
            service.prefs_colors = 1;
            service.prefs_perspective = -1;
            service.prefs_zoom = -1;
            service.prefs_pulse = 1;
            service.prefs_rotation = -1;
            service.prefs_walls = 3;
            service.prefs_cursor = 3;
        }
    }
}
