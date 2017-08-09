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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

public class IwashiData {
  private static int[] mScratch128i = new int[128];
  public static final FloatBuffer[] mVertexBuffer = new FloatBuffer[36];
  public static void init() {
    for (int ii=0; ii<mVertexBuffer.length; ii++) {
      int retry = 0;
      while(true) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        if (vbb.capacity() != vertices.length * 4) {
          // XXX: FIX ME. ByteBuffer.allocateDirect failed sometime.
          if (++retry > 3) {
            throw new RuntimeException("Memory Allocate Exception");
          }
          System.gc();
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
          }
          continue;
        }
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer[ii] = vbb.asFloatBuffer();
        break;
      }
    }
    mVertexBuffer[0].position(0);
    mVertexBuffer[0].put(vertices);
    mVertexBuffer[0].position(0);
    for (int ii=1; ii<mVertexBuffer.length; ii++) {
      float[] ret = createAnimate(ii);
      mVertexBuffer[ii].position(0);
      mVertexBuffer[ii].put(ret);
      mVertexBuffer[ii].position(0);
      ret = null;
    }
  }
  private static float getMoveWidth(float x) {
    /*=======================================================================*/
    /* z = 1/3 * x^2 の2次関数から算出                                       */
    /*=======================================================================*/
    float xt = x / Iwashi.scale + Iwashi.center_xyz[0];
    return xt * xt / 20.0f - 0.4f;
  }
  private static float[] createAnimate(int no) {
    float[] result = new float[vertices.length];
    System.arraycopy(vertices, 0, result, 0, vertices.length); 
    float s = (float)Math.sin((double)(no*10)) * Iwashi.scale;
     
    //303 101 {4.725803, 1.603915, -0.000000}
    //309 103 {4.725803, 1.603915, -0.000000}
    synchronized (mScratch128i) {
      mScratch128i[0] = 101;
      mScratch128i[1] = 103;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }

    //300 100 {4.734376, 1.502248, -0.009085}
    //312 104 {4.727424, 1.502259, 0.009085}
    //1290 430 {4.727424, 1.502259, 0.009085}
    //1317 439 {4.734376, 1.502248, -0.009085}
    synchronized (mScratch128i) {
      mScratch128i[0] = 100;
      mScratch128i[1] = 104;
      mScratch128i[2] = 430;
      mScratch128i[3] = 439;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<4; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //IwashiData.vertices[2+3*100] = IwashiData.vertices[2+3*100] + (1.0f * s);
    //IwashiData.vertices[2+3*104] = IwashiData.vertices[2+3*104] + (1.0f * s);
    //IwashiData.vertices[2+3*430] = IwashiData.vertices[2+3*430] + (1.0f * s);
    //IwashiData.vertices[2+3*439] = IwashiData.vertices[2+3*439] + (1.0f * s);

    //318 106 {4.497553, 1.130905, 0.009254}
    //1293 431 {4.497553, 1.130905, 0.009254}
    //1299 433 {4.497553, 1.130905, 0.009254}
    synchronized (mScratch128i) {
      mScratch128i[0] = 106;
      mScratch128i[1] = 431;
      mScratch128i[2] = 433;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<3; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }

    // 096 032 {3.943874, 0.549283, 0.006373}
    // 102 034 {3.943874, 0.549283, 0.006373}
    // 132 044 {3.931480, 0.549297, -0.006373}
    // 138 046 {3.931480, 0.549297, -0.006373}
    // 285 095 {3.943874, 0.549283, 0.006373}
    // 288 096 {3.943874, 0.549283, 0.006373}
    // 321 107 {3.931480, 0.549297, -0.006373}
    // 324 108 {3.931480, 0.549297, -0.006373}
    synchronized (mScratch128i) {
      //int idx[] = { 32,34,44,46,95,96,107,108,};
      mScratch128i[0] = 32;
      mScratch128i[1] = 34;
      mScratch128i[2] = 44;
      mScratch128i[3] = 46;
      mScratch128i[4] = 95;
      mScratch128i[5] = 96;
      mScratch128i[6] = 107;
      mScratch128i[7] = 108;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
 
    // 264 088 {4.587202, 0.163779, 0.009247}
    // 276 092 {4.597796, 0.163766, -0.009247}
    // 282 094 {4.597796, 0.163766, -0.009247}
    // 327 109 {4.587202, 0.163779, 0.009247}
    synchronized (mScratch128i) {
      mScratch128i[0] = 88;
      mScratch128i[1] = 92;
      mScratch128i[2] = 94;
      mScratch128i[3] = 109;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<4; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    // 267 089 {4.865566, -0.206893, 0.009037}
    // 273 091 {4.871437, -0.206896, -0.009037}
    //1329 443 {4.871437, -0.206896, -0.009037}
    //1335 445 {4.871437, -0.206896, -0.009037}
    //1344 448 {4.865566, -0.206893, 0.009037}
    //1350 450 {4.865566, -0.206893, 0.009037}
    synchronized (mScratch128i) {
      //int idx[] = { 89,91,443,445,448,450,};
      mScratch128i[0] = 89;
      mScratch128i[1] = 91;
      mScratch128i[2] = 443;
      mScratch128i[3] = 445;
      mScratch128i[4] = 448;
      mScratch128i[5] = 450;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //291 097 {4.508326, 1.130889, -0.009254}
    //1308 436 {4.508326, 1.130889, -0.009254}
    //1314 438 {4.508326, 1.130889, -0.009254}
    synchronized (mScratch128i) {
      //int idx[] = { 97,436,438,};
      mScratch128i[0] = 97;
      mScratch128i[1] = 436;
      mScratch128i[2] = 438;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<3; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1326 442 {4.868408, -0.319613, -0.000000}
    //1353 451 {4.868408, -0.319613, -0.000000}
    synchronized (mScratch128i) {
      //int idx[] = { 442,451,};
      mScratch128i[0] = 442;
      mScratch128i[1] = 451;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    // 231 077 {4.189324, -0.027536, -0.000000}
    // 237 079 {4.189324, -0.027536, -0.000000}
    //1323 441 {4.189324, -0.027536, -0.000000}
    //1332 444 {4.189324, -0.027536, -0.000000}
    //1347 449 {4.189324, -0.027536, -0.000000}
    //1356 452 {4.189324, -0.027536, -0.000000}
    synchronized (mScratch128i) {
      //int idx[] = { 77,79,441,444,449,452,};
      mScratch128i[0] = 77;
      mScratch128i[1] = 79;
      mScratch128i[2] = 441;
      mScratch128i[3] = 444;
      mScratch128i[4] = 449;
      mScratch128i[5] = 452;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //084 028 {3.994344, 0.212614, -0.011905}
    //093 031 {3.994344, 0.212614, -0.011905}
    //141 047 {3.985378, 0.212621, -0.040541}
    //150 050 {3.985378, 0.212621, -0.040541}
    //228 076 {3.985378, 0.212621, -0.040541}
    //240 080 {3.994344, 0.212614, -0.011905}
    //261 087 {3.985378, 0.212621, -0.040541}
    //270 090 {3.994344, 0.212614, -0.011905}
    //279 093 {3.994344, 0.212614, -0.011905}
    //330 110 {3.985378, 0.212621, -0.040541}
    //1338 446 {3.994344, 0.212614, -0.011905}
    //1341 447 {3.985378, 0.212621, -0.040541}
    synchronized (mScratch128i) {
      //int idx[] = { 28,31,47,50,76,80,87,90,93,110,446,447,};
      mScratch128i[0] = 28;
      mScratch128i[1] = 31;
      mScratch128i[2] = 47;
      mScratch128i[3] = 50;
      mScratch128i[4] = 76;
      mScratch128i[5] = 80;
      mScratch128i[6] = 87;
      mScratch128i[7] = 90;
      mScratch128i[8] = 93;
      mScratch128i[9] = 110;
      mScratch128i[10] = 446;
      mScratch128i[11] = 447;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<12; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //105 035 {4.001855, 0.959487, -0.012866}
    //111 037 {4.001855, 0.959487, -0.012866}
    //246 082 {4.001855, 0.959487, -0.012866}
    //294 098 {4.001855, 0.959487, -0.012866}
    //1305 435 {4.001855, 0.959487, -0.012866}
    // XXXX
    //120 040 {3.992240, 0.959496, -0.039771}
    //129 043 {3.992240, 0.959496, -0.039771}
    //258 086 {3.992240, 0.959496, -0.039771}
    //315 105 {3.992240, 0.959496, -0.039771}
    //1302 434 {3.992240, 0.959496, -0.039771}
    synchronized (mScratch128i) {
      //int idx[] = { 35,37,82,98,435,40,43,86,105,434,};
      mScratch128i[0] = 35;
      mScratch128i[1] = 37;
      mScratch128i[2] = 82;
      mScratch128i[3] = 98;
      mScratch128i[4] = 435;
      mScratch128i[5] = 40;
      mScratch128i[6] = 43;
      mScratch128i[7] = 86;
      mScratch128i[8] = 105;
      mScratch128i[9] = 434;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<10; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    // 249 083 {4.250497, 1.351480, -0.030413}
    // 255 085 {4.250497, 1.351480, -0.030413}
    // 297 099 {4.250497, 1.351480, -0.030413}
    // 306 102 {4.250497, 1.351480, -0.030413}
    //1287 429 {4.250497, 1.351480, -0.030413}
    //1296 432 {4.250497, 1.351480, -0.030413}
    //1311 437 {4.250497, 1.351480, -0.030413}
    //1320 440 {4.250497, 1.351480, -0.030413}
    synchronized (mScratch128i) {
      //int idx[] = { 83,85,99,102,429,432,437,440,};
      mScratch128i[0] = 83;
      mScratch128i[1] = 85;
      mScratch128i[2] = 99;
      mScratch128i[3] = 102;
      mScratch128i[4] = 429;
      mScratch128i[5] = 432;
      mScratch128i[6] = 437;
      mScratch128i[7] = 440;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //114 038 {3.393267, 0.860405, -0.028042}
    //117 039 {3.393267, 0.860405, -0.028042}
    //243 081 {3.393267, 0.860405, -0.028042}
    //252 084 {3.393267, 0.860405, -0.028042}
    //705 235 {3.393267, 0.860405, -0.028042}
    //714 238 {3.393267, 0.860405, -0.028042}
    synchronized (mScratch128i) {
      //int idx[] = { 38,39,81,84,235,238, };
      mScratch128i[0] = 38;
      mScratch128i[1] = 39;
      mScratch128i[2] = 81;
      mScratch128i[3] = 84;
      mScratch128i[4] = 235;
      mScratch128i[5] = 238;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //081 027 {3.465865, 0.220323, -0.023851}
    //144 048 {3.465865, 0.220323, -0.023851}
    //225 075 {3.465865, 0.220323, -0.023851}
    //234 078 {3.465865, 0.220323, -0.023851}
    //660 220 {3.465865, 0.220323, -0.023851}
    //690 230 {3.465865, 0.220323, -0.023851}
    //696 232 {3.465865, 0.220323, -0.023851}
    //720 240 {3.465865, 0.220323, -0.023851}
    synchronized (mScratch128i) {
      //int idx[] = { 27,48,75,78,220,230,232,240,};
      mScratch128i[0] = 27;
      mScratch128i[1] = 48;
      mScratch128i[2] = 75;
      mScratch128i[3] = 78;
      mScratch128i[4] = 220;
      mScratch128i[5] = 230;
      mScratch128i[6] = 232;
      mScratch128i[7] = 240;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //663 221 {3.128526, 0.180488, -0.023306}
    //669 223 {3.128526, 0.180488, -0.023306}
    //678 226 {3.128526, 0.180488, -0.023306}
    //687 229 {3.128526, 0.180488, -0.023306}
    synchronized (mScratch128i) {
      //int idx[] = { 221,223,226,229,};
      mScratch128i[0] = 221;
      mScratch128i[1] = 223;
      mScratch128i[2] = 226;
      mScratch128i[3] = 229;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<4; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //087 029 {2.908598, 0.545923, 0.068958}
    //090 030 {2.908598, 0.545923, 0.068958}
    //099 033 {2.908598, 0.545923, 0.068958}
    //108 036 {2.908598, 0.545923, 0.068958}
    //123 041 {2.897367, 0.545929, -0.111540}
    //126 042 {2.897367, 0.545929, -0.111540}
    //135 045 {2.897367, 0.545929, -0.111540}
    //147 049 {2.897367, 0.545929, -0.111540}
    //177 059 {2.908598, 0.545923, 0.068958}
    //183 061 {2.908598, 0.545923, 0.068958}
    //192 064 {2.908598, 0.545923, 0.068958}
    //201 067 {2.897367, 0.545929, -0.111540}
    //210 070 {2.897367, 0.545929, -0.111540}
    //222 074 {2.897367, 0.545929, -0.111540}
    //627 209 {2.908598, 0.545923, 0.068958}
    //633 211 {2.908598, 0.545923, 0.068958}
    //645 215 {2.897367, 0.545929, -0.111540}
    //654 218 {2.897367, 0.545929, -0.111540}
    //699 233 {2.908598, 0.545923, 0.068958}
    //702 234 {2.908598, 0.545923, 0.068958}
    //717 239 {2.897367, 0.545929, -0.111540}
    //726 242 {2.897367, 0.545929, -0.111540}
    //1371 457 {2.897367, 0.545929, -0.111540}
    //1380 460 {2.908598, 0.545923, 0.068958}
    synchronized (mScratch128i) {
      //int idx[] = { 29,30,33,36,41,42,45,49,59,61,64,67,70,74,209,211,215,218,233,234,239,242,457,460,};
      mScratch128i[0] = 29;
      mScratch128i[1] = 30;
      mScratch128i[2] = 33;
      mScratch128i[3] = 36;
      mScratch128i[4] = 41;
      mScratch128i[5] = 42;
      mScratch128i[6] = 45;
      mScratch128i[7] = 49;
      mScratch128i[8] = 59;
      mScratch128i[9] = 61;
      mScratch128i[10] = 64;
      mScratch128i[11] = 67;
      mScratch128i[12] = 70;
      mScratch128i[13] = 74;
      mScratch128i[14] = 209;
      mScratch128i[15] = 211;
      mScratch128i[16] = 215;
      mScratch128i[17] = 218;
      mScratch128i[18] = 233;
      mScratch128i[19] = 234;
      mScratch128i[20] = 239;
      mScratch128i[21] = 242;
      mScratch128i[22] = 457;
      mScratch128i[23] = 460;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<24; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //672 224 {2.755704, 0.041151, -0.025086}
    //675 225 {2.755704, 0.041151, -0.025086}
    //1182 394 {2.755704, 0.041151, -0.025086}
    //1188 396 {2.755704, 0.041151, -0.025086}
    //1203 401 {2.755704, 0.041151, -0.025086}
    //1209 403 {2.755704, 0.041151, -0.025086}
    synchronized (mScratch128i) {
      //int idx[] = { 224,225,394,396,401,403,};
      mScratch128i[0] = 224;
      mScratch128i[1] = 225;
      mScratch128i[2] = 394;
      mScratch128i[3] = 396;
      mScratch128i[4] = 401;
      mScratch128i[5] = 403;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    // 606 202 {2.601744, 0.072730, -0.082255}
    // 615 205 {2.608089, 0.072728, 0.042083}
    // 624 208 {2.608089, 0.072728, 0.042083}
    // 648 216 {2.601744, 0.072730, -0.082255}
    // 657 219 {2.601744, 0.072730, -0.082255}
    // 666 222 {2.601744, 0.072730, -0.082255}
    // 681 227 {2.608089, 0.072728, 0.042083}
    // 684 228 {2.608089, 0.072728, 0.042083}
    // 693 231 {2.608089, 0.072728, 0.042083}
    // 723 241 {2.601744, 0.072730, -0.082255}
    //1191 397 {2.608089, 0.072728, 0.042083}
    //1200 400 {2.601744, 0.072730, -0.082255}
    synchronized (mScratch128i) {
      //int idx[] = { 202,205,208,216,219,222,227,228,231,241,397,400,};
      mScratch128i[0] = 202;
      mScratch128i[1] = 205;
      mScratch128i[2] = 208;
      mScratch128i[3] = 216;
      mScratch128i[4] = 219;
      mScratch128i[5] = 222;
      mScratch128i[6] = 227;
      mScratch128i[7] = 228;
      mScratch128i[8] = 231;
      mScratch128i[9] = 241;
      mScratch128i[10] = 397;
      mScratch128i[11] = 400;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<12; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //636 212 {2.606399, 0.965839, -0.022280}
    //642 214 {2.606399, 0.965839, -0.022280}
    //708 236 {2.606399, 0.965839, -0.022280}
    //711 237 {2.606399, 0.965839, -0.022280}
    synchronized (mScratch128i) {
      //int idx[] = { 212,214,236,237,};
      mScratch128i[0] = 212;
      mScratch128i[1] = 214;
      mScratch128i[2] = 236;
      mScratch128i[3] = 237;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<4; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //174 058 {1.993230, -0.000729, 0.124182}
    //216 072 {1.985328, -0.000726, -0.159362}
    //561 187 {1.990646, 1.132275, -0.019784}
    //570 190 {1.990646, 1.132275, -0.019784}
    //603 201 {1.985328, -0.000726, -0.159362}
    //618 206 {1.993230, -0.000729, 0.124182}
    //621 207 {1.993230, -0.000729, 0.124182}
    //630 210 {1.990646, 1.132275, -0.019784}
    //639 213 {1.990646, 1.132275, -0.019784}
    //651 217 {1.985328, -0.000726, -0.159362}
    //1179 393 {1.954150, -0.416138, -0.022541}
    //1212 404 {1.954150, -0.416138, -0.022541}
    //1362 454 {1.990646, 1.132275, -0.019784}
    //1368 456 {1.990646, 1.132275, -0.019784}
    //1383 461 {1.990646, 1.132275, -0.019784}
    //1389 463 {1.990646, 1.132275, -0.019784}
    //1401 467 {1.993230, -0.000729, 0.124182}
    //1407 469 {1.993230, -0.000729, 0.124182}
    //1416 472 {1.985328, -0.000726, -0.159362}
    //1422 474 {1.985328, -0.000726, -0.159362}
    synchronized (mScratch128i) {
      //int idx[] = { 58,72,187,190,201,206,207,210,213,217,393,404,454,456,461,463,467,469,472,474, };
      mScratch128i[0]  = 58;
      mScratch128i[1]  = 72;
      mScratch128i[2]  = 187;
      mScratch128i[3]  = 190;
      mScratch128i[4]  = 201;
      mScratch128i[5]  = 206;
      mScratch128i[6]  = 207;
      mScratch128i[7]  = 210;
      mScratch128i[8]  = 213;
      mScratch128i[9]  = 217;
      mScratch128i[10] = 393;
      mScratch128i[11] = 404;
      mScratch128i[12] = 454;
      mScratch128i[13] = 456;
      mScratch128i[14] = 461;
      mScratch128i[15] = 463;
      mScratch128i[16] = 467;
      mScratch128i[17] = 469;
      mScratch128i[18] = 472;
      mScratch128i[19] = 474;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<20; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //609 203 {1.841455, -0.150632, -0.019362}
    //612 204 {1.841455, -0.150632, -0.019362}
    //1185 395 {1.841455, -0.150632, -0.019362}
    //1194 398 {1.841455, -0.150632, -0.019362}
    //1197 399 {1.841455, -0.150632, -0.019362}
    //1206 402 {1.841455, -0.150632, -0.019362}
    //1398 466 {1.841455, -0.150632, -0.019362}
    //1425 475 {1.841455, -0.150632, -0.019362}
    synchronized (mScratch128i) {
      //int idx[] = { 203,204,395,398,399,402,466,475, }; 
      mScratch128i[0] = 203;
      mScratch128i[1] = 204;
      mScratch128i[2] = 395;
      mScratch128i[3] = 398;
      mScratch128i[4] = 399;
      mScratch128i[5] = 402;
      mScratch128i[6] = 466;
      mScratch128i[7] = 475;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1218 406 {0.956889, -0.352683, -0.017794}
    //1224 408 {0.956889, -0.352683, -0.017794}
    //1239 413 {0.956889, -0.352683, -0.017794}
    //1245 415 {0.956889, -0.352683, -0.017794}
    //1395 465 {0.956889, -0.352683, -0.017794}
    //1404 468 {0.956889, -0.352683, -0.017794}
    //1419 473 {0.956889, -0.352683, -0.017794}
    //1428 476 {0.956889, -0.352683, -0.017794}
    synchronized (mScratch128i) {
      //int idx[] = { 406,408,413,415,465,468,473,476, };
      mScratch128i[0] = 406;
      mScratch128i[1] = 408;
      mScratch128i[2] = 413;
      mScratch128i[3] = 415;
      mScratch128i[4] = 465;
      mScratch128i[5] = 468;
      mScratch128i[6] = 473;
      mScratch128i[7] = 476;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //171 057 {0.581339, -0.149219, 0.291680}
    //180 060 {0.581339, -0.149219, 0.291680}
    //186 062 {0.583216, 0.232926, 0.394389}
    //189 063 {0.583216, 0.232926, 0.394389}
    //195 065 {0.583626, 0.694177, 0.392306}
    //198 066 {0.571708, 0.694188, -0.416034}
    //204 068 {0.571238, 0.232938, -0.418118}
    //207 069 {0.571238, 0.232938, -0.418118}
    //213 071 {0.572380, -0.149212, -0.315408}
    //219 073 {0.572380, -0.149212, -0.315408}
    //558 186 {0.581885, 1.091331, 0.247236}
    //573 191 {0.574212, 1.091338, -0.270963}
    //576 192 {0.581885, 1.091331, 0.247236}
    //600 200 {0.574212, 1.091338, -0.270963}
    //930 310 {0.571238, 0.232938, -0.418118}
    //933 311 {0.572380, -0.149212, -0.315408}
    //939 313 {0.572380, -0.149212, -0.315408}
    //948 316 {0.571708, 0.694188, -0.416034}
    //951 317 {0.571238, 0.232938, -0.418118}
    //957 319 {0.571238, 0.232938, -0.418118}
    //966 322 {0.574212, 1.091338, -0.270963}
    //969 323 {0.571708, 0.694188, -0.416034}
    //975 325 {0.571708, 0.694188, -0.416034}
    //993 331 {0.574212, 1.091338, -0.270963}
    //1002 334 {0.581885, 1.091331, 0.247236}
    //1020 340 {0.583626, 0.694177, 0.392306}
    //1026 342 {0.583626, 0.694177, 0.392306}
    //1029 343 {0.581885, 1.091331, 0.247236}
    //1038 346 {0.583216, 0.232926, 0.394389}
    //1044 348 {0.583216, 0.232926, 0.394389}
    //1047 349 {0.583626, 0.694177, 0.392306}
    //1056 352 {0.581339, -0.149219, 0.291680}
    //1062 354 {0.581339, -0.149219, 0.291680}
    //1065 355 {0.583216, 0.232926, 0.394389}
    //1077 359 {0.581339, -0.149219, 0.291680}
    //1083 361 {0.581339, -0.149219, 0.291680}
    //1164 388 {0.572380, -0.149212, -0.315408}
    //1170 390 {0.572380, -0.149212, -0.315408}
    //1227 409 {0.581339, -0.149219, 0.291680}
    //1236 412 {0.572380, -0.149212, -0.315408}
    //1359 453 {0.574212, 1.091338, -0.270963}
    //1365 455 {0.571708, 0.694188, -0.416034}
    //1374 458 {0.571708, 0.694188, -0.416034}
    //1377 459 {0.583626, 0.694177, 0.392306}
    //1386 462 {0.583626, 0.694177, 0.392306}
    //1392 464 {0.581885, 1.091331, 0.247236}
    //1410 470 {0.581339, -0.149219, 0.291680}
    //1413 471 {0.572380, -0.149212, -0.315408}
    synchronized (mScratch128i) {
//      int idx[] = { 57, 60, 62, 63, 65, 66, 68, 69, 71, 73, 186, 191, 192, 200, 310, 
//                    311, 313, 316, 317, 319, 322, 323, 325, 331, 334, 340, 342, 343, 
//                    346, 348, 349, 352, 354, 355, 359, 361, 388, 390, 409, 412, 453, 
//                    455, 458, 459, 462, 464, 470, 471, };
      mScratch128i[0] = 57;
      mScratch128i[1] = 60;
      mScratch128i[2] = 62;
      mScratch128i[3] = 63;
      mScratch128i[4] = 65;
      mScratch128i[5] = 66;
      mScratch128i[6] = 68;
      mScratch128i[7] = 69;
      mScratch128i[8] = 71;
      mScratch128i[9] = 73;
      mScratch128i[10] = 186;
      mScratch128i[11] = 191;
      mScratch128i[12] = 192;
      mScratch128i[13] = 200;
      mScratch128i[14] = 310;
      mScratch128i[15] = 311;
      mScratch128i[16] = 313;
      mScratch128i[17] = 316;
      mScratch128i[18] = 317;
      mScratch128i[19] = 319;
      mScratch128i[20] = 322;
      mScratch128i[21] = 323;
      mScratch128i[22] = 325;
      mScratch128i[23] = 331;
      mScratch128i[24] = 334;
      mScratch128i[25] = 340;
      mScratch128i[26] = 342;
      mScratch128i[27] = 343;
      mScratch128i[28] = 346;
      mScratch128i[29] = 348;
      mScratch128i[30] = 349;
      mScratch128i[31] = 352;
      mScratch128i[32] = 354;
      mScratch128i[33] = 355;
      mScratch128i[34] = 359;
      mScratch128i[35] = 361;
      mScratch128i[36] = 388;
      mScratch128i[37] = 390;
      mScratch128i[38] = 409;
      mScratch128i[39] = 412;
      mScratch128i[40] = 453;
      mScratch128i[41] = 455;
      mScratch128i[42] = 458;
      mScratch128i[43] = 459;
      mScratch128i[44] = 462;
      mScratch128i[45] = 464;
      mScratch128i[46] = 470;
      mScratch128i[47] = 471;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<48; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1095 365 {0.389382, -0.656846, 0.166823}
    //1101 367 {0.389382, -0.656846, 0.166823}
    //1149 383 {0.384593, -0.656843, -0.186195}
    //1155 385 {0.384593, -0.656843, -0.186195}
    synchronized (mScratch128i) {
      //int idx[] = { 365,367,383,385,};
      mScratch128i[0] = 365;
      mScratch128i[1] = 367;
      mScratch128i[2] = 383;
      mScratch128i[3] = 385;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<4; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //564 188 {0.354908, 1.325953, -0.010446}
    //567 189 {0.354908, 1.325953, -0.010446}
    //579 193 {0.354908, 1.325953, -0.010446}
    //588 196 {0.354908, 1.325953, -0.010446}
    //597 199 {0.354908, 1.325953, -0.010446}
    synchronized (mScratch128i) {
      //int idx[] = { 188,189,193,196,199,};
      mScratch128i[0] = 188;
      mScratch128i[1] = 189;
      mScratch128i[2] = 193;
      mScratch128i[3] = 196;
      mScratch128i[4] = 199;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<5; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //591 197 {0.288338, 1.460179, -0.019098}
    synchronized (mScratch128i) {
      //int idx[] = { 197, };
      mScratch128i[0] = 197;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<1; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1074 358 {0.199635, -0.364357, 0.152904}
    //1092 364 {0.199635, -0.364357, 0.152904}
    //1119 373 {0.199635, -0.364357, 0.152904}
    //1128 376 {0.193223, -0.364353, -0.172992}
    //1146 382 {0.193223, -0.364353, -0.172992}
    //1173 391 {0.193223, -0.364353, -0.172992}
    //1221 407 {0.199635, -0.364357, 0.152904}
    //1230 410 {0.199635, -0.364357, 0.152904}
    //1233 411 {0.193223, -0.364353, -0.172992}
    //1242 414 {0.193223, -0.364353, -0.172992}
    synchronized (mScratch128i) {
      //int idx[] = { 358,364,373,376,382,391,407,410,411,414, };
      mScratch128i[0] = 358;
      mScratch128i[1] = 364;
      mScratch128i[2] = 373;
      mScratch128i[3] = 376;
      mScratch128i[4] = 382;
      mScratch128i[5] = 391;
      mScratch128i[6] = 407;
      mScratch128i[7] = 410;
      mScratch128i[8] = 411;
      mScratch128i[9] = 414;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<10; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1110 370 {0.157074, -0.503665, -0.017842}
    //1116 372 {0.157074, -0.503665, -0.017842}
    //1131 377 {0.157074, -0.503665, -0.017842}
    //1137 379 {0.157074, -0.503665, -0.017842}
    //1215 405 {0.157074, -0.503665, -0.017842}
    //1248 416 {0.157074, -0.503665, -0.017842}
    synchronized (mScratch128i) {
      //int idx[] = { 370,372,377,379,405,416, };
      mScratch128i[0] = 370;
      mScratch128i[1] = 372;
      mScratch128i[2] = 377;
      mScratch128i[3] = 379;
      mScratch128i[4] = 405;
      mScratch128i[5] = 416;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii; 
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //1104 368 {0.139649, -0.683434, 0.172252}
    //1158 386 {0.134860, -0.683430, -0.190388}
    synchronized (mScratch128i) {
      //int idx[] = { 368,386,};
      mScratch128i[0] = 368;
      mScratch128i[1] = 386;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //552 184 {-0.187596, 1.774881, -0.017535}
    //735 245 {-0.187596, 1.774881, -0.017535}
    synchronized (mScratch128i) {
      //int idx[] = { 184,245,};
      mScratch128i[0] = 184;
      mScratch128i[1] = 245;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //516 172 {-0.267770, -0.458273, -0.190623}
    //525 175 {-0.267770, -0.458273, -0.190623}
    //528 176 {-0.265469, -0.539220, -0.017226}
    //534 178 {-0.265469, -0.539220, -0.017226}
    //537 179 {-0.263027, -0.458277, 0.173962}
    //543 181 {-0.263027, -0.458277, 0.173962}
    //1071 357 {-0.263027, -0.458277, 0.173962}
    //1080 360 {-0.263027, -0.458277, 0.173962}
    //1089 363 {-0.263027, -0.458277, 0.173962}
    //1098 366 {-0.263027, -0.458277, 0.173962}
    //1107 369 {-0.265469, -0.539220, -0.017226}
    //1113 371 {-0.263027, -0.458277, 0.173962}
    //1122 374 {-0.263027, -0.458277, 0.173962}
    //1125 375 {-0.267770, -0.458273, -0.190623}
    //1134 378 {-0.267770, -0.458273, -0.190623}
    //1140 380 {-0.265469, -0.539220, -0.017226}
    //1143 381 {-0.267770, -0.458273, -0.190623}
    //1152 384 {-0.267770, -0.458273, -0.190623}
    //1167 389 {-0.267770, -0.458273, -0.190623}
    //1176 392 {-0.267770, -0.458273, -0.190623}
    synchronized (mScratch128i) {
      //int idx[] = { 172,175,176,178,179,181,357,360,363,366,369,371,374,375,378,380,381,384,389,392, };
      mScratch128i[0] = 172;
      mScratch128i[1] = 175;
      mScratch128i[2] = 176;
      mScratch128i[3] = 178;
      mScratch128i[4] = 179;
      mScratch128i[5] = 181;
      mScratch128i[6] = 357;
      mScratch128i[7] = 360;
      mScratch128i[8] = 363;
      mScratch128i[9] = 366;
      mScratch128i[10] = 369;
      mScratch128i[11] = 371;
      mScratch128i[12] = 374;
      mScratch128i[13] = 375;
      mScratch128i[14] = 378;
      mScratch128i[15] = 380;
      mScratch128i[16] = 381;
      mScratch128i[17] = 384;
      mScratch128i[18] = 389;
      mScratch128i[19] = 392;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<20; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //555 185 {-0.302373, 2.225360, -0.016608}
    synchronized (mScratch128i) {
      //int idx[] = { 185, };
      mScratch128i[0] = 185;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<1; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //156 052 {-1.178311, 1.362515, -0.010047}
    //165 055 {-1.178311, 1.362515, -0.010047}
    //549 183 {-1.178311, 1.362515, -0.010047}
    //729 243 {-1.178311, 1.362515, -0.010047}
    //981 327 {-1.178311, 1.362515, -0.010047}
    //1014 338 {-1.178311, 1.362515, -0.010047}
    synchronized (mScratch128i) {
      //int idx[] = { 52,55,183,243,327,338, };
      mScratch128i[0] = 52;
      mScratch128i[1] = 55;
      mScratch128i[2] = 183;
      mScratch128i[3] = 243;
      mScratch128i[4] = 327;
      mScratch128i[5] = 338;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //153 051 {-1.440711, 1.066319, 0.368437}
    //168 056 {-1.449087, 1.066327, -0.375763}
    //513 171 {-1.450822, -0.196619, -0.432703}
    //546 182 {-1.441195, -0.196627, 0.425376}
    //741 247 {-1.450822, -0.196619, -0.432703}
    //771 257 {-1.441195, -0.196627, 0.425376}
    //777 259 {-1.439693, 0.174198, 0.532284}
    //786 262 {-1.439693, 0.174198, 0.532284}
    //789 263 {-1.439283, 0.675618, 0.529550}
    //795 265 {-1.440711, 1.066319, 0.368437}
    //804 268 {-1.449087, 1.066327, -0.375763}
    //813 271 {-1.451202, 0.675629, -0.536876}
    //816 272 {-1.451672, 0.174209, -0.539610}
    //822 274 {-1.451672, 0.174209, -0.539610}
    //858 286 {-1.451672, 0.174209, -0.539610}
    //864 288 {-1.451672, 0.174209, -0.539610}
    //867 289 {-1.450822, -0.196619, -0.432703}
    //876 292 {-1.449087, 1.066327, -0.375763}
    //879 293 {-1.451202, 0.675629, -0.536876}
    //885 295 {-1.451202, 0.675629, -0.536876}
    //894 298 {-1.439283, 0.675618, 0.529550}
    //900 300 {-1.439283, 0.675618, 0.529550}
    //903 301 {-1.440711, 1.066319, 0.368437}
    //912 304 {-1.441195, -0.196627, 0.425376}
    //915 305 {-1.439693, 0.174198, 0.532284}
    //921 307 {-1.439693, 0.174198, 0.532284}
    //927 309 {-1.451672, 0.174209, -0.539610}
    //936 312 {-1.451672, 0.174209, -0.539610}
    //942 314 {-1.450822, -0.196619, -0.432703}
    //945 315 {-1.451202, 0.675629, -0.536876}
    //954 318 {-1.451202, 0.675629, -0.536876}
    //960 320 {-1.451672, 0.174209, -0.539610}
    //963 321 {-1.449087, 1.066327, -0.375763}
    //972 324 {-1.449087, 1.066327, -0.375763}
    //978 326 {-1.451202, 0.675629, -0.536876}
    //987 329 {-1.449087, 1.066327, -0.375763}
    //996 332 {-1.449087, 1.066327, -0.375763}
    //999 333 {-1.440711, 1.066319, 0.368437}
    //1008 336 {-1.440711, 1.066319, 0.368437}
    //1017 339 {-1.439283, 0.675618, 0.529550}
    //1023 341 {-1.440711, 1.066319, 0.368437}
    //1032 344 {-1.440711, 1.066319, 0.368437}
    //1035 345 {-1.439693, 0.174198, 0.532284}
    //1041 347 {-1.439283, 0.675618, 0.529550}
    //1050 350 {-1.439283, 0.675618, 0.529550}
    //1053 351 {-1.441195, -0.196627, 0.425376}
    //1059 353 {-1.439693, 0.174198, 0.532284}
    //1068 356 {-1.439693, 0.174198, 0.532284}
    //1086 362 {-1.441195, -0.196627, 0.425376}
    //1161 387 {-1.450822, -0.196619, -0.432703}
    synchronized (mScratch128i) {
      mScratch128i[0] = 51;
      mScratch128i[1] = 56;
      mScratch128i[2] = 171;
      mScratch128i[3] = 182;
      mScratch128i[4] = 247;
      mScratch128i[5] = 257;
      mScratch128i[6] = 259;
      mScratch128i[7] = 262;
      mScratch128i[8] = 263;
      mScratch128i[9] = 265;
      mScratch128i[10] = 268;
      mScratch128i[11] = 271;
      mScratch128i[12] = 272;
      mScratch128i[13] = 274;
      mScratch128i[14] = 286;
      mScratch128i[15] = 288;
      mScratch128i[16] = 289;
      mScratch128i[17] = 292;
      mScratch128i[18] = 293;
      mScratch128i[19] = 295;
      mScratch128i[20] = 298;
      mScratch128i[21] = 300;
      mScratch128i[22] = 301;
      mScratch128i[23] = 304;
      mScratch128i[24] = 305;
      mScratch128i[25] = 307;
      mScratch128i[26] = 309;
      mScratch128i[27] = 312;
      mScratch128i[28] = 314;
      mScratch128i[29] = 315;
      mScratch128i[30] = 318;
      mScratch128i[31] = 320;
      mScratch128i[32] = 321;
      mScratch128i[33] = 324;
      mScratch128i[34] = 326;
      mScratch128i[35] = 329;
      mScratch128i[36] = 332;
      mScratch128i[37] = 333;
      mScratch128i[38] = 336;
      mScratch128i[39] = 339;
      mScratch128i[40] = 341;
      mScratch128i[41] = 344;
      mScratch128i[42] = 345;
      mScratch128i[43] = 347;
      mScratch128i[44] = 350;
      mScratch128i[45] = 351;
      mScratch128i[46] = 353;
      mScratch128i[47] = 356;
      mScratch128i[48] = 362;
      mScratch128i[49] = 387;
//      int idx[] = { 51,56,171,182,247,257,259,262,263,265,268,271,272,274,286,288,289,292,293,
//                    295,298,300,301,304,305,307,309,312,314,315,318,320,321,324,326,329,332,333,
//                    336,339,341,344,345,347,350,351,353,356,362,387, };
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<50; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //519 173 {-1.599839, -0.511679, -0.007429}
    //522 174 {-1.599839, -0.511679, -0.007429}
    //531 177 {-1.599839, -0.511679, -0.007429}
    //540 180 {-1.599839, -0.511679, -0.007429}
    //744 248 {-1.599839, -0.511679, -0.007429}
    //750 250 {-1.599839, -0.511679, -0.007429}
    //759 253 {-1.599839, -0.511679, -0.007429}
    //768 256 {-1.599839, -0.511679, -0.007429}
    synchronized (mScratch128i) {
      //int idx[] = { 173,174,177,180,248,250,253,256, };
      mScratch128i[0] = 173;
      mScratch128i[1] = 174;
      mScratch128i[2] = 177;
      mScratch128i[3] = 180;
      mScratch128i[4] = 248;
      mScratch128i[5] = 250;
      mScratch128i[6] = 253;
      mScratch128i[7] = 256;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //840 280 {-2.228655, -0.728475, -0.650837}
    //849 283 {-2.215483, -0.728483, 0.657006}
    synchronized (mScratch128i) {
      //int idx[] = { 280,283,};
      mScratch128i[0] = 280;
      mScratch128i[1] = 283;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //843 281 {-2.823029, -0.686539, -0.662640}
    //852 284 {-2.811438, -0.686546, 0.670077}
    synchronized (mScratch128i) {
      //int idx[] = { 281,284,};
      mScratch128i[0] = 281;
      mScratch128i[1] = 284;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<2; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //024 008 {-3.196742, -0.049302, 0.447789}
    //762 254 {-3.196742, -0.049302, 0.447789}
    //765 255 {-3.196742, -0.049302, 0.447789}
    //846 282 {-3.196742, -0.049302, 0.447789}
    //909 303 {-3.196742, -0.049302, 0.447789}
    //918 306 {-3.196742, -0.049302, 0.447789}
    synchronized (mScratch128i) {
      //int idx[] = { 8,254,255,282,303,306, };
      mScratch128i[0] = 8;
      mScratch128i[1] = 254;
      mScratch128i[2] = 255;
      mScratch128i[3] = 282;
      mScratch128i[4] = 303;
      mScratch128i[5] = 306;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //030 010 {-3.354060, 0.467534, 0.517028}
    //039 013 {-3.354060, 0.467534, 0.517028}
    //048 016 {-3.354060, 0.467534, 0.517028}
    //051 017 {-3.355715, 0.978260, 0.327890}
    //057 019 {-3.363465, 0.978265, -0.319692}
    //060 020 {-3.366217, 0.467542, -0.508830}
    //066 022 {-3.366217, 0.467542, -0.508830}
    //075 025 {-3.366217, 0.467542, -0.508830}
    //780 260 {-3.354060, 0.467534, 0.517028}
    //783 261 {-3.354060, 0.467534, 0.517028}
    //792 264 {-3.355715, 0.978260, 0.327890}
    //807 269 {-3.363465, 0.978265, -0.319692}
    //810 270 {-3.366217, 0.467542, -0.508830}
    //819 273 {-3.366217, 0.467542, -0.508830}
    //873 291 {-3.363465, 0.978265, -0.319692}
    //882 294 {-3.363465, 0.978265, -0.319692}
    //888 296 {-3.366217, 0.467542, -0.508830}
    //891 297 {-3.354060, 0.467534, 0.517028}
    //897 299 {-3.355715, 0.978260, 0.327890}
    //906 302 {-3.355715, 0.978260, 0.327890}
    //1437 479 {-3.363465, 0.978265, -0.319692}
    //1443 481 {-3.363465, 0.978265, -0.319692}
    //1452 484 {-3.355715, 0.978260, 0.327890}
    //1458 486 {-3.355715, 0.978260, 0.327890}
    synchronized (mScratch128i) {
      //int idx[] = { 10,13,16,17,19,20,22,25,260,261,264,269,270,273,291,294,296,297,299,302,479,481,484,486, };
      mScratch128i[0] = 10;
      mScratch128i[1] = 13;
      mScratch128i[2] = 16;
      mScratch128i[3] = 17;
      mScratch128i[4] = 19;
      mScratch128i[5] = 20;
      mScratch128i[6] = 22;
      mScratch128i[7] = 25;
      mScratch128i[8] = 260;
      mScratch128i[9] = 261;
      mScratch128i[10] = 264;
      mScratch128i[11] = 269;
      mScratch128i[12] = 270;
      mScratch128i[13] = 273;
      mScratch128i[14] = 291;
      mScratch128i[15] = 294;
      mScratch128i[16] = 296;
      mScratch128i[17] = 297;
      mScratch128i[18] = 299;
      mScratch128i[19] = 302;
      mScratch128i[20] = 479;
      mScratch128i[21] = 481;
      mScratch128i[22] = 484;
      mScratch128i[23] = 486;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<24; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //003 001 {-3.482355, -0.314469, 0.028982}
    //012 004 {-3.482355, -0.314469, 0.028982}
    //021 007 {-3.482355, -0.314469, 0.028982}
    //753 251 {-3.482355, -0.314469, 0.028982}
    //756 252 {-3.482355, -0.314469, 0.028982}
    //834 278 {-3.482355, -0.314469, 0.028982}
    synchronized (mScratch128i) {
      //int idx[] = { 1,4,7,251,252,278,};
      mScratch128i[0] = 1;
      mScratch128i[1] = 4;
      mScratch128i[2] = 7;
      mScratch128i[3] = 251;
      mScratch128i[4] = 252;
      mScratch128i[5] = 278;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //000 000 {-3.563356, -0.050557, -0.371373}
    //015 005 {-3.518670, 0.077497, 0.434307}
    //018 006 {-3.518670, 0.077497, 0.434307}
    //027 009 {-3.518670, 0.077497, 0.434307}
    //078 026 {-3.563356, -0.050557, -0.371373}
    //159 053 {-3.592829, 1.130262, 0.000657}
    //162 054 {-3.592829, 1.130262, 0.000657}
    //462 154 {-3.563356, -0.050557, -0.371373}
    //474 158 {-3.518670, 0.077497, 0.434307}
    //480 160 {-3.518670, 0.077497, 0.434307}
    //507 169 {-3.563356, -0.050557, -0.371373}
    //774 258 {-3.518670, 0.077497, 0.434307}
    //798 266 {-3.592829, 1.130262, 0.000657}
    //801 267 {-3.592829, 1.130262, 0.000657}
    //825 275 {-3.563356, -0.050557, -0.371373}
    //828 276 {-3.563356, -0.050557, -0.371373}
    //855 285 {-3.563356, -0.050557, -0.371373}
    //924 308 {-3.518670, 0.077497, 0.434307}
    //1434 478 {-3.592829, 1.130262, 0.000657}
    //1461 487 {-3.592829, 1.130262, 0.000657}
    synchronized (mScratch128i) {
      //int idx[] = { 0,5,6,9,26,53,54,154,158,160,169,258,266,267,275,276,285,308,478,487, };
      mScratch128i[0] = 0;
      mScratch128i[1] = 5;
      mScratch128i[2] = 6;
      mScratch128i[3] = 9;
      mScratch128i[4] = 26;
      mScratch128i[5] = 53;
      mScratch128i[6] = 54;
      mScratch128i[7] = 154;
      mScratch128i[8] = 158;
      mScratch128i[9] = 160;
      mScratch128i[10] = 169;
      mScratch128i[11] = 258;
      mScratch128i[12] = 266;
      mScratch128i[13] = 267;
      mScratch128i[14] = 275;
      mScratch128i[15] = 276;
      mScratch128i[16] = 285;
      mScratch128i[17] = 308;
      mScratch128i[18] = 478;
      mScratch128i[19] = 487;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<20; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //489 163 {-4.281947, 0.878867, 0.003449}
    //498 166 {-4.281947, 0.878867, 0.003449}
    //1431 477 {-4.281947, 0.878867, 0.003449}
    //1440 480 {-4.281947, 0.878867, 0.003449}
    //1455 485 {-4.281947, 0.878867, 0.003449}
    //1464 488 {-4.281947, 0.878867, 0.003449}
    synchronized (mScratch128i) {
      //int idx[] = { 163,166,477,480,485,488, };
      mScratch128i[0] = 163;
      mScratch128i[1] = 166;
      mScratch128i[2] = 477;
      mScratch128i[3] = 480;
      mScratch128i[4] = 485;
      mScratch128i[5] = 488;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //006 002 {-4.318616, -0.053987, 0.004143}
    //009 003 {-4.318616, -0.053987, 0.004143}
    //033 011 {-4.382598, 0.380261, 0.198944}
    //036 012 {-4.382598, 0.380261, 0.198944}
    //042 014 {-4.345534, 0.664362, 0.218885}
    //045 015 {-4.345534, 0.664362, 0.218885}
    //054 018 {-4.349825, 0.664365, -0.202633}
    //063 021 {-4.349825, 0.664365, -0.202633}
    //069 023 {-4.387043, 0.380264, -0.181603}
    //072 024 {-4.387043, 0.380264, -0.181603}
    //348 116 {-4.318616, -0.053987, 0.004143}
    //354 118 {-4.318616, -0.053987, 0.004143}
    //381 127 {-4.382598, 0.380261, 0.198944}
    //390 130 {-4.382598, 0.380261, 0.198944}
    //393 131 {-4.345534, 0.664362, 0.218885}
    //399 133 {-4.345534, 0.664362, 0.218885}
    //408 136 {-4.345534, 0.664362, 0.218885}
    //420 140 {-4.349825, 0.664365, -0.202633}
    //426 142 {-4.349825, 0.664365, -0.202633}
    //435 145 {-4.349825, 0.664365, -0.202633}
    //438 146 {-4.387043, 0.380264, -0.181603}
    //444 148 {-4.387043, 0.380264, -0.181603}
    //465 155 {-4.318616, -0.053987, 0.004143}
    //471 157 {-4.318616, -0.053987, 0.004143}
    //483 161 {-4.382598, 0.380261, 0.198944}
    //486 162 {-4.345534, 0.664362, 0.218885}
    //501 167 {-4.349825, 0.664365, -0.202633}
    //504 168 {-4.387043, 0.380264, -0.181603}
    //1254 418 {-4.387043, 0.380264, -0.181603}
    //1281 427 {-4.382598, 0.380261, 0.198944}
    //1446 482 {-4.349825, 0.664365, -0.202633}
    //1449 483 {-4.345534, 0.664362, 0.218885}
    synchronized (mScratch128i) {
//      int idx[] = { 2,3,11,12,14,15,18,21,23,24,116,118,127,130,131,133,136,140,142,145,146,148,155,157,161,162,167,168,418,427,482,483, };
      mScratch128i[0] = 2;
      mScratch128i[1] = 3;
      mScratch128i[2] = 11;
      mScratch128i[3] = 12;
      mScratch128i[4] = 14;
      mScratch128i[5] = 15;
      mScratch128i[6] = 18;
      mScratch128i[7] = 21;
      mScratch128i[8] = 23;
      mScratch128i[9] = 24;
      mScratch128i[10] = 116;
      mScratch128i[11] = 118;
      mScratch128i[12] = 127;
      mScratch128i[13] = 130;
      mScratch128i[14] = 131;
      mScratch128i[15] = 133;
      mScratch128i[16] = 136;
      mScratch128i[17] = 140;
      mScratch128i[18] = 142;
      mScratch128i[19] = 145;
      mScratch128i[20] = 146;
      mScratch128i[21] = 148;
      mScratch128i[22] = 155;
      mScratch128i[23] = 157;
      mScratch128i[24] = 161;
      mScratch128i[25] = 162;
      mScratch128i[26] = 167;
      mScratch128i[27] = 168;
      mScratch128i[28] = 418;
      mScratch128i[29] = 427;
      mScratch128i[30] = 482;
      mScratch128i[31] = 483;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<32; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //336 112 {-4.535238, 0.159280, -0.085091}
    //345 115 {-4.535238, 0.159280, -0.085091}
    //357 119 {-4.532741, 0.159278, 0.102805}
    //366 122 {-4.532741, 0.159278, 0.102805}
    //372 124 {-4.532741, 0.159278, 0.102805}
    //378 126 {-4.523820, 0.323676, 0.001114}
    //411 137 {-4.568282, 0.724235, 0.008114}
    //417 139 {-4.568282, 0.724235, 0.008114}
    //447 149 {-4.525612, 0.323678, 0.018667}
    //450 150 {-4.535238, 0.159280, -0.085091}
    //459 153 {-4.535238, 0.159280, -0.085091}
    //468 156 {-4.532741, 0.159278, 0.102805}
    //477 159 {-4.532741, 0.159278, 0.102805}
    //492 164 {-4.568282, 0.724235, 0.008114}
    //495 165 {-4.568282, 0.724235, 0.008114}
    //510 170 {-4.535238, 0.159280, -0.085091}
    //1251 417 {-4.525612, 0.323678, 0.018667}
    //1257 419 {-4.535238, 0.159280, -0.085091}
    //1260 420 {-4.525612, 0.323678, 0.018667}
    //1263 421 {-4.535238, 0.159280, -0.085091}
    //1272 424 {-4.532741, 0.159278, 0.102805}
    //1275 425 {-4.523820, 0.323676, 0.001114}
    //1278 426 {-4.532741, 0.159278, 0.102805}
    //1284 428 {-4.523820, 0.323676, 0.001114}
    synchronized (mScratch128i) {
      //int idx[] = { 112,115,119,122,124,126,137,139,149,150,153,156,159,164,165,170,417,419,420,421,424,425,426,428, };
      mScratch128i[0] = 112;
      mScratch128i[1] = 115;
      mScratch128i[2] = 119;
      mScratch128i[3] = 122;
      mScratch128i[4] = 124;
      mScratch128i[5] = 126;
      mScratch128i[6] = 137;
      mScratch128i[7] = 139;
      mScratch128i[8] = 149;
      mScratch128i[9] = 150;
      mScratch128i[10] = 153;
      mScratch128i[11] = 156;
      mScratch128i[12] = 159;
      mScratch128i[13] = 164;
      mScratch128i[14] = 165;
      mScratch128i[15] = 170;
      mScratch128i[16] = 417;
      mScratch128i[17] = 419;
      mScratch128i[18] = 420;
      mScratch128i[19] = 421;
      mScratch128i[20] = 424;
      mScratch128i[21] = 425;
      mScratch128i[22] = 426;
      mScratch128i[23] = 428;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<24; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //339 113 {-4.638330, 0.149347, 0.007086}
    //342 114 {-4.638330, 0.149347, 0.007086}
    //351 117 {-4.638330, 0.149347, 0.007086}
    //363 121 {-4.638330, 0.149347, 0.007086}
    //384 128 {-4.654118, 0.449342, 0.001114}
    //387 129 {-4.654118, 0.449342, 0.001114}
    //396 132 {-4.654118, 0.449342, 0.001114}
    //429 143 {-4.655901, 0.449344, 0.018667}
    //432 144 {-4.655901, 0.449344, 0.018667}
    //441 147 {-4.655901, 0.449344, 0.018667}
    synchronized (mScratch128i) {
      //int idx[] = { 113,114,117,121,128,129,132,143,144,147, };
      mScratch128i[0] = 113;
      mScratch128i[1] = 114;
      mScratch128i[2] = 117;
      mScratch128i[3] = 121;
      mScratch128i[4] = 128;
      mScratch128i[5] = 129;
      mScratch128i[6] = 132;
      mScratch128i[7] = 143;
      mScratch128i[8] = 144;
      mScratch128i[9] = 147;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<10; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //333 111 {-4.788940, 0.265594, 0.009013}
    //360 120 {-4.788940, 0.265594, 0.009013}
    //369 123 {-4.788940, 0.265594, 0.009013}
    //375 125 {-4.718528, 0.351371, 0.001114}
    //402 134 {-4.784564, 0.579400, 0.009013}
    //405 135 {-4.784564, 0.579400, 0.009013}
    //414 138 {-4.784564, 0.579400, 0.009013}
    //423 141 {-4.784564, 0.579400, 0.009013}
    //453 151 {-4.788940, 0.265594, 0.009013}
    //456 152 {-4.720216, 0.351373, 0.018667}
    //1266 422 {-4.720216, 0.351373, 0.018667}
    //1269 423 {-4.718528, 0.351371, 0.001114}
    synchronized (mScratch128i) {
      //int idx[] = { 111,120,123,125,134,135,138,141,151,152,422,423, };
      mScratch128i[0] = 111;
      mScratch128i[1] = 120;
      mScratch128i[2] = 123;
      mScratch128i[3] = 125;
      mScratch128i[4] = 134;
      mScratch128i[5] = 135;
      mScratch128i[6] = 138;
      mScratch128i[7] = 141;
      mScratch128i[8] = 151;
      mScratch128i[9] = 152;
      mScratch128i[10] = 422;
      mScratch128i[11] = 423;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<12; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //738 246 {-3.207148, -0.049295, -0.440878}
    //747 249 {-3.207148, -0.049295, -0.440878}
    //831 277 {-3.207148, -0.049295, -0.440878}
    //837 279 {-3.207148, -0.049295, -0.440878}
    //861 287 {-3.207148, -0.049295, -0.440878}
    //870 290 {-3.207148, -0.049295, -0.440878}
    synchronized (mScratch128i) {
      //int idx[] = { 246,249,277,279,287,290, };
      mScratch128i[0] = 246;
      mScratch128i[1] = 249;
      mScratch128i[2] = 277;
      mScratch128i[3] = 279;
      mScratch128i[4] = 287;
      mScratch128i[5] = 290;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<6; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    //582 194 {0.041021, 1.354216, -0.010364}
    //585 195 {0.041021, 1.354216, -0.010364}
    //594 198 {0.041021, 1.354216, -0.010364}
    //732 244 {0.041021, 1.354216, -0.010364}
    //984 328 {0.041021, 1.354216, -0.010364}
    //990 330 {0.041021, 1.354216, -0.010364}
    //1005 335 {0.041021, 1.354216, -0.010364}
    //1011 337 {0.041021, 1.354216, -0.010364}
    synchronized (mScratch128i) {
      //int idx[] = { 194,195,198,244,328,330,335,337, };
      mScratch128i[0] = 194;
      mScratch128i[1] = 195;
      mScratch128i[2] = 198;
      mScratch128i[3] = 244;
      mScratch128i[4] = 328;
      mScratch128i[5] = 330;
      mScratch128i[6] = 335;
      mScratch128i[7] = 337;
      float width = getMoveWidth(IwashiData.vertices[0+3*mScratch128i[0]]) * s;
      int ii;
      for (ii=0; ii<8; ii++) {
        result[2+3*mScratch128i[ii]] = IwashiData.vertices[2+3*mScratch128i[ii]] + width;
      }
    }
    return result;
  }
  public static int iwashiNumVerts = 489;
  public static float vertices[] = {
    // f 45/1/1 46/2/1 71/3/1
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    // f 71/4/2 46/5/2 48/6/2
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    // f 48/6/3 46/5/3 47/7/3
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    // f 48/6/4 49/8/4 72/9/4
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    // f 72/9/5 49/8/5 73/10/5
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    // f 73/10/6 49/8/6 50/11/6
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    // f 75/12/7 51/13/7 52/14/7
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    // f 75/12/8 52/14/8 76/15/8
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    // f 76/15/9 52/14/9 45/1/9
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    // f 61/16/10 62/17/10 56/18/10
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    // f 56/18/11 62/17/11 14/19/11
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    0.427431125754878f, 0.0212056603452834f, 0.00118741404677465f,
    // f 56/18/12 14/19/12 63/20/12
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.427431125754878f, 0.0212056603452834f, 0.00118741404677465f,
    0.433433065430731f, 0.0636681853585412f, -0.000804123136505047f,
    // f 56/18/13 63/20/13 64/21/13
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.433433065430731f, 0.0636681853585412f, -0.000804123136505047f,
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    // f 64/22/14 65/23/14 58/24/14
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    0.432437762659421f, 0.0636691169992007f, -0.00358921113048292f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 58/24/15 65/23/15 20/25/15
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.432437762659421f, 0.0636691169992007f, -0.00358921113048292f,
    0.426148153051018f, 0.0212071095640872f, -0.000131996158437835f,
    // f 58/24/16 20/25/16 66/26/16
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.426148153051018f, 0.0212071095640872f, -0.000131996158437835f,
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    // f 61/27/17 58/24/17 66/26/17
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    // f 40/28/18 70/29/18 41/30/18
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    // f 41/31/19 70/32/19 42/33/19
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    // f 27/34/20 82/35/20 56/36/20
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    0.225508985449173f, -0.0357291777050329f, 0.0133824867649512f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    // f 27/34/21 56/36/21 28/37/21
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.0795504995639953f, -0.0115422334480955f, 0.0413531342872994f,
    // f 28/37/22 56/36/22 29/38/22
    0.0795504995639953f, -0.0115422334480955f, 0.0413531342872994f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    // f 33/39/23 58/40/23 34/41/23
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.078310589361733f, -0.0115409912605494f, -0.0427540387557402f,
    // f 34/41/24 58/40/24 22/42/24
    0.078310589361733f, -0.0115409912605494f, -0.0427540387557402f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    // f 80/43/25 22/42/25 58/40/25
    0.224691004950069f, -0.0357288671581463f, -0.0159687486992548f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 61/27/26 66/26/26 68/44/26
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    // f 61/16/27 68/45/27 62/17/27
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    // f 64/21/28 63/20/28 69/46/28
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    0.433433065430731f, 0.0636681853585412f, -0.000804123136505047f,
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    // f 64/22/29 69/47/29 65/23/29
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    0.432437762659421f, 0.0636691169992007f, -0.00358921113048292f,
    // f 66/48/30 9/49/30 10/50/30
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    0.494025628226158f, -0.0187000286355918f, 0.00148491796406484f,
    0.522840652732966f, -0.0570703738094913f, 0.00146317968200814f,
    // f 62/51/31 12/52/31 13/53/31
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    0.523448392989894f, -0.0570706843563778f, -0.000407761793671325f,
    0.495122272798104f, -0.0187013743387667f, -0.00042950007572802f,
    // f 62/51/32 13/53/32 14/54/32
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    0.495122272798104f, -0.0187013743387667f, -0.00042950007572802f,
    0.427431125754878f, 0.0212056603452834f, 0.00118741404677465f,
    // f 14/54/33 15/55/33 63/56/33
    0.427431125754878f, 0.0212056603452834f, 0.00118741404677465f,
    0.485860729485664f, 0.0814109711732149f, -0.000430224685129909f,
    0.433433065430731f, 0.0636681853585412f, -0.000804123136505047f,
    // f 69/57/34 16/58/34 17/59/34
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    0.509260437385263f, 0.119852431584128f, -0.000412730543855713f,
    0.508372997899205f, 0.130376555021547f, 0.000527708944168409f,
    // f 69/46/35 17/60/35 18/61/35
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    0.508372997899205f, 0.130376555021547f, 0.000527708944168409f,
    0.508540796733557f, 0.119853570256046f, 0.00146814843219253f,
    // f 65/62/36 19/63/36 20/64/36
    0.432437762659421f, 0.0636691169992007f, -0.00358921113048292f,
    0.484745555616156f, 0.0814126274232763f, 0.00148564257346673f,
    0.426148153051018f, 0.0212071095640872f, -0.000131996158437835f,
    // f 20/64/37 9/49/37 66/48/37
    0.426148153051018f, 0.0212071095640872f, -0.000131996158437835f,
    0.494025628226158f, -0.0187000286355918f, 0.00148491796406484f,
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    // f 1/65/38 84/66/38 87/67/38
    -0.476551607010106f, -0.0081605848851046f, 0.00146069530691595f,
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    -0.460961118150303f, -0.0201939661910309f, 0.00126122069013857f,
    // f 87/67/39 84/66/39 71/3/39
    -0.460961118150303f, -0.0201939661910309f, 0.00126122069013857f,
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    // f 87/68/40 71/4/40 85/69/40
    -0.460961118150303f, -0.0201939661910309f, 0.00126122069013857f,
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    // f 1/70/41 87/68/41 85/69/41
    -0.476551607010106f, -0.0081605848851046f, 0.00146069530691595f,
    -0.460961118150303f, -0.0201939661910309f, 0.00126122069013857f,
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    // f 1/70/42 85/69/42 2/71/42
    -0.476551607010106f, -0.0081605848851046f, 0.00146069530691595f,
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    -0.469262864552126f, 0.000718675210024186f, 0.000643025354697729f,
    // f 3/72/43 72/9/43 4/73/43
    -0.449107543491674f, -0.00214819013073839f, 0.000643025354697729f,
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    -0.462595422898451f, 0.010860204883245f, 0.000643025354697729f,
    // f 4/73/44 72/9/44 73/10/44
    -0.462595422898451f, 0.010860204883245f, 0.000643025354697729f,
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    // f 4/73/45 73/10/45 5/74/45
    -0.462595422898451f, 0.010860204883245f, 0.000643025354697729f,
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    -0.476098622618296f, 0.0243232405390999f, 0.00146069530691595f,
    // f 5/74/46 73/10/46 86/75/46
    -0.476098622618296f, 0.0243232405390999f, 0.00146069530691595f,
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    -0.453710055381221f, 0.0393159266423441f, 0.00136763475658753f,
    // f 5/76/47 86/77/47 75/12/47
    -0.476098622618296f, 0.0243232405390999f, 0.00146069530691595f,
    -0.453710055381221f, 0.0393159266423441f, 0.00136763475658753f,
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    // f 5/78/48 75/12/48 6/79/48
    -0.476098622618296f, 0.0243232405390999f, 0.00146069530691595f,
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    -0.462779991264675f, 0.0108604119145027f, 0.00246003518775083f,
    // f 6/79/49 75/12/49 76/15/49
    -0.462779991264675f, 0.0108604119145027f, 0.00246003518775083f,
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    // f 6/79/50 76/15/50 7/80/50
    -0.462779991264675f, 0.0108604119145027f, 0.00246003518775083f,
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    -0.449293043498558f, -0.00214798309948071f, 0.00246003518775083f,
    // f 84/66/51 1/81/51 8/82/51
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    -0.476551607010106f, -0.0081605848851046f, 0.00146069530691595f,
    -0.46943759893361f, 0.000718882241281869f, 0.00246003518775083f,
    // f 84/66/52 45/1/52 71/3/52
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    // f 85/69/53 71/4/53 48/6/53
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    -0.427865722390903f, -0.0412422130658682f, 0.00095657419445833f,
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    // f 85/69/54 48/6/54 72/9/54
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    // f 73/10/55 74/83/55 86/75/55
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
    -0.453710055381221f, 0.0393159266423441f, 0.00136763475658753f,
    // f 86/77/56 74/84/56 75/12/56
    -0.453710055381221f, 0.0393159266423441f, 0.00136763475658753f,
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    // f 76/15/57 45/1/57 84/66/57
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    // f 36/85/58 77/86/58 35/87/58
    -0.131004223093309f, -0.0560068542387748f, -0.044263814202392f,
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    // f 35/87/59 77/86/59 78/88/59
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    -0.00830166189926847f, -0.0914714121954673f, -0.00125545127825355f,
    // f 35/89/60 78/90/60 79/91/60
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.00830166189926847f, -0.0914714121954673f, -0.00125545127825355f,
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    // f 35/89/61 79/91/61 37/92/61
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    -0.130007678134453f, -0.0560076823638055f, 0.0445607730782079f,
    // f 70/93/62 83/94/62 88/95/62
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    -0.000240589334502103f, 0.148074208022046f, -0.00128743760756554f,
    -0.0121218026660315f, 0.194705824986891f, -0.00119147861962957f,
    // f 30/96/63 60/97/63 89/98/63
    0.0794127202620074f, 0.0773160999275068f, 0.0261204989564008f,
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.0559170533744729f, 0.101603143797534f, -0.000553615314708859f,
    // f 89/99/64 60/100/64 32/101/64
    0.0559170533744729f, 0.101603143797534f, -0.000553615314708859f,
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.0786184448419074f, 0.0773168245369086f, -0.0275211963935839f,
    // f 30/96/65 89/98/65 31/102/65
    0.0794127202620074f, 0.0773160999275068f, 0.0261204989564008f,
    0.0559170533744729f, 0.101603143797534f, -0.000553615314708859f,
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    // f 31/102/66 89/98/66 90/103/66
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    0.0559170533744729f, 0.101603143797534f, -0.000553615314708859f,
    0.0490260179625009f, 0.1154976325944f, -0.00144923253544465f,
    // f 31/104/67 89/99/67 32/101/67
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    0.0559170533744729f, 0.101603143797534f, -0.000553615314708859f,
    0.0786184448419074f, 0.0773168245369086f, -0.0275211963935839f,
    // f 80/43/68 53/105/68 81/106/68
    0.224691004950069f, -0.0357288671581463f, -0.0159687486992548f,
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    // f 81/107/69 55/108/69 82/35/69
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    0.225508985449173f, -0.0357291777050329f, 0.0133824867649512f,
    // f 82/35/70 55/108/70 56/18/70
    0.225508985449173f, -0.0357291777050329f, 0.0133824867649512f,
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    // f 60/109/71 56/18/71 57/110/71
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.288981560070226f, 0.0643257166329417f, -0.00177861926641799f,
    // f 60/111/72 57/112/72 58/24/72
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.288981560070226f, 0.0643257166329417f, -0.00177861926641799f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 53/105/73 80/43/73 58/40/73
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.224691004950069f, -0.0357288671581463f, -0.0159687486992548f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 53/105/74 61/27/74 67/113/74
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.3430298648103f, -0.0169703859932808f, -0.00188482630160927f,
    // f 53/105/75 67/113/75 54/114/75
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.3430298648103f, -0.0169703859932808f, -0.00188482630160927f,
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    // f 54/115/76 67/116/76 55/108/76
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    0.3430298648103f, -0.0169703859932808f, -0.00188482630160927f,
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    // f 55/108/77 67/116/77 61/16/77
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    0.3430298648103f, -0.0169703859932808f, -0.00188482630160927f,
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    // f 55/108/78 61/16/78 56/18/78
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    // f 56/18/79 64/21/79 57/110/79
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    0.288981560070226f, 0.0643257166329417f, -0.00177861926641799f,
    // f 57/112/80 64/22/80 58/24/80
    0.288981560070226f, 0.0643257166329417f, -0.00177861926641799f,
    0.370434695905401f, 0.0534116498216775f, -0.00237507631980214f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 61/27/81 53/105/81 58/24/81
    0.377949723528029f, -0.0128468409183836f, -0.00194124231932783f,
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    // f 70/93/82 31/117/82 83/94/82
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    -0.000240589334502103f, 0.148074208022046f, -0.00128743760756554f,
    // f 94/118/83 36/85/83 35/87/83
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    -0.131004223093309f, -0.0560068542387748f, -0.044263814202392f,
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    // f 94/119/84 35/87/84 46/2/84
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    // f 46/5/85 35/89/85 47/120/85
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    // f 47/121/86 35/89/86 37/92/86
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    -0.146429811556368f, -0.0886204882615463f, -0.000241308662494354f,
    -0.130007678134453f, -0.0560076823638055f, 0.0445607730782079f,
    // f 48/6/87 38/122/87 49/8/87
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    // f 49/8/88 38/122/88 39/123/88
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    // f 50/11/89 40/28/89 41/30/89
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    // f 41/31/90 42/33/90 51/13/90
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    // f 52/14/91 43/124/91 44/125/91
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    // f 52/14/92 44/125/92 45/1/92
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    // f 45/1/93 94/126/93 46/2/93
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    -0.341299639100365f, -0.0682061710977338f, 0.00352779889924987f,
    // f 94/127/94 95/128/94 97/129/94
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    -0.211522095221902f, -0.111062262531847f, -0.066844092384082f,
    -0.27304909359888f, -0.106721231120754f, -0.0680658873512971f,
    // f 47/130/95 96/131/95 98/132/95
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    -0.210158587358803f, -0.111063090656877f, 0.0685380981867414f,
    -0.27184924394498f, -0.106721955730155f, 0.0698911509713274f,
    // f 45/1/96 44/125/96 94/133/96
    -0.349684508552148f, -0.0408871544589422f, -0.0379152006855489f,
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    // f 44/125/97 36/85/97 94/133/97
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    -0.131004223093309f, -0.0560068542387748f, -0.044263814202392f,
    -0.31281141343381f, -0.0407565177353443f, -0.0451100544681705f,
    // f 51/13/98 42/33/98 43/124/98
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    // f 51/13/99 43/124/99 52/14/99
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    -0.329277540997983f, 0.0127441893281585f, -0.0521441484792013f,
    // f 49/8/100 39/123/100 50/11/100
    -0.328019101498158f, 0.0127433612031278f, 0.0540481874927799f,
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    // f 39/123/101 40/28/101 50/11/101
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    // f 47/134/102 37/92/102 38/122/102
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    -0.130007678134453f, -0.0560076823638055f, 0.0445607730782079f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    // f 47/134/103 38/122/103 48/6/103
    -0.311734229800086f, -0.0407572423447462f, 0.0468808688674302f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    -0.345058809161741f, -0.0276315641232855f, 0.0454852711593904f,
    // f 44/125/104 34/41/104 22/42/104
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    0.078310589361733f, -0.0115409912605494f, -0.0427540387557402f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    // f 44/125/105 22/42/105 36/85/105
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    -0.131004223093309f, -0.0560068542387748f, -0.044263814202392f,
    // f 43/124/106 33/39/106 34/41/106
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    0.078310589361733f, -0.0115409912605494f, -0.0427540387557402f,
    // f 43/124/107 34/41/107 44/125/107
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    0.078310589361733f, -0.0115409912605494f, -0.0427540387557402f,
    -0.131092211377824f, -0.017620360626776f, -0.0553303595349396f,
    // f 42/33/108 32/101/108 33/39/108
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    0.0786184448419074f, 0.0773168245369086f, -0.0275211963935839f,
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    // f 42/33/109 33/39/109 43/124/109
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    -0.131043559032269f, 0.034284445986879f, -0.0550473478056872f,
    // f 70/135/110 31/136/110 42/33/110
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    // f 31/136/111 32/101/111 42/33/111
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    0.0786184448419074f, 0.0773168245369086f, -0.0275211963935839f,
    -0.130824623477269f, 0.0747277951439564f, -0.0383696342961627f,
    // f 40/28/112 30/96/112 31/117/112
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    0.0794127202620074f, 0.0773160999275068f, 0.0261204989564008f,
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    // f 40/28/113 31/117/113 70/93/113
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    0.0234248431843323f, 0.104528806015478f, -0.000545127033143864f,
    -0.10279507556211f, 0.105387882219233f, -0.00051231257880114f,
    // f 39/123/114 29/38/114 40/28/114
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    // f 29/38/115 30/96/115 40/28/115
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    0.0794127202620074f, 0.0773160999275068f, 0.0261204989564008f,
    -0.129957576570093f, 0.0747269670189256f, 0.0386666966876074f,
    // f 38/122/116 28/37/116 39/123/116
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    0.0795504995639953f, -0.0115422334480955f, 0.0413531342872994f,
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    // f 28/37/117 29/38/117 39/123/117
    0.0795504995639953f, -0.0115422334480955f, 0.0413531342872994f,
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    -0.129809756252108f, 0.0342833073149617f, 0.0553444101971319f,
    // f 37/92/118 27/34/118 38/122/118
    -0.130007678134453f, -0.0560076823638055f, 0.0445607730782079f,
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    // f 27/34/119 28/37/119 38/122/119
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    0.0795504995639953f, -0.0115422334480955f, 0.0413531342872994f,
    -0.129852197659933f, -0.0176214992986932f, 0.0556274219263843f,
    // f 79/137/120 25/138/120 27/34/120
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    0.0398438711373822f, -0.0733703587893735f, 0.0163556626565339f,
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    // f 79/137/121 27/34/121 37/92/121
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    -0.130007678134453f, -0.0560076823638055f, 0.0445607730782079f,
    // f 79/139/122 25/140/122 26/141/122
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    0.0398438711373822f, -0.0733703587893735f, 0.0163556626565339f,
    0.0594856511631513f, -0.103647541553566f, 0.0177964966943773f,
    // f 79/139/123 26/141/123 93/142/123
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    0.0594856511631513f, -0.103647541553566f, 0.0177964966943773f,
    0.0336343826257019f, -0.106399815093201f, 0.0183584830433573f,
    // f 78/143/124 24/144/124 79/145/124
    -0.00830166189926847f, -0.0914714121954673f, -0.00125545127825355f,
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    // f 24/144/125 25/146/125 79/145/125
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    0.0398438711373822f, -0.0733703587893735f, 0.0163556626565339f,
    -0.00804887673363777f, -0.0830925466501579f, 0.0185354947686761f,
    // f 77/147/126 21/148/126 24/149/126
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    0.0391801289252511f, -0.0733699447268582f, -0.0173796667203631f,
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    // f 77/147/127 24/149/127 78/150/127
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    -0.00830166189926847f, -0.0914714121954673f, -0.00125545127825355f,
    // f 77/151/128 21/152/128 23/153/128
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    0.0391801289252511f, -0.0733699447268582f, -0.0173796667203631f,
    0.0589899148166299f, -0.103647231006679f, -0.0187463835679561f,
    // f 77/151/129 23/153/129 92/154/129
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    0.0589899148166299f, -0.103647231006679f, -0.0187463835679561f,
    0.0331386462791805f, -0.106399401030686f, -0.0191804245996881f,
    // f 36/85/130 22/42/130 77/151/130
    -0.131004223093309f, -0.0560068542387748f, -0.044263814202392f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    // f 22/42/131 21/152/131 77/151/131
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    0.0391801289252511f, -0.0733699447268582f, -0.0173796667203631f,
    -0.00853985136123253f, -0.0830921325876425f, -0.0192047507724658f,
    // f 91/155/132 54/156/132 81/107/132
    0.221463594674052f, -0.0787305015664101f, -0.00180563684554559f,
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    // f 54/156/133 55/108/133 81/107/133
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    0.289156501482968f, -0.0281252301572301f, 0.00488395715270106f,
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    // f 81/106/134 53/105/134 54/157/134
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    0.288499694817969f, -0.0281250231259724f, -0.00798696910618097f,
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    // f 81/106/135 54/157/135 91/158/135
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    0.304436961034391f, -0.031393943169155f, -0.00206908412094696f,
    0.221463594674052f, -0.0787305015664101f, -0.00180563684554559f,
    // f 24/159/136 59/160/136 25/138/136
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    0.0398438711373822f, -0.0733703587893735f, 0.0163556626565339f,
    // f 59/160/137 27/34/137 25/138/137
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    0.0398438711373822f, -0.0733703587893735f, 0.0163556626565339f,
    // f 21/152/138 22/42/138 59/161/138
    0.0391801289252511f, -0.0733699447268582f, -0.0173796667203631f,
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    // f 21/152/139 59/161/139 24/162/139
    0.0391801289252511f, -0.0733699447268582f, -0.0173796667203631f,
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    0.0354381424582633f, -0.0877909140120113f, -0.00131921690561985f,
    // f 7/80/140 76/15/140 84/66/140
    -0.449293043498558f, -0.00214798309948071f, 0.00246003518775083f,
    -0.434948986325634f, 0.00370955227413876f, -0.0182710398003164f,
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    // f 7/80/141 84/66/141 8/82/141
    -0.449293043498558f, -0.00214798309948071f, 0.00246003518775083f,
    -0.450289484941785f, -0.0191657454497492f, -0.00828053942957518f,
    -0.46943759893361f, 0.000718882241281869f, 0.00246003518775083f,
    // f 2/71/142 85/69/142 3/72/142
    -0.469262864552126f, 0.000718675210024186f, 0.000643025354697729f,
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    -0.449107543491674f, -0.00214819013073839f, 0.000643025354697729f,
    // f 85/69/143 72/9/143 3/72/143
    -0.450031006416568f, -0.0191659524810069f, 0.0111696331672086f,
    -0.434488859355434f, 0.00370924172725224f, 0.0211215222083919f,
    -0.449107543491674f, -0.00214819013073839f, 0.000643025354697729f,
    // f 69/46/144 18/61/144 19/63/144
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    0.508540796733557f, 0.119853570256046f, 0.00146814843219253f,
    0.484745555616156f, 0.0814126274232763f, 0.00148564257346673f,
    // f 69/46/145 19/63/145 65/62/145
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    0.484745555616156f, 0.0814126274232763f, 0.00148564257346673f,
    0.432437762659421f, 0.0636691169992007f, -0.00358921113048292f,
    // f 63/56/146 15/55/146 69/57/146
    0.433433065430731f, 0.0636681853585412f, -0.000804123136505047f,
    0.485860729485664f, 0.0814109711732149f, -0.000430224685129909f,
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    // f 15/55/147 16/58/147 69/57/147
    0.485860729485664f, 0.0814109711732149f, -0.000430224685129909f,
    0.509260437385263f, 0.119852431584128f, -0.000412730543855713f,
    0.459171398417115f, 0.104245587254968f, -0.0026205118757851f,
    // f 68/163/148 11/164/148 12/52/148
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    0.523134844150133f, -0.0687386554924939f, 0.000527708944168409f,
    0.523448392989894f, -0.0570706843563778f, -0.000407761793671325f,
    // f 68/163/149 12/52/149 62/51/149
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    0.523448392989894f, -0.0570706843563778f, -0.000407761793671325f,
    0.432655559542503f, -0.013644842901122f, -0.000704644617188462f,
    // f 66/48/150 10/50/150 68/45/150
    0.431727438414311f, -0.0136441182917201f, -0.0036689181646908f,
    0.522840652732966f, -0.0570703738094913f, 0.00146317968200814f,
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    // f 10/50/151 11/165/151 68/45/151
    0.522840652732966f, -0.0570703738094913f, 0.00146317968200814f,
    0.523134844150133f, -0.0687386554924939f, 0.000527708944168409f,
    0.452839036854f, -0.0385041211673843f, 0.000527708944168409f,
    // f 32/101/152 60/111/152 33/39/152
    0.0786184448419074f, 0.0773168245369086f, -0.0275211963935839f,
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    // f 60/111/153 58/40/153 33/39/153
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.319101295562951f, 0.0208584689261493f, -0.0110184242968014f,
    0.0783592417072885f, 0.0362055925425465f, -0.0425383121852347f,
    // f 29/38/154 56/36/154 60/109/154
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    0.320263879590468f, 0.0208578478323763f, 0.00766593967781369f,
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    // f 29/38/155 60/109/155 30/96/155
    0.0795929409718203f, 0.0362044538706293f, 0.0411375112324228f,
    0.225241501064247f, 0.081554443834789f, -0.00152024425682985f,
    0.0794127202620074f, 0.0773160999275068f, 0.0261204989564008f,
    // f 59/160/156 81/107/156 82/35/156
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    0.225508985449173f, -0.0357291777050329f, 0.0133824867649512f,
    // f 59/160/157 82/35/157 27/34/157
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    0.225508985449173f, -0.0357291777050329f, 0.0133824867649512f,
    0.07935620072866f, -0.0511002134316924f, 0.030721147564628f,
    // f 22/42/158 80/43/158 59/161/158
    0.0784288042098699f, -0.0510994888222905f, -0.03212194851744f,
    0.224691004950069f, -0.0357288671581463f, -0.0159687486992548f,
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    // f 80/43/159 81/106/159 59/161/159
    0.224691004950069f, -0.0357288671581463f, -0.0159687486992548f,
    0.20979790088177f, -0.0512464810152453f, -0.00147656066145878f,
    0.118231495140048f, -0.072161917338279f, -0.00131424815543547f,
    // f 74/84/160 41/166/160 51/13/160
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    // f 74/84/161 51/13/161 75/12/161
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
    -0.328992665987411f, 0.065612001836925f, -0.0325654094713965f,
    -0.431096341651415f, 0.0331184459436094f, -0.0204479734748511f,
    // f 73/10/162 50/11/162 74/83/162
    -0.430652156088056f, 0.0331181353967229f, 0.0231857273631183f,
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
    // f 50/11/163 41/167/163 74/83/163
    -0.32819041986389f, 0.0656114842587808f, 0.0344694484849752f,
    -0.35273542468099f, 0.0813460668739313f, 0.000595718712317209f,
    -0.424069907796918f, 0.0553227553613475f, 0.000884734348042398f,
  };
  public static float normals[] = {
    // f 45/1/1 46/2/1 71/3/1
    -0.244219008258754f, -0.831609028122523f, -0.498782016867312f,
    -0.244219008258754f, -0.831609028122523f, -0.498782016867312f,
    -0.244219008258754f, -0.831609028122523f, -0.498782016867312f,
    // f 71/4/2 46/5/2 48/6/2
    -0.240380976820304f, -0.708448931684981f, 0.663563936013196f,
    -0.240380976820304f, -0.708448931684981f, 0.663563936013196f,
    -0.240380976820304f, -0.708448931684981f, 0.663563936013196f,
    // f 48/6/3 46/5/3 47/7/3
    -0.302321986643868f, -0.698607969136548f, 0.648496971350377f,
    -0.302321986643868f, -0.698607969136548f, 0.648496971350377f,
    -0.302321986643868f, -0.698607969136548f, 0.648496971350377f,
    // f 48/6/4 49/8/4 72/9/4
    -0.288230934817446f, -0.0807179817458726f, 0.954152784221235f,
    -0.288230934817446f, -0.0807179817458726f, 0.954152784221235f,
    -0.288230934817446f, -0.0807179817458726f, 0.954152784221235f,
    // f 72/9/5 49/8/5 73/10/5
    -0.293095983774207f, -0.0288399984034177f, 0.955647947095331f,
    -0.293095983774207f, -0.0288399984034177f, 0.955647947095331f,
    -0.293095983774207f, -0.0288399984034177f, 0.955647947095331f,
    // f 73/10/6 49/8/6 50/11/6
    -0.208548984522955f, 0.339051974837937f, 0.917360931919896f,
    -0.208548984522955f, 0.339051974837937f, 0.917360931919896f,
    -0.208548984522955f, 0.339051974837937f, 0.917360931919896f,
    // f 75/12/7 51/13/7 52/14/7
    -0.216816958894436f, 0.340049935531129f, -0.915071826514752f,
    -0.216816958894436f, 0.340049935531129f, -0.915071826514752f,
    -0.216816958894436f, 0.340049935531129f, -0.915071826514752f,
    // f 75/12/8 52/14/8 76/15/8
    -0.302713042405711f, -0.0308560043224791f, -0.952582133442954f,
    -0.302713042405711f, -0.0308560043224791f, -0.952582133442954f,
    -0.302713042405711f, -0.0308560043224791f, -0.952582133442954f,
    // f 76/15/9 52/14/9 45/1/9
    -0.291346945553962f, -0.140200973799665f, -0.946287823160587f,
    -0.291346945553962f, -0.140200973799665f, -0.946287823160587f,
    -0.291346945553962f, -0.140200973799665f, -0.946287823160587f,
    // f 61/16/10 62/17/10 56/18/10
    -0.0260390015004454f, -0.314992018150786f, 0.948737054669077f,
    -0.0260390015004454f, -0.314992018150786f, 0.948737054669077f,
    -0.0260390015004454f, -0.314992018150786f, 0.948737054669077f,
    // f 56/18/11 62/17/11 14/19/11
    0.0604270136526091f, -0.0450780101847239f, 0.997154225292564f,
    0.0604270136526091f, -0.0450780101847239f, 0.997154225292564f,
    0.0604270136526091f, -0.0450780101847239f, 0.997154225292564f,
    // f 56/18/12 14/19/12 63/20/12
    0.060173986624287f, 0.0382759914918605f, 0.997453778282009f,
    0.060173986624287f, 0.0382759914918605f, 0.997453778282009f,
    0.060173986624287f, 0.0382759914918605f, 0.997453778282009f,
    // f 56/18/13 63/20/13 64/21/13
    -0.0906639963306015f, 0.418467983063555f, 0.903694963425206f,
    -0.0906639963306015f, 0.418467983063555f, 0.903694963425206f,
    -0.0906639963306015f, 0.418467983063555f, 0.903694963425206f,
    // f 64/22/14 65/23/14 58/24/14
    -0.0794959814206386f, 0.371017913287744f, -0.925216783763447f,
    -0.0794959814206386f, 0.371017913287744f, -0.925216783763447f,
    -0.0794959814206386f, 0.371017913287744f, -0.925216783763447f,
    // f 58/24/15 65/23/15 20/25/15
    0.101021023645691f, -0.0955920223749405f, -0.990281231792184f,
    0.101021023645691f, -0.0955920223749405f, -0.990281231792184f,
    0.101021023645691f, -0.0955920223749405f, -0.990281231792184f,
    // f 58/24/16 20/25/16 66/26/16
    0.100115020540501f, 0.116311023863419f, -0.98815420273863f,
    0.100115020540501f, 0.116311023863419f, -0.98815420273863f,
    0.100115020540501f, 0.116311023863419f, -0.98815420273863f,
    // f 61/27/17 58/24/17 66/26/17
    -0.0351470132752227f, -0.316646119599004f, -0.947892358024225f,
    -0.0351470132752227f, -0.316646119599004f, -0.947892358024225f,
    -0.0351470132752227f, -0.316646119599004f, -0.947892358024225f,
    // f 40/28/18 70/29/18 41/30/18
    -0.075331978197083f, 0.809955765578991f, 0.581632831661233f,
    -0.075331978197083f, 0.809955765578991f, 0.581632831661233f,
    -0.075331978197083f, 0.809955765578991f, 0.581632831661233f,
    // f 41/31/19 70/32/19 42/33/19
    -0.0798329958382262f, 0.802712958153772f, -0.590997969190686f,
    -0.0798329958382262f, 0.802712958153772f, -0.590997969190686f,
    -0.0798329958382262f, 0.802712958153772f, -0.590997969190686f,
    // f 27/34/20 82/35/20 56/36/20
    0.129095943456893f, -0.116688948891068f, 0.984742568689743f,
    0.129095943456893f, -0.116688948891068f, 0.984742568689743f,
    0.129095943456893f, -0.116688948891068f, 0.984742568689743f,
    // f 27/34/21 56/36/21 28/37/21
    0.167752057880341f, -0.256649088552934f, 0.951835328416559f,
    0.167752057880341f, -0.256649088552934f, 0.951835328416559f,
    0.167752057880341f, -0.256649088552934f, 0.951835328416559f,
    // f 28/37/22 56/36/22 29/38/22
    0.138020965816283f, 0.00434999892263373f, 0.990419754702275f,
    0.138020965816283f, 0.00434999892263373f, 0.990419754702275f,
    0.138020965816283f, 0.00434999892263373f, 0.990419754702275f,
    // f 33/39/23 58/40/23 34/41/23
    0.130090939829962f, 0.00434599798987644f, -0.991492541412001f,
    0.130090939829962f, 0.00434599798987644f, -0.991492541412001f,
    0.130090939829962f, 0.00434599798987644f, -0.991492541412001f,
    // f 34/41/24 58/40/24 22/42/24
    0.160069053264427f, -0.255764085107815f, -0.953395317250925f,
    0.160069053264427f, -0.255764085107815f, -0.953395317250925f,
    0.160069053264427f, -0.255764085107815f, -0.953395317250925f,
    // f 80/43/25 22/42/25 58/40/25
    0.121039041040654f, -0.115696039229005f, -0.985882334282687f,
    0.121039041040654f, -0.115696039229005f, -0.985882334282687f,
    0.121039041040654f, -0.115696039229005f, -0.985882334282687f,
    // f 61/27/26 66/26/26 68/44/26
    -0.0343790111363605f, -0.194674063060585f, -0.980265317536416f,
    -0.0343790111363605f, -0.194674063060585f, -0.980265317536416f,
    -0.0343790111363605f, -0.194674063060585f, -0.980265317536416f,
    // f 61/16/27 68/45/27 62/17/27
    -0.0221260107212938f, 0.0315740152993823f, 0.999256484195842f,
    -0.0221260107212938f, 0.0315740152993823f, 0.999256484195842f,
    -0.0221260107212938f, 0.0315740152993823f, 0.999256484195842f,
    // f 64/21/28 63/20/28 69/46/28
    -0.0358300085811806f, 0.0673590161322842f, 0.997085238798952f,
    -0.0358300085811806f, 0.0673590161322842f, 0.997085238798952f,
    -0.0358300085811806f, 0.0673590161322842f, 0.997085238798952f,
    // f 64/22/29 69/47/29 65/23/29
    -0.0263769920933791f, 0.041223987642926f, -0.99880170060474f,
    -0.0263769920933791f, 0.041223987642926f, -0.99880170060474f,
    -0.0263769920933791f, 0.041223987642926f, -0.99880170060474f,
    // f 66/48/30 9/49/30 10/50/30
    0.0876110216652884f, 0.0663560164091481f, -0.993942245791511f,
    0.0876110216652884f, 0.0663560164091481f, -0.993942245791511f,
    0.0876110216652884f, 0.0663560164091481f, -0.993942245791511f,
    // f 62/51/31 12/52/31 13/53/31
    -0.00463700076473887f, -0.00285800047134433f, 0.999985164918567f,
    -0.00463700076473887f, -0.00285800047134433f, 0.999985164918567f,
    -0.00463700076473887f, -0.00285800047134433f, 0.999985164918567f,
    // f 62/51/32 13/53/32 14/54/32
    -0.00889499907743298f, -0.0555389942396347f, 0.998416896446701f,
    -0.00889499907743298f, -0.0555389942396347f, 0.998416896446701f,
    -0.00889499907743298f, -0.0555389942396347f, 0.998416896446701f,
    // f 14/54/33 15/55/33 63/56/33
    -0.0241219982819831f, 0.0502379964219495f, 0.998445928888687f,
    -0.0241219982819831f, 0.0502379964219495f, 0.998445928888687f,
    -0.0241219982819831f, 0.0502379964219495f, 0.998445928888687f,
    // f 69/57/34 16/58/34 17/59/34
    -0.0157509993395134f, -0.0903119962129471f, 0.995788958243582f,
    -0.0157509993395134f, -0.0903119962129471f, 0.995788958243582f,
    -0.0157509993395134f, -0.0903119962129471f, 0.995788958243582f,
    // f 69/46/35 17/60/35 18/61/35
    0.109430970805681f, -0.0867489768568508f, -0.990201735831046f,
    0.109430970805681f, -0.0867489768568508f, -0.990201735831046f,
    0.109430970805681f, -0.0867489768568508f, -0.990201735831046f,
    // f 65/62/36 19/63/36 20/64/36
    0.129470991263945f, -0.0995049932859008f, -0.986577933430656f,
    0.129470991263945f, -0.0995049932859008f, -0.986577933430656f,
    0.129470991263945f, -0.0995049932859008f, -0.986577933430656f,
    // f 20/64/37 9/49/37 66/48/37
    0.0911629675519605f, 0.114981959073961f, -0.989175647918323f,
    0.0911629675519605f, 0.114981959073961f, -0.989175647918323f,
    0.0911629675519605f, 0.114981959073961f, -0.989175647918323f,
    // f 1/65/38 84/66/38 87/67/38
    -0.487829198657262f, -0.621879253246074f, -0.61260824947067f,
    -0.487829198657262f, -0.621879253246074f, -0.61260824947067f,
    -0.487829198657262f, -0.621879253246074f, -0.61260824947067f,
    // f 87/67/39 84/66/39 71/3/39
    -0.444223903530349f, -0.690211850110505f, -0.571202875955027f,
    -0.444223903530349f, -0.690211850110505f, -0.571202875955027f,
    -0.444223903530349f, -0.690211850110505f, -0.571202875955027f,
    // f 87/68/40 71/4/40 85/69/40
    -0.441144127492436f, -0.701730202802865f, 0.559434161678734f,
    -0.441144127492436f, -0.701730202802865f, 0.559434161678734f,
    -0.441144127492436f, -0.701730202802865f, 0.559434161678734f,
    // f 1/70/41 87/68/41 85/69/41
    -0.483987812075071f, -0.637007752659811f, 0.599980767036813f,
    -0.483987812075071f, -0.637007752659811f, 0.599980767036813f,
    -0.483987812075071f, -0.637007752659811f, 0.599980767036813f,
    // f 1/70/42 85/69/42 2/71/42
    -0.228498941496738f, 0.273603929948374f, 0.934306760786668f,
    -0.228498941496738f, 0.273603929948374f, 0.934306760786668f,
    -0.228498941496738f, 0.273603929948374f, 0.934306760786668f,
    // f 3/72/43 72/9/43 4/73/43
    -0.568325290820651f, -0.589271301539041f, 0.574252293853588f,
    -0.568325290820651f, -0.589271301539041f, 0.574252293853588f,
    -0.568325290820651f, -0.589271301539041f, 0.574252293853588f,
    // f 4/73/44 72/9/44 73/10/44
    -0.585514980807405f, 0.019501999360744f, 0.810426973435015f,
    -0.585514980807405f, 0.019501999360744f, 0.810426973435015f,
    -0.585514980807405f, 0.019501999360744f, 0.810426973435015f,
    // f 4/73/45 73/10/45 5/74/45
    -0.335194026901668f, -0.388329031166124f, 0.858397068892376f,
    -0.335194026901668f, -0.388329031166124f, 0.858397068892376f,
    -0.335194026901668f, -0.388329031166124f, 0.858397068892376f,
    // f 5/74/46 73/10/46 86/75/46
    -0.428009153901205f, 0.643093231239969f, 0.635011228333886f,
    -0.428009153901205f, 0.643093231239969f, 0.635011228333886f,
    -0.428009153901205f, 0.643093231239969f, 0.635011228333886f,
    // f 5/76/47 86/77/47 75/12/47
    -0.433168960334287f, 0.642933941125899f, -0.631664942157812f,
    -0.433168960334287f, 0.642933941125899f, -0.631664942157812f,
    -0.433168960334287f, 0.642933941125899f, -0.631664942157812f,
    // f 5/78/48 75/12/48 6/79/48
    -0.337710100673085f, -0.397427118475029f, -0.853231254352541f,
    -0.337710100673085f, -0.397427118475029f, -0.853231254352541f,
    -0.337710100673085f, -0.397427118475029f, -0.853231254352541f,
    // f 6/79/49 75/12/49 76/15/49
    -0.594241721630799f, 0.0183289914138868f, -0.804077623334348f,
    -0.594241721630799f, 0.0183289914138868f, -0.804077623334348f,
    -0.594241721630799f, 0.0183289914138868f, -0.804077623334348f,
    // f 6/79/50 76/15/50 7/80/50
    -0.573080678864589f, -0.594162667050942f, -0.56440168372801f,
    -0.573080678864589f, -0.594162667050942f, -0.56440168372801f,
    -0.573080678864589f, -0.594162667050942f, -0.56440168372801f,
    // f 84/66/51 1/81/51 8/82/51
    -0.225745047378247f, 0.285683059957739f, -0.931356195468402f,
    -0.225745047378247f, 0.285683059957739f, -0.931356195468402f,
    -0.225745047378247f, 0.285683059957739f, -0.931356195468402f,
    // f 84/66/52 45/1/52 71/3/52
    -0.341197130514239f, -0.636127243330485f, -0.692045264720167f,
    -0.341197130514239f, -0.636127243330485f, -0.692045264720167f,
    -0.341197130514239f, -0.636127243330485f, -0.692045264720167f,
    // f 85/69/53 71/4/53 48/6/53
    -0.287691967853157f, -0.624690930196727f, 0.725943918882668f,
    -0.287691967853157f, -0.624690930196727f, 0.725943918882668f,
    -0.287691967853157f, -0.624690930196727f, 0.725943918882668f,
    // f 85/69/54 48/6/54 72/9/54
    -0.318840024144799f, -0.187561014203433f, 0.929065070355313f,
    -0.318840024144799f, -0.187561014203433f, 0.929065070355313f,
    -0.318840024144799f, -0.187561014203433f, 0.929065070355313f,
    // f 73/10/55 74/83/55 86/75/55
    -0.373760773476185f, 0.710095569634994f, 0.596713638351963f,
    -0.373760773476185f, 0.710095569634994f, 0.596713638351963f,
    -0.373760773476185f, 0.710095569634994f, 0.596713638351963f,
    // f 86/77/56 74/84/56 75/12/56
    -0.387539093196381f, 0.699495168216367f, -0.600433144393681f,
    -0.387539093196381f, 0.699495168216367f, -0.600433144393681f,
    -0.387539093196381f, 0.699495168216367f, -0.600433144393681f,
    // f 76/15/57 45/1/57 84/66/57
    -0.315514052968346f, -0.194082032582397f, -0.92886115593676f,
    -0.315514052968346f, -0.194082032582397f, -0.92886115593676f,
    -0.315514052968346f, -0.194082032582397f, -0.92886115593676f,
    // f 36/85/58 77/86/58 35/87/58
    -0.0515300094819091f, -0.793731146052497f, -0.606082111523664f,
    -0.0515300094819091f, -0.793731146052497f, -0.606082111523664f,
    -0.0515300094819091f, -0.793731146052497f, -0.606082111523664f,
    // f 35/87/59 77/86/59 78/88/59
    -0.0218040039410632f, -0.906023163763249f, -0.422666076396689f,
    -0.0218040039410632f, -0.906023163763249f, -0.422666076396689f,
    -0.0218040039410632f, -0.906023163763249f, -0.422666076396689f,
    // f 35/89/60 78/90/60 79/91/60
    -0.0161390033429447f, -0.920677190704029f, 0.389991080780616f,
    -0.0161390033429447f, -0.920677190704029f, 0.389991080780616f,
    -0.0161390033429447f, -0.920677190704029f, 0.389991080780616f,
    // f 35/89/61 79/91/61 37/92/61
    -0.0494480207760353f, -0.798783335616078f, 0.599583251920352f,
    -0.0494480207760353f, -0.798783335616078f, 0.599583251920352f,
    -0.0494480207760353f, -0.798783335616078f, 0.599583251920352f,
    // f 70/93/62 83/94/62 88/95/62
    0.00760800039308257f, -0.000119000006148373f, 0.999971051665506f,
    0.00760800039308257f, -0.000119000006148373f, 0.999971051665506f,
    0.00760800039308257f, -0.000119000006148373f, 0.999971051665506f,
    // f 30/96/63 60/97/63 89/98/63
    0.0954929548577216f, 0.776471632939428f, 0.622874705549133f,
    0.0954929548577216f, 0.776471632939428f, 0.622874705549133f,
    0.0954929548577216f, 0.776471632939428f, 0.622874705549133f,
    // f 89/99/64 60/100/64 32/101/64
    0.0883169954678369f, 0.776008960177549f, -0.624507967952125f,
    0.0883169954678369f, 0.776008960177549f, -0.624507967952125f,
    0.0883169954678369f, 0.776008960177549f, -0.624507967952125f,
    // f 30/96/65 89/98/65 31/102/65
    0.0692490098776449f, 0.767252109440465f, 0.637596090946394f,
    0.0692490098776449f, 0.767252109440465f, 0.637596090946394f,
    0.0692490098776449f, 0.767252109440465f, 0.637596090946394f,
    // f 31/102/66 89/98/66 90/103/66
    0.00633300095112499f, 0.0674560101309154f, 0.997702149840408f,
    0.00633300095112499f, 0.0674560101309154f, 0.997702149840408f,
    0.00633300095112499f, 0.0674560101309154f, 0.997702149840408f,
    // f 31/104/67 89/99/67 32/101/67
    0.069124979313489f, 0.769543769704442f, -0.634841810015681f,
    0.069124979313489f, 0.769543769704442f, -0.634841810015681f,
    0.069124979313489f, 0.769543769704442f, -0.634841810015681f,
    // f 80/43/68 53/105/68 81/106/68
    0.169229072161492f, -0.754558321753546f, -0.634037270361792f,
    0.169229072161492f, -0.754558321753546f, -0.634037270361792f,
    0.169229072161492f, -0.754558321753546f, -0.634037270361792f,
    // f 81/107/69 55/108/69 82/35/69
    0.174083003161608f, -0.767292013935174f, 0.617218011209605f,
    0.174083003161608f, -0.767292013935174f, 0.617218011209605f,
    0.174083003161608f, -0.767292013935174f, 0.617218011209605f,
    // f 82/35/70 55/108/70 56/18/70
    0.148416948187875f, -0.149771947714847f, 0.977517658750111f,
    0.148416948187875f, -0.149771947714847f, 0.977517658750111f,
    0.148416948187875f, -0.149771947714847f, 0.977517658750111f,
    // f 60/109/71 56/18/71 57/110/71
    0.0749750262108989f, 0.262952091926753f, 0.96189133627246f,
    0.0749750262108989f, 0.262952091926753f, 0.96189133627246f,
    0.0749750262108989f, 0.262952091926753f, 0.96189133627246f,
    // f 60/111/72 57/112/72 58/24/72
    0.0634970169890736f, 0.249409066731151f, -0.966314258544179f,
    0.0634970169890736f, 0.249409066731151f, -0.966314258544179f,
    0.0634970169890736f, 0.249409066731151f, -0.966314258544179f,
    // f 53/105/73 80/43/73 58/40/73
    0.140112927723535f, -0.14812192359214f, -0.97899349499172f,
    0.140112927723535f, -0.14812192359214f, -0.97899349499172f,
    0.140112927723535f, -0.14812192359214f, -0.97899349499172f,
    // f 53/105/74 61/27/74 67/113/74
    0.0944919872235503f, -0.791937892920501f, 0.603245918433918f,
    0.0944919872235503f, -0.791937892920501f, 0.603245918433918f,
    0.0944919872235503f, -0.791937892920501f, 0.603245918433918f,
    // f 53/105/75 67/113/75 54/114/75
    0.199865015999895f, -0.52420204196421f, -0.827808066268936f,
    0.199865015999895f, -0.52420204196421f, -0.827808066268936f,
    0.199865015999895f, -0.52420204196421f, -0.827808066268936f,
    // f 54/115/76 67/116/76 55/108/76
    0.220946056812309f, -0.600998154535877f, 0.768104197504194f,
    0.220946056812309f, -0.600998154535877f, 0.768104197504194f,
    0.220946056812309f, -0.600998154535877f, 0.768104197504194f,
    // f 55/108/77 67/116/77 61/16/77
    0.0945870030944612f, -0.808920026464224f, -0.580260018983497f,
    0.0945870030944612f, -0.808920026464224f, -0.580260018983497f,
    0.0945870030944612f, -0.808920026464224f, -0.580260018983497f,
    // f 55/108/78 61/16/78 56/18/78
    0.0961430151132025f, -0.117195018422472f, 0.988444155378491f,
    0.0961430151132025f, -0.117195018422472f, 0.988444155378491f,
    0.0961430151132025f, -0.117195018422472f, 0.988444155378491f,
    // f 56/18/79 64/21/79 57/110/79
    0.039125005676354f, 0.238967034669937f, 0.97023914076473f,
    0.039125005676354f, 0.238967034669937f, 0.97023914076473f,
    0.039125005676354f, 0.238967034669937f, 0.97023914076473f,
    // f 57/112/80 64/22/80 58/24/80
    0.0227320077755187f, 0.222917076249133f, -0.974572333353986f,
    0.0227320077755187f, 0.222917076249133f, -0.974572333353986f,
    0.0227320077755187f, 0.222917076249133f, -0.974572333353986f,
    // f 61/27/81 53/105/81 58/24/81
    0.0865799739753624f, -0.115330965333247f, -0.989546702557149f,
    0.0865799739753624f, -0.115330965333247f, -0.989546702557149f,
    0.0865799739753624f, -0.115330965333247f, -0.989546702557149f,
    // f 70/93/82 31/117/82 83/94/82
    0.000378000051486446f, 0.0172500023495799f, 0.999851136187233f,
    0.000378000051486446f, 0.0172500023495799f, 0.999851136187233f,
    0.000378000051486446f, 0.0172500023495799f, 0.999851136187233f,
    // f 94/118/83 36/85/83 35/87/83
    -0.0635289948906808f, -0.791119936374182f, -0.608352951073216f,
    -0.0635289948906808f, -0.791119936374182f, -0.608352951073216f,
    -0.0635289948906808f, -0.791119936374182f, -0.608352951073216f,
    // f 94/119/84 35/87/84 46/2/84
    -0.0983830001126485f, -0.840886000962815f, -0.532195000609363f,
    -0.0983830001126485f, -0.840886000962815f, -0.532195000609363f,
    -0.0983830001126485f, -0.840886000962815f, -0.532195000609363f,
    // f 46/5/85 35/89/85 47/120/85
    -0.0747539875174307f, -0.818697863292205f, 0.569337904930826f,
    -0.0747539875174307f, -0.818697863292205f, 0.569337904930826f,
    -0.0747539875174307f, -0.818697863292205f, 0.569337904930826f,
    // f 47/121/86 35/89/86 37/92/86
    -0.0591720232562665f, -0.796622313094936f, 0.601574236435566f,
    -0.0591720232562665f, -0.796622313094936f, 0.601574236435566f,
    -0.0591720232562665f, -0.796622313094936f, 0.601574236435566f,
    // f 48/6/87 38/122/87 49/8/87
    -0.0372730029525065f, -0.192249015228622f, 0.980638077679287f,
    -0.0372730029525065f, -0.192249015228622f, 0.980638077679287f,
    -0.0372730029525065f, -0.192249015228622f, 0.980638077679287f,
    // f 49/8/88 38/122/88 39/123/88
    -0.00713199765789515f, 0.00545799820762643f, 0.999959671619298f,
    -0.00713199765789515f, 0.00545799820762643f, 0.999959671619298f,
    -0.00713199765789515f, 0.00545799820762643f, 0.999959671619298f,
    // f 50/11/89 40/28/89 41/30/89
    -0.0505259951395006f, 0.891285914260082f, 0.450617956651456f,
    -0.0505259951395006f, 0.891285914260082f, 0.450617956651456f,
    -0.0505259951395006f, 0.891285914260082f, 0.450617956651456f,
    // f 41/31/90 42/33/90 51/13/90
    -0.0542389829332457f, 0.886535721044044f, -0.459469855423927f,
    -0.0542389829332457f, 0.886535721044044f, -0.459469855423927f,
    -0.0542389829332457f, 0.886535721044044f, -0.459469855423927f,
    // f 52/14/91 43/124/91 44/125/91
    -0.0152379992236697f, 0.00546699972147277f, -0.999868949059678f,
    -0.0152379992236697f, 0.00546699972147277f, -0.999868949059678f,
    -0.0152379992236697f, 0.00546699972147277f, -0.999868949059678f,
    // f 52/14/92 44/125/92 45/1/92
    -0.0519919981202813f, -0.237561991411184f, -0.969979964931345f,
    -0.0519919981202813f, -0.237561991411184f, -0.969979964931345f,
    -0.0519919981202813f, -0.237561991411184f, -0.969979964931345f,
    // f 45/1/93 94/126/93 46/2/93
    -0.101068002913083f, -0.839917024208929f, -0.53322101536903f,
    -0.101068002913083f, -0.839917024208929f, -0.53322101536903f,
    -0.101068002913083f, -0.839917024208929f, -0.53322101536903f,
    // f 94/127/94 95/128/94 97/129/94
    0.0433759860501984f, 0.35158188693058f, -0.935151699253391f,
    0.0433759860501984f, 0.35158188693058f, -0.935151699253391f,
    0.0433759860501984f, 0.35158188693058f, -0.935151699253391f,
    // f 47/130/95 96/131/95 98/132/95
    -0.0453589829365662f, -0.353363867069309f, -0.934385648496799f,
    -0.0453589829365662f, -0.353363867069309f, -0.934385648496799f,
    -0.0453589829365662f, -0.353363867069309f, -0.934385648496799f,
    // f 45/1/96 44/125/96 94/133/96
    -0.131321972657915f, 0.740079845910582f, -0.659572862672657f,
    -0.131321972657915f, 0.740079845910582f, -0.659572862672657f,
    -0.131321972657915f, 0.740079845910582f, -0.659572862672657f,
    // f 44/125/97 36/85/97 94/133/97
    -0.0187639968072968f, -0.277002952867813f, -0.960685836538839f,
    -0.0187639968072968f, -0.277002952867813f, -0.960685836538839f,
    -0.0187639968072968f, -0.277002952867813f, -0.960685836538839f,
    // f 51/13/98 42/33/98 43/124/98
    -0.0445770118406812f, 0.381057101217544f, -0.923476245296563f,
    -0.0445770118406812f, 0.381057101217544f, -0.923476245296563f,
    -0.0445770118406812f, 0.381057101217544f, -0.923476245296563f,
    // f 51/13/99 43/124/99 52/14/99
    -0.0514269947288876f, 0.347067964426577f, -0.936428904018852f,
    -0.0514269947288876f, 0.347067964426577f, -0.936428904018852f,
    -0.0514269947288876f, 0.347067964426577f, -0.936428904018852f,
    // f 49/8/100 39/123/100 50/11/100
    -0.0438170224419016f, 0.346823177633513f, 0.936906479858325f,
    -0.0438170224419016f, 0.346823177633513f, 0.936906479858325f,
    -0.0438170224419016f, 0.346823177633513f, 0.936906479858325f,
    // f 39/123/101 40/28/101 50/11/101
    -0.0370749881422714f, 0.380848878192742f, 0.9238937045102f,
    -0.0370749881422714f, 0.380848878192742f, 0.9238937045102f,
    -0.0370749881422714f, 0.380848878192742f, 0.9238937045102f,
    // f 47/134/102 37/92/102 38/122/102
    -0.0109749991825766f, -0.276955979372181f, 0.960819928437654f,
    -0.0109749991825766f, -0.276955979372181f, 0.960819928437654f,
    -0.0109749991825766f, -0.276955979372181f, 0.960819928437654f,
    // f 47/134/103 38/122/103 48/6/103
    -0.0465200220477975f, -0.0119000056399138f, 0.998846473395408f,
    -0.0465200220477975f, -0.0119000056399138f, 0.998846473395408f,
    -0.0465200220477975f, -0.0119000056399138f, 0.998846473395408f,
    // f 44/125/104 34/41/104 22/42/104
    0.0653929841974603f, -0.258818937455117f, -0.963709767114744f,
    0.0653929841974603f, -0.258818937455117f, -0.963709767114744f,
    0.0653929841974603f, -0.258818937455117f, -0.963709767114744f,
    // f 44/125/105 22/42/105 36/85/105
    0.0620760197993669f, -0.276346088141566f, -0.95905130589282f,
    0.0620760197993669f, -0.276346088141566f, -0.95905130589282f,
    0.0620760197993669f, -0.276346088141566f, -0.95905130589282f,
    // f 43/124/106 33/39/106 34/41/106
    0.0595890048895462f, 0.00444900036506051f, -0.998213081907879f,
    0.0595890048895462f, 0.00444900036506051f, -0.998213081907879f,
    0.0595890048895462f, 0.00444900036506051f, -0.998213081907879f,
    // f 43/124/107 34/41/107 44/125/107
    0.0597940120598256f, 0.00538700108650166f, -0.998196201325713f,
    0.0597940120598256f, 0.00538700108650166f, -0.998196201325713f,
    0.0597940120598256f, 0.00538700108650166f, -0.998196201325713f,
    // f 42/33/108 32/101/108 33/39/108
    0.0443750108800884f, 0.342522083981287f, -0.938461230096645f,
    0.0443750108800884f, 0.342522083981287f, -0.938461230096645f,
    0.0443750108800884f, 0.342522083981287f, -0.938461230096645f,
    // f 42/33/109 33/39/109 43/124/109
    0.0516669955166225f, 0.380481966983869f, -0.923343919877297f,
    0.0516669955166225f, 0.380481966983869f, -0.923343919877297f,
    0.0516669955166225f, 0.380481966983869f, -0.923343919877297f,
    // f 70/135/110 31/136/110 42/33/110
    0.00511199815701765f, 0.77524272050877f, -0.631642772279557f,
    0.00511199815701765f, 0.77524272050877f, -0.631642772279557f,
    0.00511199815701765f, 0.77524272050877f, -0.631642772279557f,
    // f 31/136/111 32/101/111 42/33/111
    0.0263360167261939f, 0.730211463762559f, -0.682713433596218f,
    0.0263360167261939f, 0.730211463762559f, -0.682713433596218f,
    0.0263360167261939f, 0.730211463762559f, -0.682713433596218f,
    // f 40/28/112 30/96/112 31/117/112
    0.0317240007955111f, 0.732074018357488f, 0.680486017063868f,
    0.0317240007955111f, 0.732074018357488f, 0.680486017063868f,
    0.0317240007955111f, 0.732074018357488f, 0.680486017063868f,
    // f 40/28/113 31/117/113 70/93/113
    0.00550899673090171f, 0.78564453379003f, 0.618653632884238f,
    0.00550899673090171f, 0.78564453379003f, 0.618653632884238f,
    0.00550899673090171f, 0.78564453379003f, 0.618653632884238f,
    // f 39/123/114 29/38/114 40/28/114
    0.0591129810745388f, 0.380744878101691f, 0.922788704562323f,
    0.0591129810745388f, 0.380744878101691f, 0.922788704562323f,
    0.0591129810745388f, 0.380744878101691f, 0.922788704562323f,
    // f 29/38/115 30/96/115 40/28/115
    0.0519660178560205f, 0.342840117803142f, 0.937955322290415f,
    0.0519660178560205f, 0.342840117803142f, 0.937955322290415f,
    0.0519660178560205f, 0.342840117803142f, 0.937955322290415f,
    // f 38/122/116 28/37/116 39/123/116
    0.0678519912090627f, 0.0053839993024464f, 0.997680870739976f,
    0.0678519912090627f, 0.0053839993024464f, 0.997680870739976f,
    0.0678519912090627f, 0.0053839993024464f, 0.997680870739976f,
    // f 28/37/117 29/38/117 39/123/117
    0.0676480235235895f, 0.00444500154568288f, 0.997699346935043f,
    0.0676480235235895f, 0.00444500154568288f, 0.997699346935043f,
    0.0676480235235895f, 0.00444500154568288f, 0.997699346935043f,
    // f 37/92/118 27/34/118 38/122/118
    0.0698400356065544f, -0.276598141018066f, 0.958444488643878f,
    0.0698400356065544f, -0.276598141018066f, 0.958444488643878f,
    0.0698400356065544f, -0.276598141018066f, 0.958444488643878f,
    // f 27/34/119 28/37/119 38/122/119
    0.0731720104822937f, -0.25919903713169f, 0.963048137961952f,
    0.0731720104822937f, -0.25919903713169f, 0.963048137961952f,
    0.0731720104822937f, -0.25919903713169f, 0.963048137961952f,
    // f 79/137/120 25/138/120 27/34/120
    0.179416085592079f, -0.737965352053097f, 0.650551310351432f,
    0.179416085592079f, -0.737965352053097f, 0.650551310351432f,
    0.179416085592079f, -0.737965352053097f, 0.650551310351432f,
    // f 79/137/121 27/34/121 37/92/121
    0.0685820211809776f, -0.51322715850587f, 0.855508264216496f,
    0.0685820211809776f, -0.51322715850587f, 0.855508264216496f,
    0.0685820211809776f, -0.51322715850587f, 0.855508264216496f,
    // f 79/139/122 25/140/122 26/141/122
    -0.03159400476455f, -0.0679530102476885f, -0.997188150381469f,
    -0.03159400476455f, -0.0679530102476885f, -0.997188150381469f,
    -0.03159400476455f, -0.0679530102476885f, -0.997188150381469f,
    // f 79/139/123 26/141/123 93/142/123
    -0.018928997901361f, -0.0262609970884697f, -0.999475889189114f,
    -0.018928997901361f, -0.0262609970884697f, -0.999475889189114f,
    -0.018928997901361f, -0.0262609970884697f, -0.999475889189114f,
    // f 78/143/124 24/144/124 79/145/124
    0.0778450433941866f, -0.918433511974475f, 0.387840216198874f,
    0.0778450433941866f, -0.918433511974475f, 0.387840216198874f,
    0.0778450433941866f, -0.918433511974475f, 0.387840216198874f,
    // f 24/144/125 25/146/125 79/145/125
    0.18603595809308f, -0.783509823504638f, 0.59287686644709f,
    0.18603595809308f, -0.783509823504638f, 0.59287686644709f,
    0.18603595809308f, -0.783509823504638f, 0.59287686644709f,
    // f 77/147/126 21/148/126 24/149/126
    0.177557935023396f, -0.752447724644815f, -0.63426776789229f,
    0.177557935023396f, -0.752447724644815f, -0.63426776789229f,
    0.177557935023396f, -0.752447724644815f, -0.63426776789229f,
    // f 77/147/127 24/149/127 78/150/127
    0.075378993514354f, -0.903165922291156f, -0.422621963637397f,
    0.075378993514354f, -0.903165922291156f, -0.422621963637397f,
    0.075378993514354f, -0.903165922291156f, -0.422621963637397f,
    // f 77/151/128 21/152/128 23/153/128
    0.0255749905214627f, 0.0617709771065991f, -0.997762630211777f,
    0.0255749905214627f, 0.0617709771065991f, -0.997762630211777f,
    0.0255749905214627f, 0.0617709771065991f, -0.997762630211777f,
    // f 77/151/129 23/153/129 92/154/129
    0.0141910036116818f, 0.024330006192109f, -0.999603254404059f,
    0.0141910036116818f, 0.024330006192109f, -0.999603254404059f,
    0.0141910036116818f, 0.024330006192109f, -0.999603254404059f,
    // f 36/85/130 22/42/130 77/151/130
    0.0616539963955533f, -0.513227969995411f, -0.856034949954058f,
    0.0616539963955533f, -0.513227969995411f, -0.856034949954058f,
    0.0616539963955533f, -0.513227969995411f, -0.856034949954058f,
    // f 22/42/131 21/152/131 77/151/131
    0.175518032644251f, -0.739510137540024f, -0.649860120866195f,
    0.175518032644251f, -0.739510137540024f, -0.649860120866195f,
    0.175518032644251f, -0.739510137540024f, -0.649860120866195f,
    // f 91/155/132 54/156/132 81/107/132
    0.0080549997635777f, -0.00855399974893156f, 0.999930970651026f,
    0.0080549997635777f, -0.00855399974893156f, 0.999930970651026f,
    0.0080549997635777f, -0.00855399974893156f, 0.999930970651026f,
    // f 54/156/133 55/108/133 81/107/133
    0.154935921614219f, -0.718353636567747f, 0.678205656879568f,
    0.154935921614219f, -0.718353636567747f, 0.678205656879568f,
    0.154935921614219f, -0.718353636567747f, 0.678205656879568f,
    // f 81/106/134 53/105/134 54/157/134
    0.135307063160066f, -0.666891311298601f, -0.732767342048914f,
    0.135307063160066f, -0.666891311298601f, -0.732767342048914f,
    0.135307063160066f, -0.666891311298601f, -0.732767342048914f,
    // f 81/106/135 54/157/135 91/158/135
    -0.0080549997635777f, 0.00855399974893156f, -0.999930970651026f,
    -0.0080549997635777f, 0.00855399974893156f, -0.999930970651026f,
    -0.0080549997635777f, 0.00855399974893156f, -0.999930970651026f,
    // f 24/159/136 59/160/136 25/138/136
    0.147969003011909f, -0.784048015959297f, 0.602805012270096f,
    0.147969003011909f, -0.784048015959297f, 0.602805012270096f,
    0.147969003011909f, -0.784048015959297f, 0.602805012270096f,
    // f 59/160/137 27/34/137 25/138/137
    0.162668035957367f, -0.72224115964962f, 0.672240148597021f,
    0.162668035957367f, -0.72224115964962f, 0.672240148597021f,
    0.162668035957367f, -0.72224115964962f, 0.672240148597021f,
    // f 21/152/138 22/42/138 59/161/138
    0.149626079482517f, -0.715453380054302f, -0.682450362522847f,
    0.149626079482517f, -0.715453380054302f, -0.682450362522847f,
    0.149626079482517f, -0.715453380054302f, -0.682450362522847f,
    // f 21/152/139 59/161/139 24/162/139
    0.142135999017272f, -0.752743994795528f, -0.642785995555778f,
    0.142135999017272f, -0.752743994795528f, -0.642785995555778f,
    0.142135999017272f, -0.752743994795528f, -0.642785995555778f,
    // f 7/80/140 76/15/140 84/66/140
    -0.815097835134661f, 0.342578930708451f, -0.467177905506504f,
    -0.815097835134661f, 0.342578930708451f, -0.467177905506504f,
    -0.815097835134661f, 0.342578930708451f, -0.467177905506504f,
    // f 7/80/141 84/66/141 8/82/141
    0.0752919679213745f, 0.52905677459197f, -0.845239639880234f,
    0.0752919679213745f, 0.52905677459197f, -0.845239639880234f,
    0.0752919679213745f, 0.52905677459197f, -0.845239639880234f,
    // f 2/71/142 85/69/142 3/72/142
    0.0742040350265018f, 0.521689246252772f, 0.8499024011791f,
    0.0742040350265018f, 0.521689246252772f, 0.8499024011791f,
    0.0742040350265018f, 0.521689246252772f, 0.8499024011791f,
    // f 85/69/143 72/9/143 3/72/143
    -0.808568815703739f, 0.34064392235738f, 0.479768890646769f,
    -0.808568815703739f, 0.34064392235738f, 0.479768890646769f,
    -0.808568815703739f, 0.34064392235738f, 0.479768890646769f,
    // f 69/46/144 18/61/144 19/63/144
    0.102392968703082f, -0.063834980488522f, -0.992693696578254f,
    0.102392968703082f, -0.063834980488522f, -0.992693696578254f,
    0.102392968703082f, -0.063834980488522f, -0.992693696578254f,
    // f 69/46/145 19/63/145 65/62/145
    0.113622045894794f, -0.0511710206692587f, -0.992205400776647f,
    0.113622045894794f, -0.0511710206692587f, -0.992205400776647f,
    0.113622045894794f, -0.0511710206692587f, -0.992205400776647f,
    // f 63/56/146 15/55/146 69/57/146
    -0.0283029924932124f, 0.0626089833942528f, 0.997636735397343f,
    -0.0283029924932124f, 0.0626089833942528f, 0.997636735397343f,
    -0.0283029924932124f, 0.0626089833942528f, 0.997636735397343f,
    // f 15/55/147 16/58/147 69/57/147
    -0.0541089791331197f, 0.0324809874738558f, 0.998006615123314f,
    -0.0541089791331197f, 0.0324809874738558f, 0.998006615123314f,
    -0.0541089791331197f, 0.0324809874738558f, 0.998006615123314f,
    // f 68/163/148 11/164/148 12/52/148
    0.0339629888941893f, 0.0789649741786551f, 0.996298674212877f,
    0.0339629888941893f, 0.0789649741786551f, 0.996298674212877f,
    0.0339629888941893f, 0.0789649741786551f, 0.996298674212877f,
    // f 68/163/149 12/52/149 62/51/149
    0.0333019937723779f, 0.076440985705193f, 0.996517813646702f,
    0.0333019937723779f, 0.076440985705193f, 0.996517813646702f,
    0.0333019937723779f, 0.076440985705193f, 0.996517813646702f,
    // f 66/48/150 10/50/150 68/45/150
    -0.0396970126010446f, -0.199009063171556f, -0.979193310825872f,
    -0.0396970126010446f, -0.199009063171556f, -0.979193310825872f,
    -0.0396970126010446f, -0.199009063171556f, -0.979193310825872f,
    // f 10/50/151 11/165/151 68/45/151
    0.0347250127493949f, 0.0807370296428481f, -0.996130365732319f,
    0.0347250127493949f, 0.0807370296428481f, -0.996130365732319f,
    0.0347250127493949f, 0.0807370296428481f, -0.996130365732319f,
    // f 32/101/152 60/111/152 33/39/152
    0.154842010843819f, 0.338107023678143f, -0.928282065008988f,
    0.154842010843819f, 0.338107023678143f, -0.928282065008988f,
    0.154842010843819f, 0.338107023678143f, -0.928282065008988f,
    // f 60/111/153 58/40/153 33/39/153
    0.143715044973475f, 0.366117114570877f, -0.919404287713826f,
    0.143715044973475f, 0.366117114570877f, -0.919404287713826f,
    0.143715044973475f, 0.366117114570877f, -0.919404287713826f,
    // f 29/38/154 56/36/154 60/109/154
    0.151114946119824f, 0.374998866293803f, 0.914625673889359f,
    0.151114946119824f, 0.374998866293803f, 0.914625673889359f,
    0.151114946119824f, 0.374998866293803f, 0.914625673889359f,
    // f 29/38/155 60/109/155 30/96/155
    0.165679076231777f, 0.339001155980231f, 0.926082426106366f,
    0.165679076231777f, 0.339001155980231f, 0.926082426106366f,
    0.165679076231777f, 0.339001155980231f, 0.926082426106366f,
    // f 59/160/156 81/107/156 82/35/156
    0.176545073047481f, -0.768123317819538f, 0.615482254662606f,
    0.176545073047481f, -0.768123317819538f, 0.615482254662606f,
    0.176545073047481f, -0.768123317819538f, 0.615482254662606f,
    // f 59/160/157 82/35/157 27/34/157
    0.155769983953434f, -0.727994925005971f, 0.667651931222174f,
    0.155769983953434f, -0.727994925005971f, 0.667651931222174f,
    0.155769983953434f, -0.727994925005971f, 0.667651931222174f,
    // f 22/42/158 80/43/158 59/161/158
    0.15054300340024f, -0.71465301614151f, -0.683087015428545f,
    0.15054300340024f, -0.71465301614151f, -0.683087015428545f,
    0.15054300340024f, -0.71465301614151f, -0.683087015428545f,
    // f 80/43/159 81/106/159 59/161/159
    0.171400004354246f, -0.755286019187286f, -0.632586016070215f,
    0.171400004354246f, -0.755286019187286f, -0.632586016070215f,
    0.171400004354246f, -0.755286019187286f, -0.632586016070215f,
    // f 74/84/160 41/166/160 51/13/160
    -0.283791167983704f, 0.771601456731871f, -0.569292336979605f,
    -0.283791167983704f, 0.771601456731871f, -0.569292336979605f,
    -0.283791167983704f, 0.771601456731871f, -0.569292336979605f,
    // f 74/84/161 51/13/161 75/12/161
    -0.301211084531439f, 0.708481198827129f, -0.63822117910946f,
    -0.301211084531439f, 0.708481198827129f, -0.63822117910946f,
    -0.301211084531439f, 0.708481198827129f, -0.63822117910946f,
    // f 73/10/162 50/11/162 74/83/162
    -0.297210037673158f, 0.719038091142398f, 0.628212079629655f,
    -0.297210037673158f, 0.719038091142398f, 0.628212079629655f,
    -0.297210037673158f, 0.719038091142398f, 0.628212079629655f,
    // f 50/11/163 41/167/163 74/83/163
    -0.280939057970516f, 0.776372160200918f, 0.564198116419754f,
    -0.280939057970516f, 0.776372160200918f, 0.564198116419754f,
    -0.280939057970516f, 0.776372160200918f, 0.564198116419754f,
  };
  public static float texCoords[] = {
    // f 45/1/1 46/2/1 71/3/1
    0.297030f, 0.185267f,
    0.301333f, 0.163178f,
    0.243135f, 0.187945f,
    // f 71/4/2 46/5/2 48/6/2
    0.223326f, 0.72363f,
    0.281423f, 0.74039f,
    0.278797f, 0.714508f,
    // f 48/6/3 46/5/3 47/7/3
    0.278797f, 0.714508f,
    0.281423f, 0.74039f,
    0.273861f, 0.729067f,
    // f 48/6/4 49/8/4 72/9/4
    0.278797f, 0.714508f,
    0.290216f, 0.687449f,
    0.218862f, 0.693504f,
    // f 72/9/5 49/8/5 73/10/5
    0.218862f, 0.693504f,
    0.290216f, 0.687449f,
    0.221434f, 0.673795f,
    // f 73/10/6 49/8/6 50/11/6
    0.221434f, 0.673795f,
    0.290216f, 0.687449f,
    0.290102f, 0.652018f,
    // f 75/12/7 51/13/7 52/14/7
    0.243142f, 0.244811f,
    0.313490f, 0.26685f,
    0.312474f, 0.225662f,
    // f 75/12/8 52/14/8 76/15/8
    0.243142f, 0.244811f,
    0.312474f, 0.225662f,
    0.239779f, 0.222326f,
    // f 76/15/9 52/14/9 45/1/9
    0.239779f, 0.222326f,
    0.312474f, 0.225662f,
    0.297030f, 0.185267f,
    // f 61/16/10 62/17/10 56/18/10
    0.769133f, 0.705789f,
    0.800006f, 0.705134f,
    0.724683f, 0.682011f,
    // f 56/18/11 62/17/11 14/19/11
    0.724683f, 0.682011f,
    0.800006f, 0.705134f,
    0.787923f, 0.681709f,
    // f 56/18/12 14/19/12 63/20/12
    0.724683f, 0.682011f,
    0.787923f, 0.681709f,
    0.800527f, 0.653321f,
    // f 56/18/13 63/20/13 64/21/13
    0.724683f, 0.682011f,
    0.800527f, 0.653321f,
    0.764765f, 0.657774f,
    // f 64/22/14 65/23/14 58/24/14
    0.794175f, 0.24222f,
    0.829644f, 0.246174f,
    0.751860f, 0.215992f,
    // f 58/24/15 65/23/15 20/25/15
    0.751860f, 0.215992f,
    0.829644f, 0.246174f,
    0.815694f, 0.214041f,
    // f 58/24/16 20/25/16 66/26/16
    0.751860f, 0.215992f,
    0.815694f, 0.214041f,
    0.824266f, 0.188468f,
    // f 61/27/17 58/24/17 66/26/17
    0.790647f, 0.188639f,
    0.751860f, 0.215992f,
    0.824266f, 0.188468f,
    // f 40/28/18 70/29/18 41/30/18
    0.422953f, 0.645909f,
    0.441200f, 0.625361f,
    0.342026f, 0.63271f,
    // f 41/31/19 70/32/19 42/33/19
    0.366614f, 0.287679f,
    0.467338f, 0.292232f,
    0.448348f, 0.268539f,
    // f 27/34/20 82/35/20 56/36/20
    0.563232f, 0.730236f,
    0.661180f, 0.719935f,
    0.661341f, 0.694434f,
    // f 27/34/21 56/36/21 28/37/21
    0.563232f, 0.730236f,
    0.661341f, 0.694434f,
    0.563362f, 0.703725f,
    // f 28/37/22 56/36/22 29/38/22
    0.563362f, 0.703725f,
    0.661341f, 0.694434f,
    0.563390f, 0.671726f,
    // f 33/39/23 58/40/23 34/41/23
    0.589514f, 0.233307f,
    0.687511f, 0.203887f,
    0.588350f, 0.196545f,
    // f 34/41/24 58/40/24 22/42/24
    0.588350f, 0.196545f,
    0.687511f, 0.203887f,
    0.587253f, 0.166356f,
    // f 80/43/25 22/42/25 58/40/25
    0.686540f, 0.174772f,
    0.587253f, 0.166356f,
    0.687511f, 0.203887f,
    // f 61/27/26 66/26/26 68/44/26
    0.790647f, 0.188639f,
    0.824266f, 0.188468f,
    0.825289f, 0.173324f,
    // f 61/16/27 68/45/27 62/17/27
    0.769133f, 0.705789f,
    0.810926f, 0.720404f,
    0.800006f, 0.705134f,
    // f 64/21/28 63/20/28 69/46/28
    0.764765f, 0.657774f,
    0.800527f, 0.653321f,
    0.824069f, 0.626887f,
    // f 64/22/29 69/47/29 65/23/29
    0.794175f, 0.24222f,
    0.828140f, 0.261182f,
    0.829644f, 0.246174f,
    // f 66/48/30 9/49/30 10/50/30
    0.809694f, 0.705733f,
    0.841135f, 0.708522f,
    0.860447f, 0.734237f,
    // f 62/51/31 12/52/31 13/53/31
    0.845540f, 0.182493f,
    0.888545f, 0.150846f,
    0.870231f, 0.181125f,
    // f 62/51/32 13/53/32 14/54/32
    0.845540f, 0.182493f,
    0.870231f, 0.181125f,
    0.832932f, 0.217262f,
    // f 14/54/33 15/55/33 63/56/33
    0.832932f, 0.217262f,
    0.866325f, 0.258439f,
    0.849286f, 0.252479f,
    // f 69/57/34 16/58/34 17/59/34
    0.860632f, 0.279043f,
    0.883122f, 0.287421f,
    0.883022f, 0.295683f,
    // f 69/46/35 17/60/35 18/61/35
    0.824069f, 0.626887f,
    0.850669f, 0.608656f,
    0.850863f, 0.615666f,
    // f 65/62/36 19/63/36 20/64/36
    0.806039f, 0.644813f,
    0.834916f, 0.641429f,
    0.803140f, 0.678363f,
    // f 20/64/37 9/49/37 66/48/37
    0.803140f, 0.678363f,
    0.841135f, 0.708522f,
    0.809694f, 0.705733f,
    // f 1/65/38 84/66/38 87/67/38
    0.210749f, 0.213176f,
    0.228600f, 0.205369f,
    0.221122f, 0.204939f,
    // f 87/67/39 84/66/39 71/3/39
    0.221122f, 0.204939f,
    0.228600f, 0.205369f,
    0.243135f, 0.187945f,
    // f 87/68/40 71/4/40 85/69/40
    0.201153f, 0.70916f,
    0.223326f, 0.72363f,
    0.208446f, 0.708834f,
    // f 1/70/41 87/68/41 85/69/41
    0.190707f, 0.700988f,
    0.201153f, 0.70916f,
    0.208446f, 0.708834f,
    // f 1/70/42 85/69/42 2/71/42
    0.190707f, 0.700988f,
    0.208446f, 0.708834f,
    0.195557f, 0.695508f,
    // f 3/72/43 72/9/43 4/73/43
    0.209065f, 0.697429f,
    0.218862f, 0.693504f,
    0.200026f, 0.688711f,
    // f 4/73/44 72/9/44 73/10/44
    0.200026f, 0.688711f,
    0.218862f, 0.693504f,
    0.221434f, 0.673795f,
    // f 4/73/45 73/10/45 5/74/45
    0.200026f, 0.688711f,
    0.221434f, 0.673795f,
    0.191005f, 0.679689f,
    // f 5/74/46 73/10/46 86/75/46
    0.191005f, 0.679689f,
    0.221434f, 0.673795f,
    0.205986f, 0.669504f,
    // f 5/76/47 86/77/47 75/12/47
    0.211617f, 0.24122f,
    0.227474f, 0.250787f,
    0.243142f, 0.244811f,
    // f 5/78/48 75/12/48 6/79/48
    0.212900f, 0.236599f,
    0.243142f, 0.244811f,
    0.220597f, 0.229082f,
    // f 6/79/49 75/12/49 76/15/49
    0.220597f, 0.229082f,
    0.243142f, 0.244811f,
    0.239779f, 0.222326f,
    // f 6/79/50 76/15/50 7/80/50
    0.220597f, 0.229082f,
    0.239779f, 0.222326f,
    0.229442f, 0.218715f,
    // f 84/66/51 1/81/51 8/82/51
    0.228600f, 0.205369f,
    0.210847f, 0.217954f,
    0.215837f, 0.221449f,
    // f 84/66/52 45/1/52 71/3/52
    0.228600f, 0.205369f,
    0.297030f, 0.185267f,
    0.243135f, 0.187945f,
    // f 85/69/53 71/4/53 48/6/53
    0.208446f, 0.708834f,
    0.223326f, 0.72363f,
    0.278797f, 0.714508f,
    // f 85/69/54 48/6/54 72/9/54
    0.208446f, 0.708834f,
    0.278797f, 0.714508f,
    0.218862f, 0.693504f,
    // f 73/10/55 74/83/55 86/75/55
    0.221434f, 0.673795f,
    0.225861f, 0.658661f,
    0.205986f, 0.669504f,
    // f 86/77/56 74/84/56 75/12/56
    0.227474f, 0.250787f,
    0.248031f, 0.262151f,
    0.243142f, 0.244811f,
    // f 76/15/57 45/1/57 84/66/57
    0.239779f, 0.222326f,
    0.297030f, 0.185267f,
    0.228600f, 0.205369f,
    // f 36/85/58 77/86/58 35/87/58
    0.445247f, 0.167748f,
    0.495036f, 0.144163f,
    0.433183f, 0.143883f,
    // f 35/87/59 77/86/59 78/88/59
    0.433183f, 0.143883f,
    0.495036f, 0.144163f,
    0.494560f, 0.138131f,
    // f 35/89/60 78/90/60 79/91/60
    0.411996f, 0.755382f,
    0.472764f, 0.758542f,
    0.472933f, 0.752864f,
    // f 35/89/61 79/91/61 37/92/61
    0.411996f, 0.755382f,
    0.472933f, 0.752864f,
    0.422920f, 0.733525f,
    // f 70/93/62 83/94/62 88/95/62
    0.482056f, 0.626278f,
    0.509887f, 0.596753f,
    0.501925f, 0.565502f,
    // f 30/96/63 60/97/63 89/98/63
    0.563269f, 0.644174f,
    0.589442f, 0.631668f,
    0.547566f, 0.627897f,
    // f 89/99/64 60/100/64 32/101/64
    0.574964f, 0.285185f,
    0.617237f, 0.279227f,
    0.590334f, 0.265329f,
    // f 30/96/65 89/98/65 31/102/65
    0.563269f, 0.644174f,
    0.547566f, 0.627897f,
    0.536720f, 0.627533f,
    // f 31/102/66 89/98/66 90/103/66
    0.536720f, 0.627533f,
    0.547566f, 0.627897f,
    0.542905f, 0.618585f,
    // f 31/104/67 89/99/67 32/101/67
    0.563993f, 0.285931f,
    0.574964f, 0.285185f,
    0.590334f, 0.265329f,
    // f 80/43/68 53/105/68 81/106/68
    0.686540f, 0.174772f,
    0.729856f, 0.179159f,
    0.678810f, 0.163358f,
    // f 81/107/69 55/108/69 82/35/69
    0.653755f, 0.730334f,
    0.703836f, 0.714839f,
    0.661180f, 0.719935f,
    // f 82/35/70 55/108/70 56/18/70
    0.661180f, 0.719935f,
    0.703836f, 0.714839f,
    0.724683f, 0.682011f,
    // f 60/109/71 56/18/71 57/110/71
    0.661157f, 0.64807f,
    0.724683f, 0.682011f,
    0.703842f, 0.65288f,
    // f 60/111/72 57/112/72 58/24/72
    0.689167f, 0.257476f,
    0.732195f, 0.250395f,
    0.751860f, 0.215992f,
    // f 53/105/73 80/43/73 58/40/73
    0.729856f, 0.179159f,
    0.686540f, 0.174772f,
    0.687511f, 0.203887f,
    // f 53/105/74 61/27/74 67/113/74
    0.729856f, 0.179159f,
    0.790647f, 0.188639f,
    0.766892f, 0.186416f,
    // f 53/105/75 67/113/75 54/114/75
    0.729856f, 0.179159f,
    0.766892f, 0.186416f,
    0.740439f, 0.176357f,
    // f 54/115/76 67/116/76 55/108/76
    0.714119f, 0.717029f,
    0.740113f, 0.707363f,
    0.703836f, 0.714839f,
    // f 55/108/77 67/116/77 61/16/77
    0.703836f, 0.714839f,
    0.740113f, 0.707363f,
    0.769133f, 0.705789f,
    // f 55/108/78 61/16/78 56/18/78
    0.703836f, 0.714839f,
    0.769133f, 0.705789f,
    0.724683f, 0.682011f,
    // f 56/18/79 64/21/79 57/110/79
    0.724683f, 0.682011f,
    0.764765f, 0.657774f,
    0.703842f, 0.65288f,
    // f 57/112/80 64/22/80 58/24/80
    0.732195f, 0.250395f,
    0.794175f, 0.24222f,
    0.751860f, 0.215992f,
    // f 61/27/81 53/105/81 58/24/81
    0.790647f, 0.188639f,
    0.729856f, 0.179159f,
    0.751860f, 0.215992f,
    // f 70/93/82 31/117/82 83/94/82
    0.482056f, 0.626278f,
    0.525790f, 0.625937f,
    0.509887f, 0.596753f,
    // f 94/118/83 36/85/83 35/87/83
    0.336025f, 0.168655f,
    0.445247f, 0.167748f,
    0.433183f, 0.143883f,
    // f 94/119/84 35/87/84 46/2/84
    0.335494f, 0.158859f,
    0.433183f, 0.143883f,
    0.301333f, 0.163178f,
    // f 46/5/85 35/89/85 47/120/85
    0.281423f, 0.74039f,
    0.411996f, 0.755382f,
    0.315165f, 0.745439f,
    // f 47/121/86 35/89/86 37/92/86
    0.315282f, 0.736601f,
    0.411996f, 0.755382f,
    0.422920f, 0.733525f,
    // f 48/6/87 38/122/87 49/8/87
    0.278797f, 0.714508f,
    0.423024f, 0.707799f,
    0.290216f, 0.687449f,
    // f 49/8/88 38/122/88 39/123/88
    0.290216f, 0.687449f,
    0.423024f, 0.707799f,
    0.423053f, 0.673014f,
    // f 50/11/89 40/28/89 41/30/89
    0.290102f, 0.652018f,
    0.422953f, 0.645909f,
    0.342026f, 0.63271f,
    // f 41/31/90 42/33/90 51/13/90
    0.366614f, 0.287679f,
    0.448348f, 0.268539f,
    0.313490f, 0.26685f,
    // f 52/14/91 43/124/91 44/125/91
    0.312474f, 0.225662f,
    0.447608f, 0.236988f,
    0.446347f, 0.197023f,
    // f 52/14/92 44/125/92 45/1/92
    0.312474f, 0.225662f,
    0.446347f, 0.197023f,
    0.297030f, 0.185267f,
    // f 45/1/93 94/126/93 46/2/93
    0.297030f, 0.185267f,
    0.314576f, 0.162665f,
    0.301333f, 0.163178f,
    // f 94/127/94 95/128/94 97/129/94
    0.315267f, 0.175755f,
    0.382197f, 0.129098f,
    0.348153f, 0.131819f,
    // f 47/130/95 96/131/95 98/132/95
    0.299619f, 0.743495f,
    0.369204f, 0.770422f,
    0.327860f, 0.767513f,
    // f 45/1/96 44/125/96 94/133/96
    0.297030f, 0.185267f,
    0.446347f, 0.197023f,
    0.322222f, 0.184222f,
    // f 44/125/97 36/85/97 94/133/97
    0.446347f, 0.197023f,
    0.445247f, 0.167748f,
    0.322222f, 0.184222f,
    // f 51/13/98 42/33/98 43/124/98
    0.313490f, 0.26685f,
    0.448348f, 0.268539f,
    0.447608f, 0.236988f,
    // f 51/13/99 43/124/99 52/14/99
    0.313490f, 0.26685f,
    0.447608f, 0.236988f,
    0.312474f, 0.225662f,
    // f 49/8/100 39/123/100 50/11/100
    0.290216f, 0.687449f,
    0.423053f, 0.673014f,
    0.290102f, 0.652018f,
    // f 39/123/101 40/28/101 50/11/101
    0.423053f, 0.673014f,
    0.422953f, 0.645909f,
    0.290102f, 0.652018f,
    // f 47/134/102 37/92/102 38/122/102
    0.301130f, 0.723305f,
    0.422920f, 0.733525f,
    0.423024f, 0.707799f,
    // f 47/134/103 38/122/103 48/6/103
    0.301130f, 0.723305f,
    0.423024f, 0.707799f,
    0.278797f, 0.714508f,
    // f 44/125/104 34/41/104 22/42/104
    0.446347f, 0.197023f,
    0.588350f, 0.196545f,
    0.587253f, 0.166356f,
    // f 44/125/105 22/42/105 36/85/105
    0.446347f, 0.197023f,
    0.587253f, 0.166356f,
    0.445247f, 0.167748f,
    // f 43/124/106 33/39/106 34/41/106
    0.447608f, 0.236988f,
    0.589514f, 0.233307f,
    0.588350f, 0.196545f,
    // f 43/124/107 34/41/107 44/125/107
    0.447608f, 0.236988f,
    0.588350f, 0.196545f,
    0.446347f, 0.197023f,
    // f 42/33/108 32/101/108 33/39/108
    0.448348f, 0.268539f,
    0.590334f, 0.265329f,
    0.589514f, 0.233307f,
    // f 42/33/109 33/39/109 43/124/109
    0.448348f, 0.268539f,
    0.589514f, 0.233307f,
    0.447608f, 0.236988f,
    // f 70/135/110 31/136/110 42/33/110
    0.508695f, 0.289479f,
    0.552982f, 0.288283f,
    0.448348f, 0.268539f,
    // f 31/136/111 32/101/111 42/33/111
    0.552982f, 0.288283f,
    0.590334f, 0.265329f,
    0.448348f, 0.268539f,
    // f 40/28/112 30/96/112 31/117/112
    0.422953f, 0.645909f,
    0.563269f, 0.644174f,
    0.525790f, 0.625937f,
    // f 40/28/113 31/117/113 70/93/113
    0.422953f, 0.645909f,
    0.525790f, 0.625937f,
    0.482056f, 0.626278f,
    // f 39/123/114 29/38/114 40/28/114
    0.423053f, 0.673014f,
    0.563390f, 0.671726f,
    0.422953f, 0.645909f,
    // f 29/38/115 30/96/115 40/28/115
    0.563390f, 0.671726f,
    0.563269f, 0.644174f,
    0.422953f, 0.645909f,
    // f 38/122/116 28/37/116 39/123/116
    0.423024f, 0.707799f,
    0.563362f, 0.703725f,
    0.423053f, 0.673014f,
    // f 28/37/117 29/38/117 39/123/117
    0.563362f, 0.703725f,
    0.563390f, 0.671726f,
    0.423053f, 0.673014f,
    // f 37/92/118 27/34/118 38/122/118
    0.422920f, 0.733525f,
    0.563232f, 0.730236f,
    0.423024f, 0.707799f,
    // f 27/34/119 28/37/119 38/122/119
    0.563232f, 0.730236f,
    0.563362f, 0.703725f,
    0.423024f, 0.707799f,
    // f 79/137/120 25/138/120 27/34/120
    0.500689f, 0.751828f,
    0.536751f, 0.745161f,
    0.563232f, 0.730236f,
    // f 79/137/121 27/34/121 37/92/121
    0.500689f, 0.751828f,
    0.563232f, 0.730236f,
    0.422920f, 0.733525f,
    // f 79/139/122 25/140/122 26/141/122
    0.537371f, 0.14009f,
    0.548102f, 0.140143f,
    0.572036f, 0.127658f,
    // f 79/139/123 26/141/123 93/142/123
    0.537371f, 0.14009f,
    0.572036f, 0.127658f,
    0.554411f, 0.12623f,
    // f 78/143/124 24/144/124 79/145/124
    0.515059f, 0.756876f,
    0.533809f, 0.754826f,
    0.515145f, 0.755611f,
    // f 24/144/125 25/146/125 79/145/125
    0.533809f, 0.754826f,
    0.525718f, 0.755199f,
    0.515145f, 0.755611f,
    // f 77/147/126 21/148/126 24/149/126
    0.537605f, 0.139636f,
    0.548325f, 0.139702f,
    0.556527f, 0.139986f,
    // f 77/147/127 24/149/127 78/150/127
    0.537605f, 0.139636f,
    0.556527f, 0.139986f,
    0.537462f, 0.138388f,
    // f 77/151/128 21/152/128 23/153/128
    0.521872f, 0.145197f,
    0.559914f, 0.150429f,
    0.572512f, 0.126748f,
    // f 77/151/129 23/153/129 92/154/129
    0.521872f, 0.145197f,
    0.572512f, 0.126748f,
    0.554908f, 0.125295f,
    // f 36/85/130 22/42/130 77/151/130
    0.445247f, 0.167748f,
    0.587253f, 0.166356f,
    0.521872f, 0.145197f,
    // f 22/42/131 21/152/131 77/151/131
    0.587253f, 0.166356f,
    0.559914f, 0.150429f,
    0.521872f, 0.145197f,
    // f 91/155/132 54/156/132 81/107/132
    0.658512f, 0.748753f,
    0.693181f, 0.730372f,
    0.653755f, 0.730334f,
    // f 54/156/133 55/108/133 81/107/133
    0.693181f, 0.730372f,
    0.703836f, 0.714839f,
    0.653755f, 0.730334f,
    // f 81/106/134 53/105/134 54/157/134
    0.678810f, 0.163358f,
    0.729856f, 0.179159f,
    0.718763f, 0.161842f,
    // f 81/106/135 54/157/135 91/158/135
    0.678810f, 0.163358f,
    0.718763f, 0.161842f,
    0.683007f, 0.142068f,
    // f 24/159/136 59/160/136 25/138/136
    0.547579f, 0.747739f,
    0.589455f, 0.740568f,
    0.536751f, 0.745161f,
    // f 59/160/137 27/34/137 25/138/137
    0.589455f, 0.740568f,
    0.563232f, 0.730236f,
    0.536751f, 0.745161f,
    // f 21/152/138 22/42/138 59/161/138
    0.559914f, 0.150429f,
    0.587253f, 0.166356f,
    0.613375f, 0.153868f,
    // f 21/152/139 59/161/139 24/162/139
    0.559914f, 0.150429f,
    0.613375f, 0.153868f,
    0.570751f, 0.147214f,
    // f 7/80/140 76/15/140 84/66/140
    0.229442f, 0.218715f,
    0.239779f, 0.222326f,
    0.228600f, 0.205369f,
    // f 7/80/141 84/66/141 8/82/141
    0.229442f, 0.218715f,
    0.228600f, 0.205369f,
    0.215837f, 0.221449f,
    // f 2/71/142 85/69/142 3/72/142
    0.195557f, 0.695508f,
    0.208446f, 0.708834f,
    0.209065f, 0.697429f,
    // f 85/69/143 72/9/143 3/72/143
    0.208446f, 0.708834f,
    0.218862f, 0.693504f,
    0.209065f, 0.697429f,
    // f 69/46/144 18/61/144 19/63/144
    0.824069f, 0.626887f,
    0.850863f, 0.615666f,
    0.834916f, 0.641429f,
    // f 69/46/145 19/63/145 65/62/145
    0.824069f, 0.626887f,
    0.834916f, 0.641429f,
    0.806039f, 0.644813f,
    // f 63/56/146 15/55/146 69/57/146
    0.849286f, 0.252479f,
    0.866325f, 0.258439f,
    0.860632f, 0.279043f,
    // f 15/55/147 16/58/147 69/57/147
    0.866325f, 0.258439f,
    0.883122f, 0.287421f,
    0.860632f, 0.279043f,
    // f 68/163/148 11/164/148 12/52/148
    0.856802f, 0.160723f,
    0.888076f, 0.141894f,
    0.888545f, 0.150846f,
    // f 68/163/149 12/52/149 62/51/149
    0.856802f, 0.160723f,
    0.888545f, 0.150846f,
    0.845540f, 0.182493f,
    // f 66/48/150 10/50/150 68/45/150
    0.809694f, 0.705733f,
    0.860447f, 0.734237f,
    0.810926f, 0.720404f,
    // f 10/50/151 11/165/151 68/45/151
    0.860447f, 0.734237f,
    0.860601f, 0.742057f,
    0.810926f, 0.720404f,
    // f 32/101/152 60/111/152 33/39/152
    0.590334f, 0.265329f,
    0.689167f, 0.257476f,
    0.589514f, 0.233307f,
    // f 60/111/153 58/40/153 33/39/153
    0.689167f, 0.257476f,
    0.687511f, 0.203887f,
    0.589514f, 0.233307f,
    // f 29/38/154 56/36/154 60/109/154
    0.563390f, 0.671726f,
    0.661341f, 0.694434f,
    0.661157f, 0.64807f,
    // f 29/38/155 60/109/155 30/96/155
    0.563390f, 0.671726f,
    0.661157f, 0.64807f,
    0.563269f, 0.644174f,
    // f 59/160/156 81/107/156 82/35/156
    0.589455f, 0.740568f,
    0.653755f, 0.730334f,
    0.661180f, 0.719935f,
    // f 59/160/157 82/35/157 27/34/157
    0.589455f, 0.740568f,
    0.661180f, 0.719935f,
    0.563232f, 0.730236f,
    // f 22/42/158 80/43/158 59/161/158
    0.587253f, 0.166356f,
    0.686540f, 0.174772f,
    0.613375f, 0.153868f,
    // f 80/43/159 81/106/159 59/161/159
    0.686540f, 0.174772f,
    0.678810f, 0.163358f,
    0.613375f, 0.153868f,
    // f 74/84/160 41/166/160 51/13/160
    0.248031f, 0.262151f,
    0.297076f, 0.2803f,
    0.313490f, 0.26685f,
    // f 74/84/161 51/13/161 75/12/161
    0.248031f, 0.262151f,
    0.313490f, 0.26685f,
    0.243142f, 0.244811f,
    // f 73/10/162 50/11/162 74/83/162
    0.221434f, 0.673795f,
    0.290102f, 0.652018f,
    0.225861f, 0.658661f,
    // f 50/11/163 41/167/163 74/83/163
    0.290102f, 0.652018f,
    0.273694f, 0.641473f,
    0.225861f, 0.658661f,
  };
}
