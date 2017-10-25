package com.gtp.nextlauncher.liverpaper.tunnelbate.opengl;

import android.content.Context;
import android.opengl.GLES20;

import com.gtp.nextlauncher.liverpaper.tunnelbate.TunnelConfig;
import com.gtp.nextlauncher.liverpaper.tunnelbate.p019c.ShaderUtil;

import java.nio.FloatBuffer;

/* compiled from: CubesModel */
public class CubesModel {
    int f786a;
    int f787b;
    int f788c;
    int f789d;
    String f790e;
    String f791f;
    FloatBuffer f792g;
    FloatBuffer f793h;
    Point f794i;
    Point f795j;
    int f796k = 0;
    private boolean f797l = false;
    private boolean f798m = false;
    private float f799n = 0.0f;
    private float f800o = 0.0f;
    private float f801p = 0.0f;
    private boolean f802q = false;

    public void m1074a(Point point) {
        this.f795j = point;
    }

    public CubesModel(ModelData modelData) {
        this.f792g = modelData.m1092b();
        this.f793h = modelData.m1093c();
        this.f794i = modelData.m1095e();
        this.f796k = modelData.m1090a();
    }

    public void m1072a(int i) {
        GLES20.glUseProgram(this.f786a);
        float f = this.f795j.f891a;
        float f2 = this.f795j.f892b;
        float f3 = this.f795j.f893c;
        if (!TunnelConfig.sensor && Math.abs(this.f800o) > 0.0f) {
            this.f800o = 0.0f;
        }
        MatrixState.m1081a(this.f800o, 0.0f, 0.0f, 1.0f);
        MatrixState.m1080a(f, f2, f3);
        MatrixState.m1081a(this.f801p, 0.0f, 1.0f, 0.0f);
        MatrixState.m1080a(-f, -f2, -f3);
        GLES20.glUniformMatrix4fv(this.f787b, 1, false, MatrixState.m1088d(), 0);
        GLES20.glVertexAttribPointer(this.f788c, 3, 5126, false, 12, this.f792g);
        GLES20.glVertexAttribPointer(this.f789d, 2, 5126, false, 8, this.f793h);
        GLES20.glEnableVertexAttribArray(this.f789d);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glEnableVertexAttribArray(this.f788c);
        GLES20.glDrawArrays(4, 0, this.f796k);
        if (this.f798m) {
            this.f801p += this.f799n;
        }
        if (TunnelConfig.autoRun && !this.f797l) {
            this.f801p = (1 * 0.25f) + this.f801p;
        }
        this.f798m = false;
    }

    public void m1073a(Context context) {
        if (!this.f802q) {
            this.f790e = ShaderUtil.m927a("data/shader/vertex.sh", context.getResources());
            this.f791f = ShaderUtil.m927a("data/shader/frag.sh", context.getResources());
            this.f786a = ShaderUtil.m926a(this.f790e, this.f791f);
            this.f788c = GLES20.glGetAttribLocation(this.f786a, "aPosition");
            this.f787b = GLES20.glGetUniformLocation(this.f786a, "uMVPMatrix");
            this.f789d = GLES20.glGetAttribLocation(this.f786a, "aTexCoor");
            this.f802q = true;
        }
    }

    public void m1069a() {
        this.f792g.clear();
        this.f793h.clear();
    }

    public void m1070a(float f) {
        this.f798m = true;
        this.f799n = f;
    }

    public void m1071a(float f, boolean z) {
        float f2 = ((z ? -65.0f : -45.0f) * f) / 9.8f;
        if (z) {
            this.f800o = f2;
        } else {
            this.f800o = -f2;
        }
    }
}
