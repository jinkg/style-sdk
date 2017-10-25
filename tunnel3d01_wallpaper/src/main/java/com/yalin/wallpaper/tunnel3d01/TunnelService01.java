package com.yalin.wallpaper.tunnel3d01;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.gtp.nextlauncher.liverpaper.tunnelbate.TunnelConfig;
import com.gtp.nextlauncher.liverpaper.tunnelbate.TunnelRender;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.ModelData;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.OpenGLES2WallpaperService;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p017a.C0146a;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.LoadUtil2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TunnelService01 extends OpenGLES2WallpaperService {
    private TunnelRender f570c;
    private ModelData f571d;
    private ModelData f572e;
    private ModelData f573f;
    private C0146a f574g;
    private ExecutorService f575h;

    public TunnelService01(Context host) {
        super(host);
    }

    public ExecutorService m882a() {
        if (this.f575h == null) {
            this.f575h = Executors.newCachedThreadPool();
        }
        return this.f575h;
    }

    public void onCreate() {
        super.onCreate();
        TunnelConfig.autoRun = true;
        TunnelConfig.autoRunSpeed = 0.05f;
        TunnelConfig.starId = 1;
        TunnelConfig.sceneId = 1;
        this.f571d = m877a("data/model/g_5_01.obj");
        this.f572e = m877a("data/model/cubes.obj");
    }

    public TunnelRender mo93c() {
        this.f570c = new TunnelRender(getApplicationContext(), this.f571d, this.f572e);
        this.f570c.m1046a(m873d());
        this.f574g = new C0146a();
        this.f574g.m885a(this.f570c);
        m881g();
        return this.f570c;
    }

    private ModelData m877a(String str) {
        return LoadUtil2.m924a(str, getResources(), getApplicationContext());
    }


    public void onDestroy() {
        super.onDestroy();
    }

    private void m881g() {
        m882a().execute(new C0178i(this));
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
    }

    class C0178i implements Runnable {
        final /* synthetic */ TunnelService01 f773a;

        C0178i(TunnelService01 tunnelService) {
            this.f773a = tunnelService;
        }

        public void run() {
            this.f773a.f573f = this.f773a.m877a("data/model/g_5_02.obj");
            Message message = new Message();
            message.obj = this.f773a.f573f;
            message.what = 1;
            this.f773a.f574g.sendMessage(message);
        }
    }
}
