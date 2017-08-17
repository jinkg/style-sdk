package com.yalin.wallpaper.nightfal_rural;

import android.content.Context;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class LiveWallpaperAndroid extends GDXWallpaperServiceProxy {
    public static Context myApplicationContext;

    public LiveWallpaperAndroid(Context host) {
        super(host);
    }

    public void onCreateApplication() {
        super.onCreateApplication();
        myApplicationContext = getApplicationContext();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGLSurfaceView20API18 = false;
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;
        initialize(new LiveWallpaperStarter(), config);
    }
}
