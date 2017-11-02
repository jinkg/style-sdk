package com.livewallpaper.waterpond;

public class RainDropsRenderer {
    private MainRenderer mainRenderer;
    private long rainTimeStamp = 0;

    public RainDropsRenderer(MainRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
    }

    public void render() {
        if (PreferencesManager.getInstance().rainyMode) {
            long now = System.currentTimeMillis();
            if (this.rainTimeStamp + 1500 < now) {
                this.mainRenderer.ripplesRenderer.renderRainDrop();
                this.rainTimeStamp = now;
            }
        }
    }
}
