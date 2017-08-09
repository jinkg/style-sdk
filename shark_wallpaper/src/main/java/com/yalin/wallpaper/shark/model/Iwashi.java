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
package com.yalin.wallpaper.shark.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.yalin.wallpaper.shark.Aquarium;
import com.yalin.wallpaper.shark.Bait;
import com.yalin.wallpaper.shark.BaitManager;
import com.yalin.wallpaper.shark.GLRenderer;
import com.yalin.wallpaper.shark.util.CoordUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;


public class Iwashi implements Model {
  private static final boolean traceBOIDS = false;
  private static final boolean debug = false;
  private static final String TAG = Iwashi.class.getName();
  private static final long BASE_TICK = 15136719L;
  private static boolean mTextureLoaded = false;
  private final FloatBuffer mTextureBuffer;  
  private final FloatBuffer mNormalBuffer;  
  private long prevTime = 0;
  private long tick = 0;
  public static float scale = 0.1035156288414f;
  public static float center_xyz[] = {-0.185271816326531f, 0.344428326530612f, -0.00509786734693878f };
  private CoordUtil coordUtil = new CoordUtil();
  private long seed = 0;
  private BaitManager baitManager;
  private boolean enableBoids = true;
  public float[] distances = new float[GLRenderer.MAX_IWASHI_COUNT];
  private Random rand = null;
  public static final float GL_IWASHI_SCALE = 0.65f;
  private float size = 10f * scale * GL_IWASHI_SCALE;
  private int iwashiCount;
  private int enemiesCount;
  private int finTick = 0;

  /** same kind list.  */
  private Model[] species;

  /** 
   * separate distance.
   * This sardine is going to be separated 
   * if distance is nearer than the appointed value.
   */
  private double separate_dist  = 7.0d * scale * (double)GL_IWASHI_SCALE;
  private double alignment_dist = 30.0d * scale * (double)GL_IWASHI_SCALE;
  private double cohesion_dist  = 110.0d * scale * (double)GL_IWASHI_SCALE;

  private float[] schoolCenter = {0f,0f,0f};
  private float[] schoolDir = {0f,0f,0f};
  private int schoolCount = 0;
  private int alignmentCount = 0;


  private final static int SHUMOKU_WEIGHT = 20;

  /*
   * Enemy
   */
  private Model[] enemies;

  private enum STATUS {
    TO_CENTER,
    TO_BAIT,
    ESCAPE,
    NORMAL,
  };

  /** current status */
  private STATUS status = STATUS.NORMAL;


  private int[] mScratch128i = new int[128];
  private float[] mScratch4f = new float[4];
  private float[] mScratch4f_1 = new float[4];
  private float[] mScratch4f_2 = new float[4];
  private Model[] mScratch4Iwashi = new Model[4];


  private float[] nextDirection = {0f,0f,0f};
  private float nextSpeed = 0.0f;
  private int nextSpeedCount = 0;
  private int nextDirectionCount = 0;

  /** Weight of separation process */
  private static final int SEPARATION_WEIGHT = 10;
  /** Weight of cohesion process */
  private static final int COHESION_WEIGHT = 1;


  /*=========================================================================*/
  /* current position of sardine                                             */
  /*=========================================================================*/
  private float[] position = { 0.0f, 1.0f, 0.0f };
  /*=========================================================================*/
  /* Direction of sardine                                                    */
  /*=========================================================================*/
  private float[] direction = { -1.0f, 0.0f, 0.0f};

  /*=========================================================================*/
  /* up or down                                                              */
  /*=========================================================================*/
  private float x_angle = 0;

  /*=========================================================================*/
  /* left or right                                                           */
  /*=========================================================================*/
  private float y_angle = 0;
  /*=========================================================================*/
  /* Speed of sardine                                                        */
  /*=========================================================================*/
  public static final float DEFAULT_SPEED = 0.03456f;
  private float speed = DEFAULT_SPEED * 0.5f;
  private float speed_unit = DEFAULT_SPEED / 5f * 0.5f;
  private float speed_max = DEFAULT_SPEED * 3f * 0.5f;
  private float speed_min = speed_unit;
  private float cohesion_speed = speed * 5f;
  private float sv_speed = speed;

  private int iwashiNo = 0;

  public Iwashi(int no) {

    ByteBuffer nbb = ByteBuffer.allocateDirect(IwashiData.normals.length * 4);
    nbb.order(ByteOrder.nativeOrder());
    mNormalBuffer = nbb.asFloatBuffer();
    mNormalBuffer.put(IwashiData.normals);
    mNormalBuffer.position(0);

    ByteBuffer tbb = ByteBuffer.allocateDirect(IwashiData.texCoords.length * 4);
    tbb.order(ByteOrder.nativeOrder());
    mTextureBuffer = tbb.asFloatBuffer();
    mTextureBuffer.put(IwashiData.texCoords);
    mTextureBuffer.position(0);

    /*=======================================================================*/
    /* calculate initial position of sardine                                 */
    /*=======================================================================*/
    this.rand = new Random(System.nanoTime() + (no * 500));
    this.seed = (long)(this.rand.nextFloat() * 5000f);
    position[0] = this.rand.nextFloat() * 8f - 4f;
    position[1] = this.rand.nextFloat() * 8f - 4f;
    position[2] = this.rand.nextFloat() * 4f - 2f;

    /*=======================================================================*/
    /* calculate inital direction of sardine                                 */
    /*=======================================================================*/
    x_angle = rand.nextFloat() * 45f - 22.5f;
    y_angle = rand.nextFloat() * 360f;
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }

    /*=======================================================================*/
    /* set up sardine number.                                                */
    /* It is a number to identify any sardine                                */
    /*=======================================================================*/
    iwashiNo = no;

    finTick = (int) (this.rand.nextFloat() * 1000 + iwashiNo);
  }

  protected static int[] textureIds = null;
  public static void loadTexture(GL10 gl10, Context context, int resource) {
    textureIds = new int[1];
    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource);
    gl10.glGenTextures(1, textureIds, 0);
    gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
    gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
    gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
    bmp.recycle();
    bmp = null;
    mTextureLoaded = true;
  }
  public static void deleteTexture(GL10 gl10) {
    if (textureIds != null && gl10 != null) {
      gl10.glDeleteTextures(1, textureIds, 0);
    }
  }
  public static boolean isTextureLoaded() {
    return mTextureLoaded;
  }

  public void calc() {
    synchronized (this) {
      think();
      move();
    }
  }

  public void draw(GL10 gl10) {
    if (gl10 == null) {
      return;
    }
    gl10.glPushMatrix();


    gl10.glPushMatrix();
    {
      /*=======================================================================*/
      /* ambient                                                               */
      /*=======================================================================*/
      synchronized (mScratch4f) {
        mScratch4f[0] = 0.07f;
        mScratch4f[1] = 0.07f;
        mScratch4f[2] = 0.07f;
        mScratch4f[3] = 1.0f;
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mScratch4f, 0);
      }
      /*=======================================================================*/
      /* diffuse                                                               */
      /*=======================================================================*/
      synchronized (mScratch4f) {
        mScratch4f[0] = 0.24f;
        mScratch4f[1] = 0.24f;
        mScratch4f[2] = 0.24f;
        mScratch4f[3] = 1.0f;
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mScratch4f, 0);
      }
      /*=======================================================================*/
      /* specular                                                              */
      /*=======================================================================*/
      synchronized (mScratch4f) {
        mScratch4f[0] = 1.0f;
        mScratch4f[1] = 1.0f;
        mScratch4f[2] = 1.0f;
        mScratch4f[3] = 1.0f;
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mScratch4f, 0);
      }
      gl10.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64f);
    }
    gl10.glTranslatef(getX(),getY(),getZ());
    gl10.glScalef(GL_IWASHI_SCALE,GL_IWASHI_SCALE,GL_IWASHI_SCALE);

    gl10.glRotatef(y_angle, 0.0f, 1.0f, 0.0f);
    gl10.glRotatef(x_angle * -1f, 0.0f, 0.0f, 1.0f);

    gl10.glColor4f(1,1,1,1);
    gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, IwashiData.mVertexBuffer[Math.abs(finTick++ / 3) % IwashiData.mVertexBuffer.length]);
    gl10.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
    gl10.glEnable(GL10.GL_TEXTURE_2D);
    gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
    gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
    gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, IwashiData.iwashiNumVerts);

    gl10.glPopMatrix();
    gl10.glPopMatrix();
  }

  public void update_speed() {
    sv_speed = speed;
    if (getStatus() == STATUS.TO_BAIT || getStatus() == STATUS.ESCAPE) {
      speed = cohesion_speed;
      return;
    }
    speed = sv_speed;

    if (this.rand.nextInt(10000) <= 1000) {
      // no change
      return;
    }
    speed += ((this.rand.nextFloat() * (speed_unit * 2f) / 2f) * ((float)iwashiCount/100f));
    if (speed <= speed_min) {
      speed = speed_min;
    }
    if (speed > speed_max) {
      speed = speed_max;
    }
  }


  /**
   * This is a part of A.I.
   * I think about which direction I advance to.
   */
  public void think() {
    long nowTime = System.nanoTime();
    if (prevTime != 0) {
      tick = nowTime - prevTime;
    }
    prevTime = nowTime;
    if (  (Aquarium.min_x.floatValue() >= position[0] || Aquarium.max_x.floatValue() <= position[0])
      ||  (Aquarium.min_y.floatValue() >= position[1] || Aquarium.max_y.floatValue() <= position[1])
      ||  (Aquarium.min_z.floatValue() >= position[2] || Aquarium.max_z.floatValue() <= position[2])) {
      /*=====================================================================*/
      /* It is the processing that does not protrude from an aquarium.       */
      /*=====================================================================*/
      if (getStatus() == STATUS.TO_BAIT || getStatus() == STATUS.ESCAPE) {
        speed = sv_speed;
      }
      setStatus(STATUS.TO_CENTER);
      aimAquariumCenter();
      if (traceBOIDS && iwashiNo == 0) Log.d(TAG, "to Aquarium Center");
      update_speed();
      return;
    }

    /*=======================================================================*/
    /* If bait is given, I sit at the bait.                                  */
    /*=======================================================================*/
    Bait bait = baitManager.getBait();
    if (bait != null) {
      if (this.rand.nextInt(10000) <= 5500) {
        if (aimBait(bait)) {
          if (traceBOIDS && iwashiNo == 0) Log.d(TAG, "to Bait");
          if (getStatus() == STATUS.TO_BAIT || getStatus() == STATUS.ESCAPE) {
            speed = sv_speed;
          }
          setStatus(STATUS.TO_BAIT);
          update_speed();
          return;
        }
      }
    }

    nextDirection[0] = nextDirection[1] = nextDirection[2] = 0;
    nextDirectionCount = 0;
    nextSpeed = 0f;
    nextSpeedCount = 0;
    if (encounterEnemy()) {
      return;
    }

    if (getStatus() == STATUS.TO_BAIT || getStatus() == STATUS.ESCAPE) {
      /* reset speed */
      speed = sv_speed;
    }

    if (getEnableBoids()) {
      if (doBoids()) {
        return;
      }
    }
    if (this.rand.nextInt(10000) <= 9500) {
      if (traceBOIDS && iwashiNo == 0) Log.d(TAG, "Nop");
      // no change
      return;
    }
    setStatus(STATUS.NORMAL);
    turn();
    if (traceBOIDS && iwashiNo == 0) Log.d(TAG, "Normal");
    update_speed();
  }

  protected boolean encounterEnemy() {
    /** Enemies come! */
    boolean encounterEnemyFlag = false;
    for (int ii=0; ii < enemiesCount && ii < enemies.length; ii++) {
      if (enemies[ii] != null) {
        if (((Shumoku)enemies[ii]).crossTestSep(getX(), getY(), getZ())) {
          calcSeparation(enemies[ii], true);
          encounterEnemyFlag = true;
        }
      }
    }
    if (false == encounterEnemyFlag) {
      return false;
    }
    nextDirection[0] /= (float)nextDirectionCount;
    nextDirection[1] /= (float)nextDirectionCount;
    nextDirection[2] /= (float)nextDirectionCount;
  
    float angle_x = (float)coordUtil.convertDegreeXY((double)nextDirection[0], (double)nextDirection[1]);
    float angle_y = (float)coordUtil.convertDegreeXZ((double)nextDirection[0] * -1d, (double)nextDirection[2]);

    if (angle_x > 180f) {
      angle_x = angle_x - 360f;
    }
    if ((angle_x < 0.0f && nextDirection[1] > 0.0f) || (angle_x > 0.0f && nextDirection[1] < 0.0f)) {
      angle_x *= -1f;
    }
    if (angle_y < 0.0f) {
      angle_y = 360f + angle_y;
    }
    angle_y = angle_y % 360f;

    aimTargetDegree(angle_x, angle_y, true);
    if (debug) {
      Log.d(TAG, "now y_angle:[" + y_angle + "]");
      Log.d(TAG, "now x_angle:[" + x_angle + "]");
    }

    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        CoordUtil.normalize3fv(mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }

    setStatus(STATUS.ESCAPE);
    update_speed();
    return true;
  }

  protected boolean doBoids() {
    if (this.rand.nextInt(10000) <= 1000) {
      if (traceBOIDS && iwashiNo == 0) Log.d(TAG, "Nop");
      // pass
      return false;
    }

    if (species == null) {
      // pass
      return false;
    }

    /**
     * rule 1 Separation
     * rule 2 Alignment
     * rule 3 Cohesion
     * and escape
     */
    schoolCenter[0] = schoolCenter[1] = schoolCenter[2] = 0f;
    schoolCount = 0;
    for (int ii=0; ii<species.length && ii<iwashiCount; ii++) {
      float dist = 0f;
      if (species[ii] == null) {
        continue;
      }
      if (ii < iwashiNo) {
        dist = species[ii].getDistances()[iwashiNo];
      }
      else if (ii == iwashiNo) {
        continue;
      }
      else {
        dist = (float)Math.sqrt(
            Math.pow(getX()-species[ii].getX(), 2)
          + Math.pow(getY()-species[ii].getY(), 2)
          + Math.pow(getZ()-species[ii].getZ(), 2));
      }
      this.distances[ii] = dist;

      if (dist < separate_dist) {
        calcSeparation(species[ii]);
      }

      if (dist < alignment_dist) {
        synchronized (mScratch4f_1) {
          synchronized (mScratch4f_2) {
            mScratch4f_1[0] = getDirectionX();
            mScratch4f_1[1] = getDirectionY();
            mScratch4f_1[2] = getDirectionZ();
            mScratch4f_2[0] = species[ii].getX() - getX();
            mScratch4f_2[1] = species[ii].getY() - getY();
            mScratch4f_2[2] = species[ii].getZ() - getZ();
            float degree = CoordUtil.includedAngle(mScratch4f_1, mScratch4f_2, 3);
            if (degree <= 150f && degree >= 0f) {
              calcAlignment(species[ii]);
            }
          }
        }
      }

      if (dist < cohesion_dist) {
        schoolCenter[0] += species[ii].getX();
        schoolCenter[1] += species[ii].getY();
        schoolCenter[2] += species[ii].getZ();
        schoolCount++;
      }
    }

    if (schoolCount != 0) {
      schoolCenter[0] /= (float)schoolCount;
      schoolCenter[1] /= (float)schoolCount;
      schoolCenter[2] /= (float)schoolCount;
      calcCohesion();
    }

    if (nextDirectionCount == 0) {
      return false;
    }

    nextDirection[0] /= (float)nextDirectionCount;
    nextDirection[1] /= (float)nextDirectionCount;
    nextDirection[2] /= (float)nextDirectionCount;

    float angle_x = (float)coordUtil.convertDegreeXY((double)nextDirection[0], (double)nextDirection[1]);
    float angle_y = (float)coordUtil.convertDegreeXZ((double)nextDirection[0] * -1d, (double)nextDirection[2]);

    if (angle_x > 180f) {
      angle_x = angle_x - 360f;
    }
    if ((angle_x < 0.0f && nextDirection[1] > 0.0f) || (angle_x > 0.0f && nextDirection[1] < 0.0f)) {
      angle_x *= -1f;
    }
    if (angle_y < 0.0f) {
      angle_y = 360f + angle_y;
    }
    angle_y = angle_y % 360f;

    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "now y_angle:[" + y_angle + "]");
      Log.d(TAG, "now x_angle:[" + x_angle + "]");
    }

    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        CoordUtil.normalize3fv(mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }

    setStatus(STATUS.NORMAL);
    if (nextSpeedCount != 0) {
      nextSpeed /= (float)nextSpeedCount;
      aimTargetSpeed(nextSpeed);
    }
    else {
      update_speed();
    }

    return true;
  }


  /**
   * I do it with one's direction that I looked at from a target 
   * as a direction doing separate.
   * @param target I appoint the sardine which wants to be separated as Iwashi Object.
   */
  public void calcSeparation(Model target) {
    calcSeparation(target, false);
  }
  public void calcSeparation(Model target, boolean enemy) {
    float v_x = 0f;
    float v_y = 0f;
    float v_z = 0f;
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        mScratch4f_2[0] = getX() - target.getX();
        mScratch4f_2[1] = getY() - target.getY();
        mScratch4f_2[2] = getZ() - target.getZ();
        CoordUtil.normalize3fv(mScratch4f_2);
      }
      v_x = mScratch4f_2[0];
      v_y = mScratch4f_2[1];
      v_z = mScratch4f_2[2];
    }
    if (debug) {
      Log.d(TAG, "Direction "
       + " x:[" + v_x + "]:"
       + " y:[" + v_y + "]:"
       + " z:[" + v_z + "]:");
    }

    float angle_x = (float)coordUtil.convertDegreeXY((double)v_x, (double)v_y);
    float angle_y = (float)coordUtil.convertDegreeXZ((double)v_x * -1d, (double)v_z);
    if (angle_x > 180f) {
      angle_x = angle_x - 360f;
    }
    if ((angle_x < 0.0f && v_y > 0.0f) || (angle_x > 0.0f && v_y < 0.0f)) {
      angle_x *= -1f;
    }
    coordUtil.setMatrixRotateZ(angle_x);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(angle_y);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        CoordUtil.normalize3fv(mScratch4f_2);
        if (enemy) {
          nextDirection[0] += (mScratch4f_2[0] * SHUMOKU_WEIGHT);
          nextDirection[1] += (mScratch4f_2[1] * SHUMOKU_WEIGHT);
          nextDirection[2] += (mScratch4f_2[2] * SHUMOKU_WEIGHT);
          nextDirectionCount += SHUMOKU_WEIGHT;
        }
        else {
          nextDirection[0] += (mScratch4f_2[0] * SEPARATION_WEIGHT);
          nextDirection[1] += (mScratch4f_2[1] * SEPARATION_WEIGHT);
          nextDirection[2] += (mScratch4f_2[2] * SEPARATION_WEIGHT);
          nextDirectionCount += SEPARATION_WEIGHT;
        }
      }
    }
  }

  public void calcAlignment(Model target) {
    /* ターゲットの角度 */
    float angle_x = target.getX_angle();
    float angle_y = target.getY_angle();
    if (debug) {
      Log.d(TAG, "candidate angle_y:[" + angle_y + "]");
      Log.d(TAG, "candidate angle_x:[" + angle_x + "]");
    }

    /** calc next direction of "alignment" */
    coordUtil.setMatrixRotateZ(angle_x);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(angle_y);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        CoordUtil.normalize3fv(mScratch4f_2);
        nextDirection[0] += mScratch4f_2[0];
        nextDirection[1] += mScratch4f_2[1];
        nextDirection[2] += mScratch4f_2[2];
        nextDirectionCount++;
      }
    }

    nextSpeed += target.getSpeed();
    nextSpeedCount++;
  }

  public void calcCohesion() {
    float v_x = 0f;
    float v_y = 0f;
    float v_z = 0f;
    synchronized (mScratch4f_2) {
      /* direction from me to school center */
      mScratch4f_2[0] = schoolCenter[0] - getX();
      mScratch4f_2[1] = schoolCenter[1] - getY();
      mScratch4f_2[2] = schoolCenter[2] - getZ();
      CoordUtil.normalize3fv(mScratch4f_2);
      nextDirection[0] += (mScratch4f_2[0] * (float)COHESION_WEIGHT);
      nextDirection[1] += (mScratch4f_2[1] * (float)COHESION_WEIGHT);
      nextDirection[2] += (mScratch4f_2[2] * (float)COHESION_WEIGHT);
      nextDirectionCount+=COHESION_WEIGHT;
    }
  }

  public void turn() {
    // 方向転換
    // 45 >= x >= -45
    // 360 >= y >= 0
    // 一回の方向転換のMAX
    // 45 >= x >= -45
    // 45 >= y >= -45
    float old_angle_x = x_angle;
    float old_angle_y = y_angle;
    x_angle = old_angle_x;
    y_angle = old_angle_y;
    float newAngleX = this.rand.nextFloat() * 45f - 22.5f;
    float newAngleY = this.rand.nextFloat() * 45f - 22.5f;
    if (newAngleX + x_angle <= 45f && newAngleX + x_angle >= -45f) {
      x_angle = x_angle + newAngleX;
    } 
    else {
      if (newAngleX + x_angle >= 45f) {
        x_angle = (this.rand.nextFloat() * 45f);
      }
      else if (newAngleX + x_angle <= -45f) {
        x_angle = (this.rand.nextFloat() * -45f);
      }
    }
    y_angle = (float)((int)(y_angle + newAngleY) % 360);
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }
  }

  public void aimTargetDegree(float angle_x, float angle_y) {
    aimTargetDegree(angle_x, angle_y, false);
  }

  public void aimTargetDegree(float angle_x, float angle_y, boolean forceTurn) {
    float newAngle = this.rand.nextFloat() * 22.5f;
    float xx = angle_x - x_angle;
    if (forceTurn) {
      x_angle += xx;
    }
    else {
      if (xx < 0.0f) {
        if (xx > -22.5f) {
          x_angle += xx;
        }
        else {
          x_angle += -newAngle;
        }
      }
      else {
        if (xx < 22.5f) {
          x_angle += xx;
        }
        else {
          x_angle += newAngle;
        }
      }
    }
    if (x_angle > 45.0f) {
      x_angle = 45.0f;
    }
    if (x_angle < -45.0f) {
      x_angle = -45.0f;
    }

    float yy = angle_y - y_angle;
    if (yy > 180.0f) {
      yy = -360f + yy;
    }
    else if (yy < -180.0f) {
      yy = 360f - yy;
    }

    if (forceTurn) {
      y_angle += yy;
    }
    else {
      if (yy < 0.0f) {
        if (yy > -22.5f) {
          y_angle += yy;
        }
        else {
          y_angle += -newAngle;
        }
      }
      else {
        if (yy < 22.5f) {
          y_angle += yy;
        }
        else {
          y_angle += newAngle;
        }
      }
    }
    y_angle = y_angle % 360f;
    if (y_angle < 0f) {
      y_angle = 360f + y_angle;
    }
  }

  public void aimTargetSpeed(float t_speed) {
    if (t_speed <= speed) {
      update_speed();
    }
    else {
      speed += (this.rand.nextFloat() * speed_unit);
      if (t_speed < speed) {
        speed = t_speed - (this.rand.nextFloat() * speed_unit);
      }
      if (speed > speed_max) {
        speed = speed_max;
      }
    }
  }

  /**
   * The disposal of to the center aquarium
   */
  public void aimAquariumCenter() {
    if (debug) {
      Log.d(TAG, "start aimAquariumCenter ");
    }
    float v_x = (Aquarium.center[0] - getX());
    float v_y = (Aquarium.center[1] - getY());
    float v_z = (Aquarium.center[2] - getZ());
    if (Aquarium.min_x.floatValue() < getX() && Aquarium.max_x.floatValue() > getX()
    &&  Aquarium.min_y.floatValue() < getY() && Aquarium.max_y.floatValue() > getY()) {
      /* over or under only Z */
      v_x = 0.0f;
      v_y = 0.0f;
    }
    else 
    if (Aquarium.min_x.floatValue() < getX() && Aquarium.max_x.floatValue() > getX()
    &&  Aquarium.min_z.floatValue() < getZ() && Aquarium.max_z.floatValue() > getZ()) {
      /* over or under only Y */
      v_x = 0.0f;
      v_z = 0.0f;
    }
    else 
    if (Aquarium.min_y.floatValue() < getY() && Aquarium.max_y.floatValue() > getY()
    &&  Aquarium.min_z.floatValue() < getZ() && Aquarium.max_z.floatValue() > getZ()) {
      /* over or under only X */
      v_y = 0.0f;
      v_z = 0.0f;
    }
    float angle_x = (float)coordUtil.convertDegreeXY((double)v_x, (double)v_y);
    float angle_y = (float)coordUtil.convertDegreeXZ((double)v_x * -1d, (double)v_z);
    if (angle_x > 180f) {
      angle_x = angle_x - 360f;
    }
    if ((angle_x < 0.0f && v_y > 0.0f) || (angle_x > 0.0f && v_y < 0.0f)) {
      angle_x *= -1f;
    }
    if (debug) {
      Log.d(TAG, "candidate angle_y:[" + angle_y + "]");
      Log.d(TAG, "candidate angle_x:[" + angle_x + "]");
    }

    if (angle_y < 0.0f) {
      angle_y = 360f + angle_y;
    }
    angle_y = angle_y % 360f;

    /* aim degree of target  */
    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "now y_angle:[" + y_angle + "]");
      Log.d(TAG, "now x_angle:[" + x_angle + "]");
    }

    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }
    if (debug) {
      Log.d(TAG, "end aimAquariumCenter "
        + "x:[" + direction[0] + "]:"
        + "y:[" + direction[1] + "]:"
        + "z:[" + direction[2] + "]:");
    }
  }
  public boolean aimBait(Bait bait) {
    if (debug) {
      Log.d(TAG, "start aimBait ");
    }
    double dist = Math.sqrt(
        Math.pow(position[0]-bait.getX(), 2)
      + Math.pow(position[1]-bait.getY(), 2)
      + Math.pow(position[2]-bait.getZ(), 2));
    if (dist <= separate_dist) {
      baitManager.eat(bait);
      return false;
    }
    float v_x = (bait.getX() - getX());
    float v_y = (bait.getY() - getY());
    float v_z = (bait.getZ() - getZ());
    if (debug) {
      Log.d(TAG, "candidate direction"
       + " x:[" + v_x + "]:"
       + " y:[" + v_y + "]:"
       + " z:[" + v_z + "]:");
    }

    /* 上下角度算出 (-1dを乗算しているのは0度の向きが違うため) */
    float angle_x = (float)coordUtil.convertDegreeXY((double)v_x, (double)v_y);
    /* 左右角度算出 (-1dを乗算しているのは0度の向きが違うため) */
    float angle_y = (float)coordUtil.convertDegreeXZ((double)v_x * -1d, (double)v_z);
    if (angle_x > 180f) {
      angle_x = angle_x - 360f;
    }
    if ((angle_x < 0.0f && v_y > 0.0f) || (angle_x > 0.0f && v_y < 0.0f)) {
      angle_x *= -1f;
    }
    if (debug) {
      Log.d(TAG, "candidate angle_y:[" + angle_y + "]");
      Log.d(TAG, "candidate angle_x:[" + angle_x + "]");
    }

    /* その角度へ近づける */
    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "実際に向かう方向のy_angle:[" + y_angle + "]");
      Log.d(TAG, "実際に向かう方向のx_angle:[" + x_angle + "]");
    }

    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine(-1.0f,0.0f, 0.0f, mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        direction[0] = mScratch4f_2[0];
        direction[1] = mScratch4f_2[1];
        direction[2] = mScratch4f_2[2];
      }
    }
    if (debug) {
      Log.d(TAG, "end aimBait "
        + "x:[" + direction[0] + "]:"
        + "y:[" + direction[1] + "]:"
        + "z:[" + direction[2] + "]:");
    }
    return true;
  }
  public void move() {
    /*=======================================================================*/
    /* 処理速度を考慮した増分                                                */
    /*=======================================================================*/
    float moveWidth = getSpeed() * (float)(tick / BASE_TICK);

    if (getX() + getDirectionX() * moveWidth >= Aquarium.max_x) {
      setX(Aquarium.max_x);
    }
    else if (getX() + getDirectionX() * moveWidth <= Aquarium.min_x) {
      setX(Aquarium.min_x);
    }
    else {
      setX(getX() + getDirectionX() * moveWidth);
    }
    if (getY() + getDirectionY() * moveWidth >= Aquarium.max_y) {
      setY(Aquarium.max_y);
    }
    else if (getY() + getDirectionY() * moveWidth <= Aquarium.min_y) {
      setY(Aquarium.min_y);
    }
    else {
      setY(getY() + getDirectionY() * moveWidth);
    }
    if (getZ() + getDirectionZ() * moveWidth >= Aquarium.max_z) {
      setZ(Aquarium.max_z);
    }
    else if (getZ() + getDirectionZ() * moveWidth <= Aquarium.min_z) {
      setZ(Aquarium.min_z);
    }
    else {
      setZ(getZ() + getDirectionZ() * moveWidth);
    }
    if (debug) {
      Log.d(TAG, "end move "
        + "dx:[" + getDirectionX() + "]:"
        + "dy:[" + getDirectionY() + "]:"
        + "dz:[" + getDirectionZ() + "]:"
        + "speed:[" + getSpeed() + "]:"
        + "x:[" + getX() + "]:"
        + "y:[" + getY() + "]:"
        + "z:[" + getZ() + "]:"
        + "x_angle:[" + x_angle + "]:"
        + "y_angle:[" + y_angle + "]:"
        );
    }
  }


  public float[] getPosition() {
    return position;
  }
  public void setPosition(float[] pos) {
    this.position = pos;
  }
  
  public float getX() {
    return position[0];
  }
  
  public void setX(float x) {
    this.position[0] = x;
  }
  
  public float getY() {
    return position[1];
  }
  
  public void setY(float y) {
    this.position[1] = y;
  }
  
  public float getZ() {
    return position[2];
  }
  
  public void setZ(float z) {
    this.position[2] = z;
  }

  public float getDirectionX() {
    return direction[0];
  }
  public float getDirectionY() {
    return direction[1];
  }
  public float getDirectionZ() {
    return direction[2];
  }
  public void setDirectionX(float x) {
    this.direction[0] = x;
  }
  public void setDirectionY(float y) {
    this.direction[1] = y;
  }
  public void setDirectionZ(float z) {
    this.direction[2] = z;
  }
  
  public float getSpeed() {
    return speed;
  }
  
  public void setSpeed(float speed) {
    this.speed = speed * 0.5f;
    this.speed_unit = speed / 5f * 0.5f;
    this.speed_max = speed * 3f * 0.5f;
    this.speed_min = this.speed_unit * 2f;
    this.cohesion_speed = speed * 5f * 0.5f;
    this.sv_speed = speed;
  }
  
  public float[] getDirection() {
    return direction;
  }
  
  public float getDirection(int index) {
    return direction[index];
  }
  
  public void setDirection(float[] direction) {
    this.direction = direction;
  }
  
  public void setDirection(float direction, int index) {
    this.direction[index] = direction;
  }
  
  /**
   * Get species.
   *
   * @return species as Iwashi[].
   */
  public Model[] getSpecies() {
    return species;
  }
  
  /**
   * Get species element at specified index.
   *
   * @param index the index.
   * @return species at index as Iwashi.
   */
  public Model getSpecies(int index) {
    return species[index];
  }
  
  /**
   * Set species.
   *
   * @param species the value to set.
   */
  public void setSpecies(Model[] species) {
    this.species = species;
    for (int ii=0; ii<species.length; ii++) {
      this.distances[ii] = 10000f;
    }
  }
  
  /**
   * Set species at the specified index.
   *
   * @param species the value to set.
   * @param index the index.
   */
  public void setSpecies(Model species, int index) {
    this.species[index] = species;
  }
  
  /**
   * Get x_angle.
   *
   * @return x_angle as float.
   */
  public float getX_angle()
  {
      return x_angle;
  }
  
  /**
   * Set x_angle.
   *
   * @param x_angle the value to set.
   */
  public void setX_angle(float x_angle)
  {
      this.x_angle = x_angle;
  }
  
  /**
   * Get y_angle.
   *
   * @return y_angle as float.
   */
  public float getY_angle()
  {
      return y_angle;
  }
  
  /**
   * Set y_angle.
   *
   * @param y_angle the value to set.
   */
  public void setY_angle(float y_angle)
  {
      this.y_angle = y_angle;
  }
  
  /**
   * Get schoolCenter.
   *
   * @return schoolCenter as float[].
   */
  public float[] getSchoolCenter()
  {
      return schoolCenter;
  }
  
  /**
   * Get schoolCenter element at specified index.
   *
   * @param index the index.
   * @return schoolCenter at index as float.
   */
  public float getSchoolCenter(int index)
  {
      return schoolCenter[index];
  }
  
  /**
   * Set schoolCenter.
   *
   * @param schoolCenter the value to set.
   */
  public void setSchoolCenter(float[] schoolCenter) {
      this.schoolCenter = schoolCenter;
  }
  
  /**
   * Set schoolCenter at the specified index.
   *
   * @param schoolCenter the value to set.
   * @param index the index.
   */
  public void setSchoolCenter(float schoolCenter, int index)
  {
      this.schoolCenter[index] = schoolCenter;
  }
  
  /**
   * Get baitManager.
   *
   * @return baitManager as BaitManager.
   */
  public BaitManager getBaitManager()
  {
      return baitManager;
  }
  
  /**
   * Set baitManager.
   *
   * @param baitManager the value to set.
   */
  public void setBaitManager(BaitManager baitManager)
  {
      this.baitManager = baitManager;
  }
  
  
  /**
   * Get enableBoids.
   *
   * @return enableBoids as boolean.
   */
  public boolean getEnableBoids()
  {
      return enableBoids;
  }
  
  /**
   * Set enableBoids.
   *
   * @param enableBoids the value to set.
   */
  public void setEnableBoids(boolean enableBoids)
  {
      this.enableBoids = enableBoids;
  }
  
  /**
   * Get status.
   *
   * @return status as STATUS.
   */
  public STATUS getStatus() {
    return status;
  }
  
  /**
   * Set status.
   *
   * @param status the value to set.
   */
  public void setStatus(STATUS status) {
    this.status = status;
  }
  
  /**
   * Get size.
   *
   * @return size as float.
   */
  public float getSize()
  {
      return size;
  }
  
  /**
   * Set size.
   *
   * @param size the value to set.
   */
  public void setSize(float size)
  {
      this.size = size;
  }
  
  /**
   * Get iwashiCount.
   *
   * @return iwashiCount as int.
   */
  public int getIwashiCount()
  {
      return iwashiCount;
  }
  
  /**
   * Set iwashiCount.
   *
   * @param iwashiCount the value to set.
   */
  public void setIwashiCount(int iwashiCount)
  {
      this.iwashiCount = iwashiCount;
  }
  
  /**
   * Get distances.
   *
   * @return distances as float[].
   */
  public float[] getDistances()
  {
      return distances;
  }
  
  /**
   * Get distances element at specified index.
   *
   * @param index the index.
   * @return distances at index as float.
   */
  public float getDistances(int index)
  {
      return distances[index];
  }
  
  /**
   * Set distances.
   *
   * @param distances the value to set.
   */
  public void setDistances(float[] distances)
  {
      this.distances = distances;
  }
  
  /**
   * Set distances at the specified index.
   *
   * @param distances the value to set.
   * @param index the index.
   */
  public void setDistances(float distances, int index)
  {
      this.distances[index] = distances;
  }
  
  /**
   * Get enemies.
   *
   * @return enemies as ModeL[].
   */
  public Model[] getEnemies()
  {
      return enemies;
  }
  
  /**
   * Get enemies element at specified index.
   *
   * @param index the index.
   * @return enemies at index as Model.
   */
  public Model getEnemies(int index)
  {
      return enemies[index];
  }
  
  /**
   * Set enemies.
   *
   * @param enemies the value to set.
   */
  public void setEnemies(Model[] enemies)
  {
      this.enemies = enemies;
  }
  
  /**
   * Set enemies at the specified index.
   *
   * @param enemies the value to set.
   * @param index the index.
   */
  public void setEnemies(Model enemies, int index)
  {
      this.enemies[index] = enemies;
  }
  
  /**
   * Get enemiesCount.
   *
   * @return enemiesCount as int.
   */
  public int getEnemiesCount()
  {
      return enemiesCount;
  }
  
  /**
   * Set enemiesCount.
   *
   * @param enemiesCount the value to set.
   */
  public void setEnemiesCount(int enemiesCount)
  {
      this.enemiesCount = enemiesCount;
  }
}
