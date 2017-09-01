package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLES20;
import android.util.Log;

public class ESShader {
    public static int loadShader(int type, String shaderSrc) {
        int[] compiled = new int[1];
        int shader = GLES20.glCreateShader(type);
        GLException.checkError();
        if (shader == 0) {
            Log.d("ESShader", "No shader created.");
            return 0;
        }
        GLES20.glShaderSource(shader, shaderSrc);
        GLException.checkError();
        GLES20.glCompileShader(shader);
        GLException.checkError();
        GLES20.glGetShaderiv(shader, 35713, compiled, 0);
        GLException.checkError();
        if (compiled[0] == 0) {
            Log.e("ESShader", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
        Log.d("ESShader", "Shader compiled successfully.");
        return shader;
    }

    public static int loadProgram(String vertShaderSrc, String fragShaderSrc) {
        int[] linked = new int[1];
        int vertexShader = loadShader(35633, vertShaderSrc);
        if (vertexShader == 0) {
            return 0;
        }
        int fragmentShader = loadShader(35632, fragShaderSrc);
        if (fragmentShader == 0) {
            GLES20.glDeleteShader(vertexShader);
            return 0;
        }
        int programObject = GLES20.glCreateProgram();
        GLException.checkError();
        if (programObject == 0) {
            return 0;
        }
        GLES20.glAttachShader(programObject, vertexShader);
        GLException.checkError();
        GLES20.glAttachShader(programObject, fragmentShader);
        GLException.checkError();
        GLES20.glLinkProgram(programObject);
        GLException.checkError();
        GLES20.glGetProgramiv(programObject, 35714, linked, 0);
        GLException.checkError();
        if (linked[0] == 0) {
            Log.e("ESShader", "Error linking program:");
            Log.e("ESShader", GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return 0;
        }
        GLES20.glDeleteShader(vertexShader);
        GLException.checkError();
        GLES20.glDeleteShader(fragmentShader);
        GLException.checkError();
        Log.d("ESShader", "Vertex shader info: " + GLES20.glGetShaderInfoLog(vertexShader));
        Log.d("ESShader", "Fragment shader info: " + GLES20.glGetShaderInfoLog(fragmentShader));
        Log.d("ESShader", "Program info: " + GLES20.glGetProgramInfoLog(programObject));
        return programObject;
    }
}
