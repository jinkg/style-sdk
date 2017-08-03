/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.yalin.wallpaper.flower;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main renderer class.
 */
public final class FlowerRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "FlowerRenderer";
    // Buffer for background colors.
    private FloatBuffer mBackgroundColors;
    // Current context.
    private Context mContext;
    // FBO for offscreen rendering.
    private FlowerFbo mFlowerFbo = new FlowerFbo();
    // Actual flower renderer instance.
    private FlowerObjects mFlowerObjects = new FlowerObjects();
    // "Final" calculated offset value.
    private final PointF mOffset = new PointF();
    // Scroll offset value.
    private final PointF mOffsetScroll = new PointF();
    // Additional animated offset source and destination values.
    private PointF mOffsetSrc = new PointF(), mOffsetDst = new PointF();
    // Animated offset time value for iterating between src and dst.
    private long mOffsetTime;
    // Vertex buffer for full scene coordinates.
    private ByteBuffer mScreenVertices;
    // Shader for rendering background gradient.
    private final FlowerShader mShaderBackground = new FlowerShader();
    // Flag for indicating whether shader compiler is supported.
    private final boolean[] mShaderCompilerSupported = new boolean[1];
    // Shader for copying offscreen texture on screen.
    private final FlowerShader mShaderCopy = new FlowerShader();
    // Surface/screen dimensions.
    private int mWidth, mHeight;

    /**
     * Default constructor.
     */
    public FlowerRenderer(Context context) {
        mContext = context;

        // Create screen coordinates buffer.
        final byte SCREEN_COORDS[] = {-1, 1, -1, -1, 1, 1, 1, -1};
        mScreenVertices = ByteBuffer.allocateDirect(2 * 4);
        mScreenVertices.put(SCREEN_COORDS).position(0);

        // Create background color float buffer.
        ByteBuffer bBuf = ByteBuffer.allocateDirect(4 * 4 * 4);
        mBackgroundColors = bBuf.order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     * Retrieves color value from preferences with given key.
     */
    private float[] getColor(int keyId, SharedPreferences prefs) {
        int value = Color.CYAN;
        float[] retVal = new float[4];
        retVal[0] = (float) Color.red(value) / 255;
        retVal[1] = (float) Color.green(value) / 255;
        retVal[2] = (float) Color.blue(value) / 255;
        retVal[3] = (float) Color.alpha(value) / 255;
        return retVal;
    }

    @Override
    public synchronized void onDrawFrame(GL10 unused) {
        // If shader compiler is not supported, clear screen buffer only.
        if (mShaderCompilerSupported[0] == false) {
            GLES20.glClearColor(0, 0, 0, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            return;
        }

        // Update offset.
        long time = SystemClock.uptimeMillis();
        // If time passed generate new target.
        if (time - mOffsetTime > 5000) {
            mOffsetTime = time;
            mOffsetSrc.set(mOffsetDst);
            mOffsetDst.x = -1f + (float) (Math.random() * 2f);
            mOffsetDst.y = -1f + (float) (Math.random() * 2f);
        }
        // Calculate final offset values.
        float t = (float) (time - mOffsetTime) / 5000;
        t = t * t * (3 - 2 * t);
        mOffset.x = mOffsetScroll.x + mOffsetSrc.x + t
                * (mOffsetDst.x - mOffsetSrc.x);
        mOffset.y = mOffsetScroll.y + mOffsetSrc.y + t
                * (mOffsetDst.y - mOffsetSrc.y);

        // Disable unneeded rendering flags.
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // Set render target to fbo.
        mFlowerFbo.bind();
        mFlowerFbo.bindTexture(0);

        // Render background gradient.
        mShaderBackground.useProgram();
        int uAspectRatio = mShaderBackground.getHandle("uAspectRatio");
        int uOffset = mShaderBackground.getHandle("uOffset");
        int uLineWidth = mShaderBackground.getHandle("uLineWidth");
        int aPosition = mShaderBackground.getHandle("aPosition");
        int aColor = mShaderBackground.getHandle("aColor");

        float aspectX = (float) Math.min(mWidth, mHeight) / mHeight;
        float aspectY = (float) Math.min(mWidth, mHeight) / mWidth;
        GLES20.glUniform2f(uAspectRatio, aspectX, aspectY);
        GLES20.glUniform2f(uOffset, mOffset.x, mOffset.y);
        GLES20.glUniform2f(uLineWidth, aspectX * 40f / mFlowerFbo.getWidth(),
                aspectY * 40f / mFlowerFbo.getHeight());
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
                mScreenVertices);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, 0,
                mBackgroundColors);
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        // Render scene.
        mFlowerObjects.onDrawFrame(mOffset);

        // Copy FBO to screen buffer.
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, mWidth, mHeight);
        mShaderCopy.useProgram();
        aPosition = mShaderCopy.getHandle("aPosition");
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
                mScreenVertices);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFlowerFbo.getTexture(0));
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // If shader compiler is not supported set viewport size only.
        if (mShaderCompilerSupported[0] == false) {
            GLES20.glViewport(0, 0, width, height);
            return;
        }

        mWidth = width;
        mHeight = height;
        mFlowerFbo.init(mWidth, mHeight, 1);
        mFlowerObjects.onSurfaceChanged(mFlowerFbo.getWidth(),
                mFlowerFbo.getHeight());
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Check if shader compiler is supported.
        GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER,
                mShaderCompilerSupported, 0);

        // If not, show user an error message and return immediately.
        if (!mShaderCompilerSupported[0]) {
            Log.e(TAG, "Shader compile error");
            return;
        }

        mShaderCopy.setProgram(mContext.getString(R.string.shader_copy_vs),
                mContext.getString(R.string.shader_copy_fs));
        mShaderBackground.setProgram(
                mContext.getString(R.string.shader_background_vs),
                mContext.getString(R.string.shader_background_fs));
        mFlowerObjects.onSurfaceCreated(mContext);
    }

    /**
     * Sets scroll offset. Called from wallpaper engine once user scrolls
     * between home screens.
     *
     * @param xOffset Offset value between [0, 1].
     * @param yOffset Offset value between [0, 1]
     */
    public void setOffset(float xOffset, float yOffset) {
        mOffsetScroll.set(xOffset * 2f, yOffset * 2f);
    }

    /**
     * Updates preference values from provided ShaderPrefence instance.
     *
     * @param prefs New preferences.
     */
    public synchronized void setPreferences(SharedPreferences prefs) {
        // Get general preferences values.
        int flowerCount = 2;
        int splineQuality = 10;
        float branchPropability = 0.5f;
        float zoomLevel = 0.4f;

        // Get color preference values.
        int colorScheme = 1;
        float bgTop[], bgBottom[], flowerColors[][] = new float[2][];
        switch (colorScheme) {
            case 1:
                bgTop = FlowerConstants.SCHEME_SUMMER_BG_TOP;
                bgBottom = FlowerConstants.SCHEME_SUMMER_BG_BOTTOM;
                flowerColors[0] = FlowerConstants.SCHEME_SUMMER_PLANT_1;
                flowerColors[1] = FlowerConstants.SCHEME_SUMMER_PLANT_2;
                break;
            case 2:
                bgTop = FlowerConstants.SCHEME_AUTUMN_BG_TOP;
                bgBottom = FlowerConstants.SCHEME_AUTUMN_BG_BOTTOM;
                flowerColors[0] = FlowerConstants.SCHEME_AUTUMN_PLANT_1;
                flowerColors[1] = FlowerConstants.SCHEME_AUTUMN_PLANT_2;
                break;
            case 3:
                bgTop = FlowerConstants.SCHEME_WINTER_BG_TOP;
                bgBottom = FlowerConstants.SCHEME_WINTER_BG_BOTTOM;
                flowerColors[0] = FlowerConstants.SCHEME_WINTER_PLANT_1;
                flowerColors[1] = FlowerConstants.SCHEME_WINTER_PLANT_2;
                break;
            case 4:
                bgTop = FlowerConstants.SCHEME_SPRING_BG_TOP;
                bgBottom = FlowerConstants.SCHEME_SPRING_BG_BOTTOM;
                flowerColors[0] = FlowerConstants.SCHEME_SPRING_PLANT_1;
                flowerColors[1] = FlowerConstants.SCHEME_SPRING_PLANT_2;
                break;
            default:
                bgTop = getColor(0, prefs);
                bgBottom = getColor(0, prefs);
                flowerColors[0] = getColor(0, prefs);
                flowerColors[1] = getColor(0, prefs);
                break;
        }

        mBackgroundColors.put(bgTop).put(bgBottom).put(bgTop).put(bgBottom)
                .position(0);
        mFlowerObjects.setPreferences(flowerCount, flowerColors, splineQuality,
                branchPropability, zoomLevel);
    }

}
