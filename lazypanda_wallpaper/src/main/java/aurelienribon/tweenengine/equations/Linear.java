package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Linear extends TweenEquation {
    public static final Linear INOUT = new C05581();

    static class C05581 extends Linear {
        C05581() {
        }

        public float compute(float t) {
            return t;
        }

        public String toString() {
            return "Linear.INOUT";
        }
    }
}
