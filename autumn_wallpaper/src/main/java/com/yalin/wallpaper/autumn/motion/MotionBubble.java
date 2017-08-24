package com.yalin.wallpaper.autumn.motion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

import java.util.Random;

public class MotionBubble extends MotionObject {
    private static final int[] sizeArray = new int[]{1, 3, 7, 10, 15, 20, 24, 29, 34, 38, 40, 46, 51, 54};
    private int arrayImageIndex = 1;
    public Bitmap bitmap;
    private int cHeight;
    private int cWidth;
    private float factor;
    public Bitmap imageMotion;
    Options options = new Options();
    private Random random = new Random();
    public boolean sizeMax = true;
    public int timeBroken = 0;
    public boolean visible = true;
    public float f19x;
    private float xObj;
    private int xway = 0;
    public float f20y;
    private float yObj;
    private int yway = -2;

    public MotionBubble(Context context, int cwidth, int cheight, int numindex, float factors) {
        super(context);
        this.cWidth = cwidth;
        this.cHeight = cheight;
        this.arrayImageIndex = numindex;
        this.factor = factors;
        this.f19x = ((float) cwidth) * this.random.nextFloat();
        this.f20y = (float) cheight;
        if (this.sizeMax) {
            this.xObj = 40.0f;
            this.yObj = 40.0f;
        } else {
            this.xObj = 30.0f;
            this.yObj = 30.0f;
        }
        this.xObj *= this.factor;
        this.yObj *= this.factor;
        for (int i : sizeArray) {
            if (i == this.arrayImageIndex) {
                this.sizeMax = false;
            }
        }
        if (!this.sizeMax) {
            this.yway = -3;
        }
    }

    public void updateTouch(int winds, float touchWidths, int touchTimes, float xtouch, float ytouch) {
        if (touchTimes == 9 && this.timeBroken == 0) {
            updateTouchTime(touchTimes, touchWidths, xtouch, ytouch);
        }
        if (this.timeBroken > 0) {
            this.timeBroken--;
        }
        if (this.timeBroken == 1) {
            if (this.sizeMax) {
                this.yway = -2;
            } else {
                this.yway = -3;
            }
            this.f19x = ((float) this.cWidth) * this.random.nextFloat();
            this.f20y = ((float) this.cHeight) * (this.random.nextFloat() + 2.0f);
        }
        if (this.f20y < -80.0f) {
            this.f20y = ((float) this.cHeight) * (this.random.nextFloat() + 2.0f);
        }
        if (this.f19x < -40.0f || this.f19x > ((float) this.cWidth)) {
            this.f19x = ((float) this.cWidth) * this.random.nextFloat();
            this.f20y = ((float) this.cHeight) * ((float) (1.4d + ((double) this.random.nextFloat())));
        }
        this.f19x += (float) (this.xway + winds);
        this.f20y += (float) this.yway;
        if (this.f20y + (this.yObj * 2.0f) <= 0.0f || this.f20y > ((float) this.cHeight)) {
            this.visible = false;
        } else {
            this.visible = true;
        }
    }

    public void updateTouchTime(int touchTime, float touchWidths, float xTouch, float yTouch) {
        float xfromTouch = (xTouch - this.f19x) - this.xObj;
        float yfromTouch = (yTouch - this.f20y) - this.yObj;
        if (((float) Math.sqrt((double) ((xfromTouch * xfromTouch) + (yfromTouch * yfromTouch)))) < 40.0f * this.factor) {
            this.yway = 0;
            this.timeBroken = 15;
        }
    }
}
