package com.yalin.animal3d.wallpaper.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;

public class MyFingerParticle {
    float f83a = 0.5f;
    float f84b = 0.6f;
    float f85c = 0.7f;
    float f86d = 1.2f;
    ParticleEffect effect;
    int effectIndex;
    String[] effectkinds = new String[]{"f1.p"};
    ParticleEmitter emitter;
    Array<ParticleEmitter> emitters;
    int pmaxCount;
    int pmaxSize;
    int pmaxSpeed;
    int pminCount;
    int pminSize;
    int pminSpeed;

    public MyFingerParticle(int w, int h) {
        intFinger(w, h);
    }

    void intFinger(int w, int h) {
        this.effectIndex = 0;
        this.effect = new ParticleEffect();
        this.effect.load(Gdx.files.internal("particle/" + this.effectkinds[this.effectIndex]),
                Gdx.files.internal("particle/"));
        this.emitters = this.effect.getEmitters();
        this.emitter = this.emitters.first();
        this.effect.allowCompletion();
        setParticleKind(w, h);
    }

    void setParticleKind(int w, int h) {
        if (w < h) {
            if (w <= 480) {
                initAttribute(this.f83a);
            } else if (w > 480 && w <= 720) {
                initAttribute(this.f84b);
            } else if (w > 720 && w < 1080) {
                initAttribute(this.f85c);
            } else if (w < 1080 || w >= 1440) {
                initAttribute(this.f86d);
            } else {
                initAttribute(1.0f);
            }
        } else if (h <= 480) {
            initAttribute(this.f83a);
        } else if (h > 480 && h <= 720) {
            initAttribute(this.f84b);
        } else if (h > 720 && h < 1080) {
            initAttribute(this.f85c);
        } else if (h < 1080 || h >= 1440) {
            initAttribute(this.f86d);
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
        setParticleS();
    }

    void setParticleS() {
        for (int i = 0; i < this.emitters.size; i++) {
            this.emitter = this.emitters.get(i);
            this.emitter.setMinParticleCount(this.pminCount);
            this.emitter.setMaxParticleCount(this.pmaxCount);
            this.emitter.getScale().setHighMin((float) this.pminSize);
            this.emitter.getScale().setHighMax((float) this.pmaxSize);
            this.emitter.getVelocity().setHighMin((float) this.pminSpeed);
            this.emitter.getVelocity().setHighMax((float) this.pminSpeed);
        }
    }

    ParticleEffect getParticleEffect() {
        return this.effect;
    }
}
