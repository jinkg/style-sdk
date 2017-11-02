package jp.kzfactory.utils.android;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SimpleImage {
    static ShortBuffer drawImageBufferIndex = null;
    static FloatBuffer drawImageBufferUv = null;
    static FloatBuffer drawImageBufferVer = null;
    private float imageBottom;
    private float imageLeft;
    private float imageRight;
    private float imageTop;
    private int texture;
    private float uvBottom;
    private float uvLeft;
    private float uvRight;
    private float uvTop;

    public SimpleImage(GL10 gl, InputStream in) {
        try {
            this.texture = LoadUtil.loadTexture(gl, in, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.uvLeft = 0.0f;
        this.uvRight = 1.0f;
        this.uvBottom = 0.0f;
        this.uvTop = 1.0f;
        this.imageLeft = -1.0f;
        this.imageRight = 1.0f;
        this.imageBottom = -1.0f;
        this.imageTop = 1.0f;
    }

    public void draw(GL10 gl) {
        float[] ver = new float[]{this.imageLeft, this.imageTop, this.imageRight, this.imageTop, this.imageRight, this.imageBottom, this.imageLeft, this.imageBottom};
        short[] index = new short[]{(short) 0, (short) 1, (short) 2, (short) 0, (short) 2, (short) 3};
        drawImageBufferUv = BufferUtil.setupFloatBuffer(drawImageBufferUv, new float[]{this.uvLeft, this.uvBottom, this.uvRight, this.uvBottom, this.uvRight, this.uvTop, this.uvLeft, this.uvTop});
        drawImageBufferVer = BufferUtil.setupFloatBuffer(drawImageBufferVer, ver);
        drawImageBufferIndex = BufferUtil.setupShortBuffer(drawImageBufferIndex, index);
        gl.glTexCoordPointer(2, 5126, 0, drawImageBufferUv);
        gl.glVertexPointer(2, 5126, 0, drawImageBufferVer);
        gl.glBindTexture(3553, this.texture);
        gl.glDrawElements(4, 6, 5123, drawImageBufferIndex);
    }

    public void setDrawRect(float left, float right, float bottom, float top) {
        this.imageLeft = left;
        this.imageRight = right;
        this.imageBottom = bottom;
        this.imageTop = top;
    }

    public void setUVRect(float left, float right, float bottom, float top) {
        this.uvLeft = left;
        this.uvRight = right;
        this.uvBottom = bottom;
        this.uvTop = top;
    }
}
