package com.yalin.wallpaper.boids;

import android.opengl.GLSurfaceView;
import android.util.Log;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BoidsRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "BoidsRenderer";

    // Used for Lighting
    private static final float[] ambientComponent0 = {1.0f, 1.0f, 1.0f, 1.0f};
    private static final float[] diffuseComponent0 = {1.0f, 1.0f, 1.0f, 1.0f};
    private static final float[] lightPosition0 = {0, 200f, 200f, 0f};

    private final Camera camera;
    private final FlockBuffer buffer;

    private long frames;
    private long last;
    private long total_elapsed;

    private int width;
    private int height;
    private final MatrixGrabber matrix_grabber = new MatrixGrabber();

    public BoidsRenderer(FlockBuffer buffer) {
        super();
        this.camera = new Camera();
        this.buffer = buffer;
    }

    final public float[] getCurrentProjection() {
        return matrix_grabber.mProjection;
    }

    final public float[] getCurrentModelView() {
        return matrix_grabber.mModelView;
    }

    final public int getWidth() {
        return width;
    }

    final public int getHeight() {
        return height;
    }

    final public void onDrawFrame(GL10 gl) {
        long now = System.currentTimeMillis();
        long elapsed = now - last;
        total_elapsed += elapsed;

        // FIXME make this is binary search
        frames++;
        if (total_elapsed > 5000) {
            frames /= 5;
            if (BuildConfig.DEBUG) Log.d(TAG, "rendered fps: " + frames);
            total_elapsed = 0;
            frames = 0;
        }

        gl.glClearColor(0f, 0f, 0f, .001f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //camera.tick(elapsed);
        //camera.update(gl);

        matrix_grabber.getCurrentState(gl);

        buffer.draw(gl);

        last = now;

        int slice = 16;
        if (elapsed < slice) {
            try {
                Thread.sleep(slice - elapsed);
            } catch (InterruptedException e) {
            }
        }
    }

    final public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;

        //setupLightSources(gl);

        camera.init(gl, width, height);
        buffer.init(gl);

        GLHelper.init(gl);

        last = System.currentTimeMillis();
    }

    final public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glClearDepthf(1f);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glEnable(GL10.GL_POINT_SMOOTH);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_DITHER);
        //gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_ALPHA_TEST);
        //gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);

        gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
        gl.glAlphaFunc(GL10.GL_GREATER, 0.01f);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_REPLACE);

        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glFrontFace(GL10.GL_CCW);

        gl.glCullFace(GL10.GL_BACK);
    }

    private void setupLightSources(GL10 gl) {
        //Enable Light source 0
        gl.glEnable(GL10.GL_LIGHT0);

        //Useful part of the Arrays start a 0
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientComponent0, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseComponent0, 0);

        //Position the light in the scene
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition0, 0);
    }

    final public void release() {
    }
}
