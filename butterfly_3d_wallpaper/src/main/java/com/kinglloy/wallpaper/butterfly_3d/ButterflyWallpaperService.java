package com.kinglloy.wallpaper.butterfly_3d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.wallpaper.WallpaperService;
import android.util.Log;

import com.kinglloy.a3d_base.MyRenderer;
import com.kinglloy.a3d_base.Settings;
import com.yalin.style.engine.GLWallpaperServiceProxy;


public class ButterflyWallpaperService extends GLWallpaperServiceProxy {
    Context f2849a = this;

    public ButterflyWallpaperService(Context host) {
        super(host);
    }

    class C0853a extends GLActiveEngine {
        C0854b f2844a;
        MyRenderer f2845b;

        public C0853a() {
            super();
            Settings settings = new Settings();
            settings.layer1 = R.drawable.back;
            settings.layer2 = R.drawable.a01;
            settings.layer3 = R.drawable.a02;

            this.f2845b = new MyRenderer(f2849a, settings);
            setRenderer(this.f2845b);
            setRenderMode(1);
            IntentFilter intentFilter = new IntentFilter("com.teslacoilsw.widgetlocker.intent.UNLOCKED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            this.f2844a = new C0854b(ButterflyWallpaperService.this, this.f2845b);
            registerReceiver(this.f2844a, intentFilter);
        }

        public void onPause() {
            super.onPause();
            this.f2845b.m4193f();
        }

        public void onResume() {
            super.onResume();
            this.f2845b.m4194g();
        }

        public void onDestroy() {
            super.onDestroy();
            if (this.f2845b != null) {
                f2849a.unregisterReceiver(this.f2844a);
                this.f2845b.m4185a();
                this.f2845b = null;
            }
        }
    }

    public class C0854b extends BroadcastReceiver {
        MyRenderer f2847a;
        final /* synthetic */ ButterflyWallpaperService f2848b;

        public C0854b(ButterflyWallpaperService myWallpaperService, MyRenderer myRenderer) {
            this.f2848b = myWallpaperService;
            this.f2847a = myRenderer;
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    this.f2847a.f2944p = false;
                    this.f2847a.f2943o = true;
                } else if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                    Log.d("XXX", "In Method:  ACTION_USER_PRESENT");
                    this.f2847a.f2944p = false;
                    this.f2847a.f2943o = true;
                } else if (intent.getAction().equals("com.teslacoilsw.widgetlocker.intent.UNLOCKED")) {
                    this.f2847a.f2944p = false;
                }
            }
        }
    }

    public void onCreate() {
        Log.d("MyWallpaperService", "on create");
        super.onCreate();
    }

    public WallpaperService.Engine onCreateEngine() {
        return new C0853a();
    }

    public void onDestroy() {
        Log.d("MyWallpaperService", "on destroy");
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        Log.d("MyWallpaperService", "rebind");
        super.onRebind(intent);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("MyWallpaperService", "on start");
        return super.onStartCommand(intent, i, i2);
    }
}
