package com.yalin.wallpaper.galaxy.base;

import android.content.Context;
import android.graphics.Color;
import android.service.wallpaper.WallpaperService;
import android.view.WindowManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class C0437b extends InputAdapter implements ApplicationListener, AndroidWallpaperListener {
    Decal f993A;
    GalaxyDecal[] f994B = null;
    int quatity = 60;
    Decal f996D;
    float speed = 1.0f;
    int direction = 1;
    boolean customColor = false;
    boolean f1000H = true;
    int f1001I = 0;
    C0431a f1002J;
    GestureDetector f1003K;
    C0432b f1004a = C0432b.Setup;
    boolean f1005b = false;
    WallpaperService f1006c;
    PerspectiveCamera f1008e;
    C0364b f1009f;
    DecalBatch f1010g;
    Vector3 f1011h;
    float f1012i;
    C0429a f1013j = new C0429a();
    int fps = 20;
    int f1015l = 0;
    long f1016m = 0;
    long f1017n = 0;
    Boolean scrolling = Boolean.TRUE;
    float f1021r = 200.0f;
    float f1022s = 0.0f;
    float f1023t = 0.0f;
    float f1024u = 0.0f;
    boolean f1025v = false;
    Decal f1026w;
    Decal f1027x;
    Decal f1028y;
    Decal f1029z;

    GalaxyConfig galaxyConfig;

    @Override
    public void create() {
        boolean z = true;
        this.f1013j.m1715a();
        this.f1002J = new C0431a();
        this.f1003K = new GestureDetector(this.f1002J);
        Gdx.input.setInputProcessor(this.f1003K);
        this.fps = 20;
        if (this.f1006c.getResources().getConfiguration().orientation != 2) {
            z = false;
        }
        C0365c.m1607a(z);
        onSharedPreferenceChanged();
    }

    @Override
    public void resize(int width, int height) {
        m1759g();
    }

    @Override
    public void render() {
        if (this.f1004a == C0432b.Setup) {
            m1765b();
        }
        if (this.f1004a == C0432b.Running) {
            this.f1012i = Gdx.graphics.getDeltaTime() * this.speed;
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl20.glClear(16640);
            float abs = ((Math.abs(this.f1023t) * -0.5f) - this.f1008e.position.x) * 0.2f;
            this.f1008e.translate(abs, abs, abs);
            this.f1008e.lookAt(0.0f, 0.0f, 0.0f);
            this.f1008e.update();
            this.f1026w.rotateZ((this.f1012i * 2.0f) * ((float) this.direction));
            this.f1028y.rotateZ((this.f1012i * 6.0f) * ((float) this.direction));
            this.f1027x.rotateZ((this.f1012i * 3.0f) * ((float) this.direction));
            this.f1029z.rotateZ((this.f1012i * 1.0f) * ((float) this.direction));
            this.f993A.rotateZ((this.f1012i * 3.0f) * ((float) this.direction));
            this.f996D.rotateZ((this.f1012i * 8.0f) * ((float) this.direction));
            try {
                this.f1010g.add(this.f1026w);
                this.f1010g.add(this.f1027x);
                this.f1010g.add(this.f1028y);
                this.f1010g.add(this.f993A);
                this.f1010g.add(this.f996D);
                for (int i = 0; i < this.f994B.length; i++) {
                    this.f994B[i].rotateZ((-this.f1012i) * 2.0f);
                    this.f994B[i].m1787a(this.f1012i);
                    this.f994B[i].m1790b(this.f1012i);
                    this.f994B[i].lookAt(this.f1008e.position, this.f1008e.up);
                    this.f1010g.add(this.f994B[i]);
                }
                this.f1010g.flush();
            } catch (Exception e) {
            }
            try {
                this.f1017n = System.currentTimeMillis() - this.f1016m;
                if (this.f1017n < ((long) this.f1015l)) {
                    Thread.sleep(((long) this.f1015l) - this.f1017n);
                    this.f1016m = System.currentTimeMillis();
                    return;
                }
                this.f1016m = System.currentTimeMillis();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override
    public void pause() {
        this.f1001I = 0;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        if (this.f1004a == C0432b.Running) {
            if (this.f1001I > 3) {
                this.f1000H = false;
            } else {
                this.f1001I++;
            }
            if (!this.f1000H) {
                if (this.f1005b || !this.scrolling) {
                    this.f1023t = this.f1022s;
                } else {
                    this.f1023t = this.f1022s + ((0.5f - xOffset) * this.f1021r);
                }
            }
        }
    }

    @Override
    public void previewStateChange(boolean isPreview) {
        this.f1005b = isPreview;
    }

    class C0431a implements GestureDetector.GestureListener {
        boolean f979a = false;
        float f981c = 0.0f;
        float f982d = 0.0f;
        float f983e = 0.0f;
        float f984f = 0.0f;
        float f985g = 0.0f;
        float f986h = 0.0f;
        float f987i = 0.0f;
        float f988j = 3.0f;

        C0431a() {

        }

        public void m1726a(int i) {
            this.f981c = (float) C0365c.m1609c();
            this.f987i = Math.abs(((((float) C0365c.m1604a()) - this.f981c) * 0.6f) / ((float) (i - 1)));
            this.f982d = C0437b.this.f1022s;
            C0437b.this.f1024u = 0.5f * C0437b.this.f1021r;
            this.f983e = C0437b.this.f1022s + (C0437b.this.f1024u * 1.0f);
            this.f984f = C0437b.this.f1022s - (C0437b.this.f1024u * 1.0f);
        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            if (C0437b.this.f1004a == C0432b.Running) {
                this.f979a = false;
                this.f986h = 0.0f;
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
            this.f979a = true;
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (C0437b.this.f1004a == C0432b.Running && C0437b.this.f1000H) {
                if (C0437b.this.f1005b || !C0437b.this.scrolling) {
                    this.f982d = C0437b.this.f1022s;
                    C0437b.this.f1023t = this.f982d;
                } else if (deltaX != 0.0f) {
                    this.f985g = deltaX > 0.0f ? this.f988j : -this.f988j;
                    this.f986h += this.f985g;
                    if (this.f985g < 0.0f) {
                        if (this.f986h > 0.0f) {
                            this.f986h = 0.0f;
                        } else if (this.f986h >= (-this.f987i)) {
                            this.f982d += this.f985g;
                        }
                    } else if (this.f985g > 0.0f) {
                        if (this.f986h < 0.0f) {
                            this.f986h = 0.0f;
                        } else if (this.f986h <= this.f987i) {
                            this.f982d += this.f985g;
                        }
                    }
                    if (this.f982d >= this.f983e) {
                        this.f982d = this.f983e;
                    } else if (this.f982d <= this.f984f) {
                        this.f982d = this.f984f;
                    }
                    C0437b.this.f1023t = this.f982d;
                }
            }
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    }

    enum C0432b {
        Setup,
        Running
    }

    public C0437b(WallpaperService wallpaperService, GalaxyConfig galaxyConfig) {
        this.f1006c = wallpaperService;
        this.galaxyConfig = galaxyConfig;
    }

    public void onSharedPreferenceChanged() {
        this.f1025v = true;
        this.customColor = false;
        GalaxyColor.m1793a(galaxyConfig.galaxyColor);
        this.quatity = 60;
        this.speed = 1;
        this.direction = 1;
        this.scrolling = true;
        this.fps = 20;
        this.f1016m = System.currentTimeMillis();
        this.f1015l = 1000 / this.fps;
        this.f1004a = C0432b.Setup;

    }

    public void m1765b() {
        if (this.f1025v) {
            this.f1025v = false;
            this.f1013j.m1716a(galaxyConfig.galaxyType);
        }
        this.f1013j.m1717a(false);
        m1760h();
        this.f1004a = C0432b.Running;
    }

    private void m1759g() {
        int a = C0365c.m1604a();
        int b = C0365c.m1608b();
        boolean z = C0365c.f818a;
        C0365c.m1606a(800, 480);
        C0365c.m1605a(((WindowManager) f1006c.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation());
        C0365c.m1607a(this.f1006c.getResources().getConfiguration().orientation == 2);
        if (C0365c.f818a != z || C0365c.m1604a() != a || C0365c.m1608b() != b) {
            this.f1004a = C0432b.Setup;
        }
    }

    private void m1760h() {
        C0365c.m1606a(800, 480);
        if (this.f1008e == null) {
            this.f1008e = new PerspectiveCamera(C0365c.m1604a() < C0365c.m1608b() ? 30.0f : 15.0f, (float) C0365c.m1604a(), (float) C0365c.m1608b());
        }
        this.f1008e.fieldOfView = C0365c.m1604a() < C0365c.m1608b() ? 30.0f : 15.0f;
        this.f1008e.viewportWidth = (float) C0365c.m1604a();
        this.f1008e.viewportHeight = (float) C0365c.m1608b();
        this.f1008e.near = 0.1f;
        this.f1008e.far = 10000.0f;
        this.f1008e.position.set(0.0f, 0.0f, 1000.0f);
        this.f1008e.direction.set(0.0f, 0.0f, -1.0f);
        this.f1008e.update();
        if (this.f1010g != null) {
            this.f1010g.dispose();
        }
        if (this.f1009f != null) {
            this.f1009f.dispose();
        }
        this.f1009f = new C0364b(this.f1008e);
        this.f1010g = new DecalBatch(this.f1009f);
        this.f1011h = new Vector3();
        float red = ((float) Color.red(GalaxyColor.f1067a)) / 255.0f;
        float green = ((float) Color.green(GalaxyColor.f1067a)) / 255.0f;
        float blue = ((float) Color.blue(GalaxyColor.f1067a)) / 255.0f;
        this.f1026w = Decal.newDecal(800.0f, 800.0f, this.f1013j.f974e, true);
        this.f1026w.setColor(red, green, blue, 0.3f);
        this.f1026w.setPosition(0.0f, 0.0f, 100.0f);
        this.f1026w.rotateY(30.0f);
        this.f1026w.rotateX(-45.0f);
        this.f1027x = Decal.newDecal(520.0f, 520.0f, this.f1013j.f974e, true);
        this.f1027x.setBlending(770, 1);
        this.f1027x.setColor(red, green, blue, 0.6f);
        this.f1027x.setPosition(0.0f, 3.0f, 200.0f);
        this.f1027x.rotateY(30.0f);
        this.f1027x.rotateX(-45.0f);
        this.f1028y = Decal.newDecal(280.0f, 280.0f, this.f1013j.f974e, true);
        this.f1028y.setBlending(770, 1);
        this.f1028y.setColor(red, green, blue, 1.0f);
        this.f1028y.setPosition(0.0f, 6.0f, 300.0f);
        this.f1028y.rotateY(30.0f);
        this.f1028y.rotateX(-45.0f);
        this.f1029z = Decal.newDecal(480.0f, 480.0f, this.f1013j.f976g, true);
        this.f1029z.setBlending(770, 1);
        this.f1029z.setPosition(0.0f, 0.0f, 100.0f);
        this.f1029z.rotateY(30.0f);
        this.f1029z.rotateX(-45.0f);
        this.f993A = Decal.newDecal(320.0f, 320.0f, this.f1013j.f977h, true);
        this.f993A.setColor(1.0f, 1.0f, 1.0f, 0.6f);
        this.f993A.setPosition(0.0f, 4.0f, 250.0f);
        this.f993A.rotateY(30.0f);
        this.f993A.rotateX(-45.0f);
        this.f996D = Decal.newDecal(80.0f, 80.0f, this.f1013j.f978i, true);
        this.f996D.setPosition(0.0f, 10.0f, 400.0f);
        Random random = new Random();
        this.f994B = new GalaxyDecal[this.quatity];
        for (int i = 0; i < this.f994B.length; i++) {
            green = (random.nextFloat() * 12.0f) + 2.0f;
            blue = (random.nextFloat() * 1.0f) + 1.0f;
            float random2 = (float) (((Math.random() * ((double) C0365c.m1604a())) / 3.0d) - ((double) (((float) C0365c.m1604a()) / 6.0f)));
            float random3 = (float) (((Math.random() * ((double) C0365c.m1608b())) / 3.0d) - ((double) (((float) C0365c.m1608b()) / 6.0f)));
            float random4 = (float) ((Math.random() * 400.0d) + 200.0d);
            this.f994B[i] = new GalaxyDecal(green, green, this.f1013j.f975f, true);
            this.f994B[i].m1789a(0.2f, 1.0f, blue, true);
            this.f994B[i].m1792b(0.2f, (random.nextFloat() * 0.2f) + 0.8f, blue, true);
            this.f994B[i].setBlending(770, 1);
            this.f994B[i].setColor(1.0f, 1.0f, 1.0f, (random.nextFloat() * 0.4f) + 0.6f);
            this.f994B[i].setPosition(random2, random3, random4);
        }
        this.f1002J.m1726a(5);
    }

}
