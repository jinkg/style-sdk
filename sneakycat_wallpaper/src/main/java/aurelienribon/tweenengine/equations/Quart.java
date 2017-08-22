package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Quart extends TweenEquation {
    public static final Quart IN = new C05621();
    public static final Quart INOUT = new C05643();
    public static final Quart OUT = new C05632();

    static class C05621 extends Quart {
        C05621() {
        }

        public final float compute(float t) {
            return ((t * t) * t) * t;
        }

        public String toString() {
            return "Quart.IN";
        }
    }

    static class C05632 extends Quart {
        C05632() {
        }

        public final float compute(float t) {
            t -= 1.0f;
            return -((((t * t) * t) * t) - 1.0f);
        }

        public String toString() {
            return "Quart.OUT";
        }
    }

    static class C05643 extends Quart {
        C05643() {
        }

        public final float compute(float t) {
            t *= 2.0f;
            if (t < 1.0f) {
                return (((0.5f * t) * t) * t) * t;
            }
            t -= 2.0f;
            return -0.5f * ((((t * t) * t) * t) - 2.0f);
        }

        public String toString() {
            return "Quart.INOUT";
        }
    }
}
