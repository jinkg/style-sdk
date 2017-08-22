package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Expo extends TweenEquation {
    public static final Expo IN = new C05551();
    public static final Expo INOUT = new C05573();
    public static final Expo OUT = new C05562();

    static class C05551 extends Expo {
        C05551() {
        }

        public final float compute(float t) {
            return t == 0.0f ? 0.0f : (float) Math.pow(2.0d, (double) (10.0f * (t - 1.0f)));
        }

        public String toString() {
            return "Expo.IN";
        }
    }

    static class C05562 extends Expo {
        C05562() {
        }

        public final float compute(float t) {
            return t == 1.0f ? 1.0f : 1.0f + (-((float) Math.pow(2.0d, (double) (-10.0f * t))));
        }

        public String toString() {
            return "Expo.OUT";
        }
    }

    static class C05573 extends Expo {
        C05573() {
        }

        public final float compute(float t) {
            if (t == 0.0f) {
                return 0.0f;
            }
            if (t == 1.0f) {
                return 1.0f;
            }
            t *= 2.0f;
            if (t < 1.0f) {
                return ((float) Math.pow(2.0d, (double) (10.0f * (t - 1.0f)))) * 0.5f;
            }
            return ((-((float) Math.pow(2.0d, (double) (-10.0f * (t - 1.0f))))) + 2.0f) * 0.5f;
        }

        public String toString() {
            return "Expo.INOUT";
        }
    }
}
