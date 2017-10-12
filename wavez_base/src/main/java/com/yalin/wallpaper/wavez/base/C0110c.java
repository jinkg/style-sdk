package com.yalin.wallpaper.wavez.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class C0110c extends C0061a {

  float f235A = 0.0f;
  float f236B = 0.0f;
  float f237C = 0.0f;
  Vector2 f238D = new Vector2();
  float f239E = 2.0f;
  float f240F = 0.92f;
  float f241G;
  float f242H;
  float f243I;
  double f244J;
  Vector2 f245K = new Vector2();
  float f246L = 10.0f;
  float f247M = 0.1f;
  final Vector2 f248g;
  final Vector2 f249h;
  final Vector2 f250i;
  final Vector2 f251j;
  float f252k = 0.0f;
  boolean f253l = false;
  float f254m = 1.0f;
  float f255n = 1.0f;
  float f256o = 1.0f;
  float f257p = 1.0f;
  float f258q = 1.0f;
  float f259r = 1.0f;
  float f260s = 0.0f;
  float f261t = 0.0f;
  float f262u = 0.0f;
  float f263v = 0.0f;
  float f264w = 1.0f;
  float f265x = 1.0f;
  float f266y = 1.0f;
  float f267z = 0.0f;

  public C0110c(float f, float f2, float f3, float f4) {
    super(f, f2, f3, f4);
    f248g = new Vector2(f, f2);
    f249h = new Vector2(f, f2);
    f250i = new Vector2(0.0f, 0.0f);
    f251j = new Vector2(f3 / 2.0f, f4 / 2.0f);
    mo45b();
  }

  public void m227a(Vector2 Vector2, float f) {
    f249h.add((f141a.x + f238D.x) * f,
        (f141a.y + f238D.y) * f);
    Vector2 Vector22 = f245K;
    Vector22.x += ((Vector2.x * f246L) - f245K.x) * f247M;
    Vector22 = f245K;
    Vector22.y += ((Vector2.y * f246L) - f245K.y) * f247M;
    f248g.set(f249h.x - f245K.x, f249h.y - f245K.y);
    f137c.set(f248g);
    f238D.scl(f240F);
    m222a();
  }

  public void m223a(float f) {
    f137c.add((f141a.x + f238D.x) * f,
        (f141a.y + f238D.y) * f);
    f238D.scl(f240F);
    m222a();
  }

  public void m230b(float f) {
    f262u += f;
    f263v = f262u / f261t;
    if (f262u >= f261t) {
      f257p = f259r;
      if (f253l) {
        m224a(f259r, f258q, f261t);
        return;
      }
      return;
    }
    f257p = f258q + (f260s * f263v);
  }

  public void m225a(float f, float f2, float f3, boolean z) {
    m224a(f, f2, f3);
    f253l = z;
  }

  public void m224a(float f, float f2, float f3) {
    f257p = f;
    f258q = f;
    f259r = f2;
    f260s = f2 - f;
    f262u = 0.0f;
    f261t = f3;
  }

  public void m226a(SpriteBatch SpriteBatch, TextureRegion TextureRegion) {
    SpriteBatch.setColor(f254m, f255n, f256o, f257p);
    SpriteBatch.draw(TextureRegion, f137c.x - f251j.x,
        f137c.y - f251j.y, f251j.x,
        f251j.y, f139e, f140f, f264w, f264w, 0.0f);
  }

  protected void m222a() {
    if (f137c.x < ((-f251j.x) * f264w) * 1.5f) {
      mo46c();
    } else if (f137c.x > ((float) C0064d.m174a()) + ((f251j.x * f264w) * 1.5f)) {
      mo46c();
    }
    if (f137c.y > ((float) C0064d.m178b()) + (f251j.y * f264w)) {
      mo46c();
    }
  }

  public boolean m228a(float f, float f2) {
    f241G = f - f137c.x;
    f242H = f2 - f137c.y;
    f243I = (f241G * f241G) + (f242H * f242H);
    if (f243I >= 36000.0f) {
      return false;
    }
    f244J = Math.atan2((double) f242H, (double) f241G);
    f238D.x = ((float) ((((double) f137c.x) + Math.cos(f244J))
        - ((double) f))) * f239E;
    f238D.y = ((float) ((((double) f137c.y) + Math.sin(f244J))
        - ((double) f2))) * f239E;
    return true;
  }

  protected void mo45b() {
    Random random = new Random();
    f249h.set(f137c);
    f141a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
    f250i.set(f141a);
    f246L = ((float) random.nextInt(6)) + 6.0f;
  }

  protected void mo46c() {
    Random random = new Random();
    f137c.x = random.nextFloat() * ((float) C0064d.m174a());
    f137c.y = (random.nextFloat() * ((((float) C0064d.m178b()) / 4.0f) * 2.0f))
        + (((float) C0064d.m178b()) / 8.0f);
    f141a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
    f238D.set(0.0f, 0.0f);
    m225a(0.0f, (random.nextFloat() * 0.4f) + 0.6f, (random.nextFloat() * 1.6f) + 0.4f, true);
  }
}
