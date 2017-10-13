package com.yalin.wallpaper.dandelion01;

import android.content.Context;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.yalin.style.engine.GDXWallpaperServiceProxy;
import com.yalin.wallpaper.dandelion.base.DandelionConfig;
import com.yalin.wallpaper.dandelion.base.MyInputAdapter;

public class Dandelion01 extends GDXWallpaperServiceProxy {

    public Dandelion01(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration c0814b = new AndroidApplicationConfiguration();
        c0814b.useAccelerometer = false;
        c0814b.useCompass = false;
        initialize(new MyInputAdapter(this, new DandelionConfig(2, true, true)), c0814b);
    }
}
