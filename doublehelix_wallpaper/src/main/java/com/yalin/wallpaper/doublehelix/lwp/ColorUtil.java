/*******************************************************************************
 * Copyright 2015 Cypher Cove, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.yalin.wallpaper.doublehelix.lwp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public final class ColorUtil {
	private static final float inv255=1/255f;
	public static Color temp = new Color();
	
	public static Color invert(Color color){
		color.set(1-color.r,1-color.g,1-color.b,1-color.a);
		return color;
	}
	
	public static Color mulRgbOnly(Color color, float scale){
		color.r *= scale;
		color.g *= scale;
		color.b *= scale;
		color.clamp();
		return color;
	}

	/** Amount is how far to shift hue (normalized across colors from 0 to 1). Returns the modified input color.*/
	public static Color shiftHue(Color color,float amount){
		float[] hsv = new float[3];
		androidIntToHSV(toAndroidInt(color),hsv);
		hsv[0] += amount*360;
		while (hsv[0]<0)
			hsv[0] += 360;
		while (hsv[0]>=360)
			hsv[0] -= 360;
		return color.set(fromAndroidInt(hsvToAndroidInt(hsv)));
	}
	
	/** Returns the modified input color.*/
	public static Color scaleSaturation(Color color,float scale){
		float[] hsv = new float[3];
		androidIntToHSV(toAndroidInt(color),hsv);
		hsv[1] *= scale;
		hsv[1] = Math.min(hsv[1], 1);
		//Unnecessary. hsv[1] = MathUtils.clamp(hsv[1], 0, 1f);
		return color.set(fromAndroidInt(hsvToAndroidInt(hsv)));
	}
	
	/** Returns the modified input color.*/
	public static Color maximizeSaturation(Color color){
		float[] hsv = new float[3];
		androidIntToHSV(toAndroidInt(color),hsv);
		hsv[1] = 1f;
		return color.set(fromAndroidInt(hsvToAndroidInt(hsv)));
	}
	
	/** Returns the modified input color.*/
	public static Color maximizeBrightness(Color color){
		float[] hsv = new float[3];
		androidIntToHSV(toAndroidInt(color),hsv);
		hsv[2] = 1f;
		return color.set(fromAndroidInt(hsvToAndroidInt(hsv)));
	}
	
	/** Returns the modified input color.*/
	public static Color scaleValue(Color color,float scale){
		float[] hsv = new float[3];
		androidIntToHSV(toAndroidInt(color),hsv);
		hsv[2] *= scale;
		hsv[2] = MathUtils.clamp(hsv[2], 0, 1f); //necessary to prevent hue shifts
		return color.set(fromAndroidInt(hsvToAndroidInt(hsv)));
	}
	
	/** Returns a temporary GDX color from given Android Color int.*/
	public static Color fromAndroidInt(int color){
		temp.set(red(color)*inv255,green(color)*inv255,blue(color)*inv255,alpha(color)*inv255);
		return temp;
	}
	
	public static int toAndroidInt(Color color){
		return IntColor.argb((int)(color.a*255), (int)(color.r*255), (int)(color.g*255), (int)(color.b*255));
	}

    public static int argbToRgba(int argb){
        return (argb << 8) | (argb >>> 24);
    }

	/** Return a temp Color defined by the given hex value.
	 * @param hex Must be of the form 0xAARRGGBB.*/
	public static Color fromHex(long hex)
	{
		float a = (hex & 0xFF000000L) >> 24;
		float r = (hex & 0xFF0000L) >> 16;
		float g = (hex & 0xFF00L) >> 8;
		float b = (hex & 0xFFL);
		temp.set(r*inv255, g*inv255, b*inv255, a*inv255);
		return temp;
	}

	/** Return a temp Color defined by the given hex value.
	 * @param s Must be of the form AARRGGBB or RRGGBB.*/
	public static Color fromHexString(String s)
	{               
		if(s.startsWith("0x"))
			s = s.substring(2);
		if (s.length()==6)
			s="FF"+s;
		if(s.length() != 8) // AARRGGBB
			throw new IllegalArgumentException("String must have the form AARRGGBB or RRGGBB.");
		return fromHex(Long.parseLong(s, 16));
	}

	/**Returns a fully saturated version of the Android Color int as a temp Color.*/
	public static Color fromAndroidIntToSaturated(int color){
		float[] hsv = {0f,0f,0f};
		IntColor.colorToHSV(color, hsv);
		hsv[2]=1f;//as bright as possible, same hue
		int i = IntColor.HSVToColor(hsv);
		temp.set(red(i)*inv255,green(i)*inv255,blue(i)*inv255,alpha(i)*inv255);
		return temp;
	}

	static float invInterp;
	/**Sets the destination Color to an interpolation between the two source Colors.
	 * @param interp Value between 0 and 1 corresponding to distance between src1 and src2.*/
	public static Color setInterpolatedColor(Color dst, Color src1, Color src2, float interp){
		invInterp=1-interp;
		dst.set(src1.r*interp+src2.r*invInterp,
				src1.g*interp+src2.g*invInterp,
				src1.b*interp+src2.b*invInterp,
				src1.a*interp+src2.a*invInterp);
		return dst;
	}

	/**Create an Android Color int from 255 ints.*/
	public static int argb(int a, int r, int g, int b){
		return IntColor.argb(a,r,g,b);
	}

	/**
	 * Return the alpha component of an Android Color int. This is the same as saying
	 * color >>> 24
	 */
	public static int alpha(int color) {
		return color >>> 24;
	}

	/**
	 * Return the red component of an Android Color int. This is the same as saying
	 * (color >> 16) & 0xFF
	 */
	public static int red(int color) {
		return (color >> 16) & 0xFF;
	}

	/**
	 * Return the green component of an Android Color int. This is the same as saying
	 * (color >> 8) & 0xFF
	 */
	public static int green(int color) {
		return (color >> 8) & 0xFF;
	}

	/**
	 * Return the blue component of an Android Color int. This is the same as saying
	 * color & 0xFF
	 */
	public static int blue(int color) {
		return color & 0xFF;
	}

	public static int hsvToAndroidInt(float[] hsv) {
		return IntColor.HSVToColor(hsv);
	}
	
	public static Color fromHSV(float[] hsv){
		return fromAndroidInt(hsvToAndroidInt(hsv));
	}

	public static void androidIntToHSV(int color, float[] hsv) {
		IntColor.colorToHSV(color, hsv);
	}
	
	public static void toHSV(Color color, float[] hsv) {
		IntColor.colorToHSV(toAndroidInt(color), hsv);
	}
	
	public static int blendAndroidInts(int one, int two, float blend){
		int a = (int)(alpha(one)*(1-blend) + alpha(two)*blend);
		int r = (int)(red(one)*(1-blend) + red(two)*blend);
		int g = (int)(green(one)*(1-blend) + green(two)*blend);
		int b = (int)(blue(one)*(1-blend) + blue(two)*blend);
		return argb(a,r,g,b);
	}
	
	static float[] hsvTemp = new float[3];
	public static int blendAndroidIntsPreservingLerpedSaturation(int one, int two, float blend){
		int a = (int)(alpha(one)*(1-blend) + alpha(two)*blend);
		int r = (int)(red(one)*(1-blend) + red(two)*blend);
		int g = (int)(green(one)*(1-blend) + green(two)*blend);
		int b = (int)(blue(one)*(1-blend) + blue(two)*blend);
		
		androidIntToHSV(one, hsvTemp);
		float saturationOne = hsvTemp[1];
		androidIntToHSV(two, hsvTemp);
		float saturationTwo = hsvTemp[1];
		androidIntToHSV(argb(a,r,g,b), hsvTemp);
		hsvTemp[1]= (1-blend)*saturationOne + blend*saturationTwo;
		
		return hsvToAndroidInt(hsvTemp);
	}
	
	public static Color blend(Color one, Color two, float blend){
		float inv = 1-blend;
		temp.set(
				inv*one.r + blend*two.r,
				inv*one.g + blend*two.g,
				inv*one.b + blend*two.b,
				inv*one.a + blend*two.a
		); 
		
		return temp;
	}
	
	public static Color blendIntoFirst(Color one, Color two, float blend){
		float inv = 1-blend;
		one.set(
				inv*one.r + blend*two.r,
				inv*one.g + blend*two.g,
				inv*one.b + blend*two.b,
				inv*one.a + blend*two.a
		); 
		
		return one;
	}
	
	public static Color blendPreservingAveragedSaturation(Color one, Color two, float blend){
		temp.set(
				(1-blend)*one.r + blend*two.r,
				(1-blend)*one.g + blend*two.g,
				(1-blend)*one.b + blend*two.b,
				(1-blend)*one.a + blend*two.a
		); 
		
		//preserve saturation in blend
		toHSV(one, hsvTemp);
		float saturationOne = hsvTemp[1];
		
		 //If one color has zero saturation, then user probably doesn't want to preserve the average (and it would skew red if the blend is fully biased to the neutral color
		if (saturationOne < 0.1)
			return temp;
		
		toHSV(two, hsvTemp);
		float saturationTwo = hsvTemp[1];
		
		if (saturationTwo < 0.1)
			return temp;
		
		toHSV(temp, hsvTemp);
		hsvTemp[1]= (hsvTemp[1] + blend*saturationOne+(1-blend)*saturationTwo) *0.5f ;
		return fromHSV(hsvTemp);
	}
}
