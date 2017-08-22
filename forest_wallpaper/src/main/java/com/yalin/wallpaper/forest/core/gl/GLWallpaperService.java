package com.yalin.wallpaper.forest.core.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;
import com.yalin.wallpaper.forest.core.ForestRenderer;
import com.yalin.wallpaper.forest.core.Weather;

public class GLWallpaperService extends WallpaperServiceProxy {
    private static final String TAG = "GLWallpaperService";

    public GLWallpaperService(Context host) {
        super(host);
    }

    protected class GLEngine extends ActiveEngine {
        private WallpaperGLSurfaceView glSurfaceView;
        private ForestRenderer renderer;
        private boolean rendererHasBeenSet;

        class WallpaperGLSurfaceView extends GLSurfaceView {
            WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            public SurfaceHolder getHolder() {
                return GLEngine.this.getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }

            public void setRenderer(Renderer renderer) {
                super.setRenderer(renderer);
            }
        }

        protected GLEngine() {
            super();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            this.glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
        }

        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (this.rendererHasBeenSet) {
                this.renderer.onVisibilityChanged(visible);
                if (visible) {
                    if (Weather.INSTANCE.update()) {
                        renderer.updateLayers();
                    }
                    this.glSurfaceView.onResume();
                } else if (!isPreview()) {
                    this.glSurfaceView.onPause();
                }
            }
        }

        public void onDestroy() {
            super.onDestroy();
            this.glSurfaceView.onDestroy();
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.glSurfaceView.surfaceChanged(holder, format, width, height);
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.glSurfaceView.surfaceCreated(holder);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.glSurfaceView.surfaceDestroyed(holder);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            this.renderer.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }

        public void onTouchEvent(MotionEvent event) {
            this.renderer.onTouchEvent(event);
        }

        protected void setRenderer(Renderer renderer) {
            this.renderer = (ForestRenderer) renderer;
            MultisampleConfigChooser chooser = new MultisampleConfigChooser();
            this.glSurfaceView.setEGLConfigChooser(chooser);
            this.renderer.multisampling(true, chooser.usesCoverageAa());
            this.glSurfaceView.setRenderer(renderer);
            this.rendererHasBeenSet = true;
        }

        protected void setEGLContextClientVersion(int version) {
            this.glSurfaceView.setEGLContextClientVersion(version);
        }

        protected void setPreserveEGLContextOnPause(boolean preserve) {
            if (VERSION.SDK_INT >= 11) {
                this.glSurfaceView.setPreserveEGLContextOnPause(preserve);
            }
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public Engine onCreateEngine() {
        return new GLEngine();
    }
}
