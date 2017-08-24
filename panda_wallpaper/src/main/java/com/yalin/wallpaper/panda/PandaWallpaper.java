package com.yalin.wallpaper.panda;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.util.Random;

public class PandaWallpaper extends WallpaperServiceProxy {
    float density = 1.0f;
    Display display;
    boolean doubleTap = true;
    private final Handler mHandler = new Handler();
    long timeSinceLastTap = 0;
    boolean usesTouch = true;

    public PandaWallpaper(Context host) {
        super(host);
    }

    public class WallpaperEngine extends ActiveEngine {
        public final Config FAST_BITMAP_CONFIG = Config.RGB_565;
        Bitmap background;
        int blinkDirection = 1;
        boolean blinking = false;
        String currentBackground = "";
        Bitmap dock;
        private final Runnable drawRunnable = new C00931();
        int eyeClosedLocationY = 0;
        Bitmap eyeGloss;
        float eyeOffsetX = 0.0f;
        float eyeOffsetY = 0.0f;
        Bitmap eyes;
        Bitmap eyesClosed;
        boolean fastEar = false;
        int fastEarTime = 0;
        boolean isLandscape = false;
        long last = 0;
        int lastBlink = 0;
        int lastLook = 0;
        Bitmap leftEar;
        RotationSprite leftEarRotation = new RotationSprite(10.0f, -3.0f, 0.0015f);
        private float mCenterX;
        private float mOffset;
        private final Paint mPaint = new Paint();
        private float mTouchX = -1.0f;
        private float mTouchY = -1.0f;
        private boolean mVisible;
        int maxLastBlink = 10000;
        int maxLastLook = 10000;
        int minLastBlink = 2000;
        int minLastLook = 2000;
        Bitmap panda;
        int pandaX;
        int pandaY;
        Random f412r = new Random();
        Resources res;
        Bitmap rightEar;
        RotationSprite rightEarRotation = new RotationSprite(4.0f, -3.0f, 5.0E-4f);
        int screen_height = 0;
        int screen_width = 0;
        int thisBlinkTime = (this.f412r.nextInt(this.maxLastBlink - this.minLastBlink) + this.minLastBlink);
        int thisLookTime = (this.f412r.nextInt(this.maxLastLook - this.minLastLook) + this.minLastLook);
        Typeface typeFace;

        class C00931 implements Runnable {
            C00931() {
            }

            public void run() {
                WallpaperEngine.this.drawFrame();
            }
        }

        public class RotationSprite {
            public int direction = 1;
            public float maxRotation = 20.0f;
            public float minRotation = -10.0f;
            public float rotation = 0.0f;
            public float rotationSpeed = 0.1f;

            public RotationSprite(float max, float min, float speed) {
                this.maxRotation = max;
                this.minRotation = min;
                this.rotationSpeed = speed;
            }

            public void update(long elapsed) {
                this.rotation += (((float) elapsed) * this.rotationSpeed) * ((float) this.direction);
                if (this.rotation >= this.maxRotation) {
                    this.rotation = this.maxRotation;
                    this.direction = -1;
                }
                if (this.rotation <= this.minRotation) {
                    this.rotation = this.minRotation;
                    this.direction = 1;
                }
            }
        }

        WallpaperEngine() {
            super();
            System.gc();
            Paint paint = this.mPaint;
            paint.setAntiAlias(true);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStyle(Style.STROKE);
            this.res = PandaWallpaper.this.getResources();
            try {
                this.typeFace = Typeface.createFromAsset(PandaWallpaper.this.getAssets(), "fonts/ARIAL.TTF");
                this.mPaint.setTypeface(this.typeFace);
            } catch (Exception e) {
            }
            onSharedPreferenceChanged();
            this.mPaint.setFakeBoldText(false);
            this.mPaint.setStrokeWidth(0.0f);
            this.mPaint.setTextSize(16.0f);
        }

        public Bitmap loadBitmap(Drawable sprite, Config bitmapConfig) {
            int width = sprite.getIntrinsicWidth();
            int height = sprite.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
            Canvas canvas = new Canvas(bitmap);
            sprite.setBounds(0, 0, width, height);
            sprite.draw(canvas);
            return bitmap;
        }

        public void onSharedPreferenceChanged() {
            if (this.res == null) {
                this.res = PandaWallpaper.this.getResources();
            }
            String backPref = "Stars";
            PandaWallpaper.this.doubleTap = true;
            if (!backPref.equalsIgnoreCase(this.currentBackground)) {
                this.currentBackground = backPref;
            }
            if (this.background != null) {
                this.background = null;
                System.gc();
            }
            this.background = loadBitmap(ContextCompat.getDrawable(PandaWallpaper.this, R.drawable.background),
                    this.FAST_BITMAP_CONFIG);
            System.gc();
            if (this.screen_width > 0) {
                this.background = Bitmap.createScaledBitmap(this.background,
                        this.isLandscape ? this.screen_width : this.screen_height,
                        this.isLandscape ? this.screen_width : this.screen_height, true);
            }
            System.gc();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            if (PandaWallpaper.this.usesTouch) {
                setTouchEventsEnabled(true);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            PandaWallpaper.this.mHandler.removeCallbacks(this.drawRunnable);
        }

        void recycleRes(Bitmap b) {
            if (b != null) {
                b.recycle();
            }
        }

        private void cleanResources() {
            recycleRes(this.background);
            recycleRes(this.panda);
            recycleRes(this.eyeGloss);
            recycleRes(this.eyes);
            recycleRes(this.leftEar);
            recycleRes(this.rightEar);
            recycleRes(this.dock);
            this.res = null;
            this.typeFace = null;
            System.gc();
        }

        public void onVisibilityChanged(boolean visible) {
            this.mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                PandaWallpaper.this.mHandler.removeCallbacks(this.drawRunnable);
            }
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.mCenterX = ((float) width) / 2.0f;
            this.screen_width = width;
            this.screen_height = height;
            if (width > height) {
                this.isLandscape = true;
            } else {
                this.isLandscape = false;
            }
            System.gc();
            this.background = Bitmap.createScaledBitmap(this.background, this.isLandscape ? this.screen_width : this.screen_height, this.isLandscape ? this.screen_width : this.screen_height, true);
            System.gc();
            this.dock = BitmapFactory.decodeResource(this.res, R.drawable.bamboo_dock);
            System.gc();
            this.dock = Bitmap.createScaledBitmap(this.dock, this.isLandscape ? this.screen_width : this.screen_height, this.isLandscape ? (int) (((float) this.screen_width) * 0.1484f) : (int) (((float) this.screen_height) * 0.1484f), true);
            System.gc();
            this.panda = BitmapFactory.decodeResource(this.res, R.drawable.panda);
            System.gc();
            this.pandaX = this.panda.getWidth();
            this.pandaY = this.panda.getHeight();
            System.gc();
            this.panda = Bitmap.createScaledBitmap(this.panda, this.isLandscape ? (int) (((float) this.screen_width) * 0.78125f) : (int) (((float) this.screen_height) * 0.78125f), this.isLandscape ? (int) (((float) this.screen_width) * 0.446785f) : (int) (((float) this.screen_height) * 0.446785f), true);
            System.gc();
            this.leftEar = BitmapFactory.decodeResource(this.res, R.drawable.left_ear);
            System.gc();
            this.leftEar = Bitmap.createScaledBitmap(this.leftEar, this.isLandscape ? (int) (((float) this.screen_width) * 0.2601f) : (int) (((float) this.screen_height) * 0.2601f), this.isLandscape ? (int) (((float) this.screen_width) * 0.2711f) : (int) (((float) this.screen_height) * 0.2711f), true);
            System.gc();
            this.rightEar = BitmapFactory.decodeResource(this.res, R.drawable.right_ear);
            System.gc();
            this.rightEar = Bitmap.createScaledBitmap(this.rightEar, this.isLandscape ? (int) (((float) this.screen_width) * 0.3343f) : (int) (((float) this.screen_height) * 0.3343f), this.isLandscape ? (int) (((float) this.screen_width) * 0.2711f) : (int) (((float) this.screen_height) * 0.2711f), true);
            System.gc();
            this.eyes = BitmapFactory.decodeResource(this.res, R.drawable.eyes);
            System.gc();
            this.eyes = Bitmap.createScaledBitmap(this.eyes, this.isLandscape ? (int) (((float) this.screen_width) * 0.5211f) : (int) (((float) this.screen_height) * 0.5211f), this.isLandscape ? (int) (((float) this.screen_width) * 0.2273f) : (int) (((float) this.screen_height) * 0.2273f), true);
            System.gc();
            this.eyeGloss = BitmapFactory.decodeResource(this.res, R.drawable.eye_gloss);
            System.gc();
            this.eyeGloss = Bitmap.createScaledBitmap(this.eyeGloss, this.isLandscape ? (int) (((float) this.screen_width) * 0.5211f) : (int) (((float) this.screen_height) * 0.5211f), this.isLandscape ? (int) (((float) this.screen_width) * 0.2273f) : (int) (((float) this.screen_height) * 0.2273f), true);
            System.gc();
            this.eyesClosed = BitmapFactory.decodeResource(this.res, R.drawable.eyes_closed);
            System.gc();
            this.eyesClosed = Bitmap.createScaledBitmap(this.eyesClosed, this.isLandscape ? (int) (((float) this.screen_width) * 0.2859f) : (int) (((float) this.screen_height) * 0.2859f), this.isLandscape ? (int) (((float) this.screen_width) * 0.09765f) : (int) (((float) this.screen_height) * 0.09765f), true);
            System.gc();
            try {
                drawFrame();
            } catch (Exception e) {
            }
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.mVisible = false;
            PandaWallpaper.this.mHandler.removeCallbacks(this.drawRunnable);
            cleanResources();
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            this.mOffset = ((float) xPixels) * 0.5f;
            drawFrame();
        }

        public void onTouchEvent(MotionEvent event) {
            this.mTouchX = event.getX();
            this.mTouchY = event.getY();
            setEyeOffset();
            super.onTouchEvent(event);
        }

        private void setEyeOffset() {
            if (this.mTouchX < this.mCenterX - 15.0f) {
                this.eyeOffsetX = (5.0f * (-(1.0f - (this.mTouchX / (this.mCenterX - 15.0f))))) * PandaWallpaper.this.density;
            } else if (this.mTouchX > this.mCenterX + 15.0f) {
                this.eyeOffsetX = (4.0f * (this.mTouchX / (this.mCenterX + 15.0f))) * PandaWallpaper.this.density;
            } else {
                this.eyeOffsetX = 0.0f;
            }
            if (this.mTouchY < ((float) ((this.screen_height - this.eyes.getHeight()) - 5))) {
                this.eyeOffsetY = ((-(1.0f - (this.mTouchY / ((float) ((this.screen_height - this.eyes.getHeight()) - 5))))) * 4.0f) * PandaWallpaper.this.density;
            } else if (this.mTouchY > ((float) ((this.screen_height - this.eyes.getHeight()) + 5))) {
                this.eyeOffsetY = (2.0f * (this.mTouchY / ((float) ((this.screen_height - this.eyes.getHeight()) + 5)))) * PandaWallpaper.this.density;
            } else {
                this.eyeOffsetY = 0.0f;
            }
            this.lastLook = 0;
        }

        void drawFrame() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    drawBackground(c);
                }
                holder.unlockCanvasAndPost(c);
            } catch (Exception e) {
                holder.unlockCanvasAndPost(c);
            }
            PandaWallpaper.this.mHandler.removeCallbacks(this.drawRunnable);
            if (this.mVisible) {
                PandaWallpaper.this.mHandler.postDelayed(this.drawRunnable, 50);
            }
        }

        void drawBackground(Canvas c) {
            long now = SystemClock.elapsedRealtime();
            float dockOffset = 0.0f;
            if (!this.isLandscape) {
                dockOffset = ((float) (-this.dock.getHeight())) * 0.91f;
            }
            long elapsed = now - this.last;
            PandaWallpaper pandaWallpaper = PandaWallpaper.this;
            pandaWallpaper.timeSinceLastTap += elapsed;
            if (this.last == 0) {
                elapsed = 0;
            }
            if (elapsed > 1000) {
                elapsed = 0;
            }
            if (this.isLandscape) {
                this.mOffset = 0.0f;
            }
            this.lastLook = (int) (((long) this.lastLook) + elapsed);
            if (!this.blinking) {
                this.lastBlink = (int) (((long) this.lastBlink) + elapsed);
            }
            if (!this.blinking && this.lastBlink >= this.thisBlinkTime) {
                this.blinking = true;
            }
            if (this.blinking) {
                this.eyeClosedLocationY = (int) (((float) this.eyeClosedLocationY) + ((((float) elapsed) * 0.4f) * ((float) this.blinkDirection)));
                if (((double) this.eyeClosedLocationY) >= ((double) this.eyesClosed.getHeight()) - (((double) this.eyesClosed.getHeight()) * 0.034d)) {
                    this.eyeClosedLocationY = (int) (((double) this.eyesClosed.getHeight()) - (((double) this.eyesClosed.getHeight()) * 0.034d));
                    this.blinkDirection = -1;
                }
                if (this.eyeClosedLocationY <= 0) {
                    this.blinkDirection = 1;
                    this.blinking = false;
                    this.thisBlinkTime = this.f412r.nextInt(this.maxLastBlink - this.minLastBlink) + this.minLastBlink;
                    this.lastBlink = 0;
                    this.eyeClosedLocationY = 0;
                }
            }
            if (this.fastEar) {
                this.fastEarTime = (int) (((long) this.fastEarTime) + elapsed);
            }
            if (this.fastEarTime > 300) {
                this.fastEarTime = 0;
                this.fastEar = false;
                this.leftEarRotation.rotationSpeed = 0.0025f;
            }
            if (this.lastLook >= this.thisLookTime) {
                this.thisLookTime = this.f412r.nextInt(this.maxLastLook - this.minLastLook) + this.minLastLook;
                this.mTouchX = (float) (this.f412r.nextInt(this.screen_width - 1) + 1);
                this.mTouchY = (float) (this.f412r.nextInt(this.screen_height - 1) + 1);
                setEyeOffset();
            }
            c.drawBitmap(this.background, this.mOffset, 0.0f, this.mPaint);
            this.leftEarRotation.update(elapsed);
            this.rightEarRotation.update(elapsed);
            c.save();
            c.translate(this.mCenterX, (float) this.screen_height);
            c.rotate(this.leftEarRotation.rotation);
            c.drawBitmap(this.leftEar, (float) ((-this.panda.getWidth()) / 2), ((float) (-this.panda.getHeight())) + dockOffset, this.mPaint);
            c.restore();
            c.save();
            c.translate(this.mCenterX, (float) this.screen_height);
            c.rotate(this.rightEarRotation.rotation);
            c.drawBitmap(this.rightEar, (float) ((this.panda.getWidth() / 2) - this.rightEar.getWidth()), ((float) (-this.panda.getHeight())) + dockOffset, this.mPaint);
            c.restore();
            c.drawBitmap(this.eyes, ((this.mCenterX + ((float) (this.panda.getWidth() / 2))) - ((float) this.eyes.getWidth())) + this.eyeOffsetX, (((float) (this.screen_height - this.eyes.getHeight())) + this.eyeOffsetY) + dockOffset, this.mPaint);
            c.drawBitmap(this.eyeGloss, (this.mCenterX + ((float) (this.panda.getWidth() / 2))) - ((float) this.eyes.getWidth()), ((float) (this.screen_height - this.eyes.getHeight())) + dockOffset, this.mPaint);
            c.drawBitmap(this.eyesClosed, (this.mCenterX + ((float) (this.panda.getWidth() / 2))) - ((float) this.eyes.getWidth()), ((float) (((this.screen_height - this.eyes.getHeight()) - this.eyesClosed.getHeight()) + this.eyeClosedLocationY)) + dockOffset, this.mPaint);
            c.drawBitmap(this.panda, this.mCenterX - ((float) (this.panda.getWidth() / 2)), ((float) (this.screen_height - this.panda.getHeight())) + dockOffset, this.mPaint);
            if (!this.isLandscape) {
                c.drawBitmap(this.dock, 0.0f, (float) (this.screen_height - this.dock.getHeight()), this.mPaint);
            }
            this.last = now;
        }
    }

    public void onCreate() {
        super.onCreate();
        this.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.display.getMetrics(outMetrics);
        this.density = outMetrics.density;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }
}
