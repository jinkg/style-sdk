package com.yalin.wallpaper.sneakycat;

import com.badlogic.gdx.math.Vector2;

public class SpawnPoint {
    public Vector2 endPosition;
    public Vector2 endPositionLandscape;
    public float rotation;
    public Vector2 startPosition;
    public Vector2 startPositionLandscape;

    public Vector2 getStartPosition(boolean isLandscape) {
        if (isLandscape) {
            return this.startPositionLandscape;
        }
        return this.startPosition;
    }

    public Vector2 getEndPosition(boolean isLandscape) {
        if (isLandscape) {
            return this.endPositionLandscape;
        }
        return this.endPosition;
    }
}
