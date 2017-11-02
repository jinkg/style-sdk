package jp.live2d.framework;

import java.util.ArrayList;

import jp.live2d.ALive2DModel;
import jp.live2d.id.PartsDataID;

/* compiled from: L2DPose */
class L2DPartsParam {
    String id;
    ArrayList<L2DPartsParam> link = null;
    int paramIndex = -1;
    int partsIndex = -1;

    public L2DPartsParam(String id) {
        this.id = id;
    }

    public void initIndex(ALive2DModel model) {
        this.paramIndex = model.getParamIndex("VISIBLE:" + this.id);
        this.partsIndex = model.getPartsDataIndex(PartsDataID.getID(this.id));
        model.setParamFloat(this.paramIndex, 1.0f);
    }
}
