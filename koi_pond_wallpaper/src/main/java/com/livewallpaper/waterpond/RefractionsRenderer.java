package com.livewallpaper.waterpond;

import com.ist.lwp.koipond.natives.NativeRefractionsRenderer;

public class RefractionsRenderer {
    public NativeRefractionsRenderer nativeRefractionsRenderer = new NativeRefractionsRenderer();

    public RefractionsRenderer() {
        onThemeTextureUpdated();
    }

    public final void render() {
        this.nativeRefractionsRenderer.render();
    }

    public void onThemeTextureUpdated() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        this.nativeRefractionsRenderer.setFogDensityAtBottomNeg(preferencesManager.fogDensityAtBottomNeg);
        this.nativeRefractionsRenderer.setEsToLightDir(preferencesManager.lightInfo.esToLightDir);
        this.nativeRefractionsRenderer.setLightDiffuse(preferencesManager.lightInfo.lightDiffuse);
        this.nativeRefractionsRenderer.setFogColorAtBottom(preferencesManager.fogColorAtBottom);
    }

    public void dispose() {
    }
}
