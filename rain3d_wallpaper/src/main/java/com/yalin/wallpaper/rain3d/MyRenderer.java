package com.yalin.wallpaper.rain3d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Build.VERSION;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer, SensorEventListener {
    private static float f2903a = 0.05f;
    public static int f2904b = 0;
    Bitmap f2905A = null;
    private float f2906B;
    private float f2907C = 0.0f;
    private float[] f2908D = new float[9];
    private float[] f2909E = new float[9];
    private float[] f2910F = new float[9];
    private final String f2911G = "L1";
    private int fgColor;
    private int bgColor;
    private float[] f2914J = new float[3];
    private float[] f2915K = new float[3];
    private float f2916L;
    private float f2917M;
    private float f2918N;
    private float f2919O;
    private float f2920P;
    private Timer f2921Q = new Timer();
    private float[] f2922R = new float[3];
    private float[] f2923S = new float[3];
    private float[] f2924T = new float[9];
    private float[] f2925U = new float[3];
    private boolean autoMove;
    private boolean invert = false;
    private boolean f2928X;
    private boolean trails;
    private Sensor sensor;
    private String bgImage1;
    private String bgImage2;
    private String bgImage3;
    private String theme;
    private SensorManager af;
    private boolean useImage1;
    private boolean useImage2;
    private boolean useImage3;
    private float[] aj = new float[3];
    private GL10 ak;
    private C0871f[] al;
    private int am = 5;
    private boolean an;
    private float[] ao = new float[9];
    private float ap;
    private float aq;
    private long ar = 1;
    float[] f2931c = new float[16];
    private float[] f2932d = new float[3];
    private Context f2933e;
    private float f2934f;
    private float f2935g;
    private float f2936h;
    private float f2937i;
    private float[] f2938j = new float[9];
    private float[] f2939k = new float[4];
    private boolean hasSensor;
    private float[] f2941m = new float[16];
    private float[] f2942n = new float[9];
    boolean f2943o = true;
    boolean f2944p = false;
    private float f2946r;
    final float f2948t = 57.29578f;
    private float[] f2949u;
    private float f2950v;
    private float f2951w;
    private float f2952x;
    private Bitmap f2953y = null;
    private Bitmap f2954z = null;

    class C0868a extends TimerTask {
        final /* synthetic */ MyRenderer f2902a;

        C0868a(MyRenderer myRenderer) {
            this.f2902a = myRenderer;
            Log.d("mgw", "timerTask create");
        }

        public void run() {
            float[] c;
            double d = 6.283185307179586d;
            if (((double) this.f2902a.f2925U[0]) < -1.5707963267948966d && ((double) this.f2902a.f2914J[0]) > 0.0d) {
                this.f2902a.f2922R[0] = (float) ((0.9900000095367432d * (((double) this.f2902a.f2925U[0]) + 6.283185307179586d)) + ((double) (this.f2902a.f2914J[0] * 0.00999999f)));
                c = this.f2902a.f2922R;
                c[0] = (float) (((double) c[0]) - (((double) this.f2902a.f2922R[0]) > 3.141592653589793d ? 6.283185307179586d : 0.0d));
            } else if (((double) this.f2902a.f2914J[0]) >= -1.5707963267948966d || ((double) this.f2902a.f2925U[0]) <= 0.0d) {
                this.f2902a.f2922R[0] = (0.99f * this.f2902a.f2925U[0]) + (this.f2902a.f2914J[0] * 0.00999999f);
            } else {
                this.f2902a.f2922R[0] = (float) (((double) (0.99f * this.f2902a.f2925U[0])) + (1.00898176E9d * (((double) this.f2902a.f2914J[0]) + 6.283185307179586d)));
                c = this.f2902a.f2922R;
                c[0] = (float) (((double) c[0]) - (((double) this.f2902a.f2922R[0]) > 3.141592653589793d ? 6.283185307179586d : 0.0d));
            }
            if (((double) this.f2902a.f2925U[1]) < -1.5707963267948966d && ((double) this.f2902a.f2914J[1]) > 0.0d) {
                this.f2902a.f2922R[1] = (float) ((0.9900000095367432d * (((double) this.f2902a.f2925U[1]) + 6.283185307179586d)) + ((double) (this.f2902a.f2914J[1] * 0.00999999f)));
                c = this.f2902a.f2922R;
                c[1] = (float) (((double) c[1]) - (((double) this.f2902a.f2922R[1]) > 3.141592653589793d ? 6.283185307179586d : 0.0d));
            } else if (((double) this.f2902a.f2914J[1]) >= -1.5707963267948966d || ((double) this.f2902a.f2925U[1]) <= 0.0d) {
                this.f2902a.f2922R[1] = (0.99f * this.f2902a.f2925U[1]) + (this.f2902a.f2914J[1] * 0.00999999f);
            } else {
                this.f2902a.f2922R[1] = (float) (((double) (0.99f * this.f2902a.f2925U[1])) + (1.00898176E9d * (((double) this.f2902a.f2914J[1]) + 6.283185307179586d)));
                c = this.f2902a.f2922R;
                c[1] = (float) (((double) c[1]) - (((double) this.f2902a.f2922R[1]) > 3.141592653589793d ? 6.283185307179586d : 0.0d));
            }
            float[] c2;
            double d2;
            if (((double) this.f2902a.f2925U[2]) < -1.5707963267948966d && ((double) this.f2902a.f2914J[2]) > 0.0d) {
                this.f2902a.f2922R[2] = (float) ((0.9900000095367432d * (((double) this.f2902a.f2925U[2]) + 6.283185307179586d)) + ((double) (this.f2902a.f2914J[2] * 0.00999999f)));
                c2 = this.f2902a.f2922R;
                d2 = (double) c2[2];
                if (((double) this.f2902a.f2922R[2]) <= 3.141592653589793d) {
                    d = 0.0d;
                }
                c2[2] = (float) (d2 - d);
            } else if (((double) this.f2902a.f2914J[2]) >= -1.5707963267948966d || ((double) this.f2902a.f2925U[2]) <= 0.0d) {
                this.f2902a.f2922R[2] = (0.99f * this.f2902a.f2925U[2]) + (this.f2902a.f2914J[2] * 0.00999999f);
            } else {
                this.f2902a.f2922R[2] = (float) (((double) (0.99f * this.f2902a.f2925U[2])) + (1.00898176E9d * (((double) this.f2902a.f2914J[2]) + 6.283185307179586d)));
                c2 = this.f2902a.f2922R;
                d2 = (double) c2[2];
                if (((double) this.f2902a.f2922R[2]) <= 3.141592653589793d) {
                    d = 0.0d;
                }
                c2[2] = (float) (d2 - d);
            }
            this.f2902a.f2924T = this.f2902a.m4173a(this.f2902a.f2922R);
            System.arraycopy(this.f2902a.f2922R, 0, this.f2902a.f2925U, 0, 3);
        }
    }

    public MyRenderer(Context context) {
        this.f2933e = context;
        this.f2925U[0] = 0.0f;
        this.f2925U[1] = 0.0f;
        this.f2925U[2] = 0.0f;
        this.f2924T[0] = 1.0f;
        this.f2924T[1] = 0.0f;
        this.f2924T[2] = 0.0f;
        this.f2924T[3] = 0.0f;
        this.f2924T[4] = 1.0f;
        this.f2924T[5] = 0.0f;
        this.f2924T[6] = 0.0f;
        this.f2924T[7] = 0.0f;
        this.f2924T[8] = 1.0f;
        this.al = new C0871f[this.am];
    }

    public static Bitmap m4165a(Resources resources, int i, int i2, int i3) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = 1;
        if (Runtime.getRuntime().maxMemory() / 1048576 >= 24) {
            options.inSampleSize = 1;
        } else {
            options.inSampleSize = 2;
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    private void setFgColor(int i) {
        this.fgColor = i;
        m4192e();
    }

    private void m4167a(C0871f c0871f, int i, int i2) {
        if (c0871f != null && this.f2933e != null) {
            try {
                c0871f.m4159a(MyRenderer.m4165a(this.f2933e.getResources(), i, i2, i2));
            } catch (Exception e) {
                Log.e("mgw", "Error in load plane bitmap:  " + i);
            }
        }
    }

    private void setTheme(String str) {
        this.theme = str;
    }

    private void setUseImage(boolean z) {
        this.useImage1 = z;
    }

    private void m4170a(float[] fArr, float[] fArr2, float f) {
        float[] fArr3 = new float[3];
        float sqrt = (float) Math.sqrt((double) (((fArr[0] * fArr[0]) + (fArr[1] * fArr[1])) + (fArr[2] * fArr[2])));
        if (sqrt > 1.0E-9f) {
            fArr3[0] = fArr[0] / sqrt;
            fArr3[1] = fArr[1] / sqrt;
            fArr3[2] = fArr[2] / sqrt;
        }
        sqrt *= f;
        float sin = (float) Math.sin((double) sqrt);
        sqrt = (float) Math.cos((double) sqrt);
        fArr2[0] = fArr3[0] * sin;
        fArr2[1] = fArr3[1] * sin;
        fArr2[2] = fArr3[2] * sin;
        fArr2[3] = sqrt;
    }

    private float[] m4173a(float[] fArr) {
        this.f2950v = (float) Math.sin((double) fArr[1]);
        this.f2934f = (float) Math.cos((double) fArr[1]);
        this.f2951w = (float) Math.sin((double) fArr[2]);
        this.f2935g = (float) Math.cos((double) fArr[2]);
        this.f2952x = (float) Math.sin((double) fArr[0]);
        this.f2936h = (float) Math.cos((double) fArr[0]);
        this.f2908D[0] = 1.0f;
        this.f2908D[1] = 0.0f;
        this.f2908D[2] = 0.0f;
        this.f2908D[3] = 0.0f;
        this.f2908D[4] = this.f2934f;
        this.f2908D[5] = this.f2950v;
        this.f2908D[6] = 0.0f;
        this.f2908D[7] = -this.f2950v;
        this.f2908D[8] = this.f2934f;
        this.f2909E[0] = this.f2935g;
        this.f2909E[1] = 0.0f;
        this.f2909E[2] = this.f2951w;
        this.f2909E[3] = 0.0f;
        this.f2909E[4] = 1.0f;
        this.f2909E[5] = 0.0f;
        this.f2909E[6] = -this.f2951w;
        this.f2909E[7] = 0.0f;
        this.f2909E[8] = this.f2935g;
        this.f2910F[0] = this.f2936h;
        this.f2910F[1] = this.f2952x;
        this.f2910F[2] = 0.0f;
        this.f2910F[3] = -this.f2952x;
        this.f2910F[4] = this.f2936h;
        this.f2910F[5] = 0.0f;
        this.f2910F[6] = 0.0f;
        this.f2910F[7] = 0.0f;
        this.f2910F[8] = 1.0f;
        this.f2949u = m4179b(this.f2908D, this.f2909E);
        this.f2949u = m4179b(this.f2910F, this.f2949u);
        return this.f2949u;
    }

    private void setBgColor(int i) {
        this.bgColor = i;
        m4192e();
        this.an = true;
    }

    private void setBgImage1(String str) {
        this.bgImage1 = str;
    }

    private void setInvert(boolean z) {
        this.invert = z;
    }

    private float[] m4179b(float[] fArr, float[] fArr2) {
        return new float[]{((fArr[0] * fArr2[0]) + (fArr[1] * fArr2[3])) + (fArr[2] * fArr2[6]), ((fArr[0] * fArr2[1]) + (fArr[1] * fArr2[4])) + (fArr[2] * fArr2[7]), ((fArr[0] * fArr2[2]) + (fArr[1] * fArr2[5])) + (fArr[2] * fArr2[8]), ((fArr[3] * fArr2[0]) + (fArr[4] * fArr2[3])) + (fArr[5] * fArr2[6]), ((fArr[3] * fArr2[1]) + (fArr[4] * fArr2[4])) + (fArr[5] * fArr2[7]), ((fArr[3] * fArr2[2]) + (fArr[4] * fArr2[5])) + (fArr[5] * fArr2[8]), ((fArr[6] * fArr2[0]) + (fArr[7] * fArr2[3])) + (fArr[8] * fArr2[6]), ((fArr[6] * fArr2[1]) + (fArr[7] * fArr2[4])) + (fArr[8] * fArr2[7]), ((fArr[6] * fArr2[2]) + (fArr[7] * fArr2[5])) + (fArr[8] * fArr2[8])};
    }

    private void m4180c(String str) {
        this.bgImage2 = str;
    }

    private void setAutoMove(boolean z) {
        this.autoMove = z;
    }

    private void m4183d(String str) {
        this.bgImage3 = str;
    }

    private void setTrails(boolean z) {
        this.trails = z;
    }

    public void m4185a() {
        if (this.f2953y != null) {
            this.f2953y.recycle();
        }
        if (this.f2954z != null) {
            this.f2954z.recycle();
        }
        if (this.f2905A != null) {
            this.f2905A.recycle();
        }
        if (this.al != null) {
            for (int i = 0; i < this.al.length; i++) {
                if (this.al[i] != null) {
                    this.al[i].m4163b(this.ak);
                }
            }
        }
    }

    public void m4186a(SensorEvent sensorEvent) {
        if (this.trails) {
            if (!this.f2944p) {
                this.f2916L = sensorEvent.values[0];
                this.f2917M = sensorEvent.values[1];
                this.f2918N = sensorEvent.values[2];
                this.f2944p = true;
            }
            this.f2932d = m4187a(sensorEvent.values, this.f2932d);
            if (this.invert) {
                if (this.f2928X) {
                    this.aq = this.f2916L - sensorEvent.values[0];
                    this.ap = (-this.f2917M) + sensorEvent.values[1];
                    return;
                }
                this.ap = this.f2916L - sensorEvent.values[0];
                this.aq = this.f2917M - sensorEvent.values[1];
            } else if (this.f2928X) {
                this.ap = this.f2916L - sensorEvent.values[0];
                this.aq = this.f2917M - sensorEvent.values[1];
            } else {
                this.aq = this.f2916L - sensorEvent.values[0];
                this.ap = this.f2917M - sensorEvent.values[1];
            }
        }
    }

    protected float[] m4187a(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            return fArr;
        }
        int i = 0;
        while (i < fArr.length) {
            if (i < fArr.length && i < fArr2.length) {
                fArr2[i] = fArr2[i] + (f2903a * (fArr[i] - fArr2[i]));
            }
            i++;
        }
        return fArr2;
    }

    public void m4188b() {
        float f;
        float f2;
        this.al = new C0871f[this.am];
        if (this.f2928X) {
            f = 2.0f;
            f2 = 3.0f;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        Log.d("mgw", "theme: " + this.theme);

        this.al[0] = new C0871f(3.75f + f2, 3.8f + f, 0.0f, 0.0f, 0.0f, 3.0f);
        this.al[1] = new C0871f(3.75f + f2, 3.8f + f, 0.0f, 0.0f, 0.1f, 5.3f);
        this.al[2] = new C0871f(3.75f + f2, 3.8f + f, 0.0f, 0.0f, 0.15f, 7.0f);
        m4167a(this.al[0], R.drawable.new_rain_0, f2904b);
        m4167a(this.al[1], R.drawable.new_rain_1, f2904b);
        m4167a(this.al[2], R.drawable.new_rain_2, f2904b);

    }

    public void m4189b(SensorEvent sensorEvent) {
        if (this.f2914J != null) {
            if (this.f2943o) {
                this.f2924T = m4179b(this.f2924T, this.f2942n);
                this.f2943o = false;
            }
            if (this.ar != 0) {
                this.f2937i = ((float) (sensorEvent.timestamp - this.ar)) * 1.0E-9f;
                System.arraycopy(sensorEvent.values, 0, this.f2923S, 0, 3);
                m4170a(this.f2923S, this.f2939k, this.f2937i / 2.0f);
            }
            this.ar = sensorEvent.timestamp;
            SensorManager.getRotationMatrixFromVector(this.f2938j, this.f2939k);
            this.f2924T = m4179b(this.f2924T, this.f2938j);
            SensorManager.getOrientation(this.f2924T, this.f2925U);
            this.f2946r = 0.00999999f;
            this.f2922R[0] = (this.f2925U[0] * 0.99f) + (this.f2946r * this.f2914J[0]);
            this.f2922R[1] = (this.f2925U[1] * 0.99f) + (this.f2946r * this.f2914J[1]);
            this.f2922R[2] = (this.f2925U[2] * 0.99f) + (this.f2946r * this.f2914J[2]);
            this.f2924T = m4173a(this.f2925U);
            if (this.invert) {
                if (this.f2928X) {
                    this.ap = this.f2922R[1] * 57.29578f;
                    this.aq = this.f2922R[2] * 57.29578f;
                    return;
                }
                this.ap = this.f2922R[2] * 57.29578f;
                this.aq = this.f2922R[1] * 57.29578f;
            } else if (this.f2928X) {
                this.ap = this.f2922R[2] * 57.29578f;
                this.aq = this.f2922R[1] * 57.29578f;
            } else {
                this.ap = this.f2922R[1] * 57.29578f;
                this.aq = this.f2922R[2] * 57.29578f;
            }
        }
    }

    public void m4190c() {
        if (this.af == null && this.f2933e != null) {
            this.af = (SensorManager) this.f2933e.getSystemService(Context.SENSOR_SERVICE);
        }
        if (this.af != null) {
            if (this.trails) {
                this.af.registerListener(this, this.af.getDefaultSensor(1), 1);
            } else if (VERSION.SDK_INT >= 19) {
                this.af.registerListener(this, this.af.getDefaultSensor(1), 1);
                this.af.registerListener(this, this.af.getDefaultSensor(4), 100);
            } else {
                this.af.registerListener(this, this.af.getDefaultSensor(1), 1);
                this.af.registerListener(this, this.af.getDefaultSensor(4), 0);
                this.af.registerListener(this, this.af.getDefaultSensor(2), 1);
            }
        }
    }

    public void m4191d() {
    }

    public void m4192e() {
        if (this.trails) {
            this.f2906B = 256.0f - (256.0f - ((float) this.fgColor));
            this.f2907C = (float) ((278 - this.bgColor) / 2);
            return;
        }
        this.f2906B = (350.0f - ((float) this.fgColor)) / 1.0f;
        this.f2907C = ((float) (350 - this.bgColor)) * 1.2f;
        Log.d("mgw", "tilt_level: " + this.f2906B);
        Log.d("mgw", "tran_level: " + this.f2907C);
    }

    public void m4193f() {
        if (this.af != null) {
            this.f2921Q.cancel();
        } else {
            this.f2921Q.cancel();
        }
    }

    public void m4194g() {
        this.f2943o = true;
        this.f2921Q = new Timer();
        this.f2921Q.schedule(new C0868a(this), 0, 90);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onDrawFrame(GL10 gl10) {
        int i;
        if (this.an && this.al != null) {
            for (i = 0; i < this.al.length; i++) {
                if (this.al[i] != null) {
                    this.al[i].m4163b(this.ak);
                }
            }
        }
        if ((this.al != null && this.al[0] == null) || this.an) {
            m4188b();
            this.an = false;
        } else if (this.al == null) {
            this.al = new C0871f[this.am];
        }
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl10.glClear(16640);
        gl10.glLoadIdentity();
        if (this.autoMove && !this.trails) {
            gl10.glTranslatef(0.0f, -0.015f, (this.f2919O / 4.0f) - 1.0f);
        } else if (!this.useImage1) {
            gl10.glTranslatef(0.0f, -0.015f, -4.0f);
        }
        gl10.glEnable(3042);
        gl10.glBlendFunc(1, 771);
        if (this.f2907C == 0.0f) {
            this.f2907C = 1.0f;
        }
        if (this.al != null) {
            i = this.f2928X ? -1 : 1;
            if (this.f2920P < 179.0f) {
                this.f2920P += 0.01f;
            } else {
                this.f2920P = 0.0f;
            }
            if (Math.abs(this.aq) < 45.0f) {
                if (this.autoMove && !this.trails && this.theme.startsWith("L")) {
                    this.f2919O = (float) Math.sin((double) this.f2920P);
                } else {
                    this.f2919O = 0.0f;
                }
                for (int i2 = 0; i2 < this.al.length; i2++) {
                    if (this.al[i2] != null) {
                        if (!this.useImage1 && this.theme.equals("L2")) {
                            this.al[i2].f2889d = ((((float) (-i)) * (this.aq + (this.f2919O * 10.0f))) / (this.f2907C * this.al[i2].f2956h)) + this.al[i2].f2957i;
                            this.al[i2].f2887b = (((float) i) * (this.aq + (this.f2919O * 10.0f))) / this.f2906B;
                        } else if (i2 <= 0 || this.useImage1) {
                            this.al[i2].f2889d = ((((float) (-i)) * (this.aq + (this.f2919O * 10.0f))) / (this.f2907C * this.al[i2].f2956h)) + this.al[i2].f2957i;
                            this.al[i2].f2887b = (((float) i) * (this.aq + (this.f2919O * 10.0f))) / this.f2906B;
                        } else {
                            float f = this.al[i2].f2956h;
                            if (this.al[i2 - 1] != null) {
                                this.al[i2].f2889d = (((((float) i) * (this.aq + (this.f2919O * 10.0f))) / (this.f2907C * this.al[i2].f2956h)) + this.al[i2].f2957i) - this.al[i2 - 1].f2957i;
                            } else {
                                this.al[i2].f2889d = ((((float) i) * (this.aq + (this.f2919O * 10.0f))) / (this.f2907C * this.al[i2].f2956h)) + this.al[i2].f2957i;
                            }
                        }
                    }
                }
            }
            if (Math.abs(this.ap) < 30.0f) {
                for (i = 0; i < this.al.length; i++) {
                    if (this.al[i] != null) {
                        if (!this.useImage1 && this.theme.equals("L2")) {
                            this.al[i].f2890e = ((-this.ap) / (this.f2907C * this.al[i].f2956h)) + this.al[i].f2958j;
                            this.al[i].f2886a = (-this.ap) / this.f2906B;
                        } else if (i <= 0 || this.useImage1) {
                            this.al[i].f2890e = ((-this.ap) / (this.f2907C * this.al[i].f2956h)) + this.al[i].f2958j;
                            this.al[i].f2886a = (-this.ap) / this.f2906B;
                        } else {
                            float f2 = this.al[i].f2956h;
                            if (this.al[i - 1] != null) {
                                this.al[i].f2890e = ((this.ap / (f2 * this.f2907C)) + this.al[i].f2958j) - this.al[i - 1].f2958j;
                            } else {
                                this.al[i].f2890e = (this.ap / (f2 * this.f2907C)) + this.al[i].f2958j;
                            }
                        }
                    }
                }
            }
            i = 0;
            while (i < this.al.length) {
                if (this.al[i] != null) {
                    if (!this.useImage1 && this.theme.equals("L2") && i > 0) {
                        gl10.glLoadIdentity();
                        gl10.glTranslatef(0.0f, -0.015f, -4.0f);
                        if (i == 1) {
                            this.al[i].f2888c = 90.0f;
                            this.al[i].f2959k = 0.01f + (this.f2919O / 4.0f);
                        } else if (i == 2) {
                            this.al[i].f2888c = 270.0f;
                            this.al[i].f2959k = 0.65f + (this.f2919O / 4.0f);
                        }
                    }
                    if (this.useImage1) {
                        gl10.glLoadIdentity();
                        gl10.glTranslatef(0.0f, 0.0f, -4.0f);
                        this.al[i].f2891f = this.al[i].f2959k;
                        this.al[i].m4160a(gl10);
                    } else {
                        this.al[i].f2891f = this.al[0].f2891f + this.al[i].f2959k;
                        this.al[i].m4160a(gl10);
                    }
                }
                i++;
            }
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case 1:
                if (this.trails) {
                    m4186a(sensorEvent);
                    return;
                }
                System.arraycopy(sensorEvent.values, 0, this.f2915K, 0, 3);
                m4191d();
                return;
            case 4:
                m4189b(sensorEvent);
                return;
            default:
                return;
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        gl10.glViewport(0, 0, i, i2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45.0f, ((float) i) / ((float) i2), 0.1f, 1000.0f);
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
        if (i > i2) {
            if (!this.f2928X) {
                this.an = true;
            }
            this.f2928X = true;
            this.f2944p = false;
            return;
        }
        if (this.f2928X) {
            this.an = true;
        }
        this.f2928X = false;
        this.f2944p = false;
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.ak = gl10;
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl10.glShadeModel(7425);
        gl10.glClearDepthf(1.0f);
        gl10.glEnable(2929);
        gl10.glDepthFunc(515);
        gl10.glHint(3152, 4354);
        gl10.glEnableClientState(32884);
        int[] iArr = new int[1];
        gl10.glGetIntegerv(3379, iArr, 0);
        f2904b = iArr[0];
        if (f2904b > 2048) {
            f2904b = 2048;
        }
        this.bgImage1 = null;
        this.bgImage2 = null;
        this.bgImage3 = null;
        this.useImage1 = false;
        this.useImage2 = false;
        this.useImage3 = false;
        this.theme = "theme2";
        this.trails = false;
        this.invert = false;
        this.autoMove = false;
        this.bgColor = 200;
        this.fgColor = 200;
        this.af = (SensorManager) this.f2933e.getSystemService(Context.SENSOR_SERVICE);
        this.hasSensor = this.af.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null;
        if (!this.hasSensor) {
            this.trails = true;
        }
        if (this.trails) {
            this.sensor = this.af.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        m4192e();
        m4190c();
    }
}
