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

import android.graphics.RectF;
import android.os.SystemClock;
import java.util.Vector;

/**
 * Particle solver class.
 */
public final class BotzSolver {

	private Vector<BotzParticle> mParticles;
	private long mTimeLast;
	private final RectF mViewRect = new RectF(-1, 1, 1, -1);

	/**
	 * Animate method.
	 */
	public void animate() {

		long timeCurrent = SystemClock.uptimeMillis();
		if (mTimeLast == -1) {
			mTimeLast = timeCurrent;
		}
		float time = (timeCurrent - mTimeLast) / 1000f;
		mTimeLast = timeCurrent;

		// Calculate force field changes.
		for (int i = 0; i < mParticles.size(); ++i) {
			BotzParticle p0 = mParticles.get(i);
			if (!p0.mEnabled)
				continue;

			for (int j = 0; j < mParticles.size(); ++j) {
				BotzParticle p1 = mParticles.get(j);
				if (i == j || !p1.mEnabled)
					continue;

				float dx = p1.mPosition[0] - p0.mPosition[0];
				float dy = p1.mPosition[1] - p0.mPosition[1];
				float dist = (float) Math.sqrt(dx * dx + dy * dy);

				final float DIST_FORCE = .4f;
				if (dist < DIST_FORCE) {
					float fx = (1f - dx / DIST_FORCE) * time * .3f;
					float fy = (1f - dy / DIST_FORCE) * time * .3f;

					p0.mVelocity[0] += fx;
					p0.mVelocity[1] += fy;
					p1.mVelocity[0] -= fx;
					p1.mVelocity[1] -= fy;
				}
			}
		}

		// Avoid collisions with walls.
		for (int i = 0; i < mParticles.size(); ++i) {
			BotzParticle p = mParticles.get(i);
			if (!p.mEnabled)
				continue;

			final float DIST_AVOID = .2f;
			float distLeft = Math.abs(p.mPosition[0] - mViewRect.left);
			float distRight = Math.abs(p.mPosition[0] - mViewRect.right);
			float distTop = Math.abs(p.mPosition[1] - mViewRect.top);
			float distBottom = Math.abs(p.mPosition[1] - mViewRect.bottom);
			if (distLeft < DIST_AVOID)
				p.mVelocity[0] += (1f - distLeft / DIST_AVOID) * time;
			if (distRight < DIST_AVOID)
				p.mVelocity[0] -= (1f - distRight / DIST_AVOID) * time;
			if (distTop < DIST_AVOID)
				p.mVelocity[1] -= (1f - distTop / DIST_AVOID) * time;
			if (distBottom < DIST_AVOID) {
				p.mVelocity[1] += (1f - distBottom / DIST_AVOID) * time;
			}
		}

		// Move particles and do bounds check.
		for (BotzParticle p : mParticles) {
			if (!p.mEnabled)
				continue;

			p.mPosition[0] += p.mVelocity[0] * time;
			p.mPosition[1] += p.mVelocity[1] * time;

			if (p.mPosition[0] < mViewRect.left) {
				p.mPosition[0] = mViewRect.left;
				p.mVelocity[0] = -p.mVelocity[0] * .5f;
				p.mCollisionTime = timeCurrent;
			}
			if (p.mPosition[0] > mViewRect.right) {
				p.mPosition[0] = mViewRect.right;
				p.mVelocity[0] = -p.mVelocity[0] * .5f;
				p.mCollisionTime = timeCurrent;
			}
			if (p.mPosition[1] > mViewRect.top) {
				p.mPosition[1] = mViewRect.top;
				p.mVelocity[1] = -p.mVelocity[1] * .5f;
				p.mCollisionTime = timeCurrent;
			}
			if (p.mPosition[1] < mViewRect.bottom) {
				p.mPosition[1] = mViewRect.bottom;
				p.mVelocity[1] = -p.mVelocity[1] * .5f;
				p.mCollisionTime = timeCurrent;
			}
		}

		// Finally apply collision detection.
		for (int i = 0; i < mParticles.size(); ++i) {
			BotzParticle p0 = mParticles.get(i);
			if (!p0.mEnabled)
				continue;

			for (int j = i + 1; j < mParticles.size(); ++j) {
				BotzParticle p1 = mParticles.get(j);
				if (i == j || !p1.mEnabled)
					continue;

				if (collide(p0, p1)) {
					float dx = p0.mPosition[0] - p1.mPosition[0];
					float dy = p0.mPosition[1] - p1.mPosition[1];
					float dist = (float) Math.sqrt(dx * dx + dy * dy);

					float nx = dx / dist;
					float ny = dy / dist;

					float x1 = nx * p0.mVelocity[0] + ny * p0.mVelocity[1];
					float x2 = -nx * p1.mVelocity[0] - ny * p1.mVelocity[1];

					float vx1 = nx * x1;
					float vy1 = ny * x1;
					float vx2 = -nx * x2;
					float vy2 = -ny * x2;

					p0.mVelocity[0] = vx2 + p0.mVelocity[0] - vx1;
					p0.mVelocity[1] = vy2 + p0.mVelocity[1] - vy1;

					p1.mVelocity[0] = vx1 + p1.mVelocity[0] - vx2;
					p1.mVelocity[1] = vy1 + p1.mVelocity[1] - vy2;

					float dt = (p0.mRadius + p1.mRadius + .0001f) / dist;
					p1.mPosition[0] = p0.mPosition[0] - dx * dt;
					p1.mPosition[1] = p0.mPosition[1] - dy * dt;

					p0.mCollisionTime = timeCurrent;
					p1.mCollisionTime = timeCurrent;
				}
			}
		}
	}

	/**
	 * Returns true if two particles collide.
	 */
	public boolean collide(BotzParticle p0, BotzParticle p1) {
		float dx = p1.mPosition[0] - p0.mPosition[0];
		float dy = p1.mPosition[1] - p0.mPosition[1];
		float r = p0.mRadius + p1.mRadius;
		return dx * dx + dy * dy < r * r;
	}

	/**
	 * Initialize method.
	 */
	public void init(Vector<BotzParticle> particles, RectF viewRect) {
		mParticles = particles;
		mViewRect.set(viewRect);

		mTimeLast = -1;

		for (int i = 0; i < mParticles.size(); ++i) {
			BotzParticle p = mParticles.get(i);
			p.mPosition[0] = rand(mViewRect.left, mViewRect.right);
			p.mPosition[1] = rand(mViewRect.bottom, mViewRect.top);
			p.mVelocity[0] = rand(-.5f, .5f);
			p.mVelocity[1] = rand(-.5f, .5f);
			p.mEnabled = true;
		}
	}

	/**
	 * Generates random value between [min, max).
	 */
	private float rand(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}

}
