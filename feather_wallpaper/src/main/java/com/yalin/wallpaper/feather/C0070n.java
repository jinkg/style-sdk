package com.yalin.wallpaper.feather;

import java.util.Random;

class C0070n extends C0069q {
    final /* synthetic */ MyApplicationListener f273g;

    C0070n(MyApplicationListener myApplicationListener, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4);
        this.f273g = myApplicationListener;
    }

    protected void mo55a() {
        Random random = new Random();
        f255i.set(f124c);
        f128a.set((random.nextFloat() * 60.0f) + 20.0f, (random.nextFloat() * 30.0f) + 10.0f);
        f256j.set(f128a);
        f129b.set(0.0f, 0.0f);
        f244G.set(0.0f, 0.0f);
        f251N.set(0.0f, 0.0f);
        f252O = ((float) random.nextInt(6)) + 6.0f;
        f265s = 1.0f;
        f259m = random.nextFloat() * 100.0f;
        f260n = (random.nextFloat() * 20.0f) + 10.0f;
    }

    protected void mo56b() {
        Random random = new Random();
        f124c.x = -f258l;
        f124c.y = random.nextFloat() * ((((float) C0030d.m135b()) / 4.0f) * 3.0f);
        f255i.set(f124c);
        f259m = 0.0f;
        f128a.set((random.nextFloat() * 60.0f) + 20.0f, (random.nextFloat() * 30.0f) + 10.0f);
        f256j.set(f128a);
        f129b.set(0.0f, 0.0f);
        f244G.set(0.0f, 0.0f);
        f251N.set(0.0f, 0.0f);
        f252O = ((float) random.nextInt(6)) + 6.0f;
        f265s = 1.0f;
        f260n = (random.nextFloat() * 20.0f) + 10.0f;
    }

    protected void mo57c() {
        if (f124c.x < ((-f258l) * f272z) * 1.2f) {
            mo56b();
        } else if (f124c.x > ((float) C0030d.m131a()) + ((f258l * f272z) * 1.2f)) {
            mo56b();
        }
        if (f124c.y > ((float) C0030d.m135b()) + ((f258l * f272z) * 1.2f)) {
            mo56b();
        } else if (f124c.y < ((-f258l) * f272z) * 1.2f) {
            mo56b();
        }
    }
}
