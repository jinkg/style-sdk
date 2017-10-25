package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Message;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0140e extends C0136f {
    final /* synthetic */ SimulateEventManager f547a;
    private Handler f548d = new Handler(this);
    private float[] f549e = new float[2];
    private float[] f550f = new float[2];
    private long f551g;
    private boolean f552h = false;
    private boolean f553i = true;

    public C0140e(SimulateEventManager simulateEventManager, boolean z) {
        super(simulateEventManager);
        this.f547a = simulateEventManager;
        this.f553i = z;
    }

    public void mo87a(long j) {
    }

    public void mo86a(int i, int i2, float f) {
    }

    private void m840a() {
        this.f552h = false;
        if (this.f553i) {
            this.f549e[0] = ((float) this.f547a.f508l.m1055d()) * 0.75f;
            this.f549e[1] = ((float) this.f547a.f508l.m1056e()) * 0.5f;
            this.f550f[0] = this.f549e[0] + ((float) ((this.f547a.f511o.getWidth() / 2) - (this.f547a.f510n.getWidth() / 3)));
            this.f550f[1] = (((float) this.f547a.f508l.m1056e()) * 0.25f) + (((float) this.f547a.f511o.getHeight()) / 2.0f);
        } else {
            LayoutParams layoutParams = (LayoutParams) this.f547a.f510n.getLayoutParams();
            this.f549e[0] = (float) layoutParams.leftMargin;
            this.f549e[1] = (float) layoutParams.topMargin;
            this.f550f[0] = ((float) this.f547a.f508l.m1055d()) * 0.75f;
            this.f550f[1] = ((float) this.f547a.f508l.m1056e()) * 0.5f;
        }
        this.f551g = AnimationUtils.currentAnimationTimeMillis();
    }

    private float[] m841c() {
        float[] fArr = new float[2];
        float max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f551g)) * 1.0f) / 500.0f, 1.0f));
        if (max == 1.0f) {
            this.f552h = true;
        }
        float f = this.f549e[0] + ((this.f550f[0] - this.f549e[0]) * max);
        max = (max * (this.f550f[1] - this.f549e[1])) + this.f549e[1];
        fArr[0] = f;
        fArr[1] = max;
        return fArr;
    }

    void mo88b(long j) {
        m840a();
        Message message = new Message();
        message.what = 2001;
        this.f548d.sendMessage(message);
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 2001:
                m842d();
                break;
        }
        return super.handleMessage(message);
    }

    private void m842d() {
        float[] c = m841c();
        int i = (int) c[0];
        int i2 = (int) c[1];
        LayoutParams layoutParams = (LayoutParams) this.f547a.f510n.getLayoutParams();
        layoutParams.setMargins(i, i2, 0, 0);
        this.f547a.f510n.setLayoutParams(layoutParams);
        if (this.f552h) {
            super.m821b();
            return;
        }
        Message message = new Message();
        message.what = 2001;
        this.f548d.sendMessage(message);
    }
}
