package com.yalin.wallpaper.wavez.base;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

public class XperiaZ extends AndroidLiveWallpaperService {

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration c0814b = new AndroidApplicationConfiguration();
        c0814b.useAccelerometer = false;
        initialize(new MyInputAdapter(this), c0814b);
    }
}
