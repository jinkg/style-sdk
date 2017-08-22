package com.yalin.wallpaper.forest.core.layer;

import android.graphics.Color;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Tree;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;
import com.yalin.wallpaper.forest.core.gl.VertexBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ForestLayer extends Layer {
    public Curve curve;
    private float[] hillcolorvalues;
    private VertexBatch hills = new VertexBatch(3);
    private int layer;
    private float targetWind;
    private double time;
    private ArrayList<Tree> trees = new ArrayList<>();
    private VertexBatch triangles;
    private float wind;

    class C00371 implements Comparator<Tree> {
        C00371() {
        }

        public int compare(Tree t1, Tree t2) {
            return t1.f2y < t2.f2y ? 1 : -1;
        }
    }

    public interface Curve {
        double mo18y(double d);
    }

    public ForestLayer(World w) {
        this.world = w;
        this.targetWind = (float) Weather.INSTANCE.getWindSpeed();
    }

    public void setup(int numTrees, int layer, float sizeBase, float sizeMul, Curve curve, Boolean fill, float fillSizeBase, float fillSizeMul) {
        this.curve = curve;
        float layer_width = getLayerWidth();
        this.layer = layer;
        this.trees.clear();
        int max;
        int i;
        if (fill) {
            max = (int) (((this.screenHeight * layer_width) * ((float) numTrees)) * 10.0f);
            for (i = 0; i < max; i++) {
                addTree(new Tree(this.world, ((float) Math.random()) * layer_width, ((float) Math.random()) * this.screenHeight, (((float) Math.random()) * fillSizeMul) + fillSizeBase, layer));
            }
        } else {
            float mul = layer == 0 ? 0.15f : 0.1f;
            max = (int) (((float) numTrees) * layer_width);
            for (i = 0; i < max; i++) {
                float x = ((((float) i) / ((float) numTrees)) + (((float) Math.random()) * mul)) - (0.5f * mul);
                float y = (float) Math.min((double) getRandomCurveY(((float) i) / ((float) numTrees)), curve.mo18y((double) x) - 0.009999999776482582d);
                addTree(new Tree(this.world, x, y, ((((float) Math.random()) * sizeMul) + sizeBase) - ((((float) curve.mo18y((double) x)) - y) * 0.2f), layer));
            }
        }
        reorder();
    }

    public void prepareForRendering() {
        this.triangles = new VertexBatch(this.trees.size() * 3);
        recolor();
    }

    private void reorder() {
        Collections.sort(this.trees, new C00371());
    }

    private void addTree(Tree t) {
        t.setForest(this);
        this.trees.add(t);
    }

    private float getRandomCurveY(float x) {
        return (float) (this.curve.mo18y((double) x) - Math.pow((Math.random() * 0.4d) * ((double) this.scale), 2.0d));
    }

    public void recolor() {
        int max = this.trees.size();
        for (int i = 0; i < max; i++) {
            ((Tree) this.trees.get(i)).prepColor();
        }
        float[] hsv = this.layer == 0 ? this.world.colors.frontHill() : this.world.colors.rearHill();
        setHillColor(hsv[0], hsv[1], hsv[2]);
    }

    private void setHillColor(float h, float s, float v) {
        int ct = Color.HSVToColor(new float[]{h, s, v});
        int cb = Color.HSVToColor(new float[]{h, s, 0.5f * v});
        this.hillcolorvalues = new float[]{((float) Color.red(cb)) / 255.0f, ((float) Color.green(cb)) / 255.0f, ((float) Color.blue(cb)) / 255.0f, ((float) Color.red(ct)) / 255.0f, ((float) Color.green(ct)) / 255.0f, ((float) Color.blue(ct)) / 255.0f};
        buildHills();
    }

    public void onDraw(ForestRenderer renderer) {
        float[] matrix = getOffsetMatrix(renderer);
        this.hills.drawTriangleStrip(renderer, matrix);
        fillArrays();
        this.triangles.arraysToBuffers();
        this.triangles.drawTriangles(renderer, matrix);
    }

    private void fillArrays() {
        this.triangles.reset();
        int max = this.trees.size();
        for (int i = 0; i < max; i++) {
            Tree t = (Tree) this.trees.get(i);
            t.update(0.02f);
            t.fillBatch(this.triangles);
        }
    }

    private void buildHills() {
        float width = getLayerWidth();
        int n = (int) (10.0f * width);
        this.hills.setSize((n + 1) * 2);
        int max = n + 1;
        for (int i = 0; i < max; i++) {
            float x = (((float) i) * width) / ((float) n);
            this.hills.addVertex(x, 0.0f, this.hillcolorvalues[0], this.hillcolorvalues[1], this.hillcolorvalues[2], 1.0f);
            this.hills.addVertex(x, (float) this.curve.mo18y((double) x), this.hillcolorvalues[3], this.hillcolorvalues[4], this.hillcolorvalues[5], 1.0f);
        }
        this.hills.arraysToBuffers();
    }

    public void tick(double dt) {
        this.time += dt;
        this.targetWind = (float) Weather.INSTANCE.getWindSpeed();
        if (this.targetWind > this.wind) {
            this.wind = (float) (((double) this.wind) + (3.0d * dt));
        } else {
            this.wind = (float) (((double) this.wind) - (3.0d * dt));
        }
    }

    public float windAt(double x) {
        return (((float) Math.max(0.0d, (Math.sin(((this.time * 0.2d) + x) * 5.0d) * 0.032999999821186066d) + 0.02500000037252903d)) * this.wind) * 0.45f;
    }
}
