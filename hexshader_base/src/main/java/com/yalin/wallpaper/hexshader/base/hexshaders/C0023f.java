package com.yalin.wallpaper.hexshader.base.hexshaders;

import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.yalin.wallpaper.hexshader.base.p000a.p001a.C0002b;
import com.yalin.wallpaper.hexshader.base.p003b.C0009a;

import java.nio.FloatBuffer;

public class C0023f extends C0002b {
    public int f79a = m3a("u_size");
    public int f80b = m3a("iGlobalTime");
    public int f81c = m3a("u_offset");
    public int f82d = m3a("iResolution");
    public int f83e = m3a("u_texture");
    public int f84f = m5b("a_pos");
    public int f85g = m5b("a_col");

    public C0023f(AssetManager assetManager, String str) {
        super(C0009a.m23a(str, assetManager));
        C0002b.m2b();
    }

    public void m51a(float f, float f2, int i, float f3, float f4, float f5) {
        GLES20.glUniform1f(this.f79a, f);
        GLES20.glUniform1f(this.f80b, f2);
        GLES20.glUniform1f(this.f81c, f5);
        GLES20.glUniform1i(this.f83e, i);
        GLES20.glUniform2f(this.f82d, f3, f4);
    }

    public void m52a(int i) {
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 1);
        GLES20.glDrawArrays(0, 0, i);
    }

    public void m53a(FloatBuffer floatBuffer) {
        floatBuffer.position(0);
        GLES20.glVertexAttribPointer(this.f84f, 2, 5126, false, 12, floatBuffer);
        GLES20.glEnableVertexAttribArray(this.f84f);
        floatBuffer.position(2);
        GLES20.glVertexAttribPointer(this.f85g, 4, 5121, true, 12, floatBuffer);
        GLES20.glEnableVertexAttribArray(this.f85g);
    }
}
