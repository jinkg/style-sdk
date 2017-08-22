package com.yalin.wallpaper.forest.core;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static SunData getSunData() {
        SunData data = new SunData();
//        Location l = LocationManager.getCachedLocation();
//        if (l != null) {
//            data.setLocation(l);
//        }
        return data;
    }

    public static FloatBuffer arrayToBuffer(float[] a) {
        return (FloatBuffer) ByteBuffer.allocateDirect(a.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(a).position(0);
    }

    public static ShortBuffer arrayToBuffer(short[] a) {
        return (ShortBuffer) ByteBuffer.allocateDirect(a.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer().put(a).position(0);
    }

    public static Date stringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }
}
