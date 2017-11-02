package jp.live2d.framework;

public class L2DViewMatrix extends L2DMatrix44 {
    private float max = 1.0f;
    private float maxBottom;
    private float maxLeft;
    private float maxRight;
    private float maxTop;
    private float min = 1.0f;
    private float screenBottom;
    private float screenLeft;
    private float screenRight;
    private float screenTop;

    public float getMaxScale() {
        return this.max;
    }

    public float getMinScale() {
        return this.min;
    }

    public void setMaxScale(float v) {
        this.max = v;
    }

    public void setMinScale(float v) {
        this.min = v;
    }

    public boolean isMaxScale() {
        return GetScaleX() == this.max;
    }

    public boolean isMinScale() {
        return GetScaleX() == this.min;
    }

    public void adjustTranslate(float shiftX, float shiftY) {
        if ((this.tr[0] * this.maxLeft) + (this.tr[12] + shiftX) > this.screenLeft) {
            shiftX = (this.screenLeft - (this.tr[0] * this.maxLeft)) - this.tr[12];
        }
        if ((this.tr[0] * this.maxRight) + (this.tr[12] + shiftX) < this.screenRight) {
            shiftX = (this.screenRight - (this.tr[0] * this.maxRight)) - this.tr[12];
        }
        if ((this.tr[5] * this.maxTop) + (this.tr[13] + shiftY) < this.screenTop) {
            shiftY = (this.screenTop - (this.tr[5] * this.maxTop)) - this.tr[13];
        }
        if ((this.tr[5] * this.maxBottom) + (this.tr[13] + shiftY) > this.screenBottom) {
            shiftY = (this.screenBottom - (this.tr[5] * this.maxBottom)) - this.tr[13];
        }
        L2DMatrix44.mul(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, shiftX, shiftY, 0.0f, 1.0f}, this.tr, this.tr);
    }

    public void adjustScale(float cx, float cy, float scale) {
        float targetScale = scale * this.tr[0];
        if (targetScale < this.min) {
            if (this.tr[0] > 0.0f) {
                scale = this.min / this.tr[0];
            }
        } else if (targetScale > this.max && this.tr[0] > 0.0f) {
            scale = this.max / this.tr[0];
        }
        float[] tr1 = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, cx, cy, 0.0f, 1.0f};
        float[] tr2 = new float[]{scale, 0.0f, 0.0f, 0.0f, 0.0f, scale, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        L2DMatrix44.mul(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -cx, -cy, 0.0f, 1.0f}, this.tr, this.tr);
        L2DMatrix44.mul(tr2, this.tr, this.tr);
        L2DMatrix44.mul(tr1, this.tr, this.tr);
    }

    public void setScreenRect(float left, float right, float bottom, float top) {
        this.screenLeft = left;
        this.screenRight = right;
        this.screenTop = top;
        this.screenBottom = bottom;
    }

    public void setMaxScreenRect(float left, float right, float bottom, float top) {
        this.maxLeft = left;
        this.maxRight = right;
        this.maxTop = top;
        this.maxBottom = bottom;
    }
}
