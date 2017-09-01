package com.yalin.wallpaper.electric_plasma;

import android.hardware.SensorEvent;

public class GeneralCompassColor extends SensorColor {
    private ColorGradient gradient;

    public GeneralCompassColor(SensorManagerGetter smg, int... colors) {
        super(smg);
        this.gradient = ColorGradient.makeLoopingGradient(colors);
    }

    public int sensorType() {
        return 3;
    }

    public int colorFunction(SensorEvent event) {
        return this.gradient.sample((double) (event.values[0] / 360.0f));
    }
}
