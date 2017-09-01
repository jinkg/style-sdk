package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLES20;

public class GLException extends RuntimeException {
    static final long serialVersionUID = 1;
    final int errorCode;

    public GLException(int errorCode) {
        super("GL Error: " + errorCode);
        this.errorCode = errorCode;
    }

    public static void checkError() {
        int error = GLES20.glGetError();
        if (error != 0) {
            throw new GLException(error);
        }
    }
}
