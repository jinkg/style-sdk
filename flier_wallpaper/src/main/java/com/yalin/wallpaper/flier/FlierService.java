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
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import com.yalin.style.engine.WallpaperServiceProxy;

/**
 * Main wallpaper service class.
 */
public final class FlierService extends WallpaperServiceProxy {

	public FlierService(Context host) {
		super(host);
	}

	@Override
	public Engine onCreateEngine() {
		return new WallpaperEngine();
	}

	/**
	 * Private wallpaper engine implementation.
	 */
	private final class WallpaperEngine extends ActiveEngine implements
			SharedPreferences.OnSharedPreferenceChangeListener {

		// Slightly modified GLSurfaceView.
		private WallpaperGLSurfaceView mGLSurfaceView;
		private SharedPreferences mPreferences;
		private FlierRenderer mRenderer;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {

			// Uncomment for debugging.
			// android.os.Debug.waitForDebugger();

			super.onCreate(surfaceHolder);
			mRenderer = new FlierRenderer(FlierService.this);

			mPreferences = PreferenceManager
					.getDefaultSharedPreferences(FlierService.this);
			mPreferences.registerOnSharedPreferenceChangeListener(this);
			mRenderer.setPreferences(mPreferences);

			mGLSurfaceView = new WallpaperGLSurfaceView(FlierService.this);
			mGLSurfaceView.setEGLContextClientVersion(2);
			mGLSurfaceView.setRenderer(mRenderer);
			mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
			mGLSurfaceView.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mPreferences.unregisterOnSharedPreferenceChangeListener(this);
			mPreferences = null;
			mGLSurfaceView.onDestroy();
			mGLSurfaceView = null;
			mRenderer = null;
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
			mRenderer.setXOffset(xOffset);
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			mRenderer.setPreferences(sharedPreferences);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				mGLSurfaceView.onResume();
			} else {
				mGLSurfaceView.onPause();
			}
		}

		/**
		 * Lazy as I am, I din't bother using GLWallpaperService (found on
		 * GitHub) project for wrapping OpenGL functionality into my wallpaper
		 * service. Instead am using GLSurfaceView and trick it into hooking
		 * into Engine provided SurfaceHolder instead of SurfaceView provided
		 * one GLSurfaceView extends.
		 */
		private final class WallpaperGLSurfaceView extends GLSurfaceView {
			public WallpaperGLSurfaceView(Context context) {
				super(context);
			}

			@Override
			public SurfaceHolder getHolder() {
				return WallpaperEngine.this.getSurfaceHolder();
			}

			/**
			 * Should be called once underlying Engine is destroyed. Calling
			 * onDetachedFromWindow() will stop rendering thread which is lost
			 * otherwise.
			 */
			public void onDestroy() {
				super.onDetachedFromWindow();
			}
		}
	}
}
