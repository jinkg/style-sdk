package com.yalin.wallpaper.electric_plasma;

import android.graphics.Color;
import android.hardware.SensorEvent;

public class AccelerometerColor extends SensorColor {
    public AccelerometerColor(SensorManagerGetter smg) {
        super(smg);
    }

    public int sensorType() {
        return 1;
    }

    public int colorFunction(SensorEvent event) {
        return Color.rgb(clamp((int) (Math.abs(event.values[0]) * 13.061224f)), clamp((int) (Math.abs(event.values[1]) * 13.061224f)), clamp((int) (Math.abs(event.values[2]) * 13.061224f)));
    }

    private int clamp(int value) {
        if (value > 255) {
            return 255;
        }
        return value < 0 ? 0 : value;
    }
}
