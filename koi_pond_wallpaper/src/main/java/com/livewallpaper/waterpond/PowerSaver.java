package com.livewallpaper.waterpond;

import com.livewallpaper.waterpond.PreferencesManager.OnPreferenceChangedListener;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

public class PowerSaver implements OnPreferenceChangedListener {
    private static int[] f130x75947dbf;
    private long deltaNanoTime;
    private long nanoTime;
    private long nanoTimeOneFrame;
    private long savedNanoTime;

    static int[] m25x75947dbf() {
        int[] iArr = f130x75947dbf;
        if (iArr == null) {
            iArr = new int[PreferenceChangedType.values().length];
            try {
                iArr[PreferenceChangedType.CURRENTTHEME.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGENABLE.ordinal()] = 10;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGLOADED.ordinal()] = 11;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PreferenceChangedType.FEEDKOI.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PreferenceChangedType.GYROENABLE.ordinal()] = 9;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[PreferenceChangedType.KOISET.ordinal()] = 12;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[PreferenceChangedType.PAGEPAN.ordinal()] = 13;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[PreferenceChangedType.POWERSAVER.ordinal()] = 7;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[PreferenceChangedType.RAINYMODE.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[PreferenceChangedType.SHOWFLOATAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[PreferenceChangedType.SHOWREFLECTION.ordinal()] = 1;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[PreferenceChangedType.SHOWSCHOOL.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[PreferenceChangedType.TOUCHPAN.ordinal()] = 6;
            } catch (NoSuchFieldError e13) {
            }
            f130x75947dbf = iArr;
        }
        return iArr;
    }

    public PowerSaver() {
        PreferencesManager.getInstance().addPreferenceChangedListener(this);
        updateNanoTimeOneFrame();
        reset();
    }

    public void reset() {
        this.nanoTime = System.nanoTime();
        this.savedNanoTime = this.nanoTime;
    }

    private void updateNanoTimeOneFrame() {
        this.nanoTimeOneFrame = (long) (1.0E9d / ((double) PreferencesManager.getInstance().fps));
    }

    public void sleep() {
        this.nanoTime = System.nanoTime();
        this.deltaNanoTime = this.nanoTime - this.savedNanoTime;
        long sleepedNonoTime = this.nanoTimeOneFrame - this.deltaNanoTime;
        if (sleepedNonoTime > 0) {
            try {
                Thread.sleep(sleepedNonoTime / 1000000);
            } catch (InterruptedException e) {
            }
            this.nanoTime = System.nanoTime();
            this.deltaNanoTime = this.nanoTime - this.savedNanoTime;
        }
        this.savedNanoTime += this.deltaNanoTime;
    }

    public void onPreferenceChanged(PreferenceChangedType type) {
        switch (m25x75947dbf()[type.ordinal()]) {
            case 7:
                updateNanoTimeOneFrame();
                return;
            default:
                return;
        }
    }

    public void dispose() {
        PreferencesManager.getInstance().removePreferenceChangedListener(this);
    }
}
