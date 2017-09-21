package com.yalin.animal3d.wallpaper.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAdornment {
    float Bgh;
    float Bgw;
    float Bgx;
    float Bgy;
    float[] Scales = new float[]{0.23f, 0.23f};
    int adornmentKind;
    int adornmentKind1 = -1;
    int adornmentSize;
    int adornmentSize1 = -1;
    String[] adornmentkinds = new String[]{"t", "beer"};
    Animation animation;
    TextureAtlas atlas;
    float delta;
    int f81h;
    boolean move;
    TextureRegion region;
    int f82w;
    int w1;

    public MyAdornment() {
    }

    public void draw(SpriteBatch batch) {
        this.delta += Gdx.graphics.getDeltaTime();
        this.region = this.animation.getKeyFrame(this.delta);
        batch.draw(this.region, this.Bgx, this.Bgy, this.Bgw, this.Bgh);
    }

    void setAttri() {
        this.adornmentKind = 0;
        this.move = true;
        if (this.adornmentKind != this.adornmentKind1) {
            if (this.atlas != null) {
                this.atlas.dispose();
                this.atlas = null;
            }
            this.atlas = new TextureAtlas(Gdx.files.internal("adornment/" +
                    this.adornmentkinds[this.adornmentKind]));
            this.animation = null;
            this.animation = new Animation(0.09f, this.atlas.findRegions("t"));
            this.animation.setPlayMode(PlayMode.LOOP);
            this.region = this.animation.getKeyFrame(0.0f);
            this.adornmentSize1 = -1;
        }
        setPicSize(this.Scales[this.adornmentKind], this.region);
        this.adornmentKind1 = this.adornmentKind;
    }

    void setPicSize(float Scale, TextureRegion region) {
        this.f82w = Gdx.graphics.getWidth();
        this.f81h = Gdx.graphics.getHeight();
        this.adornmentSize = 0;
        if (this.adornmentSize != this.adornmentSize1) {
            if (this.adornmentSize == 1) {
                Scale -= 0.06f;
            } else if (this.adornmentSize == 2) {
                Scale += 0.08f;
            }
            if (this.f82w < this.f81h) {
                this.Bgw = ((float) this.f82w) * Scale;
            } else {
                this.Bgw = ((float) this.f81h) * Scale;
            }
            this.Bgh = (this.Bgw * ((float) region.getRegionWidth())) / ((float) region.getRegionHeight());
        }
        if (this.f82w != this.w1) {
            this.Bgx = ((float) (this.f82w / 2)) - (this.Bgw / 2.0f);
            this.Bgy = ((float) (this.f81h / 2)) - (this.Bgh / 2.0f);
        }
        this.w1 = this.f82w;
        this.adornmentSize1 = this.adornmentSize;
    }

    void setPicXY(float x, float y) {
        if (this.move && x <= this.Bgx + this.Bgw && x >= this.Bgx && y <= this.Bgy + this.Bgh && y >= this.Bgy) {
            this.Bgx = x - (this.Bgw / 2.0f);
            this.Bgy = y - (this.Bgh / 2.0f);
        }
    }
}
