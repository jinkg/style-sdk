package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0142h extends C0136f {
    final /* synthetic */ SimulateEventManager f560a;
    private long f561d;
    private long f562e;
    private Handler f563f;
    private float[] f564g = new float[2];

    public void m853a(float[] fArr) {
        this.f564g = fArr;
    }

    public C0142h(SimulateEventManager simulateEventManager, long j) {
        super(simulateEventManager);
        this.f560a = simulateEventManager;
        this.f561d = j;
        this.f563f = new Handler(this);
    }

    public void mo87a(long j) {
        if (j - this.f562e > this.f561d) {
            super.m821b();
        }
    }

    public void mo86a(int i, int i2, float f) {
    }

    void mo88b(long j) {
        this.f562e = j;
        this.f560a.f508l.m1045a(((float) this.f560a.f508l.m1055d()) * 0.75f, ((float) this.f560a.f508l.m1056e()) * 0.25f, false);
        Message message = new Message();
        message.what = 9;
        this.f563f.sendMessage(message);
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 9:
                if (this.f560a.f510n != null) {
                    LayoutParams layoutParams;
                    float d = 0.75f * ((float) this.f560a.f508l.m1055d());
                    float e = 0.25f * ((float) this.f560a.f508l.m1056e());
                    if (this.f560a.f511o != null) {
                        layoutParams = (LayoutParams) this.f560a.f511o.getLayoutParams();
                        layoutParams.setMargins((int) d, (int) e, 0, 0);
                        this.f560a.f511o.setLayoutParams(layoutParams);
                    }
                    layoutParams = (LayoutParams) this.f560a.f510n.getLayoutParams();
                    layoutParams.leftMargin = ((int) d) + ((this.f560a.f511o.getWidth() / 2) - (this.f560a.f510n.getWidth() / 3));
                    layoutParams.topMargin = ((int) e) + (this.f560a.f511o.getHeight() / 2);
                    this.f560a.f510n.setLayoutParams(layoutParams);
                    this.f560a.f509m.requestRender();
                    break;
                }
                break;
        }
        return super.handleMessage(message);
    }
}
