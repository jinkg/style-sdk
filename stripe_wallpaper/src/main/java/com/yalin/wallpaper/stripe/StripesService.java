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

package com.yalin.wallpaper.stripe;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.widget.Toast;
import com.yalin.style.engine.WallpaperServiceProxy;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Wallpaper entry point.
 */
public final class StripesService extends WallpaperServiceProxy {

	public StripesService(Context host) {
		super(host);
	}

	/**
	 * Private method for loading raw String resources.
	 * 
	 * @param resourceId
	 *            Raw resource id.
	 * @return Resource as a String.
	 * @throws Exception
	 */
	private final String loadRawResource(int resourceId) throws Exception {
		InputStream is = getResources().openRawResource(resourceId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toString();
	}

	@Override
	public final Engine onCreateEngine() {
		return new WallpaperEngine();
	}

	/**
	 * Private wallpaper engine implementation.
	 */
	private final class WallpaperEngine extends ActiveEngine implements Runnable {

		// Screen aspect ratio.
		private final float mAspectRatio[] = new float[2];
		// GLSurfaceView implementation.
		private StripesSurfaceView mGLSurfaceView;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {

			// Uncomment for debugging.
			// android.os.Debug.waitForDebugger();

			super.onCreate(surfaceHolder);
			mGLSurfaceView = new StripesSurfaceView();
		}

		@Override
		public final void onDestroy() {
			super.onDestroy();
			mGLSurfaceView.onDestroy();
			mGLSurfaceView = null;
		}

		@Override
		public final void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				mGLSurfaceView.onResume();
				mGLSurfaceView.requestRender();
			} else {
				mGLSurfaceView.onPause();
			}
		}

		@Override
		public final void run() {
			Toast.makeText(StripesService.this, R.string.error_shader_compiler,
					Toast.LENGTH_LONG).show();
		}

		/**
		 * Lazy as I am, I din't bother using GLWallpaperService (found on
		 * GitHub) project for wrapping OpenGL functionality into my wallpaper
		 * service. Instead am using GLSurfaceView and trick it into hooking
		 * into Engine provided SurfaceHolder instead of SurfaceView provided
		 * one GLSurfaceView extends. For saving some bytes Renderer is
		 * implemented here too.
		 */
		private final class StripesSurfaceView extends GLSurfaceView implements
				GLSurfaceView.Renderer {

			// Spline quality value.
			private static final int SPLINE_VERTEX_COUNT = 20;
			// Buffers for vertices.
			private ByteBuffer mBufferQuad;
			private FloatBuffer mBufferSpline;
			// Boolean value for indicating if shader compiler is supported.
			private final boolean mShaderCompilerSupported[] = new boolean[1];
			// Shader program ids.
			private int mShaderQuad = 0;
			private int mShaderStripe = 0;
			// Stripe data.
			private final long mStripes[][] = new long[4][];

			/**
			 * Default constructor.
			 */
			private StripesSurfaceView() {
				super(StripesService.this);

				setEGLContextClientVersion(2);
				setRenderer(this);
				setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
				onPause();

				ByteBuffer bBuffer = ByteBuffer
						.allocateDirect(4 * 4 * SPLINE_VERTEX_COUNT);
				mBufferSpline = bBuffer.order(ByteOrder.nativeOrder())
						.asFloatBuffer();
				for (int i = 0; i < SPLINE_VERTEX_COUNT; ++i) {
					float t = (float) i / (SPLINE_VERTEX_COUNT - 1);
					mBufferSpline.put(t).put(1);
					mBufferSpline.put(t).put(-1);
				}
				mBufferSpline.position(0);

				for (int i = 0; i < mStripes.length; ++i) {
					mStripes[i] = new long[18];
					mStripes[i][3] = mStripes[i][11] = 1500;
					mStripes[i][5] = mStripes[i][13] = 600;
					mStripes[i][7] = mStripes[i][15] = -600;
					mStripes[i][9] = mStripes[i][17] = -1500;
				}

				// Create full scene quad buffer.
				final byte FULL_QUAD_COORDS[] = { -1, 1, -1, -1, 1, 1, 1, -1 };
				mBufferQuad = ByteBuffer.allocateDirect(4 * 2);
				mBufferQuad.put(FULL_QUAD_COORDS).position(0);
			}

			@Override
			public final SurfaceHolder getHolder() {
				return WallpaperEngine.this.getSurfaceHolder();
			}

			/**
			 * Private shader program loader method.
			 * 
			 * @param vs
			 *            Vertex shader source.
			 * @param fs
			 *            Fragment shader source.
			 * @return Shader program id.
			 */
			private final int loadProgram(String vs, String fs)
					throws Exception {
				int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs);
				int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs);
				int program = GLES20.glCreateProgram();
				if (program != 0) {
					GLES20.glAttachShader(program, vertexShader);
					GLES20.glAttachShader(program, fragmentShader);
					GLES20.glLinkProgram(program);
					int[] linkStatus = new int[1];
					GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,
							linkStatus, 0);
					if (linkStatus[0] != GLES20.GL_TRUE) {
						String error = GLES20.glGetProgramInfoLog(program);
						GLES20.glDeleteProgram(program);
						throw new Exception(error);
					}
				}
				return program;
			}

			/**
			 * Private shader loader method.
			 * 
			 * @param shaderType
			 *            Vertex or fragment shader.
			 * @param source
			 *            Shader source code.
			 * @return Loaded shader id.
			 */
			private final int loadShader(int shaderType, String source)
					throws Exception {
				int shader = GLES20.glCreateShader(shaderType);
				if (shader != 0) {
					GLES20.glShaderSource(shader, source);
					GLES20.glCompileShader(shader);
					int[] compiled = new int[1];
					GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,
							compiled, 0);
					if (compiled[0] == 0) {
						String error = GLES20.glGetShaderInfoLog(shader);
						GLES20.glDeleteShader(shader);
						throw new Exception(error);
					}
				}
				return shader;
			}

			/**
			 * Should be called once underlying Engine is destroyed. Calling
			 * onDetachedFromWindow() will stop rendering thread which is lost
			 * otherwise.
			 */
			public final void onDestroy() {
				super.onDetachedFromWindow();
			}

			@Override
			public final void onDrawFrame(GL10 unused) {

				// Clear screen buffer.
				GLES20.glClearColor(0.3f, 0.5f, 0.8f, 1f);
				GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

				// If shader compiler is not supported return immediately.
				if (mShaderCompilerSupported[0] == false) {
					return;
				}

				// Disable unneeded rendering flags.
				GLES20.glDisable(GLES20.GL_CULL_FACE);
				GLES20.glDisable(GLES20.GL_BLEND);
				GLES20.glDisable(GLES20.GL_DEPTH_TEST);

				GLES20.glUseProgram(mShaderStripe);
				int uControlPts = GLES20.glGetUniformLocation(mShaderStripe,
						"uControlPts");
				int uWidth = GLES20.glGetUniformLocation(mShaderStripe,
						"uWidth");
				int uAspectRatio = GLES20.glGetUniformLocation(mShaderStripe,
						"uAspectRatio");
				int aSplinePos = GLES20.glGetAttribLocation(mShaderStripe,
						"aSplinePos");

				GLES20.glUniform2f(uWidth, .4f, 1.0f);
				GLES20.glUniform2fv(uAspectRatio, 1, mAspectRatio, 0);

				GLES20.glVertexAttribPointer(aSplinePos, 2, GLES20.GL_FLOAT,
						false, 0, mBufferSpline);
				GLES20.glEnableVertexAttribArray(aSplinePos);

				final float ctrlPts[] = new float[8];
				long currentTime = SystemClock.uptimeMillis();
				for (long stripe[] : mStripes) {

					if (currentTime > stripe[0] + stripe[1]) {
						stripe[0] = currentTime;
						stripe[1] = 2000 + (long) (Math.random() * 2000);

						for (int i = 0; i < 8; i += 2) {
							stripe[i + 2] = stripe[i + 10];
							stripe[i + 10] = (long) (Math.random() * 4000) - 2000;
						}
					}

					float t = (float) (currentTime - stripe[0]) / stripe[1];
					t = t * t * (3 - 2 * t);

					for (int i = 0; i < 8; i += 2) {
						ctrlPts[i + 0] = (stripe[i + 2] + (stripe[i + 10] - stripe[i + 2])
								* t) / 1000f;
						ctrlPts[i + 1] = stripe[i + 3] / 1000f;
					}
					GLES20.glUniform2fv(uControlPts, 4, ctrlPts, 0);
					GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
							2 * SPLINE_VERTEX_COUNT);
				}

				GLES20.glUseProgram(mShaderQuad);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,
						GLES20.GL_ONE_MINUS_SRC_ALPHA);
				int aPosition = GLES20.glGetAttribLocation(mShaderQuad,
						"aPosition");
				GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE,
						false, 0, mBufferQuad);
				GLES20.glEnableVertexAttribArray(aPosition);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

			}

			@Override
			public final void onSurfaceChanged(GL10 unused, int width,
					int height) {
				GLES20.glViewport(0, 0, width, height);
				mAspectRatio[0] = (float) Math.min(width, height) / width;
				mAspectRatio[1] = (float) Math.min(width, height) / height;
			}

			@Override
			public final void onSurfaceCreated(GL10 unused, EGLConfig config) {
				// Check if shader compiler is supported.
				GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER,
						mShaderCompilerSupported, 0);

				// If not, show user an error message and return immediately.
				if (mShaderCompilerSupported[0] == false) {
					new Handler(Looper.getMainLooper())
							.post(WallpaperEngine.this);
					return;
				}

				// Shader compiler supported, try to load shader.
				try {
					String vs = loadRawResource(R.raw.stripe_vs);
					String fs = loadRawResource(R.raw.stripe_fs);
					mShaderStripe = loadProgram(vs, fs);
					vs = loadRawResource(R.raw.quad_vs);
					fs = loadRawResource(R.raw.quad_fs);
					mShaderQuad = loadProgram(vs, fs);
				} catch (Exception ex) {
					mShaderCompilerSupported[0] = false;
					ex.printStackTrace();
				}
			}

		}

	}

}