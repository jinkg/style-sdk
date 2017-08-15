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

package com.yalin.wallpaper.lovebeat;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Toast;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main renderer class.
 */
public final class LBRenderer implements GLSurfaceView.Renderer {

	// Animation tick time length in millis.
	private static final long ANIMATION_TICK_TIME = 4000;
	// Number of foreground boxes.
	private static final int FG_BOX_COUNT = 16;

	/**
	 * Background rendering variables.
	 */

	// Static coordinate buffer for rendering background.
	private ByteBuffer bg_FillBuffer;
	// Fill data elements array.
	private final StructFillData bg_FillData[] = new StructFillData[4];
	// Number of fill data elements for rendering.
	private int bg_FillDataCount;
	// Last time interpolator.
	public float bg_LastTimeT = 0;
	// Shader for rendering filled background area.
	private final LBShader bg_Shader = new LBShader();

	/**
	 * Foreground rendering variables.
	 */

	// Box data structure array.
	private final StructBoxData fg_Boxes[] = new StructBoxData[FG_BOX_COUNT];
	// Value for counting The LoveBeat.
	private int fg_LoveBeat = 0;
	// Shader for rendering filled foreground boxes.
	private final LBShader fg_Shader = new LBShader();

	/**
	 * Common variables.
	 */

	// Render area aspect ratio.
	private final float mAspectRatio[] = new float[2];
	// Application context.
	private Context mContext;
	// FBOs for offscreen rendering.
	private final LBFbo mFbo = new LBFbo();
	// Random number seed for copy shader.
	private float mRandomSeed;
	// Rotation angle and rotation animation target (= int * PI / 4).
	private int mRotationAngle, mRotationAngleTarget;
	// Vertex buffer for full scene coordinates.
	private ByteBuffer mScreenVertices;
	// Flag for indicating whether shader compiler is supported.
	private final boolean[] mShaderCompilerSupported = new boolean[1];
	// Shader for copying offscreen texture on screen.
	private final LBShader mShaderCopy = new LBShader();
	// Initialize last render time so that on first render iteration environment
	// is being set up properly.
	private long mTimeLast = -1;
	// Animation tick timer start time in millis.
	private long mTimeTickStart = -1;
	// True once following touch events. Used for fading away from displacement
	// mapping and stopping animation timer for the time touch events are being
	// executed.
	private boolean mTouchFollow;
	// Two { x, y } tuples for touch start and current touch position.
	private final float mTouchPositions[] = new float[4];
	// Surface width and height;
	private int mWidth, mHeight;

	/**
	 * Default constructor.
	 */
	public LBRenderer(Context context) {

		/**
		 * Instantiate common variables.
		 */

		// Store application context for later use.
		mContext = context;

		// Create screen coordinates buffer.
		final byte SCREEN_COORDS[] = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mScreenVertices = ByteBuffer.allocateDirect(2 * 4);
		mScreenVertices.put(SCREEN_COORDS).position(0);

		/**
		 * Instantiate background rendering variables.
		 */

		// Generate fill vertex array coordinates. Coordinates are given as
		// tuples {targetT,normalT}. Where targetT=0 for sourcePos, targetT=1
		// for targetPos. And final coordinate is position+(normal*normalT).
		final byte[] FILL_COORDS = { 0, 0, 0, 1, 1, 0, 1, 1 };
		bg_FillBuffer = ByteBuffer.allocateDirect(8);
		bg_FillBuffer.put(FILL_COORDS).position(0);
		// Instantiate fill data array.
		for (int i = 0; i < bg_FillData.length; ++i) {
			bg_FillData[i] = new StructFillData();
		}
		// Generate first animation.
		bg_GenRandFillData();

		/**
		 * Instantiate foreground rendering variables.
		 */

		// Initialize box struct array.
		for (int i = 0; i < fg_Boxes.length; ++i) {
			fg_Boxes[i] = new StructBoxData();
		}
		// Initialize foreground boxes with random values.
		for (StructBoxData box : fg_Boxes) {
			fg_GenRandBox(box);
		}
	}

	/**
	 * Generates/stores given points and normal into fill data array. Fill areas
	 * are presented by three variables; source point, target point and normal.
	 * In some cases, using random number generator, given area is split into
	 * two. Also, similarly, source and target positions are swapped for some
	 * random behavior in order to make effect more lively.
	 * 
	 * @param x1
	 *            Source position x.
	 * @param y1
	 *            Source position y.
	 * @param x2
	 *            Target position x.
	 * @param y2
	 *            Target position y.
	 * @param nx
	 *            Normal x.
	 * @param ny
	 *            Normal y.
	 */
	private void bg_GenFillData(float x1, float y1, float x2, float y2,
			float nx, float ny) {
		// Select random background color.
		float rgb = (float) (Math.random() * 0.5f) + 0.5f;
		// Randomly split filling in two independent fill areas.
		int fillDataCount = Math.random() > 0.8 ? 2 : 1;
		// Generate fill struct data.
		for (int curIdx = 0; curIdx < fillDataCount; ++curIdx) {
			// Take next unused StructFillData.
			StructFillData fillData = bg_FillData[bg_FillDataCount++];
			// Set common values.
			fillData.mColor[0] = rgb;
			fillData.mColor[1] = rgb;
			fillData.mColor[2] = rgb;
			fillData.mFillNormal[0] = nx;
			fillData.mFillNormal[1] = ny;

			// Calculate start and end positions using interpolation.
			float sourceT = (float) curIdx / fillDataCount;
			float targetT = (float) (curIdx + 1) / fillDataCount;

			// Finally store fill source and target positions. Plus randomly
			// swap them with each other for "reverse" effect.
			int posIdx = Math.random() > 0.5 ? 2 : 0;
			// Calculate new positions using sourceT and targetT.
			fillData.mFillPositions[posIdx + 0] = x1 + (x2 - x1) * sourceT;
			fillData.mFillPositions[posIdx + 1] = y1 + (y2 - y1) * sourceT;
			// Recalculate posIdx so that 0 --> 2 or 2 --> 0.
			posIdx = (posIdx + 2) % 4;
			fillData.mFillPositions[posIdx + 0] = x1 + (x2 - x1) * targetT;
			fillData.mFillPositions[posIdx + 1] = y1 + (y2 - y1) * targetT;
		}
	}

	/**
	 * Generates new fill/animation structure.
	 */
	private void bg_GenRandFillData() {
		// First reset fill data counter. Do note that genFillData increases
		// this counter once called.
		bg_FillDataCount = 0;

		// Select random integer for selecting animation.
		int randPattern = (int) (Math.random() * 8);
		switch (randPattern) {
		// Vertical and horizontal fills.
		// We set up up vector angle here too so that boxes are aligned with
		// background pattern.
		case 0:
			bg_GenFillData(-1, 1, -1, -1, 2, 0);
			mRotationAngleTarget = 0;
			break;
		case 1:
			bg_GenFillData(-1, 1, 1, 1, 0, -2);
			mRotationAngleTarget = 2;
			break;
		case 2:
			bg_GenFillData(-1, 1, -1, 0, 2, 0);
			bg_GenFillData(-1, 0, -1, -1, 2, 0);
			mRotationAngleTarget = 0;
			break;
		case 3:
			bg_GenFillData(-1, 1, 1, 1, 0, -1);
			bg_GenFillData(-1, 0, 1, 0, 0, -1);
			mRotationAngleTarget = 2;
			break;
		// Diagonal fills.
		case 4:
			bg_GenFillData(-1, 1, 1, 1, 3, -3);
			bg_GenFillData(-1, 1, -1, -1, 3, -3);
			mRotationAngleTarget = 3;
			break;
		case 5:
			bg_GenFillData(1, 1, -1, 1, -3, -3);
			bg_GenFillData(1, 1, 1, -1, -3, -3);
			mRotationAngleTarget = 1;
			break;
		case 6:
			bg_GenFillData(-1, -1, 1, 1, -1.5f, 1.5f);
			bg_GenFillData(-1, -1, 1, 1, 1.5f, -1.5f);
			mRotationAngleTarget = 1;
			break;
		case 7:
			bg_GenFillData(-1, 1, 1, -1, 1.5f, 1.5f);
			bg_GenFillData(-1, 1, 1, -1, -1.5f, -1.5f);
			mRotationAngleTarget = 3;
			break;
		}

		// Select closest target angle from left or right side of current value.
		int diff1 = Math.abs(mRotationAngleTarget - mRotationAngle);
		int diff2 = Math.abs(mRotationAngleTarget - mRotationAngle + 4);
		mRotationAngleTarget = diff1 <= diff2 ? mRotationAngleTarget
				: mRotationAngleTarget + 4;
	}

	/**
	 * Renders background onto current frame buffer.
	 * 
	 * @param timeT
	 *            Time interpolator, float between [0f, 1f].
	 * @param newTime
	 *            True once new [0f, 1f] timeT range is started.
	 */
	public void bg_OnDrawFrame(float timeT, boolean newTime) {
		// Calculate source and target interpolant t values.
		float sourceT = bg_LastTimeT;
		float targetT = newTime ? 1 : timeT;

		// Initialize background shader for use.
		bg_Shader.useProgram();
		int uInterpolators = bg_Shader.getHandle("uInterpolators");
		int uPositions = bg_Shader.getHandle("uPositions");
		int uNormal = bg_Shader.getHandle("uNormal");
		int uColor = bg_Shader.getHandle("uColor");
		int aPosition = bg_Shader.getHandle("aPosition");

		// Store interpolants.
		GLES20.glUniform2f(uInterpolators, sourceT, targetT);
		// Initiate vertex buffer.
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				bg_FillBuffer);
		GLES20.glEnableVertexAttribArray(aPosition);

		// Iterate over active fill data structs.
		for (int i = 0; i < bg_FillDataCount; ++i) {
			// Grab local reference for fill data.
			StructFillData fillData = bg_FillData[i];
			// Store fill data position and normal into shader.
			GLES20.glUniform2fv(uPositions, 2, fillData.mFillPositions, 0);
			GLES20.glUniform2fv(uNormal, 1, fillData.mFillNormal, 0);
			// Store fill data color into shader.
			GLES20.glUniform3fv(uColor, 1, fillData.mColor, 0);
			// Render fill area.
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		}

		// Finally update mLastTime and generate new animation if needed.
		if (newTime) {
			// Decrease rotation angle into range [0, 8).
			while (mRotationAngleTarget >= 8) {
				mRotationAngleTarget -= 8;
			}
			// Store rotation angle target to current value.
			mRotationAngle = mRotationAngleTarget;
			// Clear last time variable.
			bg_LastTimeT = 0;
			// Probability for generating new animation.
			if (Math.random() < 0.3) {
				bg_GenRandFillData();
			}
		} else {
			bg_LastTimeT = timeT;
		}
	}

	/**
	 * Fills box structure with random values.
	 * 
	 * @param box
	 *            Box to be adjusted.
	 */
	public void fg_GenRandBox(StructBoxData box) {
		// Set random target position.
		box.mPosTarget[0] = (float) ((Math.random() * 1.6) - 0.8);
		box.mPosTarget[1] = (float) ((Math.random() * 1.6) - 0.8);
		// Round position to 10x10 grid.
		box.mPosTarget[0] = (Math.round(box.mPosTarget[0] * 5) / 5f);
		box.mPosTarget[1] = (Math.round(box.mPosTarget[1] * 5) / 5f);
		box.mScaleTarget = (float) ((Math.random() * 0.05) + 0.05);

		// If we've hit The LoveBeat limit and are feeling lucky.
		if (fg_LoveBeat > 10 && Math.random() > 0.2) {
			fg_LoveBeat = 0;
			box.mColorTarget[0] = 0.9f;
			box.mColorTarget[1] = 0.4f;
			box.mColorTarget[2] = 0.4f;
		} else {
			float rgb = (float) (Math.random() * 0.7f) + 0.3f;
			box.mColorTarget[0] = rgb;
			box.mColorTarget[1] = rgb;
			box.mColorTarget[2] = rgb;
		}
	}

	/**
	 * Renders foreground onto current frame buffer.
	 * 
	 * @param timeT
	 *            Time interpolator, float between [0f, 1f].
	 * @param newTime
	 *            True once new [0f, 1f] timeT range is started.
	 */
	public void fg_OnDrawFrame(float timeT, boolean newTime) {
		// If we have new time span.
		if (newTime) {
			// Increase the beat.
			++fg_LoveBeat;
		}

		// Calculate final up vector value for rendering.
		double sourceAngle = (Math.PI * mRotationAngle) / 4;
		double targetAngle = (Math.PI * mRotationAngleTarget) / 4;
		double angle = sourceAngle + (targetAngle - sourceAngle) * timeT;
		// Rotate angle from right to up.
		angle -= Math.PI / 2;
		// Up direction for x and y.
		float upX = (float) Math.cos(angle) * mAspectRatio[0];
		float upY = (float) Math.sin(angle) * mAspectRatio[1];

		// Initialize foreground shader for use.
		fg_Shader.useProgram();
		int uAspectRatio = fg_Shader.getHandle("uAspectRatio");
		int uCenterPos = fg_Shader.getHandle("uCenterPos");
		int uVectorUp = fg_Shader.getHandle("uVectorUp");
		int uScale = fg_Shader.getHandle("uScale");
		int uColor = fg_Shader.getHandle("uColor");
		int aPosition = fg_Shader.getHandle("aPosition");

		GLES20.glUniform2fv(uAspectRatio, 1, mAspectRatio, 0);
		GLES20.glUniform2f(uVectorUp, upX, upY);
		// Initiate vertex buffer.
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mScreenVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		// Iterate over boxes.
		for (StructBoxData box : fg_Boxes) {
			// If we are within new time span.
			if (newTime) {
				// Copy target values into source ones.
				box.mPosSource[0] = box.mPosTarget[0];
				box.mPosSource[1] = box.mPosTarget[1];
				box.mScaleSource = box.mScaleTarget;
				box.mColorSource[0] = box.mColorTarget[0];
				box.mColorSource[1] = box.mColorTarget[1];
				box.mColorSource[2] = box.mColorTarget[2];

				// Given some probability generate current box new target
				// values. Otherwise it is being paused.
				if (Math.random() > 0.4) {
					fg_GenRandBox(box);
				}
			}

			// Interpolate scale value.
			float scale = box.mScaleSource
					+ (box.mScaleTarget - box.mScaleSource) * timeT;
			// Interpolate position values.
			float x = box.mPosSource[0]
					+ (box.mPosTarget[0] - box.mPosSource[0]) * timeT;
			float y = box.mPosSource[1]
					+ (box.mPosTarget[1] - box.mPosSource[1]) * timeT;
			// Interpolate color values.
			float r = box.mColorSource[0]
					+ (box.mColorTarget[0] - box.mColorSource[0]) * timeT;
			float g = box.mColorSource[1]
					+ (box.mColorTarget[1] - box.mColorSource[1]) * timeT;
			float b = box.mColorSource[2]
					+ (box.mColorTarget[2] - box.mColorSource[2]) * timeT;

			// Store uniform values.
			GLES20.glUniform1f(uScale, scale);
			GLES20.glUniform2f(uCenterPos, x, y);
			GLES20.glUniform3f(uColor, r, g, b);

			// Render current box.
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		}
	}

	@Override
	public synchronized void onDrawFrame(GL10 unused) {
		// If shader compiler is not supported, clear screen buffer only.
		if (mShaderCompilerSupported[0] == false) {
			GLES20.glClearColor(0, 0, 0, 1);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			return;
		}

		long currentTime = SystemClock.uptimeMillis();
		boolean newTime = false;

		// If we're following touch events stop animation timer.
		if (mTouchFollow && mTimeLast >= 0) {
			mTimeTickStart += currentTime - mTimeLast;
		} else if (mTimeLast >= 0) {
			// Adjust "current touch position" towards start touch position in
			// order to hide displacement effect. Which ends once they are
			// equal. We use interpolation for smoother transition no matter
			// what the rendering frame rate is.
			float t = Math.max(0f, 1f - (currentTime - mTimeLast) * .005f);
			mTouchPositions[2] = mTouchPositions[0]
					+ (mTouchPositions[2] - mTouchPositions[0]) * t;
			mTouchPositions[3] = mTouchPositions[1]
					+ (mTouchPositions[3] - mTouchPositions[1]) * t;
		}

		// Store current time.
		mTimeLast = currentTime;

		// If we're out of tick timer bounds.
		if (currentTime - mTimeTickStart > ANIMATION_TICK_TIME
				|| mTimeTickStart < 0) {
			mTimeTickStart = currentTime;
			newTime = true;
		}

		// Calculate time interpolator, a value between [0, 1].
		float timeT = (currentTime - mTimeTickStart)
				/ (float) ANIMATION_TICK_TIME;
		// We need only smooth Hermite interpolator.
		timeT = timeT * timeT * (3 - 2 * timeT);

		// Disable unneeded rendering flags.
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);

		/**
		 * Render scene to offscreen FBOs.
		 */
		mFbo.bind();
		// Render background.
		mFbo.bindTexture(0);
		bg_OnDrawFrame(timeT, newTime);
		// Render foreground.
		mFbo.bindTexture(1);
		// Clear foreground fbo texture only.
		GLES20.glClearColor(0, 0, 0, 0);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		fg_OnDrawFrame(timeT, newTime);

		// Copy FBOs to screen buffer.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		// Enable final copy shader.
		mShaderCopy.useProgram();
		int sTextureBg = mShaderCopy.getHandle("sTextureBg");
		int sTextureFg = mShaderCopy.getHandle("sTextureFg");
		int uTouchPos = mShaderCopy.getHandle("uTouchPos");
		int uRandom = mShaderCopy.getHandle("uRandom");
		int aPosition = mShaderCopy.getHandle("aPosition");

		// Set touch coordinates for shader.
		GLES20.glUniform2fv(uTouchPos, 2, mTouchPositions, 0);
		// Pass seed for GLSL pseudo random number generator.
		if (!mTouchFollow) {
			mRandomSeed = ((currentTime / 80) % 10) + 40f;
		}
		GLES20.glUniform1f(uRandom, mRandomSeed);
		// Enable vertex coordinate array.
		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mScreenVertices);
		GLES20.glEnableVertexAttribArray(aPosition);

		// Set up fore- and background textures.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFbo.getTexture(0));
		GLES20.glUniform1i(sTextureBg, 0);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFbo.getTexture(1));
		GLES20.glUniform1i(sTextureFg, 1);

		// Render scene to screen buffer.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Store width and height for later use.
		mWidth = width;
		mHeight = height;
		// Set viewport size.
		GLES20.glViewport(0, 0, mWidth, mHeight);
		// If shader compiler is not supported set viewport size only.
		if (mShaderCompilerSupported[0] == false) {
			return;
		}

		// Calculate aspect ratio.
		mAspectRatio[0] = Math.max(mWidth, mHeight) / (float) mWidth;
		mAspectRatio[1] = Math.max(mWidth, mHeight) / (float) mHeight;

		// Initialize two fbo screen sized textures.
		mFbo.init(mWidth, mHeight, 2);

		// Bind background texture and clear it. This is the only time we do
		// this, later on it'll be only overdrawn with background renderer.
		mFbo.bind();
		mFbo.bindTexture(0);
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
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

		// Initiate copy shader.
		mShaderCopy.setProgram(mContext.getString(R.string.shader_copy_vs),
				mContext.getString(R.string.shader_copy_fs));

		// Initialize background shader.
		bg_Shader.setProgram(mContext.getString(R.string.shader_background_vs),
				mContext.getString(R.string.shader_background_fs));
		// Initialize foreground shader.
		fg_Shader.setProgram(mContext.getString(R.string.shader_foreground_vs),
				mContext.getString(R.string.shader_foreground_fs));
	}

	/**
	 * Touch event callback method.
	 * 
	 * @param me
	 *            Current motion/touch event.
	 */
	public void onTouchEvent(MotionEvent me) {
		switch (me.getAction()) {
		// On touch down set following flag and initialize touch position start
		// and current values.
		case MotionEvent.ACTION_DOWN:
			mTouchFollow = true;
			mTouchPositions[0] = mTouchPositions[2] = me.getX() / mWidth;
			mTouchPositions[1] = mTouchPositions[3] = 1f - (me.getY() / mHeight);
			break;
		// On touch move update current position only.
		case MotionEvent.ACTION_MOVE:
			mTouchPositions[2] = me.getX() / mWidth;
			mTouchPositions[3] = 1f - (me.getY() / mHeight);
			break;
		// On touch up mark touch follow flag as false.
		case MotionEvent.ACTION_UP:
			mTouchFollow = false;
			break;
		}
	}

	/**
	 * Struct for storing box related data.
	 */
	private final class StructBoxData {
		// Box source color RGB values.
		public final float mColorSource[] = new float[3];
		// Box target color RGB values.
		public final float mColorTarget[] = new float[3];
		// Box source position values.
		public final float mPosSource[] = new float[2];
		// Box target position values.
		public final float mPosTarget[] = new float[2];
		// Box scale source and target values.
		public float mScaleSource, mScaleTarget;
	}

	/**
	 * Private fill data structure for storing source position, target position,
	 * normal and color index. Normal is stored as {x,y} tuple and positions as
	 * two {x,y} tuples.
	 */
	private final class StructFillData {
		// Fill color RGB values.
		public final float mColor[] = new float[3];
		// Normal direction.
		public final float mFillNormal[] = new float[2];
		// Source and target positions.
		public final float mFillPositions[] = new float[4];
	}

}
