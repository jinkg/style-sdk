package jp.live2d.framework;

import jp.live2d.util.UtSystem;

public class L2DTargetPoint {
    public static final int FRAME_RATE = 30;
    private float faceTargetX = 0.0f;
    private float faceTargetY = 0.0f;
    private float faceVX = 0.0f;
    private float faceVY = 0.0f;
    private float faceX = 0.0f;
    private float faceY = 0.0f;
    private long lastTimeSec = 0;

    public void set(float x, float y) {
        this.faceTargetX = x;
        this.faceTargetY = y;
    }

    public float getX() {
        return this.faceX;
    }

    public float getY() {
        return this.faceY;
    }

    public void update() {
        if (this.lastTimeSec == 0) {
            this.lastTimeSec = UtSystem.getUserTimeMSec();
            return;
        }
        long curTimeSec = UtSystem.getUserTimeMSec();
        float deltaTimeWeight = (((float) (curTimeSec - this.lastTimeSec)) * 30.0f) / 1000.0f;
        this.lastTimeSec = curTimeSec;
        float MAX_A = (0.17777778f * deltaTimeWeight) / 4.5f;
        float dx = this.faceTargetX - this.faceX;
        float dy = this.faceTargetY - this.faceY;
        if (dx != 0.0f || dy != 0.0f) {
            float d = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            float ax = ((0.17777778f * dx) / d) - this.faceVX;
            float ay = ((0.17777778f * dy) / d) - this.faceVY;
            float a = (float) Math.sqrt((double) ((ax * ax) + (ay * ay)));
            if (a < (-MAX_A) || a > MAX_A) {
                ax *= MAX_A / a;
                ay *= MAX_A / a;
                a = MAX_A;
            }
            this.faceVX += ax;
            this.faceVY += ay;
            float max_v = 0.5f * (((float) Math.sqrt((double) (((MAX_A * MAX_A) + ((16.0f * MAX_A) * d)) - ((8.0f * MAX_A) * d)))) - MAX_A);
            float cur_v = (float) Math.sqrt((double) ((this.faceVX * this.faceVX) + (this.faceVY * this.faceVY)));
            if (cur_v > max_v) {
                this.faceVX *= max_v / cur_v;
                this.faceVY *= max_v / cur_v;
            }
            this.faceX += this.faceVX;
            this.faceY += this.faceVY;
        }
    }
}
