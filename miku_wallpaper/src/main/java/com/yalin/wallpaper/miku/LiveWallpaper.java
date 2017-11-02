package com.yalin.wallpaper.miku;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import jp.kzfactory.utils.android.FileManager;
import jp.kzfactory.utils.android.SimpleImage;
import jp.kzfactory.utils.android.TouchManager;
import jp.live2d.framework.L2DMatrix44;
import jp.live2d.framework.L2DTargetPoint;
import jp.live2d.framework.L2DViewMatrix;

public class LiveWallpaper extends WallpaperServiceProxy {
    static boolean Aisatsu_am = false;
    static boolean Aisatsu_ohayo = false;
    static boolean Aisatsu_pm = false;
    static boolean BatterySaving = true;
    static boolean Headphone = false;
    static boolean Headphone_motion = false;
    static boolean Headphone_offmotion = false;
    static boolean LockScreenMode = false;
    static boolean LockScreen_off = false;
    static String SelectName = "Miku";
    public static final String TAG = "Touchevent_WP";
    static boolean TapAction = true;
    static boolean Time = false;
    static long TouchDistanceTime = System.currentTimeMillis();
    static boolean VisibleIn = false;
    static long VisibleTime = 0;
    static boolean Voice = false;
    static boolean WP_Visible = false;
    static long afterTime;
    static int chr = 1;
    static float cpu_system = 0.0f;
    static int day;
    static long diffDays;
    static int emotion = 0;
    static int flick = 0;
    static int hour;
    static boolean info_tap = false;
    private static WallpaperService instance;
    static boolean juuden = false;
    static boolean katamuki = false;
    static int level = 0;
    public static SensorManager manager;
    static int minute;
    static int month;
    static boolean okoru = false;
    public static String path = null;
    static int plugged = 0;
    static boolean preview_mode = false;
    static double ratio = 0.0d;
    static long saveTime;
    static int saveday;
    static int savemonth;
    static int saveyear;
    static int second = 0;
    public static Sensor sensor;
    public static SensorEventListener sensorListener;
    static int size = 0;
    static int sleep = 0;
    static int sleep_count = 0;
    static float sx;
    static float sy;
    static float sz;
    static boolean tate_gamen = true;
    static float temperature = 0.0f;
    static float temperature_f = 0.0f;
    static long time_am;
    static long time_ohayo;
    static long time_pm;
    static int timedisplay = 0;
    static float[] tr_wp = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    static int voltage = 0;
    static boolean wakeup = false;
    static int week;
    static int year;
    int D_count = 0;
    String ListChr = "Miku";
    final String PREF_KEY = "preferenceTest";
    String TimeDisplay = "Display";
    public float VIEW_LOGICAL_MAX_BOTTOM = -2.5f;
    public float VIEW_LOGICAL_MAX_LEFT = -3.0f;
    public float VIEW_LOGICAL_MAX_RIGHT = 3.0f;
    public float VIEW_LOGICAL_MAX_TOP = 4.0f;
    boolean bg_load = false;
    Bitmap bm = null;
    Calendar calendar = Calendar.getInstance();
    private LAppLive2DManager_WP delegate;
    private L2DMatrix44 deviceToScreen;
    private L2DTargetPoint dragMgr;
    int f250h;
    long infoTime = System.currentTimeMillis();
    boolean infocall = true;
    String listSize = "NORMAL";
    String listStr = "Standard";
    private LAppLive2DManager_WP live2DMgr;
    private final GestureDetector mGestureDetector = new GestureDetector(new C17762());
    long nowTime;
    long okoruTime = 0;
    boolean rep = false;
    int sel_time = 100;
    boolean settei_load1 = false;
    boolean settei_load2 = false;
    boolean sta = false;
    boolean tablet = false;
    boolean tate_gamen_m = true;
    private TouchManager touchMgr;
    boolean tr_load = false;
    int val = -1;
    private L2DViewMatrix viewMatrix;
    int f251w;

    public LiveWallpaper(Context host) {
        super(host);
    }


    class C17762 extends SimpleOnGestureListener {
        C17762() {
        }

        public boolean onDoubleTap(MotionEvent event) {
            return delegate.wtapEvent(transformDeviceToViewX(TouchManager.lastX),
                    transformDeviceToViewY(TouchManager.lastY)) | super.onSingleTapUp(event);
        }

        public boolean onDown(MotionEvent event) {
            super.onDown(event);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent event) {
            float x = transformDeviceToViewX(TouchManager.lastX);
            float y = transformDeviceToViewY(TouchManager.lastY);
            return delegate.tapEvent(x, y) | super.onSingleTapUp(event);
        }

        public boolean onSingleTapUp(MotionEvent event) {
            return super.onSingleTapUp(event);
        }
    }

    public class GLEngine extends ActiveEngine {
        int NadeCount = 0;
        private float adjust = 120.0f;
        private GLEngineSurface gl = null;
        private float nowTouchedX;
        private float nowTouchedY;
        private float startTouchX;
        private float startTouchY;

        public GLEngine() {
            super();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.gl = new GLEngineSurface(getSurfaceHolder());
            this.gl.start();
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            D_count = 0;
            sta = false;
            super.onSurfaceChanged(holder, format, width, height);
            this.gl.windowWidth = width;
            this.gl.windowHeight = height;
            f251w = width;
            f250h = height;
            log("画面サイズ(w + h)" + f251w + " x " + f250h);
            tr_load = true;
            deviceToScreen = new L2DMatrix44();
            viewMatrix = new L2DViewMatrix();
            viewMatrix.setMaxScale(LAppDefine.VIEW_MAX_SCALE);
            viewMatrix.setMinScale(LAppDefine.VIEW_MIN_SCALE);
            VIEW_LOGICAL_MAX_LEFT = (LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0]) - 1.0f;
            VIEW_LOGICAL_MAX_RIGHT = (LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0]) - 1.0f;
            VIEW_LOGICAL_MAX_BOTTOM = LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0];
            VIEW_LOGICAL_MAX_TOP = (LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0]) - 1.0f;
            viewMatrix.setMaxScreenRect(VIEW_LOGICAL_MAX_LEFT, VIEW_LOGICAL_MAX_RIGHT, VIEW_LOGICAL_MAX_BOTTOM, VIEW_LOGICAL_MAX_TOP);
            touchMgr = new TouchManager();
            dragMgr = new L2DTargetPoint();
            setupView(width, height);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            if (xOffset > 0.0f) {
            }
            if (xOffset < 0.0f) {
                super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            } else {
                super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            }
        }

        public void onVisibilityChanged(boolean visible) {
            log("onVisibilityChanged : " + visible + " 設定時ライフサイクル 04 " + tr_wp[0]);
            super.onVisibilityChanged(visible);
            if (visible) {
                log("onVisibilityChanged1 : " + visible);
                this.gl.onResume();
                log("onVisibilityChanged2 : " + visible);
                WP_Visible = true;
                if (MKConfig.Start_Aisatsu) {
                    preview_mode = true;
                }
                if (!preview_mode) {
                    VisibleIn = true;
                }
                Headphone_motion = false;
                try {
                    SettingItem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ResetPosition();
                D_count = 0;
                sta = false;
                LAppLive2DManager_WP.reloadFlg = true;
                return;
            }
            log("onVisibilityChanged : 画面表示ＯＦＦ" + visible + "　" + f251w + "*" + f250h);
            WP_Visible = false;
            saveyear = calendar.get(Calendar.YEAR);
            savemonth = calendar.get(Calendar.MONTH) + 1;
            saveday = calendar.get(Calendar.DATE);
            saveTime = System.currentTimeMillis();

            log("onVisibilityChanged : 画面位置保存  縦画面 = " + tate_gamen + "   スケール値 = " + tr_wp[0]);
            this.gl.onPause();
            if (preview_mode) {
                preview_mode = false;
            }
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (this.gl != null) {
                this.gl.onDestroy();
                this.gl = null;
            }
        }

        public void setupView(int width, int height) {
            float ratio = ((float) height) / ((float) width);
            float bottom = -ratio;
            float top = ratio;
            viewMatrix.setScreenRect(-1.0f, 1.0f, bottom, top);
            float screenW = Math.abs(-1.0f - 1.0f);
            deviceToScreen.identity();
            deviceToScreen.multTranslate(((float) (-width)) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2, ((float) height) / LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2);
            deviceToScreen.multScale(screenW / ((float) width), screenW / ((float) width));
        }

        public void onTouchEvent(MotionEvent event) {
            LAppModel.TapRendaStop = false;
            if (sleep == 0) {
                TouchDistanceTime = System.currentTimeMillis();
            }
            mGestureDetector.onTouchEvent(event);
            int touchNum;
            switch (event.getAction()) {
                case 0:
                    flick = 0;
                    LAppLive2DManager_WP.okiru_3rd = false;
                    touchNum = event.getPointerCount();
                    if (touchNum == 1) {
                        touchesBegan(event.getX(), event.getY());
                        this.startTouchX = event.getX();
                        this.startTouchY = event.getY();
                    } else if (touchNum == 2) {
                        if (TapAction) {
                            touchesBegan(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                        } else {
                            return;
                        }
                    }
                    TouchReset();
                    return;
                case 1:
                    if (event.getPointerCount() >= 2) {
                        return;
                    }
                    if (this.NadeCount >= 3) {
                        delegate.HeadNadeEvent(transformDeviceToViewX(touchMgr.getX()), transformDeviceToViewY(touchMgr.getY()));
                        this.NadeCount = 0;
                        return;
                    }
                    this.NadeCount = 0;
                    this.nowTouchedX = event.getX();
                    this.nowTouchedY = event.getY();
                    if (this.startTouchY > this.nowTouchedY) {
                        if (this.startTouchX > this.nowTouchedX) {
                            if (this.startTouchY - this.nowTouchedY > this.startTouchX - this.nowTouchedX) {
                                if (this.startTouchY > this.nowTouchedY + this.adjust) {
                                    flick = 1;
                                }
                            } else if (this.startTouchY - this.nowTouchedY < this.startTouchX - this.nowTouchedX && this.startTouchX > this.nowTouchedX + this.adjust) {
                                flick = 4;
                            }
                        } else if (this.startTouchX < this.nowTouchedX) {
                            if (this.startTouchY - this.nowTouchedY > this.nowTouchedX - this.startTouchX) {
                                if (this.startTouchY > this.nowTouchedY + this.adjust) {
                                    flick = 1;
                                }
                            } else if (this.startTouchY - this.nowTouchedY < this.nowTouchedX - this.startTouchX && this.startTouchX < this.nowTouchedX + this.adjust) {
                                flick = 3;
                            }
                        }
                    } else if (this.startTouchY < this.nowTouchedY) {
                        if (this.startTouchX > this.nowTouchedX) {
                            if (this.nowTouchedY - this.startTouchY > this.startTouchX - this.nowTouchedX) {
                                if (this.startTouchY < this.nowTouchedY + this.adjust) {
                                    flick = 2;
                                }
                            } else if (this.nowTouchedY - this.startTouchY < this.startTouchX - this.nowTouchedX && this.startTouchX > this.nowTouchedX + this.adjust) {
                                flick = 4;
                            }
                        } else if (this.startTouchX < this.nowTouchedX) {
                            if (this.nowTouchedY - this.startTouchY > this.nowTouchedX - this.startTouchX) {
                                if (this.startTouchY < this.nowTouchedY + this.adjust) {
                                    flick = 2;
                                }
                            } else if (this.nowTouchedY - this.startTouchY < this.nowTouchedX - this.startTouchX && this.startTouchX < this.nowTouchedX + this.adjust) {
                                flick = 3;
                            }
                        }
                    }
                    touchesFlick(event.getX(), event.getY());
                    touchesEnded();
                    LAppLive2DManager_WP.ChrTouch = false;
                    return;
                case 2:
                    touchNum = event.getPointerCount();
                    if (touchNum == 1) {
                        touchesMoved(event.getX(), event.getY());
                        if (this.startTouchX + 100.0f < event.getX() && this.NadeCount == 0) {
                            this.NadeCount = 1;
                        }
                        if (this.startTouchX + 50.0f > event.getX() && this.NadeCount == 1) {
                            this.NadeCount = 2;
                        }
                        if (this.startTouchX + 100.0f < event.getX() && this.NadeCount == 2) {
                            this.NadeCount = 3;
                        }
                        if (this.startTouchX + 50.0f > event.getX() && this.NadeCount == 3) {
                            this.NadeCount = 4;
                        }
                    } else if (touchNum == 2) {
                        if (TapAction) {
                            touchesMoved(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                        } else {
                            return;
                        }
                    }
                    TouchReset();
                    return;
                default:
                    return;
            }
        }
    }

    public class GLEngineSurface extends Thread {
        private boolean destroy = false;
        private EGL10 egl;
        private EGLConfig eglConfig = null;
        private EGLContext eglContext = null;
        private EGLDisplay eglDisplay = null;
        private EGLSurface eglSurface = null;
        protected GL10 gl10 = null;
        private SurfaceHolder holder;
        private boolean pause = false;
        long reloadtime = 0;
        private int windowHeight = -1;
        private int windowWidth = -1;

        public GLEngineSurface(SurfaceHolder holder) {
            this.holder = holder;
        }

        private void initialize() {
            this.egl = (EGL10) EGLContext.getEGL();
            this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.egl.eglInitialize(this.eglDisplay, new int[]{-1, -1})) {
                EGLConfig[] configs = new EGLConfig[1];
                if (this.egl.eglChooseConfig(this.eglDisplay, new int[]{12344}, configs, 1, new int[1])) {
                    this.eglConfig = configs[0];
                    this.eglContext = this.egl.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, null);
                    if (this.eglContext == EGL10.EGL_NO_CONTEXT) {
                        log("glContext == EGL10.EGL_NO_CONTEXT");
                        return;
                    }
                    this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                    this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, this.holder, null);
                    if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
                        log("glSurface == EGL10.EGL_NO_SURFACE");
                        return;
                    }
                    this.gl10 = (GL10) this.eglContext.getGL();
                    if (!this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
                        log("!eglMakeCurrent");
                        return;
                    }
                    return;
                }
                log("!eglChooseConfig");
                return;
            }
            log("!eglInitialize");
        }

        private void dispose() {
            log("dispose");
            if (this.eglSurface != null) {
                this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.egl.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            if (this.eglContext != null) {
                this.egl.eglDestroyContext(this.eglDisplay, this.eglContext);
                this.eglContext = null;
            }
            if (this.eglDisplay != null) {
                this.egl.eglTerminate(this.eglDisplay);
                this.eglDisplay = null;
            }
        }

        public void run() {
            LAppRenderer_WP render = new LAppRenderer_WP();
            initialize();
            while (!this.destroy) {
                int sleeptime;
                if (plugged == 0) {
                    sleeptime = 180000;
                } else if (plugged <= 2) {
                    sleeptime = 600000;
                } else {
                    sleeptime = 300000;
                }
                if (!this.pause) {
                    if (System.currentTimeMillis() - TouchDistanceTime >= ((long) (sleeptime + 2000)) && BatterySaving) {
                        sleep = 1;
                    } else if (System.currentTimeMillis() - TouchDistanceTime >= ((long) sleeptime) && BatterySaving) {
                        sleep = 2;
                    } else if (System.currentTimeMillis() - TouchDistanceTime < ((long) (sleeptime - 30000)) || !BatterySaving) {
                        sleep = 0;
                    } else {
                        sleep = 10;
                    }
                }
                int he;
                if (!this.pause && System.currentTimeMillis() - TouchDistanceTime >= ((long) (sleeptime + 3000)) && sleep >= 1) {
                    second = Calendar.getInstance().get(Calendar.SECOND);
                    if (second <= 1) {
                        this.reloadtime = System.currentTimeMillis();
                        if (this.windowWidth < this.windowHeight) {
                            he = (int) (((float) this.windowWidth) * 1.6f);
                            this.gl10.glViewport(0, (this.windowHeight - he) / 2, this.windowWidth, he);
                        } else {
                            this.gl10.glViewport(0, 0, this.windowWidth, this.windowHeight);
                        }
                        try {
                            render.onDrawFrame(this.gl10);
                        } catch (Exception e) {
                        }
                        this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface);
                    }
                } else if (!this.pause && System.currentTimeMillis() - this.reloadtime >= ((long) sel_time)) {
                    this.reloadtime = System.currentTimeMillis();
                    if (this.windowWidth < this.windowHeight) {
                        he = (int) (((float) this.windowWidth) * 1.6f);
                        this.gl10.glViewport(0, (this.windowHeight - he) / 2, this.windowWidth, he);
                    } else {
                        this.gl10.glViewport(0, 0, this.windowWidth, this.windowHeight);
                    }
                    try {
                        render.onDrawFrame(this.gl10);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface);
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e3) {
                }
            }
            dispose();
        }

        public void onPause() {
            log("onPause");
            if (sensorListener != null) {
                manager.unregisterListener(sensorListener);
            }
            this.pause = true;
        }

        public void onResume() {
            log("onResume");
            this.pause = false;
            TouchDistanceTime = System.currentTimeMillis();
            if (sensor != null) {
                manager.registerListener(sensorListener, sensor, 3);
            }
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            LockScreenMode = keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode();
            Headphone = false;
        }

        public void onDestroy() {
            log("onDestroy");
            synchronized (this) {
                this.destroy = true;
            }
            try {
                join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public class LAppRenderer_WP {
        private SimpleImage bg;
        int c01 = 0;
        int c02 = 0;
        int c03 = 0;
        int c04 = 0;
        int c05 = 0;
        int c06 = 0;
        int c07 = 0;
        int c11 = 0;
        int c12 = 0;
        int c13 = 0;
        int c14 = 0;
        int c15 = 0;
        int c16 = 0;
        int c17 = 0;
        int cpu_p1 = 0;
        int cpu_p2 = 0;
        float cpu_sa = 0.0f;
        boolean cpu_time = false;
        float system_sa = 0.0f;

        public void GetCPU_Speed() {
            try {
                String[] cmdArgs = new String[]{"/system/bin/cat", "/proc/stat"};
                String cpuLine = "";
                StringBuilder cpuBuffer = new StringBuilder();
                InputStream in = new ProcessBuilder(cmdArgs).start().getInputStream();
                byte[] lineBytes = new byte[1024];
                while (in.read(lineBytes) != -1) {
                    cpuBuffer.append(new String(lineBytes));
                }
                in.close();
                cpuLine = cpuBuffer.toString();
                String[] cpuList = cpuLine.substring(cpuLine.indexOf("cpu"), cpuLine.indexOf("cpu0")).split(" ", 0);
                second = Calendar.getInstance().get(Calendar.SECOND);
                if (second % 2 == 0 && !this.cpu_time) {
                    this.c01 = Integer.parseInt(cpuList[2]);
                    this.c02 = Integer.parseInt(cpuList[3]);
                    this.c03 = Integer.parseInt(cpuList[4]);
                    this.c04 = Integer.parseInt(cpuList[5]);
                    this.c05 = Integer.parseInt(cpuList[6]);
                    this.c06 = Integer.parseInt(cpuList[7]);
                    this.c07 = Integer.parseInt(cpuList[8]);
                    this.cpu_p1 = ((((this.c01 + this.c02) + this.c03) + this.c05) + this.c06) + this.c07;
                    this.cpu_time = true;
                }
                if (second % 2 == 1 && this.cpu_time) {
                    this.c11 = Integer.parseInt(cpuList[2]);
                    this.c12 = Integer.parseInt(cpuList[3]);
                    this.c13 = Integer.parseInt(cpuList[4]);
                    this.c14 = Integer.parseInt(cpuList[5]);
                    this.c15 = Integer.parseInt(cpuList[6]);
                    this.c16 = Integer.parseInt(cpuList[7]);
                    this.c17 = Integer.parseInt(cpuList[8]);
                    this.cpu_p2 = ((((this.c11 + this.c12) + this.c13) + this.c15) + this.c16) + this.c17;
                    this.cpu_time = false;
                    this.cpu_sa = (float) (this.cpu_p2 - this.cpu_p1);
                    this.system_sa = (float) (this.c14 - this.c04);
                    cpu_system = (this.system_sa / this.cpu_sa) * 100.0f;
                    cpu_system = (this.cpu_sa / this.system_sa) * 100.0f;
                }
            } catch (IOException e) {
            }
        }

        public void getMemoryInfo() {
            DecimalFormat f1 = new DecimalFormat("#,###KB");
            DecimalFormat f2 = new DecimalFormat("##.#");
            long total = Runtime.getRuntime().totalMemory() / 1024;
            long max = Runtime.getRuntime().maxMemory() / 1024;
            long used = total - (Runtime.getRuntime().freeMemory() / 1024);
            ratio = ((double) (100 * used)) / ((double) total);
            String info = "Java メモリ情報 : 合計=" + f1.format(total) + "、" + "使用量=" + f1.format(used) + " (" + f2.format(ratio) + "%)、" + "使用可能最大=" + f1.format(max);
        }

        public void onDrawFrame(GL10 gl) {
            if (bg_load) {
                setupBackground(gl);
                bg_load = false;
            }
            dragMgr.update();
            delegate.setDrag(dragMgr.getX(), dragMgr.getY());
            gl.glClear(16640);
            if (emotion > 3) {
                emotion = 2;
            }
            if (emotion < -3) {
                emotion = -2;
            }
            if (okoru && okoruTime == 0) {
                okoruTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() > okoruTime + 6000) {
                okoru = false;
                okoruTime = 0;
            }
            nowTime = System.currentTimeMillis();
            if (infoTime + 1000 <= System.currentTimeMillis()) {
                infocall = true;
            }
            if (timedisplay != 1 && infocall) {
                infoTime = System.currentTimeMillis();
                infocall = false;
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DATE);
                week = calendar.get(Calendar.DAY_OF_WEEK);
            }
            if (timedisplay != 1) {
                delegate.minEvent();
            }
            if (timedisplay != 1) {
                delegate.timeEvent();
            }
            if (timedisplay != 1) {
                delegate.weekEvent();
            }
            if (timedisplay != 1) {
                delegate.BattelyEvent();
            }
            delegate.CPU_Current();
            delegate.Headphone();
            if (sleep == 0) {
                GetCPU_Speed();
                getMemoryInfo();
            }
            if (delegate.bustlevel >= 1) {
                nowTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - afterTime < 200 || delegate.bustlevel != 2) {
                    delegate.BustUpEvent01();
                    delegate.bustlevel = 2;
                } else {
                    delegate.BustUpEvent00();
                    delegate.bustlevel = 0;
                }
            }
            if (sleep == 1) {
                delegate.sleep01();
            }
            if (LockScreen_off) {
                delegate.CallAisatsu();
            }
            if (plugged >= 1 && !juuden) {
                delegate.JuudenEvent();
                TouchReset();
                juuden = true;
            }
            if (plugged == 0 && juuden) {
                juuden = false;
            }
            delegate.update(gl);
            gl.glMatrixMode(5888);
            gl.glLoadIdentity();
            gl.glDisable(2929);
            gl.glDisable(2884);
            gl.glDisable(3042);
            gl.glEnable(3553);
            gl.glEnableClientState(32888);
            gl.glEnableClientState(32884);
            gl.glTexParameterx(3553, 10242, 33071);
            gl.glTexParameterx(3553, 10243, 33071);
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (f251w < f250h) {
                gl.glOrthof(-1.0f, 1.0f, -1.6f, 1.6f, 0.5f, -0.5f);
            } else {
                gl.glOrthof(-1.6f, 1.6f, -1.0f, 1.0f, 0.5f, -0.5f);
            }
            gl.glPushMatrix();
            if (this.bg != null) {
                gl.glPushMatrix();
                gl.glTranslatef((-0.2f) * (sx / 10.0f), (sy / 10.0f) * 0.1f, 0.0f);
                this.bg.draw(gl);
                gl.glPopMatrix();
            }
            gl.glPushMatrix();
            tr_wp = viewMatrix.getArray();
            if (tr_load) {
                tr_wp[0] = 1.0f;
                tr_wp[5] = 1.0f;
                tr_wp[12] = 0.0f;
                tr_wp[13] = 0.0f;
                if (tate_gamen) {
                    if (tr_wp[13] < -0.22f) {
                        tr_wp[13] = -0.7f;
                    }
                } else {
                    float s = tr_wp[0] * 0.8f;
                    tr_wp[0] = s;
                    tr_wp[5] = s;
                    if (tr_wp[13] < -0.3f) {
                        tr_wp[13] = -0.3f;
                    }
                }
                tr_load = false;
            }
            gl.glMultMatrixf(tr_wp, 0);
            LAppModel model = delegate.getModel(0);
            model.update();
            model.draw(gl);
            gl.glPopMatrix();
            if (timedisplay != 1) {
                gl.glPushMatrix();
                if (f251w < f250h) {
                    if (info_tap) {
                        tr_wp = viewMatrix.getArray();
                        gl.glMultMatrixf(tr_wp, 0);
                    } else {
                        gl.glTranslatef(0.0f, 0.0f, 0.0f);
                        gl.glScalef(0.9f, 0.9f, 1.0f);
                    }
                } else if (info_tap) {
                    tr_wp = viewMatrix.getArray();
                    gl.glMultMatrixf(tr_wp, 0);
                } else {
                    gl.glTranslatef(0.0f, 0.15f, 0.0f);
                    gl.glScalef(0.9f, 0.9f, 1.0f);
                }
//                LAppModel model2 = delegate.getModel(1);
//                if (model2.isInitialized() && !model2.isUpdating()) {
//                    model2.update();
//                    model2.draw(gl);
//                }
                gl.glPopMatrix();
            }
            gl.glPopMatrix();
        }

        private void setupBackground(GL10 gl) {
            try {
                if (tate_gamen || !tablet) {
                    this.bg = new SimpleImage(gl, FileManager.open(LAppDefine.BACK_IMAGE_NAME));
                    this.bg.setDrawRect(-1.2f, 1.2f, LAppDefine.VIEW_LOGICAL_MAX_BOTTOM_BG, LAppDefine.VIEW_LOGICAL_MAX_TOP_BG);
                } else {
                    this.bg = new SimpleImage(gl, FileManager.open(LAppDefine.BACK_IMAGE_NAME2));
                    this.bg.setDrawRect(LAppDefine.VIEW_LOGICAL_MAX_LEFT_BG2, LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2, -1.2f, 1.2f);
                }
                this.bg.setUVRect(0.0f, 1.0f, 0.0f, 1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MySensorListener implements SensorEventListener {
        private static final int FORCE_THRESHOLD = 400;
        private static final int SHAKE_COUNT = 3;
        private static final int SHAKE_DURATION = 100;
        private static final int SHAKE_TIMEOUT = 500;
        private static final int TIME_THRESHOLD = 100;
        private long mLastForce;
        private long mLastShake;
        private long mLastTime;
        private float mLastX = -1.0f;
        private float mLastY = -1.0f;
        private float mLastZ = -1.0f;
        private int mShakeCount = 0;

        MySensorListener() {
        }

        public void onSensorChanged(SensorEvent e) {
            rota();
            if (e.sensor.getType() == 1) {
                if (val == 0) {
                    sx = e.values[0];
                    sy = e.values[1];
                } else if (val == 90) {
                    sx = e.values[1] * -1.0f;
                    sy = e.values[0];
                } else if (val == 180) {
                    sx = e.values[0] * -1.0f;
                    sy = e.values[1] * -1.0f;
                } else if (val == 270) {
                    sx = e.values[1];
                    sy = e.values[0] * -1.0f;
                } else {
                    sx = e.values[0];
                    sy = e.values[1];
                }
                long now = System.currentTimeMillis();
                if (now - this.mLastForce > 500) {
                    this.mShakeCount = 0;
                }
                if (now - this.mLastTime > 100) {
                    if ((Math.abs(((((e.values[0] + e.values[1]) + e.values[2]) - this.mLastX) - this.mLastY) - this.mLastZ) / ((float) (now - this.mLastTime))) * 10000.0f > 400.0f) {
                        int i = this.mShakeCount + 1;
                        this.mShakeCount = i;
                        if (i >= 3 && now - this.mLastShake > 100) {
                            this.mLastShake = now;
                            this.mShakeCount = 0;
                            live2DMgr.shakeEvent();
                            TouchDistanceTime = System.currentTimeMillis();
                        }
                        this.mLastForce = now;
                    }
                    this.mLastTime = now;
                    this.mLastX = e.values[0];
                    this.mLastY = e.values[1];
                    this.mLastZ = e.values[2];
                }
            }
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
    }

    public void log(String msg) {
    }

    public Engine onCreateEngine() {
        sensorListener = new MySensorListener();
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(1);
        instance = this;
        this.live2DMgr = new LAppLive2DManager_WP();
        this.delegate = this.live2DMgr;
        FileManager.init(getApplicationContext());
        return new GLEngine();
    }

    public void rota() {
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        Display d = wm.getDefaultDisplay();
        d.getMetrics(displayMetrics);
        switch (d.getRotation()) {
            case 0:
                this.val = 0;
                return;
            case 1:
                this.val = 90;
                return;
            case 2:
                this.val = 180;
                return;
            case 3:
                this.val = 270;
                return;
            default:
                return;
        }
    }

    public static void TouchReset() {
        if (LAppLive2DManager_WP.ChrTouch) {
            TouchDistanceTime = System.currentTimeMillis();
        }
        if (sleep == 1) {
            wakeup = true;
        }
    }

    public void updateViewMatrix(float dx, float dy, float cx, float cy, float scale, boolean enableEvent) {
        boolean isMaxScale = this.viewMatrix.isMaxScale();
        boolean isMinScale = this.viewMatrix.isMinScale();
        this.viewMatrix.adjustScale(cx, cy, scale);
        this.viewMatrix.adjustTranslate(dx, dy);
        if (enableEvent) {
            if (!isMaxScale && this.viewMatrix.isMaxScale()) {
                this.delegate.maxScaleEvent();
            }
            if (!isMinScale && this.viewMatrix.isMinScale()) {
                this.delegate.minScaleEvent();
            }
        }
    }

    private float transformDeviceToViewX(float deviceX) {
        return this.viewMatrix.invertTransformX(this.deviceToScreen.transformX(deviceX));
    }

    private float transformDeviceToViewY(float deviceY) {
        return this.viewMatrix.invertTransformY(this.deviceToScreen.transformY(deviceY));
    }

    public void touchesBegan(float p1x, float p1y) {
        this.touchMgr.touchBegan(p1x, p1y);
        this.dragMgr.set(transformDeviceToViewX(this.touchMgr.getX()), transformDeviceToViewY(this.touchMgr.getY()));
    }

    public void touchesBegan(float p1x, float p1y, float p2x, float p2y) {
        this.touchMgr.touchBegan(p1x, p1y, p2x, p2y);
        this.dragMgr.set(transformDeviceToViewX(this.touchMgr.getX()), transformDeviceToViewY(this.touchMgr.getY()));
    }

    public void touchesMoved(float p1x, float p1y) {
        this.touchMgr.touchesMoved(p1x, p1y);
        this.dragMgr.set(transformDeviceToViewX(this.touchMgr.getX()), transformDeviceToViewY(this.touchMgr.getY()));
        if (this.touchMgr.isSingleTouch() && this.touchMgr.isFlickAvailable() && this.touchMgr.getFlickDistance() > 100.0f) {
            this.delegate.flickEvent(transformDeviceToViewX(this.touchMgr.getStartX()), transformDeviceToViewY(this.touchMgr.getStartY()));
            this.touchMgr.disableFlick();
        }
    }

    public void touchesMoved(float p1x, float p1y, float p2x, float p2y) {
        this.touchMgr.touchesMoved(p1x, p1y, p2x, p2y);
        float dx = this.touchMgr.getDeltaX() * this.deviceToScreen.GetScaleX();
        float dy = this.touchMgr.getDeltaY() * this.deviceToScreen.GetScaleY();
        float cx = this.deviceToScreen.transformX(this.touchMgr.getCenterX()) * this.touchMgr.getScale();
        float cy = this.deviceToScreen.transformY(this.touchMgr.getCenterY()) * this.touchMgr.getScale();
        float scale = this.touchMgr.getScale();
        this.VIEW_LOGICAL_MAX_LEFT = 0.0f - ((LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0]) - 0.5f);
        this.VIEW_LOGICAL_MAX_RIGHT = (LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 / tr_wp[0]) - 0.5f;
        this.VIEW_LOGICAL_MAX_BOTTOM = -2.5f / tr_wp[0];
        this.VIEW_LOGICAL_MAX_TOP = 2.5f / tr_wp[0];
        this.viewMatrix.setMaxScreenRect(this.VIEW_LOGICAL_MAX_LEFT, this.VIEW_LOGICAL_MAX_RIGHT, this.VIEW_LOGICAL_MAX_BOTTOM, this.VIEW_LOGICAL_MAX_TOP);
        updateViewMatrix(dx, dy, cx, cy, scale, true);
        this.dragMgr.set(transformDeviceToViewX(this.touchMgr.getX()), transformDeviceToViewY(this.touchMgr.getY()));
    }

    public void touchesFlick(float p1x, float p1y) {
        if (this.touchMgr.getFlickDistance() > 100.0f) {
            this.delegate.flickEvent(transformDeviceToViewX(this.touchMgr.getStartX()), transformDeviceToViewY(this.touchMgr.getStartY()));
            this.touchMgr.disableFlick();
        }
    }

    public void touchesTurn(float p1x, float p1y) {
        this.delegate.TurnEvent(transformDeviceToViewX(this.touchMgr.getStartX()), transformDeviceToViewY(this.touchMgr.getStartY()));
        this.touchMgr.disableFlick();
    }

    public void touchesEnded() {
        this.dragMgr.set(0.0f, 0.0f);
        this.delegate.TouchUpEvent();
    }

    public void touchesEnded(float p1x, float p1y) {
        this.dragMgr.set(0.0f, 0.0f);
        this.delegate.TouchUpEvent();
        this.touchMgr.touchBegan(p1x, p1y);
        this.touchMgr.touchBegan(p1x, p1y);
        this.dragMgr.set(transformDeviceToViewX(this.touchMgr.getX()), transformDeviceToViewY(this.touchMgr.getY()));
    }

    public void SettingItem() {
        this.bg_load = true;
        this.tablet = false;
        time_ohayo = 0;
        time_am = 0;
        time_pm = 0;
        info_tap = false;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MONTH);
        second = calendar.get(Calendar.SECOND);
        this.nowTime = System.currentTimeMillis();
        saveyear = year;
        savemonth = month;
        saveday = day;
        saveTime = nowTime;

        diffDays = 0;
        long diffSeconds = (this.nowTime - saveTime) / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        diffDays = diffHours / 24;
        BatterySaving = true;
        TapAction = true;
        Voice = true;
        Time = true;
        VisibleTime = System.currentTimeMillis();
        this.listStr = "Standard";
        this.listSize = "NORMAL";
        if (this.listSize.equals("HIGH")) {
            size = 0;
        } else if (this.listSize.equals("NORMAL")) {
            size = 1;
        } else if (this.listSize.equals("LOW")) {
            size = 2;
        } else {
            size = 1;
        }
        this.TimeDisplay = "Display";
        if (this.TimeDisplay.equals("Display") || this.TimeDisplay.equals("表示")) {
            timedisplay = 0;
        } else if (this.TimeDisplay.equals("Hide") || this.TimeDisplay.equals("非表示")) {
            timedisplay = 1;
        } else {
            timedisplay = 0;
        }
        if (this.listStr.equals("アニメーション停止") || this.listStr.equals("Animation Stop")) {
            this.sel_time = 60000;
        }
        if (this.listStr.equals("１ (遅い)") || this.listStr.equals("１ (Slow)")) {
            this.sel_time = 180;
        }
        if (this.listStr.equals("２") || this.listStr.equals("２")) {
            this.sel_time = 150;
        }
        if (this.listStr.equals("３") || this.listStr.equals("３")) {
            this.sel_time = 100;
        }
        if (this.listStr.equals("４") || this.listStr.equals("４")) {
            this.sel_time = 67;
        }
        if (this.listStr.equals("５ (速い)") || this.listStr.equals("５ (Fast)")) {
            this.sel_time = 30;
        }
    }

    public void bitmap_set() {
        Options imageOptions = new Options();
        imageOptions.inJustDecodeBounds = true;
        imageOptions.inPreferredConfig = Config.RGB_565;
        if (path != null) {
            this.bm = BitmapFactory.decodeFile(path, imageOptions);
        }
        float imageScaleWidth = ((float) imageOptions.outWidth) / ((float) 300);
        float imageScaleHeight = ((float) imageOptions.outHeight) / ((float) 300);
        Options imageOptions2 = new Options();
        imageOptions2.inPreferredConfig = Config.RGB_565;
        if (imageScaleWidth <= LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2 || imageScaleHeight <= LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2) {
            this.bm = BitmapFactory.decodeFile(path, imageOptions2);
            return;
        }
        if (imageScaleWidth <= imageScaleHeight) {
            imageScaleHeight = imageScaleWidth;
        }
        int imageScale = (int) Math.floor((double) imageScaleHeight);
        for (int i = 2; i < imageScale; i *= 2) {
            imageOptions2.inSampleSize = i;
        }
        this.bm = BitmapFactory.decodeFile(path, imageOptions2);
    }

    public void ResetPosition() {
        if (this.f251w < this.f250h) {
            tate_gamen = true;
            log("表示位置の読み込み（タテ画面）");
        } else {
            tate_gamen = false;
            log("表示位置の読み込み（ヨコ画面）");
        }
        if (tate_gamen) {
            if (this.tate_gamen_m) {
                katamuki = false;
            }
            if (!this.tate_gamen_m) {
                katamuki = true;
            }
            this.tate_gamen_m = true;
        } else {
            if (this.tate_gamen_m) {
                katamuki = true;
            }
            if (!this.tate_gamen_m) {
                katamuki = false;
            }
            this.tate_gamen_m = false;
        }
        this.tr_load = true;
    }
}
