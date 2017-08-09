package com.yalin.wallpaper.bezierclock;

public class BezierAnimator {
    private static final float PI = (float)Math.PI;
    private float animationStartRatio;
    public final float[] points = new float[BezierDigit.SIZE];

    public BezierAnimator(float timeInterval, float animDuration) {
        animationStartRatio = (timeInterval - animDuration) / timeInterval;
    }

    public void update(int currentDigit, int nextDigit, float ratio) {
        float animationRatio = 0.0f;
        if (ratio > animationStartRatio) {
            animationRatio = (ratio - animationStartRatio) / (1 - animationStartRatio);
        }
        animationRatio = (1 - (float)Math.cos(animationRatio * PI)) / 2;
        for (int i = 0; i < BezierDigit.SIZE; ++i) {
            points[i] = BezierDigit.DIGITS[currentDigit].vertices[i] +
                    (BezierDigit.DIGITS[nextDigit].vertices[i] - BezierDigit.DIGITS[currentDigit].vertices[i]) * animationRatio;
        }
    }

    public void update(int currentDigit) {
        System.arraycopy(BezierDigit.DIGITS[currentDigit].vertices, 0, points, 0, BezierDigit.SIZE);
    }
}
