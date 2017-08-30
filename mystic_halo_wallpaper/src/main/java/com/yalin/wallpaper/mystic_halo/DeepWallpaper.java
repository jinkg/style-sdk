package com.yalin.wallpaper.mystic_halo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.yalin.style.engine.GLWallpaperServiceProxy;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class DeepWallpaper extends GLWallpaperServiceProxy {
    boolean ACCELERATION = false;
    int AccelDelta = 0;
    float BaseMasterRot = 0.07f;
    boolean Clockwise = true;
    boolean CustomColor = false;
    boolean DrawMark = false;
    int ExtraX = 50;
    int ExtraY = 50;
    boolean GlowCentre = false;
    boolean HeartBeat = false;
    public int HeartRate = 1;
    boolean IsDefColor = true;
    boolean IsHDPhone = false;
    boolean IsqHD = false;
    boolean JustSharedPref = false;
    String LastParms = "";
    boolean LeaveCentre = false;
    boolean LinearAlpha = true;
    public int LinearRate = 8;
    float MasterAngle = 0.0f;
    float MasterDeltaRot = this.BaseMasterRot;
    public float MaxBrightness = 1.0f;
    int MinAlpha = 0;
    float Modscale = 1.0f;
    boolean NoAlpha = false;
    boolean OddsEvens = false;
    boolean PerRingColor = false;
    int ProgressiveFade = 0;
    boolean PulseBeat = false;
    boolean QuickTouch = false;
    float SaveX = 0.0f;
    float SaveY = 0.0f;
    int ScreenHeight = 0;
    int ScreenWidth = 0;
    boolean SwipeUp = false;
    boolean SyncAlt = false;
    boolean SyncRot = false;
    boolean TouchEnabled = false;
    boolean WasPreving = false;
    public int beatstepshigh = 45;
    public int beatstepsup = 10;
    int[] bitmaps = new int[8];
    String colortouse = "bluebase";
    int[] hsvs = new int[16];
    float lastxOffset = 0.5f;
    float lastxOffsetStep = 0.0f;
    int lastxPixelOffset = 0;
    float lastyOffset = 0.0f;
    float lastyOffsetStep = 0.0f;
    int lastyPixelOffset = 0;
    boolean mColorise = false;
    float mHue = 304.0f;
    float mSat = 100.0f;
    boolean mVisible = false;
    boolean noOuter = true;
    public float offx = 0.0f;
    public float offy = 0.0f;
    int[] poses = new int[8];
    boolean rebindtex = false;
    public boolean regeneratepulse = false;
    String[] ringcolors = new String[]{"bluebase", "bluebase", "bluebase", "bluebase", "bluebase",
            "bluebase", "bluebase", "bluebase", "bluebase"};
    int xPixOffset;
    int yPixOffset;

    public DeepWallpaper(Context host) {
        super(host);
    }

    class DeepEngine extends GLActiveEngine {
        DeepRenderer renderer;

        public DeepEngine(GLWallpaperService glws) {
            super();
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DeepWallpaper.this.ScreenWidth = display.getWidth();
            DeepWallpaper.this.ScreenHeight = display.getHeight();
            this.renderer = new DeepRenderer(glws, this);
            setRenderer(this.renderer);
            setRenderMode(0);
            onSharedPreferenceChanged();
            setTouchEventsEnabled(true);
        }

        public void onDestroy() {
            super.onDestroy();
            this.renderer.draw_handler.removeCallbacks(this.renderer.draw_run);
            if (this.renderer != null) {
                this.renderer.release();
            }
            this.renderer = null;
        }

        public void onVisibilityChanged(boolean visible) {
            if (!visible && DeepWallpaper.this.mVisible && isPreview()) {
                DeepWallpaper.this.WasPreving = true;
            }
            if (!visible || DeepWallpaper.this.mVisible) {
                this.renderer.draw_handler.removeCallbacks(this.renderer.draw_run);
            } else {
                this.renderer.draw_handler.postDelayed(this.renderer.draw_run, 40);
                if (DeepWallpaper.this.WasPreving && !isPreview()) {
                    onOffsetsChanged(DeepWallpaper.this.lastxOffset, DeepWallpaper.this.lastyOffset, DeepWallpaper.this.lastxOffsetStep, DeepWallpaper.this.lastyOffsetStep, DeepWallpaper.this.lastxPixelOffset, DeepWallpaper.this.lastyPixelOffset);
                    DeepWallpaper.this.regeneratepulse = true;
                    DeepWallpaper.this.WasPreving = false;
                }
                if (DeepWallpaper.this.JustSharedPref) {
                    if (!isPreview()) {
                        DeepWallpaper.this.JustSharedPref = false;
                    }
                    DeepWallpaper.this.rebindtex = true;
                }
            }
            DeepWallpaper.this.mVisible = visible;
        }

        public void onTouchEvent(MotionEvent event) {
            if (DeepWallpaper.this.TouchEnabled) {
                super.onTouchEvent(event);
            } else {
                super.onTouchEvent(event);
            }
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            if (!isPreview()) {
                DeepWallpaper.this.lastxOffset = xOffset;
                DeepWallpaper.this.lastyOffset = yOffset;
                DeepWallpaper.this.lastxOffsetStep = xOffsetStep;
                DeepWallpaper.this.lastyOffsetStep = yOffsetStep;
                DeepWallpaper.this.lastxPixelOffset = xPixelOffset;
                DeepWallpaper.this.lastyPixelOffset = yPixelOffset;
            }
            float y_add = 0.0f;
            if ((DeepWallpaper.this.ScreenWidth > 1000 || DeepWallpaper.this.ScreenHeight > 1000) && !DeepWallpaper.this.IsHDPhone) {
                y_add = -0.5f;
                if (this.renderer != null) {
                    this.renderer.BASESCALE = 0.1f;
                }
            }
            y_add = (float) (((double) y_add) + (((double) (((float) DeepWallpaper.this.ExtraY) / 100.0f)) - 0.5d));
            float x_add = (float) (((double) 0.0f) + (((double) (((float) DeepWallpaper.this.ExtraX) / 100.0f)) - 0.5d));
            if (isPreview()) {
                yOffset = 0.0f;
                xOffset = 0.5f;
            }
            if (DeepWallpaper.this.IsqHD) {
                yOffset = 0.0f;
            }
            DeepWallpaper.this.offx = (xOffset - 0.5f) + x_add;
            DeepWallpaper.this.offy = yOffset + y_add;
            DeepWallpaper.this.xPixOffset = xPixelOffset;
            DeepWallpaper.this.yPixOffset = yPixelOffset;
        }

        public void onSharedPreferenceChanged() {
            String[] sep;
            String fadestyle = "linearfade";
            NoAlpha = fadestyle.equals("off");
            LinearAlpha = fadestyle.equals("linearfade");
            HeartBeat = fadestyle.equals("heartbeat");
            PulseBeat = fadestyle.equals("pulse");
            MinAlpha = 100;
            LinearRate = 8;
            HeartRate = 1;
            MaxBrightness = ((float) 255) / 255.0f;
            IsqHD = true;
            LeaveCentre = false;
            GlowCentre = false;
            String syncstyle = "random";
            SyncRot = syncstyle.equals("sync");
            SyncAlt = syncstyle.equals("alternate");
            TouchEnabled = true;
            String newcolor = "bluebase";
            if (!newcolor.contentEquals(DeepWallpaper.this.colortouse)) {
                DeepWallpaper.this.colortouse = newcolor;
                DeepWallpaper.this.mColorise = true;
                DeepWallpaper.this.rebindtex = true;
                DeepWallpaper.this.JustSharedPref = true;
            }
            boolean nineparms = false;
            if (nineparms) {
                sep = "100_100".split("_");
                float tempHue = (float) Integer.parseInt(sep[0]);
                float tempSat = ((float) Integer.parseInt(sep[1])) / 100.0f;
                if (!(DeepWallpaper.this.mHue == tempHue && DeepWallpaper.this.mSat
                        == tempSat && DeepWallpaper.this.CustomColor)) {
                    DeepWallpaper.this.mHue = tempHue;
                    DeepWallpaper.this.mSat = tempSat;
                    DeepWallpaper.this.mColorise = true;
                    DeepWallpaper.this.rebindtex = true;
                    DeepWallpaper.this.JustSharedPref = true;
                    DeepWallpaper.this.CustomColor = true;
                }
            } else {
                if (DeepWallpaper.this.CustomColor) {
                    DeepWallpaper.this.mColorise = true;
                    DeepWallpaper.this.rebindtex = true;
                    DeepWallpaper.this.JustSharedPref = true;
                }
                DeepWallpaper.this.CustomColor = false;
            }
            boolean yOffset = true;
            if (yOffset) {
                DeepWallpaper.this.ExtraY = 0;
                DeepWallpaper.this.ExtraX = 0;
            } else {
                DeepWallpaper.this.ExtraY = 50;
                DeepWallpaper.this.ExtraX = 50;
            }
            boolean masterScale = true;
            if (masterScale) {
                DeepWallpaper.this.Modscale = ((float) 80) / 100.0f;
            } else {
                DeepWallpaper.this.Modscale = 1.0f;
            }
            noOuter = true;
            if (DeepWallpaper.this.BaseMasterRot != ((float) 7) / 100.0f) {
                DeepWallpaper.this.BaseMasterRot = ((float) 7) / 100.0f;
                DeepWallpaper.this.QuickTouch = true;
                DeepWallpaper.this.ACCELERATION = true;
            }
            boolean hdPhone = true;
            if (hdPhone) {
                IsHDPhone = true;
                if (this.renderer != null) {
                    this.renderer.BASESCALE = 0.15f;
                }
            } else {
                IsHDPhone = false;
            }
            boolean multitone = false;
            if (!multitone) {
                DeepWallpaper.this.PerRingColor = false;
            } else if (nineparms) {
                DeepWallpaper.this.PerRingColor = true;
                String multiparms = "0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0";
                if (multiparms != DeepWallpaper.this.LastParms) {
                    DeepWallpaper.this.LastParms = multiparms;
                    String[] vals = multiparms.split("_");
                    int i = 0;
                    while (i < 8) {
                        try {
                            DeepWallpaper.this.poses[i] = Integer.parseInt(vals[i]);
                            i++;
                        } catch (Exception e) {
                            for (i = 0; i < 8; i++) {
                                DeepWallpaper.this.poses[i] = 0;
                            }
                        }
                    }
                    i = 0;
                    while (i < 14) {
                        try {
                            DeepWallpaper.this.hsvs[i] = Integer.parseInt(vals[i + 8]);
                            i++;
                        } catch (Exception e2) {
                            for (i = 0; i < 14; i++) {
                                DeepWallpaper.this.hsvs[i] = 0;
                            }
                        }
                    }
                    if (DeepWallpaper.this.poses[0] == 0) {
                        DeepWallpaper.this.OddsEvens = true;
                    } else {
                        DeepWallpaper.this.OddsEvens = false;
                    }
                    String[] colorvals = getResources().getStringArray(R.array.valsperring);
                    for (int z = 0; z < 7; z++) {
                        DeepWallpaper.this.ringcolors[z] = colorvals[DeepWallpaper.this.poses[z + 1]];
                    }
                    DeepWallpaper.this.ringcolors[7] = DeepWallpaper.this.ringcolors[0];
                    DeepWallpaper.this.hsvs[14] = DeepWallpaper.this.hsvs[0];
                    DeepWallpaper.this.hsvs[15] = DeepWallpaper.this.hsvs[1];
                    DeepWallpaper.this.mColorise = true;
                    DeepWallpaper.this.rebindtex = true;
                    DeepWallpaper.this.JustSharedPref = true;
                    DeepWallpaper.this.CustomColor = true;
                }
            }
            sep = "10_45".split("_");
            DeepWallpaper.this.beatstepsup = Integer.parseInt(sep[0]);
            DeepWallpaper.this.beatstepshigh = Integer.parseInt(sep[1]);
            DeepWallpaper.this.regeneratepulse = true;
        }
    }

    public class DeepRenderer implements GLSurfaceView.Renderer {
        static final int VERTS = 4;
        float BASESCALE = 0.15f;
        float DELTAALPHA = -0.5f;
        float MAXROTVEL = 0.72f;
        float MinAlpha = 0.0f;
        float ModAlpha = 255.0f;
        int SPAWNINTERVAL = 5;
        int TTL = 250;
        int aniprogress = 0;
        float[] beatanimation;
        Handler draw_handler = new Handler();
        private final Runnable draw_run = new C00001();
        float fh = 1.0f;
        FLARE[] flares = new FLARE[8];
        int fnum = 0;
        float fw = 1.0f;
        GLActiveEngine gl_surface_view;
        FloatBuffer mFVertexBuffer;
        FloatBuffer mTexBuffer;
        EGLConfig mconfig;
        GL10 mgl;
        float[] modscales = new float[9];
        public float off_x = 0.0f;
        public float off_y = 0.0f;
        float[] pulseanimation;
        Random random;
        final float[] scales = new float[9];
        int[] sizes = new int[]{74, 140, 208, 308, 408, 574, 790, 800};
        int spawn_i = 0;
        int[] textures = new int[8];

        class C00001 implements Runnable {
            C00001() {
            }

            public void run() {
                gl_surface_view.requestRender();
            }
        }

        class FLARE {
            float angle;
            float anglez;
            float finsize;
            float initsize;
            float[] pos;
            float rotvel;
            float scale;
            int f0t;
            long ttl;

            FLARE() {
            }
        }

        public DeepRenderer(Context context, GLActiveEngine glsv) {
            this.gl_surface_view = glsv;
            DeepWallpaper.this.bitmaps[0] = R.drawable.seg0;
            DeepWallpaper.this.bitmaps[1] = R.drawable.seg1;
            DeepWallpaper.this.bitmaps[2] = R.drawable.seg2;
            DeepWallpaper.this.bitmaps[3] = R.drawable.seg3;
            DeepWallpaper.this.bitmaps[VERTS] = R.drawable.seg4;
            DeepWallpaper.this.bitmaps[5] = R.drawable.seg5;
            DeepWallpaper.this.bitmaps[6] = R.drawable.seg6;
            DeepWallpaper.this.bitmaps[7] = R.drawable.inner;
        }

        public void Accelerate(int arg) {
            if (DeepWallpaper.this.QuickTouch) {
                DeepWallpaper.this.QuickTouch = false;
                DeepWallpaper.this.ACCELERATION = false;
                DeepWallpaper.this.MasterDeltaRot = DeepWallpaper.this.BaseMasterRot;
                DeepWallpaper.this.AccelDelta = 0;
                ReRandomiseSpins();
            }
            int i;
            FLARE flare;
            if (arg == 1) {
                for (i = 0; i < 8; i++) {
                    if (this.flares[i].rotvel >= 0.0f) {
                        flare = this.flares[i];
                        flare.rotvel += 4.0f;
                    } else {
                        flare = this.flares[i];
                        flare.rotvel -= 4.0f;
                    }
                }
                DeepWallpaper.this.MasterDeltaRot = DeepWallpaper.this.BaseMasterRot + 1.0f;
                DeepWallpaper.this.ACCELERATION = true;
                DeepWallpaper.this.AccelDelta = 200;
            } else if (DeepWallpaper.this.ACCELERATION) {
                for (i = 0; i < 8; i++) {
                    if (this.flares[i].rotvel >= 0.0f) {
                        flare = this.flares[i];
                        flare.rotvel -= 0.02f;
                    } else {
                        flare = this.flares[i];
                        flare.rotvel += 0.02f;
                    }
                }
                DeepWallpaper deepWallpaper = DeepWallpaper.this;
                deepWallpaper.MasterDeltaRot -= 0.005f;
                deepWallpaper = DeepWallpaper.this;
                deepWallpaper.AccelDelta--;
                if (DeepWallpaper.this.AccelDelta == 0) {
                    DeepWallpaper.this.ACCELERATION = false;
                }
            }
        }

        public Bitmap changeHue2(Bitmap source, int hue, int sat) {
            Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
            float[] hsv = new float[3];
            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {
                    int c = source.getPixel(x, y);
                    if (c == -16777216 || c == 0) {
                        result.setPixel(x, y, c);
                    } else {
                        Color.colorToHSV(c, hsv);
                        hsv[0] = (float) hue;
                        hsv[1] = ((float) sat) / 100.0f;
                        result.setPixel(x, y, (Color.HSVToColor(hsv) & 16777215) | (c & -16777216));
                    }
                }
            }
            return result;
        }

        public Bitmap changeHue2(Bitmap source) {
            Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
            float[] hsv = new float[3];
            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {
                    int c = source.getPixel(x, y);
                    if (c == -16777216 || c == 0) {
                        result.setPixel(x, y, c);
                    } else {
                        Color.colorToHSV(c, hsv);
                        hsv[0] = DeepWallpaper.this.mHue;
                        hsv[1] = DeepWallpaper.this.mSat;
                        result.setPixel(x, y, (Color.HSVToColor(hsv) & 16777215) | (c & -16777216));
                    }
                }
            }
            return result;
        }

        public Bitmap changeHue(Bitmap source) {
            float[] mults2 = null;
            if (DeepWallpaper.this.colortouse.contentEquals("bluebase")) {
                return source;
            }
            float[] mults;
            if (DeepWallpaper.this.colortouse.contentEquals("apink") || DeepWallpaper.this.colortouse.contentEquals("magpink1")) {
                mults = new float[]{1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("tgreen")) {
                mults = new float[]{0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.3f, 0.0f, 0.0f, 0.0f, 0.3f, 0.3f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("nred")) {
                mults = new float[]{1.0f, 1.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("white")) {
                mults = new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("bgreen")) {
                mults = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("npink")) {
                mults = new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("magenta") || DeepWallpaper.this.colortouse.contentEquals("magblue") || DeepWallpaper.this.colortouse.contentEquals("magpink2")) {
                mults = new float[]{1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("yellow")) {
                mults = new float[]{0.7f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.7f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("pale")) {
                mults = new float[]{1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("golden") || DeepWallpaper.this.colortouse.contentEquals("fire1")) {
                mults = new float[]{1.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("borange")) {
                mults = new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.4f, 0.4f, 0.4f, 0.0f, 0.0f, 0.4f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("yorange")) {
                mults = new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.4f, 0.4f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("mgreen")) {
                mults = new float[]{1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("tgreen")) {
                mults = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("tblue") || DeepWallpaper.this.colortouse.contentEquals("ice")) {
                mults = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7f, 0.7f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else if (DeepWallpaper.this.colortouse.contentEquals("norange")) {
                mults = new float[]{1.0f, 1.0f, 1.5f, 0.0f, 0.0f, 0.7f, 0.7f, 0.1f, 0.0f, 0.0f, 0.3f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            } else {
                mults = new float[]{1.0f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            }
            Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {
                    int c = source.getPixel(x, y);
                    int srcr = Color.red(c);
                    int srcg = Color.green(c);
                    int srcb = Color.blue(c);
                    int srca = Color.alpha(c);
                    if (srcr == 0 && srcb == 0 && srcg == 0) {
                        result.setPixel(x, y, c);
                    } else {
                        int destR = (int) (((((float) srcr) * mults[0]) + (((float) srcg) * mults[1])) + (((float) srcb) * mults[2]));
                        int destG = (int) (((((float) srcr) * mults[5]) + (((float) srcg) * mults[6])) + (((float) srcb) * mults[7]));
                        int destB = (int) (((((float) srcr) * mults[10]) + (((float) srcg) * mults[11])) + (((float) srcb) * mults[12]));
                        if (destR > 255) {
                            destR = 255;
                        }
                        if (destG > 255) {
                            destG = 255;
                        }
                        if (destB > 255) {
                            destB = 255;
                        }
                        if (destR < 0) {
                            destR = 0;
                        }
                        if (destG < 0) {
                            destG = 0;
                        }
                        if (destB < 0) {
                            destB = 0;
                        }
                        result.setPixel(x, y, Color.argb(srca, destR, destG, destB));
                    }
                }
            }
            return result;
        }

        private void buildMipmap(GL10 gl, Bitmap bitmap) {
            int level = 0;
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            while (true) {
                if (height >= 1 || width >= 1) {
                    GLUtils.texImage2D(3553, level, bitmap, 0);
                    if (height != 1 && width != 1) {
                        level++;
                        height /= 2;
                        width /= 2;
                        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
                        bitmap.recycle();
                        bitmap = bitmap2;
                    } else {
                        return;
                    }
                }
                return;
            }
        }

        public void BindTextures() {
            GLES10.glGenTextures(8, this.textures, 0);
            int i = 0;
            while (i < 8) {
                Bitmap altered;
                GLES10.glBindTexture(3553, this.textures[i]);
                GLES10.glTexParameterf(3553, 10241, 9987.0f);
                GLES10.glTexParameterf(3553, 10240, 9729.0f);
                GLES10.glTexParameterx(3553, 10242, 33071);
                GLES10.glTexParameterx(3553, 10243, 33071);
                GLES10.glTexEnvf(8960, 8704, 8448.0f);
                Bitmap origin;
                if (DeepWallpaper.this.PerRingColor && DeepWallpaper.this.CustomColor) {
                    origin = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            DeepWallpaper.this.bitmaps[i]);
                    String backupcolor;
                    if (DeepWallpaper.this.OddsEvens) {
                        int o;
                        if (i % 2 == 0) {
                            o = 0;
                        } else {
                            o = 1;
                        }
                        if (i == 7) {
                            o = 0;
                        }
                        if (DeepWallpaper.this.ringcolors[o].contains("custom")) {
                            altered = changeHue2(origin, DeepWallpaper.this.hsvs[o * 2], DeepWallpaper.this.hsvs[(o * 2) + 1]);
                        } else {
                            backupcolor = DeepWallpaper.this.colortouse;
                            DeepWallpaper.this.colortouse = DeepWallpaper.this.ringcolors[o];
                            altered = changeHue(origin);
                            DeepWallpaper.this.colortouse = backupcolor;
                        }
                    } else if (DeepWallpaper.this.ringcolors[i].contains("custom")) {
                        altered = changeHue2(origin, DeepWallpaper.this.hsvs[i * 2], DeepWallpaper.this.hsvs[(i * 2) + 1]);
                    } else {
                        backupcolor = DeepWallpaper.this.colortouse;
                        DeepWallpaper.this.colortouse = DeepWallpaper.this.ringcolors[i];
                        altered = changeHue(origin);
                        DeepWallpaper.this.colortouse = backupcolor;
                    }
                } else if (DeepWallpaper.this.CustomColor) {
                    altered = changeHue2(BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]));
                } else if (DeepWallpaper.this.colortouse.contentEquals("magpink1")) {
                    altered = changeHue(BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]));
                    DeepWallpaper.this.colortouse = "magpink2";
                } else if (DeepWallpaper.this.colortouse.contentEquals("magpink2")) {
                    altered = changeHue(BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]));
                    DeepWallpaper.this.colortouse = "magpink1";
                } else if (DeepWallpaper.this.colortouse.contentEquals("fire1")) {
                    altered = changeHue(BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]));
                    DeepWallpaper.this.colortouse = "fire2";
                } else if (DeepWallpaper.this.colortouse.contentEquals("fire2")) {
                    altered = changeHue(BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]));
                    DeepWallpaper.this.colortouse = "fire1";
                } else if (DeepWallpaper.this.colortouse.contentEquals("bluebase")) {
                    DeepWallpaper.this.IsDefColor = true;
                    altered = BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]);
                } else {
                    origin = BitmapFactory.decodeResource(getApplicationContext().getResources(), DeepWallpaper.this.bitmaps[i]);
                    if ((DeepWallpaper.this.colortouse.contentEquals("magblue") || DeepWallpaper.this.colortouse.contentEquals("ice")) && i % 2 != 0) {
                        altered = origin;
                    } else {
                        altered = changeHue(origin);
                    }
                    DeepWallpaper.this.IsDefColor = false;
                }
                if (this.mgl instanceof GL11) {
                    this.mgl.glTexParameterf(3553, 33169, 1.0f);
                    GLUtils.texImage2D(3553, 0, altered, 0);
                } else {
                    buildMipmap(this.mgl, altered);
                }
                i++;
            }
            if (DeepWallpaper.this.colortouse.contentEquals("fire2")) {
                DeepWallpaper.this.colortouse = "fire1";
            }
            if (DeepWallpaper.this.colortouse.contentEquals("magpink2")) {
                DeepWallpaper.this.colortouse = "magpink1";
            }
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            int i;
            this.mgl = gl;
            this.mconfig = config;
            GLES10.glDisable(2929);
            GLES10.glDisable(3024);
            GLES10.glEnable(3553);
            GLES10.glHint(3152, 4353);
            GLES10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES10.glShadeModel(7425);
            GLES10.glEnable(3042);
            GLES10.glDisable(2912);
            GLES10.glDisable(2896);
            GLES10.glBlendFunc(770, 771);
            GLES10.glFrontFace(2305);
            GLES10.glEnableClientState(32888);
            GLES10.glEnableClientState(32884);
            GLES10.glActiveTexture(33984);
            BindTextures();
            ByteBuffer vbb = ByteBuffer.allocateDirect(48);
            vbb.order(ByteOrder.nativeOrder());
            this.mFVertexBuffer = vbb.asFloatBuffer();
            ByteBuffer tbb = ByteBuffer.allocateDirect(48);
            tbb.order(ByteOrder.nativeOrder());
            this.mTexBuffer = tbb.asFloatBuffer();
            float[] coords = new float[]{0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f};
            for (i = 0; i < VERTS; i++) {
                int j;
                for (j = 0; j < 3; j++) {
                    this.mFVertexBuffer.put(coords[(i * 3) + j] * 2.0f);
                }
            }
            for (i = 0; i < VERTS; i++) {
                for (int j = 0; j < 3; j++) {
                    this.mTexBuffer.put(coords[(i * 3) + j] + 0.5f);
                }
            }
            this.mFVertexBuffer.position(0);
            this.mTexBuffer.position(0);
            this.random = new Random();
            this.scales[0] = this.BASESCALE;
            int innerh = this.sizes[0];
            for (i = 1; i < 8; i++) {
                this.scales[i] = this.BASESCALE * (((float) this.sizes[i]) / ((float) innerh));
            }
            this.scales[8] = this.BASESCALE * 0.55f;
            GenerateBeat();
            GeneratePulse();
            ReModScaled();
        }

        public void ReModScaled() {
            int i;
            this.scales[0] = this.BASESCALE;
            int innerh = this.sizes[0];
            for (i = 1; i < 8; i++) {
                this.scales[i] = this.BASESCALE * (((float) this.sizes[i]) / ((float) innerh));
            }
            this.scales[8] = this.BASESCALE * 0.55f;
            for (i = 0; i < 9; i++) {
                this.modscales[i] = this.scales[i] * DeepWallpaper.this.Modscale;
            }
        }

        public void GeneratePulse() {
            int i;
            int stepsup = DeepWallpaper.this.beatstepsup;
            int stepshigh = DeepWallpaper.this.beatstepshigh;
            int max = (int) (DeepWallpaper.this.MaxBrightness * 255.0f);
            int min = DeepWallpaper.this.MinAlpha;
            this.pulseanimation = new float[((stepsup + stepsup) + stepshigh)];
            float stepdel = ((float) (max - min)) / ((float) stepsup);
            for (i = 0; i < stepsup; i++) {
                this.pulseanimation[i] = (((float) i) * stepdel) + ((float) min);
            }
            for (i = stepsup; i < stepshigh + stepsup; i++) {
                this.pulseanimation[i] = (float) max;
            }
            for (i = stepshigh + stepsup; i < (stepshigh + stepsup) + stepsup; i++) {
                this.pulseanimation[i] = (((float) (((stepshigh + stepsup) + stepsup) - i)) * stepdel) + ((float) min);
            }
        }

        public void GenerateBeat() {
            int i;
            int stepsup = DeepWallpaper.this.beatstepsup;
            int stepshigh = DeepWallpaper.this.beatstepshigh;
            int max = (int) (DeepWallpaper.this.MaxBrightness * 255.0f);
            int min = DeepWallpaper.this.MinAlpha;
            this.beatanimation = new float[800];
            float stepdel = ((float) (max - min)) / ((float) stepsup);
            for (i = 0; i < stepsup; i++) {
                this.beatanimation[i] = (((float) i) * stepdel) + ((float) min);
            }
            for (i = stepsup; i < stepsup * 2; i++) {
                this.beatanimation[i] = this.beatanimation[i - 1] - stepdel;
            }
            for (i = stepsup * 2; i < stepsup * 3; i++) {
                this.beatanimation[i] = this.beatanimation[i - 1] + stepdel;
            }
            for (i = stepsup * 3; i < (stepsup * 3) + stepshigh; i++) {
                this.beatanimation[i] = (float) max;
            }
            for (i = stepshigh + (stepsup * 3); i < (stepsup * VERTS) + stepshigh; i++) {
                this.beatanimation[i] = this.beatanimation[i - 1] - stepdel;
            }
        }

        public void ReRandomiseSpins() {
            for (int i = 0; i < 8; i++) {
                do {
                    this.flares[i].rotvel = ((this.random.nextFloat() * this.MAXROTVEL) * 2.0f) - this.MAXROTVEL;
                } while (this.flares[i].rotvel == 0.0f);
            }
        }

        public void Rebind() {
            int i;
            GLES10.glDeleteTextures(8, this.textures, 0);
            GLES10.glDisable(2929);
            GLES10.glDisable(3024);
            GLES10.glEnable(3553);
            GLES10.glHint(3152, 4353);
            GLES10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES10.glShadeModel(7425);
            GLES10.glEnable(3042);
            GLES10.glDisable(2912);
            GLES10.glDisable(2896);
            GLES10.glBlendFunc(770, 771);
            GLES10.glFrontFace(2305);
            GLES10.glEnableClientState(32888);
            GLES10.glEnableClientState(32884);
            GLES10.glActiveTexture(33984);
            BindTextures();
            ByteBuffer vbb = ByteBuffer.allocateDirect(48);
            vbb.order(ByteOrder.nativeOrder());
            this.mFVertexBuffer = vbb.asFloatBuffer();
            ByteBuffer tbb = ByteBuffer.allocateDirect(48);
            tbb.order(ByteOrder.nativeOrder());
            this.mTexBuffer = tbb.asFloatBuffer();
            float[] coords = new float[]{0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f};
            for (i = 0; i < VERTS; i++) {
                int j;
                for (j = 0; j < 3; j++) {
                    this.mFVertexBuffer.put(coords[(i * 3) + j] * 2.0f);
                }
            }
            for (i = 0; i < VERTS; i++) {
                for (int j = 0; j < 3; j++) {
                    this.mTexBuffer.put(coords[(i * 3) + j] + 0.5f);
                }
            }
            this.mFVertexBuffer.position(0);
            this.mTexBuffer.position(0);
        }

        public void onDrawFrame(GL10 gl) {
            int i;
            this.draw_handler.removeCallbacks(this.draw_run);
            this.draw_handler.postDelayed(this.draw_run, 40);
            ReModScaled();
            GLES10.glDisable(2929);
            GLES10.glEnable(3042);
            GLES10.glBlendFunc(770, 1);
            GLES10.glClear(16384);
            GLES10.glMatrixMode(5888);
            GLES10.glLoadIdentity();
            GLES10.glVertexPointer(3, 5126, 0, this.mFVertexBuffer);
            GLES10.glTexCoordPointer(3, 5126, 0, this.mTexBuffer);
            if (DeepWallpaper.this.rebindtex) {
                DeepWallpaper.this.rebindtex = false;
                Rebind();
                this.spawn_i = 0;
                this.fnum = 0;
            }
            if (DeepWallpaper.this.regeneratepulse) {
                DeepWallpaper.this.regeneratepulse = false;
                GeneratePulse();
                GenerateBeat();
            }
            if (this.spawn_i == 0 && this.fnum == 0) {
                this.fnum = 7;
                for (i = 0; i < 8; i++) {
                    this.flares[i] = new FLARE();
                    this.flares[i].pos = new float[2];
                    this.flares[i].anglez = 0.0f;
                    this.flares[i].ttl = (long) this.TTL;
                    this.flares[i].angle = 0.0f;
                    this.flares[i].initsize = 0.1f;
                    this.flares[i].finsize = this.random.nextFloat() + 1.0f;
                    this.flares[i].pos[0] = ((this.random.nextFloat() * this.fw) * 3.0f) - this.fw;
                    this.flares[i].pos[1] = ((this.random.nextFloat() * this.fh) * 2.0f) - this.fh;
                    this.flares[i].rotvel = ((this.random.nextFloat() * this.MAXROTVEL) * 2.0f) - this.MAXROTVEL;
                    this.flares[i].f0t = this.textures[i];
                }
                ReRandomiseSpins();
            }
            this.spawn_i++;
            if (this.spawn_i == this.SPAWNINTERVAL) {
                this.spawn_i = 0;
            }
            if (DeepWallpaper.this.ACCELERATION) {
                Accelerate(0);
            }
            i = 6;
            while (i >= 0) {
                FLARE flare = this.flares[i];
                flare.angle += this.flares[i].rotvel;
                DeepWallpaper deepWallpaper;
                if (DeepWallpaper.this.Clockwise) {
                    deepWallpaper = DeepWallpaper.this;
                    deepWallpaper.MasterAngle -= DeepWallpaper.this.MasterDeltaRot;
                } else {
                    deepWallpaper = DeepWallpaper.this;
                    deepWallpaper.MasterAngle += DeepWallpaper.this.MasterDeltaRot;
                }
                this.flares[i].scale = ((this.flares[i].initsize * ((float) this.flares[i].ttl)) / ((float) this.TTL)) + ((this.flares[i].finsize * ((float) (((long) this.TTL) - this.flares[i].ttl))) / ((float) this.TTL));
                GLES10.glLoadIdentity();
                GLES10.glTranslatef(-DeepWallpaper.this.offx, -DeepWallpaper.this.offy, 0.0f);
                if (DeepWallpaper.this.SyncRot) {
                    GLES10.glRotatef(DeepWallpaper.this.MasterAngle, 0.0f, 0.0f, 1.0f);
                } else if (!DeepWallpaper.this.SyncAlt) {
                    GLES10.glRotatef(this.flares[i].angle, 0.0f, 0.0f, 1.0f);
                } else if (i % 2 == 0) {
                    GLES10.glRotatef(DeepWallpaper.this.MasterAngle, 0.0f, 0.0f, 1.0f);
                } else {
                    GLES10.glRotatef(-1.0f * DeepWallpaper.this.MasterAngle, 0.0f, 0.0f, 1.0f);
                }
                GLES10.glScalef(this.modscales[i], this.modscales[i], this.modscales[i]);
                if (DeepWallpaper.this.NoAlpha) {
                    GLES10.glColor4f(1.0f, 1.0f, 1.0f, DeepWallpaper.this.MaxBrightness);
                } else if (DeepWallpaper.this.LeaveCentre && i == 0 && this.ModAlpha < 70.0f) {
                    GLES10.glColor4f(1.0f, 1.0f, 1.0f, 0.27450982f);
                } else {
                    GLES10.glColor4f(1.0f, 1.0f, 1.0f, this.ModAlpha / 255.0f);
                }
                GLES10.glBindTexture(3553, this.flares[i].f0t);
                GLES10.glDrawArrays(5, 0, VERTS);
                i--;
            }
            if (DeepWallpaper.this.GlowCentre && (DeepWallpaper.this.LinearAlpha || DeepWallpaper.this.PulseBeat || DeepWallpaper.this.HeartBeat)) {
                DrawMiddle(1);
            }
            if (DeepWallpaper.this.LinearAlpha) {
                LinearInOutAlphaMod();
            }
            if (DeepWallpaper.this.PulseBeat) {
                PulseAlpha();
            }
            if (DeepWallpaper.this.HeartBeat) {
                BeatAlpha();
            }
            if (DeepWallpaper.this.SwipeUp) {
                DeepWallpaper.this.SwipeUp = false;
                ReRandomiseSpins();
            }
            if (DeepWallpaper.this.DrawMark) {
                DeepWallpaper.this.DrawMark = false;
            }
        }

        void DrawMiddle(int parm) {
            GLES10.glLoadIdentity();
            GLES10.glTranslatef(-DeepWallpaper.this.offx, -DeepWallpaper.this.offy, 0.0f);
            GLES10.glScalef(this.modscales[8], this.modscales[8], this.modscales[8]);
            float newAlpha = (200.0f - this.ModAlpha) / 500.0f;
            if (parm == 0) {
                newAlpha = 0.0f;
            }
            GLES10.glColor4f(1.0f, 1.0f, 1.0f, newAlpha);
            GLES10.glBindTexture(3553, this.flares[7].f0t);
            GLES10.glDrawArrays(5, 0, VERTS);
        }

        public void DrawTouch(FLARE f) {
            GLES10.glLoadIdentity();
            GLES10.glTranslatef((DeepWallpaper.this.SaveX - 240.0f) / 240.0f, ((DeepWallpaper.this.SaveY - 400.0f) / 400.0f) * -1.5f, 0.0f);
            GLES10.glScalef(this.scales[8], this.scales[8], this.scales[8]);
            GLES10.glColor4f(1.0f, 1.0f, 1.0f, ((float) DeepWallpaper.this.ProgressiveFade) / 1000.0f);
            GLES10.glBindTexture(3553, f.f0t);
            GLES10.glDrawArrays(5, 0, VERTS);
            DeepWallpaper deepWallpaper = DeepWallpaper.this;
            deepWallpaper.ProgressiveFade--;
            if (DeepWallpaper.this.ProgressiveFade == 0) {
                DeepWallpaper.this.DrawMark = false;
            }
        }

        public void PulseAlpha() {
            if (this.aniprogress >= (DeepWallpaper.this.beatstepsup * 2) + DeepWallpaper.this.beatstepshigh) {
                this.aniprogress = 0;
            }
            float[] fArr = this.pulseanimation;
            int i = this.aniprogress;
            this.aniprogress = i + 1;
            this.ModAlpha = fArr[i];
        }

        public void BeatAlpha() {
            if (this.aniprogress >= DeepWallpaper.this.beatstepshigh + (DeepWallpaper.this.beatstepsup * VERTS)) {
                this.aniprogress = 0;
            }
            float[] fArr = this.beatanimation;
            int i = this.aniprogress;
            this.aniprogress = i + 1;
            this.ModAlpha = fArr[i];
            this.aniprogress += DeepWallpaper.this.HeartRate;
        }

        public void LinearInOutAlphaMod() {
            this.ModAlpha += this.DELTAALPHA * ((float) DeepWallpaper.this.LinearRate);
            if (this.ModAlpha >= 255.0f * DeepWallpaper.this.MaxBrightness) {
                this.DELTAALPHA = -0.5f;
            }
            if (this.ModAlpha <= ((float) DeepWallpaper.this.MinAlpha)) {
                this.DELTAALPHA = 0.5f;
            }
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {
            GLES10.glViewport(0, 0, w, h);
            if (w > h) {
                this.fw = ((float) w) / ((float) h);
                this.fh = 1.0f;
            } else {
                this.fw = 1.0f;
                this.fh = ((float) h) / ((float) w);
            }
            GLES10.glMatrixMode(5889);
            GLES10.glLoadIdentity();
            GLES10.glOrthof(-this.fw, this.fw, -this.fh, this.fh, -1.0f, 1.0f);
            GLES10.glMatrixMode(5888);
        }

        public void release() {
        }
    }

    public Engine onCreateEngine() {
        return new DeepEngine(this);
    }
}
