package com.yalin.wallpaper.nightfal_windmill;

import android.content.res.Resources;
import android.util.Log;

public class MyPreferences {
    public static MyPreferences f370I;
    public boolean doubleTapOpensSettings = false;
    public String foregroundScene = "foreground.png";
    public String moon = "moon.png";
    public int numberOfStars = 50;
    public float rotationSpeed = 2.0f;
    LiveWallpaperScreen screen;
    public boolean showClouds = false;
    public boolean showMoon = true;
    public boolean showTwinklingStars = true;
    public boolean updatedForeground = false;
    public boolean updatedMoon = false;
    public boolean updatedNumberOfStars = false;
    public boolean updatedSettings = false;

    public MyPreferences(LiveWallpaperScreen s) {
        f370I = this;
        this.screen = s;
        s.prefs = this;
        SetPreferences();
    }

    public void SetPreferences() {
        this.updatedNumberOfStars = true;
        this.numberOfStars = 50;
        this.moon = "moon.png";
        this.updatedMoon = true;
        this.doubleTapOpensSettings = false;
        this.showMoon = true;
        this.showTwinklingStars = true;
        this.rotationSpeed = (((float) 20) / 50.0f) * 5.0f;
        this.showClouds = false;
        this.foregroundScene = "foreground_windmill.png";
        this.updatedForeground = true;
        this.updatedSettings = true;
    }
}
