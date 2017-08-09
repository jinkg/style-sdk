/*
 * Copyright (C) 2011 QSDN,Inc.
 * Copyright (C) 2011 Atsushi Konno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yalin.wallpaper.shark;

import android.content.Context;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;

import com.yalin.wallpaper.shark.model.Background;
import com.yalin.wallpaper.shark.model.Ground;
import com.yalin.wallpaper.shark.model.Iwashi;
import com.yalin.wallpaper.shark.model.IwashiData;
import com.yalin.wallpaper.shark.model.Model;
import com.yalin.wallpaper.shark.model.Shumoku;
import com.yalin.wallpaper.shark.model.Wave;
import com.yalin.wallpaper.shark.util.CoordUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.yalin.wallpaper.shark.R;


public class GLRenderer {
    private static final boolean _debug = false;
    public static final int DEFAULT_IWASHI_COUNT = 20;
    public static final int DEFAULT_IWASHI_SPEED = 50;
    public static final boolean DEFAULT_IWASHI_BOIDS = true;
    public static final boolean DEFAULT_SHUMOKU_BOIDS = true;
    public static final int DEFAULT_CAMERA_DISTANCE = 10;
    public static final int DEFAULT_SHUMOKU_SPEED = 50;
    public static final int DEFAULT_SHUMOKU_COUNT = 5;

    private static final String TAG = GLRenderer.class.getName();
    /**
     * IWASHI is sardine. it is japanese.
     */
    public static final int MAX_IWASHI_COUNT = 150;
    /**
     * SHUMOKU ZAME is hammerhead shark. it is japanese.
     */
    public static final int MAX_SHUMOKU_COUNT = 10;
    private final Background background = new Background();
    private final Ground ground = new Ground();
    private final Wave wave = new Wave();
    private Model[] iwashi = null;
    private Model[] shumoku = null;
    private int iwashi_count = 1;
    private int shumoku_count = 1;
    private boolean enableIwashiBoids = true;
    private boolean enableShumokuBoids = true;
    private float iwashi_speed = 0.03f;
    private float shumoku_speed = 0.03f;
    /**
     * Position of Camera
     */
    private float[] camera = {0f, 0f, 0f};
    private float[] org_camera = {0f, 0f, 0f};

    /*
     * Distance from camera to aquarium center.
     */
    private float cameraDistance = 10f;
    private float zFar = 80.0f;
    private float zNear = 1.0f;
    private float perspectiveAngle = 45.0f;
    public long tick = 0L;
    public long prevTick = 0L;

    private BaitManager baitManager = new BaitManager();

    private float baseAngle = 0f;
    private float[] mScratch32 = new float[32];
    private float[] mScratch4f = new float[4];
    public static GLRenderer glRenderer = null;

    /* Center of school */
    float[] schoolCenter = {0f, 0f, 0f};
    private CoordUtil coordUtil = new CoordUtil();

    private GLRenderer(Context context) {
        iwashi_count = DEFAULT_IWASHI_COUNT;
        shumoku_count = DEFAULT_SHUMOKU_COUNT;
        iwashi_speed = ((float) DEFAULT_IWASHI_SPEED / 50f) * Iwashi.DEFAULT_SPEED;
        shumoku_speed = ((float) DEFAULT_SHUMOKU_SPEED / 50f) * Shumoku.DEFAULT_SPEED;
        enableIwashiBoids = DEFAULT_IWASHI_BOIDS;
        enableShumokuBoids = DEFAULT_SHUMOKU_BOIDS;
        cameraDistance = (float) DEFAULT_CAMERA_DISTANCE;

        IwashiData.init();

        shumoku = new Shumoku[MAX_SHUMOKU_COUNT];
        for (int ii = 0; ii < MAX_SHUMOKU_COUNT; ii++) {
            shumoku[ii] = new Shumoku(ii);
            ((Shumoku) shumoku[ii]).setBaitManager(baitManager);
            ((Shumoku) shumoku[ii]).setSpeed(shumoku_speed);
            ((Shumoku) shumoku[ii]).setSpecies(shumoku);
            ((Shumoku) shumoku[ii]).setEnableBoids(enableShumokuBoids);
        }


        iwashi = new Model[MAX_IWASHI_COUNT];
        for (int ii = 0; ii < MAX_IWASHI_COUNT; ii++) {
            iwashi[ii] = new Iwashi(ii);
        }
        for (int ii = 0; ii < MAX_IWASHI_COUNT; ii++) {
            ((Iwashi) iwashi[ii]).setEnableBoids(enableIwashiBoids);
            ((Iwashi) iwashi[ii]).setEnemiesCount(shumoku_count);
            ((Iwashi) iwashi[ii]).setEnemies(shumoku);
            ((Iwashi) iwashi[ii]).setSpecies(iwashi);
            ((Iwashi) iwashi[ii]).setSpeed(iwashi_speed);
            ((Iwashi) iwashi[ii]).setIwashiCount(iwashi_count);
            ((Iwashi) iwashi[ii]).setBaitManager(baitManager);
        }
    }

    public static GLRenderer getInstance(Context context) {
        if (_debug) Log.d(TAG, "start getInstance()");
        if (glRenderer == null) {
            if (_debug) Log.d(TAG, "new GLRenderer");
            glRenderer = new GLRenderer(context);
        }
        if (_debug) Log.d(TAG, "end getInstance()");
        return glRenderer;
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig, Context context) {
        if (_debug) Log.d(TAG, "start onSurfaceCreated()");
        gl10.glEnable(GL10.GL_DEPTH_TEST);
        gl10.glDepthFunc(GL10.GL_LEQUAL);

        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    /*=======================================================================*/
    /* カリングの有効化                                                      */
    /*=======================================================================*/
        gl10.glEnable(GL10.GL_CULL_FACE);
    /*=======================================================================*/
    /* ティザーを無効に                                                      */
    /*=======================================================================*/
        gl10.glEnable(GL10.GL_DITHER);

    /*=======================================================================*/
    /* OpenGLにスムージングを設定                                            */
    /*=======================================================================*/
        gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

    /*=======================================================================*/
    /* テクスチャ                                                            */
    /*=======================================================================*/
        gl10.glEnable(GL10.GL_TEXTURE_2D);
        Background.loadTexture(gl10, context, R.drawable.background);
        Ground.loadTexture(gl10, context, R.drawable.sand);
        Wave.loadTexture(gl10, context, R.drawable.wave);
        Iwashi.loadTexture(gl10, context, R.drawable.iwashi);
        Shumoku.loadTexture(gl10, context, R.drawable.shumoku);


        org_camera[0] = camera[0] = 0f;
        org_camera[1] = camera[1] = 0f;
        org_camera[2] = camera[2] = Aquarium.max_z + zNear;

    /*=======================================================================*/
    /* フォグのセットアップ                                                  */
    /*=======================================================================*/
        setupFog(gl10);

        gl10.glEnable(GL10.GL_NORMALIZE);
        gl10.glEnable(GL10.GL_RESCALE_NORMAL);
        gl10.glShadeModel(GL10.GL_SMOOTH);
        //背景のクリア
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl10.glClearDepthf(1.0f);

        if (_debug) Log.d(TAG, "end onSurfaceCreated()");
    }

    public void onSurfaceDestroyed(GL10 gl10) {
        Background.deleteTexture(gl10);
        Ground.deleteTexture(gl10);
        Wave.deleteTexture(gl10);
        Iwashi.deleteTexture(gl10);
    }

    /**
     * 光のセットアップ
     */
    public void setupLighting1(GL10 gl10) {
        gl10.glEnable(GL10.GL_LIGHTING);
        gl10.glEnable(GL10.GL_LIGHT0);
        gl10.glEnable(GL10.GL_LIGHT1);
    }

    public void setupLighting2(GL10 gl10) {
        {
      /*=======================================================================*/
      /* 環境光の色設定                                                        */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 1.0f;
                mScratch4f[1] = 1.0f;
                mScratch4f[2] = 1.0f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* 拡散反射光の色設定                                                    */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 1.0f;
                mScratch4f[1] = 1.0f;
                mScratch4f[2] = 1.0f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* 鏡面反射光の色設定                                                    */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 1.0f;
                mScratch4f[1] = 1.0f;
                mScratch4f[2] = 1.0f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* そもそもの光の位置設定                                                */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 0.0f;
                mScratch4f[1] = 10.0f;
                mScratch4f[2] = 0.0f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* そもそもの光の向き設定                                                */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 0.0f;
                mScratch4f[1] = -1.0f;
                mScratch4f[2] = 0.0f;
                mScratch4f[3] = 0.0f;
                gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, mScratch4f, 0);
            }
            gl10.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 90);
            gl10.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_EXPONENT, 0);
      /*=======================================================================*/
      /* 減衰ほとんどなしに設定                                                */
      /*=======================================================================*/
            gl10.glLightf(GL10.GL_LIGHT0, GL10.GL_CONSTANT_ATTENUATION, 0.2f);
            gl10.glLightf(GL10.GL_LIGHT0, GL10.GL_LINEAR_ATTENUATION, 0.002f);
            gl10.glLightf(GL10.GL_LIGHT0, GL10.GL_QUADRATIC_ATTENUATION, 0.0f);
        }
        {
      /*=======================================================================*/
      /* 環境光の色設定                                                        */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 0.019f * 0.6f;
                mScratch4f[1] = 0.9606f * 0.6f;
                mScratch4f[2] = 1.0f * 0.6f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, mScratch4f, 0);
      /*=======================================================================*/
      /* 拡散反射光の色設定                                                    */
      /*=======================================================================*/
                gl10.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, mScratch4f, 0);
      /*=======================================================================*/
      /* 鏡面反射光の色設定                                                    */
      /*=======================================================================*/
                mScratch4f[0] *= 0.5f;
                mScratch4f[1] *= 0.5f;
                mScratch4f[2] *= 0.5f;
                gl10.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* そもそもの光の位置設定                                                */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 0.0f;
                mScratch4f[1] = -10.0f;
                mScratch4f[2] = 0.0f;
                mScratch4f[3] = 1.0f;
                gl10.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, mScratch4f, 0);
            }
      /*=======================================================================*/
      /* そもそもの光の向き設定                                                */
      /*=======================================================================*/
            synchronized (mScratch4f) {
                mScratch4f[0] = 0.0f;
                mScratch4f[1] = 1.0f;
                mScratch4f[2] = 0.0f;
                mScratch4f[3] = 0.0f;
                gl10.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPOT_DIRECTION, mScratch4f, 0);
            }
            gl10.glLightf(GL10.GL_LIGHT1, GL10.GL_SPOT_CUTOFF, 90);
            gl10.glLightf(GL10.GL_LIGHT1, GL10.GL_SPOT_EXPONENT, 0);
      /*=======================================================================*/
      /* 減衰ほとんどなしに設定                                                */
      /*=======================================================================*/
            gl10.glLightf(GL10.GL_LIGHT1, GL10.GL_CONSTANT_ATTENUATION, 0.2f);
            gl10.glLightf(GL10.GL_LIGHT1, GL10.GL_LINEAR_ATTENUATION, 0.002f);
            gl10.glLightf(GL10.GL_LIGHT1, GL10.GL_QUADRATIC_ATTENUATION, 0.0f);
        }

    }

    /**
     * フォグのセットアップ
     */
    public void setupFog(GL10 gl10) {
        gl10.glEnable(GL10.GL_FOG);
        gl10.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        gl10.glFogf(GL10.GL_FOG_START, 7f + (cameraDistance - 5f));
        gl10.glFogf(GL10.GL_FOG_END, Aquarium.max_x + 28.0f + (cameraDistance - 5f));

        synchronized (mScratch4f) {
            mScratch4f[0] = 0.011f;
            mScratch4f[1] = 0.4218f;
            mScratch4f[2] = 0.6445f;
            mScratch4f[3] = 1.0f;
            gl10.glFogfv(GL10.GL_FOG_COLOR, mScratch4f, 0);
        }
    }

    public void setupFog2(GL10 gl10) {
        gl10.glEnable(GL10.GL_FOG);
        gl10.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        gl10.glFogf(GL10.GL_FOG_START, cameraDistance + 1f);
        gl10.glFogf(GL10.GL_FOG_END, cameraDistance + 10f);

        synchronized (mScratch4f) {
            mScratch4f[0] = 1.0f;
            mScratch4f[1] = 1.0f;
            mScratch4f[2] = 1.0f;
            mScratch4f[3] = 1.0f;
            gl10.glFogfv(GL10.GL_FOG_COLOR, mScratch4f, 0);
        }
    }

    public void updateSetting(Context context) {

    }


    private int screen_width = 0;
    private int screen_height = 0;

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        if (_debug) Log.d(TAG, "start onSurfaceChanged()");
        gl10.glViewport(0, 0, width, height);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        float ratio = (float) width / height;
        CoordUtil.perspective(gl10, perspectiveAngle, ratio, zNear, zFar);
        this.screen_width = width;
        this.screen_height = height;

        if (_debug) Log.d(TAG, "end onSurfaceChanged()");
    }

    public void onOffsetsChanged(GL10 gl10, float xOffset, float yOffset,
                                 float xOffsetStep, float yOffsetStep,
                                 int xPixelOffset, int yPixelOffset) {
        if (_debug) Log.d(TAG, "start onOffsetsChanged()");
        if (_debug) {
            Log.d(TAG,
                    "xOffset:[" + xOffset + "]:"
                            + "yOffset:[" + yOffset + "]:"
                            + "xOffsetStep:[" + xOffsetStep + "]:"
                            + "yOffsetStep:[" + yOffsetStep + "]:"
                            + "xPixelOffset:[" + xPixelOffset + "]:"
                            + "yPixelOffset:[" + yPixelOffset + "]:");
        }
        synchronized (this) {
            float xx = (float) xPixelOffset / 480f;
            baseAngle = xx * 180f + 90f;
        }
        if (_debug) {
            Log.d(TAG,
                    "end onOffsetsChanged():"
                            + "new baseAngle:[" + baseAngle + "]"
            );
        }
    }

    public void onCommand(GL10 gl10, String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
        if (_debug) Log.d(TAG, "start onCommand");
    /*=======================================================================*/
    /* タッチされたら寄ってくる                                              */
    /* 餌はiwashi_count分                                                    */
    /*=======================================================================*/

    /*=======================================================================*/
    /* スクリーン座標ー＞ワールド座標変換                                    */
    /*=======================================================================*/
        float[] modelview = new float[16];
        ((GL11) gl10).glGetFloatv(GL10.GL_MODELVIEW, modelview, 0);
        float[] projection = new float[16];
        ((GL11) gl10).glGetFloatv(GL10.GL_PROJECTION, projection, 0);
        float[] viewport = new float[16];
        ((GL11) gl10).glGetFloatv(GL11.GL_VIEWPORT, viewport, 0);

        float[] view = new float[16];
        System.arraycopy(CoordUtil.viewMatrix, 0, view, 0, 16);
        float nx = 0f;
        float ny = 0f;
        float nz = 0f;
        {
            float[] ret = new float[4];
      /* カメラから水槽までの距離を算出 */
            float dist_from_camera = 0.0f;
            dist_from_camera = cameraDistance;
            if (dist_from_camera < 0.0f) {
                dist_from_camera = 0.0f;
            } else {
                dist_from_camera = (dist_from_camera / (zFar - zNear));
                if (dist_from_camera > 1.0f) {
                    dist_from_camera = 1.0f;
                }
            }

            GLU.gluUnProject((float) x, (float) y, dist_from_camera, view, 0, projection, 0, new int[]{0, 0, screen_width, screen_height}, 0, ret, 0);
            if (_debug)
                Log.d(TAG, "変換結果(UnProject):[" + ret[0] + "][" + ret[1] + "][" + ret[2] + "][" + ret[3] + "]");
            {
                float bb = (cameraDistance == 0.0f) ? 0.1f : cameraDistance;
                nx = ret[0] * bb / ret[3];
                ny = ret[1] * -bb / ret[3];
                nz = ret[2] / ret[3];
            }
            if (_debug) {
                Log.d(TAG, "変換結果"
                        + "dist:[" + dist_from_camera + "] "
                        + "x:[" + nx + "] "
                        + "y:[" + ny + "] "
                        + "z:[" + nz + "] "
                );
            }
        }

        synchronized (mScratch4f) {
            coordUtil.setMatrixRotateY(-baseAngle);
            coordUtil.affine(nx, ny, nz, mScratch4f);
            nx = mScratch4f[0];
            ny = mScratch4f[1];
            nz = mScratch4f[2];

        }

        {
            float tmp = 0f;
            if (nx > Aquarium.max_x.floatValue() - 0.2f) {
                tmp = (Aquarium.max_x.floatValue() - 0.2f) / nx;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
            if (ny > Aquarium.max_y.floatValue() - 0.2f) {
                tmp = (Aquarium.max_y.floatValue() - 0.2f) / ny;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
            if (nz > Aquarium.max_z.floatValue() - 0.2f) {
                tmp = (Aquarium.max_z.floatValue() - 0.2f) / nz;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
            if (nx < Aquarium.min_x.floatValue() + 0.2f) {
                tmp = (Aquarium.min_x.floatValue() + 0.2f) / nx;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
            if (ny < Aquarium.min_y.floatValue() + 0.2f) {
                tmp = (Aquarium.min_y.floatValue() + 0.2f) / ny;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
            if (nz < Aquarium.min_z.floatValue() + 0.2f) {
                tmp = (Aquarium.min_z.floatValue() + 0.2f) / nz;
                nx *= tmp;
                ny *= tmp;
                nz *= tmp;
            }
        }
        baitManager.addBait(nx, ny, nz);


        if (_debug) Log.d(TAG, "end onCommand");
    }


    public synchronized void onDrawFrame(GL10 gl10) {
        setupFog(gl10);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glPushMatrix();

        // 画面をクリアする
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // モデルの位置を決める
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();

      /* 通常モード */
        CoordUtil.lookAt(gl10,
                camera[0], camera[1], camera[2] + cameraDistance,
                camera[0], camera[1], -10f,
                0, 1, 0);
        gl10.glPushMatrix();
        gl10.glRotatef(baseAngle, 0.0f, 1.0f, 0.0f);


    /*=======================================================================*/
    /* 光のセットアップ                                                      */
    /*=======================================================================*/
        setupLighting2(gl10);
        setupLighting1(gl10);

        gl10.glDisable(GL10.GL_DEPTH_TEST);

        synchronized (this) {
            for (int ii = 0; ii < shumoku_count; ii++) {
                shumoku[ii].calc();
            }
            for (int ii = 0; ii < iwashi_count; ii++) {
                iwashi[ii].calc();
            }
        }

        // 背景描画
        background.draw(gl10);
        ground.draw(gl10, iwashi);
        wave.calc();

        // model
        wave.draw(gl10);
        gl10.glEnable(GL10.GL_DEPTH_TEST);
        synchronized (this) {
            for (int ii = 0; ii < iwashi_count; ii++) {
                if (iwashi[ii] != null) {
                    iwashi[ii].draw(gl10);
                }
            }
        }
        synchronized (this) {
            for (int ii = 0; ii < shumoku_count; ii++) {
                if (ii != 0) {
                    if (shumoku[ii] != null) {
                        shumoku[ii].draw(gl10);
                    }
                } else if (ii == 0) {
                    if (shumoku[ii] != null) {
                        shumoku[ii].draw(gl10);
                    }
                }
            }
        }
        gl10.glDisable(GL10.GL_DEPTH_TEST);
        gl10.glPopMatrix();

        gl10.glPopMatrix();
    }

    public void onDestroy() {
    }
}
