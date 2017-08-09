package com.yalin.wallpaper.boids;

import android.content.Context;
import android.opengl.GLSurfaceView.GLWrapper;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;

import com.yalin.style.engine.GLWallpaperServiceProxy;

import javax.microedition.khronos.opengles.GL;

// Stolen from:
// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers

public class BoidsWallpaperService extends GLWallpaperServiceProxy {

    private static final String TAG = "BoidsWallpaperService";

    private FlockThread simulation_thread;
    private Flock flock;
    private FlockBuffer buffer;
    private Profile profile;

    private BoidsEngine mEngine;

    public BoidsWallpaperService(Context host) {
        super(host);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: 1");
        TextureLoader.init(this);

        Log.d(TAG, "onCreate: 2");
        ProfileLoader.init(BoidsWallpaperService.this);
        Log.d(TAG, "onCreate: 3");
        profile = ProfileLoader.getProfile("Default");
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "loaded profile: " + profile.name);
            Log.d(TAG, profile.toString(3));
            Log.d(TAG, "------------------------------------");
        }

        Log.d(TAG, "onCreate: 4");
        flock = new Flock();
        flock.init(profile);

        buffer = new FlockBuffer(flock);

        simulation_thread = new FlockThread(flock, buffer);

        simulation_thread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public WallpaperService.Engine onCreateEngine() {
        mEngine = new BoidsEngine();
        return mEngine;
    }

    class BoidsEngine extends GLActiveEngine {

        private static final String TAG = "BoidsEngine";

        BoidsRenderer renderer;
        final Vector3 last_touch = new Vector3();
        final Vector3 last_push = new Vector3();

        public BoidsEngine() {
            super();

            setGLWrapper(new GLWrapper() {
                public GL wrap(GL gl) {
                    return new MatrixTrackingGL(gl);
                }
            });

            renderer = new BoidsRenderer(buffer);
            setRenderer(renderer);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
            setTouchEventsEnabled(true);
        }

        public void onDestroy() {
            super.onDestroy();

            if (renderer != null)
                renderer.release();

            renderer = null;
        }

        @Override
        public void onPause() {
            super.onPause();
            simulation_thread.pauseSimulation();
        }

        @Override
        public void onResume() {
            super.onResume();
            simulation_thread.resumeSimulation();
        }

        public void onOffsetsChanged(float xOffset, float yOffset,
                                     float xOffsetStep, float yOffsetStep,
                                     int xPixelOffset, int yPixelOffset) {
            Vector3 v = new Vector3(xPixelOffset, yPixelOffset, 0);
            v.subtract(last_push);
            v.normalize();
            if (v.magnitude() > 0) {
                if (BuildConfig.DEBUG) Log.d(TAG, "pushing " + v);
                flock.push(v);
            }

            last_push.x = xPixelOffset;
            last_push.y = yPixelOffset;
        }

        public Bundle onCommand(String action, int x, int y, int z,
                                Bundle extras, boolean resultRequested) {
            if (BuildConfig.DEBUG) Log.d(TAG, "command: " + action);
            return null;
        }

        // FIXME queue event; copy event data into finals
        public void onTouchEvent(MotionEvent event) {
            Vector3 v = GLHelper.projectTouchToWorld(renderer.getWidth(), renderer.getHeight(),
                    renderer.getCurrentModelView(), renderer.getCurrentProjection(), event.getX(), event.getY());

            if (BuildConfig.DEBUG) Log.d(TAG, "touch: " + v);

            flock.touch(v);
        }
    }
}
