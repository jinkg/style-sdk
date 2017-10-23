package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.content.Context;
import android.opengl.GLES20;

import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0153a;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.C0158f;

import java.nio.FloatBuffer;

/* compiled from: ModelObject */
public class C0186g {
    int f818a;
    int f819b;
    int f820c;
    int f821d;
    String f822e;
    String f823f;
    FloatBuffer f824g;
    FloatBuffer f825h;
    FloatBuffer f826i;
    C0197r f827j;
    int f828k = 0;
    float f829l = 0.0f;
    public float f830m = 0.0f;
    private float f831n = 0.0f;
    private boolean f832o = false;
    private boolean f833p = false;
    private float f834q = 0.0f;
    private float f835r = 0.0f;
    private boolean f836s = true;
    private boolean f837t = false;
    private float f838u;
    private boolean f839v = false;

    public void m1101a(boolean z) {
        this.f837t = z;
    }

    public void m1105b(boolean z) {
        this.f836s = z;
    }

    public C0186g(C0185f c0185f) {
        this.f824g = c0185f.m1092b();
        this.f825h = c0185f.m1093c();
        this.f826i = c0185f.m1094d();
        this.f827j = c0185f.m1095e();
        this.f828k = c0185f.m1090a();
    }

    public boolean m1102a() {
        return this.f833p;
    }

    public void m1098a(int i) {
        GLES20.glUseProgram(this.f818a);
        float f = this.f827j.f891a;
        float f2 = this.f827j.f892b;
        float f3 = this.f827j.f893c;
        boolean sensor = false;
        if (!sensor && Math.abs(this.f830m) > 0.0f) {
            this.f830m = 0.0f;
        }
        C0184e.m1081a(this.f830m, 0.0f, 0.0f, 1.0f);
        C0184e.m1080a(f, f2, f3);
        C0184e.m1081a(this.f829l, 0.0f, 1.0f, 0.0f);
        C0184e.m1080a(-f, -f2, -f3);
        GLES20.glUniformMatrix4fv(this.f819b, 1, false, C0184e.m1088d(), 0);
        GLES20.glVertexAttribPointer(this.f820c, 3, 5126, false, 12, this.f824g);
        GLES20.glVertexAttribPointer(this.f821d, 2, 5126, false, 8, this.f825h);
        GLES20.glEnableVertexAttribArray(this.f821d);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glEnableVertexAttribArray(this.f820c);
        GLES20.glDrawArrays(4, 0, this.f828k);
        if (this.f832o) {
            this.f829l += this.f831n;
        }
        if (this.f839v) {
            this.f829l += this.f838u;
        }
        boolean autoRun = false;
        if (autoRun && !this.f837t) {
            this.f829l = (C0153a.getAutoRunSpeed() * 0.25f) + this.f829l;
        }
        if (this.f832o) {
            this.f832o = false;
        }
        if (this.f839v) {
            this.f839v = false;
        }
        this.f831n = 0.0f;
        this.f838u = 0.0f;
    }

    public float m1103b() {
        return this.f829l;
    }

    public void m1096a(float f) {
        this.f829l = f;
    }

    public void m1099a(Context context) {
        if (!this.f833p) {
            this.f822e = C0158f.m927a("data/shader/vertex.sh", context.getResources());
            this.f823f = C0158f.m927a("data/shader/frag.sh", context.getResources());
            this.f818a = C0158f.m926a(this.f822e, this.f823f);
            this.f820c = GLES20.glGetAttribLocation(this.f818a, "aPosition");
            this.f819b = GLES20.glGetUniformLocation(this.f818a, "uMVPMatrix");
            this.f821d = GLES20.glGetAttribLocation(this.f818a, "aTexCoor");
            this.f833p = true;
        }
    }

    public void m1100a(String str, String str2, int i, int i2, int i3, int i4) {
        this.f822e = str;
        this.f823f = str2;
        this.f820c = i2;
        this.f819b = i3;
        this.f821d = i4;
        this.f818a = i;
    }

    public int m1106c() {
        return this.f818a;
    }

    public void m1097a(float f, boolean z) {
        float f2 = ((z ? -65.0f : -45.0f) * f) / 9.8f;
        if (z) {
            this.f830m = f2;
        } else {
            this.f830m = -f2;
        }
    }

    public void m1108d() {
        this.f824g.clear();
        this.f825h.clear();
    }

    public void m1104b(float f) {
        this.f832o = true;
        this.f831n = f;
    }

    public int m1109e() {
        return this.f819b;
    }

    public int m1110f() {
        return this.f820c;
    }

    public int m1111g() {
        return this.f821d;
    }

    public String m1112h() {
        return this.f822e;
    }

    public String m1113i() {
        return this.f823f;
    }

    public void m1107c(float f) {
        this.f839v = true;
        this.f838u = f;
    }

    public C0197r m1114j() {
        return new C0197r(528.9468f, 0.0f, -9.410217f);
    }
}
