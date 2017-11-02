package com.livewallpaper.plants;

import com.ist.lwp.koipond.natives.NativePlantsRenderer;
import com.livewallpaper.waterpond.PreferencesManager;

public class PlantsRenderer {
    private NativePlantsRenderer nativePlantsRenderer = new NativePlantsRenderer();

    public PlantsRenderer() {
        onThemeTextureUpdated();
    }

    public void render(float deltaTime) {
        this.nativePlantsRenderer.render(deltaTime);
    }

    public void onThemeTextureUpdated() {
        this.nativePlantsRenderer.setPlantsSizeScale(PreferencesManager.getInstance().plantSizeScale);
    }
}
