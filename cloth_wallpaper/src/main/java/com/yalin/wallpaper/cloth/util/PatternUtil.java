package com.yalin.wallpaper.cloth.util;

import com.yalin.wallpaper.cloth.R;

import java.util.Calendar;

/**
 * @author jinyalin
 * @since 2017/8/15.
 */

public class PatternUtil {
    private static int[] patterns = new int[]{
            R.raw.pattern1,
            R.raw.pattern2,
            R.raw.pattern3,
            R.raw.pattern4,
            R.raw.pattern5,
            R.raw.pattern6,
            R.raw.pattern7,
            R.raw.pattern8,
            R.raw.pattern9,
            R.raw.pattern10,
            R.raw.pattern11
    };

    public static int getPattern() {
        int days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int patternIndex = days % patterns.length;
        return patterns[patternIndex];
    }
}
