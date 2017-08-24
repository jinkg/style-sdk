package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Back extends TweenEquation {
    public static final Back IN = new C05401();
    public static final Back INOUT = new C05423();
    public static final Back OUT = new C05412();
    protected float param_s = 1.70158f;

    static class C05401 extends Back {
        C05401() {
        }

        public final float compute(float t) {
            float s = this.param_s;
            return (t * t) * (((1.0f + s) * t) - s);
        }

        public String toString() {
            return "Back.IN";
        }
    }

    static class C05412 extends Back {
        C05412() {
        }

        public final float compute(float t) {
            float s = this.param_s;
            t -= 1.0f;
            return ((t * t) * (((s + 1.0f) * t) + s)) + 1.0f;
        }

        public String toString() {
            return "Back.OUT";
        }
    }

    static class C05423 extends Back {
        C05423() {
        }

        public final float compute(float t) {
            float s = this.param_s;
            t *= 2.0f;
            if (t < 1.0f) {
                s *= 1.525f;
                return ((t * t) * (((1.0f + s) * t) - s)) * 0.5f;
            }
            t -= 2.0f;
            s *= 1.525f;
            return (((t * t) * (((1.0f + s) * t) + s)) + 2.0f) * 0.5f;
        }

        public String toString() {
            return "Back.INOUT";
        }
    }

    public Back m210s(float s) {
        this.param_s = s;
        return this;
    }
}
