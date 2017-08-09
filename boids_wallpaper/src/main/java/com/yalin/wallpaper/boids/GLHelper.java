package com.yalin.wallpaper.boids;

import android.graphics.Bitmap;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

// derived from https://github.com/lithium/android-game
class GLHelper
{
    private static final String TAG = "GLHelper";

    private static String vendor;
    private static String renderer;
    private static String version;
    private static String extensions;

    public static void init(GL10 gl) {
        inspect(gl);
    }

    private static void inspect(GL10 gl) {
        vendor = gl.glGetString(GL10.GL_VENDOR);
        renderer = gl.glGetString(GL10.GL_RENDERER);
        version = gl.glGetString(GL10.GL_VERSION);
        extensions = gl.glGetString(GL10.GL_EXTENSIONS);
    }

    public static String getVendor() {
        return vendor;
    }    

    public static String getRenderer() {
        return renderer;
    }    

    public static String getVersion() {
        return version;
    }    

    public static String getExtensions() {
        return extensions;
    }    

    private static boolean hasExtension(String extension) {
        return extensions.indexOf(extension) >= 0;        
    }

    public static boolean hasVertexBufferObject() {
        return hasExtension("vertex_buffer_object");
    }

    public static boolean hasDrawTexture() {
        return hasExtension("draw_texture");
    }

    public static boolean hasPointSprite() {
        return hasExtension("point_sprite");
    }

    public static ByteBuffer byteBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf;
    }

    public static IntBuffer intBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size * 4);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf.asIntBuffer();
    }

    public static FloatBuffer floatBuffer(int size) {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(size * 4);
        byte_buf.order(ByteOrder.nativeOrder());
        return byte_buf.asFloatBuffer();
    }

    public static FloatBuffer toFloatBuffer(float[] data) {
        FloatBuffer rv = floatBuffer(data.length);

        for(int i=0; i<data.length; i++)
            rv.put(data[i]);
        
        rv.position(0);

        return rv;
    }

    public static FloatBuffer toFloatBuffer(Vector3[] data) {
        FloatBuffer rv = floatBuffer(data.length*3);

        for(int i=0; i<data.length; i++) {
            rv.put(data[i].x);
            rv.put(data[i].y);
            rv.put(data[i].z);
        }

        rv.position(0);

        return rv;
    }

    public static FloatBuffer toFloatBuffer(Vector2[] data) {
        FloatBuffer rv = floatBuffer(data.length*2);

        for(int i=0; i<data.length; i++) {
            rv.put(data[i].x);
            rv.put(data[i].y);
        }

        rv.position(0);

        return rv;
    }
    
    private static int createTexture(GL10 gl) {
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);

        if (BuildConfig.DEBUG) Log.d(TAG, "Created Texture: " + textures[0]);

        return textures[0];
    }

    public static int loadTexture(GL10 gl, Bitmap bitmap) {
        int texture = createTexture(gl);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);        
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        return texture;
    }
    
    private static int createBufferObject(GL10 gl) {
        int[] rv = new int[1];
        ((GL11)gl).glGenBuffers(1, rv, 0);
        return rv[0];
    }
    
    public static int loadBufferObject(GL10 gl, FloatBuffer data) {
        int buffer = createBufferObject(gl);

        ((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, buffer);
        ((GL11)gl).glBufferData(GL11.GL_ARRAY_BUFFER, data.limit() * 4, data, 
                                GL11.GL_STATIC_DRAW);

        return buffer;
    }

    // TODO implement fallback to a single rect if needed
    public static void drawTexture(GL10 gl, int x, int y, int z, int w, int h) {
        int[] crop = {0, h, w, -h}; 

        ((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
                                    GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);

        ((GL11Ext) gl).glDrawTexfOES(x, y, z, w, h); 

    }

    // TODO implement with fallback to a single rect if needed
    public static void drawPoints(GL10 gl, FloatBuffer points,
                                  FloatBuffer sizes, FloatBuffer colors) {
    }

    public static Vector3 projectTouchToWorld(int width, int height, float[] modelView, float[] projection, float x, float y) {
        int[] view = new int[] {0, 0, width, height};

        float[] touch_position = new float[4];
        int r = GLU.gluUnProject(x, view[3] - y, 1f,
                modelView, 0,
                projection, 0,
                view, 0,
                touch_position, 0);

        touch_position[0] /= touch_position[3];
        touch_position[1] /= touch_position[3];
        touch_position[2] /= touch_position[3];
        touch_position[3] /= touch_position[3];

        Vector3 rv = new Vector3(touch_position[0], touch_position[1], touch_position[2]);
        rv.normalize();
        return rv;
 }

}
