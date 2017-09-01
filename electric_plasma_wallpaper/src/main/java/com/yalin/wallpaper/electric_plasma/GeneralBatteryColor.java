package com.yalin.wallpaper.electric_plasma;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class GeneralBatteryColor implements ColorStream {
    private static final long checkThreshold = 100000;
    private final Context context;
    private ColorGradient gradient;
    private long lastChecked = 0;
    private int lastColor;

    public GeneralBatteryColor(Context context, int... colors) {
        this.context = context;
        this.gradient = new ColorGradient(colors);
    }

    public int getColor() {
        long time = System.currentTimeMillis();
        if (time - this.lastChecked > checkThreshold) {
            this.lastColor = this.gradient.sample(getCurrentBatteryPercent());
            this.lastChecked = time;
        }
        return this.lastColor;
    }

    private double getCurrentBatteryPercent() {
        Intent battery = this.context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return ((double) battery.getIntExtra("level", -1)) / ((double) battery.getIntExtra("scale", -1));
    }

    public boolean isChanged() {
        return System.currentTimeMillis() - this.lastChecked > checkThreshold;
    }

    public void pause() {
    }

    public void unpause() {
    }
}
