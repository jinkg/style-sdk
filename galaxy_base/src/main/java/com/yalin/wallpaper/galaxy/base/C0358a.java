package com.yalin.wallpaper.galaxy.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class C0358a implements TextureData {
    public static boolean f798a;
    final FileHandle f799b;
    int f800c = 0;
    int f801d = 0;
    Format f802e;
    Pixmap f803f;
    boolean f804g;
    boolean f805h = false;
    int f806i = 0;

    public C0358a(FileHandle c0818a, Pixmap c0882j, Format c0881b, boolean z, int i) {
        this.f799b = c0818a;
        this.f803f = c0882j;
        this.f802e = c0881b;
        this.f804g = z;
        this.f806i = i;
        if (this.f803f != null) {
            this.f803f = m1570a(this.f803f);
            this.f800c = this.f803f.getWidth();
            this.f801d = this.f803f.getHeight();
            if (c0881b == null) {
                this.f802e = this.f803f.getFormat();
            }
        }
    }

    private Pixmap m1570a(Pixmap c0882j) {
        if (Gdx.gl20 == null && f798a) {
            int b = c0882j.getWidth();
            int d = c0882j.getHeight();
            int b2 = MathUtils.nextPowerOfTwo(b);
            int b3 = MathUtils.nextPowerOfTwo(d);
            if (!(b == b2 && d == b3)) {
                Pixmap c0882j2 = new Pixmap(b2, b3, c0882j.getFormat());
                c0882j2.drawPixmap(c0882j, 0, 0, 0, 0, b, d);
                c0882j.dispose();
                return c0882j2;
            }
        }
        return c0882j;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }

    @Override
    public boolean isPrepared() {
        return this.f805h;
    }

    @Override
    public void prepare() {
        if (this.f805h) {
            throw new GdxRuntimeException("Already prepared");
        }
        if (this.f803f == null) {
            if (this.f799b.extension().equals("cim")) {
                this.f803f = PixmapIO.readCIM(this.f799b);
            } else {
                byte[] g = this.f799b.readBytes();
                byte[] bArr = new byte[(g.length - this.f806i)];
                System.arraycopy(g, this.f806i, bArr, 0, g.length - this.f806i);
                this.f803f = m1570a(new Pixmap(bArr, 0, bArr.length));
            }
            this.f800c = this.f803f.getWidth();
            this.f801d = this.f803f.getHeight();
            if (this.f802e == null) {
                this.f802e = this.f803f.getFormat();
            }
        }
        this.f805h = true;
    }

    @Override
    public Pixmap consumePixmap() {
        if (this.f805h) {
            this.f805h = false;
            Pixmap c0882j = this.f803f;
            this.f803f = null;
            return c0882j;
        }
        throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }

    @Override
    public void consumeCustomData(int target) {
        throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
    }

    @Override
    public int getWidth() {
        return this.f800c;
    }

    @Override
    public int getHeight() {
        return this.f801d;
    }

    @Override
    public Format getFormat() {
        return this.f802e;
    }

    @Override
    public boolean useMipMaps() {
        return this.f804g;
    }

    @Override
    public boolean isManaged() {
        return true;
    }
}
