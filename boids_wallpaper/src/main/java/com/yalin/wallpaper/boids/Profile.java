package com.yalin.wallpaper.boids;

import org.json.JSONException;

class Profile {
    public String name;

    public boolean RANDOMIZE_COLORS = false;

    public float MIN_SEED;
    public float MAX_SEED;

    public int FLOCK_SIZE;
    public float MAX_SIZE;
    public float MAX_VELOCITY;
    public float RANGE;
    public float AVOID_RADIUS;
    public float REBOUND_VELOCITY;
    public float MIN_SIZE;
    public float SIZE_SCALE;
    public long FLEE_TIME;

    public float SCALE_V1;
    public float SCALE_V2;
    public float SCALE_V3;
    public float SCALE_V4;
    public float SCALE_V5;
    public float SCALE_V6;

    public int BONUS_V1 = 100;
    public int BONUS_V2 = 100;
    public int BONUS_V3 = 100;
    public int BONUS_V4 = 100;
    public int BONUS_V5 = 100;
    public int BONUS_V6 = 100;
    public int BONUS_V7 = 100;
    public int BONUS_VELOCITY = 100;

    public float MIN_X;
    public float MAX_X;

    public float MIN_Y;
    public float MAX_Y;

    public float MIN_Z;
    public float MAX_Z;

    public float MIN_ALPHA;
    public int NEIGHBORS;

    public String toString() {
        return ProfileLoader.storeProfile(this).toString();
    }

    public String toString(int spaces) {
        try {
            return ProfileLoader.storeProfile(this).toString(spaces);   
        } catch(JSONException e) {
            return null;
        }
    }

}

