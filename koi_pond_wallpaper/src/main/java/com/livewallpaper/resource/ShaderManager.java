package com.livewallpaper.resource;

import com.badlogic.gdx.utils.Disposable;
import com.ist.lwp.koipond.natives.NativeShaderManager;

public class ShaderManager implements Disposable {
    private static ShaderManager sInstance = null;
    NativeShaderManager nativeShaderManager = new NativeShaderManager();

    private ShaderManager() {
    }

    public static ShaderManager getInstance() {
        if (sInstance == null) {
            sInstance = new ShaderManager();
        }
        return sInstance;
    }

    public void dispose() {
        sInstance = null;
    }
}
