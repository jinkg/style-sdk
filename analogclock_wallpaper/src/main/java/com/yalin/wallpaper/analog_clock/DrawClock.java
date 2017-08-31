package com.yalin.wallpaper.analog_clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DrawClock {
    static final float radianPerGradus = 0.017453292f;
    static final int typeface_max = 5;

    public static void DrawDial(Context context, Bitmap dial, int size_base, Ini ini) {
        float angle;
        String text;
        float c = (float) (dial.getHeight() / 2);
        dial.eraseColor(0);
        Canvas canvas = new Canvas(dial);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Cap.ROUND);
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(GetTypeface(ini.font_index));
        float radius = (float) (size_base / 2);
        float r1 = radius * 0.98f;
        float r2 = radius * 0.95f;
        float rt = radius * 0.8f;
        float width1 = radius / 45.0f;
        float width2 = radius / 150.0f;
        paint.setTextSize(radius / 5.0f);
        if (ini.show_gradient) {
            paint.setShader(new LinearGradient(0.0f, c - radius, 0.0f, c, -1, ini.color_1, TileMode.MIRROR));
        } else {
            paint.setColor(ini.color_1);
        }
        for (int i = 0; i < 60; i++) {
            float x2;
            float y2;
            angle = ((float) (i * 6)) * radianPerGradus;
            float x1 = RotateX(angle, radius, c, c);
            float y1 = RotateY(angle, radius, c, c);
            if (i % 5 == 0) {
                x2 = RotateX(angle, r2, c, c);
                y2 = RotateY(angle, r2, c, c);
                paint.setStrokeWidth(width1);
            } else {
                x2 = RotateX(angle, r1, c, c);
                y2 = RotateY(angle, r1, c, c);
                paint.setStrokeWidth(width2);
            }
            canvas.drawLine(x1, y1, x2, y2, paint);
            if (i % 5 == 0) {
                Integer num = Integer.valueOf(i / 5);
                if (num.intValue() == 0) {
                    num = Integer.valueOf(12);
                }
                float addX = 0.0f;
                switch (num.intValue()) {
                    case 2:
                    case 4:
                        addX = (float) (size_base / 75);
                        break;
                    case 3:
                        addX = (float) (size_base / 50);
                        break;
                    case 8:
                        addX = (float) ((-size_base) / 75);
                        break;
                    case 9:
                        addX = (float) ((-size_base) / 50);
                        break;
                }
                canvas.drawText(num.toString(), RotateX(angle, rt, c, c) + addX, RotateY(angle, rt, c, c) - ((paint.ascent() + (paint.descent() / 2.0f)) / 2.0f), paint);
            }
        }
        paint.setTextSize(radius / 12.0f);
        canvas.drawText(ini.logo, c, (c - (0.3f * radius)) - paint.descent(), paint);
        Calendar calendar = Calendar.getInstance();
        if (ini.show_date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
            paint.setTextSize(radius / 7.0f);
            canvas.drawText(simpleDateFormat.format(calendar.getTime()), (0.6f * radius) + c, c - ((paint.ascent() + (paint.descent() / 2.0f)) / 2.0f), paint);
            RectF rectF = new RectF((0.5f * radius) + c, c - (0.085f * radius), (0.7f * radius) + c, (0.095f * radius) + c);
            paint.setStrokeWidth(width2);
            paint.setStyle(Style.STROKE);
            paint.setAntiAlias(false);
            canvas.drawRect(rectF, paint);
            paint.setAntiAlias(true);
            paint.setStyle(Style.FILL);
        }
        if (ini.show_day_of_week) {
            paint.setTextSize(radius / 9.0f);
            text = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
            if (text.length() > 3) {
                text = text.substring(0, 3);
            }
            canvas.drawText(text, c - (0.6f * radius), c - ((paint.ascent() + (paint.descent() / 2.0f)) / 2.0f), paint);
        }
        if (ini.show_month) {
            text = new SimpleDateFormat("MMM").format(calendar.getTime()).toUpperCase().replace(".", "");
            if (text.length() > 3) {
                text = text.substring(0, 3);
            }
            canvas.drawText(text, (0.3f * radius) + c, c - ((paint.ascent() + (paint.descent() / 2.0f)) / 2.0f), paint);
        }
        if (ini.show_digital_clock) {
            boolean is24 = DateFormat.is24HourFormat(context);
            SimpleDateFormat df;
            if (is24) {
                df = new SimpleDateFormat("HH:mm");
            } else {
                df = new SimpleDateFormat("hh:mm");
            }
            text = df.format(calendar.getTime());
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize(radius / 7.0f);
            canvas.drawText(text, c, (0.4f * radius) + c, paint);
            if (!is24) {
                String text2 = new SimpleDateFormat("a").format(calendar.getTime());
                if (text2.length() > 2) {
                    if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                        text2 = "AM";
                    } else {
                        text2 = "PM";
                    }
                }
                float tmp = paint.measureText(text);
                paint.setTextSize(radius / 13.0f);
                paint.setTextAlign(Align.LEFT);
                canvas.drawText(text2, (tmp / 2.0f) + c, (0.35f * radius) + c, paint);
                paint.setTextAlign(Align.CENTER);
            }
        }
        if (!ini.show_gradient) {
            float shadowDx = radius / 150.0f;
            paint.setShadowLayer(radius / 150.0f, shadowDx, shadowDx, Color.argb(128, 0, 0, 0));
        }
        float hour = (float) calendar.get(Calendar.HOUR);
        if (hour > 12.0f) {
            hour -= 12.0f;
        }
        float min = (float) calendar.get(Calendar.MINUTE);
        angle = radianPerGradus * ((360.0f * (hour + (min / 60.0f))) / 12.0f);
        float x = RotateX(angle, 0.6f * radius, c, c);
        float y = RotateY(angle, 0.6f * radius, c, c);
        paint.setStrokeWidth(radius / 20.0f);
        canvas.drawLine(x, y, c, c, paint);
        angle = (6.0f * min) * radianPerGradus;
        x = RotateX(angle, 0.85f * radius, c, c);
        y = RotateY(angle, 0.85f * radius, c, c);
        paint.setStrokeWidth(radius / 25.0f);
        canvas.drawLine(x, y, c, c, paint);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(c, c, radius / 20.0f, paint);
    }

    public static void DrawBack(Canvas canvas, Bitmap dial, int add_x, int add_y) {
        int size_base = dial.getHeight();
        int dx = add_x + ((canvas.getWidth() - size_base) / 2);
        int dy = add_y + ((canvas.getHeight() - size_base) / 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(dial, new Rect(0, 0, size_base, size_base), new Rect(dx + 0, dy + 0, size_base + dx, size_base + dy), paint);
    }

    public static void DrawSecondHand(Canvas canvas, int size_base, Ini ini, int dx, int dy) {
        float cx = (float) ((canvas.getWidth() / 2) + dx);
        float cy = (float) ((canvas.getHeight() / 2) + dy);
        Calendar calendar = Calendar.getInstance();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Cap.ROUND);
        float radius = (float) (size_base / 2);
        if (ini.show_gradient) {
            paint.setShader(new LinearGradient(0.0f, cx - radius, 0.0f, cy, -1, ini.color_2, TileMode.MIRROR));
        } else {
            paint.setColor(ini.color_2);
            float shadowDx = radius / 150.0f;
            paint.setShadowLayer(radius / 150.0f, shadowDx, shadowDx, Color.argb(128, 0, 0, 0));
        }
        float angle = ((float) (calendar.get(Calendar.SECOND) * 6)) * radianPerGradus;
        float x = RotateX(angle, 0.9f * radius, cx, cy);
        float y = RotateY(angle, 0.9f * radius, cx, cy);
        paint.setStrokeWidth(radius / 50.0f);
        canvas.drawLine(x, y, cx, cy, paint);
        angle += 3.1415927f;
        x = RotateX(angle, 0.33f * radius, cx, cy);
        y = RotateY(angle, 0.33f * radius, cx, cy);
        paint.setStrokeWidth(radius / 30.0f);
        canvas.drawLine(x, y, cx, cy, paint);
        canvas.drawCircle(cx, cy, radius / 28.0f, paint);
    }

    public static float RotateX(float angle, float r, float cx, float cy) {
        return (((float) Math.sin((double) angle)) * r) + cx;
    }

    public static float RotateY(float angle, float r, float cx, float cy) {
        return cy - (((float) Math.cos((double) angle)) * r);
    }

    static Typeface GetTypeface(int index) {
        switch (index) {
            case 0:
                return Typeface.SANS_SERIF;
            case 1:
                return Typeface.create(Typeface.SANS_SERIF, 1);
            case 2:
                return Typeface.SERIF;
            case 3:
                return Typeface.create(Typeface.SERIF, 1);
            default:
                return Typeface.MONOSPACE;
        }
    }
}
