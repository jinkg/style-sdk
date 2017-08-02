package com.yalin.wallpaper.drippler;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StepsHelper {

    private final OnStepsCountFetchedListener listener;
    private final ExecutorService ex;

    public StepsHelper(OnStepsCountFetchedListener listener) {
        this.listener = listener;
        // Single thread pool with double of STEPS_CHECK_INTERVAL time of idle thread
        this.ex = new ThreadPoolExecutor(1, 1, DripWallpaperService.STEPS_CHECK_INTERVAL * 2, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void fetchStepsCount() {
        ex.execute(new Runnable() {
            @Override
            public void run() {
                // Find steps from Fitness API

                Calendar cal = Calendar.getInstance();
                Calendar dayStart = new GregorianCalendar(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                Log.d("test", dayStart.getTimeInMillis() + "");
                long dayPass = cal.getTimeInMillis() - dayStart.getTimeInMillis();
                int count = (int) (dayPass / DripWallpaperService.STEPS_CHECK_INTERVAL);

                listener.onStepsCountFetched(count);
            }
        });
    }

    public interface OnStepsCountFetchedListener {
        void onStepsCountFetched(int count);
    }
}

