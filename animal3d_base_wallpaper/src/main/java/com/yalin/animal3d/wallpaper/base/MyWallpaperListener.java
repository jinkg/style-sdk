package com.yalin.animal3d.wallpaper.base;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

@SuppressLint({"HandlerLeak"})
public class MyWallpaperListener implements ApplicationListener, AndroidWallpaperListener {
    MoveByAction action;
    MyAdornment adornment;
    int arg00;
    float f410b;
    SpriteBatch batch;
    int bgIndex;
    int bgIndex1 = -1;
    float bgh;
    float bgw;
    float bgx;
    private ParticleEffect effect;
    MyFingerParticle fingerParticle;
    int f411h;
    Image image;
    InputProcessor inputProcessor;
    boolean isFScroll;
    boolean isHaveFinger;
    boolean isHaveParticle;
    boolean isHaveParticle1;
    boolean isPrivew;
    boolean isScroll;
    boolean ishaveadro;
    float f412m;
    private float mPositionX;
    private float mPositionY;
    MoveToAction moveToAction;
    MyParticle myParticle;
    Stage stage;
    Texture texture;
    int version;
    int f413w;
    int w1;

    private String bgFileName;

    public MyWallpaperListener(String bgFileName) {
        this.bgFileName = bgFileName;
    }

    class C05951 implements InputProcessor {
        C05951() {
        }

        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
            if (MyWallpaperListener.this.effect != null) {
                MyWallpaperListener.this.effect.allowCompletion();
            }
            return false;
        }

        public boolean touchDragged(int arg0, int arg1, int arg2) {
            MyWallpaperListener.this.mPositionX = (float) arg0;
            MyWallpaperListener.this.mPositionY = (float) (Gdx.graphics.getHeight() - arg1);
            MyWallpaperListener.this.adornment.setPicXY((float) arg0, MyWallpaperListener.this.mPositionY);
            if (MyWallpaperListener.this.version <= 17 && MyWallpaperListener.this.f413w < MyWallpaperListener.this.f411h && MyWallpaperListener.this.isScroll) {
                if (MyWallpaperListener.this.arg00 != 0) {
                    MyWallpaperListener.this.f412m = (float) (arg0 - MyWallpaperListener.this.arg00);
                }
                MyWallpaperListener.this.arg00 = arg0;
                if (1.0f <= Math.abs(MyWallpaperListener.this.f412m)
                        && Math.abs(MyWallpaperListener.this.f412m) <= 10.0f) {
                    MyWallpaperListener.this.f412m =
                            (MyWallpaperListener.this.f412m * 2.0f) * MyWallpaperListener.this.f410b;
                    MyWallpaperListener.this.action =
                            Actions.moveBy(MyWallpaperListener.this.f412m, 0.0f, 0.5f);
                } else if (10.0f < Math.abs(MyWallpaperListener.this.f412m)
                        && Math.abs(MyWallpaperListener.this.f412m) < 20.0f) {
                    MyWallpaperListener.this.f412m =
                            (MyWallpaperListener.this.f412m + (MyWallpaperListener.this.f412m / 3.0f)) * MyWallpaperListener.this.f410b;
                    MyWallpaperListener.this.action =
                            Actions.moveBy(MyWallpaperListener.this.f412m, 0.0f, 0.7f);
                } else if (20.0f < Math.abs(MyWallpaperListener.this.f412m) && Math.abs(MyWallpaperListener.this.f412m) <= 40.0f) {
                    MyWallpaperListener.this.f412m = (MyWallpaperListener.this.f412m + (MyWallpaperListener.this.f412m / 4.0f)) * MyWallpaperListener.this.f410b;
                    MyWallpaperListener.this.action = Actions.moveBy(MyWallpaperListener.this.f412m, 0.0f, 0.8f);
                } else if (40.0f >= Math.abs(MyWallpaperListener.this.f412m) || Math.abs(MyWallpaperListener.this.f412m) > 60.0f) {
                    MyWallpaperListener.this.f412m = 0.0f;
                    MyWallpaperListener.this.action = Actions.moveBy(0.0f, 0.0f, 0.2f);
                } else {
                    MyWallpaperListener myWallpaperListener = MyWallpaperListener.this;
                    myWallpaperListener.f412m *= MyWallpaperListener.this.f410b;
                    MyWallpaperListener.this.action = Actions.moveBy(MyWallpaperListener.this.f412m, 0.0f, 0.9f);
                }
                if ((-(MyWallpaperListener.this.bgw - ((float) MyWallpaperListener.this.f413w))) + 20.0f < MyWallpaperListener.this.image.getX() + MyWallpaperListener.this.f412m && MyWallpaperListener.this.image.getX() + MyWallpaperListener.this.f412m < -20.0f) {
                    MyWallpaperListener.this.image.addAction(MyWallpaperListener.this.action);
                }
            }
            return false;
        }

        public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
            if (MyWallpaperListener.this.effect != null) {
                MyWallpaperListener.this.effect.start();
            }
            MyWallpaperListener.this.mPositionX = (float) arg0;
            MyWallpaperListener.this.mPositionY = (float) (Gdx.graphics.getHeight() - arg1);
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

    public void create() {
        this.myParticle = new MyParticle();
        this.version = VERSION.SDK_INT;
        if (this.version <= 17) {
            this.isFScroll = true;
        }
        this.effect = new ParticleEffect();
        this.fingerParticle = new MyFingerParticle(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.effect = this.fingerParticle.getParticleEffect();
        this.adornment = new MyAdornment();
        int mw = Gdx.graphics.getWidth();
        int mh = Gdx.graphics.getHeight();
        if (mw < mh) {
            if (mh <= GL20.GL_INVALID_ENUM && mh < 1920) {
                this.f410b = 0.6f;
            }
            if (mh < GL20.GL_INVALID_ENUM) {
                this.f410b = 0.4f;
            }
            if (mh > 1920) {
                this.f410b = 1.5f;
            }
            if (mh == 1920) {
                this.f410b = 0.8f;
            }
        } else {
            if (mw <= GL20.GL_INVALID_ENUM && mh < 1920) {
                this.f410b = 0.6f;
            }
            if (mw < GL20.GL_INVALID_ENUM) {
                this.f410b = 0.45f;
            }
            if (mw > 1920) {
                this.f410b = 1.5f;
            }
            if (mw == 1920) {
                this.f410b = 0.8f;
            }
        }
        this.inputProcessor = new C05951();
        Gdx.input.setInputProcessor(this.inputProcessor);
    }

    public void resize(int width, int height) {
        this.f413w = width;
        this.f411h = height;
        this.isHaveFinger = true;
        this.ishaveadro = true;
        if (this.f413w != this.w1) {
            initStageBach();
        }
        setBG();
        setParticle();
        this.adornment.setAttri();
        this.w1 = this.f413w;
    }

    void initStageBach() {
        this.isHaveParticle = true;
        if (this.stage != null) {
            this.stage.dispose();
            this.stage = null;
        }
        if (this.batch != null) {
            this.batch.dispose();
            this.batch = null;
        }
        this.stage = new Stage();
        this.batch = new SpriteBatch();
        if (this.isHaveParticle) {
            this.isHaveParticle1 = false;
        }
    }

    void setBG() {
        this.isScroll = true;
        setMyBGIndex();
        if (this.bgw <= ((float) (this.f413w + 60))) {
            this.isScroll = false;
        }
        if (!(this.isScroll || this.image == null || this.f413w >= this.f411h) || this.isPrivew) {
            this.image.setX((-(this.bgw - ((float) this.f413w))) / 2.0f);
        }
    }

    void setMyBGIndex() {
        this.bgIndex = 0;
        if (!(this.bgIndex == this.bgIndex1 && this.f413w == this.w1)) {
            this.image = null;
            if (this.texture != null) {
                this.texture.dispose();
                this.texture = null;
            }
            this.texture = new Texture(Gdx.files.internal("bg/" +
                    bgFileName + ".jpg"));
            this.image = new Image(this.texture);
            setBgAttri();
        }
        this.bgIndex1 = this.bgIndex;
    }

    void setBgAttri() {
        if (this.f413w < this.f411h) {
            this.bgh = (float) this.f411h;
            this.bgw = this.bgh * (((float) this.texture.getWidth()) / ((float) this.texture.getHeight()));
            if (this.bgw < ((float) this.f413w)) {
                this.bgw = (float) this.f413w;
                this.bgh = this.bgw * (((float) this.texture.getHeight()) / ((float) this.texture.getWidth()));
            }
        } else {
            this.bgw = (float) this.f413w;
            this.bgh = this.bgw * (((float) this.texture.getHeight()) / ((float) this.texture.getWidth()));
            this.image.setX(0.0f);
            this.image.setY((-(this.bgh - ((float) this.f411h))) / 2.0f);
            this.isScroll = false;
        }
        this.image.setWidth(this.bgw);
        this.image.setHeight(this.bgh);
        if (this.stage.getActors().size == 0) {
            this.stage.addActor(this.image);
        } else {
            this.stage.getActors().set(0, this.image);
        }
    }

    void setParticle() {
        this.isHaveParticle = true;
        if (this.isHaveParticle != this.isHaveParticle1) {
            if (!this.isHaveParticle) {
                this.stage.getActors().removeValue(this.myParticle, true);
            } else if (!this.stage.getActors().contains(this.myParticle, true)) {
                this.stage.addActor(this.myParticle);
            }
        }
        if (this.isHaveParticle) {
            this.myParticle.CheckParticleAttributes();
        }
        this.isHaveParticle1 = this.isHaveParticle;
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(16384);
        if (this.isFScroll && this.isScroll && this.f413w < this.f411h) {
            if (this.image.getX() < (-(this.bgw - ((float) this.f413w))) + 20.0f) {
                this.image.setX((-(this.bgw - ((float) this.f413w))) + 20.0f);
            } else if (this.image.getX() > -20.0f) {
                this.image.setX(-20.0f);
            }
        }
        this.stage.act();
        this.stage.draw();
        if (this.isHaveFinger || this.ishaveadro) {
            this.effect.setPosition(this.mPositionX, this.mPositionY);
            this.batch.begin();
            if (this.isHaveFinger) {
                this.effect.draw(this.batch, Gdx.graphics.getDeltaTime());
            }
            this.batch.end();
        }
    }

    public void offsetChange(float arg0, float arg1, float arg2, float arg3, int arg4, int arg5) {
        if (!this.isFScroll && this.version > 17 && this.isScroll && arg0 <= 1.0f && this.f413w < this.f411h && this.image != null && !this.isPrivew) {
            this.bgx = (-arg0) * (this.bgw - ((float) this.f413w));
            this.moveToAction = Actions.moveTo(this.bgx, 0.0f, 0.7f);
            this.image.addAction(this.moveToAction);
        }
    }

    public void previewStateChange(boolean arg0) {
        this.isPrivew = arg0;
        if (arg0 && this.f413w < this.f411h) {
            this.image.setX((-(this.bgw - ((float) this.f413w))) / 2.0f);
        }
    }

    public void resume() {
    }

    public void dispose() {
    }

    public void pause() {
        System.out.println("pause()");
    }
}
