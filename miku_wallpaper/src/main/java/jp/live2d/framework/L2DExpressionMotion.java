package jp.live2d.framework;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import jp.live2d.ALive2DModel;
import jp.live2d.motion.AMotion;
import jp.live2d.motion.MotionQueueManager.MotionQueueEnt;
import jp.live2d.util.Json;
import jp.live2d.util.Json.Value;
import jp.live2d.util.UtFile;

public class L2DExpressionMotion extends AMotion {
    private static final String EXPRESSION_DEFAULT = "DEFAULT";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_MULT = 2;
    public static final int TYPE_SET = 0;
    private ArrayList<L2DExpressionParam> paramList = new ArrayList();

    public static class L2DExpressionParam {
        public String id;
        public int type;
        public float value;
    }

    public void updateParamExe(ALive2DModel model, long timeMSec, float weight, MotionQueueEnt motionQueueEnt) {
        for (int i = this.paramList.size() - 1; i >= 0; i--) {
            L2DExpressionParam param = (L2DExpressionParam) this.paramList.get(i);
            if (param.type == 1) {
                model.addToParamFloat(param.id, param.value, weight);
            } else if (param.type == 2) {
                model.multParamFloat(param.id, param.value, weight);
            } else if (param.type == 0) {
                model.setParamFloat(param.id, param.value, weight);
            }
        }
    }

    public static L2DExpressionMotion loadJson(InputStream in) throws Exception {
        return loadJson(UtFile.load(in));
    }

    public static L2DExpressionMotion loadJson(byte[] buf) throws Exception {
        L2DExpressionMotion ret = new L2DExpressionMotion();
        Value json = Json.parseFromBytes(buf);
        ret.setFadeIn(json.get("fade_in").toInt(1000));
        ret.setFadeOut(json.get("fade_out").toInt(1000));
        if (json.get("params") != null) {
            Value params = json.get("params");
            int paramNum = params.getVector(null).size();
            ret.paramList = new ArrayList(paramNum);
            for (int i = 0; i < paramNum; i++) {
                int calcTypeInt;
                Value param = params.get(i);
                String paramID = param.get("id").toString();
                float value = param.get("val").toFloat();
                String calc = param.get("calc") != null ? param.get("calc").toString() : "add";
                if (calc.equals("add")) {
                    calcTypeInt = 1;
                } else if (calc.equals("mult")) {
                    calcTypeInt = 2;
                } else if (calc.equals("set")) {
                    calcTypeInt = 0;
                } else {
                    calcTypeInt = 1;
                }
                if (calcTypeInt == 1) {
                    value -= param.get("def") == null ? 0.0f : param.get("def").toFloat();
                } else if (calcTypeInt == 2) {
                    float defaultValue = param.get("def") == null ? 1.0f : param.get("def").toFloat(0.0f);
                    if (defaultValue == 0.0f) {
                        defaultValue = 1.0f;
                    }
                    value /= defaultValue;
                }
                L2DExpressionParam item = new L2DExpressionParam();
                item.id = paramID;
                item.type = calcTypeInt;
                item.value = value;
                ret.paramList.add(item);
            }
        }
        return ret;
    }

    public static HashMap<String, AMotion> loadExpressionJsonV09(InputStream in) throws Exception {
        HashMap<String, AMotion> expressions = new HashMap();
        Value mo = Json.parseFromBytes(UtFile.load(in));
        Value defaultExpr = mo.get(EXPRESSION_DEFAULT);
        for (Object key : mo.keySet()) {
            if (!EXPRESSION_DEFAULT.equals(key)) {
                expressions.put((String) key, loadJsonV09(defaultExpr, mo.get((String) key)));
            }
        }
        return expressions;
    }

    private static L2DExpressionMotion loadJsonV09(Value defaultExpr, Value expr) {
        String id;
        L2DExpressionMotion ret = new L2DExpressionMotion();
        ret.setFadeIn(expr.get("FADE_IN").toInt(1000));
        ret.setFadeOut(expr.get("FADE_OUT").toInt(1000));
        Value defaultParams = defaultExpr.get("PARAMS");
        Value params = expr.get("PARAMS");
        Set<String> paramID = params.keySet();
        ArrayList<String> idList = new ArrayList();
        for (String id2 : paramID) {
            idList.add(id2);
        }
        for (int i = idList.size() - 1; i >= 0; i--) {
            String id2 = (String) idList.get(i);
            float value = params.get(id2).toFloat(0.0f) - defaultParams.get(id2).toFloat(0.0f);
            L2DExpressionParam param = new L2DExpressionParam();
            param.id = id2;
            param.type = 1;
            param.value = value;
            ret.paramList.add(param);
        }
        return ret;
    }
}
