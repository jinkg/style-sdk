package com.yalin.wallpaper.hexshader.base.p003b;

public class C0012d {
    public static final float f28a = ((float) (Math.sqrt(3.0d) / 2.0d));
    public static final byte[] f29b;
    public static final byte[] f30c;
    public static final float[] f31d = new float[]{120.0f, 180.0f, 240.0f, 300.0f, 0.0f, 60.0f};
    private static final byte[] f32e;

    static {
        byte[] bArr = new byte[9];
        bArr[0] = (byte) -1;
        bArr[1] = (byte) 5;
        bArr[3] = (byte) 4;
        bArr[4] = (byte) -1;
        bArr[5] = (byte) 1;
        bArr[6] = (byte) 3;
        bArr[7] = (byte) 2;
        bArr[8] = (byte) -1;
        f32e = bArr;
        bArr = new byte[6];
        bArr[0] = (byte) 1;
        bArr[1] = (byte) 1;
        bArr[3] = (byte) -1;
        bArr[4] = (byte) -1;
        f29b = bArr;
        bArr = new byte[6];
        bArr[0] = (byte) -1;
        bArr[2] = (byte) 1;
        bArr[3] = (byte) 1;
        bArr[5] = (byte) -1;
        f30c = bArr;
    }

    public static final float m28a(int i) {
        return ((float) i) * 0.75f;
    }

    public static final float m29a(int i, int i2) {
        return (((float) i) * f28a) + ((((float) i2) * f28a) * 0.5f);
    }
}
