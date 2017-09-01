package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public abstract class AbstractShader {
    private static final String vShaderStr = "precision mediump float;     \nattribute vec4 a_position;   \nattribute vec2 a_texCoord;   \nvarying vec2 v_texCoord;     \nvoid main() {                \n   v_texCoord = a_texCoord;  \n   gl_Position = a_position; \n}                            \n";
    final int positionLoc;
    final int programObject;
    final int samplerLoc;
    final int texCoordLoc;

    public abstract void setColor(int i);

    public abstract void setTimestep(float f);

    public AbstractShader(String fragmentShaderCode) {
        GLException.checkError();
        this.programObject = ESShader.loadProgram(vShaderStr, fragmentShaderCode);
        positionLoc = GLES20.glGetAttribLocation(this.programObject, "a_position");
        GLException.checkError();
        GLES20.glUseProgram(this.programObject);
        GLException.checkError();
        GLException.checkError();
        this.texCoordLoc = GLES20.glGetAttribLocation(this.programObject, "a_texCoord");
        GLException.checkError();
        this.samplerLoc = GLES20.glGetUniformLocation(this.programObject, "texture");
        GLException.checkError();
    }

    private void setTexture(int textureVar, Texture t) {
        setVertexInformation(t.getVertices());
        GLES20.glActiveTexture(33984 + t.id);
        GLException.checkError();
        GLES20.glBindTexture(3553, t.getGLBinding());
        GLException.checkError();
        GLES20.glUniform1i(textureVar, t.id);
        GLException.checkError();
    }

    public void drawTexture(ByteTexture texture) {
        GLES20.glUseProgram(this.programObject);
        GLException.checkError();
        GLES20.glClear(16384);
        GLException.checkError();
        setTexture(this.samplerLoc, texture);
        GLES20.glDrawElements(4, 6, 5123, texture.getIndices());
    }

    public String getInfo() {
        return GLES20.glGetProgramInfoLog(this.programObject);
    }

    public void setVertexInformation(FloatBuffer vertices) {
        vertices.position(0);
        GLES20.glVertexAttribPointer(this.positionLoc, 3, 5126, false, 20, vertices);
        GLException.checkError();
        vertices.position(3);
        GLES20.glVertexAttribPointer(this.texCoordLoc, 2, 5126, false, 20, vertices);
        GLException.checkError();
        GLES20.glEnableVertexAttribArray(this.positionLoc);
        GLException.checkError();
        GLES20.glEnableVertexAttribArray(this.texCoordLoc);
        GLException.checkError();
    }

    public void finalize() {
        GLES20.glDeleteProgram(this.programObject);
        GLException.checkError();
    }
}
