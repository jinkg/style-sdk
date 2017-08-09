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
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import java.nio.ByteBuffer;

/**
 * Class for handling wave movement and rendering.
 */
public final class FlierWaves {

	// Texture shader for rendering actual waves.
	private final FlierShader mShaderWave = new FlierShader();
	// Point shader for rendering wave texture.
	private final FlierShader mShaderWaveTexture = new FlierShader();
	// Screen vertices.
	private ByteBuffer mVertices;
	// Front and back wave colors.
	private float[] mWaveColorFront = new float[3],
			mWaveColorBack = new float[3];
	// FBO for rendering wave texture into.
	private final FlierFbo mWaveFbo = new FlierFbo();
	// View width, height and wave texture size.
	private int mWidth, mHeight, mWaveSize;
	// X offset received from wallpaper scrolling.
	private float mXOffset;

	/**
	 * Default constructor.
	 */
	public FlierWaves() {
		mVertices = ByteBuffer.allocateDirect(4 * 2);
		mVertices.put(FlierConstants.FULL_QUAD_COORDS).position(0);
	}

	/**
	 * Called from renderer for rendering paper plane into the scene.
	 */
	public void onDrawFrame() {
		mShaderWave.useProgram();

		int uPositionOffset = mShaderWave.getHandle("uPositionOffset");
		int uTextureSize = mShaderWave.getHandle("uTextureSize");
		int uColor = mShaderWave.getHandle("uColor");
		int aPosition = mShaderWave.getHandle("aPosition");

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWaveFbo.getTexture(0));

		int width = mWidth;
		int height = mHeight / 4;
		long time = SystemClock.uptimeMillis();
		float dx1 = sin(time, 2000, .2f) + .2f - mXOffset;
		float dx2 = sin(time, 2345, .2f) + .2f - mXOffset;
		float dy1 = sin(time, 5000, .2f) - .2f;
		float dy2 = sin(time, 5234, .2f) - .4f;

		GLES20.glViewport(0, 0, width, height);

		GLES20.glUniform2f(uPositionOffset, dx1, dy1);
		GLES20.glUniform2f(uTextureSize, (float) width / mWaveSize,
				(float) height / mWaveSize);
		GLES20.glUniform3fv(uColor, 1, mWaveColorBack, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0x01, 0xFFFFFFFF);
		GLES20.glStencilOp(GLES20.GL_REPLACE, GLES20.GL_REPLACE,
				GLES20.GL_REPLACE);
		GLES20.glUniform2f(uPositionOffset, dx2, dy2);
		GLES20.glUniform3fv(uColor, 1, mWaveColorFront, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		GLES20.glDisable(GLES20.GL_STENCIL_TEST);

		GLES20.glViewport(0, 0, mWidth, mHeight);
	}

	/**
	 * Called from renderer once surface has changed.
	 * 
	 * @param width
	 *            Width in pixels.
	 * @param height
	 *            Height in pixels.
	 */
	public void onSurfaceChanged(int width, int height) {
		mWidth = width;
		mHeight = height;
		mWaveSize = Math.min(width, height) / 5;

		mWaveFbo.init(mWaveSize, mWaveSize, 1);
		mWaveFbo.bind();
		mWaveFbo.bindTexture(0);
		GLES20.glClearColor(1f, 0f, 0f, 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_REPEAT);

		mShaderWaveTexture.useProgram();
		int uModelViewProjM = mShaderWaveTexture.getHandle("uModelViewProjM");
		int uPointPosition = mShaderWaveTexture.getHandle("uPointPosition");
		int uPointSize = mShaderWaveTexture.getHandle("uPointSize");
		int uAspectRatio = mShaderWaveTexture.getHandle("uAspectRatio");
		int uBrightness = mShaderWaveTexture.getHandle("uBrightness");
		int aPosition = mShaderWaveTexture.getHandle("aPosition");

		float[] mvpM = new float[16];
		Matrix.setIdentityM(mvpM, 0);
		GLES20.glUniformMatrix4fv(uModelViewProjM, 1, false, mvpM, 0);
		GLES20.glUniform3f(uPointPosition, 0f, -1f, 0f);
		GLES20.glUniform2f(uAspectRatio, 1f, 1f);
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glUniform1f(uPointSize, 1.075f);
		GLES20.glUniform1f(uBrightness, .6f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glUniform1f(uPointSize, 1f);
		GLES20.glUniform1f(uBrightness, .0f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	/**
	 * Called from renderer once surface has been created.
	 * 
	 * @param ctx
	 *            Context to read shaders from.
	 */
	public void onSurfaceCreated(Context ctx) {
		mWaveFbo.reset();
		mShaderWaveTexture.setProgram(ctx.getString(R.string.shader_point_vs),
				ctx.getString(R.string.shader_wave_texture_fs));
		mShaderWave.setProgram(ctx.getString(R.string.shader_wave_vs),
				ctx.getString(R.string.shader_wave_fs));
	}

	/**
	 * Sets wave colors.
	 * 
	 * @param waveColorFront
	 *            Three float RGB array.
	 * @param waveColorBack
	 *            Three float RGB arrat.
	 */
	public void setColors(float[] waveColorFront, float[] waveColorBack) {
		mWaveColorFront = waveColorFront;
		mWaveColorBack = waveColorBack;
	}

	/**
	 * Sets x offset for clouds. Offset is expected to be a value between [0,
	 * 1].
	 * 
	 * @param xOffset
	 *            New x offset value.
	 */
	public void setXOffset(float xOffset) {
		mXOffset = xOffset * 2f;
	}

	/**
	 * Calculates sin value for timed position.
	 * 
	 * @param time
	 *            Current time.
	 * @param frequency
	 *            Time between full 360 degree cycle in millis.
	 * @param multiplier
	 *            Multiplier for calculating return value sin(x) * multiplier.
	 * @return Value between [-multiplier, multiplier].
	 */
	private float sin(long time, long frequency, float multiplier) {
		return multiplier
				* (float) Math.sin((2 * Math.PI * (time % frequency))
						/ frequency);
	}

}
