package com.yalin.wallpaper.forest.core.layer;

import android.opengl.GLES20;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Textures;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.VertexBatch;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CloudLayer extends Layer {
    private VertexBatch batch;
    private float cloudiness;
    private ArrayList<Cloud> clouds = new ArrayList();
    private double lowerEdge = 0.65d;
    private int numClouds;
    private float[] rgbBottom;
    private float[] rgbTop;
    private float scale;
    private double timer;
    private float wind;
    private float zenithAngle;

    class Cloud {
        public float f8a;
        private float altitude;
        private float brightnessMultiplier;
        private float[] color = new float[3];
        private float[][] colors = ((float[][]) Array.newInstance(Float.TYPE, new int[]{4, 4}));
        public float size;
        private float velocity;
        private boolean vertexOrderZ;
        public float f9x;
        private float xr;
        public float f10y;
        private float yr;

        public Cloud() {
            reset();
            this.f9x = (((float) Math.random()) * (CloudLayer.this.getLayerWidth() + (this.xr * 2.0f))) - this.xr;
        }

        public void recolor() {
            float mt = (float) ((((double) this.f10y) - CloudLayer.this.lowerEdge) / (1.0d - CloudLayer.this.lowerEdge));
            float mb = 1.0f - mt;
            for (int i = 0; i < 3; i++) {
                this.color[i] = (CloudLayer.this.rgbTop[i] * mt) + (CloudLayer.this.rgbBottom[i] * mb);
            }
            if (((double) CloudLayer.this.zenithAngle) <= 83.7168146928204d || ((double) CloudLayer.this.zenithAngle) >= 96.2831853071796d) {
                this.brightnessMultiplier = 1.0f;
            } else {
                this.brightnessMultiplier = 0.9f - (((float) Math.cos((double) ((CloudLayer.this.zenithAngle - 90.0f) / 2.0f))) * 0.1f);
            }
            setVertexColor(0, 1.0f);
            setVertexColor(1, 1.0f);
            setVertexColor(2, 0.5f);
            setVertexColor(3, 0.5f);
        }

        private void setVertexColor(int i, float m) {
            this.colors[i][0] = this.color[0] * m;
            this.colors[i][1] = this.color[1] * m;
            this.colors[i][2] = this.color[2] * m;
            this.colors[i][3] = this.f8a * this.brightnessMultiplier;
        }

        public void reset() {
            float width = CloudLayer.this.getLayerWidth();
            setSize((CloudLayer.this.scale * ((((float) Math.random()) * 10.0f) + 10.0f)) / 200.0f);
            this.f9x = this.xr + width;
            this.altitude = (float) Math.random();
            this.f10y = (float) ((((double) this.altitude) * (1.0d - CloudLayer.this.lowerEdge)) + CloudLayer.this.lowerEdge);
            this.f8a = 0.65f + (0.35f * ((float) Math.random()));
            this.vertexOrderZ = Math.random() < 0.5d;
            this.velocity = ((float) (Math.random() + ((double) (1.0f - this.altitude)))) * 0.5f;
            recolor();
        }

        public void setSize(float s) {
            this.size = s;
            this.xr = s;
            this.yr = 0.25f * s;
        }

        public void tick() {
            this.f9x -= (((this.velocity * 4.0f) + 1.0f) + (CloudLayer.this.wind * ((2.0f * this.velocity) + 4.0f))) * 1.0E-4f;
            if (this.f9x < (-this.xr)) {
                reset();
            }
        }

        public void draw(VertexBatch batch) {
            float o = (this.altitude * CloudLayer.this.cloudiness) + (1.0f - CloudLayer.this.cloudiness);
            batch.addVertex(this.f9x - this.xr, this.yr + this.f10y, this.colors[0][0] * o, this.colors[0][1] * o, this.colors[0][2] * o, this.colors[0][3] * o, 0.0f, 0.0f);
            if (this.vertexOrderZ) {
                batch.addVertex(this.xr + this.f9x, this.yr + this.f10y, this.colors[1][0] * o, this.colors[1][1] * o, this.colors[1][2] * o, this.colors[1][3] * o, 1.0f, 0.0f);
                batch.addVertex(this.f9x - this.xr, this.f10y - this.yr, this.colors[2][0] * o, this.colors[2][1] * o, this.colors[2][2] * o, this.colors[2][3] * o, 0.0f, 1.0f);
            } else {
                batch.addVertex(this.f9x - this.xr, this.f10y - this.yr, this.colors[2][0] * o, this.colors[2][1] * o, this.colors[2][2] * o, this.colors[2][3] * o, 0.0f, 1.0f);
                batch.addVertex(this.xr + this.f9x, this.yr + this.f10y, this.colors[1][0] * o, this.colors[1][1] * o, this.colors[1][2] * o, this.colors[1][3] * o, 1.0f, 0.0f);
            }
            batch.addVertex(this.xr + this.f9x, this.f10y - this.yr, this.colors[3][0] * o, this.colors[3][1] * o, this.colors[3][2] * o, this.colors[3][3] * o, 1.0f, 1.0f);
        }
    }

    public CloudLayer(World w, float scale) {
        this.world = w;
        this.scale = scale;
        this.zenithAngle = (float) this.world.getSunData().getZenithAngle();
        setWind((float) Weather.INSTANCE.getWindSpeed());
        this.cloudiness = (float) Weather.INSTANCE.getCloudiness();
    }

    public void prepareForRendering() {
        setupColors();
        this.numClouds = (int) (((100.0f * this.screenWidth) * this.screenHeight) / this.scale);
        for (int i = 0; i < this.numClouds; i++) {
            this.clouds.add(new Cloud());
        }
        this.batch = new VertexBatch(this.numClouds * 4);
        this.batch.buildSquareIndices();
    }

    public void recolor() {
        setupColors();
        int max = this.clouds.size();
        for (int i = 0; i < max; i++) {
            this.clouds.get(i).recolor();
        }
    }

    public void onDraw(ForestRenderer renderer) {
        this.batch.reset();
        int max = this.clouds.size();
        for (int i = 0; i < max; i++) {
            Cloud c = this.clouds.get(i);
            c.tick();
            c.draw(this.batch);
        }
        Textures.use(Textures.CLOUD);
        this.batch.arraysToBuffers();
        GLES20.glBlendFunc(1, 771);
        this.batch.drawTexturedTriangleElements(renderer, getOffsetMatrix(renderer));
        GLES20.glBlendFunc(770, 771);
    }

    public void tick(double dt) {
        double d = this.timer + dt;
        this.timer = d;
        if (d > 1.0d) {
            this.timer = 0.0d;
            this.zenithAngle = (float) this.world.getSunData().getZenithAngle();
            setWind((float) Weather.INSTANCE.getWindSpeed());
            this.cloudiness = (float) Weather.INSTANCE.getCloudiness();
        }
    }

    private void setupColors() {
        this.rgbTop = this.world.colors.cloudsTop();
        this.rgbBottom = this.world.colors.cloudsBottom();
    }

    private void setWind(float mps) {
        this.wind = Math.min(1.0f, mps / 30.0f);
    }
}
