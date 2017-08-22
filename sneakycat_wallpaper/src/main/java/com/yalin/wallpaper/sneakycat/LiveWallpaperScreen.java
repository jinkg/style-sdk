package com.yalin.wallpaper.sneakycat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;

public class LiveWallpaperScreen implements Screen {
    float appearElapsed = 0.0f;
    float appearLength = 0.0f;
    float appearLengthElapsed = 0.0f;
    float appearSpeed = 0.5f;
    float appearSpeed2 = 0.1f;
    float appearTime = 0.0f;
    float appearTimeMax = 1.0f;
    float appearTimeMin = 0.5f;
    boolean appearing = true;
    TextureRegion background;
    SpriteBatch batcher;
    float blinkElapseMax = 10.0f;
    float blinkElapseMin = 5.0f;
    float blinkElapseTime = 0.0f;
    float blinkElapsed = 0.0f;
    float blinkTime = 0.08f;
    boolean blinking = false;
    OrthographicCamera camera;
    TextureRegion cat;
    Group catGroup;
    ImageObject catObject;
    ArrayList<SpawnPoint> catSpawnPoints = new ArrayList();
    float disappearSpeed = 0.15f;
    TextureRegion earLeft;
    ImageObject earLeftObject;
    TextureRegion earRight;
    ImageObject earRightObject;
    float earRotateSpeed = 1.0f;
    TextureRegion eye;
    ImageObject eyeObjectLeft;
    ImageObject eyeObjectRight;
    TextureRegion eyeSurprised;
    float eyesClosedElapsed = 0.0f;
    TextureRegion eyesSmug;
    ImageObject eyesSmugObject;
    boolean first = true;
    Game game;
    boolean isLandscape = false;
    float lastTouch = 0.0f;
    Viewport myViewport;
    MyPreferences prefs;
    Random f574r = new Random();
    int randomSpawnPoint = 0;
    float size = 0.0f;
    boolean smug = false;
    float smugElapsed = 0.0f;
    Stage stage;
    Texture textureBg;
    Texture textureCat;
    Texture textureEarLeft;
    Texture textureEarRight;
    Texture textureEye;
    Texture textureEyeSurprised;
    Texture textureEyesSmug;
    Texture textureWhiskers;
    TweenManager tweenManager;
    TextureRegion whiskers;
    ImageObject whiskersObject;

    public LiveWallpaperScreen(Game game) {
        this.game = game;
        this.size = this.isLandscape ? (float) Gdx.graphics.getWidth() : (float) Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.myViewport = new FillViewport(1280.0f, 1280.0f, this.camera);
        this.myViewport.apply();
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        this.textureBg = new Texture("backgroundnew.png");
        this.textureBg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.background = new TextureRegion(this.textureBg);
        this.textureCat = new Texture("cat.png");
        this.textureCat.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.cat = new TextureRegion(this.textureCat, 0, 0, HttpStatus.SC_LENGTH_REQUIRED, 666);
        this.catObject = new ImageObject();
        this.catObject.image = this.cat;
        this.catObject.setBounds(0.0f, 0.0f, (float) this.cat.getRegionHeight(), (float) this.cat.getRegionWidth());
        this.catObject.setRotation(-90.0f);
        this.catObject.setPosition(((float) this.cat.getRegionWidth()) / -2.0f, ((float) this.cat.getRegionHeight()) / 2.0f);
        this.textureEarLeft = new Texture("ear_left.png");
        this.textureEarLeft.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.earLeft = new TextureRegion(this.textureEarLeft, 0, 0, HttpStatus.SC_MULTI_STATUS, 268);
        this.earLeftObject = new ImageObject();
        this.earLeftObject.image = this.earLeft;
        this.earLeftObject.setBounds(0.0f, 0.0f, (float) this.earLeft.getRegionHeight(), (float) this.earLeft.getRegionWidth());
        this.earLeftObject.setOrigin(((float) this.earLeft.getRegionWidth()) / 2.0f, ((float) this.earLeft.getRegionWidth()) / 2.0f);
        this.earLeftObject.setRotation(-90.0f);
        this.earLeftObject.setPosition(-155.0f, 100.0f);
        this.textureEarRight = new Texture("ear_right.png");
        this.textureEarRight.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.earRight = new TextureRegion(this.textureEarRight);
        this.earRightObject = new ImageObject();
        this.earRightObject.image = this.earRight;
        this.earRightObject.setBounds(0.0f, 0.0f, (float) this.earRight.getRegionHeight(), (float) this.earRight.getRegionWidth());
        this.earRightObject.setOrigin(((float) this.earRight.getRegionWidth()) / 2.0f, ((float) this.earRight.getRegionWidth()) / 2.0f);
        this.earRightObject.setRotation(-90.0f);
        this.earRightObject.setPosition(-35.0f, 100.0f);
        this.textureEye = new Texture("eye_normal.png");
        this.textureEye.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.eye = new TextureRegion(this.textureEye);
        this.eyeObjectLeft = new ImageObject();
        this.eyeObjectLeft.image = this.eye;
        this.eyeObjectLeft.setBounds(0.0f, 0.0f, (float) this.eye.getRegionHeight(), (float) this.eye.getRegionWidth());
        this.eyeObjectLeft.setOrigin(((float) this.eye.getRegionWidth()) / 2.0f, ((float) this.eye.getRegionWidth()) / 2.0f);
        this.eyeObjectLeft.setPosition(-140.0f, 0.0f);
        this.eyeObjectRight = new ImageObject();
        this.eyeObjectRight.image = this.eye;
        this.eyeObjectRight.setBounds(0.0f, 0.0f, (float) this.eye.getRegionHeight(), (float) this.eye.getRegionWidth());
        this.eyeObjectRight.setOrigin(((float) this.eye.getRegionWidth()) / 2.0f, ((float) this.eye.getRegionWidth()) / 2.0f);
        this.eyeObjectRight.setPosition(7.0f, 0.0f);
        this.textureEyeSurprised = new Texture("eye_surprised.png");
        this.textureEyeSurprised.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.eyeSurprised = new TextureRegion(this.textureEyeSurprised);
        this.textureEyesSmug = new Texture("eyes_smug.png");
        this.textureEyesSmug.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.eyesSmug = new TextureRegion(this.textureEyesSmug);
        this.eyesSmugObject = new ImageObject();
        this.eyesSmugObject.image = this.eyesSmug;
        this.eyesSmugObject.setBounds(0.0f, 0.0f, (float) this.eyesSmug.getRegionHeight(), (float) this.eyesSmug.getRegionWidth());
        this.eyesSmugObject.setOrigin(((float) this.eyesSmug.getRegionWidth()) / 2.0f, ((float) this.eyesSmug.getRegionWidth()) / 2.0f);
        this.eyesSmugObject.setPosition(((float) this.eyesSmug.getRegionWidth()) / -2.0f, -125.0f);
        this.eyesSmugObject.setRotation(-90.0f);
        this.eyesSmugObject.show = false;
        this.textureWhiskers = new Texture("whiskers.png");
        this.textureWhiskers.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.whiskers = new TextureRegion(this.textureWhiskers);
        this.whiskersObject = new ImageObject();
        this.whiskersObject.image = this.whiskers;
        this.whiskersObject.setBounds(0.0f, 0.0f, (float) this.whiskers.getRegionHeight(), (float) this.whiskers.getRegionWidth());
        this.whiskersObject.setOrigin(((float) this.whiskers.getRegionWidth()) / 2.0f, ((float) this.whiskers.getRegionWidth()) / 2.0f);
        this.whiskersObject.setPosition(((float) this.whiskers.getRegionWidth()) / -2.0f, (((float) this.whiskers.getRegionWidth()) / -2.0f) - 200.0f);
        this.whiskersObject.setRotation(-90.0f);
        this.batcher = new SpriteBatch();
        this.prefs = new MyPreferences();
        this.catGroup = new ImageObjectGroup();
        this.catGroup.addActor(this.catObject);
        this.catGroup.addActor(this.earLeftObject);
        this.catGroup.addActor(this.earRightObject);
        this.catGroup.addActor(this.whiskersObject);
        this.catGroup.addActor(this.eyeObjectLeft);
        this.catGroup.addActor(this.eyeObjectRight);
        this.catGroup.addActor(this.eyesSmugObject);
        SpawnPoint leftSide = new SpawnPoint();
        leftSide.startPosition = new Vector2(-500.0f, this.camera.viewportHeight / 2.0f);
        leftSide.endPosition = new Vector2(400.0f, this.camera.viewportHeight / 2.0f);
        leftSide.startPositionLandscape = new Vector2(-500.0f, this.camera.viewportHeight / 2.0f);
        leftSide.endPositionLandscape = new Vector2(200.0f, this.camera.viewportHeight / 2.0f);
        leftSide.rotation = -90.0f;
        SpawnPoint rightSide = new SpawnPoint();
        rightSide.startPosition = new Vector2(this.camera.viewportWidth + 500.0f, this.camera.viewportHeight / 2.0f);
        rightSide.endPosition = new Vector2(this.camera.viewportWidth - 400.0f, this.camera.viewportHeight / 2.0f);
        rightSide.startPositionLandscape = new Vector2(this.camera.viewportWidth + 500.0f, this.camera.viewportHeight / 2.0f);
        rightSide.endPositionLandscape = new Vector2(this.camera.viewportWidth - 200.0f, this.camera.viewportHeight / 2.0f);
        rightSide.rotation = 90.0f;
        SpawnPoint bottomLeftSide = new SpawnPoint();
        bottomLeftSide.startPosition = new Vector2(-500.0f, -300.0f);
        bottomLeftSide.endPosition = new Vector2(400.0f, 200.0f);
        bottomLeftSide.startPositionLandscape = new Vector2(-500.0f, -300.0f);
        bottomLeftSide.endPositionLandscape = new Vector2(200.0f, 450.0f);
        bottomLeftSide.rotation = -45.0f;
        SpawnPoint bottomRightSide = new SpawnPoint();
        bottomRightSide.startPosition = new Vector2(this.camera.viewportWidth + 500.0f, -300.0f);
        bottomRightSide.endPosition = new Vector2(this.camera.viewportWidth - 400.0f, 200.0f);
        bottomRightSide.startPositionLandscape = new Vector2(this.camera.viewportWidth + 500.0f, -300.0f);
        bottomRightSide.endPositionLandscape = new Vector2(this.camera.viewportWidth - 200.0f, 450.0f);
        bottomRightSide.rotation = 45.0f;
        SpawnPoint topLeftSide = new SpawnPoint();
        topLeftSide.startPosition = new Vector2(-500.0f, this.camera.viewportHeight + 300.0f);
        topLeftSide.endPosition = new Vector2(400.0f, this.camera.viewportHeight - 200.0f);
        topLeftSide.startPositionLandscape = new Vector2(-500.0f, this.camera.viewportHeight + 300.0f);
        topLeftSide.endPositionLandscape = new Vector2(200.0f, this.camera.viewportHeight - 450.0f);
        topLeftSide.rotation = -135.0f;
        SpawnPoint top = new SpawnPoint();
        top.startPosition = new Vector2(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight + 500.0f);
        top.endPosition = new Vector2(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight - 200.0f);
        top.startPositionLandscape = new Vector2(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight + 500.0f);
        top.endPositionLandscape = new Vector2(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight - 400.0f);
        top.rotation = 180.0f;
        SpawnPoint topRightSide = new SpawnPoint();
        topRightSide.startPosition = new Vector2(this.camera.viewportWidth + 500.0f, this.camera.viewportHeight + 300.0f);
        topRightSide.endPosition = new Vector2(this.camera.viewportWidth - 400.0f, this.camera.viewportHeight - 200.0f);
        topRightSide.startPositionLandscape = new Vector2(this.camera.viewportWidth + 500.0f, this.camera.viewportHeight + 300.0f);
        topRightSide.endPositionLandscape = new Vector2(this.camera.viewportWidth - 200.0f, this.camera.viewportHeight - 450.0f);
        topRightSide.rotation = 135.0f;
        SpawnPoint bottom = new SpawnPoint();
        bottom.startPosition = new Vector2(this.camera.viewportWidth / 2.0f, -500.0f);
        bottom.endPosition = new Vector2(this.camera.viewportWidth / 2.0f, 200.0f);
        bottom.startPositionLandscape = new Vector2(this.camera.viewportWidth / 2.0f, -500.0f);
        bottom.endPositionLandscape = new Vector2(this.camera.viewportWidth / 2.0f, 400.0f);
        bottom.rotation = 0.0f;
        this.catSpawnPoints.add(leftSide);
        this.catSpawnPoints.add(bottomLeftSide);
        this.catSpawnPoints.add(bottomRightSide);
        this.catSpawnPoints.add(topLeftSide);
        this.catSpawnPoints.add(topRightSide);
        this.catSpawnPoints.add(rightSide);
        this.catSpawnPoints.add(top);
        this.catSpawnPoints.add(bottom);
        this.stage = new Stage(this.myViewport, this.batcher);
        this.stage.addActor(this.catGroup);
        Tween.registerAccessor(ImageObject.class, this.earLeftObject);
        this.tweenManager = new TweenManager();
        this.blinkElapseTime = (this.f574r.nextFloat() * (this.blinkElapseMax - this.blinkElapseMin)) + this.blinkElapseMin;
        getNewSpawnPoint();
    }

    void getNewSpawnPoint() {
        this.randomSpawnPoint = this.f574r.nextInt(this.catSpawnPoints.size());
        this.catGroup.setRotation(this.catSpawnPoints.get(this.randomSpawnPoint).rotation);
        this.catGroup.setPosition(this.catSpawnPoints.get(this.randomSpawnPoint).startPosition.x, ((SpawnPoint) this.catSpawnPoints.get(this.randomSpawnPoint)).startPosition.y);
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    private void draw(float delta) {
        this.lastTouch += delta;
        if (this.appearing) {
            this.smug = false;
            this.appearLengthElapsed += delta;
            if (this.appearLengthElapsed > this.appearLength) {
                this.appearing = false;
                this.smug = true;
                this.appearLengthElapsed = 0.0f;
                this.appearLength = (this.f574r.nextFloat() * (MyPreferences.f368I.appearLengthMax - MyPreferences.f368I.appearLengthMin)) + MyPreferences.f368I.appearLengthMin;
            }
        } else if (this.smug) {
            this.smugElapsed += delta;
            if (this.smugElapsed > MyPreferences.f368I.smugTime) {
                this.smug = false;
                this.eyesSmugObject.show = false;
                this.eyeObjectRight.show = true;
                this.eyeObjectLeft.show = true;
                this.smugElapsed = 0.0f;
                this.appearElapsed = 0.0f;
                this.appearTime = (this.f574r.nextFloat() * (this.appearTimeMax - this.appearTimeMin)) + this.appearTimeMin;
            }
        } else {
            this.appearElapsed += delta;
            if (this.appearElapsed > this.appearTime) {
                this.appearElapsed = 0.0f;
                this.appearing = true;
                this.smug = false;
            }
        }
        if (!this.smug) {
            if (this.blinking) {
                this.eyesClosedElapsed += delta;
                if (this.eyesClosedElapsed > this.blinkTime) {
                    this.eyesClosedElapsed = 0.0f;
                    this.blinking = false;
                    this.eyeObjectRight.show = true;
                    this.eyeObjectLeft.show = true;
                    this.eyesSmugObject.show = false;
                }
            } else {
                this.blinkElapsed += delta;
            }
            if (this.blinkElapsed > this.blinkElapseTime) {
                this.blinkElapseTime = (this.f574r.nextFloat() * (this.blinkElapseMax - this.blinkElapseMin)) + this.blinkElapseMin;
                this.blinkElapsed = 0.0f;
                this.eyeObjectRight.show = false;
                this.eyeObjectLeft.show = false;
                this.eyesSmugObject.show = true;
                this.blinking = true;
            }
        }
        this.tweenManager.update(delta);
        if (this.appearLengthElapsed == 0.0f && this.appearing) {
            this.earLeftObject.setRotation(-40.0f);
            this.earRightObject.setRotation(-130.0f);
            getNewSpawnPoint();
            this.tweenManager.killAll();
            this.first = true;
        }
        this.eyeObjectRight.rotateBy(200.0f * delta);
        this.eyeObjectLeft.rotateBy(200.0f * delta);
        if (this.appearing) {
            Vector2 endPos = this.catSpawnPoints.get(this.randomSpawnPoint).getEndPosition(this.isLandscape);
            if (this.first) {
                this.first = false;
                Tween.to(this.catGroup, 3, 0.8f).target(endPos.x, endPos.y).ease(Elastic.INOUT).start(this.tweenManager);
                Tween.to(this.earLeftObject, 4, 3.0f).target(-80.0f).ease(Elastic.OUT).start(this.tweenManager);
                Tween.to(this.earRightObject, 4, 3.0f).target(-95.0f).ease(Elastic.OUT).start(this.tweenManager);
            }
        } else if (this.smug) {
            this.eyesSmugObject.show = true;
            this.eyeObjectRight.show = false;
            this.eyeObjectLeft.show = false;
            Tween.to(this.earLeftObject, 4, 1.0f).target(-50.0f).ease(Bounce.IN).start(this.tweenManager);
            Tween.to(this.earRightObject, 4, 1.0f).target(-130.0f).ease(Bounce.IN).start(this.tweenManager);
        } else {
            Vector2 startPos = this.catSpawnPoints.get(this.randomSpawnPoint).getStartPosition(this.isLandscape);
            Tween.to(this.catGroup, 3, this.disappearSpeed).target(startPos.x, startPos.y).ease(Bounce.INOUT).start(this.tweenManager);
            Tween.to(this.earLeftObject, 4, this.disappearSpeed * 0.2f).target(-95.0f).ease(Bounce.IN).start(this.tweenManager);
            Tween.to(this.earRightObject, 4, this.disappearSpeed * 0.2f).target(-85.0f).ease(Bounce.IN).start(this.tweenManager);
        }
        this.camera.update();
        if (Gdx.input.justTouched()) {
            if (this.prefs.hidesOnTouch) {
                this.appearing = false;
                this.smug = false;
                this.appearElapsed = 0.0f;
                this.appearLengthElapsed = 0.0f;
                this.smugElapsed = 0.0f;
                this.eyesSmugObject.show = false;
                this.eyeObjectRight.show = true;
                this.eyeObjectLeft.show = true;
            }
            if (this.prefs.doubleTapHide && this.lastTouch < 0.3f) {
                this.appearing = false;
                this.smug = false;
                this.appearElapsed = 0.0f;
                this.appearLengthElapsed = 0.0f;
                this.smugElapsed = 0.0f;
                this.eyesSmugObject.show = false;
                this.eyeObjectRight.show = true;
                this.eyeObjectLeft.show = true;
            }
            this.lastTouch = 0.0f;
        }
        this.batcher.setProjectionMatrix(this.camera.combined);
        this.batcher.enableBlending();
        this.batcher.begin();
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batcher.setColor(Color.valueOf(MyPreferences.f368I.backgroundColor));
        this.batcher.draw(this.background, 0.0f, 0.0f, this.camera.viewportWidth, this.camera.viewportHeight);
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batcher.disableBlending();
        this.batcher.end();
        this.stage.draw();
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
