package com.livewallpaper.school;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.ist.lwp.koipond.natives.NativeSchoolRenderer;
import com.livewallpaper.resource.TextureMananger;
import com.livewallpaper.waterpond.PreferencesManager;
import com.livewallpaper.waterpond.PreferencesManager.OnPreferenceChangedListener;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

public final class SchoolRenderer implements OnPreferenceChangedListener {
    private static int[] f127x75947dbf = null;
    private static final String TEXTUREKEY = "FISHSCHOOL";
    public final int fishPopulation;
    private NativeSchoolRenderer nativeSchoolRenderer;
    public final float refFishLength;
    private Texture texture = TextureMananger.getInstance().getTexture(TEXTUREKEY);

    static int[] m22x75947dbf() {
        int[] iArr = f127x75947dbf;
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
            f127x75947dbf = iArr;
        }
        return iArr;
    }

    public SchoolRenderer() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        preferencesManager.addPreferenceChangedListener(this);
        this.fishPopulation = preferencesManager.fishPopulationInt;
        this.refFishLength = preferencesManager.fishSizeIndex;
        this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.nativeSchoolRenderer = new NativeSchoolRenderer(this.fishPopulation, this.refFishLength);
        this.nativeSchoolRenderer.setFps(preferencesManager.fps);
        onThemeTextureUpdated();
    }

    public final void dispose() {
        PreferencesManager.getInstance().removePreferenceChangedListener(this);
    }

    public final void render(float deltaTime) {
        this.nativeSchoolRenderer.render(deltaTime);
    }

    public void onThemeTextureUpdated() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        this.nativeSchoolRenderer.setFogDensity(preferencesManager.fishFogDensity);
        this.nativeSchoolRenderer.setFogColor(preferencesManager.waterColor);
        this.nativeSchoolRenderer.setFishScalesColor(preferencesManager.fishScalesColor);
        this.nativeSchoolRenderer.setFishFinsColor(preferencesManager.fishFinsColor);
    }

    public void onPreferenceChanged(PreferenceChangedType type) {
        switch (m22x75947dbf()[type.ordinal()]) {
            case 7:
                this.nativeSchoolRenderer.setFps(PreferencesManager.getInstance().fps);
                return;
            default:
                return;
        }
    }
}
