package com.yalin.wallpaper.miku;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import jp.kzfactory.utils.android.BufferUtil;
import jp.kzfactory.utils.android.FileManager;
import jp.kzfactory.utils.android.LoadUtil;
import jp.kzfactory.utils.android.ModelSetting;
import jp.kzfactory.utils.android.ModelSettingJson;
import jp.kzfactory.utils.android.SoundUtil;
import jp.live2d.Live2D;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.framework.L2DBaseModel;
import jp.live2d.framework.L2DExpressionMotion;
import jp.live2d.framework.L2DEyeBlink;
import jp.live2d.framework.L2DModelMatrix;
import jp.live2d.framework.L2DPhysics;
import jp.live2d.framework.L2DPose;
import jp.live2d.framework.L2DStandardID;
import jp.live2d.motion.AMotion;
import jp.live2d.util.UtSystem;

public class LAppModel extends L2DBaseModel {
    static boolean TapRendaStop = false;
    static FloatBuffer debugBufferColor = null;
    static FloatBuffer debugBufferVer = null;
    static Object lock = new Object();
    int Aisatsu = 0;
    boolean FirstAisatsu = false;
    public String TAG = "LAppModel ";
    private String modelHomeDir;
    private ModelSetting modelSetting = null;
    private MediaPlayer voice;

    public LAppModel() {
        if (LAppDefine.DEBUG_LOG) {
            this.mainMotionManager.setMotionDebugMode(true);
        }
    }

    public void release() {
        if (this.live2DModel != null) {
            this.live2DModel.deleteTextures();
            Log.d("LAppModel　release()", "　live2DModel.deleteTextures() テクスチャーを開放");
        }
    }

    public void load(GL10 gl, String modelSettingPath) throws Exception {
        this.updating = true;
        this.initialized = false;
        this.modelHomeDir = modelSettingPath.substring(0, modelSettingPath.lastIndexOf("/") + 1);
        if (LAppDefine.DEBUG_LOG) {
            Log.d(this.TAG, "json読み込み : " + modelSettingPath);
        }
        try {
            int i;
            InputStream in = FileManager.open(modelSettingPath);
            this.modelSetting = new ModelSettingJson(in);
            in.close();
            if (this.modelSetting.getModelName() != null) {
                this.TAG += this.modelSetting.getModelName();
            }
            if (LAppDefine.DEBUG_LOG) {
                Log.d(this.TAG, "モデルを読み込みます");
            }
            loadModelData(gl, this.modelHomeDir, this.modelSetting.getModelFile(), this.modelSetting.getTextureFiles());
            loadExpressions(this.modelHomeDir, this.modelSetting.getExpressionNames(), this.modelSetting.getExpressionFiles());
            loadPhysics(this.modelHomeDir, this.modelSetting.getPhysicsFile());
            loadPose(this.modelHomeDir, this.modelSetting.getPoseFile());
            HashMap<String, Float> layout = new HashMap();
            if (this.modelSetting.getLayout(layout)) {
                if (layout.get("width") != null) {
                    this.modelMatrix.setWidth(((Float) layout.get("width")).floatValue());
                }
                if (layout.get("height") != null) {
                    this.modelMatrix.setHeight(((Float) layout.get("height")).floatValue());
                }
                if (layout.get("x") != null) {
                    this.modelMatrix.setX(((Float) layout.get("x")).floatValue());
                }
                if (layout.get("y") != null) {
                    this.modelMatrix.setY(((Float) layout.get("y")).floatValue());
                }
                if (layout.get("center_x") != null) {
                    this.modelMatrix.centerX(((Float) layout.get("center_x")).floatValue());
                }
                if (layout.get("center_y") != null) {
                    this.modelMatrix.centerY(((Float) layout.get("center_y")).floatValue());
                }
                if (layout.get("top") != null) {
                    this.modelMatrix.top(((Float) layout.get("top")).floatValue());
                }
                if (layout.get("bottom") != null) {
                    this.modelMatrix.bottom(((Float) layout.get("bottom")).floatValue());
                }
                if (layout.get("left") != null) {
                    this.modelMatrix.left(((Float) layout.get("left")).floatValue());
                }
                if (layout.get("right") != null) {
                    this.modelMatrix.right(((Float) layout.get("right")).floatValue());
                }
            }
            for (i = 0; i < this.modelSetting.getInitParamNum(); i++) {
                this.live2DModel.setParamFloat(this.modelSetting.getInitParamID(i), this.modelSetting.getInitParamValue(i));
            }
            for (i = 0; i < this.modelSetting.getInitPartsVisibleNum(); i++) {
                this.live2DModel.setPartsOpacity(this.modelSetting.getInitPartsVisibleID(i), this.modelSetting.getInitPartsVisibleValue(i));
            }
            this.eyeBlink = new L2DEyeBlink();
            this.updating = false;
            this.initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("LAppModel", "　ファイルの指定ミス。続行不可");
            throw new Exception();
        }
    }

    public void loadExpressions(String dir, String[] names, String[] fileNames) {
        if (fileNames != null && fileNames.length != 0) {
            int i = 0;
            while (i < fileNames.length) {
                try {
                    if (LAppDefine.DEBUG_LOG) {
                        Log.d(this.TAG, "表情ファイルを読み込みます : " + fileNames[i]);
                    }
                    InputStream in = FileManager.open(dir + fileNames[i]);
                    this.expressions.put(names[i], L2DExpressionMotion.loadJson(in));
                    in.close();
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    }

    public void loadPose(String dir, String fileName) {
        if (fileName != null) {
            if (LAppDefine.DEBUG_LOG) {
                Log.d(this.TAG, "json読み込み : " + fileName);
            }
            try {
                InputStream in = FileManager.open(dir + fileName);
                this.pose = L2DPose.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void loadPhysics(String dir, String fileName) {
        if (fileName != null) {
            Log.d(this.TAG, "loadPhysics　　json読み込み : " + fileName);
            try {
                InputStream in = FileManager.open(dir + fileName);
                this.physics = L2DPhysics.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void loadModelData(GL10 gl, String dir, String modelFile, String[] texFiles) throws Exception {
        if (modelFile != null && texFiles != null) {
            if (this.live2DModel != null) {
                this.live2DModel.deleteTextures();
            }
            try {
                if (LAppDefine.DEBUG_LOG) {
                    Log.d(this.TAG, "モデル読み込み : " + modelFile);
                }
                InputStream in = FileManager.open(dir + modelFile);
                this.live2DModel = Live2DModelAndroid.loadModel(in);
                in.close();
                if (Live2D.getError() != Live2D.L2D_NO_ERROR) {
                    throw new Exception();
                }
                for (int i = 0; i < texFiles.length; i++) {
                    if (LAppDefine.DEBUG_LOG) {
                        Log.d("", "テクスチャ読み込み : " + texFiles[i]);
                    }
                    in = FileManager.open(dir + texFiles[i]);
                    ((Live2DModelAndroid) this.live2DModel).setTexture(i, LoadUtil.loadTexture(gl, in, true));
                    in.close();
                }
                this.modelMatrix = new L2DModelMatrix(this.live2DModel.getCanvasWidth(), this.live2DModel.getCanvasHeight());
                this.modelMatrix.setWidth(LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2);
                this.modelMatrix.setCenterPosition(0.0f, 0.0f);
            } catch (IOException e) {
                e.printStackTrace();
                if (this.live2DModel != null) {
                    this.live2DModel.deleteTextures();
                }
                throw new Exception();
            }
        }
    }

    public void preloadMotionGroup(String name) {
        int len = this.modelSetting.getMotionNum(name);
        for (int i = 0; i < len; i++) {
            AMotion motion = LoadUtil.loadAssetsMotion(this.modelHomeDir + this.modelSetting.getMotionFile(name, i));
            motion.setFadeIn(this.modelSetting.getMotionFadeIn(name, i));
            motion.setFadeOut(this.modelSetting.getMotionFadeOut(name, i));
        }
    }

    public void update() {
        if (this.live2DModel != null) {
            double t = (2.0d * (((double) (UtSystem.getUserTimeMSec() - this.startTimeMSec)) / 1000.0d)) * 3.141592653589793d;
            synchronized (lock) {
                if (this.mainMotionManager.isFinished()) {
                    if (LiveWallpaper.sleep == 1 || LiveWallpaper.sleep == 2) {
                        startRandomMotion("idle_sleep01", 3);
                    } else if (LiveWallpaper.sleep == 10) {
                        startRandomMotion("idle_sleepy", 1);
                    } else if (LiveWallpaper.level <= LAppDefine.BATTERY_LOW && LiveWallpaper.plugged == 0) {
                        startRandomMotion("idle_low", 1);
                    } else if (LiveWallpaper.level <= LAppDefine.BATTERY_MID && LiveWallpaper.plugged == 0) {
                        startRandomMotion("idle_mid", 1);
                    } else if (MKConfig.Start_Aisatsu) {
                        if (this.Aisatsu == 0) {
                            Log.d("aisatu", "MOTION_GROUP_START00" + this.Aisatsu);
                            startRandomMotion("start00", 3);
                            this.Aisatsu = 1;
                        }
                        if (this.Aisatsu == 1) {
                            Log.d("aisatu", "MOTION_GROUP_START01" + this.Aisatsu);
                            this.Aisatsu = 0;
                            if (LiveWallpaper.diffDays >= 1) {
                                startRandomMotion("start02", 4);
                            } else {
                                startRandomMotion("start01", 4);
                            }
                            MKConfig.Start_Aisatsu = false;
                        }
                    } else if (LiveWallpaper.katamuki) {
                        Log.d("aisatu", "MOTION_GROUP_KATAMUKI" + this.Aisatsu);
                        LiveWallpaper.katamuki = false;
                        startRandomMotion("katamuki", 3);
                    } else if (LiveWallpaper.VisibleIn && !LiveWallpaper.preview_mode && !LiveWallpaper.LockScreenMode) {
                        LiveWallpaper.VisibleIn = false;
                        Log.d("aisatu", "あいさつ" + this.Aisatsu);
                        if (LiveWallpaper.time_ohayo + 18000000 < System.currentTimeMillis()) {
                            LiveWallpaper.Aisatsu_ohayo = false;
                            Log.d("Aisatsu_ohayo", LiveWallpaper.time_ohayo + " あいさつA" + LiveWallpaper.Aisatsu_ohayo + "  " + System.currentTimeMillis());
                        }
                        if (LiveWallpaper.time_am + 18000000 < System.currentTimeMillis()) {
                            LiveWallpaper.Aisatsu_am = false;
                        }
                        if (LiveWallpaper.time_pm + 18000000 < System.currentTimeMillis()) {
                            LiveWallpaper.Aisatsu_pm = false;
                        }
                        if (LiveWallpaper.hour >= 5 && LiveWallpaper.hour <= 10 && !LiveWallpaper.Aisatsu_ohayo) {
                            startRandomMotion("ohayou", 3);
                            Log.d("Aisatsu_ohayo", LiveWallpaper.time_ohayo + "  あいさつ" + LiveWallpaper.Aisatsu_ohayo);
                            LiveWallpaper.Aisatsu_ohayo = true;
                            LiveWallpaper.time_ohayo = System.currentTimeMillis();
                        } else if (LiveWallpaper.hour >= 11 && LiveWallpaper.hour <= 18 && !LiveWallpaper.Aisatsu_am) {
                            startRandomMotion("konnitiwa", 3);
                            LiveWallpaper.Aisatsu_am = true;
                            LiveWallpaper.time_am = System.currentTimeMillis();
                        } else if ((LiveWallpaper.hour >= 19 || LiveWallpaper.hour <= 4) && !LiveWallpaper.Aisatsu_pm) {
                            LiveWallpaper.Aisatsu_pm = true;
                            startRandomMotion("konbanwa", 3);
                            LiveWallpaper.time_pm = System.currentTimeMillis();
                        } else {
                            startRandomMotion("okaerinasai", 3);
                            Log.d("Aisatsu_okaerinasai", LiveWallpaper.time_ohayo + "  おかえりなさい" + LiveWallpaper.Aisatsu_ohayo + "  " + LiveWallpaper.hour + "時 " + LiveWallpaper.minute + "分");
                        }
                    } else if (LAppLive2DManager_WP.okiru_3rd) {
                        startRandomMotion("idle_muka", 3);
                    } else if (LiveWallpaper.Headphone) {
                        startRandomMotion("idle_headphone", 1);
                    } else {
                        startRandomMotion("idle", 1);
                    }
                }
                this.live2DModel.loadParam();
                if (!this.mainMotionManager.updateParam(this.live2DModel)) {
                    this.eyeBlink.updateParam(this.live2DModel);
                }
                this.live2DModel.saveParam();
            }
            if (this.expressionManager != null) {
                this.expressionManager.updateParam(this.live2DModel);
            }
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_X, this.dragX * 30.0f, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Y, this.dragY * 30.0f, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Z, (this.dragX * this.dragY) * -30.0f, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_BODY_ANGLE_X, this.dragX * 10.0f, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_EYE_BALL_X, this.dragX, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_EYE_BALL_Y, this.dragY, 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_X, (float) (15.0d * Math.sin(t / 6.5345d)), 0.5f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Y, (float) (8.0d * Math.sin(t / 3.5345d)), 0.5f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Z, (float) (10.0d * Math.sin(t / 5.5345d)), 0.5f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_BODY_ANGLE_X, (float) (4.0d * Math.sin(t / 15.5345d)), 0.5f);
            this.live2DModel.setParamFloat(L2DStandardID.PARAM_BREATH, (float) (0.5d + (0.5d * Math.sin(t / 3.2345d))), 1.0f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Z, (90.0f * (-LiveWallpaper.sx)) / 10.0f, 0.5f);
            this.live2DModel.addToParamFloat(L2DStandardID.PARAM_BUST_Y, (90.0f * (-LiveWallpaper.sy)) / 200.0f, 0.5f);
            if (this.physics != null) {
                this.physics.updateParam(this.live2DModel);
            }
            if (this.lipSync) {
                this.live2DModel.setParamFloat(L2DStandardID.PARAM_MOUTH_OPEN_Y, this.lipSyncValue, 0.8f);
            }
            if (this.pose != null) {
                this.pose.updateParam(this.live2DModel);
            }
            this.live2DModel.update();
        } else if (LAppDefine.DEBUG_LOG) {
            Log.d(this.TAG, "モデルデータがないので更新できません");
        }
    }

    private void drawHitArea(GL10 gl) {
        gl.glDisable(3553);
        gl.glDisableClientState(32888);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32886);
        gl.glPushMatrix();
        gl.glMultMatrixf(this.modelMatrix.getArray(), 0);
        int len = this.modelSetting.getHitAreasNum();
        for (int i = 0; i < len; i++) {
            int drawIndex = this.live2DModel.getDrawDataIndex(this.modelSetting.getHitAreaID(i));
            if (drawIndex >= 0) {
                float[] points = this.live2DModel.getTransformedPoints(drawIndex);
                float left = this.live2DModel.getCanvasWidth();
                float right = 0.0f;
                float top = this.live2DModel.getCanvasHeight();
                float bottom = 0.0f;
                for (int j = 0; j < points.length; j += 2) {
                    float x = points[j];
                    float y = points[j + 1];
                    if (x < left) {
                        left = x;
                    }
                    if (x > right) {
                        right = x;
                    }
                    if (y < top) {
                        top = y;
                    }
                    if (y > bottom) {
                        bottom = y;
                    }
                }
                float[] vertex = new float[]{left, top, right, top, right, bottom, left, bottom, left, top};
                float[] color = new float[]{1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f};
                gl.glLineWidth((float) 5);
                gl.glVertexPointer(2, 5126, 0, BufferUtil.setupFloatBuffer(debugBufferVer, vertex));
                gl.glColorPointer(4, 5126, 0, BufferUtil.setupFloatBuffer(debugBufferColor, color));
                gl.glDrawArrays(3, 0, 5);
            }
        }
        gl.glPopMatrix();
        gl.glEnable(3553);
        gl.glEnableClientState(32888);
        gl.glEnableClientState(32884);
        gl.glDisableClientState(32886);
    }

    public void startRandomMotion(String name, int priority) {
        try {
            startMotion(name, (int) (Math.random() * ((double) this.modelSetting.getMotionNum(name))), priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMotion(String name, int no, int priority) {
        String motionName = this.modelSetting.getMotionFile(name, no);
        if (motionName != null && !motionName.equals("")) {
            if (this.mainMotionManager.reserveMotion(priority)) {
                AMotion motion = LoadUtil.loadAssetsMotion(this.modelHomeDir + motionName);
                if (motion == null) {
                    Log.e(this.TAG, "モーションの読み込みに失敗しました。");
                    this.mainMotionManager.setReservePriority(0);
                    return;
                }
                motion.setFadeIn(this.modelSetting.getMotionFadeIn(name, no));
                motion.setFadeOut(this.modelSetting.getMotionFadeOut(name, no));
                if (this.modelSetting.getMotionSound(name, no) == null || !LiveWallpaper.Voice) {
                    Log.d(this.TAG, "モーションの開始(ボイスなし) : " + motionName + "   voice = " + LiveWallpaper.Voice);
                    this.mainMotionManager.startMotionPrio(motion, priority);
                    return;
                }
                String soundName = this.modelSetting.getMotionSound(name, no);
                MediaPlayer player = LoadUtil.loadAssetsSound(this.modelHomeDir + soundName);
                Log.d(this.TAG, "モーションの開始 : " + motionName + " 音声 : " + soundName);
                startVoiceMotion(motion, player, priority);
                return;
            }
            Log.e(this.TAG, "再生予約があるか再生中のモーションがあるので再生しません");
        }
    }

    public void startVoiceMotion(final AMotion motion, final MediaPlayer player, final int priority) {
        player.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                SoundUtil.release(LAppModel.this.voice);
                synchronized (LAppModel.lock) {
                    LAppModel.this.mainMotionManager.startMotionPrio(motion, priority);
                }
                LAppModel.this.voice = player;
                LAppModel.this.voice.start();
            }
        });
        player.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                SoundUtil.release(player);
            }
        });
        try {
            player.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void setExpression(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setRandomExpression() {
        int no = (int) (Math.random() * ((double) this.expressions.size()));
        if (LAppDefine.DEBUG_LOG) {
            Log.d(this.TAG, "表情をランダムに切り替える : " + this.expressions.size());
        }
        setExpression(((String[]) this.expressions.keySet().toArray(new String[this.expressions.size()]))[no]);
    }

    public void setExpressionTime(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionLevel1(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionLevel2(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionTemp1(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionTemp2(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionTemp3(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionVol_1(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionVolt01(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionCharge(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionCpu(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionRam(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void setExpressionWifi(String name) {
        if (this.expressions.containsKey(name)) {
            this.expressionManager.startMotion((AMotion) this.expressions.get(name), false);
        }
    }

    public void draw(GL10 gl) {
        ((Live2DModelAndroid) this.live2DModel).setGL(gl);
        this.alpha += this.accAlpha;
        if (this.alpha < 0.0f) {
            this.alpha = 0.0f;
            this.accAlpha = 0.0f;
        } else if (this.alpha > 1.0f) {
            this.alpha = 1.0f;
            this.accAlpha = 0.0f;
        }
        if (((double) this.alpha) >= 0.001d) {
            gl.glPushMatrix();
            gl.glMultMatrixf(this.modelMatrix.getArray(), 0);
            this.live2DModel.draw();
            gl.glPopMatrix();
            if (LAppDefine.DEBUG_DRAW_HIT_AREA) {
                drawHitArea(gl);
            }
        }
    }

    public boolean hitTest(String id, float testX, float testY) {
        boolean z = false;
        if (this.modelSetting != null) {
            int len = this.modelSetting.getHitAreasNum();
            int i = 0;
            while (i < len) {
                try {
                    if (id.equals(this.modelSetting.getHitAreaName(i))) {
                        z = hitTestSimple(this.modelSetting.getHitAreaID(i), testX, testY);
                        break;
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("タッチエラー ", "タッチエラー ");
                }
            }
        }
        return z;
    }

    public void feedIn() {
        this.alpha = 0.0f;
        this.accAlpha = 0.1f;
    }
}
