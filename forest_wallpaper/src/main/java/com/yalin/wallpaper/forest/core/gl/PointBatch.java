package com.yalin.wallpaper.forest.core.gl;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Utils;

import java.nio.FloatBuffer;

public class PointBatch {
    private int ci;
    private FloatBuffer colorBuffer;
    private float[] colors;
    private int count;
    private int limit;
    private int si;
    private FloatBuffer sizeBuffer;
    private float[] sizes;
    private FloatBuffer vertexBuffer;
    private float[] vertices;
    private int vi;

    public PointBatch(int numPoints) {
        this.limit = numPoints;
        this.vertices = new float[(numPoints * 2)];
        this.colors = new float[(numPoints * 4)];
        this.sizes = new float[numPoints];
        colorBuffer = Utils.arrayToBuffer(this.colors);
        sizeBuffer = Utils.arrayToBuffer(this.sizes);
        vertexBuffer = Utils.arrayToBuffer(this.vertices);
    }

    public void add(float x, float y, float r, float g, float b, float a, float s) {
        if (this.count != this.limit) {
            float[] fArr = this.vertices;
            int i = this.vi;
            this.vi = i + 1;
            fArr[i] = x;
            fArr = this.vertices;
            i = this.vi;
            this.vi = i + 1;
            fArr[i] = y;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = r;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = g;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = b;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = a;
            fArr = this.sizes;
            i = this.si;
            this.si = i + 1;
            fArr[i] = s;
            this.count++;
        }
    }

    public void arraysToBuffers() {
        this.vertexBuffer.put(this.vertices);
        this.vertexBuffer.position(0);
        this.colorBuffer.put(this.colors);
        this.colorBuffer.position(0);
        this.sizeBuffer.put(this.sizes);
        this.sizeBuffer.position(0);
    }

    public void draw(ForestRenderer renderer, float[] matrix) {
        renderer.drawPoints(this.vertexBuffer, this.colorBuffer, this.sizeBuffer, this.count, matrix);
    }

    public void reset() {
        this.count = 0;
        this.si = 0;
        this.ci = 0;
        this.vi = 0;
    }
}
