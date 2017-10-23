package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

public abstract class GLWallpaperService extends WallpaperServiceProxy {
    public GLWallpaperService(Context host) {
        super(host);
    }

    public class C0182c extends ActiveEngine {
        final /* synthetic */ GLWallpaperService f803a;
        private C0183d f804b;

        public C0182c(GLWallpaperService gLWallpaperService) {
            super();
            this.f803a = gLWallpaperService;
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            super.onOffsetsChanged(f, f2, f3, f4, i, i2);
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.f804b = new C0183d(this.f803a);
        }

        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                this.f804b.onResume();
            } else {
                this.f804b.onPause();
            }
        }

        public void onDestroy() {
            super.onDestroy();
            this.f804b.m1078a();
        }

        protected void m1075a(GLSurfaceView.Renderer renderer) {
            this.f804b.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            this.f804b.getHolder().setFormat(-3);
            this.f804b.setRenderer(renderer);
            this.f804b.setRenderMode(0);
        }

        protected void m1076b(int i) {
            this.f804b.setEGLContextClientVersion(i);
        }

        protected GLSurfaceView m1077d() {
            return this.f804b;
        }

        class C0183d extends GLSurfaceView {

            C0183d(Context context) {
                super(context);
            }

            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void m1078a() {
                super.onDetachedFromWindow();
            }
        }
    }


}
