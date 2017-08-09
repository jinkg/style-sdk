package com.yalin.wallpaper.bezierclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.util.Calendar;

public class BezierWallpaperService extends WallpaperServiceProxy {
    public BezierWallpaperService(Context host) {
        super(host);
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new Engine();
    }

    public class Engine extends ActiveEngine {
        private int background = Color.parseColor("#385d99");
        private int foreground = Color.WHITE;

        private Paint paint = new Paint();
        private Matrix matrix = new Matrix();
        private Path path = new Path();

        private final float[] shifts = {0.0f, 300.0f, 800.0f, 1100.0f, 1600.0f, 1900.0f};
        private final RectF modelRect = new RectF(0.0f, 0.0f, 2380.0f, 550.0f);
        private final RectF realRect = new RectF();

        private static final int DELAY = 16; //ms

        private final Calendar calendar = Calendar.getInstance();

        private BezierAnimator[] digits = {
                new BezierAnimator(36000.0f, 5.0f),
                new BezierAnimator(3600.0f, 5.0f),
                new BezierAnimator(600.0f, 5.0f),
                new BezierAnimator(60.0f, 5.0f),
                new BezierAnimator(10.0f, 2.0f),
                new BezierAnimator(1.0f, 1.0f),
        };

        private boolean visible = false;
        private final Handler handler = new Handler();
        private final Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public Engine() {
            super();
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);
            paint.setDither(true);
            paint.setAntiAlias(true);

            paint.setColor(foreground);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                draw();
            } else {
                handler.removeCallbacks(drawRunnable);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            float partSize = width / modelRect.width();
            float y = (height - modelRect.height() * partSize) / 2;
            realRect.set(0, y, width, y + modelRect.height() * partSize);
            matrix.setRectToRect(modelRect, realRect, Matrix.ScaleToFit.CENTER);
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            visible = false;
            handler.removeCallbacks(drawRunnable);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            visible = false;
            handler.removeCallbacks(drawRunnable);
        }

        private int getNextInt(int current, int max) {
            return (current + 1) % max;
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();

                if (canvas != null) {
                    canvas.setMatrix(matrix);
                    canvas.drawColor(background);

                    calendar.setTimeInMillis(System.currentTimeMillis());
                    int millis = calendar.get(Calendar.MILLISECOND);

                    int second = calendar.get(Calendar.SECOND);
                    int secondsUnit = second % 10;
                    int secondsTen = second / 10;
                    float secondsUnitRatio = millis / 1000.0f;
                    float secondsTenRatio = (secondsUnit * 1000 + millis) / 10000.0f;
                    digits[5].update(secondsUnit, getNextInt(secondsUnit, 10), secondsUnitRatio);
                    digits[4].update(secondsTen, getNextInt(secondsTen, 6), secondsTenRatio);

                    // Minutes
                    int minute = calendar.get(Calendar.MINUTE);
                    int minutesUnit = minute % 10;
                    int minutesTen = minute / 10;
                    float minutesUnitRatio = (second * 1000 + millis) / 60000.0f;
                    float minutesTenRatio = (minutesUnit * 60000 + second * 1000 + millis) / 600000.0f;
                    digits[3].update(minutesUnit, getNextInt(minutesUnit, 10), minutesUnitRatio);
                    digits[2].update(minutesTen, getNextInt(minutesTen, 6), minutesTenRatio);

                    // Hours
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int hoursUnit = hour % 10;
                    int hoursTen = hour / 10;
                    float hoursUnitRatio = (minute * 60000 + second * 1000 + millis) / 3600000.0f;
                    float hoursTenRatio;
                    int hoursUnitNext;
                    if (hour == 23) {
                        hoursUnitNext = 0;
                        hoursTenRatio = (hoursUnit * 3600000 + minute * 60000 + second * 1000 + millis) / (4 * 3600000.0f);
                    } else {
                        hoursUnitNext = getNextInt(hoursUnit, 10);
                        hoursTenRatio = (hoursUnit * 3600000 + minute * 60000 + second * 1000 + millis) / 36000000.0f;
                    }

                    digits[1].update(hoursUnit, hoursUnitNext, hoursUnitRatio);
                    digits[0].update(hoursTen, getNextInt(hoursTen, 3), hoursTenRatio);

                    path.reset();
                    for (int i = 0; i < 6; ++i) {
                        path.moveTo(shifts[i] + digits[i].points[0], digits[i].points[1]);
                        for (int j = 0, k = 2; j < 4; ++j) {
                            path.cubicTo(
                                    shifts[i] + digits[i].points[k++], digits[i].points[k++],
                                    shifts[i] + digits[i].points[k++], digits[i].points[k++],
                                    shifts[i] + digits[i].points[k++], digits[i].points[k++]);
                        }
                    }

                    canvas.drawPath(path, paint);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            handler.removeCallbacks(drawRunnable);

            if (visible) {
                handler.postDelayed(drawRunnable, DELAY);
            }
        }
    }
}