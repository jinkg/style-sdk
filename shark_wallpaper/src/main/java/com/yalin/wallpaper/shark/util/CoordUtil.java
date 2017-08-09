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
package com.yalin.wallpaper.shark.util;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class CoordUtil {
  private static final String TAG = CoordUtil.class.getName();
  private static final boolean _debug = false;

  protected float[][] matrix = null;

  public float convertDegreeToRadian(float angle) {
    return angle * ((float)Math.PI) / 180.0f;
  }
  
  private void __initMatrix() {
    matrix = new float[4][];
    for (int ii=0; ii<4; ii++) {
      matrix[ii] = new float[4];
    }
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        matrix[x][y] = 0f;
      }
    }
  }
  protected void initMatrix() {
    if (matrix == null) {
      __initMatrix(); 
    }
    else {
      for (int x = 0; x < 4; x++) {
        for (int y = 0; y < 4; y++) {
          matrix[x][y] = 0f;
        }
      }
    }
  }
  public void setMatrixRotateX(float angle) {
    initMatrix();
    matrix[0][0] = 1f;
    matrix[1][1] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[2][1] = (float)-Math.sin((double)convertDegreeToRadian(angle));
    matrix[1][2] = (float)Math.sin((double)convertDegreeToRadian(angle));
    matrix[2][2] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[3][3] = 1f;
  }
  
  public void setMatrixRotateY(float angle) {
    initMatrix();
    matrix[0][0] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[2][0] = (float)Math.sin((double)convertDegreeToRadian(angle));
    matrix[1][1] = 1f;
    matrix[0][2] = (float)-Math.sin((double)convertDegreeToRadian(angle));
    matrix[2][2] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[3][3] = 1f;
  }
  
  public void setMatrixRotateZ(float angle) {
    initMatrix();
    angle *= -1f;
    matrix[0][0] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[1][0] = (float)-Math.sin((double)convertDegreeToRadian(angle));
    matrix[0][1] = (float)Math.sin((double)convertDegreeToRadian(angle));
    matrix[1][1] = (float)Math.cos((double)convertDegreeToRadian(angle));
    matrix[2][2] = 1f;
    matrix[3][3] = 1f;
  }
  
  public void setMatrixScale(float s1, float s2, float s3) {
    initMatrix();
    matrix[0][0] = s1;
    matrix[1][1] = s2;
    matrix[2][2] = s3;
    matrix[3][3] = 1f;
  }
  
  public void setMatrixTranslate(float t1, float t2, float t3) {
    initMatrix();
    matrix[0][0] = 1f; 
    matrix[3][0] = t1;
    matrix[1][1] = 1f; 
    matrix[3][1] = t2;
    matrix[2][2] = 1f; 
    matrix[3][2] = t3;
    matrix[3][3] = 1f;
  }
  
  public void affine(float x, float y, float z, float[] ret) {
    ret[0] = 
          matrix[0][0] * x +
          matrix[1][0] * y +
          matrix[2][0] * z +
          matrix[3][0] * 1f;
    ret[1] =
          matrix[0][1] * x +
          matrix[1][1] * y +
          matrix[2][1] * z +
          matrix[3][1] * 1f;
    ret[2] =
          matrix[0][2] * x +
          matrix[1][2] * y +
          matrix[2][2] * z +
          matrix[3][2] * 1f;
  }

  public double convertDegreeXZ(double x,double y) {
    double s;
    if (x == 0.0d && y == 0.0d) {
      return 0.0d;
    }
    s=Math.acos(x/Math.sqrt(x*x+y*y));
    s=(s/Math.PI)*180.0d;
    if (y < 0.0d) {
      return 360.0d - s;
    }
    return s;
  }
  public double convertDegreeXY(double x,double y) {
    double s;
    if (x == 0.0d && y == 0.0d) {
      return 0.0d;
    }
    s=Math.acos(x/Math.sqrt(x*x+y*y));
    s=(s/Math.PI)*180.0d;
    if (y < 0.0d || x < 0.0d) {
      return 360.0d - s;
    }
    return s;
  }
  public static double convertToDegree(double radian) {
    return radian * 180.0d / (Math.atan(1.0d) * 4.0d);
  }

  /**
   * AndroidのOpenGL ESではglGetIntやglGetFloatでMATRIXが取得できないので
   * 自前で計算する
   */
  public static float[] calcProjectionMatrix(float fov, float aspect, float znear, float zfar) {
    float xymax = znear * (float)Math.tan((double)(fov * 0.00872664626f));
    float ymin = -xymax;
    float xmin = -xymax;
  
    //スクリーンの横、縦を計算
    float width = xymax - xmin;
    float height = xymax - ymin;
  
    //奥行き値関係
    float depth = zfar - znear;
    float q = -(zfar + znear) / depth;
    float qn = -2 * (zfar * znear) / depth;
  
    //アスペクト比関係
    float w = 2 * znear / width;
    w = w / aspect;
    float h = 2 * znear / height;
  
    //行列に値を設定
    float[] ret = new float[16];
    ret[0]  = w;
    ret[1]  = 0;
    ret[2]  = 0;
    ret[3]  = 0;
  
    ret[4]  = 0;
    ret[5]  = h;
    ret[6]  = 0;
    ret[7]  = 0;
  
    ret[8]  = 0;
    ret[9]  = 0;
    ret[10] = q;
    ret[11] = -1;
  
    ret[12] = 0;
    ret[13] = 0;
    ret[14] = qn;
    ret[15] = 0;

    return ret;
  }

  /**
   * 正規化
   */
  public static void normalize3fv(float[] v) {
    float l;
    l = (float)Math.sqrt((double)(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]));
    if (l == 0.0f) {
      return;
    }

    v[0] /= l;
    v[1] /= l;
    v[2] /= l;
  }

  /**
   * 外積
   */
  public static void cross(float[] v1, float[] v2, float[] result) {
    result[0] = v1[1] * v2[2] - v1[2] * v2[1];
    result[1] = v1[2] * v2[0] - v1[0] * v2[2];
    result[2] = v1[0] * v2[1] - v1[1] * v2[0];
  }

  /**
   * 内積
   */
  public static float dot(float[] v1, float[] v2, int n) {
    float ret = 0f;
    for (int ii=0; ii<n; ii++) {
      ret += (v1[ii] * v2[ii]);
    }
    return ret;
  }

  public static void calcViewMatrix(float eyex, float eyey, float eyez,
    float tarx, float tary, float tarz,
    float upx, float upy, float upz, float[][] m) {
    synchronized (mScratch3f_1) {
      synchronized (mScratch3f_2) {
        synchronized (mScratch3f_3) {
          float[] view = mScratch3f_1; 
          float[] up = mScratch3f_2; 
          float[] side = mScratch3f_3; 
      
          view[0] = tarx - eyex;
          view[1] = tary - eyey;
          view[2] = tarz - eyez;
      
          up[0] = upx;
          up[1] = upy;
          up[2] = upz;
      
          normalize3fv(view);
          normalize3fv(up);
      
          cross(view, up, side);
          normalize3fv(side);
          cross(side, view, up);
      
          m[0][0] = side[0];
          m[1][0] = side[1];
          m[2][0] = side[2];
          m[3][0] = 0f;
      
          m[0][1] = up[0];
          m[1][1] = up[1];
          m[2][1] = up[2];
          m[3][1] = 0f;
      
          m[0][2] = -view[0];
          m[1][2] = -view[1];
          m[2][2] = -view[2];
          m[3][2] = 0f;
      
          m[0][3] = 0f;
          m[1][3] = 0f;
          m[2][3] = 0f;
          m[3][3] = 1f;
        }
      }
    }
  }

  public static float[][] mScratch4x4f = new float[4][4];
  public static float[] mScratch3f_1 = new float[3];
  public static float[] mScratch3f_2 = new float[3];
  public static float[] mScratch3f_3 = new float[3];
  public static float[] viewMatrix = new float[16];
  public static void lookAt(GL10 gl10,
                            float eyex, float eyey, float eyez,
                            float tarx, float tary, float tarz,
                            float upx,  float upy,  float upz) {
    synchronized (mScratch4x4f) {
      calcViewMatrix(eyex, eyey, eyez,
                     tarx, tary, tarz,
                     upx,  upy,  upz, mScratch4x4f);
      viewMatrix[0] = mScratch4x4f[0][0];
      viewMatrix[1] = mScratch4x4f[0][1];
      viewMatrix[2] = mScratch4x4f[0][2];
      viewMatrix[3] = mScratch4x4f[0][3];
      viewMatrix[4] = mScratch4x4f[1][0];
      viewMatrix[5] = mScratch4x4f[1][1];
      viewMatrix[6] = mScratch4x4f[1][2];
      viewMatrix[7] = mScratch4x4f[1][3];
      viewMatrix[8] = mScratch4x4f[2][0];
      viewMatrix[9] = mScratch4x4f[2][1];
      viewMatrix[10] = mScratch4x4f[2][2];
      viewMatrix[11] = mScratch4x4f[2][3];
      viewMatrix[12] = mScratch4x4f[3][0];
      viewMatrix[13] = mScratch4x4f[3][1];
      viewMatrix[14] = mScratch4x4f[3][2];
      viewMatrix[15] = mScratch4x4f[3][3];
    };
    
    /* 
    GLU.gluLookAt(gl10,
                  camera[0],camera[1],camera[2],
                  camera[0],camera[1],-100f,
                  0,1,0);
    */
    gl10.glMultMatrixf(viewMatrix, 0);
    gl10.glTranslatef(-eyex, -eyey, -eyez);

/*
    float[] eye = {eyex, eyey, eyez};
    float[] xaxis = { viewMatrix[0], viewMatrix[4], viewMatrix[8], };
    float[] yaxis = { viewMatrix[1], viewMatrix[5], viewMatrix[9], };
    float[] zaxis = { viewMatrix[2], viewMatrix[6], viewMatrix[10], };
    viewMatrix[12] = -dot(xaxis,eye);
    viewMatrix[13] = -dot(yaxis,eye);
    viewMatrix[14] = -dot(zaxis,eye);
*/
    ((GL11)gl10).glGetFloatv(GL10.GL_MODELVIEW, viewMatrix, 0);
  } 

  public static void perspective(GL10 gl10,
                                 float fovy,
                                 float aspect,
                                 float zNear,
                                 float zFar) {
    float top = (float)Math.tan(fovy / 2.0f * (float)Math.PI / 180.0f) * zNear;
    float bottom = -top;
    float left = aspect * bottom;
    float right = -left;
    if (_debug) {
      Log.d(TAG, 
          "left:[" + left + "]:"
        + "right:[" + right + "]:"
        + "bottom:[" + bottom + "]:"
        + "top:[" + top + "]:"
        + "zNear:[" + zNear + "]:"
        + "zFar:[" + zFar + "]:"
      );
    }


    gl10.glFrustumf(left, right, bottom, top, zNear, zFar);
  }

  /**
   * ベクトル間の角度を求める 
   * @param v1 ベクトル1
   * @param v2 ベクトル2
   * @param n 次元数
   * @return 角度
   */
  public static float includedAngle(float[] v1, float[] v2, int n) {
    return (float)convertToDegree(Math.acos((double)(dot(v1, v2, n) / (norm(v1, n) * norm(v2, n)))));
  }

  /**
   * ノルム
   */
  public static float norm(float[] v1, int n) {
    return (float)Math.sqrt((double)dot(v1, v1, n));
  }
}
