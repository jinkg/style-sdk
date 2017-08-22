package aurelienribon.tweenengine;

import aurelienribon.tweenengine.paths.CatmullRom;
import aurelienribon.tweenengine.paths.Linear;

public interface TweenPaths {
    public static final CatmullRom catmullRom = new CatmullRom();
    public static final Linear linear = new Linear();
}
