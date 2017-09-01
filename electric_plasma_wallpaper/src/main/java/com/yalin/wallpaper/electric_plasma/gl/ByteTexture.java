package com.yalin.wallpaper.electric_plasma.gl;

import android.util.Log;

import java.nio.ByteBuffer;

public class ByteTexture extends Texture {
    private ByteBuffer contents;
    private final int f2h;
    public boolean needsUpdating = false;
    private final int f3w;

    public int width() {
        return this.f3w;
    }

    public int height() {
        return this.f2h;
    }

    public ByteBuffer data() {
        return this.contents;
    }

    public int glType() {
        return 5121;
    }

    public ByteTexture(int id, byte[][] arr, int width, int height) {
        super(id);
        this.f3w = width;
        this.f2h = height;
        setData(arr);
        Log.d("plasma", "ByteTexture constructor finished.");
    }

    public void setData(byte[][] arr) {
        int maxWidth = Texture.roundUpPowerOfTwo(this.f3w);
        int maxHeight = Texture.roundUpPowerOfTwo(this.f2h);
        if (this.contents == null) {
            this.contents = ByteBuffer.allocateDirect(maxWidth * maxHeight);
        }
        for (int p = 0; p < this.f2h; p++) {
            this.contents.put(arr[p]).position((p + 1) * maxWidth);
        }
        this.contents.position(0);
    }
}
