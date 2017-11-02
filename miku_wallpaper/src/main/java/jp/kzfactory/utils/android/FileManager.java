package jp.kzfactory.utils.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {
    static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static boolean exists_resource(String path) {
        try {
            context.getAssets().open(path).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static InputStream open_resource(String path) throws IOException {
        return context.getAssets().open(path);
    }

    public static boolean exists_cache(String path) {
        return new File(context.getCacheDir(), path).exists();
    }

    public static InputStream open_cache(String path) throws FileNotFoundException {
        return new FileInputStream(new File(context.getCacheDir(), path));
    }

    public static InputStream open(String path, boolean isCache) throws IOException {
        if (isCache) {
            return open_cache(path);
        }
        return open_resource(path);
    }

    public static InputStream open(String path) throws IOException {
        return open(path, false);
    }

    public static AssetFileDescriptor openFd(String path) throws IOException {
        return context.getAssets().openFd(path);
    }
}
