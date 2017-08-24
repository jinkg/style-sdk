package aurelienribon.tweenengine;

public interface TweenAccessor<T> {
    int getValues(T t, int i, float[] fArr);

    void setValues(T t, int i, float[] fArr);
}
