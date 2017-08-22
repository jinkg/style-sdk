package com.yalin.wallpaper.sneakycat;


public class MyPreferences {
    public static MyPreferences f368I;
    float appearLengthMax = 8.0f;
    float appearLengthMin = 4.0f;
    public String backgroundColor = "3A66B2FF";
    public boolean doubleTapHide = false;
    public String eyeColor = "5ea9feFF";
    public boolean hidesOnTouch = false;
    float smugTime = 3.0f;

    public MyPreferences() {
        f368I = this;
        SetPreferences();
    }

    public void SetPreferences() {
        this.hidesOnTouch = false;
        this.doubleTapHide = true;

        this.appearLengthMin = 4.0f;
        this.appearLengthMax = 8.0f;
        this.smugTime = 3.0f;
    }
}
