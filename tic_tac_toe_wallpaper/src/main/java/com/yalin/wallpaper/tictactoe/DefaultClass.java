package com.yalin.wallpaper.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.yalin.wallpaper.tictactoe.screens.GameScreen;

public class DefaultClass extends Game {
	@Override
	public void create () {
        // se up our game screen
        Gdx.graphics.setContinuousRendering(false);
        setScreen(new GameScreen());
    }


}
