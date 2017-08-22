package net.e175.klaus.solarpositioning;

public class AzimuthZenithAngle {
    private final double azimuth;
    private final double zenithAngle;

    public AzimuthZenithAngle(double azimuth, double zenithAngle) {
        this.zenithAngle = zenithAngle;
        this.azimuth = azimuth;
    }

    public final double getZenithAngle() {
        return this.zenithAngle;
    }

    public final double getAzimuth() {
        return this.azimuth;
    }

    public String toString() {
        return String.format("azimuth %.6f deg, zenith angle %.6f deg",
                new Object[]{Double.valueOf(this.azimuth), Double.valueOf(this.zenithAngle)});
    }
}
