package aurelienribon.tweenengine.paths;

import aurelienribon.tweenengine.TweenPath;

public class Linear implements TweenPath {
    public float compute(float t, float[] points, int pointsCnt) {
        int segment = Math.min(Math.max((int) Math.floor((double) (((float) (pointsCnt - 1)) * t)), 0), pointsCnt - 2);
        return points[segment] + ((points[segment + 1] - points[segment]) * ((((float) (pointsCnt - 1)) * t) - ((float) segment)));
    }
}
