package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Sine extends TweenEquation {
    public static final Sine IN = new C05681();
    public static final Sine INOUT = new C05703();
    public static final Sine OUT = new C05692();
    private static final float PI = 3.1415927f;

    static class C05681 extends Sine {
        C05681() {
        }

        public final float compute(float t) {
            return ((float) (-Math.cos((double) (1.5707964f * t)))) + 1.0f;
        }

        public String toString() {
            return "Sine.IN";
        }
    }

    static class C05692 extends Sine {
        C05692() {
        }

        public final float compute(float t) {
            return (float) Math.sin((double) (1.5707964f * t));
        }

        public String toString() {
            return "Sine.OUT";
        }
    }

    static class C05703 extends Sine {
        C05703() {
        }

        public final float compute(float t) {
            return -0.5f * (((float) Math.cos((double) (3.1415927f * t))) - 1.0f);
        }

        public String toString() {
            return "Sine.INOUT";
        }
    }
}
