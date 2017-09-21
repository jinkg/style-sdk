package com.yalin.wallpaper.animal3d_wolf;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.animal3d.wallpaper.base.MyWallpaperListener;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class MyLiveWallPaperService extends GDXWallpaperServiceProxy {
    Engine engine;
    MyWallpaperListener myWallpaperListener;

    public MyLiveWallPaperService(Context host) {
        super(host);
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onCreateApplication() {
        super.onCreateApplication();
        this.engine = onCreateEngine();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.getTouchEventsForLiveWallpaper = true;
        this.myWallpaperListener = new MyWallpaperListener("bg1");
        initialize(this.myWallpaperListener, config);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public Engine onCreateEngine() {
        return super.onCreateEngine();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
