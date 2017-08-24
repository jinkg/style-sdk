package com.yalin.wallpaper.lazypanda;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

public class LiveWallpaperScreen implements Screen {
    TextureRegion background2;
    SpriteBatch batcher;
    TextureRegion branch;
    ImageObject branchObject;
    OrthographicCamera camera;
    TextureRegion ear;
    float earElapsed = 0.0f;
    ImageObject earObject;
    float earTime = 0.2f;
    float eyeElapsed = 0.0f;
    float eyeTime = 4.0f;
    TextureRegion eyes;
    Game game;
    boolean isLandscape = false;
    float lastTouch = 0.0f;
    TextureRegion mouth;
    float mouthElapsed = 0.0f;
    float mouthTime = 3.0f;
    Viewport myViewport;
    TextureRegion panda;
    Particle[] particles = new Particle[20];
    TextureRegion petal;
    TextureRegion petal2;
    TextureRegion petal3;
    ImageObject petalObject;
    ImageObject petalObject2;
    ImageObject petalObject3;
    TextureRegion[] petals = new TextureRegion[3];
    MyPreferences prefs;
    boolean showEyes = false;
    boolean showMouth = false;
    float size = 0.0f;
    Texture textureBg2;
    Texture textureBranch;
    Texture textureEar;
    Texture textureEyes;
    Texture textureMouth;
    Texture texturePanda;
    Texture texturePetal;
    Texture texturePetal2;
    Texture texturePetal3;
    TweenManager tweenManager;
    boolean twitchEar = false;

    public LiveWallpaperScreen(Game game) {
        float width;
        this.game = game;
        this.tweenManager = new TweenManager();
        if (this.isLandscape) {
            width = (float) Gdx.graphics.getWidth();
        } else {
            width = (float) Gdx.graphics.getHeight();
        }
        this.size = width;
        this.camera = new OrthographicCamera();
        this.myViewport = new FillViewport(1280.0f, 1280.0f, this.camera);
        this.myViewport.apply();
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        this.textureBg2 = new Texture("background2.jpg");
        this.textureBg2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.background2 = new TextureRegion(this.textureBg2);
        this.texturePanda = new Texture("panda.png");
        this.texturePanda.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.panda = new TextureRegion(this.texturePanda, 0, 0, 667, 765);
        this.textureEyes = new Texture("eyes.png");
        this.textureEyes.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.eyes = new TextureRegion(this.textureEyes, 0, 0, 373, 318);
        this.textureMouth = new Texture("mouth.png");
        this.textureMouth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.mouth = new TextureRegion(this.textureMouth, 0, 0, 377, 386);
        this.textureBranch = new Texture("branch.png");
        this.textureBranch.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.branch = new TextureRegion(this.textureBranch, 0, 0, (int) GL20.GL_INVALID_ENUM, 524);
        this.branchObject = new ImageObject();
        this.branchObject.image = this.branch;
        this.branchObject.setBounds(0.0f, 0.0f, (float) this.branch.getRegionHeight(), (float) this.branch.getRegionWidth());
        this.branchObject.setPosition(0.0f, 0.0f);
        this.textureEar = new Texture("ear.png");
        this.textureEar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.ear = new TextureRegion(this.textureEar, 0, 0, 140, 297);
        this.earObject = new ImageObject();
        this.earObject.image = this.ear;
        this.earObject.setBounds(0.0f, 0.0f, (float) this.ear.getRegionHeight(), (float) this.ear.getRegionWidth());
        this.earObject.setPosition(0.0f, 0.0f);
        this.texturePetal = new Texture("petal.png");
        this.texturePetal.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.petal = new TextureRegion(this.texturePetal, 0, 0, 64, 102);
        this.petalObject = new ImageObject();
        this.petalObject.image = this.petal;
        this.petalObject.setBounds(0.0f, 0.0f, (float) this.petal.getRegionHeight(), (float) this.petal.getRegionWidth());
        this.texturePetal2 = new Texture("petal2.png");
        this.texturePetal2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.petal2 = new TextureRegion(this.texturePetal2, 0, 0, 48, 69);
        this.petalObject2 = new ImageObject();
        this.petalObject2.image = this.petal2;
        this.petalObject2.setBounds(0.0f, 0.0f, (float) this.petal2.getRegionHeight(), (float) this.petal2.getRegionWidth());
        this.texturePetal3 = new Texture("petal3.png");
        this.texturePetal3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.petal3 = new TextureRegion(this.texturePetal3, 0, 0, 43, 73);
        this.petalObject3 = new ImageObject();
        this.petalObject3.image = this.petal3;
        this.petalObject3.setBounds(0.0f, 0.0f, (float) this.petal3.getRegionHeight(), (float) this.petal3.getRegionWidth());
        this.petals[0] = this.petal;
        this.petals[1] = this.petal2;
        this.petals[2] = this.petal3;
        this.prefs = new MyPreferences(this.background2);
        for (int i = 0; i < 20; i++) {
            Particle p = new Particle();
            p.images = this.petals;
            p.myTweenManager = this.tweenManager;
            p.Reset();
            this.particles[i] = p;
        }
        this.batcher = new SpriteBatch();
        Tween.to(this.branchObject, 4, 7.0f).target(5.0f).repeatYoyo(-1, 0.0f).start(this.tweenManager);
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    private void draw(float delta) {
        int i;
        this.tweenManager.update(delta);
        this.lastTouch += delta;
        if (this.showMouth) {
            this.mouthElapsed += delta;
            if (this.mouthElapsed >= this.mouthTime) {
                this.showMouth = false;
            }
        }
        if (this.showEyes) {
            this.eyeElapsed += delta;
            if (this.eyeElapsed >= this.eyeTime) {
                this.showEyes = false;
            }
        }
        if (this.twitchEar) {
            this.earElapsed += delta;
            if (this.earElapsed >= this.earTime) {
                this.twitchEar = false;
            }
        }
        this.camera.update();
        if (Gdx.input.justTouched()) {
            if (this.prefs.reactsToTouch) {
                switch (MathUtils.random(3)) {
                    case 0:
                        this.showMouth = true;
                        this.mouthElapsed = 0.0f;
                        break;
                    case 1:
                        this.showEyes = true;
                        this.eyeElapsed = 0.0f;
                        break;
                    case 2:
                        Tween.to(this.earObject, 4, 0.1f).target(15.0f).repeatYoyo(3, 0.0f).ease(Linear.INOUT).start(this.tweenManager);
                        break;
                }
            }

            this.lastTouch = 0.0f;
        }
        this.batcher.setProjectionMatrix(this.camera.combined);
        this.batcher.enableBlending();
        this.batcher.begin();
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batcher.draw(this.prefs.bg, 0.0f, 0.0f, this.camera.viewportWidth, this.camera.viewportHeight);
        this.batcher.draw(this.branchObject.image, 100.0f, 780.0f, this.branchObject.getOriginX(), this.branchObject.getOriginY(), (float) this.branchObject.image.getRegionWidth(), (float) this.branchObject.image.getRegionHeight(), 1.0f, 1.0f, this.branchObject.getRotation());
        for (i = 0; i < this.prefs.numberOfPetals; i++) {
            if (this.particles[i].layer != 1) {
                this.batcher.draw(this.particles[i].GetImage(), this.particles[i].getX(), this.particles[i].getY(), this.particles[i].getOriginX(), this.particles[i].getOriginY(), (float) this.particles[i].GetImage().getRegionWidth(), (float) this.particles[i].GetImage().getRegionHeight(), 1.0f, 1.0f, this.particles[i].getRotation());
            }
        }
        this.batcher.draw(this.earObject.image, 830.0f, 750.0f, this.earObject.getOriginX(), this.earObject.getOriginY(), (float) this.earObject.image.getRegionWidth(), (float) this.earObject.image.getRegionHeight(), 1.0f, 1.0f, this.earObject.getRotation());
        this.batcher.draw(this.panda, 300.0f, 270.0f);
        if (this.showEyes) {
            this.batcher.draw(this.eyes, 593.0f, 720.0f);
        }
        if (this.showMouth) {
            this.batcher.draw(this.mouth, 590.0f, 655.0f);
        }
        for (i = 0; i < this.prefs.numberOfPetals; i++) {
            if (this.particles[i].layer != -1) {
                this.batcher.draw(this.particles[i].GetImage(), this.particles[i].getX(), this.particles[i].getY(), this.particles[i].getOriginX(), this.particles[i].getOriginY(), (float) this.particles[i].GetImage().getRegionWidth(), (float) this.particles[i].GetImage().getRegionHeight(), 1.0f, 1.0f, this.particles[i].getRotation());
            }
        }
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batcher.disableBlending();
        this.batcher.end();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
    }

    private void update(float delta) {
    }

    public void render(float delta) {
        update(delta);
        draw(delta);
    }

    public void resize(int width, int height) {
        this.myViewport.update(width, height);
        if (width > height) {
            this.isLandscape = true;
        } else {
            this.isLandscape = false;
        }
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        if (this.isLandscape) {
            this.size = (float) width;
        } else {
            this.size = (float) height;
        }
    }

    public void resume() {
    }

    public void show() {
    }
}
