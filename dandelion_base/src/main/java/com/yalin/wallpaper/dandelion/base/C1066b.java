package com.yalin.wallpaper.dandelion.base;

import android.graphics.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class C1066b {
    public Texture f2827a;
    public Texture f2828b;
    public Point f2829c = new Point();
    public TextureRegion f2830d;
    public Animation f2831e;

    public void m5289a() {
        this.f2828b = new Texture(new C1017b(Gdx.files.internal("textures/item.png"),
                null, null, false, 24084));
        if ((Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) >= 800 ? 1 : null) != null) {
            this.f2830d = new TextureRegion(this.f2828b, 68, 7, 92, 144);
        } else {
            this.f2830d = new TextureRegion(this.f2828b, 4, 5, 53, 83);
        }
        this.f2831e = new Animation(0.06f, new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1),
                new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1),
                new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1),
                new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1),
                new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1),
                new TextureRegion(this.f2828b, 254, 254, 2, 2),
                new TextureRegion(this.f2828b, 250, 250, 1, 1));
    }

    public Sound m5288a(int i) {
        return Gdx.audio.newSound(Gdx.files.internal("sounds/water_drop.ogg"));
    }

    public void m5293b(int i) {
        if (i == 2) {
            this.f2830d = new TextureRegion(this.f2828b, 161, 7, 92, 144);
        } else if (i == 3) {
            this.f2830d = new TextureRegion(this.f2828b, 101, 173, 62, 64);
        } else if (i == 4) {
            this.f2830d = new TextureRegion(this.f2828b, 168, 168, 88, 41);
        } else if (i == 5) {
            this.f2830d = new TextureRegion(this.f2828b, 257, 1, 79, 73);
        } else if (i == 6) {
            this.f2830d = new TextureRegion(this.f2828b, 64, 152, 30, 30);
        } else {
            this.f2830d = new TextureRegion(this.f2828b, 68, 7, 92, 144);
        }
    }

    public void m5294c(int i) {
        try {
            if (this.f2827a != null) {
                this.f2827a.dispose();
                this.f2827a = null;
            }
        } catch (Exception e) {
        }
        if (i > 5) {
            i = 1;
        }
        this.f2827a = new Texture(new C1017b(Gdx.files.internal("textures/background" + i + ".jpg"),
                null, null, false, 4448));
        this.f2829c.x = 1024;
        this.f2829c.y = 1024;
    }

    public void m5290a(boolean z) {
        if (z) {
            this.f2827a.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            this.f2828b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            return;
        }
        this.f2827a.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.f2828b.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
}
