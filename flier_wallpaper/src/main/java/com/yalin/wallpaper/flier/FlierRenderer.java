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

package com.yalin.wallpaper.flier;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.widget.Toast;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main renderer class.
 */
public final class FlierRenderer implements GLSurfaceView.Renderer {

  // Holder for background colors.
  private FloatBuffer mBufferBgColors;
  // Vertices for full view rendering.
  private ByteBuffer mBufferVertices;
  // Application context.
  private Context mContext;
  // Clouds rendering class.
  private final FlierClouds mFlierClouds = new FlierClouds();
  // Fbo for offscreen rendering.
  private final FlierFbo mFlierFbo = new FlierFbo();
  // Plane rendering class.
  private final FlierPlane mFlierPlane = new FlierPlane();
  // Waves rendering class.
  private final FlierWaves mFlierWaves = new FlierWaves();
  // Brightness preference.
  private float mPreferenceBrightness;
  // Render quality preference.
  private int mPreferenceQuality;
  // Boolean to indicate preferences have changed.
  private boolean mPreferencesChanged;
  // Flag for indicating whether shader compiler is supported.
  private final boolean[] mShaderCompilerSupported = new boolean[1];
  // Shader for copying offscreen texture on screen.
  private final FlierShader mShaderCopy = new FlierShader();
  // Shader for rendering background gradient.
  private final FlierShader mShaderFill = new FlierShader();
  // Surface/screen dimensions.
  private int mWidth, mHeight;

  /**
   * Default constructor.
   *
   * @param context Context to read shaders from.
   */
  public FlierRenderer(Context context) {
    mContext = context;

    // Create full scene quad buffer.
    mBufferVertices = ByteBuffer.allocateDirect(4 * 2);
    mBufferVertices.put(FlierConstants.FULL_QUAD_COORDS).position(0);

    // Create background color float buffer.
    ByteBuffer bBuf = ByteBuffer.allocateDirect(3 * 4 * 4);
    mBufferBgColors = bBuf.order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

  /**
   * Loads three component RGB values from preferences.
   *
   * @param resId Color preference key resource id.
   * @param preferences Preferences to load value from.
   * @return Three element float RGB array.
   */
  private float[] loadColor(int resId, SharedPreferences preferences) {
    String key = mContext.getString(resId);
    int color = preferences.getInt(key, 0);
    float[] retVal = new float[3];
    retVal[0] = (float) Color.red(color) / 255;
    retVal[1] = (float) Color.green(color) / 255;
    retVal[2] = (float) Color.blue(color) / 255;
    return retVal;
  }

  @Override
  public void onDrawFrame(GL10 unused) {
    // If shader compiler is not supported, clear screen buffer only.
    if (mShaderCompilerSupported[0] == false) {
      GLES20.glClearColor(0, 0, 0, 1);
      GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
      return;
    }

    // If preferences have changed.
    if (mPreferencesChanged) {
      int width = mWidth;
      int height = mHeight;
      switch (mPreferenceQuality) {
        case 0:
          width /= 3;
          height /= 3;
          break;
        case 1:
          width /= 2;
          height /= 2;
          break;
      }
      mFlierFbo.init(width, height, 1, true, true);
      mFlierWaves.onSurfaceChanged(width, height);
      mFlierPlane.onSurfaceChanged(width, height);
      mFlierClouds.onSurfaceChanged(width, height);
      mPreferencesChanged = false;
    }

    // Disable unneeded rendering flags.
    GLES20.glDisable(GLES20.GL_CULL_FACE);
    GLES20.glDisable(GLES20.GL_BLEND);
    GLES20.glDisable(GLES20.GL_DEPTH_TEST);

    // Set render target to fbo.
    mFlierFbo.bind();
    mFlierFbo.bindTexture(0);
    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
        | GLES20.GL_STENCIL_BUFFER_BIT);

    // Render background gradient.
    mShaderFill.useProgram();
    int positionAttribLocation = mShaderFill.getHandle("aPosition");
    GLES20.glVertexAttribPointer(positionAttribLocation, 2, GLES20.GL_BYTE,
        false, 0, mBufferVertices);
    GLES20.glEnableVertexAttribArray(positionAttribLocation);
    int colorAttribLocation = mShaderFill.getHandle("aColor");
    GLES20.glVertexAttribPointer(colorAttribLocation, 3, GLES20.GL_FLOAT,
        false, 0, mBufferBgColors);
    GLES20.glEnableVertexAttribArray(colorAttribLocation);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

    // Render actual scene.
    mFlierWaves.onDrawFrame();
    mFlierPlane.onDrawFrame();
    mFlierClouds.onDrawFrame();

    // Copy FBO to screen buffer.
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    GLES20.glViewport(0, 0, mWidth, mHeight);
    mShaderCopy.useProgram();
    int uBrightness = mShaderCopy.getHandle("uBrightness");
    int aPosition = mShaderCopy.getHandle("aPosition");
    GLES20.glUniform1f(uBrightness, mPreferenceBrightness);
    GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
        mBufferVertices);
    GLES20.glEnableVertexAttribArray(aPosition);
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFlierFbo.getTexture(0));
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
    mPreferencesChanged = true;
  }

  @Override
  public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    // Check if shader compiler is supported.
    GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER,
        mShaderCompilerSupported, 0);

    // If not, show user an error message and return immediately.
    if (mShaderCompilerSupported[0] == false) {
      Handler handler = new Handler(mContext.getMainLooper());
      handler.post(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(mContext, R.string.error_shader_compiler,
              Toast.LENGTH_LONG).show();
        }
      });
      return;
    }

    mShaderCopy.setProgram(mContext.getString(R.string.shader_copy_vs),
        mContext.getString(R.string.shader_copy_fs));
    mShaderFill.setProgram(mContext.getString(R.string.shader_fill_vs),
        mContext.getString(R.string.shader_fill_fs));
    mFlierWaves.onSurfaceCreated(mContext);
    mFlierPlane.onSurfaceCreated(mContext);
    mFlierClouds.onSurfaceCreated(mContext);
  }

  /**
   * Updates rendering values from preferences.
   *
   * @param preferences Preferences values.
   */
  public void setPreferences(SharedPreferences preferences) {
    mPreferenceQuality = 1;
    mPreferenceBrightness = 0.8f;

    float[] bgColorTop, bgColorBottom, waveColorFront, waveColorBack,
        planeColor, planeOutlineColor, cloudColor, cloudOutlineColor;

    bgColorTop = FlierConstants.SCHEME_BLUE_BG_TOP;
    bgColorBottom = FlierConstants.SCHEME_BLUE_BG_BOTTOM;
    waveColorFront = FlierConstants.SCHEME_BLUE_WAVE_FRONT;
    waveColorBack = FlierConstants.SCHEME_BLUE_WAVE_BACK;
    planeColor = FlierConstants.SCHEME_BLUE_PLANE;
    planeOutlineColor = FlierConstants.SCHEME_BLUE_PLANE_OUTLINE;
    cloudColor = FlierConstants.SCHEME_BLUE_CLOUD;
    cloudOutlineColor = FlierConstants.SCHEME_BLUE_CLOUD_OUTLINE;

    mBufferBgColors.put(bgColorTop).put(bgColorBottom).put(bgColorTop)
        .put(bgColorBottom).position(0);
    mFlierWaves.setColors(waveColorFront, waveColorBack);
    mFlierPlane.setColor(planeColor, planeOutlineColor);
    mFlierClouds.setColors(cloudColor, cloudOutlineColor);

    mPreferencesChanged = true;
  }

  /**
   * Sets x offset for clouds. Offset is expected to be a value between [0,
   * 1].
   *
   * @param xOffset New x offset value.
   */
  public void setXOffset(float xOffset) {
    mFlierWaves.setXOffset(xOffset);
    mFlierClouds.setXOffset(xOffset);
  }

}
