package com.kinglloy.wallpaper.clock.ray;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ClockRenderer implements Renderer {
    private float hours;
    private int hoursHandle;
    private final int mBytesPerFloat = 4;
    private FloatBuffer mCubePositions;
    private int mPerVertexProgramHandle;
    private final int mPositionDataSize = 3;
    private int mPositionHandle;
    private float minutes;
    private int minutesHandle;
    private float[] resolution = new float[2];
    private int resolutionHandle;
    private float seconds;
    private int secondsHandle;
    private float time;
    private int timeHandle;

    public ClockRenderer() {
        float[] cubePositionData = new float[]{-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
        this.mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mCubePositions.put(cubePositionData).position(0);
    }

    protected String getVertexShader() {
        String vertexShader = "           //attribute vec3 position;\n\t\t\tattribute vec3 a_Position;\n\t\t\tvarying vec2 surfacePosition;\n\t\t\tvoid main() {\n\t\t\t\tsurfacePosition = a_Position.xy;\n\t\t\t\tgl_Position = vec4(a_Position, 1.0);\n\t\t\t}";
        return "           //attribute vec3 position;\n\t\t\tattribute vec3 a_Position;\n\t\t\tvarying vec2 surfacePosition;\n\t\t\tvoid main() {\n\t\t\t\tsurfacePosition = a_Position.xy;\n\t\t\t\tgl_Position = vec4(a_Position, 1.0);\n\t\t\t}";
    }

    protected String getFragmentShader() {
        String fragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 surfacePosition;uniform float time;uniform float hours;uniform float minutes;uniform float seconds;uniform vec2 resolution;const float PI = 3.1415926;float box(vec2 p){\tfloat h = hours;\tfloat hangle = 2.0*PI*(-h)/12.0;\tvec2 hvec = vec2(sin(hangle), -cos(hangle));\t\tfloat m = minutes;\tfloat mangle = 2.0*PI*(-m)/60.0;\tvec2 mvec = vec2(sin(mangle), -cos(mangle));\t\tfloat s = seconds;\tfloat sangle = 2.0*PI*(-s)/60.0;\tvec2 svec = vec2(sin(sangle), -cos(sangle));\t\tfloat d = (dot(normalize(p), hvec)+1.0) * 1.5 + .15 * length(p);\td = min(d, (dot(normalize(p), mvec)+1.0) * 2.0 + .1 * length(p));\td = min(d, (dot(normalize(p), svec)+1.0) * 5.0 + .05 * length(p));   d = min(d, .9*(length(p)+.01));\treturn d;}void main(void) {   vec2 pos = vec2(2.0 * gl_FragCoord.x/resolution.x - 1.0, 2.0 * gl_FragCoord.y/resolution.y - 1.0);   pos.y *= resolution.y/resolution.x;\tfloat ftime = time + length(pos)*10. + length(pos)*10.*cos(atan(pos.y,pos.x)+time);\tfloat pulse = 0.9+cos(ftime);\tvec3 baseColor = vec3(0.0);\tvec3 light = vec3(0.0, 0.6, 1.0);\tvec3 color = vec3(0);\tfloat b = box(pos);\tfloat dust = .85*smoothstep(0.05, 0.0, b);\t\tfloat block = 0.1*smoothstep(0.001, 0.0, b);\tfloat shine = 1.0*pulse*smoothstep(-0.002, b, 0.037);\tcolor +=  dust + block + shine * light;\tgl_FragColor = vec4(color, 1.0);}";
        return "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 surfacePosition;uniform float time;uniform float hours;uniform float minutes;uniform float seconds;uniform vec2 resolution;const float PI = 3.1415926;float box(vec2 p){\tfloat h = hours;\tfloat hangle = 2.0*PI*(-h)/12.0;\tvec2 hvec = vec2(sin(hangle), -cos(hangle));\t\tfloat m = minutes;\tfloat mangle = 2.0*PI*(-m)/60.0;\tvec2 mvec = vec2(sin(mangle), -cos(mangle));\t\tfloat s = seconds;\tfloat sangle = 2.0*PI*(-s)/60.0;\tvec2 svec = vec2(sin(sangle), -cos(sangle));\t\tfloat d = (dot(normalize(p), hvec)+1.0) * 1.5 + .15 * length(p);\td = min(d, (dot(normalize(p), mvec)+1.0) * 2.0 + .1 * length(p));\td = min(d, (dot(normalize(p), svec)+1.0) * 5.0 + .05 * length(p));   d = min(d, .9*(length(p)+.01));\treturn d;}void main(void) {   vec2 pos = vec2(2.0 * gl_FragCoord.x/resolution.x - 1.0, 2.0 * gl_FragCoord.y/resolution.y - 1.0);   pos.y *= resolution.y/resolution.x;\tfloat ftime = time + length(pos)*10. + length(pos)*10.*cos(atan(pos.y,pos.x)+time);\tfloat pulse = 0.9+cos(ftime);\tvec3 baseColor = vec3(0.0);\tvec3 light = vec3(0.0, 0.6, 1.0);\tvec3 color = vec3(0);\tfloat b = box(pos);\tfloat dust = .85*smoothstep(0.05, 0.0, b);\t\tfloat block = 0.1*smoothstep(0.001, 0.0, b);\tfloat shine = 1.0*pulse*smoothstep(-0.002, b, 0.037);\tcolor +=  dust + block + shine * light;\tgl_FragColor = vec4(color, 1.0);}";
    }

    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
        String vertexShader = getVertexShader();
        String fragmentShader = getFragmentShader();
        this.mPerVertexProgramHandle = createAndLinkProgram(compileShader(35633, vertexShader), compileShader(35632, fragmentShader), new String[]{"a_Position", "a_Color", "a_Normal"});
    }

    public void onDrawFrame(GL10 arg0) {
        GLES20.glClear(16640);
        drawSurface();
    }

    private void drawSurface() {
        GLES20.glUseProgram(this.mPerVertexProgramHandle);
        this.mCubePositions.position(0);
        GLES20.glVertexAttribPointer(this.mPositionHandle, 3, 5126, false, 0, this.mCubePositions);
        GLES20.glEnableVertexAttribArray(this.mPositionHandle);
        this.time = (float) (((double) (SystemClock.uptimeMillis() % 100000)) / 4000.0d);
        this.timeHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "time");
        GLES20.glUniform1f(this.timeHandle, this.time);
        Date date = new Date();
        this.hours = (float) ((((double) date.getHours()) + (((double) date.getMinutes()) / 60.0d)) + (((double) date.getSeconds()) / 36000.0d));
        this.hoursHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "hours");
        GLES20.glUniform1f(this.hoursHandle, this.hours);
        this.minutes = (float) (((double) date.getMinutes()) + (((double) date.getSeconds()) / 60.0d));
        this.minutesHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "minutes");
        GLES20.glUniform1f(this.minutesHandle, this.minutes);
        this.seconds = (float) (((double) date.getSeconds()) + (((double) (date.getTime() % 1000)) / 1000.0d));
        this.secondsHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "seconds");
        GLES20.glUniform1f(this.secondsHandle, this.seconds);
        this.resolutionHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "resolution");
        GLES20.glUniform2f(this.resolutionHandle, this.resolution[0], this.resolution[1]);
        GLES20.glDrawArrays(4, 0, 6);
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.resolution[0] = (float) width;
        this.resolution[1] = (float) height;
    }

    private int compileShader(int shaderType, String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, 35713, compileStatus, 0);
            if (compileStatus[0] == 0) {
                Log.e("raymarching", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }
        if (shaderHandle != 0) {
            return shaderHandle;
        }
        throw new RuntimeException("Error creating shader.");
    }

    private int createAndLinkProgram(int vertexShaderHandle, int fragmentShaderHandle, String[] attributes) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            if (attributes != null) {
                int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }
            GLES20.glLinkProgram(programHandle);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, 35714, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e("", "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle != 0) {
            return programHandle;
        }
        throw new RuntimeException("Error creating program.");
    }
}
