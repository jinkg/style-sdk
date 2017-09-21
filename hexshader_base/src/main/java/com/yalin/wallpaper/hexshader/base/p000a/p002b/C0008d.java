package com.yalin.wallpaper.hexshader.base.p000a.p002b;

class C0008d implements Runnable {
    final /* synthetic */ C0007c f19a;

    C0008d(C0007c c0007c) {
        this.f19a = c0007c;
    }

    public void run() {
        this.f19a.f15a.requestRender();
        this.f19a.f17c.postDelayed(this.f19a.f18d, (long) this.f19a.f16b);
    }
}
