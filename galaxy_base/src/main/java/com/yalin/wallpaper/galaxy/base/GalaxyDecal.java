package com.yalin.wallpaper.galaxy.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class GalaxyDecal extends Decal {
    boolean f1043a = false;
    float f1046d = 1.0f;
    float f1047e = 1.0f;
    float f1048f = 1.0f;
    float f1049g = 0.0f;
    float f1050h = 1.0f;
    float f1051i = 1.0f;
    float f1052j = 1.0f;
    float f1053k = 0.0f;
    float f1054l = 0.0f;
    float f1055m = 0.0f;
    float f1056n = 1.0f;
    float f1057o = 1.0f;
    float f1058p = 1.0f;
    float f1059q = 0.0f;
    float f1060r = 0.0f;
    float f1061s = 0.0f;
    float f1062t = 0.0f;

    public GalaxyDecal(float f, float f2, TextureRegion c0858a, boolean z) {
        int i = -1;
        int i2 = z ? 770 : -1;
        if (z) {
            i = 771;
        }
        setTextureRegion(c0858a);
        setBlending(i2, i);
        this.dimensions.x = f;
        this.dimensions.y = f2;
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void m1787a(float f) {
        this.f1054l += f;
        this.f1055m = this.f1054l / this.f1053k;
        if (this.f1054l >= this.f1053k) {
            this.f1046d = this.f1048f;
            if (this.f1043a) {
                m1788a(this.f1048f, this.f1047e, this.f1053k);
            }
        } else {
            this.f1046d = this.f1047e + (this.f1049g * this.f1055m);
        }
        setColor(this.f1050h, this.f1051i, this.f1052j, this.f1046d);
    }

    public void m1789a(float f, float f2, float f3, boolean z) {
        m1788a(f, f2, f3);
        this.f1043a = z;
    }

    public void m1788a(float f, float f2, float f3) {
        this.f1046d = f;
        this.f1047e = f;
        this.f1048f = f2;
        this.f1049g = f2 - f;
        this.f1054l = 0.0f;
        this.f1053k = f3;
    }

    public void m1790b(float f) {
        this.f1061s += f;
        this.f1062t = this.f1061s / this.f1060r;
        if (this.f1061s >= this.f1060r) {
            this.f1056n = this.f1058p;
            if (this.f1043a) {
                m1791b(this.f1058p, this.f1057o, this.f1060r);
            }
        } else {
            this.f1056n = this.f1057o + (this.f1059q * this.f1062t);
        }
        this.scale.x = this.f1056n;
        this.scale.y = this.f1056n;
        this.updated = false;
    }

    public void m1792b(float f, float f2, float f3, boolean z) {
        m1791b(f, f2, f3);
        this.f1043a = z;
    }

    public void m1791b(float f, float f2, float f3) {
        this.f1056n = f;
        this.f1057o = f;
        this.f1058p = f2;
        this.f1059q = f2 - f;
        this.f1061s = 0.0f;
        this.f1060r = f3;
    }
}
