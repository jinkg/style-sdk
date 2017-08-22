package aurelienribon.tweenengine;

public abstract class TweenEquation {
    public abstract float compute(float f);

    public boolean isValueOf(String str) {
        return str.equals(toString());
    }
}
