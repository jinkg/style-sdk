package com.yalin.wallpaper.electric_plasma;

public interface ColorStream {
    int getColor();

    boolean isChanged();

    void pause();

    void unpause();
}
