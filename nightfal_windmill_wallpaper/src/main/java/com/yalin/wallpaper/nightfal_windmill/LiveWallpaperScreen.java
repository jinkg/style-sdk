package com.yalin.wallpaper.nightfal_windmill;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class LiveWallpaperScreen implements Screen {
    TextureRegion background;
    SpriteBatch batcher;
    OrthographicCamera camera;
    TextureRegion clouds;
    float cloudsTopX = 0.0f;
    float cloudsTopX2 = 1280.0f;
    boolean drawRuralWindMill = false;
    boolean drawWindMill = false;
    boolean first = true;
    TextureRegion foreground;
    Game game;
    int heightAdjustment = 0;
    boolean isLandscape = false;
    float lastTouch = 0.0f;
    TextureRegion moon;
    ImageObject moonObject;
    Viewport myViewport;
    MyPreferences prefs;
    Random f576r = new Random();
    TextureRegion ruralMillWheel;
    ImageObject ruralmillObject;
    Group sceneGroup;
    StarHolder sh;
    float size = 0.0f;
    Stage stage;
    TextureRegion star;
    TextureRegion star1;
    Animation star1Anim = new Animation();
    TextureRegion star2;
    Group starGroup;
    ArrayList<StarHolder> stars = new ArrayList();
    Texture textureBg;
    Texture textureClouds;
    Texture textureForeground;
    Texture textureMoon;
    Texture textureRuralMillWheel;
    Texture textureStar1;
    Texture textureStar2;
    Texture textureWindmillWheel;
    ImageObject windmillObject;
    TextureRegion windmillWheel;

    public class Animation {
        ArrayList<AnimationFrame> animation = new ArrayList();
        boolean animationComplete = false;
        boolean autoReverse = false;
        int currentFrame = 0;
        boolean hasNextAnimation = false;
        boolean hasPreviousAnimation = false;
        Animation nextAnimation;
        Animation previousAnimation;
        Random rand = new Random();
        boolean reverseWhenCompleted = false;

        public void setDefaultTimeToNextFrame(int wait) {
            Iterator it = this.animation.iterator();
            while (it.hasNext()) {
                ((AnimationFrame) it.next()).setTimeToNextFrame(wait);
            }
        }

        public boolean getIsAnimationComplete() {
            return this.animationComplete;
        }

        public void setIsAnimationComplete(boolean complete) {
            this.animationComplete = complete;
        }

        public boolean getHasNextAnimation() {
            return this.hasNextAnimation;
        }

        public void setHasNextAnimation(boolean hasNext) {
            this.hasNextAnimation = hasNext;
        }

        public boolean getHasPreviousAnimation() {
            return this.hasPreviousAnimation;
        }

        public void setHasPreviousAnimation(boolean hasPrevious) {
            this.hasPreviousAnimation = hasPrevious;
        }

        public boolean getReverseWhenCompleted() {
            return this.reverseWhenCompleted;
        }

        public void setReverseWhenCompleted(boolean reverse) {
            this.reverseWhenCompleted = reverse;
        }

        public boolean getAutoReverse() {
            return this.autoReverse;
        }

        public void setAutoReverse(boolean reverse) {
            this.autoReverse = reverse;
        }

        public Animation getNextAnimation() {
            return this.nextAnimation;
        }

        public Animation getPreviousAnimation() {
            return this.previousAnimation;
        }

        public void atCompletionGoTo(Animation a) {
            this.nextAnimation = a;
            this.hasNextAnimation = true;
        }

        public void atReverseGoTo(Animation a) {
            this.previousAnimation = a;
            this.hasPreviousAnimation = true;
        }

        public void addFrame(AnimationFrame f) {
            this.animation.add(f);
            f.setFrameNumber(this.animation.indexOf(f));
        }

        public void reverse() {
            ArrayList<AnimationFrame> newAnimation = new ArrayList();
            for (int x = this.animation.size() - 1; x >= 0; x--) {
                AnimationFrame f = (AnimationFrame) this.animation.get(x);
                newAnimation.add(f);
                f.setFrameNumber(newAnimation.indexOf(f));
            }
            this.animation = newAnimation;
            this.currentFrame = 0;
        }

        public void reverseDestinations() {
            Animation currentNextAnimation = getNextAnimation();
            atCompletionGoTo(getPreviousAnimation());
            atReverseGoTo(currentNextAnimation);
        }

        public void updateCurrentFrame() {
            if (this.currentFrame < this.animation.size() - 1) {
                this.currentFrame++;
                return;
            }
            if (this.autoReverse || this.reverseWhenCompleted) {
                reverse();
            }
            if (this.autoReverse || !getHasNextAnimation()) {
                this.currentFrame = 0;
            } else {
                setIsAnimationComplete(true);
            }
        }

        public AnimationFrame getNextFrame(float elapsedTime) {
            AnimationFrame currentAnimationFrame = (AnimationFrame) this.animation.get(this.currentFrame);
            currentAnimationFrame.currentFrameTime += 1000.0f * elapsedTime;
            if (currentAnimationFrame.currentFrameTime < ((float) currentAnimationFrame.timeToNextFrame)) {
                return currentAnimationFrame;
            }
            currentAnimationFrame.currentFrameTime = 0.0f;
            if (currentAnimationFrame.getRandomizeTimeToNextFrame()) {
                currentAnimationFrame.setTimeToNextFrame(this.rand.nextInt(currentAnimationFrame.timeToNextFrameHighBounds - currentAnimationFrame.timeToNextFrameLowBounds) + currentAnimationFrame.timeToNextFrameLowBounds);
            }
            updateCurrentFrame();
            return (AnimationFrame) this.animation.get(this.currentFrame);
        }

        public void reset() {
            Iterator it = this.animation.iterator();
            while (it.hasNext()) {
                ((AnimationFrame) it.next()).setBitmap(null);
            }
            this.animation.clear();
        }
    }

    public class AnimationFrame {
        TextureRegion bitmap;
        public float currentFrameTime = 0.0f;
        int frame = 0;
        long lastTime = 0;
        boolean randomizeTimeToNextFrame = false;
        int resourceID = 0;
        int timeToNextFrame = 100;
        int timeToNextFrameHighBounds = Keys.NUMPAD_6;
        int timeToNextFrameLowBounds = 50;

        public int getFrameNumber() {
            return this.frame;
        }

        public void setFrameNumber(int f) {
            this.frame = f;
        }

        public int getResourceID() {
            return this.resourceID;
        }

        public void setResourceID(int id) {
            this.resourceID = id;
        }

        public int getTimeToNextFrame() {
            return this.timeToNextFrame;
        }

        public void setTimeToNextFrame(int time) {
            this.timeToNextFrame = time;
        }

        public long getLastTime() {
            return this.lastTime;
        }

        public void setLastTine(long time) {
            this.lastTime = time;
        }

        public TextureRegion getBitmap() {
            return this.bitmap;
        }

        public void setBitmap(TextureRegion b) {
            this.bitmap = b;
        }

        public boolean getRandomizeTimeToNextFrame() {
            return this.randomizeTimeToNextFrame;
        }

        public void setRandomizeTimeToNextFrame(boolean randomize) {
            this.randomizeTimeToNextFrame = randomize;
        }

        public void setBoundsForTimeToNextFrame(int low, int high) {
            this.timeToNextFrameLowBounds = low;
            this.timeToNextFrameHighBounds = high;
        }
    }

    public class StarHolder {
        float alpha = 0.2f;
        int alphaDirection = 1;
        float alpha_speed = 0.1f;
        float maxAlpha = 0.2f;
        float minAlpha = 0.2f;
        public ImageObject starObject;
        float f368x = 0.0f;
        float f369y = 0.0f;
    }

    public LiveWallpaperScreen(Game game) {
        this.game = game;
        this.size = this.isLandscape ? (float) Gdx.graphics.getWidth() : (float) Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.myViewport = new FillViewport(1024.0f, 1024.0f, this.camera);
        this.myViewport.apply();
        this.prefs = new MyPreferences(this);
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        this.textureBg = new Texture("background.jpg");
        this.textureBg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.background = new TextureRegion(this.textureBg);
        ImageObject backgroundObject = new ImageObject();
        backgroundObject.image = this.background;
        backgroundObject.setBounds(0.0f, 0.0f, (float) this.background.getRegionWidth(), (float) this.background.getRegionHeight());
        backgroundObject.setPosition(((float) this.background.getRegionWidth()) / -2.0f, ((float) this.background.getRegionHeight()) / -2.0f);
        this.textureClouds = new Texture("clouds.png");
        this.textureClouds.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.clouds = new TextureRegion(this.textureClouds);
        setMoon();
        setForeground();
        this.textureStar1 = new Texture("star1.png");
        this.textureStar1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.star1 = new TextureRegion(this.textureStar1, 0, 0, 16, 16);
        this.textureStar2 = new Texture("star2.png");
        this.textureStar2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.star2 = new TextureRegion(this.textureStar2, 0, 0, 16, 16);
        AnimationFrame star11 = new AnimationFrame();
        star11.setBitmap(this.star1);
        AnimationFrame star12 = new AnimationFrame();
        star12.setBitmap(this.star2);
        this.star1Anim.addFrame(star11);
        this.star1Anim.addFrame(star12);
        this.starGroup = new Group();
        this.starGroup.setOrigin(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f);
        this.starGroup.setPosition(100.0f, 100.0f);
        this.batcher = new SpriteBatch();
        setupTwinklingStars();
        this.sceneGroup = new Group();
        this.sceneGroup.addActor(backgroundObject);
        this.sceneGroup.addActor(this.moonObject);
        this.sceneGroup.setPosition((this.camera.viewportWidth / 2.0f) + 100.0f, (this.camera.viewportHeight / 2.0f) + 100.0f);
        this.stage = new Stage(this.myViewport, this.batcher);
        this.stage.addActor(this.sceneGroup);
    }

    public void setForeground() {
        this.drawWindMill = false;
        this.drawRuralWindMill = false;
        this.textureForeground = new Texture(this.prefs.foregroundScene);
        this.textureForeground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.foreground = new TextureRegion(this.textureForeground);
        this.foreground.setRegion(0, 0, this.foreground.getRegionWidth(), this.foreground.getRegionHeight());
        if (this.prefs.foregroundScene.equalsIgnoreCase("foreground_windmill.png")) {
            this.textureWindmillWheel = new Texture("foreground_windmill_wheel.png");
            this.textureWindmillWheel.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            this.windmillWheel = new TextureRegion(this.textureWindmillWheel);
            this.drawWindMill = true;
            this.windmillObject = new ImageObject();
            this.windmillObject.image = this.windmillWheel;
            this.windmillObject.setBounds(0.0f, 0.0f, (float) this.windmillWheel.getRegionWidth(),
                    (float) this.windmillWheel.getRegionHeight());
            this.windmillObject.setOrigin((float) (this.windmillWheel.getRegionWidth() / 2),
                    (float) (this.windmillWheel.getRegionHeight() / 2));
            this.windmillObject.setPosition((this.camera.viewportWidth / 2.0f) - ((float) (this.windmillWheel.getRegionWidth() / 2)), ((((-this.camera.viewportHeight) / 2.0f) + ((float) this.foreground.getRegionHeight())) + (((float) this.windmillWheel.getRegionHeight()) / 3.0f)) + 80.0f);
        }
        if (this.prefs.foregroundScene.equalsIgnoreCase("rural2.png")) {
            this.textureRuralMillWheel = new Texture("mill.png");
            this.textureRuralMillWheel.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            this.ruralMillWheel = new TextureRegion(this.textureRuralMillWheel);
            this.drawRuralWindMill = true;
            this.ruralmillObject = new ImageObject();
            this.ruralmillObject.image = this.ruralMillWheel;
            this.ruralmillObject.setBounds(0.0f, 0.0f, (float) this.ruralMillWheel.getRegionWidth(), (float) this.ruralMillWheel.getRegionHeight());
            this.ruralmillObject.setOrigin((float) (this.ruralMillWheel.getRegionWidth() / 2), (float) (this.ruralMillWheel.getRegionHeight() / 2));
            this.ruralmillObject.setPosition((this.camera.viewportWidth / 2.0f) + 90.0f, (this.camera.viewportHeight / 2.0f) - 160.0f);
        }
        this.heightAdjustment = 500 - this.foreground.getRegionHeight();
    }

    public void setMoon() {
        this.textureMoon = new Texture(this.prefs.moon);
        this.textureMoon.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.moon = new TextureRegion(this.textureMoon);
        if (this.moonObject == null) {
            this.moonObject = new ImageObject();
        }
        this.moonObject.image = this.moon;
        this.moonObject.setBounds(0.0f, 0.0f, (float) this.moon.getRegionWidth(), (float) this.moon.getRegionHeight());
        this.moonObject.setPosition(((float) (this.moon.getRegionWidth() / -2)) - 400.0f, ((float) this.moon.getRegionHeight()) / -2.0f);
    }

    private void setupTwinklingStars() {
        this.starGroup.clear();
        if (this.stars == null) {
            this.stars = new ArrayList();
        }
        if (this.background != null) {
            this.stars.clear();
            for (int i = 0; i < this.prefs.numberOfStars; i++) {
                StarHolder sh = new StarHolder();
                sh.f368x = MathUtils.random(0.0f, this.camera.viewportWidth);
                sh.f369y = MathUtils.random(0.0f, this.camera.viewportHeight);
                sh.maxAlpha = this.f576r.nextFloat();
                if (sh.maxAlpha > 0.95f) {
                    sh.maxAlpha = 0.95f;
                }
                if (sh.maxAlpha < 0.5f) {
                    sh.maxAlpha = 0.5f;
                }
                sh.minAlpha = this.f576r.nextFloat();
                if (sh.minAlpha > sh.maxAlpha || sh.maxAlpha - sh.minAlpha < 0.3f) {
                    sh.minAlpha = sh.maxAlpha - 0.7f;
                }
                if (((double) sh.minAlpha) < 0.2d) {
                    sh.minAlpha = 0.2f;
                }
                sh.alpha = this.f576r.nextFloat() * sh.maxAlpha;
                sh.alpha_speed = (this.f576r.nextFloat() + 0.1f) * 5.0E-4f;
                this.stars.add(sh);
                ImageObject starObject = new ImageObject();
                starObject.image = this.star1;
                starObject.setBounds(0.0f, 0.0f, 16.0f, 16.0f);
                starObject.setPosition(sh.f368x, sh.f369y);
                sh.starObject = starObject;
                starObject.setOrigin(8.0f, 8.0f);
                this.starGroup.addActor(starObject);
            }
        }
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    private void draw(float delta) {
        this.lastTouch += delta;
        this.cloudsTopX -= 20.0f * delta;
        this.cloudsTopX2 -= 20.0f * delta;
        if (this.prefs.updatedSettings) {
            if (this.prefs.showMoon) {
                this.moonObject.setScale(1.0f, 1.0f);
            } else {
                this.moonObject.setScale(0.0f, 0.0f);
            }
            this.prefs.updatedSettings = false;
        }
        if (this.prefs.updatedForeground) {
            this.prefs.updatedForeground = false;
            setForeground();
        }
        if (this.prefs.updatedNumberOfStars) {
            setupTwinklingStars();
            this.prefs.updatedNumberOfStars = false;
        }
        if (this.prefs.updatedMoon) {
            Log.d("test", "Updated moon! Calling set moon.");
            this.prefs.updatedMoon = false;
            setMoon();
        }
        this.camera.update();
        if (Gdx.input.justTouched()) {
            if (this.prefs.doubleTapOpensSettings && this.lastTouch < 0.3f) {
                WallpaperInfo info = WallpaperManager.getInstance(LiveWallpaperAndroid.myApplicationContext).getWallpaperInfo();
                if (info != null) {
                    String str1 = info.getSettingsActivity();
                    if (str1 != null) {
                        String str2 = info.getPackageName();
                        Intent localIntent1 = new Intent();
                        localIntent1.setComponent(new ComponentName(str2, str1));
                        localIntent1.addFlags(268435456);
                        LiveWallpaperAndroid.myApplicationContext.startActivity(localIntent1);
                    }
                }
            }
            this.lastTouch = 0.0f;
        }
        this.batcher.setProjectionMatrix(this.camera.combined);
        this.sceneGroup.rotateBy(this.prefs.rotationSpeed * delta);
        this.starGroup.rotateBy(this.prefs.rotationSpeed * delta);
        this.stage.draw();
        this.batcher.enableBlending();
        this.batcher.begin();
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        drawStars(this.batcher);
        if (this.prefs.showClouds) {
            this.batcher.draw(this.clouds, this.cloudsTopX, this.camera.viewportHeight - ((float) this.clouds.getRegionHeight()));
            this.batcher.draw(this.clouds, this.cloudsTopX2, this.camera.viewportHeight - ((float) this.clouds.getRegionHeight()));
        }
        this.batcher.draw(this.foreground, (this.camera.viewportWidth / 2.0f) - (((float) this.foreground.getRegionWidth()) / 2.0f), ((this.camera.viewportHeight / -2.0f) + ((float) this.foreground.getRegionHeight())) + ((float) this.heightAdjustment));
        if (this.drawWindMill) {
            this.windmillObject.rotateBy(-30.0f * delta);
            this.windmillObject.draw(this.batcher, 1.0f);
        }
        if (this.drawRuralWindMill) {
            this.ruralmillObject.rotateBy(-200.0f * delta);
            this.ruralmillObject.draw(this.batcher, 1.0f);
        }
        this.batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batcher.disableBlending();
        this.batcher.end();
        if (this.cloudsTopX < -1280.0f) {
            this.cloudsTopX = 1280.0f;
        }
        if (this.cloudsTopX2 < -1280.0f) {
            this.cloudsTopX2 = 1280.0f;
        }
        int sleepTime = 60 - ((int) (1000.0f * delta));
        if (sleepTime < 0) {
            sleepTime = 1;
        }
        try {
            Thread.sleep((long) sleepTime);
        } catch (InterruptedException e) {
        }
    }

    void drawStars(SpriteBatch batcher) {
        if (this.prefs.showTwinklingStars && !this.prefs.updatedNumberOfStars) {
            if (this.prefs.numberOfStars > this.stars.size()) {
                setupTwinklingStars();
            }
            this.star = this.star1Anim.getNextFrame(Gdx.graphics.getDeltaTime()).getBitmap();
            int i = 0;
            while (i < this.prefs.numberOfStars && i <= this.stars.size()) {
                this.sh = (StarHolder) this.stars.get(i);
                StarHolder starHolder = this.sh;
                starHolder.alpha += ((Gdx.graphics.getDeltaTime() * 1000.0f) * this.sh.alpha_speed) * ((float) this.sh.alphaDirection);
                int alpha = (int) (255.0f * this.sh.alpha);
                if (this.sh.alpha >= this.sh.maxAlpha) {
                    this.sh.alphaDirection = -1;
                } else if (this.sh.alpha <= this.sh.minAlpha) {
                    this.sh.alphaDirection = 1;
                }
                if (this.isLandscape) {
                }
                this.sh.starObject.image = this.star;
                this.sh.starObject.setScale(this.sh.alpha, this.sh.alpha);
                i++;
            }
            batcher.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            this.starGroup.draw(batcher, 1.0f);
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
        if (this.isLandscape) {
            this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 3.0f, 0.0f);
        } else {
            this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        }
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
