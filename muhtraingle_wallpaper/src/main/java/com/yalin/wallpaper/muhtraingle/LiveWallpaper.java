package com.yalin.wallpaper.muhtraingle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class LiveWallpaper extends GDXWallpaperServiceProxy {
    public LiveWallpaper(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;
        config.numSamples = 2;

        final LWP listener = new LWP();
        initialize(listener, config);
    }

    private class LWP extends MyGdxGame implements AndroidWallpaperListener {
        private boolean isPreviewing = false;
        private boolean justStarted = true;

        @Override
        public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            setXOffset(xOffset);
        }

        @Override
        public void resume() {
            super.resume();
            readBattery();
            if (isPreviewing)
                readPrefs();
        }

        @Override
        public void previewStateChange(boolean isPreview) {
            if (!justStarted && isPreview == isPreviewing) return;
            isPreviewing = isPreview;
            justStarted = false;
            readPrefs();
        }

        @Override
        public void readBattery() {
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);
                    float rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    float scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    if (rawlevel >= 0 && scale > 0) {
                        setBattery(rawlevel / scale);
                    }
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }

        private void readPrefs() {
            boolean gradientMode = true;
            int gradientSubtle = 5;
            int gradientType = 5;
            boolean gradientInvert = false;
            String hueModeS = "c1";
            int hueMode = 4, customHue = 0;
            if (hueModeS.charAt(0) == 'm') {
                hueMode = 6;
            } else if (hueModeS.charAt(0) == 'c') {
                hueMode = Integer.parseInt(hueModeS.substring(1));
            } else {
                hueMode = 4;
                customHue = hueModeS.charAt(0) == 'r' ? (int) (Math.random() * 360) : Integer.parseInt(hueModeS);
            }
            int satMode = 1;
            int lumaMode = 2;
            boolean touchReact = true;
            int instability = 0;
            int outline = 2;
            int speed = 2;
            int density = 3;
            int fpsLimit = 20;
            int outlineThickness = 3;
            int algorithm = 0;
            setPreferences(MyGdxGame.GRADIENTMODE_SMOOTH, gradientSubtle, gradientType,
                    MyGdxGame.GRADIENTINVERT_NO, hueMode, customHue, satMode,
                    lumaMode, MyGdxGame.TOUCHREACT_ANIMATE, speed, density, instability,
                    outline, outlineThickness, algorithm, fpsLimit);
        }

    }


}

