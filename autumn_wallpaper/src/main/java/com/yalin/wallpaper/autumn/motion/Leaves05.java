package com.yalin.wallpaper.autumn.motion;

import android.content.Context;

import java.util.Random;

public class Leaves05 extends MotionObject {
    private int cHeight;
    private int cWidth;
    private int maxDg = 55;
    private int minDg = -45;
    private Random random = new Random();
    private boolean rightDirection = true;
    public int rotate = 0;
    private int rotateSpeed = 2;
    public boolean visible = true;
    public float f15x;
    private int xObj = 22;
    private int xway = 0;
    public float f16y;
    private int yObj = 20;
    private int yway = 1;

    public Leaves05(Context context, int cwidth, int cheight) {
        super(context);
        this.cWidth = cwidth;
        this.cHeight = cheight;
        this.f15x = ((float) cwidth) * this.random.nextFloat();
        this.f16y = -40.0f;
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
        if (this.f16y > ((float) this.cHeight)) {
            this.f15x = ((float) this.cWidth) * this.random.nextFloat();
            this.f16y = ((float) (-this.cHeight)) * this.random.nextFloat();
        }
        this.f15x += (float) (this.xway + winds);
        this.f16y += (float) this.yway;
        if (this.f16y + ((float) (this.yObj * 4)) <= 0.0f || this.f16y > ((float) this.cHeight)) {
            this.visible = false;
        } else {
            this.visible = true;
        }
    }

    public void updateTouchTime(int touchTime, float touchWidths, float xTouch, float yTouch) {
        float xfromTouch = (xTouch - ((float) this.xObj)) - this.f15x;
        float yfromTouch = (yTouch - ((float) this.yObj)) - this.f16y;
        float fromTouch = (float) Math.sqrt((double) ((xfromTouch * xfromTouch) + (yfromTouch * yfromTouch)));
        float touchWidth = touchWidths;
        if (fromTouch < touchWidth) {
            float scaleTouch = (touchWidth - fromTouch) / fromTouch;
            this.f15x -= ((xfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
            this.f16y -= ((yfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
        }
    }
}
