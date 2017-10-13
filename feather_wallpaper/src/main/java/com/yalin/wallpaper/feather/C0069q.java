package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.graphics.g2d.SpriteBatch;
import com.badlogic1.gdx.graphics.g2d.TextureRegion;
import com.badlogic1.gdx.math.Vector2;

import java.util.Random;

public class C0069q extends C0027a {
    float f238A = 1.0f;
    float f239B = 1.0f;
    float f240C = 0.0f;
    float f241D = 0.0f;
    float f242E = 0.0f;
    float f243F = 0.0f;
    Vector2 f244G = new Vector2();
    float f245H = 1.0f;
    float f246I = 0.96f;
    float f247J;
    float f248K;
    float f249L;
    double f250M;
    Vector2 f251N = new Vector2();
    float f252O = 10.0f;
    float f253P = 0.1f;
    final Vector2 f254h;
    final Vector2 f255i;
    final Vector2 f256j;
    final Vector2 f257k;
    final float f258l;
    float f259m = 0.0f;
    float f260n = 0.0f;
    boolean f261o = false;
    float f262p = 1.0f;
    float f263q = 1.0f;
    float f264r = 1.0f;
    float f265s = 1.0f;
    float f266t = 1.0f;
    float f267u = 1.0f;
    float f268v = 0.0f;
    float f269w = 0.0f;
    float f270x = 0.0f;
    float f271y = 0.0f;
    float f272z = 1.0f;

    public C0069q(float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4);
        this.f254h = new Vector2(f, f2);
        this.f255i = new Vector2(f, f2);
        this.f256j = new Vector2(0.0f, 0.0f);
        this.f257k = new Vector2(f3 / 2.0f, f4 / 2.0f);
        this.f258l = Math.max(f3 / 2.0f, f4 / 2.0f);
        mo55a();
    }

    public void m210a(Vector2 c0367i, float f) {
        this.f255i.add((f128a.x + this.f244G.x) * f, (f128a.y + this.f244G.y) * f);
        Vector2 c0367i2 = this.f251N;
        c0367i2.x += ((c0367i.x * this.f252O) - this.f251N.x) * this.f253P;
        c0367i2 = this.f251N;
        c0367i2.y += ((c0367i.y * this.f252O) - this.f251N.y) * this.f253P;
        this.f254h.set(this.f255i.x - this.f251N.x, this.f255i.y - this.f251N.y);
        f124c.set(this.f254h);
        this.f244G.mul(this.f246I);
        mo57c();
    }

    public void m206a(float f) {
        f124c.add((f128a.x + this.f244G.x) * f, (f128a.y + this.f244G.y) * f);
        this.f244G.mul(this.f246I);
        mo57c();
    }

    public void m213b(float f) {
        this.f259m += this.f260n * f;
    }

    public void m208a(float f, float f2, float f3, boolean z) {
        m207a(f, f2, f3);
        this.f261o = z;
    }

    public void m207a(float f, float f2, float f3) {
        this.f265s = f;
        this.f266t = f;
        this.f267u = f2;
        this.f268v = f2 - f;
        this.f270x = 0.0f;
        this.f269w = f3;
    }

    public void m209a(SpriteBatch c0321a, TextureRegion c0322b) {
        c0321a.setColor(this.f262p, this.f263q, this.f264r, this.f265s);
        c0321a.draw(c0322b, f124c.x - this.f257k.x, f124c.y - this.f257k.y, this.f257k.x,
                this.f257k.y, this.f126e, this.f127f, this.f272z, this.f272z, this.f259m);
    }

    protected void mo57c() {
        if (f124c.x < ((-this.f258l) * this.f272z) * 1.2f) {
            mo56b();
        } else if (f124c.x > ((float) C0030d.m131a()) + ((this.f258l * this.f272z) * 1.2f)) {
            mo56b();
        }
        if (f124c.y > ((float) C0030d.m135b()) + ((this.f258l * this.f272z) * 1.2f)) {
            mo56b();
        } else if (f124c.y < ((-this.f258l) * this.f272z) * 1.2f) {
            mo56b();
        }
    }

    public boolean m211a(float f, float f2) {
        this.f247J = f - f124c.x;
        this.f248K = f2 - f124c.y;
        this.f249L = (this.f247J * this.f247J) + (this.f248K * this.f248K);
        if (this.f249L >= 36000.0f) {
            return false;
        }
        this.f250M = Math.atan2((double) this.f248K, (double) this.f247J);
        this.f244G.x = ((float) ((((double) f124c.x) + Math.cos(this.f250M)) - ((double) f))) * this.f245H;
        this.f244G.y = ((float) ((((double) f124c.y) + Math.sin(this.f250M)) - ((double) f2))) * this.f245H;
        return true;
    }

    protected void mo55a() {
        Random random = new Random();
        this.f255i.set(f124c);
        f128a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
        this.f256j.set(f128a);
        this.f252O = ((float) random.nextInt(6)) + 6.0f;
    }

    protected void mo56b() {
        Random random = new Random();
        f124c.x = random.nextFloat() * ((float) C0030d.m131a());
        f124c.y = (random.nextFloat() * ((((float) C0030d.m135b()) / 4.0f) * 2.0f)) + (((float) C0030d.m135b()) / 8.0f);
        f128a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
        this.f244G.set(0.0f, 0.0f);
        m208a(0.0f, (random.nextFloat() * 0.4f) + 0.6f, (random.nextFloat() * 1.6f) + 0.4f, true);
    }
}
