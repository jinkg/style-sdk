package com.livewallpaper.waterpond;

import com.badlogic.gdx.math.Vector3;
import com.ist.lwp.koipond.natives.NativeRipplesRenderer;

public final class RipplesRenderer {
    private final float IDLE_THRESHOLD = 15.0f;
    private float gridSize;
    private boolean idle = false;
    private float idleDuration = 0.0f;
    public final NativeRipplesRenderer nativeRipplesRenderer;

    public RipplesRenderer(MainRenderer mainRenderer) {
        Vector3 rightTop = new Vector3(mainRenderer.rightTop.x, mainRenderer.rightTop.y, 0.0f);
        this.gridSize = 50.0f;
        this.gridSize /= (rightTop.x + rightTop.y) / 2.0f;
        this.nativeRipplesRenderer = new NativeRipplesRenderer((int) this.gridSize, rightTop.x, rightTop.y);
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        this.nativeRipplesRenderer.setUnitWaterSpeed((double) preferencesManager.unitWaterSpeed);
        this.nativeRipplesRenderer.setWaterDamping(preferencesManager.waterDamping);
        this.nativeRipplesRenderer.setWaterTurbulencePercent(preferencesManager.waterTurbulencePercent);
    }

    public final void dispose() {
    }

    public final void render(float deltaTime) {
        this.nativeRipplesRenderer.render(deltaTime);
        if (!this.idle) {
            this.idleDuration += deltaTime;
            if (this.idleDuration > 15.0f) {
                this.idle = true;
                this.nativeRipplesRenderer.setWaterTurbulencePercent(0.0f);
            }
        }
    }

    public final void renderRainDrop() {
        this.nativeRipplesRenderer.renderRainDrop();
    }

    public void wakeUp() {
        this.idleDuration = 0.0f;
        if (this.idle) {
            this.idle = false;
            this.nativeRipplesRenderer.setWaterTurbulencePercent(PreferencesManager.getInstance().waterTurbulencePercent);
        }
    }
}
