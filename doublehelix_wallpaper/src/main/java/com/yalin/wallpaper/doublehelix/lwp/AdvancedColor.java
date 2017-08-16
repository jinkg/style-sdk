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

import android.graphics.Color;

import java.util.Map;

public class AdvancedColor {

	public static class ColorType {
		public static final int NOT_A_COLOR = -1;
		public static final int CONSTANT = 0;
		public static final int BATTERY_LEVEL_BASED = 1;
		public static final int PLUGGED_STATE_BASED = 2;
	}

	private int mColorType;
	private int[] mColors;
	private String mNonColorMode="";
	private boolean threeColorBatteryLevel = false;

    public AdvancedColor (int constantColor){
        this(ColorType.CONSTANT, constantColor);
    }

	public AdvancedColor(int colorType, int... colors){
		mColorType = colorType;

		switch (colorType){
		case ColorType.CONSTANT:
			//must have length of 1
			if (colors.length<1){
				mColors = new int[1];
				mColors[0] = Color.WHITE;
			} else if (colors.length>1){
				mColors = new int[1];
				mColors[0] = colors[0];
			} else { //colors.length==1
				mColors = colors;
			}
			break;
		case ColorType.BATTERY_LEVEL_BASED:
			//must have length of 2 or 3
			if (colors.length<1){
				mColors = new int[2];
				mColors[0] = mColors[1] = Color.WHITE;
			} else if (colors.length==1){
				mColors = new int[2];
				mColors[0] = mColors[1] = colors[0];
			} else if (colors.length>3){ //too many colors. reduce to 3.
				mColors = new int[3];
				mColors[0] = colors[0];
				mColors[1] = colors[1];
				mColors[2] = colors[2];
			} else { //2 or 3 colors provided
				mColors = colors;
			}
			threeColorBatteryLevel = (mColors.length==3);
			break;
		case ColorType.PLUGGED_STATE_BASED:
			//must have length of 2
			if (colors.length<1){
				mColors = new int[2];
				mColors[0] = mColors[1] = Color.WHITE;
			} else if (colors.length==1){
				mColors = new int[2];
				mColors[0] = mColors[1] = colors[0];
			} else if (colors.length>2){
				mColors = new int[2];
				mColors[0] = colors[0];
				mColors[1] = colors[1];
			} else { // exactly 2 colors provided
				mColors = colors;
			}
		}
	}
	
	public void setThreeColorBatteryLevelMode(boolean threeColorBatteryLevel){
		this.threeColorBatteryLevel = threeColorBatteryLevel;
		if (mColorType== ColorType.BATTERY_LEVEL_BASED ){
			if (threeColorBatteryLevel && mColors.length<3){
				//expand to three
				int color0 = mColors[0];
				int color1 = mColors[1];
				mColors = new int[3];
				mColors[0] = color0;
				mColors[1] = color1;
				mColors[2] = color1;
			} else if (!threeColorBatteryLevel && mColors.length!=2){
				//reduce to 2
				int color0 = mColors[0];
				int color1 = mColors[1];
				mColors = new int[2];
				mColors[0] = color0;
				mColors[1] = color1;
			}
		}
	}

	/**
	 * Constructor for a color that is in a non-color mode. Defaults array to constant black.
	 * @param nonColorMode
	 */
	public AdvancedColor(String nonColorMode){
		setNonColor(nonColorMode);
	}

	public int getColorType(){
		return mColorType;
	}

    public boolean isAColor() {
        return mColorType != ColorType.NOT_A_COLOR;
    }

	public void setColorType(int colorType){
		if (mColorType==colorType)
			return;

		mColorType = colorType;

		int color0, color1, color2;

		switch (colorType){
		case ColorType.CONSTANT:
			color0 = mColors[0];
			mColors = new int[1];
			mColors[0] = color0;
			break;
		case ColorType.BATTERY_LEVEL_BASED:
		case ColorType.PLUGGED_STATE_BASED:
			color0 = mColors[0];
			color1 = (mColors.length>1) ? mColors[1] : color0;
			color2 = (mColors.length>2) ? mColors[2] : color1;
			mColors = new int[(colorType== ColorType.PLUGGED_STATE_BASED || !threeColorBatteryLevel)?2:3];
			mColors[0] = color0;
			mColors[1] = color1;
			if (mColors.length==3){
				mColors[2] = color2;
			}
			break;
		}
	}

	public void setNonColor(String nonColorMode){
		mColorType = ColorType.NOT_A_COLOR;
		mColors = new int[1];
		mColors[0] = Color.BLACK;
		mNonColorMode = nonColorMode;
	}

	/**Returned colors can be modified to change the AdvancedColor*/
	public int[] getColors(){
		return mColors;
	}

	private static final String TYPE_TO_COLORS_SEPARATOR = "A";
	private static final String COLORS_SEPARATOR = "B";

	@Override 
	public String toString(){
		if (mColorType== ColorType.NOT_A_COLOR)
			return mNonColorMode;

		StringBuilder sb = new StringBuilder();
		sb.append(mColorType);
		sb.append(TYPE_TO_COLORS_SEPARATOR);
		for (int i=0; i<mColors.length; i++){
			sb.append(mColors[i]);
			if (i!=mColors.length-1){ //separator after all but last
				sb.append(COLORS_SEPARATOR);
			}
		}
		return sb.toString();
	}

	public static AdvancedColor fromString(String string){
		String[] typeAndColors = string.split(TYPE_TO_COLORS_SEPARATOR);

		if (typeAndColors.length != 2)
			return new AdvancedColor(string); //not a color
		
		int colorType;
		
		try{
			colorType = Integer.parseInt(typeAndColors[0]);
		} catch (NumberFormatException e){
			return new AdvancedColor(string); //not a color
		}
		
		String[] stringColors = typeAndColors[1].split(COLORS_SEPARATOR);
		
		if (stringColors.length < 1)
			return new AdvancedColor(string); //not a color
		
		int colors[] = new int[stringColors.length];
		try {
			for (int i=0; i< colors.length; i++){
				colors[i] = Integer.parseInt(stringColors[i]);
			}
		} catch (NumberFormatException e){
			return new AdvancedColor(string); //not a color
		}
		
		return new AdvancedColor(colorType, colors);
	}

    public String toDebugString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Color type: ").append(mColorType);
        if (mColorType == ColorType.NOT_A_COLOR){
            sb.append("Not a color. Mode: ").append(mNonColorMode);
        } else {
            sb.append("\nColors array length: ").append(mColors.length)
                    .append("\nColors: ").append(mColors.toString());
        }
        sb.append("\nThree color battery level?: ").append(threeColorBatteryLevel);

        return sb.toString();
    }

	private static AdvancedColor sStringConstructor = new AdvancedColor(ColorType.CONSTANT,Color.BLACK);
	public static String constantColorToAdvancedColorString(int color){
		sStringConstructor.mColors[0] = color;
		return sStringConstructor.toString();
	}

	/**
	 * Directly update a map of libgdx colors from AdvancedColors. AdvancedColors that are NOT_A_COLOR are ignored.
	 * @param map
	 * @param batteryLevel
	 * @param isCharging
	 * @param powerBasedColorsOnly Whether to ignore CONSTANT AdvancedColors.
	 */
	public static void updateLibgdxColorsFromMap(Map<AdvancedColor, com.badlogic.gdx.graphics.Color> map,
			float batteryLevel, boolean isCharging, boolean powerBasedColorsOnly){
		for (Map.Entry<AdvancedColor, com.badlogic.gdx.graphics.Color> entry : map.entrySet()){
			AdvancedColor advancedColor = entry.getKey();
			int[] argbColors = advancedColor.getColors();
			switch (advancedColor.getColorType()){
			case ColorType.NOT_A_COLOR:
				break;
			case ColorType.CONSTANT:
				if (!powerBasedColorsOnly){
					entry.getValue().set(IntColor.rgba(argbColors[0]));
				}
				break;
			case ColorType.BATTERY_LEVEL_BASED:
                int colorOne = argbColors[0];
                int colorTwo = argbColors[1];
                if (advancedColor.threeColorBatteryLevel && argbColors.length>2){
                    if (batteryLevel >0.5f){
                        batteryLevel = (batteryLevel-0.5f)*2;
                        colorOne = argbColors[1];
                        colorTwo = argbColors[2];
                    } else {
                        batteryLevel *= 2;
                    }
                }
                entry.getValue().set(
                        IntColor.rgba(
                                ColorUtil.blendAndroidIntsPreservingLerpedSaturation(
										colorOne, colorTwo, batteryLevel)));
				break;
			case ColorType.PLUGGED_STATE_BASED:
				entry.getValue().set(IntColor.rgba(isCharging ? argbColors[1] : argbColors[0]));
				break;
			}
		}
	}

    public static String produceString(int constantColor){
        return produceString(ColorType.CONSTANT, constantColor);
    }

    public static String produceString(int colorType, int... colors){
        return (new AdvancedColor(colorType, colors)).toString();
    }
}
