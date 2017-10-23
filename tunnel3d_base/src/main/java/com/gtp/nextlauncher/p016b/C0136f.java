package com.gtp.nextlauncher.p016b;

import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;

import com.gtp.nextlauncher.liverpaper.tunnelbate.C0135h;

import java.util.EmptyStackException;

/* compiled from: SimulateEventManager */
abstract class C0136f implements Callback, C0135h {
    public long f513b;
    final /* synthetic */ C0134a f514c;

    abstract void mo88b(long j);

    C0136f(C0134a c0134a) {
        this.f514c = c0134a;
    }

    void m821b() {
        C0136f c0135h;
        try {
            c0135h = (C0136f) this.f514c.f507k.pop();
        } catch (EmptyStackException e) {
            c0135h = null;
        }
        if (c0135h != null) {
            c0135h.mo88b(SystemClock.uptimeMillis());
            this.f514c.f508l.m1047a(c0135h);
            return;
        }
        this.f514c.m809e();
        this.f514c.m818d();
    }

    public boolean handleMessage(Message message) {
        return false;
    }
}
