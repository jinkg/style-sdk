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

/**
 * Common particle data container class.
 */
public final class BotzParticle {
	// Collision time used to indicate about possible collisions.
	public long mCollisionTime;
	// If false, this particle does not have effect in collision detection.
	public boolean mEnabled;
	// Particle position.
	public final float[] mPosition = new float[2];
	// Particle radius used for collision detection.
	public float mRadius;
	// Particle velocity.
	public final float[] mVelocity = new float[2];
}
