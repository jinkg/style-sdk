package com.yalin.wallpaper.forest.core.gl;

import android.opengl.GLES20;
import java.nio.FloatBuffer;

public class Program {
    private static int programInUse = 0;
    public int aColor;
    public int aPosition;
    public int aSize;
    public int aTexCoord;
    public int handle = GLES20.glCreateProgram();
    public int pointsize;
    public int uMVPMatrix;
    public int uTexture;
    public int useSizeBuffer;

    public Program(int vertexShaderHandle, int fragmentShaderHandle) {
        if (this.handle != 0) {
            GLES20.glAttachShader(this.handle, vertexShaderHandle);
            GLES20.glAttachShader(this.handle, fragmentShaderHandle);
            GLES20.glLinkProgram(this.handle);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(this.handle, 35714, linkStatus, 0);
            if (linkStatus[0] == 0) {
                throw new RuntimeException("Error compiling program: " + GLES20.glGetProgramInfoLog(this.handle));
            }
            this.pointsize = GLES20.glGetUniformLocation(this.handle, "pointsize");
            this.useSizeBuffer = GLES20.glGetUniformLocation(this.handle, "useSizeBuffer");
            this.uMVPMatrix = GLES20.glGetUniformLocation(this.handle, "uMVPMatrix");
            this.aPosition = GLES20.glGetAttribLocation(this.handle, "aPosition");
            this.aColor = GLES20.glGetAttribLocation(this.handle, "aColor");
            this.aSize = GLES20.glGetAttribLocation(this.handle, "aSize");
            this.uTexture = GLES20.glGetAttribLocation(this.handle, "uTexture");
            this.aTexCoord = GLES20.glGetAttribLocation(this.handle, "aTexCoord");
            return;
        }
        throw new RuntimeException("Failed to create program");
    }

    public void use() {
        if (programInUse != this.handle) {
            GLES20.glUseProgram(this.handle);
            programInUse = this.handle;
        }
    }

    public void drawArrays(int mode, FloatBuffer vertices, FloatBuffer colors, FloatBuffer sizes, int vertexCount, float[] matrix) {
        GLES20.glUniformMatrix4fv(this.uMVPMatrix, 1, false, matrix, 0);
        if (sizes != null) {
            GLES20.glUniform1i(this.useSizeBuffer, 1);
            GLES20.glVertexAttribPointer(this.aSize, 1, 5126, false, 0, sizes);
            GLES20.glEnableVertexAttribArray(this.aSize);
        } else {
            GLES20.glUniform1i(this.useSizeBuffer, 0);
            GLES20.glDisableVertexAttribArray(this.aSize);
        }
        GLES20.glVertexAttribPointer(this.aPosition, 2, 5126, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(this.aPosition);
        GLES20.glVertexAttribPointer(this.aColor, 4, 5126, false, 0, colors);
        GLES20.glEnableVertexAttribArray(this.aColor);
        GLES20.glDrawArrays(mode, 0, vertexCount);
    }
}
