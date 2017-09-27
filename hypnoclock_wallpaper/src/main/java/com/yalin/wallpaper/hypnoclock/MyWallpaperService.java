package com.yalin.wallpaper.hypnoclock;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yalin.style.engine.GLWallpaperServiceProxy;


public class MyWallpaperService extends GLWallpaperServiceProxy {

    public MyWallpaperService(Context host) {
        super(host);
    }

    class C1544a extends GLActiveEngine {
        MyRenderer f6019a;
        private DisplayMetrics f6021d;
        private boolean f6022e;
        private float f6023f = 0.0f;

        public C1544a(MyWallpaperService myWallpaperService) {
            super();
            this.f6021d = new DisplayMetrics();
            ((WindowManager) myWallpaperService.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(this.f6021d);
            this.f6019a = new MyRenderer();
            this.f6019a.m8559a(myWallpaperService.getApplicationContext());
            this.f6022e = false;
            this.f6019a.m8557a(0.0f, 0.0f);
            setRenderer(this.f6019a);
            setRenderMode(1);
            this.f6019a.m8560a(this.f6021d);
        }

        public void m8523a(float f, float f2, float f3) {
            if (this.f6019a != null) {
                this.f6019a.m8558a(f, f2, f3);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            if (this.f6019a != null) {
                this.f6019a.m8556a();
            }
            this.f6019a = null;
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            super.onOffsetsChanged(f, f2, f3, f4, i, i2);
            if (this.f6022e) {
                float f5 = f3 == 1.0f ? 0.0f : f3;
                if (((double) f5) >= 0.5d) {
                    f5 /= 2.0f;
                }
                if (f5 != this.f6023f) {
                    this.f6019a.m8557a(0.0f, 0.0f);
                    this.f6023f = f5;
                }
                this.f6019a.m8557a(f, f5);
            }
        }

    }

    public WallpaperService.Engine onCreateEngine() {
        return new C1544a(this);
    }
}
