package com.yalin.wallpaper.boids;

import java.util.Random;

class RandomGenerator {
    private static final Random random = new Random();
   
    public static float randomRange(float min, float max) {
        return min + ((max-min) * random.nextFloat());
    }

    public static float randomPercentile() {
        return randomRange(0, 1);
    }

    public static int randomInt(int min, int max) {
        return min + random.nextInt(max-min);
    }
}
