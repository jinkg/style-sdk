package com.yalin.wallpaper.hypnoclock.p030a;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class C1545a {
    public int f6028a;
    public int f6029b;
    public int f6030c;
    public int f6031d;
    public int f6032e;
    private FloatBuffer f6033f;
    private FloatBuffer f6034g;
    private float[] f6035h = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
    private float[] f6036i = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};

    public C1545a() {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.f6035h.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f6033f = allocateDirect.asFloatBuffer();
        this.f6033f.put(this.f6035h);
        this.f6033f.position(0);
        allocateDirect = ByteBuffer.allocateDirect(this.f6036i.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.f6034g = allocateDirect.asFloatBuffer();
        this.f6034g.put(this.f6036i);
        this.f6034g.position(0);
    }

    public void m8524a(GL10 gl10) {
        gl10.glEnableClientState(32884);
        gl10.glEnableClientState(32888);
        gl10.glVertexPointer(3, 5126, 0, this.f6033f);
        gl10.glTexCoordPointer(2, 5126, 0, this.f6034g);
        gl10.glDrawArrays(5, 0, this.f6035h.length / 3);
        gl10.glDisableClientState(32884);
        gl10.glDisableClientState(32888);
    }
}
