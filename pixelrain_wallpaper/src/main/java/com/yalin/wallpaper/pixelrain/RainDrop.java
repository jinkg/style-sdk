package com.yalin.wallpaper.pixelrain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class RainDrop {
    private int color = -16735512;
    private float delay;
    private Paint dropPaint = new Paint();
    private RectF dropRect = new RectF();
    private ArrayList<Droplet> droplets = new ArrayList<>();
    private BitmapDrawable glow;
    private Rect glowRect = new Rect();
    private Rect groundGlowRect = new Rect();
    private int prevSize;
    private boolean randomSize;
    private int size;
    private float[][] trail = ((float[][]) Array.newInstance(Float.TYPE, 10, 2));
    private boolean visible;
    private boolean wasTouched = false;
    private float f2x;
    private float f3y;
    private float yVelocity;

    public RainDrop(float x, float y, float yVelocity, int size, Bitmap glowImage) {
        this.f2x = x;
        this.f3y = y;
        this.yVelocity = yVelocity;
        this.size = size;
        this.dropPaint.setColor(this.color);
        this.dropPaint.setAntiAlias(true);
        this.glow = new BitmapDrawable(glowImage);
        this.glow.setAntiAlias(true);
        for (int i = 0; i < 10; i++) {
            this.trail[i][0] = this.f2x;
            this.trail[i][1] = this.f3y - ((float) ((i + 1) * size));
        }
        this.visible = false;
        this.delay = 3000.0f * ((float) Math.random());
        while (this.droplets.size() < 4) {
            this.droplets.add(new Droplet(size / 2, this.color));
        }
    }

    public float getX() {
        return this.f2x;
    }

    public float getXCenter() {
        return this.f2x + ((float) (this.size / 2));
    }

    public float getY() {
        return this.f3y;
    }

    public float getYCenter() {
        return this.f3y + ((float) (this.size / 2));
    }

    public float getYVelocity() {
        return this.yVelocity;
    }

    public int getSize() {
        return this.size;
    }

    public Boolean wasTouched() {
        Boolean touched = Boolean.valueOf(this.wasTouched);
        this.wasTouched = false;
        return touched;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setX(float x) {
        this.f2x = x;
    }

    public void setY(float y) {
        this.f3y = y;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void setTouched(Boolean touched) {
        this.wasTouched = touched.booleanValue();
    }

    public void setOffset(float offset, boolean depthEnabled) {
        if (offset < 0.0f && depthEnabled) {
            offset -= ((((float) this.size) - 10.0f) / 20.0f) * 5.0f;
        } else if (offset > 0.0f && depthEnabled) {
            offset += ((((float) this.size) - 10.0f) / 20.0f) * 5.0f;
        }
        this.f2x += offset;
        Iterator it = this.droplets.iterator();
        while (it.hasNext()) {
            Droplet d = (Droplet) it.next();
            if (d.isVisible()) {
                d.setXOffset(offset);
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
        this.dropPaint.setColor(color);
        this.glow.setColorFilter(color, Mode.MULTIPLY);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void createDroplets(float x, float y, int ground, int shatterEffect) {
        ((Droplet) this.droplets.get(0)).create(x, y, 1, -1, ground, shatterEffect, this.color, this.size / 2);
        ((Droplet) this.droplets.get(1)).create(((float) (this.size / 2)) + x, y, 1, 1, ground, shatterEffect, this.color, this.size / 2);
        ((Droplet) this.droplets.get(2)).create(x, y + ((float) (this.size / 2)), -1, -1, ground, shatterEffect, this.color, this.size / 2);
        ((Droplet) this.droplets.get(3)).create(((float) (this.size / 2)) + x, ((float) (this.size / 2)) + y, -1, 1, ground, shatterEffect, this.color, this.size / 2);
    }

    public ArrayList<Droplet> getDroplets() {
        return this.droplets;
    }

    public void reset(float x, float y, float yVelocity, int yDroplet, int ground, boolean createDroplets, int shatterEffect) {
        if (createDroplets) {
            createDroplets(this.f2x, (float) (yDroplet - this.size), ground, shatterEffect);
        }
        this.f2x = x;
        this.f3y = y;
        this.yVelocity = yVelocity;
        if (this.randomSize) {
            this.prevSize = this.size;
            this.size = (int) (10.0d + (20.0d * Math.random()));
        }
    }

    public void prefReset(float x, float y, float yVelocity, boolean randomSize) {
        this.f2x = x;
        this.f3y = y;
        this.yVelocity = yVelocity;
        for (int i = 0; i < 10; i++) {
            this.trail[i][0] = this.f2x;
            this.trail[i][1] = this.f3y - ((float) ((i + 1) * this.size));
        }
        this.visible = false;
        this.delay = 3000.0f * ((float) Math.random());
        this.randomSize = randomSize;
    }

    public void draw(Canvas c, int ground, int trailLength, boolean drawGlow, boolean drawGroundGlow, int glowAlpha) {
        this.dropRect.set(this.f2x, this.f3y, this.f2x + ((float) this.size), this.f3y + ((float) this.size));
        this.dropPaint.setAlpha(255);
        c.drawRoundRect(this.dropRect, (float) (this.size / 5), (float) (this.size / 5), this.dropPaint);
        int i = 1;
        while (i <= trailLength) {
            this.dropPaint.setAlpha((int) (255.0f - (((float) i) * ((float) (255 / (trailLength + 1))))));
            if (!this.randomSize || this.trail[i - 1][1] <= this.f3y) {
                this.dropRect.set(this.trail[i - 1][0], this.trail[i - 1][1], this.trail[i - 1][0] + ((float) this.size), this.trail[i - 1][1] + ((float) this.size));
                c.drawRoundRect(this.dropRect, (float) (this.size / 5), (float) (this.size / 5), this.dropPaint);
            } else {
                this.dropRect.set(this.trail[i - 1][0], this.trail[i - 1][1], this.trail[i - 1][0] + ((float) this.prevSize), this.trail[i - 1][1] + ((float) this.prevSize));
                c.drawRoundRect(this.dropRect, (float) (this.prevSize / 5), (float) (this.prevSize / 5), this.dropPaint);
            }
            i++;
        }
        if (drawGroundGlow) {
            this.glow.setAlpha((int) (((float) glowAlpha) * (this.f3y / ((float) ground))));
            this.groundGlowRect.set(((int) this.f2x) - this.size, ground - ((int) (0.75d * ((double) this.size))), ((int) this.f2x) + (this.size * 2), ((int) (0.75d * ((double) this.size))) + ground);
            this.glow.setBounds(this.groundGlowRect);
            this.glow.draw(c);
        }
        if (drawGlow) {
            this.glow.setAlpha(glowAlpha);
            this.glowRect.set(((int) this.f2x) - this.size, ((int) this.f3y) - this.size, ((int) this.f2x) + (this.size * 2), ((int) this.f3y) + (this.size * 2));
            this.glow.setBounds(this.glowRect);
            this.glow.draw(c);
        }
    }

    public void update() {
        if (this.delay <= 0.0f) {
            this.visible = true;
        }
        if (this.visible) {
            for (int i = 9; i > 0; i--) {
                this.trail[i][0] = this.trail[i - 1][0];
                this.trail[i][1] = this.trail[i - 1][1];
            }
            this.trail[0][0] = this.f2x;
            this.trail[0][1] = this.f3y;
            this.f3y += this.yVelocity;
            return;
        }
        this.delay -= 40.0f;
    }
}
