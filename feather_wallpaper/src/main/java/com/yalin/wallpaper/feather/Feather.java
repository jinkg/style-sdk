package com.yalin.wallpaper.feather;

import android.content.Context;

import com.badlogic1.gdx.ApplicationListener;
import com.badlogic1.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic1.gdx.backends.android.AndroidLiveWallpaperService;

public class Feather extends AndroidLiveWallpaperService {

    public Feather(Context host) {
        super(host);
    }

    @Override
    public ApplicationListener createListener(boolean isPreview) {
        return new MyApplicationListener(this);
    }

    @Override
    public AndroidApplicationConfiguration createConfig() {
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useCompass = false;
        return configuration;
    }

    @Override
    public void offsetChange(ApplicationListener listener, float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }
}
