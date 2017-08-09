package com.yalin.wallpaper.boids;

public class Vector2 extends Vector3 {
    public Vector2() {}
    
    public Vector2(float x, float y) {
        super(x, y, 0);
    }

    public Vector2(Vector2 v) {
        super(v);
        z = 0;
    }

    public String toString() {
        return "Vector2(" + this.x + ", " + this.y + ")";
    }

    final void add(float x, float y) {
        add(x, y, 0);
    }

    final public void add(Vector2 o) {
        add(o.x, o.y);
    }

    final public void subtract(float x, float y) {
        subtract(x, y, 0);
    }

    final public void subtract(Vector2 o) {
        subtract(o.x, o.y, 0);
    }
}
