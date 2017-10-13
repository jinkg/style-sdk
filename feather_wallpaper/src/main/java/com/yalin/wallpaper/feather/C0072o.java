package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.input.GestureDetector.GestureListener;
import com.badlogic1.gdx.math.Intersector;
import com.badlogic1.gdx.math.Vector2;

class C0072o implements GestureListener {
    boolean f274a = false;
    float f276c = 0.0f;
    float f277d = 0.0f;
    float f278e = 0.0f;
    float f279f = 0.0f;
    float f280g = 0.0f;
    float f281h = 6.0f;
    final /* synthetic */ MyApplicationListener f282i;

    C0072o(MyApplicationListener myApplicationListener) {
        this.f282i = myApplicationListener;
    }

    public void m226a(int i) {
        if (this.f282i.water) {
            this.f281h = 0.3f;
            this.f277d = this.f282i.f227p;
            this.f280g = this.f282i.f229r / ((float) (i - 1));
            return;
        }
        this.f281h = 6.0f;
        this.f276c = (float) C0030d.m136c();
        this.f277d = (((float) C0030d.m131a()) - this.f276c) / 2.0f;
        this.f280g = Math.abs((((float) C0030d.m131a()) - this.f276c) / ((float) (i - 1)));
    }

    @Override
    public boolean touchDown(float f, float f2, int i, int i2) {
        if (this.f282i.f213b == C0073p.Running) {
            this.f274a = false;
            this.f279f = 0.0f;
            if (this.f282i.touch && this.f282i.particle) {
                this.f282i.f217f.unproject(this.f282i.f219h.set(f, f2, 0.0f));
                for (C0069q a : this.f282i.f233v) {
                    a.m211a(this.f282i.f219h.x, this.f282i.f219h.y);
                }
            }
            if (this.f282i.water) {
                Intersector.intersectRayPlane(this.f282i.f203Q.getPickRay(f, f2), this.f282i.f205S, this.f282i.f206T);
                this.f282i.m191a(this.f282i.f206T);
            }
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        this.f274a = true;
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (this.f282i.f213b == C0073p.Running) {
            if (this.f282i.water && this.f282i.dragging) {
                Intersector.intersectRayPlane(this.f282i.f203Q.getPickRay(x, y), this.f282i.f205S, this.f282i.f206T);
                if (((float) ((int) this.f282i.f206T.x)) < this.f282i.f207U.x - 1.0f
                        || ((float) ((int) this.f282i.f206T.x)) > this.f282i.f207U.x + 1.0f
                        || ((float) ((int) this.f282i.f206T.y)) < this.f282i.f207U.y - 1.0f
                        || ((float) ((int) this.f282i.f206T.y)) > this.f282i.f207U.y + 1.0f) {
                    this.f282i.m191a(this.f282i.f206T);
                    this.f282i.f207U.x = (float) ((int) this.f282i.f206T.x);
                    this.f282i.f207U.y = (float) ((int) this.f282i.f206T.y);
                }
            }
            if (this.f282i.f212Z) {
                if (this.f282i.water) {
                    if (this.f282i.f146a || !this.f282i.scrolling) {
                        this.f277d = this.f282i.f227p;
                        this.f282i.f228q = this.f277d;
                    } else if (deltaX != 0.0f) {
                        this.f278e = deltaX > 0.0f ? this.f281h : -this.f281h;
                        this.f279f += this.f278e;
                        if (this.f278e < 0.0f) {
                            if (this.f279f > 0.0f) {
                                this.f279f = 0.0f;
                            } else if (this.f279f >= (-this.f280g)) {
                                this.f277d -= this.f278e;
                            }
                        } else if (this.f278e > 0.0f) {
                            if (this.f279f < 0.0f) {
                                this.f279f = 0.0f;
                            } else if (this.f279f <= this.f280g) {
                                this.f277d -= this.f278e;
                            }
                        }
                        if (this.f277d >= this.f282i.f227p + (this.f282i.f229r / 2.0f)) {
                            this.f277d = this.f282i.f227p + (this.f282i.f229r / 2.0f);
                        } else if (this.f277d <= this.f282i.f227p - (this.f282i.f229r / 2.0f)) {
                            this.f277d = this.f282i.f227p - (this.f282i.f229r / 2.0f);
                        }
                        this.f282i.f228q = this.f277d;
                    }
                } else if (this.f282i.f146a || !this.f282i.scrolling) {
                    this.f277d = (((float) C0030d.m131a()) - this.f276c) / 2.0f;
                    this.f282i.f230s.f186g.x = this.f277d;
                } else if (deltaX != 0.0f) {
                    this.f278e = deltaX > 0.0f ? this.f281h : -this.f281h;
                    this.f279f += this.f278e;
                    if (this.f278e < 0.0f) {
                        if (this.f279f > 0.0f) {
                            this.f279f = 0.0f;
                        } else if (this.f279f >= (-this.f280g)) {
                            this.f277d += this.f278e;
                        }
                    } else if (this.f278e > 0.0f) {
                        if (this.f279f < 0.0f) {
                            this.f279f = 0.0f;
                        } else if (this.f279f <= this.f280g) {
                            this.f277d += this.f278e;
                        }
                    }
                    if (this.f277d >= 0.0f) {
                        this.f277d = 0.0f;
                    } else if (this.f277d <= ((float) C0030d.m131a()) - this.f276c) {
                        this.f277d = ((float) C0030d.m131a()) - this.f276c;
                    }
                    this.f282i.f230s.f186g.x = this.f277d;
                }
            }
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }
}
