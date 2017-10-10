package com.yalin.wallpaper.galaxy.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class C0429a {
    public Texture f972c;
    public Texture f973d;
    public TextureRegion f974e;
    public TextureRegion f975f;
    public TextureRegion f976g;
    public TextureRegion f977h;
    public TextureRegion f978i;

    public void m1715a() {
        this.f972c = new Texture(new C0358a(Gdx.files.internal("textures/item.png"), null, null, false, 4448));
        if (!String.valueOf(Gdx.files.internal("textures/item.png").length()).equals("312259")) {
            this.f972c = new Texture(1, 1, Format.RGB565);
        }
        this.f975f = new TextureRegion(this.f972c, 0, 512, 64, 64);
        this.f976g = new TextureRegion(this.f972c, 12, 11, 483, 489);
        this.f977h = new TextureRegion(this.f972c, 514, 2, 470, 470);
        this.f978i = new TextureRegion(this.f972c, 68, 583, 367, 367);
    }

    public void m1716a(String str) {
        try {
            if (this.f973d != null) {
                this.f973d.dispose();
                this.f973d = null;
            }
        } catch (Exception e) {
        }
        this.f973d = new Texture(new C0358a(Gdx.files.internal("textures/galaxy" + str + ".png"), null, null, false, 4883));
        this.f974e = new TextureRegion(this.f973d, 0, 0, 512, 512);
    }

    public void m1717a(boolean z) {
        if (z) {
            this.f972c.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            this.f973d.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            return;
        }
        this.f972c.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        this.f973d.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
}
