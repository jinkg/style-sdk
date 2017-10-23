package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0138c extends C0136f implements Callback {
    final /* synthetic */ C0134a f531a;
    private long f532d;
    private float[] f533e;
    private float[] f534f;
    private Handler f535g = new Handler(this);
    private boolean f536h = true;
    private int f537i = 0;

    public C0138c(C0134a c0134a, boolean z, float[] fArr, float[] fArr2) {
        super(c0134a);
        this.f531a = c0134a;
        this.f536h = z;
        this.f533e = fArr;
        this.f534f = fArr2;
    }

    public void mo87a(long j) {
    }

    public void mo86a(int i, int i2, float f) {
    }

    void mo88b(long j) {
        this.f532d = j;
        float d = 0.75f * ((float) this.f531a.f508l.m1055d());
        float d2 = 0.25f * ((float) this.f531a.f508l.m1055d());
        this.f533e[0] = this.f536h ? d : d2;
        float[] fArr = this.f534f;
        if (!this.f536h) {
            d2 = d;
        }
        fArr[0] = d2;
        if (this.f535g != null) {
            Message message = new Message();
            message.what = 2000;
            this.f535g.sendMessageDelayed(message, 0);
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 2000:
                m830c();
                break;
        }
        return false;
    }

    private void m830c() {
        float f = this.f533e[0];
        float f2 = this.f534f[0];
        float f3 = 10.0f * ((float) this.f537i);
        float d = this.f536h ? ((float) this.f531a.f508l.m1055d()) * 0.75f : ((float) this.f531a.f508l.m1055d()) * 0.25f;
        float f4 = this.f536h ? f - f3 : f + f3;
        this.f531a.f508l.m1054c(f4, f);
        if (this.f531a.f510n != null) {
            LayoutParams layoutParams = (LayoutParams) this.f531a.f510n.getLayoutParams();
            layoutParams.setMargins((int) f4, this.f531a.f508l.m1056e() / 2, 0, 0);
            this.f531a.f510n.setLayoutParams(layoutParams);
        }
        if (this.f531a.f511o != null) {
            LayoutParams layoutParams = (LayoutParams) this.f531a.f511o.getLayoutParams();
            if (this.f536h) {
                f3 *= -1.0f;
            }
            layoutParams.setMargins((int) (d + f3), (this.f531a.f508l.m1056e() / 2) - ((this.f531a.f511o.getHeight() * 2) + (this.f531a.f511o.getHeight() / 2)), 0, 0);
            this.f531a.f511o.setLayoutParams(layoutParams);
        }
        if (this.f536h && f4 < f2) {
            super.m821b();
        } else if (this.f536h || f4 <= f2) {
            Message message = new Message();
            message.what = 2000;
            this.f535g.sendMessageDelayed(message, 0);
            this.f537i++;
        } else {
            super.m821b();
        }
    }

    public C0138c m831a() {
        this.f537i = 0;
        return this;
    }
}
