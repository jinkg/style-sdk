/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.yalin.wallpaper.flower;

/**
 * Wallpaper constant values.
 */
public final class FlowerConstants {

	// ElementFlower related constants.
	public static final float FLOWER_BRANCH_WIDTH_MAX = 0.1f;
	public static final float FLOWER_BRANCH_WIDTH_MIN = 0.05f;
	public static final float FLOWER_POINT_SCALE_MAX = .24f;
	public static final float FLOWER_POINT_SCALE_MIN = .12f;
	public static final int FLOWER_ROOT_ELEMENT_COUNT = 6;
	public static final float FLOWER_ROOT_WIDTH_MAX = 0.12f;
	public static final float FLOWER_ROOT_WIDTH_MIN = 0.06f;

	// Autumn color scheme.
	public static final float[] SCHEME_AUTUMN_BG_BOTTOM = { 1f, .8f, .5f, 1f };
	public static final float[] SCHEME_AUTUMN_BG_TOP = { .7f, .4f, .2f, 1f };
	public static final float[] SCHEME_AUTUMN_PLANT_1 = { .8f, .6f, .4f, .3f };
	public static final float[] SCHEME_AUTUMN_PLANT_2 = { .7f, .5f, .3f, .3f };
	// Spring color scheme.
	public static final float[] SCHEME_SPRING_BG_BOTTOM = { .9f, .9f, .5f, 1f };
	public static final float[] SCHEME_SPRING_BG_TOP = { .7f, .7f, .4f, 1f };
	public static final float[] SCHEME_SPRING_PLANT_1 = { .8f, .4f, 1f, .8f };
	public static final float[] SCHEME_SPRING_PLANT_2 = { 1f, .4f, .8f, .8f };
	// Summer color scheme.
	public static final float[] SCHEME_SUMMER_BG_BOTTOM = { .9f, 1f, .9f, 1f };
	public static final float[] SCHEME_SUMMER_BG_TOP = { .9f, .9f, .9f, 1f };
	public static final float[] SCHEME_SUMMER_PLANT_1 = { .9f, .7f, .7f, .8f };
	public static final float[] SCHEME_SUMMER_PLANT_2 = { .6f, .8f, .6f, .8f };
	// Winter color scheme.
	public static final float[] SCHEME_WINTER_BG_BOTTOM = { .9f, .9f, .95f, 1f };
	public static final float[] SCHEME_WINTER_BG_TOP = { .8f, .8f, .85f, 1f };
	public static final float[] SCHEME_WINTER_PLANT_1 = { .4f, .6f, .8f, .8f };
	public static final float[] SCHEME_WINTER_PLANT_2 = { .5f, .7f, .9f, .8f };

	// Spline width extremes.
	public static final float SPLINE_WIDTH_MAX = FLOWER_ROOT_WIDTH_MAX;
	public static final float SPLINE_WIDTH_MIN = FLOWER_ROOT_WIDTH_MIN;
}
