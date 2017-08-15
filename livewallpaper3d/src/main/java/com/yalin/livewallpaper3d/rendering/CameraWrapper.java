package com.yalin.livewallpaper3d.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Paul on 9/9/2014.
 */
public class CameraWrapper {
    // a wrapper for a spritebatch,viewport and camera
    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public Viewport viewport;

    public CameraWrapper() {
        camera=new OrthographicCamera(480,800);
        camera.position.set(240,400,0);
        viewport=new ExtendViewport(480,800,camera);
        spriteBatch=new SpriteBatch();
    }
    public void resize(int screenWidth,int screenHeight){
        viewport.update(screenWidth,screenHeight);
    }
    public void begin(){
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
    }
    public void end(){
        spriteBatch.end();
    }
}
