package com.yalin.livewallpaper3d;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Blake on 9/18/2015.
 */
public class TimeTracker
{
    Calendar calendar;
    Date currentTime;
    int nightBegin = 20;
    int nightEnd = 4;
    int dawnBegin = 5;
    int dawnEnd = 9;
    int dayBegin = 10;
    int dayEnd = 18;
    int sunsetBegin = 19;
    int sunsetEnd = 19;

    public TimeTracker()
    {
        calendar = new GregorianCalendar();
        currentTime = new Date();
    }

    public int getDayHour()
    {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("Hour of day", "Hour of day: " + hour);
        if((hour >= nightBegin && hour <= 24) || (hour >= 0 && hour <= nightEnd))
        {
            return DataCodes.NIGHT;
        }
        else if(hour >= dawnBegin && hour <= dawnEnd)
        {
            return DataCodes.DAWN;
        }
        else if(hour >= dayBegin && hour <= dayEnd)
        {
            return DataCodes.DAY;
        }
        else return DataCodes.SUNSET;

    }
}
