package net.e175.klaus.solarpositioning;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class PSA {
    private static final double D_ASTRONOMICAL_UNIT = 1.4959789E8d;
    private static final double D_EARTH_MEAN_RADIUS = 6371.01d;
    private static final double PI = 3.141592653589793d;
    private static final double RAD = 0.017453292519943295d;
    private static final double TWOPI = 6.283185307179586d;

    private PSA() {
    }

    public static AzimuthZenithAngle calculateSolarPosition(GregorianCalendar date, double latitude, double longitude) {
        Calendar utcTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        utcTime.setTimeInMillis(date.getTimeInMillis());
        double dDecimalHours = ((double) utcTime.get(Calendar.HOUR_OF_DAY))
                + ((((double) utcTime.get(Calendar.MINUTE))
                + (((double) utcTime.get(Calendar.SECOND)) / 60.0d)) / 60.0d);
        long liAux1 = (long) (((utcTime.get(Calendar.MONTH) + 1) - 14) / 12);
        double dElapsedJulianDays = ((((double) ((((((1461 * (((long) (utcTime.get(Calendar.YEAR) + 4800)) + liAux1)) / 4)
                + ((367 * (((long) ((utcTime.get(Calendar.MONTH) + 1) - 2)) - (12 * liAux1))) / 12))
                - ((3 * ((((long) (utcTime.get(Calendar.YEAR) + 4900)) + liAux1) / 100)) / 4))
                + ((long) utcTime.get(Calendar.DAY_OF_MONTH))) - 32075)) - 0.5d)
                + (dDecimalHours / 24.0d)) - 2451545.0d;
        double dOmega = 2.1429d - (0.0010394594d * dElapsedJulianDays);
        double dMeanAnomaly = 6.24006d + (0.0172019699d * dElapsedJulianDays);
        double dEclipticLongitude = ((((0.03341607d * Math.sin(dMeanAnomaly))
                + (4.895063d + (0.017202791698d * dElapsedJulianDays)))
                + (3.4894E-4d * Math.sin(2.0d * dMeanAnomaly))) - 1.134E-4d) - (2.03E-5d * Math.sin(dOmega));
        double dEclipticObliquity = (0.4090928d - (6.214E-9d * dElapsedJulianDays)) + (3.96E-5d * Math.cos(dOmega));
        double dSinEclipticLongitude = Math.sin(dEclipticLongitude);
        double dRightAscension = Math.atan2(Math.cos(dEclipticObliquity) * dSinEclipticLongitude, Math.cos(dEclipticLongitude));
        if (dRightAscension < 0.0d) {
            dRightAscension += TWOPI;
        }
        double dDeclination = Math.asin(Math.sin(dEclipticObliquity) * dSinEclipticLongitude);
        double dHourAngle = (((15.0d * ((6.6974243242d + (0.0657098283d * dElapsedJulianDays)) + dDecimalHours)) + longitude) * RAD) - dRightAscension;
        double dLatitudeInRadians = latitude * RAD;
        double dCosLatitude = Math.cos(dLatitudeInRadians);
        double dSinLatitude = Math.sin(dLatitudeInRadians);
        double dCosHourAngle = Math.cos(dHourAngle);
        double zenithAngle = Math.acos(((dCosLatitude * dCosHourAngle) * Math.cos(dDeclination)) + (Math.sin(dDeclination) * dSinLatitude));
        double azimuth = Math.atan2(-Math.sin(dHourAngle), (Math.tan(dDeclination) * dCosLatitude) - (dSinLatitude * dCosHourAngle));
        if (azimuth < 0.0d) {
            azimuth += TWOPI;
        }
        return new AzimuthZenithAngle(azimuth / RAD, (zenithAngle + (4.2587565907513806E-5d * Math.sin(zenithAngle))) / RAD);
    }
}
