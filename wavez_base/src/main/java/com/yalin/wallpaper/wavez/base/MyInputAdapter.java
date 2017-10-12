package com.yalin.wallpaper.wavez.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.service.wallpaper.WallpaperService;
import android.view.WindowManager;
import com.androidwasabi.p000a.C0064d;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class MyInputAdapter extends InputAdapter
    implements OnSharedPreferenceChangeListener, ApplicationListener, AndroidWallpaperListener {

  private float f281A = 1.0f;
  private boolean f282B = true;
  private boolean f283C = true;
  private boolean f284D = true;
  private int f285E = 0;
  private C0114a f286F;
  private GestureDetector f287G;
  private C0115b f288a = C0115b.Setup;
  private boolean f289b = false;
  private boolean f290c = false;
  private WallpaperService f291d;
  private SharedPreferences f292e;
  private OrthographicCamera f293f;
  private SpriteBatch f294g;
  private Vector3 f295h;
  private float f296i = 0.0f;
  private C0108a f297j = new C0108a();
  private int f298k = 30;
  private int f299l = 0;
  private long f300m = 0;
  private long f301n = 0;
  private float f302o = 0.0f;
  private boolean f303p = false;
  private C0109b f304q;
  private boolean f305r = true;
  private boolean f306s = false;
  private Vector2 f307t = new Vector2();
  private Vector2 f308u = new Vector2();
  private float f309v = 6.0f;
  private C0110c[] f310w = null;
  private boolean f311x = true;
  private int f312y = 20;
  private float f313z = 1.0f;

  @Override
  public void create() {
    this.f297j.m215a();
    this.f286F = new C0114a(this);
    this.f287G = new GestureDetector(this.f286F);
    Gdx.input.setInputProcessor(this.f287G);
    C0064d.m177a(this.f291d.getResources().getConfiguration().orientation == 2);
    this.f292e.registerOnSharedPreferenceChangeListener(this);
    onSharedPreferenceChanged(this.f292e, "");
  }

  @Override
  public void resize(int width, int height) {
    m276g();
  }

  @Override
  public void render() {
    if (this.f288a == C0115b.Setup) {
      m282b();
    }
    if (this.f288a == C0115b.Running) {
      this.f296i = Gdx.graphics.getDeltaTime() * this.f281A;
      if (Gdx.input.getNativeOrientation() != Orientation.Portrait) {
        switch (Gdx.input.getRotation()) {
          case 0:
            // TODO: 2017/10/13
            this.f307t.x = -Gdx.input.getDeltaX();
            this.f307t.y = Gdx.input.getDeltaY();
            break;
          case 90:
            this.f307t.x = -Gdx.input.getDeltaY();
            this.f307t.y = -Gdx.input.getDeltaX();
            break;
          case 180:
            this.f307t.x = Gdx.input.getDeltaX();
            this.f307t.y = -Gdx.input.getDeltaY();
            break;
          case 270:
            this.f307t.x = Gdx.input.getDeltaY();
            this.f307t.y = Gdx.input.getDeltaX();
            break;
          default:
            break;
        }
      }
      switch (Gdx.input.getRotation()) {
        case 0:
          this.f307t.x = Gdx.input.getDeltaY();
          this.f307t.y = Gdx.input.getDeltaX();
          break;
        case 90:
          this.f307t.x = -Gdx.input.getDeltaX();
          this.f307t.y = Gdx.input.getDeltaY();
          break;
        case 180:
          this.f307t.x = -Gdx.input.getDeltaY();
          this.f307t.y = -Gdx.input.getDeltaX();
          break;
        case 270:
          this.f307t.x = Gdx.input.getDeltaX();
          this.f307t.y = -Gdx.input.getDeltaY();
          break;
      }
      this.f308u.set(this.f307t);
      this.f307t.scl(this.f309v);
      Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      Gdx.gl20.glClear(16640);
      this.f294g.disableBlending();
      this.f294g.begin();
      if (this.f306s) {
        this.f304q.m221a(this.f307t);
      } else {
        this.f304q.m218a();
      }
      this.f304q.m220a(this.f294g, this.f297j.f215c);
      this.f294g.end();
      this.f294g.enableBlending();
      this.f294g.begin();
      this.f294g.setBlendFunction(770, 1);
      for (int i = 0; i < this.f310w.length; i++) {
        if (this.f283C) {
          this.f310w[i].m227a(this.f308u, this.f296i);
        } else {
          this.f310w[i].m223a(this.f296i);
        }
        this.f310w[i].m230b(this.f296i);
        if (this.f311x) {
          this.f310w[i].m226a(this.f294g, this.f297j.f216d);
        }
      }
      this.f294g.setBlendFunction(770, 771);
      this.f294g.end();
      try {
        this.f301n = System.currentTimeMillis() - this.f300m;
        if (this.f301n < ((long) this.f299l)) {
          Thread.sleep(((long) this.f299l) - this.f301n);
          this.f300m = System.currentTimeMillis();
          return;
        }
        this.f300m = System.currentTimeMillis();
      } catch (Throwable th) {
        th.printStackTrace();
      }
    }
  }

  @Override
  public void pause() {
    this.f285E = 0;
  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
      int xPixelOffset, int yPixelOffset) {
    if (this.f288a == C0115b.Running) {
      if (this.f285E > 3) {
        this.f284D = false;
      } else {
        this.f285E++;
      }
      if (!this.f284D) {
        if (this.f290c) {
          this.f302o = (((float) C0064d.m174a()) - this.f304q.f217g.x) / 2.0f;
          return;
        }
        if (this.f305r) {
          this.f302o =
              ((((float) C0064d.m174a()) - this.f304q.f221k.x) * xOffset) - this.f304q.f223m;
        } else {
          this.f302o = (((float) C0064d.m174a()) - this.f304q.f217g.x) / 2.0f;
        }
        if (this.f302o != 0.0f) {
          this.f304q.f218h.x = this.f302o;
        }
      }
    }
  }

  @Override
  public void previewStateChange(boolean isPreview) {
    this.f290c = isPreview;
  }

  private class C0114a implements GestureListener {

    boolean f270a = false;
    int f271b = 5;
    float f272c = 0.0f;
    float f273d = 0.0f;
    float f274e = 0.0f;
    float f275f = 0.0f;
    float f276g = 0.0f;
    final /* synthetic */ MyInputAdapter f277h;

    C0114a(MyInputAdapter myInputAdapter) {
      this.f277h = myInputAdapter;
    }

    public void m243a(int i) {
      this.f272c = this.f277h.f304q.f217g.x;
      this.f273d = (((float) C0064d.m174a()) - this.f272c) / 2.0f;
      this.f276g = Math.abs((((float) C0064d.m174a()) - this.f272c) / ((float) (i - 1)));
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
      if (this.f277h.f288a == C0115b.Running) {
        this.f270a = false;
        this.f275f = 0.0f;
        if (this.f277h.f282B && this.f277h.f311x) {
          this.f277h.f293f.unproject(this.f277h.f295h.set(x, y, 0.0f));
          for (C0110c a : this.f277h.f310w) {
            a.m228a(this.f277h.f295h.x, this.f277h.f295h.y);
          }
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
      this.f270a = true;
      return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
      if (this.f277h.f288a == C0115b.Running && this.f277h.f284D) {
        if (this.f277h.f290c || !this.f277h.f305r) {
          this.f273d = (((float) C0064d.m174a()) - this.f272c) / 2.0f;
          this.f277h.f304q.f218h.x = this.f273d;
        } else if (deltaX != 0.0f) {
          this.f274e = deltaX > 0.0f ? 6.0f : -6.0f;
          this.f275f += this.f274e;
          if (this.f274e < 0.0f) {
            if (this.f275f > 0.0f) {
              this.f275f = 0.0f;
            } else if (this.f275f >= (-this.f276g)) {
              this.f273d += this.f274e;
            }
          } else if (this.f274e > 0.0f) {
            if (this.f275f < 0.0f) {
              this.f275f = 0.0f;
            } else if (this.f275f <= this.f276g) {
              this.f273d += this.f274e;
            }
          }
          if (this.f273d >= (-this.f277h.f304q.f223m)) {
            this.f273d = -this.f277h.f304q.f223m;
          } else if (this.f273d <= (((float) C0064d.m174a()) - this.f277h.f304q.f221k.x)
              - this.f277h.f304q.f223m) {
            this.f273d =
                (((float) C0064d.m174a()) - this.f277h.f304q.f221k.x) - this.f277h.f304q.f223m;
          }
          this.f277h.f304q.f218h.x = this.f273d;
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
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
        Vector2 pointer2) {
      return false;
    }
  }

  enum C0115b {
    Setup,
    Running
  }

  public MyInputAdapter(WallpaperService wallpaperService) {
    this.f291d = wallpaperService;
    this.f292e = wallpaperService.getSharedPreferences("settings", 0);
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
    if (str.equals("")) {
      this.f303p = true;
      XperiaZColor.m232a(this.f292e.getString("color", "3"));
      this.f305r = this.f292e.getBoolean("scrolling", true);
      this.f306s = this.f292e.getBoolean("bgparallax", false);
      this.f311x = this.f292e.getBoolean("show", true);
      this.f312y = Integer.valueOf(this.f292e.getString("quantity", "20"));
      this.f313z = Float.valueOf(this.f292e.getString("size", "1"));
      this.f281A = Float.valueOf(this.f292e.getString("speed", "1"));
      this.f282B = this.f292e.getBoolean("touch", true);
      this.f283C = this.f292e.getBoolean("parallax", false);
      this.f298k = Integer.valueOf(this.f292e.getString("fps", "30"));
      this.f300m = System.currentTimeMillis();
      this.f299l = 1000 / this.f298k;
      this.f288a = C0115b.Setup;
    } else if (str.equals("smooth")) {
      this.f288a = C0115b.Setup;
    } else if (str.equals("fps")) {
      this.f298k = Integer.valueOf(this.f292e.getString("fps", "30"));
      this.f300m = System.currentTimeMillis();
      this.f299l = 1000 / this.f298k;
    } else if (str.equals("color")) {
      XperiaZColor.m232a(this.f292e.getString("color", "3"));
      this.f288a = C0115b.Setup;
    } else if (str.equals("background") || str.equals("type")) {
      this.f303p = true;
      this.f288a = C0115b.Setup;
    } else if (str.equals("bgparallax")) {
      this.f306s = this.f292e.getBoolean("bgparallax", false);
      this.f288a = C0115b.Setup;
    } else if (str.equals("show")) {
      this.f311x = this.f292e.getBoolean("show", true);
    } else if (str.equals("quantity")) {
      this.f312y = Integer.valueOf(this.f292e.getString("quantity", "20"));
      this.f288a = C0115b.Setup;
    } else if (str.equals("size")) {
      this.f313z = Float.valueOf(this.f292e.getString("size", "1"));
      this.f288a = C0115b.Setup;
    } else if (str.equals("speed")) {
      this.f281A = Float.valueOf(this.f292e.getString("speed", "1"));
    } else if (str.equals("touch")) {
      this.f282B = this.f292e.getBoolean("touch", true);
    } else if (str.equals("parallax")) {
      this.f283C = this.f292e.getBoolean("parallax", false);
    } else if (str.equals("scrolling")) {
      this.f305r = this.f292e.getBoolean("scrolling", true);
    }
  }

  public void m282b() {
    if (this.f303p) {
      this.f303p = false;
      this.f297j.m216a(this.f292e.getString("type", "1"));
    }
    this.f297j.m217a(this.f292e.getBoolean("smooth", true));

    m277h();
    this.f289b = true;
    this.f288a = C0115b.Running;
  }

  private void m276g() {
    int a = C0064d.m174a();
    int b = C0064d.m178b();
    boolean z = C0064d.f152a;
    C0064d.m176a(800, 480);
    C0064d.m175a(
        ((WindowManager) this.f291d.getApplication()
            .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
            .getOrientation());
    C0064d.m177a(this.f291d.getResources().getConfiguration().orientation == 2);
    if (C0064d.f152a != z || C0064d.m174a() != a || C0064d.m178b() != b) {
      this.f288a = C0115b.Setup;
    }
  }

  private void m277h() {
    float f;
    float f2;
    this.f295h = new Vector3();
    C0064d.m176a(800, 480);
    if (this.f293f == null) {
      this.f293f = new OrthographicCamera((float) C0064d.m174a(), (float) C0064d.m178b());
    }
    this.f293f.viewportWidth = (float) C0064d.m174a();
    this.f293f.viewportHeight = (float) C0064d.m178b();
    this.f293f.position.set((float) (C0064d.m174a() / 2), (float) (C0064d.m178b() / 2), 0.0f);
    this.f293f.update();
    if (this.f294g == null) {
      this.f294g = new SpriteBatch();
    }
    this.f294g.setProjectionMatrix(this.f293f.combined);
    if (this.f306s) {
      Vector2 vector2 = new Vector2((float) C0064d.m179c(), (float) C0064d.m179c());
      float f3 = this.f309v * 10.0f;
      f = vector2.x + (f3 * 2.0f);
      f2 = vector2.y + (f3 * 2.0f);
      this.f304q = new C0109b((((float) C0064d.m174a()) / 2.0f) - (f / 2.0f),
          (((float) C0064d.m178b()) / 2.0f) - (f2 / 2.0f), f, f2);
      this.f304q.f221k = vector2;
      this.f304q.f223m = f3;
    } else {
      this.f304q = new C0109b(
          (((float) C0064d.m174a()) / 2.0f) - (((float) C0064d.m179c()) / 2.0f),
          (((float) C0064d.m178b()) / 2.0f) - (((float) C0064d.m179c()) / 2.0f),
          (float) C0064d.m179c(), (float) C0064d.m179c());
      this.f304q.f221k = this.f304q.f217g;
      this.f304q.f223m = 0.0f;
    }
    this.f304q.m219a(XperiaZColor.f268a);
    Random random = new Random();
    this.f310w = new C0110c[this.f312y];
    for (int i = 0; i < this.f310w.length; i++) {
      float nextFloat = ((random.nextFloat() * 20.0f) + 6.0f) * this.f313z;
      f = random.nextFloat() * ((float) C0064d.m174a());
      f2 = (random.nextFloat() * ((((float) C0064d.m178b()) / 4.0f) * 2.0f)) + (
          ((float) C0064d.m178b()) / 4.0f);
      this.f310w[i] = new C0110c(f, f2, nextFloat, nextFloat) {
        protected void mo45b() {
          Random random = new Random();
          this.f249h.set(this.f137c);
          this.f141a
              .set((random.nextFloat() * 70.0f) + 10.0f, (random.nextFloat() * 15.0f) + 5.0f);
          this.f250i.set(this.f141a);
          this.f246L = ((float) random.nextInt(6)) + 6.0f;
          this.f257p = (random.nextFloat() * 0.6f) + 0.4f;
          m225a(0.0f, (random.nextFloat() * 0.4f) + 0.6f, (random.nextFloat() * 1.6f) + 0.4f,
              true);
        }

        protected void mo46c() {
          Random random = new Random();
          this.f137c.x = random.nextFloat() * ((float) C0064d.m174a());
          this.f137c.y = (random.nextFloat() * ((((float) C0064d.m178b()) / 4.0f) * 2.0f)) + (
              ((float) C0064d.m178b()) / 4.0f);
          this.f249h.set(this.f137c);
          this.f141a
              .set((random.nextFloat() * 70.0f) + 10.0f, (random.nextFloat() * 15.0f) + 5.0f);
          this.f250i.set(this.f141a);
          this.f238D.set(0.0f, 0.0f);
          this.f257p = (random.nextFloat() * 0.6f) + 0.4f;
          m225a(0.0f, (random.nextFloat() * 0.4f) + 0.6f, (random.nextFloat() * 1.6f) + 0.4f,
              true);
        }
      };

      this.f286F.m243a(5);
    }
  }
}
