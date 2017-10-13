package com.yalin.wallpaper.dandelion.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class C1089j extends C1015a {
    public float f2979g = 8.0f;
    public float f2980h = 0.0f;
    float f2981i = 0.1f;

    public C1089j(float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4);
    }

    public void m5397a(float f) {
        if (this.f2980h >= this.f2979g) {
            this.f2980h = 0.0f;
        } else {
            this.f2980h += f;
        }
    }

    public void m5398a(SpriteBatch c1208c, TextureRegion c1209d) {
        c1208c.setColor(1.0f, 1.0f, 1.0f, this.f2981i);
        c1208c.draw(c1209d, this.f2694c.x, this.f2694c.y, this.f2695d.width, this.f2695d.height);
    }
}
