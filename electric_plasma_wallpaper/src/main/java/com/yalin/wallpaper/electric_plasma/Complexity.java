package com.yalin.wallpaper.electric_plasma;

public enum Complexity {
    LOW(6, 4),
    MEDIUM(8, 6),
    HIGH(10, 7);

    public final int f0h;
    public final int f1w;

    private Complexity(int h, int w) {
        this.f1w = w;
        this.f0h = h;
    }

    public static Complexity getFromPreferences() {
        int complex = 1;
        switch (complex) {
            case 0:
                return LOW;
            case 1:
                return MEDIUM;
            case 2:
                return HIGH;
            default:
                return MEDIUM;
        }
    }
}
