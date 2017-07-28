package com.yalin.component2;

import android.content.Context;

import com.yalin.style.engine.IProvider;
import com.yalin.style.engine.WallpaperServiceProxy;

/**
 * @author jinyalin
 * @since 2017/7/28.
 */

public class ProviderImpl implements IProvider {
    @Override
    public WallpaperServiceProxy provideProxy(Context host) {
        return new MyGLWallpaperService(host);
    }
}
