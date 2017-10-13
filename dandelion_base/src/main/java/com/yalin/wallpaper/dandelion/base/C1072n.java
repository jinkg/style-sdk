package com.yalin.wallpaper.dandelion.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class C1072n extends C1015a {
    float f2856A = 1.0f;
    float f2857B = 1.0f;
    float f2858C = 1.0f;
    float f2859D = 0.0f;
    float f2860E = 0.0f;
    float f2861F = 0.0f;
    float f2862G = 0.0f;
    Vector2 f2863H = new Vector2();
    float f2864I = 1.0f;
    float f2865J = 0.96f;
    float f2866K;
    float f2867L;
    float f2868M;
    double f2869N;
    Vector2 f2870O = new Vector2();
    float f2871P = 10.0f;
    float f2872Q = 0.1f;
    final Vector2 f2873i;
    final Vector2 f2874j;
    final Vector2 f2875k;
    final Vector2 f2876l;
    final float f2877m;
    float f2878n = 0.0f;
    float f2879o = 0.0f;
    boolean f2880p = false;
    float f2881q = 1.0f;
    float f2882r = 1.0f;
    float f2883s = 1.0f;
    float f2884t = 1.0f;
    float f2885u = 1.0f;
    float f2886v = 1.0f;
    float f2887w = 0.0f;
    float f2888x = 0.0f;
    float f2889y = 0.0f;
    float f2890z = 0.0f;

    public C1072n(float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4);
        this.f2873i = new Vector2(f, f2);
        this.f2874j = new Vector2(f, f2);
        this.f2875k = new Vector2(0.0f, 0.0f);
        this.f2876l = new Vector2(f3 / 2.0f, f4 / 2.0f);
        this.f2877m = Math.max(f3 / 2.0f, f4 / 2.0f);
        mo1157a();
    }

    public void m5309a(Vector2 c1255g, float f) {
        this.f2874j.add((this.f2698a.x + this.f2863H.x) * f, (this.f2698a.y + this.f2863H.y) * f);
        Vector2 c1255g2 = this.f2870O;
        c1255g2.x += ((c1255g.x * this.f2871P) - this.f2870O.x) * this.f2872Q;
        c1255g2 = this.f2870O;
        c1255g2.y += ((c1255g.y * this.f2871P) - this.f2870O.y) * this.f2872Q;
        this.f2873i.set(this.f2874j.x - this.f2870O.x, this.f2874j.y - this.f2870O.y);
        this.f2694c.set(this.f2873i);
        this.f2863H.scl(this.f2865J);
        mo1159c();
    }

    public void m5305a(float f) {
        this.f2694c.add((this.f2698a.x + this.f2863H.x) * f, (this.f2698a.y + this.f2863H.y) * f);
        this.f2863H.scl(this.f2865J);
        mo1159c();
    }

    public void m5312b(float f) {
        this.f2878n += this.f2879o * f;
    }

    public void m5307a(float f, float f2, float f3, boolean z) {
        m5306a(f, f2, f3);
        this.f2880p = z;
    }

    public void m5306a(float f, float f2, float f3) {
        this.f2884t = f;
        this.f2885u = f;
        this.f2886v = f2;
        this.f2887w = f2 - f;
        this.f2889y = 0.0f;
        this.f2888x = f3;
    }

    public void m5308a(SpriteBatch c1208c, TextureRegion c1209d) {
        c1208c.setColor(this.f2881q, this.f2882r, this.f2883s, this.f2884t);
        c1208c.draw(c1209d, this.f2694c.x - this.f2876l.x, this.f2694c.y - this.f2876l.y,
                this.f2876l.x, this.f2876l.y, this.f2696e, this.f2697f, this.f2856A, this.f2856A, this.f2878n);
    }

    protected void mo1159c() {
        if (this.f2694c.x < ((-this.f2877m) * this.f2856A) * 1.2f) {
            mo1158b();
        } else if (this.f2694c.x > ((float) C1018d.m5168a()) + ((this.f2877m * this.f2856A) * 1.2f)) {
            mo1158b();
        }
        if (this.f2694c.y > ((float) C1018d.m5173b()) + ((this.f2877m * this.f2856A) * 1.2f)) {
            mo1158b();
        } else if (this.f2694c.y < ((-this.f2877m) * this.f2856A) * 1.2f) {
            mo1158b();
        }
    }

    public boolean m5310a(float f, float f2) {
        this.f2866K = f - this.f2694c.x;
        this.f2867L = f2 - this.f2694c.y;
        this.f2868M = (this.f2866K * this.f2866K) + (this.f2867L * this.f2867L);
        if (this.f2868M >= 36000.0f) {
            return false;
        }
        this.f2869N = Math.atan2((double) this.f2867L, (double) this.f2866K);
        this.f2863H.x = ((float) ((((double) this.f2694c.x) + Math.cos(this.f2869N)) - ((double) f))) * this.f2864I;
        this.f2863H.y = ((float) ((((double) this.f2694c.y) + Math.sin(this.f2869N)) - ((double) f2))) * this.f2864I;
        return true;
    }

    protected void mo1157a() {
        Random random = new Random();
        this.f2874j.set(this.f2694c);
        this.f2698a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
        this.f2875k.set(this.f2698a);
        this.f2871P = ((float) random.nextInt(6)) + 6.0f;
    }

    protected void mo1158b() {
        Random random = new Random();
        this.f2694c.x = random.nextFloat() * ((float) C1018d.m5168a());
        this.f2694c.y = (random.nextFloat() * ((((float) C1018d.m5173b()) / 4.0f) * 2.0f)) + (((float) C1018d.m5173b()) / 8.0f);
        this.f2698a.set(-((random.nextFloat() * 70.0f) + 10.0f), (random.nextFloat() * 25.0f) + 10.0f);
        this.f2863H.set(0.0f, 0.0f);
        m5307a(0.0f, (random.nextFloat() * 0.4f) + 0.6f, (random.nextFloat() * 1.6f) + 0.4f, true);
    }
}
