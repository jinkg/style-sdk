package com.yalin.wallpaper.autumn.motion;

import android.content.Context;

import java.util.Random;

public class Leaves06 extends MotionObject {
    private int cHeight;
    private int cWidth;
    private int maxDg = 45;
    private int minDg = -25;
    private Random random = new Random();
    private boolean rightDirection = true;
    public int rotate = 0;
    private int rotateSpeed = 2;
    public boolean visible = true;
    public float f17x;
    private int xObj = 23;
    private int xway = 0;
    public float f18y;
    private int yObj = 23;
    private int yway = 1;

    public Leaves06(Context context, int cwidth, int cheight) {
        super(context);
        this.cWidth = cwidth;
        this.cHeight = cheight;
        this.f17x = ((float) cwidth) * this.random.nextFloat();
        this.f18y = -40.0f;
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
        if (this.f18y > ((float) this.cHeight)) {
            this.f17x = ((float) this.cWidth) * this.random.nextFloat();
            this.f18y = ((float) (-this.cHeight)) * this.random.nextFloat();
        }
        this.f17x += (float) (this.xway + winds);
        this.f18y += (float) this.yway;
        if (this.f18y + ((float) (this.yObj * 4)) <= 0.0f || this.f18y > ((float) this.cHeight)) {
            this.visible = false;
        } else {
            this.visible = true;
        }
    }

    public void updateTouchTime(int touchTime, float touchWidths, float xTouch, float yTouch) {
        float xfromTouch = (xTouch - ((float) this.xObj)) - this.f17x;
        float yfromTouch = (yTouch - ((float) this.yObj)) - this.f18y;
        float fromTouch = (float) Math.sqrt((double) ((xfromTouch * xfromTouch) + (yfromTouch * yfromTouch)));
        float touchWidth = touchWidths;
        if (fromTouch < touchWidth) {
            float scaleTouch = (touchWidth - fromTouch) / fromTouch;
            this.f17x -= ((xfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
            this.f18y -= ((yfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
        }
    }
}
