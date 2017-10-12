package com.yalin.wallpaper.wavez.base;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class C0060c {
    public final Vector2 f137c;
    public final Rectangle f138d;
    public float f139e;
    public float f140f;

    public C0060c(float f, float f2, float f3, float f4) {
        this.f139e = f3;
        this.f140f = f4;
        this.f137c = new Vector2(f, f2);
        this.f138d = new Rectangle(f - (f3 / 2.0f), f2 - (f4 / 2.0f), f3, f4);
    }
}
