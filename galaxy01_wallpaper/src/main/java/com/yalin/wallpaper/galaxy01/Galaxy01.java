package com.yalin.wallpaper.galaxy01;

import android.content.Context;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.style.engine.GDXWallpaperServiceProxy;
import com.yalin.wallpaper.galaxy.base.C0437b;
import com.yalin.wallpaper.galaxy.base.GalaxyConfig;

public class Galaxy01 extends GDXWallpaperServiceProxy {
    public Galaxy01(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration c0814b = new AndroidApplicationConfiguration();
        c0814b.useGLSurfaceView20API18 = true;
        c0814b.useAccelerometer = false;
        initialize(new C0437b(this, new GalaxyConfig("1", "2")), c0814b);
    }
}
