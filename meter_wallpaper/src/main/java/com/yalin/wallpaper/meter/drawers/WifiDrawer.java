package com.yalin.wallpaper.meter.drawers;

import android.content.Context;
import android.graphics.Color;


public class WifiDrawer extends TriangleFillDrawer {
    private final String TAG = this.getClass().getSimpleName();


    public WifiDrawer(Context context) {
        super(
                context,
                Color.parseColor("#0c272b"),
                Color.parseColor("#005e53"),
                Color.parseColor("#25ceb6"),
                Color.parseColor("#005e53")
        );
        connected = true;
        _percent = (float) (0.5 - 0.001);

    }

}
