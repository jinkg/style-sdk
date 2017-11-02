package com.livewallpaper.models;

import com.livewallpaper.datareader.SharedPreferenceHelper;
import com.livewallpaper.settings.IABManager;

public class BaitsManager {
    private static int[] f124x975825de = null;
    private static final int DEFAULT_BAITS_NUM = 100000000;
    private static BaitsManager sInstance;
    private final String AWARD_TIMESTAMP = "AWARD_TIMESTAMP";
    private final String BAITS_NUM = "BAITS_NUM";
    private final String BANNER_TIMESTAMP = "BANNER_TIMESTAMP";
    private long awardTimestamp = Long.valueOf(SharedPreferenceHelper.getInstance().getString("AWARD_TIMESTAMP", "0")).longValue();
    private int baitsNum;
    private long bannerTimestamp = Long.valueOf(SharedPreferenceHelper.getInstance().getString("BANNER_TIMESTAMP", "0")).longValue();
    private boolean infiniteBaits = IABManager.getInstance().isBaitsPurchased();

    public enum BaitQuantity {
        FEW,
        MEDIUM,
        BONUS
    }

    static int[] m19x975825de() {
        int[] iArr = f124x975825de;
        if (iArr == null) {
            iArr = new int[BaitQuantity.values().length];
            try {
                iArr[BaitQuantity.BONUS.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[BaitQuantity.FEW.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[BaitQuantity.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            f124x975825de = iArr;
        }
        return iArr;
    }

    private BaitsManager() {
        if (this.infiniteBaits) {
            this.baitsNum = Integer.MAX_VALUE;
        } else {
            this.baitsNum = SharedPreferenceHelper.getInstance().getInteger("BAITS_NUM", DEFAULT_BAITS_NUM);
        }
    }

    public static BaitsManager getInstance() {
        if (sInstance == null) {
            sInstance = new BaitsManager();
        }
        return sInstance;
    }

    public void consume() {
        if (!this.infiniteBaits) {
            if (this.baitsNum <= 0) {
                this.baitsNum = 0;
                return;
            }
            this.baitsNum--;
            saveBaitsNum();
        }
    }

    public int getBaitsNum() {
        return this.baitsNum;
    }

    public void setBaitsNum(int number) {
        if (!this.infiniteBaits) {
            this.baitsNum = number;
            saveBaitsNum();
        }
    }

    public void setInfiniteBaits() {
        this.infiniteBaits = true;
        this.baitsNum = Integer.MAX_VALUE;
    }

    private void saveBaitsNum() {
        SharedPreferenceHelper.getInstance().putInteger("BAITS_NUM", this.baitsNum);
    }

    public void dispose() {
        sInstance = null;
    }

    public int award(BaitQuantity quantity) {
        int awardCount;
        switch (m19x975825de()[quantity.ordinal()]) {
            case 1:
                awardCount = (int) ((Math.random() * 5.0d) + 5.0d);
                break;
            case 2:
                awardCount = (int) ((Math.random() * 10.0d) + 10.0d);
                break;
            case 3:
                awardCount = 90;
                break;
            default:
                awardCount = (int) ((Math.random() * 5.0d) + 5.0d);
                break;
        }
        setBaitsNum(this.baitsNum + awardCount);
        return awardCount;
    }

    public long getAwardTimestamp() {
        return this.awardTimestamp;
    }

    public void setAwardTimestamp(long timestamp) {
        this.awardTimestamp = timestamp;
        SharedPreferenceHelper.getInstance().putString("AWARD_TIMESTAMP", String.valueOf(this.awardTimestamp));
    }

    public long getBannerTimestamp() {
        return this.bannerTimestamp;
    }

    public void setBannerTimestamp(long timestamp) {
        this.bannerTimestamp = timestamp;
        SharedPreferenceHelper.getInstance().putString("BANNER_TIMESTAMP", String.valueOf(this.bannerTimestamp));
    }
}
