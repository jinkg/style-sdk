package com.yalin.wallpaper.miku;

import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import jp.live2d.Live2D;

public class LAppLive2DManager_WP {
    static boolean ChrTouch = false;
    public static final String TAG = "LAppLive2DManager_WP";
    static boolean okiru_3rd = false;
    static boolean reloadFlg = true;
    int bustlevel = 0;
    int katamuki = 0;
    private int modelCount = -1;
    private ArrayList<LAppModel> models;
    int yure = 0;

    public LAppLive2DManager_WP() {
        Live2D.init();
        this.models = new ArrayList();
    }

    public void releaseModel() {
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).release();
        }
        this.models.clear();
    }

    public void update(GL10 gl) {
        if (reloadFlg) {
            reloadFlg = false;
            int no = this.modelCount % 5;
            Log.d(TAG, "キャラ更新　update " + LiveWallpaper.chr);
            try {
                releaseModel();
                this.models.add(new LAppModel());
                if (LiveWallpaper.size == 2) {
                    this.models.get(0).load(gl, LAppDefine.MODEL_MIKU_LOW);
                } else if (LiveWallpaper.size == 1) {
                    this.models.get(0).load(gl, LAppDefine.MODEL_MIKU_NOR);
                } else {
                    this.models.get(0).load(gl, LAppDefine.MODEL_MIKU_HI);
                }
                this.models.get(0).feedIn();
            } catch (Exception e) {
                reloadFlg = true;
                e.printStackTrace();
            }
        }
    }

    public LAppModel getModel(int no) {
        if (no >= this.models.size()) {
            return null;
        }
        return this.models.get(no);
    }

    public int getModelNum() {
        return this.models.size();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("onSurfaceChanged_WP", width + "　LAPP " + height);
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "onSurfaceChanged " + width + " " + height);
        }
        if (getModelNum() == 0) {
            changeModel();
        }
    }

    public void changeModel() {
        reloadFlg = true;
        this.modelCount++;
    }

    public void timeEvent() {
        int i;
        String hou = "";
        if (LiveWallpaper.hour == 0) {
            hou = "h00";
        } else if (LiveWallpaper.hour == 1) {
            hou = "h01";
        } else if (LiveWallpaper.hour == 2) {
            hou = "h02";
        } else if (LiveWallpaper.hour == 3) {
            hou = "h03";
        } else if (LiveWallpaper.hour == 4) {
            hou = "h04";
        } else if (LiveWallpaper.hour == 5) {
            hou = "h05";
        } else if (LiveWallpaper.hour == 6) {
            hou = "h06";
        } else if (LiveWallpaper.hour == 7) {
            hou = "h07";
        } else if (LiveWallpaper.hour == 8) {
            hou = "h08";
        } else if (LiveWallpaper.hour == 9) {
            hou = "h09";
        } else if (LiveWallpaper.hour == 10) {
            hou = "h10";
        } else if (LiveWallpaper.hour == 11) {
            hou = "h11";
        } else if (LiveWallpaper.hour == 12) {
            hou = "h12";
        } else if (LiveWallpaper.hour == 13) {
            hou = "h13";
        } else if (LiveWallpaper.hour == 14) {
            hou = "h14";
        } else if (LiveWallpaper.hour == 15) {
            hou = "h15";
        } else if (LiveWallpaper.hour == 16) {
            hou = "h16";
        } else if (LiveWallpaper.hour == 17) {
            hou = "h17";
        } else if (LiveWallpaper.hour == 18) {
            hou = "h18";
        } else if (LiveWallpaper.hour == 19) {
            hou = "h19";
        } else if (LiveWallpaper.hour == 20) {
            hou = "h20";
        } else if (LiveWallpaper.hour == 21) {
            hou = "h21";
        } else if (LiveWallpaper.hour == 22) {
            hou = "h22";
        } else if (LiveWallpaper.hour == 23) {
            hou = "h23";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(hou);
        }
        String sec = "";
        if (LiveWallpaper.second % 2 == 0) {
            sec = "s00";
        }
        if (LiveWallpaper.second % 2 == 1) {
            sec = "s01";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(sec);
        }
    }

    public void minEvent() {
        int i;
        String min01 = "";
        if (LiveWallpaper.minute % 10 == 0) {
            min01 = "m00";
        }
        if (LiveWallpaper.minute % 10 == 1) {
            min01 = "m01";
        }
        if (LiveWallpaper.minute % 10 == 2) {
            min01 = "m02";
        }
        if (LiveWallpaper.minute % 10 == 3) {
            min01 = "m03";
        }
        if (LiveWallpaper.minute % 10 == 4) {
            min01 = "m04";
        }
        if (LiveWallpaper.minute % 10 == 5) {
            min01 = "m05";
        }
        if (LiveWallpaper.minute % 10 == 6) {
            min01 = "m06";
        }
        if (LiveWallpaper.minute % 10 == 7) {
            min01 = "m07";
        }
        if (LiveWallpaper.minute % 10 == 8) {
            min01 = "m08";
        }
        if (LiveWallpaper.minute % 10 == 9) {
            min01 = "m09";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(min01);
        }
        String min10 = "";
        if (LiveWallpaper.minute >= 50 && LiveWallpaper.minute <= 59) {
            min10 = "m50";
        }
        if (LiveWallpaper.minute >= 40 && LiveWallpaper.minute <= 49) {
            min10 = "m40";
        }
        if (LiveWallpaper.minute >= 30 && LiveWallpaper.minute <= 39) {
            min10 = "m30";
        }
        if (LiveWallpaper.minute >= 20 && LiveWallpaper.minute <= 29) {
            min10 = "m20";
        }
        if (LiveWallpaper.minute >= 10 && LiveWallpaper.minute <= 19) {
            min10 = "m10";
        }
        if (LiveWallpaper.minute >= 0 && LiveWallpaper.minute <= 9) {
            min10 = "m60";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(min10);
        }
    }

    public void weekEvent() {
        int i;
        int year = LiveWallpaper.year - 2000;
        String year01 = "";
        if (year % 10 == 0) {
            year01 = "y00";
        }
        if (year % 10 == 1) {
            year01 = "y01";
        }
        if (year % 10 == 2) {
            year01 = "y02";
        }
        if (year % 10 == 3) {
            year01 = "y03";
        }
        if (year % 10 == 4) {
            year01 = "y04";
        }
        if (year % 10 == 5) {
            year01 = "y05";
        }
        if (year % 10 == 6) {
            year01 = "y06";
        }
        if (year % 10 == 7) {
            year01 = "y07";
        }
        if (year % 10 == 8) {
            year01 = "y08";
        }
        if (year % 10 == 9) {
            year01 = "y09";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(year01);
        }
        String year10 = "";
        if (year >= 0 && year <= 9) {
            year10 = "y100";
        }
        if (year >= 10 && year <= 19) {
            year10 = "y10";
        }
        if (year >= 20 && year <= 29) {
            year10 = "y20";
        }
        if (year >= 30 && year <= 39) {
            year10 = "y30";
        }
        if (year >= 40 && year <= 49) {
            year10 = "y40";
        }
        if (year >= 50 && year <= 59) {
            year10 = "y50";
        }
        if (year >= 60 && year <= 69) {
            year10 = "y60";
        }
        if (year >= 70 && year <= 79) {
            year10 = "y70";
        }
        if (year >= 80 && year <= 89) {
            year10 = "y80";
        }
        if (year >= 90 && year <= 99) {
            year10 = "y90";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(year10);
        }
        String mon = "";
        if (LiveWallpaper.month == 1) {
            mon = "n01";
        }
        if (LiveWallpaper.month == 2) {
            mon = "n02";
        }
        if (LiveWallpaper.month == 3) {
            mon = "n03";
        }
        if (LiveWallpaper.month == 4) {
            mon = "n04";
        }
        if (LiveWallpaper.month == 5) {
            mon = "n05";
        }
        if (LiveWallpaper.month == 6) {
            mon = "n06";
        }
        if (LiveWallpaper.month == 7) {
            mon = "n07";
        }
        if (LiveWallpaper.month == 8) {
            mon = "n08";
        }
        if (LiveWallpaper.month == 9) {
            mon = "n09";
        }
        if (LiveWallpaper.month == 10) {
            mon = "n10";
        }
        if (LiveWallpaper.month == 11) {
            mon = "n11";
        }
        if (LiveWallpaper.month == 12) {
            mon = "n12";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(mon);
        }
        String we = "";
        if (LiveWallpaper.week == 1) {
            we = "we00";
        }
        if (LiveWallpaper.week == 2) {
            we = "we01";
        }
        if (LiveWallpaper.week == 3) {
            we = "we02";
        }
        if (LiveWallpaper.week == 4) {
            we = "we03";
        }
        if (LiveWallpaper.week == 5) {
            we = "we04";
        }
        if (LiveWallpaper.week == 6) {
            we = "we05";
        }
        if (LiveWallpaper.week == 7) {
            we = "we06";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(we);
        }
        String day10 = "";
        if (LiveWallpaper.day >= 0 && LiveWallpaper.day <= 9) {
            day10 = "d40";
        }
        if (LiveWallpaper.day >= 10 && LiveWallpaper.day <= 19) {
            day10 = "d10";
        }
        if (LiveWallpaper.day >= 20 && LiveWallpaper.day <= 29) {
            day10 = "d20";
        }
        if (LiveWallpaper.day >= 30) {
            day10 = "d30";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(day10);
        }
        String day01 = "";
        if (LiveWallpaper.day % 10 == 0) {
            day01 = "d00";
        }
        if (LiveWallpaper.day % 10 == 1) {
            day01 = "d01";
        }
        if (LiveWallpaper.day % 10 == 2) {
            day01 = "d02";
        }
        if (LiveWallpaper.day % 10 == 3) {
            day01 = "d03";
        }
        if (LiveWallpaper.day % 10 == 4) {
            day01 = "d04";
        }
        if (LiveWallpaper.day % 10 == 5) {
            day01 = "d05";
        }
        if (LiveWallpaper.day % 10 == 6) {
            day01 = "d06";
        }
        if (LiveWallpaper.day % 10 == 7) {
            day01 = "d07";
        }
        if (LiveWallpaper.day % 10 == 8) {
            day01 = "d08";
        }
        if (LiveWallpaper.day % 10 == 9) {
            day01 = "d09";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTime(day01);
        }
    }

    public void BattelyEvent() {
        int i;
        String Lv01 = "";
        if (LiveWallpaper.level % 10 == 0) {
            Lv01 = "L0";
        }
        if (LiveWallpaper.level % 10 == 1) {
            Lv01 = "L01";
        }
        if (LiveWallpaper.level % 10 == 2) {
            Lv01 = "L02";
        }
        if (LiveWallpaper.level % 10 == 3) {
            Lv01 = "L03";
        }
        if (LiveWallpaper.level % 10 == 4) {
            Lv01 = "L04";
        }
        if (LiveWallpaper.level % 10 == 5) {
            Lv01 = "L05";
        }
        if (LiveWallpaper.level % 10 == 6) {
            Lv01 = "L06";
        }
        if (LiveWallpaper.level % 10 == 7) {
            Lv01 = "L07";
        }
        if (LiveWallpaper.level % 10 == 8) {
            Lv01 = "L08";
        }
        if (LiveWallpaper.level % 10 == 9) {
            Lv01 = "L09";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionLevel1(Lv01);
        }
        String Lv10 = "";
        if (LiveWallpaper.level >= 0 && LiveWallpaper.level <= 9) {
            Lv10 = "L00";
        }
        if (LiveWallpaper.level >= 10 && LiveWallpaper.level <= 19) {
            Lv10 = "L10";
        }
        if (LiveWallpaper.level >= 20 && LiveWallpaper.level <= 29) {
            Lv10 = "L20";
        }
        if (LiveWallpaper.level >= 30 && LiveWallpaper.level <= 39) {
            Lv10 = "L30";
        }
        if (LiveWallpaper.level >= 40 && LiveWallpaper.level <= 49) {
            Lv10 = "L40";
        }
        if (LiveWallpaper.level >= 50 && LiveWallpaper.level <= 59) {
            Lv10 = "L50";
        }
        if (LiveWallpaper.level >= 60 && LiveWallpaper.level <= 69) {
            Lv10 = "L60";
        }
        if (LiveWallpaper.level >= 70 && LiveWallpaper.level <= 79) {
            Lv10 = "L70";
        }
        if (LiveWallpaper.level >= 80 && LiveWallpaper.level <= 89) {
            Lv10 = "L80";
        }
        if (LiveWallpaper.level >= 90 && LiveWallpaper.level <= 99) {
            Lv10 = "L90";
        }
        if (LiveWallpaper.level == 100) {
            Lv10 = "L100";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionLevel2(Lv10);
        }
        int tem = (int) (LiveWallpaper.temperature_f * 10.0f);
        String temp_1 = "";
        if (tem % 10 == 0) {
            temp_1 = "t_0";
        }
        if (tem % 10 == 1) {
            temp_1 = "t_1";
        }
        if (tem % 10 == 2) {
            temp_1 = "t_2";
        }
        if (tem % 10 == 3) {
            temp_1 = "t_3";
        }
        if (tem % 10 == 4) {
            temp_1 = "t_4";
        }
        if (tem % 10 == 5) {
            temp_1 = "t_5";
        }
        if (tem % 10 == 6) {
            temp_1 = "t_6";
        }
        if (tem % 10 == 7) {
            temp_1 = "t_7";
        }
        if (tem % 10 == 8) {
            temp_1 = "t_8";
        }
        if (tem % 10 == 9) {
            temp_1 = "t_9";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp1(temp_1);
        }
        int tmp = (int) LiveWallpaper.temperature_f;
        String temp01 = "";
        if (tmp % 10 == 0) {
            temp01 = "t0";
        }
        if (tmp % 10 == 1) {
            temp01 = "t01";
        }
        if (tmp % 10 == 2) {
            temp01 = "t02";
        }
        if (tmp % 10 == 3) {
            temp01 = "t03";
        }
        if (tmp % 10 == 4) {
            temp01 = "t04";
        }
        if (tmp % 10 == 5) {
            temp01 = "t05";
        }
        if (tmp % 10 == 6) {
            temp01 = "t06";
        }
        if (tmp % 10 == 7) {
            temp01 = "t07";
        }
        if (tmp % 10 == 8) {
            temp01 = "t08";
        }
        if (tmp % 10 == 9) {
            temp01 = "t09";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp2(temp01);
        }
        String temp100 = "";
        if (LiveWallpaper.temperature_f < 100.0f) {
            temp100 = "t000";
        }
        if (LiveWallpaper.temperature_f > 100.0f) {
            temp100 = "t100";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp3(temp100);
        }
        String select = "";
        if (!LiveWallpaper.info_tap) {
            select = "select_off";
        }
        if (LiveWallpaper.info_tap) {
            select = "select_on";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpression(select);
        }
        String hot = "";
        if (LiveWallpaper.temperature < 40.0f) {
            hot = "hot00";
        }
        if (LiveWallpaper.temperature >= 40.0f) {
            hot = "hot01";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpression(hot);
        }
        String temp10 = "";
        boolean f = false;
        if (LiveWallpaper.temperature_f >= 100.0f) {
            LiveWallpaper.temperature_f -= 100.0f;
            f = true;
        }
        if (LiveWallpaper.temperature_f >= 0.0f && ((double) LiveWallpaper.temperature_f) <= 9.9d) {
            temp10 = "t00";
        }
        if (((double) LiveWallpaper.temperature_f) >= 10.0d && ((double) LiveWallpaper.temperature_f) <= 19.9d) {
            temp10 = "t10";
        }
        if (((double) LiveWallpaper.temperature_f) >= 20.0d && ((double) LiveWallpaper.temperature_f) <= 29.9d) {
            temp10 = "t20";
        }
        if (((double) LiveWallpaper.temperature_f) >= 30.0d && ((double) LiveWallpaper.temperature_f) <= 39.9d) {
            temp10 = "t30";
        }
        if (((double) LiveWallpaper.temperature_f) >= 40.0d && ((double) LiveWallpaper.temperature_f) <= 49.9d) {
            temp10 = "t40";
        }
        if (((double) LiveWallpaper.temperature_f) >= 50.0d && ((double) LiveWallpaper.temperature_f) <= 59.9d) {
            temp10 = "t50";
        }
        if (((double) LiveWallpaper.temperature_f) >= 60.0d && ((double) LiveWallpaper.temperature_f) <= 69.9d) {
            temp10 = "t60";
        }
        if (((double) LiveWallpaper.temperature_f) >= 70.0d && ((double) LiveWallpaper.temperature_f) <= 79.9d) {
            temp10 = "t70";
        }
        if (((double) LiveWallpaper.temperature_f) >= 80.0d && ((double) LiveWallpaper.temperature_f) <= 89.9d) {
            temp10 = "t80";
        }
        if (((double) LiveWallpaper.temperature_f) >= 90.0d && ((double) LiveWallpaper.temperature_f) <= 99.9d) {
            temp10 = "t90";
        }
        if (f) {
            LiveWallpaper.temperature_f += 100.0f;
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp3(temp10);
        }
        int tem_c = (int) (LiveWallpaper.temperature * 10.0f);
        String temp_1c = "";
        if (tem_c % 10 == 0) {
            temp_1c = "t_0c";
        }
        if (tem_c % 10 == 1) {
            temp_1c = "t_1c";
        }
        if (tem_c % 10 == 2) {
            temp_1c = "t_2c";
        }
        if (tem_c % 10 == 3) {
            temp_1c = "t_3c";
        }
        if (tem_c % 10 == 4) {
            temp_1c = "t_4c";
        }
        if (tem_c % 10 == 5) {
            temp_1c = "t_5c";
        }
        if (tem_c % 10 == 6) {
            temp_1c = "t_6c";
        }
        if (tem_c % 10 == 7) {
            temp_1c = "t_7c";
        }
        if (tem_c % 10 == 8) {
            temp_1c = "t_8c";
        }
        if (tem_c % 10 == 9) {
            temp_1c = "t_9c";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp1(temp_1c);
        }
        int tmp_c = (int) LiveWallpaper.temperature;
        String temp01c = "";
        if (tmp_c % 10 == 0) {
            temp01c = "t0c";
        }
        if (tmp_c % 10 == 1) {
            temp01c = "t01c";
        }
        if (tmp_c % 10 == 2) {
            temp01c = "t02c";
        }
        if (tmp_c % 10 == 3) {
            temp01c = "t03c";
        }
        if (tmp_c % 10 == 4) {
            temp01c = "t04c";
        }
        if (tmp_c % 10 == 5) {
            temp01c = "t05c";
        }
        if (tmp_c % 10 == 6) {
            temp01c = "t06c";
        }
        if (tmp_c % 10 == 7) {
            temp01c = "t07c";
        }
        if (tmp_c % 10 == 8) {
            temp01c = "t08c";
        }
        if (tmp_c % 10 == 9) {
            temp01c = "t09c";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp2(temp01c);
        }
        String temp10c = "";
        if (LiveWallpaper.temperature >= 0.0f && ((double) LiveWallpaper.temperature) <= 9.9d) {
            temp10c = "t00c";
        }
        if (((double) LiveWallpaper.temperature) >= 10.0d && ((double) LiveWallpaper.temperature) <= 19.9d) {
            temp10c = "t10c";
        }
        if (((double) LiveWallpaper.temperature) >= 20.0d && ((double) LiveWallpaper.temperature) <= 29.9d) {
            temp10c = "t20c";
        }
        if (((double) LiveWallpaper.temperature) >= 30.0d && ((double) LiveWallpaper.temperature) <= 39.9d) {
            temp10c = "t30c";
        }
        if (((double) LiveWallpaper.temperature) >= 40.0d && ((double) LiveWallpaper.temperature) <= 49.9d) {
            temp10c = "t40c";
        }
        if (((double) LiveWallpaper.temperature) >= 50.0d && ((double) LiveWallpaper.temperature) <= 59.9d) {
            temp10c = "t50c";
        }
        if (((double) LiveWallpaper.temperature) >= 60.0d && ((double) LiveWallpaper.temperature) <= 69.9d) {
            temp10c = "t60c";
        }
        if (((double) LiveWallpaper.temperature) >= 70.0d && ((double) LiveWallpaper.temperature) <= 79.9d) {
            temp10c = "t70c";
        }
        if (((double) LiveWallpaper.temperature) >= 80.0d && ((double) LiveWallpaper.temperature) <= 89.9d) {
            temp10c = "t80c";
        }
        if (((double) LiveWallpaper.temperature) >= 90.0d && ((double) LiveWallpaper.temperature) <= 99.9d) {
            temp10c = "t90c";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionTemp3(temp10c);
        }
        String volt01 = "";
        if (LiveWallpaper.voltage >= 5000) {
            volt01 = "v05";
        } else if (LiveWallpaper.voltage >= 4000 && LiveWallpaper.voltage < 5000) {
            volt01 = "v04";
        } else if (LiveWallpaper.voltage >= 3000 && LiveWallpaper.voltage < 4000) {
            volt01 = "v03";
        } else if (LiveWallpaper.voltage >= 2000 && LiveWallpaper.voltage < 3000) {
            volt01 = "v02";
        } else if (LiveWallpaper.voltage < 1000 || LiveWallpaper.voltage >= 2000) {
            volt01 = "v0";
        } else {
            volt01 = "v01";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionVolt01(volt01);
        }
        String vol_1 = "";
        if ((LiveWallpaper.voltage % 1000) / 100 == 0) {
            vol_1 = "v_0";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 1) {
            vol_1 = "v_1";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 2) {
            vol_1 = "v_2";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 3) {
            vol_1 = "v_3";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 4) {
            vol_1 = "v_4";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 5) {
            vol_1 = "v_5";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 6) {
            vol_1 = "v_6";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 7) {
            vol_1 = "v_7";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 8) {
            vol_1 = "v_8";
        }
        if ((LiveWallpaper.voltage % 1000) / 100 == 9) {
            vol_1 = "v_9";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionVol_1(vol_1);
        }
        String charge = "";
        if (LiveWallpaper.plugged >= 1) {
            charge = "charge01";
        } else {
            charge = "charge00";
        }
        for (i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpressionCharge(charge);
        }
    }

    public void CPU_Current() {
        float c = LiveWallpaper.cpu_system;
        if (LiveWallpaper.sleep == 1) {
            c = 0.0f;
        }
        String cpu = "";
        if (c < 10.0f) {
            cpu = "c00";
        } else if (c < 20.0f) {
            cpu = "c01";
        } else if (c < 30.0f) {
            cpu = "c02";
        } else if (c < 40.0f) {
            cpu = "c03";
        } else if (c < 50.0f) {
            cpu = "c04";
        } else if (c < 60.0f) {
            cpu = "c05";
        } else if (c < 70.0f) {
            cpu = "c06";
        } else if (c < 80.0f) {
            cpu = "c07";
        } else if (c < 90.0f) {
            cpu = "c08";
        } else if (c < 100.0f) {
            cpu = "c09";
        } else if (c >= 100.0f) {
            cpu = "c10";
        }
        for (int i = 0; i < this.models.size(); i++)
            this.models.get(i).setExpressionCpu(cpu);
    }

    public void Headphone() {
        int i;
        if (LiveWallpaper.Headphone_motion) {
            Log.d(TAG, " Headphone() ヘッドホン装着");
            for (i = 0; i < this.models.size(); i++) {
                this.models.get(i).startRandomMotion("headphone", 4);
                Log.d(TAG, " Headphone() ヘッドホン装着モーション");
                LiveWallpaper.Headphone_motion = false;
            }
        }
        if (LiveWallpaper.Headphone_offmotion) {
            Log.d(TAG, " Headphone() ヘッドホンOFF");
            for (i = 0; i < this.models.size(); i++) {
                this.models.get(i).startRandomMotion("headphone_off", 4);
                Log.d(TAG, " Headphone() ヘッドホンOFFモーション");
                LiveWallpaper.Headphone_offmotion = false;
            }
        }
        String headphone = "";
        if (LiveWallpaper.Headphone) {
            headphone = "head_on";
        } else {
            headphone = "head_off";
        }
        for (i = 0; i < this.models.size(); i++) {
            this.models.get(i).setExpressionCharge(headphone);
        }
    }

    public void wakeup() {
        Log.d("wakeup", "居眠りモードOFF" + LiveWallpaper.sleep + "   " + LiveWallpaper.sleep_count);
        for (int i = 0; i < this.models.size(); i++) {
            if (LiveWallpaper.wakeup) {
                if (LiveWallpaper.sleep_count >= 2) {
                    Log.d(TAG, " wakeup() 起きる3回目");
                    ((LAppModel) this.models.get(i)).startRandomMotion("wakeup03", 3);
                    LiveWallpaper.sleep_count = 0;
                    okiru_3rd = true;
                } else if (LiveWallpaper.minute % 10 <= 4) {
                    Log.d(TAG, " wakeup() 起きる１");
                    ((LAppModel) this.models.get(i)).startRandomMotion("wakeup01", 3);
                    LiveWallpaper.sleep_count++;
                } else {
                    Log.d(TAG, " wakeup() 起きる２");
                    ((LAppModel) this.models.get(i)).startRandomMotion("wakeup02", 3);
                    LiveWallpaper.sleep_count++;
                }
            }
            LiveWallpaper.sleep = 0;
            LiveWallpaper.wakeup = false;
        }
    }

    public void sleep01() {
        for (int i = 0; i < this.models.size(); i++) {
            if (LiveWallpaper.minute % 10 <= 4) {
                ((LAppModel) this.models.get(i)).startRandomMotion("idle_sleep01", 2);
            } else {
                ((LAppModel) this.models.get(i)).startRandomMotion("idle_sleep02", 2);
            }
        }
    }

    public void mailcall() {
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).startRandomMotion("maildayo", 4);
        }
    }

    public void CallAisatsu() {
        Log.d("aisatu", "ロック画面解除直後のあいさつ");
        LiveWallpaper.LockScreen_off = false;
        for (int i = 0; i < this.models.size(); i++) {
            if (LiveWallpaper.hour >= 5 && LiveWallpaper.hour <= 10 && !LiveWallpaper.Aisatsu_ohayo) {
                ((LAppModel) this.models.get(i)).startRandomMotion("ohayou", 3);
                Log.d("Aisatsu_ohayo", LiveWallpaper.time_ohayo + "  あいさつ" + LiveWallpaper.Aisatsu_ohayo);
                LiveWallpaper.Aisatsu_ohayo = true;
                LiveWallpaper.time_ohayo = System.currentTimeMillis();
            } else if (LiveWallpaper.hour >= 11 && LiveWallpaper.hour <= 18 && !LiveWallpaper.Aisatsu_am) {
                ((LAppModel) this.models.get(i)).startRandomMotion("konnitiwa", 3);
                LiveWallpaper.Aisatsu_am = true;
                LiveWallpaper.time_am = System.currentTimeMillis();
            } else if (LiveWallpaper.hour < 19 || LiveWallpaper.hour > 4 || LiveWallpaper.Aisatsu_pm) {
                ((LAppModel) this.models.get(i)).startRandomMotion("okaerinasai", 3);
            } else {
                LiveWallpaper.Aisatsu_pm = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("konbanwa", 3);
                LiveWallpaper.time_pm = System.currentTimeMillis();
            }
        }
    }

    public boolean tapEvent(float x, float y) {
        float x1 = (LiveWallpaper.tr_wp[12] + x) * LiveWallpaper.tr_wp[0];
        float y1 = (LiveWallpaper.tr_wp[13] + y) * LiveWallpaper.tr_wp[0];
        float Y = -0.6f;
        if (LiveWallpaper.tate_gamen) {
            Y = -0.6f;
        }
        if (!LiveWallpaper.tate_gamen) {
            Y = -0.2f;
        }
        Log.d(TAG, "tapEvent view2 x1:" + x1 + " y1:" + y1 + " tate_gamen = " + LiveWallpaper.tate_gamen + " " + Y + "  info_tap = " + LiveWallpaper.info_tap);
        boolean info_read = false;
        int i = 0;
        while (i < this.models.size()) {
            Log.d(TAG, "tapEvent view3 x1:" + x1 + " y1:" + y1 + " tate_gamen = " + LiveWallpaper.tate_gamen + " " + Y + "  info_tap = " + LiveWallpaper.info_tap);
            if (info_read && LiveWallpaper.timedisplay != 2) {
                if (((LAppModel) this.models.get(i)).hitTest("board", x, y) && LiveWallpaper.info_tap) {
                    LiveWallpaper.info_tap = false;
                    Log.d(TAG, "時計エリアをタップOFF info_tap = " + LiveWallpaper.info_tap);
                } else if (y1 < Y && !LiveWallpaper.info_tap) {
                    Log.d(TAG, "画面下部をタップON info_tap = " + LiveWallpaper.info_tap);
                    LiveWallpaper.info_tap = true;
                }
            }
            info_read = true;
            if (((LAppModel) this.models.get(i)).hitTest("face", x, y)) {
                Log.d(TAG, "顔をタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_face", 2);
                Log.d(TAG, "顔をタップした MOTION_GROUP_TAP_FACE再生  " + LiveWallpaper.Voice);
            } else if (((LAppModel) this.models.get(i)).hitTest("head", x, y)) {
                Log.d(TAG, "頭をタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_head", 2);
                Log.d(TAG, "頭をタップした MOTION_GROUP_TAP_HEAD再生");
            } else if (((LAppModel) this.models.get(i)).hitTest("arm_l", x, y) || ((LAppModel) this.models.get(i)).hitTest("arm_r", x, y)) {
                Log.d(TAG, "腕をタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_arm", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("bust", x, y)) {
                Log.d(TAG, "胸をタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_bust", 3);
                Log.d(TAG, "胸をタップした MOTION_GROUP_TAP_BUST再生");
            } else if (((LAppModel) this.models.get(i)).hitTest("body", x, y)) {
                Log.d(TAG, "体をタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_body", 2);
                Log.d(TAG, "身体をタップした MOTION_GROUP_TAP_BODY再生");
            } else if (((LAppModel) this.models.get(i)).hitTest("hair1", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair2", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair3", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair4", x, y)) {
                Log.d(TAG, "横髪をタップした");
                ChrTouch = true;
            } else {
                Log.d(TAG, "タップした（当たり判定なし） " + LiveWallpaper.sleep);
            }
            if (ChrTouch) {
                LiveWallpaper.TouchReset();
                if (LiveWallpaper.wakeup) {
                    wakeup();
                }
            }
            i++;
        }
        return true;
    }

    public boolean wtapEvent(float x, float y) {
        Log.d("タブルタップ", "wtapEvent view x:" + x + " y:" + y + " sleep = " + LiveWallpaper.sleep);
        int i = 0;
        while (i < this.models.size()) {
            if (((LAppModel) this.models.get(i)).hitTest("face", x, y)) {
                Log.d(TAG, "顔をWタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("wtap_face", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("head", x, y)) {
                Log.d(TAG, "頭をWタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("wtap_head", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("arm_l", x, y) || ((LAppModel) this.models.get(i)).hitTest("arm_r", x, y)) {
                Log.d(TAG, "腕をWタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_arm", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("bust", x, y)) {
                Log.d(TAG, "胸をWタップした");
                ChrTouch = true;
                LiveWallpaper.emotion--;
                ((LAppModel) this.models.get(i)).startRandomMotion("wtap_bust", 3);
            } else if (((LAppModel) this.models.get(i)).hitTest("body", x, y)) {
                Log.d(TAG, "体をWタップした");
                ChrTouch = true;
                ((LAppModel) this.models.get(i)).startRandomMotion("tap_body", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("hair1", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair2", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair3", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair4", x, y)) {
                Log.d(TAG, "横髪をＷタップした");
                ChrTouch = true;
            } else if (LAppDefine.DEBUG_LOG) {
                Log.d(TAG, "Wタップした");
            }
            if (ChrTouch) {
                LiveWallpaper.TouchReset();
                if (LiveWallpaper.wakeup) {
                    wakeup();
                }
            }
            i++;
        }
        return true;
    }

    public void flickEvent(float x, float y) {
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "flick x:" + x + " y:" + y);
        }
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "フリックイベント");
        }
        int i = 0;
        while (i < this.models.size()) {
            if (((LAppModel) this.models.get(i)).hitTest("face", x, y)) {
                if (LiveWallpaper.flick == 1) {
                    Log.d(TAG, "顔をフリックした　上");
                    ((LAppModel) this.models.get(i)).startRandomMotion("face_flick", 4);
                } else if (LiveWallpaper.flick == 3) {
                    Log.d(TAG, "顔をフリックした　右");
                    ((LAppModel) this.models.get(i)).startRandomMotion("face_flick_R", 2);
                } else if (LiveWallpaper.flick == 4) {
                    Log.d(TAG, "顔をフリックした　左");
                    ((LAppModel) this.models.get(i)).startRandomMotion("face_flick_L", 3);
                } else {
                    Log.d(TAG, "顔をフリックした　上方向以外");
                    ((LAppModel) this.models.get(i)).startRandomMotion("face_flick2", 2);
                }
            } else if (((LAppModel) this.models.get(i)).hitTest("arm_r", x, y) || ((LAppModel) this.models.get(i)).hitTest("arm_l", x, y)) {
                Log.d(TAG, "腕をフリックした");
                ((LAppModel) this.models.get(i)).startRandomMotion("flick_arm", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("wear", x, y)) {
                Log.d(TAG, "えりをフリックした " + LiveWallpaper.flick);
                if (LiveWallpaper.emotion < 0) {
                    ((LAppModel) this.models.get(i)).startRandomMotion("iya_iya2", 3);
                } else if (LiveWallpaper.flick != 1 && LiveWallpaper.emotion >= -1) {
                    ((LAppModel) this.models.get(i)).startRandomMotion("flick_wear", 3);
                    LiveWallpaper.okoru = true;
                    LiveWallpaper.emotion--;
                }
            } else if (((LAppModel) this.models.get(i)).hitTest("bust", x, y)) {
                if (LAppDefine.DEBUG_LOG) {
                    Log.d(TAG, "胸をフリックした");
                }
                ((LAppModel) this.models.get(i)).setExpression("b01");
                LiveWallpaper.emotion--;
                this.bustlevel = 1;
                LiveWallpaper.afterTime = System.currentTimeMillis();
            } else if (((LAppModel) this.models.get(i)).hitTest("body", x, y)) {
                if (LiveWallpaper.flick == 1) {
                    Log.d(TAG, "身体をフリックした　上");
                    ((LAppModel) this.models.get(i)).startRandomMotion("drag", 3);
                }
            } else if (((LAppModel) this.models.get(i)).hitTest("skirt", x, y)) {
                Log.d(TAG, "スカートをフリックした");
                if (LiveWallpaper.emotion < 0) {
                    ((LAppModel) this.models.get(i)).startRandomMotion("iya_iya", 3);
                } else if (LiveWallpaper.flick != 2 && LiveWallpaper.emotion >= -1) {
                    ((LAppModel) this.models.get(i)).startRandomMotion("skirt_flick", 2);
                    LiveWallpaper.okoru = true;
                    LiveWallpaper.emotion--;
                }
            } else if (((LAppModel) this.models.get(i)).hitTest("hair1", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair2", x, y)) {
                Log.d(TAG, "右髪をフリックした");
                ((LAppModel) this.models.get(i)).startRandomMotion("flick_hair_r", 2);
            } else if (((LAppModel) this.models.get(i)).hitTest("hair3", x, y) || ((LAppModel) this.models.get(i)).hitTest("hair4", x, y)) {
                Log.d(TAG, "左髪をフリックした");
                ((LAppModel) this.models.get(i)).startRandomMotion("flick_hair_l", 2);
            }
            i++;
        }
    }

    public void TurnEvent(float x, float y) {
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "Turn x:" + x + " y:" + y);
        }
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "ターンイベント    emotion = " + LiveWallpaper.emotion);
        }
        for (int i = 0; i < this.models.size(); i++) {
            if (((LAppModel) this.models.get(i)).hitTest("head", x, y)) {
                Log.d(TAG, "頭をフリックした");
                if (LiveWallpaper.emotion >= 2) {
                    ((LAppModel) this.models.get(i)).startRandomMotion("head_nade", 2);
                    LiveWallpaper.emotion++;
                } else {
                    ((LAppModel) this.models.get(i)).startRandomMotion("flick_head", 2);
                    LiveWallpaper.emotion++;
                }
            }
        }
    }

    public boolean HeadNadeEvent(float x, float y) {
        for (int i = 0; i < this.models.size(); i++) {
            Log.d(TAG, "なでるイベント " + x + "  " + y);
            if (((LAppModel) this.models.get(i)).hitTest("head", x, y)) {
                Log.d(TAG, "頭なでるイベント発生  " + x + "  " + y);
                ((LAppModel) this.models.get(i)).startRandomMotion("head_nade", 2);
            }
        }
        return true;
    }

    public void maxScaleEvent() {
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "画面の最大化イベント");
        }
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).startRandomMotion("pinch_in", 2);
        }
    }

    public void minScaleEvent() {
        if (LAppDefine.DEBUG_LOG) {
            Log.d(TAG, "画面の最小化イベント");
        }
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).startRandomMotion("pinch_out", 2);
        }
    }

    public void shakeEvent() {
        Log.d(TAG, "シェイクイベント");
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).startRandomMotion("shake", 3);
        }
    }

    public void JuudenEvent() {
        Log.d(TAG, "プラグ接続イベント " + LiveWallpaper.plugged);
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).startRandomMotion("connect", 3);
        }
    }

    public void setDrag(float x, float y) {
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setDrag(x, y);
        }
    }

    public boolean TouchUpEvent() {
        Log.d("TouchUpEvent()　", "タップ終了した  sleep = " + LiveWallpaper.sleep);
        ChrTouch = false;
        for (int i = 0; i < this.models.size(); i++) {
        }
        return true;
    }

    public boolean TouchUpEvent(float x, float y) {
        Log.d("TouchUpEvent() ２", "TouchUpEvent  タップ終了した  " + x + " " + y);
        ChrTouch = false;
        for (int i = 0; i < this.models.size(); i++) {
            if (((LAppModel) this.models.get(i)).hitTest("head", x, y)) {
                Log.d(TAG, "頭をタップ終了した");
            }
        }
        return true;
    }

    public void BustUpEvent_pos() {
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpression("b00");
        }
    }

    public void BustUpEvent00() {
        for (int i = 0; i < this.models.size(); i++) {
            Log.d(TAG, "b00  胸位置00");
            ((LAppModel) this.models.get(i)).setExpression("b00");
        }
    }

    public void BustUpEvent01() {
        for (int i = 0; i < this.models.size(); i++) {
            Log.d(TAG, "b01  胸をあげる+1.0");
            ((LAppModel) this.models.get(i)).setExpression("b01");
        }
    }

    public void BustUpEvent02() {
        for (int i = 0; i < this.models.size(); i++) {
            Log.d(TAG, "b02  胸をあげる+0.5");
            ((LAppModel) this.models.get(i)).setExpression("b02");
        }
    }

    public void BustUpEvent03() {
        for (int i = 0; i < this.models.size(); i++) {
            ((LAppModel) this.models.get(i)).setExpression("b03");
        }
    }

    public void preview_on() {
        Log.d(TAG, "プレビュー時に再生するモーション " + LiveWallpaper.level + "  好感度 " + LiveWallpaper.emotion);
        Log.d(TAG, "プレビュー");
        ((LAppModel) this.models.get(23)).startRandomMotion("start01", 3);
    }
}
