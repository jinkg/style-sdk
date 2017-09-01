package com.yalin.wallpaper.electric_plasma;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class SensorColor implements ColorStream, SensorEventListener {
    private boolean changed = true;
    private int color;
    private long deactivateThreshold = 1000;
    private long lastColorCall;
    private SensorManager sensormanager;
    private final SensorManagerGetter smg;

    public interface SensorManagerGetter {
        SensorManager get();
    }

    abstract int colorFunction(SensorEvent sensorEvent);

    abstract int sensorType();

    public SensorColor(SensorManagerGetter smg) {
        this.smg = smg;
    }

    protected void register() {
        SensorManager newsensormanager = this.smg.get();
        Log.d("plasma", "New sensor manager same as old sensor manager?: " + (newsensormanager == this.sensormanager));
        this.sensormanager = newsensormanager;
        this.sensormanager.registerListener(this, this.sensormanager.getDefaultSensor(sensorType()), 2);
        Log.d("plasma", "Sensor registered.");
    }

    public int getColor() {
        if (this.sensormanager == null) {
            register();
        }
        this.changed = false;
        this.lastColorCall = System.currentTimeMillis();
        return this.color;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (this.sensormanager == null) {
            Log.d("plasma", "Received sensor event when sensor manager is null.");
        } else if (System.currentTimeMillis() - this.lastColorCall > this.deactivateThreshold) {
            pause();
        } else {
            this.color = colorFunction(event);
            this.changed = true;
        }
    }

    public void pause() {
        if (this.sensormanager != null) {
            this.sensormanager.unregisterListener(this);
            this.sensormanager = null;
            Log.d("plasma", "Sensor unregistered.");
        }
    }

    public void unpause() {
        if (this.sensormanager == null) {
            register();
        }
    }
}
