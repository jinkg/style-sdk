package com.yalin.wallpaper.autumn.motion;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class MotionObject {
    public Context context;

    public MotionObject(Context context) {
        this.context = context;
    }

    public static Bitmap creatFromScale(float scale, Bitmap oldImage) {
        return Bitmap.createScaledBitmap(oldImage, (int) (((float) oldImage.getWidth()) * scale), (int) (((float) oldImage.getHeight()) * scale), true);
    }

    public Context getContext() {
        return this.context;
    }

    public void updateTouch(int winds, int touchWidths, int touchTimes, float xtouch, float ytouch) {
    }

    public void updateTouchTime(int touchTime, int touchWidths, float xTouch, float yTouch) {
    }
}
