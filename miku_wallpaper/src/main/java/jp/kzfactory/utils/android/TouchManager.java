package jp.kzfactory.utils.android;

public class TouchManager {
    static boolean SCALING_ENABLED = true;
    public static float lastX = 0.0f;
    public static float lastY = 0.0f;
    private boolean flipAvailable;
    private float lastTouchDistance = -1.0f;
    private float lastX1 = 0.0f;
    private float lastX2 = 0.0f;
    private float lastY1 = 0.0f;
    private float lastY2 = 0.0f;
    private float moveX;
    private float moveY;
    private float scale;
    private float startX;
    private float startY;
    private boolean touchSingle;

    public void touchBegan(float x1, float y1, float x2, float y2) {
        float dist = distance(x1, y1, x2, y2);
        float centerX = (this.lastX1 + this.lastX2) * 0.5f;
        float centerY = ((-this.lastY1) - this.lastY2) * 0.5f;
        lastX = centerX;
        lastY = centerY;
        this.startX = centerX;
        this.startY = centerY;
        this.lastTouchDistance = dist;
        this.flipAvailable = true;
        this.touchSingle = false;
    }

    public void touchBegan(float x, float y) {
        lastX = x;
        lastY = -y;
        this.startX = x;
        this.startY = -y;
        this.lastTouchDistance = -1.0f;
        this.flipAvailable = true;
        this.touchSingle = true;
    }

    public void touchesMoved(float x, float y) {
        lastX = x;
        lastY = -y;
        this.lastTouchDistance = -1.0f;
        this.touchSingle = true;
    }

    public void touchesMoved(float x1, float y1, float x2, float y2) {
        float dist = distance(x1, y1, x2, y2);
        float centerX = (x1 + x2) * 0.5f;
        float centerY = ((-y1) + (-y2)) * 0.5f;
        if (this.lastTouchDistance > 0.0f) {
            this.scale = (float) Math.pow((double) (dist / this.lastTouchDistance), 0.75d);
            this.moveX = calcShift(x1 - this.lastX1, x2 - this.lastX2);
            this.moveY = calcShift((-y1) - this.lastY1, (-y2) - this.lastY2);
        } else {
            this.scale = 1.0f;
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        lastX = centerX;
        lastY = centerY;
        this.lastX1 = x1;
        this.lastY1 = -y1;
        this.lastX2 = x2;
        this.lastY2 = -y2;
        this.lastTouchDistance = dist;
        this.touchSingle = false;
    }

    public float getCenterX() {
        return lastX;
    }

    public float getCenterY() {
        return lastY;
    }

    public float getDeltaX() {
        return this.moveX;
    }

    public float getDeltaY() {
        return this.moveY;
    }

    public float getStartX() {
        return this.startX;
    }

    public float getStartY() {
        return this.startY;
    }

    public float getScale() {
        return this.scale;
    }

    public float getX() {
        return lastX;
    }

    public float getY() {
        return lastY;
    }

    public float getX1() {
        return this.lastX1;
    }

    public float getY1() {
        return this.lastY1;
    }

    public float getX2() {
        return this.lastX2;
    }

    public float getY2() {
        return this.lastY2;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((double) (((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2))));
    }

    private float calcShift(float v1, float v2) {
        Object obj = 1;
        Object obj2 = v1 > 0.0f ? 1 : null;
        if (v2 <= 0.0f) {
            obj = null;
        }
        if (obj2 != obj) {
            return 0.0f;
        }
        float fugou = v1 > 0.0f ? 1.0f : -1.0f;
        float a1 = Math.abs(v1);
        float a2 = Math.abs(v2);
        if (a1 >= a2) {
            a1 = a2;
        }
        return fugou * a1;
    }

    public float getFlickDistance() {
        return distance(this.startX, this.startY, lastX, lastY);
    }

    public boolean isSingleTouch() {
        return this.touchSingle;
    }

    public boolean isFlickAvailable() {
        return this.flipAvailable;
    }

    public void disableFlick() {
        this.flipAvailable = false;
    }
}
