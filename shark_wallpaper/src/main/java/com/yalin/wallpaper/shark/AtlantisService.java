/*
 * Copyright (C) 2011 QSDN,Inc.
 * Copyright (C) 2011 Atsushi Konno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yalin.wallpaper.shark;

import android.content.Context;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;
import com.yalin.wallpaper.shark.util.MatrixTrackingGL;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;


//import android.os.Debug;


public class AtlantisService extends WallpaperServiceProxy {
    private static final String TAG = AtlantisService.class.getName();
    private static final boolean _debug = false;
    private static final int RETRY_COUNT = 3;

    public AtlantisService(Context host) {
        super(host);
    }

    private class AtlantisEngine extends ActiveEngine {
        private final String TAG = AtlantisEngine.class.getName();
        private int width = 0;
        private int height = 0;
        private boolean binded = false;
        private boolean mInitialized = false;

        /* EGL関連は毎回再作成？？ */
        private MatrixTrackingGL gl10 = null;
        private EGL10 egl10 = null;
        private EGLContext eglContext = null;
        private EGLDisplay eglDisplay = null;
        private EGLSurface eglSurface = null;
        private GLRenderer glRenderer = null;
        private long BASE_TICK = 45410157L;

        private ExecutorService getExecutor() {
            if (executor == null) {
                executor = Executors.newSingleThreadExecutor();
            }
            return executor;
        }

        private ExecutorService executor = null;
        private Runnable drawCommand = null;

        private void doExecute(Runnable command) {
            if (command == null) {
                return;
            }
            while (true) {
                try {
                    getExecutor().execute(command);
                } catch (RejectedExecutionException e) {
                    if (getExecutor().isShutdown()) {
                        // ignore
                    } else {
                        Log.e(TAG, "command execute failure", e);
                        waitNano();
                        System.gc();
                        continue;
                    }
                }
                break;
            }
        }


        @Override
        public void onCreate(final SurfaceHolder holder) {
            if (_debug) Log.d(TAG, "start onCreate() [" + this + "]");
            super.onCreate(holder);
      /*=====================================================================*/
      /* 携帯電話として機能しなくなるので                                    */
      /* タッチイベントは無効にしておく.                                     */
      /* 画面の空いたところのタッチにだけ反応したいので                      */
      /* Engine.onCommandで対応する                                          */
      /*=====================================================================*/
            setTouchEventsEnabled(false);

            if (_debug) Log.d(TAG, "end onCreate() [" + this + "]");
        }

        @Override
        public void onDestroy() {
            if (_debug) Log.d(TAG, "start onDestroy() [" + this + "]");
            super.onDestroy();
            System.gc();
            if (_debug) Log.d(TAG, "end onDestroy() [" + this + "]");
        }

        int[][] configSpec = {
                {
                        // RGB565 color
                        EGL10.EGL_RED_SIZE, 5,
                        EGL10.EGL_GREEN_SIZE, 6,
                        EGL10.EGL_BLUE_SIZE, 5,
                        EGL10.EGL_ALPHA_SIZE, EGL10.EGL_DONT_CARE,
                        EGL10.EGL_DEPTH_SIZE, 24,
                        EGL10.EGL_STENCIL_SIZE, EGL10.EGL_DONT_CARE,
                        //  window (and not a pixmap or a pbuffer)
                        EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT,
                        EGL10.EGL_NONE,
                },
                {
                        // RGB565 color
                        EGL10.EGL_RED_SIZE, 5,
                        EGL10.EGL_GREEN_SIZE, 6,
                        EGL10.EGL_BLUE_SIZE, 5,
                        EGL10.EGL_ALPHA_SIZE, EGL10.EGL_DONT_CARE,
                        EGL10.EGL_DEPTH_SIZE, 16,
                        EGL10.EGL_STENCIL_SIZE, EGL10.EGL_DONT_CARE,
                        //  window (and not a pixmap or a pbuffer)
                        EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT,
                        EGL10.EGL_NONE,
                },
                {
                        EGL10.EGL_NONE,
                }
        };

        @Override
        public void onSurfaceCreated(final SurfaceHolder holder) {

            if (_debug) Log.d(TAG, "start onSurfaceCreated() [" + this + "]");
            super.onSurfaceCreated(holder);
            Runnable surfaceCreatedCommand = new Runnable() {
                @Override
                public void run() {
                    doSurfaceCreated();
                }

                private void doSurfaceCreated() {
                    if (mInitialized) {
                        if (_debug) Log.d(TAG, "already Initialized(surfaceCreatedCommand)");
                        return;
                    }
                    boolean ret;
          /* OpenGLの初期化 */
                    int counter = 0;
                    int specCounter = 0;
                    while (true) {
                        if (_debug) Log.d(TAG, "start EGLContext.getEGL()");
                        exitEgl();
                        egl10 = (EGL10) EGLContext.getEGL();
                        if (_debug) Log.d(TAG, "end EGLContext.getEGL()");
                        if (_debug) Log.d(TAG, "start eglGetDisplay");
                        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                        if (_debug) Log.d(TAG, "end eglGetDisplay");
                        if (eglDisplay == null || EGL10.EGL_NO_DISPLAY.equals(eglDisplay)) {
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            if (_debug) Log.d(TAG, "eglGetDisplayがEGL_NO_DISPLAY [" + errStr + "]");
                            exitEgl();
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                Log.e(TAG, "egl10.eglCreateContextがEGL_NO_DISPLAY");
                                throw new RuntimeException("OpenGL Error(EGL_NO_DISPLAY) "
                                        + errStr + ": "
                                );
                            }
                            if (_debug) Log.d(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }
                        {
                            egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                        }
                        int[] version = new int[2];
                        if (!egl10.eglInitialize(eglDisplay, version)) {
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            if (_debug) Log.d(TAG, "egl10.eglInitializeがfalse [" + errStr + "]");
                            exitEgl();
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                Log.e(TAG, "egl10.eglInitializeがfalse");
                                throw new RuntimeException("OpenGL Error(eglInitialize) "
                                        + errStr + ": "
                                );
                            }
                            if (_debug) Log.d(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }

                        EGLConfig[] configs = new EGLConfig[1];
                        int[] numConfig = new int[1];

            /*-----------------------------------------------------------------*/
            /* 条件に見合うEGLConfigを取得                                     */
            /*-----------------------------------------------------------------*/
                        egl10.eglChooseConfig(eglDisplay, configSpec[specCounter++], configs, 1, numConfig);
            /*-----------------------------------------------------------------*/
            /* もしEGLConfigが取得できなければ                                 */
            /*-----------------------------------------------------------------*/
                        if (numConfig[0] == 0) {
                            if (_debug) Log.d(TAG, "numConfig[0]=" + numConfig[0] + "");
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            errStr += " eglChooseConfig numConfig == 0 ";
                            errStr += " numConfig:[" + numConfig[0] + "]";
                            errStr += " specCounter:[" + (specCounter - 1) + "]";
                            exitEgl();
                            Log.e(TAG, "eglChooseConfig失敗:"
                                    + "numConfig:[" + numConfig[0] + "]"
                                    + errStr);
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                throw new RuntimeException("OpenGL Error "
                                        + errStr + " :"
                                );
                            }
                            if (_debug) Log.d(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }

                        EGLConfig config = configs[0];

            /*-----------------------------------------------------------------*/
            /* 取得したEGLDisplayとEGLConfigでEGLContext作成                   */
            /*-----------------------------------------------------------------*/
                        eglContext = egl10.eglCreateContext(eglDisplay, config, EGL10.EGL_NO_CONTEXT, null);
                        if (eglContext == null || EGL10.EGL_NO_CONTEXT.equals(eglContext)) {
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            errStr += "egl10.eglCreateContext == EGL_NO_CONTEXT ";
                            errStr += "specCounter:[" + (specCounter - 1) + "]";
                            if (_debug)
                                Log.d(TAG, "egl10.eglCreateContext == EGL_NO_CONTEXT [" + errStr + "]");
                            exitEgl();
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                Log.e(TAG, "egl10.eglCreateContextがEGL_NO_CONTEXT");
                                throw new RuntimeException("OpenGL Error(EGL_NO_CONTEXT) "
                                        + errStr + " :"
                                );
                            }
                            if (_debug) Log.d(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }
                        if (_debug) Log.d(TAG, "eglCreateContext done.");
            /*-----------------------------------------------------------------*/
            /* 取得したEGLDisplayとEGLConfigでEGLSurface作成                   */
            /*-----------------------------------------------------------------*/
                        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, config, holder, null);
                        if (eglSurface == null || EGL10.EGL_NO_SURFACE.equals(eglSurface)) {
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            errStr += "egl10.eglCreateWindowSurface == EGL_NO_SURFACE ";
                            errStr += "specCounter:[" + (specCounter - 1) + "]";
                            if (_debug)
                                Log.d(TAG, "egl10.eglCreateWindowSurface == EGL_NO_SURFACE [" + errStr + "]");
                            exitEgl();
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                Log.e(TAG, "egl10.eglCreateWindowSurfaceがEGL_NO_SURFACE");
                                throw new RuntimeException("OpenGL Error(EGL_NO_SURFACE) "
                                        + errStr + " :"
                                );
                            }
                            if (_debug) Log.e(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }
                        if (_debug) Log.d(TAG, "eglCreateWindowSurface done.");
            /*-----------------------------------------------------------------*/
            /* EGLContextとEGLSurfaceを関連付ける(アタッチ)                    */
            /*-----------------------------------------------------------------*/
                        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                            String errStr = AtlantisService.getErrorString(egl10.eglGetError());
                            errStr += "egl10.eglMakeCurrent == false ";
                            errStr += "specCounter:[" + (specCounter - 1) + "]";
                            if (_debug)
                                Log.d(TAG, "egl10.eglMakeCurrent == false [" + errStr + "]");
                            exitEgl();
                            if (++counter >= AtlantisService.RETRY_COUNT) {
                                Log.e(TAG, "egl10.eglMakeCurrentがfalse");
                                throw new RuntimeException("OpenGL Error(eglMakeCurrent) "
                                        + errStr + " :"
                                );
                            }
                            if (_debug) Log.d(TAG, "RETRY");
                            System.gc();
                            waitNano();
                            continue;
                        }

                        if (_debug) Log.d(TAG, "eglMakeCurrent done.");
                        if (_debug) Log.d(TAG, "now create gl10 object");

                        gl10 = new MatrixTrackingGL((GL10) (eglContext.getGL()));
   
            /*-----------------------------------------------------------------*/
            /* Rendererの初期化                                                */
            /*-----------------------------------------------------------------*/
                        glRenderer = GLRenderer.getInstance(getApplicationContext());
                        synchronized (glRenderer) {
                            glRenderer.onSurfaceCreated(gl10, config, getApplicationContext());
                        }
                        if (_debug) Log.d(TAG, "EGL initalize done.");
                        mInitialized = true;
                        if (drawCommand == null) {
                            drawCommand = new Runnable() {
                                public void run() {
                                    doDrawCommand();
                                }

                                protected void doDrawCommand() {
                                    if (mInitialized && glRenderer != null && gl10 != null) {
                                        synchronized (glRenderer) {
                                            long nowTime = System.nanoTime();
                                            if (nowTime - glRenderer.prevTick < BASE_TICK) {
                                                try {
                                                    TimeUnit.NANOSECONDS.sleep(BASE_TICK - (nowTime - glRenderer.prevTick));
                                                } catch (InterruptedException e) {
                                                }
                                            }
                                            glRenderer.onDrawFrame(gl10);
                                            glRenderer.prevTick = nowTime;
                                        }
                                        egl10.eglSwapBuffers(eglDisplay, eglSurface);
                                        if (!getExecutor().isShutdown() && isVisible() && egl10.eglGetError() != EGL11.EGL_CONTEXT_LOST) {
                                            doExecute(drawCommand);
                                        }
                                    }
                                }
                            };
                            doExecute(drawCommand);
                        }
                        break;
                    }
                }
            };
            doExecute(surfaceCreatedCommand);
            if (_debug) Log.d(TAG, "end onSurfaceCreated() [" + this + "]");
        }

        @Override
        public void onSurfaceDestroyed(final SurfaceHolder holder) {
            if (_debug) Log.d(TAG, "start onSurfaceDestroyed() [" + this + "]");
            Runnable surfaceDestroyedCommand = new Runnable() {
                @Override
                public void run() {
                    doSurfaceDestroyedCommand();
                }

                private void doSurfaceDestroyedCommand() {
                    synchronized (glRenderer) {
                        glRenderer.onSurfaceDestroyed(gl10);
                    }
                    exitEgl();
                    gl10.shutdown();
                    gl10 = null;
                    System.gc();
                    mInitialized = false;
                }
            };

            doExecute(surfaceDestroyedCommand);
            getExecutor().shutdown();
            try {
                if (!getExecutor().awaitTermination(60, TimeUnit.SECONDS)) {
                    getExecutor().shutdownNow();
                    if (!getExecutor().awaitTermination(60, TimeUnit.SECONDS)) {
                        if (_debug) Log.d(TAG, "ExecutorService did not terminate....");
                        getExecutor().shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            drawCommand = null;
            super.onSurfaceDestroyed(holder);
            if (_debug) Log.d(TAG, "end onSurfaceDestroyed() [" + this + "]");
        }

        @Override
        public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
            if (_debug) Log.d(TAG, "start onSurfaceChanged() [" + this + "]");
            super.onSurfaceChanged(holder, format, width, height);
            this.width = width;
            this.height = height;
            Runnable surfaceChangedCommand = new Runnable() {
                public void run() {
                    doSurfaceChanged();
                }

                private void doSurfaceChanged() {
                    if (glRenderer != null && gl10 != null && mInitialized) {
                        synchronized (glRenderer) {
                            glRenderer.onSurfaceChanged(gl10, width, height);
                        }
                    }
                }

                ;
            };
            doExecute(surfaceChangedCommand);
            if (_debug) Log.d(TAG, "end onSurfaceChanged() [" + this + "]");
        }

        @Override
        public void onVisibilityChanged(final boolean visible) {
            if (_debug) Log.d(TAG, "start onVisibilityChanged()");
            super.onVisibilityChanged(visible);
      /* サーフェスが見えるようになったよ！ */
            if (visible && drawCommand != null && mInitialized) {
        /* 設定変更のタイミング */
                if (glRenderer != null) {
                    synchronized (glRenderer) {
                        glRenderer.updateSetting(getApplicationContext());
                    }
                }
                doExecute(drawCommand);
            }
            if (_debug) Log.d(TAG, "end onVisibilityChanged()");
        }

        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset,
                                     final float xOffsetStep, final float yOffsetStep,
                                     final int xPixelOffset, final int yPixelOffset) {
            if (_debug) Log.d(TAG, "start onOffsetsChanged()");
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            if (xOffsetStep == 0.0f && yOffsetStep == 0.0f) {
                if (_debug) Log.d(TAG, "end onOffsetChanged() no execute");
                return;
            }
            Runnable offsetsChangedCommand = new Runnable() {
                public void run() {
                    doOffsetsChanged();
                }

                private void doOffsetsChanged() {
                    if (mInitialized && glRenderer != null && gl10 != null) {
                        synchronized (glRenderer) {
                            glRenderer.onOffsetsChanged(gl10, xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
                        }
                    }
                }

                ;
            };
            doExecute(offsetsChangedCommand);
            if (_debug) Log.d(TAG, "end onOffsetChanged()");
        }

        @Override
        public Bundle onCommand(final String action, final int x, final int y, final int z, final Bundle extras, final boolean resultRequested) {
            if (_debug) {
                Log.d(TAG, "start onCommand "
                        + "action:[" + action + "]:"
                        + "x:[" + x + "]:"
                        + "y:[" + y + "]:"
                        + "z:[" + z + "]:"
                        + "extras:[" + extras + "]:"
                        + "resultRequested:[" + resultRequested + "]:"
                );
            }
      /*=====================================================================*/
      /* 画面の何もないところへのタッチにだけ反応するため                    */
      /* actionがandroid.wallpaper.tapのときだけ処理する                     */
      /*=====================================================================*/
            if (action.equals("android.wallpaper.tap")) {
                Runnable onCommandCommand = new Runnable() {
                    public void run() {
                        doCommandCommand();
                    }

                    private void doCommandCommand() {
                        if (mInitialized && glRenderer != null && gl10 != null) {
                            synchronized (glRenderer) {
                                glRenderer.onCommand(gl10, action, x, y, z, extras, resultRequested);
                            }
                        }
                    }
                };
                doExecute(onCommandCommand);
            }
            Bundle ret = super.onCommand(action, x, y, z, extras, resultRequested);
            if (_debug) Log.d(TAG, "end onCommand");
            return ret;
        }

        public void exitEgl() {
            if (_debug) Log.d(TAG, "start exitEgl");
            if (egl10 != null) {
                if (eglDisplay != null && !eglDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
                    if (!egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_CONTEXT)) {
                        Log.e(TAG, "eglMakeCurrentがfalse [" + AtlantisService.getErrorString(egl10.eglGetError()) + "]");
                    }

                    if (eglSurface != null && !eglSurface.equals(EGL10.EGL_NO_SURFACE)) {
                        if (!egl10.eglDestroySurface(eglDisplay, eglSurface)) {
                            Log.e(TAG, "eglDestroySurfaceがfalse [" + AtlantisService.getErrorString(egl10.eglGetError()) + "]");
                        }
                        eglSurface = null;
                    }
                    if (eglContext != null && !eglContext.equals(EGL10.EGL_NO_CONTEXT)) {
                        if (!egl10.eglDestroyContext(eglDisplay, eglContext)) {
                            Log.e(TAG, "eglDestroyContextがfalse [" + AtlantisService.getErrorString(egl10.eglGetError()) + "]");
                        }
                        eglContext = null;
                    }
                    if (!egl10.eglTerminate(eglDisplay)) {
                        Log.e(TAG, "eglTerminateがfalse [" + AtlantisService.getErrorString(egl10.eglGetError()) + "]");
                    }
                    eglDisplay = null;
                }
                egl10 = null;
            }
            if (_debug) Log.d(TAG, "end exitEgl");
        }
    }

    @Override
    public Engine onCreateEngine() {
        if (_debug) Log.d(TAG, "start onCreateEngine()");
        AtlantisEngine engine = new AtlantisEngine();
        if (_debug) Log.d(TAG, "engine:[" + engine + "]");
        if (_debug) Log.d(TAG, "end onCreateEngine()");
        return engine;
    }

    public void waitNano() {
        if (_debug) Log.d(TAG, "start waitNano");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        if (_debug) Log.d(TAG, "end waitNano");
    }

    public static String getErrorString(int err) {
        switch (err) {
            case EGL10.EGL_NOT_INITIALIZED:
                return "EGL_NOT_INITIALIZED";
            case EGL10.EGL_BAD_ACCESS:
                return "EGL_BAD_ACCESS";
            case EGL10.EGL_BAD_ALLOC:
                return "EGL_BAD_ALLOC";
            case EGL10.EGL_BAD_ATTRIBUTE:
                return "EGL_BAD_ATTRIBUTE";
            case EGL10.EGL_BAD_CONTEXT:
                return "EGL_BAD_CONTEXT";
            case EGL10.EGL_BAD_CONFIG:
                return "EGL_BAD_CONFIG";
            case EGL10.EGL_BAD_CURRENT_SURFACE:
                return "EGL_BAD_CURRENT_SURFACE";
            case EGL10.EGL_BAD_DISPLAY:
                return "EGL_BAD_DISPLAY";
            case EGL10.EGL_BAD_SURFACE:
                return "EGL_BAD_SURFACE";
            case EGL10.EGL_BAD_MATCH:
                return "EGL_BAD_MATCH";
            case EGL10.EGL_BAD_PARAMETER:
                return "EGL_BAD_PARAMETER";
            case EGL10.EGL_BAD_NATIVE_PIXMAP:
                return "EGL_BAD_NATIVE_PIXMAP";
            case EGL10.EGL_BAD_NATIVE_WINDOW:
                return "EGL_BAD_NATIVE_WINDOW";
            case EGL11.EGL_CONTEXT_LOST:
                return "EGL_CONTEXT_LOST";
            default:
                return "OTHER err:[" + err + "]";
        }
    }
}
