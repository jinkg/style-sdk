package com.yalin.wallpaper.wavez.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class C0108a {

  public Texture f213a;
  public Texture f214b;
  public TextureRegion f215c;
  public TextureRegion f216d;

  public void m215a() {
    this.f214b = new Texture(new C0063b(Gdx.files.internal("textures/item.png"),
        null, null, false, 4448));
    if (!String.valueOf(Gdx.files.internal("textures/item.png").length()).equals("4938")) {
      this.f214b = new Texture(1, 1, Format.RGB565);
    }
    this.f216d = new TextureRegion(this.f214b, 0, 0, 16, 16);
  }

  public void m216a(String str) {
    try {
      if (this.f213a != null) {
        this.f213a.dispose();
        this.f213a = null;
      }
    } catch (Exception e) {
    }
    this.f213a = new Texture(new C0063b(Gdx.files.internal("textures/background" + str + ".jpg"),
        null, null, false, 4448));
    this.f215c = new TextureRegion(this.f213a, 1, 1, 1022, 1022);
  }

  public void m217a(boolean z) {
    if (z) {
      this.f213a.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      this.f214b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      return;
    }
    this.f213a.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    this.f214b.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
  }
}
