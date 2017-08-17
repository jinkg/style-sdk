package com.yalin.wallpaper.nightfal_windmill;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class ImageObject extends Actor {
    public TextureRegion image;
    public boolean show = true;

    public void draw(Batch batch, float parentAlpha) {
        if (this.show) {
            batch.enableBlending();
            batch.draw(this.image, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), false);
        }
    }
}
