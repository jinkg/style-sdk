package com.yalin.wallpaper.boids;

public class Vector3 {
    public float x,y,z;

    public Vector3() {}

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    Vector3(Vector3 v) {
        copy(v);
    }

    final public void copy(Vector3 v) {
        x = v.x;
        y = v.y; 
        z = v.z;
    }

    final public void zero(){
        x = y = z = 0;
    }
       
    final public float magnitude() {
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    final public void normalize() { 
        float mag = magnitude();
        x /= mag;
        y /= mag;
        z /= mag;
    }

    final public void scale(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    final public void scale(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }
   
    public String toString() {
        return "Vector3(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    final public float dot(Vector3 v) {
        return x*v.x + y*v.y + z*v.z;
    }

    final void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    final public void add(Vector3 o) {
        add(o.x, o.y, o.z);
    }

    final void subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    final public void subtract(Vector3 o) {
        subtract(o.x, o.y, o.z);
    }

    final public float component(int c) {
        if(c == 0) return x;
        else if(c == 1) return y;
        else if(c == 2) return z;
        else return 0;       
    }

    final public float distance(Vector3 o) {
        float sum_of_squares = (float)(Math.pow(x-o.x, 2)+Math.pow(y-o.y, 2)+Math.pow(z-o.z, 2));
        return (float)Math.sqrt(sum_of_squares);
    }
}
