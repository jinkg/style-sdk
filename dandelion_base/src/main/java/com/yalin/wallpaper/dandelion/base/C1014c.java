package com.yalin.wallpaper.dandelion.base;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class C1014c {
    public final Vector2 f2694c;
    public final Rectangle f2695d;
    public float f2696e;
    public float f2697f;

    public C1014c(float f, float f2, float f3, float f4) {
        this.f2696e = f3;
        this.f2697f = f4;
        this.f2694c = new Vector2(f, f2);
        this.f2695d = new Rectangle(f - (f3 / 2.0f), f2 - (f4 / 2.0f), f3, f4);
    }
}
