package com.yalin.style.engine;


import android.content.Context;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

/**
 * @author jinyalin
 * @since 2017/7/28.
 */

public class GLWallpaperServiceProxy extends GLWallpaperService {
    public GLWallpaperServiceProxy(Context host) {

    }

    @Override
    public Engine onCreateEngine() {
        return null;
    }

    public class GLActiveEngine extends GLEngine {

    }
}
