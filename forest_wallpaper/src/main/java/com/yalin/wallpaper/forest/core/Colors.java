package com.yalin.wallpaper.forest.core;

import android.graphics.Color;
import android.util.Pair;

import com.yalin.wallpaper.forest.R;
import com.yalin.wallpaper.forest.Settings;

public class Colors {
    private float[] _cloudsBottom;
    private float[] _cloudsTop;
    private int _cloudySkyBottom;
    private int _cloudySkyTop;
    private float[] _frontHill;
    private float[][] _frontHillTree;
    private int _mountain;
    private float[] _rain;
    private float[] _rearHill;
    private float[][] _rearHillTree;
    private int _skyBottom;
    private int _skyTop;
    private float[] _snow;
    private int _sunLightColored;
    private int _sunLightGrey;
    private SunData sundata;

    public int toInt(Color col) {
        return 0;
    }

    public float[] toHSV(int col) {
        float[] hsv = new float[3];
        Color.colorToHSV(col, hsv);
        return hsv;
    }

    public float[] toRGB(int col) {
        return new float[]{((float) Color.red(col)) / 255.0f, ((float) Color.green(col)) / 255.0f,
                ((float) Color.blue(col)) / 255.0f};
    }

    public int fromHSV(float[] hsv) {
        return Color.HSVToColor(hsv);
    }

    public int fromRGB(float[] rgb) {
        return Color.argb(255, (int) (rgb[0] * 255.0f), (int) (rgb[1] * 255.0f), (int) (rgb[2] * 255.0f));
    }

    public int mix(int col1, int col2, double m) {
        return Color.argb((int) ((((double) Color.alpha(col1)) * m) + (((double) Color.alpha(col2)) * (1.0d - m))), (int) ((((double) Color.red(col1)) * m) + (((double) Color.red(col2)) * (1.0d - m))), (int) ((((double) Color.green(col1)) * m) + (((double) Color.green(col2)) * (1.0d - m))), (int) ((((double) Color.blue(col1)) * m) + (((double) Color.blue(col2)) * (1.0d - m))));
    }

    public int add(int col1, int col2) {
        return Color.argb(Math.min(255, Math.max(0, Color.alpha(col1) + Color.alpha(col2))), Math.min(255, Math.max(0, Color.red(col1) + Color.red(col2))), Math.min(255, Math.max(0, Color.green(col1) + Color.green(col2))), Math.min(255, Math.max(0, Color.blue(col1) + Color.blue(col2))));
    }

    public int multiply(int col, double m) {
        return Color.argb(Color.alpha(col), (int) (((double) Color.red(col)) * m), (int) (((double) Color.green(col)) * m), (int) (((double) Color.blue(col)) * m));
    }

    public int multiply(int col1, int col2) {
        return Color.argb((Color.alpha(col1) * Color.alpha(col2)) / 255, (Color.red(col1) * Color.red(col2)) / 255, (Color.green(col1) * Color.green(col2)) / 255, (Color.blue(col1) * Color.blue(col2)) / 255);
    }

    private Pair<Integer, Double> calcValueInStops(double val, int[] stops) {
        int index = 0;
        double mixval = -1.0d;
        if (val > ((double) stops[0])) {
            index = 0;
        } else if (val <= ((double) stops[stops.length - 1])) {
            index = stops.length - 1;
        } else {
            for (int i = 1; i < stops.length; i++) {
                if (val > ((double) stops[i])) {
                    mixval = (val - ((double) stops[i])) / ((double) (stops[i - 1] - stops[i]));
                    index = i;
                    break;
                }
            }
        }
        return new Pair<>(index, mixval);
    }

    private int colorFromStops(Pair<Integer, Double> p, int... colors) {
        int index = p.first;
        double mixval = p.second;
        if (mixval < 0.0d) {
            return colors[index];
        }
        return mix(colors[index - 1], colors[index], mixval);
    }

    public Colors() {
        rebuild();
    }

    public void rebuild() {
        rebuild(Utils.getSunData());
    }

    public void rebuild(SunData data) {
        int i;
        this.sundata = data;
        if (this._frontHillTree == null) {
            this._frontHillTree = new float[100][];
        }
        if (this._rearHillTree == null) {
            this._rearHillTree = new float[100][];
        }
        double zenith = data.getZenithAngle();
        boolean dayNightCycle = Settings.getBoolean(R.string.pk_day_night_cycle, R.bool.default_day_night_cycle);
//        dayNightCycle = false;
        this._skyTop = Settings.getInt(R.string.pk_sky_top_color, R.color.default_sky_top_color);
        this._skyBottom = Settings.getInt(R.string.pk_sky_bottom_color, R.color.default_sky_bottom_color);
        this._cloudySkyTop = Color.argb(255, 180, 200, 210);
        this._cloudySkyBottom = Color.argb(255, 40, 45, 50);
        this._sunLightColored = colorFromStops(calcValueInStops(zenith,
                new int[]{102, 94, 89, 84}), -16777216, -24416, -96, -1);
        this._sunLightGrey = colorFromStops(calcValueInStops(zenith,
                new int[]{102, 84}), -16777216, -1);
        this._cloudsTop = toRGB(colorFromStops(calcValueInStops(zenith,
                new int[]{105, 100, 95, 90}), -8355680, -3108704, -8032, -1));
        this._cloudsBottom = toRGB(colorFromStops(calcValueInStops(zenith,
                new int[]{102, 96, 90, 84}), -12566400, -3108704, -8032, -1));
        if (dayNightCycle) {
            Pair<Integer, Double> p = calcValueInStops(zenith, new int[]{102, 96, 90, 84});
            this._skyTop = colorFromStops(p, -16777216, -11375363, -5123843, -8331009);
            this._skyBottom = colorFromStops(p, -14671744, -111723, -13955, -1705218);
            p = calcValueInStops(zenith, new int[]{102, 84});
            this._cloudySkyTop = colorFromStops(p, -14144206, -4929326);
            this._cloudySkyBottom = colorFromStops(p, multiply(-14144206, 0.5d), -14144206);
        }
        double cloudiness = Weather.INSTANCE.getCloudiness();
        int skyTop = mix(this._cloudySkyTop, this._skyTop, cloudiness);
        int skyBottom = mix(this._cloudySkyBottom, this._skyBottom, cloudiness);
        int daytimeColor = -1;
        if (dayNightCycle) {
            Pair<Integer, Double> p = calcValueInStops(zenith, new int[]{102, 84});
            int nightColor = mix(-16777216, Settings.getInt(R.string.pk_night_color, R.color.default_night_color), 0.4d * cloudiness);
            daytimeColor = colorFromStops(p, nightColor, -1);
        }
        if (dayNightCycle) {
            this._rain = toRGB(colorFromStops(calcValueInStops(zenith, new int[]{102, 84}), -8355680, -1));
            this._rain = toRGB(mix(this._cloudySkyTop, fromRGB(this._rain), 0.5d * cloudiness));
        } else {
            this._rain = toRGB(skyTop);
        }
        if (dayNightCycle) {
            this._snow = toRGB(multiply(-1, daytimeColor));
        } else {
            this._snow = toRGB(-1);
        }
        int sky = skyBottom;
        float[] hsv = toHSV(Settings.getInt(R.string.pk_tree_color, R.color.default_tree_color));
        float v = hsv[2];
        for (i = 0; i < 100; i++) {
            hsv[2] = (float) (((double) Math.min(1.0f - 0.15f, Math.max(0.15f, v))) + (((((double) (((float) i) / 100.0f)) - 0.5d) * ((double) 0.15f)) * 2.0d));
            this._frontHillTree[i] = toRGB(multiply(mix(fromHSV(hsv), sky, 0.95d), daytimeColor));
        }
        sky = skyBottom;
        for (i = 0; i < 100; i++) {
            float[] rgb = frontHillTree(((float) i) / 100.0f);
            this._rearHillTree[i] = toRGB(mix(Color.rgb((int) (rgb[0] * 255.0f), (int) (rgb[1] * 255.0f), (int) (rgb[2] * 255.0f)), sky, 0.75d));
        }
        this._frontHill = toHSV(mix(multiply(Settings.getInt(R.string.pk_hill_color, R.color.default_hill_color), daytimeColor), skyBottom, 0.95d));
        this._rearHill = toHSV(mix(multiply(Settings.getInt(R.string.pk_hill_color, R.color.default_hill_color), daytimeColor), skyBottom, 0.75d));
        int color = Settings.getInt(R.string.pk_mountain_color, R.color.default_mountain_color);
        if (dayNightCycle) {
            int clear = multiply(color, this._sunLightColored);
            this._mountain = mix(multiply(color, this._sunLightGrey), clear, cloudiness);
            return;
        }
        this._mountain = color;
    }


    public SunData getSunData() {
        return this.sundata;
    }

    public int sunLightColored() {
        return this._sunLightColored;
    }

    public int sunLightGrey() {
        return this._sunLightGrey;
    }

    public int skyTop() {
        return this._skyTop;
    }

    public int skyBottom() {
        return this._skyBottom;
    }

    public int cloudySkyTop() {
        return this._cloudySkyTop;
    }

    public int cloudySkyBottom() {
        return this._cloudySkyBottom;
    }

    public float[] cloudsTop() {
        return this._cloudsTop;
    }

    public float[] cloudsBottom() {
        return this._cloudsBottom;
    }

    public float[] rain() {
        return this._rain;
    }

    public float[] snow() {
        return this._snow;
    }

    public float[] frontHillTree(float value) {
        return this._frontHillTree[(int) (100.0f * value)];
    }

    public float[] rearHillTree(float value) {
        return this._rearHillTree[(int) (100.0f * value)];
    }

    public float[] frontHill() {
        return this._frontHill;
    }

    public float[] rearHill() {
        return this._rearHill;
    }

    public int mountain() {
        return this._mountain;
    }
}
