package com.yalin.wallpaper.forest.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class CalendarManager {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private double timer;
    private ArrayList<Trigger> triggers = new ArrayList<>();

    class C00321 implements Comparator<Trigger> {
        C00321() {
        }

        public int compare(Trigger t1, Trigger t2) {
            return t1.datetime.compareTo(t2.datetime);
        }
    }

    class Trigger {
        public Runnable callback;
        public GregorianCalendar datetime;
        public int repeat;

        public Trigger(GregorianCalendar cal, Runnable run, int repeat) {
            this.datetime = cal;
            this.callback = run;
            this.repeat = repeat;
            rotate();
        }

        public boolean rotate() {
            if (this.repeat != 1 && this.repeat != 2) {
                return false;
            }
            GregorianCalendar now = new GregorianCalendar();
            while (this.datetime.before(now)) {
                this.datetime.add(this.repeat, 1);
            }
            return true;
        }

        public void run() {
            this.callback.run();
        }
    }

    public void addTrigger(String datetime, int repeat, Runnable callback) {
        try {
            Date date = this.dateFormat.parse(datetime);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            addTrigger(new Trigger(cal, callback, repeat));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void addTrigger(GregorianCalendar cal, int repeat, Runnable callback) {
        addTrigger(new Trigger(cal, callback, repeat));
    }

    private void addTrigger(Trigger trigger) {
        if (this.triggers.size() > 0) {
            Iterator it = this.triggers.iterator();
            while (it.hasNext()) {
                Trigger t = (Trigger) it.next();
                if (trigger.datetime.before(t.datetime)) {
                    this.triggers.add(this.triggers.indexOf(t), trigger);
                    return;
                }
            }
        }
        this.triggers.add(trigger);
    }

    public void update(double dt) {
        double d = this.timer - dt;
        this.timer = d;
        if (d <= 0.0d) {
            this.timer = 10.0d;
            checkTriggers();
            reorder();
        }
    }

    private void checkTriggers() {
        GregorianCalendar now = new GregorianCalendar();
        ArrayList<Trigger> dead = new ArrayList<>();
        Iterator it = this.triggers.iterator();
        while (it.hasNext()) {
            Trigger t = (Trigger) it.next();
            if (!now.after(t.datetime)) {
                break;
            }
            t.run();
            if (!t.rotate()) {
                dead.add(t);
            }
        }
        it = dead.iterator();
        while (it.hasNext()) {
            this.triggers.remove(it.next());
        }
    }

    private void reorder() {
        Collections.sort(this.triggers, new C00321());
    }
}
