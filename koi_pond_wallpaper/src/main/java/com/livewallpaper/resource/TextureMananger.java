package com.livewallpaper.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.ist.lwp.koipond.natives.NativeTextureManager;
import com.livewallpaper.koipond.KoiPondService;
import com.livewallpaper.models.KoiModel;
import com.livewallpaper.models.PondsManager;
import com.livewallpaper.waterpond.PreferencesManager;
import com.livewallpaper.waterpond.PreferencesManager.OnPreferenceChangedListener;
import com.livewallpaper.waterpond.PreferencesManager.PreferenceChangedType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextureMananger implements Disposable, OnPreferenceChangedListener {
    private static int[] f125xd75e0cfa = null;
    private static int[] f126x75947dbf = null;
    public static final String CUSTOMBGNAME = "koipond_custom_bg.png";
    private static TextureMananger sInstance = null;
    private final Map<String, TextureUrl> koiUrls = new C09431();
    private List<OnThemeTextureUpdated> listeners;
    private NativeTextureManager nativeTextureManager;
    private Map<String, TextureUrl> textureFileUrls = new C09442();
    private Map<String, Texture> textures = null;
    private boolean themeTexturesDirty;

    class C09431 extends HashMap<String, TextureUrl> {
        private static final long serialVersionUID = 1;

        C09431() {
            put(KoiModel.SPECIES_KOIA1, new TextureUrl("textures/koi3d/koi-01.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA2, new TextureUrl("textures/koi3d/koi-02.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA3, new TextureUrl("textures/koi3d/koi-03.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA4, new TextureUrl("textures/koi3d/koi-04.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA5, new TextureUrl("textures/koi3d/koi-05.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA6, new TextureUrl("textures/koi3d/koi-06.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIA7, new TextureUrl("textures/koi3d/koi-07.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB1, new TextureUrl("textures/koi3d/koi-08.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB2, new TextureUrl("textures/koi3d/koi-09.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB3, new TextureUrl("textures/koi3d/koi-10.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB4, new TextureUrl("textures/koi3d/koi-11.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB5, new TextureUrl("textures/koi3d/koi-12.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB6, new TextureUrl("textures/koi3d/koi-13.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB7, new TextureUrl("textures/koi3d/koi-14.png", UrlType.ASSETS));
            put(KoiModel.SPECIES_KOIB8, new TextureUrl("textures/koi3d/koi-15.png", UrlType.ASSETS));
        }
    }

    class C09442 extends HashMap<String, TextureUrl> {
        private static final long serialVersionUID = 1;

        C09442() {
            put("BG", new TextureUrl("textures/forest/bg.etc1", UrlType.ASSETS));
            put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            put("FISHSCHOOL", new TextureUrl("textures/school/fish_0.cim", UrlType.ASSETS));
            put("BAIT", new TextureUrl("textures/bait/bait.png", UrlType.ASSETS));
            put("PLANT01", new TextureUrl("textures/floatage/aspen-01.png", UrlType.ASSETS));
            put("PLANT02", new TextureUrl("textures/floatage/aspen-02.png", UrlType.ASSETS));
            put("PLANT03", new TextureUrl("textures/floatage/aspen-03.png", UrlType.ASSETS));
            put("PLANT04", new TextureUrl("textures/floatage/aspen-04.png", UrlType.ASSETS));
        }
    }

    public interface OnThemeTextureUpdated {
        void onThemeTextureUpdated();
    }

    private class TextureUrl {
        public UrlType type;
        public String url;

        public TextureUrl(String url, UrlType type) {
            this.url = url;
            this.type = type;
        }
    }

    private enum UrlType {
        ASSETS,
        ONDISK
    }

    static int[] m20xd75e0cfa() {
        int[] iArr = f125xd75e0cfa;
        if (iArr == null) {
            iArr = new int[UrlType.values().length];
            try {
                iArr[UrlType.ASSETS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[UrlType.ONDISK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            f125xd75e0cfa = iArr;
        }
        return iArr;
    }

    static int[] m21x75947dbf() {
        int[] iArr = f126x75947dbf;
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
            f126x75947dbf = iArr;
        }
        return iArr;
    }

    private TextureMananger() {
        PreferencesManager.getInstance().addPreferenceChangedListener(this);
        this.textures = new HashMap();
        this.nativeTextureManager = new NativeTextureManager();
        this.listeners = new ArrayList();
        updateThemeUrls();
        initTextures();
        this.themeTexturesDirty = false;
    }

    public static TextureMananger getInstance() {
        if (sInstance == null) {
            sInstance = new TextureMananger();
        }
        return sInstance;
    }

    private void notifyThemeTextureUpdated() {
        for (OnThemeTextureUpdated listener : this.listeners) {
            listener.onThemeTextureUpdated();
        }
    }

    private void initTextures() {
        for (Entry<String, TextureUrl> entry : this.textureFileUrls.entrySet()) {
            String key = (String) entry.getKey();
            putTexture(key, new Texture(createTexFileHandle(key), true));
        }
        updateKoiTextures();
    }

    private FileHandle createTexFileHandle(String key) {
        TextureUrl textureUrl = this.textureFileUrls.containsKey(key) ? (TextureUrl) this.textureFileUrls.get(key) : (TextureUrl) this.koiUrls.get(key);
        switch (m20xd75e0cfa()[textureUrl.type.ordinal()]) {
            case 1:
                return Gdx.files.internal(textureUrl.url);
            case 2:
                return new FileHandle(new File(KoiPondService.getContext().getFilesDir(), textureUrl.url));
            default:
                return Gdx.files.internal(textureUrl.url);
        }
    }

    private void updateThemeUrls() {
        this.themeTexturesDirty = true;
        String currentTheme = PreferencesManager.getInstance().theme;
        if (currentTheme.equals("Muddy")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/muddy/bg.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/muddy/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/green-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/green-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/green-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/green-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Pebble")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/pebble/bg.jpg", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/pebble/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/leaf-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/leaf-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/leaf-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Stone")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/stone/bg.jpg", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/stone/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/leaf-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/leaf-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/leaf-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Pavement")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/pavement/bg.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/pavement/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/ginkgo-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/ginkgo-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/ginkgo-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/ginkgo-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Forest")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/forest/bg.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/aspen-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/aspen-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/aspen-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/aspen-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Sandy")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/sandy/bg.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/birch-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/birch-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/birch-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/birch-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Yellow")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/yellow/bg.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/lilypad-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/lilypad-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/lilypad-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/lilypad-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Stream")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/stream/bg.jpg", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/leaf-01.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/leaf-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/leaf-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Summer")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/summer/bg.jpg", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/leaf-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/leaf-04.png", UrlType.ASSETS));
        }
        if (currentTheme.equals("Seashell")) {
            this.textureFileUrls.put("BG", new TextureUrl("textures/seashell/bg.jpg", UrlType.ASSETS));
            this.textureFileUrls.put("ENV", new TextureUrl("textures/forest/env.etc1", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT01", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT02", new TextureUrl("textures/floatage/leaf-02.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT03", new TextureUrl("textures/floatage/leaf-03.png", UrlType.ASSETS));
            this.textureFileUrls.put("PLANT04", new TextureUrl("textures/floatage/leaf-04.png", UrlType.ASSETS));
        }
        if (PreferencesManager.getInstance().bgUseCustomBoolean && PreferencesManager.getInstance().customBgLoaded && new File(KoiPondService.getContext().getFilesDir(), CUSTOMBGNAME).exists()) {
            this.textureFileUrls.put("BG", new TextureUrl(CUSTOMBGNAME, UrlType.ONDISK));
        }
    }

    public boolean isThemeTexturesDirty() {
        return this.themeTexturesDirty;
    }

    public void putTexture(String key, Texture tex) {
        this.textures.put(key, tex);
        this.nativeTextureManager.putTexture(key, tex.getTextureObjectHandle(), tex.getWidth(), tex.getHeight());
    }

    public void removeTexture(String key) {
        ((Texture) this.textures.get(key)).dispose();
        this.textures.remove(key);
        this.nativeTextureManager.removeTexture(key);
    }

    public void updateThemeTextures() {
        if (this.themeTexturesDirty) {
            ((Texture) this.textures.get("ENV")).dispose();
            putTexture("ENV", new Texture(createTexFileHandle("ENV"), true));
            ((Texture) this.textures.get("BG")).dispose();
            putTexture("BG", new Texture(createTexFileHandle("BG"), true));
            ((Texture) this.textures.get("PLANT01")).dispose();
            putTexture("PLANT01", new Texture(createTexFileHandle("PLANT01"), true));
            ((Texture) this.textures.get("PLANT02")).dispose();
            putTexture("PLANT02", new Texture(createTexFileHandle("PLANT02"), true));
            ((Texture) this.textures.get("PLANT03")).dispose();
            putTexture("PLANT03", new Texture(createTexFileHandle("PLANT03"), true));
            ((Texture) this.textures.get("PLANT04")).dispose();
            putTexture("PLANT04", new Texture(createTexFileHandle("PLANT04"), true));
            this.themeTexturesDirty = false;
            notifyThemeTextureUpdated();
        }
    }

    public void addThemeChangedListener(OnThemeTextureUpdated listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeThemeChangedListener(OnThemeTextureUpdated listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public Texture getTexture(String textureKey) {
        return (Texture) this.textures.get(textureKey);
    }

    public void dispose() {
        for (Entry<String, Texture> entry : this.textures.entrySet()) {
            ((Texture) entry.getValue()).dispose();
        }
        Texture.clearAllTextures(Gdx.app);
        this.textures = null;
        sInstance = null;
        this.listeners = null;
        this.textureFileUrls = null;
        PreferencesManager.getInstance().removePreferenceChangedListener(this);
    }

    public void onPreferenceChanged(PreferenceChangedType type) {
        switch (m21x75947dbf()[type.ordinal()]) {
            case 3:
            case 10:
            case 11:
                updateThemeUrls();
                return;
            default:
                return;
        }
    }

    public void updateKoiTextures() {
        for (KoiModel model : PondsManager.getInstance().getCurrentPond().getKoiModels()) {
            String koiSpecies = model.species;
            if (!this.textures.containsKey(koiSpecies)) {
                putTexture(koiSpecies, new Texture(createTexFileHandle(koiSpecies), true));
            }
        }
    }

    public void invalidateAllTextures() {
        for (Entry<String, Texture> entry : this.textures.entrySet()) {
            putTexture((String) entry.getKey(), (Texture) entry.getValue());
        }
    }
}
