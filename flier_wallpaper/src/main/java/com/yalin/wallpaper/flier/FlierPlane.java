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
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Class for handling paper plane animation and rendering.
 */
public final class FlierPlane {
	// FBO aspect ratio.
	private float mAspectRatio;
	// Buffer for line indices.
	private ByteBuffer mBufferLineIndices;
	// Vertices buffer.
	private FloatBuffer mBufferVertices;
	// Outline line width;
	private int mLineWidth;
	// Plane color.
	private float[] mPlaneColor = new float[3],
			mPlaneOutlineColor = new float[3];
	// Projection and view matrices.
	private final float[] mProjM = new float[16], mViewM = new float[16];
	// Plane shader used for rendering both lines and surfaces.
	private final FlierShader mShaderPlane = new FlierShader();

	/**
	 * Default constructor.
	 */
	public FlierPlane() {
		ByteBuffer bBuffer = ByteBuffer.allocateDirect(6 * 3 * 4);
		mBufferVertices = bBuffer.order(ByteOrder.nativeOrder())
				.asFloatBuffer();

		final float WIDTH = 1f, HEIGHT = 0.3f, LENGTH = 1.2f, BEND = 0.3f;
		final float[] vertices = { 0f, HEIGHT, -LENGTH, WIDTH, HEIGHT, LENGTH,
				BEND, HEIGHT, LENGTH, 0f, -HEIGHT, LENGTH, -BEND, HEIGHT,
				LENGTH, -WIDTH, HEIGHT, LENGTH };
		mBufferVertices.put(vertices).position(0);

		mBufferLineIndices = ByteBuffer.allocateDirect(9 * 2);
		final byte[] indices = { 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 1, 2, 2, 3, 3,
				4, 4, 5 };
		mBufferLineIndices.put(indices).position(0);
	}

	/**
	 * Called from renderer for rendering paper plane into the scene.
	 */
	public void onDrawFrame() {
		long time = SystemClock.uptimeMillis();
		float rx = sin(time, 4000, 2f) * mAspectRatio;
		float rz = sin(time, 6234, 2f) * mAspectRatio;
		float ry = (float) (time % (360 * 60)) / 60;
		float scale = (0.15f + sin(time, 8345, .025f)) * mAspectRatio;

		final float[] modelViewProjM = new float[16];
		Matrix.setRotateM(modelViewProjM, 0, rx, 1f, 0, 0);
		Matrix.rotateM(modelViewProjM, 0, ry, 0, 1f, 0);
		Matrix.rotateM(modelViewProjM, 0, rz, 0, 0, 1f);

		Matrix.translateM(modelViewProjM, 0, 1f, -mAspectRatio / 5f, 0f);
		Matrix.scaleM(modelViewProjM, 0, scale, scale, scale);

		Matrix.multiplyMM(modelViewProjM, 0, mViewM, 0, modelViewProjM, 0);
		Matrix.multiplyMM(modelViewProjM, 0, mProjM, 0, modelViewProjM, 0);

		mShaderPlane.useProgram();
		int uModelViewProjM = mShaderPlane.getHandle("uModelViewProjM");
		int uColor = mShaderPlane.getHandle("uColor");
		int uAlpha = mShaderPlane.getHandle("uAlpha");
		int aPosition = mShaderPlane.getHandle("aPosition");
		GLES20.glUniformMatrix4fv(uModelViewProjM, 1, false, modelViewProjM, 0);
		GLES20.glUniform1f(uAlpha, 1f);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false,
				3 * 4, mBufferVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LESS);
		GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0x01, 0xFFFFFFFF);
		GLES20.glStencilOp(GLES20.GL_REPLACE, GLES20.GL_REPLACE,
				GLES20.GL_REPLACE);

		// Render filled polygons.
		GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
		GLES20.glPolygonOffset(1f, 1f);
		GLES20.glUniform3fv(uColor, 1, mPlaneColor, 0);
		GLES20.glUniform1f(uAlpha, 1f);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
		GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);

		// Render sharp outlines.
		GLES20.glLineWidth(mLineWidth);
		GLES20.glUniform3fv(uColor, 1, mPlaneOutlineColor, 0);
		GLES20.glDrawElements(GLES20.GL_LINES, mBufferLineIndices.capacity(),
				GLES20.GL_UNSIGNED_BYTE, mBufferLineIndices);

		// Render outlines with blending for smoothening them a bit.
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glLineWidth(mLineWidth + .5f);
		GLES20.glUniform1f(uAlpha, .5f);
		GLES20.glDrawElements(GLES20.GL_LINES, mBufferLineIndices.capacity(),
				GLES20.GL_UNSIGNED_BYTE, mBufferLineIndices);

		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_STENCIL_TEST);
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
		mLineWidth = Math.max(1, Math.min(width, height) / 160);
		mAspectRatio = (float) height / width;
		Matrix.orthoM(mProjM, 0, -1f, 1f, -mAspectRatio, mAspectRatio, 1f, 21f);
		Matrix.setLookAtM(mViewM, 0, 0, 1f, 5f, 0, 0, 0, 0f, 1f, 0f);
	}

	/**
	 * Called from renderer once surface has been created.
	 * 
	 * @param ctx
	 *            Context to read shaders from.
	 */
	public void onSurfaceCreated(Context ctx) {
		mShaderPlane.setProgram(ctx.getString(R.string.shader_plane_vs),
				ctx.getString(R.string.shader_plane_fs));
	}

	/**
	 * Sets plane color.
	 * 
	 * @param planeColor
	 *            Three float RGB array.
	 * @param planeOutlineColor
	 *            Three float RGB array.
	 */
	public void setColor(float[] planeColor, float[] planeOutlineColor) {
		mPlaneColor = planeColor;
		mPlaneOutlineColor = planeOutlineColor;
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
