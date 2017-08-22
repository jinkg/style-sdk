package com.yalin.wallpaper.forest.core.layer;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Utils;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class StarLayer extends Layer {
    private FloatBuffer colorBuffer;
    private float[] colors;
    private Boolean drawStars = Boolean.TRUE;
    private double lowerEdge = 0.5d;
    private int numStars;
    private FloatBuffer sizeBuffer;
    private float[] sizes;
    private ArrayList<Star> stars = new ArrayList<>();
    private FloatBuffer vertexBuffer;
    private float[] vertices;

    class Star {
        public float f23a;
        public float size;
        public float f24x;
        public float f25y;

        Star() {
        }
    }

    public StarLayer(World w) {
        this.world = w;
    }

    public void prepareForRendering() {
        this.numStars = (int) (((180.0f * this.screenWidth) * this.screenHeight) / this.scale);
        for (int i = 0; i < this.numStars; i++) {
            this.stars.add(new Star());
        }
        float width = getLayerWidth();
        Iterator it = this.stars.iterator();
        while (it.hasNext()) {
            Star s = (Star) it.next();
            s.f24x = ((float) Math.random()) * width;
            double d = Math.random();
            s.f23a = (float) (((Math.pow(Math.random(), 2.0d) * d) * 0.9d) + 0.1d);
            s.f25y = (float) (((1.0d - this.lowerEdge) * d) + this.lowerEdge);
            s.size = ((float) Math.random()) * 10.0f;
        }
        this.vertices = new float[(this.numStars * 2)];
        this.colors = new float[(this.numStars * 4)];
        this.sizes = new float[this.numStars];
        this.vertexBuffer = Utils.arrayToBuffer(this.vertices);
        this.colorBuffer = Utils.arrayToBuffer(this.colors);
        this.sizeBuffer = Utils.arrayToBuffer(this.sizes);
        fillArrays();
    }

    public void recolor() {
        if (this.colorBuffer != null) {
            Boolean valueOf = true;
            this.drawStars = valueOf;
            if (valueOf.booleanValue()) {
                double z = this.world.getSunData().getZenithAngle();
                float amul = 0.0f;
                if (z > 98.0d) {
                    if (z < 104.0d) {
                        amul = ((float) (z - 98.0d)) / 6.0f;
                    } else {
                        amul = 1.0f;
                    }
                }
                amul = (float) (((double) amul) * Math.pow(1.0d - Weather.INSTANCE.getCloudiness(), 2.0d));
                int max = this.stars.size();
                int c = 0;
                for (int i = 0; i < max; i++) {
                    Star s = (Star) this.stars.get(i);
                    int c2 = c + 1;
                    this.colors[c] = 1.0f;
                    c = c2 + 1;
                    this.colors[c2] = 1.0f;
                    c2 = c + 1;
                    this.colors[c] = 1.0f;
                    c = c2 + 1;
                    this.colors[c2] = s.f23a * amul;
                }
                this.colorBuffer.position(0);
                this.colorBuffer.put(this.colors);
            }
        }
    }

    public void onDraw(ForestRenderer renderer) {
        if (this.drawStars) {
            this.vertexBuffer.position(0);
            this.colorBuffer.position(0);
            this.sizeBuffer.position(0);
            renderer.drawPoints(this.vertexBuffer, this.colorBuffer, this.numStars, getOffsetMatrix(renderer));
        }
    }

    private void fillArrays() {
        int i = 0;
        int max = this.stars.size();
        int ps = 0;
        int v = 0;
        while (i < max) {
            Star s = (Star) this.stars.get(i);
            int i2 = v + 1;
            this.vertices[v] = s.f24x;
            v = i2 + 1;
            this.vertices[i2] = s.f25y;
            int ps2 = ps + 1;
            this.sizes[ps] = s.size;
            i++;
            ps = ps2;
        }
        this.vertexBuffer.position(0);
        this.vertexBuffer.put(this.vertices);
        this.sizeBuffer.position(0);
        this.sizeBuffer.put(this.sizes);
        recolor();
    }

    public void tick(double dt) {
    }
}
