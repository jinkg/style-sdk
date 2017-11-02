package com.livewallpaper.koi3d;

import com.ist.lwp.koipond.natives.NativeKoi3DRenderer;
import com.livewallpaper.models.KoiModel;
import com.livewallpaper.models.KoiModel.KoiSize;
import com.livewallpaper.models.PondsManager;
import com.livewallpaper.waterpond.MainRenderer;

public class Koi3DRenderer {
    private static int[] $SWITCH_TABLE$com$ist$lwp$koipond$models$KoiModel$KoiSize;
    private NativeKoi3DRenderer nativeKoiRenderer = new NativeKoi3DRenderer();

    static int[] $SWITCH_TABLE$com$ist$lwp$koipond$models$KoiModel$KoiSize() {
        int[] iArr = $SWITCH_TABLE$com$ist$lwp$koipond$models$KoiModel$KoiSize;
        if (iArr == null) {
            iArr = new int[KoiSize.values().length];
            try {
                iArr[KoiSize.BIG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[KoiSize.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[KoiSize.SMALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$ist$lwp$koipond$models$KoiModel$KoiSize = iArr;
        }
        return iArr;
    }

    public Koi3DRenderer(MainRenderer mainRenderer) {
        for (KoiModel model : PondsManager.getInstance().getCurrentPond().getKoiModels()) {
            addKoi(model);
        }
    }

    public void render(float deltaTime) {
        if (!PondsManager.getInstance().getCurrentPond().getKoiModels().isEmpty()) {
            this.nativeKoiRenderer.render(deltaTime);
        }
    }

    public void addKoi(KoiModel koiModel) {
        this.nativeKoiRenderer.addKoi(koiModel.getId(), koiModel.species, sizeToFloat(koiModel.size));
    }

    public void removeKoi(KoiModel koiModel) {
        this.nativeKoiRenderer.removeKoi(koiModel.getId());
    }

    public void updateKoi(KoiModel koiModel) {
        this.nativeKoiRenderer.updateKoi(koiModel.getId(), koiModel.species, sizeToFloat(koiModel.size));
    }

    private float sizeToFloat(KoiSize size) {
        switch ($SWITCH_TABLE$com$ist$lwp$koipond$models$KoiModel$KoiSize()[size.ordinal()]) {
            case 1:
                return 0.115f;
            case 3:
                return 0.085f;
            default:
                return 0.1f;
        }
    }

    private void addSampleKoi() {
        KoiModel sample1 = new KoiModel();
        sample1.species = KoiModel.SPECIES_KOIB1;
        addKoi(sample1);
        KoiModel sample2 = new KoiModel();
        sample2.species = KoiModel.SPECIES_KOIB2;
        addKoi(sample2);
    }

    public void dispose() {
    }
}
