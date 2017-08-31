package com.yalin.wallpaper.analog_clock;

import android.graphics.Color;

public class Ini {
    int color_1;
    int color_2;
    int color_back;
    int dx;
    int dy;
    int font_index;
    String logo;
    boolean show_date;
    boolean show_day_of_week;
    boolean show_digital_clock;
    boolean show_gradient;
    boolean show_month;
    boolean show_seconds;
    int size;

    void Get() {
        this.show_seconds = true;
        this.show_date = true;
        this.show_month = this.show_date;
        this.show_day_of_week = this.show_date;
        this.show_digital_clock = false;
        this.show_gradient = true;
        this.font_index = 0;
        this.color_1 = Color.parseColor("#FF4081");
        this.color_2 = Color.parseColor("#303F9F");
        this.color_back = Color.parseColor("#1b1b1b");
        this.logo = "Kinglloy.com";
        this.size = 90;
        this.dx = 0;
        this.dy = 0;
    }

    int GetClockSize(int size_base) {
        int clock_size = (this.size * (size_base - 4)) / 100;
        if ((clock_size & 1) == 0) {
            return clock_size - 1;
        }
        return clock_size;
    }
}
