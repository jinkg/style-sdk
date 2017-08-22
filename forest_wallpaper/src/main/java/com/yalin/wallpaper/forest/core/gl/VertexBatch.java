package com.yalin.wallpaper.forest.core.gl;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Utils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class VertexBatch {
    private int ci;
    private FloatBuffer colorBuffer;
    public float[] colors;
    private ShortBuffer indexBuffer;
    public short[] indices;
    private int limit;
    private FloatBuffer texCoordBuffer;
    public float[] texCoords;
    private int ti;
    private FloatBuffer vertexBuffer;
    public float[] vertices;
    private int vi;

    public VertexBatch(int numVertices) {
        setSize(numVertices);
    }

    public void setSize(int numVertices) {
        if (numVertices < 3) {
            numVertices = 3;
        }
        this.limit = numVertices;
        this.vertices = new float[(this.limit * 2)];
        this.colors = new float[(this.limit * 4)];
        this.texCoords = new float[(this.limit * 2)];
        this.vertexBuffer = Utils.arrayToBuffer(this.vertices);
        this.colorBuffer = Utils.arrayToBuffer(this.colors);
        this.texCoordBuffer = Utils.arrayToBuffer(this.texCoords);
        reset();
    }

    public void addVertex(float x, float y) {
        if (this.vi != this.vertices.length) {
            float[] fArr = this.vertices;
            int i = this.vi;
            this.vi = i + 1;
            fArr[i] = x;
            fArr = this.vertices;
            i = this.vi;
            this.vi = i + 1;
            fArr[i] = y;
        }
    }

    public void addVertex(float x, float y, float r, float g, float b, float a) {
        if (this.vi != this.vertices.length) {
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
        }
    }

    public void addVertex(float x, float y, float r, float g, float b, float a, float tx, float ty) {
        if (this.vi != this.vertices.length) {
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
            fArr = this.texCoords;
            i = this.ti;
            this.ti = i + 1;
            fArr[i] = tx;
            fArr = this.texCoords;
            i = this.ti;
            this.ti = i + 1;
            fArr[i] = ty;
        }
    }

    public void addColor(float r, float g, float b, float a) {
        if (this.ci != this.colors.length) {
            float[] fArr = this.colors;
            int i = this.ci;
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
        }
    }

    public void addTexCoord(float tx, float ty) {
        if (this.ti != this.texCoords.length) {
            float[] fArr = this.texCoords;
            int i = this.ti;
            this.ti = i + 1;
            fArr[i] = tx;
            fArr = this.texCoords;
            i = this.ti;
            this.ti = i + 1;
            fArr[i] = ty;
        }
    }

    public void buildSquareIndices() {
        this.indices = new short[((this.limit * 6) / 4)];
        this.indexBuffer = Utils.arrayToBuffer(this.indices);
        short i = (short) 0;
        short o = (short) 0;
        short max = (short) this.indices.length;
        while (i < max) {
            this.indices[i] = o;
            this.indices[i + 1] = (short) (o + 1);
            this.indices[i + 2] = (short) (o + 2);
            this.indices[i + 3] = (short) (o + 1);
            this.indices[i + 4] = (short) (o + 2);
            this.indices[i + 5] = (short) (o + 3);
            i = (short) (i + 6);
            o = (short) (o + 4);
        }
        this.indexBuffer.put(this.indices);
        this.indexBuffer.position(0);
    }

    public void arraysToBuffers() {
        this.vertexBuffer.put(this.vertices);
        this.vertexBuffer.position(0);
        this.colorBuffer.put(this.colors);
        this.colorBuffer.position(0);
        this.texCoordBuffer.put(this.texCoords);
        this.texCoordBuffer.position(0);
    }

    public void drawTriangles(ForestRenderer renderer, float[] matrix) {
        renderer.drawTriangles(this.vertexBuffer, this.colorBuffer, this.vertices.length >> 1, matrix);
    }

    public void drawTriangleStrip(ForestRenderer renderer, float[] matrix) {
        renderer.drawTriangleStrips(this.vertexBuffer, this.colorBuffer, this.vertices.length >> 1, matrix);
    }

    public void drawTriangleStripTextured(ForestRenderer renderer, float[] matrix) {
        renderer.drawTexturedTriangleStrips(this.vertexBuffer, this.colorBuffer, this.texCoordBuffer, this.vertices.length >> 1, matrix);
    }

    public void drawTexturedTriangleElements(ForestRenderer renderer, float[] matrix) {
        renderer.drawTexturedTriangles(this.vertexBuffer, this.colorBuffer, this.texCoordBuffer, this.indexBuffer, this.indices.length, matrix);
    }

    public void reset() {
        this.ti = 0;
        this.ci = 0;
        this.vi = 0;
    }

    public void resetColors() {
        this.ci = 0;
    }
}
