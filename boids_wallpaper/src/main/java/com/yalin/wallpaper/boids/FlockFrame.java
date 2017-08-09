package com.yalin.wallpaper.boids;

import android.graphics.Color;

import java.util.Comparator;

class FlockFrame {
    private static final String TAG = "FlockFrame";

    public static class FlockNode {

        public final float[] position = new float[3];
        public float size;
        public final float[] color = new float[4];

        public void copy(Boid b) {
            position[0] = b.position.x;
            position[1] = b.position.y;
            position[2] = b.position.z;

            size = b.size;

            int alpha = Math.round(b.color[3] * 255);
            int rgb = Color.HSVToColor(alpha, b.color);
            color[0] = (float)Color.red(rgb) / 255f;
            color[1] = (float)Color.green(rgb) / 255f;
            color[2] = (float)Color.blue(rgb) / 255f;
            color[3] = b.opacity;
        }

        public void copy(FlockNode n, float sizeScale, float opacityScale) {
            position[0] = n.position[0];
            position[1] = n.position[1];
            position[2] = n.position[2];
            size = sizeScale * n.size;
            color[0] = n.color[0];
            color[1] = n.color[1];
            color[2] = n.color[2];
            color[3] = opacityScale * n.color[3];
        }

        public void copy(FlockNode n) {
            copy(n, 1.0f, 1.0f);
        }
    }

    public static class DepthComparator implements Comparator {
        public final int compare(Object a, Object b) {
            FlockNode ba = (FlockNode)a;
            FlockNode bb = (FlockNode)b;
            return Float.compare(ba.position[2], bb.position[2]);
        }
    }


    private FlockNode[] nodes;
    
    private int count = 0;
    private int size = 0;

    private boolean dirty = false;

    private float[] positions;
    private float[] sizes;
    private float[] colors;

    private float opacityScale = 1.0f;
    private float sizeScale = 1.0f;

    private final DepthComparator comparator = new DepthComparator();

    public FlockFrame(int size) {
        this.count = 0;
        allocate(size);
    }

    void allocate(int size) {
        this.size = size;

        nodes = new FlockNode[size];

        for(int i=0; i<size; i++)
            nodes[i] = new FlockNode();

        positions = new float[size * 3];
        sizes = new float[size];
        colors = new float[size * 4];        

        clear();
    }

    public void add(Boid b) {
        if(count < size) {
            nodes[count++].copy(b);
            dirty = true;
        }
    }

    public void add(FlockFrame f) {

        FlockNode other[] = f.getNodes();

        for(int i=0; i<f.getCount(); i++) {
            if (count< size) {
                nodes[count++].copy(other[i], f.getSizeScale(), f.getOpacityScale());
                dirty = true;
            }
        }

    }

    public void clear() {
        count = 0;
        dirty = false;
    }

    float getSizeScale() {
        return sizeScale;
    }

    void setSizeScale(float sizeScale) {
        this.sizeScale = sizeScale;
    }

    float getOpacityScale() {
        return opacityScale;
    }

    void setOpacityScale(float opacityScale) {
        this.opacityScale = opacityScale;
    }

    public void render() {
        
        //Sort.instance().sort(nodes, comparator, 0, count);

        for(int i=0; i<count; i++) { 
            int p_offset = i * 3; 
            positions[p_offset] = nodes[i].position[0];
            positions[p_offset + 1] = nodes[i].position[1];
            positions[p_offset + 2] = nodes[i].position[2];

            int c_offset = i * 4;
            colors[c_offset] = nodes[i].color[0];
            colors[c_offset + 1] = nodes[i].color[1];
            colors[c_offset + 2] = nodes[i].color[2];
            colors[c_offset + 3] = nodes[i].color[3] * opacityScale;

            sizes[i] = nodes[i].size * sizeScale;
        }

        dirty = false;
    }

    public int getCount() {
        return count;
    }

    public FlockNode[] getNodes() {
        return nodes;
    }

    public float[] getPositions() {
        if(dirty) {
            render();
        }

        return positions;
    }

    public float[] getColors() {
        if(dirty) {
            render();
        }

        return colors;
    }

    public float[] getSizes() {
        if(dirty) {
            render();
        }

        return sizes;
    }

}
