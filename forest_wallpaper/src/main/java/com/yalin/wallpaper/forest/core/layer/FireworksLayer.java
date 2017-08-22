package com.yalin.wallpaper.forest.core.layer;

import android.opengl.GLES20;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.LineBatch;
import com.yalin.wallpaper.forest.core.gl.PointBatch;

import java.util.ArrayList;
import java.util.Stack;

public class FireworksLayer extends Layer {
    private ArrayList<Firework> deadfireworks = new ArrayList();
    private ArrayList<Firework> fireworks = new ArrayList();
    private LineBatch lines;
    private int numSparks;
    private PointBatch points;
    private SparkPool sparkPool;
    private float targetWind;
    private double timer;
    private float wind;

    abstract class Firework {
        public boolean dead = false;

        public abstract void draw();

        public abstract void tick(double d);

        Firework() {
        }
    }

    class SparkPool {
        Stack<Spark> sparks = new Stack();

        public SparkPool(int count) {
            for (int i = 0; i < count; i++) {
                this.sparks.push(new Spark());
            }
        }

        public void createSpark(float x, float y, float xv, float yv, float hue, boolean sparkling) {
            if (!this.sparks.empty()) {
                Spark s = (Spark) this.sparks.pop();
                s.init(x, y, xv, yv, hue, sparkling);
                FireworksLayer.this.fireworks.add(s);
            }
        }

        public void returnSpark(Spark s) {
            this.sparks.push(s);
            FireworksLayer.this.fireworks.remove(s);
        }
    }

    class Rocket extends Firework {
        public float f26a;
        public double freefall;
        public float hue;
        public float[] rgb;
        public int state = 0;
        public double timer;
        public float f27v;
        public double wait;
        public float f28x;
        public float xv;
        public float f29y;
        public float yv;

        public Rocket(float x, float y, float angle, double launchTime, double freefallTime, double delay, float hue) {
            super();
            this.f28x = x;
            this.f29y = y;
            this.f26a = angle;
            this.f27v = 0.25f;
            this.xv = ((float) Math.sin((double) this.f26a)) * this.f27v;
            this.yv = ((float) Math.cos((double) this.f26a)) * this.f27v;
            this.timer = launchTime;
            this.freefall = freefallTime;
            this.wait = delay;
            this.rgb = FireworksLayer.this.world.colors.toRGB(FireworksLayer.this.world.colors.fromHSV(new float[]{hue, 1.0f, 1.0f}));
            this.hue = hue;
        }

        public void tick(double dt) {
            double d;
            switch (this.state) {
                case 0:
                    d = this.wait - dt;
                    this.wait = d;
                    if (d < 0.0d) {
                        this.state = 1;
                        return;
                    }
                    return;
                case 1:
                    this.f28x = (float) (((double) this.f28x) + (((double) this.xv) * dt));
                    this.f29y = (float) (((double) this.f29y) + (((double) this.yv) * dt));
                    float angle = (float) (((((double) this.f26a) + 3.141592653589793d) + (Math.random() * 0.3d)) - 0.15d);
                    FireworksLayer.this.sparkPool.createSpark(this.f28x, this.f29y, ((float) Math.sin((double) angle)) * this.f27v, ((float) Math.cos((double) angle)) * this.f27v, (((float) Math.random()) * 30.0f) + 30.0f, false);
                    d = this.timer - dt;
                    this.timer = d;
                    if (d < 0.0d) {
                        this.timer = this.freefall;
                        this.state = 2;
                        return;
                    }
                    return;
                case 2:
                    this.yv = (float) (((double) this.yv) - 0.005d);
                    this.f28x = (float) (((double) this.f28x) + (((double) this.xv) * dt));
                    this.f29y = (float) (((double) this.f29y) + (((double) this.yv) * dt));
                    d = this.timer - dt;
                    this.timer = d;
                    if (d < 0.0d) {
                        explode();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void draw() {
            if (this.state == 1) {
                FireworksLayer.this.lines.add(this.f28x, this.f29y, this.f28x - (this.xv * 0.2f), this.f29y - (this.yv * 0.2f), this.rgb[0], this.rgb[1], this.rgb[2], 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
                FireworksLayer.this.points.add(this.f28x, this.f29y, 1.0f, 0.8f, 0.6f, 1.0f, 2.5f * FireworksLayer.this.world.scale);
            }
            if (this.state == 2) {
                float i = (float) (this.timer / this.freefall);
                FireworksLayer.this.points.add(this.f28x, this.f29y, 1.0f, i, i * 0.5f, i, 2.5f * FireworksLayer.this.world.scale);
            }
        }

        private void explode() {
            this.dead = true;
            float strength = (float) ((Math.random() * 0.5d) + 0.4d);
            for (int i = 0; i < 150; i++) {
                float angle = (float) ((Math.random() * 3.141592653589793d) * 2.0d);
                float vel = (float) (Math.random() * ((double) strength));
                FireworksLayer.this.sparkPool.createSpark(this.f28x, this.f29y, this.xv + (((float) Math.cos((double) angle)) * vel), this.yv + (((float) Math.sin((double) angle)) * vel), this.hue, Math.random() > 0.6d);
            }
        }
    }

    class Spark extends Firework {
        public float f30a;
        public double duration;
        public float friction;
        public float gravity;
        public float hue;
        public float[] rgb;
        public boolean sparkling;
        public double timer;
        public float f31x;
        public float xv;
        public float f32y;
        public float yv;

        public Spark() {
            super();
        }

        public void init(float x, float y, float xv, float yv, float hue, boolean sparkling) {
            this.f31x = x;
            this.f32y = y;
            this.xv = xv;
            this.yv = yv;
            this.hue = hue;
            this.rgb = FireworksLayer.this.world.colors.toRGB(FireworksLayer.this.world.colors.fromHSV(new float[]{hue, 1.0f, 1.0f}));
            this.f30a = 1.0f;
            double random = 0.5d + Math.random();
            this.timer = random;
            this.duration = random;
            this.gravity = (((float) Math.random()) * 0.0075f) + 0.0025f;
            this.sparkling = sparkling;
            if (sparkling) {
                this.duration += 0.5d + Math.random();
                this.timer = this.duration;
            }
            if (sparkling) {
                this.rgb = FireworksLayer.this.world.colors.toRGB(FireworksLayer.this.world.colors.fromHSV(new float[]{hue, 0.5f, 1.0f}));
            }
            this.friction = sparkling ? 0.9f : 0.94f;
            this.dead = false;
        }

        public void tick(double dt) {
            this.xv *= this.friction;
            this.yv = (this.yv * this.friction) - this.gravity;
            this.f31x = (float) (((double) this.f31x) + (((double) this.xv) * dt));
            this.f32y = (float) (((double) this.f32y) + (((double) this.yv) * dt));
            this.f30a = (float) (this.timer / this.duration);
            double d = this.timer - dt;
            this.timer = d;
            if (d < 0.0d) {
                this.dead = true;
                this.f30a = 0.0f;
            }
        }

        public void draw() {
            if (this.sparkling) {
                FireworksLayer.this.points.add(this.f31x, this.f32y, this.rgb[0], this.rgb[1], this.rgb[2], this.f30a, ((((float) Math.random()) * 2.5f) * FireworksLayer.this.world.scale) + 0.5f);
            } else {
                FireworksLayer.this.lines.add(this.f31x, this.f32y, this.f31x - (this.xv * 0.2f), this.f32y - (this.yv * 0.2f), this.rgb[0], this.rgb[1], this.rgb[2], this.f30a, 1.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    public FireworksLayer(World w) {
        this.world = w;
        this.wind = (float) Weather.INSTANCE.getWindSpeed();
    }

    public void prepareForRendering() {
        this.numSparks = 1000;
        this.points = new PointBatch(this.numSparks / 2);
        this.lines = new LineBatch(this.numSparks / 2);
        this.sparkPool = new SparkPool(this.numSparks);
    }

    public void recolor() {
    }

    public void onDraw(ForestRenderer renderer) {
        GLES20.glBlendFunc(770, 1);
        GLES20.glLineWidth(this.world.scale);
        this.points.reset();
        this.lines.reset();
        int max = this.fireworks.size();
        for (int n = 0; n < max; n++) {
            ((Firework) this.fireworks.get(n)).draw();
        }
        this.points.arraysToBuffers();
        this.points.draw(renderer, getOffsetMatrix(renderer));
        this.lines.arraysToBuffers();
        this.lines.draw(renderer, getOffsetMatrix(renderer));
        GLES20.glBlendFunc(770, 771);
    }

    public void tick(double dt) {
        int n;
        this.wind = (float) Weather.INSTANCE.getWindSpeed();
        double d = this.timer - dt;
        this.timer = d;
        if (d < 0.0d) {
            this.timer = (Math.random() * 3.0d) + 1.0d;
            int hue = (((int) (Math.random() * 300.0d)) + 270) % 360;
            this.fireworks.add(new Rocket(0.5f * getLayerWidth(), this.world.skyBottom, (float) ((Math.random() * 0.6d) - 0.3d), Math.random() + 1.5d, Math.random(), 0.0d, (float) hue));
            hue = (((int) (Math.random() * 300.0d)) + 270) % 360;
            this.fireworks.add(new Rocket(0.5f * getLayerWidth(), this.world.skyBottom, (float) ((Math.random() * 0.8d) - 0.4d), Math.random() + 1.5d, Math.random(), 0.2d, (float) hue));
        }
        int max = this.fireworks.size();
        for (n = 0; n < max; n++) {
            Firework r = (Firework) this.fireworks.get(n);
            r.tick(dt);
            if (r.dead) {
                this.deadfireworks.add(r);
            }
        }
        max = this.deadfireworks.size();
        for (n = 0; n < max; n++) {
            Firework r = this.deadfireworks.get(n);
            if (r instanceof Spark) {
                this.sparkPool.returnSpark((Spark) r);
            } else {
                this.fireworks.remove(r);
            }
        }
        this.deadfireworks.clear();
    }
}
