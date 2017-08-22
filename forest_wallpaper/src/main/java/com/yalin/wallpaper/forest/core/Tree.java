package com.yalin.wallpaper.forest.core;


import com.yalin.wallpaper.forest.core.gl.VertexBatch;
import com.yalin.wallpaper.forest.core.layer.ForestLayer;

public class Tree {
    private float angle = 0.0f;
    private float angle_vel = 0.0f;
    private float aspect;
    private float botB;
    private float botG;
    private float botR;
    private float[] bottomcolor;
    private float[] color;
    private float colorvalue;
    private float elasticity;
    private ForestLayer forest;
    private int layer;
    private float size;
    private double time;
    private float topB;
    private float topG;
    private float topR;
    private World world;
    public float f1x;
    public float f2y;

    public Tree(World w, float x, float y, float size, int layer) {
        this.world = w;
        this.f1x = x;
        this.f2y = y;
        this.size = ((1.2f - (y * y)) * size) * 1.5f;
        this.aspect = (((float) Math.random()) * 0.5f) + 1.0f;
        this.layer = layer;
        this.colorvalue = (float) Math.random();
        prepColor();
        this.elasticity = 0.3f;
    }

    public void setPosition(float x, float y) {
        this.f1x = x;
        this.f2y = y;
    }

    public void setForest(ForestLayer f) {
        this.forest = f;
    }

    public void prepColor() {
        if (this.layer == 0) {
            this.color = this.world.colors.frontHillTree(this.colorvalue);
            this.bottomcolor = new float[]{this.color[0] * 0.7f, this.color[1] * 0.7f, this.color[2] * 0.7f};
        } else {
            this.color = this.world.colors.rearHillTree(this.colorvalue);
            this.bottomcolor = new float[]{this.color[0] * 0.85f, this.color[1] * 0.85f, this.color[2] * 0.85f};
        }
        this.topR = this.color[0];
        this.topG = this.color[1];
        this.topB = this.color[2];
        this.botR = this.bottomcolor[0];
        this.botG = this.bottomcolor[1];
        this.botB = this.bottomcolor[2];
    }

    public void update(float dt) {
        this.time += ((double) dt) * 0.5d;
        if (this.time >= 6.283185307179586d) {
            this.time -= 6.283185307179586d;
        }
        float wind = this.forest.windAt((double) this.f1x);
        this.angle_vel = (this.angle_vel + ((this.elasticity * wind) / (2.5f - this.aspect))) - ((this.angle * this.elasticity) * (2.5f - this.aspect));
        if (this.angle_vel > wind) {
            this.angle_vel *= 0.99f;
        }
        this.angle += this.angle_vel * dt;
    }

    public void fillBatch(VertexBatch batch) {
        float width = 0.05f * this.size;
        float height = (0.1f * this.size) * this.aspect;
        float xdiff = ((float) Math.cos(((double) this.angle) * 0.5d)) * width;
        float ydiff = ((float) Math.sin(((double) this.angle) * 0.5d)) * width;
        batch.addVertex(this.f1x - xdiff, this.f2y - ydiff, this.botR, this.botG, this.botB, 1.0f);
        batch.addVertex(this.f1x + xdiff, this.f2y + ydiff, this.botR, this.botG, this.botB, 1.0f);
        batch.addVertex(this.f1x - (((float) Math.sin((double) this.angle)) * height),
                (((float) Math.cos((double) this.angle)) * height) + this.f2y,
                this.topR, this.topG, this.topB, 1.0f);
    }
}
