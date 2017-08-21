/*
 * Copyright (C) 2012, 2013 OBN-soft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.wallpaper.genesis;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

public class MyWallpaperService extends WallpaperServiceProxy {

    public MyWallpaperService(Context host) {
        super(host);
    }

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    /*-----------------------------------------------------------------------*/

    private class MyEngine extends ActiveEngine {

        private MyThread mThread = null;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            mThread = new MyThread(getSurfaceHolder());
            mThread.start();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            if (mThread != null) {
                mThread.setSize(width, height);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (mThread != null) {
                if (visible) {
                    mThread.onResume();
                } else {
                    mThread.onPause();
                }
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (mThread != null) {
                mThread.onDestroy();
                mThread = null;
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

    }

    /*-----------------------------------------------------------------------*/

    private class MyThread extends Thread {

        private static final int DRAW_INTERVAL = 50;
        private static final int WAIT_INTERVAL = 500;

        private SurfaceHolder mHolder;
        private int mWindowWidth;
        private int mWindowHeight;
        private boolean mLoop;
        private boolean mPause;

        public MyThread(SurfaceHolder holder) {
            mHolder = holder;
        }

        @Override
        public void run() {
            MyRenderer renderer = new MyRenderer();
            renderer.onStartDrawing(MyWallpaperService.this, mHolder);
            long tm = System.currentTimeMillis();
            boolean stopped = false;
            mLoop = true;
            mPause = false;
            while (mLoop) {
                if (mPause) {
                    stopped = true;
                    tm += WAIT_INTERVAL;
                } else {
                    if (stopped) {
                        renderer.onResumeDrawing();
                        stopped = false;
                    }
                    renderer.onDrawFrame(mWindowWidth, mWindowHeight);
                    tm += DRAW_INTERVAL;
                }
                long wait = tm - System.currentTimeMillis();
                if (wait > 0) {
                    try {
                        Thread.sleep(wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    yield();
                    tm -= wait;
                }
            }
            renderer.onFinishDrawing();
        }

        public void setSize(int width, int height) {
            mWindowWidth = width;
            mWindowHeight = height;
        }

        public void onPause() {
            mPause = true;
        }

        public void onResume() {
            mPause = false;
        }

        public void onDestroy() {
            synchronized (this) {
                mLoop = false;
            }
            try {
                join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
