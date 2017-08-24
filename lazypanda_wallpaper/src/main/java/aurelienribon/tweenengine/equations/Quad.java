package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Quad extends TweenEquation {
    public static final Quad IN = new C05591();
    public static final Quad INOUT = new C05613();
    public static final Quad OUT = new C05602();

    static class C05591 extends Quad {
        C05591() {
        }

        public final float compute(float t) {
            return t * t;
        }

        public String toString() {
            return "Quad.IN";
        }
    }

    static class C05602 extends Quad {
        C05602() {
        }

        public final float compute(float t) {
            return (-t) * (t - 2.0f);
        }

        public String toString() {
            return "Quad.OUT";
        }
    }

    static class C05613 extends Quad {
        C05613() {
        }

        public final float compute(float t) {
            t *= 2.0f;
            if (t < 1.0f) {
                return (0.5f * t) * t;
            }
            t -= 1.0f;
            return -0.5f * (((t - 2.0f) * t) - 1.0f);
        }

        public String toString() {
            return "Quad.INOUT";
        }
    }
}
