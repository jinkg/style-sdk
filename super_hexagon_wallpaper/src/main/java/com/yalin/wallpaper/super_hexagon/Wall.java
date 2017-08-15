package com.yalin.wallpaper.super_hexagon;

public class Wall {
	public int lane;
	public float dist;
    public boolean dodged;

	public Wall(int l) {
		lane = l;
        dist = 1f;
        dodged = false;
	}
}
