package com.yalin.style.engine;

import android.content.Context;

/**
 * @author jinyalin
 * @since 2017/7/28.
 */

public interface IProvider {

    WallpaperServiceProxy provideProxy(Context host);
}