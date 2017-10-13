package com.yalin.wallpaper.feather;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic1.gdx.ApplicationListener;
import com.badlogic1.gdx.Gdx;
import com.badlogic1.gdx.graphics.Mesh;
import com.badlogic1.gdx.graphics.OrthographicCamera;
import com.badlogic1.gdx.graphics.PerspectiveCamera;
import com.badlogic1.gdx.graphics.VertexAttribute;
import com.badlogic1.gdx.graphics.g2d.SpriteBatch;
import com.badlogic1.gdx.input.GestureDetector;
import com.badlogic1.gdx.math.Plane;
import com.badlogic1.gdx.math.Vector2;
import com.badlogic1.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.Random;

public class MyApplicationListener implements ApplicationListener,
        AndroidWallpaperListener {
    boolean rotation = true;
    boolean touch = true;
    Vector2 f189C = new Vector2();
    boolean parallax = true;
    boolean water = true;
    boolean dragging = true;
    boolean sound = false;
    short f194H = (short) 60;
    short f195I = (short) 60;
    float f196J = (1.0f / ((float) this.f194H));
    float f197K = (1.0f / ((float) this.f195I));
    float f198L = 0.86f;
    float f199M = -60.0f;
    float f200N = 0.033f;
    int f201O = 3;
    float f202P;
    PerspectiveCamera f203Q;
    Mesh f204R;
    Plane f205S = new Plane(new Vector3(), new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f));
    Vector3 f206T = new Vector3();
    Vector3 f207U = new Vector3();
    float[][] f208V;
    float[][] f209W;
    float[][] f210X;
    float[] f211Y;
    boolean f212Z = true;
    int aa = 0;
    C0072o ab;
    GestureDetector ac;
    C0073p f213b = C0073p.Setup;
    Boolean f214c = Boolean.FALSE;
    WallpaperService f215d;
    OrthographicCamera f217f;
    SpriteBatch f218g;
    Vector3 f219h;
    float f220i = 0.0f;
    C0066k f221j = new C0066k();
    int fps = 25;
    int f223l = 0;
    long f224m = 0;
    long f225n = 0;
    float f226o = 0.0f;
    float f227p = 0.0f;
    float f228q = 0.0f;
    float f229r = 10.0f;
    C0067l f230s;
    boolean f231t = false;
    boolean scrolling;
    C0069q[] f233v = null;
    boolean particle = true;
    int quantity = 4;
    float size = 1.0f;
    float speed = 1.0f;

    public boolean f146a;

    public MyApplicationListener(WallpaperService wallpaperService) {
        this.f215d = wallpaperService;
    }

    public void onSharedPreferenceChanged() {
        this.particle = true;
        this.quantity = 4;
        this.size = 1;
        this.speed = 1;
        this.rotation = true;
        this.touch = true;
        this.parallax = false;
        this.scrolling = true;
        this.water = true;
        this.dragging = true;
        this.sound = false;
        this.fps = 25;
        this.f224m = System.currentTimeMillis();
        this.f223l = 1000 / this.fps;
        this.f213b = C0073p.Setup;
    }

    public void m204f() {
        this.f221j.m186a(true);
        C0030d.m133a(800, 480);
        if (this.f217f == null) {
            this.f217f = new OrthographicCamera((float) C0030d.m131a(), (float) C0030d.m135b());
        }
        this.f217f.viewportWidth = (float) C0030d.m131a();
        this.f217f.viewportHeight = (float) C0030d.m135b();
        this.f217f.position.set((float) (C0030d.m131a() / 2), (float) (C0030d.m135b() / 2), 0.0f);
        this.f217f.update();
        if (this.f218g == null) {
            this.f218g = new SpriteBatch();
        }
        this.f218g.setProjectionMatrix(this.f217f.combined);
        m194h();
        this.f214c = Boolean.TRUE;
        this.f213b = C0073p.Running;
    }

    private void m193g() {
        C0030d.m133a(800, 480);
        Boolean bool = C0030d.f139a;
        C0030d.m132a(((WindowManager) this.f215d.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation());
        C0030d.m134a(this.f215d.getResources().getConfiguration().orientation == 2);
        if (C0030d.f139a != bool) {
            this.f213b = C0073p.Setup;
        }
    }

    private void m194h() {
        this.f219h = new Vector3();
        this.f230s = new C0067l((((float) C0030d.m131a()) / 2.0f) - (((float) C0030d.m136c()) / 2.0f),
                C0030d.f139a ? ((float) ((-C0030d.m136c()) + C0030d.m135b())) / 2.0f : 0.0f,
                (float) C0030d.m136c(), (float) C0030d.m136c());
        Random random = new Random();
        this.f233v = new C0069q[this.quantity];
        for (int i = 0; i < this.f233v.length; i++) {
            float nextFloat = (random.nextFloat() * 0.5f) + 0.4f;
            this.f233v[i] = new C0070n(this, ((float) C0030d.m131a()) * random.nextFloat(),
                    ((float) C0030d.m135b()) * random.nextFloat(),
                    (float) ((int) ((128.0f * nextFloat) * this.size)),
                    (float) ((int) ((nextFloat * 256.0f) * this.size)));
        }
        if (this.water) {
            if (this.f203Q == null) {
                this.f203Q = new PerspectiveCamera();
            }
            this.f203Q.position.set(((float) this.f194H) / 2.0f,
                    ((float) this.f195I) / 2.0f, ((float) this.f194H) / 2.0f);
            this.f203Q.near = 0.1f;
            this.f203Q.far = 1000.0f;
            this.f203Q.viewportWidth = (float) C0030d.m131a();
            this.f203Q.viewportHeight = (float) C0030d.m135b();
            this.f203Q.fieldOfView = 90.0f / (C0030d.m131a() < C0030d.m135b() ? 1.0f : (1.5f * (((float) C0030d.m131a()) / ((float) C0030d.m135b()))) / 1.65f);
            this.f203Q.update();
            this.f203Q.apply(Gdx.gl11);
            this.f208V = (float[][]) Array.newInstance(Float.TYPE, this.f194H + 1, this.f195I + 1);
            this.f209W = (float[][]) Array.newInstance(Float.TYPE, this.f194H + 1, this.f195I + 1);
            this.f210X = (float[][]) Array.newInstance(Float.TYPE, this.f194H + 1, this.f195I + 1);
            this.f211Y = new float[(((this.f194H + 1) * (this.f195I + 1)) * 5)];
            this.f204R = new Mesh(true, (this.f194H + 1) * (this.f195I + 1), (this.f194H * this.f195I) * 6,
                    new VertexAttribute(0, 3, "a_Position"), new VertexAttribute(3, 2, "a_texCoords"));
            m195i();
            m192a(this.f209W);
            this.f227p = ((float) this.f194H) / 2.0f;
            this.f228q = this.f227p;
            this.f229r = (this.f230s.f125d.width - ((float) C0030d.m131a())) * (((float) this.f194H) / this.f230s.f125d.width);
        }
        this.ab.m226a(5);
    }

    private void m195i() {
        short[] sArr = new short[((this.f194H * this.f195I) * 6)];
        int i = 0;
        for (short s = (short) 0; s < this.f195I; s++) {
            short s2 = (short) ((this.f194H + 1) * s);
            short s3 = (short) 0;
            while (s3 < this.f194H) {
                int i2 = i + 1;
                sArr[i] = s2;
                i = i2 + 1;
                sArr[i2] = (short) (s2 + 1);
                i2 = i + 1;
                sArr[i] = (short) ((this.f194H + s2) + 1);
                i = i2 + 1;
                sArr[i2] = (short) (s2 + 1);
                int i3 = i + 1;
                sArr[i] = (short) ((this.f194H + s2) + 2);
                i2 = i3 + 1;
                sArr[i3] = (short) ((this.f194H + s2) + 1);
                s2 = (short) (s2 + 1);
                s3++;
                i = i2;
            }
        }
        this.f204R.setIndices(sArr);
    }

    private void m192a(float[][] fArr) {
        short s = (short) 0;
        int i = 0;
        while (s <= this.f195I) {
            short s2 = (short) 0;
            while (s2 <= this.f194H) {
                float f;
                float f2;
                if (s2 <= (short) 0 || s2 >= this.f194H || s <= (short) 0 || s >= this.f195I) {
                    f = 0.0f;
                    f2 = 0.0f;
                } else {
                    f2 = fArr[s2 - 1][s] - fArr[s2 + 1][s];
                    f = fArr[s2][s - 1] - fArr[s2][s + 1];
                }
                int i2 = i + 1;
                this.f211Y[i] = (float) s2;
                int i3 = i2 + 1;
                this.f211Y[i2] = (float) s;
                i2 = i3 + 1;
                this.f211Y[i3] = 0.0f;
                i3 = i2 + 1;
                this.f211Y[i2] = (f2 + ((float) s2)) * this.f196J;
                int i4 = i3 + 1;
                this.f211Y[i3] = 1.0f - ((f + ((float) s)) * this.f197K);
                s2++;
                i = i4;
            }
            s++;
        }
        this.f204R.setVertices(this.f211Y);
    }

    private void m196j() {
        short s = (short) 0;
        while (s < this.f195I + 1) {
            short s2 = (short) 0;
            while (s2 < this.f194H + 1) {
                if (s2 > (short) 0 && s2 < this.f194H && s > (short) 0 && s < this.f195I) {
                    this.f209W[s2][s] = ((((this.f208V[s2 - 1][s] + this.f208V[s2 + 1][s]) + this.f208V[s2][s + 1]) + this.f208V[s2][s - 1]) / 2.0f) - this.f209W[s2][s];
                    if (this.f209W[s2][s] > 10.0f) {
                        this.f209W[s2][s] = 10.0f;
                    }
                }
                float[] fArr = this.f209W[s2];
                fArr[s] = fArr[s] * this.f198L;
                s2++;
            }
            s++;
        }
    }

    private void m189a(float f) {
        for (short s = (short) 0; s < this.f195I; s++) {
            for (short s2 = (short) 0; s2 < this.f194H; s2++) {
                this.f210X[s2][s] = (this.f208V[s2][s] * f) + ((1.0f - f) * this.f209W[s2][s]);
            }
        }
    }

    public void m191a(Vector3 c0368j) {
        for (int max = Math.max(0, ((int) c0368j.y) - this.f201O); max < Math.min(this.f195I, ((int) c0368j.y) + this.f201O); max++) {
            for (int max2 = Math.max(0, ((int) c0368j.x) - this.f201O); max2 < Math.min(this.f194H, ((int) c0368j.x) + this.f201O); max2++) {
                float max3 = this.f209W[max2][max] + (this.f199M * Math.max(0.0f,
                        (float) Math.cos((1.5700000524520874d * Math.sqrt((double) c0368j.dst2((float) max2,
                                (float) max, 0.0f))) / ((double) this.f201O))));
                if (max3 < this.f199M) {
                    max3 = this.f199M;
                } else if (max3 > (-this.f199M)) {
                    max3 = -this.f199M;
                }
                this.f209W[max2][max] = max3;
            }
        }
    }

    @Override
    public void render() {
        int i = 0;
        if (this.f213b == C0073p.Setup) {
            m204f();
        }
        if (this.f213b == C0073p.Running) {
            // TODO: 2017/10/13  
            this.f220i = Gdx.graphics.getDeltaTime();
            switch (C0030d.f143e) {
                case 0:
                    this.f189C.x = Gdx.input.getDeltaX();
                    this.f189C.y = Gdx.input.getDeltaY();
                    break;
                case 1:
                    this.f189C.x = -Gdx.input.getDeltaY();
                    this.f189C.y = Gdx.input.getDeltaX();
                    break;
                case 2:
                    this.f189C.x = -Gdx.input.getDeltaX();
                    this.f189C.y = -Gdx.input.getDeltaY();
                    break;
                case 3:
                    this.f189C.x = Gdx.input.getDeltaY();
                    this.f189C.y = -Gdx.input.getDeltaX();
                    break;
            }
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(16384);
            if (this.water) {
                this.f203Q.translate((this.f228q - this.f203Q.position.x) * 0.1f, 0.0f, 0.0f);
                this.f203Q.update();
                Gdx.gl10.glMatrixMode(5889);
                Gdx.gl10.glLoadMatrixf(this.f203Q.combined.val, 0);
                Gdx.gl10.glMatrixMode(5888);
                this.f202P += Gdx.graphics.getDeltaTime();
                while (this.f202P > this.f200N) {
                    m196j();
                    float[][] fArr = this.f209W;
                    this.f209W = this.f208V;
                    this.f208V = fArr;
                    this.f202P -= this.f200N;
                }
                m189a(this.f202P / this.f200N);
                m192a(this.f210X);
                Gdx.gl.glEnable(3553);
                this.f221j.f181b.bind();
                this.f204R.render(4);
            } else {
                this.f218g.disableBlending();
                this.f218g.begin();
                this.f230s.m187a();
                this.f230s.m188a(this.f218g, this.f221j.f183d);
                this.f218g.end();
            }
            this.f218g.enableBlending();
            this.f218g.begin();
            while (i < this.f233v.length) {
                if (this.rotation) {
                    this.f233v[i].m213b(this.f220i * this.speed);
                }
                if (this.parallax) {
                    this.f233v[i].m210a(this.f189C, this.f220i);
                } else {
                    this.f233v[i].m206a(this.f220i);
                }
                if (this.particle) {
                    this.f233v[i].m209a(this.f218g, this.f221j.f184e);
                }
                i++;
            }
            this.f218g.end();
            try {
                this.f225n = System.currentTimeMillis() - this.f224m;
                if (this.f225n < ((long) this.f223l)) {
                    Thread.sleep(((long) this.f223l) - this.f225n);
                    this.f224m = System.currentTimeMillis();
                    return;
                }
                this.f224m = System.currentTimeMillis();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override
    public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
                             int xPixelOffset, int yPixelOffset) {
        if (this.f213b == C0073p.Running) {
            if (this.aa > 3) {
                this.f212Z = false;
            } else {
                this.aa++;
            }
            if (!this.f212Z) {
                if (this.f146a) {
                    this.f226o = ((float) (C0030d.m131a() - C0030d.m136c())) / 2.0f;
                    this.f230s.f186g.x = this.f226o;
                    if (this.water) {
                        this.f228q = this.f227p;
                        return;
                    }
                    return;
                }
                if (this.scrolling) {
                    this.f226o = ((float) (C0030d.m131a() - C0030d.m136c())) * xOffset;
                } else {
                    this.f226o = ((float) (C0030d.m131a() - C0030d.m136c())) / 2.0f;
                }
                this.f230s.f186g.x = this.f226o;
                if (!this.water) {
                    return;
                }
                if (this.scrolling) {
                    this.f228q = this.f227p - ((0.5f - xOffset) * this.f229r);
                } else {
                    this.f228q = this.f227p;
                }
            }
        }
    }

    @Override
    public void previewStateChange(boolean isPreview) {
        this.f146a = isPreview;
    }

    @Override
    public void create() {
        this.f221j.m185a();
        this.ab = new C0072o(this);
        this.ac = new GestureDetector(this.ab);
        Gdx.input.setInputProcessor(this.ac);
        C0030d.m134a(this.f215d.getResources().getConfiguration().orientation == 2);
        onSharedPreferenceChanged();
    }

    @Override
    public void resize(int width, int height) {
        m193g();
    }

    @Override
    public void pause() {
        this.aa = 0;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        try {
            if (this.f218g != null) {
                this.f218g.dispose();
            }
            if (this.f204R != null) {
                this.f204R.dispose();
            }
        } catch (Exception e) {
        }
    }
}
