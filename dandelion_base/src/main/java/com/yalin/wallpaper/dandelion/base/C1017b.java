package com.yalin.wallpaper.dandelion.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class C1017b implements TextureData {
    public static boolean f2700a;
    final FileHandle f2701b;
    int f2702c = 0;
    int f2703d = 0;
    Format f2704e;
    Pixmap f2705f;
    boolean f2706g;
    boolean f2707h = false;
    int f2708i = 0;

    public C1017b(FileHandle c1166a, Pixmap c1233j, Format c1232b, boolean z, int i) {
        this.f2701b = c1166a;
        this.f2705f = c1233j;
        this.f2704e = c1232b;
        this.f2706g = z;
        this.f2708i = i;
        if (this.f2705f != null) {
            this.f2705f = m5156a(this.f2705f);
            this.f2702c = this.f2705f.getWidth();
            this.f2703d = this.f2705f.getHeight();
            if (c1232b == null) {
                this.f2704e = this.f2705f.getFormat();
            }
        }
    }

    @Override
    public boolean isPrepared() {
        return this.f2707h;
    }

    @Override
    public void prepare() {
        if (this.f2707h) {
            throw new GdxRuntimeException("Already prepared");
        }
        if (this.f2705f == null) {
            if (this.f2701b.extension().equals("cim")) {
                this.f2705f = PixmapIO.readCIM(this.f2701b);
            } else {
                byte[] i = this.f2701b.readBytes();
                byte[] bArr = new byte[(i.length - this.f2708i)];
                System.arraycopy(i, this.f2708i, bArr, 0, i.length - this.f2708i);
                this.f2705f = m5156a(new Pixmap(bArr, 0, bArr.length));
            }
            this.f2702c = this.f2705f.getWidth();
            this.f2703d = this.f2705f.getHeight();
            if (this.f2704e == null) {
                this.f2704e = this.f2705f.getFormat();
            }
        }
        this.f2707h = true;
    }

    private Pixmap m5156a(Pixmap c1233j) {
        if (Gdx.gl20 == null && f2700a) {
            int b = c1233j.getWidth();
            int d = c1233j.getHeight();
            int b2 = MathUtils.nextPowerOfTwo(b);
            int b3 = MathUtils.nextPowerOfTwo(d);
            if (!(b == b2 && d == b3)) {
                Pixmap c1233j2 = new Pixmap(b2, b3, c1233j.getFormat());
                c1233j2.drawPixmap(c1233j, 0, 0, 0, 0, b, d);
                c1233j.dispose();
                return c1233j2;
            }
        }
        return c1233j;
    }

    @Override
    public Pixmap consumePixmap() {
        if (this.f2707h) {
            this.f2707h = false;
            Pixmap c1233j = this.f2705f;
            this.f2705f = null;
            return c1233j;
        }
        throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }

    @Override
    public int getWidth() {
        return this.f2702c;
    }

    @Override
    public int getHeight() {
        return this.f2703d;
    }

    @Override
    public Format getFormat() {
        return this.f2704e;
    }

    @Override
    public boolean useMipMaps() {
        return this.f2706g;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }

    @Override
    public void consumeCustomData(int target) {
        throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
    }
}
