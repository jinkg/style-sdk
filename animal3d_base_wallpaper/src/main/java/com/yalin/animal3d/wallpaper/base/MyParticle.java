package com.yalin.animal3d.wallpaper.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;

import java.util.Timer;
import java.util.TimerTask;

public class MyParticle extends Actor {
    float Sx;
    float Sx1;
    float Sy;
    float Sy1;
    float f404a = 0.5f;
    float f405b = 0.6f;
    float f406c = 0.7f;
    float f407d = 1.2f;
    ParticleEffect effect;
    ParticleEmitter emitter;
    Array<ParticleEmitter> emitters;
    int f408h;
    float level1 = 0.6f;
    float level3 = 1.4f;
    private MoveToAction moveToAction;
    int particleCount;
    int particleCount1 = -1;
    int particleKind;
    int particleKind1 = -1;
    boolean particleParallax;
    boolean particleParallax1;
    int particleSize;
    int particleSize1 = -1;
    int particleSpeed;
    int particleSpeed1 = -1;
    String[] particlekinds;
    int pmaxCount;
    int pmaxSize;
    int pmaxSpeed;
    int pminCount;
    int pminSize;
    int pminSpeed;
    TimerTask task = new C01281();
    Timer timer;
    float transx;
    float transy;
    int f409w;
    int w1;

    class C01281 extends TimerTask {
        C01281() {
        }

        public void run() {
            if (MyParticle.this.particleParallax) {
                MyParticle.this.ParticleMove(true);
            }
        }
    }

    public MyParticle() {
        this.f409w = Gdx.graphics.getWidth();
        this.f408h = Gdx.graphics.getHeight();
        this.effect = new ParticleEffect();
        this.particlekinds = new String[]{"p2.p"};
        this.moveToAction = Actions.moveTo(0.0f, 0.0f, 1.0f);
        this.timer = new Timer();
        this.timer.schedule(this.task, 1000, 200);
        CheckParticleAttributes();
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(16384);
        setPosion(this.effect, this.f409w, this.f408h, getX(), getY(), this.particleKind);
        this.effect.draw(batch, delta);
    }

    void CheckParticleAttributes() {
        this.f409w = Gdx.graphics.getWidth();
        this.f408h = Gdx.graphics.getHeight();
        this.particleKind = 0;
        this.particleCount = 0;
        this.particleSize = 0;
        this.particleSpeed = 0;
        this.particleParallax = true;
        if (this.particleKind != this.particleKind1) {
            setParticleKind(this.f409w, this.f408h, this.particleKind);
        }
        if (this.particleCount != this.particleCount1) {
            setParticleCount(this.particleCount);
        }
        if (this.particleSize != this.particleSize1) {
            setParticleSize(this.particleSize);
        }
        if (this.particleSpeed != this.particleSpeed1) {
            setParticleSpeed(this.particleSpeed);
        }
        if (this.particleParallax != this.particleParallax1) {
            if (this.particleParallax) {
                this.moveToAction = Actions.moveTo(0.0f, 0.0f, 1.0f);
                addAction(Actions.forever(Actions.repeat(3, this.moveToAction)));
            } else {
                clearActions();
                setX(0.0f);
                setY(0.0f);
            }
        }
        if (this.f409w != this.w1) {
            setSpawnSize(this.f409w, this.f408h, this.particleKind);
        }
        this.particleKind1 = this.particleKind;
        this.particleCount1 = this.particleCount;
        this.particleSize1 = this.particleSize;
        this.particleSpeed1 = this.particleSpeed;
        this.particleParallax1 = this.particleParallax;
        this.w1 = this.f409w;
    }

    void setParticleCount(int particleCount) {
        for (int i = 0; i < this.emitters.size; i++) {
            this.emitter = this.emitters.get(i);
            if (particleCount == 0) {
                this.emitter.setMinParticleCount(this.pminCount);
                this.emitter.setMaxParticleCount(this.pmaxCount);
            } else if (particleCount == 1) {
                this.emitter.setMinParticleCount((int) (((float) this.pminCount) * this.level1));
                this.emitter.setMaxParticleCount((int) (((float) this.pmaxCount) * this.level1));
            } else if (particleCount == 2) {
                this.emitter.setMinParticleCount((int) (((float) this.pminCount) * this.level3));
                this.emitter.setMaxParticleCount((int) (((float) this.pmaxCount) * this.level3));
            }
        }
    }

    void setParticleSize(int particleSize) {
        for (int i = 0; i < this.emitters.size; i++) {
            this.emitter = this.emitters.get(i);
            if (particleSize == 0) {
                this.emitter.getScale().setHighMin((float) this.pminSize);
                this.emitter.getScale().setHighMax((float) this.pmaxSize);
            } else if (particleSize == 1) {
                this.emitter.getScale().setHighMin((float) ((int) (((float) this.pminSize) * this.level1)));
                this.emitter.getScale().setHighMax((float) ((int) (((float) this.pmaxSize) * this.level1)));
            } else if (particleSize == 2) {
                this.emitter.getScale().setHighMin((float) ((int) (((float) this.pminSize) * this.level3)));
                this.emitter.getScale().setHighMax((float) ((int) (((float) this.pmaxSize) * this.level3)));
            }
        }
    }

    void setParticleSpeed(int particleSpeed) {
        for (int i = 0; i < this.emitters.size; i++) {
            this.emitter = this.emitters.get(i);
            if (particleSpeed == 0) {
                this.emitter.getVelocity().setHighMin((float) this.pminSpeed);
                this.emitter.getVelocity().setHighMax((float) this.pminSpeed);
            } else if (particleSpeed == 1) {
                this.emitter.getVelocity().setHighMin((float) ((int) (((float) this.pminSpeed) * this.level1)));
                this.emitter.getVelocity().setHighMax((float) ((int) (((float) this.pminSpeed) * this.level1)));
            } else if (particleSpeed == 2) {
                this.emitter.getVelocity().setHighMin((float) ((int) (((float) this.pminSpeed) * this.level3)));
                this.emitter.getVelocity().setHighMax((float) ((int) (((float) this.pmaxSpeed) * this.level3)));
            }
        }
    }

    void setPosion(ParticleEffect effect, int w, int h, float x, float y, int particleKind) {
        if (particleKind == 0) {
            effect.setPosition((((float) w) * 0.3f) + x, (((float) h) * 0.4f) + y);
        } else if (particleKind == 1) {
            effect.setPosition((((float) w) * 0.3f) + x, (((float) h) * 0.4f) + y);
        } else if (particleKind == 2) {
            effect.setPosition((((float) w) * 0.3f) + x, (((float) h) * 0.4f) + y);
        }
    }

    void setSpawnSize(int w, int h, int particleKind) {
        int i;
        if (particleKind == 0) {
            for (i = 0; i < this.emitters.size; i++) {
                this.emitter = this.emitters.get(i);
                this.emitter.getSpawnHeight().setHigh((float) h);
                this.emitter.getSpawnHeight().setLow((float) h);
                this.emitter.getSpawnWidth().setHigh((float) w);
                this.emitter.getSpawnWidth().setLow((float) w);
            }
        } else if (particleKind == 1) {
            for (i = 0; i < this.emitters.size; i++) {
                this.emitter = this.emitters.get(i);
                this.emitter.getSpawnHeight().setHigh((float) h);
                this.emitter.getSpawnHeight().setLow((float) h);
                this.emitter.getSpawnWidth().setHigh((float) w);
                this.emitter.getSpawnWidth().setLow((float) w);
            }
        } else if (particleKind == 2) {
            for (i = 0; i < this.emitters.size; i++) {
                this.emitter = this.emitters.get(i);
                this.emitter.getSpawnHeight().setHigh((float) h);
                this.emitter.getSpawnHeight().setLow((float) h);
                this.emitter.getSpawnWidth().setHigh((float) w);
                this.emitter.getSpawnWidth().setLow((float) w);
            }
        }
    }

    void setParticleKind(int w, int h, int particlekind) {
        this.effect.load(Gdx.files.internal("particle/" + this.particlekinds[particlekind]),
                Gdx.files.internal("particle/"));
        this.emitters = this.effect.getEmitters();
        this.emitter = this.emitters.first();
        this.particleKind1 = -1;
        this.particleCount1 = -1;
        this.particleSize1 = -1;
        this.particleSpeed1 = -1;
        this.w1 = 0;
        if (w < h) {
            if (w <= 480) {
                initAttribute(this.f404a);
            } else if (w > 480 && w <= 720) {
                initAttribute(this.f405b);
            } else if (w > 720 && w < 1080) {
                initAttribute(this.f406c);
            } else if (w < 1080 || w >= 1440) {
                initAttribute(this.f407d);
            } else {
                initAttribute(1.0f);
            }
        } else if (h <= 480) {
            initAttribute(this.f404a);
        } else if (h > 480 && h <= 720) {
            initAttribute(this.f405b);
        } else if (h > 720 && h < 1080) {
            initAttribute(this.f406c);
        } else if (h < 1080 || h >= 1440) {
            initAttribute(this.f407d);
        } else {
            initAttribute(1.0f);
        }
    }

    void initAttribute(float x) {
        this.pminCount = (int) (((float) this.emitter.getMinParticleCount()) * x);
        this.pmaxCount = (int) (((float) this.emitter.getMaxParticleCount()) * x);
        this.pminSize = (int) (this.emitter.getScale().getHighMin() * x);
        this.pmaxSize = (int) (this.emitter.getScale().getHighMax() * x);
        this.pminSpeed = (int) (this.emitter.getVelocity().getHighMax() * x);
        this.pmaxSpeed = (int) (this.emitter.getVelocity().getHighMin() * x);
    }

    void ParticleMove(boolean isYmove) {
        if (this.particleParallax) {
            this.Sx1 = this.Sx;
            this.Sy1 = this.Sy;
            this.Sx = Gdx.input.getAccelerometerX();
            this.Sy = Gdx.input.getAccelerometerY();
            this.transx = Math.abs(this.Sx - this.Sx1);
            this.transy = Math.abs(this.Sy - this.Sy1);
            if (this.transx >= 0.3f || this.transy >= 0.3f) {
                if (this.transx >= 0.3f && this.transx < 10.0f) {
                    this.moveToAction.setX(this.Sx * ((((float) this.f409w) * 0.15f) / 10.0f));
                }
                if (isYmove && this.transy >= 0.3f && this.transy < 10.0f) {
                    this.moveToAction.setY(this.Sy * ((((float) this.f408h) * 0.15f) / 10.0f));
                }
                this.moveToAction.setDuration(0.2f);
                this.moveToAction.restart();
            }
        }
    }
}
