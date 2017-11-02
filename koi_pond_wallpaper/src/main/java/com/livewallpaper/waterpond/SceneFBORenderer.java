package com.livewallpaper.waterpond;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class SceneFBORenderer extends FBOWrapper {
    private final MainRenderer mainRenderer;

    public SceneFBORenderer(MainRenderer mainRenderer, float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
        this.mainRenderer = mainRenderer;
        create(Format.RGB565);
    }

    public final void dispose() {
        super.dispose();
    }

    public final void render(float deltaTime) {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        GL20 gl20 = Gdx.graphics.getGL20();
        begin();
        gl20.glClear(16640);
        this.mainRenderer.bottomRenderer.render();
        this.mainRenderer.refractionsRenderer.render();
        if (preferencesManager.schoolEnabled) {
            this.mainRenderer.schoolRenderer.render(deltaTime);
        }
        this.mainRenderer.koi3dRenderer.render(deltaTime);
        this.mainRenderer.baitsRenderer.render();
        if (preferencesManager.showFloatage) {
            this.mainRenderer.plantsRenderer.render(deltaTime);
        }
        end();
    }
}
