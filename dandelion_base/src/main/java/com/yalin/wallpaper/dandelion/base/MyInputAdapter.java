package com.yalin.wallpaper.dandelion.base;

import android.content.Context;
import android.graphics.Point;
import android.service.wallpaper.WallpaperService;
import android.view.WindowManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.Random;

public class MyInputAdapter extends InputAdapter
        implements ApplicationListener, AndroidWallpaperListener {
    float size = 1.0f;
    float speed = 1.0f;
    boolean rotation = true;
    boolean touch = true;
    int particle_type = 1;
    Vector2 f2910F = new Vector2();
    boolean parallax = true;
    boolean water = true;
    boolean dragging = true;
    boolean sound = false;
    short f2915K = (short) 60;
    short f2916L = (short) 36;
    float f2917M = (1.0f / ((float) this.f2915K));
    float f2918N = (1.0f / ((float) this.f2916L));
    ShaderProgram f2923S;
    Mesh f2924T;
    Vector3 f2926V = new Vector3();
    float f2927W;
    float[][] f2928X;
    float[][] f2929Y;
    float[][] f2930Z;
    C1076b f2931a = C1076b.Setup;
    float[] aa;
    boolean auto = true;
    float ac = 0.0f;
    float ad = 0.0f;
    Random ae = new Random();
    boolean light = false;
    C1089j ag;
    boolean ah = true;
    int ai = 0;
    C1075a aj;
    GestureDetector ak;
    boolean f2932b = false;
    WallpaperService f2933c;
    OrthographicCamera f2935e;
    SpriteBatch f2936f;
    Vector3 f2937g;
    float f2938h = 0.0f;
    C1066b f2939i = new C1066b();
    int fps = 25;
    int f2941k = 0;
    long f2942l = 0;
    long f2943m = 0;
    int mode = 1;
    OrthographicCamera f2945o;
    float f2946p = 0.0f;
    float f2947q = 0.0f;
    float f2948r = 10.0f;
    boolean f2949s = false;
    boolean scrolling;
    boolean flip;
    boolean custom = false;
    String load_custom;
    C1072n[] f2954x = null;
    boolean particle = true;
    int quantity = 10;

    private DandelionConfig dandelionConfig;

    class C1075a implements GestureListener {
        boolean f2893a = false;
        float f2896d = 0.0f;
        float f2897e = 0.0f;
        float f2898f = 0.0f;
        float f2899g = 0.0f;
        float f2900h = 0.3f;

        C1075a() {
        }

        public void m5325a(int i) {
            this.f2896d = f2946p;
            this.f2899g = f2948r / ((float) (i - 1));
        }

        @Override
        public boolean touchDown(float f, float f2, int i, int i2) {
            if (f2931a == C1076b.Running) {
                this.f2893a = false;
                this.f2898f = 0.0f;
                if (touch && particle) {
                    f2935e.unproject(f2937g.set(f, f2, 0.0f));
                    for (C1072n a : f2954x) {
                        a.m5310a(f2937g.x, f2937g.y);
                    }
                }
                if (water) {
                    if (sound) {
                        DandelionSound.f2996a.play();
                    }
                    f2945o.unproject(f2937g.set(f, f2, 0.0f));
                    m5360a(f2937g);
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
            this.f2893a = true;
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (f2931a == C1076b.Running) {
                if (water && dragging) {
                    f2945o.unproject(f2937g.set((float) Gdx.input.getX(),
                            (float) Gdx.input.getY(), 0.0f));
                    if (((float) ((int) f2937g.x)) < f2926V.x - 1.0f
                            || ((float) ((int) f2937g.x)) > f2926V.x + 1.0f
                            || ((float) ((int) f2937g.y)) < f2926V.y - 1.0f
                            || ((float) ((int) f2937g.y)) > f2926V.y + 1.0f) {
                        m5360a(f2937g);
                        f2926V.x = (float) ((int) f2937g.x);
                        f2926V.y = (float) ((int) f2937g.y);
                    }
                }
                if (ah) {
                    if (!scrolling) {
                        this.f2896d = f2946p;
                        f2947q = this.f2896d;
                    } else if (deltaX != 0.0f) {
                        this.f2897e = deltaX > 0.0f ? this.f2900h : -this.f2900h;
                        this.f2898f += this.f2897e;
                        if (this.f2897e < 0.0f) {
                            if (this.f2898f > 0.0f) {
                                this.f2898f = 0.0f;
                            } else if (this.f2898f >= (-this.f2899g)) {
                                this.f2896d -= this.f2897e;
                            }
                        } else if (this.f2897e > 0.0f) {
                            if (this.f2898f < 0.0f) {
                                this.f2898f = 0.0f;
                            } else if (this.f2898f <= this.f2899g) {
                                this.f2896d -= this.f2897e;
                            }
                        }
                        if (this.f2896d >= f2946p + (f2948r / 2.0f)) {
                            this.f2896d = f2946p + (f2948r / 2.0f);
                        } else if (this.f2896d <= f2946p - (f2948r / 2.0f)) {
                            this.f2896d = f2946p - (f2948r / 2.0f);
                        }
                        f2947q = this.f2896d;
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
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

    }

    enum C1076b {
        Setup,
        Running
    }

    public MyInputAdapter(WallpaperService wallpaperService, DandelionConfig dandelionConfig) {
        this.f2933c = wallpaperService;
        this.dandelionConfig = dandelionConfig;
    }

    @Override
    public void create() {
        this.f2939i.m5289a();
        this.aj = new C1075a();
        this.ak = new GestureDetector(this.aj);
        Gdx.input.setInputProcessor(this.ak);
        C1018d.m5172a(this.f2933c.getResources().getConfiguration().orientation == 2);
        onSharedPreferenceChanged();
    }

    public void onSharedPreferenceChanged() {
        this.f2949s = true;
        this.particle = true;
        this.particle_type = 1;
        this.quantity = 10;
        this.size = 1;
        this.speed = 1;
        this.rotation = true;
        this.touch = true;
        this.parallax = false;
        this.scrolling = true;
        this.water = true;
        this.auto = dandelionConfig.dandelionAuto;
        this.dragging = true;
        this.sound = dandelionConfig.dandelionSound;
        DandelionSound.m5408a(1, this.f2939i);
        this.light = false;
        this.flip = false;
        this.custom = false;
        this.load_custom = "-1";
        this.fps = 30;
        this.f2942l = System.currentTimeMillis();
        this.f2941k = 1000 / this.fps;
        this.mode = dandelionConfig.dandelionType;
        this.f2931a = C1076b.Setup;
    }

    public void m5370b() {
        this.f2939i.m5293b(this.particle_type);
        if (this.f2949s) {
            this.f2949s = false;
            this.f2939i.m5294c(this.mode);
        }
        this.f2939i.m5290a(true);
        m5363h();
        this.f2931a = C1076b.Running;
    }

    private void m5362g() {
        int a = C1018d.m5168a();
        int b = C1018d.m5173b();
        boolean z = C1018d.f2709a;
        C1018d.m5171a(800, 480);
        C1018d.m5170a(((WindowManager) f2933c.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation());
        C1018d.m5172a(f2933c.getResources().getConfiguration().orientation == 2);
        if (C1018d.f2709a != z || C1018d.m5168a() != a || C1018d.m5173b() != b) {
            this.f2931a = C1076b.Setup;
        }
    }

    private void m5363h() {
        this.f2937g = new Vector3();
        C1018d.m5171a(60, 36);
        Point a = C1018d.m5169a(this.f2939i.f2829c.x, this.f2939i.f2829c.y, C1018d.m5168a(), C1018d.m5173b());
        this.f2915K = (short) a.x;
        this.f2916L = (short) a.y;
        this.f2917M = 1.0f / ((float) this.f2915K);
        this.f2918N = 1.0f / ((float) this.f2916L);
        if (this.f2945o == null) {
            this.f2945o = new OrthographicCamera((float) C1018d.m5168a(), (float) C1018d.m5173b());
        }
        this.f2945o.viewportWidth = (float) C1018d.m5168a();
        this.f2945o.viewportHeight = (float) C1018d.m5173b();
        this.f2945o.position.set((((float) C1018d.m5168a()) / 2.0f)
                        - (((float) (C1018d.m5168a() - this.f2915K)) / 2.0f),
                (((float) C1018d.m5173b()) / 2.0f) - (((float) (C1018d.m5173b() - this.f2916L)) / 2.0f), 0.0f);
        this.f2945o.update();
        this.f2946p = ((float) this.f2915K) / 2.0f;
        this.f2947q = this.f2946p;
        this.f2948r = ((float) this.f2915K) - ((float) C1018d.m5168a());
        this.f2928X = (float[][]) Array.newInstance(Float.TYPE, this.f2915K + 1, this.f2916L + 1);
        this.f2929Y = (float[][]) Array.newInstance(Float.TYPE, this.f2915K + 1, this.f2916L + 1);
        this.f2930Z = (float[][]) Array.newInstance(Float.TYPE, this.f2915K + 1, this.f2916L + 1);
        this.aa = new float[(7 * ((this.f2915K + 1) * (this.f2916L + 1)))];
        this.f2924T = new Mesh(true, (this.f2915K + 1) * (this.f2916L + 1), (this.f2915K * this.f2916L) * 6,
                VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.TexCoords(1));
        m5364i();
        m5361a(this.f2929Y);
        String str = "attribute vec4 a_position;\nattribute vec2 a_texCoord0;\nattribute vec2 a_texCoord1;                                                     \n                                                                                \nvarying float v_light;                                                          \nvarying vec2 v_texCoords;                                                       \n                                                                                \nuniform mat4  u_projTrans;                                                      \n                                                                                \nvoid main()                                                                     \n{                                                                               \n\tgl_Position = u_projTrans * a_position;                                      \n\tv_texCoords = a_texCoord0;                                                   \n                                                                                \n\tv_light = (a_texCoord1.y * 4.0) - pow(a_texCoord1.y * 3.0, 2.0);             \n\tv_light = v_light < -0.2 ? -0.2 : v_light > 1.0 ? 1.0 : v_light;             \n}                                                                               \n";
        ShaderProgram.pedantic = false;
        if (this.f2923S != null) {
            this.f2923S.dispose();
        }
        this.f2923S = new ShaderProgram(str, Gdx.files.internal(this.light ?
                "shaders/tex2_fs.glsl" : "shaders/tex_fs.glsl").readString());
        if (this.f2923S.isCompiled()) {
            C1018d.m5171a(800, 480);
            if (this.f2935e == null) {
                this.f2935e = new OrthographicCamera((float) C1018d.m5168a(), (float) C1018d.m5173b());
            }
            this.f2935e.viewportWidth = (float) C1018d.m5168a();
            this.f2935e.viewportHeight = (float) C1018d.m5173b();
            this.f2935e.position.set((float) (C1018d.m5168a() / 2), (float) (C1018d.m5173b() / 2), 0.0f);
            this.f2935e.update();
            if (this.f2936f == null) {
                this.f2936f = new SpriteBatch();
            }
            this.f2936f.setProjectionMatrix(this.f2935e.combined);
            Random random = new Random();
            this.f2954x = new C1072n[this.quantity];
            for (int i = 0; i < this.f2954x.length; i++) {
                float nextFloat = random.nextFloat() + 0.6f;
                float f = (52.0f * nextFloat) * this.size;
                float f2 = (82.0f * nextFloat) * this.size;
                final float nextFloat2 = this.particle_type == 3 ? (random.nextFloat() * 0.4f) + 0.6f
                        : this.particle_type == 4 ? (random.nextFloat() * 0.3f) + 0.7f : 1.0f;
                if (this.particle_type == 3) {
                    f2 = ((random.nextFloat() * 48.0f) + 32.0f) * this.size;
                    f = f2;
                } else if (this.particle_type == 4) {
                    f = (88.0f * nextFloat) * this.size;
                    f2 = (nextFloat * 42.0f) * this.size;
                } else if (this.particle_type == 5) {
                    f = (64.0f * nextFloat) * this.size;
                    f2 = (nextFloat * 60.0f) * this.size;
                } else if (this.particle_type == 6) {
                    f = (32.0f * nextFloat) * this.size;
                    f2 = (nextFloat * 32.0f) * this.size;
                }
                this.f2954x[i] = new C1072n(random.nextFloat() * ((float) C1018d.m5168a()),
                        random.nextFloat() * ((float) C1018d.m5173b()), f, f2) {
                    protected void mo1157a() {
                        Random random = new Random();
                        this.f2874j.set(this.f2694c);
                        this.f2698a.set((random.nextFloat() * 60.0f) + 20.0f,
                                (random.nextFloat() * 30.0f) + 10.0f);
                        this.f2875k.set(this.f2698a);
                        this.f2870O.set(0.0f, 0.0f);
                        this.f2863H.set(0.0f, 0.0f);
                        this.f2870O.set(0.0f, 0.0f);
                        this.f2871P = ((float) random.nextInt(6)) + 6.0f;
                        this.f2884t = nextFloat2;
                        if (mode == 4) {
                            this.f2878n = (random.nextFloat() * 30.0f) - 15.0f;
                            this.f2879o = (random.nextFloat() * 8.0f) - 4.0f;
                            return;
                        }
                        this.f2878n = random.nextFloat() * 100.0f;
                        this.f2879o = (random.nextFloat() * 20.0f) + 10.0f;
                    }

                    protected void mo1158b() {
                        Random random = new Random();
                        this.f2694c.x = -this.f2877m;
                        this.f2694c.y = random.nextFloat() * ((((float) C1018d.m5173b()) / 4.0f) * 3.0f);
                        this.f2874j.set(this.f2694c);
                        this.f2878n = 0.0f;
                        this.f2698a.set((random.nextFloat() * 60.0f) + 20.0f, (random.nextFloat() * 30.0f) + 10.0f);
                        this.f2875k.set(this.f2698a);
                        this.f2870O.set(0.0f, 0.0f);
                        this.f2863H.set(0.0f, 0.0f);
                        this.f2870O.set(0.0f, 0.0f);
                        this.f2871P = ((float) random.nextInt(6)) + 6.0f;
                        this.f2884t = nextFloat2;
                        if (mode == 4) {
                            this.f2878n = (random.nextFloat() * 30.0f) - 15.0f;
                            this.f2879o = (random.nextFloat() * 8.0f) - 4.0f;
                            return;
                        }
                        this.f2878n = random.nextFloat() * 100.0f;
                        this.f2879o = (random.nextFloat() * 20.0f) + 10.0f;
                    }

                    protected void mo1159c() {
                        if (this.f2694c.x < ((-this.f2877m) * this.f2856A) * 1.2f) {
                            mo1158b();
                        } else if (this.f2694c.x > ((float) C1018d.m5168a()) + ((this.f2877m * this.f2856A) * 1.2f)) {
                            mo1158b();
                        }
                        if (this.f2694c.y > ((float) C1018d.m5173b()) + ((this.f2877m * this.f2856A) * 1.2f)) {
                            mo1158b();
                        } else if (this.f2694c.y < ((-this.f2877m) * this.f2856A) * 1.2f) {
                            mo1158b();
                        }
                    }
                };
            }
            this.ag = new C1089j(0.0f, 0.0f, (float) C1018d.m5168a(), (float) C1018d.m5173b());
            this.aj.m5325a(5);
            return;
        }
        throw new IllegalStateException(this.f2923S.getLog());
    }

    private void m5364i() {
        short[] sArr = new short[((this.f2915K * this.f2916L) * 6)];
        int i = 0;
        for (short s = (short) 0; s < this.f2916L; s++) {
            short s2 = (short) ((this.f2915K + 1) * s);
            short s3 = (short) 0;
            while (s3 < this.f2915K) {
                int i2 = i + 1;
                sArr[i] = s2;
                i = i2 + 1;
                sArr[i2] = (short) (s2 + 1);
                i2 = i + 1;
                sArr[i] = (short) ((this.f2915K + s2) + 1);
                i = i2 + 1;
                sArr[i2] = (short) (s2 + 1);
                int i3 = i + 1;
                sArr[i] = (short) ((this.f2915K + s2) + 2);
                i2 = i3 + 1;
                sArr[i3] = (short) ((this.f2915K + s2) + 1);
                s2 = (short) (s2 + 1);
                s3++;
                i = i2;
            }
        }
        this.f2924T.setIndices(sArr);
    }

    private void m5361a(float[][] fArr) {
        short s = (short) 0;
        int i = 0;
        while (s <= this.f2916L) {
            short s2 = (short) 0;
            while (s2 <= this.f2915K) {
                float f;
                float f2;
                float f3;
                if (s2 <= (short) 0 || s2 >= this.f2915K || s <= (short) 0 || s >= this.f2916L) {
                    f = 0.0f;
                    f2 = 0.0f;
                } else {
                    f2 = fArr[s2 - 1][s] - fArr[s2 + 1][s];
                    f = fArr[s2][s - 1] - fArr[s2][s + 1];
                }
                int i2 = i + 1;
                this.aa[i] = (float) s2;
                int i3 = i2 + 1;
                this.aa[i2] = (float) s;
                i2 = i3 + 1;
                this.aa[i3] = 0.0f;
                float[] fArr2 = this.aa;
                int i4 = i2 + 1;
                if (this.flip) {
                    f3 = 1.0f - ((((float) s2) + f2) * this.f2917M);
                } else {
                    f3 = (((float) s2) + f2) * this.f2917M;
                }
                fArr2[i2] = f3;
                i3 = i4 + 1;
                this.aa[i4] = 1.0f - ((((float) s) + f) * this.f2918N);
                i2 = i3 + 1;
                this.aa[i3] = f2 * this.f2917M;
                int i5 = i2 + 1;
                this.aa[i2] = f * this.f2918N;
                s2++;
                i = i5;
            }
            s++;
        }
        this.f2924T.setVertices(this.aa);
    }

    private void m5365j() {
        short s = (short) 0;
        while (s < this.f2916L + 1) {
            short s2 = (short) 0;
            while (s2 < this.f2915K + 1) {
                if (s2 > (short) 0 && s2 < this.f2915K && s > (short) 0 && s < this.f2916L) {
                    this.f2929Y[s2][s] = ((((this.f2928X[s2 - 1][s] + this.f2928X[s2 + 1][s])
                            + this.f2928X[s2][s + 1]) + this.f2928X[s2][s - 1]) / 2.0f) - this.f2929Y[s2][s];
                    if (this.f2929Y[s2][s] > 10.0f) {
                        this.f2929Y[s2][s] = 10.0f;
                    }
                }
                float[] fArr = this.f2929Y[s2];
                fArr[s] = fArr[s] * 0.86f;
                s2++;
            }
            s++;
        }
    }

    private void m5358a(float f) {
        for (short s = (short) 0; s < this.f2916L; s++) {
            for (short s2 = (short) 0; s2 < this.f2915K; s2++) {
                this.f2930Z[s2][s] = (this.f2928X[s2][s] * f) + ((1.0f - f) * this.f2929Y[s2][s]);
            }
        }
    }

    private void m5360a(Vector3 c1256h) {
        for (int max = Math.max(0, ((int) c1256h.y) - 3);
             max < Math.min(this.f2916L, ((int) c1256h.y) + 3); max++) {
            for (int max2 = Math.max(0, ((int) c1256h.x) - 3);
                 max2 < Math.min(this.f2915K, ((int) c1256h.x) + 3); max2++) {
                float max3 = this.f2929Y[max2][max] + (Math.max(0.0f,
                        (float) Math.cos((1.5700000524520874d * Math.sqrt((double) c1256h.dst2((float) max2,
                                (float) max, 0.0f))) / 3.0d)) * -60.0f);
                if (max3 < -60.0f) {
                    max3 = -60.0f;
                } else if (max3 > 60.0f) {
                    max3 = 60.0f;
                }
                this.f2929Y[max2][max] = max3;
            }
        }
    }

    @Override
    public void render() {
        int i = 0;
        if (this.f2931a == C1076b.Setup) {
            m5370b();
        }
        if (this.f2931a == C1076b.Running) {
            this.f2938h = Gdx.graphics.getDeltaTime() * this.speed;
            if (Gdx.input.getNativeOrientation() != Orientation.Portrait) {
                switch (Gdx.input.getRotation()) {
                    case 0:
                        this.f2910F.x = -Gdx.input.getDeltaX();
                        this.f2910F.y = Gdx.input.getDeltaY();
                        break;
                    case 90:
                        this.f2910F.x = -Gdx.input.getDeltaY();
                        this.f2910F.y = -Gdx.input.getDeltaX();
                        break;
                    case 180:
                        this.f2910F.x = Gdx.input.getDeltaX();
                        this.f2910F.y = -Gdx.input.getDeltaY();
                        break;
                    case 270:
                        this.f2910F.x = Gdx.input.getDeltaY();
                        this.f2910F.y = Gdx.input.getDeltaX();
                        break;
                    default:
                        break;
                }
            }
            switch (Gdx.input.getRotation()) {
                case 0:
                    this.f2910F.x = Gdx.input.getDeltaY();
                    this.f2910F.y = Gdx.input.getDeltaX();
                    break;
                case 90:
                    this.f2910F.x = -Gdx.input.getDeltaX();
                    this.f2910F.y = Gdx.input.getDeltaY();
                    break;
                case 180:
                    this.f2910F.x = -Gdx.input.getDeltaY();
                    this.f2910F.y = -Gdx.input.getDeltaX();
                    break;
                case 270:
                    this.f2910F.x = Gdx.input.getDeltaX();
                    this.f2910F.y = -Gdx.input.getDeltaY();
                    break;
            }
            Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl20.glClear(16640);
            if (this.f2932b) {
                this.f2945o.position.x = this.f2946p;
            } else {
                this.f2945o.translate((this.f2947q - this.f2945o.position.x) * 0.1f, 0.0f, 0.0f);
            }
            this.f2945o.update();
            if (this.water && this.auto) {
                this.ac += Gdx.graphics.getDeltaTime();
                if (this.ac >= this.ad) {
                    this.ac = 0.0f;
                    this.ad = (this.ae.nextFloat() * 1.0f) + 0.6f;
                    this.f2937g.x = (float) (this.ae.nextInt(this.f2915K / 2) + (this.f2915K / 4));
                    this.f2937g.y = (float) this.ae.nextInt(this.f2916L);
                    m5360a(this.f2937g);
                }
            }
            this.f2927W += Gdx.graphics.getDeltaTime();
            while (this.f2927W > 0.033f) {
                m5365j();
                float[][] fArr = this.f2929Y;
                this.f2929Y = this.f2928X;
                this.f2928X = fArr;
                this.f2927W -= 0.033f;
            }
            m5358a(this.f2927W / 0.033f);
            m5361a(this.f2930Z);
            Gdx.gl20.glBlendFunc(770, 771);
            Gdx.gl20.glEnable(3042);
            Gdx.gl20.glActiveTexture(33984);
            this.f2939i.f2827a.bind();
            this.f2923S.begin();
            this.f2923S.setUniformi("u_texture0", 0);
            this.f2923S.setUniformMatrix("u_projTrans", this.f2945o.combined);
            this.f2924T.render(this.f2923S, 4);
            this.f2923S.end();
            this.f2936f.enableBlending();
            this.f2936f.begin();
            if (this.mode == 7) {
                this.ag.m5397a(this.f2938h);
                this.ag.m5398a(this.f2936f, this.f2939i.f2831e.getKeyFrame(this.ag.f2980h, false));
            }
            while (i < this.f2954x.length) {
                if (this.rotation) {
                    this.f2954x[i].m5312b(this.f2938h);
                }
                if (this.parallax) {
                    this.f2954x[i].m5309a(this.f2910F, this.f2938h);
                } else {
                    this.f2954x[i].m5305a(this.f2938h);
                }
                if (this.particle) {
                    this.f2954x[i].m5308a(this.f2936f, this.f2939i.f2830d);
                }
                i++;
            }
            this.f2936f.end();
            try {
                this.f2943m = System.currentTimeMillis() - this.f2942l;
                if (this.f2943m < ((long) this.f2941k)) {
                    Thread.sleep(((long) this.f2941k) - this.f2943m);
                    this.f2942l = System.currentTimeMillis();
                    return;
                }
                this.f2942l = System.currentTimeMillis();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override
    public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
                             int xPixelOffset, int yPixelOffset) {
        if (this.f2931a == C1076b.Running) {
            if (this.ai > 3) {
                this.ah = false;
            } else {
                this.ai++;
            }
            if (!this.ah) {
                if (this.scrolling) {
                    this.f2947q = this.f2946p - ((0.5f - xOffset) * this.f2948r);
                } else {
                    this.f2947q = this.f2946p;
                }
            }
        }
    }

    @Override
    public void previewStateChange(boolean isPreview) {
        this.f2932b = isPreview;
    }

    @Override
    public void resize(int width, int height) {
        m5362g();
    }

    @Override
    public void pause() {
        this.ai = 0;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
