package com.livewallpaper.waterpond;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.ist.lwp.koipond.natives.NativeBottomRenderer;
import com.livewallpaper.resource.TextureMananger;

public class BottomRenderer {
    private NativeBottomRenderer nativeBottomRenderer = new NativeBottomRenderer();
    private Texture texture;

    public BottomRenderer() {
        onThemeTextureUpdated();
    }

    public final void render() {
        this.nativeBottomRenderer.render();
    }

    public void onThemeTextureUpdated() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        this.texture = TextureMananger.getInstance().getTexture("BG");
        this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.nativeBottomRenderer.setFogDensityAtBottomNeg(preferencesManager.fogDensityAtBottomNeg);
        this.nativeBottomRenderer.setFogColorAtBottom(preferencesManager.fogColorAtBottom);
    }

    public final void dispose() {
    }
}
