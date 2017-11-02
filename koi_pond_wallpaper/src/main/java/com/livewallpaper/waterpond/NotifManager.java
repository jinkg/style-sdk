package com.livewallpaper.waterpond;

import android.app.NotificationManager;

import com.badlogic.gdx.utils.Disposable;
import com.livewallpaper.datareader.SharedPreferenceHelper;
import com.livewallpaper.koipond.KoiPondService;

public class NotifManager implements Disposable {
    private static int[] f129x81cb391e = null;
    private static final String NOBAITS_NOTIFY_TIMESTAMP = "NOBAITS_NOTIFY_TIMESTAMP";
    private static NotifManager sInstance;
    private long noBaitsTimestamp = Long.valueOf(SharedPreferenceHelper.getInstance().getString(NOBAITS_NOTIFY_TIMESTAMP, "0")).longValue();

    public enum NotifType {
        BAITS_EXHAUSTED
    }

    static int[] m24x81cb391e() {
        int[] iArr = f129x81cb391e;
        if (iArr == null) {
            iArr = new int[NotifType.values().length];
            try {
                iArr[NotifType.BAITS_EXHAUSTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            f129x81cb391e = iArr;
        }
        return iArr;
    }

    private NotifManager() {
    }

    public static NotifManager getInstance() {
        if (sInstance == null) {
            sInstance = new NotifManager();
        }
        return sInstance;
    }

    public void notify(NotifType type) {
        NotificationManager notificationManager = (NotificationManager) KoiPondService.getContext().getSystemService("notification");
        long currentTimestamp = System.currentTimeMillis();
        switch (m24x81cb391e()[type.ordinal()]) {
            case 1:
                if (currentTimestamp - this.noBaitsTimestamp < 36000000) {
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void dispose() {
        sInstance = null;
    }
}
