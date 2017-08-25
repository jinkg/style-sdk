/*
 * Copyright 2010 Elijah Cornell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */

package com.badlogic1.gdx.backends.android;

import android.content.Context;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.badlogic1.gdx.ApplicationListener;
import com.badlogic1.gdx.backends.android.surfaceview.GLBaseSurfaceViewLW;
import com.yalin.style.engine.WallpaperServiceProxy;


public abstract class AndroidLiveWallpaperService extends WallpaperServiceProxy {
    final String TAG = "AndroidLiveWallpaperService";
    static boolean DEBUG = false;
    protected static volatile int runningEngines = 0;

    public AndroidLiveWallpaperService(Context host) {
        super(host);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new AndroidWallpaperEngine();
    }

    /**
     * @return a new {@link ApplicationListener} that implements the live wallpaper
     */
    public abstract ApplicationListener createListener(boolean isPreview);

    /**
     * @return a new {@link AndroidApplicationConfiguration} that specifies the config to be used for the live wall paper
     */
    public abstract AndroidApplicationConfiguration createConfig();

    /**
     * Called when the live wallpaper's offset changed. This method will be called
     * on the rendering thread.
     */
    public abstract void offsetChange(ApplicationListener listener, float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class AndroidWallpaperEngine extends ActiveEngine {
        protected AndroidLiveWallpaper app;
        protected ApplicationListener listener;
        protected GLBaseSurfaceViewLW view;

        public AndroidWallpaperEngine() {
        }

        @Override
        public Bundle onCommand(final String pAction, final int pX, final int pY, final int pZ, final Bundle pExtras,
                                final boolean pResultRequested) {

            return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
        }

        @Override
        public void onCreate(final SurfaceHolder surfaceHolder) {
            runningEngines++;
            super.onCreate(surfaceHolder);
            this.app = new AndroidLiveWallpaper(AndroidLiveWallpaperService.this, this);
            AndroidApplicationConfiguration config = createConfig();
            listener = createListener(isPreview());
            this.app.initialize(listener, config);
            this.view = ((AndroidGraphicsLiveWallpaper) app.getGraphics()).getView();

            if (config.getTouchEventsForLiveWallpaper && Integer.parseInt(android.os.Build.VERSION.SDK) < 9)
                this.setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            runningEngines--;
            view.onDestroy();
            if (listener != null)
                listener.dispose();
            app.onDestroy();
            super.onDestroy();
        }

        public void onPause() {
            app.onPause();
            view.onPause();
        }

        public void onResume() {
            app.onResume();
            view.onResume();
        }

        @Override
        public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(final SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(final SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onVisibilityChanged(final boolean visible) {
            if (AndroidLiveWallpaperService.DEBUG)
                if (visible) {
                    onResume();
                } else {
                    onPause();
                }

            super.onVisibilityChanged(visible);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            app.input.onTouch(null, event);
        }

        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset, final float xOffsetStep, final float yOffsetStep, final int xPixelOffset,
                                     final int yPixelOffset) {

            app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    AndroidLiveWallpaperService.this.offsetChange(listener, xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
                }
            });
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }
    }
}
