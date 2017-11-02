package jp.live2d.framework;

import java.io.InputStream;
import java.util.ArrayList;

import jp.live2d.ALive2DModel;
import jp.live2d.physics.PhysicsHair;
import jp.live2d.physics.PhysicsHair.Src;
import jp.live2d.physics.PhysicsHair.Target;
import jp.live2d.util.Json;
import jp.live2d.util.Json.Value;
import jp.live2d.util.UtDebug;
import jp.live2d.util.UtFile;
import jp.live2d.util.UtSystem;

public class L2DPhysics {
    private ArrayList<PhysicsHair> physicsList = new ArrayList();
    private long startTimeMSec = UtSystem.getUserTimeMSec();

    public void updateParam(ALive2DModel model) {
        long timeMSec = UtSystem.getUserTimeMSec() - this.startTimeMSec;
        for (int i = 0; i < this.physicsList.size(); i++) {
            ((PhysicsHair) this.physicsList.get(i)).update(model, timeMSec);
        }
    }

    public static L2DPhysics load(InputStream in) throws Exception {
        return load(UtFile.load(in));
    }

    public static L2DPhysics load(byte[] buf) throws Exception {
        L2DPhysics ret = new L2DPhysics();
        Value params = Json.parseFromBytes(buf).get("physics_hair");
        int paramNum = params.getVector(null).size();
        for (int i = 0; i < paramNum; i++) {
            int j;
            Value param = params.get(i);
            PhysicsHair physics = new PhysicsHair();
            Value setup = param.get("setup");
            physics.setup(setup.get("length").toFloat(), setup.get("regist").toFloat(), setup.get("mass").toFloat());
            Value srcList = param.get("src");
            int srcNum = srcList.getVector(null).size();
            for (j = 0; j < srcNum; j++) {
                Value src = srcList.get(j);
                String id = src.get("id").toString();
                Src type = Src.SRC_TO_X;
                String typeStr = src.get("ptype").toString();
                if (typeStr.equals("x")) {
                    type = Src.SRC_TO_X;
                } else if (typeStr.equals("y")) {
                    type = Src.SRC_TO_Y;
                } else if (typeStr.equals("angle")) {
                    type = Src.SRC_TO_G_ANGLE;
                } else {
                    UtDebug.error("live2d", "Invalid parameter:PhysicsHair.Src");
                }
                physics.addSrcParam(type, id, src.get("scale").toFloat(), src.get("weight").toFloat());
            }
            Value targetList = param.get("targets");
            int targetNum = targetList.getVector(null).size();
            for (j = 0; j < targetNum; j++) {
                Value target = targetList.get(j);
                String id = target.get("id").toString();
                Target type2 = Target.TARGET_FROM_ANGLE;
                String typeStr = target.get("ptype").toString();
                if (typeStr.equals("angle")) {
                    type2 = Target.TARGET_FROM_ANGLE;
                } else if (typeStr.equals("angle_v")) {
                    type2 = Target.TARGET_FROM_ANGLE_V;
                } else {
                    UtDebug.error("live2d", "Invalid parameter:PhysicsHair.Target");
                }
                physics.addTargetParam(type2, id, target.get("scale").toFloat(), target.get("weight").toFloat());
            }
            ret.physicsList.add(physics);
        }
        return ret;
    }
}
