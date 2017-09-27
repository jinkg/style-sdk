package com.yalin.wallpaper.hypnoclock.p030a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;


import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

import com.yalin.wallpaper.hypnoclock.R;

public class C1546b {
    private float f6039C;
    private float f6040D;
    private float f6041E;
    private boolean f6042F;
    private boolean f6043G;
    public boolean f6044a;
    public boolean f6045b;
    private C1545a[] f6046c;
    private Calendar f6047d;
    private int f6048e;
    private int f6049f;
    private int f6050g;
    private int f6051h;
    private int f6052i;
    private int f6053j;
    private DisplayMetrics f6055l;
    private float[] f6056m;
    private float[] f6057n;
    private float f6058o;
    private float f6059p = -2.0f;
    private float[] f6060q = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private int[] f6061r = new int[24];
    private C1545a f6062s;
    private int[] f6063t;
    private int f6065v;
    private boolean f6066w;
    private boolean f6067x;
    private boolean f6068y;
    private boolean f6069z;

    public C1546b(int i, boolean z, boolean z2) {
        m8525a(i, z, z2);
    }

    private void m8525a(int i, boolean z, boolean z2) {
        this.f6039C = 0.0f;
        this.f6040D = 0.0f;
        this.f6041E = 0.0f;
        this.f6067x = true;
        this.f6068y = true;
        this.f6046c = new C1545a[i];
        if (z2) {
            this.f6062s = new C1545a();
            this.f6063t = new int[1];
        }
        for (int i2 = 0; i2 < i; i2++) {
            this.f6046c[i2] = new C1545a();
        }
        this.f6056m = new float[45];
        this.f6057n = new float[45];
        m8550b(z);
    }

    private void m8526b(GL10 gl10, boolean z, int i) {
        switch (i) {
            case 0:
            case 1:
            case 27:
            case 32:
            case 42:
                gl10.glBindTexture(3553, this.f6061r[0]);
                break;
            case 2:
            case 33:
            case 38:
                gl10.glBindTexture(3553, this.f6061r[1]);
                break;
            case 3:
            case 35:
                gl10.glBindTexture(3553, this.f6061r[3]);
                break;
            case 4:
            case 5:
            case 19:
            case 29:
                gl10.glBindTexture(3553, this.f6061r[4]);
                break;
            case 6:
            case 7:
            case 13:
            case 23:
                gl10.glBindTexture(3553, this.f6061r[2]);
                break;
            case 8:
            case 16:
                gl10.glBindTexture(3553, this.f6061r[7]);
                break;
            case 9:
            case 22:
                gl10.glBindTexture(3553, this.f6061r[8]);
                break;
            case 10:
            case 11:
            case 15:
                gl10.glBindTexture(3553, this.f6061r[6]);
                break;
            case 12:
                gl10.glBindTexture(3553, this.f6061r[11]);
                break;
            case 14:
                gl10.glBindTexture(3553, this.f6061r[9]);
                break;
            case 17:
                gl10.glBindTexture(3553, this.f6061r[12]);
                break;
            case 18:
            case 34:
                gl10.glBindTexture(3553, this.f6061r[10]);
                break;
            case 20:
                gl10.glBindTexture(3553, this.f6061r[23]);
                break;
            case 21:
                gl10.glBindTexture(3553, this.f6061r[5]);
                break;
            case 24:
                gl10.glBindTexture(3553, this.f6061r[13]);
                break;
            case 25:
                gl10.glBindTexture(3553, this.f6061r[14]);
                break;
            case 26:
            case 28:
            case 37:
                gl10.glBindTexture(3553, this.f6061r[15]);
                break;
            case 30:
            case 31:
                gl10.glBindTexture(3553, this.f6061r[16]);
                break;
            case 36:
                gl10.glBindTexture(3553, this.f6061r[17]);
                break;
            case 39:
                gl10.glBindTexture(3553, this.f6061r[18]);
                break;
            case 40:
                gl10.glBindTexture(3553, this.f6061r[19]);
                break;
            case 41:
                gl10.glBindTexture(3553, this.f6061r[20]);
                break;
            case 43:
                gl10.glBindTexture(3553, this.f6061r[21]);
                break;
            case 44:
                gl10.glBindTexture(3553, this.f6061r[22]);
                break;
        }
        gl10.glLoadIdentity();
        if (z) {
            gl10.glTranslatef((((-this.f6057n[i]) + this.f6058o) + this.f6040D) * ((float) this.f6046c[i].f6031d), ((-this.f6056m[i]) + this.f6039C) * ((float) this.f6046c[i].f6031d), (this.f6059p * 0.5625f) * ((float) this.f6046c[i].f6031d));
            if (i == 43 || i == 44) {
                gl10.glTranslatef(0.01f, 0.01f, 0.0f);
            } else {
                gl10.glTranslatef(0.05f * (((float) this.f6046c[2].f6032e) / ((float) this.f6046c[i].f6032e)), 0.05f * (((float) this.f6046c[2].f6032e) / ((float) this.f6046c[i].f6032e)), 0.0f);
            }
            if (i == 37 || i == 36) {
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[41] + 90.0f, 0.0f, 0.0f, 1.0f);
                if (i == 37) {
                    gl10.glTranslatef(3.25f, 0.0f, 0.0f);
                    gl10.glPushMatrix();
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 3.25f);
                } else {
                    gl10.glTranslatef(1.65f, 0.0f, 0.0f);
                    gl10.glPushMatrix();
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.65f);
                }
                gl10.glPushMatrix();
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
                this.f6046c[i].m8524a(gl10);
                gl10.glPopMatrix();
                gl10.glPopMatrix();
                gl10.glPopMatrix();
                return;
            } else if (i == 39) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                if (this.f6043G) {
                    if (this.f6049f % 2 == 0) {
                        if (this.f6048e <= 333) {
                            gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (this.f6048e <= 333) {
                        gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (!this.f6067x || this.f6068y) {
                    if (this.f6067x && this.f6068y) {
                        if (this.f6049f % 2 == 0) {
                            if (this.f6048e <= 333) {
                                gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                            } else if (this.f6048e <= 667) {
                                gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                            } else if (this.f6048e <= 1000) {
                                gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                            }
                        } else if (this.f6048e <= 333) {
                            gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (!this.f6067x) {
                        if (this.f6049f % 2 == 0) {
                            gl10.glScalef((((float) this.f6048e) / 1600.0f) + 0.2f, (((float) this.f6048e) / 1600.0f) + 0.2f, 1.0f);
                        } else {
                            gl10.glScalef((((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, 1.0f);
                        }
                    }
                } else if (this.f6048e < 500) {
                    gl10.glScalef((((float) this.f6048e) / 800.0f) + 0.2f, (((float) this.f6048e) / 800.0f) + 0.2f, 1.0f);
                } else {
                    gl10.glScalef((((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, 1.0f);
                }
                gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
                this.f6046c[i].m8524a(gl10);
                return;
            } else {
                if (i != 41 && i != 43 && i != 44) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    if (i == 0) {
                        gl10.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                    }
                } else if (i == 41) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 1.475f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                } else if (i == 43) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 0.35f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                } else {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 0.51f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                if (i != 27) {
                    gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
                    this.f6046c[i].m8524a(gl10);
                    return;
                }
                return;
            }
        }
        gl10.glTranslatef(((this.f6056m[i] + this.f6058o) + this.f6039C) * ((float) this.f6046c[i].f6031d), (this.f6057n[i] + this.f6040D) * ((float) this.f6046c[i].f6031d), (this.f6059p + this.f6041E) * ((float) this.f6046c[i].f6031d));
        if (i == 43 || i == 44) {
            gl10.glTranslatef(0.01f, 0.01f, 0.0f);
        } else {
            gl10.glTranslatef(0.05f * (((float) this.f6046c[2].f6032e) / ((float) this.f6046c[i].f6032e)), 0.05f * (((float) this.f6046c[2].f6032e) / ((float) this.f6046c[i].f6032e)), 0.0f);
        }
        if (i == 37 || i == 36) {
            gl10.glPushMatrix();
            gl10.glRotatef(this.f6060q[41] + 90.0f, 0.0f, 0.0f, 1.0f);
            if (i == 37) {
                gl10.glTranslatef(3.25f, 0.0f, 0.0f);
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 3.25f);
            } else {
                gl10.glTranslatef(1.65f, 0.0f, 0.0f);
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.65f);
            }
            gl10.glPushMatrix();
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            this.f6046c[i].m8524a(gl10);
            gl10.glPopMatrix();
            gl10.glPopMatrix();
            gl10.glPopMatrix();
        } else if (i == 39) {
            gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            if (this.f6043G) {
                if (this.f6049f % 2 == 0) {
                    if (this.f6048e <= 333) {
                        gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (this.f6048e <= 333) {
                    gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                } else if (this.f6048e <= 667) {
                    gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                } else if (this.f6048e <= 1000) {
                    gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                }
            } else if (!this.f6067x || this.f6068y) {
                if (this.f6067x && this.f6068y) {
                    if (this.f6049f % 2 == 0) {
                        if (this.f6048e <= 333) {
                            gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (this.f6048e <= 333) {
                        gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (!this.f6067x) {
                    if (this.f6049f % 2 == 0) {
                        gl10.glScalef((((float) this.f6048e) / 1600.0f) + 0.2f, (((float) this.f6048e) / 1600.0f) + 0.2f, 1.0f);
                    } else {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, 1.0f);
                    }
                }
            } else if (this.f6048e < 500) {
                gl10.glScalef((((float) this.f6048e) / 800.0f) + 0.2f, (((float) this.f6048e) / 800.0f) + 0.2f, 1.0f);
            } else {
                gl10.glScalef((((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, 1.0f);
            }
            gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            this.f6046c[i].m8524a(gl10);
        } else {
            if (i != 41 && i != 43 && i != 44) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                if (i == 0) {
                    gl10.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                }
            } else if (i == 41) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 1.475f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            } else if (i == 43) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 0.35f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 0.51f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            }
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            if (i != 27) {
                gl10.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
                this.f6046c[i].m8524a(gl10);
            }
        }
    }

    private void m8527e(GL10 gl10, boolean z) {
        m8526b(gl10, z, 0);
        m8526b(gl10, z, 5);
        m8526b(gl10, z, 2);
        m8526b(gl10, z, 7);
        m8526b(gl10, z, 9);
        m8526b(gl10, z, 13);
        m8526b(gl10, z, 32);
        m8526b(gl10, z, 30);
    }

    private void m8528e(boolean z) {
        this.f6047d = Calendar.getInstance();
        this.f6048e = this.f6047d.get(Calendar.MILLISECOND);
        this.f6049f = this.f6047d.get(Calendar.SECOND);
        this.f6050g = this.f6047d.get(Calendar.MINUTE);
        this.f6051h = this.f6047d.get(Calendar.HOUR);
        this.f6052i = this.f6047d.get(Calendar.DAY_OF_YEAR);
        this.f6053j = this.f6047d.get(Calendar.HOUR_OF_DAY);
        if (this.f6042F) {
            this.f6060q[1] = -1.0f * (((((((float) this.f6051h) / 12.0f) * 360.0f) + (30.0f * (((float) this.f6050g) / 60.0f))) + ((30.0f * (((float) this.f6049f) / 60.0f)) / 60.0f)) + (((30.0f * (((float) this.f6048e) / 60.0f)) / 60.0f) / 1000.0f));
            this.f6060q[29] = -1.0f * ((((((float) this.f6050g) / 60.0f) * 360.0f) + ((((((float) this.f6048e) / 1000.0f) * 360.0f) / 360.0f) * 0.1f)) + ((((((float) this.f6049f) / 60.0f) * 360.0f) / 360.0f) * 6.0f));
            this.f6060q[21] = -1.0f * ((((((float) this.f6049f) / 60.0f) * 360.0f) + ((((((float) this.f6048e) / 1000.0f) * 360.0f) / 360.0f) * 6.0f)) + 12.5f);
            this.f6060q[8] = 9.0f * this.f6060q[21];
            this.f6060q[7] = this.f6060q[8];
            if (z) {
                this.f6060q[15] = (6.0f * this.f6060q[21]) + 3.0f;
                this.f6060q[10] = this.f6060q[15];
                this.f6060q[11] = this.f6060q[15] + 6.0f;
            } else {
                this.f6060q[15] = 6.0f * this.f6060q[21];
                this.f6060q[10] = this.f6060q[15] + 3.0f;
                this.f6060q[11] = this.f6060q[15] + 3.0f;
            }
            if (this.f6050g % 2 == 0) {
                this.f6060q[20] = 3.0f - (1.5f * this.f6060q[21]);
                this.f6060q[14] = this.f6060q[20] - 3.0f;
                this.f6060q[18] = this.f6060q[20] - 3.0f;
                this.f6060q[12] = (-this.f6060q[21]) / 2.0f;
                this.f6060q[17] = this.f6060q[12];
            } else {
                this.f6060q[20] = -177.0f - (1.5f * this.f6060q[21]);
                this.f6060q[14] = this.f6060q[20] - 3.0f;
                this.f6060q[18] = this.f6060q[20] - 3.0f;
                this.f6060q[12] = -180.0f - (this.f6060q[21] / 2.0f);
                this.f6060q[17] = this.f6060q[12];
            }
            this.f6060q[9] = -2.0f * this.f6060q[8];
            this.f6060q[13] = this.f6060q[7];
            this.f6060q[16] = this.f6060q[7];
            this.f6060q[19] = this.f6060q[21];
            this.f6060q[22] = 12.0f * this.f6060q[29];
            this.f6060q[23] = -6.0f * this.f6060q[29];
            this.f6060q[24] = 18.0f * this.f6060q[29];
            this.f6060q[25] = -1.0f * this.f6060q[22];
            this.f6060q[6] = (6.0f * this.f6060q[29]) + 3.0f;
            if (this.f6052i % 3 == 0) {
                if (this.f6053j < 12) {
                    this.f6060q[3] = 2.0f * (this.f6060q[1] / 3.0f);
                } else {
                    this.f6060q[3] = (2.0f * (this.f6060q[1] / 3.0f)) + 270.0f;
                }
            } else if (this.f6052i % 3 == 1) {
                if (this.f6053j < 12) {
                    this.f6060q[3] = 120.0f + (2.0f * (this.f6060q[1] / 3.0f));
                } else {
                    this.f6060q[3] = 2.0f * (this.f6060q[1] / 3.0f);
                }
            } else if (this.f6052i % 3 == 2) {
                if (this.f6053j < 12) {
                    this.f6060q[3] = (2.0f * (this.f6060q[1] / 3.0f)) + 270.0f;
                } else {
                    this.f6060q[3] = 120.0f + (2.0f * (this.f6060q[1] / 3.0f));
                }
            }
            this.f6060q[4] = -2.0f * this.f6060q[1];
            this.f6060q[5] = -3.0f * this.f6060q[21];
            if (this.f6050g % 3 == 0) {
                this.f6060q[0] = -2.0f * (this.f6060q[29] / 3.0f);
            } else if (this.f6050g % 3 == 1) {
                this.f6060q[0] = -120.0f + (-2.0f * (this.f6060q[29] / 3.0f));
            } else {
                this.f6060q[0] = -240.0f + (-2.0f * (this.f6060q[29] / 3.0f));
            }
            this.f6060q[27] = this.f6060q[0];
            this.f6060q[2] = this.f6060q[1];
            this.f6060q[26] = this.f6060q[6];
            this.f6060q[28] = this.f6060q[2];
            this.f6060q[30] = -6.0f * this.f6060q[20];
            this.f6060q[31] = this.f6060q[30] - 33.0f;
            this.f6060q[32] = 2.0f * this.f6060q[21];
            this.f6060q[33] = this.f6060q[32];
            this.f6060q[34] = -3.0f * this.f6060q[21];
            this.f6060q[35] = -1.0f * (((((float) this.f6049f) / 60.0f) * 360.0f) + ((((((float) this.f6048e) / 1000.0f) * 360.0f) / 360.0f) * 6.0f));
            this.f6060q[38] = 0.0f;
            this.f6060q[37] = 6.0f * (-1.0f * (((((float) this.f6049f) / 60.0f) * 360.0f) + ((((((float) this.f6048e) / 1000.0f) * 360.0f) / 360.0f) * 6.0f)));
            this.f6060q[36] = this.f6060q[37];
            if (this.f6043G) {
                if (this.f6049f % 2 == 0) {
                    if (this.f6048e <= 333) {
                        this.f6060q[39] = -270.0f + ((((float) this.f6048e) / 333.0f) * 540.0f);
                    } else if (this.f6048e <= 667) {
                        this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                    } else if (this.f6048e <= 1000) {
                        this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
                    }
                } else if (this.f6048e <= 333) {
                    this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                } else if (this.f6048e <= 667) {
                    this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
                } else if (this.f6048e <= 1000) {
                    this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                }
            } else if (!this.f6067x || this.f6068y) {
                if (this.f6067x && this.f6068y) {
                    if (this.f6049f % 2 == 0) {
                        if (this.f6048e <= 333) {
                            this.f6060q[39] = -270.0f + ((((float) this.f6048e) / 333.0f) * 540.0f);
                        } else if (this.f6048e <= 667) {
                            this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                        } else if (this.f6048e <= 1000) {
                            this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
                        }
                    } else if (this.f6048e <= 333) {
                        this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                    } else if (this.f6048e <= 667) {
                        this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
                    } else if (this.f6048e <= 1000) {
                        this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
                    }
                } else if (!this.f6067x) {
                    if (this.f6049f % 2 == 0) {
                        this.f6060q[39] = -270.0f + ((((float) this.f6048e) / 1000.0f) * 540.0f);
                    } else {
                        this.f6060q[39] = ((((float) this.f6048e) / 1000.0f) * -540.0f) + 270.0f;
                    }
                }
            } else if (this.f6048e < 500) {
                this.f6060q[39] = -270.0f + ((((float) this.f6048e) / 500.0f) * 540.0f);
            } else {
                this.f6060q[39] = ((((float) (this.f6048e - 500)) / 500.0f) * -540.0f) + 270.0f;
            }
            this.f6060q[40] = this.f6060q[39];
            this.f6060q[41] = -1.0f * (((((float) this.f6049f) / 60.0f) * 360.0f) + ((((((float) this.f6048e) / 1000.0f) * 360.0f) / 360.0f) * 6.0f));
            this.f6060q[43] = this.f6060q[1];
            this.f6060q[44] = this.f6060q[29];
            this.f6060q[42] = this.f6060q[0];
            return;
        }
        if (this.f6049f % 2 == 0) {
            if (this.f6048e <= 333) {
                this.f6060q[39] = -270.0f + ((((float) this.f6048e) / 333.0f) * 540.0f);
            } else if (this.f6048e <= 667) {
                this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
            } else if (this.f6048e <= 1000) {
                this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
            }
        } else if (this.f6048e <= 333) {
            this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
        } else if (this.f6048e <= 667) {
            this.f6060q[39] = -270.0f + ((((float) (1000 - this.f6048e)) / 333.0f) * 540.0f);
        } else if (this.f6048e <= 1000) {
            this.f6060q[39] = ((((float) (667 - this.f6048e)) / 333.0f) * -540.0f) + 270.0f;
        }
        this.f6060q[40] = this.f6060q[39];
    }

    private void m8529f(GL10 gl10, boolean z) {
        m8526b(gl10, z, 3);
        m8526b(gl10, z, 4);
        m8526b(gl10, z, 8);
        m8526b(gl10, z, 16);
        m8526b(gl10, z, 14);
        m8526b(gl10, z, 28);
        m8526b(gl10, z, 33);
        m8526b(gl10, z, 34);
        m8526b(gl10, z, 35);
    }

    private void m8530g(GL10 gl10, boolean z) {
        m8526b(gl10, z, 18);
        m8526b(gl10, z, 19);
        m8526b(gl10, z, 12);
        m8526b(gl10, z, 36);
    }

    private void m8531h(GL10 gl10, boolean z) {
        m8526b(gl10, z, 6);
        m8526b(gl10, z, 1);
        m8526b(gl10, z, 17);
        m8526b(gl10, z, 22);
        m8526b(gl10, z, 23);
        m8526b(gl10, z, 24);
        m8526b(gl10, z, 25);
        m8526b(gl10, z, 38);
        m8526b(gl10, z, 37);
    }

    private void m8532i(GL10 gl10, boolean z) {
        m8526b(gl10, z, 39);
    }

    private void m8533j(GL10 gl10, boolean z) {
        m8526b(gl10, z, 40);
    }

    private void m8534k(GL10 gl10, boolean z) {
        m8526b(gl10, z, 15);
        m8526b(gl10, z, 10);
        m8526b(gl10, z, 11);
    }

    private void m8535l(GL10 gl10, boolean z) {
        m8526b(gl10, z, 41);
        m8526b(gl10, z, 21);
        m8526b(gl10, z, 20);
        m8526b(gl10, z, 31);
    }

    private void m8536m(GL10 gl10, boolean z) {
        m8526b(gl10, z, 26);
        m8526b(gl10, z, 27);
        m8526b(gl10, z, 42);
        m8526b(gl10, z, 29);
    }

    public void m8537a(float f) {
        this.f6058o = f;
    }

    public void m8538a(float f, float f2, float f3) {
        this.f6039C = f;
        this.f6040D = f2;
        this.f6041E = f3;
    }

    public void setDarkness(int i) {
        this.f6065v = i;
    }

    public void m8540a(int i, int i2, int i3, int i4) {
        this.f6046c[i].f6028a = i2;
        this.f6046c[i].f6029b = i3;
        this.f6046c[i].f6030c = i4;
    }

    public void m8541a(DisplayMetrics displayMetrics) {
        this.f6055l = displayMetrics;
    }

    public void m8542a(GL10 gl10, Context context, int i, int i2, int[] iArr) {
        int i3 = 0;
        Options options = new Options();
        options.inScaled = false;
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i2, options);
        if (this.f6055l.widthPixels > this.f6055l.heightPixels) {
            if (this.f6055l.widthPixels <= 1370) {
                decodeResource = Bitmap.createScaledBitmap(decodeResource, decodeResource.getWidth() / 2, decodeResource.getHeight() / 2, false);
            }
        } else if (this.f6055l.heightPixels <= 1370) {
            decodeResource = Bitmap.createScaledBitmap(decodeResource, decodeResource.getWidth() / 2, decodeResource.getHeight() / 2, false);
        }
        gl10.glBindTexture(3553, this.f6061r[i]);
        gl10.glTexParameterf(3553, 10240, 9729.0f);
        gl10.glTexParameterf(3553, 10241, 9729.0f);
        GLUtils.texImage2D(3553, 0, decodeResource, 0);
        while (i3 < iArr.length) {
            this.f6046c[iArr[i3]].f6032e = decodeResource.getWidth();
            if (decodeResource.getWidth() <= 8) {
                this.f6046c[iArr[i3]].f6031d = 128;
            } else if (decodeResource.getWidth() <= 16) {
                this.f6046c[iArr[i3]].f6031d = 64;
            } else if (decodeResource.getWidth() <= 32) {
                this.f6046c[iArr[i3]].f6031d = 32;
            } else if (decodeResource.getWidth() <= 64) {
                this.f6046c[iArr[i3]].f6031d = 16;
            } else if (decodeResource.getWidth() <= 128) {
                this.f6046c[iArr[i3]].f6031d = 8;
            } else if (decodeResource.getWidth() <= 256) {
                this.f6046c[iArr[i3]].f6031d = 4;
            } else if (decodeResource.getWidth() <= 512) {
                this.f6046c[iArr[i3]].f6031d = 2;
            } else if (decodeResource.getWidth() <= 1024) {
                this.f6046c[iArr[i3]].f6031d = 1;
            }
            if (this.f6066w) {
                C1545a c1545a = this.f6046c[iArr[i3]];
                c1545a.f6031d /= 2;
            }
            i3++;
        }
        decodeResource.recycle();
        System.gc();
    }

    public void m8543a(GL10 gl10, Context context, String str, int i) {
        Bitmap decodeResource;
        Options options = new Options();
        options.inScaled = false;

        decodeResource = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back_volcano_2, options);

        if (this.f6055l.widthPixels > this.f6055l.heightPixels) {
            if (this.f6055l.widthPixels <= 1370) {
                decodeResource = Bitmap.createScaledBitmap(decodeResource, decodeResource.getWidth() / 2, decodeResource.getHeight() / 2, false);
            }
        } else if (this.f6055l.heightPixels <= 1370) {
            decodeResource = Bitmap.createScaledBitmap(decodeResource, decodeResource.getWidth() / 2, decodeResource.getHeight() / 2, false);
        }
        this.f6063t = new int[1];
        this.f6062s = new C1545a();
        gl10.glGenTextures(1, this.f6063t, 0);
        gl10.glBindTexture(3553, this.f6063t[0]);
        gl10.glTexParameterf(3553, 10240, 9729.0f);
        gl10.glTexParameterf(3553, 10241, 9729.0f);
        GLUtils.texImage2D(3553, 0, decodeResource, 0);
        decodeResource.recycle();
    }

    public void m8544a(GL10 gl10, boolean z) {
        gl10.glBindTexture(3553, this.f6063t[0]);
        gl10.glLoadIdentity();
        if (z) {
            gl10.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
            gl10.glTranslatef(0.0f, 0.0f, -1.125f);
        } else {
            gl10.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            gl10.glTranslatef(0.0f, 0.0f, -2.0f);
        }
        gl10.glPushMatrix();
        gl10.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        gl10.glColor4f(((float) (255 - this.f6065v)) / 255.0f, ((float) (255 - this.f6065v)) / 255.0f, ((float) (255 - this.f6065v)) / 255.0f, 1.0f);
        this.f6062s.m8524a(gl10);
        gl10.glPopMatrix();
    }

    public void m8545a(GL10 gl10, boolean z, int i) {
        m8528e(this.f6069z);
        switch (i) {
            case 0:
            case 1:
            case 27:
            case 32:
            case 42:
                gl10.glBindTexture(3553, this.f6061r[0]);
                break;
            case 2:
            case 33:
            case 38:
                gl10.glBindTexture(3553, this.f6061r[1]);
                break;
            case 3:
            case 35:
                gl10.glBindTexture(3553, this.f6061r[3]);
                break;
            case 4:
            case 5:
            case 19:
            case 29:
                gl10.glBindTexture(3553, this.f6061r[4]);
                break;
            case 6:
            case 7:
            case 13:
            case 23:
                gl10.glBindTexture(3553, this.f6061r[2]);
                break;
            case 8:
            case 16:
                gl10.glBindTexture(3553, this.f6061r[7]);
                break;
            case 9:
            case 22:
                gl10.glBindTexture(3553, this.f6061r[8]);
                break;
            case 10:
            case 11:
            case 15:
                gl10.glBindTexture(3553, this.f6061r[6]);
                break;
            case 12:
                gl10.glBindTexture(3553, this.f6061r[11]);
                break;
            case 14:
                gl10.glBindTexture(3553, this.f6061r[9]);
                break;
            case 17:
                gl10.glBindTexture(3553, this.f6061r[12]);
                break;
            case 18:
            case 34:
                gl10.glBindTexture(3553, this.f6061r[10]);
                break;
            case 20:
                gl10.glBindTexture(3553, this.f6061r[23]);
                break;
            case 21:
                gl10.glBindTexture(3553, this.f6061r[5]);
                break;
            case 24:
                gl10.glBindTexture(3553, this.f6061r[13]);
                break;
            case 25:
                gl10.glBindTexture(3553, this.f6061r[14]);
                break;
            case 26:
            case 28:
            case 37:
                gl10.glBindTexture(3553, this.f6061r[15]);
                break;
            case 30:
            case 31:
                gl10.glBindTexture(3553, this.f6061r[16]);
                break;
            case 36:
                gl10.glBindTexture(3553, this.f6061r[17]);
                break;
            case 39:
                gl10.glBindTexture(3553, this.f6061r[18]);
                break;
            case 40:
                gl10.glBindTexture(3553, this.f6061r[19]);
                break;
            case 41:
                gl10.glBindTexture(3553, this.f6061r[20]);
                break;
            case 43:
                gl10.glBindTexture(3553, this.f6061r[21]);
                break;
            case 44:
                gl10.glBindTexture(3553, this.f6061r[22]);
                break;
        }
        if (z) {
            gl10.glLoadIdentity();
            gl10.glTranslatef((((-this.f6057n[i]) + this.f6058o) + this.f6040D) * ((float) this.f6046c[i].f6031d), ((-this.f6056m[i]) + this.f6039C) * ((float) this.f6046c[i].f6031d), (this.f6059p * 0.5625f) * ((float) this.f6046c[i].f6031d));
            if (i == 37 || i == 36) {
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[41] + 90.0f, 0.0f, 0.0f, 1.0f);
                if (i == 37) {
                    gl10.glTranslatef(3.25f, 0.0f, 0.0f);
                    gl10.glPushMatrix();
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 3.25f);
                } else {
                    gl10.glTranslatef(1.65f, 0.0f, 0.0f);
                    gl10.glPushMatrix();
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.65f);
                }
                gl10.glPushMatrix();
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 1.0f);
                this.f6046c[i].m8524a(gl10);
                gl10.glPopMatrix();
                gl10.glPopMatrix();
                gl10.glPopMatrix();
                return;
            } else if (i == 39) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                if (this.f6043G) {
                    if (this.f6049f % 2 == 0) {
                        if (this.f6048e <= 333) {
                            gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (this.f6048e <= 333) {
                        gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (!this.f6067x || this.f6068y) {
                    if (this.f6067x && this.f6068y) {
                        if (this.f6049f % 2 == 0) {
                            if (this.f6048e <= 333) {
                                gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                            } else if (this.f6048e <= 667) {
                                gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                            } else if (this.f6048e <= 1000) {
                                gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                            }
                        } else if (this.f6048e <= 333) {
                            gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (!this.f6067x) {
                        if (this.f6049f % 2 == 0) {
                            gl10.glScalef((((float) this.f6048e) / 1600.0f) + 0.2f, (((float) this.f6048e) / 1600.0f) + 0.2f, 1.0f);
                        } else {
                            gl10.glScalef((((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, 1.0f);
                        }
                    }
                } else if (this.f6048e < 500) {
                    gl10.glScalef((((float) this.f6048e) / 800.0f) + 0.2f, (((float) this.f6048e) / 800.0f) + 0.2f, 1.0f);
                } else {
                    gl10.glScalef((((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, 1.0f);
                }
                gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 1.0f);
                this.f6046c[i].m8524a(gl10);
                return;
            } else {
                if (i != 41 && i != 43 && i != 44) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    if (i == 0) {
                        gl10.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                    }
                } else if (i == 41) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 1.475f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                } else if (i == 43) {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 0.35f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                } else {
                    gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                    gl10.glTranslatef(0.0f, 0.51f, 0.0f);
                    gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
                if (i != 27) {
                    gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 1.0f);
                    this.f6046c[i].m8524a(gl10);
                    return;
                }
                return;
            }
        }
        gl10.glLoadIdentity();
        gl10.glTranslatef(((this.f6056m[i] + this.f6058o) + this.f6039C) * ((float) this.f6046c[i].f6031d), (this.f6057n[i] + this.f6040D) * ((float) this.f6046c[i].f6031d), (this.f6059p + this.f6041E) * ((float) this.f6046c[i].f6031d));
        if (i == 37 || i == 36) {
            gl10.glPushMatrix();
            gl10.glRotatef(this.f6060q[41] + 90.0f, 0.0f, 0.0f, 1.0f);
            if (i == 37) {
                gl10.glTranslatef(3.25f, 0.0f, 0.0f);
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 3.25f);
            } else {
                gl10.glTranslatef(1.65f, 0.0f, 0.0f);
                gl10.glPushMatrix();
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.65f);
            }
            gl10.glPushMatrix();
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 1.0f);
            this.f6046c[i].m8524a(gl10);
            gl10.glPopMatrix();
            gl10.glPopMatrix();
            gl10.glPopMatrix();
        } else if (i == 39) {
            gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            if (this.f6043G) {
                if (this.f6049f % 2 == 0) {
                    if (this.f6048e <= 333) {
                        gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (this.f6048e <= 333) {
                    gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                } else if (this.f6048e <= 667) {
                    gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                } else if (this.f6048e <= 1000) {
                    gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                }
            } else if (!this.f6067x || this.f6068y) {
                if (this.f6067x && this.f6068y) {
                    if (this.f6049f % 2 == 0) {
                        if (this.f6048e <= 333) {
                            gl10.glScalef((((float) this.f6048e) / 400.0f) + 0.2f, (((float) this.f6048e) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 667) {
                            gl10.glScalef((((float) (667 - this.f6048e)) / 400.0f) + 0.2f, (((float) (667 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                        } else if (this.f6048e <= 1000) {
                            gl10.glScalef((((float) (this.f6048e - 667)) / 400.0f) + 0.2f, (((float) (this.f6048e - 667)) / 400.0f) + 0.2f, 1.0f);
                        }
                    } else if (this.f6048e <= 333) {
                        gl10.glScalef((((float) (333 - this.f6048e)) / 400.0f) + 0.2f, (((float) (333 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 667) {
                        gl10.glScalef((((float) (this.f6048e - 333)) / 400.0f) + 0.2f, (((float) (this.f6048e - 333)) / 400.0f) + 0.2f, 1.0f);
                    } else if (this.f6048e <= 1000) {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 400.0f) + 0.2f, 1.0f);
                    }
                } else if (!this.f6067x) {
                    if (this.f6049f % 2 == 0) {
                        gl10.glScalef((((float) this.f6048e) / 1600.0f) + 0.2f, (((float) this.f6048e) / 1600.0f) + 0.2f, 1.0f);
                    } else {
                        gl10.glScalef((((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 1600.0f) + 0.2f, 1.0f);
                    }
                }
            } else if (this.f6048e < 500) {
                gl10.glScalef((((float) this.f6048e) / 800.0f) + 0.2f, (((float) this.f6048e) / 800.0f) + 0.2f, 1.0f);
            } else {
                gl10.glScalef((((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, (((float) (1000 - this.f6048e)) / 800.0f) + 0.2f, 1.0f);
            }
            gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 0.5f);
            this.f6046c[i].m8524a(gl10);
        } else {
            if (i != 41 && i != 43 && i != 44) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                if (i == 0) {
                    gl10.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                }
            } else if (i == 41) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 1.475f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            } else if (i == 43) {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 0.35f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            } else {
                gl10.glRotatef(this.f6060q[i], 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.0f, 0.51f, 0.0f);
                gl10.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            }
            gl10.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            if (i != 27) {
                gl10.glColor4f(((float) this.f6046c[i].f6028a) / 255.0f, ((float) this.f6046c[i].f6029b) / 255.0f, ((float) this.f6046c[i].f6030c) / 255.0f, 1.0f);
                this.f6046c[i].m8524a(gl10);
            }
        }
    }

    public void m8546a(boolean z) {
        if (z) {
            this.f6062s = new C1545a();
            this.f6063t = new int[1];
        }
    }

    public void m8547a(boolean z, boolean z2) {
        this.f6067x = z;
        this.f6068y = z2;
    }

    public void m8548b(float f) {
        this.f6059p = f;
    }

    public void m8549b(GL10 gl10, boolean z) {
        if (this.f6048e >= 310 && this.f6048e <= 340) {
            this.f6042F = true;
        } else if (this.f6048e >= 650 && this.f6048e <= 680) {
            this.f6042F = true;
        } else if (this.f6048e >= 980 || this.f6048e <= 20) {
            this.f6042F = true;
        } else {
            this.f6042F = false;
        }
        m8551c(gl10, z);
    }

    public void m8550b(boolean z) {
        this.f6069z = z;
        if (z) {
            this.f6056m[1] = 0.0f;
            this.f6057n[1] = 0.0f;
            this.f6056m[2] = -0.5375f;
            this.f6057n[2] = 0.6375f;
            this.f6056m[6] = 0.13f;
            this.f6057n[6] = 0.43f;
            this.f6056m[3] = -0.7f;
            this.f6057n[3] = 0.3f;
            this.f6056m[4] = -0.4175f;
            this.f6057n[4] = -0.1485f;
            this.f6056m[5] = -0.325f;
            this.f6057n[5] = -0.5425f;
            this.f6056m[0] = -0.165f;
            this.f6057n[0] = 0.405f;
            this.f6056m[21] = 0.0f;
            this.f6057n[21] = 0.0f;
            this.f6056m[15] = 0.0f;
            this.f6057n[15] = -0.43f;
            this.f6056m[10] = -0.3675f;
            this.f6057n[10] = 0.22f;
            this.f6056m[11] = 0.3675f;
            this.f6057n[11] = 0.22f;
            this.f6056m[20] = 0.0f;
            this.f6057n[20] = 0.0f;
            this.f6056m[8] = 0.44f;
            this.f6057n[8] = -0.125f;
            this.f6056m[7] = 0.44f;
            this.f6057n[7] = -0.125f;
            this.f6056m[9] = 0.695f;
            this.f6057n[9] = -0.05f;
            this.f6056m[13] = 0.71f;
            this.f6057n[13] = 0.215f;
            this.f6056m[16] = 0.71f;
            this.f6057n[16] = 0.215f;
            this.f6056m[14] = 0.63f;
            this.f6057n[14] = 0.615f;
            this.f6056m[18] = 0.63f;
            this.f6057n[18] = 0.615f;
            this.f6056m[19] = 0.45f;
            this.f6057n[19] = 0.8525f;
            this.f6056m[12] = 0.144f;
            this.f6057n[12] = 1.28f;
            this.f6056m[17] = 0.144f;
            this.f6057n[17] = 1.28f;
            this.f6056m[22] = 0.0775f;
            this.f6057n[22] = 1.175f;
            this.f6056m[23] = -0.05f;
            this.f6057n[23] = 0.95f;
            this.f6056m[24] = 0.14f;
            this.f6057n[24] = 0.805f;
            this.f6056m[25] = 0.22f;
            this.f6057n[25] = 0.68f;
            this.f6056m[26] = 0.13f;
            this.f6057n[26] = 0.43f;
            this.f6056m[27] = this.f6056m[0];
            this.f6057n[27] = this.f6057n[0];
            this.f6056m[28] = this.f6056m[2];
            this.f6057n[28] = this.f6057n[2];
            this.f6056m[29] = 0.0f;
            this.f6057n[29] = 0.0f;
            this.f6056m[30] = -0.2f;
            this.f6057n[30] = -0.35f;
            this.f6056m[31] = this.f6056m[30];
            this.f6057n[31] = this.f6057n[30];
            this.f6056m[32] = -0.69f;
            this.f6057n[32] = -0.78f;
            this.f6056m[33] = this.f6056m[32];
            this.f6057n[33] = this.f6057n[32];
            this.f6056m[34] = -0.425f;
            this.f6057n[34] = -0.91f;
            this.f6056m[35] = 0.0f;
            this.f6057n[35] = -1.1f;
            this.f6056m[38] = 0.0f;
            this.f6057n[38] = -1.1f;
            this.f6056m[37] = 0.0f;
            this.f6057n[37] = -1.1f;
            this.f6056m[36] = 0.0f;
            this.f6057n[36] = -1.1f;
            this.f6056m[39] = 0.0f;
            this.f6057n[39] = -1.1f;
            this.f6056m[40] = 0.0f;
            this.f6057n[40] = -1.1f;
            this.f6056m[42] = this.f6056m[0];
            this.f6057n[42] = this.f6057n[0];
            return;
        }
        this.f6056m[1] = 0.0f;
        this.f6057n[1] = 0.0f;
        this.f6056m[2] = -0.675f;
        this.f6057n[2] = 0.539f;
        this.f6056m[6] = -0.42f;
        this.f6057n[6] = -0.1f;
        this.f6056m[3] = -0.36f;
        this.f6057n[3] = 0.75f;
        this.f6056m[4] = 0.055f;
        this.f6057n[4] = 0.43f;
        this.f6056m[5] = 0.5f;
        this.f6057n[5] = 0.39f;
        this.f6056m[0] = -0.3975f;
        this.f6057n[0] = 0.195f;
        this.f6056m[21] = 0.0f;
        this.f6057n[21] = 0.0f;
        this.f6056m[15] = -0.21775f;
        this.f6057n[15] = -0.365f;
        this.f6056m[10] = -0.21775f;
        this.f6057n[10] = 0.365f;
        this.f6056m[11] = 0.43f;
        this.f6057n[11] = 0.0f;
        this.f6056m[20] = 0.0f;
        this.f6057n[20] = 0.0f;
        this.f6056m[8] = 0.125f;
        this.f6057n[8] = -0.44f;
        this.f6056m[7] = 0.125f;
        this.f6057n[7] = -0.44f;
        this.f6056m[9] = 0.14f;
        this.f6057n[9] = -0.705f;
        this.f6056m[13] = 0.375f;
        this.f6057n[13] = -0.82f;
        this.f6056m[16] = 0.375f;
        this.f6057n[16] = -0.82f;
        this.f6056m[14] = 0.1f;
        this.f6057n[14] = -1.125f;
        this.f6056m[18] = 0.1f;
        this.f6057n[18] = -1.125f;
        this.f6056m[19] = -0.19f;
        this.f6057n[19] = -1.14f;
        this.f6056m[12] = -0.475f;
        this.f6057n[12] = -0.6975f;
        this.f6056m[17] = -0.475f;
        this.f6057n[17] = -0.6975f;
        this.f6056m[22] = -0.52f;
        this.f6057n[22] = -0.58f;
        this.f6056m[23] = -0.775f;
        this.f6057n[23] = -0.5f;
        this.f6056m[24] = -0.785f;
        this.f6057n[24] = -0.27f;
        this.f6056m[25] = -0.64f;
        this.f6057n[25] = -0.25f;
        this.f6056m[26] = this.f6056m[6];
        this.f6057n[26] = this.f6057n[6];
        this.f6056m[27] = this.f6056m[0];
        this.f6057n[27] = this.f6057n[0];
        this.f6056m[28] = this.f6056m[2];
        this.f6057n[28] = this.f6057n[2];
        this.f6056m[29] = 0.0f;
        this.f6057n[29] = 0.0f;
        this.f6056m[30] = 0.33f;
        this.f6057n[30] = 0.225f;
        this.f6056m[31] = this.f6056m[30];
        this.f6057n[31] = this.f6057n[30];
        this.f6056m[32] = 0.805f;
        this.f6057n[32] = 0.705f;
        this.f6056m[33] = this.f6056m[32];
        this.f6057n[33] = this.f6057n[32];
        this.f6056m[34] = 0.7525f;
        this.f6057n[34] = 0.9875f;
        this.f6056m[35] = 0.3f;
        this.f6057n[35] = 1.1f;
        this.f6056m[38] = 0.3f;
        this.f6057n[38] = 1.1f;
        this.f6056m[37] = 0.3f;
        this.f6057n[37] = 1.1f;
        this.f6056m[36] = 0.3f;
        this.f6057n[36] = 1.1f;
        this.f6056m[39] = 0.3f;
        this.f6057n[39] = 1.1f;
        this.f6056m[40] = 0.3f;
        this.f6057n[40] = 1.1f;
        this.f6056m[42] = this.f6056m[0];
        this.f6057n[42] = this.f6057n[0];
    }

    public void m8551c(GL10 gl10, boolean z) {
        if (!this.f6044a && this.f6045b) {
            m8527e(gl10, z);
        }
        m8545a(gl10, z, 0);
        m8545a(gl10, z, 5);
        m8545a(gl10, z, 2);
        m8545a(gl10, z, 7);
        m8545a(gl10, z, 9);
        m8545a(gl10, z, 13);
        m8545a(gl10, z, 32);
        m8545a(gl10, z, 30);
        if (!this.f6044a && this.f6045b) {
            m8529f(gl10, z);
        }
        m8545a(gl10, z, 3);
        m8545a(gl10, z, 4);
        m8545a(gl10, z, 8);
        m8545a(gl10, z, 16);
        m8545a(gl10, z, 14);
        m8545a(gl10, z, 28);
        m8545a(gl10, z, 33);
        m8545a(gl10, z, 34);
        m8545a(gl10, z, 35);
        if (!this.f6044a && this.f6045b) {
            m8530g(gl10, z);
        }
        m8545a(gl10, z, 18);
        m8545a(gl10, z, 19);
        m8545a(gl10, z, 12);
        m8545a(gl10, z, 36);
        if (!this.f6044a && this.f6045b) {
            m8531h(gl10, z);
        }
        m8545a(gl10, z, 6);
        m8545a(gl10, z, 1);
        m8545a(gl10, z, 17);
        m8545a(gl10, z, 22);
        m8545a(gl10, z, 23);
        m8545a(gl10, z, 24);
        m8545a(gl10, z, 25);
        m8545a(gl10, z, 38);
        m8545a(gl10, z, 37);
        if (!this.f6044a && this.f6045b) {
            m8532i(gl10, z);
        }
        m8545a(gl10, z, 39);
        if (!this.f6044a && this.f6045b) {
            m8533j(gl10, z);
        }
        m8545a(gl10, z, 40);
        if (!this.f6044a && this.f6045b) {
            m8534k(gl10, z);
        }
        if (!this.f6044a && this.f6045b) {
            m8535l(gl10, z);
        }
        m8545a(gl10, z, 15);
        m8545a(gl10, z, 10);
        m8545a(gl10, z, 11);
        m8545a(gl10, z, 41);
        m8545a(gl10, z, 21);
        m8545a(gl10, z, 20);
        m8545a(gl10, z, 31);
        if (!this.f6044a && this.f6045b) {
            m8536m(gl10, z);
        }
        m8545a(gl10, z, 26);
        m8545a(gl10, z, 27);
        m8545a(gl10, z, 42);
        m8545a(gl10, z, 29);
        m8526b(gl10, z, 43);
        m8545a(gl10, z, 43);
        m8526b(gl10, z, 44);
        m8545a(gl10, z, 44);
    }

    public void m8552c(boolean z) {
    }

    public void m8553d(GL10 gl10, boolean z) {
        gl10.glGenTextures(24, this.f6061r, 0);
        this.f6066w = z;
    }

    public void m8554d(boolean z) {
        this.f6043G = z;
        if (!this.f6043G) {
            this.f6042F = true;
        }
    }
}
