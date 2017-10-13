package com.yalin.wallpaper.dandelion.base;

import com.badlogic.gdx.audio.Sound;

public class DandelionSound {
    public static Sound f2996a;

    public static void m5408a(int i, C1066b c1066b) {
        try {
            if (f2996a != null) {
                f2996a.stop();
            }
            f2996a = c1066b.m5288a(i);
        } catch (Exception e) {
        }
    }
}
