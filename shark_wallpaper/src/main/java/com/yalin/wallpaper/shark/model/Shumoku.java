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

public class Shumoku implements Model {
  private static final boolean traceBOIDS = false;
  private static final boolean debug = false;
  private static final String TAG = Shumoku.class.getName();
  private static final long BASE_TICK = 17852783L;
  private static boolean mTextureLoaded = false;
  private final FloatBuffer mVertexBuffer;
  private final FloatBuffer mTextureBuffer;  
  private final FloatBuffer mNormalBuffer;  
  private long prevTime = 0;
  private long tick = 0;
  private static final float scale = 0.119717280459159f;
  private float center_xyz[] = {0.944553637254902f, -0.0858584215686275f, 0.00370374509803921f};
  private CoordUtil coordUtil = new CoordUtil();
  private long seed = 0;
  private BaitManager baitManager;
  private boolean enableBoids = true;
  public float[] distances = new float[GLRenderer.MAX_IWASHI_COUNT + 1];
  private Random rand = null;
  public static final float GL_SHUMOKU_SCALE = 4f;
  private float size = 10f * scale * GL_SHUMOKU_SCALE;
  private int shumokuCount;
  private static final float MAX_X_ANGLE = 3f;
  private static final float GAP_WIDTH = 0.05f;
  private static final float MAX_GAP_WIDTH = 0.25f;

  /*
   * The same kind list
   */
  private Model[] species;

  public static final double separate_dist  = 10.0d * scale * (double)GL_SHUMOKU_SCALE;
  private static double[] separate_dist_xyz = { 
                                    5.404d * scale * (double)GL_SHUMOKU_SCALE,
                                    0.734d * scale * (double)GL_SHUMOKU_SCALE, 
                                    0.347d * scale * (double)GL_SHUMOKU_SCALE,
                                  };
  public static double[] aabb_org = {
    -separate_dist_xyz[0], -separate_dist_xyz[1], -separate_dist_xyz[2],
    separate_dist_xyz[0], separate_dist_xyz[1], separate_dist_xyz[2],
  };
  public static double[] sep_aabb = {
    0d,0d,0d,
    0d,0d,0d,
  };
  public static double[] al1_aabb = {
    0d,0d,0d,
    0d,0d,0d,
  };
  public static double[] al2_aabb = {
    0d,0d,0d,
    0d,0d,0d,
  };
  public static double[] sch_aabb = {
    0d,0d,0d,
    0d,0d,0d,
  };
  public static double[] coh_aabb = {
    0d,0d,0d,
    0d,0d,0d,
  };
  public static final double alignment_dist1= 15.0d * scale * (double)Iwashi.GL_IWASHI_SCALE;
  public static final double alignment_dist2= 35.0d * scale * (double)Iwashi.GL_IWASHI_SCALE;
  public static final double school_dist    = 70.0d * scale * (double)Iwashi.GL_IWASHI_SCALE;
  public static final double cohesion_dist  = 110.0d * scale * (double)Iwashi.GL_IWASHI_SCALE;
  private float[] schoolCenter = {0f,0f,0f};
  private float[] schoolDir = {0f,0f,0f};

  private enum STATUS {
    TO_CENTER, /* 画面の真ん中へ向かい中 */
    TO_BAIT,   /* 餌へ向かっている最中   */
    SEPARATE,  /* 近づき過ぎたので離れる */
    ALIGNMENT, /* 整列中 */
    COHESION,  /* 近づく */
    TO_SCHOOL_CENTER,   /* 群れの真ん中へ */
    NORMAL,    /* ランダム */
  };

  /** 現在の行動中の行動 */
  private STATUS status = STATUS.NORMAL;

  private enum TURN_DIRECTION {
    TURN_RIGHT, /* 右に曲がり中 */
    STRAIGHT,   /* まっすぐ */
    TURN_LEFT,  /* 左に曲がり中 */
  };

  /** 現在曲がろうとしているかどうか */
  private TURN_DIRECTION turnDirection = TURN_DIRECTION.STRAIGHT;


  private int[] mScratch128i = new int[128];
  private float[] mScratch4f = new float[4];
  private float[] mScratch4f_1 = new float[4];
  private float[] mScratch4f_2 = new float[4];
  private Model[] mScratch3Shumoku = new Shumoku[4];

  private float animationGap = 0.0f;


  /*=========================================================================*/
  /* 現在位置                                                                */
  /*=========================================================================*/
  private float[] position = { 0.0f, 1.0f, 0.0f };
  /*=========================================================================*/
  /* 向き                                                                    */
  /*=========================================================================*/
  private float[] direction = { -1.0f, 0.0f, 0.0f};

  /* 上下 */
  private float x_angle = 0;
  /* 左右 */
  private float y_angle = 0;

  /* angle for animation */
  private float angleForAnimation = 0f;
  /*=========================================================================*/
  /* スピード                                                                */
  /*=========================================================================*/
  public static final float DEFAULT_SPEED = 0.03456f;
  private float speed = DEFAULT_SPEED * 0.5f;
  private float speed_unit = DEFAULT_SPEED / 5f * 0.5f;
  private float speed_max = DEFAULT_SPEED * 3f * 0.5f;
  private float speed_min = speed_unit;
  private float cohesion_speed = speed * 5f * 0.5f;
  private float sv_speed = speed;

  private int shumokuNo = 0;

  public Shumoku(int ii) {

    ByteBuffer nbb = ByteBuffer.allocateDirect(ShumokuData.normals.length * 4);
    nbb.order(ByteOrder.nativeOrder());
    mNormalBuffer = nbb.asFloatBuffer();
    mNormalBuffer.put(ShumokuData.normals);
    mNormalBuffer.position(0);

    ByteBuffer tbb = ByteBuffer.allocateDirect(ShumokuData.texCoords.length * 4);
    tbb.order(ByteOrder.nativeOrder());
    mTextureBuffer = tbb.asFloatBuffer();
    mTextureBuffer.put(ShumokuData.texCoords);
    mTextureBuffer.position(0);

    ByteBuffer vbb = ByteBuffer.allocateDirect(ShumokuData.vertices.length * 4);
    vbb.order(ByteOrder.nativeOrder());
    mVertexBuffer = vbb.asFloatBuffer();

    // 初期配置
    this.rand = new Random(System.nanoTime() + (ii * 500));
    this.seed = (long)(this.rand.nextFloat() * 5000f);
    position[0] = this.rand.nextFloat() * 8f - 4f;
    position[1] = this.rand.nextFloat() * 8f - 4f;
    position[2] = this.rand.nextFloat() * 4f - 2f;

    /*=======================================================================*/
    /* Sets the initial direction of hammerhead shark                        */
    /*=======================================================================*/
    x_angle = rand.nextFloat() * (MAX_X_ANGLE * 2f) - MAX_X_ANGLE;
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
    // 鰯番号セット
    shumokuNo = ii;
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
    if (textureIds != null) {
      gl10.glDeleteTextures(1, textureIds, 0);
    }
  }
  public static boolean isTextureLoaded() {
    return mTextureLoaded;
  }

  private float getMoveWidth(float x) {
    /*=======================================================================*/
    /* z = 1/3 * x^2 の2次関数から算出                                       */
    /*=======================================================================*/
    float xt = x / scale + center_xyz[0];
    return xt * xt / 20.0f;
  }


  private void animate() {
    long current = System.currentTimeMillis() + this.seed;
    float nf = (float)((current / 100) % 10000);
    float s = (float)Math.sin((double)nf/2f);
    if (getTurnDirection() == TURN_DIRECTION.TURN_LEFT) {
      animationGap -= GAP_WIDTH;
      if (animationGap < -MAX_GAP_WIDTH) {
        animationGap = -MAX_GAP_WIDTH;
      }
    }
    else if (getTurnDirection() == TURN_DIRECTION.TURN_RIGHT) {
      animationGap += GAP_WIDTH;
      if (animationGap > MAX_GAP_WIDTH) {
        animationGap = MAX_GAP_WIDTH;
      }
    }
    else {
      if (animationGap > 0.0f) {
        animationGap -= GAP_WIDTH;
        if (animationGap < 0.0f) {
          animationGap = 0.0f;
        }
      }
      else if (animationGap < 0.0f) {
        animationGap += GAP_WIDTH;
        if (animationGap > 0.0f) {
          animationGap = 0.0f;
        }
      }
    }
    s *= scale;
    angleForAnimation = 3.0625f * (float)Math.cos((double)nf/2f) * -1f;

    generated_animate(s);


    mVertexBuffer.position(0);
    mVertexBuffer.put(ShumokuData.vertices);
    mVertexBuffer.position(0);
  }

  private void generated_animate(float s) {
    /* **DONT EDIT FOLLOWING LINE** */
    /* Generate by perl script */
    //312 104 {-2.398415, -0.560331, 0.007942}
    //321 107 {-2.398415, -0.560331, 0.007942}
    //330 110 {-2.398415, -0.560331, 0.007942}
    //339 113 {-2.398415, -0.560331, 0.007942}
    //582 194 {-2.398415, -0.560331, 0.007942}
    //591 197 {-2.398415, -0.560331, 0.007942}
    //1209 403 {-2.398415, -0.560331, 0.007942}
    //1224 408 {-2.398415, -0.560331, 0.007942}
    synchronized (mScratch128i) {
      mScratch128i[0] = 104;
      mScratch128i[1] = 107;
      mScratch128i[2] = 110;
      mScratch128i[3] = 113;
      mScratch128i[4] = 194;
      mScratch128i[5] = 197;
      mScratch128i[6] = 403;
      mScratch128i[7] = 408;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //318 106 {-2.253708, -0.463222, 0.012815}
    //324 108 {-2.253708, -0.463222, 0.012815}
    //1338 446 {-2.253708, -0.463222, 0.012815}
    //1347 449 {-2.253708, -0.463222, 0.012815}
    //1356 452 {-2.253708, -0.463222, 0.012815}
    //1362 454 {-2.253708, -0.463222, 0.012815}
    synchronized (mScratch128i) {
      mScratch128i[0] = 106;
      mScratch128i[1] = 108;
      mScratch128i[2] = 446;
      mScratch128i[3] = 449;
      mScratch128i[4] = 452;
      mScratch128i[5] = 454;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<6; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //240 080 {-2.149198, -0.630022, 0.012816}
    //249 083 {-2.149198, -0.630022, 0.012816}
    //255 085 {-2.149198, -0.569430, 0.213486}
    //258 086 {-2.149198, -0.630022, 0.012816}
    //282 094 {-2.149198, -0.630022, 0.012816}
    //285 095 {-2.149198, -0.569431, -0.187855}
    //294 098 {-2.187970, -0.539861, 0.608049}
    //303 101 {-2.187970, -0.539861, -0.575508}
    //306 102 {-2.149198, -0.630022, 0.012816}
    //309 103 {-2.149198, -0.569430, 0.213486}
    //315 105 {-2.187970, -0.539861, 0.608049}
    //327 109 {-2.187970, -0.539861, -0.575508}
    //333 111 {-2.149198, -0.569431, -0.187855}
    //336 112 {-2.149198, -0.630022, 0.012816}
    //576 192 {-2.187970, -0.539861, -0.575508}
    //579 193 {-2.149198, -0.569431, -0.187855}
    //585 195 {-2.149198, -0.569430, 0.213486}
    //588 196 {-2.187970, -0.539861, 0.608049}
    //597 199 {-2.149198, -0.569431, -0.187855}
    //600 200 {-2.187970, -0.539861, -0.575508}
    //603 201 {-2.149198, -0.569430, 0.213486}
    //609 203 {-2.187970, -0.539861, 0.608049}
    //618 206 {-2.149198, -0.569431, -0.187855}
    //624 208 {-2.149198, -0.569431, -0.187855}
    //630 210 {-2.149198, -0.569431, -0.187855}
    //633 211 {-2.187970, -0.539861, -0.575508}
    //639 213 {-2.187970, -0.539861, 0.608049}
    //642 214 {-2.149198, -0.569430, 0.213486}
    //648 216 {-2.149198, -0.569431, -0.187855}
    //657 219 {-2.149198, -0.569431, -0.187855}
    //669 223 {-2.187970, -0.539861, -0.575508}
    //675 225 {-2.187970, -0.539861, 0.608049}
    //684 228 {-2.149198, -0.569430, 0.213486}
    //693 231 {-2.149198, -0.569430, 0.213486}
    //702 234 {-2.149198, -0.569430, 0.213486}
    //1137 379 {-2.149198, -0.569430, 0.213486}
    //1155 385 {-2.149198, -0.569430, 0.213486}
    //1173 391 {-2.149198, -0.569431, -0.187855}
    //1188 396 {-2.149198, -0.569431, -0.187855}
    //1206 402 {-2.149198, -0.569430, 0.213486}
    //1212 404 {-2.187970, -0.539861, 0.608049}
    //1215 405 {-2.149198, -0.569430, 0.213486}
    //1218 406 {-2.187970, -0.539861, 0.608049}
    //1227 409 {-2.149198, -0.569431, -0.187855}
    //1230 410 {-2.187970, -0.539861, -0.575508}
    //1233 411 {-2.149198, -0.569431, -0.187855}
    //1239 413 {-2.187970, -0.539861, -0.575508}
    //1248 416 {-2.149198, -0.569430, 0.213486}
    //1257 419 {-2.149198, -0.569430, 0.213486}
    //1344 448 {-2.187970, -0.539861, -0.575508}
    //1365 455 {-2.187970, -0.539861, 0.608049}
    synchronized (mScratch128i) {
      mScratch128i[0] = 80;
      mScratch128i[1] = 83;
      mScratch128i[2] = 85;
      mScratch128i[3] = 86;
      mScratch128i[4] = 94;
      mScratch128i[5] = 95;
      mScratch128i[6] = 98;
      mScratch128i[7] = 101;
      mScratch128i[8] = 102;
      mScratch128i[9] = 103;
      mScratch128i[10] = 105;
      mScratch128i[11] = 109;
      mScratch128i[12] = 111;
      mScratch128i[13] = 112;
      mScratch128i[14] = 192;
      mScratch128i[15] = 193;
      mScratch128i[16] = 195;
      mScratch128i[17] = 196;
      mScratch128i[18] = 199;
      mScratch128i[19] = 200;
      mScratch128i[20] = 201;
      mScratch128i[21] = 203;
      mScratch128i[22] = 206;
      mScratch128i[23] = 208;
      mScratch128i[24] = 210;
      mScratch128i[25] = 211;
      mScratch128i[26] = 213;
      mScratch128i[27] = 214;
      mScratch128i[28] = 216;
      mScratch128i[29] = 219;
      mScratch128i[30] = 223;
      mScratch128i[31] = 225;
      mScratch128i[32] = 228;
      mScratch128i[33] = 231;
      mScratch128i[34] = 234;
      mScratch128i[35] = 379;
      mScratch128i[36] = 385;
      mScratch128i[37] = 391;
      mScratch128i[38] = 396;
      mScratch128i[39] = 402;
      mScratch128i[40] = 404;
      mScratch128i[41] = 405;
      mScratch128i[42] = 406;
      mScratch128i[43] = 409;
      mScratch128i[44] = 410;
      mScratch128i[45] = 411;
      mScratch128i[46] = 413;
      mScratch128i[47] = 416;
      mScratch128i[48] = 419;
      mScratch128i[49] = 448;
      mScratch128i[50] = 455;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<51; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //636 212 {-2.098644, -0.568532, -0.972742}
    //645 215 {-2.098644, -0.568531, 1.012195}
    //654 218 {-2.098644, -0.568532, -0.972742}
    //660 220 {-2.098644, -0.568532, -0.972742}
    //672 224 {-2.098644, -0.568532, -0.972742}
    //681 227 {-2.098644, -0.568531, 1.012195}
    //687 229 {-2.098644, -0.568531, 1.012195}
    //699 233 {-2.098644, -0.568531, 1.012195}
    //705 235 {-2.098644, -0.568531, 1.012195}
    //819 273 {-2.098644, -0.568532, -0.972742}
    //837 279 {-2.098644, -0.568531, 1.012195}
    //1221 407 {-2.098644, -0.568531, 1.012195}
    //1236 412 {-2.098644, -0.568532, -0.972742}
    synchronized (mScratch128i) {
      mScratch128i[0] = 212;
      mScratch128i[1] = 215;
      mScratch128i[2] = 218;
      mScratch128i[3] = 220;
      mScratch128i[4] = 224;
      mScratch128i[5] = 227;
      mScratch128i[6] = 229;
      mScratch128i[7] = 233;
      mScratch128i[8] = 235;
      mScratch128i[9] = 273;
      mScratch128i[10] = 279;
      mScratch128i[11] = 407;
      mScratch128i[12] = 412;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<13; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //267 089 {-1.979557, -0.436034, 0.538350}
    //273 091 {-1.979557, -0.436034, -0.512718}
    //291 097 {-1.979557, -0.436034, 0.538350}
    //297 099 {-1.979557, -0.436034, -0.512718}
    //1335 445 {-1.979557, -0.436034, -0.512718}
    //1341 447 {-1.979557, -0.436034, -0.512718}
    //1350 450 {-1.979557, -0.436034, 0.538350}
    //1359 453 {-1.979557, -0.436034, 0.538350}
    //1374 458 {-1.979557, -0.436034, -0.512718}
    //1383 461 {-1.979557, -0.436034, -0.512718}
    //1392 464 {-1.979557, -0.436034, 0.538350}
    //1398 466 {-1.979557, -0.436034, 0.538350}
    synchronized (mScratch128i) {
      mScratch128i[0] = 89;
      mScratch128i[1] = 91;
      mScratch128i[2] = 97;
      mScratch128i[3] = 99;
      mScratch128i[4] = 445;
      mScratch128i[5] = 447;
      mScratch128i[6] = 450;
      mScratch128i[7] = 453;
      mScratch128i[8] = 458;
      mScratch128i[9] = 461;
      mScratch128i[10] = 464;
      mScratch128i[11] = 466;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<12; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //288 096 {-1.850063, -0.417212, 0.977750}
    //300 100 {-1.847628, -0.416809, -0.939963}
    //594 198 {-1.847628, -0.416809, -0.939963}
    //606 202 {-1.850063, -0.417212, 0.977750}
    //627 209 {-1.847628, -0.416809, -0.939963}
    //651 217 {-1.847724, -0.626668, -0.882202}
    //663 221 {-1.847628, -0.416809, -0.939963}
    //666 222 {-1.847628, -0.416809, -0.939963}
    //678 226 {-1.850063, -0.417212, 0.977750}
    //690 230 {-1.850063, -0.417212, 0.977750}
    //696 232 {-1.847724, -0.424539, 0.977214}
    //708 236 {-1.847724, -0.626667, 0.921660}
    //723 241 {-1.847628, -0.416809, -0.939963}
    //738 246 {-1.850063, -0.417212, 0.977750}
    //822 274 {-1.847724, -0.626668, -0.882202}
    //825 275 {-1.847628, -0.416809, -0.939963}
    //831 277 {-1.847628, -0.416809, -0.939963}
    //834 278 {-1.847724, -0.626668, -0.882202}
    //840 280 {-1.847724, -0.424539, 0.977214}
    //843 281 {-1.847724, -0.626667, 0.921660}
    //849 283 {-1.847724, -0.626667, 0.921660}
    //852 284 {-1.850063, -0.417212, 0.977750}
    //1140 380 {-1.847724, -0.626667, 0.921660}
    //1146 382 {-1.847724, -0.626667, 0.921660}
    //1158 386 {-1.850063, -0.417212, 0.977750}
    //1164 388 {-1.850063, -0.417212, 0.977750}
    //1176 392 {-1.847628, -0.416809, -0.939963}
    //1182 394 {-1.847628, -0.416809, -0.939963}
    //1194 398 {-1.847724, -0.626668, -0.882202}
    //1203 401 {-1.847724, -0.626668, -0.882202}
    //1254 418 {-1.850063, -0.417212, 0.977750}
    //1380 460 {-1.847628, -0.416809, -0.939963}
    //1401 467 {-1.850063, -0.417212, 0.977750}
    synchronized (mScratch128i) {
      mScratch128i[0] = 96;
      mScratch128i[1] = 100;
      mScratch128i[2] = 198;
      mScratch128i[3] = 202;
      mScratch128i[4] = 209;
      mScratch128i[5] = 217;
      mScratch128i[6] = 221;
      mScratch128i[7] = 222;
      mScratch128i[8] = 226;
      mScratch128i[9] = 230;
      mScratch128i[10] = 232;
      mScratch128i[11] = 236;
      mScratch128i[12] = 241;
      mScratch128i[13] = 246;
      mScratch128i[14] = 274;
      mScratch128i[15] = 275;
      mScratch128i[16] = 277;
      mScratch128i[17] = 278;
      mScratch128i[18] = 280;
      mScratch128i[19] = 281;
      mScratch128i[20] = 283;
      mScratch128i[21] = 284;
      mScratch128i[22] = 380;
      mScratch128i[23] = 382;
      mScratch128i[24] = 386;
      mScratch128i[25] = 388;
      mScratch128i[26] = 392;
      mScratch128i[27] = 394;
      mScratch128i[28] = 398;
      mScratch128i[29] = 401;
      mScratch128i[30] = 418;
      mScratch128i[31] = 460;
      mScratch128i[32] = 467;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<33; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //045 015 {-1.679328, -0.213798, -0.008253}
    //057 019 {-1.679328, -0.213798, -0.008253}
    //150 050 {-1.690959, -0.595577, -0.282308}
    //159 053 {-1.690959, -0.531698, 0.339597}
    //168 056 {-1.690959, -0.531698, 0.339597}
    //174 058 {-1.690959, -0.461340, 0.349529}
    //177 059 {-1.690959, -0.531698, 0.339597}
    //183 061 {-1.690959, -0.324411, 0.287138}
    //186 062 {-1.690959, -0.461340, 0.349529}
    //195 065 {-1.690959, -0.324411, 0.287138}
    //201 067 {-1.679328, -0.213798, -0.008253}
    //204 068 {-1.690959, -0.324411, 0.287138}
    //207 069 {-1.679328, -0.213798, -0.008253}
    //213 071 {-1.690959, -0.324412, -0.261506}
    //222 074 {-1.690959, -0.324412, -0.261506}
    //228 076 {-1.690959, -0.514919, -0.319164}
    //231 077 {-1.690959, -0.324412, -0.261506}
    //234 078 {-1.690959, -0.595577, -0.282308}
    //246 082 {-1.690959, -0.531698, 0.339597}
    //252 084 {-1.690959, -0.531698, 0.339597}
    //261 087 {-1.690959, -0.324411, 0.287138}
    //264 088 {-1.679328, -0.213798, -0.008253}
    //270 090 {-1.690959, -0.324412, -0.261506}
    //276 092 {-1.679328, -0.213798, -0.008253}
    //279 093 {-1.690959, -0.595577, -0.282308}
    //612 204 {-1.690959, -0.514919, -0.319164}
    //615 205 {-1.690959, -0.595577, -0.282308}
    //621 207 {-1.690959, -0.514919, -0.319164}
    //711 237 {-1.690959, -0.595577, -0.282308}
    //714 238 {-1.690959, -0.514919, -0.319164}
    //720 240 {-1.690959, -0.514919, -0.319164}
    //729 243 {-1.690959, -0.461340, 0.349529}
    //732 244 {-1.690959, -0.531698, 0.339597}
    //741 247 {-1.690959, -0.461340, 0.349529}
    //1134 378 {-1.690959, -0.531698, 0.339597}
    //1143 381 {-1.690959, -0.531698, 0.339597}
    //1152 384 {-1.690959, -0.531698, 0.339597}
    //1161 387 {-1.690959, -0.531698, 0.339597}
    //1170 390 {-1.690959, -0.514919, -0.319164}
    //1179 393 {-1.690959, -0.514919, -0.319164}
    //1191 397 {-1.690959, -0.595577, -0.282308}
    //1197 399 {-1.690959, -0.595577, -0.282308}
    //1242 414 {-1.690959, -0.531698, 0.339597}
    //1245 415 {-1.690959, -0.461340, 0.349529}
    //1251 417 {-1.690959, -0.461340, 0.349529}
    //1332 444 {-1.679328, -0.213798, -0.008253}
    //1353 451 {-1.679328, -0.213798, -0.008253}
    //1368 456 {-1.690959, -0.324412, -0.261506}
    //1371 457 {-1.690959, -0.514919, -0.319164}
    //1377 459 {-1.690959, -0.514919, -0.319164}
    //1386 462 {-1.690959, -0.461340, 0.349529}
    //1389 463 {-1.690959, -0.324411, 0.287138}
    //1395 465 {-1.690959, -0.461340, 0.349529}
    //1410 470 {-1.690959, -0.514919, -0.319164}
    //1416 472 {-1.690959, -0.595577, -0.282308}
    //1419 473 {-1.690959, -0.514919, -0.319164}
    synchronized (mScratch128i) {
      mScratch128i[0] = 15;
      mScratch128i[1] = 19;
      mScratch128i[2] = 50;
      mScratch128i[3] = 53;
      mScratch128i[4] = 56;
      mScratch128i[5] = 58;
      mScratch128i[6] = 59;
      mScratch128i[7] = 61;
      mScratch128i[8] = 62;
      mScratch128i[9] = 65;
      mScratch128i[10] = 67;
      mScratch128i[11] = 68;
      mScratch128i[12] = 69;
      mScratch128i[13] = 71;
      mScratch128i[14] = 74;
      mScratch128i[15] = 76;
      mScratch128i[16] = 77;
      mScratch128i[17] = 78;
      mScratch128i[18] = 82;
      mScratch128i[19] = 84;
      mScratch128i[20] = 87;
      mScratch128i[21] = 88;
      mScratch128i[22] = 90;
      mScratch128i[23] = 92;
      mScratch128i[24] = 93;
      mScratch128i[25] = 204;
      mScratch128i[26] = 205;
      mScratch128i[27] = 207;
      mScratch128i[28] = 237;
      mScratch128i[29] = 238;
      mScratch128i[30] = 240;
      mScratch128i[31] = 243;
      mScratch128i[32] = 244;
      mScratch128i[33] = 247;
      mScratch128i[34] = 378;
      mScratch128i[35] = 381;
      mScratch128i[36] = 384;
      mScratch128i[37] = 387;
      mScratch128i[38] = 390;
      mScratch128i[39] = 393;
      mScratch128i[40] = 397;
      mScratch128i[41] = 399;
      mScratch128i[42] = 414;
      mScratch128i[43] = 415;
      mScratch128i[44] = 417;
      mScratch128i[45] = 444;
      mScratch128i[46] = 451;
      mScratch128i[47] = 456;
      mScratch128i[48] = 457;
      mScratch128i[49] = 459;
      mScratch128i[50] = 462;
      mScratch128i[51] = 463;
      mScratch128i[52] = 465;
      mScratch128i[53] = 470;
      mScratch128i[54] = 472;
      mScratch128i[55] = 473;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<56; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //717 239 {-1.593937, -0.522068, -0.852975}
    //726 242 {-1.593937, -0.522068, -0.852975}
    //735 245 {-1.593937, -0.522068, 0.892432}
    //744 248 {-1.593937, -0.522068, 0.892432}
    //828 276 {-1.593937, -0.522068, -0.852975}
    //846 282 {-1.593937, -0.522068, 0.892432}
    //1149 383 {-1.593937, -0.522068, 0.892432}
    //1167 389 {-1.593937, -0.522068, 0.892432}
    //1185 395 {-1.593937, -0.522068, -0.852975}
    //1200 400 {-1.593937, -0.522068, -0.852975}
    synchronized (mScratch128i) {
      mScratch128i[0] = 239;
      mScratch128i[1] = 242;
      mScratch128i[2] = 245;
      mScratch128i[3] = 248;
      mScratch128i[4] = 276;
      mScratch128i[5] = 282;
      mScratch128i[6] = 383;
      mScratch128i[7] = 389;
      mScratch128i[8] = 395;
      mScratch128i[9] = 400;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<10; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //000 000 {-1.029931, -0.487081, 0.329853}
    //012 004 {-1.029931, -0.487082, -0.304221}
    //021 007 {-1.029931, -0.487082, -0.304221}
    //027 009 {-1.029931, -0.487081, 0.329853}
    //144 048 {-1.029931, -0.487082, -0.304221}
    //156 052 {-1.029931, -0.487081, 0.329853}
    //162 054 {-1.029931, -0.487081, 0.329853}
    //747 249 {-1.029931, -0.487081, 0.329853}
    //759 253 {-1.029931, -0.487082, -0.304221}
    //774 258 {-1.029931, -0.487081, 0.329853}
    //909 303 {-1.029931, -0.487082, -0.304221}
    //1101 367 {-1.029931, -0.487081, 0.329853}
    //1116 372 {-1.029931, -0.487082, -0.304221}
    //1407 469 {-1.029931, -0.487082, -0.304221}
    //1413 471 {-1.029931, -0.487082, -0.304221}
    synchronized (mScratch128i) {
      mScratch128i[0] = 0;
      mScratch128i[1] = 4;
      mScratch128i[2] = 7;
      mScratch128i[3] = 9;
      mScratch128i[4] = 48;
      mScratch128i[5] = 52;
      mScratch128i[6] = 54;
      mScratch128i[7] = 249;
      mScratch128i[8] = 253;
      mScratch128i[9] = 258;
      mScratch128i[10] = 303;
      mScratch128i[11] = 367;
      mScratch128i[12] = 372;
      mScratch128i[13] = 469;
      mScratch128i[14] = 471;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<15; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //048 016 {-0.955028, -0.033702, 0.300949}
    //054 018 {-0.955028, -0.033701, -0.275317}
    //192 064 {-0.955028, -0.033702, 0.300949}
    //198 066 {-0.955028, -0.033702, 0.300949}
    //210 070 {-0.955028, -0.033701, -0.275317}
    //216 072 {-0.955028, -0.033701, -0.275317}
    //1695 565 {-0.955028, -0.033701, -0.275317}
    //1701 567 {-0.955028, -0.033701, -0.275317}
    //1710 570 {-0.955028, -0.033701, -0.275317}
    //1731 577 {-0.955028, -0.033702, 0.300949}
    //1746 582 {-0.955028, -0.033702, 0.300949}
    //1755 585 {-0.955028, -0.033702, 0.300949}
    synchronized (mScratch128i) {
      mScratch128i[0] = 16;
      mScratch128i[1] = 18;
      mScratch128i[2] = 64;
      mScratch128i[3] = 66;
      mScratch128i[4] = 70;
      mScratch128i[5] = 72;
      mScratch128i[6] = 565;
      mScratch128i[7] = 567;
      mScratch128i[8] = 570;
      mScratch128i[9] = 577;
      mScratch128i[10] = 582;
      mScratch128i[11] = 585;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<12; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //006 002 {-0.833656, -0.250074, 0.423287}
    //015 005 {-0.833656, -0.250073, -0.397656}
    //036 012 {-0.833656, -0.250074, 0.423287}
    //066 022 {-0.833656, -0.250073, -0.397656}
    //165 055 {-0.833656, -0.250074, 0.423287}
    //171 057 {-0.833656, -0.250074, 0.423287}
    //180 060 {-0.833656, -0.250074, 0.423287}
    //189 063 {-0.833656, -0.250074, 0.423287}
    //219 073 {-0.833656, -0.250073, -0.397656}
    //225 075 {-0.833656, -0.250073, -0.397656}
    //1404 468 {-0.833656, -0.250073, -0.397656}
    //1677 559 {-0.833656, -0.250073, -0.397656}
    //1692 564 {-0.833656, -0.250073, -0.397656}
    //1749 583 {-0.833656, -0.250074, 0.423287}
    //1764 588 {-0.833656, -0.250074, 0.423287}
    synchronized (mScratch128i) {
      mScratch128i[0] = 2;
      mScratch128i[1] = 5;
      mScratch128i[2] = 12;
      mScratch128i[3] = 22;
      mScratch128i[4] = 55;
      mScratch128i[5] = 57;
      mScratch128i[6] = 60;
      mScratch128i[7] = 63;
      mScratch128i[8] = 73;
      mScratch128i[9] = 75;
      mScratch128i[10] = 468;
      mScratch128i[11] = 559;
      mScratch128i[12] = 564;
      mScratch128i[13] = 583;
      mScratch128i[14] = 588;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<15; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //018 006 {-0.745447, -0.569023, 0.012816}
    //030 010 {-0.745447, -0.569023, 0.012816}
    //147 049 {-0.745447, -0.569023, 0.012816}
    //153 051 {-0.745447, -0.569023, 0.012816}
    //237 079 {-0.745447, -0.569023, 0.012816}
    //243 081 {-0.745447, -0.569023, 0.012816}
    //1785 595 {-0.745447, -0.569023, 0.012816}
    //1800 600 {-0.745447, -0.569023, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 6;
      mScratch128i[1] = 10;
      mScratch128i[2] = 49;
      mScratch128i[3] = 51;
      mScratch128i[4] = 79;
      mScratch128i[5] = 81;
      mScratch128i[6] = 595;
      mScratch128i[7] = 600;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //003 001 {-0.671751, -0.341348, 0.414681}
    //009 003 {-0.671751, -0.341346, -0.389052}
    //039 013 {-0.671751, -0.341348, 0.414681}
    //063 021 {-0.671751, -0.341346, -0.389052}
    //750 250 {-0.671751, -0.341348, 0.414681}
    //753 251 {-0.671751, -0.341348, 0.414681}
    //756 252 {-0.671751, -0.341346, -0.389052}
    //762 254 {-0.671751, -0.341346, -0.389052}
    //768 256 {-0.671751, -0.341346, -0.389052}
    //771 257 {-0.671751, -0.341346, -0.389052}
    //777 259 {-0.671751, -0.341348, 0.414681}
    //915 305 {-0.671751, -0.341346, -0.389052}
    //933 311 {-0.671751, -0.341348, 0.414681}
    //939 313 {-0.671751, -0.341346, -0.389052}
    synchronized (mScratch128i) {
      mScratch128i[0] = 1;
      mScratch128i[1] = 3;
      mScratch128i[2] = 13;
      mScratch128i[3] = 21;
      mScratch128i[4] = 250;
      mScratch128i[5] = 251;
      mScratch128i[6] = 252;
      mScratch128i[7] = 254;
      mScratch128i[8] = 256;
      mScratch128i[9] = 257;
      mScratch128i[10] = 259;
      mScratch128i[11] = 305;
      mScratch128i[12] = 311;
      mScratch128i[13] = 313;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<14; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //024 008 {-0.419080, -0.306335, -0.328394}
    //033 011 {-0.419080, -0.306337, 0.354023}
    //042 014 {-0.419080, -0.306337, 0.354023}
    //069 023 {-0.419080, -0.306335, -0.328394}
    //765 255 {-0.419080, -0.306335, -0.328394}
    //780 260 {-0.401466, -0.398520, 0.913889}
    //912 304 {-0.401466, -0.398520, -0.906539}
    //918 306 {-0.419080, -0.306337, 0.354023}
    //924 308 {-0.401466, -0.398520, 0.913889}
    //927 309 {-0.419080, -0.306337, 0.354023}
    //930 310 {-0.401466, -0.398520, 0.913889}
    //936 312 {-0.419080, -0.306335, -0.328394}
    //942 314 {-0.401466, -0.398520, -0.906539}
    //945 315 {-0.419080, -0.306335, -0.328394}
    //948 316 {-0.401466, -0.398520, -0.906539}
    //1098 366 {-0.419080, -0.306337, 0.354023}
    //1104 368 {-0.401466, -0.398520, 0.913889}
    //1107 369 {-0.419080, -0.306337, 0.354023}
    //1110 370 {-0.401466, -0.398520, 0.913889}
    //1119 373 {-0.419080, -0.306335, -0.328394}
    //1122 374 {-0.401466, -0.398520, -0.906539}
    //1125 375 {-0.419080, -0.306335, -0.328394}
    //1131 377 {-0.401466, -0.398520, -0.906539}
    //1674 558 {-0.419080, -0.306335, -0.328394}
    //1683 561 {-0.419080, -0.306335, -0.328394}
    //1767 589 {-0.419080, -0.306337, 0.354023}
    //1773 591 {-0.419080, -0.306337, 0.354023}
    //1782 594 {-0.419080, -0.306337, 0.354023}
    //1791 597 {-0.419080, -0.306337, 0.354023}
    //1803 601 {-0.419080, -0.306335, -0.328394}
    //1809 603 {-0.419080, -0.306335, -0.328394}
    synchronized (mScratch128i) {
      mScratch128i[0] = 8;
      mScratch128i[1] = 11;
      mScratch128i[2] = 14;
      mScratch128i[3] = 23;
      mScratch128i[4] = 255;
      mScratch128i[5] = 260;
      mScratch128i[6] = 304;
      mScratch128i[7] = 306;
      mScratch128i[8] = 308;
      mScratch128i[9] = 309;
      mScratch128i[10] = 310;
      mScratch128i[11] = 312;
      mScratch128i[12] = 314;
      mScratch128i[13] = 315;
      mScratch128i[14] = 316;
      mScratch128i[15] = 366;
      mScratch128i[16] = 368;
      mScratch128i[17] = 369;
      mScratch128i[18] = 370;
      mScratch128i[19] = 373;
      mScratch128i[20] = 374;
      mScratch128i[21] = 375;
      mScratch128i[22] = 377;
      mScratch128i[23] = 558;
      mScratch128i[24] = 561;
      mScratch128i[25] = 589;
      mScratch128i[26] = 591;
      mScratch128i[27] = 594;
      mScratch128i[28] = 597;
      mScratch128i[29] = 601;
      mScratch128i[30] = 603;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<31; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //051 017 {-0.380327, 0.296043, 0.007275}
    //060 020 {-0.380327, 0.296043, 0.007275}
    //786 262 {-0.380327, 0.296043, 0.007275}
    //792 264 {-0.380327, 0.296043, 0.007275}
    //1713 571 {-0.380327, 0.296043, 0.007275}
    //1719 573 {-0.380327, 0.296043, 0.007275}
    //1728 576 {-0.380327, 0.296043, 0.007275}
    //1737 579 {-0.380327, 0.296043, 0.007275}
    synchronized (mScratch128i) {
      mScratch128i[0] = 17;
      mScratch128i[1] = 20;
      mScratch128i[2] = 262;
      mScratch128i[3] = 264;
      mScratch128i[4] = 571;
      mScratch128i[5] = 573;
      mScratch128i[6] = 576;
      mScratch128i[7] = 579;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //072 024 {0.043745, 0.424596, 0.122775}
    //795 265 {0.043745, 0.424596, 0.122775}
    //921 307 {0.001424, -0.388491, 1.123781}
    //951 317 {0.001424, -0.388491, -1.116434}
    //1062 354 {0.043745, 0.424596, 0.122775}
    //1113 371 {0.001424, -0.388491, 1.123781}
    //1128 376 {0.001424, -0.388491, -1.116434}
    //1743 581 {0.043745, 0.424596, 0.122775}
    synchronized (mScratch128i) {
      mScratch128i[0] = 24;
      mScratch128i[1] = 265;
      mScratch128i[2] = 307;
      mScratch128i[3] = 317;
      mScratch128i[4] = 354;
      mScratch128i[5] = 371;
      mScratch128i[6] = 376;
      mScratch128i[7] = 581;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //1074 358 {0.244100, 1.574999, 0.012821}
    //1095 365 {0.244100, 1.574999, 0.012821}
    synchronized (mScratch128i) {
      mScratch128i[0] = 358;
      mScratch128i[1] = 365;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<2; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //075 025 {0.373355, 0.404897, 0.320376}
    //081 027 {0.373355, 0.404899, -0.294743}
    //1530 510 {0.373355, -0.136658, -0.348838}
    //1533 511 {0.373355, 0.101219, -0.415852}
    //1539 513 {0.373355, -0.136658, -0.348838}
    //1548 516 {0.373355, 0.101219, -0.415852}
    //1551 517 {0.373355, 0.404899, -0.294743}
    //1557 519 {0.373355, 0.404899, -0.294743}
    //1566 522 {0.373355, 0.404899, -0.294743}
    //1587 529 {0.373355, 0.404897, 0.320376}
    //1602 534 {0.373355, 0.404897, 0.320376}
    //1605 535 {0.373355, 0.101216, 0.441483}
    //1611 537 {0.373355, 0.404897, 0.320376}
    //1620 540 {0.373355, 0.101216, 0.441483}
    //1623 541 {0.373355, -0.136660, 0.374468}
    //1629 543 {0.373355, -0.136660, 0.374468}
    //1638 546 {0.373355, -0.136660, 0.374468}
    //1641 547 {0.373355, -0.308028, 0.012816}
    //1647 549 {0.373355, -0.308028, 0.012816}
    //1656 552 {0.373355, -0.308028, 0.012816}
    //1659 553 {0.373355, -0.136658, -0.348838}
    //1665 555 {0.373355, -0.308028, 0.012816}
    //1680 560 {0.373355, 0.101219, -0.415852}
    //1686 562 {0.373355, 0.101219, -0.415852}
    //1689 563 {0.373355, -0.136658, -0.348838}
    //1698 566 {0.373355, 0.101219, -0.415852}
    //1704 568 {0.373355, 0.404899, -0.294743}
    //1707 569 {0.373355, 0.101219, -0.415852}
    //1716 572 {0.373355, 0.404899, -0.294743}
    //1725 575 {0.373355, 0.404899, -0.294743}
    //1734 578 {0.373355, 0.404897, 0.320376}
    //1740 580 {0.373355, 0.404897, 0.320376}
    //1752 584 {0.373355, 0.101216, 0.441483}
    //1758 586 {0.373355, 0.101216, 0.441483}
    //1761 587 {0.373355, 0.404897, 0.320376}
    //1770 590 {0.373355, 0.101216, 0.441483}
    //1776 592 {0.373355, -0.136660, 0.374468}
    //1779 593 {0.373355, 0.101216, 0.441483}
    //1788 596 {0.373355, -0.308028, 0.012816}
    //1794 598 {0.373355, -0.308028, 0.012816}
    //1797 599 {0.373355, -0.136660, 0.374468}
    //1806 602 {0.373355, -0.308028, 0.012816}
    //1812 604 {0.373355, -0.136658, -0.348838}
    //1815 605 {0.373355, -0.308028, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 25;
      mScratch128i[1] = 27;
      mScratch128i[2] = 510;
      mScratch128i[3] = 511;
      mScratch128i[4] = 513;
      mScratch128i[5] = 516;
      mScratch128i[6] = 517;
      mScratch128i[7] = 519;
      mScratch128i[8] = 522;
      mScratch128i[9] = 529;
      mScratch128i[10] = 534;
      mScratch128i[11] = 535;
      mScratch128i[12] = 537;
      mScratch128i[13] = 540;
      mScratch128i[14] = 541;
      mScratch128i[15] = 543;
      mScratch128i[16] = 546;
      mScratch128i[17] = 547;
      mScratch128i[18] = 549;
      mScratch128i[19] = 552;
      mScratch128i[20] = 553;
      mScratch128i[21] = 555;
      mScratch128i[22] = 560;
      mScratch128i[23] = 562;
      mScratch128i[24] = 563;
      mScratch128i[25] = 566;
      mScratch128i[26] = 568;
      mScratch128i[27] = 569;
      mScratch128i[28] = 572;
      mScratch128i[29] = 575;
      mScratch128i[30] = 578;
      mScratch128i[31] = 580;
      mScratch128i[32] = 584;
      mScratch128i[33] = 586;
      mScratch128i[34] = 587;
      mScratch128i[35] = 590;
      mScratch128i[36] = 592;
      mScratch128i[37] = 593;
      mScratch128i[38] = 596;
      mScratch128i[39] = 598;
      mScratch128i[40] = 599;
      mScratch128i[41] = 602;
      mScratch128i[42] = 604;
      mScratch128i[43] = 605;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<44; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //078 026 {0.518062, 0.547588, 0.002451}
    //087 029 {0.518062, 0.547588, 0.002451}
    //1065 355 {0.518062, 0.547588, 0.002451}
    //1071 357 {0.518062, 0.547588, 0.002451}
    //1080 360 {0.518062, 0.547588, 0.002451}
    //1089 363 {0.518062, 0.547588, 0.002451}
    //1569 523 {0.518062, 0.547588, 0.002451}
    //1575 525 {0.518062, 0.547588, 0.002451}
    //1584 528 {0.518062, 0.547588, 0.002451}
    //1593 531 {0.518062, 0.547588, 0.002451}
    synchronized (mScratch128i) {
      mScratch128i[0] = 26;
      mScratch128i[1] = 29;
      mScratch128i[2] = 355;
      mScratch128i[3] = 357;
      mScratch128i[4] = 360;
      mScratch128i[5] = 363;
      mScratch128i[6] = 523;
      mScratch128i[7] = 525;
      mScratch128i[8] = 528;
      mScratch128i[9] = 531;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<10; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //129 043 {1.212120, 0.247772, 0.339352}
    //135 045 {1.212120, 0.247775, -0.313721}
    //1425 475 {1.212120, 0.247775, -0.313721}
    //1476 492 {1.212120, 0.247772, 0.339352}
    //1536 512 {1.212120, 0.247775, -0.313721}
    //1542 514 {1.212120, 0.247775, -0.313721}
    //1554 518 {1.212120, 0.247775, -0.313721}
    //1563 521 {1.212120, 0.247775, -0.313721}
    //1608 536 {1.212120, 0.247772, 0.339352}
    //1614 538 {1.212120, 0.247772, 0.339352}
    //1626 542 {1.212120, 0.247772, 0.339352}
    //1635 545 {1.212120, 0.247772, 0.339352}
    synchronized (mScratch128i) {
      mScratch128i[0] = 43;
      mScratch128i[1] = 45;
      mScratch128i[2] = 475;
      mScratch128i[3] = 492;
      mScratch128i[4] = 512;
      mScratch128i[5] = 514;
      mScratch128i[6] = 518;
      mScratch128i[7] = 521;
      mScratch128i[8] = 536;
      mScratch128i[9] = 538;
      mScratch128i[10] = 542;
      mScratch128i[11] = 545;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<12; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //090 030 {1.390324, -0.034011, 0.012816}
    //093 031 {1.386304, 0.016958, -0.186763}
    //099 033 {1.386304, 0.016959, 0.212395}
    //102 034 {1.390324, -0.034011, 0.012816}
    //111 037 {1.335053, 0.070335, -0.235167}
    //117 039 {1.335053, 0.070334, 0.260798}
    //126 042 {1.317970, 0.490269, 0.234033}
    //138 046 {1.317970, 0.490271, -0.208400}
    //855 285 {1.386304, 0.016959, 0.212395}
    //858 286 {1.335053, 0.070334, 0.260798}
    //861 287 {1.390324, -0.034011, 0.012816}
    //864 288 {1.335053, 0.070334, 0.260798}
    //867 289 {1.386304, 0.016959, 0.212395}
    //873 291 {1.386304, 0.016959, 0.212395}
    //876 292 {1.335053, 0.070334, 0.260798}
    //882 294 {1.335053, 0.070335, -0.235167}
    //885 295 {1.386304, 0.016958, -0.186763}
    //888 296 {1.390324, -0.034011, 0.012816}
    //891 297 {1.386304, 0.016958, -0.186763}
    //894 298 {1.335053, 0.070335, -0.235167}
    //900 300 {1.386304, 0.016958, -0.186763}
    //903 301 {1.335053, 0.070335, -0.235167}
    //957 319 {1.335053, 0.070335, -0.235167}
    //975 325 {1.335053, 0.070334, 0.260798}
    //993 331 {1.386304, 0.016959, 0.212395}
    //1011 337 {1.386304, 0.016958, -0.186763}
    //1422 474 {1.335053, 0.070335, -0.235167}
    //1431 477 {1.335053, 0.070335, -0.235167}
    //1440 480 {1.317970, 0.490271, -0.208400}
    //1443 481 {1.317970, 0.574170, 0.012817}
    //1449 483 {1.317970, 0.490271, -0.208400}
    //1458 486 {1.317970, 0.574170, 0.012817}
    //1461 487 {1.317970, 0.490269, 0.234033}
    //1467 489 {1.317970, 0.490269, 0.234033}
    //1479 493 {1.335053, 0.070334, 0.260798}
    //1485 495 {1.335053, 0.070334, 0.260798}
    //1497 499 {1.390324, -0.034011, 0.012816}
    //1512 504 {1.390324, -0.034011, 0.012816}
    //1545 515 {1.335053, 0.070335, -0.235167}
    //1560 520 {1.317970, 0.490271, -0.208400}
    //1572 524 {1.317970, 0.490271, -0.208400}
    //1578 526 {1.317970, 0.574170, 0.012817}
    //1581 527 {1.317970, 0.490271, -0.208400}
    //1590 530 {1.317970, 0.490269, 0.234033}
    //1596 532 {1.317970, 0.490269, 0.234033}
    //1599 533 {1.317970, 0.574170, 0.012817}
    //1617 539 {1.317970, 0.490269, 0.234033}
    //1632 544 {1.335053, 0.070334, 0.260798}
    //1644 548 {1.335053, 0.070334, 0.260798}
    //1650 550 {1.390324, -0.034011, 0.012816}
    //1653 551 {1.335053, 0.070334, 0.260798}
    //1662 554 {1.335053, 0.070335, -0.235167}
    //1668 556 {1.335053, 0.070335, -0.235167}
    //1671 557 {1.390324, -0.034011, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 30;
      mScratch128i[1] = 31;
      mScratch128i[2] = 33;
      mScratch128i[3] = 34;
      mScratch128i[4] = 37;
      mScratch128i[5] = 39;
      mScratch128i[6] = 42;
      mScratch128i[7] = 46;
      mScratch128i[8] = 285;
      mScratch128i[9] = 286;
      mScratch128i[10] = 287;
      mScratch128i[11] = 288;
      mScratch128i[12] = 289;
      mScratch128i[13] = 291;
      mScratch128i[14] = 292;
      mScratch128i[15] = 294;
      mScratch128i[16] = 295;
      mScratch128i[17] = 296;
      mScratch128i[18] = 297;
      mScratch128i[19] = 298;
      mScratch128i[20] = 300;
      mScratch128i[21] = 301;
      mScratch128i[22] = 319;
      mScratch128i[23] = 325;
      mScratch128i[24] = 331;
      mScratch128i[25] = 337;
      mScratch128i[26] = 474;
      mScratch128i[27] = 477;
      mScratch128i[28] = 480;
      mScratch128i[29] = 481;
      mScratch128i[30] = 483;
      mScratch128i[31] = 486;
      mScratch128i[32] = 487;
      mScratch128i[33] = 489;
      mScratch128i[34] = 493;
      mScratch128i[35] = 495;
      mScratch128i[36] = 499;
      mScratch128i[37] = 504;
      mScratch128i[38] = 515;
      mScratch128i[39] = 520;
      mScratch128i[40] = 524;
      mScratch128i[41] = 526;
      mScratch128i[42] = 527;
      mScratch128i[43] = 530;
      mScratch128i[44] = 532;
      mScratch128i[45] = 533;
      mScratch128i[46] = 539;
      mScratch128i[47] = 544;
      mScratch128i[48] = 548;
      mScratch128i[49] = 550;
      mScratch128i[50] = 551;
      mScratch128i[51] = 554;
      mScratch128i[52] = 556;
      mScratch128i[53] = 557;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<54; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //879 293 {1.525407, -0.022785, 0.340291}
    //906 302 {1.525407, -0.022785, -0.314659}
    //954 318 {1.525407, -0.022785, -0.314659}
    //963 321 {1.525407, -0.022785, -0.314659}
    //972 324 {1.525407, -0.022785, 0.340291}
    //981 327 {1.525407, -0.022785, 0.340291}
    //996 332 {1.525407, -0.022785, 0.340291}
    //1002 334 {1.525407, -0.022785, 0.340291}
    //1014 338 {1.525407, -0.022785, -0.314659}
    //1020 340 {1.525407, -0.022785, -0.314659}
    synchronized (mScratch128i) {
      mScratch128i[0] = 293;
      mScratch128i[1] = 302;
      mScratch128i[2] = 318;
      mScratch128i[3] = 321;
      mScratch128i[4] = 324;
      mScratch128i[5] = 327;
      mScratch128i[6] = 332;
      mScratch128i[7] = 334;
      mScratch128i[8] = 338;
      mScratch128i[9] = 340;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<10; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //096 032 {1.601420, 0.079291, -0.161763}
    //105 035 {1.601420, 0.079292, 0.187395}
    //108 036 {1.601420, 0.079291, -0.161763}
    //120 040 {1.601420, 0.079292, 0.187395}
    //870 290 {1.601420, 0.079292, 0.187395}
    //897 299 {1.601420, 0.079291, -0.161763}
    //960 320 {1.601420, 0.079291, -0.161763}
    //966 322 {1.601420, 0.079291, -0.161763}
    //978 326 {1.601420, 0.079292, 0.187395}
    //984 328 {1.601420, 0.079292, 0.187395}
    //990 330 {1.601420, 0.079292, 0.187395}
    //999 333 {1.601420, 0.079292, 0.187395}
    //1008 336 {1.601420, 0.079291, -0.161763}
    //1017 339 {1.601420, 0.079291, -0.161763}
    //1494 498 {1.601420, 0.079292, 0.187395}
    //1503 501 {1.601420, 0.079292, 0.187395}
    //1515 505 {1.601420, 0.079291, -0.161763}
    //1521 507 {1.601420, 0.079291, -0.161763}
    synchronized (mScratch128i) {
      mScratch128i[0] = 32;
      mScratch128i[1] = 35;
      mScratch128i[2] = 36;
      mScratch128i[3] = 40;
      mScratch128i[4] = 290;
      mScratch128i[5] = 299;
      mScratch128i[6] = 320;
      mScratch128i[7] = 322;
      mScratch128i[8] = 326;
      mScratch128i[9] = 328;
      mScratch128i[10] = 330;
      mScratch128i[11] = 333;
      mScratch128i[12] = 336;
      mScratch128i[13] = 339;
      mScratch128i[14] = 498;
      mScratch128i[15] = 501;
      mScratch128i[16] = 505;
      mScratch128i[17] = 507;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<18; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //969 323 {1.718350, -0.041985, -0.357785}
    //987 329 {1.718350, -0.041984, 0.383417}
    //1005 335 {1.718350, -0.041984, 0.383417}
    //1023 341 {1.718350, -0.041985, -0.357785}
    synchronized (mScratch128i) {
      mScratch128i[0] = 323;
      mScratch128i[1] = 329;
      mScratch128i[2] = 335;
      mScratch128i[3] = 341;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<4; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //342 114 {2.348002, 0.600541, 0.012816}
    //354 118 {2.348002, 0.600541, 0.012816}
    //360 120 {2.348002, 0.600541, 0.012816}
    //372 124 {2.348002, 0.600541, 0.012816}
    //1026 342 {2.348002, 0.600541, 0.012816}
    //1047 349 {2.348002, 0.600541, 0.012816}
    //1446 482 {2.348002, 0.600541, 0.012816}
    //1452 484 {2.348002, 0.600541, 0.012816}
    //1464 488 {2.348002, 0.600541, 0.012816}
    //1473 491 {2.348002, 0.600541, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 114;
      mScratch128i[1] = 118;
      mScratch128i[2] = 120;
      mScratch128i[3] = 124;
      mScratch128i[4] = 342;
      mScratch128i[5] = 349;
      mScratch128i[6] = 482;
      mScratch128i[7] = 484;
      mScratch128i[8] = 488;
      mScratch128i[9] = 491;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<10; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //1032 344 {2.482447, 0.688611, 0.012818}
    //1041 347 {2.482447, 0.688611, 0.012818}
    //1050 350 {2.482447, 0.688611, 0.012818}
    //1056 352 {2.482447, 0.688611, 0.012818}
    synchronized (mScratch128i) {
      mScratch128i[0] = 344;
      mScratch128i[1] = 347;
      mScratch128i[2] = 350;
      mScratch128i[3] = 352;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<4; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //114 038 {2.583151, 0.271833, -0.067486}
    //123 041 {2.583151, 0.271832, 0.093118}
    //351 117 {2.543960, 0.580612, 0.037585}
    //363 121 {2.543960, 0.580612, -0.011952}
    //378 126 {2.583151, 0.271833, -0.067486}
    //387 129 {2.570087, 0.214159, 0.012816}
    //390 130 {2.583151, 0.271833, -0.067486}
    //396 132 {2.583151, 0.271832, 0.093118}
    //399 133 {2.570087, 0.214159, 0.012816}
    //405 135 {2.583151, 0.271832, 0.093118}
    //804 268 {2.543960, 0.580612, -0.011952}
    //810 270 {2.543960, 0.580612, 0.037585}
    //1029 343 {2.543960, 0.580612, 0.037585}
    //1035 345 {2.543960, 0.580612, 0.037585}
    //1044 348 {2.543960, 0.580612, -0.011952}
    //1053 351 {2.543960, 0.580612, -0.011952}
    //1437 479 {2.583151, 0.271833, -0.067486}
    //1488 496 {2.583151, 0.271832, 0.093118}
    //1500 500 {2.570087, 0.214159, 0.012816}
    //1506 502 {2.570087, 0.214159, 0.012816}
    //1509 503 {2.583151, 0.271832, 0.093118}
    //1518 506 {2.570087, 0.214159, 0.012816}
    //1524 508 {2.583151, 0.271833, -0.067486}
    //1527 509 {2.570087, 0.214159, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 38;
      mScratch128i[1] = 41;
      mScratch128i[2] = 117;
      mScratch128i[3] = 121;
      mScratch128i[4] = 126;
      mScratch128i[5] = 129;
      mScratch128i[6] = 130;
      mScratch128i[7] = 132;
      mScratch128i[8] = 133;
      mScratch128i[9] = 135;
      mScratch128i[10] = 268;
      mScratch128i[11] = 270;
      mScratch128i[12] = 343;
      mScratch128i[13] = 345;
      mScratch128i[14] = 348;
      mScratch128i[15] = 351;
      mScratch128i[16] = 479;
      mScratch128i[17] = 496;
      mScratch128i[18] = 500;
      mScratch128i[19] = 502;
      mScratch128i[20] = 503;
      mScratch128i[21] = 506;
      mScratch128i[22] = 508;
      mScratch128i[23] = 509;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<24; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //132 044 {2.658017, 0.432141, 0.113505}
    //141 047 {2.658017, 0.432142, -0.087873}
    //345 115 {2.658017, 0.432141, 0.113505}
    //369 123 {2.658017, 0.432142, -0.087873}
    //381 127 {2.658017, 0.432142, -0.087873}
    //411 137 {2.658017, 0.432141, 0.113505}
    //417 139 {2.658017, 0.432141, 0.113505}
    //423 141 {2.658017, 0.432142, -0.087873}
    //435 145 {2.658017, 0.432142, -0.087873}
    //459 153 {2.658017, 0.432141, 0.113505}
    //807 269 {2.623093, 0.715001, 0.012817}
    //816 272 {2.623093, 0.715001, 0.012817}
    //1038 346 {2.623093, 0.715001, 0.012817}
    //1059 353 {2.623093, 0.715001, 0.012817}
    //1296 432 {2.658017, 0.432142, -0.087873}
    //1317 439 {2.658017, 0.432141, 0.113505}
    //1428 476 {2.658017, 0.432142, -0.087873}
    //1434 478 {2.658017, 0.432142, -0.087873}
    //1455 485 {2.658017, 0.432142, -0.087873}
    //1470 490 {2.658017, 0.432141, 0.113505}
    //1482 494 {2.658017, 0.432141, 0.113505}
    //1491 497 {2.658017, 0.432141, 0.113505}
    synchronized (mScratch128i) {
      mScratch128i[0] = 44;
      mScratch128i[1] = 47;
      mScratch128i[2] = 115;
      mScratch128i[3] = 123;
      mScratch128i[4] = 127;
      mScratch128i[5] = 137;
      mScratch128i[6] = 139;
      mScratch128i[7] = 141;
      mScratch128i[8] = 145;
      mScratch128i[9] = 153;
      mScratch128i[10] = 269;
      mScratch128i[11] = 272;
      mScratch128i[12] = 346;
      mScratch128i[13] = 353;
      mScratch128i[14] = 432;
      mScratch128i[15] = 439;
      mScratch128i[16] = 476;
      mScratch128i[17] = 478;
      mScratch128i[18] = 485;
      mScratch128i[19] = 490;
      mScratch128i[20] = 494;
      mScratch128i[21] = 497;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<22; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //348 116 {2.752981, 0.538035, 0.012816}
    //357 119 {2.752981, 0.538035, 0.012816}
    //366 122 {2.752981, 0.538035, 0.012816}
    //375 125 {2.752981, 0.538035, 0.012816}
    //414 138 {2.752981, 0.538035, 0.012816}
    //426 142 {2.752981, 0.538035, 0.012816}
    //801 267 {2.752981, 0.538035, 0.012816}
    //813 271 {2.752981, 0.538035, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 116;
      mScratch128i[1] = 119;
      mScratch128i[2] = 122;
      mScratch128i[3] = 125;
      mScratch128i[4] = 138;
      mScratch128i[5] = 142;
      mScratch128i[6] = 267;
      mScratch128i[7] = 271;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //384 128 {3.026316, 0.322189, 0.012816}
    //393 131 {3.026316, 0.322189, 0.012816}
    //402 134 {3.026316, 0.322189, 0.012816}
    //408 136 {3.026316, 0.322189, 0.012816}
    //420 140 {3.026316, 0.501066, 0.012816}
    //429 143 {3.026316, 0.501066, 0.012816}
    //432 144 {3.026316, 0.322189, 0.012816}
    //441 147 {3.026316, 0.322189, 0.012816}
    //450 150 {3.026316, 0.322189, 0.012816}
    //462 154 {3.026316, 0.322189, 0.012816}
    //1299 433 {3.026316, 0.501066, 0.012816}
    //1305 435 {3.026316, 0.501066, 0.012816}
    //1314 438 {3.026316, 0.501066, 0.012816}
    //1323 441 {3.026316, 0.501066, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 128;
      mScratch128i[1] = 131;
      mScratch128i[2] = 134;
      mScratch128i[3] = 136;
      mScratch128i[4] = 140;
      mScratch128i[5] = 143;
      mScratch128i[6] = 144;
      mScratch128i[7] = 147;
      mScratch128i[8] = 150;
      mScratch128i[9] = 154;
      mScratch128i[10] = 433;
      mScratch128i[11] = 435;
      mScratch128i[12] = 438;
      mScratch128i[13] = 441;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<14; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //447 149 {3.235338, 0.272275, 0.012816}
    //453 151 {3.235338, 0.272275, 0.012816}
    //468 156 {3.235338, 0.272275, 0.012816}
    //480 160 {3.235338, 0.272275, 0.012816}
    //486 162 {3.235338, 0.554113, 0.012816}
    //498 166 {3.235338, 0.554113, 0.012816}
    //1308 436 {3.235338, 0.554113, 0.012816}
    //1329 443 {3.235338, 0.554113, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 149;
      mScratch128i[1] = 151;
      mScratch128i[2] = 156;
      mScratch128i[3] = 160;
      mScratch128i[4] = 162;
      mScratch128i[5] = 166;
      mScratch128i[6] = 436;
      mScratch128i[7] = 443;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<8; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //438 146 {3.464372, 0.451621, 0.002777}
    //444 148 {3.464372, 0.451621, 0.002777}
    //456 152 {3.464372, 0.451618, 0.022856}
    //465 155 {3.464372, 0.451618, 0.022856}
    //471 157 {3.464372, 0.451621, 0.002777}
    //477 159 {3.464372, 0.451618, 0.022856}
    //489 163 {3.464372, 0.451618, 0.022856}
    //495 165 {3.464372, 0.451621, 0.002777}
    //504 168 {3.464372, 0.451618, 0.022856}
    //513 171 {3.464372, 0.451621, 0.002777}
    //522 174 {3.464372, 0.451618, 0.022856}
    //534 178 {3.464372, 0.451621, 0.002777}
    //540 180 {3.464372, 0.451618, 0.022856}
    //552 184 {3.464372, 0.451618, 0.022856}
    //558 186 {3.464372, 0.451621, 0.002777}
    //570 190 {3.464372, 0.451621, 0.002777}
    //1260 420 {3.464372, 0.451618, 0.022856}
    //1281 427 {3.464372, 0.451621, 0.002777}
    //1302 434 {3.464372, 0.451621, 0.002777}
    //1311 437 {3.464372, 0.451621, 0.002777}
    //1320 440 {3.464372, 0.451618, 0.022856}
    //1326 442 {3.464372, 0.451618, 0.022856}
    synchronized (mScratch128i) {
      mScratch128i[0] = 146;
      mScratch128i[1] = 148;
      mScratch128i[2] = 152;
      mScratch128i[3] = 155;
      mScratch128i[4] = 157;
      mScratch128i[5] = 159;
      mScratch128i[6] = 163;
      mScratch128i[7] = 165;
      mScratch128i[8] = 168;
      mScratch128i[9] = 171;
      mScratch128i[10] = 174;
      mScratch128i[11] = 178;
      mScratch128i[12] = 180;
      mScratch128i[13] = 184;
      mScratch128i[14] = 186;
      mScratch128i[15] = 190;
      mScratch128i[16] = 420;
      mScratch128i[17] = 427;
      mScratch128i[18] = 434;
      mScratch128i[19] = 437;
      mScratch128i[20] = 440;
      mScratch128i[21] = 442;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<22; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //474 158 {3.516712, -0.278531, 0.012816}
    //483 161 {3.516712, -0.278531, 0.012816}
    //1263 421 {3.516712, -0.278531, 0.012816}
    //1269 423 {3.516712, -0.278531, 0.012816}
    //1278 426 {3.516712, -0.278531, 0.012816}
    //1287 429 {3.516712, -0.278531, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 158;
      mScratch128i[1] = 161;
      mScratch128i[2] = 421;
      mScratch128i[3] = 423;
      mScratch128i[4] = 426;
      mScratch128i[5] = 429;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<6; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //492 164 {3.777989, 0.913256, 0.012816}
    //501 167 {3.777989, 0.913256, 0.012816}
    //507 169 {3.765930, -0.159603, 0.013754}
    //510 170 {3.745076, 0.292977, 0.013978}
    //516 172 {3.745076, 0.292984, 0.011655}
    //519 173 {3.765930, -0.159600, 0.011878}
    //525 175 {3.745076, 0.292977, 0.013978}
    //531 177 {3.745076, 0.292984, 0.011655}
    //549 183 {3.777989, 0.913256, 0.012816}
    //561 187 {3.777989, 0.913256, 0.012816}
    //1266 422 {3.765930, -0.159603, 0.013754}
    //1272 424 {3.765930, -0.477161, 0.012816}
    //1275 425 {3.765930, -0.159603, 0.013754}
    //1284 428 {3.765930, -0.159600, 0.011878}
    //1290 430 {3.765930, -0.159600, 0.011878}
    //1293 431 {3.765930, -0.477161, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 164;
      mScratch128i[1] = 167;
      mScratch128i[2] = 169;
      mScratch128i[3] = 170;
      mScratch128i[4] = 172;
      mScratch128i[5] = 173;
      mScratch128i[6] = 175;
      mScratch128i[7] = 177;
      mScratch128i[8] = 183;
      mScratch128i[9] = 187;
      mScratch128i[10] = 422;
      mScratch128i[11] = 424;
      mScratch128i[12] = 425;
      mScratch128i[13] = 428;
      mScratch128i[14] = 430;
      mScratch128i[15] = 431;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<16; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //528 176 {4.256675, 0.731350, 0.014599}
    //537 179 {4.256675, 0.731355, 0.011033}
    //543 181 {4.256675, 0.731350, 0.014599}
    //567 189 {4.256675, 0.731355, 0.011033}
    synchronized (mScratch128i) {
      mScratch128i[0] = 176;
      mScratch128i[1] = 179;
      mScratch128i[2] = 181;
      mScratch128i[3] = 189;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<4; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
    //546 182 {5.631042, 1.448036, 0.012816}
    //555 185 {5.631042, 1.448036, 0.012816}
    //564 188 {5.631042, 1.448036, 0.012816}
    //573 191 {5.631042, 1.448036, 0.012816}
    synchronized (mScratch128i) {
      mScratch128i[0] = 182;
      mScratch128i[1] = 185;
      mScratch128i[2] = 188;
      mScratch128i[3] = 191;
      float width = getMoveWidth(ShumokuData.vertices[0+3*mScratch128i[0]]) * (s + (animationGap * scale));
      int ii;
      for (ii=0; ii<4; ii++) {
        ShumokuData.vertices[2+3*mScratch128i[ii]] = ShumokuData.org_vertices[2+3*mScratch128i[ii]] + width;
        if (ShumokuData.vertices[0+3*mScratch128i[ii]] > 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] - (width/3f);
        } else if (ShumokuData.vertices[0+3*mScratch128i[ii]] < 0.0f) {
          ShumokuData.vertices[0+3*mScratch128i[ii]] = ShumokuData.org_vertices[0+3*mScratch128i[ii]] + (width/3f);
        }
      }
    }
  }

  public void calc() {
    synchronized (this) {
      setTurnDirection(TURN_DIRECTION.STRAIGHT);
      think();
      move();
      animate();
    }
  }

  public void draw(GL10 gl10) {
    gl10.glPushMatrix();


    gl10.glPushMatrix();
    {
      /*=======================================================================*/
      /* 環境光の材質色設定                                                    */
      /*=======================================================================*/
      synchronized (mScratch4f) {
        mScratch4f[0] = 0.07f;
        mScratch4f[1] = 0.07f;
        mScratch4f[2] = 0.07f;
        mScratch4f[3] = 1.0f;
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mScratch4f, 0);
      }
      /*=======================================================================*/
      /* 拡散反射光の色設定                                                    */
      /*=======================================================================*/
      synchronized (mScratch4f) {
        mScratch4f[0] = 0.24f;
        mScratch4f[1] = 0.24f;
        mScratch4f[2] = 0.24f;
        mScratch4f[3] = 1.0f;
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mScratch4f, 0);
      }
      /*=======================================================================*/
      /* 鏡面反射光の質感色設定                                                */
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
    gl10.glScalef(GL_SHUMOKU_SCALE,GL_SHUMOKU_SCALE,GL_SHUMOKU_SCALE);

    gl10.glRotatef(angleForAnimation, 0.0f, 1.0f, 0.0f);

    gl10.glRotatef(y_angle, 0.0f, 1.0f, 0.0f);
    gl10.glRotatef(5f * -1f, 0.0f, 0.0f, 1.0f);
    gl10.glRotatef(x_angle * -1f, 0.0f, 0.0f, 1.0f);

    // boundingboxを計算
    separateBoundingBox();
    alignment1BoundingBox();
    alignment2BoundingBox();

    gl10.glColor4f(1,1,1,1);
    gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
    gl10.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
    gl10.glEnable(GL10.GL_TEXTURE_2D);
    gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
    gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
    gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, ShumokuData.numVerts);



    gl10.glPopMatrix();
    gl10.glPopMatrix();
  }

  public void update_speed() {
    sv_speed = speed;
    if (getStatus() == STATUS.COHESION || getStatus() == STATUS.TO_SCHOOL_CENTER || getStatus() == STATUS.TO_BAIT) {
      speed = cohesion_speed;
      return;
    }
    speed = sv_speed;

    if (this.rand.nextInt(10000) <= 1000) {
      // 変更なし
      return;
    }
    speed += (this.rand.nextFloat() * (speed_unit * 2f) / 2f);
    if (speed <= speed_min) {
      speed = speed_min;
    }
    if (speed > speed_max) {
      speed = speed_max;
    }
  }

  /** 
   * Returns the closest hammerhead shark.
   * (Spearate Only.)
   */
  public Model[] getTarget() {
    float targetDistanceS = 10000f;
    float targetDistanceA = 10000f;
    float targetDistanceC = 10000f;
    int targetS = 9999;
    int targetA = 9999;
    int targetC = 9999;
    for (int ii=0; species != null && ii<species.length; ii++) {
      float dist = 0f;
      if (ii != species.length - 1 && shumokuCount <= ii) {
        continue;
      }
      else if (ii < shumokuNo) {
        dist = species[ii].getDistances()[shumokuNo];
      }
      else if (ii == shumokuNo) {
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
        if (targetDistanceS > dist) {
          targetDistanceS = dist;
          targetS = ii;
        }
        continue;
      }

      if (dist < alignment_dist2) {
        if (targetDistanceA > dist) {
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
                targetDistanceA = dist;
                targetA = ii;
              }
            }
          }
        }
        continue;
      }

      if (dist < cohesion_dist) {
        if (targetDistanceC > dist) {
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
                targetDistanceC = dist;
                targetC = ii;
              }
            }
          }
        }
      }
    }
    if (targetS != 9999) {
      mScratch3Shumoku[0] = species[targetS];
    }
    else {
      mScratch3Shumoku[0] = null;
    }
    if (targetA != 9999) {
      mScratch3Shumoku[1] = species[targetA];
    }
    else {
      mScratch3Shumoku[1] = null;
    }
    if (targetC != 9999) {
      mScratch3Shumoku[2] = species[targetC];
    }
    else {
      mScratch3Shumoku[2] = null;
    }
    return mScratch3Shumoku;
  }

  /**
   * どの方向に進むか考える
   */
  public void think() {
    long nowTime = System.nanoTime();
    if (prevTime != 0) {
      tick = nowTime - prevTime;
    }
    if (getStatus() == STATUS.COHESION || getStatus() == STATUS.TO_SCHOOL_CENTER || getStatus() == STATUS.TO_BAIT) {
      /* 元に戻す */
      speed = sv_speed;
    }
    prevTime = nowTime;
    /* Aquarium ----------------------------------------------------------*/
    if (  (Aquarium.min_x.floatValue() + (getSize() * 1.5f) >= position[0] || Aquarium.max_x.floatValue() - (getSize() * 1.5f) <= position[0])
      ||  (Aquarium.min_y.floatValue() + (getSize()/3f) >= position[1] || Aquarium.max_y.floatValue() - (getSize()/3f) <= position[1])
      ||  (Aquarium.min_z.floatValue() + (getSize() * 1.5f) >= position[2] || Aquarium.max_z.floatValue() - (getSize() * 1.5f) <= position[2])) {
      /*=====================================================================*/
      /* 水槽からはみ出てる                                                  */
      /*=====================================================================*/
      setStatus(STATUS.TO_CENTER);
      aimAquariumCenter();
      if (traceBOIDS && shumokuNo == 0) Log.d(TAG, "to Aquarium Center");
      update_speed();
      return;
    }

    /* Bait -------------------------------------------------------------*/
    Bait bait = baitManager.getBait();
    if (bait != null) {
      if (this.rand.nextInt(10000) <= 5500) {
        if (aimBait(bait)) {
          if (traceBOIDS && shumokuNo == 0) Log.d(TAG, "to Bait");
          setStatus(STATUS.TO_BAIT);
          update_speed();
          return;
        }
      }
    }

    /* Separate -------------------------------------------------------------*/
    if (getEnableBoids()) {
      Model[] target = getTarget();
      if (target[0] != null) {
        if (doSeparation(target[0])) {
          update_speed();
          target[0] = null;
          target[1] = null;
          target[2] = null;
          return;
        }
      }
      if (target[1] != null) {
        if (doAlignment(target[1])) {
          update_speed();
          target[0] = null;
          target[1] = null;
          target[2] = null;
          return;
        }
      }
      if (target[2] != null) {
        if (doCohesion(target[2])) {
          update_speed();
          target[0] = null;
          target[1] = null;
          target[2] = null;
          return;
        }
      }
    }

    if (this.rand.nextInt(10000) <= 9500) {
      if (traceBOIDS && shumokuNo == 0) Log.d(TAG, "Nop");
      // 変更なし
      return;
    }
    setStatus(STATUS.NORMAL);
    turn();
    if (traceBOIDS && shumokuNo == 0) Log.d(TAG, "Normal");
    update_speed();
  }

  /**
   * Perform the separation.
   *
   * @param target separation target as Model.
   * @return true If the separation is performed.
   */
  public boolean doSeparation(Model target) {
    setStatus(STATUS.SEPARATE);
    turnSeparation(target);
    return true;
  }
  /**
   * Separation version turnaround processing.
   */
  protected void turnSeparation(Model target) {
    if (debug) { Log.d(TAG, "start turnSeparation"); }
    float v_x = 0f;
    float v_y = 0f;
    float v_z = 0f;
    synchronized (mScratch4f_1) {
      /*=======================================================================*/
      /* Get direction of Separation Target                                    */
      /*=======================================================================*/
      mScratch4f_1[0] = target.getDirectionX();
      mScratch4f_1[1] = target.getDirectionY();
      mScratch4f_1[2] = target.getDirectionZ();
      CoordUtil.normalize3fv(mScratch4f_1);
      synchronized (mScratch4f_2) {
        /*=====================================================================*/
        /* Get direction of me from Separation Target.                         */
        /*=====================================================================*/
        mScratch4f_2[0] = getX() - target.getX();
        mScratch4f_2[1] = getY() - target.getY();
        mScratch4f_2[2] = getZ() - target.getZ();
        CoordUtil.normalize3fv(mScratch4f_2);
        /*=====================================================================*/
        /* X 2 because I want to be closer to the target direction.            */
        /*=====================================================================*/
        mScratch4f_1[0] *= 2f;
        mScratch4f_1[1] *= 2f;
        mScratch4f_1[2] *= 2f;
        /*=====================================================================*/
        /* Do Sum                                                              */
        /*=====================================================================*/
        mScratch4f_1[0] += mScratch4f_2[0];
        mScratch4f_1[1] += mScratch4f_2[1];
        mScratch4f_1[2] += mScratch4f_2[2];
      }
      /*=====================================================================*/
      /* calclate sum avarage                                                */
      /*=====================================================================*/
      mScratch4f_1[0] /= 3f;
      mScratch4f_1[1] /= 3f;
      mScratch4f_1[2] /= 3f;

      v_x = mScratch4f_1[0];
      v_y = mScratch4f_1[1];
      v_z = mScratch4f_1[2];
    }
    if (debug) {
      Log.d(TAG, "向かいたい方向"
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
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
    }

    /* その角度へ近づける */
    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "実際に向かう方向のy_angle:[" + y_angle + "]");
      Log.d(TAG, "実際に向かう方向のx_angle:[" + x_angle + "]");
    }

    /* Set Direction */
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
      Log.d(TAG, "結果的に向かう方向"
       + " x:[" + direction[0] + "]:"
       + " y:[" + direction[1] + "]:"
       + " z:[" + direction[2] + "]:");
      Log.d(TAG, "end turnSeparation");
    }
  }


  protected boolean doAlignment(Model target) {
    if (this.rand.nextInt(10000) <= 30000) {
      return false;
    }
    /*===================================================================*/
    /* アラインメント領域にターゲットがいる場合                          */
    /*===================================================================*/
    if (debug) {
      if (shumokuNo == 0) {
        Log.d(TAG, "doAlignment");
      }
    }
    setStatus(STATUS.ALIGNMENT);
    turnAlignment(target);
    return true;
  }


  protected boolean doCohesion(Model target) {
    /*===================================================================*/
    /* 鰯は結構な確率でCohesionするものと思われる                        */
    /*===================================================================*/
    if (getStatus() == STATUS.COHESION) {
      if (this.rand.nextInt(10000) <= 500) {
        /*===============================================================*/
        /* 前回COHESIONである場合今回もCOHESIONである可能性は高い        */
        /*===============================================================*/
        return false;
      }
    }
    else {
      if (this.rand.nextInt(10000) <= 1000) {
        return false;
      }
    }
    /*===================================================================*/
    /* コアージョン領域にターゲットがいる場合                            */
    /*===================================================================*/
    setStatus(STATUS.COHESION);
    turnCohesion(target);
    return true;
  }
  public void turn() {
    /*
     * x_angle is up or down.
     * y_angle is right or left.
     */
    float old_angle_x = x_angle;
    float old_angle_y = y_angle;
    x_angle = old_angle_x;
    y_angle = old_angle_y;
    float newAngleX = 0f;
    float newAngleY = 0f;
    if (angleForAnimation < 0f) {
      newAngleY = this.rand.nextFloat() * -1.5f;
      setTurnDirection(TURN_DIRECTION.TURN_RIGHT);
    }
    else {
      newAngleY = this.rand.nextFloat() * 1.5f;
      setTurnDirection(TURN_DIRECTION.TURN_LEFT);
    }

    if (this.rand.nextInt(10000) <= 1000) {
      newAngleX = this.rand.nextFloat() * (MAX_X_ANGLE * 2f) - MAX_X_ANGLE;
      if (newAngleX + x_angle <= MAX_X_ANGLE && newAngleX + x_angle >= -MAX_X_ANGLE) {
        x_angle = x_angle + newAngleX;
      }
      else {
        if (newAngleX + x_angle >= MAX_X_ANGLE) {
          x_angle = (this.rand.nextFloat() * MAX_X_ANGLE);
        }
        else if (newAngleX + x_angle <= -MAX_X_ANGLE) {
          x_angle = (this.rand.nextFloat() * -MAX_X_ANGLE);
        }
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
    float newAngle = this.rand.nextFloat() * MAX_X_ANGLE;
    float xx = angle_x - x_angle;
    if (xx < 0.0f) {
      if (xx > -MAX_X_ANGLE) {
        x_angle += xx;
      }
      else {
        x_angle += -newAngle;
      }
    }
    else {
      if (xx < MAX_X_ANGLE) {
        x_angle += xx;
      }
      else {
        x_angle += newAngle;
      }
    }
    if (x_angle > MAX_X_ANGLE) {
      x_angle = MAX_X_ANGLE;
    }
    if (x_angle < -MAX_X_ANGLE) {
      x_angle = -MAX_X_ANGLE;
    }


    float yy = angle_y - y_angle;
    if (yy > 180.0f) {
      yy = -360f + yy;
    }
    else if (yy < -180.0f) {
      yy = 360f - yy;
    }

    if (yy < 0.0f) {
      if (angleForAnimation < 0f) {
        if (yy > -6f) {
          y_angle += yy;
        }
        else {
          y_angle += (-newAngle * 2f);
        }
        setTurnDirection(TURN_DIRECTION.TURN_LEFT);
      }
    }
    else if (yy > 0.0f) {
      if (angleForAnimation > 0f) {
        if (yy < 6f) {
          y_angle += yy;
        }
        else {
          y_angle += (newAngle * 2f);
        }
        setTurnDirection(TURN_DIRECTION.TURN_RIGHT);
      }
    }
    else {
      setTurnDirection(TURN_DIRECTION.STRAIGHT);
    }
    y_angle = y_angle % 360f;
    if (y_angle < 0f) {
      y_angle = 360f + y_angle;
    }
  }
  public void aimTargetSpeed(float t_speed) {
    if (t_speed <= speed) {
      /* 自分のスピードよりも相手の方が遅い場合 */
      if (false) {
        speed -= (this.rand.nextFloat() * speed_unit);
        if (speed <= speed_min) {
          speed = speed_unit;
        }
      }
      else {
       update_speed();
      }
    }
    else {
      /* 相手の方が早い場合 */
      speed += (this.rand.nextFloat() * speed_unit);
      if (t_speed < speed) {
        /* 越えちゃったらちょっとだけ遅く*/
        speed = t_speed - (this.rand.nextFloat() * speed_unit);
      }
      if (speed > speed_max) {
        speed = speed_max;
      }
    }
  }
  public void turnAlignment(Model target) {
    if (debug) {
      Log.d(TAG, "start turnAlignment");
    }
    /* ターゲットの角度 */
    float angle_x = target.getX_angle();
    float angle_y = target.getY_angle();
    if (debug) {
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
    }

    /* その角度へ近づける */
    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "実際に向かう方向のy_angle:[" + y_angle + "]");
      Log.d(TAG, "実際に向かう方向のx_angle:[" + x_angle + "]");
    }

    /* direction設定 */
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
      Log.d(TAG, "結果的に向かう方向"
       + " x:[" + direction[0] + "]:"
       + " y:[" + direction[1] + "]:"
       + " z:[" + direction[2] + "]:");
    }

    /* スピードも合わせる */
    aimTargetSpeed(target.getSpeed());

    if (debug) {
      Log.d(TAG, "end turnAlignment");
    }
  }
  public void turnCohesion(Model target) {
    if (debug) { Log.d(TAG, "start turnCohesion"); }
    float v_x = 0f;
    float v_y = 0f;
    float v_z = 0f;
    synchronized (mScratch4f_1) {
      /*=======================================================================*/
      /* Separationしたいターゲットの方向取得                                  */
      /*=======================================================================*/
      mScratch4f_1[0] = target.getDirectionX();
      mScratch4f_1[1] = target.getDirectionY();
      mScratch4f_1[2] = target.getDirectionZ();
      CoordUtil.normalize3fv(mScratch4f_1);
      synchronized (mScratch4f_2) {
        /*=====================================================================*/
        /* 自分から見て、ターゲットの方向を算出                                */
        /*=====================================================================*/
        mScratch4f_2[0] = target.getX() - getX();
        mScratch4f_2[1] = target.getY() - getY();
        mScratch4f_2[2] = target.getZ() - getZ();
        CoordUtil.normalize3fv(mScratch4f_2);
        /*=====================================================================*/
        /* ややターゲットに近づきたいので x2                                   */
        /*=====================================================================*/
        mScratch4f_2[0] *= 2f;
        mScratch4f_2[1] *= 2f;
        mScratch4f_2[2] *= 2f;
        /*=====================================================================*/
        /* 足し込む                                                            */
        /*=====================================================================*/
        mScratch4f_1[0] += mScratch4f_2[0];
        mScratch4f_1[1] += mScratch4f_2[1];
        mScratch4f_1[2] += mScratch4f_2[2];
      }
      /*=====================================================================*/
      /* 平均算出                                                            */
      /*=====================================================================*/
      mScratch4f_1[0] /= 3f;
      mScratch4f_1[1] /= 3f;
      mScratch4f_1[2] /= 3f;

      v_x = mScratch4f_1[0];
      v_y = mScratch4f_1[1];
      v_z = mScratch4f_1[2];
    }
    if (debug) {
      Log.d(TAG, "向かいたい方向"
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
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
    }

    /* その角度へ近づける */
    aimTargetDegree(angle_x, angle_y);
    if (debug) {
      Log.d(TAG, "実際に向かう方向のy_angle:[" + y_angle + "]");
      Log.d(TAG, "実際に向かう方向のx_angle:[" + x_angle + "]");
    }

    /* direction設定 */
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
      Log.d(TAG, "結果的に向かう方向"
       + " x:[" + direction[0] + "]:"
       + " y:[" + direction[1] + "]:"
       + " z:[" + direction[2] + "]:");
      Log.d(TAG, "end turnCohesion");
    }
  }

  /**
   * 強制的に水槽の中心へ徐々に向ける
   */
  public void aimAquariumCenter() {
    if (debug) {
      Log.d(TAG, "start aimAquariumCenter ");
    }
    float v_x = (Aquarium.center[0] - getX());
    float v_y = (Aquarium.center[1] - getY());
    float v_z = (Aquarium.center[2] - getZ());
    if (Aquarium.min_x.floatValue() + (getSize() * 1.5f)    < getX() && Aquarium.max_x.floatValue() - (getSize() * 1.5f)    > getX()
    &&  Aquarium.min_y.floatValue() + (getSize()/3f) < getY() && Aquarium.max_y.floatValue() - (getSize()/3f) > getY()) {
      /* Zだけはみ出た */
      v_x = 0.0f;
      v_y = 0.0f;
    }
    else 
    if (Aquarium.min_x.floatValue() + (getSize() * 1.5f) < getX() && Aquarium.max_x.floatValue() - (getSize() * 1.5f) > getX()
    &&  Aquarium.min_z.floatValue() + (getSize() * 1.5f) < getZ() && Aquarium.max_z.floatValue() - (getSize() * 1.5f) > getZ()) {
      /* Yだけはみ出た */
      v_x = 0.0f;
      v_z = 0.0f;
    }
    else 
    if (Aquarium.min_y.floatValue() + (getSize()/3f)      < getY() && Aquarium.max_y.floatValue() - (getSize()/3f)     > getY()
    &&  Aquarium.min_z.floatValue() + (getSize() * 1.5f) < getZ() && Aquarium.max_z.floatValue() - (getSize() * 1.5f) > getZ()) {
      /* Xだけはみ出た */
      v_y = 0.0f;
      v_z = 0.0f;
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
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
    }

    if (angle_y < 0.0f) {
      angle_y = 360f + angle_y;
    }
    angle_y = angle_y % 360f;

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
      Log.d(TAG, "end aimAquariumCenter "
        + "x:[" + direction[0] + "]:"
        + "y:[" + direction[1] + "]:"
        + "z:[" + direction[2] + "]:");
    }
  }
  public void aimSchoolCenter() {
    if (debug) {
      Log.d(TAG, "start aimSchoolCenter ");
    }

    float v_x = 0f;
    float v_y = 0f;
    float v_z = 0f;
    synchronized (mScratch4f_1) {
      /*=======================================================================*/
      /* 向かいたいschoolの方向取得                                            */
      /*=======================================================================*/
      mScratch4f_1[0] = schoolDir[0];
      mScratch4f_1[1] = schoolDir[1];
      mScratch4f_1[2] = schoolDir[2];
      CoordUtil.normalize3fv(mScratch4f_1);
      synchronized (mScratch4f_2) {
        /*=====================================================================*/
        /* 自分から見て、ターゲットの方向を算出                                */
        /*=====================================================================*/
        mScratch4f_2[0] = schoolCenter[0] - getX();
        mScratch4f_2[1] = schoolCenter[1] - getY();
        mScratch4f_2[2] = schoolCenter[2] - getZ();
        CoordUtil.normalize3fv(mScratch4f_2);
        /*=====================================================================*/
        /* ややターゲットに近づきたいので x2                                   */
        /*=====================================================================*/
        mScratch4f_2[0] *= 2f;
        mScratch4f_2[1] *= 2f;
        mScratch4f_2[2] *= 2f;
        /*=====================================================================*/
        /* 足し込む                                                            */
        /*=====================================================================*/
        mScratch4f_1[0] += mScratch4f_2[0];
        mScratch4f_1[1] += mScratch4f_2[1];
        mScratch4f_1[2] += mScratch4f_2[2];
      }
      /*=====================================================================*/
      /* 平均算出                                                            */
      /*=====================================================================*/
      mScratch4f_1[0] /= 3f;
      mScratch4f_1[1] /= 3f;
      mScratch4f_1[2] /= 3f;

      v_x = mScratch4f_1[0];
      v_y = mScratch4f_1[1];
      v_z = mScratch4f_1[2];
    }
    //float v_x = (schoolCenter[0] - getX());
    //float v_y = (schoolCenter[1] - getY());
    //float v_z = (schoolCenter[2] - getZ());

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
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
    }

    if (angle_y < 0.0f) {
      angle_y = 360f + angle_y;
    }
    angle_y = angle_y % 360f;

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
      Log.d(TAG, "end aimSchoolCenter "
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
      Log.d(TAG, "向かいたい方向"
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
      Log.d(TAG, "向かいたい方向のangle_y:[" + angle_y + "]");
      Log.d(TAG, "向かいたい方向のangle_x:[" + angle_x + "]");
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
   * Get shumokuCount.
   *
   * @return shumokuCount as int.
   */
  public int getShumokuCount()
  {
      return shumokuCount;
  }
  
  /**
   * Set shumokuCount.
   *
   * @param shumokuCount the value to set.
   */
  public void setShumokuCount(int shumokuCount)
  {
      this.shumokuCount = shumokuCount;
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

  public void separateBoundingBox() {
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[0] - ((float)separate_dist / 2f), 
                         (float)aabb_org[1] - (float)separate_dist, 
                         (float)aabb_org[2] - (float)separate_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        sep_aabb[0] = mScratch4f_2[0];
        sep_aabb[1] = mScratch4f_2[1];
        sep_aabb[2] = mScratch4f_2[2];
      }
    }
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[3] + ((float)separate_dist / 2f),
                         (float)aabb_org[4] + (float)separate_dist, 
                         (float)aabb_org[5] + (float)separate_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        sep_aabb[3] = mScratch4f_2[0];
        sep_aabb[4] = mScratch4f_2[1];
        sep_aabb[5] = mScratch4f_2[2];
      }
    }
    if (sep_aabb[0] > sep_aabb[3]) {
      double tmp = sep_aabb[0];
      sep_aabb[0] = sep_aabb[3];
      sep_aabb[3] = tmp;
    }
    if (sep_aabb[1] > sep_aabb[4]) {
      double tmp = sep_aabb[1];
      sep_aabb[1] = sep_aabb[4];
      sep_aabb[4] = tmp;
    }
    if (sep_aabb[2] > sep_aabb[5]) {
      double tmp = sep_aabb[2];
      sep_aabb[2] = sep_aabb[5];
      sep_aabb[5] = tmp;
    }
    sep_aabb[0] += getX();
    sep_aabb[1] += getY();
    sep_aabb[2] += getZ();
    sep_aabb[3] += getX();
    sep_aabb[4] += getY();
    sep_aabb[5] += getZ();
  }

  public static boolean crossTestSep(float x, float y, float z) {
    double min_x = sep_aabb[0];
    double min_y = sep_aabb[1];
    double min_z = sep_aabb[2];
    double max_x = sep_aabb[3];
    double max_y = sep_aabb[4];
    double max_z = sep_aabb[5];
    return (   (float)min_x <= x && (float)max_x >= x
            && (float)min_y <= y && (float)max_y >= y
            && (float)min_z <= z && (float)max_z >= z);
  }

  public void alignment1BoundingBox() {
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[0] - (float)alignment_dist1,
                         (float)aabb_org[1] - (float)alignment_dist1, 
                         (float)aabb_org[2] - (float)alignment_dist1, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        al1_aabb[0] = mScratch4f_2[0];
        al1_aabb[1] = mScratch4f_2[1];
        al1_aabb[2] = mScratch4f_2[2];
      }
    }
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[3] + (float)alignment_dist1,
                         (float)aabb_org[4] + (float)alignment_dist1, 
                         (float)aabb_org[5] + (float)alignment_dist1, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        al1_aabb[3] = mScratch4f_2[0];
        al1_aabb[4] = mScratch4f_2[1];
        al1_aabb[5] = mScratch4f_2[2];
      }
    }
    if (al1_aabb[0] > al1_aabb[3]) {
      double tmp = al1_aabb[0];
      al1_aabb[0] = al1_aabb[3];
      al1_aabb[3] = tmp;
    }
    if (al1_aabb[1] > al1_aabb[4]) {
      double tmp = al1_aabb[1];
      al1_aabb[1] = al1_aabb[4];
      al1_aabb[4] = tmp;
    }
    if (al1_aabb[2] > al1_aabb[5]) {
      double tmp = al1_aabb[2];
      al1_aabb[2] = al1_aabb[5];
      al1_aabb[5] = tmp;
    }
    al1_aabb[0] += getX();
    al1_aabb[1] += getY();
    al1_aabb[2] += getZ();
    al1_aabb[3] += getX();
    al1_aabb[4] += getY();
    al1_aabb[5] += getZ();
  }
  public static boolean crossTestAl1(float x, float y, float z) {
    double min_x = al1_aabb[0];
    double min_y = al1_aabb[1];
    double min_z = al1_aabb[2];
    double max_x = al1_aabb[3];
    double max_y = al1_aabb[4];
    double max_z = al1_aabb[5];
    return (   (float)min_x <= x && (float)max_x >= x
            && (float)min_y <= y && (float)max_y >= y
            && (float)min_z <= z && (float)max_z >= z);
  }
  public void alignment2BoundingBox() {
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[0] - (float)alignment_dist2,
                         (float)aabb_org[1] - (float)alignment_dist2, 
                         (float)aabb_org[2] - (float)alignment_dist2, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        al2_aabb[0] = mScratch4f_2[0];
        al2_aabb[1] = mScratch4f_2[1];
        al2_aabb[2] = mScratch4f_2[2];
      }
    }
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[3] + (float)alignment_dist2,
                         (float)aabb_org[4] + (float)alignment_dist2, 
                         (float)aabb_org[5] + (float)alignment_dist2, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        al2_aabb[3] = mScratch4f_2[0];
        al2_aabb[4] = mScratch4f_2[1];
        al2_aabb[5] = mScratch4f_2[2];
      }
    }
    if (al2_aabb[0] > al2_aabb[3]) {
      double tmp = al2_aabb[0];
      al2_aabb[0] = al2_aabb[3];
      al2_aabb[3] = tmp;
    }
    if (al2_aabb[1] > al2_aabb[4]) {
      double tmp = al2_aabb[1];
      al2_aabb[1] = al2_aabb[4];
      al2_aabb[4] = tmp;
    }
    if (al2_aabb[2] > al2_aabb[5]) {
      double tmp = al2_aabb[2];
      al2_aabb[2] = al2_aabb[5];
      al2_aabb[5] = tmp;
    }
    al2_aabb[0] += getX();
    al2_aabb[1] += getY();
    al2_aabb[2] += getZ();
    al2_aabb[3] += getX();
    al2_aabb[4] += getY();
    al2_aabb[5] += getZ();
  }
  public static boolean crossTestAl2(float x, float y, float z) {
    double min_x = al2_aabb[0];
    double min_y = al2_aabb[1];
    double min_z = al2_aabb[2];
    double max_x = al2_aabb[3];
    double max_y = al2_aabb[4];
    double max_z = al2_aabb[5];
    return (   (float)min_x <= x && (float)max_x >= x
            && (float)min_y <= y && (float)max_y >= y
            && (float)min_z <= z && (float)max_z >= z);
  }
  public void schoolBoundingBox() {
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[0] - (float)school_dist,
                         (float)aabb_org[1] - (float)school_dist, 
                         (float)aabb_org[2] - (float)school_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        sch_aabb[0] = mScratch4f_2[0];
        sch_aabb[1] = mScratch4f_2[1];
        sch_aabb[2] = mScratch4f_2[2];
      }
    }
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[3] + (float)school_dist,
                         (float)aabb_org[4] + (float)school_dist, 
                         (float)aabb_org[5] + (float)school_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        sch_aabb[3] = mScratch4f_2[0];
        sch_aabb[4] = mScratch4f_2[1];
        sch_aabb[5] = mScratch4f_2[2];
      }
    }
    if (sch_aabb[0] > sch_aabb[3]) {
      double tmp = sch_aabb[0];
      sch_aabb[0] = sch_aabb[3];
      sch_aabb[3] = tmp;
    }
    if (sch_aabb[1] > sch_aabb[4]) {
      double tmp = sch_aabb[1];
      sch_aabb[1] = sch_aabb[4];
      sch_aabb[4] = tmp;
    }
    if (sch_aabb[2] > sch_aabb[5]) {
      double tmp = sch_aabb[2];
      sch_aabb[2] = sch_aabb[5];
      sch_aabb[5] = tmp;
    }
    sch_aabb[0] += getX();
    sch_aabb[1] += getY();
    sch_aabb[2] += getZ();
    sch_aabb[3] += getX();
    sch_aabb[4] += getY();
    sch_aabb[5] += getZ();
  }
  public static boolean crossTestSch(float x, float y, float z) {
    double min_x = sch_aabb[0];
    double min_y = sch_aabb[1];
    double min_z = sch_aabb[2];
    double max_x = sch_aabb[3];
    double max_y = sch_aabb[4];
    double max_z = sch_aabb[5];
    return (   (float)min_x <= x && (float)max_x >= x
            && (float)min_y <= y && (float)max_y >= y
            && (float)min_z <= z && (float)max_z >= z);
  }
  public void cohesionBoundingBox() {
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[0] - (float)cohesion_dist,
                         (float)aabb_org[1] - (float)cohesion_dist, 
                         (float)aabb_org[2] - (float)cohesion_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        coh_aabb[0] = mScratch4f_2[0];
        coh_aabb[1] = mScratch4f_2[1];
        coh_aabb[2] = mScratch4f_2[2];
      }
    }
    coordUtil.setMatrixRotateZ(x_angle);
    synchronized (mScratch4f_1) {
      synchronized (mScratch4f_2) {
        coordUtil.affine((float)aabb_org[3] + (float)cohesion_dist,
                         (float)aabb_org[4] + (float)cohesion_dist, 
                         (float)aabb_org[5] + (float)cohesion_dist, 
                         mScratch4f_1);
        coordUtil.setMatrixRotateY(y_angle);
        coordUtil.affine(mScratch4f_1[0],mScratch4f_1[1], mScratch4f_1[2], mScratch4f_2);
        coh_aabb[3] = mScratch4f_2[0];
        coh_aabb[4] = mScratch4f_2[1];
        coh_aabb[5] = mScratch4f_2[2];
      }
    }
    if (coh_aabb[0] > coh_aabb[3]) {
      double tmp = coh_aabb[0];
      coh_aabb[0] = coh_aabb[3];
      coh_aabb[3] = tmp;
    }
    if (coh_aabb[1] > coh_aabb[4]) {
      double tmp = coh_aabb[1];
      coh_aabb[1] = coh_aabb[4];
      coh_aabb[4] = tmp;
    }
    if (coh_aabb[2] > coh_aabb[5]) {
      double tmp = coh_aabb[2];
      coh_aabb[2] = coh_aabb[5];
      coh_aabb[5] = tmp;
    }
    coh_aabb[0] += getX();
    coh_aabb[1] += getY();
    coh_aabb[2] += getZ();
    coh_aabb[3] += getX();
    coh_aabb[4] += getY();
    coh_aabb[5] += getZ();
  }
  public static boolean crossTestCoh(float x, float y, float z) {
    double min_x = coh_aabb[0];
    double min_y = coh_aabb[1];
    double min_z = coh_aabb[2];
    double max_x = coh_aabb[3];
    double max_y = coh_aabb[4];
    double max_z = coh_aabb[5];
    return (   (float)min_x <= x && (float)max_x >= x
            && (float)min_y <= y && (float)max_y >= y
            && (float)min_z <= z && (float)max_z >= z);
  }
  
  /**
   * Get turnDirection.
   *
   * @return turnDirection as TURN_DIRECTION.
   */
  public TURN_DIRECTION getTurnDirection()
  {
      return turnDirection;
  }
  
  /**
   * Set turnDirection.
   *
   * @param turnDirection the value to set.
   */
  public void setTurnDirection(TURN_DIRECTION turnDirection) {
    this.turnDirection = turnDirection;
  }
  
  /**
   * Get species.
   *
   * @return species as Model[].
   */
  public Model[] getSpecies() {
    return species;
  }
  
  /**
   * Get species element at specified index.
   *
   * @param index the index.
   * @return species at index as Model.
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
}
