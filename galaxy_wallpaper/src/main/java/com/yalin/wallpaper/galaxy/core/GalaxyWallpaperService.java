package com.yalin.wallpaper.galaxy.core;


import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class GalaxyWallpaperService extends GDXWallpaperServiceProxy {


    public GalaxyWallpaperService(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        ApplicationListener app =
                new ShadowGalaxyWallpaper();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        initialize(app, config);
    }

}
