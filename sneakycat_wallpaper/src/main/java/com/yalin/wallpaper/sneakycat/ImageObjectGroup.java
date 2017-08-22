package com.yalin.wallpaper.sneakycat;

import com.badlogic.gdx.scenes.scene2d.Group;

import aurelienribon.tweenengine.TweenAccessor;

public class ImageObjectGroup extends Group implements TweenAccessor<ImageObjectGroup> {
    public static final int POSITION_X = 1;
    public static final int POSITION_XY = 3;
    public static final int POSITION_Y = 2;
    public static final int ROTATION = 4;

    public int getValues(ImageObjectGroup target, int tweenType, float[] returnValues) {
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

    public void setValues(ImageObjectGroup target, int tweenType, float[] newValues) {
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
