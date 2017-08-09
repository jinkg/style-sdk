package com.yalin.wallpaper.boids;

public class Boid {
    public final Vector3 position = new Vector3();
    public final Vector3 velocity = new Vector3();
    public float seed;
    public float size;
    public float opacity;
    public int age;
    public boolean alive;
    public final float[] color = new float[4];

    public Boid(float x, float y, float z) {
        alive = true;
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Boid(float x, float y, float z, float dx, float dy, float dz) {
        this(x,y,z);
        velocity.x = dx;
        velocity.y = dy;
        velocity.z = dz;
    }

    public String toString() {
        return "Boid(position=" + position + ", velocity=" + velocity + ")";
    }
}
