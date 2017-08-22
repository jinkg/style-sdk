package com.yalin.wallpaper.forest.core;

public class Easing {

    public static class quad {
        public static float out(double val) {
            return (float) ((-val) * (val - 2.0d));
        }
    }

    public static class sine {
        public static float out(double val) {
            return (float) Math.sin((3.141592653589793d * val) * 0.5d);
        }
    }
}
