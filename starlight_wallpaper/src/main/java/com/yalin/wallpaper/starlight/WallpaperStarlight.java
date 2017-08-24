package com.yalin.wallpaper.starlight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.util.ArrayList;
import java.util.Random;

public class WallpaperStarlight extends WallpaperServiceProxy {
    private float density;
    public boolean largePortrait = false;
    private final Handler mHandler = new Handler();
    public boolean staticForeground = false;

    public WallpaperStarlight(Context host) {
        super(host);
    }


    public class WallpaperEngine extends ActiveEngine {
        final Config FAST_BITMAP_CONFIG = Config.RGB_565;
        Bitmap background;
        float bottomOffset = 0.0f;
        float cloudBottomOffset = 0.0f;
        float cloudBottomReverseOffset = 0.0f;
        float cloudOffset = 0.0f;
        float cloudReverseOffset = 0.0f;
        float cloudSpeed = 80.0f;
        Bitmap clouds;
        Bitmap cloudsBottom;
        float cloudsRatio = 1.0f;
        boolean doubleTap = true;
        private final Runnable drawRunnable = new C00091();
        float f2f = 0.0f;
        Bitmap flowers;
        int frameDelay = 20;
        int frames = 0;
        boolean isLandscape = false;
        long last = 0;
        long lastShootingStar = 0;
        long lastShootingStarTime = 0;
        long lastShootingStarUpdate;
        private long lastStarFieldUpdate = 0;
        int lastStarXPos = 0;
        int lastStarYPos = 0;
        private float mOffset;
        private final Paint mPaint = new Paint();
        private float mTouchX = -1.0f;
        private boolean mVisible;
        int maximumShootingStarDelayMS = 15000;
        int maximumShootingStarX = 200;
        int minimumShootingStarDelayMS = 5000;
        int minimumShootingStarX = -100;
        int numberOfStars = 25;
        Random f3r = new Random();
        Resources res;
        Random rs = new Random(2);
        float f4s = 0.0f;
        int screenHeight;
        int screenWidth;
        StarHolder sh;
        Bitmap shootingStar;
        Bitmap shootingStar1;
        Bitmap shootingStar2;
        Bitmap shootingStar3;
        Bitmap shootingStar4;
        Bitmap shootingStar5;
        Bitmap shootingStar6;
        float shootingStarFieldDelayMS = 30.0f;
        int shootingStarIndex = 1;
        boolean showClouds = true;
        boolean showFlowers = true;
        boolean showShootingStars = true;
        boolean showStarField = true;
        Bitmap star;
        Animation star1Anim = new Animation();
        private int starFieldDelayMS = 100;
        ArrayList<StarHolder> stars = new ArrayList<>();
        int startStarX = 0;
        int thisShootingStarDelayMS = 4000;
        long timeSinceLastTap = 0;
        float topOffset = -35.0f;

        class C00091 implements Runnable {
            C00091() {
            }

            public void run() {
                WallpaperEngine.this.drawFrame();
            }
        }

        public class Animation {
            ArrayList<AnimationFrame> animation = new ArrayList();
            boolean animationComplete = false;
            boolean autoReverse = false;
            int currentFrame = 0;
            boolean hasNextAnimation = false;
            boolean hasPreviousAnimation = false;
            Animation nextAnimation;
            Animation previousAnimation;
            Random rand = new Random();
            boolean reverseWhenCompleted = false;

            public void setIsAnimationComplete(boolean complete) {
                this.animationComplete = complete;
            }

            public boolean getHasNextAnimation() {
                return this.hasNextAnimation;
            }

            public Animation getNextAnimation() {
                return this.nextAnimation;
            }

            public Animation getPreviousAnimation() {
                return this.previousAnimation;
            }

            public void atCompletionGoTo(Animation a) {
                this.nextAnimation = a;
                this.hasNextAnimation = true;
            }

            public void atReverseGoTo(Animation a) {
                this.previousAnimation = a;
                this.hasPreviousAnimation = true;
            }

            public void addFrame(AnimationFrame f) {
                this.animation.add(f);
                f.setFrameNumber(this.animation.indexOf(f));
            }

            public void reverse() {
                ArrayList<AnimationFrame> newAnimation = new ArrayList();
                for (int x = this.animation.size() - 1; x >= 0; x--) {
                    AnimationFrame f = this.animation.get(x);
                    newAnimation.add(f);
                    f.setFrameNumber(newAnimation.indexOf(f));
                }
                this.animation = newAnimation;
                this.currentFrame = 0;
            }

            public void updateCurrentFrame() {
                if (this.currentFrame < this.animation.size() - 1) {
                    this.currentFrame++;
                    return;
                }
                if (this.autoReverse || this.reverseWhenCompleted) {
                    reverse();
                }
                if (this.autoReverse || !getHasNextAnimation()) {
                    this.currentFrame = 0;
                } else {
                    setIsAnimationComplete(true);
                }
            }

            public AnimationFrame getNextFrame(long currentTime) {
                AnimationFrame currentAnimationFrame = this.animation.get(this.currentFrame);
                if (currentTime - currentAnimationFrame.lastTime < ((long) currentAnimationFrame.timeToNextFrame)) {
                    return currentAnimationFrame;
                }
                if (currentAnimationFrame.getRandomizeTimeToNextFrame()) {
                    currentAnimationFrame.setTimeToNextFrame(this.rand.nextInt(currentAnimationFrame.timeToNextFrameHighBounds - currentAnimationFrame.timeToNextFrameLowBounds) + currentAnimationFrame.timeToNextFrameLowBounds);
                }
                updateCurrentFrame();
                AnimationFrame newAnimationFrame = this.animation.get(this.currentFrame);
                newAnimationFrame.lastTime = currentTime;
                return newAnimationFrame;
            }

        }

        public class AnimationFrame {
            Bitmap bitmap;
            int frame = 0;
            long lastTime = 0;
            boolean randomizeTimeToNextFrame = false;
            int resourceID = 0;
            int timeToNextFrame = 100;
            int timeToNextFrameHighBounds = 150;
            int timeToNextFrameLowBounds = 50;

            public int getFrameNumber() {
                return this.frame;
            }

            public void setFrameNumber(int f) {
                this.frame = f;
            }

            public int getResourceID() {
                return this.resourceID;
            }

            public void setResourceID(int id) {
                this.resourceID = id;
            }

            public int getTimeToNextFrame() {
                return this.timeToNextFrame;
            }

            public void setTimeToNextFrame(int time) {
                this.timeToNextFrame = time;
            }

            public long getLastTime() {
                return this.lastTime;
            }

            public void setLastTine(long time) {
                this.lastTime = time;
            }

            public Bitmap getBitmap() {
                return this.bitmap;
            }

            public void setBitmap(Bitmap b) {
                this.bitmap = b;
            }

            public boolean getRandomizeTimeToNextFrame() {
                return this.randomizeTimeToNextFrame;
            }

            public void setRandomizeTimeToNextFrame(boolean randomize) {
                this.randomizeTimeToNextFrame = randomize;
            }

            public void setBoundsForTimeToNextFrame(int low, int high) {
                this.timeToNextFrameLowBounds = low;
                this.timeToNextFrameHighBounds = high;
            }
        }

        public class StarHolder {
            float alpha = 0.2f;
            int alphaDirection = 1;
            float alpha_speed = 0.1f;
            float maxAlpha = 0.2f;
            float minAlpha = 0.2f;
            int f0x = 0;
            int f1y = 0;
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

        private Bitmap decodeBitmap(int id) {
            int maxSize;
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(this.res, id, o);
            if (this.isLandscape) {
                maxSize = this.screenWidth;
            } else {
                maxSize = this.screenHeight;
            }
            if (o.outHeight <= maxSize) {
                int i = o.outWidth;
            }
            Options o2 = new Options();
            o2.inSampleSize = 1;
            o2.inPurgeable = true;
            return BitmapFactory.decodeResource(this.res, id, o2);
        }

        WallpaperEngine() {
            super();
            Paint paint = this.mPaint;
            paint.setAntiAlias(true);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStyle(Style.STROKE);
            this.res = WallpaperStarlight.this.getResources();
            new Options().inSampleSize = 1;
            this.mPaint.setFakeBoldText(false);
            this.mPaint.setStrokeWidth(0.0f);
            this.mPaint.setTextSize(16.0f);
            onSharedPreferenceChanged();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        private void cleanResources() {
            if (this.background != null) {
                this.background.recycle();
            }
            if (this.clouds != null) {
                this.clouds.recycle();
            }
            if (this.shootingStar1 != null) {
                this.shootingStar1.recycle();
            }
            if (this.shootingStar2 != null) {
                this.shootingStar2.recycle();
            }
            if (this.shootingStar3 != null) {
                this.shootingStar3.recycle();
            }
            if (this.shootingStar4 != null) {
                this.shootingStar4.recycle();
            }
            if (this.shootingStar5 != null) {
                this.shootingStar5.recycle();
            }
            if (this.shootingStar6 != null) {
                this.shootingStar6.recycle();
            }
            this.background = null;
            this.clouds = null;
            this.flowers.recycle();
        }

        public void onDestroy() {
            super.onDestroy();
            WallpaperStarlight.this.mHandler.removeCallbacks(this.drawRunnable);
            cleanResources();
            System.gc();
        }

        public void onVisibilityChanged(boolean visible) {
            this.mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                WallpaperStarlight.this.mHandler.removeCallbacks(this.drawRunnable);
            }
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            int i;
            int i2;
            super.onSurfaceChanged(holder, format, width, height);
            this.screenWidth = width;
            this.screenHeight = height;
            this.maximumShootingStarX = width - 150;
            setupTwinklingStars();
            if (width > height) {
                this.isLandscape = true;
            } else {
                this.isLandscape = false;
            }
            try {
                drawFrame();
            } catch (Exception e) {
            }
            if (WallpaperStarlight.this.largePortrait) {
                this.cloudsRatio = 1.5f;
            } else {
                this.cloudsRatio = 1.0f;
            }
            this.background = loadBitmap(this.res.getDrawable(R.drawable.background), this.FAST_BITMAP_CONFIG);
            this.background = Bitmap.createScaledBitmap(this.background, this.isLandscape ? this.screenWidth : this.screenHeight, this.isLandscape ? this.screenWidth : this.screenHeight, true);
            System.gc();
            this.clouds = decodeBitmap(R.drawable.starlight_clouds);
            this.clouds = Bitmap.createScaledBitmap(this.clouds, this.isLandscape ? (int) (((float) this.screenWidth) * 1.19f) : (int) ((((float) this.screenHeight) * 1.19f) * this.cloudsRatio), this.isLandscape ? (int) (((float) this.screenWidth) * 0.224f) : (int) ((((float) this.screenHeight) * 0.224f) * this.cloudsRatio), true);
            System.gc();
            this.cloudsBottom = decodeBitmap(R.drawable.starlight_clouds_bottom);
            this.cloudsBottom = Bitmap.createScaledBitmap(this.cloudsBottom, this.isLandscape ? (int) (((float) this.screenWidth) * 1.19f) : (int) ((((float) this.screenHeight) * 1.19f) * this.cloudsRatio), this.isLandscape ? (int) (((float) this.screenWidth) * 0.22f) : (int) ((((float) this.screenHeight) * 0.22f) * this.cloudsRatio), true);
            System.gc();
            this.flowers = decodeBitmap(R.drawable.flowers);
            this.flowers = Bitmap.createScaledBitmap(this.flowers, this.isLandscape ? (int) (((float) this.screenWidth) * 0.525f) : (int) (((float) this.screenHeight) * 0.525f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.409f) : (int) (((float) this.screenHeight) * 0.409f), true);
            System.gc();
            this.shootingStar1 = decodeBitmap(R.drawable.shooting_star1);
            this.shootingStar1 = Bitmap.createScaledBitmap(this.shootingStar1, this.isLandscape ? (int) (((float) this.screenWidth) * 0.051f) : (int) (((float) this.screenHeight) * 0.051f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.05f) : (int) (((float) this.screenHeight) * 0.05f), true);
            System.gc();
            this.shootingStar2 = decodeBitmap(R.drawable.shooting_star2);
            this.shootingStar2 = Bitmap.createScaledBitmap(this.shootingStar2, this.isLandscape ? (int) (((float) this.screenWidth) * 0.076f) : (int) (((float) this.screenHeight) * 0.076f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.076f) : (int) (((float) this.screenHeight) * 0.076f), true);
            System.gc();
            this.shootingStar3 = decodeBitmap(R.drawable.shooting_star3);
            this.shootingStar3 = Bitmap.createScaledBitmap(this.shootingStar3, this.isLandscape ? (int) (((float) this.screenWidth) * 0.104f) : (int) (((float) this.screenHeight) * 0.104f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.104f) : (int) (((float) this.screenHeight) * 0.104f), true);
            System.gc();
            this.shootingStar4 = decodeBitmap(R.drawable.shooting_star4);
            this.shootingStar4 = Bitmap.createScaledBitmap(this.shootingStar4, this.isLandscape ? (int) (((float) this.screenWidth) * 0.082f) : (int) (((float) this.screenHeight) * 0.082f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.082f) : (int) (((float) this.screenHeight) * 0.082f), true);
            System.gc();
            this.shootingStar5 = decodeBitmap(R.drawable.shooting_star5);
            this.shootingStar5 = Bitmap.createScaledBitmap(this.shootingStar5, this.isLandscape ? (int) (((float) this.screenWidth) * 0.056f) : (int) (((float) this.screenHeight) * 0.056f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.056f) : (int) (((float) this.screenHeight) * 0.056f), true);
            System.gc();
            this.shootingStar6 = decodeBitmap(R.drawable.shooting_star6);
            this.shootingStar6 = Bitmap.createScaledBitmap(this.shootingStar6, this.isLandscape ? (int) (((float) this.screenWidth) * 0.032f) : (int) (((float) this.screenHeight) * 0.032f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.032f) : (int) (((float) this.screenHeight) * 0.032f), true);
            System.gc();
            Bitmap star1 = Bitmap.createScaledBitmap(decodeBitmap(R.drawable.star1), this.isLandscape ? (int) (((float) this.screenWidth) * 0.013f) : (int) (((float) this.screenHeight) * 0.013f), this.isLandscape ? (int) (((float) this.screenWidth) * 0.013f) : (int) (((float) this.screenHeight) * 0.013f), true);
            Bitmap star2 = decodeBitmap(R.drawable.star2);
            if (this.isLandscape) {
                i = (int) (((float) this.screenWidth) * 0.013f);
            } else {
                i = (int) (((float) this.screenHeight) * 0.013f);
            }
            if (this.isLandscape) {
                i2 = (int) (((float) this.screenWidth) * 0.013f);
            } else {
                i2 = (int) (((float) this.screenHeight) * 0.013f);
            }
            star2 = Bitmap.createScaledBitmap(star2, i, i2, true);
            AnimationFrame star11 = new AnimationFrame();
            star11.setBitmap(star1);
            AnimationFrame star12 = new AnimationFrame();
            star12.setBitmap(star2);
            this.star1Anim.addFrame(star11);
            this.star1Anim.addFrame(star12);
            this.cloudReverseOffset = (float) this.clouds.getWidth();
            this.cloudBottomReverseOffset = (float) this.cloudsBottom.getWidth();
            this.bottomOffset = ((float) this.cloudsBottom.getHeight()) * 0.8f;
            if (WallpaperStarlight.this.density > 1.0f) {
                this.bottomOffset += 20.0f * WallpaperStarlight.this.density;
                this.topOffset -= 50.0f * WallpaperStarlight.this.density;
            }
            setupTwinklingStars();
        }

        private void setupTwinklingStars() {
            if (this.stars == null) {
                this.stars = new ArrayList<>();
            }
            if (this.rs == null) {
                this.rs = new Random(2);
            }
            if (this.background != null) {
                this.stars.clear();
                for (int i = 0; i < this.numberOfStars; i++) {
                    StarHolder sh = new StarHolder();
                    boolean notGood;
                    do {
                        sh.f0x = this.rs.nextInt(this.background.getWidth());
                        notGood = !(((float) sh.f0x) <= ((float) this.background.getWidth()) * 0.27f || ((float) sh.f0x) >= ((float) this.background.getWidth()) * 0.49f);
                    } while (notGood);
                    sh.f1y = this.rs.nextInt(this.screenHeight / 2);
                    sh.maxAlpha = this.rs.nextFloat();
                    if (sh.maxAlpha > 0.95f) {
                        sh.maxAlpha = 0.95f;
                    }
                    if (sh.maxAlpha < 0.5f) {
                        sh.maxAlpha = 0.5f;
                    }
                    sh.minAlpha = this.rs.nextFloat();
                    if (sh.minAlpha > sh.maxAlpha || sh.maxAlpha - sh.minAlpha < 0.3f) {
                        sh.minAlpha = sh.maxAlpha - 0.7f;
                    }
                    if (((double) sh.minAlpha) < 0.2d) {
                        sh.minAlpha = 0.2f;
                    }
                    sh.alpha = this.rs.nextFloat() * sh.maxAlpha;
                    sh.alpha_speed = (this.rs.nextFloat() + 0.1f) * 0.001f;
                    this.stars.add(sh);
                }
            }
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.mVisible = false;
            WallpaperStarlight.this.mHandler.removeCallbacks(this.drawRunnable);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            this.mOffset = ((float) xPixels) * 0.4f;
        }

        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == 2) {
                this.mTouchX = -1.0f;
            } else {
                this.mTouchX = event.getX();
            }
            super.onTouchEvent(event);
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
            try {
                WallpaperStarlight.this.mHandler.removeCallbacks(this.drawRunnable);
                if (this.mVisible) {
                    WallpaperStarlight.this.mHandler.postDelayed(this.drawRunnable, (long) this.frameDelay);
                }
            } catch (Exception e2) {
            }
        }

        void drawBackground(Canvas c) {
            if (this.isLandscape || WallpaperStarlight.this.staticForeground) {
                this.mOffset = 0.0f;
            }
            long now = SystemClock.elapsedRealtime();
            long elapsed = now - this.last;
            this.timeSinceLastTap += elapsed;
            if (this.last == 0) {
                elapsed = 0;
            }
            if (elapsed > 2000) {
                elapsed = 0;
            }
            float starFieldElapsedTime = (float) (now - this.lastStarFieldUpdate);
            float shootingStarElapsedTime = (float) (now - this.lastShootingStarUpdate);
            boolean newShootingStar = false;
            if (((float) (now - this.lastShootingStar)) >= ((float) this.thisShootingStarDelayMS)) {
                newShootingStar = true;
                this.thisShootingStarDelayMS = 10000;
                this.lastShootingStar = now;
            }
            if (starFieldElapsedTime >= ((float) this.starFieldDelayMS)) {
                this.lastStarFieldUpdate = now;
            }
            boolean updateShootingStar = false;
            if (shootingStarElapsedTime >= this.shootingStarFieldDelayMS) {
                updateShootingStar = true;
                this.lastShootingStarUpdate = now;
            }
            this.f2f += ((float) elapsed) / (this.cloudSpeed * 2.5f);
            this.f4s += ((float) elapsed) / this.cloudSpeed;
            if ((((float) this.clouds.getWidth()) - this.f2f) + this.cloudOffset < 0.0f) {
                this.cloudOffset = this.cloudReverseOffset + ((float) this.clouds.getWidth());
            }
            if ((((float) this.clouds.getWidth()) + this.cloudReverseOffset) - this.f2f < 0.0f) {
                this.cloudReverseOffset = this.cloudOffset + ((float) this.clouds.getWidth());
            }
            if ((((float) this.cloudsBottom.getWidth()) - this.f4s) + this.cloudBottomOffset < 0.0f) {
                this.cloudBottomOffset = this.cloudBottomReverseOffset + ((float) this.cloudsBottom.getWidth());
            }
            if ((((float) this.cloudsBottom.getWidth()) + this.cloudBottomReverseOffset) - this.f4s < 0.0f) {
                this.cloudBottomReverseOffset = this.cloudBottomOffset + ((float) this.cloudsBottom.getWidth());
            }
            c.drawBitmap(this.background, this.mOffset, 0.0f, this.mPaint);
            if (this.showFlowers) {
                c.drawBitmap(this.flowers, (this.mOffset + ((float) this.background.getWidth())) - ((float) this.flowers.getWidth()), (float) (this.background.getHeight() - this.flowers.getHeight()), this.mPaint);
            }
            if (this.showStarField) {
                this.star = this.star1Anim.getNextFrame(now).getBitmap();
                for (int i = 0; i < this.numberOfStars; i++) {
                    this.sh = (StarHolder) this.stars.get(i);
                    StarHolder starHolder = this.sh;
                    starHolder.alpha += (((float) elapsed) * this.sh.alpha_speed) * ((float) this.sh.alphaDirection);
                    int alpha = (int) (255.0f * this.sh.alpha);
                    if (this.sh.alpha >= this.sh.maxAlpha) {
                        this.sh.alphaDirection = -1;
                    } else if (this.sh.alpha <= this.sh.minAlpha) {
                        this.sh.alphaDirection = 1;
                    }
                    this.mPaint.setAlpha(alpha);
                    c.drawBitmap(this.star, ((float) this.sh.f0x) + this.mOffset, (float) this.sh.f1y, this.mPaint);
                }
                this.mPaint.setAlpha(255);
            }
            int starPosX = this.lastStarXPos;
            int starPosY = this.lastStarYPos;
            boolean drawShootingStar = true;
            if (this.showShootingStars) {
                if (this.shootingStarIndex > 6) {
                    this.shootingStarIndex = 1;
                }
                if (!newShootingStar && this.shootingStarIndex == 1) {
                    updateShootingStar = false;
                    drawShootingStar = false;
                }
                if (updateShootingStar) {
                    switch (this.shootingStarIndex) {
                        case 1:
                            starPosX = 0;
                            starPosY = 0;
                            this.startStarX = this.f3r.nextInt(this.maximumShootingStarX + Math.abs(this.minimumShootingStarX)) - Math.abs(this.minimumShootingStarX);
                            this.thisShootingStarDelayMS = this.f3r.nextInt(this.minimumShootingStarDelayMS + this.maximumShootingStarDelayMS) - this.minimumShootingStarDelayMS;
                            this.shootingStar = this.shootingStar1;
                            break;
                        case 2:
                            starPosX = (int) (((double) starPosX) + (((double) this.shootingStar1.getWidth()) * 0.4d));
                            starPosY = (int) (((double) starPosY) + (((double) this.shootingStar1.getHeight()) * 0.4d));
                            this.shootingStar = this.shootingStar2;
                            break;
                        case 3:
                            starPosX = (int) (((double) starPosX) + (((double) this.shootingStar2.getWidth()) * 0.4d));
                            starPosY = (int) (((double) starPosY) + (((double) this.shootingStar2.getHeight()) * 0.4d));
                            this.shootingStar = this.shootingStar3;
                            break;
                        case 4:
                            starPosX = (int) (((double) starPosX) + (((double) this.shootingStar3.getWidth()) * 0.5d));
                            starPosY = (int) (((double) starPosY) + (((double) this.shootingStar3.getHeight()) * 0.5d));
                            this.shootingStar = this.shootingStar4;
                            break;
                        case 5:
                            starPosX = (int) (((double) starPosX) + (((double) this.shootingStar4.getWidth()) * 0.7d));
                            starPosY = (int) (((double) starPosY) + (((double) this.shootingStar4.getHeight()) * 0.7d));
                            this.shootingStar = this.shootingStar5;
                            break;
                        case 6:
                            starPosX = (int) (((double) starPosX) + (((double) this.shootingStar5.getWidth()) * 0.7d));
                            starPosY = (int) (((double) starPosY) + (((double) this.shootingStar5.getHeight()) * 0.7d));
                            this.shootingStar = this.shootingStar6;
                            break;
                    }
                    this.lastStarXPos = starPosX;
                    this.lastStarYPos = starPosY;
                    this.shootingStarIndex++;
                }
                if (this.shootingStar != null && drawShootingStar) {
                    c.drawBitmap(this.shootingStar, (float) (this.startStarX + starPosX), (float) starPosY, this.mPaint);
                }
                this.lastShootingStarTime = now;
            }
            if (this.showClouds) {
                c.drawBitmap(this.clouds, ((-this.f2f) + this.mOffset) + this.cloudOffset, this.topOffset, this.mPaint);
                c.drawBitmap(this.clouds, ((-this.f2f) + this.mOffset) + this.cloudReverseOffset, this.topOffset, this.mPaint);
                c.drawBitmap(this.cloudsBottom, ((-this.f4s) + this.mOffset) + this.cloudBottomOffset, this.bottomOffset, this.mPaint);
                c.drawBitmap(this.cloudsBottom, ((-this.f4s) + this.mOffset) + this.cloudBottomReverseOffset, this.bottomOffset, this.mPaint);
            }
            this.last = now;
            this.frames++;
        }

        private void onSharedPreferenceChanged() {
            WallpaperStarlight.this.largePortrait = false;
            WallpaperStarlight.this.staticForeground = false;
            this.showClouds = true;
            this.showShootingStars = true;
            this.showStarField = true;
            this.showFlowers = true;
            this.doubleTap = false;

            this.frameDelay = 50;
            this.numberOfStars = 50;
            if (this.screenWidth > 0) {
                setupTwinklingStars();
            }
            this.cloudSpeed = 80.0f;
            this.minimumShootingStarDelayMS = 500;
            this.maximumShootingStarDelayMS = 2000;
        }
    }

    public void onCreate() {
        super.onCreate();
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        this.density = outMetrics.density;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }
}
