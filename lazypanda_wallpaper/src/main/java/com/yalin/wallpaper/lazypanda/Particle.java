package com.yalin.wallpaper.lazypanda;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

public class Particle extends ImageObject implements TweenCallback {
    public int imageIndex;
    public TextureRegion[] images;
    public int layer;
    public float movementDuration;
    public TweenManager myTweenManager;
    public int rotationDirection;
    public float rotationDuration;
    public float startingX;
    public float startingY;

    public TextureRegion GetImage() {
        return this.images[this.imageIndex];
    }

    public void Reset() {
        this.imageIndex = MathUtils.random(this.images.length - 1);
        setOrigin(((float) GetImage().getRegionWidth()) / 2.0f, ((float) GetImage().getRegionHeight()) / 2.0f);
        this.rotationDuration = 1.0f;
        this.movementDuration = MathUtils.random(10.0f, 30.0f);
        this.startingX = 0.0f;
        this.startingY = 0.0f;
        setX(MathUtils.random(0.0f, 1000.0f));
        setY(1280.0f);
        this.rotationDirection = MathUtils.randomSign();
        this.layer = MathUtils.randomSign();
        this.myTweenManager.killTarget(this);
        Tween.to(this, 3, this.movementDuration).target(MathUtils.random(1280.0f), -100.0f).setCallback(this).start(this.myTweenManager);
        Tween.to(this, 4, MathUtils.random(1.0f, 4.0f)).targetRelative(360.0f * ((float) this.rotationDirection)).ease(Linear.INOUT).repeat(-1, 0.0f).start(this.myTweenManager);
    }

    public void onEvent(int i, BaseTween<?> baseTween) {
        if (i == 8) {
            Reset();
        }
    }
}
