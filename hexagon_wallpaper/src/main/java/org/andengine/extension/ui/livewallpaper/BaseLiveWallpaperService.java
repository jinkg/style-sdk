package org.andengine.extension.ui.livewallpaper;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;

import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.AndEngine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.opengl.GLWallpaperService;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.sensor.orientation.IOrientationListener;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.shader.ShaderProgramManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.opengl.view.EngineRenderer;
import org.andengine.opengl.view.IRendererListener;
import org.andengine.ui.IGameInterface;
import org.andengine.util.debug.Debug;

public abstract class BaseLiveWallpaperService extends GLWallpaperService implements IGameInterface, IRendererListener {
    private boolean mCreateGameCalled;
    protected AndEngine mEngine;
    protected EngineOptions mEngineOptions;
    private boolean mGameCreated;
    private boolean mGamePaused;
    private boolean mOnReloadResourcesScheduled;

    public BaseLiveWallpaperService(Context host) {
        super(host);
    }

    class C00251 implements OnPopulateSceneCallback {
        C00251() {
        }

        public void onPopulateSceneFinished() {
            try {
                Debug.d(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onGameCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                BaseLiveWallpaperService.this.onGameCreated();
            } catch (Throwable pThrowable) {
                Debug.e(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onGameCreated failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
            }
            BaseLiveWallpaperService.this.onResumeGame();
        }
    }

    protected class BaseWallpaperGLEngine extends GLEngine {
        private ConfigChooser mConfigChooser;
        private EngineRenderer mEngineRenderer;

        public BaseWallpaperGLEngine(IRendererListener pRendererListener) {
            super();
            if (this.mConfigChooser == null) {
                this.mConfigChooser = new ConfigChooser(BaseLiveWallpaperService.this.mEngine.getEngineOptions().getRenderOptions().isMultiSampling());
            }
            setEGLConfigChooser(this.mConfigChooser);
            this.mEngineRenderer = new EngineRenderer(BaseLiveWallpaperService.this.mEngine, this.mConfigChooser, pRendererListener);
            setRenderer(this.mEngineRenderer);
            setRenderMode(1);
        }

        public Bundle onCommand(String pAction, int pX, int pY, int pZ, Bundle pExtras, boolean pResultRequested) {
            if (pAction.equals("android.wallpaper.tap")) {
                BaseLiveWallpaperService.this.onTap(pX, pY);
            } else if (pAction.equals("android.home.drop")) {
                BaseLiveWallpaperService.this.onDrop(pX, pY);
            }
            return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
        }

        public void onOffsetsChanged(float pXOffset, float pYOffset, float pXOffsetStep, float pYOffsetStep, int pXPixelOffset, int pYPixelOffset) {
            BaseLiveWallpaperService.this.onOffsetsChanged(pXOffset, pYOffset, pXOffsetStep, pYOffsetStep, pXPixelOffset, pYPixelOffset);
        }

        public void onResume() {
            super.onResume();
            BaseLiveWallpaperService.this.getEngine().onReloadResources();
            BaseLiveWallpaperService.this.onResume();
        }

        public void onPause() {
            super.onPause();
            BaseLiveWallpaperService.this.onPause();
        }

        public void onDestroy() {
            super.onDestroy();
            this.mEngineRenderer = null;
        }
    }

    public void onCreate() {
        Debug.d(new StringBuilder(String.valueOf(getClass().getSimpleName())).append(".onCreate").append(" @(Thread: '").append(Thread.currentThread().getName()).append("')").toString());
        super.onCreate();
        this.mGamePaused = true;
        this.mEngineOptions = onCreateEngineOptions();
        this.mEngine = onCreateEngine(this.mEngineOptions);
        this.mEngine.startUpdateThread();
        applyEngineOptions();
    }

    public AndEngine onCreateEngine(EngineOptions pEngineOptions) {
        return new AndEngine(pEngineOptions);
    }

    public synchronized void onSurfaceCreated(GLState pGLState) {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onSurfaceCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        if (this.mGameCreated) {
            onReloadResources();
        } else if (this.mCreateGameCalled) {
            this.mOnReloadResourcesScheduled = true;
        } else {
            this.mCreateGameCalled = true;
            onCreateGame();
        }
    }

    public void onSurfaceChanged(GLState pGLState, int pWidth, int pHeight) {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onSurfaceChanged(Width=" + pWidth + ",  Height=" + pHeight + ")" + " @(Thread: '" + Thread.currentThread().getName() + "')");
    }

    protected void onCreateGame() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onCreateGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        final OnPopulateSceneCallback onPopulateSceneCallback = new C00251();
        final OnCreateSceneCallback onCreateSceneCallback = new OnCreateSceneCallback() {
            public void onCreateSceneFinished(Scene pScene) {
                BaseLiveWallpaperService.this.mEngine.setScene(pScene);
                try {
                    Debug.d(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onPopulateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                    BaseLiveWallpaperService.this.onPopulateScene(pScene, onPopulateSceneCallback);
                } catch (Throwable pThrowable) {
                    Debug.e(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onPopulateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
                }
            }
        };
        OnCreateResourcesCallback onCreateResourcesCallback = new OnCreateResourcesCallback() {
            public void onCreateResourcesFinished() {
                try {
                    Debug.d(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onCreateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                    BaseLiveWallpaperService.this.onCreateScene(onCreateSceneCallback);
                } catch (Throwable pThrowable) {
                    Debug.e(String.valueOf(BaseLiveWallpaperService.this.getClass().getSimpleName()) + ".onCreateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
                }
            }
        };
        try {
            Debug.d(String.valueOf(getClass().getSimpleName()) + ".onCreateResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
            onCreateResources(onCreateResourcesCallback);
        } catch (Throwable pThrowable) {
            Debug.e(String.valueOf(getClass().getSimpleName()) + ".onCreateGame failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
        }
    }

    public synchronized void onGameCreated() {
        this.mGameCreated = true;
        if (this.mOnReloadResourcesScheduled) {
            this.mOnReloadResourcesScheduled = false;
            try {
                onReloadResources();
            } catch (Throwable pThrowable) {
                Debug.e(String.valueOf(getClass().getSimpleName()) + ".onReloadResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
            }
        }
    }

    protected synchronized void onResume() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onResume" + " @(Thread: '" + Thread.currentThread().getName() + "')");
    }

    public synchronized void onResumeGame() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onResumeGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        this.mEngine.start();
        this.mGamePaused = false;
    }

    public void onReloadResources() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onReloadResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        this.mEngine.onReloadResources();
        onResumeGame();
    }

    protected void onPause() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onPause" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        if (!this.mGamePaused) {
            onPauseGame();
        }
    }

    public void onPauseGame() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onPauseGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        this.mGamePaused = true;
        this.mEngine.stop();
    }

    public void onDestroy() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onDestroy" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        super.onDestroy();
        this.mEngine.onDestroy();
        try {
            onDestroyResources();
        } catch (Throwable pThrowable) {
            Debug.e(String.valueOf(getClass().getSimpleName()) + ".onDestroyResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
        }
        onGameDestroyed();
    }

    public void onDestroyResources() throws Exception {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onDestroyResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        if (this.mEngine.getEngineOptions().getAudioOptions().needsMusic()) {
            this.mEngine.getMusicManager().releaseAll();
        }
        if (this.mEngine.getEngineOptions().getAudioOptions().needsSound()) {
            this.mEngine.getSoundManager().releaseAll();
        }
    }

    public synchronized void onGameDestroyed() {
        Debug.d(String.valueOf(getClass().getSimpleName()) + ".onGameDestroyed" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        this.mGameCreated = false;
    }

    public AndEngine getEngine() {
        return this.mEngine;
    }

    public boolean isGamePaused() {
        return this.mGamePaused;
    }

    public boolean isGameRunning() {
        return !this.mGamePaused;
    }

    public boolean isGameLoaded() {
        return this.mGameCreated;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager() {
        return this.mEngine.getVertexBufferObjectManager();
    }

    public TextureManager getTextureManager() {
        return this.mEngine.getTextureManager();
    }

    public FontManager getFontManager() {
        return this.mEngine.getFontManager();
    }

    public ShaderProgramManager getShaderProgramManager() {
        return this.mEngine.getShaderProgramManager();
    }

    public SoundManager getSoundManager() throws IllegalStateException {
        return this.mEngine.getSoundManager();
    }

    public MusicManager getMusicManager() throws IllegalStateException {
        return this.mEngine.getMusicManager();
    }

    public WallpaperService.Engine onCreateEngine() {
        return new BaseWallpaperGLEngine(this);
    }

    protected void onTap(int pX, int pY) {
        this.mEngine.onTouch(null, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float) pX, (float) pY, 0));
    }

    protected void onDrop(int pX, int pY) {
    }

    protected void onOffsetsChanged(float pXOffset, float pYOffset, float pXOffsetStep, float pYOffsetStep, int pXPixelOffset, int pYPixelOffset) {
    }

    protected void applyEngineOptions() {
    }

    protected boolean enableVibrator() {
        return this.mEngine.enableVibrator(this);
    }

    protected boolean enableAccelerationSensor(IAccelerationListener pAccelerationListener) {
        return this.mEngine.enableAccelerationSensor(this, pAccelerationListener);
    }

    protected boolean enableOrientationSensor(IOrientationListener pOrientationListener) {
        return this.mEngine.enableOrientationSensor(this, pOrientationListener);
    }
}
