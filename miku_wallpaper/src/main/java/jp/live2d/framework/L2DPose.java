package jp.live2d.framework;

import java.io.InputStream;
import java.util.ArrayList;

import jp.live2d.ALive2DModel;
import jp.live2d.util.Json;
import jp.live2d.util.Json.Value;
import jp.live2d.util.UtFile;
import jp.live2d.util.UtSystem;

public class L2DPose {
    private ALive2DModel lastModel = null;
    private long lastTime = 0;
    private ArrayList<L2DPartsParam[]> partsGroups = new ArrayList();

    public void updateParam(ALive2DModel model) {
        if (model != null) {
            if (!model.equals(this.lastModel)) {
                initParam(model);
            }
            this.lastModel = model;
            long curTime = UtSystem.getTimeMSec();
            float deltaTimeSec = this.lastTime == 0 ? 0.0f : ((float) (curTime - this.lastTime)) / 1000.0f;
            this.lastTime = curTime;
            if (deltaTimeSec < 0.0f) {
                deltaTimeSec = 0.0f;
            }
            for (int i = 0; i < this.partsGroups.size(); i++) {
                normalizePartsOpacityGroup(model, (L2DPartsParam[]) this.partsGroups.get(i), deltaTimeSec);
                copyOpacityOtherParts(model, (L2DPartsParam[]) this.partsGroups.get(i));
            }
        }
    }

    public void initParam(ALive2DModel model) {
        if (model != null) {
            for (int i = 0; i < this.partsGroups.size(); i++) {
                L2DPartsParam[] partsGroup = (L2DPartsParam[]) this.partsGroups.get(i);
                for (int j = 0; j < partsGroup.length; j++) {
                    partsGroup[j].initIndex(model);
                    int partsIndex = partsGroup[j].partsIndex;
                    int paramIndex = partsGroup[j].paramIndex;
                    if (partsIndex >= 0) {
                        float f;
                        boolean v = model.getParamFloat(paramIndex) != 0.0f;
                        if (v) {
                            f = 1.0f;
                        } else {
                            f = 0.0f;
                        }
                        model.setPartsOpacity(partsIndex, f);
                        if (v) {
                            f = 1.0f;
                        } else {
                            f = 0.0f;
                        }
                        model.setParamFloat(paramIndex, f);
                        if (partsGroup[j].link != null) {
                            for (int k = 0; k < partsGroup[j].link.size(); k++) {
                                ((L2DPartsParam) partsGroup[j].link.get(k)).initIndex(model);
                            }
                        }
                    }
                }
            }
        }
    }

    public void normalizePartsOpacityGroup(ALive2DModel model, L2DPartsParam[] partsGroup, float deltaTimeSec) {
        int i;
        int visibleParts = -1;
        float visibleOpacity = 1.0f;
        for (i = 0; i < partsGroup.length; i++) {
            int partsIndex = partsGroup[i].partsIndex;
            int paramIndex = partsGroup[i].paramIndex;
            if (partsIndex >= 0 && model.getParamFloat(paramIndex) != 0.0f) {
                if (visibleParts >= 0) {
                    break;
                }
                visibleParts = i;
                visibleOpacity = model.getPartsOpacity(partsIndex) + (deltaTimeSec / 0.5f);
                if (visibleOpacity > 1.0f) {
                    visibleOpacity = 1.0f;
                }
            }
        }
        if (visibleParts < 0) {
            visibleParts = 0;
            visibleOpacity = 1.0f;
        }
        for (i = 0; i < partsGroup.length; i++) {
            int partsIndex = partsGroup[i].partsIndex;
            if (partsIndex >= 0) {
                if (visibleParts == i) {
                    model.setPartsOpacity(partsIndex, visibleOpacity);
                } else {
                    float a1;
                    float opacity = model.getPartsOpacity(partsIndex);
                    if (visibleOpacity < 0.5f) {
                        a1 = (((0.5f - 1.0f) * visibleOpacity) / 0.5f) + 1.0f;
                    } else {
                        a1 = ((1.0f - visibleOpacity) * 0.5f) / (1.0f - 0.5f);
                    }
                    if ((1.0f - a1) * (1.0f - visibleOpacity) > 0.15f) {
                        a1 = 1.0f - (0.15f / (1.0f - visibleOpacity));
                    }
                    if (opacity > a1) {
                        opacity = a1;
                    }
                    model.setPartsOpacity(partsIndex, opacity);
                }
            }
        }
    }

    public void copyOpacityOtherParts(ALive2DModel model, L2DPartsParam[] partsGroup) {
        for (L2DPartsParam partsParam : partsGroup) {
            if (partsParam.link != null && partsParam.partsIndex >= 0) {
                float opacity = model.getPartsOpacity(partsParam.partsIndex);
                for (int i_link = 0; i_link < partsParam.link.size(); i_link++) {
                    L2DPartsParam linkParts = (L2DPartsParam) partsParam.link.get(i_link);
                    if (linkParts.partsIndex >= 0) {
                        model.setPartsOpacity(linkParts.partsIndex, opacity);
                    }
                }
            }
        }
    }

    public static L2DPose load(InputStream in) throws Exception {
        return load(UtFile.load(in));
    }

    public static L2DPose load(byte[] buf) throws Exception {
        L2DPose ret = new L2DPose();
        Value poseListInfo = Json.parseFromBytes(buf).get("parts_visible");
        int poseNum = poseListInfo.getVector(null).size();
        for (int i_pose = 0; i_pose < poseNum; i_pose++) {
            Value idListInfo = poseListInfo.get(i_pose).get("group");
            int idNum = idListInfo.getVector(null).size();
            L2DPartsParam[] partsGroup = new L2DPartsParam[idNum];
            for (int i_group = 0; i_group < idNum; i_group++) {
                Value partsInfo = idListInfo.get(i_group);
                L2DPartsParam parts = new L2DPartsParam(partsInfo.get("id").toString());
                partsGroup[i_group] = parts;
                if (partsInfo.get("link") != null) {
                    Value linkListInfo = partsInfo.get("link");
                    int linkNum = linkListInfo.getVector(null).size();
                    parts.link = new ArrayList();
                    for (int i_link = 0; i_link < linkNum; i_link++) {
                        parts.link.add(new L2DPartsParam(linkListInfo.get(i_link).toString()));
                    }
                }
            }
            ret.partsGroups.add(partsGroup);
        }
        return ret;
    }
}
