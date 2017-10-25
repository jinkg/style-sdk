package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0141g extends C0136f implements Callback {
    final /* synthetic */ SimulateEventManager f554a;
    private long f555d;
    private float f556e = -1.0f;
    private boolean f557f = false;
    private Handler f558g = null;
    private OvershootInterpolator f559h;

    public C0141g(SimulateEventManager simulateEventManager, long j, boolean z) {
        super(simulateEventManager);
        this.f554a = simulateEventManager;
        this.f555d = j;
        this.f558g = new Handler(this);
        this.f559h = new OvershootInterpolator();
        this.f557f = z;
    }

    public void mo87a(long j) {
    }

    public void mo86a(int i, int i2, float f) {
        if (i == 4 && i2 == 1) {
            super.m821b();
        }
        if (i == 4 && i2 == 0) {
            Message message = new Message();
            message.obj = f;
            message.what = 4;
            this.f558g.sendMessage(message);
        }
    }

    void mo88b(long j) {
        this.f513b = j;
        this.f554a.f508l.m1043a((float) this.f555d);
        this.f554a.f509m.requestRender();
    }

    public C0141g m847a() {
        this.f556e = -1.0f;
        return this;
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 4:
                m846a((Float) message.obj);
                break;
        }
        return false;
    }

    private void m846a(float f) {
        if (this.f554a.f511o != null) {
            LayoutParams layoutParams = (LayoutParams) this.f554a.f511o.getLayoutParams();
            if (this.f556e == -1.0f) {
                this.f556e = (float) layoutParams.leftMargin;
            }
            int d = (int) ((((float) this.f554a.f508l.m1055d()) * 0.25f) * this.f559h.getInterpolation(f));
            if (this.f557f) {
                this.f554a.f511o.offsetLeftAndRight(-d);
            } else {
                this.f554a.f511o.offsetLeftAndRight(d);
            }
        }
    }
}
