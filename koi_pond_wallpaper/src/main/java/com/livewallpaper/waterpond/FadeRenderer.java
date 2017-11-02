package com.livewallpaper.waterpond;

import com.ist.lwp.koipond.natives.NativeFadeRenderer;
import com.livewallpaper.resource.TextureMananger;

public class FadeRenderer {
    private NativeFadeRenderer nativeFadeRenderer = new NativeFadeRenderer();

    public final void dispose() {
    }

    public final void render(float deltaTime) {
        this.nativeFadeRenderer.render(deltaTime);
        if (this.nativeFadeRenderer.isFaded()) {
            PreferencesManager.getInstance().updateThemePrefs();
            TextureMananger.getInstance().updateThemeTextures();
        }
    }

    public final void animate() {
        this.nativeFadeRenderer.animate();
    }
}
