package com.yalin.livewallpaper3d;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

/**
 * Created by Paul on 9/9/2014.
 */
public class TicTacToeWallpaper extends GDXWallpaperServiceProxy {

    public TicTacToeWallpaper(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;

        ApplicationListener listener = new DefaultClass();
        initialize(listener, config);
    }

}
