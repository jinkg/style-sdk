package com.yalin.livewallpaper3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.yalin.livewallpaper3d.screens.GameScreen;

public class DefaultClass extends Game {
	@Override
	public void create () {
        // se up our game screen
        Gdx.graphics.setContinuousRendering(false);
        setScreen(new GameScreen());
    }


}
