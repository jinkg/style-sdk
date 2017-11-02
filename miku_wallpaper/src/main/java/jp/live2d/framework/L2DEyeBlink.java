package jp.live2d.framework;

import jp.live2d.ALive2DModel;
import jp.live2d.util.UtSystem;

public class L2DEyeBlink {
    int blinkIntervalMsec = 4000;
    boolean closeIfZero = true;
    int closedMotionMsec = 50;
    int closingMotionMsec = 100;
    String eyeID_L = L2DStandardID.PARAM_EYE_L_OPEN;
    String eyeID_R = L2DStandardID.PARAM_EYE_R_OPEN;
    EYE_STATE eyeState = EYE_STATE.STATE_FIRST;
    long nextBlinkTime;
    int openingMotionMsec = 150;
    long stateStartTime;

    enum EYE_STATE {
        STATE_FIRST,
        STATE_INTERVAL,
        STATE_CLOSING,
        STATE_CLOSED,
        STATE_OPENING
    }

    public long calcNextBlink() {
        return (long) (((double) UtSystem.getUserTimeMSec()) + (((double) ((this.blinkIntervalMsec * 2) - 1)) * Math.random()));
    }

    public void setInterval(int blinkIntervalMsec) {
        this.blinkIntervalMsec = blinkIntervalMsec;
    }

    public void setEyeMotion(int closingMotionMsec, int closedMotionMsec, int openingMotionMsec) {
        this.closingMotionMsec = closingMotionMsec;
        this.closedMotionMsec = closedMotionMsec;
        this.openingMotionMsec = openingMotionMsec;
    }

    public void updateParam(ALive2DModel model) {
        float eyeParamValue;
        long time = UtSystem.getUserTimeMSec();
        float t;
        switch (this.eyeState) {
            case STATE_CLOSING:
                t = ((float) (time - this.stateStartTime)) / ((float) this.closingMotionMsec);
                if (t >= 1.0f) {
                    t = 1.0f;
                    this.eyeState = EYE_STATE.STATE_CLOSED;
                    this.stateStartTime = time;
                }
                eyeParamValue = 1.0f - t;
                break;
            case STATE_CLOSED:
                if (((float) (time - this.stateStartTime)) / ((float) this.closedMotionMsec) >= 1.0f) {
                    this.eyeState = EYE_STATE.STATE_OPENING;
                    this.stateStartTime = time;
                }
                eyeParamValue = 0.0f;
                break;
            case STATE_OPENING:
                t = ((float) (time - this.stateStartTime)) / ((float) this.openingMotionMsec);
                if (t >= 1.0f) {
                    t = 1.0f;
                    this.eyeState = EYE_STATE.STATE_INTERVAL;
                    this.nextBlinkTime = calcNextBlink();
                }
                eyeParamValue = t;
                break;
            case STATE_INTERVAL:
                if (this.nextBlinkTime < time) {
                    this.eyeState = EYE_STATE.STATE_CLOSING;
                    this.stateStartTime = time;
                }
                eyeParamValue = 1.0f;
                break;
            default:
                this.eyeState = EYE_STATE.STATE_INTERVAL;
                this.nextBlinkTime = calcNextBlink();
                eyeParamValue = 1.0f;
                break;
        }
        if (!this.closeIfZero) {
            eyeParamValue = -eyeParamValue;
        }
        model.setParamFloat(this.eyeID_L, eyeParamValue);
        model.setParamFloat(this.eyeID_R, eyeParamValue);
    }
}
