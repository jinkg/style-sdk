package com.yalin.wallpaper.hexshader.base.hexshaders;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

import com.yalin.wallpaper.hexshader.base.p000a.p001a.C0003c;
import com.yalin.wallpaper.hexshader.base.p000a.p002b.C0005a;
import com.yalin.wallpaper.hexshader.base.p000a.p002b.C0006b;
import com.yalin.wallpaper.hexshader.base.p003b.C0009a;
import com.yalin.wallpaper.hexshader.base.p003b.C0010b;
import com.yalin.wallpaper.hexshader.base.p003b.C0011c;
import com.yalin.wallpaper.hexshader.base.p003b.C0012d;
import com.yalin.wallpaper.hexshader.base.p004c.C0017d;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HexRenderer implements Renderer, C0011c, C0017d {
    private static float f60o = 0.0f;
    private float f61a;
    private AssetManager f62b;
    private String f63c;
    private C0010b f64d = new C0010b(this);
    private C0003c f65e;
    private C0023f f66f;
    private float f67g;
    private float f68h;
    private float f69i;
    private float f70j;
    private float f71k;
    private float f72l;
    private int f73m = 0;
    private FloatBuffer f74n;
    private long f75p = SystemClock.elapsedRealtime();
    private float f76q;
    private float f77r = 0.0f;

    public HexRenderer(AssetManager assetManager, String str, float f, float f2) {
        this.f62b = assetManager;
        this.f63c = str;
        this.f76q = f2;
        this.f61a = f;
    }

    public void mo3a(float f, float f2) {
        this.f77r = f;
    }

    public void mo4a(C0010b c0010b) {
    }

    public void onDrawFrame(GL10 gl10) {
        this.f64d.m25a();
        gl10.glClear(16384);
        this.f66f.m4a();
        f60o += (((float) (SystemClock.elapsedRealtime() - this.f75p)) * 0.001f) * this.f76q;
        if (f60o > 600.0f) {
            f60o = 0.0f;
        }
        this.f75p = SystemClock.elapsedRealtime();
        this.f66f.m51a(this.f72l, f60o, 0, this.f69i, this.f70j, this.f77r * this.f67g);
        this.f66f.m53a(this.f74n);
        this.f66f.m52a(this.f73m);
        this.f64d.m26b();
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        gl10.glViewport(0, 0, i, i2);
        this.f71k = 2.0f / this.f61a;
        if (i < i2) {
            this.f67g = 1.0f;
            this.f68h = ((float) i) / ((float) i2);
            this.f72l = (((float) i) * 0.9f) / this.f61a;
        } else {
            this.f67g = ((float) i2) / ((float) i);
            this.f68h = 1.0f;
            this.f72l = (((float) i2) * 0.9f) / this.f61a;
        }
        this.f69i = (float) i;
        this.f70j = (float) i2;
        this.f73m = 0;
        int i3 = (int) ((((float) i) * 0.7f) / this.f72l);
        int i4 = (int) ((((float) i2) * 0.7f) / this.f72l);
        this.f74n = C0005a.m11a((((i4 * 2) + 1) * 3) * ((i3 * 2) + 1));
        this.f74n.position(0);
        for (int i5 = -i4; i5 <= i4; i5++) {
            for (int i6 = (-i3) - (i5 / 2); i6 <= i3 - (i5 / 2); i6++) {
                float a = (this.f67g * C0012d.m29a(i6, i5)) * this.f71k;
                float a2 = (this.f68h * C0012d.m28a(i5)) * this.f71k;
                if (Math.abs(a) <= this.f71k + 1.0f && Math.abs(a2) <= this.f71k + 1.0f) {
                    this.f74n.put(a);
                    this.f74n.put(a2);
                    this.f74n.put(C0006b.f6b);
                    this.f73m++;
                }
            }
        }
        GLES20.glDisable(2929);
        GLES20.glEnable(3553);
        GLES20.glActiveTexture(33984);
        this.f65e.m9a();
        C0003c.m6a(9729, 9729);
        C0003c.m8b(33071, 33071);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.f65e = new C0003c(C0009a.m24b("hex.png", this.f62b));
        this.f66f = new C0023f(this.f62b, this.f63c);
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
