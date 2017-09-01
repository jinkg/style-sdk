package com.yalin.wallpaper.electric_plasma;

public class PreferenceModernizer {
    public static final String batterycolors = "plasma_battery_color_list";
    public static final String colorpref = "plasma_color_rgb";
    public static final String colortype = "plasma_color_type";
    public static final String colortypeAccel = "a";
    public static final String colortypeBattery = "b";
    public static final String colortypeCompass = "m";
    public static final String colortypeCycle = "c";
    public static final String colortypeRandom = "r";
    public static final String colortypeStatic = "s";
    public static final String compasscolors = "plasma_compass_color_list";
    public static final String complexitypref = "plasma_complexity";
    public static final String complexpref = "plasma_complex_movement";
    public static final String cyclecolors = "plasma_cycle_color_list";
    public static final String defaultBattery = "8395033;8421401;1671193";
    public static final String defaultCycle = "8395033;8421401;1671193;1671296;1644928;8395136";
    public static final String defaultStatic = "1321088";
    public static final String smoothnesspref = "plasma_smoothness_string";
    public static final String speedpref = "plasma_speed_slider";
    public static final String symmetrypref = "plasma_symmetry_int";
    public static final String touchpref = "plasma_touch";

    public static int[] parseMultipleColors(String multiples) {
        String[] parsed = multiples.split(";");
        int[] results = new int[parsed.length];
        for (int i = 0; i < parsed.length; i++) {
            results[i] = Integer.parseInt(parsed[i]);
        }
        return results;
    }

    public static String colorListToString(int... colors) {
        String acc = "";
        for (int i = 0; i < colors.length - 1; i++) {
            acc = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(acc)).append(String.valueOf(colors[i])).toString())).append(";").toString();
        }
        return new StringBuilder(String.valueOf(acc)).append(String.valueOf(colors[colors.length - 1])).toString();
    }
}
