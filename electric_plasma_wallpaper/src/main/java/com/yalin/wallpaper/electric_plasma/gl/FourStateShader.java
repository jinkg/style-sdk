package com.yalin.wallpaper.electric_plasma.gl;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

public class FourStateShader {
    private static Feature complex = new C00332();
    private static Feature touch = new C00321();
    private FeatureShader both;
    private boolean complexEnabled;
    private ComplexHandler complexhandler;
    private FeatureShader current;
    private boolean needsUpdating = false;
    private FeatureShader onlyComplex;
    private FeatureShader onlyTouch;
    private FeatureShader simple;
    private boolean touchEnabled;
    private TouchHandler touchhandler;

    private static class ComplexHandler {
        final int centerPixelLoc;
        final int waveAmplitudesLoc;
        final int wavestretchLoc;

        private ComplexHandler(FeatureShader shdr) {
            GLES20.glUseProgram(shdr.programObject);
            this.waveAmplitudesLoc = GLES20.glGetUniformLocation(shdr.programObject, "waveAmplitudes");
            GLException.checkError();
            this.wavestretchLoc = GLES20.glGetUniformLocation(shdr.programObject, "wavestretch");
            GLException.checkError();
            this.centerPixelLoc = GLES20.glGetUniformLocation(shdr.programObject, "centerPixel");
            GLException.checkError();
        }

        public static ComplexHandler get(FeatureShader shdr) {
            if (shdr.hasFeature(FourStateShader.complex)) {
                return new ComplexHandler(shdr);
            }
            return null;
        }
    }

    private static class Feature {
        String function;
        String functionName;
        String uniforms;

        private Feature() {
        }
    }

    private static class TouchHandler {
        final int touchLocationLoc;
        final int touchMultiplierLoc;

        private TouchHandler(FeatureShader shdr) {
            GLES20.glUseProgram(shdr.programObject);
            this.touchLocationLoc = GLES20.glGetUniformLocation(shdr.programObject, "touchLocation");
            GLException.checkError();
            this.touchMultiplierLoc = GLES20.glGetUniformLocation(shdr.programObject, "touchMultiplier");
            GLException.checkError();
        }

        public static TouchHandler get(FeatureShader shdr) {
            if (shdr.hasFeature(FourStateShader.touch)) {
                return new TouchHandler(shdr);
            }
            return null;
        }
    }

    static class C00321 extends Feature {
        C00321() {
            super();
            this.function = "float touchwarp(){    vec2 diff = (gl_FragCoord.xy - touchLocation) / 16.0;    float dist = (diff.x * diff.x) + (diff.y * diff.y);    return max(-0.005 + 1.0 / (20.0 + dist), 0.0) * touchMultiplier;}";
            this.functionName = "touchwarp()";
            this.uniforms = "uniform vec2 touchLocation;uniform float touchMultiplier;";
        }
    }

    static class C00332 extends Feature {
        C00332() {
            super();
            this.uniforms = "uniform vec2 waveAmplitudes;uniform float wavestretch;uniform vec2 centerPixel;";
            this.function = "float complexAdd() {    vec2 fromCenter = (gl_FragCoord.xy - centerPixel) / wavestretch;    float xcos = waveAmplitudes.x * cos(fromCenter.x);    float ycos = waveAmplitudes.y * cos(fromCenter.y);    return xcos + ycos + 2.0;}";
            this.functionName = "complexAdd()";
        }
    }

    static class FeatureShader extends AbstractShader {
        final int colorLoc = GLES20.glGetUniformLocation(this.programObject, "color");
        final Feature[] features;
        final int timestepLoc;

        public FeatureShader(Feature... features) {
            super(FourStateShader.genCode(features));
            this.features = features;
            GLException.checkError();
            this.timestepLoc = GLES20.glGetUniformLocation(this.programObject, "timestep");
            GLException.checkError();
        }

        public boolean hasFeature(Feature f) {
            for (Feature a : this.features) {
                if (a == f) {
                    return true;
                }
            }
            return false;
        }

        public void setColor(int color) {
            GLES20.glUniform3f(this.colorLoc, ((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f);
            GLException.checkError();
        }

        public void setTimestep(float timestep) {
            if (timestep < 0.0f) {
                timestep = 0.0f;
            }
            GLES20.glUniform1f(this.timestepLoc, timestep);
            GLException.checkError();
        }
    }

    private static String genCode(Feature... features) {
        int length;
        String shiftaddline;
        int i = 0;
        String code = "precision mediump float;varying vec2 v_texCoord;uniform sampler2D texture;uniform vec3 color;uniform float timestep;";
        for (Feature f : features) {
            code = new StringBuilder(String.valueOf(code)).append(f.uniforms).toString();
        }
        for (Feature f2 : features) {
            code = new StringBuilder(String.valueOf(code)).append(f2.function).toString();
        }
        if (features.length == 0) {
            shiftaddline = "";
        } else {
            shiftaddline = "shift = shift";
            length = features.length;
            while (i < length) {
                shiftaddline = new StringBuilder(String.valueOf(shiftaddline)).append(" + ").append(features[i].functionName).toString();
                i++;
            }
            shiftaddline = new StringBuilder(String.valueOf(shiftaddline)).append(";").toString();
        }
        return new StringBuilder(String.valueOf(code)).append("void main() {  float tex = (texture2D( texture, v_texCoord ).g);  float shift = (tex + timestep);").append(shiftaddline).append("  shift = fract(shift);").append("  float mid = 0.5 - shift;").append("  if (mid < 0.0) mid = -mid;").append("  float lum = 0.08 / ( 0.004 + mid );").append("  gl_FragColor.rgb = (color * lum);").append("}").toString();
    }

    private FeatureShader getSimple() {
        if (this.simple == null) {
            this.simple = new FeatureShader(new Feature[0]);
        }
        Log.d("plasma", "Simple shader");
        return this.simple;
    }

    private FeatureShader getOnlyTouch() {
        if (this.onlyTouch == null) {
            this.onlyTouch = new FeatureShader(touch);
        }
        Log.d("plasma", "Only touch");
        return this.onlyTouch;
    }

    private FeatureShader getOnlyComplex() {
        if (this.onlyComplex == null) {
            this.onlyComplex = new FeatureShader(complex);
        }
        Log.d("plasma", "Only complex");
        return this.onlyComplex;
    }

    private FeatureShader getBoth() {
        if (this.both == null) {
            this.both = new FeatureShader(touch, complex);
        }
        Log.d("plasma", "Both");
        return this.both;
    }

    private void updateHandlers(FeatureShader s) {
        this.touchhandler = TouchHandler.get(s);
        this.complexhandler = ComplexHandler.get(s);
    }

    public AbstractShader currentShader() {
        if (this.current == null || this.needsUpdating) {
            updateShader();
        }
        return this.current;
    }

    private void updateShader() {
        Log.d("plasma", "Updating shader: " + this.touchEnabled + ", " + this.complexEnabled);
        if (this.touchEnabled) {
            if (this.complexEnabled) {
                this.current = getBoth();
            } else {
                this.current = getOnlyTouch();
            }
        } else if (this.complexEnabled) {
            this.current = getOnlyComplex();
        } else {
            this.current = getSimple();
        }
        GLES20.glUseProgram(this.current.programObject);
        if (this.needsUpdating) {
            updateHandlers(this.current);
            this.needsUpdating = false;
        }
    }

    public void setTouchEnabled(boolean enabled) {
        if (this.touchEnabled != enabled) {
            this.touchEnabled = enabled;
            this.needsUpdating = true;
        }
    }

    public void setComplexEnabled(boolean enabled) {
        if (this.complexEnabled != enabled) {
            this.complexEnabled = enabled;
            this.needsUpdating = true;
        }
    }

    public FourStateShader(boolean touchEnabled, boolean complexEnabled) {
        Log.d("plasma", "Four-state shader: " + touchEnabled + ", " + complexEnabled);
        this.touchEnabled = touchEnabled;
        this.complexEnabled = complexEnabled;
        this.needsUpdating = true;
    }

    public void setTouchLocation(float x, float y, float multiplier) {
        if (this.touchhandler != null) {
            GLES20.glUniform2f(this.touchhandler.touchLocationLoc, x, y);
            GLException.checkError();
            GLES20.glUniform1f(this.touchhandler.touchMultiplierLoc, multiplier);
            GLException.checkError();
        }
    }

    public void touchOff() {
        if (this.touchhandler != null) {
            GLES20.glUniform1f(this.touchhandler.touchMultiplierLoc, 0.0f);
            GLException.checkError();
        }
    }

    public void setCenterPixel(float x, float y) {
        if (this.complexhandler != null) {
            GLES20.glUniform2f(this.complexhandler.centerPixelLoc, x, y);
            GLException.checkError();
        }
    }

    public void setComplexVariables(float xwave, float ywave, float wavestretch) {
        if (this.complexhandler != null) {
            GLES20.glUniform2f(this.complexhandler.waveAmplitudesLoc, xwave, ywave);
            GLException.checkError();
            GLES20.glUniform1f(this.complexhandler.wavestretchLoc, wavestretch);
        }
    }

    public void drawTexture(ByteTexture t) {
        this.current.drawTexture(t);
    }
}
