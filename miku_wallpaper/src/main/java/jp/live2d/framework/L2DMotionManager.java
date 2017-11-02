package jp.live2d.framework;

import jp.live2d.ALive2DModel;
import jp.live2d.motion.AMotion;
import jp.live2d.motion.MotionQueueManager;

public class L2DMotionManager extends MotionQueueManager {
    private int currentPriority;
    private int reservePriority;

    public int getCurrentPriority() {
        return this.currentPriority;
    }

    public int getReservePriority() {
        return this.reservePriority;
    }

    public boolean reserveMotion(int priority) {
        if (this.reservePriority >= priority || this.currentPriority >= priority) {
            return false;
        }
        this.reservePriority = priority;
        return true;
    }

    public void setReservePriority(int val) {
        this.reservePriority = val;
    }

    public boolean updateParam(ALive2DModel model) {
        boolean updated = super.updateParam(model);
        if (isFinished()) {
            this.currentPriority = 0;
        }
        return updated;
    }

    public int startMotionPrio(AMotion motion, int priority) {
        if (priority == this.reservePriority) {
            this.reservePriority = 0;
        }
        this.currentPriority = priority;
        return super.startMotion(motion, false);
    }
}
