package com.yalin.wallpaper.hacker;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;

import java.util.ArrayList;
import java.util.List;

public class HackerWallpaperService extends WallpaperServiceProxy {

    private static boolean reset = false;
    private static boolean previewReset = false;

    private int r, g, b;

    public HackerWallpaperService(Context host) {
        super(host);
    }

    public static void reset() {
        previewReset = true;
        reset = true;
    }

    @Override
    public Engine onCreateEngine() {
        return new HackerWallpaperEngine();
    }

    public class HackerWallpaperEngine extends ActiveEngine {

        private Handler handler = new Handler();
        private boolean visible = true;

        /**
         * The sequences to draw on the screen
         */
        private List<BitSequence> sequences = new ArrayList<>();

        private int width;

        /**
         * The main runnable that is given to the Handler to draw the animation
         */
        private Runnable drawRunnable = new Runnable() {
            public void run() {
                draw();
            }
        };

        /**
         * Draws all of the bit sequences on the screen
         */
        private void draw() {
            if (visible) {
                // We can't have just one reset flag, because then the preview
                // would consume that flag and the actual wallpaper wouldn't be
                // reset
                if (previewReset && isPreview()) {
                    previewReset = false;
                    resetSequences();
                } else if (reset && !isPreview()) {
                    reset = false;
                    resetSequences();
                }
                SurfaceHolder holder = getSurfaceHolder();
                Canvas c = holder.lockCanvas();
                try {
                    if (c != null) {
                        c.drawARGB(255, r, g, b);

                        for (int i = 0; i < sequences.size(); i++) {
                            sequences.get(i).draw(c);
                        }
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }

                // Remove the runnable, and only schedule the next run if
                // visible
                handler.removeCallbacks(drawRunnable);

                handler.post(drawRunnable);
            } else {
                pause();
            }
        }

        // TODO: Not all of the sequences need to be cleared
        private void resetSequences() {
            int color = 0;
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = (color >> 0) & 0xFF;
            stop();
            sequences.clear();
            int numSequences = (int) (1.5 * width / BitSequence.getWidth());
            for (int i = 0; i < numSequences; i++) {
                sequences.add(new BitSequence(
                        (int) (i * BitSequence.getWidth() / 1.5)));
            }
            start();
        }

        private void pause() {
            handler.removeCallbacks(drawRunnable);
            for (int i = 0; i < sequences.size(); i++) {
                sequences.get(i).pause();
            }
        }

        private void start() {
            handler.post(drawRunnable);
            for (int i = 0; i < sequences.size(); i++) {
                sequences.get(i).unpause();
            }
        }

        private void stop() {
            handler.removeCallbacks(drawRunnable);
            for (int i = 0; i < sequences.size(); i++) {
                sequences.get(i).stop();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            BitSequence.configure(getApplicationContext());
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            pause();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.width = width;

            BitSequence.setScreenDim(width, height);

            // Initialize BitSequences
            resetSequences();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                start();
            } else {
                pause();
            }
            this.visible = visible;
        }
    }
}
