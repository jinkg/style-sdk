package com.yalin.wallpaper.forest.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RotationManager implements SensorEventListener {
    private final float DEGREES_TO_RADIANS = 0.017453292f;
    private final float LIMIT = 0.31415927f;
    private Filter[] filters;
    public boolean isRegistered = false;
    public float lastPitch = 0.0f;
    public float lastRoll = 0.0f;
    public float lastYaw = 0.0f;
    private SensorManager sensorManager;
    private int sensorRate = 0;
    long time;

    private class Filter {
        static final int AVERAGE_BUFFER = 3;
        float[] arr;
        int idx;

        private Filter() {
            this.arr = new float[3];
            this.idx = 0;
        }

        public void append(float val) {
            this.arr[this.idx] = val;
            this.idx++;
            if (this.idx == 3) {
                this.idx = 0;
            }
        }

        public float avg() {
            float sum = 0.0f;
            for (float x : this.arr) {
                sum += x;
            }
            return sum / 3.0f;
        }
    }

    public RotationManager(Context ctx) {
        this.sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        updateSensorRate();
    }

    public void registerListeners() {
        this.filters = new Filter[]{new Filter(), new Filter(), new Filter()};
        this.sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), sensorRate);
        this.isRegistered = true;
        this.time = System.nanoTime();
    }

    public void unregisterListeners() {
        this.sensorManager.unregisterListener(this);
        this.isRegistered = false;
    }

    public void updateSensorRate() {
        String s = "0";
        if (s == null || !s.equals("1")) {
            this.sensorRate = 0;
        } else {
            this.sensorRate = 1;
        }
        if (this.isRegistered) {
            unregisterListeners();
            registerListeners();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 4) {
            this.filters[0].append(event.values[0]);
            this.filters[1].append(event.values[1]);
            this.filters[2].append(event.values[2]);
            long now = System.nanoTime();
            float dt = ((((float) (now - this.time)) * 1.0E-8f) * 6.0f) * 0.017453292f;
            this.lastYaw = Math.max(Math.min(this.lastYaw + (this.filters[0].avg() * dt), 0.31415927f), -0.31415927f);
            this.lastPitch = Math.max(Math.min(this.lastPitch + (this.filters[1].avg() * dt), 0.31415927f), -0.31415927f);
            this.lastRoll = Math.max(Math.min(this.lastRoll + (this.filters[2].avg() * dt), 0.31415927f), -0.31415927f);
            this.time = now;
        }
    }
}
