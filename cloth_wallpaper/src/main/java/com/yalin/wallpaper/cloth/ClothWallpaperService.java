package com.yalin.wallpaper.cloth;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yalin.wallpaper.cloth.render.ClothRenderer;

/**
 * Created by rares on 6/11/2014.
 */
public class ClothWallpaperService extends OpenGLES2WallpaperService {

    public ClothWallpaperService(Context host) {
        super(host);
    }

    @Override
    GLSurfaceView.Renderer getNewRenderer() {
        return new ClothRenderer(getApplicationContext());
    }
}
