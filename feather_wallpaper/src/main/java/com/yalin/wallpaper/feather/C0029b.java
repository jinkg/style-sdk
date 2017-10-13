package com.yalin.wallpaper.feather;

import com.badlogic1.gdx.Gdx;
import com.badlogic1.gdx.files.FileHandle;
import com.badlogic1.gdx.graphics.Pixmap;
import com.badlogic1.gdx.graphics.Pixmap.Format;
import com.badlogic1.gdx.graphics.PixmapIO;
import com.badlogic1.gdx.graphics.TextureData;
import com.badlogic1.gdx.math.MathUtils;
import com.badlogic1.gdx.utils.GdxRuntimeException;

public class C0029b implements TextureData {
    public static boolean f130a;
    final FileHandle f131b;
    int f132c = 0;
    int f133d = 0;
    Format f134e;
    Pixmap f135f;
    boolean f136g;
    boolean f137h = false;
    int f138i = 0;

    public C0029b(FileHandle c0285a, Pixmap c0343l, Format c0345n, boolean z, int i) {
        this.f131b = c0285a;
        this.f135f = c0343l;
        this.f134e = c0345n;
        this.f136g = z;
        this.f138i = i;
        if (this.f135f != null) {
            this.f135f = m119a(this.f135f);
            this.f132c = this.f135f.getWidth();
            this.f133d = this.f135f.getHeight();
            if (c0345n == null) {
                this.f134e = this.f135f.getFormat();
            }
        }
    }

    @Override
    public boolean isPrepared() {
        return this.f137h;
    }

    @Override
    public void prepare() {
        if (this.f137h) {
            throw new GdxRuntimeException("Already prepared");
        }
        if (this.f135f == null) {
            if (this.f131b.extension().equals("cim")) {
                this.f135f = PixmapIO.readCIM(this.f131b);
            } else {
                byte[] h = this.f131b.readBytes();
                byte[] bArr = new byte[(h.length - this.f138i)];
                System.arraycopy(h, this.f138i, bArr, 0, h.length - this.f138i);
                this.f135f = m119a(new Pixmap(bArr, 0, bArr.length));
            }
            this.f132c = this.f135f.getWidth();
            this.f133d = this.f135f.getHeight();
            if (this.f134e == null) {
                this.f134e = this.f135f.getFormat();
            }
        }
        this.f137h = true;
    }

    private Pixmap m119a(Pixmap c0343l) {
        if (Gdx.gl20 == null && f130a) {
            int b = c0343l.getWidth();
            int d = c0343l.getHeight();
            int b2 = MathUtils.nextPowerOfTwo(b);
            int b3 = MathUtils.nextPowerOfTwo(d);
            if (!(b == b2 && d == b3)) {
                Pixmap c0343l2 = new Pixmap(b2, b3, c0343l.getFormat());
                c0343l2.drawPixmap(c0343l, 0, 0, 0, 0, b, d);
                c0343l.dispose();
                return c0343l2;
            }
        }
        return c0343l;
    }

    @Override
    public Pixmap consumePixmap() {
        if (this.f137h) {
            this.f137h = false;
            Pixmap c0343l = this.f135f;
            this.f135f = null;
            return c0343l;
        }
        throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }

    @Override
    public void consumeCompressedData() {

    }

    @Override
    public int getWidth() {
        return this.f132c;
    }

    @Override
    public int getHeight() {
        return this.f133d;
    }

    @Override
    public Format getFormat() {
        return this.f134e;
    }

    @Override
    public boolean useMipMaps() {
        return this.f136g;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }
}
