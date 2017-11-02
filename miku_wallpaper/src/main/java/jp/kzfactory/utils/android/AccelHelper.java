package jp.kzfactory.utils.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Display;

import jp.live2d.util.UtSystem;

import static android.view.Surface.ROTATION_0;

public class AccelHelper {
    private static float acceleration_x = 0.0f;
    private static float acceleration_y = 0.0f;
    private static float acceleration_z = 0.0f;
    private static float dst_acceleration_x = 0.0f;
    private static float dst_acceleration_y = 0.0f;
    private static float dst_acceleration_z = 0.0f;
    private static float lastMove;
    private static long lastTimeMSec = -1;
    private static float last_dst_acceleration_x = 0.0f;
    private static float last_dst_acceleration_y = 0.0f;
    private static float last_dst_acceleration_z = 0.0f;
    private float[] accel = new float[3];
    private final Sensor accelerometer;
    private float[] accelerometerValues = new float[3];
    private final Activity activity;
    private float[] geomagneticMatrix = new float[3];
    private final Sensor magneticField;
    private MySensorListener sensorListener = new MySensorListener();
    private SensorManager sensorManager;
    private boolean sensorReady;

    private static class DispRotateGetter {

        private interface IDispRotateGetter {
            int getRotate(Display display);
        }

        private static class DispRotateGetterV1 implements IDispRotateGetter {
            private DispRotateGetterV1() {
            }

            public int getRotate(Display d) {
                return d.getOrientation() == ROTATION_0 ? 0 : 1;
            }
        }

        private static class DispRotateGetterV8 implements IDispRotateGetter {
            private DispRotateGetterV8() {
            }

            public int getRotate(Display d) {
                return d.getRotation();
            }
        }

        private DispRotateGetter() {
        }

        private static IDispRotateGetter getInstance() {
            if (VERSION.SDK_INT >= 8) {
                return new DispRotateGetterV8();
            }
            return new DispRotateGetterV1();
        }
    }

    private class MySensorListener implements SensorEventListener {
        private MySensorListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent e) {
            switch (e.sensor.getType()) {
                case 1:
                    AccelHelper.this.accelerometerValues = (float[]) e.values.clone();
                    break;
                case 2:
                    AccelHelper.this.geomagneticMatrix = (float[]) e.values.clone();
                    AccelHelper.this.sensorReady = true;
                    break;
            }
            if (AccelHelper.this.geomagneticMatrix != null && AccelHelper.this.accelerometerValues != null && AccelHelper.this.sensorReady) {
                AccelHelper.this.sensorReady = false;
                SensorManager.getRotationMatrix(new float[16], new float[16], AccelHelper.this.accelerometerValues, AccelHelper.this.geomagneticMatrix);
                int dr = AccelHelper.getDispRotation(AccelHelper.this.activity);
                float x = 0.0f;
                float y = 0.0f;
                float z = 0.0f;
                if (dr == 0) {
                    x = (-AccelHelper.this.accelerometerValues[0]) / 9.80665f;
                    y = (-AccelHelper.this.accelerometerValues[1]) / 9.80665f;
                    z = (-AccelHelper.this.accelerometerValues[2]) / 9.80665f;
                } else if (dr == 1) {
                    x = AccelHelper.this.accelerometerValues[1] / 9.80665f;
                    y = (-AccelHelper.this.accelerometerValues[0]) / 9.80665f;
                    z = (-AccelHelper.this.accelerometerValues[2]) / 9.80665f;
                } else if (dr == 2) {
                    x = AccelHelper.this.accelerometerValues[0] / 9.80665f;
                    y = AccelHelper.this.accelerometerValues[1] / 9.80665f;
                    z = (-AccelHelper.this.accelerometerValues[2]) / 9.80665f;
                } else if (dr == 3) {
                    x = (-AccelHelper.this.accelerometerValues[1]) / 9.80665f;
                    y = AccelHelper.this.accelerometerValues[0] / 9.80665f;
                    z = (-AccelHelper.this.accelerometerValues[2]) / 9.80665f;
                }
                AccelHelper.this.setCurAccel(x, y, z);
            }
        }
    }

    public AccelHelper(Activity activity) {
        this.sensorManager = (SensorManager) activity.getSystemService("sensor");
        Log.d("AccelHelper", "AccelHelper");
        this.activity = activity;
        if (this.sensorManager.getSensorList(1).size() <= 0 || this.sensorManager.getSensorList(2).size() <= 0) {
            this.accelerometer = null;
            this.magneticField = null;
        } else {
            this.accelerometer = (Sensor) this.sensorManager.getSensorList(1).get(0);
            this.magneticField = (Sensor) this.sensorManager.getSensorList(2).get(0);
        }
        start();
    }

    public float getShake() {
        return lastMove;
    }

    public void resetShake() {
        lastMove = 0.0f;
    }

    public void start() {
        try {
            if (this.accelerometer != null && this.magneticField != null) {
                this.sensorManager.registerListener(this.sensorListener, this.magneticField, 3);
                this.sensorManager.registerListener(this.sensorListener, this.accelerometer, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.sensorManager.unregisterListener(this.sensorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getDispRotation(Activity act) {
        return DispRotateGetter.getInstance().getRotate(act.getWindowManager().getDefaultDisplay());
    }

    public void setCurAccel(float a1, float a2, float a3) {
        dst_acceleration_x = a1;
        dst_acceleration_y = a2;
        dst_acceleration_z = a3;
        lastMove = (lastMove * 0.7f) + (0.3f * ((fabs(dst_acceleration_x - last_dst_acceleration_x) + fabs(dst_acceleration_y - last_dst_acceleration_y)) + fabs(dst_acceleration_z - last_dst_acceleration_z)));
        last_dst_acceleration_x = dst_acceleration_x;
        last_dst_acceleration_y = dst_acceleration_y;
        last_dst_acceleration_z = dst_acceleration_z;
    }

    public void update() {
        Log.v("AH update(1)", "accelHelper.update();");
        float dx = dst_acceleration_x - acceleration_x;
        float dy = dst_acceleration_y - acceleration_y;
        float dz = dst_acceleration_z - acceleration_z;
        if (dx > 0.04f) {
            dx = 0.04f;
        }
        if (dx < -0.04f) {
            dx = -0.04f;
        }
        if (dy > 0.04f) {
            dy = 0.04f;
        }
        if (dy < -0.04f) {
            dy = -0.04f;
        }
        if (dz > 0.04f) {
            dz = 0.04f;
        }
        if (dz < -0.04f) {
            dz = -0.04f;
        }
        acceleration_x += dx;
        acceleration_y += dy;
        acceleration_z += dz;
        Log.v("AH update(2)", "accelHelper.update();");
        long time = UtSystem.getUserTimeMSec();
        long diff = time - lastTimeMSec;
        lastTimeMSec = time;
        float scale = ((0.2f * ((float) diff)) * 60.0f) / 1000.0f;
        if (scale > 0.5f) {
            scale = 0.5f;
        }
        Log.v("AH update()", "accelHelper.update();");
        this.accel[0] = (acceleration_x * scale) + (this.accel[0] * (1.0f - scale));
        this.accel[1] = (acceleration_y * scale) + (this.accel[1] * (1.0f - scale));
        this.accel[2] = (acceleration_z * scale) + (this.accel[2] * (1.0f - scale));
    }

    private float fabs(float v) {
        return v > 0.0f ? v : -v;
    }

    public float getAccelX() {
        return this.accel[0];
    }

    public float getAccelY() {
        return this.accel[1];
    }

    public float getAccelZ() {
        return this.accel[2];
    }
}
