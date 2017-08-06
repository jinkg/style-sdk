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

package com.yalin.wallpaper.metaballs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Vector;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Renderer class.
 */
public final class MetaballsRenderer implements GLSurfaceView.Renderer {

	// Number of automated blobs.
	private final int BLOB_COUNT = 20;
	// Screen aspect ratio.
	private final float[] mAspectRatio = new float[2];
	// Automated blobs.
	private final Vector<StructBlob> mBlobs = new Vector<StructBlob>();
	// Touch event blobs.
	private final SparseArray<StructBlob> mBlobsTouch = new SparseArray<StructBlob>(
			5);
	private ByteBuffer mBufferQuad;
	private Context mContext;
	private final MetaballsFbo mFbo = new MetaballsFbo();
	private final MetaballsShader mShaderBlob = new MetaballsShader();
	private final boolean[] mShaderCompilerSupport = new boolean[1];
	private final MetaballsShader mShaderCopy = new MetaballsShader();
	private int mWidth, mHeight;

	/**
	 * Default constructor.
	 */
	public MetaballsRenderer(Context context) {
		mContext = context;

		// Full view quad buffer.
		final byte[] QUAD = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mBufferQuad = ByteBuffer.allocateDirect(8);
		mBufferQuad.put(QUAD).position(0);

		// Initialize blobs.
		for (int i = 0; i < BLOB_COUNT; ++i) {
			StructBlob blob = new StructBlob();
			genRandBlob(blob);
			mBlobs.add(blob);
		}
	}

	/**
	 * Generates random blob values.
	 */
	private void genRandBlob(StructBlob blob) {
		blob.mSizeTarget = ((float) Math.random() * 0.5f + 0.3f);
		blob.mPositionTarget[0] = ((float) Math.random() * 2 - 1);
		blob.mPositionTarget[1] = ((float) Math.random() * 2 - 1);

		final float[] hsv = { (float) (Math.random() * 360), 1f, 1f };
		int color = Color.HSVToColor(hsv);

		blob.mColorTarget[0] = Color.red(color) / 255f;
		blob.mColorTarget[1] = Color.green(color) / 255f;
		blob.mColorTarget[2] = Color.blue(color) / 255f;
	}

	/**
	 * Loads String from raw resources with given id.
	 */
	private String loadRawString(int rawId) throws Exception {
		InputStream is = mContext.getResources().openRawResource(rawId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toString();
	}

	@Override
	public void onDrawFrame(GL10 unused) {

		// If shader compiler not supported return immediately.
		if (!mShaderCompilerSupport[0]) {
			// Clear view buffer.
			GLES20.glClearColor(0f, 0f, 0f, 1f);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			return;
		}

		// Disable unnecessary OpenGL flags.
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		long time = SystemClock.uptimeMillis();

		// Check automated blob animation times.
		for (StructBlob blob : mBlobs) {
			if (blob.mTimeTarget < time) {
				blob.mTimeSource = time;
				blob.mTimeTarget = time + 8000 + (long) (Math.random() * 4000);

				for (int i = 0; i < 2; ++i) {
					blob.mPositionSource[i] = blob.mPositionTarget[i];
				}
				for (int i = 0; i < 3; ++i) {
					blob.mColorSource[i] = blob.mColorTarget[i];
				}
				blob.mSizeSource = blob.mSizeTarget;

				genRandBlob(blob);
			}
		}
		// Check touch blob animation times.
		for (int i = 0; i < mBlobsTouch.size();) {
			StructBlob blob = mBlobsTouch.valueAt(i);
			if (blob.mSizeTarget == 0f && blob.mTimeTarget < time) {
				mBlobsTouch.remove(mBlobsTouch.keyAt(i));
			} else {
				++i;
			}
		}

		// Render blobs to offscreen buffer.
		mShaderBlob.useProgram();
		int uModelViewM = mShaderBlob.getHandle("uModelViewM");
		int uColor = mShaderBlob.getHandle("uColor");
		int aPosition = mShaderBlob.getHandle("aPosition");

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_ALPHA);

		mFbo.bind();
		mFbo.bindTexture(0);

		// Clear view buffer.
		GLES20.glClearColor(0f, 0f, 0f, 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		final Matrix matrix = new Matrix();
		final float[] matrixValues = new float[9];

		// Render automated blobs.
		for (StructBlob blob : mBlobs) {
			float t = (float) (time - blob.mTimeSource)
					/ (blob.mTimeTarget - blob.mTimeSource);
			t = t * t * (3 - 2 * t);
			for (int i = 0; i < 2; ++i) {
				blob.mPosition[i] = blob.mPositionSource[i]
						+ (blob.mPositionTarget[i] - blob.mPositionSource[i])
						* t;
			}
			for (int i = 0; i < 3; ++i) {
				blob.mColor[i] = blob.mColorSource[i]
						+ (blob.mColorTarget[i] - blob.mColorSource[i]) * t;
			}
			blob.mSize = blob.mSizeSource
					+ (blob.mSizeTarget - blob.mSizeSource) * t;

			matrix.setScale(mAspectRatio[0] * blob.mSize, mAspectRatio[1]
					* blob.mSize);
			matrix.postTranslate(blob.mPosition[0], blob.mPosition[1]);
			matrix.getValues(matrixValues);
			GLES20.glUniformMatrix3fv(uModelViewM, 1, false, matrixValues, 0);

			GLES20.glUniform3f(uColor, blob.mColor[0], blob.mColor[1],
					blob.mColor[2]);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		}

		// Render touch event blobs.
		for (int i = 0; i < mBlobsTouch.size(); ++i) {
			StructBlob blob = mBlobsTouch.valueAt(i);

			float t = (float) (time - blob.mTimeSource)
					/ (blob.mTimeTarget - blob.mTimeSource);
			if (t > 1.0f) {
				t = 1.0f;
			}
			t = t * t * (3 - 2 * t);

			blob.mSize = blob.mSizeSource
					+ (blob.mSizeTarget - blob.mSizeSource) * t;

			matrix.setScale(mAspectRatio[0] * blob.mSize, mAspectRatio[1]
					* blob.mSize);
			matrix.postTranslate(blob.mPosition[0], blob.mPosition[1]);
			matrix.getValues(matrixValues);
			GLES20.glUniformMatrix3fv(uModelViewM, 1, false, matrixValues, 0);

			GLES20.glUniform3f(uColor, blob.mColor[0], blob.mColor[1],
					blob.mColor[2]);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		}

		GLES20.glDisable(GLES20.GL_BLEND);

		// Copy offscreen buffer to screen.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, mWidth, mHeight);

		mShaderCopy.useProgram();
		int uNoise = mShaderCopy.getHandle("uNoise");
		aPosition = mShaderCopy.getHandle("aPosition");

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFbo.getTexture(0));

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		GLES20.glUniform1f(uNoise, (time % 200) / 400f);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		mWidth = width;
		mHeight = height;

		GLES20.glViewport(0, 0, mWidth, mHeight);

		// Store view aspect ratio.
		mAspectRatio[0] = 1f / ((float) Math.max(mWidth, mHeight) / mHeight);
		mAspectRatio[1] = 1f / ((float) Math.max(mWidth, mHeight) / mWidth);

		mFbo.init(mWidth, mHeight, 1);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Check if shader compiler is supported.
		GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER, mShaderCompilerSupport,
				0);

		// If not, show user an error message and return immediately.
		if (!mShaderCompilerSupport[0]) {
			String msg = mContext.getString(R.string.error_shader_compiler);
			showError(msg);
			return;
		}

		// Load vertex and fragment shaders.
		try {
			String vertexSource, fragmentSource;
			vertexSource = loadRawString(R.raw.blob_vs);
			fragmentSource = loadRawString(R.raw.blob_fs);
			mShaderBlob.setProgram(vertexSource, fragmentSource);
			vertexSource = loadRawString(R.raw.copy_vs);
			fragmentSource = loadRawString(R.raw.copy_fs);
			mShaderCopy.setProgram(vertexSource, fragmentSource);
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	public void onTouchEvent(MotionEvent me) {
		switch (me.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN: {
			// On touch down event generate new touch blob.
			StructBlob blob = new StructBlob();
			genRandBlob(blob);

			blob.mSizeSource = 0.0f;
			blob.mSizeTarget = 1.0f;
			blob.mPosition[0] = ((me.getX(me.getActionIndex()) * 2) / mWidth) - 1f;
			blob.mPosition[1] = 1f - ((me.getY(me.getActionIndex()) * 2) / mHeight);
			for (int i = 0; i < 3; ++i) {
				blob.mColor[i] = blob.mColorTarget[i];
			}

			blob.mTimeSource = SystemClock.uptimeMillis();
			blob.mTimeTarget = blob.mTimeSource + 300;

			mBlobsTouch.put(me.getPointerId(me.getActionIndex()), blob);
			break;

		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP: {
			// On touch up event mark blob for removal.
			StructBlob blob = mBlobsTouch.get(me.getPointerId(me
					.getActionIndex()));
			blob.mSizeSource = blob.mSize;
			blob.mSizeTarget = 0f;

			blob.mTimeSource = SystemClock.uptimeMillis();
			blob.mTimeTarget = blob.mTimeSource + 300;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			// On touch move event update touch blob positions.
			for (int i = 0; i < me.getPointerCount(); ++i) {
				StructBlob blob = mBlobsTouch.get(me.getPointerId(i));
				blob.mPosition[0] = ((me.getX(i) * 2) / mWidth) - 1f;
				blob.mPosition[1] = 1f - ((me.getY(i) * 2) / mHeight);
			}
			break;
		}
		}
	}

	/**
	 * Shows Toast on screen with given message.
	 */
	private void showError(final String errorMsg) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * Private blob structure class.
	 */
	private class StructBlob {
		public final float[] mColor = new float[3];
		public final float[] mColorSource = new float[3];
		public final float[] mColorTarget = new float[3];
		public final float[] mPosition = new float[2];
		public final float[] mPositionSource = new float[2];
		public final float[] mPositionTarget = new float[2];
		public float mSize;
		public float mSizeSource;
		public float mSizeTarget;
		public long mTimeSource;
		public long mTimeTarget;
	}

}
