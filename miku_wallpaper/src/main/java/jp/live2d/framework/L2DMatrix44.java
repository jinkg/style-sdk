package jp.live2d.framework;

import android.util.Log;

public class L2DMatrix44 {
    int f423n = 0;
    protected float[] tr = new float[16];

    public L2DMatrix44() {
        identity();
    }

    public void identity() {
        Log.v("L2DMatrix44", "identity()1 tr[0]  " + this.tr[0] + "  +tr[5] " + this.tr[5]);
        for (int i = 0; i < 16; i++) {
            this.tr[i] = i % 5 == 0 ? 1.0f : 0.0f;
        }
    }

    public float[] getArray() {
        return this.tr;
    }

    public float GetScaleX() {
        return this.tr[0];
    }

    public float GetScaleY() {
        return this.tr[5];
    }

    public float transformX(float src) {
        return (this.tr[0] * src) + this.tr[12];
    }

    public float transformY(float src) {
        return (this.tr[5] * src) + this.tr[13];
    }

    public float invertTransformX(float src) {
        return (src - this.tr[12]) / this.tr[0];
    }

    public float invertTransformY(float src) {
        return (src - this.tr[13]) / this.tr[5];
    }

    protected static void mul(float[] a, float[] b, float[] dst) {
        int i;
        float[] c = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    int i2 = (j * 4) + i;
                    c[i2] = c[i2] + (a[(k * 4) + i] * b[(j * 4) + k]);
                }
            }
        }
        for (i = 0; i < 16; i++) {
            dst[i] = c[i];
        }
    }

    public void multTranslate(float shiftX, float shiftY) {
        mul(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, shiftX, shiftY, 0.0f, 1.0f}, this.tr, this.tr);
    }

    public void translate(float x, float y) {
        this.tr[12] = x;
        this.tr[13] = y;
    }

    public void translateX(float x) {
        this.tr[12] = x;
    }

    public void translateY(float y) {
        this.tr[13] = y;
    }

    public void multScale(float scaleX, float scaleY) {
        mul(new float[]{scaleX, 0.0f, 0.0f, 0.0f, 0.0f, scaleY, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}, this.tr, this.tr);
    }

    public void scale(float scaleX, float scaleY) {
        this.tr[0] = scaleX;
        this.tr[5] = scaleY;
    }
}
