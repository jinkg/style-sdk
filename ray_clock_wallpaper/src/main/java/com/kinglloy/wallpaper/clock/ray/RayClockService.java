package com.kinglloy.wallpaper.clock.ray;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

public class RayClockService extends WallpaperServiceProxy {

    public RayClockService(Context host) {
        super(host);
    }

    private class RaymarchingEngine extends ActiveEngine {
        private RaymarchingView raymarchingView;
        private boolean rendererHasBeenSet;

        private class RaymarchingView extends GLSurfaceView {
            public RaymarchingView(Context context) {
                super(context);
            }

            public SurfaceHolder getHolder() {
                return RaymarchingEngine.this.getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }

        private RaymarchingEngine() {
            super();
            this.rendererHasBeenSet = false;
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.raymarchingView = new RaymarchingView(RayClockService.this);
            if (((ActivityManager) RayClockService.this.getSystemService(Context.ACTIVITY_SERVICE))
                    .getDeviceConfigurationInfo().reqGlEsVersion >= 131072) {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(getNewRenderer());
            }
        }

        protected Renderer getNewRenderer() {
            return new ClockRenderer();
        }

        public void onVisibilityChanged(boolean visible) {
            if (!this.rendererHasBeenSet) {
                return;
            }
            if (visible) {
                this.raymarchingView.onResume();
            } else {
                this.raymarchingView.onPause();
            }
        }

        public void onDestroy() {
            super.onDestroy();
            this.raymarchingView.onDestroy();
        }

        protected void setRenderer(Renderer renderer) {
            this.raymarchingView.setRenderer(renderer);
            this.rendererHasBeenSet = true;
        }

        protected void setEGLContextClientVersion(int version) {
            this.raymarchingView.setEGLContextClientVersion(version);
        }

        public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
            this.raymarchingView.setPreserveEGLContextOnPause(preserveOnPause);
        }
    }

    public Engine onCreateEngine() {
        return new RaymarchingEngine();
    }
}
