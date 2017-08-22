package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Cubic extends TweenEquation {
    public static final Cubic IN = new C05491();
    public static final Cubic INOUT = new C05513();
    public static final Cubic OUT = new C05502();

    static class C05491 extends Cubic {
        C05491() {
        }

        public final float compute(float t) {
            return (t * t) * t;
        }

        public String toString() {
            return "Cubic.IN";
        }
    }

    static class C05502 extends Cubic {
        C05502() {
        }

        public final float compute(float t) {
            t -= 1.0f;
            return ((t * t) * t) + 1.0f;
        }

        public String toString() {
            return "Cubic.OUT";
        }
    }

    static class C05513 extends Cubic {
        C05513() {
        }

        public final float compute(float t) {
            t *= 2.0f;
            if (t < 1.0f) {
                return ((0.5f * t) * t) * t;
            }
            t -= 2.0f;
            return (((t * t) * t) + 2.0f) * 0.5f;
        }

        public String toString() {
            return "Cubic.INOUT";
        }
    }
}
