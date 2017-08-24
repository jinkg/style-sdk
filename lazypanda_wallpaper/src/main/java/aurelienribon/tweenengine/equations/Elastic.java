package aurelienribon.tweenengine.equations;

import com.badlogic.gdx.math.MathUtils;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Elastic extends TweenEquation {
    public static final Elastic IN = new C05521();
    public static final Elastic INOUT = new C05543();
    public static final Elastic OUT = new C05532();
    private static final float PI = 3.1415927f;
    protected float param_a;
    protected float param_p;
    protected boolean setA = false;
    protected boolean setP = false;

    static class C05521 extends Elastic {
        C05521() {
        }

        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0f) {
                return 0.0f;
            }
            if (t == 1.0f) {
                return 1.0f;
            }
            float s;
            if (!this.setP) {
                p = 0.3f;
            }
            if (!this.setA || a < 1.0f) {
                a = 1.0f;
                s = p / 4.0f;
            } else {
                s = (p / MathUtils.PI2) * ((float) Math.asin((double) (1.0f / a)));
            }
            t -= 1.0f;
            return -((((float) Math.pow(2.0d, (double) (10.0f * t))) * a) * ((float) Math.sin((double) (((t - s) * MathUtils.PI2) / p))));
        }

        public String toString() {
            return "Elastic.IN";
        }
    }

    static class C05532 extends Elastic {
        C05532() {
        }

        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0f) {
                return 0.0f;
            }
            if (t == 1.0f) {
                return 1.0f;
            }
            float s;
            if (!this.setP) {
                p = 0.3f;
            }
            if (!this.setA || a < 1.0f) {
                a = 1.0f;
                s = p / 4.0f;
            } else {
                s = (p / MathUtils.PI2) * ((float) Math.asin((double) (1.0f / a)));
            }
            return ((((float) Math.pow(2.0d, (double) (-10.0f * t))) * a) * ((float) Math.sin((double) (((t - s) * MathUtils.PI2) / p)))) + 1.0f;
        }

        public String toString() {
            return "Elastic.OUT";
        }
    }

    static class C05543 extends Elastic {
        C05543() {
        }

        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0f) {
                return 0.0f;
            }
            t *= 2.0f;
            if (t == 2.0f) {
                return 1.0f;
            }
            float s;
            if (!this.setP) {
                p = 0.45000002f;
            }
            if (!this.setA || a < 1.0f) {
                a = 1.0f;
                s = p / 4.0f;
            } else {
                s = (p / MathUtils.PI2) * ((float) Math.asin((double) (1.0f / a)));
            }
            if (t < 1.0f) {
                t -= 1.0f;
                return -0.5f * ((((float) Math.pow(2.0d, (double) (10.0f * t))) * a) * ((float) Math.sin((double) (((t - s) * MathUtils.PI2) / p))));
            }
            t -= 1.0f;
            return (((((float) Math.pow(2.0d, (double) (-10.0f * t))) * a) * ((float) Math.sin((double) (((t - s) * MathUtils.PI2) / p)))) * 0.5f) + 1.0f;
        }

        public String toString() {
            return "Elastic.INOUT";
        }
    }

    public Elastic m211a(float a) {
        this.param_a = a;
        this.setA = true;
        return this;
    }

    public Elastic m212p(float p) {
        this.param_p = p;
        this.setP = true;
        return this;
    }
}
