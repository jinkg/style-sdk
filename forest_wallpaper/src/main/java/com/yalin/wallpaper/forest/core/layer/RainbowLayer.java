package com.yalin.wallpaper.forest.core.layer;

import android.opengl.GLES20;

import com.yalin.wallpaper.forest.core.Easing;
import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Textures;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.VertexBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RainbowLayer extends Layer {
    private static Random random = new Random();
    private VertexBatch glow;
    private PotOfGold pot = new PotOfGold();
    private VertexBatch triangles;

    class PotOfGold {
        ArrayList<Ray> rays;
        VertexBatch triangles;
        float f18x;
        float f19y;

        class Ray {
            ValueAnimator angle;
            ValueAnimator[] animators;
            float duration;
            ValueAnimator length;
            ValueAnimator strength;
            float time;
            ValueAnimator width;

            class ValueAnimator {
                float diff;
                float duration;
                float percent;
                float start;
                float value;

                ValueAnimator() {
                }

                ValueAnimator setValues(float start, float diff, float duration) {
                    this.start = start;
                    this.diff = diff;
                    this.duration = duration;
                    this.value = start;
                    this.percent = 0.0f;
                    return this;
                }

                void calcValue(float time) {
                    this.percent = time / this.duration;
                    this.value = this.start + (this.diff * this.percent);
                }
            }

            class PeakAnimator extends ValueAnimator {
                PeakAnimator() {
                    super();
                }

                void calcValue(float time) {
                    this.percent = (((float) Math.cos(((6.283185307179586d * ((double) time)) / ((double) this.duration)) - 3.141592653589793d)) * 0.5f) + 0.5f;
                    this.value = this.start + (this.diff * this.percent);
                }
            }

            Ray() {
                ValueAnimator[] valueAnimatorArr = new ValueAnimator[4];
                ValueAnimator valueAnimator = new ValueAnimator();
                this.angle = valueAnimator;
                valueAnimatorArr[0] = valueAnimator;
                valueAnimator = new ValueAnimator();
                this.length = valueAnimator;
                valueAnimatorArr[1] = valueAnimator;
                valueAnimator = new ValueAnimator();
                this.width = valueAnimator;
                valueAnimatorArr[2] = valueAnimator;
                valueAnimator = new PeakAnimator();
                this.strength = valueAnimator;
                valueAnimatorArr[3] = valueAnimator;
                this.animators = valueAnimatorArr;
                init();
            }

            void init() {
                float g = (float) RainbowLayer.random.nextGaussian();
                this.time = 0.0f;
                this.duration = (g * 0.5f) + 2.5f;
                float val = (g * 0.5f) + 1.25f;
                this.angle.setValues(val, ((((float) Math.random()) * (val - 1.25f)) * this.duration) * 0.2f, this.duration);
                this.length.setValues((((float) Math.random()) * 0.2f) + 0.2f, (((float) Math.random()) * 0.2f) * ((Math.abs(g) * -2.0f) + 1.0f), this.duration);
                val = (((float) Math.random()) * 0.03f) + 0.01f;
                this.width.setValues(val, ((float) Math.random()) * (-(val - 0.002f)), this.duration);
                this.strength.setValues(0.0f, (((float) Math.random()) * 0.2f) + 0.2f, this.duration);
            }

            void tick(double dt) {
                this.time = (float) (((double) this.time) + dt);
                if (this.time >= this.duration) {
                    init();
                    return;
                }
                for (ValueAnimator va : this.animators) {
                    va.calcValue(this.time);
                }
            }

            void draw() {
                PotOfGold.this.triangles.addVertex(PotOfGold.this.f18x, PotOfGold.this.f19y, 1.0f, 1.0f, 0.5f, this.strength.value);
                PotOfGold.this.triangles.addVertex(PotOfGold.this.f18x + (((float) Math.cos((double) (this.angle.value - this.width.value))) * this.length.value), PotOfGold.this.f19y + (((float) Math.sin((double) (this.angle.value - this.width.value))) * this.length.value), 1.0f, 1.0f, 0.5f, 0.0f);
                PotOfGold.this.triangles.addVertex(PotOfGold.this.f18x + (((float) Math.cos((double) (this.angle.value + this.width.value))) * this.length.value), PotOfGold.this.f19y + (((float) Math.sin((double) (this.angle.value + this.width.value))) * this.length.value), 1.0f, 1.0f, 0.5f, 0.0f);
            }
        }

        PotOfGold() {
        }

        void setup() {
            this.rays = new ArrayList<>(10);
            this.triangles = new VertexBatch(30);
            for (int i = 0; i < 10; i++) {
                Ray r = new Ray();
                this.rays.add(r);
                r.draw();
            }
            this.triangles.arraysToBuffers();
        }

        void tick(double dt) {
            this.triangles.reset();
            Iterator it = this.rays.iterator();
            while (it.hasNext()) {
                Ray r = (Ray) it.next();
                r.tick(dt);
                r.draw();
            }
            this.triangles.arraysToBuffers();
        }

        void draw(ForestRenderer renderer, float[] matrix) {
            GLES20.glBlendFunc(770, 1);
            this.triangles.drawTriangles(renderer, matrix);
            GLES20.glBlendFunc(770, 771);
        }
    }

    public RainbowLayer(World w) {
        this.world = w;
    }

    public void prepareForRendering() {
        int i;
        this.triangles = new VertexBatch(40);
        int ty = 0;
        for (i = 0; i < 20; i++) {
            float angle = ((((float) i) / ((float) 20)) * (2.984513f - 1.7278761f)) + 1.7278761f;
            this.triangles.addVertex((((float) Math.cos((double) angle)) * 0.55f) + 0.75f, (((float) Math.sin((double) angle)) * 0.55f) + 0.15f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, (float) ty);
            this.triangles.addVertex((((float) Math.cos((double) angle)) * 0.7f) + 0.75f, (((float) Math.sin((double) angle)) * 0.7f) + 0.15f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, (float) ty);
            ty = 1 - ty;
        }
        this.pot.f18x = ((((float) Math.cos(((double) 2.984513f) - 0.15d)) * (0.55f + 0.7f)) * 0.5f) + 0.75f;
        this.pot.f19y = ((((float) Math.sin(((double) 2.984513f) - 0.15d)) * (0.55f + 0.7f)) * 0.5f) + 0.15f;
        this.pot.setup();
        this.glow = new VertexBatch(4);
        this.glow.addVertex(this.pot.f18x + (((float) Math.cos(-0.65d)) * 0.5f), this.pot.f19y + (((float) Math.sin(-0.65d)) * 0.5f));
        this.glow.addVertex(this.pot.f18x + (((float) Math.cos(1.5707963267948966d - 6.8d)) * 0.5f), this.pot.f19y + (((float) Math.sin(1.5707963267948966d - 6.8d)) * 0.5f));
        this.glow.addVertex(this.pot.f18x + (((float) Math.cos(3.141592653589793d - 6.8d)) * 0.5f), this.pot.f19y + (((float) Math.sin(3.141592653589793d - 6.8d)) * 0.5f));
        this.glow.addVertex(this.pot.f18x + (((float) Math.cos(-0.65d)) * 0.5f), this.pot.f19y + (((float) Math.sin(-0.65d)) * 0.5f));
        for (i = 0; i < 4; i++) {
            this.glow.addColor(1.0f, 0.75f, 0.0f, 1.0f);
        }
        this.glow.addTexCoord(1.0f, 0.0f);
        this.glow.addTexCoord(1.0f, 1.0f);
        this.glow.addTexCoord(0.0f, 1.0f);
        this.glow.addTexCoord(1.0f, 0.0f);
        this.glow.arraysToBuffers();
        recolor();
    }

    public void recolor() {
        this.triangles.resetColors();
        double z = this.world.getSunData().getZenithAngle();
        for (int i = 0; i < 20; i++) {
            float alpha = 0.2f * Easing.quad.out((double) (((float) i) / ((float) 20)));
            this.triangles.addColor(1.0f, 1.0f, 1.0f, alpha);
            this.triangles.addColor(1.0f, 1.0f, 1.0f, alpha);
        }
        this.triangles.arraysToBuffers();
    }

    public void onDraw(ForestRenderer renderer) {
        float[] matrix = getOffsetMatrix(renderer);
        Textures.use(Textures.RAINBOW);
        this.triangles.drawTriangleStripTextured(renderer, matrix);
        this.pot.draw(renderer, matrix);
        Textures.use(Textures.CLOUD);
        GLES20.glBlendFunc(770, 1);
        this.glow.drawTriangleStripTextured(renderer, matrix);
        GLES20.glBlendFunc(770, 771);
    }

    public void tick(double dt) {
        this.pot.tick(dt);
    }
}
