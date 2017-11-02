package jp.kzfactory.utils.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

public class OffscreenImage {
    public static final int OFFSCREEN_SIZE = 512;
    private static int defaultFrameBuffer;
    private static ShortBuffer indexBuffer;
    private static int offscreenFrameBuffer;
    private static int offscreenTexture = -1;
    private static FloatBuffer uvBuffer;
    private static FloatBuffer vertexBuffer;
    private static int viewportHeight;
    private static int viewportWidth;

    public static void setOnscreen(GL10 gl) {
        ((GL11ExtensionPack) gl).glBindFramebufferOES(36160, defaultFrameBuffer);
        gl.glViewport(0, 0, viewportWidth, viewportHeight);
    }

    public static void setOffscreen(GL10 gl) {
        ((GL11ExtensionPack) gl).glBindFramebufferOES(36160, offscreenFrameBuffer);
        gl.glViewport(0, 0, 512, 512);
    }

    public static void createFrameBuffer(GL10 gl, int width, int height, int fbo) {
        viewportWidth = width;
        viewportHeight = height;
        defaultFrameBuffer = fbo;
        if (offscreenTexture > 0) {
            releaseFrameBuffer(gl);
        }
        int[] buffers = new int[1];
        gl.glGenTextures(1, buffers, 0);
        gl.glBindTexture(3553, buffers[0]);
        offscreenTexture = buffers[0];
        Bitmap bitmap = Bitmap.createBitmap(512, 512, Config.ARGB_8888);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        gl.glTexParameterf(3553, 10241, 9728.0f);
        gl.glTexParameterf(3553, 10240, 9728.0f);
        GL11ExtensionPack gl11ep = (GL11ExtensionPack) gl;
        int[] framebuffers = new int[1];
        gl11ep.glGenFramebuffersOES(1, framebuffers, 0);
        gl11ep.glBindFramebufferOES(36160, framebuffers[0]);
        gl11ep.glFramebufferTexture2DOES(36160, 36064, 3553, offscreenTexture, 0);
        int status = gl11ep.glCheckFramebufferStatusOES(36160);
        if (status != 36053) {
            throw new RuntimeException("Framebuffer is not complete: " + Integer.toHexString(status));
        }
        offscreenFrameBuffer = framebuffers[0];
        gl11ep.glBindFramebufferOES(36160, defaultFrameBuffer);
        uvBuffer = BufferUtil.createFloatBuffer(8);
        uvBuffer.put(0.0f);
        uvBuffer.put(0.0f);
        uvBuffer.put(1.0f);
        uvBuffer.put(0.0f);
        uvBuffer.put(0.0f);
        uvBuffer.put(1.0f);
        uvBuffer.put(1.0f);
        uvBuffer.put(1.0f);
        uvBuffer.position(0);
        vertexBuffer = BufferUtil.createFloatBuffer(8);
        vertexBuffer.put(-1.0f);
        vertexBuffer.put(((float) (-height)) / ((float) width));
        vertexBuffer.put(1.0f);
        vertexBuffer.put(((float) (-height)) / ((float) width));
        vertexBuffer.put(-1.0f);
        vertexBuffer.put(((float) height) / ((float) width));
        vertexBuffer.put(1.0f);
        vertexBuffer.put(((float) height) / ((float) width));
        vertexBuffer.position(0);
        indexBuffer = ShortBuffer.allocate(6);
        indexBuffer.put((short) 0);
        indexBuffer.put((short) 1);
        indexBuffer.put((short) 2);
        indexBuffer.put((short) 2);
        indexBuffer.put((short) 1);
        indexBuffer.put((short) 3);
        indexBuffer.position(0);
    }

    public static void releaseFrameBuffer(GL10 gl) {
        gl.glDeleteTextures(1, new int[]{offscreenTexture}, 0);
        offscreenTexture = -1;
    }

    public static void drawDisplay(GL10 gl, float opacity) {
        gl.glEnable(3553);
        gl.glEnable(3042);
        gl.glBlendFunc(1, 771);
        gl.glColor4f(opacity, opacity, opacity, opacity);
        gl.glBindTexture(3553, offscreenTexture);
        gl.glEnableClientState(32888);
        gl.glEnableClientState(32884);
        gl.glTexCoordPointer(2, 5126, 0, uvBuffer);
        gl.glVertexPointer(2, 5126, 0, vertexBuffer);
        gl.glDrawElements(4, 6, 5123, indexBuffer);
    }
}
