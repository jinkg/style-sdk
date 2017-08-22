package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Bounce extends TweenEquation {
    public static final Bounce IN = new C05431();
    public static final Bounce INOUT = new C05453();
    public static final Bounce OUT = new C05442();

    static class C05431 extends Bounce {
        C05431() {
        }

        public final float compute(float t) {
            return 1.0f - OUT.compute(1.0f - t);
        }

        public String toString() {
            return "Bounce.IN";
        }
    }

    static class C05442 extends Bounce {
        C05442() {
        }

        public final float compute(float t) {
            if (((double) t) < 0.36363636363636365d) {
                return (7.5625f * t) * t;
            }
            if (((double) t) < 0.7272727272727273d) {
                t -= 0.54545456f;
                return ((7.5625f * t) * t) + 0.75f;
            } else if (((double) t) < 0.9090909090909091d) {
                t -= 0.8181818f;
                return ((7.5625f * t) * t) + 0.9375f;
            } else {
                t -= 0.95454544f;
                return ((7.5625f * t) * t) + 0.984375f;
            }
        }

        public String toString() {
            return "Bounce.OUT";
        }
    }

    static class C05453 extends Bounce {
        C05453() {
        }

        public final float compute(float t) {
            if (t < 0.5f) {
                return IN.compute(2.0f * t) * 0.5f;
            }
            return (OUT.compute((2.0f * t) - 1.0f) * 0.5f) + 0.5f;
        }

        public String toString() {
            return "Bounce.INOUT";
        }
    }
}
