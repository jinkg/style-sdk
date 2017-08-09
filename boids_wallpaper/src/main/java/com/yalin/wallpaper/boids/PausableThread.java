
package com.yalin.wallpaper.boids;

class PausableThread extends Thread {
    private boolean running, paused;
    private static final int FPS = 60;

    public PausableThread() {
        super();
        pauseRunning();
    }

    @Override
    public void start() {
        super.start();
        startRunning();
    }

    public synchronized void startRunning() {
        running = true;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void stopRunning() {
        running = false;
    }

    public synchronized void pauseRunning() {
        paused = true;
        notifyAll();
    }

    public synchronized void resumeRunning() {
        paused = false;
        notifyAll();
    }

    public synchronized void waitForResume() {
        while(paused) {
            try {
                wait();
            } catch(InterruptedException e) {
            }
        }
    }

    public void throttle(int timeslice, long elapsed) {
        if(elapsed < timeslice) {
            try {
                Thread.sleep(timeslice - elapsed);
            } catch(InterruptedException e) {
            }
        }
    }

    public void update(long elapsed) {
    }

    @Override
    public void run() {

        int frames = 0;
        long last = System.currentTimeMillis();
        while (isRunning()) {
            frames++;
            waitForResume();

            long now = System.currentTimeMillis();
            long elapsed = now - last;

            update(elapsed);

            last = now;

            throttle(1000/FPS, elapsed);
        }
    }
}