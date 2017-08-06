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
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class for handling cloud manipulation and rendering.
 */
public final class FlierClouds {

	// Maximum point sizes for near and far clipping plane.
	private static final float MAX_POINTSIZE_NEAR = .2f,
			MAX_POINTSIZE_FAR = .1f;
	// Border size for cloud points.
	private static final float POINT_BORDER_SIZE = .015f;
	// Multiplier for x -offset scrolling.
	private static final float X_OFFSET_MULTIPLIER = 2f;
	// Z near and far clipping planes.
	private static final float ZNEAR = 1f, ZFAR = 6f;

	// Projection matrix aspect ratio.
	private float mAspectRatioX, mAspectRatioY;
	// Cloud and cloud outline colors.
	private float[] mCloudColor = new float[3],
			mCloudOutlineColor = new float[3];
	// Cloud storage.
	private final StructCloud[] mClouds = new StructCloud[FlierConstants.CLOUD_COUNT];
	// Projection matrix.
	private final float[] mProjM = new float[16];
	// View rectangles for near and far clipping planes.
	private final RectF mRectNear = new RectF(), mRectFar = new RectF();
	// Last rendering time.
	private long mRenderTime;
	// Shader for rendering points clouds consist of.
	private final FlierShader mShaderPoint = new FlierShader();
	// Point shader vertices.
	private ByteBuffer mVertices;
	// X -offset for handling scrolling.
	private float mXOffset;

	/**
	 * Default constructor.
	 */
	public FlierClouds() {
		mVertices = ByteBuffer.allocateDirect(4 * 2);
		mVertices.put(FlierConstants.FULL_QUAD_COORDS).position(0);

		for (int i = 0; i < mClouds.length; ++i) {
			mClouds[i] = new StructCloud();
		}
	}

	/**
	 * Generates/initializes cloud with random values.
	 * 
	 * @param cloud
	 *            Cloud to modify.
	 */
	private void genRandCloud(StructCloud cloud) {
		RectF rect = cloud.mViewRect;

		cloud.mZValue = rand(-ZFAR, -ZNEAR);
		float t = (-cloud.mZValue - ZNEAR) / (ZFAR - ZNEAR);
		rect.left = mRectNear.left + t * (mRectFar.left - mRectNear.left);
		rect.right = mRectNear.right + t * (mRectFar.right - mRectNear.right);
		rect.top = mRectNear.top + t * (mRectFar.top - mRectNear.top);
		rect.bottom = rect.top * 0.4f;

		cloud.mWidth = rect.width() * 0.2f;
		cloud.mHeight = rect.height() * 0.2f;
		cloud.mSpeed = rand(.3f, .6f);

		float y = rand(rect.bottom, rect.top - cloud.mHeight);
		float maxPointSz = MAX_POINTSIZE_NEAR + t
				* (MAX_POINTSIZE_FAR - MAX_POINTSIZE_NEAR);

		for (StructCloudPoint point : cloud.mPoints) {
			float pointSz = rand(maxPointSz / 2, maxPointSz);
			point.mSize = pointSz;
			point.mPosition[0] = rand(pointSz, cloud.mWidth - pointSz);
			point.mPosition[1] = rand(pointSz, cloud.mHeight - pointSz) + y;
		}
	}

	/**
	 * Called from renderer for rendering clouds into scene.
	 */
	public void onDrawFrame() {
		// First do animation.
		boolean needsSorting = false;
		long renderTime = SystemClock.uptimeMillis();
		float t = (float) (renderTime - mRenderTime) / 1000;
		mRenderTime = renderTime;
		for (StructCloud cloud : mClouds) {
			cloud.mXOffset -= t * cloud.mSpeed;
			if (cloud.mXOffset + cloud.mWidth < cloud.mViewRect.left) {
				genRandCloud(cloud);
				cloud.mXOffset = cloud.mViewRect.right + cloud.mWidth;
				needsSorting = true;
			}
		}
		if (needsSorting) {
			sortClouds();
		}

		// Get shader ids.
		mShaderPoint.useProgram();
		int uModelViewProjM = mShaderPoint.getHandle("uModelViewProjM");
		int uPointPosition = mShaderPoint.getHandle("uPointPosition");
		int uPointSize = mShaderPoint.getHandle("uPointSize");
		int uAspectRatio = mShaderPoint.getHandle("uAspectRatio");
		int uColor = mShaderPoint.getHandle("uColor");
		int aPosition = mShaderPoint.getHandle("aPosition");

		// Set common values to shader.
		GLES20.glUniformMatrix4fv(uModelViewProjM, 1, false, mProjM, 0);
		GLES20.glUniform2f(uAspectRatio, mAspectRatioX, mAspectRatioY);
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		GLES20.glStencilFunc(GLES20.GL_EQUAL, 0x00, 0xFFFFFFFF);
		GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_INCR, GLES20.GL_INCR);

		for (StructCloud cloud : mClouds) {
			// First render inner part of circles.
			GLES20.glUniform3fv(uColor, 1, mCloudColor, 0);
			for (StructCloudPoint point : cloud.mPoints) {
				GLES20.glUniform3f(uPointPosition, point.mPosition[0]
						+ cloud.mXOffset - mXOffset, point.mPosition[1],
						cloud.mZValue);
				GLES20.glUniform1f(uPointSize, point.mSize - POINT_BORDER_SIZE);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			}
			// Then border.
			GLES20.glUniform3fv(uColor, 1, mCloudOutlineColor, 0);
			for (StructCloudPoint point : cloud.mPoints) {
				GLES20.glUniform3f(uPointPosition, point.mPosition[0]
						+ cloud.mXOffset - mXOffset, point.mPosition[1],
						cloud.mZValue);
				GLES20.glUniform1f(uPointSize, point.mSize);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			}
		}

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
		mAspectRatioX = (float) Math.min(width, height) / width;
		mAspectRatioY = (float) Math.min(width, height) / height;
		Matrix.frustumM(mProjM, 0, -mAspectRatioX, mAspectRatioX,
				-mAspectRatioY, mAspectRatioY, ZNEAR, ZFAR);

		final float projInvM[] = new float[16];
		Matrix.invertM(projInvM, 0, mProjM, 0);
		unproject(projInvM, mRectNear, -1);
		unproject(projInvM, mRectFar, 1);

		mRectNear.right += X_OFFSET_MULTIPLIER;
		mRectFar.right += X_OFFSET_MULTIPLIER;

		for (StructCloud cloud : mClouds) {
			genRandCloud(cloud);
			cloud.mXOffset = rand(cloud.mViewRect.left, cloud.mViewRect.right);
		}
		sortClouds();
		mRenderTime = SystemClock.uptimeMillis();
	}

	/**
	 * Called once surface has been created.
	 * 
	 * @param ctx
	 *            Context for reading shaders from.
	 */
	public void onSurfaceCreated(Context ctx) {
		mShaderPoint.setProgram(ctx.getString(R.string.shader_point_vs),
				ctx.getString(R.string.shader_cloud_fs));
	}

	/**
	 * Generates random value between [min, max).
	 * 
	 * @param min
	 *            Minimum value.
	 * @param max
	 *            Maximum value.
	 * @return Random value between [min, max).
	 */
	private float rand(float min, float max) {
		return min + (float) Math.random() * (max - min);
	}

	/**
	 * Sets cloud colors.
	 * 
	 * @param cloudColor
	 *            Three float RGB array.
	 * @param cloudOutlineColor
	 *            Three float RGB array.
	 */
	public void setColors(float[] cloudColor, float[] cloudOutlineColor) {
		mCloudColor = cloudColor;
		mCloudOutlineColor = cloudOutlineColor;
	}

	/**
	 * Sets x offset for clouds. Offset is expected to be a value between [0,
	 * 1].
	 * 
	 * @param xOffset
	 *            New x offset value.
	 */
	public void setXOffset(float xOffset) {
		mXOffset = xOffset * X_OFFSET_MULTIPLIER;
	}

	/**
	 * Sorts clouds based on their z value.
	 */
	public void sortClouds() {
		final Comparator<StructCloud> comparator = new Comparator<StructCloud>() {
			@Override
			public int compare(StructCloud arg0, StructCloud arg1) {
				float z0 = arg0.mZValue;
				float z1 = arg1.mZValue;
				return z0 == z1 ? 0 : z0 < z1 ? 1 : -1;
			}
		};
		Arrays.sort(mClouds, comparator);
	}

	/**
	 * Calculates unprojected rectangle at given z value in screen space.
	 * 
	 * @param projInv
	 *            Inverse of projection matrix.
	 * @param rect
	 *            Rectangle to contain result.
	 * @param z
	 *            Z value.
	 */
	private void unproject(float[] projInv, RectF rect, float z) {
		final float result[] = new float[4];
		Matrix.multiplyMV(result, 0, projInv, 0, new float[] { -1, 1, z, 1 }, 0);
		rect.left = result[0] / result[3];
		rect.top = result[1] / result[3];
		Matrix.multiplyMV(result, 0, projInv, 0, new float[] { 1, -1, z, 1 }, 0);
		rect.right = result[0] / result[3];
		rect.bottom = result[1] / result[3];
	}

	/**
	 * Private class for storing cloud information.
	 */
	private final class StructCloud {
		public final StructCloudPoint mPoints[] = new StructCloudPoint[FlierConstants.CLOUD_POINT_COUNT];
		public float mSpeed, mXOffset;
		public final RectF mViewRect = new RectF();
		public float mWidth, mHeight, mZValue;

		public StructCloud() {
			for (int i = 0; i < mPoints.length; ++i) {
				mPoints[i] = new StructCloudPoint();
			}
		}
	}

	/**
	 * Private class for storing cloud point information.
	 */
	private final class StructCloudPoint {
		public final float[] mPosition = new float[2];
		public float mSize;
	}

}
