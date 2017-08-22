package com.yalin.wallpaper.forest.core;

public class World {
    public Colors colors = new Colors();
    public float scale = 1.0f;
    public float skyBottom = 0.25f;

    public SunData getSunData() {
        return this.colors.getSunData();
    }
}
