package com.yalin.wallpaper.galaxy.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public abstract class LibdgxWallpaperApp implements ApplicationListener, AndroidWallpaperListener {
    protected boolean isPreview;

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }
}
