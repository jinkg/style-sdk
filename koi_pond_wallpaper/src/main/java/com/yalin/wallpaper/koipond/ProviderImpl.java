package com.yalin.wallpaper.koipond;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.livewallpaper.koipond.KoiPondService;
import com.yalin.style.engine.IProvider;

/**
 * @author jinyalin
 * @since 2017/7/28.
 */

public class ProviderImpl implements IProvider {
    @Override
    public WallpaperService provideProxy(Context host) {
        return new KoiPondService(host);
    }
}
