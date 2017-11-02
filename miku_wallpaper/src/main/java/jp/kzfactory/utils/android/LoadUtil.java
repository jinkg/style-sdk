package jp.kzfactory.utils.android;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.opengl.GLUtils;
import android.util.Log;

import com.yalin.wallpaper.miku.LAppDefine;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import jp.live2d.motion.Live2DMotion;
import jp.live2d.util.UtDebug;
import jp.live2d.util.UtFile;

public class LoadUtil {
    static final int GEN_TEX_LOOP = 999;

    public static int loadTexture(GL10 gl, InputStream in, boolean mipmap) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        if (mipmap) {
            return buildMipmap(gl, bitmap);
        }
        int texture = genTexture(gl);
        gl.glBindTexture(3553, texture);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexEnvf(8960, 8704, 8448.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        return texture;
    }

    public static int genTexture(GL10 gl) {
        int texture = 0;
        int i = 0;
        while (i < GEN_TEX_LOOP) {
            int[] ret = new int[]{0};
            gl.glGenTextures(1, ret, 0);
            texture = ret[0];
            if (texture >= 0) {
                break;
            }
            gl.glDeleteTextures(1, ret, 0);
            i++;
        }
        if (i != GEN_TEX_LOOP) {
            return texture;
        }
        UtDebug.error("gen texture loops over 999times @UtOpenGL", new Object[0]);
        return 0;
    }

    public static int buildMipmap(GL10 gl, Bitmap bitmap) {
        return buildMipmap(gl, bitmap, true);
    }

    public static int buildMipmap(GL10 gl, Bitmap srcBitmap, boolean recycle) {
        Bitmap bitmap = srcBitmap;
        int level = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int textureID = genTexture(gl);
        gl.glBindTexture(3553, textureID);
        try {
            ((GL11) gl).glTexParameteri(3553, 33169, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10241, 9987.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexEnvf(8960, 8704, 8448.0f);
        while (height >= 1 && width >= 1) {
            GLUtils.texImage2D(3553, level, bitmap, 0);
            if (height != 1 && width != 1) {
                level++;
                height /= 2;
                width /= 2;
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
                if (recycle || bitmap != srcBitmap) {
                    bitmap.recycle();
                }
                bitmap = bitmap2;
            } else if (recycle || bitmap != srcBitmap) {
                bitmap.recycle();
                break;
            } else {
                break;
            }
        }
        return textureID;
    }

    public static Live2DMotion loadAssetsMotion(String path) {
        Live2DMotion motion = null;
        if (LAppDefine.DEBUG_LOG) {
            Log.d("", "モーション読み込み : " + path);
        }
        try {
            InputStream in = FileManager.open(path);
            motion = Live2DMotion.loadMotion(UtFile.load(in));
            in.close();
            return motion;
        } catch (IOException e) {
            e.printStackTrace();
            return motion;
        }
    }

    public static MediaPlayer loadAssetsSound(String filename) {
        if (LAppDefine.DEBUG_LOG) {
            Log.d("", "音声読み込み : " + filename);
        }
        MediaPlayer player = new MediaPlayer();
        try {
            AssetFileDescriptor assetFileDescritorArticle = FileManager.openFd(filename);
            player.reset();
            player.setDataSource(assetFileDescritorArticle.getFileDescriptor(), assetFileDescritorArticle.getStartOffset(), assetFileDescritorArticle.getLength());
            player.setAudioStreamType(3);
            assetFileDescritorArticle.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return player;
    }
}
