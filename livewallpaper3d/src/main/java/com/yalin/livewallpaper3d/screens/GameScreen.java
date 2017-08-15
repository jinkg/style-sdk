package com.yalin.livewallpaper3d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.yalin.livewallpaper3d.assets.Gfx;
import com.yalin.livewallpaper3d.game.Box;
import com.yalin.livewallpaper3d.game.TTTInputListener;
import com.yalin.livewallpaper3d.game.TicTacToeLogic;
import com.yalin.livewallpaper3d.rendering.CameraWrapper;

/**
 * Created by Paul on 9/9/2014.
 */
public class GameScreen implements Screen {

    public CameraWrapper cameraWrapper;
    public Sprite playerVsPlayer,playerVsPhone;
    public Box[] boxes=new Box[9];
    // box are positioned like this
    // 0 1 2
    // 3 4 5
    // 6 7 8
    public boolean turn=true;
    //  true - player 1 turn
    //  false - player 2 / phone turn

    public boolean gameType=true;
    // true = player vs player
    // false = player vs phone
    public int gameState=0;
    // 0 = playing
    // 1 = player 1 won
    // 2 = player 2 won
    // 3 = phone won
    // 4 = game draw

    public void newGame(){
        for(Box box:boxes){
            box.reset();
            gameState=0;
        }
        turn=true;
    }
    private void loadBoxes(){
        float yOffset=-15;
        boxes[1]=new Box(240-60,620+yOffset);
        boxes[4]=new Box(240-60,480+yOffset);
        boxes[7]=new Box(240-60,340+yOffset);

        boxes[0]=new Box(240-60-140,620+yOffset);
        boxes[3]=new Box(240-60-140,480+yOffset);
        boxes[6]=new Box(240-60-140,340+yOffset);

        boxes[2]=new Box(240-60+140,620+yOffset);
        boxes[5]=new Box(240-60+140,480+yOffset);
        boxes[8]=new Box(240-60+140,340+yOffset);

    }
    private void loadSprites(){
        playerVsPlayer=new Sprite(Gfx.playervsplayer);
        playerVsPlayer.setSize(161,95);
        playerVsPlayer.setPosition(50,150);

        playerVsPhone=new Sprite(Gfx.playervsphone);
        playerVsPhone.setSize(165,110);
        playerVsPhone.setPosition(270,150);
    }

    @Override
    public void show() {
        Gfx.load();
        cameraWrapper=new CameraWrapper();
        loadBoxes();
        loadSprites();
        Gdx.input.setInputProcessor(new TTTInputListener(this));
    }

    public void checkGameState(){
        int state= TicTacToeLogic.getGameState(boxes);
        if(state==1){
            gameState=1;
        }
        if(state==2){
            if(gameType) {
                gameState = 2;
            }
            else{
                gameState=3;
            }
        }
        if(state==3){
            gameState=4;
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(46/255f, 52/255f, 54/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cameraWrapper.begin();
        for(Box box : boxes){
                box.draw(cameraWrapper.spriteBatch);
        }

        float messageY=290;
        if(gameState==1){
            cameraWrapper.spriteBatch.draw(Gfx.player1_wins,240-252/2f,messageY,252,27);
        }
        if(gameState==2){
            cameraWrapper.spriteBatch.draw(Gfx.player2_wins,240-263/2f,messageY,263,27);
        }
        if(gameState==3){
            cameraWrapper.spriteBatch.draw(Gfx.phone_wins,240-228/2f,messageY,228,26);
        }
        if(gameState==4){
            cameraWrapper.spriteBatch.draw(Gfx.draw,240-108/2f,messageY,108,27);
        }
        playerVsPlayer.draw(cameraWrapper.spriteBatch);
        playerVsPhone.draw(cameraWrapper.spriteBatch);
        cameraWrapper.end();
    }

    @Override
    public void resize(int width, int height) {
        cameraWrapper.resize(width, height);
    }



    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
