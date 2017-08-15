package com.yalin.livewallpaper3d.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Paul on 9/9/2014.
 */
public class Gfx {
    // this class will have all the texture regions that we need in our game
    public static TextureRegion x, o;
    public static TextureRegion playervsplayer, playervsphone;
    public static TextureRegion redcolor, greycolor;
    public static TextureRegion player1_wins, player2_wins, phone_wins, draw;

    public static void load() {
        x = loadTextureRegion("x");
        o = loadTextureRegion("o");
        playervsplayer = loadTextureRegion("playervsplayer");
        playervsphone = loadTextureRegion("playervsphone");
        redcolor = loadTextureRegion("redcolor");
        greycolor = loadTextureRegion("greycolor");
        player1_wins = loadTextureRegion("player1_wins");
        player2_wins = loadTextureRegion("player2_wins");
        phone_wins = loadTextureRegion("phone_wins");
        draw = loadTextureRegion("draw");
    }

    private static TextureRegion loadTextureRegion(String textureName) {
        TextureRegion textureRegion = new TextureRegion(new Texture(textureName + ".png"));
        textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return textureRegion;
    }
}
