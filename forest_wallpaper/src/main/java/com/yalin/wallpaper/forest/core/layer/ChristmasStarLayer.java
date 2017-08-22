package com.yalin.wallpaper.forest.core.layer;

import android.opengl.GLES20;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.LineBatch;

import java.util.ArrayList;

public class ChristmasStarLayer extends Layer {
    private Boolean drawStar = Boolean.TRUE;
    private LineBatch lines = new LineBatch(14);
    private Star star;

    class Star {
        public float f5a;
        ArrayList<Ray> rays = new ArrayList<>();
        public float f6x;
        public float f7y;

        class Ray {
            float f3a;
            float av;
            float f4r;
            float rmax;
            float rmul;
            float rtime;

            Ray() {
            }
        }

        public Star(float x, float y) {
            this.f6x = x;
            this.f7y = y;
            for (int i = 0; i < 10; i++) {
                Ray r = new Ray();
                r.f3a = ((float) (Math.random() * 3.141592653589793d)) * 2.0f;
                r.av = ((float) (Math.random() - 0.5d)) * 0.5f;
                r.rmax = (((float) Math.random()) * 0.015f) + 0.015f;
                r.rtime = (float) Math.random();
                r.rmul = ((float) ((Math.random() * 0.5d) + 0.5d)) * 0.5f;
                this.rays.add(r);
            }
        }

        public void tick(double dt) {
            for (int i = 0; i < 10; i++) {
                Ray r = (Ray) this.rays.get(i);
                r.f3a = (float) (((double) r.f3a) + (((double) r.av) * dt));
                r.rtime = (float) (((double) r.rtime) + dt);
                r.f4r = (float) (Math.cos((double) (r.rtime * r.rmul)) * ((double) r.rmax));
            }
        }

        public void draw() {
            for (int i = 0; i < 10; i++) {
                Ray r = (Ray) this.rays.get(i);
                ChristmasStarLayer.this.lines.add(this.f6x, this.f7y, this.f6x + (((float) Math.cos((double) r.f3a)) * r.f4r), this.f7y + (((float) Math.sin((double) r.f3a)) * r.f4r), 1.0f, 1.0f, 1.0f, 0.75f * this.f5a, 0.0f, 0.5f, 1.0f, 0.0f);
            }
            float l = (((float) Math.random()) * 0.01f) + 0.035f;
            ChristmasStarLayer.this.lines.add(this.f6x, this.f7y, this.f6x - l, this.f7y, 1.0f, 1.0f, 1.0f, this.f5a, 0.0f, 0.5f, 1.0f, 0.0f);
            ChristmasStarLayer.this.lines.add(this.f6x, this.f7y, this.f6x + l, this.f7y, 1.0f, 1.0f, 1.0f, this.f5a, 0.0f, 0.5f, 1.0f, 0.0f);
            ChristmasStarLayer.this.lines.add(this.f6x, this.f7y, this.f6x, this.f7y - l, 1.0f, 1.0f, 1.0f, this.f5a, 0.0f, 0.5f, 1.0f, 0.0f);
            ChristmasStarLayer.this.lines.add(this.f6x, this.f7y, this.f6x, this.f7y + l, 1.0f, 1.0f, 1.0f, this.f5a, 0.0f, 0.5f, 1.0f, 0.0f);
        }
    }

    public ChristmasStarLayer(World w) {
        this.world = w;
    }

    public void prepareForRendering() {
        this.star = new Star(getLayerWidth() * 0.4f, this.screenHeight * 0.85f);
    }

    public void recolor() {
        Boolean valueOf = true;
        this.drawStar = valueOf;
        if (valueOf) {
            double z = this.world.getSunData().getZenithAngle();
            float amul = 0.0f;
            if (z > 98.0d) {
                if (z < 104.0d) {
                    amul = ((float) (z - 98.0d)) / 6.0f;
                } else {
                    amul = 1.0f;
                }
            }
            this.star.f5a = (0.5f + ((float) (((double) amul) * Math.pow(1.0d - Weather.INSTANCE.getCloudiness(), 2.0d)))) / 1.5f;
        }
    }

    public void onDraw(ForestRenderer renderer) {
        if (this.drawStar) {
            GLES20.glBlendFunc(770, 1);
            GLES20.glLineWidth(this.world.scale);
            this.lines.reset();
            this.star.draw();
            this.lines.arraysToBuffers();
            this.lines.draw(renderer, getOffsetMatrix(renderer));
            GLES20.glBlendFunc(770, 771);
        }
    }

    public void tick(double dt) {
        this.star.tick(dt);
    }
}
