package com.yalin.wallpaper.analog_clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.util.Calendar;

public class ClockWallpaperService extends WallpaperServiceProxy {

    public ClockWallpaperService(Context host) {
        super(host);
    }

    public class MyWallpaperServiceEngine extends ActiveEngine {
        private Bitmap dial;
        private final Runnable drawRunnable = new C02631();
        private final Handler handler = new Handler();
        private int last_min = -1;
        private boolean visible = true;
        private Ini ini;


        class C02631 implements Runnable {
            C02631() {
            }

            public void run() {
                iteration();
                draw();
            }
        }

        public MyWallpaperServiceEngine() {
            super();
            ini = new Ini();
        }

        void draw() {
            ini.Get();
            Calendar localCalendar = Calendar.getInstance();
            for (; ; ) {
                SurfaceHolder localSurfaceHolder = getSurfaceHolder();
                Canvas localCanvas = null;
                try {
                    localCanvas = localSurfaceHolder.lockCanvas();
                    if (localCanvas != null) {
                        int k = localCanvas.getWidth();
                        int j = localCanvas.getHeight();
                        int i = Math.min(k, j);
                        if (this.dial == null) {
                            this.dial = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
                        }
                        if (localCalendar.get(Calendar.MINUTE) != last_min) {
                            last_min = localCalendar.get(Calendar.MINUTE);
                            DrawClock.DrawDial(getApplicationContext(),
                                    this.dial, ini.GetClockSize(i), ini);
                        }
                        localCanvas.drawColor(ini.color_back);
                        k = (k - ini.GetClockSize(i)) * ini.dx / 200;
                        j = (j - ini.GetClockSize(i)) * ini.dy / 200;
                        DrawClock.DrawBack(localCanvas, this.dial, k, j);
                        if (ini.show_seconds) {
                            DrawClock.DrawSecondHand(localCanvas, ini.GetClockSize(i), ini, k, j);
                        }
                    }
                    if (localCanvas == null) {
                        continue;
                    }
                    return;
                } finally {
                    localSurfaceHolder.unlockCanvasAndPost(localCanvas);
                }
            }
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        public void onDestroy() {
            super.onDestroy();
            this.handler.removeCallbacks(this.drawRunnable);
        }

        public void onVisibilityChanged(boolean isVisible) {
            if (isVisible) {
                this.handler.post(this.drawRunnable);
            } else {
                this.handler.removeCallbacks(this.drawRunnable);
            }
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }

        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.last_min = -1;
            iteration();
            draw();
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            this.handler.removeCallbacks(this.drawRunnable);
        }

        protected void iteration() {
            this.handler.removeCallbacks(this.drawRunnable);
            if (this.visible) {
                this.handler.postDelayed(this.drawRunnable, 1000);
            }
        }
    }

    public Engine onCreateEngine() {
        return new MyWallpaperServiceEngine();
    }
}
