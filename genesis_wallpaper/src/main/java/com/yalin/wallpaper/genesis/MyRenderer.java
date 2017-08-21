/*
 * Copyright (C) 2012, 2013 OBN-soft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.wallpaper.genesis;

import android.content.Context;
import android.opengl.GLU;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

public class MyRenderer {

    private static final int BYTES_FLOAT = 4;

    private EGL10 mEGL = null;
    private EGLContext mEGLContext = null;
    private EGLDisplay mEGLDisplay = null;
    private EGLSurface mEGLSurface = null;
    private EGLConfig mEGLConfig = null;
    private GL10 mGL10 = null;

    private int mBufferIndex = 0;

    private int mLevel;
    private float mAlpha;
    private float mScale;
    private boolean mInvert;

    private float mRotX;
    private float mRotY;
    private float mRotZ;
    private float mRotR;
    private long mStartTime;

    public void onStartDrawing(Context context, SurfaceHolder holder) {
        initializeGL();
        initializeSurface(mGL10, holder);
        initializeObject(mGL10);
    }

    public void onResumeDrawing() {
        initializeObject(mGL10);
    }

    public void onDrawFrame(int width, int height) {
        drawFrame(mGL10, width, height);
    }

    public void onFinishDrawing() {
        clearFrame(mGL10);
        finalizeSurface();
    }

    public void onDispose() {
        finalizeGL();
    }

    /*-----------------------------------------------------------------------*/

    private void initializeGL() {

        /*  Initialize  */
        mEGL = (EGL10) EGLContext.getEGL();
        mEGLDisplay = mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = {-1, -1};
        if (!mEGL.eglInitialize(mEGLDisplay, version)) {
            return;
        }

        /*  Get configuration  */
        EGLConfig[] configs = new EGLConfig[1];
        int[] num = new int[1];
        int[] spec = {EGL10.EGL_NONE};
        if (!mEGL.eglChooseConfig(mEGLDisplay, spec, configs, 1, num)) {
            return;
        }
        mEGLConfig = configs[0];

        /*  Create rendering context  */
        mEGLContext = mEGL.eglCreateContext(mEGLDisplay, mEGLConfig, EGL10.EGL_NO_CONTEXT, null);
        if (mEGLContext == EGL10.EGL_NO_CONTEXT) {
            return;
        }

        /*  Get GLES interface  */
        mGL10 = (GL10) mEGLContext.getGL();
    }

    private void initializeSurface(GL10 gl10, SurfaceHolder holder) {
        /*  Create surface  */
        mEGL.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        mEGLSurface = mEGL.eglCreateWindowSurface(
                mEGLDisplay, mEGLConfig, holder, null);
        if (mEGLSurface == EGL10.EGL_NO_SURFACE) {
            return;
        }

        /*  Attach surface to context  */
        if (!mEGL.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
            return;
        }

        /*  Get buffer index  */
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        int[] buf = new int[1];
        ((GL11) gl10).glGenBuffers(1, buf, 0);
        mBufferIndex = buf[0];

        /*  Enable alpha-blending  */
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glEnable(GL10.GL_ALPHA);
    }

    private void initializeObject(GL10 gl10) {
        /*  Parameters  */
        applyPrefs();
        Random random = new Random();
        mRotX = random.nextFloat() * 64f - 32f;
        mRotY = random.nextFloat() * 64f - 32f;
        mRotZ = random.nextFloat() * 64f - 32f;
        mRotR = random.nextFloat() * 64f - 32f;
        mStartTime = System.currentTimeMillis();

        /*  Define vertices of quadrangle  */
        float v = (6f / mLevel) * mScale;
        float ary[] = {-v, v, 0f, -v, -v, 0f, v, v, 0f, v, -v, 0f};
        ByteBuffer bb = ByteBuffer.allocateDirect(ary.length * BYTES_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vbo = bb.asFloatBuffer();
        vbo.put(ary);
        vbo.position(0);

        /*  Assign vertices array  */
        GL11 gl11 = (GL11) gl10;
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mBufferIndex);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER,
                vbo.capacity() * BYTES_FLOAT, vbo, GL11.GL_STATIC_DRAW);
        gl11.glVertexPointer(3, GL10.GL_FLOAT, 0/*GL10.GL_FLOAT * 3*/, 0);
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

        /*  Alpha-blending setting  */
        try {
            GL11ExtensionPack gl11ep = (GL11ExtensionPack) mGL10;
            gl11ep.glBlendEquation(mInvert ?
                    GL11ExtensionPack.GL_FUNC_REVERSE_SUBTRACT : GL11ExtensionPack.GL_FUNC_ADD);
        } catch (UnsupportedOperationException e) {
        }
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
    }

    private void applyPrefs() {
        mLevel = 3;
        mAlpha = 0.5f;
        mScale = 2f;
        mInvert = false;
    }

    private void clearFrame(GL10 gl10) {
        if (mInvert) {
            gl10.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        } else {
            gl10.glClearColor(0f, 0f, 0f, 1f);
        }
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    private void drawFrame(GL10 gl10, int width, int height) {
        /*  Clear  */
        clearFrame(gl10);

        /*  View port  */
        gl10.glViewport(0, 0, width, height);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45f, (float) width / (float) height, 1f, 100f);
        GLU.gluLookAt(gl10, 0f, 0f, -32f, 0f, 0f, 0f, 0f, 1f, 0f);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        /*  Rotate  */
        float tick = (float) ((System.currentTimeMillis() - mStartTime) / 1000.0);
        gl10.glLoadIdentity();
        gl10.glRotatef(mRotX * tick, 1.0f, 0.0f, 0.0f);
        gl10.glRotatef(mRotY * tick, 0.0f, 1.0f, 0.0f);
        gl10.glRotatef(mRotZ * tick, 0.0f, 0.0f, 1.0f);

        /*  Draw quadrangles  */
        for (int i = -mLevel; i <= mLevel; i++) {
            int jRange = mLevel - Math.abs(i);
            for (int j = -jRange; j <= jRange; j++) {
                int kRange = jRange - Math.abs(j);
                for (int k = -kRange; k <= kRange; k++) {
                    drawQuad(gl10,
                            (float) i / (float) mLevel,
                            (float) j / (float) mLevel,
                            (float) k / (float) mLevel,
                            (float) Math.cos(Math.toRadians(mRotR * tick)) * 48f);
                }
            }
        }

        /*  Update surface  */
        mEGL.eglSwapBuffers(mEGLDisplay, mEGLSurface);
    }

    private void drawQuad(GL10 gl10, float x, float y, float z, float r) {
        gl10.glPushMatrix();
        gl10.glColor4f((x + 1f) / 2f, (y + 1f) / 2f, (z + 1f) / 2f, mAlpha);
        gl10.glTranslatef(x * r, y * r, z * r);
        //gl10.glScalef(mScale, mScale, 1f);
        gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl10.glPopMatrix();
    }

    private void finalizeSurface() {
        if (mEGLSurface != null) {
            mEGL.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            mEGL.eglDestroySurface(mEGLDisplay, mEGLSurface);
            mEGLSurface = null;
        }
    }

    private void finalizeGL() {
        if (mEGLSurface != null) {
            mEGL.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            mEGL.eglDestroySurface(mEGLDisplay, mEGLSurface);
            mEGLSurface = null;
        }

        if (mEGLContext != null) {
            mEGL.eglDestroyContext(mEGLDisplay, mEGLContext);
            mEGLContext = null;
        }

        if (mEGLDisplay != null) {
            mEGL.eglTerminate(mEGLDisplay);
            mEGLDisplay = null;
        }
    }

}
