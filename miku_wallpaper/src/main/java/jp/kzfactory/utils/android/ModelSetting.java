package jp.kzfactory.utils.android;

import java.util.Map;

public interface ModelSetting {
    String getExpressionFile(int i);

    String[] getExpressionFiles();

    String getExpressionName(int i);

    String[] getExpressionNames();

    int getExpressionNum();

    String getHitAreaID(int i);

    String getHitAreaName(int i);

    int getHitAreasNum();

    String getInitParamID(int i);

    int getInitParamNum();

    float getInitParamValue(int i);

    String getInitPartsVisibleID(int i);

    int getInitPartsVisibleNum();

    float getInitPartsVisibleValue(int i);

    boolean getLayout(Map<String, Float> map);

    String getModelFile();

    String getModelName();

    int getMotionFadeIn(String str, int i);

    int getMotionFadeOut(String str, int i);

    String getMotionFile(String str, int i);

    String[] getMotionGroupNames();

    int getMotionNum(String str);

    String getMotionSound(String str, int i);

    String getPhysicsFile();

    String getPoseFile();

    String getTextureFile(int i);

    String[] getTextureFiles();

    int getTextureNum();
}
