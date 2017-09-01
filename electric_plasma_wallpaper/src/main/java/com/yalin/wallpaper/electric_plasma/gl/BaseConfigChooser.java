package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/* compiled from: GLWallpaperService */
abstract class BaseConfigChooser implements GLSurfaceView.EGLConfigChooser {
    protected int[] mConfigSpec;

    /* compiled from: GLWallpaperService */
    public static class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public /* bridge */ /* synthetic */ EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            return super.chooseConfig(egl10, eGLDisplay);
        }

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            EGLConfig closestConfig = null;
            int closestDistance = 1000;
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int distance = ((Math.abs(findConfigAttrib(egl, display, config, 12324, 0) - this.mRedSize) + Math.abs(findConfigAttrib(egl, display, config, 12323, 0) - this.mGreenSize)) + Math.abs(findConfigAttrib(egl, display, config, 12322, 0) - this.mBlueSize)) + Math.abs(findConfigAttrib(egl, display, config, 12321, 0) - this.mAlphaSize);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestConfig = config;
                    }
                }
            }
            return closestConfig;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                return this.mValue[0];
            }
            return defaultValue;
        }
    }

    /* compiled from: GLWallpaperService */
    public static class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public /* bridge */ /* synthetic */ EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            return super.chooseConfig(egl10, eGLDisplay);
        }

        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            super(4, 4, 4, 0, withDepthBuffer ? 16 : 0, 0);
            this.mRedSize = 5;
            this.mGreenSize = 6;
            this.mBlueSize = 5;
        }
    }

    abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

    public BaseConfigChooser(int[] configSpec) {
        this.mConfigSpec = configSpec;
    }

    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config);
        int numConfigs = num_config[0];
        if (numConfigs <= 0) {
            throw new IllegalArgumentException("No configs match configSpec");
        }
        EGLConfig[] configs = new EGLConfig[numConfigs];
        egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config);
        EGLConfig config = chooseConfig(egl, display, configs);
        if (config != null) {
            return config;
        }
        throw new IllegalArgumentException("No config chosen");
    }
}
