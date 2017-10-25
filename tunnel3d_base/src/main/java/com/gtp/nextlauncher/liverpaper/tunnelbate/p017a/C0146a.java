package com.gtp.nextlauncher.liverpaper.tunnelbate.p017a;

import android.os.Handler;
import android.os.Message;

import com.gtp.nextlauncher.liverpaper.tunnelbate.TunnelRender;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.ModelData;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.Constant;
import com.gtp.nextlauncher.p016b.SimulateEventManager;

/* compiled from: AnsyHandler */
public class C0146a extends Handler {
    private TunnelRender f576a;
    private SimulateEventManager f577b;

    public void m885a(TunnelRender tunnelRender) {
        this.f576a = tunnelRender;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        switch (message.what) {
            case 1:
                Constant.f593a = true;
                this.f576a.m1048a((ModelData) message.obj);
                return;
            case 10001:
                if (this.f577b != null) {
                    this.f577b.m818d();
                    return;
                }
                return;
            default:
                break;
        }
    }
}
