package com.yalin.wallpaper.tunnel3d01;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.gtp.nextlauncher.liverpaper.tunnelbate.C0177g;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.C0185f;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.OpenGLES2WallpaperService;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p017a.C0146a;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0157e;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TunnelService extends OpenGLES2WallpaperService {
    private C0177g f570c;
    private C0185f f571d;
    private C0185f f572e;
    private C0185f f573f;
    private C0146a f574g;
    private ExecutorService f575h;

    public TunnelService(Context host) {
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
        long currentTimeMillis = System.currentTimeMillis();
        this.f571d = m877a("data/model/g_5_01.obj");
        this.f572e = m877a("data/model/cubes.obj");
//        C0232l.m1362b("tyler.tang", "加载模型用时:\t" + (System.currentTimeMillis() - currentTimeMillis));
        m879e();
        m883b();
        m880f();
    }

    private void m879e() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("interval_time_notification_net_params", "not_dur_tunnel");
        hashMap.put("max_count24h_net_notification_net_params", "sho_not_tunnel");
        hashMap.put("power_deadline_notification_time_net_params", "bat_tunnel");
//        C0122a.m763b(getApplicationContext()).m765a(hashMap).m764a("market://details?id=com.gau.go.launcherex.gowidget.gopowermaster&referrer=utm_source%3Dcom.gtp.nextlauncher.liverpaper.tunnelbate_gopower_Notify%26utm_medium%3DHyperlink%26utm_campaign%3DNotify");
    }

    public void m883b() {
        PendingIntent service = PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), TunnelService.class), 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
        alarmManager.cancel(service);
        alarmManager.setRepeating(0, System.currentTimeMillis() + 300000, 28800000, service);
    }

    public C0177g mo93c() {
        this.f570c = new C0177g(getApplicationContext(), this.f571d, this.f572e);
        this.f570c.m1046a(m873d());
        this.f574g = new C0146a();
        this.f574g.m885a(this.f570c);
        m881g();
        return this.f570c;
    }

    private C0185f m877a(String str) {
        return C0157e.m924a(str, getResources(), getApplicationContext());
    }

    private void m880f() {

    }

    public void onDestroy() {
        super.onDestroy();
//        C0122a.m763b(getApplicationContext()).m766a();
//        if (C0154b.f593a) {
//            C0232l.m1362b("cycle", "销毁服务。。。。。。");
//        } else {
//            C0232l.m1362b("cycle", "销毁服务。。。。。。");
//        }
    }

    private void m881g() {
        m882a().execute(new C0178i(this));
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
//        C0150c.m910a((Context) this).m911a();
    }

    class C0178i implements Runnable {
        final /* synthetic */ TunnelService f773a;

        C0178i(TunnelService tunnelService) {
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
