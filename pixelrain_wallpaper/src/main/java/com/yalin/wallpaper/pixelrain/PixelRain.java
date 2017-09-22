package com.yalin.wallpaper.pixelrain;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class PixelRain extends WallpaperServiceProxy {
    public PixelRain(Context host) {
        super(host);
    }

    class RainEngine extends ActiveEngine {
        private Bitmap backgroundImage;
        private RectF backgroundRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        private int bgColor = -16777216;
        private String bgImagePath;
        private final Paint collisionPaint = new Paint();
        private int[] colorArray = new int[10];
        private int columns;
        private boolean depthEnabled = false;
        private ArrayList<RainDrop> drops = new ArrayList<>();
        private boolean[] enabledArray = new boolean[10];
        private int glowAlpha = 100;
        private Bitmap glowImage;
        private int ground;
        private int height;
        private float heightPref = 0.75f;
        private boolean isAlignEnabled = false;
        private boolean isCircleVisible = false;
        private boolean isGlowEnabled = false;
        private boolean isGroundGlowEnabled = false;
        private boolean isImageScrollEnabled = false;
        private boolean isRainScrollEnabled = false;
        private boolean isShatterEnabled = false;
        private boolean isTouchEnabled = false;
        private final Runnable mDrawRain = new C00001();
        private final Handler mHandler = new Handler();
        private float mOffset = 0.0f;
        private int mPixels = 0;
        private float mTouchX = -1.0f;
        private float mTouchY = -1.0f;
        private boolean mVisible;
        private int numDrops = 0;
        private boolean preferencesChanged = false;
        private int prevPixels = 0;
        private boolean randomSize = false;
        private boolean randomSpeed = false;
        private Resources res;
        private int shatterEffect = 0;
        private int size = 10;
        private float speedPref = 1.0f;
        private float tempX;
        private int touchPreference = 7;
        private int touchRadius = 0;
        private int trailLength = 4;
        private boolean useCustomImage = false;
        private int width;

        class C00001 implements Runnable {
            C00001() {
            }

            public void run() {
                RainEngine.this.drawFrame();
            }
        }


        RainEngine() {
            super();
            this.res = PixelRain.this.getResources();
            this.collisionPaint.setAntiAlias(true);
            this.collisionPaint.setStrokeWidth(2.0f);
            this.collisionPaint.setStrokeCap(Cap.ROUND);
            this.collisionPaint.setStyle(Style.STROKE);
            this.collisionPaint.setColor(ContextCompat.getColor(PixelRain.this, R.color.COLOR_WHITE));
            this.glowImage = BitmapFactory.decodeResource(PixelRain.this.getResources(), R.drawable.glow);
            onSharedPreferenceChanged();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        public void onDestroy() {
            super.onDestroy();
            this.mHandler.removeCallbacks(this.mDrawRain);
        }

        public void onVisibilityChanged(boolean visible) {
            this.mVisible = visible;
            if (visible) {
                if (this.preferencesChanged) {
                    setNumDrops(this.numDrops);
                    setSize(this.size);
                    setColors();
                    this.ground = (int) (((float) this.height) * this.heightPref);
                    this.touchRadius = this.width / this.touchPreference;
                    if (this.useCustomImage && this.bgImagePath != null) {
                        getBackground();
                    }
                    if (!(this.useCustomImage || this.backgroundImage == null || this.backgroundImage.isRecycled())) {
                        clearBitmap(this.backgroundImage);
                    }
                    resetDrops();
                    this.preferencesChanged = false;
                }
                drawFrame();
                return;
            }
            this.mHandler.removeCallbacks(this.mDrawRain);
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.width = width;
            this.height = height;
            this.ground = (int) (((float) height) * this.heightPref);
            this.columns = width / this.size;
            this.touchRadius = width / this.touchPreference;
            for (int x = 0; x < Math.min(this.numDrops, this.drops.size()); x++) {
                this.tempX = this.drops.get(x).getX();
                this.drops.get(x).setX(this.drops.get(x).getY());
                this.drops.get(x).setY(this.tempX);
                Iterator it = this.drops.get(x).getDroplets().iterator();
                while (it.hasNext()) {
                    Droplet d = (Droplet) it.next();
                    this.tempX = d.getX();
                    d.setX(d.getY());
                    d.setY(this.tempX);
                }
            }
            if (this.useCustomImage && this.bgImagePath != null) {
                getBackground();
                adjustBackgroundRect();
            }
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.mVisible = false;
            this.mHandler.removeCallbacks(this.mDrawRain);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            if (this.isRainScrollEnabled) {
                this.mPixels = xPixels - this.prevPixels;
                this.prevPixels = xPixels;
                for (int x = 0; x < Math.min(this.drops.size(), this.numDrops); x++) {
                    this.drops.get(x).setOffset((float) this.mPixels, this.depthEnabled);
                }
            }
            this.mOffset = xOffset;
            if (this.useCustomImage) {
                adjustBackgroundRect();
            }
        }

        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() != 0 && event.getAction() != 2) {
                this.mTouchX = -1.0f;
                this.mTouchY = -1.0f;
            } else if (event.getX() <= 0.0f || event.getY() <= 0.0f) {
                this.mTouchX = -1.0f;
                this.mTouchY = -1.0f;
            } else {
                this.mTouchX = event.getX();
                this.mTouchY = event.getY();
            }
            super.onTouchEvent(event);
        }

        void drawFrame() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            c = holder.lockCanvas();
            if (c != null) {
                c.save();
                if (!this.useCustomImage || this.backgroundImage == null || this.backgroundImage.isRecycled()) {
                    try {
                        c.drawColor(this.bgColor);
                    } catch (Throwable th) {
                        holder.unlockCanvasAndPost(c);
                    }
                } else {
                    c.drawColor(-16777216);
                    c.drawBitmap(this.backgroundImage, null, this.backgroundRect, null);
                }
                int x = 0;
                while (x < Math.min(this.numDrops, this.drops.size())) {
                    int rainGround;
                    this.drops.get(x).update();
                    if (this.mTouchX >= 0.0f && this.mTouchY >= 0.0f && this.isTouchEnabled) {
                        if (this.isCircleVisible) {
                            drawCollisionCircle(c);
                        }
                        checkTouchCollisions(this.drops);
                    }
                    if (this.randomSize && this.depthEnabled) {
                        rainGround = (int) (((float) this.ground) - (((1.0f - ((((float) this.drops.get(x).getSize()) - 10.0f) / 20.0f)) * ((float) this.height)) * 0.125f));
                    } else {
                        rainGround = this.ground;
                    }
                    Boolean touched = this.drops.get(x).wasTouched();
                    if (this.drops.get(x).getY() >= ((float) (rainGround - this.drops.get(x).getSize())) || touched) {
                        resetDrop(this.drops.get(x), rainGround, touched);
                    }
                    if (this.drops.get(x).isVisible()) {
                        if (((float) this.drops.get(x).getSize()) + this.drops.get(x).getX() >= 0.0f && this.drops.get(x).getX() < ((float) this.width)) {
                            this.drops.get(x).draw(c, rainGround, this.trailLength, this.isGlowEnabled, this.isGroundGlowEnabled, this.glowAlpha);
                        }
                    }
                    Iterator it = this.drops.get(x).getDroplets().iterator();
                    while (it.hasNext()) {
                        Droplet d = (Droplet) it.next();
                        if ((d.getY() >= ((float) this.height) && this.shatterEffect != 1) || d.getX() + ((float) d.getSize()) < 0.0f || d.getX() > ((float) this.width)) {
                            d.setVisible(false);
                        }
                        if (d.isVisible()) {
                            d.update();
                            d.draw(c);
                        }
                    }
                    x++;
                }
                c.restore();
            }
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
            this.mHandler.removeCallbacks(this.mDrawRain);
            if (this.mVisible) {
                this.mHandler.postDelayed(this.mDrawRain, 40);
            }
        }

        void drawCollisionCircle(Canvas c) {
            c.drawCircle(this.mTouchX, this.mTouchY, (float) this.touchRadius, this.collisionPaint);
        }

        void checkTouchCollisions(ArrayList<RainDrop> drops) {
            for (int x = 0; x < Math.min(this.numDrops, drops.size()); x++) {
                if (Math.sqrt(Math.pow((double) (drops.get(x).getXCenter() - this.mTouchX), 2.0d) + Math.pow((double) (drops.get(x).getYCenter() - this.mTouchY), 2.0d)) <= ((double) this.touchRadius)) {
                    drops.get(x).setTouched(Boolean.TRUE);
                }
            }
        }

        void resetDrop(RainDrop drop, int ground, Boolean fromTouch) {
            boolean z = false;
            if (this.randomSpeed) {
                this.speedPref = 0.1f + (1.9f * ((float) Math.random()));
            }
            if (!this.isAlignEnabled || this.randomSize) {
                float random = ((float) this.width) * ((float) Math.random());
                float random2 = (this.speedPref * 20.0f) + (((float) Math.random()) * 10.0f);
                int y = (int) drop.getY();
                if (this.isShatterEnabled || fromTouch) {
                    z = true;
                }
                drop.reset(random, 0.0f, random2, y, ground, z, this.shatterEffect);
                return;
            }
            float random = (float) (this.size * ((int) (((double) this.columns) * Math.random())));
            float random2 = (this.speedPref * 20.0f) + (((float) Math.random()) * 10.0f);
            int y = (int) drop.getY();
            if (this.isShatterEnabled || fromTouch) {
                z = true;
            }
            drop.reset(random, 0.0f, random2, y, ground, z, this.shatterEffect);
        }

        void resetDrops() {
            for (int x = 0; x < this.numDrops; x++) {
                if (this.randomSpeed) {
                    this.speedPref = 0.1f + (1.9f * ((float) Math.random()));
                }
                if (!this.isAlignEnabled || this.randomSize) {
                    this.drops.get(x).prefReset(((float) this.width) * ((float) Math.random()), 0.0f, (this.speedPref * 20.0f) + (((float) Math.random()) * 10.0f), this.randomSize);
                } else {
                    this.drops.get(x).prefReset((float) (this.size * ((int) (((float) this.columns) * ((float) Math.random())))), 0.0f, (this.speedPref * 20.0f) + (((float) Math.random()) * 10.0f), this.randomSize);
                }
            }
        }

        public void onSharedPreferenceChanged() {
            boolean z = false;
            this.isRainScrollEnabled = true;
            this.isImageScrollEnabled = true;
            this.numDrops = 12;
            this.enabledArray[0] = true;
            this.enabledArray[1] = true;
            this.enabledArray[2] = true;
            this.enabledArray[3] = true;
            this.enabledArray[4] = true;
            this.enabledArray[5] = true;
            this.enabledArray[6] = true;
            this.enabledArray[7] = true;
            this.enabledArray[8] = true;
            this.enabledArray[9] = true;
            this.colorArray[0] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_BLUE);
            this.colorArray[1] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_PURPLE);
            this.colorArray[2] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_RED);
            this.colorArray[3] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_ORANGE);
            this.colorArray[4] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_YELLOW);
            this.colorArray[5] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_GREEN);
            this.colorArray[6] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_PINK);
            this.colorArray[7] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_CHOCOLATE);
            this.colorArray[8] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_WHITE);
            this.colorArray[9] = ContextCompat.getColor(PixelRain.this, R.color.COLOR_BLACK);
            this.size = 55;
            this.randomSize = false;
            this.isGlowEnabled = true;
            this.isGroundGlowEnabled = true;
            this.glowAlpha = 100;
            this.heightPref = 0.875f;
            this.isShatterEnabled = true;
            this.shatterEffect = 1;
            this.isAlignEnabled = true;
            this.depthEnabled = false;
            this.speedPref = 1.5f;
            this.randomSpeed = false;
            this.trailLength = 10;
            this.useCustomImage = false;
            this.bgColor = ContextCompat.getColor(PixelRain.this, R.color.COLOR_BLACK);
            this.bgImagePath = null;
            this.isTouchEnabled = true;
            this.isCircleVisible = false;
            this.collisionPaint.setColor(ContextCompat.getColor(PixelRain.this, R.color.COLOR_WHITE));
            this.touchPreference = 7;

            this.preferencesChanged = true;
        }

        public void setColors() {
            boolean atLeastOneEnabled = false;
            for (boolean z : this.enabledArray) {
                if (z) {
                    atLeastOneEnabled = true;
                }
            }
            if (atLeastOneEnabled) {
                int index = 0;
                for (int iDrop = 0; iDrop < this.numDrops; iDrop++) {
                    while (!this.enabledArray[index]) {
                        index++;
                        if (index == this.enabledArray.length) {
                            index = 0;
                        }
                    }
                    this.drops.get(iDrop).setColor(this.colorArray[index]);
                    index++;
                    if (index == this.enabledArray.length) {
                        index = 0;
                    }
                }
                return;
            }
            for (int x = 0; x < this.numDrops; x++) {
                this.drops.get(x).setColor(this.res.getColor(R.color.COLOR_BLUE));
            }
        }

        public void setNumDrops(int numDrops) {
            while (this.drops.size() < numDrops) {
                this.drops.add(new RainDrop((float) (this.size * ((int) (((float) this.columns) * ((float) Math.random())))), 0.0f, (20.0f * this.speedPref) + (10.0f * ((float) Math.random())), this.size, this.glowImage));
            }
        }

        public void setSize(int size) {
            int x;
            if (this.randomSize) {
                for (x = 0; x < this.numDrops; x++) {
                    this.drops.get(x).setSize((int) (10.0d + (20.0d * Math.random())));
                }
            } else {
                for (x = 0; x < this.numDrops; x++) {
                    ((RainDrop) this.drops.get(x)).setSize(size);
                }
            }
            this.columns = this.width / size;
        }

        void adjustBackgroundRect() {
            if (!this.isImageScrollEnabled || isPreview() || this.backgroundRect.width() <= ((float) this.width)) {
                this.backgroundRect.offsetTo((((float) this.width) - this.backgroundRect.width()) / 2.0f, 0.0f);
            } else {
                this.backgroundRect.offsetTo((-this.mOffset) * (this.backgroundRect.width() - ((float) this.width)), 0.0f);
            }
        }

        void getBackground() {
            if (!(this.backgroundImage == null || this.backgroundImage.isRecycled())) {
                clearBitmap(this.backgroundImage);
            }
            if (this.width == 0 || this.height == 0) {
                this.width = 320;
                this.height = 480;
            }
            if (new File(this.bgImagePath).exists()) {
                int SampleSize = 1;
                do {
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    this.backgroundImage = BitmapFactory.decodeFile(this.bgImagePath, options);
                    options.inJustDecodeBounds = false;
                    try {
                        options.inSampleSize = SampleSize;
                        this.backgroundImage = BitmapFactory.decodeFile(this.bgImagePath, options);
                    } catch (OutOfMemoryError e) {
                        SampleSize *= 2;
                    }
                } while (this.backgroundImage == null);
                if (this.backgroundImage.getHeight() > this.height) {
                    try {
                        this.backgroundImage = Bitmap.createScaledBitmap(this.backgroundImage, (int) (((float) this.backgroundImage.getWidth()) * (((float) this.height) / ((float) this.backgroundImage.getHeight()))), this.height, true);
                    } catch (OutOfMemoryError e2) {
                        clearBitmap(this.backgroundImage);
                    }
                }
                this.backgroundRect.set(0.0f, 0.0f, (float) (this.backgroundImage.getWidth() * (this.height / this.backgroundImage.getHeight())), (float) this.height);
                adjustBackgroundRect();
            }
        }

        void clearBitmap(Bitmap bm) {
            bm.recycle();
            System.gc();
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public Engine onCreateEngine() {
        return new RainEngine();
    }
}
