package com.gtp.nextlauncher.liverpaper.tunnelbate;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.Cube;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.CubesModel;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.MatrixState;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.ModelData;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.ModelObject;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.Point;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.CalculateUtil;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.ImageUtil;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: TunnelRender */
public class TunnelRender implements Renderer {
    private float f728A = 0.0f;
    private long f729B = 0;
    private int f730C = 0;
    private GLSurfaceView f731D;
    private boolean f732E = false;
    private boolean f733F = false;
    private long f734G;
    private C0135h f735H;
    private boolean f736I = true;
    private boolean f737J = true;
    private long f738K = 0;
    private float f739L;
    private boolean f740M = false;
    private int f741N = 1;
    private float f742O = 0.0f;
    private float f743P = 0.0f;
    private float f744Q = 0.0f;
    private float f745R;
    private float f746S;
    private Context f747a;
    private ModelObject f748b;
    private ModelObject f749c;
    private CubesModel f750d;
    private int f751e;
    private int f752f;
    private float f753g;
    private boolean f754h = false;
    private float f755i = 0.0f;
    private float f756j = 0.0f;
    private float f757k = 0.0f;
    private float f758l;
    private float f759m;
    private float f760n;
    private Cube f761o;
    private int f762p;
    private int f763q;
    private float f764r;
    private Point f765s = new Point();
    private boolean f766t = false;
    private long f767u = 0;
    private float f768v = 0.0f;
    private float f769w = 0.0f;
    private long f770x = 6000;
    private float f771y = 0.0f;
    private Point f772z = null;

    public void m1046a(GLSurfaceView gLSurfaceView) {
        this.f731D = gLSurfaceView;
    }

    public TunnelRender(Context context, ModelData modelData, ModelData modelData2) {
        this.f747a = context;
        this.f748b = new ModelObject(modelData);
        this.f750d = new CubesModel(modelData2);
        this.f750d.m1074a(this.f748b.m1114j());
    }

    private void m1037f() {
        this.f734G = SystemClock.uptimeMillis();
        if (this.f735H != null) {
            this.f735H.mo87a(this.f734G);
        }
    }

    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16640);
        m1037f();
        if (this.f748b != null) {
            MatrixState.m1085b();
            this.f748b.m1098a(this.f752f);
            MatrixState.m1087c();
        }
        if (this.f749c != null) {
            MatrixState.m1085b();
            if (!this.f732E) {
                this.f749c.m1096a(this.f748b.m1103b());
                this.f732E = true;
            }
            if (!this.f749c.m1102a()) {
                this.f749c.m1100a(this.f748b.m1112h(), this.f748b.m1113i(), this.f748b.m1106c(), this.f748b.m1110f(), this.f748b.m1109e(), this.f748b.m1111g());
            }
            this.f749c.m1098a(this.f752f);
            MatrixState.m1087c();
        }
        if (this.f761o != null) {
            MatrixState.m1085b();
            m1053c();
            MatrixState.m1080a(this.f765s.f891a, this.f765s.f892b, this.f765s.f893c - 0.5f);
            if (this.f754h) {
                MatrixState.m1081a(this.f755i, 1.0f, 0.0f, 0.0f);
                MatrixState.m1081a(this.f756j, 0.0f, 1.0f, 0.0f);
                MatrixState.m1081a(this.f757k, 0.0f, 0.0f, 1.0f);
                this.f761o.m1066a(this.f751e, null, false, false, 0.0f);
                this.f755i += 6.0f;
                this.f756j += 8.0f;
                this.f757k += 10.0f;
            }
            if (this.f766t) {
                float i = m1040i();
                float f = this.f772z.f891a;
                float f2 = this.f772z.f892b;
                float f3 = this.f772z.f893c;
                MatrixState.m1081a(this.f728A, 0.0f, 0.0f, 1.0f);
                MatrixState.m1080a(f, f2, f3);
                MatrixState.m1081a(i, 0.0f, 1.0f, 0.0f);
                MatrixState.m1080a(-f, -f2, -f3);
                if (Math.abs(i) < 10.0f) {
                    MatrixState.m1086b(this.f758l, this.f759m, this.f760n);
                    this.f758l = (float) (((double) this.f758l) + 0.2d);
                    this.f759m = (float) (((double) this.f759m) + 0.2d);
                    this.f760n = (float) (((double) this.f760n) + 0.2d);
                } else {
                    MatrixState.m1086b(this.f758l, this.f759m, this.f760n);
                }
                this.f761o.m1066a(this.f751e, this.f772z, true, true, this.f771y);
            }
            MatrixState.m1087c();
        }
        if (this.f750d != null) {
            GLES20.glDisable(2884);
            MatrixState.m1085b();
            this.f750d.m1072a(this.f751e);
            MatrixState.m1087c();
            GLES20.glEnable(2884);
        }
        if (this.f740M) {
            m1038g();
        }
        if (this.f754h || this.f766t || TunnelConfig.autoRun) {
            this.f731D.requestRender();
        }
        m1041j();
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        m1036a(i, i2);
        GLES20.glViewport(0, 0, i, i2);
        float f = ((float) i) / ((float) i2);
        this.f764r = f;
        MatrixState.m1082a(-f, f, -1.0f, 1.0f, 2.0f, 560.0f);
        MatrixState.m1083a(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);
    }

    private void m1036a(int i, int i2) {
        this.f762p = i;
        this.f763q = i2;
        this.f736I = i < i2;
        if (this.f736I != this.f737J) {
            this.f761o.m1068a(this.f736I);
        }
        this.f737J = this.f736I;
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(2929);
        GLES20.glEnable(2884);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glFrontFace(2305);
        MatrixState.m1079a();
        this.f748b.m1099a(this.f747a);
        this.f750d.m1073a(this.f747a);
        m1035a(TunnelConfig.sceneId, TunnelConfig.starId);
        this.f731D.requestRender();
    }

    private void m1035a(int sceneId, int starId) {
        String str = "gd_scene_" + sceneId + ".png";
        String str2 = "star_" + starId + ".png";
        this.f752f = ImageUtil.m923a(str, this.f747a);
        this.f751e = ImageUtil.m923a(str2, this.f747a);
        this.f761o = new Cube(this.f747a);
    }

    public void m1049a(Point point) {
        float f = point.f891a;
        float c = CalculateUtil.getSensorSpeed();
        this.f753g = (float) (((1.0d - ((double) c)) * ((double) this.f753g)) + ((double) (f * c)));
        if (this.f748b != null) {
            this.f748b.m1097a(this.f753g, this.f736I);
        }
        if (this.f749c != null) {
            this.f749c.m1097a(this.f753g, this.f736I);
        }
        if (this.f761o != null) {
            f = ((this.f736I ? -65.0f : -45.0f) * this.f753g) / 9.8f;
            if (!this.f736I) {
                f = -f;
            }
            this.f728A = f;
        }
        if (this.f750d != null) {
            this.f750d.m1071a(this.f753g, this.f736I);
        }
        if (!this.f740M) {
            this.f731D.requestRender();
        }
    }

    public void m1042a() {
        if (this.f748b != null) {
            this.f748b.m1108d();
        }
        if (this.f749c != null) {
            this.f749c.m1108d();
        }
        if (this.f750d != null) {
            this.f750d.m1069a();
        }
    }

    public void m1045a(float f, float f2, boolean z) {
        this.f754h = true;
        if (!z) {
            float[] a = CalculateUtil.m916a(f, f2, (float) this.f762p, (float) this.f763q, this.f764r, 1.0f, 2.0f, 560.0f);
            this.f765s.f891a = a[0];
            this.f765s.f892b = a[1];
            this.f765s.f893c = a[2];
        }
        this.f731D.requestRender();
    }

    public void m1044a(float f, float f2) {
        if (f != 0.0f && f2 != 0.0f) {
            float[] a = CalculateUtil.m916a(f, f2, (float) this.f762p, (float) this.f763q, this.f764r, 1.0f, 2.0f, 560.0f);
            this.f765s.f891a = a[0];
            this.f765s.f892b = a[1];
            this.f765s.f893c = a[2];
            this.f731D.requestRender();
        }
    }

    public void m1052b(float f, float f2) {
        this.f754h = false;
        this.f758l = 1.0f;
        this.f759m = 1.0f;
        this.f760n = 1.0f;
        m1039h();
        this.f731D.requestRender();
    }

    public void m1051b() {
        this.f754h = false;
    }

    private float m1038g() {
        float max = Math.max(0.0f, Math.min(((float) (AnimationUtils.currentAnimationTimeMillis() - this.f738K)) / this.f739L, 1.0f));
        if (max == 1.0f) {
            this.f740M = false;
            this.f743P = 0.0f;
            this.f742O = 0.0f;
            if (this.f735H != null) {
                this.f735H.mo86a(4, 1, max * max);
            }
        }
        float b = m1050b(max);
        if (this.f735H != null) {
            this.f735H.mo86a(4, 0, max);
        }
        this.f742O = (1.0f * b) * ((float) this.f741N);
        if (this.f743P == 1.0f || this.f743P == -1.0f) {
            this.f743P = 0.0f;
            max = 0.0f;
        } else {
            max = this.f743P;
        }
        this.f743P = max;
        if (this.f743P != 0.0f) {
            max = this.f742O - this.f743P;
        } else {
            max = 0.0f;
        }
        this.f743P = this.f742O;
        int r1 = 0;
        if (this.f744Q * max >= 0.0f) {
            r1 = 1;
        } else {
            boolean z = false;
        }
        max *= -10.0f;
        if (!(this.f748b == null || r1 == 0)) {
            this.f748b.m1107c(max);
        }
        if (!(this.f749c == null || r1 == 0)) {
            this.f749c.m1107c(max);
        }
        if (!(this.f750d == null || r1 == 0)) {
            this.f750d.m1070a(CalculateUtil.getStarSpeed() * (8.0f * max));
        }
        this.f731D.requestRender();
        return max;
    }

    public void m1043a(float f) {
        if (Math.abs(f) >= 130.0f) {
            this.f744Q = f;
            this.f746S = 0.0f;
            this.f741N = f < 0.0f ? -1 : 1;
            this.f740M = true;
            this.f738K = AnimationUtils.currentAnimationTimeMillis();
            this.f739L = (Math.abs(f) / 2700.0f) * 3000.0f;
            this.f731D.requestRender();
        }
    }

    private void m1039h() {
        this.f771y = 0.0f;
        this.f766t = true;
        this.f767u = AnimationUtils.currentAnimationTimeMillis();
        this.f768v = 0.0f;
        this.f769w = this.f768v - 90.0f;
        this.f772z = this.f748b.m1114j();
        this.f731D.requestRender();
    }

    private float m1040i() {
        float max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f767u)) * 1.0f) / ((float) this.f770x), 1.0f));
        if (max == 1.0f) {
            this.f766t = false;
            if (this.f735H != null) {
                this.f735H.mo86a(8, 1, max);
            }
        }
        return (max * (this.f769w - this.f768v)) + this.f768v;
    }

    public void m1048a(ModelData modelData) {
        this.f749c = new ModelObject(modelData);
        this.f749c.m1096a(this.f748b.m1103b());
        this.f749c.m1101a(this.f733F);
        this.f749c.m1105b(false);
    }

    public float m1050b(float f) {
        return (float) (1.0d - Math.pow((double) (1.0f - f), 4.0d));
    }

    public void m1054c(float f, float f2) {
        float f3 = 0.0f;
        float f4 = (f - f2) / ((float) this.f762p);
        f4 *= 10.0f;
        this.f745R = f4;
        if (this.f746S != 0.0f) {
            f3 = (f4 - this.f746S) * -1.0f;
        }
        if (this.f748b != null) {
            this.f748b.m1104b(f3);
        }
        if (this.f749c != null) {
            this.f749c.m1104b(f3);
        }
        if (this.f750d != null) {
            this.f750d.m1070a(f3);
        }
        this.f746S = this.f745R;
        this.f731D.requestRender();
    }

    private void m1041j() {
        if (this.f730C == 0) {
            this.f729B = Calendar.getInstance().getTimeInMillis();
        }
        this.f730C++;
        if (Calendar.getInstance().getTimeInMillis() - this.f729B > 1000) {
            Log.i("cycle", "Frame FPS:\t" + this.f730C);
            this.f730C = 0;
        }
    }

    public void m1053c() {
        if (!TunnelConfig.sensor && Math.abs(this.f728A) > 0.0f) {
            this.f728A = 0.0f;
        }
    }

    public void m1047a(C0135h c0135h) {
        this.f735H = c0135h;
    }

    public int m1055d() {
        return this.f762p;
    }

    public int m1056e() {
        return this.f763q;
    }
}
