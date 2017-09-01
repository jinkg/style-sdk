package com.yalin.wallpaper.electric_plasma.gl;

public class TimeKeeper {
    private long lastTimeCalled = System.currentTimeMillis();
    double ratePerSecond;

    public TimeKeeper(double ratePerSecond) {
        this.ratePerSecond = ratePerSecond;
    }

    public double delta() {
        long currentTime = System.currentTimeMillis();
        long diff = Math.min(currentTime - this.lastTimeCalled, 60);
        if (diff < 0) {
            diff = 0;
        }
        double seconds = ((double) diff) / 1000.0d;
        this.lastTimeCalled = currentTime;
        return this.ratePerSecond * seconds;
    }
}
