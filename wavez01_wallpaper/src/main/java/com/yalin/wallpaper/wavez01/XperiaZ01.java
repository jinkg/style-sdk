package com.yalin.wallpaper.wavez01;

import android.content.Context;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.yalin.style.engine.GDXWallpaperServiceProxy;
import com.yalin.wallpaper.wavez.base.MyInputAdapter;
import com.yalin.wallpaper.wavez.base.XperiaZConfig;

public class XperiaZ01 extends GDXWallpaperServiceProxy {

    public XperiaZ01(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration c0814b = new AndroidApplicationConfiguration();
        c0814b.useAccelerometer = false;
        initialize(new MyInputAdapter(this, new XperiaZConfig("2", "5")), c0814b);
    }
}
