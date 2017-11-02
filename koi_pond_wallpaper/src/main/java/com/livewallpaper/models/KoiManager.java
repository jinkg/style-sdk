package com.livewallpaper.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class KoiManager {
    private static int MAX_ID = 100;
    private static KoiManager sInstance;
    protected Map<Integer, KoiModel> koiModels = new LinkedHashMap();

    public int allocID(int recommended) {
        if (!this.koiModels.containsKey(Integer.valueOf(recommended)) && recommended > 0) {
            return recommended;
        }
        for (int i = 1; i <= MAX_ID; i++) {
            if (!this.koiModels.containsKey(Integer.valueOf(i))) {
                return i;
            }
        }
        return -1;
    }

    private KoiManager() {
    }

    public static KoiManager getInstance() {
        if (sInstance == null) {
            sInstance = new KoiManager();
        }
        return sInstance;
    }

    public void dispose() {
        sInstance = null;
        this.koiModels.clear();
        this.koiModels = null;
    }
}
