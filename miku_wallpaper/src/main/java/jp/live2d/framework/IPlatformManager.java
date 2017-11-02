package jp.live2d.framework;

import jp.live2d.ALive2DModel;

public interface IPlatformManager {
    byte[] loadBytes(String str);

    ALive2DModel loadLive2DModel(String str);

    String loadString(String str);

    void loadTexture(ALive2DModel aLive2DModel, int i, String str);

    void log(String str);
}
