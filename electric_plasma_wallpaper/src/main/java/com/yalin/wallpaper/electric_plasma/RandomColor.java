package com.yalin.wallpaper.electric_plasma;

import android.graphics.Color;

public class RandomColor implements ColorStream {
    private static final int distance = 533;
    private int currenttarget = randomColor();
    private int oldtarget = randomColor();
    private int step = 0;

    private static int randomColor() {
        return Color.argb(255, (int) (Math.random() * 255.0d), (int) (Math.random() * 255.0d), (int) (Math.random() * 255.0d));
    }

    public int getColor() {
        this.step = (this.step + 1) % distance;
        if (this.step == 0) {
            this.oldtarget = this.currenttarget;
            this.currenttarget = randomColor();
        }
        float frac = ((float) this.step) / 533.0f;
        float inverse = 1.0f - frac;
        return Color.argb(255, (int) ((((float) Color.red(this.oldtarget)) * inverse) + (((float) Color.red(this.currenttarget)) * frac)), (int) ((((float) Color.green(this.oldtarget)) * inverse) + (((float) Color.green(this.currenttarget)) * frac)), (int) ((((float) Color.blue(this.oldtarget)) * inverse) + (((float) Color.blue(this.currenttarget)) * frac)));
    }

    public boolean isChanged() {
        return true;
    }

    public void pause() {
    }

    public void unpause() {
    }
}
