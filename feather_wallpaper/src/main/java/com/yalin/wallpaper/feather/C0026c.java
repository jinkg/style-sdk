package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.math.Rectangle;
import com.badlogic1.gdx.math.Vector2;

public class C0026c {
    public final Vector2 f124c;
    public final Rectangle f125d;
    public float f126e;
    public float f127f;

    public C0026c(float f, float f2, float f3, float f4) {
        this.f126e = f3;
        this.f127f = f4;
        this.f124c = new Vector2(f, f2);
        this.f125d = new Rectangle(f - (f3 / 2.0f), f2 - (f4 / 2.0f), f3, f4);
    }
}
