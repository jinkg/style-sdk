package com.yalin.wallpaper.boids;

import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL10;

public class Texture {
    private static final String TAG = "Texture";

    private final String name;
    private boolean loaded = false;

    public int id = -1;

    private final Bitmap bitmap;

    public Texture(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public void load(GL10 gl) {
        id = GLHelper.loadTexture(gl, bitmap);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                     GL10.GL_MODULATE);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, 
                           GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                           GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                           GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                           GL10.GL_REPEAT);

        //bitmap.recycle();
        //bitmap = null;

        loaded = true;
    }

    public void enable(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);        
    }
}