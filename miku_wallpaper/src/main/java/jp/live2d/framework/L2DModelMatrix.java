package jp.live2d.framework;


import com.yalin.wallpaper.miku.LAppDefine;

public class L2DModelMatrix extends L2DMatrix44 {
    private float height;
    private float width;

    public L2DModelMatrix(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public void setPosition(float x, float y) {
        translate(x, y);
    }

    public void setCenterPosition(float x, float y) {
        translate(x - ((this.width * GetScaleX()) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2), y - ((this.height * GetScaleY()) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2));
    }

    public void top(float y) {
        setY(y);
    }

    public void bottom(float y) {
        translateY(y - (this.height * GetScaleY()));
    }

    public void left(float x) {
        setX(x);
    }

    public void right(float x) {
        translateX(x - (this.width * GetScaleX()));
    }

    public void centerX(float x) {
        translateX(x - ((this.width * GetScaleX()) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2));
    }

    public void centerY(float y) {
        translateY(y - ((this.height * GetScaleY()) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2));
    }

    public void setX(float x) {
        translateX(x);
    }

    public void setY(float y) {
        translateY(y);
    }

    public void setHeight(float h) {
        float scaleX = h / this.height;
        scale(scaleX, -scaleX);
    }

    public void setWidth(float w) {
        float scaleX = w / this.width;
        scale(scaleX, -scaleX);
    }
}
