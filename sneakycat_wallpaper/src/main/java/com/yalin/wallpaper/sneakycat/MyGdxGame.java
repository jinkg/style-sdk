package com.yalin.wallpaper.sneakycat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    public void create() {
        this.batch = new SpriteBatch();
        this.img = new Texture("badlogic.jpg");
    }

    public void render() {
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        this.batch.begin();
        this.batch.draw(this.img, 0.0f, 0.0f);
        this.batch.end();
    }
}
