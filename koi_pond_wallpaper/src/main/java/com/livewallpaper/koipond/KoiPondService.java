package com.livewallpaper.koipond;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.livewallpaper.datareader.SharedPreferenceHelper;
import com.livewallpaper.models.BaitsManager;
import com.livewallpaper.models.KoiManager;
import com.livewallpaper.models.PondsManager;
import com.livewallpaper.resource.ShaderManager;
import com.livewallpaper.resource.TextureMananger;
import com.livewallpaper.settings.IABManager;
import com.livewallpaper.waterpond.MainRenderer;
import com.livewallpaper.waterpond.NotifManager;
import com.livewallpaper.waterpond.PreferencesManager;
import com.yalin.style.engine.GDXWallpaperServiceProxy;

public class KoiPondService extends GDXWallpaperServiceProxy {
    private ApplicationListener listener;

    private static Context mContext;

    public KoiPondService(Context host) {
        super(host);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    private class KoiPondApplication implements ApplicationListener, AndroidWallpaperListener {
        private boolean created;
        private IABManager iabManager;
        private MainRenderer mMainRenderer;
        private ShaderManager shaderManager;
        private TextureMananger textureManager;
        private int viewportHeight;
        private int viewportWidth;

        private KoiPondApplication() {
        }

        public void offsetChange(float xOffsetPercent, float yOffsetPercent, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            this.mMainRenderer.updateSurfaceCameraDestin(xOffsetPercent, yOffsetPercent);
        }

        public void previewStateChange(boolean isPreview) {
            this.mMainRenderer.setPanEnabled(isPreview);
        }

        public void create() {
            this.shaderManager = ShaderManager.getInstance();
            this.textureManager = TextureMananger.getInstance();
            this.iabManager = IABManager.getInstance();
            this.viewportWidth = Gdx.graphics.getWidth();
            this.viewportHeight = Gdx.graphics.getHeight();
            this.mMainRenderer = new MainRenderer();
            this.mMainRenderer.show();
            this.created = true;
        }

        public void dispose() {
            if (this.created) {
                if (this.mMainRenderer != null) {
                    this.mMainRenderer.dispose();
                }
                this.shaderManager.dispose();
                this.textureManager.dispose();
                this.iabManager.dispose();
                SharedPreferenceHelper.getInstance().dispose();
                PondsManager.getInstance().dispose();
                PreferencesManager.getInstance().dispose();
                KoiManager.getInstance().dispose();
                BaitsManager.getInstance().dispose();
                NotifManager.getInstance().dispose();
            }
        }

        public void pause() {
            this.mMainRenderer.pause();
        }

        public void render() {
            this.mMainRenderer.render(Gdx.graphics.getDeltaTime());
        }

        public void resize(int width, int height) {
            if (this.viewportWidth != width || this.viewportHeight != height) {
                this.viewportHeight = height;
                this.viewportWidth = width;
                if (this.mMainRenderer != null) {
                    this.mMainRenderer.dispose();
                }
                this.mMainRenderer = new MainRenderer();
                this.mMainRenderer.resize(width, height);
            }
        }

        public void resume() {
            this.mMainRenderer.resume();
        }

        public void onSurfaceCreated() {
            if (this.created) {
                loseGLContext();
            }
        }

        private void loseGLContext() {
            if (this.mMainRenderer != null) {
                this.mMainRenderer.dispose();
            }
            this.shaderManager.dispose();
            this.shaderManager = ShaderManager.getInstance();
            this.textureManager.invalidateAllTextures();
            this.mMainRenderer = new MainRenderer();
        }
    }

    public void onCreateApplication() {
        super.onCreateApplication();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.getTouchEventsForLiveWallpaper = true;
        this.listener = new KoiPondApplication();
        initialize(this.listener, config);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.listener != null) {
            this.listener.dispose();
        }
    }
}
