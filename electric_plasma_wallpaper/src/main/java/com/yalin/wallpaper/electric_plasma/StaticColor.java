package com.yalin.wallpaper.electric_plasma;

public class StaticColor implements ColorStream {
    private final int color;
    private boolean isChanged = true;

    public StaticColor(int c) {
        this.color = c;
    }

    public int getColor() {
        this.isChanged = false;
        return this.color;
    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public void pause() {
    }

    public void unpause() {
    }
}
