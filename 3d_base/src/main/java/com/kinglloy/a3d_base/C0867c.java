package com.kinglloy.a3d_base;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class C0867c {
    public float f2886a = 0.0f;
    public float f2887b = 0.0f;
    public float f2888c = 0.0f;
    public float f2889d = 0.0f;
    public float f2890e = 0.0f;
    public float f2891f = 0.0f;
    private Bitmap f2892g;
    private FloatBuffer f2893h = null;
    private ShortBuffer f2894i = null;
    private int f2895j = -1;
    private final float[] f2896k = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private boolean f2897l = false;
    private FloatBuffer f2898m;
    private int f2899n = -1;
    private FloatBuffer f2900o = null;
    private int[] f2901p = new int[1];

    private void m4158c(GL10 gl10) {
        this.f2901p = new int[1];
        gl10.glGenTextures(1, this.f2901p, 0);
        this.f2899n = this.f2901p[0];
        gl10.glBindTexture(3553, this.f2899n);
        gl10.glTexParameterf(3553, 10241, 9729.0f);
        gl10.glTexParameterf(3553, 10240, 9729.0f);
        gl10.glTexParameterf(3553, 10242, 33071.0f);
        gl10.glTexParameterf(3553, 10243, 10497.0f);
        GLUtils.texImage2D(3553, 0, this.f2892g, 0);
    }

    public void m4159a(Bitmap bitmap) {
        if (bitmap != null) {
            this.f2892g = bitmap;
            this.f2897l = true;
        }
    }

    public void m4160a(GL10 gl10) {
        gl10.glFrontFace(2305);
        gl10.glEnable(2884);
        gl10.glCullFace(1029);
        gl10.glEnableClientState(32884);
        gl10.glVertexPointer(3, 5126, 0, this.f2900o);
        gl10.glColor4f(this.f2896k[0], this.f2896k[1], this.f2896k[2], this.f2896k[3]);
        if (this.f2893h != null) {
            gl10.glEnableClientState(32886);
            gl10.glColorPointer(4, 5126, 0, this.f2893h);
        }
        if (this.f2897l) {
            m4158c(gl10);
            this.f2897l = false;
            this.f2892g.recycle();
        }
        if (!(this.f2899n == -1 || this.f2898m == null)) {
            gl10.glEnable(3553);
            gl10.glEnableClientState(32888);
            gl10.glTexCoordPointer(2, 5126, 0, this.f2898m);
            gl10.glBindTexture(3553, this.f2899n);
        }
        gl10.glTranslatef(this.f2889d, this.f2890e, this.f2891f);
        gl10.glRotatef(this.f2886a, 1.0f, 0.0f, 0.0f);
        gl10.glRotatef(this.f2887b, 0.0f, 1.0f, 0.0f);
        gl10.glRotatef(this.f2888c, 0.0f, 0.0f, 1.0f);
        gl10.glDrawElements(4, this.f2895j, 5123, this.f2894i);
        if (this.f2899n != -1 && this.f2898m != null) {
            gl10.glDisableClientState(32888);
        }
    }

    protected void m4161a(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f2900o = allocateDirect.asFloatBuffer();
        this.f2900o.put(fArr);
        this.f2900o.position(0);
    }

    protected void m4162a(short[] sArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(sArr.length * 2);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f2894i = allocateDirect.asShortBuffer();
        this.f2894i.put(sArr);
        this.f2894i.position(0);
        this.f2895j = sArr.length;
    }

    public void m4163b(GL10 gl10) {
        if (this.f2901p != null) {
            gl10.glDeleteTextures(1, this.f2901p, 0);
        }
    }

    protected void m4164b(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f2898m = allocateDirect.asFloatBuffer();
        this.f2898m.put(fArr);
        this.f2898m.position(0);
    }
}
