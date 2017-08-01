package com.yalin.livewallpaper3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

/**
 * Created by Blake on 4/15/2016.
 */
public class Clouds
{
// Geometric variables
    public static float vertices[];
    public short indices[];
    public static float uvs[];
    float xMovement = 0.0f;
    float maxX = 3.0f;
    float minX = -3.0f;
    float[] offsets;
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer colorBuffer;
    //    public FloatBuffer uvBuffer;
    private int mProgram;
    Sprite[] clouds;
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    public static float colors[];

    public Clouds(float[][] mVerticesArray, float[] textureColor, short[] mIndices)
    {
        float minX = -3.0f;
        float maxX = 3.0f;
        offsets = new float[mVerticesArray.length];
        Random rnd = new Random();
        clouds = new Sprite[mVerticesArray.length];
        for(int i = 0; i < mVerticesArray.length; i++)
        {
            clouds[i] = new Sprite(mVerticesArray[i], textureColor, mIndices);
            offsets[i] = rnd.nextFloat() * (maxX - minX) + minX;
        }
    }

    public void draw(float[][] m, FloatBuffer uvBuffer[], int textureIndex,float[] mModelMatrix,
                     float[] mtrxView, float[] mtrxProjection, float xOffset, float yOffset)
    {
        for(int i = 0; i < clouds.length; i++)
        {
            Matrix.setIdentityM(mModelMatrix, 0);
            offsets[i] += 0.003f;
            float newOffset;
            if(offsets[i] > maxX)
            {
                offsets[i] = minX;
            }
            newOffset = xOffset * 0.1f + offsets[i];
            Matrix.translateM(mModelMatrix, 0, newOffset, yOffset * 0.1f, 1.0f);
            Matrix.multiplyMM(m[i], 0, mtrxView, 0, mModelMatrix, 0);
            Matrix.multiplyMM(m[i], 0, mtrxProjection, 0, m[i], 0);
            clouds[i].draw(m[i], uvBuffer[i],textureIndex);
        }
    }

    public void setColor(float[] textureColor)
    {
        for(int i = 0; i < clouds.length; i++)
        {
            clouds[i].setColor(textureColor);
        }
    }


}
