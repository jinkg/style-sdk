package com.yalin.wallpaper.water02;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.badlogic1.gdx.ApplicationListener;
import com.badlogic1.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic1.gdx.backends.android.AndroidLiveWallpaperService;

public class MyLiveWallPaperService extends AndroidLiveWallpaperService {
    String Tag = "MyLiveWallPaperService——";
    MyWallpaperListener myWallpaperListener;

    public MyLiveWallPaperService(Context host) {
        super(host);
    }

    public void onCreate() {
        super.onCreate();
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

    public AndroidApplicationConfiguration createConfig() {
        return new AndroidApplicationConfiguration();
    }

    public ApplicationListener createListener(boolean isPreview) {
        this.myWallpaperListener = new MyWallpaperListener();
        return this.myWallpaperListener;
    }

    public void offsetChange(ApplicationListener arg0, float arg1, float arg2, float arg3, float arg4, int arg5, int arg6) {
    }
}
