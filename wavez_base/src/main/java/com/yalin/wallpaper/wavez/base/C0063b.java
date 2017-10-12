package com.yalin.wallpaper.wavez.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class C0063b implements TextureData {

  public static boolean f143a;
  final FileHandle f144b;
  int f145c = 0;
  int f146d = 0;
  Format f147e;
  Pixmap f148f;
  boolean f149g;
  boolean f150h = false;
  int f151i = 0;

  public C0063b(FileHandle FileHandle, Pixmap Pixmap, Format Format, boolean z, int i) {
    this.f144b = FileHandle;
    this.f148f = Pixmap;
    this.f147e = Format;
    this.f149g = z;
    this.f151i = i;
    if (this.f148f != null) {
      this.f148f = m162a(this.f148f);
      this.f145c = this.f148f.getWidth();
      this.f146d = this.f148f.getHeight();
      if (Format == null) {
        this.f147e = this.f148f.getFormat();
      }
    }
  }

  private Pixmap m162a(Pixmap Pixmap) {
    if (Gdx.gl20 == null && f143a) {
      int b = Pixmap.getWidth();
      int d = Pixmap.getHeight();
      int b2 = MathUtils.nextPowerOfTwo(b);
      int b3 = MathUtils.nextPowerOfTwo(d);
      if (!(b == b2 && d == b3)) {
        Pixmap Pixmap2 = new Pixmap(b2, b3, Pixmap.getFormat());
        Pixmap2.drawPixmap(Pixmap, 0, 0, 0, 0, b, d);
        Pixmap.dispose();
        return Pixmap2;
      }
    }
    return Pixmap;
  }

  @Override
  public TextureDataType getType() {
    return TextureDataType.Pixmap;
  }

  @Override
  public boolean isPrepared() {
    return this.f150h;
  }

  @Override
  public void prepare() {
    if (this.f150h) {
      throw new GdxRuntimeException("Already prepared");
    }
    if (this.f148f == null) {
      if (this.f144b.extension().equals("cim")) {
        this.f148f = PixmapIO.readCIM(this.f144b);
      } else {
        byte[] g = this.f144b.readBytes();
        byte[] bArr = new byte[(g.length - this.f151i)];
        System.arraycopy(g, this.f151i, bArr, 0, g.length - this.f151i);
        this.f148f = m162a(new Pixmap(bArr, 0, bArr.length));
      }
      this.f145c = this.f148f.getWidth();
      this.f146d = this.f148f.getHeight();
      if (this.f147e == null) {
        this.f147e = this.f148f.getFormat();
      }
    }
    this.f150h = true;
  }

  @Override
  public Pixmap consumePixmap() {
    if (this.f150h) {
      this.f150h = false;
      Pixmap Pixmap = this.f148f;
      this.f148f = null;
      return Pixmap;
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
    return this.f145c;
  }

  @Override
  public int getHeight() {
    return this.f146d;
  }

  @Override
  public Format getFormat() {
    return this.f147e;
  }

  @Override
  public boolean useMipMaps() {
    return this.f149g;
  }

  @Override
  public boolean isManaged() {
    return true;
  }
}
