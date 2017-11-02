package jp.kzfactory.utils.android;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ImageClip {
    static ByteBuffer colorBuffer;
    static FloatBuffer drawRectBuffer;
    static float[] squareVertices = new float[8];

    public static void drawClippedRect(GL10 gl, float left, float right, float bottom, float top, float clipLeft, float clipRight, float clipBottom, float clipTop, int color) {
        drawRect(gl, left, clipLeft, bottom, top, color);
        drawRect(gl, clipRight, right, bottom, top, color);
        drawRect(gl, clipLeft, clipRight, bottom, clipBottom, color);
        drawRect(gl, clipLeft, clipRight, clipTop, top, color);
    }

    public static void drawRect(GL10 gl, float left, float right, float bottom, float top, int argb) {
        int alpha = (argb >> 24) & 255;
        int red = (argb >> 16) & 255;
        int green = (argb >> 8) & 255;
        int blue = argb & 255;
        squareVertices[0] = left;
        squareVertices[1] = top;
        squareVertices[2] = right;
        squareVertices[3] = top;
        squareVertices[4] = left;
        squareVertices[5] = bottom;
        squareVertices[6] = right;
        squareVertices[7] = bottom;
        byte[] squareColors = new byte[]{(byte) red, (byte) green, (byte) blue, (byte) alpha, (byte) red, (byte) green, (byte) blue, (byte) alpha, (byte) red, (byte) green, (byte) blue, (byte) alpha, (byte) red, (byte) green, (byte) blue, (byte) alpha};
        drawRectBuffer = BufferUtil.setupFloatBuffer(drawRectBuffer, squareVertices);
        colorBuffer = BufferUtil.setupByteBuffer(colorBuffer, squareColors);
        gl.glDisable(3553);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32886);
        gl.glVertexPointer(2, 5126, 0, drawRectBuffer);
        gl.glColorPointer(4, 5121, 0, colorBuffer);
        gl.glDrawArrays(5, 0, 4);
        gl.glDisableClientState(32886);
        gl.glDisableClientState(32884);
    }
}
