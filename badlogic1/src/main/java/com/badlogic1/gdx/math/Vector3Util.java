package com.badlogic1.gdx.math;

/**
 * @author jinyalin
 * @since 2017/8/25.
 */

public class Vector3Util {
    public static com.badlogic.gdx.math.Vector3 switchToBadlogic(Vector3 vector3) {
        return new com.badlogic.gdx.math.Vector3(vector3.x, vector3.y, vector3.z);
    }
}
