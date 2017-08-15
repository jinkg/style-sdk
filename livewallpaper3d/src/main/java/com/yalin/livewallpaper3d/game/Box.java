package com.yalin.livewallpaper3d.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.yalin.livewallpaper3d.assets.Gfx;

/**
 * Created by Paul on 9/9/2014.
 */
public class Box {
    private static Rectangle rectangle=new Rectangle();
    private float x,y;
    public char value=' ';
    public boolean red=false;
    // the value can be x,o, or ' '

    public Box(float x, float y) {
        this.x = x;
        this.y = y;

    }
    public boolean touched(float x,float y){
        rectangle.set(this.x,this.y,120,120);
        return rectangle.contains(x,y);
    }
    public void reset(){
        value=' ';
        red=false;
    }

    public void draw(SpriteBatch spriteBatch){
        spriteBatch.draw(Gfx.greycolor,x,y,120,120);
        if(value=='x'){
            float width= 76 ,height= 93;
            spriteBatch.draw(Gfx.x,x+60-width/2f,y+60-height/2f,width,height);
        }
        if(value=='o'){
            float width= 102 ,height= 96;
            spriteBatch.draw(Gfx.o,x+60-width/2f,y+60-height/2f,width,height);
        }
        if(red){
            spriteBatch.draw(Gfx.redcolor,x,y,120,120);
        }
    }
}
