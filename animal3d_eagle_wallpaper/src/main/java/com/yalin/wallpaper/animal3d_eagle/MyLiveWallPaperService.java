package com.yalin.wallpaper.animal3d_eagle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.service.wallpaper.WallpaperService;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.animal3d.wallpaper.base.MyWallpaperListener;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class MyLiveWallPaperService extends GDXWallpaperServiceProxy {
    WallpaperService.Engine engine;
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
        this.myWallpaperListener = new MyWallpaperListener("bg15");
        initialize(this.myWallpaperListener, config);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public WallpaperService.Engine onCreateEngine() {
        return super.onCreateEngine();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
