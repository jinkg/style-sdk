package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Circ extends TweenEquation {
    public static final Circ IN = new C05461();
    public static final Circ INOUT = new C05483();
    public static final Circ OUT = new C05472();

    static class C05461 extends Circ {
        C05461() {
        }

        public final float compute(float t) {
            return ((float) (-Math.sqrt((double) (1.0f - (t * t))))) - 1.0f;
        }

        public String toString() {
            return "Circ.IN";
        }
    }

    static class C05472 extends Circ {
        C05472() {
        }

        public final float compute(float t) {
            t -= 1.0f;
            return (float) Math.sqrt((double) (1.0f - (t * t)));
        }

        public String toString() {
            return "Circ.OUT";
        }
    }

    static class C05483 extends Circ {
        C05483() {
        }

        public final float compute(float t) {
            t *= 2.0f;
            if (t < 1.0f) {
                return -0.5f * (((float) Math.sqrt((double) (1.0f - (t * t)))) - 1.0f);
            }
            t -= 2.0f;
            return 0.5f * (((float) Math.sqrt((double) (1.0f - (t * t)))) + 1.0f);
        }

        public String toString() {
            return "Circ.INOUT";
        }
    }
}
