package com.yalin.livewallpaper3d;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by User on 3/2/2016.
 */
public class Square {
    // Geometric variables
    public static float vertices[];
    public static short indices[] ={0, 1, 2, 0, 2, 3};
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public FloatBuffer colorBuffer;
    public ShortBuffer drawListBuffer;
    //    public FloatBuffer uvBuffer;
    private final int mProgram;
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.3f };
    public static float colors[];

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    public Square(float[] mVertices, float[] textureColor)
    {
        vertices = mVertices;
        colors = textureColor;

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
                riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.fs_SolidColor);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    public void draw(float[] m) {
        //set the program
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        // Get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, m, 0);

        // get handle to vertex shader's vPosition member
        mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vColor");
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mColorHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

//        GLES20.glClearColor(0, 0, 0, 1.0f);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
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
