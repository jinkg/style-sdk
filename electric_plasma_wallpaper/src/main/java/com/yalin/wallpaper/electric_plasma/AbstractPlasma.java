package com.yalin.wallpaper.electric_plasma;

public abstract class AbstractPlasma {
    public ColorStream colors;
    protected Complexity com = Complexity.HIGH;

    public void setColorPalette(ColorStream c) {
        if (this.colors != null) {
            this.colors.pause();
        }
        this.colors = c;
    }

    public void setComplexity(Complexity c) {
        this.com = c;
    }
}
