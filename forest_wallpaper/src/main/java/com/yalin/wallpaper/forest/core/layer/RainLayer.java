package com.yalin.wallpaper.forest.core.layer;

import android.opengl.GLES20;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.LineBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class RainLayer extends Layer {
    private float dropWidth;
    private LineBatch lines;
    private int numDrops;
    private float rain;
    private int rainInUse;
    private ArrayList<RainDrop> raindrops = new ArrayList<>();
    private float targetRain;
    private float targetWind;
    private double time;
    private float wind;

    class RainDrop {
        public float f15a;
        public float f16x;
        public float xv;
        public float f17y;
        public float yv;

        public RainDrop() {
            this.f15a = ((float) ((Math.random() * 0.2d) + 0.35d)) * RainLayer.this.scale;
        }

        public void tick(double dt) {
            this.f16x = (float) (((double) this.f16x) + (((double) this.xv) * dt));
            this.f17y = (float) (((double) this.f17y) + (((double) this.yv) * dt));
            if (this.f17y < -0.1f) {
                this.f16x = ((float) Math.random()) * (RainLayer.this.screenWidth + RainLayer.this.dropWidth);
                this.f17y = RainLayer.this.screenHeight * 1.1f;
                this.xv = ((((float) ((Math.random() * 0.20000000298023224d) - 0.10000000149011612d)) * (RainLayer.this.wind * 0.03333333f)) - (RainLayer.this.windAt((double) this.f16x) * 0.06666667f)) * RainLayer.this.scale;
            }
            if (this.f16x < (-RainLayer.this.dropWidth)) {
                this.f16x += RainLayer.this.screenWidth + RainLayer.this.dropWidth;
            }
        }
    }

    public RainLayer(World w) {
        this.world = w;
        this.wind = (float) Weather.INSTANCE.getWindSpeed();
        this.rain = (float) Weather.INSTANCE.getRainDownpour();
    }

    public void prepareForRendering() {
        this.numDrops = (int) (((250.0f * this.screenWidth) * this.screenHeight) / this.scale);
        for (int i = 0; i < this.numDrops; i++) {
            this.raindrops.add(new RainDrop());
        }
        for (RainDrop d : this.raindrops) {
            d.f16x = ((float) Math.random()) * this.screenWidth;
            d.f17y = ((float) Math.random()) * this.screenHeight;
            d.xv = (-((float) (Math.random() * 0.20000000298023224d))) * (this.wind / 30.0f);
            d.yv = (((-((float) Math.random())) * 0.25f) - 1.25f) * this.scale;
        }
        this.lines = new LineBatch(this.numDrops);
    }

    public void recolor() {
    }

    public void onDraw(ForestRenderer renderer) {
        this.lines.reset();
        fillArrays();
        this.lines.arraysToBuffers();
        GLES20.glLineWidth((((float) renderer.pixelHeight) * 0.00125f) / this.zDistance);
        this.lines.draw(renderer, renderer.identityMatrix, this.rainInUse * 2);
    }

    private void fillArrays() {
        float[] col = this.world.colors.rain();
        int max = this.raindrops.size();
        for (int n = 0; n < max; n++) {
            RainDrop d = this.raindrops.get(n);
            d.tick(0.019999999552965164d);
            this.lines.add(d.f16x - (d.xv * 0.05f), d.f17y - (d.yv * 0.05f), d.f16x, d.f17y, col[0], col[1], col[2], d.f15a * 0.75f, col[0], col[1], col[2], d.f15a);
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
        this.dropWidth = this.wind * 0.0033f;
        this.targetRain = (float) Weather.INSTANCE.getRainDownpour();
        if (this.targetRain > this.rain) {
            this.rain = (float) (((double) this.rain) + (dt * 0.1d));
        } else {
            this.rain = (float) (((double) this.rain) - (dt * 0.1d));
        }
        this.rainInUse = Math.min((int) (this.rain * ((float) this.numDrops)), this.numDrops);
        if (this.rainInUse < 3) {
            this.rainInUse = 0;
        }
    }

    public float windAt(double x) {
        return ((float) Math.max(0.0d, (Math.sin((((this.time * 0.4d) + x) * 5.0d) * ((double) this.scale)) * 0.20000000298023224d) + 0.800000011920929d)) * this.wind;
    }
}
