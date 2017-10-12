package com.yalin.wallpaper.wavez.base;

import android.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class C0109b extends C0061a {

  final Vector2 f217g;
  final Vector2 f218h;
  private final Vector2 f219i;
  private final Vector2 f220j;
  Vector2 f221k;
  private float f222l = 1.0f;
  float f223m = 1.0f;
  private float f224n = 1.0f;
  private float f225o = 1.0f;
  private float f226p = 1.0f;
  private boolean f227q = false;
  private float f228r = 1.0f;
  private float f229s = 1.0f;
  private float f230t = 1.0f;
  private float f231u = 0.0f;
  private float f232v = 0.0f;
  private float f233w = 0.0f;
  private float f234x = 0.0f;

  public C0109b(float f, float f2, float f3, float f4) {
    super(f, f2, f3, f4);
    this.f217g = new Vector2(f3, f4);
    this.f218h = new Vector2(f, f2);
    this.f219i = new Vector2(f, f2);
    this.f220j = new Vector2(f, f2);
  }

  public void m221a(Vector2 Vector2) {
    this.f220j.set(this.f218h.x + Vector2.x, this.f218h.y + Vector2.y);
    Vector2 vector2 = this.f137c;
    vector2.x += (this.f220j.x - this.f137c.x) * 0.1f;
    vector2 = this.f137c;
    vector2.y += (this.f220j.y - this.f137c.y) * 0.1f;
  }

  public void m218a() {
    Vector2 vector2 = this.f137c;
    vector2.x += (this.f218h.x - this.f137c.x) * 0.1f;
  }

  public void m220a(SpriteBatch spriteBatch, TextureRegion textureRegion) {
    spriteBatch.setColor(this.f224n, this.f225o, this.f226p, this.f228r);
    spriteBatch
        .draw(textureRegion, this.f137c.x, this.f137c.y, this.f138d.width, this.f138d.height);
    spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
  }

  public void m219a(int i) {
    this.f224n = ((float) Color.red(i)) / 255.0f;
    this.f225o = ((float) Color.green(i)) / 255.0f;
    this.f226p = ((float) Color.blue(i)) / 255.0f;
  }
}
