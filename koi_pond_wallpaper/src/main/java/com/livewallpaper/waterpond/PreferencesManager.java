package com.livewallpaper.waterpond;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.livewallpaper.datareader.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public final class PreferencesManager {
    private static int[] f131x75947dbf;
    private static PreferencesManager sInstance;
    public boolean bgUseCustomBoolean;
    public boolean customBgLoaded;
    public final float depthLimit = 0.18f;
    public Color envColor;
    public boolean envReflectionsBoolean;
    public float envReflectionsPercent;
    public boolean feedKoi;
    public Color fishFinsColor;
    public float fishFogDegree;
    public float fishFogDensity;
    public int fishPopulationInt = 60;
    public Color fishScalesColor;
    public float fishSizeIndex;
    public Color fogColorAtBottom;
    public float fogDensity;
    public float fogDensityAtBottomNeg;
    public float fps;
    public boolean gyroEnabled;
    public float interGyroSensitivityPercent;
    public LightInfo lightInfo;
    private List<OnPreferenceChangedListener> mListeners = new ArrayList();
    public boolean pagePan;
    public float plantSizeScale;
    public boolean rainyMode;
    public boolean schoolEnabled;
    public boolean showFloatage;
    public String theme;
    public boolean touchPan;
    public float unitWaterSpeed;
    public float waterClarityPercent;
    public Color waterColor;
    public float waterDamping;
    public float waterTurbulencePercent;

    public interface OnPreferenceChangedListener {
        void onPreferenceChanged(PreferenceChangedType preferenceChangedType);
    }

    public enum PreferenceChangedType {
        SHOWREFLECTION,
        SHOWFLOATAGE,
        CURRENTTHEME,
        RAINYMODE,
        FEEDKOI,
        TOUCHPAN,
        POWERSAVER,
        SHOWSCHOOL,
        GYROENABLE,
        CUSTOMBGENABLE,
        CUSTOMBGLOADED,
        KOISET,
        PAGEPAN
    }

    static int[] m26x75947dbf() {
        int[] iArr = f131x75947dbf;
        if (iArr == null) {
            iArr = new int[PreferenceChangedType.values().length];
            try {
                iArr[PreferenceChangedType.CURRENTTHEME.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGENABLE.ordinal()] = 10;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PreferenceChangedType.CUSTOMBGLOADED.ordinal()] = 11;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PreferenceChangedType.FEEDKOI.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PreferenceChangedType.GYROENABLE.ordinal()] = 9;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[PreferenceChangedType.KOISET.ordinal()] = 12;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[PreferenceChangedType.PAGEPAN.ordinal()] = 13;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[PreferenceChangedType.POWERSAVER.ordinal()] = 7;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[PreferenceChangedType.RAINYMODE.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[PreferenceChangedType.SHOWFLOATAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[PreferenceChangedType.SHOWREFLECTION.ordinal()] = 1;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[PreferenceChangedType.SHOWSCHOOL.ordinal()] = 8;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[PreferenceChangedType.TOUCHPAN.ordinal()] = 6;
            } catch (NoSuchFieldError e13) {
            }
            f131x75947dbf = iArr;
        }
        return iArr;
    }

    public static PreferencesManager getInstance() {
        if (sInstance == null) {
            sInstance = new PreferencesManager();
        }
        return sInstance;
    }

    public void notifyPreferenceChanged(PreferenceChangedType type) {
        switch (m26x75947dbf()[type.ordinal()]) {
            case 1:
                updateReflection();
                break;
            case 2:
                updateFloatage();
                break;
            case 3:
                updateThemeCode();
                break;
            case 4:
                updateRainyMode();
                break;
            case 5:
                updateDoubeClickFeed();
                break;
            case 6:
                updateTouchPan();
                break;
            case 7:
                updateFPS();
                break;
            case 8:
                updateSchool();
                break;
            case 9:
                updateGyro();
                break;
            case 10:
                updateCustomBgEnable();
                break;
            case 11:
                updateBgLoaded();
                break;
            case 13:
                updatePagePan();
                break;
        }
        for (OnPreferenceChangedListener listener : this.mListeners) {
            if (listener != null) {
                listener.onPreferenceChanged(type);
            }
        }
    }

    public void addPreferenceChangedListener(OnPreferenceChangedListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void dispose() {
        this.mListeners.clear();
        this.mListeners = null;
        sInstance = null;
    }

    public void removePreferenceChangedListener(OnPreferenceChangedListener listener) {
        this.mListeners.remove(listener);
    }

    private PreferencesManager() {
        updateThemeCode();
        updateWaterDamping();
        updateThemeData();
        updateFPS();
        updateFishSize();
        updateUnitWaterSpeed();
        updateReflection();
        updateFloatage();
        updateRainyMode();
        updateDoubeClickFeed();
        updateTouchPan();
        updatePagePan();
        updateSchool();
        updateGyro();
        updateCustomBgEnable();
        updateBgLoaded();
    }

    public void updateThemePrefs() {
        updateThemeData();
        updateReflection();
    }

    private void updateReflection() {
        this.envReflectionsBoolean = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.SHOWREFLECTION.toString(), false);
        if (this.envReflectionsBoolean) {
            String themeCode = this.theme;
            if (themeCode.equals("Muddy")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
            }
            if (themeCode.equals("Pebble")) {
                this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            }
            if (themeCode.equals("Stone")) {
                this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            }
            if (themeCode.equals("Pavement")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
            }
            if (themeCode.equals("Forest")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else if (themeCode.equals("Sandy")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else if (themeCode.equals("Yellow")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else if (themeCode.equals("Stream")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else if (themeCode.equals("Summer")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else if (themeCode.equals("Seashell")) {
                this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
                return;
            } else {
                return;
            }
        }
        this.envReflectionsPercent = 0.0f;
    }

    private void updateThemeCode() {
        this.theme = SharedPreferenceHelper.getInstance().getString(PreferenceChangedType.CURRENTTHEME.toString(), "Forest");
    }

    private void updateCustomBgEnable() {
        this.bgUseCustomBoolean = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.CUSTOMBGENABLE.toString(), false);
    }

    private void updateBgLoaded() {
        this.customBgLoaded = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.CUSTOMBGLOADED.toString(), false);
    }

    private void updateFloatage() {
        this.showFloatage = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.SHOWFLOATAGE.toString(), true);
    }

    private void updateRainyMode() {
        this.rainyMode = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.RAINYMODE.toString(), true);
    }

    private void updateDoubeClickFeed() {
        this.feedKoi = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.FEEDKOI.toString(), true);
    }

    private void updateTouchPan() {
        this.touchPan = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.TOUCHPAN.toString(), true);
    }

    private void updatePagePan() {
        this.pagePan = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.PAGEPAN.toString(), false);
    }

    private void updateSchool() {
        this.schoolEnabled = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.SHOWSCHOOL.toString(), false);
    }

    private void updateGyro() {
        this.interGyroSensitivityPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
        if (this.interGyroSensitivityPercent < 0.05f) {
            this.gyroEnabled = false;
        } else {
            this.gyroEnabled = SharedPreferenceHelper.getInstance().getBoolean(PreferenceChangedType.GYROENABLE.toString(), false);
        }
    }

    private void updateThemeData() {
        String themeCode = this.theme;
        this.waterColor = parseColor(-5721214, new Color());
        this.waterClarityPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
        this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
        this.envColor = parseColor(-5592406, new Color());
        this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
        this.fishScalesColor = parseColor(-13886972, new Color());
        this.fishFinsColor = parseColor(-8370944, new Color());
        this.plantSizeScale = 0.1f;
        if (themeCode.equals("Muddy")) {
            this.waterColor = parseColor(-5721214, new Color());
            this.waterClarityPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-5592406, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
            this.plantSizeScale = 0.05f;
        }
        if (themeCode.equals("Pebble")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Pavement")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
            this.plantSizeScale = 0.07f;
        }
        if (themeCode.equals("Forest")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.7f, 0.0f, 1.0f);
            this.plantSizeScale = 0.08f;
        }
        if (themeCode.equals("Stone")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Sandy")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Yellow")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Stream")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Summer")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        if (themeCode.equals("Seashell")) {
            this.waterClarityPercent = MathUtils.clamp(1.0f, 0.0f, 1.0f);
            this.waterTurbulencePercent = MathUtils.clamp(0.3f, 0.0f, 1.0f);
            this.envColor = parseColor(-1, new Color());
            this.envReflectionsPercent = MathUtils.clamp(0.8f, 0.0f, 1.0f);
            this.plantSizeScale = 0.1f;
        }
        this.lightInfo = new LightInfo();
        this.lightInfo.esToLightDir.set(0.0f, -2.0f, -10.0f).nor();
        Color envColor = new Color(this.envColor);
        float complementaryColor = 1.0f - Math.max(Math.max(envColor.r, envColor.g), envColor.b);
        envColor.r += complementaryColor;
        envColor.g += complementaryColor;
        envColor.b += complementaryColor;
        this.lightInfo.lightDiffuse.set(envColor);
        float f5 = 0.15f * (1.0f - this.envReflectionsPercent);
        float f6 = 1.0f - (0.15f * this.envReflectionsPercent);
        Color envColor2 = new Color(this.envColor);
        float maxColorComponent = Math.max(Math.max(envColor2.r, envColor2.g), envColor2.b);
        envColor2.r = (0.6f * envColor2.r) + (0.4f * maxColorComponent);
        envColor2.g = (0.6f * envColor2.g) + (0.4f * maxColorComponent);
        envColor2.b = (0.6f * envColor2.b) + (0.4f * maxColorComponent);
        envColor2.r = (envColor2.r * (f6 - f5)) + f5;
        envColor2.g = (envColor2.g * (f6 - f5)) + f5;
        envColor2.b = (envColor2.b * (f6 - f5)) + f5;
        this.lightInfo.lightAmbient.set(envColor2);
        float waterClarityFactor = (1.0f - this.waterClarityPercent) * 1.3f;
        this.fogDensity = 0.6f * waterClarityFactor;
        float clamp = MathUtils.clamp(waterClarityFactor - (this.fogDensity / 2.0f), 0.0f, 1.0f);
        getClass();
        this.fishFogDensity = clamp * (1.0f / 0.18f);
        getClass();
        this.fishFogDegree = MathUtils.clamp((0.18f - 0.0f) * this.fishFogDensity, 0.0f, 1.0f);
        this.fogDensityAtBottomNeg = 1.0f - this.fishFogDegree;
        this.fogColorAtBottom = this.waterColor.cpy().mul(this.fishFogDegree);
        this.fogColorAtBottom.a = 1.0f;
    }

    private void updateFPS() {
        switch (Integer.parseInt(SharedPreferenceHelper.getInstance().getString(PreferenceChangedType.POWERSAVER.toString(), "1"))) {
            case 0:
                this.fps = 30.0f;
                return;
            case 1:
                this.fps = 40.0f;
                return;
            case 2:
                this.fps = 60.0f;
                return;
            default:
                this.fps = 40.0f;
                return;
        }
    }

    private void updateFishSize() {
        this.fishSizeIndex = 0.8f;
    }

    private void updateUnitWaterSpeed() {
        switch (1) {
            case 0:
                this.unitWaterSpeed = 0.04f;
                return;
            case 1:
                this.unitWaterSpeed = 0.028571429f;
                return;
            case 2:
                this.unitWaterSpeed = 0.022222223f;
                return;
            case 3:
                this.unitWaterSpeed = 0.016666668f;
                return;
            default:
                this.unitWaterSpeed = 0.028571429f;
                return;
        }
    }

    private void updateWaterDamping() {
        switch (1) {
            case 0:
                this.waterDamping = 0.988f;
                return;
            case 1:
                this.waterDamping = 0.975f;
                return;
            case 2:
                this.waterDamping = 0.9f;
                return;
            default:
                this.waterDamping = 0.975f;
                return;
        }
    }

    private static Color parseColor(int packedColor, Color color) {
        Color.rgb888ToColor(color, packedColor);
        color.a = 1.0f;
        return color;
    }
}
