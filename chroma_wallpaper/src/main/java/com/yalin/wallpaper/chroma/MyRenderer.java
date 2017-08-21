package com.yalin.wallpaper.chroma;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    private Context context;
    protected ChromaBackground mBackground;
    private Sprite mSprites;

    public MyRenderer(Context context) {
        this.context = context;
    }

    public void close() {
        if (mBackground != null) {
            mBackground.close(context);
        }
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        if (mBackground == null) {
            try {
                mBackground = new ChromaBackground(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mSprites == null) {
            mSprites = new Sprite(context);
        }
    }

    public void onDrawFrame(GL10 unused) {
        mBackground.draw();
        mSprites.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mBackground.displayWidth = width;
    }

    public void onTouchEvent(MotionEvent event) {
        if (mBackground != null) {
            mBackground.motionEvent = event;
        }
    }

}

class Sprite {

    public Sprite(Context context) {

    }

    public void draw() {

    }

}

class ChromaBackground {

    private final int mProgramHandle;
    private final int mPositionHandle;
    private final int mTimeHandle;
    private final int mTouchHandle;
    private final int mColorSwathHandle;
    private final int mNoiseHandle;

    public MotionEvent motionEvent;
    private int frameNum;
    public int displayWidth;

    private final int COORDS_PER_VERTEX = 3;
    private final int TESSELATION_FACTOR = 10;
    private final int BYTES_PER_FLOAT = 4;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;
    private final int vertexCount;
    private final int coordinateHandle[] = new int[1];

    public ChromaBackground(Context context) throws Exception {

        // Copy the geometry to GPU memory
        vertexCount = LoadGeometry(coordinateHandle);

        // Compile and link shader programs
        final String vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.chroma_v);
        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        if (vertexShaderHandle == 0) // Shader compilation failed.
        {
            throw new Exception("Vertex shader compilation failed.", null);
        }

        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.chroma_f);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        if (fragmentShaderHandle == 0) // Shader compilation failed.
        {
            throw new Exception("Fragment shader compilation failed.", null);
        }

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"u_Time", "a_Position", "u_ColorSwath", "u_Noise"});
        if (mProgramHandle == 0) // Shader compilation failed.
        {
            throw new Exception("Shader compilation failed during linking.", null);
        }

        // Store handles to attributes and uniforms
        mColorSwathHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_ColorSwath");
        mNoiseHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Noise");
        mTimeHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Time");
        mTouchHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Touch");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");

        // Load textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        int selectedColor = R.drawable.swath_chroma;
        TextureHelper.loadTexture(context, selectedColor, mColorSwathHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mColorSwathHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        TextureHelper.loadTexture(context, R.drawable.noise, mNoiseHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mNoiseHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        // Initialize frameNumber to last known or random
        frameNum = (int) (65535.0 * Math.random());
    }

    private int LoadGeometry(int[] buffers) {

        float[] coordinates = new float[2 * TESSELATION_FACTOR * COORDS_PER_VERTEX];
        for (int i = 0; i < TESSELATION_FACTOR; i++) {
            // Bottom
            coordinates[6 * i + 0] = 2.0f * i / (TESSELATION_FACTOR - 1.0f) - 1.0f;
            coordinates[6 * i + 1] = -1.0f;
            coordinates[6 * i + 2] = 0.0f;

            // Top
            coordinates[6 * i + 3] = 2.0f * i / (TESSELATION_FACTOR - 1.0f) - 1.0f;
            coordinates[6 * i + 4] = 1.0f;
            coordinates[6 * i + 5] = 0.0f;
        }

        final FloatBuffer mBuffer = ByteBuffer.allocateDirect(coordinates.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mBuffer.put(coordinates, 0, coordinates.length);
        mBuffer.position(0);

        GLES20.glGenBuffers(1, buffers, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mBuffer
                .capacity() * BYTES_PER_FLOAT, mBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        mBuffer.limit(0);

        return 2 * TESSELATION_FACTOR;
    }

    public void close(Context context) {
    }

    public void draw() {

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgramHandle);

        // Specify rendering settings
        GLES20.glEnable(GLES20.GL_DITHER);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glFrontFace(GLES20.GL_CW);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_BLEND);

        // Pass the current frame number
        if (++frameNum >= 65535) // Wrap to GLSL highp int
        {
            frameNum = -65535;
        }
        GLES20.glUniform1i(mTimeHandle, frameNum);

        // Pass in touches
        if (motionEvent == null) {
            GLES20.glUniform4f(mTouchHandle, -1.0f, -1.0f, -1.0f, -1.0f);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            motionEvent = null;
            GLES20.glUniform4f(mTouchHandle, -1.0f, -1.0f, -1.0f, -1.0f);
        } else {
            switch (motionEvent.getPointerCount()) {
                case 1:
                    GLES20.glUniform4f(mTouchHandle, motionEvent.getX(0) / displayWidth, -1.0f, -1.0f, -1.0f);
                    break;
                case 2:
                    GLES20.glUniform4f(mTouchHandle, motionEvent.getX(0) / displayWidth,
                            motionEvent.getX(1) / displayWidth, -1.0f, -1.0f);
                    break;
                case 3:
                    GLES20.glUniform4f(mTouchHandle, motionEvent.getX(0) / displayWidth,
                            motionEvent.getX(1) / displayWidth, motionEvent.getX(2) / displayWidth, -1.0f);
                    break;
                default:
                    GLES20.glUniform4f(mTouchHandle, motionEvent.getX(0) / displayWidth,
                            motionEvent.getX(1) / displayWidth, motionEvent.getX(2) / displayWidth,
                            motionEvent.getX(3) / displayWidth);
            }
        }

        // Pass in the texture information
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mColorSwathHandle, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mColorSwathHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glUniform1i(mNoiseHandle, 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mNoiseHandle);

        // Pass in the position information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, coordinateHandle[0]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, VERTEX_STRIDE, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Draw the triangles
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
