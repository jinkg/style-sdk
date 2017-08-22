package com.yalin.wallpaper.forest.core.layer;

import android.graphics.Color;

import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Utils;
import com.yalin.wallpaper.forest.core.Weather;
import com.yalin.wallpaper.forest.core.World;

import java.nio.FloatBuffer;


public class SkyLayer extends Layer {
    private float[] bottom = new float[3];
    private float[] bottomCloudy = new float[3];
    private float[] bottomReal = new float[3];
    private float cloudiness = ((float) Weather.INSTANCE.getCloudiness());
    private FloatBuffer colorBuffer;
    private float[] colors;
    private float targetCloudiness;
    private float[] top = new float[3];
    private float[] topCloudy = new float[3];
    private float[] topReal = new float[3];
    private FloatBuffer vertexBuffer;

    public SkyLayer(World w) {
        this.world = w;
    }

    public void prepareForRendering() {
        float[] vertices = new float[]{0.0f, this.world.skyBottom, this.screenWidth,
                this.world.skyBottom, 0.0f, this.screenHeight, this.screenWidth, this.screenHeight};
        this.vertexBuffer = Utils.arrayToBuffer(vertices);
        this.vertexBuffer.put(vertices);
        this.vertexBuffer.position(0);
        recolor();
    }

    public void recolor() {
        int topcol = this.world.colors.skyTop();
        int botcol = this.world.colors.skyBottom();
        this.topReal[0] = ((float) Color.red(topcol)) / 255.0f;
        this.topReal[1] = ((float) Color.green(topcol)) / 255.0f;
        this.topReal[2] = ((float) Color.blue(topcol)) / 255.0f;
        this.bottomReal[0] = ((float) Color.red(botcol)) / 255.0f;
        this.bottomReal[1] = ((float) Color.green(botcol)) / 255.0f;
        this.bottomReal[2] = ((float) Color.blue(botcol)) / 255.0f;
        topcol = this.world.colors.cloudySkyTop();
        botcol = this.world.colors.cloudySkyBottom();
        this.topCloudy[0] = ((float) Color.red(topcol)) / 255.0f;
        this.topCloudy[1] = ((float) Color.green(topcol)) / 255.0f;
        this.topCloudy[2] = ((float) Color.blue(topcol)) / 255.0f;
        this.bottomCloudy[0] = ((float) Color.red(botcol)) / 255.0f;
        this.bottomCloudy[1] = ((float) Color.green(botcol)) / 255.0f;
        this.bottomCloudy[2] = ((float) Color.blue(botcol)) / 255.0f;
        this.colors = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        this.colorBuffer = Utils.arrayToBuffer(this.colors);
    }

    public void onDraw(ForestRenderer renderer) {
        this.colorBuffer.put(this.colors);
        this.colorBuffer.position(0);
        renderer.drawTriangleStrips(this.vertexBuffer, this.colorBuffer, 4, renderer.identityMatrix);
    }

    public void tick(double dt) {
        this.targetCloudiness = (float) Weather.INSTANCE.getCloudiness();
        this.cloudiness = this.targetCloudiness;
        this.top[0] = (this.topReal[0] * (1.0f - this.cloudiness)) + (this.topCloudy[0] * this.cloudiness);
        this.top[1] = (this.topReal[1] * (1.0f - this.cloudiness)) + (this.topCloudy[1] * this.cloudiness);
        this.top[2] = (this.topReal[2] * (1.0f - this.cloudiness)) + (this.topCloudy[2] * this.cloudiness);
        this.bottom[0] = (this.bottomReal[0] * (1.0f - this.cloudiness)) + (this.bottomCloudy[0] * this.cloudiness);
        this.bottom[1] = (this.bottomReal[1] * (1.0f - this.cloudiness)) + (this.bottomCloudy[1] * this.cloudiness);
        this.bottom[2] = (this.bottomReal[2] * (1.0f - this.cloudiness)) + (this.bottomCloudy[2] * this.cloudiness);
        float[] fArr = this.colors;
        float[] fArr2 = this.colors;
        float f = this.bottom[0];
        fArr2[4] = f;
        fArr[0] = f;
        fArr = this.colors;
        fArr2 = this.colors;
        f = this.bottom[1];
        fArr2[5] = f;
        fArr[1] = f;
        fArr = this.colors;
        fArr2 = this.colors;
        f = this.bottom[2];
        fArr2[6] = f;
        fArr[2] = f;
        fArr = this.colors;
        float[] fArr3 = this.colors;
        float f2 = this.top[0];
        fArr3[12] = f2;
        fArr[8] = f2;
        fArr = this.colors;
        fArr3 = this.colors;
        f2 = this.top[1];
        fArr3[13] = f2;
        fArr[9] = f2;
        fArr = this.colors;
        fArr3 = this.colors;
        f2 = this.top[2];
        fArr3[14] = f2;
        fArr[10] = f2;
    }
}
