package jp.live2d.framework;

public class Live2DFramework {
    private static IPlatformManager platformManager;

    public static IPlatformManager getPlatformManager() {
        return platformManager;
    }

    public static void setPlatformManager(IPlatformManager platformManager) {
        platformManager = platformManager;
    }
}
