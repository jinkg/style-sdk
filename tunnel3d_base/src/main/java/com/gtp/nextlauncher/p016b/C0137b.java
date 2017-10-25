package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0137b extends C0136f implements Callback {
    final /* synthetic */ SimulateEventManager f515a;
    private Handler f516d = new Handler(this);
    private int f517e = 0;
    private float[] f518f = new float[2];
    private float[] f519g = new float[2];
    private float[] f520h = new float[2];
    private float[] f521i = new float[2];
    private int f522j = 1;
    private float f523k = 1.0f;
    private boolean f524l = false;
    private final int f525m = 0;
    private long f526n = 0;
    private long f527o = 600;
    private long f528p = 500;
    private long f529q = 500;
    private long f530r = 500;

    public C0137b(SimulateEventManager simulateEventManager) {
        super(simulateEventManager);
        this.f515a = simulateEventManager;
    }

    public void mo87a(long j) {
    }

    public void mo86a(int i, int i2, float f) {
    }

    void mo88b(long j) {
        f513b = j;
        if (this.f516d != null) {
            this.f526n = AnimationUtils.currentAnimationTimeMillis();
            Message message = new Message();
            message.what = 6;
            this.f516d.sendMessageDelayed(message, 0);
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 6:
                m823c();
                break;
        }
        return false;
    }

    private void m823c() {
        m824d();
        float[] e = m825e();
        if (this.f524l) {
            this.f515a.f508l.m1051b();
            super.m821b();
            return;
        }
        Message message = new Message();
        message.what = 6;
        this.f516d.sendMessageDelayed(message, 0);
        float f = e[0];
        float f2 = e[1];
        if (this.f515a.f511o != null) {
            LayoutParams layoutParams = (LayoutParams) this.f515a.f511o.getLayoutParams();
            layoutParams.setMargins((int) f, (int) f2, 0, 0);
            this.f515a.f511o.setLayoutParams(layoutParams);
        }
        if (this.f515a.f510n != null) {
            LayoutParams layoutParams = (LayoutParams) this.f515a.f510n.getLayoutParams();
            layoutParams.setMargins(((int) f) + ((this.f515a.f511o.getWidth() / 2) - (this.f515a.f510n.getWidth() / 3)), ((int) f2) + (this.f515a.f511o.getHeight() / 2), 0, 0);
            this.f515a.f510n.setLayoutParams(layoutParams);
        }
        this.f515a.f508l.m1044a(f, f2);
    }

    private void m824d() {
        float d = (float) this.f515a.f508l.m1055d();
        float e = (float) this.f515a.f508l.m1056e();
        this.f523k = e / d;
        this.f518f[0] = d * 0.75f;
        this.f518f[1] = e * 0.25f;
        this.f519g[0] = d * 0.25f;
        this.f519g[1] = e * 0.25f;
        this.f520h[0] = d * 0.75f;
        this.f520h[1] = e * 0.75f;
        this.f521i[0] = d * 0.25f;
        this.f521i[1] = e * 0.75f;
    }

    private float[] m825e() {
        float max;
        float[] fArr = new float[2];
        if (this.f522j == 1) {
            max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f526n)) * 1.0f) / ((float) this.f527o), 1.0f));
            if (max < 1.0f) {
                fArr[0] = this.f518f[0] - (max * (this.f518f[0] - this.f519g[0]));
                fArr[1] = this.f518f[1];
            } else {
                this.f522j = 2;
                this.f526n = AnimationUtils.currentAnimationTimeMillis();
            }
        }
        if (this.f522j == 2) {
            max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f526n)) * 1.0f) / ((float) this.f528p), 1.0f));
            if (max < 1.0f) {
                max *= this.f520h[0] - this.f519g[0];
                float f = this.f523k * max;
                fArr[0] = max + this.f519g[0];
                fArr[1] = this.f519g[1] + f;
            } else {
                this.f522j = 3;
                this.f526n = AnimationUtils.currentAnimationTimeMillis();
            }
        }
        if (this.f522j == 3) {
            max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f526n)) * 1.0f) / ((float) this.f529q), 1.0f));
            if (max < 1.0f) {
                fArr[0] = this.f520h[0] - (max * (this.f520h[0] - this.f521i[0]));
                fArr[1] = this.f520h[1];
            } else {
                this.f522j = 4;
                this.f526n = AnimationUtils.currentAnimationTimeMillis();
            }
        }
        if (this.f522j == 4) {
            max = Math.max(0.0f, Math.min((((float) (AnimationUtils.currentAnimationTimeMillis() - this.f526n)) * 1.0f) / ((float) this.f530r), 1.0f));
            if (max < 1.0f) {
                max *= (this.f518f[0] + ((float) ((this.f515a.f511o.getWidth() / 2) - (this.f515a.f510n.getWidth() / 3)))) - this.f521i[0];
                float f = this.f523k * max;
                fArr[0] = max + this.f521i[0];
                fArr[1] = this.f521i[1] - f;
                Log.i(this.f515a.f497a, "运行时的位置:\t" + fArr[0] + "Y:\t" + fArr[1]);
            } else {
                Log.i(this.f515a.f497a, "结束时的位置:\t" + fArr[0] + "Y:\t" + fArr[1]);
                this.f524l = true;
                this.f517e = 0;
            }
        }
        return fArr;
    }

    public void m826a() {
        this.f522j = 1;
        this.f517e = 0;
        this.f524l = false;
    }
}
