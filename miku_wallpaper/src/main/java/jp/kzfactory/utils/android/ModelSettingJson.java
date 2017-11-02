package jp.kzfactory.utils.android;

import android.util.Log;

import java.io.InputStream;
import java.util.Map;

import jp.live2d.util.Json;
import jp.live2d.util.Json.Value;
import jp.live2d.util.UtFile;

public class ModelSettingJson implements ModelSetting {
    private static final String EXPRESSIONS = "expressions";
    private static final String FADE_IN = "fade_in";
    private static final String FADE_OUT = "fade_out";
    private static final String FILE = "file";
    private static final String HIT_AREAS = "hit_areas";
    private static final String ID = "id";
    private static final String INIT_PARAM = "init_param";
    private static final String INIT_PARTS_VISIBLE = "init_parts_visible";
    private static final String LAYOUT = "layout";
    private static final String MODEL = "model";
    private static final String MOTION_GROUPS = "motions";
    private static final String NAME = "name";
    private static final String PHYSICS = "physics";
    private static final String POSE = "pose";
    private static final String SOUND = "sound";
    private static final String TEXTURES = "textures";
    private static final String VALUE = "val";
    private Value json;

    public ModelSettingJson(InputStream in) {
        this.json = Json.parseFromBytes(UtFile.load(in));
    }

    public boolean existMotion(String name) {
        return this.json.get(MOTION_GROUPS).get(name) != null;
    }

    public boolean existMotionSound(String name, int n) {
        return this.json.get(MOTION_GROUPS).get(name).get(n).get(SOUND) != null;
    }

    public boolean existMotionFadeIn(String name, int n) {
        return this.json.get(MOTION_GROUPS).get(name).get(n).get(FADE_IN) != null;
    }

    public boolean existMotionFadeOut(String name, int n) {
        return this.json.get(MOTION_GROUPS).get(name).get(n).get(FADE_OUT) != null;
    }

    public String getModelName() {
        if (this.json.get(NAME) == null) {
            return null;
        }
        Log.d("モデル名を取得", "" + this.json.get(NAME));
        return this.json.get(NAME).toString();
    }

    public String getModelFile() {
        if (this.json.get(MODEL) == null) {
            return null;
        }
        Log.d("モデルファイルを取得", "" + this.json.get(MODEL));
        return this.json.get(MODEL).toString();
    }

    public int getTextureNum() {
        if (this.json.get(TEXTURES) == null) {
            return 0;
        }
        Log.d("テクスチャーナンバーを取得", "" + this.json.get(TEXTURES));
        return this.json.get(TEXTURES).getVector(null).size();
    }

    public String getTextureFile(int n) {
        Log.d("テクスチャーファイルを取得", "" + n);
        return this.json.get(TEXTURES).get(n).toString();
    }

    public int getHitAreasNum() {
        if (this.json.get(HIT_AREAS) == null) {
            return 0;
        }
        return this.json.get(HIT_AREAS).getVector(null).size();
    }

    public String getHitAreaID(int n) {
        return this.json.get(HIT_AREAS).get(n).get(ID).toString();
    }

    public String getHitAreaName(int n) {
        return this.json.get(HIT_AREAS).get(n).get(NAME).toString();
    }

    public String getPhysicsFile() {
        if (this.json.get(PHYSICS) == null) {
            return null;
        }
        return this.json.get(PHYSICS).toString();
    }

    public String getPoseFile() {
        if (this.json.get(POSE) == null) {
            return null;
        }
        return this.json.get(POSE).toString();
    }

    public int getMotionNum(String name) {
        if (existMotion(name)) {
            return this.json.get(MOTION_GROUPS).get(name).getVector(null).size();
        }
        return 0;
    }

    public String getMotionFile(String name, int n) {
        if (existMotion(name)) {
            return this.json.get(MOTION_GROUPS).get(name).get(n).get(FILE).toString();
        }
        return null;
    }

    public String getMotionSound(String name, int n) {
        if (existMotionSound(name, n)) {
            return this.json.get(MOTION_GROUPS).get(name).get(n).get(SOUND).toString();
        }
        return null;
    }

    public int getMotionFadeIn(String name, int n) {
        return !existMotionFadeIn(name, n) ? 1000 : this.json.get(MOTION_GROUPS).get(name).get(n).get(FADE_IN).toInt();
    }

    public int getMotionFadeOut(String name, int n) {
        return !existMotionFadeOut(name, n) ? 1000 : this.json.get(MOTION_GROUPS).get(name).get(n).get(FADE_OUT).toInt();
    }

    public String[] getMotionGroupNames() {
        String[] strArr = null;
        if (this.json.get(MOTION_GROUPS) != null) {
            Object[] keys = this.json.get(MOTION_GROUPS).getMap(null).keySet().toArray();
            if (keys.length != 0) {
                strArr = new String[keys.length];
                for (int i = 0; i < strArr.length; i++) {
                    strArr[i] = (String) keys[i];
                }
            }
        }
        return strArr;
    }

    public boolean getLayout(Map<String, Float> layout) {
        if (this.json.get(LAYOUT) == null) {
            return false;
        }
        Map<String, Value> map = this.json.get(LAYOUT).getMap(null);
        String[] keys = (String[]) map.keySet().toArray(new String[map.size()]);
        for (int i = 0; i < keys.length; i++) {
            layout.put(keys[i], Float.valueOf(this.json.get(LAYOUT).get(keys[i]).toFloat()));
        }
        return true;
    }

    public int getInitParamNum() {
        if (this.json.get(INIT_PARAM) == null) {
            return 0;
        }
        return this.json.get(INIT_PARAM).getVector(null).size();
    }

    public float getInitParamValue(int n) {
        return this.json.get(INIT_PARAM).get(n).get(VALUE).toFloat();
    }

    public String getInitParamID(int n) {
        return this.json.get(INIT_PARAM).get(n).get(ID).toString();
    }

    public int getInitPartsVisibleNum() {
        if (this.json.get(INIT_PARTS_VISIBLE) == null) {
            return 0;
        }
        return this.json.get(INIT_PARTS_VISIBLE).getVector(null).size();
    }

    public float getInitPartsVisibleValue(int n) {
        return this.json.get(INIT_PARTS_VISIBLE).get(n).get(VALUE).toFloat();
    }

    public String getInitPartsVisibleID(int n) {
        return this.json.get(INIT_PARTS_VISIBLE).get(n).get(ID).toString();
    }

    public int getExpressionNum() {
        if (this.json.get(EXPRESSIONS) == null) {
            return 0;
        }
        return this.json.get(EXPRESSIONS).getVector(null).size();
    }

    public String getExpressionFile(int n) {
        return this.json.get(EXPRESSIONS).get(n).get(FILE).toString();
    }

    public String getExpressionName(int n) {
        return this.json.get(EXPRESSIONS).get(n).get(NAME).toString();
    }

    public String[] getTextureFiles() {
        String[] ret = new String[getTextureNum()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getTextureFile(i);
        }
        return ret;
    }

    public String[] getExpressionFiles() {
        String[] ret = new String[getExpressionNum()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getExpressionFile(i);
        }
        return ret;
    }

    public String[] getExpressionNames() {
        String[] ret = new String[getExpressionNum()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getExpressionName(i);
        }
        return ret;
    }
}
