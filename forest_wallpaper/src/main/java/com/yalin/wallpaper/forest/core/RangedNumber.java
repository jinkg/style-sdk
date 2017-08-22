package com.yalin.wallpaper.forest.core;

public class RangedNumber {
    private float clamped;
    private float in;
    private float lower = 0.0f;
    private float upper = 1.0f;

    public RangedNumber(double in) {
        this.in = (float) in;
    }

    public RangedNumber between(double lower, double upper) {
        this.lower = (float) lower;
        this.upper = (float) upper;
        float f = this.in < this.lower ? this.lower : this.in > this.upper ? this.upper : this.in;
        this.clamped = f;
        return this;
    }

    public RangedNumber flip() {
        this.clamped = (this.upper + this.lower) - this.clamped;
        return this;
    }

    public float get() {
        return this.clamped;
    }

    public float getPercent() {
        return (this.clamped - this.lower) / (this.upper - this.lower);
    }
}
