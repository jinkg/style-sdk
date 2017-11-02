package com.livewallpaper.models;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class PondsManager implements Disposable {
    private static PondsManager sInstance;
    private PondModel currentPond;
    private List<PondModel> pondModels = new ArrayList();

    private PondsManager() {
        PondModel pondModel = new PondModel(0);
        this.pondModels.add(pondModel);
        this.currentPond = pondModel;
    }

    public static PondsManager getInstance() {
        if (sInstance == null) {
            sInstance = new PondsManager();
        }
        return sInstance;
    }

    public PondModel getCurrentPond() {
        return this.currentPond;
    }

    public List<PondModel> getAllPonds() {
        return this.pondModels;
    }

    public void dispose() {
        this.pondModels = null;
        sInstance = null;
    }
}
