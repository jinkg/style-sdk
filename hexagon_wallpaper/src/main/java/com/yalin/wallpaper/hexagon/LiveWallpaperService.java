package com.yalin.wallpaper.hexagon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.view.Display;
import android.view.WindowManager;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.opengl.view.EngineRenderer;
import org.andengine.opengl.view.IRendererListener;

public class LiveWallpaperService extends BaseLiveWallpaperService implements
        OnSharedPreferenceChangeListener, IOffsetsChanged {
    public static final String SHARED_PREFS_NAME = "livewallpaperservicesettings";
    public int CAMERA_HEIGHT = 800;
    public int CAMERA_WIDTH = 480;
    LoopEntityModifier androidModifier01;
    LoopEntityModifier androidModifier02;
    LoopEntityModifier androidModifier03;
    LoopEntityModifier androidModifier04;
    LoopEntityModifier androidModifier05;
    LoopEntityModifier androidModifier06;
    LoopEntityModifier androidModifier07;
    boolean assetsCreated = false;
    Sprite background00;
    Sprite background01;
    Sprite background02;
    Sprite background03;
    Sprite background04;
    Sprite background05;
    Sprite background06;
    Sprite background07;
    ITextureRegion backgroundRegion00;
    ITextureRegion backgroundRegion01;
    ITextureRegion backgroundRegion02;
    ITextureRegion backgroundRegion03;
    ITextureRegion backgroundRegion04;
    ITextureRegion backgroundRegion05;
    ITextureRegion backgroundRegion06;
    ITextureRegion backgroundRegion07;
    BitmapTextureAtlas backgroundTexture00;
    BitmapTextureAtlas backgroundTexture01;
    BitmapTextureAtlas backgroundTexture02;
    BitmapTextureAtlas backgroundTexture03;
    BitmapTextureAtlas backgroundTexture04;
    BitmapTextureAtlas backgroundTexture05;
    BitmapTextureAtlas backgroundTexture06;
    BitmapTextureAtlas backgroundTexture07;
    Display display;
    private SharedPreferences mSharedPreferences;
    float off = 0.0f;
    int oldRotation = 0;
    float on = Line.LINE_WIDTH_DEFAULT;
    int rotation = 1;
    Scene scene;
    public int time = 7;
    public int timeNew = this.time;
    public int timeSet = 2;
    ZoomCamera zoomCamera;

    public LiveWallpaperService(Context host) {
        super(host);
    }

    protected class MyBaseWallpaperGLEngine extends GLEngine {
        private ConfigChooser mConfigChooser;
        private EngineRenderer mEngineRenderer;

        public MyBaseWallpaperGLEngine(IRendererListener pRendererListener) {
            super();
            if (this.mConfigChooser == null) {
                mEngine.getEngineOptions().getRenderOptions().setMultiSampling(false);
                this.mConfigChooser = new ConfigChooser(LiveWallpaperService.this.mEngine.getEngineOptions().getRenderOptions().isMultiSampling());
            }
            setEGLConfigChooser(this.mConfigChooser);
            this.mEngineRenderer = new EngineRenderer(LiveWallpaperService.this.mEngine, this.mConfigChooser, pRendererListener);
            setRenderer(this.mEngineRenderer);
            setRenderMode(1);
        }

        public Bundle onCommand(String pAction, int pX, int pY, int pZ, Bundle pExtras, boolean pResultRequested) {
            if (!pAction.equals("android.wallpaper.tap")) {
                pAction.equals("android.home.drop");
            }
            return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
        }

        public void onOffsetsChanged(float pXOffset, float pYOffset, float pXOffsetStep, float pYOffsetStep, int pXPixelOffset, int pYPixelOffset) {
            LiveWallpaperService.this.offsetsChanged(pXOffset, pYOffset, pXOffsetStep, pYOffsetStep, pXPixelOffset, pYPixelOffset);
        }

        public void onResume() {
            super.onResume();
            LiveWallpaperService.this.onResume();
        }

        public void onPause() {
            super.onPause();
            LiveWallpaperService.this.onPause();
        }

        public void onDestroy() {
            super.onDestroy();
            this.mEngineRenderer = null;
        }
    }

    class C01161 extends FPSLogger {
        public C01161() {
            super();
        }

        public void onUpdate(float pSecondsElapsed) {
        }

        public void reset() {
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences pSharedPrefs, String pKey) {
        this.time = Integer.valueOf(pSharedPrefs.getString("speed", "2"));
        switch (this.time) {
            case 1:
                this.timeNew = 14;
                break;
            case 2:
                this.timeNew = 7;
                break;
            default:
                this.timeNew = 1;
                break;
        }
        if (this.assetsCreated) {
            resetAndroidAsset();
        }
    }

    public void IOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
    }

    public void loadOrientation() {
        this.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public void checkOrientation() {
        this.rotation = this.display.getOrientation();
        if (this.oldRotation != this.rotation) {
            this.oldRotation = this.rotation;
            if (this.rotation == 1 || this.rotation == 3) {
                this.mEngine.getCamera().setCenter((float) (this.CAMERA_WIDTH / 2), 540.0f);
                this.scene.setScaleX(0.5f);
                this.scene.setScaleY(1.4f);
                return;
            }
            if (this.mEngine.getCamera().getCenterY() != ((float) (this.CAMERA_HEIGHT / 2))) {
                this.mEngine.getCamera().setCenter((float) this.CAMERA_WIDTH, (float) this.CAMERA_HEIGHT);
            }
            this.scene.setScale(Line.LINE_WIDTH_DEFAULT);
        }
    }

    public void initializePreferences() {
        this.mSharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, 0);
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(this.mSharedPreferences, null);
    }

    public EngineOptions onCreateEngineOptions() {
        this.zoomCamera = new ZoomCamera(0.0f, 0.0f, (float) this.CAMERA_WIDTH, (float) this.CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy((float) this.CAMERA_WIDTH, (float) this.CAMERA_HEIGHT), this.zoomCamera);
    }

    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        this.scene = new Scene();
        initializePreferences();
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.backgroundTexture00 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion00 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture00, (Context) this, "background.jpg", 0, 0);
        this.background00 = new Sprite(0.0f, 0.0f, this.backgroundRegion00, getVertexBufferObjectManager());
        this.backgroundTexture00.load();
        this.scene.attachChild(this.background00);
        this.backgroundTexture07 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion07 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture07, (Context) this, "background07.jpg", 0, 0);
        this.background07 = new Sprite(0.0f, 0.0f, this.backgroundRegion07, getVertexBufferObjectManager());
        this.background07.setAlpha(0.0f);
        this.backgroundTexture07.load();
        this.scene.attachChild(this.background07);
        this.androidModifier07 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off)));
        this.background07.registerEntityModifier(this.androidModifier07);
        this.backgroundTexture06 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion06 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture06, (Context) this, "background06.jpg", 0, 0);
        this.background06 = new Sprite(0.0f, 0.0f, this.backgroundRegion06, getVertexBufferObjectManager());
        this.background06.setAlpha(0.0f);
        this.backgroundTexture06.load();
        this.scene.attachChild(this.background06);
        this.androidModifier06 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background06.registerEntityModifier(this.androidModifier06);
        this.backgroundTexture05 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion05 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture05, (Context) this, "background05.jpg", 0, 0);
        this.background05 = new Sprite(0.0f, 0.0f, this.backgroundRegion05, getVertexBufferObjectManager());
        this.background05.setAlpha(0.0f);
        this.backgroundTexture05.load();
        this.scene.attachChild(this.background05);
        this.androidModifier05 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background05.registerEntityModifier(this.androidModifier05);
        this.backgroundTexture04 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion04 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture04, (Context) this, "background04.jpg", 0, 0);
        this.background04 = new Sprite(0.0f, 0.0f, this.backgroundRegion04, getVertexBufferObjectManager());
        this.background04.setAlpha(0.0f);
        this.backgroundTexture04.load();
        this.scene.attachChild(this.background04);
        this.androidModifier04 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background04.registerEntityModifier(this.androidModifier04);
        this.backgroundTexture03 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion03 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture03, (Context) this, "background03.jpg", 0, 0);
        this.background03 = new Sprite(0.0f, 0.0f, this.backgroundRegion03, getVertexBufferObjectManager());
        this.background03.setAlpha(0.0f);
        this.backgroundTexture03.load();
        this.scene.attachChild(this.background03);
        this.androidModifier03 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background03.registerEntityModifier(this.androidModifier03);
        this.backgroundTexture02 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion02 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture02, (Context) this, "background02.jpg", 0, 0);
        this.background02 = new Sprite(0.0f, 0.0f, this.backgroundRegion02, getVertexBufferObjectManager());
        this.background02.setAlpha(0.0f);
        this.backgroundTexture02.load();
        this.scene.attachChild(this.background02);
        this.androidModifier02 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background02.registerEntityModifier(this.androidModifier02);
        this.backgroundTexture01 = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
        this.backgroundRegion01 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture01, (Context) this, "background.jpg", 0, 0);
        this.background01 = new Sprite(0.0f, 0.0f, this.backgroundRegion01, getVertexBufferObjectManager());
        this.background01.setAlpha(0.0f);
        this.backgroundTexture01.load();
        this.scene.attachChild(this.background01);
        this.androidModifier01 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
        this.background01.registerEntityModifier(this.androidModifier01);
        this.assetsCreated = true;
        loadOrientation();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    public void resetAndroidAsset() {
        if (this.assetsCreated) {
            this.backgroundTexture00.clearTextureAtlasSources();
            this.backgroundRegion00 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.backgroundTexture00, (Context) this, "background.jpg", 0, 0);
            this.background07.unregisterEntityModifier(this.androidModifier07);
            this.background06.unregisterEntityModifier(this.androidModifier06);
            this.background05.unregisterEntityModifier(this.androidModifier05);
            this.background04.unregisterEntityModifier(this.androidModifier04);
            this.background03.unregisterEntityModifier(this.androidModifier03);
            this.background02.unregisterEntityModifier(this.androidModifier02);
            this.background01.unregisterEntityModifier(this.androidModifier01);
            this.androidModifier07 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off)));
            this.background07.registerEntityModifier(this.androidModifier07);
            this.androidModifier06 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background06.registerEntityModifier(this.androidModifier06);
            this.androidModifier05 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background05.registerEntityModifier(this.androidModifier05);
            this.androidModifier04 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background04.registerEntityModifier(this.androidModifier04);
            this.androidModifier03 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background03.registerEntityModifier(this.androidModifier03);
            this.androidModifier02 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.on), new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background02.registerEntityModifier(this.androidModifier02);
            this.androidModifier01 = new LoopEntityModifier(new SequenceEntityModifier(new AlphaModifier((float) this.time, this.on, this.on), new AlphaModifier((float) this.timeSet, this.on, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off), new AlphaModifier((float) this.time, this.off, this.off), new AlphaModifier((float) this.timeSet, this.off, this.off)));
            this.background01.registerEntityModifier(this.androidModifier01);
        }
    }

    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new C01161());
        pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
    }

    public WallpaperService.Engine onCreateEngine() {
        return new MyBaseWallpaperGLEngine(this);
    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    public void offsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
    }
}
