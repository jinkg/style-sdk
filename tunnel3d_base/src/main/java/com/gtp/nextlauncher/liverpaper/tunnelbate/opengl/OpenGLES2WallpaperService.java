package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.VelocityTracker;
import android.view.WindowManager;

import com.gtp.nextlauncher.liverpaper.tunnelbate.C0177g;
import com.gtp.nextlauncher.liverpaper.tunnelbate.launcher.C0179a;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0153a;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0154b;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0155c;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class OpenGLES2WallpaperService extends GLWallpaperService {
    static List f568b = new ArrayList();
    C0187h f569a;

    public OpenGLES2WallpaperService(Context host) {
        super(host);
    }

    public abstract C0177g mo93c();

    public Engine onCreateEngine() {
        this.f569a = new C0187h(this);
        return this.f569a;
    }

    protected GLSurfaceView m873d() {
        return this.f569a.m1077d();
    }

    public class C0187h extends C0182c implements SensorEventListener, GestureDetector.OnGestureListener, C0179a {
        private float f840A = 0.0f;
        private int f841B = -1;
        final /* synthetic */ OpenGLES2WallpaperService f842b;
        private C0155c f843c;
        private C0177g f844d;
        private SensorManager f845e;
        private GestureDetector f846f;
        private boolean f848h;
        private boolean f849i;
        private float f850j;
        private float f851k;
        private float f852l = 0.0f;
        private float f853m = 0.0f;
        private boolean f854n;
        private int f855o;
        private C0197r f856p = new C0197r();
        private C0197r f857q = new C0197r();
        private boolean f858r;
        private boolean f859s = false;
        private float f860t;
        private float f861u;
        private VelocityTracker f862v;
        private int f863w;
        private float f864x;
        private float f865y;
        private long f866z;

        public C0187h(OpenGLES2WallpaperService openGLES2WallpaperService) {
            super(openGLES2WallpaperService);
            this.f842b = openGLES2WallpaperService;
        }

        public C0155c m1135e() {
            if (this.f843c == null) {
                this.f843c = new C0155c();
            }
            return this.f843c;
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
//            C0232l.m1362b("cycle", "引擎创建。");
            if (m1124h()) {
                this.f844d = this.f842b.mo93c();
                m1075a(this.f844d);
                this.f845e = (SensorManager) this.f842b.getSystemService("sensor");
                setTouchEventsEnabled(true);
                this.f846f = new GestureDetector(this);
                this.f848h = m1121f();
                this.f849i = false;
                this.f858r = false;
            }
        }

        private boolean m1121f() {
            return true;
        }

        private void m1122g() {
            for (Sensor registerListener : this.f845e.getSensorList(1)) {
                this.f845e.registerListener(this, registerListener, 1);
            }
        }

        private boolean m1124h() {
            if ((((ActivityManager) this.f842b.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072 ? 1 : null) == null) {
                return false;
            }
            m1076b(2);
            return true;
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            boolean z = false;
            super.onOffsetsChanged(f, f2, f3, f4, i, i2);
            this.f860t = f;
            if (this.f860t - this.f861u > 0.0f) {
                this.f859s = true;
            } else if (this.f860t == this.f861u) {
                z = true;
            } else {
                this.f859s = false;
            }
            this.f861u = this.f860t;
            if (!isPreview() && this.f844d != null) {
                m1077d().queueEvent(new C0188i(this, z));
            }
        }

        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.f855o = ((WindowManager) this.f842b.getSystemService("window")).getDefaultDisplay().getOrientation();
        }

        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (!this.f848h) {
                this.f848h = m1121f();
            }
            if (z) {
                if (!this.f854n) {
                    m1122g();
                    this.f854n = true;
                }
            } else if (this.f854n) {
                this.f845e.unregisterListener(this);
                this.f854n = false;
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (this.f855o == 0) {
                this.f857q.f891a = sensorEvent.values[0];
                m1118a(this.f857q);
            } else if (this.f855o == 1) {
                this.f857q.f891a = sensorEvent.values[1];
                m1118a(this.f857q);
            } else if (this.f855o == 2) {
                this.f857q.f891a = sensorEvent.values[0];
                m1118a(this.f857q);
            } else if (this.f855o == 3) {
                this.f857q.f891a = sensorEvent.values[1];
                m1118a(this.f857q);
            }
        }

        private void m1118a(C0197r c0197r) {
            if (!m1125i()) {
                boolean sensor;
                try {
                    sensor = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    sensor = false;
                }
                if (sensor) {
                    this.f864x = c0197r.f891a;
                    if (Math.abs(this.f864x - this.f865y) > C0153a.m919d()) {
                        sensor = true;
                    } else {
                        sensor = false;
                    }
                    this.f865y = this.f864x;
                    if (this.f844d != null) {
                        GLSurfaceView d = m1077d();
                        if (d != null) {
                            d.queueEvent(new C0189j(this, sensor, c0197r));
                        }
                    }
                }
            }
        }

        public void onTouchEvent(MotionEvent motionEvent) {
            super.onTouchEvent(motionEvent);
            if (this.f848h) {
                int action;
                int findPointerIndex;
                m1135e().m922a(motionEvent);
                this.f846f.onTouchEvent(motionEvent);
                if (this.f858r) {
                    action = motionEvent.getAction();
                    findPointerIndex = motionEvent.findPointerIndex(this.f863w);
                } else {
                    action = motionEvent.getAction();
                    findPointerIndex = motionEvent.findPointerIndex(this.f863w);
                }
                if (findPointerIndex == -1) {
                    findPointerIndex = 0;
                }
                switch (action) {
                    case 1:
                    case 3:
                        if (this.f849i) {
                            this.f866z = Calendar.getInstance().getTimeInMillis();
//                            C0232l.m1362b("cycle", "锁定开始");
                            m1123g(this.f850j - this.f852l, this.f851k - this.f853m);
                            this.f849i = false;
                            this.f850j = -1.0f;
                            this.f851k = -1.0f;
                            this.f852l = 0.0f;
                            this.f853m = 0.0f;
                        }
                        m1135e().m921a(1000, C0154b.f594b);
                        if (Calendar.getInstance().getTimeInMillis() - this.f866z > 200) {
                            if (!m1125i()) {
                                m1116a(this.f843c.m920a());
                            } else {
                                return;
                            }
                        }
                        this.f863w = 0;
                        return;
                    case 2:
                        this.f850j = motionEvent.getX(findPointerIndex);
                        this.f851k = motionEvent.getY(findPointerIndex);
                        if (this.f849i) {
                            m1120f(this.f850j - this.f852l, this.f851k - this.f853m);
                            return;
                        } else {
                            m1119e(this.f850j, this.f840A);
                            return;
                        }
                    case 6:
                        if (!m1125i() && this.f849i) {
                            this.f863w = 1;
                            return;
                        }
                        return;
                    case 262:
                        if (!m1125i() && this.f849i) {
                            this.f863w = 0;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        private boolean m1125i() {
            if (this.f841B == 101 || this.f841B == 103) {
                return true;
            }
            return false;
        }

        private void m1119e(float f, float f2) {
            if (!m1125i() && this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0190k(this, f, f2));
                }
            }
        }

        private void m1116a(float f) {
            if (this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0191l(this, f));
                }
            }
        }

        public boolean onDown(MotionEvent motionEvent) {
            int findPointerIndex = motionEvent.findPointerIndex(this.f863w);
            if (findPointerIndex == -1) {
                findPointerIndex = 0;
            }
            this.f840A = motionEvent.getX(findPointerIndex);
            return false;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (this.f848h) {
                this.f850j = motionEvent.getX();
                this.f851k = motionEvent.getY();
//                C0232l.m1362b("position", "长按时:\t" + this.f850j + ":\t" + this.f851k);
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        public void onShowPress(MotionEvent motionEvent) {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        public void mo115a(float f, float f2) {
            boolean z = true;
            if (!m1125i() && this.f848h) {
//                C0232l.m1362b("position", "原来的坐标:\tx:\t" + f + "y:\t" + f2);
                this.f852l = f;
                this.f853m = f2;
                this.f849i = true;
                if ((-1.0f * f) - 1.0f != this.f850j - this.f852l) {
                    z = false;
                }
                m1117a(this.f850j - this.f852l, this.f851k - this.f853m, z);
            }
        }

        public void mo114a() {
            if (this.f848h && this.f849i) {
                m1126j();
            }
        }

        public void mo118b(float f, float f2) {
            this.f858r = true;
        }

        public void mo120c(float f, float f2) {
        }

        private void m1117a(float f, float f2, boolean z) {
            if (this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0192m(this, f, f2, z));
                }
            }
        }

        private void m1120f(float f, float f2) {
            if (this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0193n(this, f, f2));
                }
            }
        }

        private void m1123g(float f, float f2) {
            if (this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0194o(this, f, f2));
                }
            }
        }

        private void m1126j() {
            if (this.f844d != null) {
                GLSurfaceView d = m1077d();
                if (d != null) {
                    d.queueEvent(new C0195p(this));
                }
            }
        }

        public void mo121d(float f, float f2) {
        }

        public void mo117b() {
            this.f848h = true;
        }

        public void mo119c() {
            this.f848h = false;
        }

        public void onDestroy() {
//            C0232l.m1362b("cycle", "引擎销毁。");
            super.onDestroy();
            if (this.f862v != null) {
                this.f862v.recycle();
            }
            if (this.f844d != null) {
                this.f844d.m1042a();
            }
        }

        public void mo116a(int i) {
//            C0232l.m1362b("state", "当前状态:" + i);
            this.f841B = i;
        }
    }

    class C0188i implements Runnable {
        final /* synthetic */ boolean f867a;
        final /* synthetic */ C0187h f868b;

        C0188i(C0187h c0187h, boolean z) {
            this.f868b = c0187h;
            this.f867a = z;
        }

        public void run() {
            if (!this.f867a) {
            }
        }
    }

    class C0189j implements Runnable {
        final /* synthetic */ boolean f869a;
        final /* synthetic */ C0197r f870b;
        final /* synthetic */ C0187h f871c;

        C0189j(C0187h c0187h, boolean z, C0197r c0197r) {
            this.f871c = c0187h;
            this.f869a = z;
            this.f870b = c0197r;
        }

        public void run() {
            if (this.f869a) {
                this.f871c.f844d.m1049a(this.f870b);
            }
        }
    }

    class C0190k implements Runnable {
        final /* synthetic */ float f872a;
        final /* synthetic */ float f873b;
        final /* synthetic */ C0187h f874c;

        C0190k(C0187h c0187h, float f, float f2) {
            this.f874c = c0187h;
            this.f872a = f;
            this.f873b = f2;
        }

        public void run() {
            this.f874c.f844d.m1054c(this.f872a, this.f873b);
        }
    }

    class C0191l implements Runnable {
        final /* synthetic */ float f875a;
        final /* synthetic */ C0187h f876b;

        C0191l(C0187h c0187h, float f) {
            this.f876b = c0187h;
            this.f875a = f;
        }

        public void run() {
            this.f876b.f844d.m1043a(this.f875a);
        }
    }

    class C0192m implements Runnable {
        final /* synthetic */ float f877a;
        final /* synthetic */ float f878b;
        final /* synthetic */ boolean f879c;
        final /* synthetic */ C0187h f880d;

        C0192m(C0187h c0187h, float f, float f2, boolean z) {
            this.f880d = c0187h;
            this.f877a = f;
            this.f878b = f2;
            this.f879c = z;
        }

        public void run() {
            this.f880d.f844d.m1045a(this.f877a, this.f878b, this.f879c);
        }
    }

    class C0193n implements Runnable {
        final /* synthetic */ float f881a;
        final /* synthetic */ float f882b;
        final /* synthetic */ C0187h f883c;

        C0193n(C0187h c0187h, float f, float f2) {
            this.f883c = c0187h;
            this.f881a = f;
            this.f882b = f2;
        }

        public void run() {
            this.f883c.f844d.m1044a(this.f881a, this.f882b);
            C0196q c0196q = new C0196q(this.f883c);
            c0196q.f888a = this.f881a;
            c0196q.f889b = this.f882b;
            OpenGLES2WallpaperService.f568b.add(c0196q);
        }
    }

    class C0194o implements Runnable {
        final /* synthetic */ float f884a;
        final /* synthetic */ float f885b;
        final /* synthetic */ C0187h f886c;

        C0194o(C0187h c0187h, float f, float f2) {
            this.f886c = c0187h;
            this.f884a = f;
            this.f885b = f2;
        }

        public void run() {
            this.f886c.f844d.m1052b(this.f884a, this.f885b);
        }
    }

    class C0195p implements Runnable {
        final /* synthetic */ C0187h f887a;

        C0195p(C0187h c0187h) {
            this.f887a = c0187h;
        }

        public void run() {
            this.f887a.f844d.m1051b();
        }
    }

    class C0196q {
        public float f888a;
        public float f889b;
        final /* synthetic */ C0187h f890c;

        C0196q(C0187h c0187h) {
            this.f890c = c0187h;
        }
    }

}
