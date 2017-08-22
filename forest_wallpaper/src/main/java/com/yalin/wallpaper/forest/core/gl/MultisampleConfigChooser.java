package com.yalin.wallpaper.forest.core.gl;

import android.opengl.GLSurfaceView.EGLConfigChooser;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class MultisampleConfigChooser implements EGLConfigChooser {
    private boolean mUsesCoverageAa;
    private int[] mValue;

    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        this.mValue = new int[1];
        int[] configSpec = new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12338, 1, 12337, 2, 12344};
        if (egl.eglChooseConfig(display, configSpec, null, 0, this.mValue)) {
            int numConfigs = this.mValue[0];
            if (numConfigs <= 0) {
                configSpec = new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12512, 1, 12513, 2, 12344};
                if (egl.eglChooseConfig(display, configSpec, null, 0, this.mValue)) {
                    numConfigs = this.mValue[0];
                    if (numConfigs <= 0) {
                        configSpec = new int[]{12324, 8, 12323, 8, 12322, 8, 12325, 16, 12352, 4, 12344};
                        if (egl.eglChooseConfig(display, configSpec, null, 0, this.mValue)) {
                            numConfigs = this.mValue[0];
                            if (numConfigs <= 0) {
                                throw new IllegalArgumentException("No configs match configSpec");
                            }
                        }
                        throw new IllegalArgumentException("3rd eglChooseConfig failed");
                    }
                    this.mUsesCoverageAa = true;
                } else {
                    throw new IllegalArgumentException("2nd eglChooseConfig failed");
                }
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            if (egl.eglChooseConfig(display, configSpec, configs, numConfigs, this.mValue)) {
                EGLConfig config = configs.length > 0 ? configs[0] : null;
                if (config != null) {
                    return config;
                }
                throw new IllegalArgumentException("No config chosen");
            }
            throw new IllegalArgumentException("data eglChooseConfig failed");
        }
        throw new IllegalArgumentException("eglChooseConfig failed");
    }

    private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
        if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
            return this.mValue[0];
        }
        return defaultValue;
    }

    public boolean usesCoverageAa() {
        return this.mUsesCoverageAa;
    }
}
