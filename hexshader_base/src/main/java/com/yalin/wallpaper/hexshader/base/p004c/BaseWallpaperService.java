package com.yalin.wallpaper.hexshader.base.p004c;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;
import com.yalin.wallpaper.hexshader.base.p000a.p002b.C0007c;

public abstract class BaseWallpaperService extends WallpaperServiceProxy {
    public BaseWallpaperService(Context host) {
        super(host);
    }

    public abstract C0017d mo2a(GLSurfaceView gLSurfaceView);

    public Engine onCreateEngine() {
        return new HexEngine();
    }

    public class HexEngine extends ActiveEngine {
        boolean f35a = false;
        private GLSurfaceView f37c = null;
        private C0007c f38d = new C0007c();
        private C0017d f39e;
        private float f40f = 0.0f;
        private int f41g = 0;

        public HexEngine() {
        }

        private void m35a() {
            this.f35a = false;
            this.f40f = 0.0f;
            this.f41g = 0;
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            if (this.f35a) {
                if (f < 0.0f || f > 1.0f) {
                    this.f39e.mo3a(0.0f, 0.0f);
                } else {
                    this.f39e.mo3a(f - 0.5f, 0.0f);
                }
            } else if (this.f41g > 3) {
                this.f35a = true;
                this.f39e.mo3a(f - 0.5f, 0.0f);
            } else if (Math.abs(f - this.f40f) > 0.001f) {
                this.f41g++;
                this.f40f = f;
            } else {
                this.f41g = 0;
            }
        }

        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            this.f37c = new C0016c(BaseWallpaperService.this);
            this.f39e = BaseWallpaperService.this.mo2a(this.f37c);
            this.f37c.setRenderMode(0);
        }

        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            this.f37c.surfaceDestroyed(surfaceHolder);
        }

        public void onVisibilityChanged(boolean z) {
            if (z) {
                m35a();
                this.f37c.onResume();
                this.f38d.m21a(this.f37c);
                return;
            }
            this.f38d.m20a();
            this.f37c.onPause();
        }

        class C0016c extends GLSurfaceView {

            C0016c(Context context) {
                super(context);
            }

            public SurfaceHolder getHolder() {
                return HexEngine.this.getSurfaceHolder();
            }
        }
    }
}
