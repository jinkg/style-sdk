package com.yalin.wallpaper.lazypanda;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ImageObject extends Actor implements TweenAccessor<ImageObject> {
    public TextureRegion image;
    public boolean show = true;

    public void draw(Batch batch, float parentAlpha) {
        if (this.show) {
            batch.enableBlending();
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            batch.draw(this.image, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), false);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public int getValues(ImageObject target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case 1:
                returnValues[0] = target.getX();
                return 1;
            case 2:
                returnValues[0] = target.getY();
                return 1;
            case 3:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case 4:
                returnValues[0] = target.getRotation();
                return 1;
            default:
                return -1;
        }
    }

    public void setValues(ImageObject target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case 1:
                target.setX(newValues[0]);
                return;
            case 2:
                target.setY(newValues[0]);
                return;
            case 3:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                return;
            case 4:
                target.setRotation(newValues[0]);
                return;
            default:
                return;
        }
    }
}
