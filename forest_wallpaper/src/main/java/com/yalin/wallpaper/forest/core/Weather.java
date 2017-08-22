package com.yalin.wallpaper.forest.core;


import java.util.Random;

public class Weather {
    public static final Weather INSTANCE = new Weather();
    private static final double[] WEATHER_CLOUD = new double[]{0.23 * 30.0d, 0, 0, 0.0};
    private static final double[] WEATHER_RAIN = new double[]{0.4 * 30.0d, 0.2, 0, 0.8};
    private static final double[] WEATHER_BIG_RAIN = new double[]{0.9 * 30.0d, 0.9, 0, 0.9};
    private static final double[] WEATHER_SNOW = new double[]{0.3 * 30.0d, 0.0, 0.4, 0.2};
    private static final double[] WEATHER_BIG_SNOW = new double[]{0.8 * 30.0d, 0.0, 0.9, 0.6};
    private double cloudiness;
    private long lastUpdate;
    private double rain;
    private double snowfall;
    private double windSpeed;

    private Weather() {
    }

    public boolean update() {
        return updateWeather();
    }

    private boolean updateWeather() {
        if (timeToUpdate()) {
            double[] weather = getWeatherByTime();
            windSpeed = weather[0];
            rain = weather[1];
            snowfall = weather[2];
            cloudiness = weather[3];

            lastUpdate = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    private boolean timeToUpdate() {
        return System.currentTimeMillis() - this.lastUpdate > 60 * 60 * 1000;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public double getRainDownpour() {
        return this.rain;
    }

    public double getSnowfall() {
        return this.snowfall;
    }

    public double getCloudiness() {
        return this.cloudiness;
    }

    public boolean showRainbow() {
        return showRainbow;
    }

    private boolean raining = false;
    private boolean showRainbow = false;

    private double[] getWeatherByTime() {
        int index = new Random().nextInt(5);
        if (index == 1 || index == 2) {
            raining = true;
            showRainbow = false;
            if (index == 1) {
                return WEATHER_RAIN;
            } else {
                return WEATHER_BIG_RAIN;
            }
        }
        if (index == 0) {
            showRainbow = raining;
            raining = false;
            return WEATHER_CLOUD;
        }
        showRainbow = false;
        raining = false;
        if (index == 3) {
            return WEATHER_SNOW;
        } else {
            return WEATHER_BIG_SNOW;
        }
    }
}
