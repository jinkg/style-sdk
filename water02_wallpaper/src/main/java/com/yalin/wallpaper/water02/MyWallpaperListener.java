package com.yalin.wallpaper.water02;

import android.annotation.SuppressLint;

import com.badlogic1.gdx.ApplicationListener;
import com.badlogic1.gdx.Gdx;
import com.badlogic1.gdx.InputProcessor;
import com.badlogic1.gdx.graphics.GL20;
import com.badlogic1.gdx.graphics.g2d.ParticleEffect;
import com.badlogic1.gdx.graphics.g2d.SpriteBatch;

@SuppressLint({"HandlerLeak"})
public class MyWallpaperListener implements ApplicationListener {
    boolean Clicked;
    SpriteBatch batch;
    private ParticleEffect effect;
    GL20 gl;
    int f401h;
    InputProcessor inputProcessor;
    boolean isHaveParticle;
    boolean isSlow;
    MyParticle myParticle;
    WaterRipples myWaterRipples1;
    int f402w;
    int w1;
    int f403x;
    int x1;
    int f404y;
    int y1;

    class C06201 implements InputProcessor {
        C06201() {
        }

        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
            MyWallpaperListener.this.Clicked = false;
            MyWallpaperListener.this.isSlow = false;
            return false;
        }

        public boolean touchDragged(int arg0, int arg1, int arg2) {
            if (MyWallpaperListener.this.Clicked) {
                MyWallpaperListener.this.f403x = arg0;
                MyWallpaperListener.this.f404y = arg1;
                if (Math.abs(MyWallpaperListener.this.f403x - MyWallpaperListener.this.x1) <= 12 && Math.abs(MyWallpaperListener.this.f404y - MyWallpaperListener.this.y1) <= 12) {
                    MyWallpaperListener.this.isSlow = true;
                } else if (MyWallpaperListener.this.myWaterRipples1 != null) {
                    MyWallpaperListener.this.myWaterRipples1.touchScreen(arg0, arg1);
                }
                MyWallpaperListener.this.x1 = MyWallpaperListener.this.f403x;
                MyWallpaperListener.this.y1 = MyWallpaperListener.this.f404y;
            }
            return false;
        }

        public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
            if (!(MyWallpaperListener.this.Clicked || MyWallpaperListener.this.myWaterRipples1 == null)) {
                MyWallpaperListener.this.myWaterRipples1.touchScreen(arg0, arg1);
                MyWallpaperListener.this.Clicked = true;
            }
            return false;
        }

        public boolean scrolled(int arg0) {
            return false;
        }

        public boolean mouseMoved(int arg0, int arg1) {
            return false;
        }

        public boolean keyUp(int arg0) {
            return false;
        }

        public boolean keyTyped(char arg0) {
            return false;
        }

        public boolean keyDown(int arg0) {
            return false;
        }
    }

    public MyWallpaperListener() {
    }

    public void create() {
        this.myParticle = new MyParticle();
        this.effect = this.myParticle.getPE();
        this.f402w = Gdx.graphics.getWidth();
        this.f401h = Gdx.graphics.getHeight();
        this.myWaterRipples1 = new WaterRipples();
        this.inputProcessor = new C06201();
        Gdx.input.setInputProcessor(this.inputProcessor);
    }

    public void resize(int width, int height) {
        this.f402w = width;
        this.f401h = height;
        if (this.f402w != this.w1) {
            initStageBach();
        }
        this.myWaterRipples1.reset(width, height);
        setParticle();
    }

    void initStageBach() {
        this.isHaveParticle = true;
        if (this.batch != null) {
            this.batch.dispose();
            this.batch = null;
        }
        this.batch = new SpriteBatch();
    }

    void setParticle() {
        this.isHaveParticle = true;
        this.myParticle.CheckParticleAttributes();
    }

    public void render() {
        this.gl = Gdx.graphics.getGL20();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(16384);
        this.myWaterRipples1.render(false);
        if (this.isHaveParticle) {
            this.myParticle.setPosion(this.effect, this.f402w, this.f401h, this.myParticle.getX(), this.myParticle.getY(), this.myParticle.particleKind);
            this.batch.begin();
            this.effect.draw(this.batch, Gdx.graphics.getDeltaTime());
            this.batch.end();
        }
    }

    public void resume() {
        this.myWaterRipples1.RandomWaterRipple(true);
    }

    public void dispose() {
    }

    public void pause() {
        this.myWaterRipples1.RandomWaterRipple(false);
    }
}
