package com.livewallpaper.models;

import com.livewallpaper.datareader.SharedPreferenceHelper;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

import java.util.LinkedHashSet;

public class KoiModel {
    public static final String SPECIES_KOIA1 = "KOIA1";
    public static final String SPECIES_KOIA2 = "KOIA2";
    public static final String SPECIES_KOIA3 = "KOIA3";
    public static final String SPECIES_KOIA4 = "KOIA4";
    public static final String SPECIES_KOIA5 = "KOIA5";
    public static final String SPECIES_KOIA6 = "KOIA6";
    public static final String SPECIES_KOIA7 = "KOIA7";
    public static final String SPECIES_KOIB1 = "KOIB1";
    public static final String SPECIES_KOIB2 = "KOIB2";
    public static final String SPECIES_KOIB3 = "KOIB3";
    public static final String SPECIES_KOIB4 = "KOIB4";
    public static final String SPECIES_KOIB5 = "KOIB5";
    public static final String SPECIES_KOIB6 = "KOIB6";
    public static final String SPECIES_KOIB7 = "KOIB7";
    public static final String SPECIES_KOIB8 = "KOIB8";
    public boolean fromDB;
    protected int id;
    public int pondId;
    public KoiSize size;
    public String species;

    public enum KoiSize {
        BIG,
        MEDIUM,
        SMALL
    }

    public KoiModel(int recommended) {
        this.species = SPECIES_KOIA1;
        this.size = KoiSize.BIG;
        this.fromDB = false;
        this.pondId = 0;
        KoiManager koiManager = KoiManager.getInstance();
        this.id = koiManager.allocID(recommended);
        koiManager.koiModels.put(Integer.valueOf(this.id), this);
    }

    public KoiModel() {
        this(-1);
    }

    public int getId() {
        return this.id;
    }

    public void saveIntoPond() {
        String key = PreferenceChangedType.KOISET.toString();
        LinkedHashSet<String> koiInPond = SharedPreferenceHelper.getInstance().getStringSet(key, PondModel.defaultKoiDataSet);
        koiInPond.add(toString());
        SharedPreferenceHelper.getInstance().putStringSet(key, koiInPond);
    }

    public void vanishFromPond() {
        String key = PreferenceChangedType.KOISET.toString();
        LinkedHashSet<String> koiInPond = SharedPreferenceHelper.getInstance().getStringSet(key, PondModel.defaultKoiDataSet);
        koiInPond.remove(toString());
        SharedPreferenceHelper.getInstance().putStringSet(key, koiInPond);
    }

    public void updateSpecies(String species) {
        String key = PreferenceChangedType.KOISET.toString();
        LinkedHashSet<String> koiInPond = SharedPreferenceHelper.getInstance().getStringSet(key, PondModel.defaultKoiDataSet);
        koiInPond.remove(toString());
        this.species = species;
        koiInPond.add(toString());
        SharedPreferenceHelper.getInstance().putStringSet(key, koiInPond);
    }

    public void updateSize(KoiSize size) {
        String key = PreferenceChangedType.KOISET.toString();
        LinkedHashSet<String> koiInPond = SharedPreferenceHelper.getInstance().getStringSet(key, PondModel.defaultKoiDataSet);
        koiInPond.remove(toString());
        this.size = size;
        koiInPond.add(toString());
        SharedPreferenceHelper.getInstance().putStringSet(key, koiInPond);
    }

    public String toString() {
        return this.id + "|" + this.species + "|" + this.size.toString() + "|" + this.pondId;
    }
}
