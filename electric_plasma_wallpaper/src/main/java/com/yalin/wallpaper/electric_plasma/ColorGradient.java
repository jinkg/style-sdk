package com.yalin.wallpaper.electric_plasma;

import android.graphics.Color;

public class ColorGradient {
    private int[] colors;

    public ColorGradient(int... colors) {
        this.colors = colors;
    }

    private static int interpolate(int a, int b, double percentage) {
        return (int) Math.round(((double) a) + (((double) (b - a)) * percentage));
    }

    private static int interpolateColor(int a, int b, double percentage) {
        int ar = Color.red(a);
        int ag = Color.green(a);
        int ab = Color.blue(a);
        return Color.rgb(interpolate(ar, Color.red(b), percentage), interpolate(ag, Color.green(b), percentage), interpolate(ab, Color.blue(b), percentage));
    }

    public int sample(double percentage) {
        if (percentage <= 0.0d) {
            return this.colors[0];
        }
        if (percentage >= 1.0d) {
            return this.colors[this.colors.length - 1];
        }
        double ext = percentage * ((double) (this.colors.length - 1));
        int index1 = (int) Math.floor(ext);
        return interpolateColor(this.colors[index1], this.colors[index1 + 1], ext - ((double) index1));
    }

    public static ColorGradient makeLoopingGradient(int... colors) {
        int[] newcolors = new int[(colors.length + 1)];
        for (int i = 0; i < colors.length; i++) {
            newcolors[i] = colors[i];
        }
        newcolors[colors.length] = colors[0];
        return new ColorGradient(newcolors);
    }
}
