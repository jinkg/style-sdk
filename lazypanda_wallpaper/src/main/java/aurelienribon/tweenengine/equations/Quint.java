package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Quint extends TweenEquation {
    public static final Quint IN = new C05651();
    public static final Quint INOUT = new C05673();
    public static final Quint OUT = new C05662();

    static class C05651 extends Quint {
        C05651() {
        }

        public final float compute(float t) {
            return (((t * t) * t) * t) * t;
        }

        public String toString() {
            return "Quint.IN";
        }
    }

    static class C05662 extends Quint {
        C05662() {
        }

        public final float compute(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }

        public String toString() {
            return "Quint.OUT";
        }
    }

    static class C05673 extends Quint {
        C05673() {
        }

        public final float compute(float t) {
            t *= 2.0f;
            if (t < 1.0f) {
                return ((((0.5f * t) * t) * t) * t) * t;
            }
            t -= 2.0f;
            return (((((t * t) * t) * t) * t) + 2.0f) * 0.5f;
        }

        public String toString() {
            return "Quint.INOUT";
        }
    }
}
