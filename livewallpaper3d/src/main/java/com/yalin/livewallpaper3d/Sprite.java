package com.yalin.livewallpaper3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Created by Blake Hashimoto on 8/15/2015.
 */
public class Sprite {
    // Geometric variables
    public static float vertices[];
    public short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer colorBuffer;
//    public FloatBuffer uvBuffer;
    private final int mProgram;
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    public static float colors[];

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    //    //Added for Textures
//    private final FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private final int mTextureCoordinateDataSize = 2;
    private int mTextureDataHandle;

    public Sprite(float[] mVertices, float[] textureColor, short[] mIndices)
    {
        vertices = mVertices;
        colors = textureColor;
        indices = mIndices;

        ByteBuffer cb = ByteBuffer.allocateDirect(colors.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_Image);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.fs_Image);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

//        setupImage(context, bitmapAddress, texturenames);
    }

    public void draw(float[] m, FloatBuffer uvBuffer, int textureIndex ) {
        //set the program
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_Color");
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mColorHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

            // Get handle to texture coordinates location
            int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord");

            // Enable generic vertex attribute array
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);

            // Prepare the texture coordinates
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

            // Get handle to shape's transformation matrix
            int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

            // Get handle to textures locations
            int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "s_texture");

            // Set the sampler texture unit to x, where we have saved the texture.
            GLES20.glUniform1i(mSamplerLoc, textureIndex);

        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, m, 0);

        //enable transparency
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

//        GLES20.glClearColor(0, 0, 0, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisable(GLES20.GL_BLEND);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

        if(textureIndex >= 0)
            GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    public void setColor(float[] textureColor)
    {
        ByteBuffer cb = ByteBuffer.allocateDirect(colors.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(textureColor);
        colorBuffer.position(0);
    }

    public void setVertices(float[] vertices)
    {
        this.vertices = vertices;
        vertexBuffer.put(this.vertices);
        vertexBuffer.position(0);
    }
}
