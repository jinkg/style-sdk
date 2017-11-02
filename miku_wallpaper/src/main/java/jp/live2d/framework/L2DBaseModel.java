package jp.live2d.framework;

import com.yalin.wallpaper.miku.LAppDefine;

import java.util.HashMap;
import java.util.Map;

import jp.live2d.ALive2DModel;
import jp.live2d.Live2D;
import jp.live2d.motion.AMotion;
import jp.live2d.motion.Live2DMotion;
import jp.live2d.motion.MotionQueueManager;

public class L2DBaseModel {
    protected float accAlpha = 0.0f;
    protected float accelX = 0.0f;
    protected float accelY = 0.0f;
    protected float accelZ = 0.0f;
    protected float alpha = 1.0f;
    protected boolean debugMode = false;
    protected float dragX = 0.0f;
    protected float dragY = 0.0f;
    protected L2DMotionManager expressionManager = new L2DMotionManager();
    protected Map<String, AMotion> expressions = new HashMap();
    protected L2DEyeBlink eyeBlink;
    protected boolean initialized = false;
    protected boolean lipSync = false;
    protected float lipSyncValue;
    protected ALive2DModel live2DModel = null;
    protected L2DMotionManager mainMotionManager = new L2DMotionManager();
    protected L2DModelMatrix modelMatrix = null;
    protected Map<String, AMotion> motions = new HashMap();
    protected L2DPhysics physics;
    protected L2DPose pose;
    protected long startTimeMSec;
    protected boolean updating = false;

    public L2DModelMatrix getModelMatrix() {
        return this.modelMatrix;
    }

    public void setAlpha(float a) {
        if (((double) a) > 0.999d) {
            a = 1.0f;
        }
        if (((double) a) < 0.001d) {
            a = 0.0f;
        }
        this.alpha = a;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean v) {
        this.initialized = v;
    }

    public boolean isUpdating() {
        return this.updating;
    }

    public void setUpdating(boolean v) {
        this.updating = v;
    }

    public ALive2DModel getLive2DModel() {
        return this.live2DModel;
    }

    public void setLipSync(boolean v) {
        this.lipSync = v;
    }

    public void setLipSyncValue(float v) {
        this.lipSyncValue = v;
    }

    public void setAccel(float x, float y, float z) {
        this.accelX = x;
        this.accelY = y;
        this.accelZ = z;
    }

    public void setDrag(float x, float y) {
        this.dragX = x;
        this.dragY = y;
    }

    public MotionQueueManager getMainMotionManager() {
        return this.mainMotionManager;
    }

    public MotionQueueManager getExpressionManager() {
        return this.expressionManager;
    }

    public void loadModelData(String path) {
        if (this.live2DModel != null) {
            this.live2DModel.deleteTextures();
        }
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load model : " + path);
        }
        this.live2DModel = pm.loadLive2DModel(path);
        this.live2DModel.saveParam();
        if (Live2D.getError() != Live2D.L2D_NO_ERROR) {
            pm.log("Error : Failed to loadModelData().");
            return;
        }
        this.modelMatrix = new L2DModelMatrix(this.live2DModel.getCanvasWidth(), this.live2DModel.getCanvasHeight());
        this.modelMatrix.setWidth(LAppDefine.VIEW_LOGICAL_MAX_RIGHT_BG2);
        this.modelMatrix.setCenterPosition(0.0f, 0.0f);
    }

    public void loadTexture(int no, String path) {
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load Texture : " + path);
        }
        pm.loadTexture(this.live2DModel, no, path);
    }

    public AMotion loadMotion(String name, String path) {
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load Motion : " + path);
        }
        Live2DMotion motion = Live2DMotion.loadMotion(pm.loadBytes(path));
        if (name != null) {
            this.motions.put(name, motion);
        }
        return motion;
    }

    public void loadExpression(String name, String path) {
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load Expression : " + path);
        }
        try {
            this.expressions.put(name, L2DExpressionMotion.loadJson(pm.loadBytes(path)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPose(String path) {
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load Pose : " + path);
        }
        try {
            this.pose = L2DPose.load(pm.loadBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPhysics(String path) {
        IPlatformManager pm = Live2DFramework.getPlatformManager();
        if (this.debugMode) {
            pm.log("Load Physics : " + path);
        }
        try {
            this.physics = L2DPhysics.load(pm.loadBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hitTestSimple(String drawID, float testX, float testY) {
        if (this.alpha < 1.0f) {
            return false;
        }
        int drawIndex = this.live2DModel.getDrawDataIndex(drawID);
        if (drawIndex < 0) {
            return false;
        }
        float[] points = this.live2DModel.getTransformedPoints(drawIndex);
        float left = this.live2DModel.getCanvasWidth();
        float right = 0.0f;
        float top = this.live2DModel.getCanvasHeight();
        float bottom = 0.0f;
        for (int j = 0; j < points.length; j += 2) {
            float x = points[j];
            float y = points[j + 1];
            if (x < left) {
                left = x;
            }
            if (x > right) {
                right = x;
            }
            if (y < top) {
                top = y;
            }
            if (y > bottom) {
                bottom = y;
            }
        }
        float tx = this.modelMatrix.invertTransformX(testX);
        float ty = this.modelMatrix.invertTransformY(testY);
        return left <= tx && tx <= right && top <= ty && ty <= bottom;
    }
}
