package com.livewallpaper.models;

import com.livewallpaper.datareader.SharedPreferenceHelper;
import com.livewallpaper.models.KoiModel.KoiSize;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class PondModel {
    public static final LinkedHashSet<String> defaultKoiDataSet = new C09421();
    public int id;
    private List<KoiModel> koiModels;
    private List<PondEditListener> listeners = new ArrayList();

    static class C09421 extends LinkedHashSet<String> {
        private static final long serialVersionUID = 1;

        C09421() {
            add("1|KOIA1|MEDIUM|0");
            add("2|KOIA2|MEDIUM|0");
            add("3|KOIA3|MEDIUM|0");
            add("4|KOIA4|MEDIUM|0");
            add("5|KOIA5|MEDIUM|0");
            add("6|KOIA6|SMALL|0");
            add("7|KOIA7|SMALL|0");
            add("8|KOIB1|BIG|0");
            add("9|KOIB2|BIG|0");
            add("10|KOIB3|BIG|0");
        }
    }

    public enum Opcode {
        ADD,
        REMOVE,
        EDIT
    }

    public interface PondEditListener {
        void sendPondEditEvent(PropsType propsType, Opcode opcode, Object obj);
    }

    public enum PropsType {
        KOI,
        PLANTS
    }

    private void initKoiModels() {
        this.koiModels = new ArrayList();
        Iterator it = SharedPreferenceHelper.getInstance().getStringSet(PreferenceChangedType.KOISET.toString(), defaultKoiDataSet).iterator();
        while (it.hasNext()) {
            String[] array = ((String) it.next()).split("\\|");
            KoiModel model = new KoiModel(Integer.parseInt(array[0]));
            model.fromDB = true;
            model.species = array[1];
            if (array[2].equalsIgnoreCase("BIG")) {
                model.size = KoiSize.BIG;
            }
            if (array[2].equalsIgnoreCase("MEDIUM")) {
                model.size = KoiSize.MEDIUM;
            }
            if (array[2].equalsIgnoreCase("SMALL")) {
                model.size = KoiSize.SMALL;
            }
            model.pondId = Integer.parseInt(array[3]);
            addKoiModel(model);
        }
    }

    public void reset() {
        List<KoiModel> clonedKoiModels = new ArrayList();
        for (KoiModel model : this.koiModels) {
            clonedKoiModels.add(model);
        }
        for (KoiModel model2 : clonedKoiModels) {
            removeKoiModel(model2);
        }
        SharedPreferenceHelper.getInstance().putStringSet(PreferenceChangedType.KOISET.toString(), null);
        initKoiModels();
    }

    public PondModel(int id) {
        this.id = id;
        initKoiModels();
    }

    public void addPondEditListener(PondEditListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removePondEditListener(PondEditListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public void addKoiModel(KoiModel koiModel) {
        if (!this.koiModels.contains(koiModel)) {
            this.koiModels.add(koiModel);
            if (!koiModel.fromDB) {
                koiModel.saveIntoPond();
            }
            for (PondEditListener listener : this.listeners) {
                listener.sendPondEditEvent(PropsType.KOI, Opcode.ADD, koiModel);
            }
        }
    }

    public void removeKoiModel(KoiModel koiModel) {
        if (this.koiModels.contains(koiModel)) {
            this.koiModels.remove(koiModel);
            koiModel.vanishFromPond();
            for (PondEditListener listener : this.listeners) {
                listener.sendPondEditEvent(PropsType.KOI, Opcode.REMOVE, koiModel);
            }
        }
    }

    public void updateKoiSize(KoiModel koiModel, KoiSize size) {
        if (this.koiModels.contains(koiModel)) {
            koiModel.updateSize(size);
            for (PondEditListener listener : this.listeners) {
                listener.sendPondEditEvent(PropsType.KOI, Opcode.EDIT, koiModel);
            }
        }
    }

    public void updateKoiSpecies(KoiModel koiModel, String species) {
        if (this.koiModels.contains(koiModel)) {
            koiModel.updateSpecies(species);
            for (PondEditListener listener : this.listeners) {
                listener.sendPondEditEvent(PropsType.KOI, Opcode.EDIT, koiModel);
            }
        }
    }

    public List<KoiModel> getKoiModels() {
        return this.koiModels;
    }
}
