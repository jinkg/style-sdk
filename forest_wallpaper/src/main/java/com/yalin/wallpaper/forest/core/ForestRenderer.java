package com.yalin.wallpaper.forest.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.yalin.wallpaper.forest.ForestService;
import com.yalin.wallpaper.forest.R;
import com.yalin.wallpaper.forest.Settings;
import com.yalin.wallpaper.forest.core.gl.Program;
import com.yalin.wallpaper.forest.core.layer.ChristmasStarLayer;
import com.yalin.wallpaper.forest.core.layer.CloudLayer;
import com.yalin.wallpaper.forest.core.layer.FireworksLayer;
import com.yalin.wallpaper.forest.core.layer.ForestLayer;
import com.yalin.wallpaper.forest.core.layer.ForestLayer.Curve;
import com.yalin.wallpaper.forest.core.layer.Layer;
import com.yalin.wallpaper.forest.core.layer.MountainLayer;
import com.yalin.wallpaper.forest.core.layer.RainLayer;
import com.yalin.wallpaper.forest.core.layer.RainbowLayer;
import com.yalin.wallpaper.forest.core.layer.SkyLayer;
import com.yalin.wallpaper.forest.core.layer.SnowLayer;
import com.yalin.wallpaper.forest.core.layer.StarLayer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ForestRenderer implements Renderer, OnSharedPreferenceChangeListener {
    private static final String TAG = "ForestRenderer";
    private int aColor;
    private int aPosition;
    private int aSize;
    private Program basicProgram;
    private CalendarManager calendar = new CalendarManager();
    private int clearMask = 16384;
    public float defaultPointsize;
    private int deviceRotation = 0;
    private float dragPrevX;
    private float dragStartScreenOffset;
    private float dragStartX;
    private int[] fbo;
    private long fpsCount;
    private long frames;
    public float[] identityMatrix = new float[16];
    private boolean isDragging;
    private ArrayList<Layer> layers = new ArrayList<>();
    private Boolean logTime = Boolean.FALSE;
    private float[] modelMatrix = new float[16];
    private int oldTextureProgram;
    private boolean overrideSimulatedScrollingSetting;
    public int pixelHeight;
    public int pixelWidth;
    private int pointsize;
    private float[] projectionMatrix = new float[16];
    private long realTime;
    public int recolorTimer = 0;
    private RotationManager rotationManager;
    public float screenHeight;
    public float screenOffset;
    public float screenWidth;
    private float scrollVelocity;
    private Average scrollVelocityAverage = new Average(3);
    private boolean simulatedScrolling;
    private int[] tex;
    private ShortBuffer[] texBuffers;
    private Program textureProgram;
    private long timeAccumulated;
    private long timeStart;
    private int uMVPMatrix;
    private int useSizeBuffer;
    private Boolean useTexture = Boolean.valueOf(false);
    private boolean useTilt = false;
    private float[] viewMatrix = new float[16];
    public World world = new World();

    class C00465 implements Curve {
        C00465() {
        }

        public double mo18y(double x) {
            return (0.3d + (Math.sin((1.0025d * x) * 3.141592653589793d) * 0.15d)) + (Math.sin(0.35d * x) * 0.2d);
        }
    }

    class C00476 implements Curve {
        C00476() {
        }

        public double mo18y(double x) {
            return ((0.3d + (Math.sin(((0.6d + x) * 3.141592653589793d) * 1.1d) * 0.2d)) + (Math.sin(8.0d * x) * 0.03d)) + (Math.sin(0.15d * x) * 0.05d);
        }
    }

    public ForestRenderer(Context context) {
        this.simulatedScrolling = Settings.getBoolean(R.string.pk_simulated_scrolling,
                R.bool.default_simulated_scrolling);
        this.rotationManager = new RotationManager(context);
        updateTiltValues();
    }

    public void destroy() {
        this.rotationManager.unregisterListeners();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (Settings.keyAnyOf(key, R.string.pk_clouds_manual, R.string.pk_cloudiness, R.string.pk_day_night_cycle, R.string.pk_night_color, R.string.pk_sky_top_color, R.string.pk_sky_bottom_color, R.string.pk_hill_color, R.string.pk_tree_color, R.string.pk_mountain_color)) {
            updateColors();
        }
        if (Settings.keyEquals(key, R.string.pk_simulated_scrolling) && !this.overrideSimulatedScrollingSetting) {
            this.simulatedScrolling = Settings.getBoolean((int) R.string.pk_simulated_scrolling, (int) R.bool.default_simulated_scrolling);
        }
        if (Settings.keyAnyOf(key, R.string.pk_parallax_enabled, R.string.pk_parallax_sensor_rate, R.string.pk_parallax_depth)) {
            updateTiltValues();
        }
        if (Settings.keyEquals(key, R.string.pk_parallax_sensor_rate)) {
            this.rotationManager.updateSensorRate();
        }
        if (Layer.stateChangedForKey(key)) {
            updateLayers();
        }
    }

    public void overrideSimulatedScrollingSetting(boolean enabled) {
        this.overrideSimulatedScrollingSetting = true;
        this.simulatedScrolling = enabled;
    }

    public void onTouchEvent(MotionEvent event) {
        if (this.simulatedScrolling) {
            switch (event.getAction()) {
                case 0:
                    this.isDragging = true;
                    this.dragPrevX = event.getX();
                    this.dragStartX = event.getX();
                    this.dragStartScreenOffset = this.screenOffset;
                    return;
                case 1:
                    this.isDragging = false;
                    this.scrollVelocity = this.scrollVelocityAverage.avg();
                    this.scrollVelocityAverage.reset();
                    return;
                case 2:
                    this.scrollVelocityAverage.add((this.dragPrevX - event.getX()) / ((float) this.pixelWidth));
                    this.screenOffset = this.dragStartScreenOffset + (((this.dragStartX - event.getX()) / ((float) this.pixelWidth)) * 0.5f);
                    if (this.screenOffset < 0.0f) {
                        this.screenOffset = 0.0f;
                    } else if (this.screenOffset > 1.0f) {
                        this.screenOffset = 1.0f;
                    }
                    this.dragPrevX = event.getX();
                    return;
                default:
                    return;
            }
        }
    }

    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            updateColors();
            this.calendar.update(11.0d);
            if (this.useTilt) {
                this.rotationManager.registerListeners();
                return;
            }
            return;
        }
        this.rotationManager.unregisterListeners();
    }

    public void updateColors() {
        updateColors(true);
    }

    public void updateColors(boolean rebuild) {
        this.recolorTimer = 0;
        if (rebuild) {
            this.world.colors.rebuild();
        }
        if (this.layers.size() != 0) {
            synchronized (this.layers) {
                int max = this.layers.size();
                for (int i = 0; i < max; i++) {
                    this.layers.get(i).recolor();
                }
            }
        }
    }

    public void multisampling(Boolean useMultisampling, Boolean usesCoverageAA) {
        this.clearMask = 16640;
        if (useMultisampling && usesCoverageAA) {
            this.clearMask |= 32768;
        }
    }

    private void setupLayers() {
        this.layers.clear();
        SkyLayer skyLayer = new SkyLayer(this.world);
        skyLayer.id = 0;
        this.layers.add(skyLayer);
        StarLayer starLayer = new StarLayer(this.world);
        starLayer.id = 5;
        starLayer.setDistance(5.0f);
        this.layers.add(starLayer);
        ChristmasStarLayer christmasStarLayer = new ChristmasStarLayer(this.world);
        christmasStarLayer.id = 8;
        christmasStarLayer.setDistance(5.0f);
        this.layers.add(christmasStarLayer);
        CloudLayer clouds = new CloudLayer(this.world, 3.0f);
        clouds.id = 11;
        clouds.setDistance(4.75f);
        this.layers.add(clouds);
        MountainLayer mountains = new MountainLayer(this.world, 0.6d, 0.2d);
        mountains.id = 6;
        mountains.setDistance(4.5f);
        this.layers.add(mountains);
        FireworksLayer fireworksLayer = new FireworksLayer(this.world);
        fireworksLayer.id = 7;
        fireworksLayer.setDistance(4.0f);
        this.layers.add(fireworksLayer);
        ForestLayer f1 = new ForestLayer(this.world);
        f1.id = 1;
        f1.setDistance(2.0f);
        this.layers.add(f1);
        RainLayer rainLayer = new RainLayer(this.world);
        rainLayer.id = 3;
        rainLayer.setDistance(1.8f);
        this.layers.add(rainLayer);
        SnowLayer snowLayer = new SnowLayer(this.world);
        snowLayer.id = 9;
        snowLayer.setDistance(1.8f);
        this.layers.add(snowLayer);
        RainbowLayer rainbowLayer = new RainbowLayer(this.world);
        rainbowLayer.id = 12;
        rainbowLayer.setDistance(1.6f);
        this.layers.add(rainbowLayer);
        ForestLayer f2 = new ForestLayer(this.world);
        f2.id = 2;
        f2.setDistance(1.0f);
        this.layers.add(f2);
        rainLayer = new RainLayer(this.world);
        rainLayer.id = 4;
        rainLayer.setDistance(0.8f);
        this.layers.add(rainLayer);
        snowLayer = new SnowLayer(this.world);
        snowLayer.id = 10;
        snowLayer.setDistance(0.8f);
        this.layers.add(snowLayer);
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((Layer) it.next()).setScreenSize(this.screenWidth, this.screenHeight);
        }
        f1.setup(150, 1, 0.35f, 0.2f, new C00465(), Boolean.FALSE, 0.3f, 0.2f);
        f2.setup(100, 0, 0.5f, 0.4f, new C00476(), Boolean.FALSE, 0.4f, 0.3f);
        it = this.layers.iterator();
        while (it.hasNext()) {
            ((Layer) it.next()).prepareForRendering();
        }
        updateLayers();
    }

    public void updateLayers() {
        GregorianCalendar now = new GregorianCalendar();
        int year = now.get(Calendar.YEAR);
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            Layer l = (Layer) it.next();
            l.enabled = Layer.loadState(l.id);
            boolean z;
            if (l.id == 7) {
                z = now.get(Calendar.DAY_OF_YEAR) == 1
                        && now.get(Calendar.HOUR_OF_DAY) >= 0
                        && now.get(Calendar.HOUR_OF_DAY) < 4;
                l.enabled = z;
            } else if (l.id == 8) {
                z = now.after(new GregorianCalendar(year, 11, 23, 23, 0))
                        && now.before(new GregorianCalendar(year, 11, 26, 1, 0));
                l.enabled = z;
            } else if (l.id == 12) {
                l.enabled = Weather.INSTANCE.showRainbow();
            }
        }
    }

    private Layer layerById(int id) {
        for (int i = 0; i < this.layers.size(); i++) {
            Layer l = this.layers.get(i);
            if (l.id == id) {
                return l;
            }
        }
        return null;
    }

    private void updateTiltValues() {
        this.useTilt = Settings.getBoolean(R.string.pk_parallax_enabled, R.bool.default_parallax_enabled);
        if (this.useTilt && !this.rotationManager.isRegistered) {
            this.rotationManager.registerListeners();
        } else if (!this.useTilt && this.rotationManager.isRegistered) {
            this.rotationManager.unregisterListeners();
        }
        String s = "3";
        float tiltValue = 0.02f;
        if (s.equals("0")) {
            tiltValue = 0.01f;
        } else if (s.equals("1")) {
            tiltValue = 0.02f;
        } else if (s.equals("2")) {
            tiltValue = 0.03f;
        } else if (s.equals("3")) {
            tiltValue = 0.04f;
        }
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            Layer l = (Layer) it.next();
            l.setTiltValue(this.useTilt, tiltValue);
            l.setIdentityMatrix(this);
        }
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                                 float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        if (!this.simulatedScrolling) {
            this.screenOffset = xOffset;
        }
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glEnable(3024);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glDisable(2929);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Matrix.setLookAtM(this.viewMatrix, 0, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);
        Textures.loadAll();
    }

    public void onSurfaceChanged(GL10 unused, int w, int h) {
        this.screenWidth = ((float) w) / ((float) h);
        this.screenHeight = 1.0f;
        GLES20.glViewport(0, 0, w, h);
        this.deviceRotation = ((WindowManager) ForestService.context().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();
        this.world.scale = ((float) h) / 640.0f;
        this.defaultPointsize = this.world.scale;
        synchronized (this.layers) {
            if (!(w == this.pixelWidth && h == this.pixelHeight)) {
                setupLayers();
            }
        }
        this.pixelWidth = w;
        this.pixelHeight = h;
        Matrix.orthoM(this.projectionMatrix, 0, 0.0f, ((float) w) / ((float) h), 0.0f, 1.0f, 1.0f, 10.0f);
        Matrix.setIdentityM(this.modelMatrix, 0);
        Matrix.multiplyMM(this.identityMatrix, 0, this.viewMatrix, 0, this.modelMatrix, 0);
        Matrix.multiplyMM(this.identityMatrix, 0, this.projectionMatrix, 0, this.identityMatrix, 0);
        setupShaders();
        updateTiltValues();
        if (this.useTexture) {
            this.fbo = new int[1];
            this.tex = new int[1];
            this.texBuffers = new ShortBuffer[1];
            GLES20.glGenFramebuffers(1, this.fbo, 0);
            GLES20.glGenTextures(1, this.tex, 0);
            for (int i = 0; i < 1; i++) {
                GLES20.glBindTexture(3553, this.tex[i]);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10241, 9729);
                this.texBuffers[i] = ByteBuffer.allocateDirect(new int[(w * h)].length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
                GLES20.glTexImage2D(3553, 0, 6407, w, h, 0, 6407, 5121, this.texBuffers[i]);
            }
        }
    }

    public void onDrawFrame(GL10 unused) {
        this.timeStart = System.currentTimeMillis();
        this.calendar.update(0.0166666666667d);
        if (this.useTexture) {
            GLES20.glUseProgram(this.basicProgram.handle);
            GLES20.glBindFramebuffer(36160, this.fbo[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.tex[0], 0);
            if (GLES20.glCheckFramebufferStatus(36160) != 36053) {
                return;
            }
        }
        GLES20.glClear(this.clearMask);
        if (this.simulatedScrolling && !this.isDragging) {
            this.scrollVelocity = (float) (((double) this.scrollVelocity) * 0.9d);
            this.screenOffset += this.scrollVelocity;
            if (this.screenOffset > 1.0f) {
                this.screenOffset = 1.0f;
            } else if (this.screenOffset < 0.0f) {
                this.screenOffset = 0.0f;
            }
        }
        synchronized (this.layers) {
            float tiltX = 0.0f;
            float tiltY = 0.0f;
            if (this.useTilt) {
                if (this.deviceRotation == 0) {
                    tiltX = -this.rotationManager.lastPitch;
                    tiltY = this.rotationManager.lastYaw;
                } else if (this.deviceRotation == 1) {
                    tiltX = -this.rotationManager.lastYaw;
                    tiltY = -this.rotationManager.lastPitch;
                } else if (this.deviceRotation == 3) {
                    tiltX = this.rotationManager.lastYaw;
                    tiltY = this.rotationManager.lastPitch;
                }
            }
            int max = this.layers.size();
            for (int i = 0; i < max; i++) {
                Layer l = (Layer) this.layers.get(i);
                if (l.enabled) {
                    l.tick(0.02d);
                    l.setTilt(tiltX, tiltY);
                    l.onDraw(this);
                }
            }
        }
        int i2 = this.recolorTimer + 1;
        this.recolorTimer = i2;
        if (i2 >= 7200) {
            updateColors();
        }
        if (this.useTexture) {
            GLES20.glUseProgram(this.oldTextureProgram);
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glClear(this.clearMask);
            drawTextureToScreen(this.tex[0]);
        }
        if (this.logTime) {
            this.timeAccumulated += System.currentTimeMillis() - this.timeStart;
            long j = this.frames + 1;
            this.frames = j;
            if (j >= 200) {
                this.timeAccumulated = 0;
                this.frames = 0;
            }
            this.fpsCount++;
            if (System.currentTimeMillis() - this.realTime >= 1000) {
                this.fpsCount = 0;
                this.realTime = System.currentTimeMillis();
            }
        }
    }

    private void drawTextureToScreen(int tex) {
        FloatBuffer _qvb = Utils.arrayToBuffer(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, this.screenHeight, 0.0f, 1.0f, this.screenWidth, 0.0f, 1.0f, 0.0f, this.screenWidth, this.screenHeight, 1.0f, 1.0f});
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(this.oldTextureProgram, "aColor"), 4, 5126, false, 0, Utils.arrayToBuffer(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}));
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(this.oldTextureProgram, "aColor"));
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(this.oldTextureProgram, "uMVPMatrix"), 1, false, this.identityMatrix, 0);
        _qvb.position(0);
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(this.oldTextureProgram, "aPosition"), 2, 5126, false, 16, _qvb);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(this.oldTextureProgram, "aPosition"));
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, tex);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.oldTextureProgram, "texture1"), 0);
        _qvb.position(2);
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(this.oldTextureProgram, "textureCoord"), 2, 5126, false, 16, _qvb);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(this.oldTextureProgram, "textureCoord"));
        GLES20.glDrawArrays(5, 0, 4);
    }

    public void drawPoints(FloatBuffer vertices, FloatBuffer colors, int vertexCount, float[] matrix) {
        drawPoints(vertices, colors, vertexCount, this.defaultPointsize, matrix);
    }

    public void drawPoints(FloatBuffer vertices, FloatBuffer colors, int vertexCount, float pointSize, float[] matrix) {
        GLES20.glUniform1f(this.pointsize, pointSize);
        drawArrays(0, vertices, colors, null, vertexCount, matrix);
    }

    public void drawPoints(FloatBuffer vertices, FloatBuffer colors, FloatBuffer sizes, int vertexCount, float[] matrix) {
        drawArrays(0, vertices, colors, sizes, vertexCount, matrix);
    }

    public void drawLines(FloatBuffer vertices, FloatBuffer colors, int vertexCount, float[] matrix) {
        drawArrays(1, vertices, colors, null, vertexCount, matrix);
    }

    public void drawTriangles(FloatBuffer vertices, FloatBuffer colors, int vertexCount, float[] matrix) {
        drawArrays(4, vertices, colors, null, vertexCount, matrix);
    }

    public void drawTriangleStrips(FloatBuffer vertices, FloatBuffer colors, int vertexCount, float[] matrix) {
        drawArrays(5, vertices, colors, null, vertexCount, matrix);
    }

    public void drawArrays(int mode, FloatBuffer vertices, FloatBuffer colors, FloatBuffer sizes, int vertexCount, float[] matrix) {
        GLES20.glUseProgram(this.basicProgram.handle);
        GLES20.glUniformMatrix4fv(this.basicProgram.uMVPMatrix, 1, false, matrix, 0);
        if (sizes != null) {
            GLES20.glUniform1i(this.basicProgram.useSizeBuffer, 1);
            GLES20.glVertexAttribPointer(this.basicProgram.aSize, 1, 5126, false, 0, sizes);
            GLES20.glEnableVertexAttribArray(this.basicProgram.aSize);
        } else {
            GLES20.glUniform1i(this.basicProgram.useSizeBuffer, 0);
            GLES20.glDisableVertexAttribArray(this.basicProgram.aSize);
        }
        GLES20.glVertexAttribPointer(this.basicProgram.aPosition, 2, 5126, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(this.basicProgram.aPosition);
        GLES20.glVertexAttribPointer(this.basicProgram.aColor, 4, 5126, false, 0, colors);
        GLES20.glEnableVertexAttribArray(this.basicProgram.aColor);
        GLES20.glDrawArrays(mode, 0, vertexCount);
    }

    public void drawTexturedTriangles(FloatBuffer vertices, FloatBuffer colors, FloatBuffer texCoords, ShortBuffer indices, int vertexCount, float[] matrix) {
        GLES20.glUseProgram(this.textureProgram.handle);
        GLES20.glUniformMatrix4fv(this.textureProgram.uMVPMatrix, 1, false, matrix, 0);
        GLES20.glVertexAttribPointer(this.textureProgram.aPosition, 2, 5126, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aPosition);
        GLES20.glVertexAttribPointer(this.textureProgram.aColor, 4, 5126, false, 0, colors);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aColor);
        GLES20.glVertexAttribPointer(this.textureProgram.aTexCoord, 2, 5126, false, 0, texCoords);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aTexCoord);
        GLES20.glDrawElements(4, vertexCount, 5123, indices);
    }

    public void drawTexturedTriangleStrips(FloatBuffer vertices, FloatBuffer colors, FloatBuffer texCoords, int vertexCount, float[] matrix) {
        GLES20.glUseProgram(this.textureProgram.handle);
        GLES20.glUniformMatrix4fv(this.textureProgram.uMVPMatrix, 1, false, matrix, 0);
        GLES20.glVertexAttribPointer(this.textureProgram.aPosition, 2, 5126, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aPosition);
        GLES20.glVertexAttribPointer(this.textureProgram.aColor, 4, 5126, false, 0, colors);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aColor);
        GLES20.glVertexAttribPointer(this.textureProgram.aTexCoord, 2, 5126, false, 0, texCoords);
        GLES20.glEnableVertexAttribArray(this.textureProgram.aTexCoord);
        GLES20.glDrawArrays(5, 0, vertexCount);
    }

    public void drawPoint(float x, float y, float r, float g, float b, float a, float size, float[] matrix) {
        drawPoints(Utils.arrayToBuffer(new float[]{x, y}), Utils.arrayToBuffer(new float[]{r, g, b, a}), 1, size, matrix);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a, float[] matrix) {
        drawLine(x1, y1, x2, y2, r, g, b, a, r, g, b, a, matrix);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float[] matrix) {
        drawLines(Utils.arrayToBuffer(new float[]{x1, y1, x2, y2}), Utils.arrayToBuffer(new float[]{r1, g1, b1, a1, r2, g2, b2, a2}), 2, matrix);
    }

    private int compileShader(int shaderType, String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);
            int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, 35713, compileStatus, 0);
            if (compileStatus[0] == 0) {
                throw new RuntimeException("Error compiling shader:" + GLES20.glGetShaderInfoLog(shaderHandle));
            }
        }
        return shaderHandle;
    }

    private int createAndLinkProgram(int vertexShaderHandle, int fragmentShaderHandle) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            GLES20.glLinkProgram(programHandle);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, 35714, linkStatus, 0);
            if (linkStatus[0] == 0) {
                throw new RuntimeException("Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
            }
        }
        return programHandle;
    }

    private void setupShaders() {
        this.basicProgram = new Program(compileShader(35633, getVertexShaderCode()), compileShader(35632, getFragmentShaderCode()));
        this.basicProgram.use();
        this.textureProgram = new Program(compileShader(35633, getTextureVertexShaderCode()), compileShader(35632, getTextureFragmentShaderCode()));
        if (this.useTexture.booleanValue()) {
            this.oldTextureProgram = createAndLinkProgram(compileShader(35633, getVertexShaderCode2()), compileShader(35632, getFragmentShaderCode2()));
        }
    }

    private String getVertexShaderCode() {
        return "uniform float pointsize;uniform int useSizeBuffer;uniform mat4 uMVPMatrix;attribute vec4 aPosition;attribute vec4 aColor;attribute float aSize;varying vec4 vColor;void main() {  vColor = aColor;  gl_Position = uMVPMatrix * aPosition;  if(useSizeBuffer == 1) {    gl_PointSize = aSize;  } else {    gl_PointSize = pointsize;  }}";
    }

    private String getFragmentShaderCode() {
        return "precision mediump float;varying vec4 vColor;void main() {  gl_FragColor = vColor;}";
    }

    private String getTextureVertexShaderCode() {
        return "uniform mat4 uMVPMatrix;attribute vec4 aPosition;attribute vec2 aTexCoord;varying vec2 vTexCoord;attribute vec4 aColor;varying vec4 vColor;void main() {\tvColor = aColor;\tvTexCoord = aTexCoord;\tgl_Position = uMVPMatrix * aPosition;}";
    }

    private String getTextureFragmentShaderCode() {
        return "precision mediump float;uniform sampler2D uTexture;varying vec2 vTexCoord;varying vec4 vColor;void main() {\tgl_FragColor = texture2D(uTexture, vTexCoord) * vColor;}";
    }

    private String getVertexShaderCode2() {
        return "uniform mat4 uMVPMatrix;attribute vec4 aPosition;attribute vec2 textureCoord;varying vec2 tCoord;varying vec2 v_blurTexCoords[3];attribute vec4 aColor;varying vec4 vColor;void main() {\tvColor = aColor;\ttCoord = textureCoord;\tgl_Position = uMVPMatrix * aPosition;\tv_blurTexCoords[0] = tCoord + vec2(-0.005,  0.002);\tv_blurTexCoords[1] = tCoord + vec2( 0.005,  0.002);\tv_blurTexCoords[2] = tCoord + vec2( 0.0,   -0.005);}";
    }

    private String getFragmentShaderCode2() {
        return "precision mediump float;uniform sampler2D texture1;varying vec2 tCoord;varying vec4 vColor;varying vec2 v_blurTexCoords[3];vec4 hardlight(in vec4 base, in vec4 blend){\treturn vec4((blend.r < 0.5 ? (2.0 * base.r * blend.r) : (1.0 - 2.0 * (1.0 - base.r) * (1.0 - blend.r))),\t\t    (blend.g < 0.5 ? (2.0 * base.g * blend.g) : (1.0 - 2.0 * (1.0 - base.g) * (1.0 - blend.g))),\t\t    (blend.b < 0.5 ? (2.0 * base.b * blend.b) : (1.0 - 2.0 * (1.0 - base.b) * (1.0 - blend.b))), 1.0);}vec4 softlight(in vec4 base, in vec4 blend){\treturn vec4((blend.r < 0.5) ? (2.0 * base.r * blend.r + base.r*base.r - 2.0 * base.r*base.r*blend.r) : (2.0 * sqrt(base.r) * blend.r - sqrt(base.r) + 2.0 * base.r - 2.0 * base.r*blend.r),\t\t    (blend.g < 0.5) ? (2.0 * base.g * blend.g + base.g*base.g - 2.0 * base.g*base.g*blend.g) : (2.0 * sqrt(base.g) * blend.g - sqrt(base.g) + 2.0 * base.g - 2.0 * base.g*blend.g),\t\t    (blend.b < 0.5) ? (2.0 * base.b * blend.b + base.b*base.b - 2.0 * base.b*base.b*blend.b) : (2.0 * sqrt(base.b) * blend.b - sqrt(base.b) + 2.0 * base.b - 2.0 * base.b*blend.b), 1.0);}void main() {\tvec4 col = texture2D(texture1, tCoord);\tgl_FragColor = vec4(0.0);\tgl_FragColor += col\t\t\t\t       *0.4;\tgl_FragColor += texture2D(texture1, v_blurTexCoords[0])*0.2;\tgl_FragColor += texture2D(texture1, v_blurTexCoords[1])*0.2;\tgl_FragColor += texture2D(texture1, v_blurTexCoords[2])*0.2;\tgl_FragColor = hardlight(col, gl_FragColor);}";
    }
}
