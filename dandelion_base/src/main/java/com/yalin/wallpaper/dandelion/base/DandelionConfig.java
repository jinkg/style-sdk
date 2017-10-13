package com.yalin.wallpaper.dandelion.base;

/**
 * @author jinyalin
 * @since 2017/10/13.
 */

public class DandelionConfig {
    public int dandelionType;
    public boolean dandelionSound;
    public boolean dandelionAuto;

    public DandelionConfig(int dandelionType, boolean dandelionSound, boolean dandelionAuto) {
        this.dandelionType = dandelionType;
        this.dandelionSound = dandelionSound;
        this.dandelionAuto = dandelionAuto;
    }
}
