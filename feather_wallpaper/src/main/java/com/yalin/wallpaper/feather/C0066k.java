package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.Gdx;
import com.badlogic1.gdx.files.FileHandle;
import com.badlogic1.gdx.graphics.Texture;
import com.badlogic1.gdx.graphics.g2d.TextureRegion;

public class C0066k {
    public Texture f181b;
    public Texture f182c;
    public TextureRegion f183d;
    public TextureRegion f184e;

    public void m185a() {
        FileHandle a;
        int i = 0;
        if (Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) >= 800) {
            i = 1;
        } else {
            boolean z = false;
        }
        if (i != 0) {
            a = Gdx.files.internal("textures/background2.jpg");
        } else {
            a = Gdx.files.internal("textures/background1.jpg");
        }
        this.f181b = new Texture(new C0029b(a, null, null, false, 4448));
        this.f182c = new Texture(new C0029b(Gdx.files.internal("textures/item.png"), null, null, false, 4448));
        if (i != 0) {
            this.f183d = new TextureRegion(this.f181b, 1, 1, 1022, 1022);
        } else {
            this.f183d = new TextureRegion(this.f181b, 1, 1, 510, 510);
        }
        this.f184e = new TextureRegion(this.f182c, 0, 0, 128, 256);
    }

    public void m186a(boolean z) {
        if (z) {
            this.f181b.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            this.f182c.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            return;
        }
        this.f181b.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.f182c.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }
}
