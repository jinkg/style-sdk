package com.livewallpaper.waterpond;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.ist.lwp.koipond.natives.NativeSurfaceRenderer;
import com.livewallpaper.resource.TextureMananger;
import com.livewallpaper.waterpond.PreferencesManager.OnPreferenceChangedListener;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

public final class SurfaceRenderer implements OnPreferenceChangedListener {
    private static int[] f132x75947dbf;
    private MainRenderer mMainRenderer;
    private final NativeSurfaceRenderer nativeSurfaceRenderer = new NativeSurfaceRenderer();
    private Texture texture;

    static int[] m27x75947dbf() {
        int[] iArr = f132x75947dbf;
        if (iArr == null) {
            iArr = new int[PreferenceChangedType.values().length];
            try {
                iArr[PreferenceChangedType.CURRENTTHEME.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGENABLE.ordinal()] = 10;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGLOADED.ordinal()] = 11;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PreferenceChangedType.FEEDKOI.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PreferenceChangedType.GYROENABLE.ordinal()] = 9;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[PreferenceChangedType.KOISET.ordinal()] = 12;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[PreferenceChangedType.PAGEPAN.ordinal()] = 13;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[PreferenceChangedType.POWERSAVER.ordinal()] = 7;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[PreferenceChangedType.RAINYMODE.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[PreferenceChangedType.SHOWFLOATAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[PreferenceChangedType.SHOWREFLECTION.ordinal()] = 1;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[PreferenceChangedType.SHOWSCHOOL.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[PreferenceChangedType.TOUCHPAN.ordinal()] = 6;
            } catch (NoSuchFieldError e13) {
            }
            f132x75947dbf = iArr;
        }
        return iArr;
    }

    public SurfaceRenderer(MainRenderer mainRenderer) {
        this.mMainRenderer = mainRenderer;
        PreferencesManager.getInstance().addPreferenceChangedListener(this);
        onThemeTextureUpdated();
    }

    public final void render() {
        this.mMainRenderer.sceneFBORenderer.getColorBufferTexture().bind(0);
        this.nativeSurfaceRenderer.render();
    }

    public void onThemeTextureUpdated() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        this.texture = TextureMananger.getInstance().getTexture("ENV");
        this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);
        this.nativeSurfaceRenderer.setFogDensity(preferencesManager.fogDensity);
        this.nativeSurfaceRenderer.setEnvReflectionsPercent(preferencesManager.envReflectionsPercent);
        this.nativeSurfaceRenderer.setFogColor(preferencesManager.waterColor);
        this.nativeSurfaceRenderer.setLightAmbient(preferencesManager.lightInfo.lightAmbient);
        this.nativeSurfaceRenderer.setLightDiffuse(preferencesManager.lightInfo.lightDiffuse);
        this.nativeSurfaceRenderer.setEsToLightDir(preferencesManager.lightInfo.esToLightDir);
    }

    public void dispose() {
        PreferencesManager.getInstance().removePreferenceChangedListener(this);
    }

    public void onPreferenceChanged(PreferenceChangedType type) {
        switch (m27x75947dbf()[type.ordinal()]) {
            case 1:
                this.nativeSurfaceRenderer.setEnvReflectionsPercent(PreferencesManager.getInstance().envReflectionsPercent);
                return;
            default:
                return;
        }
    }
}
