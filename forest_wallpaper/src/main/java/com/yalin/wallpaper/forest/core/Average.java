package com.yalin.wallpaper.forest.core;

public class Average {
    float[] arr;
    int f0i = 0;
    int size;

    public Average(int size) {
        this.size = size;
        this.arr = new float[size];
    }

    public float add(float val) {
        float[] fArr = this.arr;
        int i = this.f0i;
        this.f0i = i + 1;
        fArr[i] = val;
        if (this.f0i == this.size) {
            this.f0i = 0;
        }
        return avg();
    }

    public float avg() {
        float sum = 0.0f;
        for (float x : this.arr) {
            sum += x;
        }
        return sum / ((float) this.size);
    }

    public void reset() {
        for (int o = 0; o < this.size; o++) {
            this.arr[o] = 0.0f;
        }
    }
}
