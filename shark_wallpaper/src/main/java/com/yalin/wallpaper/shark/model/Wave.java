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

import com.yalin.wallpaper.shark.Aquarium;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Wave {
  private final FloatBuffer mVertexBuffer;
  private final FloatBuffer mVertexForStencilBuffer;
  private final FloatBuffer mTextureBuffer;  
  private static boolean mTextureLoaded = false;

  float oneW = Aquarium.max_x + 0.5f;
  float oneH = Aquarium.max_y + 0.3f;
  private float scale = 0.05f;
  float vertices[] = {
     oneW, oneH,  oneW,   // 左下
    -oneW, oneH,  oneW,   // 右下
     oneW, oneH, -oneW,   // 左上
    -oneW, oneH, -oneW,   // 右上
  };
  float org_vertices[];
  float vertices_for_stencil[] = {
     oneW * 2.0f, oneH,  oneW * 2.0f,   // 左下
    -oneW * 2.0f, oneH,  oneW * 2.0f,   // 右下
     oneW * 2.0f, oneH,  oneW * 2.0f / 4.0f,   // 左上
    -oneW * 2.0f, oneH,  oneW * 2.0f / 4.0f,   // 右上
  };

  private float[] mScratch4f = new float[4];
  public Wave() {

    float texCoords[] = {
      0.0f,1.0f,
      1.0f,1.0f,
      0.0f,0.0f,
      1.0f,0.0f,
    };

    mVertexBuffer = createFloatBuffer(vertices);
    mVertexForStencilBuffer = createFloatBuffer(vertices_for_stencil);
    mTextureBuffer = createFloatBuffer(texCoords);
    org_vertices = new float[vertices.length];
    System.arraycopy(vertices,0, org_vertices, 0, vertices.length);
  }

  public FloatBuffer createFloatBuffer(float[] vert) {
    ByteBuffer vbb = ByteBuffer.allocateDirect(vert.length * 4);
    vbb.order(ByteOrder.nativeOrder());
    FloatBuffer fb = vbb.asFloatBuffer();
    fb.put(vert);
    fb.position(0);
    return fb;
  }

  static int textureIds[] = null;
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


  void animate() {
    long current = System.currentTimeMillis();
    float nf1 = (float)((current / 200) % 10000);
    float nf2 = (float)(((current + 500) / 200) % 10000);
    float nf3 = (float)(((current + 1000) / 200) % 10000);
    float nf4 = (float)(((current + 1500) / 200) % 10000);
    float s1 = (float)Math.sin((double)nf1) * scale;
    float s2 = (float)Math.sin((double)nf2) * scale;
    float s3 = (float)Math.sin((double)nf3) * scale;
    float s4 = (float)Math.sin((double)nf4) * scale;

    vertices[0*3+1] = org_vertices[0*3+1] + s1;
    vertices[1*3+1] = org_vertices[1*3+1] + s2;
    vertices[2*3+1] = org_vertices[2*3+1] + s3;
    vertices[3*3+1] = org_vertices[3*3+1] + s4;

    mVertexBuffer.position(0);
    mVertexBuffer.put(vertices);
    mVertexBuffer.position(0);
  }

  public void calc() {
    animate();
  }

  public void draw(GL10 gl10) {
    _draw(gl10,mVertexBuffer);
  }
  public void drawForStencil(GL10 gl10) {
    _draw(gl10,mVertexForStencilBuffer);
  }
  public void _draw(GL10 gl10, FloatBuffer vertexBuffer) {

    /*-----------------------------------------------------------------------*/
    /* 背景描画                                                              */
    /*-----------------------------------------------------------------------*/
    gl10.glMatrixMode(GL10.GL_MODELVIEW);  // ModelView行列をクリア
    gl10.glPushMatrix();
    /* シースルーモード */
    gl10.glEnable(GL10.GL_BLEND);
    gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_SRC_ALPHA);

    /*=======================================================================*/
    /* 環境光の材質色設定                                                    */
    /*=======================================================================*/
    synchronized (mScratch4f) {
      mScratch4f[0] = 1.0f;
      mScratch4f[1] = 1.0f;
      mScratch4f[2] = 1.0f;
      mScratch4f[3] = 1.0f;
      gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mScratch4f, 0);
    }
    /*=======================================================================*/
    /* 拡散反射光の色設定                                                    */
    /*=======================================================================*/
    synchronized (mScratch4f) {
      mScratch4f[0] = 1.0f;
      mScratch4f[1] = 1.0f;
      mScratch4f[2] = 1.0f;
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
    gl10.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100f);

        
    /*-----------------------------------------------------------------------*/
    /* 頂点座標バッファを読み込む                                            */
    /*-----------------------------------------------------------------------*/
    gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
    gl10.glEnable(GL10.GL_TEXTURE_2D);

    gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
    gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);


    gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
    gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    /*-----------------------------------------------------------------------*/
    /* 頂点描画                                                              */
    /*-----------------------------------------------------------------------*/
    gl10.glColor4f(1,1,1,1);
    gl10.glNormal3f(0,-1,0);
    gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    gl10.glDisable(GL10.GL_BLEND);
    gl10.glPopMatrix();
  }
}
