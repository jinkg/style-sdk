package com.yalin.wallpaper.forest.core.layer;

import android.graphics.Color;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.VertexBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MountainLayer extends Layer {
    private double cloudiness;
    private double mountainH;
    private double mountainY;
    private ArrayList<Mountain> mountains = new ArrayList();

    class C00381 implements Comparator<Mountain> {
        C00381() {
        }

        public int compare(Mountain m1, Mountain m2) {
            return m1.f14y - m1.f11h < m2.f14y - m2.f11h ? 1 : -1;
        }
    }

    class Mountain {
        public VertexBatch batch;
        public float f11h;
        public int numVertices;
        public float f12w;
        public float f13x;
        public float f14y;

        public Mountain(double x, double y, double w, double h) {
            f13x = (float) x;
            f14y = (float) y;
            f12w = (float) w;
            f11h = (float) h;
            numVertices = ((int) (100.0d * h)) + 2;
            batch = new VertexBatch(numVertices);
            build((float) x, (float) y, (float) w, (float) h);
        }

        private void build(float x, float y, float w, float h) {
            int side = 0;
            float diff = (1.0f * h) / ((float) this.numVertices);
            float diff2 = diff / 2.0f;
            int max = this.numVertices - 2;
            for (int n = 0; n < max; n++) {
                this.batch.addVertex((((((((float) n) * w) * 0.5f) / ((float) max)) * ((float) side)) + x) + ((float) ((Math.random() * ((double) diff)) - ((double) diff2))), (y - ((((float) n) * h) / ((float) max))) + ((float) ((Math.random() * ((double) diff)) - ((double) diff2))));
                side = side == 1 ? -1 : 1;
            }
            this.batch.addVertex(((((y - MountainLayer.this.world.skyBottom) * (w / h)) * 0.5f) * ((float) side)) + x, MountainLayer.this.world.skyBottom);
            this.batch.addVertex(((((y - MountainLayer.this.world.skyBottom) * (w / h)) * 0.5f) * ((float) (side == 1 ? -1 : 1))) + x, MountainLayer.this.world.skyBottom);
            this.batch.vertices[0] = (((this.batch.vertices[0] * 4.0f) + (this.batch.vertices[2] * 2.0f)) + (this.batch.vertices[4] * 1.0f)) / 7.0f;
            this.batch.vertices[1] = (((this.batch.vertices[1] * 4.0f) + (this.batch.vertices[3] * 2.0f)) + (this.batch.vertices[5] * 1.0f)) / 7.0f;
        }

        public void color(int skyTop, int skyBottom) {
            int n;
            int color = MountainLayer.this.world.colors.mountain();
            int bottom = skyColorAtHeight(this.f14y - this.f11h, skyTop, skyBottom);
            this.batch.resetColors();
            int max = this.numVertices - 2;
            for (n = 0; n < max; n++) {
                int col = MountainLayer.this.world.colors.mix(color, bottom, (double) (((float) Math.max(0, (max - n) - 2)) / ((float) max)));
                this.batch.addColor(((float) Color.red(col)) / 255.0f, ((float) Color.green(col)) / 255.0f, ((float) Color.blue(col)) / 255.0f, 1.0f);
            }
            for (n = 0; n < 2; n++) {
                this.batch.addColor(((float) Color.red(skyBottom)) / 255.0f, ((float) Color.green(skyBottom)) / 255.0f, ((float) Color.blue(skyBottom)) / 255.0f, 1.0f);
            }
            this.batch.arraysToBuffers();
        }

        private int skyColorAtHeight(float h, int skyTop, int skyBottom) {
            return MountainLayer.this.world.colors.mix(skyTop, skyBottom, (double) ((h - MountainLayer.this.world.skyBottom) / (1.0f - MountainLayer.this.world.skyBottom)));
        }
    }

    public MountainLayer(World w, double y, double h) {
        this.world = w;
        this.mountainY = y;
        this.mountainH = h;
        this.cloudiness = (double) ((float) Weather.INSTANCE.getCloudiness());
    }

    public void prepareForRendering() {
        double cx = ((Math.random() * ((double) this.screenWidth)) * 0.7d) + (((double) this.screenWidth) * 0.15d);
        int n = (((int) (2.0f * this.screenWidth)) + 2) + (((int) (Math.random() * 99.0d)) % 3);
        for (int i = 0; i < n; i++) {
            double h;
            double y;
            double w;
            double x;
            double base = 0.55d - (0.15d * (((double) i) / ((double) (n - 1))));
            boolean flag;
            do {
                flag = false;
                h = ((Math.random() * 0.25d) + 0.75d) * 0.25d;
                y = base + h;
                w = (((Math.random() * 4.0d) + 9.0d) * h) * 0.2d;
                while (true) {
                    x = cx + (Math.random() - 0.5d);
                    if ((0.5d * w) + x >= 0.2d && x - (0.5d * w) <= ((double) this.screenWidth) - 0.2d) {
                        break;
                    }
                }
                Iterator it = this.mountains.iterator();
                while (it.hasNext()) {
                    Mountain m = (Mountain) it.next();
                    if (distance(x, y, (double) m.f13x, (double) m.f14y) < 0.04d) {
                        flag = true;
                        break;
                    }
                }
            } while (flag);
            this.mountains.add(new Mountain(x, y, w, h));
        }
        reorder();
        recolor();
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
    }

    private void reorder() {
        Collections.sort(this.mountains, new C00381());
    }

    public void recolor() {
//        this.cloudiness = Weather.INSTANCE.getCloudiness();
        this.cloudiness = 0.5;
        int top = this.world.colors.mix(this.world.colors.cloudySkyTop(),
                this.world.colors.skyTop(), (double) ((float) this.cloudiness));
        int bottom = this.world.colors.mix(this.world.colors.cloudySkyBottom(),
                this.world.colors.skyBottom(), (double) ((float) this.cloudiness));
        for (Mountain mountain : this.mountains) {
            mountain.color(top, bottom);
        }
    }

    public void onDraw(ForestRenderer renderer) {
        float[] matrix = getOffsetMatrix(renderer);
        for (Mountain mountain : this.mountains) {
            mountain.batch.drawTriangleStrip(renderer, matrix);
        }
    }

    public void tick(double dt) {
        if (Math.abs(Weather.INSTANCE.getCloudiness() - this.cloudiness) > 0.001d) {
            recolor();
        }
    }
}
