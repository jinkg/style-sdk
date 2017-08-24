package com.yalin.wallpaper.lazypanda;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyPreferences {
    public static MyPreferences f82I;
    public TextureRegion background2;
    public TextureRegion bg;
    public boolean doubleTapOpensSettings = false;
    public int numberOfPetals = 5;
    public boolean reactsToTouch = false;

    public MyPreferences(TextureRegion bg2) {
        f82I = this;
        this.background2 = bg2;
        SetPreferences();
    }

    public void SetPreferences() {
        this.doubleTapOpensSettings = false;
        this.reactsToTouch = true;
        this.numberOfPetals = 14;
        this.bg = this.background2;
    }
}
