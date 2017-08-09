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

package com.yalin.wallpaper.botz;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Renderer class.
 */
public final class BotzRenderer implements GLSurfaceView.Renderer {

	private static final float[] COLOR_BG = { .2f, .2f, .2f };
	private static final float[] COLOR_BORDER = { .8f, .3f, .2f };
	private static final float[] COLOR_BULLET = { .7f, .7f, .7f };
	private static final float[] COLOR_ENERGY1 = { .3f, .8f, .2f };
	private static final float[] COLOR_ENERGY2 = { .8f, .3f, .2f };
	private static final float[] COLOR_EXPLODE = { .7f, .6f, .1f };
	private static final float[] COLOR_SHIP = { .2f, .4f, .9f };

	private static final int NUM_BULLETS = 40;
	private static final int NUM_SHIPS = 30;
	private static final float RADIUS_BULLET = .01f;
	private static final float RADIUS_SHIP = .1f;

	private final Vector<Bullet> mArrBullets = new Vector<Bullet>();
	private final Vector<BotzParticle> mArrParticles = new Vector<BotzParticle>();
	private final Vector<Ship> mArrShips = new Vector<Ship>();
	private final float[] mAspectRatio = new float[2];
	private ByteBuffer mBufferQuad;
	private FloatBuffer mBufferShipLines;
	private Context mContext;
	private final float[] mMatrixM = new float[9];
	private final Matrix mMatrixModel = new Matrix();
	private final Matrix mMatrixModelView = new Matrix();
	private final Matrix mMatrixView = new Matrix();
	private final BotzShader mShaderCircle = new BotzShader();
	private final boolean[] mShaderCompilerSupport = new boolean[1];
	private final BotzShader mShaderEnergy = new BotzShader();
	private final BotzShader mShaderLine = new BotzShader();
	private final BotzSolver mSolver = new BotzSolver();
	private int mWidth, mHeight;

	/**
	 * Default constructor.
	 */
	public BotzRenderer(Context context) {
		mContext = context;

		// Full view quad buffer.
		final byte[] QUAD = { -1, 1, -1, -1, 1, 1, 1, -1 };
		mBufferQuad = ByteBuffer.allocateDirect(8);
		mBufferQuad.put(QUAD).position(0);

		// Ship triangle vertices buffer.
		final float[] SHIP_LINES = { -.4f, -.5f, 0, .7f, .4f, -.5f };
		ByteBuffer buf = ByteBuffer.allocateDirect(4 * 2 * 3);
		mBufferShipLines = buf.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mBufferShipLines.put(SHIP_LINES).position(0);

		// Particle and ship instance generation.
		for (int i = 0; i < NUM_SHIPS; ++i) {
			BotzParticle p = new BotzParticle();
			p.mRadius = RADIUS_SHIP;
			mArrParticles.add(p);

			Ship s = new Ship(p);
			mArrShips.add(s);
		}

		// Bullets generation.
		for (int i = 0; i < NUM_BULLETS; ++i) {
			mArrBullets.add(new Bullet());
		}
	}

	/**
	 * Adds 'gravity' to particles. Used for touch event handling.
	 */
	public void addGravity(float dx, float dy) {
		float t = Math.min(mWidth, mHeight) * .8f;
		dx /= t;
		dy /= t;

		for (BotzParticle p : mArrParticles) {
			p.mVelocity[0] += dx;
			p.mVelocity[1] += dy;
		}
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

		// Clear view buffer.
		GLES20.glClearColor(COLOR_BG[0], COLOR_BG[1], COLOR_BG[2], 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// If shader compiler not supported return immediately.
		if (!mShaderCompilerSupport[0]) {
			return;
		}

		// Disable unnecessary OpenGL flags.
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		long time = SystemClock.uptimeMillis();

		// Scale factor.
		float scale = 1f;
		// Scaling happens in 20sec cycles.
		long timeScale = time % 20000;
		// Calculate smooth transitions between [1f, 2f].
		if (timeScale > 17000) {
			float t = (timeScale - 17000) / 3000f;
			scale = 2f - t * t * (3 - 2 * t);
		} else if (timeScale > 10000) {
			scale = 2f;
		} else if (timeScale > 7000) {
			float t = (timeScale - 7000) / 3000f;
			scale = 1f + t * t * (3 - 2 * t);
		}

		// Calculate line width.
		float lineWidth = Math.max(1f, Math.min(mWidth, mHeight) * 0.008f);
		GLES20.glLineWidth(lineWidth * scale);

		// Set up view matrix.
		mMatrixView.setScale(mAspectRatio[0], mAspectRatio[1]);
		mMatrixView.postScale(scale, scale);

		// Animate ships.
		mSolver.animate();

		// Handle bullet movement.
		for (Bullet b : mArrBullets) {
			// How long bullet lives after shot.
			final float BULLET_LIVE_TIME = 700;
			// If lifetime exceeded generate new shot.
			if (time - b.mShootTime > BULLET_LIVE_TIME) {
				// Find random enabled ship particle.
				BotzParticle p = mArrParticles
						.get((int) (Math.random() * mArrParticles.size()));
				while (!p.mEnabled) {
					p = mArrParticles.get((int) (Math.random() * mArrParticles
							.size()));
				}
				// Calculate velocity normal.
				float len = (float) Math.sqrt(p.mVelocity[0] * p.mVelocity[0]
						+ p.mVelocity[1] * p.mVelocity[1]);
				float nx = p.mVelocity[0] / len;
				float ny = p.mVelocity[1] / len;
				// Set bullet start position in front of selected ship. Add some
				// border to avoid collision with itself.
				b.mPosStart[0] = p.mPosition[0] + nx * (RADIUS_SHIP + .01f);
				b.mPosStart[1] = p.mPosition[1] + ny * (RADIUS_SHIP + .01f);
				// Max length for shoot is of length 1.
				b.mPosEnd[0] = p.mPosition[0] + nx;
				b.mPosEnd[1] = p.mPosition[1] + ny;
				b.mShootTime = time;
			}

			// Move bullet.
			float t = (time - b.mShootTime) / 700f;
			BotzParticle p = b.mParticle;
			p.mPosition[0] = b.mPosStart[0] + (b.mPosEnd[0] - b.mPosStart[0])
					* t;
			p.mPosition[1] = b.mPosStart[1] + (b.mPosEnd[1] - b.mPosStart[1])
					* t;
		}

		// Check bullet collisions against all ships.
		for (Ship s : mArrShips) {
			// Skip disabled ships / particles.
			if (!s.mParticle.mEnabled)
				continue;
			for (Bullet b : mArrBullets) {
				if (mSolver.collide(s.mParticle, b.mParticle)) {
					// This will trigger new bullet shot.
					b.mShootTime = -1;
					// Mark ship as colliding.
					s.mParticle.mCollisionTime = time;
				}
			}

		}

		// Iterate over ships to see if there are collisions, explosions and
		// after certain amount of time restore ship back to enabled.
		for (Ship ship : mArrShips) {
			// If there was a collision during this render iteration decrease
			// ship's energy.
			if (ship.mParticle.mCollisionTime >= time) {
				ship.mEnergy -= .01f;
			}
			// If ship isn't exploding already and energy goes to zero, mark
			// ship as exploding.
			if (!ship.mExplode && ship.mEnergy <= 0f) {
				ship.mExplodeTime = time;
				ship.mParticle.mEnabled = false;
				ship.mExplode = true;
			}
			// If ship is exploding and certain amount of time has passed mark
			// ship back to enabled.
			if (ship.mExplode && time - ship.mExplodeTime > 5000) {
				ship.mEnergy = 1.0f;
				ship.mParticle.mEnabled = true;
				ship.mExplode = false;
				ship.mVisible = true;
			}
		}

		// Rendering calls.
		renderBullets(mShaderCircle);
		renderShipBorders(mShaderCircle, time);
		renderShipEnergies(mShaderEnergy, time);
		renderShipLines(mShaderLine);
		renderShipExplosions(mShaderCircle, time);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		mWidth = width;
		mHeight = height;

		GLES20.glViewport(0, 0, mWidth, mHeight);

		// Initialize solver with particles and view rectangle.
		float dx = (float) Math.max(mWidth, mHeight) / mHeight;
		float dy = (float) Math.max(mWidth, mHeight) / mWidth;
		mSolver.init(mArrParticles, new RectF(-dx, dy, dx, -dy));

		// Store view aspect ratio.
		mAspectRatio[0] = 1f / dx;
		mAspectRatio[1] = 1f / dy;

		// Initialize ships to initial state.
		for (Ship ship : mArrShips) {
			ship.mEnergy = 1f;
			ship.mVisible = true;
			ship.mExplode = false;
		}
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Check if shader compiler is supported.
		GLES20.glGetBooleanv(GLES20.GL_SHADER_COMPILER, mShaderCompilerSupport,
				0);

		// If not, show user an error message and return immediately.
		if (mShaderCompilerSupport[0] == false) {
			String msg = mContext.getString(R.string.error_shader_compiler);
			showError(msg);
			return;
		}

		// Load vertex and fragment shaders.
		try {
			String vertexSource, fragmentSource;
			vertexSource = loadRawString(R.raw.line_vs);
			fragmentSource = loadRawString(R.raw.line_fs);
			mShaderLine.setProgram(vertexSource, fragmentSource);
			vertexSource = loadRawString(R.raw.energy_vs);
			fragmentSource = loadRawString(R.raw.energy_fs);
			mShaderEnergy.setProgram(vertexSource, fragmentSource);
			vertexSource = loadRawString(R.raw.circle_vs);
			fragmentSource = loadRawString(R.raw.circle_fs);
			mShaderCircle.setProgram(vertexSource, fragmentSource);
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	/**
	 * Renders bullets on current FBO.
	 */
	private void renderBullets(BotzShader shader) {
		shader.useProgram();
		int uModelViewM = shader.getHandle("uModelViewM");
		int uColor = shader.getHandle("uColor");
		int uLimits = shader.getHandle("uLimits");
		int aPosition = shader.getHandle("aPosition");

		GLES20.glUniform3fv(uColor, 1, COLOR_BULLET, 0);
		GLES20.glUniform2f(uLimits, 0, 2);

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		for (Bullet b : mArrBullets) {

			BotzParticle p = b.mParticle;
			mMatrixModel.setScale(RADIUS_BULLET, RADIUS_BULLET);
			mMatrixModel.postTranslate(p.mPosition[0], p.mPosition[1]);
			mMatrixModelView.set(mMatrixModel);
			mMatrixModelView.postConcat(mMatrixView);
			mMatrixModelView.getValues(mMatrixM);
			GLES20.glUniformMatrix3fv(uModelViewM, 1, false, mMatrixM, 0);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		}
	}

	/**
	 * Renders ship borders into current FBO.
	 */
	private void renderShipBorders(BotzShader shader, long time) {
		shader.useProgram();
		int uModelViewM = shader.getHandle("uModelViewM");
		int uColor = shader.getHandle("uColor");
		int uLimits = shader.getHandle("uLimits");
		int aPosition = shader.getHandle("aPosition");

		GLES20.glUniform2f(uLimits, 0.85f, 1.0f);

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		final float[] color = new float[3];

		for (Ship ship : mArrShips) {
			if (!ship.mVisible)
				continue;
			BotzParticle p = ship.mParticle;
			// Borders show for certain amount of time only.
			float ct = (time - p.mCollisionTime) / 200f;
			if (ct < 1f) {
				mMatrixModel.setScale(RADIUS_SHIP, RADIUS_SHIP);
				mMatrixModel.postTranslate(p.mPosition[0], p.mPosition[1]);
				mMatrixModelView.set(mMatrixModel);
				mMatrixModelView.postConcat(mMatrixView);
				mMatrixModelView.getValues(mMatrixM);

				for (int i = 0; i < 3; ++i) {
					color[i] = COLOR_BORDER[i]
							+ (COLOR_BG[i] - COLOR_BORDER[i]) * ct;
				}

				GLES20.glUniformMatrix3fv(uModelViewM, 1, false, mMatrixM, 0);
				GLES20.glUniform3fv(uColor, 1, color, 0);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			}
		}
	}

	/**
	 * Renders ship energy indicators.
	 */
	private void renderShipEnergies(BotzShader shader, long time) {
		shader.useProgram();
		int uModelViewM = shader.getHandle("uModelViewM");
		int uColor1 = shader.getHandle("uColor1");
		int uColor2 = shader.getHandle("uColor2");
		int uEnergy = shader.getHandle("uEnergy");
		int aPosition = shader.getHandle("aPosition");

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		final float[] color1 = new float[3];
		final float[] color2 = new float[3];
		for (Ship ship : mArrShips) {
			if (!ship.mVisible)
				continue;
			BotzParticle p = ship.mParticle;
			// Energy shows only for certain amount of time.
			float ct = (time - p.mCollisionTime) / 400f;
			if (ct < 1f) {
				mMatrixModel.setScale(1f, .1f);
				mMatrixModel.postTranslate(0f, -.9f);
				mMatrixModel.postScale(RADIUS_SHIP, RADIUS_SHIP);
				mMatrixModel.postTranslate(p.mPosition[0], p.mPosition[1]);
				mMatrixModelView.set(mMatrixModel);
				mMatrixModelView.postConcat(mMatrixView);
				mMatrixModelView.getValues(mMatrixM);

				for (int i = 0; i < 3; ++i) {
					color1[i] = COLOR_ENERGY1[i]
							+ (COLOR_BG[i] - COLOR_ENERGY1[i]) * ct;
					color2[i] = COLOR_ENERGY2[i]
							+ (COLOR_BG[i] - COLOR_ENERGY2[i]) * ct;
				}

				GLES20.glUniformMatrix3fv(uModelViewM, 1, false, mMatrixM, 0);
				GLES20.glUniform1f(uEnergy, ship.mEnergy);
				GLES20.glUniform3fv(uColor1, 1, color1, 0);
				GLES20.glUniform3fv(uColor2, 1, color2, 0);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			}
		}
	}

	/**
	 * Renders ship explosions into current FBO.
	 */
	private void renderShipExplosions(BotzShader shader, long time) {
		shader.useProgram();
		int uModelViewM = shader.getHandle("uModelViewM");
		int uColor = shader.getHandle("uColor");
		int uLimits = shader.getHandle("uLimits");
		int aPosition = shader.getHandle("aPosition");

		GLES20.glUniform3fv(uColor, 1, COLOR_EXPLODE, 0);

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0,
				mBufferQuad);
		GLES20.glEnableVertexAttribArray(aPosition);

		for (Ship ship : mArrShips) {
			if (!ship.mVisible || !ship.mExplode)
				continue;
			BotzParticle p = ship.mParticle;
			// Explosion lasts only for certain amount of time.
			float ct = (time - ship.mExplodeTime) / 800f;
			if (ct < 1f) {
				mMatrixModel.setScale(RADIUS_SHIP * 1.5f, RADIUS_SHIP * 1.5f);
				mMatrixModel.postTranslate(p.mPosition[0], p.mPosition[1]);
				mMatrixModelView.set(mMatrixModel);
				mMatrixModelView.postConcat(mMatrixView);
				mMatrixModelView.getValues(mMatrixM);

				GLES20.glUniformMatrix3fv(uModelViewM, 1, false, mMatrixM, 0);
				GLES20.glUniform2f(uLimits, 0, ct);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			}
			// Hide ship after explosion is done.
			else {
				ship.mVisible = false;
			}
		}
	}

	/**
	 * Renders actual ship into current FBO.
	 */
	private void renderShipLines(BotzShader shader) {
		shader.useProgram();
		int uModelViewM = shader.getHandle("uModelViewM");
		int uColor = shader.getHandle("uColor");
		int aPosition = shader.getHandle("aPosition");

		GLES20.glUniform3fv(uColor, 1, COLOR_SHIP, 0);

		GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 0,
				mBufferShipLines);
		GLES20.glEnableVertexAttribArray(aPosition);

		for (Ship ship : mArrShips) {
			if (!ship.mVisible)
				continue;
			BotzParticle p = ship.mParticle;

			double tan = Math.atan2(-p.mVelocity[0], p.mVelocity[1]);
			mMatrixModel.setScale(RADIUS_SHIP, RADIUS_SHIP);
			mMatrixModel.postRotate((float) (tan * 180 / Math.PI));
			mMatrixModel.postTranslate(p.mPosition[0], p.mPosition[1]);
			mMatrixModelView.set(mMatrixModel);
			mMatrixModelView.postConcat(mMatrixView);
			mMatrixModelView.getValues(mMatrixM);

			GLES20.glUniformMatrix3fv(uModelViewM, 1, false, mMatrixM, 0);
			GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, 3);
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
	 * Private bullet info holder class.
	 */
	private final class Bullet {
		public final BotzParticle mParticle = new BotzParticle();
		public final float[] mPosEnd = new float[2];
		public final float[] mPosStart = new float[2];
		public long mShootTime;
	}

	/**
	 * Private ship info holder class.
	 */
	private final class Ship {
		public float mEnergy;
		public boolean mExplode;
		public long mExplodeTime;
		public BotzParticle mParticle;
		public boolean mVisible;

		public Ship(BotzParticle particle) {
			mParticle = particle;
		}
	}

}
