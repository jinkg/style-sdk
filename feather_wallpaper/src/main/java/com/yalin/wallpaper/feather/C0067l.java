package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.graphics.g2d.SpriteBatch;
import com.badlogic1.gdx.graphics.g2d.TextureRegion;
import com.badlogic1.gdx.math.Vector2;

public class C0067l extends C0027a {
    public final Vector2 f186g;

    public C0067l(float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4);
        this.f186g = new Vector2(f, f2);
    }

    public void m187a() {
        f124c.x += (this.f186g.x - f124c.x) * 0.1f;
    }

    public void m188a(SpriteBatch c0321a, TextureRegion c0322b) {
        c0321a.draw(c0322b, f124c.x, f124c.y, f125d.width, f125d.height);
    }
}
