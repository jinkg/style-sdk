package com.yalin.wallpaper.forest.core.layer;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.PointBatch;

import java.util.ArrayList;

public class SnowLayer extends Layer {
    private float[] color;
    private int flakesInUse;
    private float layerWidth;
    private int numFlakes;
    private PointBatch points;
    private float snow;
    private ArrayList<SnowFlake> snowflakes = new ArrayList<>();
    private float targetSnow;
    private float targetWind;
    private double time;
    private float wind;

    class SnowFlake {
        public float f20a = ((float) ((Math.random() * 0.5d) + 0.5d));
        private float frequency;
        private float magnitude;
        private float offset;
        public float size;
        private float time;
        public float f21x;
        public float xv;
        public float f22y;
        public float yv;

        public SnowFlake() {
            reset();
            this.f21x = ((float) Math.random()) * SnowLayer.this.layerWidth;
            this.f22y = ((float) Math.random()) * SnowLayer.this.screenHeight;
            this.size = (((((float) Math.random()) * 2.0f) + 3.0f) * SnowLayer.this.scale) * SnowLayer.this.world.scale;
            this.size *= 0.5f;
            this.magnitude = ((float) ((Math.random() * 0.5d) + 0.5d)) * 0.001f;
            this.frequency = (((float) Math.random()) * 1.0f) + 1.0f;
            this.offset = (float) ((Math.random() * 3.141592653589793d) * 2.0d);
        }

        public void tick(double dt) {
            this.time = (float) (((double) this.time) + dt);
            this.xv = ((this.xv + (((float) Math.sin((double) ((this.frequency * this.time) + this.offset))) * this.magnitude)) - (SnowLayer.this.windAt((double) this.f21x) * 1.0E-4f)) * 0.99f;
            this.f21x = (float) (((double) this.f21x) + (((double) this.xv) * dt));
            this.f22y = (float) (((double) this.f22y) + (((double) this.yv) * dt));
            if (this.f22y < -0.1f) {
                reset();
            }
            if (this.f21x < 0.0f) {
                this.f21x += SnowLayer.this.layerWidth;
            }
        }

        private void reset() {
            this.f21x = ((float) Math.random()) * SnowLayer.this.layerWidth;
            this.f22y = SnowLayer.this.screenHeight * 1.1f;
            this.xv = ((((float) ((Math.random() * 0.20000000298023224d) - 0.10000000149011612d)) * ((SnowLayer.this.wind * 0.03333333f) * 0.5f)) - ((SnowLayer.this.windAt((double) this.f21x) * 0.06666667f) * 0.5f)) * SnowLayer.this.scale;
            this.yv = (((-((float) Math.random())) * 0.05f) - 0.1f) * SnowLayer.this.scale;
        }
    }

    public SnowLayer(World w) {
        this.world = w;
        this.wind = (float) Weather.INSTANCE.getWindSpeed();
        this.snow = (float) Weather.INSTANCE.getSnowfall();
    }

    public void prepareForRendering() {
        this.layerWidth = this.screenWidth;
        this.numFlakes = (int) (((250.0f * this.screenWidth) * this.screenHeight) / this.scale);
        for (int i = 0; i < this.numFlakes; i++) {
            this.snowflakes.add(new SnowFlake());
        }
        this.points = new PointBatch(this.numFlakes * 3);
        recolor();
    }

    public void recolor() {
        this.color = this.world.colors.snow();
    }

    public void onDraw(ForestRenderer renderer) {
        this.points.reset();
        fillArrays();
        this.points.arraysToBuffers();
        this.points.draw(renderer, getOffsetMatrix(renderer));
    }

    private void fillArrays() {
        for (int n = 0; n < this.flakesInUse; n++) {
            SnowFlake f = this.snowflakes.get(n);
            f.tick(0.019999999552965164d);
            this.points.add(f.f21x, f.f22y, this.color[0], this.color[1], this.color[2], 1.0f, f.size);
            this.points.add(f.f21x + this.screenWidth, f.f22y, this.color[0], this.color[1], this.color[2], 1.0f, f.size);
            this.points.add((f.f21x + this.screenWidth) + this.screenWidth, f.f22y, this.color[0], this.color[1], this.color[2], 1.0f, f.size);
        }
    }

    public void tick(double dt) {
        this.time += dt;
        this.targetWind = (float) Weather.INSTANCE.getWindSpeed();
        if (this.targetWind > this.wind) {
            this.wind = (float) (((double) this.wind) + (3.0d * dt));
        } else {
            this.wind = (float) (((double) this.wind) - (3.0d * dt));
        }
        this.targetSnow = (float) Weather.INSTANCE.getSnowfall();
        if (this.targetSnow > this.snow) {
            this.snow = (float) (((double) this.snow) + (dt * 0.1d));
        } else {
            this.snow = (float) (((double) this.snow) - (dt * 0.1d));
        }
        this.flakesInUse = Math.min((int) (this.snow * ((float) this.numFlakes)), this.numFlakes);
        if (this.flakesInUse < 3) {
            this.flakesInUse = 0;
        }
    }

    public float windAt(double x) {
        return ((float) Math.max(0.0d, (Math.sin((((this.time * 0.4d) + x) * 5.0d) * ((double) this.scale)) * 0.10000000149011612d) + 0.8999999761581421d)) * this.wind;
    }
}
