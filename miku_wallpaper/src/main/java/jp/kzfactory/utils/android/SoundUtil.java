package jp.kzfactory.utils.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

import java.io.IOException;

public class SoundUtil {
    public static void release(MediaPlayer player) {
        if (player != null) {
            player.setOnCompletionListener(null);
            player.release();
        }
    }

    public static void playAssets(Context context, String filename) {
        final MediaPlayer player = new MediaPlayer();
        try {
            AssetFileDescriptor assetFileDescritorArticle = context.getAssets().openFd(filename);
            player.reset();
            player.setDataSource(assetFileDescritorArticle.getFileDescriptor(), assetFileDescritorArticle.getStartOffset(), assetFileDescritorArticle.getLength());
            player.setAudioStreamType(3);
            assetFileDescritorArticle.close();
            player.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
            player.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    SoundUtil.release(player);
                }
            });
            player.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
