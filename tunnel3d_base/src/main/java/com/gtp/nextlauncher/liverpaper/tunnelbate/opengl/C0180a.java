package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.content.Context;
import android.opengl.GLES20;

import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0158f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* compiled from: Cube */
public class C0180a {
    int f775a;
    int f776b;
    int f777c;
    int f778d;
    String f779e;
    String f780f;
    FloatBuffer f781g;
    FloatBuffer f782h;
    int f783i;
    private float f784j;
    private float f785k;

    public C0180a(Context context) {
        this.f783i = 0;
        this.f784j = 0.0f;
        this.f785k = 0.0f;
        this.f784j = 0.3f;
        m1065a();
        m1067a(context);
    }

    public void m1068a(boolean z) {
        this.f784j = z ? 0.3f : 0.51000005f;
        m1065a();
    }

    public void m1065a() {
        float[] fArr = new float[]{-this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, this.f784j, -this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, this.f784j, -this.f784j, -this.f784j, this.f784j, -this.f784j, this.f784j};
        this.f783i = 36;
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f781g = allocateDirect.asFloatBuffer();
        this.f781g.put(fArr);
        this.f781g.position(0);
        fArr = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f782h = allocateDirect.asFloatBuffer();
        this.f782h.put(fArr);
        this.f782h.position(0);
    }

    public void m1067a(Context context) {
        this.f779e = C0158f.m927a("data/shader/vertex.sh", context.getResources());
        this.f780f = C0158f.m927a("data/shader/frag.sh", context.getResources());
        this.f775a = C0158f.m926a(this.f779e, this.f780f);
        this.f777c = GLES20.glGetAttribLocation(this.f775a, "aPosition");
        this.f776b = GLES20.glGetUniformLocation(this.f775a, "uMVPMatrix");
        this.f778d = GLES20.glGetAttribLocation(this.f775a, "aTexCoor");
    }

    public void m1066a(int i, C0197r c0197r, boolean z, boolean z2, float f) {
        GLES20.glUseProgram(this.f775a);
        GLES20.glUniformMatrix4fv(this.f776b, 1, false, C0184e.m1088d(), 0);
        GLES20.glVertexAttribPointer(this.f777c, 3, 5126, false, 12, this.f781g);
        GLES20.glVertexAttribPointer(this.f778d, 2, 5126, false, 8, this.f782h);
        GLES20.glEnableVertexAttribArray(this.f777c);
        GLES20.glEnableVertexAttribArray(this.f778d);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glDrawArrays(4, 0, this.f783i);
        this.f785k += 8.0f;
    }
}
