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
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.yalin.style.engine.WallpaperServiceProxy;

public final class MetaballsService extends WallpaperServiceProxy {

	public MetaballsService(Context host) {
		super(host);
	}

	@Override
	public Engine onCreateEngine() {
		return new WallpaperEngine();
	}

	/**
	 * Private wallpaper engine implementation.
	 */
	private final class WallpaperEngine extends ActiveEngine {

		private MetaballsRenderer mRenderer;
		private WallpaperSurfaceView mWallpaperSurfaceView;

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {

			// Uncomment for debugging.
			// android.os.Debug.waitForDebugger();

			super.onCreate(surfaceHolder);
			mWallpaperSurfaceView = new WallpaperSurfaceView();
			mRenderer = new MetaballsRenderer(MetaballsService.this);
			mWallpaperSurfaceView.setEGLContextClientVersion(2);
			mWallpaperSurfaceView.setRenderer(mRenderer);
			mWallpaperSurfaceView
					.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

			setTouchEventsEnabled(true);
		}

		@Override
		public final void onDestroy() {
			super.onDestroy();
			mWallpaperSurfaceView.onDestroy();
			mWallpaperSurfaceView = null;
		}

		@Override
		public void onTouchEvent(MotionEvent me) {
			mRenderer.onTouchEvent(me);
		}

		@Override
		public final void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				mWallpaperSurfaceView.onResume();
				mWallpaperSurfaceView.requestRender();
			} else {
				mWallpaperSurfaceView.onPause();
			}
		}

		/**
		 * Lazy as I am, I din't bother using GLWallpaperService (found on
		 * GitHub) project for wrapping OpenGL functionality into my wallpaper
		 * service. Instead am using GLSurfaceView and trick it into hooking
		 * into Engine provided SurfaceHolder instead of SurfaceView provided
		 * one GLSurfaceView extends. For saving some bytes Renderer is
		 * implemented here too.
		 */
		private final class WallpaperSurfaceView extends GLSurfaceView {
			public WallpaperSurfaceView() {
				super(MetaballsService.this);
			}

			@Override
			public final SurfaceHolder getHolder() {
				return WallpaperEngine.this.getSurfaceHolder();
			}

			/**
			 * Should be called once underlying Engine is destroyed. Calling
			 * onDetachedFromWindow() will stop rendering thread which is lost
			 * otherwise.
			 */
			public final void onDestroy() {
				super.onDetachedFromWindow();
			}
		}
	}
}
