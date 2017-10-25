package com.gtp.nextlauncher.p016b;

import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: SimulateEventManager */
class C0139d extends C0136f implements AnimationListener {
    final /* synthetic */ SimulateEventManager f538a;
    private float[] f539d = new float[2];
    private Handler f540e = new Handler(this);
    private long f541f = 600;
    private float f542g = 0.1f;
    private float f543h = 2.5f;
    private float f544i = 1.0f;
    private float f545j = 0.0f;
    private AnimationSet f546k = new AnimationSet(false);

    public C0139d(SimulateEventManager simulateEventManager) {
        super(simulateEventManager);
        this.f538a = simulateEventManager;
        Animation scaleAnimation = new ScaleAnimation(this.f542g, this.f543h, this.f542g, this.f543h, 1, 0.5f, 1, 0.5f);
        Animation alphaAnimation = new AlphaAnimation(this.f544i, this.f545j);
        this.f546k.addAnimation(scaleAnimation);
        this.f546k.addAnimation(alphaAnimation);
        this.f546k.setAnimationListener(this);
        this.f546k.setDuration(this.f541f);
        this.f546k.setFillAfter(true);
    }

    public void mo87a(long j) {
    }

    public void mo86a(int i, int i2, float f) {
    }

    void mo88b(long j) {
        m835a();
        m836a(true);
    }

    private void m835a() {
        LayoutParams layoutParams = (LayoutParams) this.f538a.f510n.getLayoutParams();
        this.f539d[0] = (float) ((layoutParams.leftMargin - (this.f538a.f510n.getWidth() / 3)) + 5);
        this.f539d[1] = (float) (layoutParams.topMargin - (this.f538a.f510n.getHeight() / 2));
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 2003:
                LayoutParams layoutParams = (LayoutParams) this.f538a.f512p.getLayoutParams();
                layoutParams.setMargins((int) this.f539d[0], (int) this.f539d[1], 0, 0);
                this.f538a.f512p.setLayoutParams(layoutParams);
                this.f538a.f512p.startAnimation(this.f546k);
                break;
        }
        return super.handleMessage(message);
    }

    private void m836a(boolean z) {
        Message message = new Message();
        message.what = z ? 2003 : 2002;
        this.f540e.sendMessage(message);
    }

    public void onAnimationEnd(Animation animation) {
        super.m821b();
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}
