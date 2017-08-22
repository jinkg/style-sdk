package com.yalin.wallpaper.forest.core.layer;

import android.opengl.Matrix;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.World;

import java.util.HashMap;

public abstract class Layer {
    public static final int CHRISTMAS_STAR = 8;
    public static final int CLOUDS = 11;
    public static final int FIREWORKS = 7;
    public static final int FOREST_FRONT = 2;
    public static final int FOREST_REAR = 1;
    public static final int MOUNTAINS = 6;
    public static final int RAINBOW = 12;
    public static final int RAIN_FRONT = 4;
    public static final int RAIN_REAR = 3;
    public static final int SKY = 0;
    public static final int SNOW_FRONT = 10;
    public static final int SNOW_REAR = 9;
    public static final int STARS = 5;
    private static HashMap<Integer, String> layer2name = new HashMap<>();
    private static HashMap<String, Integer> name2layer = new HashMap<>();
    public boolean enabled = true;
    public int id = -1;
    private final float[] identityMatrix = new float[16];
    private final float[] matrix = new float[16];
    protected float scale;
    protected float screenAspect;
    protected float screenHeight;
    protected float screenWidth;
    private float tiltOffsetX;
    private float tiltOffsetY;
    private float tiltValue = 0.0f;
    private boolean useTilt = false;
    public World world;
    protected float zDistance;

    public abstract void onDraw(ForestRenderer forestRenderer);

    public abstract void prepareForRendering();

    public void setDistance(float z) {
        this.zDistance = z;
        this.scale = (0.5f / this.zDistance) + 0.5f;
    }

    public void setScreenSize(float w, float h) {
        this.screenWidth = w;
        this.screenHeight = h;
        this.screenAspect = this.screenWidth / this.screenHeight;
    }

    public float getLayerWidth() {
        return this.screenWidth + (this.screenWidth / this.zDistance);
    }

    public void recolor() {
    }

    public void setTilt(float x, float y) {
        this.tiltOffsetX = ((float) Math.tan((double) x)) * this.tiltValue;
        this.tiltOffsetY = ((float) Math.tan((double) y)) * this.tiltValue;
    }

    public void setTiltValue(boolean enabled, float value) {
        this.useTilt = enabled;
        if (!enabled) {
            setTilt(0.0f, 0.0f);
        }
        this.tiltValue = ((((((float) Math.sqrt((double) Math.max(0.0f, this.zDistance - 1.0f))) * 2.0f) + 1.0f) + this.zDistance) * 0.5f) * value;
    }

    protected float[] getOffsetMatrix(ForestRenderer renderer) {
        System.arraycopy(this.identityMatrix, 0, this.matrix, 0, 16);
        Matrix.translateM(this.matrix, 0, (((-renderer.screenOffset) * renderer.screenWidth) / this.zDistance) + this.tiltOffsetX, this.tiltOffsetY, 0.0f);
        return this.matrix;
    }

    public void setIdentityMatrix(ForestRenderer renderer) {
        System.arraycopy(renderer.identityMatrix, 0, this.identityMatrix, 0, 16);
        if (this.useTilt) {
            Matrix.translateM(this.identityMatrix, 0, renderer.screenWidth * 0.5f, renderer.screenHeight * 0.5f, 0.0f);
            float s = 1.0f + (this.tiltValue * 1.25f);
            Matrix.scaleM(this.identityMatrix, 0, s, s, 1.0f);
            Matrix.translateM(this.identityMatrix, 0, (-renderer.screenWidth) * 0.5f, (-renderer.screenHeight) * 0.5f, 0.0f);
        }
    }

    public void tick(double dt) {
    }

    static {
        setupMaps(3, "rain-rear");
        setupMaps(4, "rain-front");
        setupMaps(9, "snow-rear");
        setupMaps(10, "snow-front");
        setupMaps(5, "stars");
        setupMaps(6, "mountains");
        setupMaps(7, "fireworks");
        setupMaps(11, "clouds");
        setupMaps(12, "rainbow");
    }

    public static void setupMaps(int id, String name) {
        name = String.format("layer-%s-enabled", name);
        layer2name.put(id, name);
        name2layer.put(name, id);
    }

    public static boolean loadState(int id) {
        if (id == 0) {
            return true;
        }
        if (id == 1) {
            return true;
        }
        if (id == 2) {
            return true;
        }
        if (layer2name.containsKey(id)) {
            return true;
        }
        return false;
    }

    public static boolean stateChangedForKey(String key) {
        return name2layer.containsKey(key);
    }
}
