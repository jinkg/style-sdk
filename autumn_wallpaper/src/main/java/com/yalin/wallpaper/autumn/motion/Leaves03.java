package com.yalin.wallpaper.autumn.motion;

import android.content.Context;

import java.util.Random;

public class Leaves03 extends MotionObject {
    private int cHeight;
    private int cWidth;
    private Random random = new Random();
    private boolean rightDirection = true;
    public int rotate = 0;
    private int rotateSpeed = 8;
    public boolean visible = true;
    public float f11x;
    private int xObj = 15;
    private int xway = 0;
    public float f12y;
    private int yObj = 14;
    private int yway = 2;

    public Leaves03(Context context, int cwidth, int cheight) {
        super(context);
        this.cWidth = cwidth;
        this.cHeight = cheight;
        this.f11x = ((float) cwidth) * this.random.nextFloat();
        this.f12y = -30.0f;
    }

    public void updateTouch(int winds, float touchWidths, int touchTimes, float xtouch, float ytouch) {
        if (this.rotate > 360) {
            this.rotate -= 360;
        }
        if (this.rotate < 0) {
            this.rotate += 360;
        }
        if (this.rightDirection) {
            this.rotate += this.rotateSpeed;
        } else {
            this.rotate -= this.rotateSpeed;
        }
        if (touchTimes > 0) {
            updateTouchTime(touchTimes, touchWidths, xtouch, ytouch);
        }
        if (this.f12y > ((float) this.cHeight)) {
            this.f11x = ((float) this.cWidth) * this.random.nextFloat();
            this.f12y = ((float) (-this.cHeight)) * this.random.nextFloat();
        }
        this.f11x += (float) (this.xway + winds);
        this.f12y += (float) this.yway;
        if (this.f12y + ((float) (this.yObj * 4)) <= 0.0f || this.f12y > ((float) this.cHeight)) {
            this.visible = false;
        } else {
            this.visible = true;
        }
    }

    public void updateTouchTime(int touchTime, float touchWidths, float xTouch, float yTouch) {
        float xfromTouch = (xTouch - ((float) this.xObj)) - this.f11x;
        float yfromTouch = (yTouch - ((float) this.yObj)) - this.f12y;
        float fromTouch = (float) Math.sqrt((double) ((xfromTouch * xfromTouch) + (yfromTouch * yfromTouch)));
        float touchWidth = touchWidths;
        if (fromTouch < touchWidth) {
            float scaleTouch = (touchWidth - fromTouch) / fromTouch;
            this.f11x -= ((xfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
            this.f12y -= ((yfromTouch * scaleTouch) * ((float) touchTime)) / 45.0f;
        }
    }
}
