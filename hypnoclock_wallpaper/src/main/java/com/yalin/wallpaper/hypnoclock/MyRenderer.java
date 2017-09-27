package com.yalin.wallpaper.hypnoclock;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.DisplayMetrics;

import com.yalin.wallpaper.hypnoclock.p030a.C1546b;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.yalin.wallpaper.hypnoclock.R;

public class MyRenderer implements GLSurfaceView.Renderer {
    private C1546b f6070a = new C1546b(45, false, false);
    private boolean f6071b;
    private boolean f6073d = true;
    private boolean f6074e = true;
    private Context f6075f;
    private boolean f6076g;
    private boolean f6077h;
    private boolean f6078i;
    private boolean f6079j;
    private int r;
    private int g;
    private int b;
    private String f6083n;
    private int f6084o;
    private boolean f6085p = true;
    private DisplayMetrics f6086q;
    private boolean realTick;
    private float f6088s = 0.0f;
    private boolean f6089t;
    private boolean f6090u;

    private void m8555b() {
//        Editor edit = this.f6072c.edit();
//        if (this.f6072c.getBoolean("ghostly_gears", false)) {
//            edit.putString("ghost_list_pref", "G");
//        } else {
//            edit.putString("ghost_list_pref", "R");
//        }
//        if (this.f6072c.getBoolean("real_tick", false)) {
//            edit.putString("mov_list_pref", "R");
//        } else {
//            edit.putString("mov_list_pref", "F");
//        }
//        if (this.f6072c.getBoolean("fast_tick", true)) {
//            edit.putString("speed_list_pref", "2");
//        } else {
//            edit.putString("speed_list_pref", "1");
//        }
//        edit.putBoolean("adjust_one_five_five", false);
//        edit.commit();
    }

    public void m8556a() {
        System.gc();
    }

    public void m8557a(float f, float f2) {
        this.f6088s = -1.0f * (f - f2);
        this.f6070a.m8537a(this.f6088s);
    }

    public void m8558a(float f, float f2, float f3) {
        this.f6070a.m8538a(f, f2, f3);
    }

    public void m8559a(Context context) {
        this.f6075f = context;
    }

    public void m8560a(DisplayMetrics displayMetrics) {
        this.f6086q = displayMetrics;
        this.f6070a.m8541a(displayMetrics);
    }

    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(16640);
        gl10.glLoadIdentity();
        if (this.f6073d) {
            gl10.glEnable(2896);
        } else {
            gl10.glDisable(2896);
        }
        if (this.f6074e) {
            gl10.glEnable(3042);
            gl10.glDisable(2929);
        } else {
            gl10.glDisable(3042);
            gl10.glEnable(2929);
        }
        if (this.f6089t) {
            if (this.f6090u) {
                gl10.glBlendFunc(770, 1);
                this.f6070a.f6044a = true;
            } else {
                gl10.glBlendFunc(770, 771);
                this.f6070a.f6044a = false;
            }
        }
        if (this.f6076g) {
            this.f6070a.m8550b(this.f6078i);
        } else {
            this.f6070a.m8550b(this.f6077h);
        }
        if (this.f6071b) {
            if (this.f6079j) {
                this.f6070a.m8543a(gl10, this.f6075f, this.f6083n, this.f6084o);
                this.f6079j = false;
            }
            this.f6070a.m8544a(gl10, this.f6076g);
        }
        if (this.realTick) {
            this.f6070a.m8549b(gl10, this.f6076g);
        } else {
            this.f6070a.m8551c(gl10, this.f6076g);
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        if (i2 > i) {
            this.f6076g = false;
        } else {
            this.f6076g = true;
            this.f6070a.m8552c(false);
        }
        if (i2 == 0) {
            i2 = 1;
        }
        gl10.glViewport(0, 0, i, i2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45.0f, ((float) i) / ((float) i2), 0.1f, 100.0f);
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.f6073d = false;
        gl10.glEnable(3553);
        gl10.glShadeModel(7425);
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl10.glClearDepthf(1.0f);
        gl10.glHint(3152, 4354);
        gl10.glEnable(3042);
        gl10.glDisable(2929);
        this.f6089t = false;

        gl10.glBlendFunc(770, 771);
        this.f6090u = false;
        this.f6070a.f6044a = false;

        this.f6070a.m8548b(-8.0f + (((float) 30) / 10.0f));
        realTick = false;
        this.f6070a.m8554d(realTick);
        this.f6077h = true;
        this.f6078i = true;
        this.f6070a.f6045b = true;
        if (this.f6085p) {
            this.f6070a.m8547a(false, false);
            this.r = 255;
            this.g = 173;
            this.b = 37;
            this.f6070a.m8537a(0.0f);
            this.f6070a.m8540a(6, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(2, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(28, this.r - ((int) (((float) this.r) * 0.3f)), this.g - ((int) (((float) this.g) * 0.3f)), this.b - ((int) (((float) this.b) * 0.3f)));
            this.f6070a.m8540a(4, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(5, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(3, this.r - ((int) (((float) this.r) * 0.3f)), this.g - ((int) (((float) this.g) * 0.3f)), this.b - ((int) (((float) this.b) * 0.3f)));
            this.f6070a.m8540a(8, this.r - ((int) (((float) this.r) * 0.45f)), this.g - ((int) (((float) this.g) * 0.45f)), this.b - ((int) (((float) this.b) * 0.45f)));
            this.f6070a.m8540a(7, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(9, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(13, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(16, this.r - ((int) (((float) this.r) * 0.45f)), this.g - ((int) (((float) this.g) * 0.45f)), this.b - ((int) (((float) this.b) * 0.45f)));
            this.f6070a.m8540a(14, this.r - ((int) (((float) this.r) * 0.45f)), this.g - ((int) (((float) this.g) * 0.45f)), this.b - ((int) (((float) this.b) * 0.45f)));
            this.f6070a.m8540a(18, this.r - ((int) (((float) this.r) * 0.35f)), this.g - ((int) (((float) this.g) * 0.35f)), this.b - ((int) (((float) this.b) * 0.35f)));
            this.f6070a.m8540a(19, this.r - ((int) (((float) this.r) * 0.35f)), this.g - ((int) (((float) this.g) * 0.35f)), this.b - ((int) (((float) this.b) * 0.35f)));
            this.f6070a.m8540a(12, this.r - ((int) (((float) this.r) * 0.35f)), this.g - ((int) (((float) this.g) * 0.35f)), this.b - ((int) (((float) this.b) * 0.35f)));
            this.f6070a.m8540a(17, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(22, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(23, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(24, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(25, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(26, this.r, this.g, this.b);
            this.f6070a.m8540a(31, this.r - ((int) (((float) this.r) * 0.0f)), this.g - ((int) (((float) this.g) * 0.0f)), this.b - ((int) (((float) this.b) * 0.0f)));
            this.f6070a.m8540a(30, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(32, this.r - ((int) (((float) this.r) * 0.6f)), this.g - ((int) (((float) this.g) * 0.6f)), this.b - ((int) (((float) this.b) * 0.6f)));
            this.f6070a.m8540a(33, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(34, this.r - ((int) (((float) this.r) * 0.2f)), this.g - ((int) (((float) this.g) * 0.2f)), this.b - ((int) (((float) this.b) * 0.2f)));
            this.f6070a.m8540a(1, r, g, b);
            this.f6070a.m8540a(41, r, g, b);
            this.f6070a.m8540a(43, r, g, b);
            this.f6070a.m8540a(44, r, g, b);
            this.f6070a.m8540a(29, r, g, b);
            this.f6070a.m8540a(21, r, g, b);
            this.f6070a.m8540a(27, r, g, b);
            this.f6070a.m8540a(42, r, g, b);
            this.f6070a.m8540a(0, r / 2, g / 2, b / 2);
            this.f6070a.m8540a(15, r, g, b);
            this.f6070a.m8540a(10, r, g, b);
            this.f6070a.m8540a(11, r, g, b);
            this.f6070a.m8540a(20, r, g, b);
            this.f6070a.m8540a(35, r, g, b);
            this.f6070a.m8540a(38, r, g, b);
            this.f6070a.m8540a(37, r, g, b);
            this.f6070a.m8540a(36, r, g, b);
            this.f6070a.m8540a(39, r, g, b);
            this.f6070a.m8540a(40, r, g, b);
            if (this.f6086q.widthPixels > this.f6086q.heightPixels) {
                if (this.f6086q.widthPixels <= 1300) {
                    this.f6070a.m8553d(gl10, true);
                } else {
                    this.f6070a.m8553d(gl10, false);
                }
            } else if (this.f6086q.heightPixels <= 1300) {
                this.f6070a.m8553d(gl10, true);
            } else {
                this.f6070a.m8553d(gl10, false);
            }
            this.f6070a.m8542a(gl10, this.f6075f, 0, R.drawable.g90_3s, new int[]{0, 1, 27, 32, 42});
            this.f6070a.m8542a(gl10, this.f6075f, 1, R.drawable.g60_6s_h2, new int[]{2, 33, 38});
            this.f6070a.m8542a(gl10, this.f6075f, 2, R.drawable.g60_3s_h2, new int[]{6, 13, 7, 23});
            this.f6070a.m8542a(gl10, this.f6075f, 3, R.drawable.g120_3s, new int[]{3, 35});
            this.f6070a.m8542a(gl10, this.f6075f, 4, R.drawable.g60_3s, new int[]{4, 5, 19, 29});
            this.f6070a.m8542a(gl10, this.f6075f, 5, R.drawable.g180i, new int[]{21});
            this.f6070a.m8542a(gl10, this.f6075f, 6, R.drawable.g30_3s, new int[]{10, 11, 15});
            this.f6070a.m8542a(gl10, this.f6075f, 7, R.drawable.g20_4s, new int[]{8, 16});
            this.f6070a.m8542a(gl10, this.f6075f, 8, R.drawable.g30_4s, new int[]{9, 22});
            this.f6070a.m8542a(gl10, this.f6075f, 9, R.drawable.g120_8s, new int[]{14});
            this.f6070a.m8542a(gl10, this.f6075f, 10, R.drawable.g40, new int[]{18, 34});
            this.f6070a.m8542a(gl10, this.f6075f, 11, R.drawable.g120_4cs, new int[]{12});
            this.f6070a.m8542a(gl10, this.f6075f, 12, R.drawable.g12, new int[]{17});
            this.f6070a.m8542a(gl10, this.f6075f, 13, R.drawable.g20, new int[]{24});
            this.f6070a.m8542a(gl10, this.f6075f, 14, R.drawable.g30_5s, new int[]{25});
            this.f6070a.m8542a(gl10, this.f6075f, 15, R.drawable.g10, new int[]{26, 28, 37});
            this.f6070a.m8542a(gl10, this.f6075f, 16, R.drawable.g20_3s, new int[]{30, 31});
            this.f6070a.m8542a(gl10, this.f6075f, 17, R.drawable.escapementwheel, new int[]{36});
            this.f6070a.m8542a(gl10, this.f6075f, 18, R.drawable.hairspring, new int[]{39});
            this.f6070a.m8542a(gl10, this.f6075f, 19, R.drawable.balancewheel, new int[]{40});
            this.f6070a.m8542a(gl10, this.f6075f, 20, R.drawable.second4, new int[]{41});
            this.f6070a.m8542a(gl10, this.f6075f, 21, R.drawable.hour, new int[]{43});
            this.f6070a.m8542a(gl10, this.f6075f, 22, R.drawable.minute, new int[]{44});
            this.f6070a.m8542a(gl10, this.f6075f, 23, R.drawable.g120_1cs, new int[]{20});
            m8558a(0, 0, 0);
            this.f6079j = false;

            this.f6083n = "~9";
            this.f6084o = 0;
            this.f6070a.setDarkness(50);
            this.f6070a.m8546a(true);
            this.f6071b = true;
            this.f6079j = true;

            this.f6085p = false;
        }
    }
}
