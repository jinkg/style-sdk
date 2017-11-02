package com.livewallpaper.settings;

import com.livewallpaper.datareader.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class IABManager {
    public static final String BAITS_SKU = "koipond_baits";
    public static final String CUSTOMBG_SKU = "koipond_custom_bg";
    public static final String FISHSCHOOL_SKU = "koipond_fish_school";
    public static final String GYROSENSOR_SKU = "koipond_gyro_sensor";
    public static final String KOIPACK1_SKU = "koipond_koi_pack_1";
    static final String KOIPOND_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgLY126Ku3k878bFTZdy+ZdoM/eLF49/mGoZVfbXjanD9GOXpsEr2uRBFrHx1pxy+g64Re6QfgpYSW7dXNJNlQPWhbKEhzbprHp5zQMLCHypUCyexNQNxRUbi5zrz0d3w/Gr2EbuXSSFPkC9HffvpsnnuCwzuHA/k0h7TR9X6SONZUNs7w2yr//CzThl0X/Axa8bJqfC5U6fONkNIq0regCrJqGjNN6ZxDQ1S72DsZhWx5IOpEgjpi6OBPbV2H14f+JP+PmGR/Ptsr6C3ZrknjY6X1PziouJb8AxaYGCKd12cAfmVchz0STPxv8JgWQRGqxJBbWiKRJucKtmYyFb87QIDAQAB";
    static final String TAG = "IAB";
    public static final String THEMEPACK1_SKU = "koipond_theme_pack_1";
    private static IABManager sInstance;
    private List<OnItemPuchasedListener> listeners = new ArrayList();

    public interface OnItemPuchasedListener {
        void onItemPurchased(String str);
    }

    private IABManager() {
        syncFromIABServer();
    }

    public void syncFromIABServer() {

    }

    public static IABManager getInstance() {
        if (sInstance == null) {
            sInstance = new IABManager();
        }
        return sInstance;
    }

    public boolean isItemPurchased(String key) {
        return SharedPreferenceHelper.getInstance().getBoolean(key, true);
    }

    public void setItemPurchaseStatus(String key, boolean status) {
        if (isItemPurchased(key) != status) {
            SharedPreferenceHelper.getInstance().putBoolean(key, status);
            for (OnItemPuchasedListener listener : this.listeners) {
                listener.onItemPurchased(key);
            }
        }
    }

    public void addListener(OnItemPuchasedListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(OnItemPuchasedListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public boolean isCustumBgPurchased() {
        return isItemPurchased(CUSTOMBG_SKU);
    }

    public boolean isBaitsPurchased() {
        return isItemPurchased(BAITS_SKU);
    }

    public boolean isGyroSensorPurchased() {
        return isItemPurchased(GYROSENSOR_SKU);
    }

    public boolean isFishSchoolPurchased() {
        return isItemPurchased(FISHSCHOOL_SKU);
    }

    public boolean isThemePack1Purchased() {
        return isItemPurchased(THEMEPACK1_SKU);
    }

    public boolean isKoiPack1Purchased() {
        return isItemPurchased(KOIPACK1_SKU);
    }

    public void dispose() {
        sInstance = null;
    }
}
