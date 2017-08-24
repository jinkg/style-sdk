package com.yalin.wallpaper.autumn.motion;

import android.content.Context;

import java.util.Random;

public class Leaves01 extends MotionObject {
    private int cHeight;
    private int cWidth;
    private int maxDg = 30;
    private int minDg = -30;
    private Random random = new Random();
    private boolean rightDirection = true;
    public int rotate = 0;
    private int rotateSpeed = 2;
    public boolean visible = true;
    public float f7x;
    private int xObj = 25;
    private int xway = 0;
    public float f8y;
    private int yObj = 12;
    private int yway = 1;

    public Leaves01(Context context, int cwidth, int cheight) {
        super(context);
        this.cWidth = cwidth;
        this.cHeight = cheight;
        this.f7x = ((float) cwidth) * this.random.nextFloat();
        this.f8y = -30.0f;
    }

    public void updateTouch(int winds, float touchWidths, int touchTimes, float xtouch, float ytouch) {
        if (this.rightDirection) {
            this.rotate += this.rotateSpeed;
        } else {
            this.rotate -= this.rotateSpeed;
        }
        if (this.rotate > this.maxDg) {
            this.rightDirection = false;
        }
        if (this.rotate < this.minDg) {
            this.rightDirection = true;
        }
        if (touchTimes > 0) {
            updateTouchTime(touchTimes, touchWidths, xtouch, ytouch);
        }
        if (this.f8y > ((float) this.cHeight)) {
            this.f7x = ((float) this.cWidth) * this.random.nextFloat();
            this.f8y = ((float) (-this.cHeight)) * this.random.nextFloat();
        }
        this.f7x += (float) (this.xway + winds);
        this.f8y += (float) this.yway;
        if (this.f8y + ((float) (this.yObj * 4)) <= 0.0f || this.f8y > ((float) this.cHeight)) {
            this.visible = false;
        } else {
            this.visible = true;
        }
    }

    public void updateTouchTime(int touchTime, float touchWidths, float xTouch, float yTouch) {
        float xfromTouch = (xTouch - ((float) this.xObj)) - this.f7x;
        float yfromTouch = (yTouch - ((float) this.yObj)) - this.f8y;
        float fromTouch = (float) Math.sqrt((double) ((xfromTouch * xfromTouch) + (yfromTouch * yfromTouch)));
        float touchWidth = touchWidths;
        if (fromTouch < touchWidth) {
            float scaleTouch = (touchWidth - fromTouch) / fromTouch;
            this.f7x -= ((xfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
            this.f8y -= ((yfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
        }
    }
}
