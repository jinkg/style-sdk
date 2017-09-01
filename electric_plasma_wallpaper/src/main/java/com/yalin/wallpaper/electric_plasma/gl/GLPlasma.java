package com.yalin.wallpaper.electric_plasma.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.yalin.wallpaper.electric_plasma.AbstractPlasma;
import com.yalin.wallpaper.electric_plasma.ColorStream;
import com.yalin.wallpaper.electric_plasma.Complexity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLPlasma extends AbstractPlasma implements GLSurfaceView.Renderer {
    private static final float slowrate = 0.01f;
    private ComplexVariables complexVariables = new ComplexVariables();
    private boolean complexVariablesEnabled;
    private long gentime = 0;
    private int height;
    private ByteTexture landscape = null;
    private ByteTexture portrait = null;
    private boolean press = false;
    private final int regenInterval = 86400000;
    private FourStateShader shdr;
    private int smoothness = 1;
    private Symmetry symmetry;
    private TimeKeeper timekeeper;
    private double timestep = 0.0d;
    private boolean touchEnabled;
    private float touchMultiplier = 0.0f;
    private float touchX;
    private float touchY;
    private int width;

    private class ComplexVariables {
        float xcos;
        float ycos;

        private ComplexVariables() {
            this.xcos = (float) ((Math.random() * 3.141592653589793d) * 2.0d);
            this.ycos = (float) ((Math.random() * 3.141592653589793d) * 2.0d);
        }

        public void step(float delta) {
            if (GLPlasma.this.shdr != null) {
                this.xcos = (float) ((((double) this.xcos) + (((double) delta) * 2.8d)) % 6.283185307179586d);
                this.ycos = (float) ((((double) this.ycos) + (((double) delta) * 3.431d)) % 6.283185307179586d);
                GLPlasma.this.shdr.setComplexVariables(((float) Math.cos((double) this.xcos)) * 0.2f, ((float) Math.cos((double) this.ycos)) * 0.2f, ((float) Math.max(GLPlasma.this.height, GLPlasma.this.width)) / ((float) (GLPlasma.this.com.f0h * 3)));
            }
        }
    }

    public GLPlasma(Context context, int width, int height,
                    Complexity comp, ColorStream color, int speed,
                    Symmetry symmetry, boolean touch, boolean complexMovement, int smoothness) {
        this.width = width;
        this.height = height;
        this.colors = color;
        this.com = comp;
        this.gentime = System.currentTimeMillis();
        this.symmetry = symmetry;
        this.touchEnabled = touch;
        this.complexVariablesEnabled = complexMovement;
        this.smoothness = smoothness;
        this.timekeeper = new TimeKeeper((double) (slowrate * ((float) speed)));
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        updateTextures();
        GLException.checkError();
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        GLException.checkError();
        if (System.currentTimeMillis() - this.gentime >= 86400000) {
            generateNewPlasma();
        }
    }

    private FourStateShader getShdr() {
        if (this.shdr == null) {
            this.shdr = new FourStateShader(this.touchEnabled, this.complexVariablesEnabled);
        }
        return this.shdr;
    }

    private ByteTexture currentTexture(int width, int height) {
        if (width > height) {
            return this.landscape;
        }
        return this.portrait;
    }

    private void subcount() {
        double delta = this.timekeeper.delta();
        this.timestep = (this.timestep + delta) % 1.0d;
        AbstractShader s = getShdr().currentShader();
        s.setTimestep((float) this.timestep);
        s.setColor(this.colors.getColor());
        if (this.press && this.touchMultiplier < 50.0f) {
            this.touchMultiplier += 3.0f;
        } else if (!this.press && this.touchMultiplier > 0.0f) {
            this.touchMultiplier = (float) (((double) this.touchMultiplier) * 0.9d);
            if (((double) this.touchMultiplier) <= 0.5d) {
                this.touchMultiplier = 0.0f;
            }
        }
        if (this.complexVariablesEnabled) {
            this.complexVariables.step((float) delta);
        }
    }

    public void setSpeed(int speed) {
        this.timekeeper.ratePerSecond = (double) (((float) speed) * slowrate);
    }

    private void needsUpdating() {
        if (this.portrait != null) {
            this.portrait.needsUpdating = true;
        }
        if (this.landscape != null) {
            this.landscape.needsUpdating = true;
        }
    }

    private void updateIfNecessary() {
        ByteTexture t = currentTexture(this.width, this.height);
        if (t != null && t.needsUpdating) {
            t.setData(newdata(t.width(), t.height(), this.smoothness));
            t.updateGL();
            t.needsUpdating = false;
        }
    }

    public void setComplexity(Complexity com) {
        if (com != this.com) {
            this.com = com;
            needsUpdating();
        }
    }

    public void onDrawFrame(GL10 glUnused) {
        subcount();
        updateIfNecessary();
        if (this.touchMultiplier > 0.0f) {
            getShdr().setTouchLocation(this.touchX, ((float) this.height) - this.touchY, this.touchMultiplier);
        } else {
            getShdr().touchOff();
        }
        getShdr().drawTexture(currentTexture(this.width, this.height));
    }

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        this.width = width;
        this.height = height;
        getShdr().setCenterPixel(((float) width) / 2.0f, ((float) height) / 2.0f);
        GLES20.glViewport(0, 0, width, height);
        GLException.checkError();
        updateTextures();
    }

    private void updateTextures() {
        int w = this.width;
        int h = this.height;
        if (w > h) {
            if (this.landscape == null) {
                this.landscape = new ByteTexture(0, newdata(w, h, this.smoothness), w, h);
                this.landscape.bindGL();
            }
        } else if (this.portrait == null) {
            this.portrait = new ByteTexture(1, newdata(w, h, this.smoothness), w, h);
            this.portrait.bindGL();
        }
    }

    private byte[][] newdata(int width, int height, int smoothness) {
        int w;
        int h;
        if (width > height) {
            w = this.com.f0h;
            h = this.com.f1w;
        } else {
            w = this.com.f1w;
            h = this.com.f0h;
        }
        return PatternGenerator.makeData(width, height, 255, w, h, this.symmetry, smoothness);
    }

    public void generateNewPlasma() {
        needsUpdating();
        this.gentime = System.currentTimeMillis();
    }

    public void setSymmetry(Symmetry symmetry) {
        if (symmetry != this.symmetry) {
            this.symmetry = symmetry;
            needsUpdating();
        }
    }

    public void setTouchCoordinates(float x, float y) {
        this.touchX = x;
        this.touchY = y;
        this.press = true;
    }

    public void touchOff() {
        this.press = false;
    }

    public void setComplexVariablesEnabled(boolean enabled) {
        this.complexVariablesEnabled = enabled;
        if (this.shdr != null) {
            this.shdr.setComplexEnabled(enabled);
        }
    }

    public void setTouchEnabled(boolean enabled) {
        this.touchEnabled = enabled;
        if (this.shdr != null) {
            this.shdr.setTouchEnabled(enabled);
        }
    }

    public void pause() {
        this.colors.pause();
    }

    public void unpause() {
        this.colors.unpause();
    }

    public void finalize() {
        this.colors.pause();
    }

    public void setSmoothness(int smoothness) {
        if (smoothness != this.smoothness) {
            this.smoothness = smoothness;
            needsUpdating();
        }
    }
}
