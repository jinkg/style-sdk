package com.yalin.wallpaper.wavez03;

import android.content.Context;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.style.engine.GDXWallpaperServiceProxy;
import com.yalin.wallpaper.wavez.base.MyInputAdapter;
import com.yalin.wallpaper.wavez.base.XperiaZConfig;

public class XperiaZ03 extends GDXWallpaperServiceProxy {

    public XperiaZ03(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration c0814b = new AndroidApplicationConfiguration();
        c0814b.useAccelerometer = false;
        initialize(new MyInputAdapter(this, new XperiaZConfig("4", "3")), c0814b);
    }
}
