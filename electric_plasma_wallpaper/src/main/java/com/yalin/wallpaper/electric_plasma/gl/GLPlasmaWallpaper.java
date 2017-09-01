package com.yalin.wallpaper.electric_plasma.gl;

import android.content.Context;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.yalin.style.engine.GLWallpaperServiceProxy;
import com.yalin.wallpaper.electric_plasma.AccelerometerColor;
import com.yalin.wallpaper.electric_plasma.ColorStream;
import com.yalin.wallpaper.electric_plasma.Complexity;
import com.yalin.wallpaper.electric_plasma.GeneralBatteryColor;
import com.yalin.wallpaper.electric_plasma.GeneralCompassColor;
import com.yalin.wallpaper.electric_plasma.GeneralCycleColor;
import com.yalin.wallpaper.electric_plasma.PreferenceModernizer;
import com.yalin.wallpaper.electric_plasma.RandomColor;
import com.yalin.wallpaper.electric_plasma.SensorColor;
import com.yalin.wallpaper.electric_plasma.StaticColor;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class GLPlasmaWallpaper extends GLWallpaperServiceProxy {

    public GLPlasmaWallpaper(Context host) {
        super(host);
    }

    private abstract class BaseConfigChooser implements GLSurfaceView.EGLConfigChooser {
        protected int[] mConfigSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = filterConfigSpec(configSpec);
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            if (egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config)) {
                int numConfigs = num_config[0];
                if (numConfigs <= 0) {
                    throw new IllegalArgumentException("No configs match configSpec");
                }
                EGLConfig[] configs = new EGLConfig[numConfigs];
                if (egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config)) {
                    EGLConfig config = chooseConfig(egl, display, configs);
                    if (config != null) {
                        return config;
                    }
                    throw new IllegalArgumentException("No config chosen");
                }
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            throw new IllegalArgumentException("eglChooseConfig failed");
        }

        private int[] filterConfigSpec(int[] configSpec) {
            int len = configSpec.length;
            int[] newConfigSpec = new int[(len + 2)];
            System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1);
            newConfigSpec[len - 1] = 12352;
            newConfigSpec[len] = 4;
            newConfigSpec[len + 1] = 12344;
            return newConfigSpec;
        }
    }

    class GLES20ContextFactory implements GLSurfaceView.EGLContextFactory {
        private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;

        GLES20ContextFactory() {
        }

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT,
                    new int[]{EGL_CONTEXT_CLIENT_VERSION, 2, 12344});
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }

    private class GetSensorService implements SensorColor.SensorManagerGetter {
        private GetSensorService() {
        }

        public SensorManager get() {
            return (SensorManager) GLPlasmaWallpaper.this.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    class WallpaperEngine extends GLActiveEngine {
        private GLPlasma plasma;
        private boolean touchEnabled = false;

        public WallpaperEngine() {
            super();
            setEGLContextFactory(new GLES20ContextFactory());
            setEGLConfigChooser(new SimpleEGLConfigChooser(false));
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
            this.touchEnabled = true;
            this.plasma = new GLPlasma(GLPlasmaWallpaper.this,
                    metrics.widthPixels, metrics.heightPixels,
                    Complexity.getFromPreferences(),
                    getPaletteFromPrefs(), 50,
                    GLPlasmaWallpaper.getSymmetry(), this.touchEnabled,
                    false,
                    1);
            setTouchEventsEnabled(this.touchEnabled);
            this.plasma.setTouchEnabled(this.touchEnabled);
            setRenderer(this.plasma);
            setRenderMode(1);
            Log.d("plasma", "WallpaperEngine constructor finished.");
        }

        public void onTouchEvent(MotionEvent ev) {
            int action = ev.getAction();
            if (this.touchEnabled && (action == 0 || action == 2)) {
                this.plasma.setTouchCoordinates(ev.getX(), ev.getY());
            } else if (action == 1) {
                this.plasma.touchOff();
            }
        }

        public void onPause() {
            super.onPause();
            this.plasma.pause();
        }

        public void onResume() {
            super.onResume();
            this.plasma.unpause();
        }

        public void onDestroy() {
            super.onDestroy();
            this.plasma.pause();
        }
    }

    private class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize,
                                    int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize,
                    12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl, display, config, 12324, 0);
                    int g = findConfigAttrib(egl, display, config, 12323, 0);
                    int b = findConfigAttrib(egl, display, config, 12322, 0);
                    int a = findConfigAttrib(egl, display, config, 12321, 0);
                    if (r == this.mRedSize && g == this.mGreenSize && b == this.mBlueSize && a == this.mAlphaSize) {
                        return config;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                return this.mValue[0];
            }
            return defaultValue;
        }
    }

    private class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            super(5, 6, 5, 0, withDepthBuffer ? 16 : 0, 0);
        }
    }

    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    ColorStream getPaletteFromPrefs() {
        String s = PreferenceModernizer.colortypeRandom;
        if (s.equals(PreferenceModernizer.colortypeRandom)) {
            return new RandomColor();
        }
        if (s.equals(PreferenceModernizer.colortypeCycle)) {
            return new GeneralCycleColor(PreferenceModernizer.parseMultipleColors(PreferenceModernizer.defaultCycle));
        }
        if (s.equals(PreferenceModernizer.colortypeBattery)) {
            return new GeneralBatteryColor(getBaseContext().getApplicationContext(),
                    PreferenceModernizer.parseMultipleColors(PreferenceModernizer.defaultBattery));
        } else if (s.equals(PreferenceModernizer.colortypeAccel)) {
            return new AccelerometerColor(new GetSensorService());
        } else {
            if (!s.equals(PreferenceModernizer.colortypeCompass)) {
                return new StaticColor(Integer.parseInt(PreferenceModernizer.defaultStatic));
            }
            return new GeneralCompassColor(new GetSensorService(),
                    PreferenceModernizer.parseMultipleColors(PreferenceModernizer.defaultCycle));
        }
    }

    static Symmetry getSymmetry() {
        int symmetry = 0;
        switch (symmetry) {
            case 0:
                return Symmetry.None;
            case 1:
                return Symmetry.Horizontal;
            case 2:
                return Symmetry.HorizontalVertical;
            default:
                return Symmetry.None;
        }
    }
}
