package com.yalin.wallpaper.doublehelix.lwp;

import android.content.Context;
import android.content.SharedPreferences;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.math.MathUtils;

public class LiveWallpaperAndroidAdapter implements ApplicationListener, AndroidWallpaperListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    LiveWallpaperBaseRenderer liveWallpaper;
    MultiTapListener multiTapListener;

    int maxFPS;

    protected float xOffset = 0.5f;
    protected float yOffset;

    /**
     * A version of xOffset that automatically pans based on finger swipes, ignoring launcher.
     */
    protected float xOffsetFake = 0.5f;
    float xOffsetFakeTarget = 0.5f;
    static final float DIP_TO_XOFFSET_FAKE_RATIO = 750f;
    float swipeXStart;

    float pixPerDIP;

    //Homescreen looping params
    /**
     * A version of xOffset that automatically pans back across when the homescreen loops
     */
    protected float xOffsetLooping;
    /**
     * A version of xOffset that has smoothed motion. Also supports looping.
     */
    protected float xOffsetSmooth;
    private float offsetDelta;
    private float previousOffset = 0.5f;
    private float offsetAdder;
    private float offsetVelocity = 0;
    private float offsetMaxVelocity = 1.5f; //per second
    private float offsetMinVelocity = .4f; //per second
    private static final float DELTA_OFFSET_MIN_LOOPING = 0.9f;
    private boolean catchingUp = false;

    //Screen tapping
    int tapCount = 0;
    float timeSinceFirstTap = 0;
    public static float timeToDoubleTap = 0.35f;
    public static float timeToTripleTap = 0.6f;
    float firstTapX;
    float firstTapY;
    float tapThreshold;
    public static float multiTapThresholdDipRadius = 80;

    private boolean needSettingsUpdate = false;

    /**
     * /
     *
     * @param liveWallpaper
     * @param multiTapListener Optional additional listener for multi-taps. (can be null) Live Wallpaper also listen for multi-taps.
     * @param context
     * @param maxFPS
     */
    public LiveWallpaperAndroidAdapter(LiveWallpaperBaseRenderer liveWallpaper,
                                       MultiTapListener multiTapListener, Context context, int maxFPS) {
        this.liveWallpaper = liveWallpaper;
        this.multiTapListener = multiTapListener;
        this.maxFPS = maxFPS;
        pixPerDIP = context.getResources().getDisplayMetrics().density;
        tapThreshold = multiTapThresholdDipRadius * pixPerDIP;
    }


    /**
     * Adjust these parameters to control the motion when homescreen looping. Defaults
     * are 1.5f and 0.4f.
     */
    protected void setHomescreenLoopableXOffsetParams(float maxVel, float minVel) {
        offsetMaxVelocity = maxVel;
        offsetMinVelocity = minVel;
    }

    private void updateSpecialXOffsets() {

        //Handle xOffsetFake
        offsetDelta = xOffsetFakeTarget - xOffsetFake;
        offsetVelocity = offsetMaxVelocity * MathUtils.sin(offsetDelta * MathUtils.PI);
        if (offsetDelta < 0) {//moving left
            offsetVelocity -= offsetMinVelocity;
        } else if (offsetDelta > 0) {//moving right
            offsetVelocity += offsetMinVelocity;
        }
        offsetAdder = offsetVelocity * Gdx.graphics.getDeltaTime();
        if (Math.abs(offsetAdder) >= Math.abs(offsetDelta)) {
            xOffsetFake = xOffsetFakeTarget;
        } else {
            xOffsetFake += offsetAdder;
        }

        //Handle xOffsetSmooth
        offsetDelta = xOffset - xOffsetSmooth;
        offsetVelocity = offsetMaxVelocity * MathUtils.sin(offsetDelta * MathUtils.PI);
        if (offsetDelta < 0) {//moving left
            offsetVelocity -= offsetMinVelocity;
        } else if (offsetDelta > 0) {//moving right
            offsetVelocity += offsetMinVelocity;
        }
        offsetAdder = offsetVelocity * Gdx.graphics.getDeltaTime();
        if (Math.abs(offsetAdder) >= Math.abs(offsetDelta)) {
            xOffsetSmooth = xOffset;
            catchingUp = false;
        } else {
            xOffsetSmooth += offsetAdder;
        }

        //Handle xOffsetLooping. Uses xOffset if not looping/catching up. Otherwise, use same value as xOffsetSmooth.
        if (!catchingUp) {
            final float offsetChange = xOffset - previousOffset;
            if (offsetChange >= DELTA_OFFSET_MIN_LOOPING || -offsetChange >= DELTA_OFFSET_MIN_LOOPING)
                catchingUp = true;
        }
        if (!catchingUp) { //Not catching up. Just pass through xOffset.
            xOffsetLooping = xOffset;
        } else if (offsetDelta == 0) {  //Just caught up.
            xOffsetLooping = xOffset;
            catchingUp = false;
        } else {  //Still catching up, so use smoothed value.
            xOffsetLooping = xOffsetSmooth;
        }
        previousOffset = xOffset;

    }

    //TODO how is this ever called?
    public void dispose() {
        liveWallpaper.dispose();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        needSettingsUpdate = true;
    }

    @Override
    public void offsetChange(float xOffset, float yOffset, float xOffsetStep,
                             float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void previewStateChange(boolean isPreview) {
        liveWallpaper.setIsPreview(isPreview);
    }

    @Override
    public void create() {
        liveWallpaper.create();
    }

    @Override
    public void resize(int width, int height) {
        liveWallpaper.resize(width, height);
    }

    @Override
    public void render() {

        long frameStartTime = System.currentTimeMillis();

        if (needSettingsUpdate) {
            needSettingsUpdate = false;
            liveWallpaper.onSettingsChanged();
        }

        updateSpecialXOffsets();
        handleTouch();
        handleMultiTaps();
        liveWallpaper.draw(Gdx.graphics.getDeltaTime(), xOffsetFake, xOffsetLooping, xOffsetSmooth, yOffset);

        //limit frame rate
        if (maxFPS < 60) {
            int deltaMillisMin = 1000 / maxFPS;
            long deltaMillis = System.currentTimeMillis() - frameStartTime;
            if (deltaMillis < deltaMillisMin) {
                try {
                    Thread.sleep(deltaMillisMin - deltaMillis);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void pause() {
        liveWallpaper.pause();
    }

    @Override
    public void resume() {
        liveWallpaper.resume();
    }

    public final void handleTouch() {
        Input input = Gdx.input;

        if (input.justTouched()) {
            if (tapCount == 0) {
                timeSinceFirstTap = 0;
                firstTapX = input.getX();
                firstTapY = input.getY();
                tapCount = 1;
            } else if (Math.abs(firstTapX - input.getX()) <= tapThreshold &&
                    Math.abs(firstTapY - input.getY()) <= tapThreshold) {
                tapCount++;
                if (tapCount == 2 && timeSinceFirstTap <= timeToDoubleTap) {
                    liveWallpaper.onDoubleTap();
                    if (multiTapListener != null)
                        multiTapListener.onDoubleTap();
                }
                if (tapCount == 3) {// no need to check time here because we wouldn't be here if not satisfied (see handleMultiTaps)
                    liveWallpaper.onTripleTap();
                    if (multiTapListener != null)
                        multiTapListener.onTripleTap();
                }
            }

            swipeXStart = input.getX(0);
        }

        if (input.isTouched(0) && !input.isTouched(1)) { //only one finger down and swiping
            //Measure swipe distance since last frame and apply. Then reset for next frame.
            float swipeDelta = swipeXStart - input.getX();
            xOffsetFakeTarget += (swipeDelta / pixPerDIP) / DIP_TO_XOFFSET_FAKE_RATIO;
            if (xOffsetFakeTarget < 0)
                xOffsetFakeTarget = 0;
            else if (xOffsetFakeTarget > 1)
                xOffsetFakeTarget = 1;
            swipeXStart = input.getX();
        }

    }

    private void handleMultiTaps() {
        timeSinceFirstTap += Gdx.graphics.getDeltaTime();
        if (timeSinceFirstTap >= timeToTripleTap)
            tapCount = 0;
    }
}
