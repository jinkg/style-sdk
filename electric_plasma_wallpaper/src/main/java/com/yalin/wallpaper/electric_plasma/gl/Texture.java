package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class Texture {
    protected boolean bound = false;
    private int glBinding;
    public final int id;
    private ShortBuffer indices = null;
    private FloatBuffer vertices = null;

    public abstract Buffer data();

    public abstract int glType();

    public abstract int height();

    public abstract int width();

    public Texture(int id) {
        this.id = id;
    }

    public static int roundUpPowerOfTwo(int a) {
        if (a <= 0) {
            return 0;
        }
        int counter = 1;
        while (counter < a) {
            counter *= 2;
        }
        return counter;
    }

    public ShortBuffer getIndices() {
        if (this.indices != null) {
            return this.indices;
        }
        short[] is = new short[6];
        is[1] = (short) 2;
        is[2] = (short) 1;
        is[4] = (short) 3;
        is[5] = (short) 2;
        ShortBuffer indicesBuffer = ByteBuffer.allocateDirect(is.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indicesBuffer.put(is).position(0);
        this.indices = indicesBuffer;
        return indicesBuffer;
    }

    public FloatBuffer getVertices() {
        if (this.vertices != null) {
            return this.vertices;
        }
        float heightRatio = ((float) height()) / ((float) roundUpPowerOfTwo(height()));
        float widthRatio = (((float) width()) / ((float) roundUpPowerOfTwo(width())));
        Log.d("plasma", "widthRatio: " + widthRatio + ", heightRatio: " + heightRatio);
        float[] vs = new float[]{-1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, heightRatio, 1.0f, -1.0f, 0.0f, widthRatio, heightRatio, 1.0f, 1.0f, 0.0f, widthRatio, 0.0f};
        FloatBuffer verticesBuffer = ByteBuffer.allocateDirect(vs.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(vs).position(0);
        this.vertices = verticesBuffer;
        return verticesBuffer;
    }

    public int getGLBinding() {
        return this.glBinding;
    }

    public void bindGL() {
        if (!this.bound) {
            Log.d("plasma", "Texture binding started.");
            int[] textureId = new int[1];
            GLES20.glPixelStorei(3317, 1);
            GLException.checkError();
            GLES20.glGenTextures(1, textureId, 0);
            GLException.checkError();
            GLES20.glActiveTexture(33984 + this.id);
            GLException.checkError();
            GLES20.glBindTexture(3553, textureId[0]);
            GLException.checkError();
            updateGL();
            GLES20.glTexParameteri(3553, 10241, 9728);
            GLException.checkError();
            GLES20.glTexParameteri(3553, 10240, 9728);
            GLException.checkError();
            this.glBinding = textureId[0];
            this.bound = true;
            Log.d("plasma", "Texture bound to " + this.glBinding);
        }
    }

    public synchronized void updateGL() {
        GLES20.glActiveTexture(33984 + this.id);
        GLException.checkError();
        GLES20.glTexImage2D(3553, 0, 6409, roundUpPowerOfTwo(width()), roundUpPowerOfTwo(height()), 0, 6409, glType(), data());
        GLException.checkError();
    }

    public void finalize() {
        GLES20.glDeleteTextures(1, new int[]{this.glBinding}, 0);
        GLException.checkError();
    }
}
