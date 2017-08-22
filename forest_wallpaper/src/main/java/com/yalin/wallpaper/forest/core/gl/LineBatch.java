package com.yalin.wallpaper.forest.core.gl;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Utils;

import java.nio.FloatBuffer;

public class LineBatch {
    private int ci;
    private FloatBuffer colorBuffer;
    private float[] colors;
    private int count;
    private int limit;
    private FloatBuffer vertexBuffer;
    private float[] vertices;
    private int vi;

    public LineBatch(int numLines) {
        this.limit = numLines;
        this.vertices = new float[((numLines * 2) * 2)];
        this.colors = new float[((numLines * 4) * 2)];
        colorBuffer = Utils.arrayToBuffer(this.colors);
        vertexBuffer = Utils.arrayToBuffer(this.vertices);
    }

    public void add(float x1, float y1, float x2, float y2, float r1,
                    float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        if (this.count != this.limit) {
            float[] fArr = this.vertices;
            int i = this.vi;
            this.vi = i + 1;
            fArr[i] = x1;
            fArr = this.vertices;
            i = this.vi;
            this.vi = i + 1;
            fArr[i] = y1;
            fArr = this.vertices;
            i = this.vi;
            this.vi = i + 1;
            fArr[i] = x2;
            fArr = this.vertices;
            i = this.vi;
            this.vi = i + 1;
            fArr[i] = y2;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = r1;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = g1;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = b1;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = a1;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = r2;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = g2;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = b2;
            fArr = this.colors;
            i = this.ci;
            this.ci = i + 1;
            fArr[i] = a2;
            this.count++;
        }
    }

    public void arraysToBuffers() {
        this.vertexBuffer.put(this.vertices);
        this.vertexBuffer.position(0);
        this.colorBuffer.put(this.colors);
        this.colorBuffer.position(0);
    }

    public void draw(ForestRenderer renderer, float[] matrix) {
        renderer.drawLines(this.vertexBuffer, this.colorBuffer, this.count * 2, matrix);
    }

    public void draw(ForestRenderer renderer, float[] matrix, int numVertices) {
        renderer.drawLines(this.vertexBuffer, this.colorBuffer, numVertices, matrix);
    }

    public void reset() {
        this.count = 0;
        this.ci = 0;
        this.vi = 0;
    }
}
