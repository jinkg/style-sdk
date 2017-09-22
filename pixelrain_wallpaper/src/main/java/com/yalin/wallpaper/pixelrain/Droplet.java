package com.yalin.wallpaper.pixelrain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Droplet {
    private float angle;
    private Paint dropletPaint;
    private RectF dropletRect = new RectF();
    private int ground;
    private int shatterEffect;
    private int size;
    private float time;
    private boolean visible = false;
    private float f0x;
    private float xInit;
    private float xVelocity;
    private float f1y;
    private float yInit;
    private float yVelocity;

    public Droplet(int size, int color) {
        this.size = size;
        this.dropletPaint = new Paint();
        this.dropletPaint.setAntiAlias(true);
        this.dropletPaint.setColor(color);
    }

    public float getX() {
        return this.f0x;
    }

    public float getY() {
        return this.f1y;
    }

    public float getDX() {
        return this.xVelocity;
    }

    public float getDY() {
        return this.yVelocity;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setX(float x) {
        this.xInit = x;
    }

    public void setY(float y) {
        this.yInit = y;
    }

    public void setXOffset(float xOffset) {
        this.xInit += xOffset;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setColor(int color) {
        this.dropletPaint.setColor(color);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void create(float xInit, float yInit, int vPosition, int hPosition, int ground, int shatterEffect, int color, int size) {
        this.dropletPaint.setAlpha(255);
        this.xInit = xInit;
        this.f0x = xInit;
        this.yInit = yInit;
        this.f1y = yInit;
        this.ground = ground;
        this.shatterEffect = shatterEffect;
        setColor(color);
        setSize(size);
        if (shatterEffect == 2) {
            if (vPosition == 1 && hPosition == 1) {
                this.angle = 45.0f;
            } else if (vPosition == 1 && hPosition == -1) {
                this.angle = 135.0f;
            } else if (vPosition == -1 && hPosition == -1) {
                this.angle = 225.0f;
            } else if (vPosition == -1 && hPosition == 1) {
                this.angle = 315.0f;
            }
        } else if (shatterEffect != 4) {
            this.angle = (((float) Math.random()) * 60.0f) + 60.0f;
        } else if (vPosition == 1 && hPosition == 1) {
            this.angle = (float) ((int) (Math.random() * 90.0d));
        } else if (vPosition == 1 && hPosition == -1) {
            this.angle = (float) ((int) ((Math.random() * 90.0d) + 90.0d));
        } else if (vPosition == -1 && hPosition == -1) {
            this.angle = (float) ((int) (180.0d + (Math.random() * 90.0d)));
        } else if (vPosition == -1 && hPosition == 1) {
            this.angle = (float) ((int) (270.0d + (Math.random() * 90.0d)));
        }
        this.xVelocity = 30.0f * ((float) Math.cos(Math.toRadians((double) this.angle)));
        this.yVelocity = -30.0f * ((float) Math.sin(Math.toRadians((double) this.angle)));
        this.visible = true;
        this.time = 0.0f;
    }

    public void draw(Canvas c) {
        if (this.dropletPaint.getAlpha() > 0) {
            this.dropletRect.set(this.f0x, this.f1y, this.f0x + ((float) this.size), this.f1y + ((float) this.size));
            c.drawRoundRect(this.dropletRect, (float) (this.size / 5), (float) (this.size / 5), this.dropletPaint);
        }
    }

    public void classicShatter() {
        this.f0x = this.xInit + (this.xVelocity * this.time);
        this.f1y = (this.yInit + (this.yVelocity * this.time)) + ((4.9f * this.time) * this.time);
        this.time = (float) (((double) this.time) + 0.7d);
        if (this.dropletPaint.getAlpha() > 0) {
            this.dropletPaint.setAlpha(this.dropletPaint.getAlpha() - 5);
        }
    }

    public void splashShatter() {
        this.f0x = this.xInit + (this.xVelocity * this.time);
        this.f1y = (this.yInit + (this.yVelocity * this.time)) + ((this.time * 4.9f) * this.time);
        if (this.f1y >= ((float) (this.ground - this.size)) && (this.yVelocity * this.time) + ((this.time * 4.9f) * this.time) > 0.0f) {
            this.xInit = this.f0x;
            this.yInit = (float) (this.ground - this.size);
            this.xVelocity /= 2.0f;
            this.yVelocity /= 2.0f;
            this.time = 0.0f;
        } else if (this.yVelocity < -1.0f) {
            this.time = (float) (((double) this.time) + 0.7d);
        }
        if (this.dropletPaint.getAlpha() > 0) {
            this.dropletPaint.setAlpha(this.dropletPaint.getAlpha() - 5);
        }
    }

    public void impactShatter() {
        this.f0x = this.xInit + (this.xVelocity * this.time);
        this.f1y = this.yInit + (this.yVelocity * this.time);
        this.time = (float) (((double) this.time) + 0.4d);
        if (this.dropletPaint.getAlpha() > 0) {
            this.dropletPaint.setAlpha(this.dropletPaint.getAlpha() - 5);
        }
    }

    public void zeroGShatter() {
        this.f0x = this.xInit + ((this.xVelocity / 2.0f) * this.time);
        this.f1y = this.yInit + ((this.yVelocity / 2.0f) * this.time);
        this.time = (float) (((double) this.time) + 0.4d);
        if (this.dropletPaint.getAlpha() > 0) {
            this.dropletPaint.setAlpha(this.dropletPaint.getAlpha() - 5);
        }
    }

    public void burstShatter() {
        this.f0x = this.xInit + ((this.xVelocity / 2.0f) * this.time);
        this.f1y = this.yInit + ((this.yVelocity / 2.0f) * this.time);
        this.time = (float) (((double) this.time) + 0.4d);
        this.dropletPaint.setAlpha((int) (1.0d + (254.0d * Math.random())));
    }

    public void update() {
        switch (this.shatterEffect) {
            case 0:
                classicShatter();
                return;
            case 1:
                splashShatter();
                return;
            case 2:
                impactShatter();
                return;
            case 3:
                zeroGShatter();
                return;
            case 4:
                burstShatter();
                return;
            default:
                return;
        }
    }
}
