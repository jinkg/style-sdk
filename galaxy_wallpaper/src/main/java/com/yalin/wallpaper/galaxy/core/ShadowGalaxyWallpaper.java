package com.yalin.wallpaper.galaxy.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yalin.wallpaper.galaxy.ParticleEmitter;
import com.yalin.wallpaper.galaxy.ParticleEmitter.ScaledNumericValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class ShadowGalaxyWallpaper extends LibdgxWallpaperApp implements InputProcessor {
    private static final int emiSize = 2;
    private static final String imgDir = "shadow";
    public static int maxFreq = getMaxFreq();
    private static final int maxWH = 1000;
    public static int numCores = getNumCores();
    private static float wind = 0.0f;
    private long FPS = 25;
    private String HDtexture = "shadow2048-7.jpg";
    private String OLDtexture = "shadow.jpg";
    private Array<ParticleEmitter> ParticleE = null;
    private Array<ParticleEmitter> ParticleEFull = null;
    private boolean accON;
    private boolean anim = false;
    private float ax;
    float[] axT = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    float[] axT2;
    private float axd;
    private int axi = 0;
    private float ay;
    float[] ayT = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    float[] ayT2;
    private float ayd;
    private float cX;
    private float cY;
    private float delta = 1.0f;
    private int dx;
    private float dxx;
    private ParticleEffectEnc effect;
    private float emX;
    private float emXnew;
    private float emY;
    private String emiFile = "/assets/shadow/shadow.parc";
    private boolean hdON;
    private boolean load = false;
    private int maxP = 10;
    private int oldx;
    private int orient;
    private int pref = 0;
    private SpriteBatch spriteBatch;
    private boolean stars;
    private boolean tab = false;
    private float u_scale;
    private float[] f110w = new float[]{0.0f};
    private float wind2;
    private float xadM = 1.0f;
    private float yadM = 1.0f;

    public ShadowGalaxyWallpaper() {
    }

    public void create() {
        if (Gdx.graphics.getWidth() > 1000 || Gdx.graphics.getHeight() > 1000) {
            this.tab = true;
            if (numCores >= 2 && maxFreq > 1200000) {
                this.tab = false;
            }
        } else {
            this.tab = false;
        }
        this.tab = true;
        if (this.tab) {
            this.emiFile = "/assets/shadow/shadowT.parc";
        }
        Gdx.input.setInputProcessor(this);
        onSharedPreferenceChanged();
        this.spriteBatch = null;
        this.effect = null;
        this.spriteBatch = new SpriteBatch();
        this.effect = new ParticleEffectEnc();
        boolean loading = true;
        while (loading) {
            try {
                this.effect.loadEmitters(this.emiFile);
                if (this.hdON) {
                    this.effect.loadEmittersImages(Gdx.files.internal(imgDir), this.HDtexture, "star.png");
                } else {
                    this.effect.loadEmittersImages(Gdx.files.internal(imgDir), this.OLDtexture, "star.png");
                }
                loading = false;
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                }
                loading = true;
            }
        }
        this.ParticleE = null;
        this.ParticleE = this.effect.getEmitters();
        if (this.ParticleE != null) {
            this.ParticleEFull = new Array<>(this.effect.getEmitters());
            this.ParticleE.get(0).setMaxParticleCount(this.maxP);
        }
        if (Gdx.graphics.getHeight() < Gdx.graphics.getWidth()) {
            this.u_scale = ((float) Gdx.graphics.getHeight()) / 600.0f;
        } else {
            this.u_scale = ((float) Gdx.graphics.getHeight()) / 800.0f;
        }
        if (this.u_scale > 1.4f) {
            if (this.u_scale <= 1.8f) {
                this.u_scale *= 0.85f;
            } else if (this.u_scale <= 2.5f) {
                this.u_scale *= 0.85f;
            } else if (this.u_scale <= 5.0f) {
                this.u_scale *= 0.85f;
            }
            ScaledNumericValue gs = this.ParticleE.get(0).getScale();
            float lMin = gs.getLowMin();
            float lMax = gs.getLowMax();
            float hMin = gs.getHighMin();
            float hMax = gs.getHighMax();
            this.ParticleE.get(0).getScale().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(0).getScale().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(0).getScale().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(0).getScale().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            ScaledNumericValue gSH = (this.ParticleE.get(0)).getSpawnHeight();
            lMin = gSH.getLowMin();
            lMax = gSH.getLowMax();
            hMin = gSH.getHighMin();
            hMax = gSH.getHighMax();
            this.ParticleE.get(0).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(0).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(0).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(0).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            ScaledNumericValue gSW = (this.ParticleE.get(0)).getSpawnWidth();
            lMin = gSW.getLowMin();
            lMax = gSW.getLowMax();
            hMin = gSW.getHighMin();
            hMax = gSW.getHighMax();
            this.ParticleE.get(0).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(0).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(0).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(0).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            gs = this.ParticleE.get(1).getScale();
            lMin = gs.getLowMin();
            lMax = gs.getLowMax();
            hMin = gs.getHighMin();
            hMax = gs.getHighMax();
            this.ParticleE.get(1).getScale().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(1).getScale().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(1).getScale().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(1).getScale().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            gSH = this.ParticleE.get(1).getSpawnHeight();
            lMin = gSH.getLowMin();
            lMax = gSH.getLowMax();
            hMin = gSH.getHighMin();
            hMax = gSH.getHighMax();
            this.ParticleE.get(1).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(1).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(1).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(1).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            gSW = this.ParticleE.get(1).getSpawnWidth();
            lMin = gSW.getLowMin();
            lMax = gSW.getLowMax();
            hMin = gSW.getHighMin();
            hMax = gSW.getHighMax();
            this.ParticleE.get(1).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleE.get(1).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            this.ParticleEFull.get(1).getSpawnHeight().setLow(this.u_scale * lMin, this.u_scale * lMax);
            this.ParticleEFull.get(1).getSpawnHeight().setHigh(this.u_scale * hMin, this.u_scale * hMax);
            if (this.u_scale <= 1.5f) {
                this.ParticleE.get(1).getLife().setHighMax(15000.0f);
                this.ParticleEFull.get(1).getLife().setHighMax(15000.0f);
            } else if (this.u_scale <= 5.0f) {
                this.ParticleE.get(1).getLife().setHighMax(30000.0f);
                this.ParticleEFull.get(1).getLife().setHighMax(30000.0f);
            } else {
                this.ParticleE.get(1).getLife().setHighMax(50000.0f);
                this.ParticleEFull.get(1).getLife().setHighMax(50000.0f);
            }
        }
        if (!this.stars) {
            this.effect.getEmitters().removeIndex(1);
        }
        ((ParticleEmitter) this.ParticleE.get(0)).getSprite().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.orient = 0;
        this.axT2 = this.axT;
        this.ayT2 = this.ayT;
        this.xadM = 1.0f;
        this.yadM = 1.0f;
        this.orient = Gdx.input.getRotation();
        switch (this.orient) {
            case 0:
                this.axT2 = this.axT;
                this.ayT2 = this.ayT;
                this.xadM = 1.0f;
                this.yadM = 1.0f;
                break;
            case 1:
                this.axT2 = this.ayT;
                this.ayT2 = this.axT;
                this.xadM = 1.0f;
                this.yadM = 1.0f;
                break;
            case 2:
                this.axT2 = this.axT;
                this.ayT2 = this.ayT;
                this.xadM = -1.0f;
                this.yadM = -1.0f;
                break;
            case 3:
                this.axT2 = this.ayT;
                this.ayT2 = this.axT;
                this.xadM = -1.0f;
                this.yadM = -1.0f;
                break;
        }
        this.cX = (float) (Gdx.graphics.getWidth() / 2);
        this.cY = (float) (Gdx.graphics.getHeight() / 2);
        this.emX = this.cX;
        this.emY = this.cY;
        this.dxx = 0.0f;
        this.effect.setPosition(this.emX, this.emY);
        this.load = true;
    }

    public void dispose() {
    }

    public void pause() {
    }

    public void resume() {
        switch (this.pref) {
            case 4:
                setFPS(this.FPS);
                break;
            case 5:
                this.effect.getEmitters();
                if (!this.stars) {
                    if (this.effect.getEmitters().size == 2) {
                        this.effect.getEmitters().removeIndex(1);
                        break;
                    }
                } else if (this.effect.getEmitters().size == 1) {
                    this.effect.getEmitters().add(this.ParticleEFull.get(1));
                    this.ParticleE = this.effect.getEmitters();
                    this.effect.setPosition(this.emX, this.emY);
                    break;
                }
                break;
            case 6:
                try {
                    if (this.hdON) {
                        this.effect.loadEmittersImages(Gdx.files.internal(imgDir), this.HDtexture, "star.png");
                    } else {
                        this.effect.loadEmittersImages(Gdx.files.internal(imgDir), this.OLDtexture, "star.png");
                    }
                    this.ParticleE.get(0).getSprite().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                    break;
                } catch (Exception e) {
                    break;
                }
        }
        this.pref = 0;
    }

    public void render() {
        if (this.load) {
            GL20 gl = Gdx.graphics.getGL20();
            if (gl != null) {
                gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                gl.glClear(16384);
                this.spriteBatch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
                if (Gdx.graphics.getDeltaTime() > 0.001f) {
                    this.delta = Gdx.graphics.getDeltaTime();
                }
                try {
                    this.spriteBatch.begin();
                    this.effect.draw(this.spriteBatch, this.delta);
                    this.spriteBatch.end();
                } catch (Exception e) {
                    try {
                        this.spriteBatch.end();
                    } catch (Exception e2) {
                    }
                }
                if (this.anim) {
                    if (this.accON) {
                        this.ax = Gdx.input.getAccelerometerX();
                        this.ay = Gdx.input.getAccelerometerY();
                        this.axT[this.axi] = this.ax;
                        this.ayT[this.axi] = this.ay;
                        this.axi++;
                        if (this.axi > 9) {
                            this.axi = 0;
                        }
                        this.axd = 0.0f;
                        this.ayd = 0.0f;
                        for (int i = 0; i < 10; i++) {
                            this.axd += this.axT2[i];
                        }
                        if (this.axd < 60.0f && this.axd > -60.0f) {
                            this.wind2 = (this.axd * 0.03f) * this.xadM;
                        }
                    }
                    if (testEmiters()) {
                        wind *= 0.99f;
                        this.wind2 *= 0.99f;
                    } else {
                        wind *= 0.99f;
                        this.wind2 *= 0.99f;
                    }
                    try {
                        this.f110w = ((ParticleEmitter) this.ParticleE.get(0)).getWind().getScaling();
                        this.f110w[0] = ((wind + this.wind2) / 2.0f) + 0.5f;
                        this.f110w[1] = 0.5f - (wind + this.wind2);
                        if (this.stars) {
                            ((ParticleEmitter) this.ParticleE.get(1)).getWind().setScaling(this.f110w);
                        }
                    } catch (Exception e3) {
                    }
                }
            }
        }
    }

    public void resize(int width, int height) {
        if (this.load) {
            GL20 gl = Gdx.graphics.getGL20();
            if (gl != null) {
                gl.glClear(16384);
                this.cX = (float) (Gdx.graphics.getWidth() / 2);
                this.cY = (float) (Gdx.graphics.getHeight() / 2);
                this.emXnew = this.cX;
                this.emX = this.cX;
                this.emY = this.cY;
                this.dxx = 0.0f;
                this.effect.setPosition(this.emX, this.emY);
            }
        }
    }

    public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
    }

    @Override
    public void previewStateChange(boolean isPreview) {

    }

    public void onSharedPreferenceChanged() {
        this.anim = true;
        this.accON = true;
        this.hdON = true;
        this.stars = true;
        this.maxP = 10;
        this.FPS = 25;
        setFPS(this.FPS);
    }

    private void setFPS(long ms) {
        if (ms == 40) {
            this.maxP = 8;
        } else if (ms == 30) {
            this.maxP = 9;
        } else if (ms == 25) {
            this.maxP = 10;
        } else if (ms == 1) {
            this.maxP = 11;
        }
        if (testEmiters()) {
            ((ParticleEmitter) this.ParticleE.get(0)).setMaxParticleCount(this.maxP);
        }
//        ((AndroidGraphics) Gdx.graphics).setMSSleep(ms);
    }

    public boolean keyDown(int arg0) {
        return false;
    }

    public boolean keyTyped(char arg0) {
        return false;
    }

    public boolean keyUp(int arg0) {
        return false;
    }

    public boolean scrolled(int arg0) {
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int newParam) {
        this.oldx = x;
        this.emX = (float) x;
        this.emY = (float) (Gdx.graphics.getHeight() - y);
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        this.dx = this.oldx - x;
        this.emX = (float) x;
        this.emY = (float) (Gdx.graphics.getHeight() - y);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean touchMoved(int arg0, int arg1) {
        return false;
    }

    public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        if (this.load && this.anim) {
            wind = 0.0f;
            if (this.tab) {
                if (this.dx > 0) {
                    wind = 0.5f;
                } else {
                    wind = -0.6f;
                }
            } else if (this.dx > 0) {
                wind = 0.6f;
            } else {
                wind = -0.7f;
            }
            if (testEmiters()) {
                try {
                } catch (Exception e) {
                }
            }
            this.f110w = ((ParticleEmitter) this.ParticleE.get(0)).getWind().getScaling();
            this.f110w[0] = ((wind + this.wind2) / 2.0f) + 0.5f;
            this.f110w[1] = 0.5f - (wind + this.wind2);
            if (this.stars) {
                ((ParticleEmitter) this.ParticleE.get(1)).getWind().setScaling(this.f110w);
            }
        }
        return false;
    }

    private boolean testEmiters() {
        if (this.ParticleE == null || this.ParticleE.size != 2) {
            return false;
        }
        return true;
    }

    public void touchDrop(int x, int y) {
    }

    public void touchTap(int x, int y) {
    }

    private static int getNumCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            }).length;
        } catch (Exception e) {
            return 1;
        }
    }

    private static int getMaxFreq() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")), 20);
            String load = reader.readLine();
            reader.close();
            return Integer.parseInt(load);
        } catch (Exception e) {
            return 0;
        }
    }
}
