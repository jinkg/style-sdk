package com.yalin.wallpaper.electric_plasma;

public class GeneralCycleColor implements ColorStream {
    private ColorGradient gradient;
    private double position;

    public GeneralCycleColor(int... colors) {
        this.gradient = ColorGradient.makeLoopingGradient(colors);
    }

    public int getColor() {
        this.position = (this.position + 5.0E-4d) % 1.0d;
        return this.gradient.sample(this.position);
    }

    public boolean isChanged() {
        return true;
    }

    public void pause() {
    }

    public void unpause() {
    }
}
