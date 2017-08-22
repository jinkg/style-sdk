package com.yalin.wallpaper.forest.core;


import net.e175.klaus.solarpositioning.PSA;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SunData {
    private double latitude;
    private double longitude;
    private boolean upperHemisphere;

    public SunData() {
        latitude = 20;
        longitude = 100;
        upperHemisphere = true;
    }

    public double getAzimuth() {
        return getAzimuth(new GregorianCalendar());
    }

    public double getAzimuth(GregorianCalendar cal) {
        return PSA.calculateSolarPosition(cal, this.latitude, this.longitude).getAzimuth();
    }

    public double getZenithAngle() {
        return getZenithAngle(new GregorianCalendar());
    }

    public double getZenithAngle(GregorianCalendar cal) {
        return PSA.calculateSolarPosition(cal, this.latitude, this.longitude).getZenithAngle();
    }

    public boolean isSummer(Calendar cal) {
        int i = 1;
        int day = cal.get(Calendar.DAY_OF_YEAR);
        int i2 = (day <= 91 || day >= 273) ? 0 : 1;
        if (this.upperHemisphere) {
            i = 0;
        }
        return (i2 ^ i) != 0;
    }

    public String toString() {
        return String.format("<SUN DATA: azimuth: %.2f - zenith: %.2f>", new Object[]{Double.valueOf(getAzimuth()), Double.valueOf(getZenithAngle())});
    }
}
