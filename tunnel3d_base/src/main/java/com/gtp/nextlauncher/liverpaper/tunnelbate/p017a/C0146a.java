package com.gtp.nextlauncher.liverpaper.tunnelbate.p017a;

import android.os.Handler;
import android.os.Message;

import com.gtp.nextlauncher.liverpaper.tunnelbate.C0177g;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.C0185f;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0154b;
import com.gtp.nextlauncher.p016b.C0134a;

/* compiled from: AnsyHandler */
public class C0146a extends Handler {
    private C0177g f576a;
    private C0134a f577b;

    public void m885a(C0177g c0177g) {
        this.f576a = c0177g;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        switch (message.what) {
            case 1:
                C0154b.f593a = true;
                this.f576a.m1048a((C0185f) message.obj);
                return;
            case 10001:
                if (this.f577b != null) {
                    this.f577b.m818d();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
