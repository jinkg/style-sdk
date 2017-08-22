package com.yalin.wallpaper.galaxy;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.yalin.style.engine.IProvider;
import com.yalin.wallpaper.galaxy.core.GalaxyWallpaperService;

/**
 * @author jinyalin
 * @since 2017/7/28.
 */

public class ProviderImpl implements IProvider {
    @Override
    public WallpaperService provideProxy(Context host) {
        return new GalaxyWallpaperService(host);
    }
}
