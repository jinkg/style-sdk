package com.yalin.wallpaper.boids;

import android.util.Log;

class FlockThread extends PausableThread {
    private static final String TAG = "FlockThread";

    private Flock flock;
    private FlockBuffer buffer;
    private long frames;
    private long total_elapsed;

    public FlockThread(Flock flock, FlockBuffer buffer) {
        super();
        this.flock = flock;
        this.buffer = buffer;
    }

    final public void setFlock(Flock flock) {
        this.flock = flock;
    }

    final public void setBuffer(FlockBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    final public void update(long elapsed) {
        frames++;

        flock.tick(elapsed);
        buffer.add(flock);
        buffer.swap();

        total_elapsed += elapsed;

        if(total_elapsed > 5000) {
            frames /= 5;
            if (BuildConfig.DEBUG)Log.d(TAG, "ticked " + flock.getAlive()+ " boids at fps: " + frames);

            if(frames < 54)
                flock.throttleDown();
            else if(frames >= 66)
                flock.throttleUp();

            total_elapsed = 0;
            frames = 0;
        }

    }

    final public synchronized void pauseSimulation() {
        pauseRunning();
    }

    final public synchronized void resumeSimulation() {
        resumeRunning();
    }
}
