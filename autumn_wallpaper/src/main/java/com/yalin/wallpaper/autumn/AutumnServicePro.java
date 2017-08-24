package com.yalin.wallpaper.autumn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yalin.style.engine.WallpaperServiceProxy;
import com.yalin.wallpaper.autumn.motion.Leaves01;
import com.yalin.wallpaper.autumn.motion.Leaves02;
import com.yalin.wallpaper.autumn.motion.Leaves03;
import com.yalin.wallpaper.autumn.motion.Leaves04;
import com.yalin.wallpaper.autumn.motion.Leaves05;
import com.yalin.wallpaper.autumn.motion.Leaves06;
import com.yalin.wallpaper.autumn.motion.MotionBubble;
import com.yalin.wallpaper.autumn.motion.MotionObject;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AutumnServicePro extends WallpaperServiceProxy {

    public AutumnServicePro(Context host) {
        super(host);
    }

    public class AutumnEngine extends ActiveEngine {
        public boolean alowBubble;
        public Bitmap backgroundImage;
        private int bgHeight = 0;
        private int bgWidth = 0;
        public Bitmap bubbleBitmap;
        public Bitmap bubbleMax;
        public Bitmap bubbleMaxBroken1;
        public Bitmap bubbleMaxBroken2;
        public Bitmap bubbleMin;
        public Bitmap bubbleMinBroken1;
        public Bitmap bubbleMinBroken2;
        private int cHeight = 0;
        private int cWidth = 0;
        private Context context;
        public float factor = 0.0f;
        public float factorB = 1.0f;
        public boolean firstFactorBubble = true;
        public boolean firstFactorLeaves = true;
        private SurfaceHolder holder;
        public int imageBubble;
        private Set<Leaves01> leaves01 = new HashSet<>();
        public float leaves01X;
        public float leaves01Y;
        private Set<Leaves02> leaves02 = new HashSet<>();
        public float leaves02X;
        public float leaves02Y;
        private Set<Leaves03> leaves03 = new HashSet<>();
        public float leaves03X;
        public float leaves03Y;
        private Set<Leaves04> leaves04 = new HashSet<>();
        public float leaves04X;
        public float leaves04Y;
        private Set<Leaves05> leaves05 = new HashSet<>();
        public float leaves05X;
        public float leaves05Y;
        private Set<Leaves06> leaves06 = new HashSet<>();
        public float leaves06X;
        public float leaves06Y;
        public Bitmap leavesBitmap01;
        public Bitmap leavesBitmap02;
        public Bitmap leavesBitmap03;
        public Bitmap leavesBitmap04;
        public Bitmap leavesBitmap05;
        public Bitmap leavesBitmap06;
        private float leftWidth = 0.0f;
        public Matrix localMatrix = new Matrix();
        private int mActivePointerId = -1;
        private float mLastTouchX;
        private Set<MotionBubble> motionBubble = new HashSet<>();
        private int newWidth = 0;
        public int numBackground;
        public int numBackgroundOld;
        public int numBubble;
        public int numBubbleOld;
        public int numLeaves01;
        public int numLeaves01Old;
        public int numLeaves02;
        public int numLeaves02Old;
        public int numLeaves03;
        public int numLeaves03Old;
        public int numLeaves04;
        public int numLeaves04Old;
        public int numLeaves05;
        public int numLeaves05Old;
        public int numLeaves06;
        public int numLeaves06Old;
        private Random random = new Random();
        private int sBubble1;
        private int sBubble2;
        public boolean slideBackground;
        public boolean soundBubble;
        private SoundPool soundBubblePlay = new SoundPool(10, 3, 0);
        private AutumnThread thread;
        public int timeBackground;
        public long timeChangeBackground;
        public int touchTime;
        public float touchWidth = 160.0f;
        private boolean visible = true;
        private int wind = 0;
        public float xTouch;
        public float yTouch;

        public class AutumnThread extends Thread {
            private boolean isLimited = true;
            private boolean isRunning = true;
            private long numTimeAddBubble = 1200;
            private long numTimeAddLeaves01 = 1000;
            private long numTimeAddLeaves02 = 2000;
            private long numTimeAddLeaves03 = 2500;
            private long numTimeAddLeaves04 = 3000;
            private long numTimeAddLeaves05 = 2300;
            private long numTimeAddLeaves06 = 3500;
            private long timeLastBubble = 0;
            private long timeLastLeaves01 = 0;
            private long timeLastLeaves02 = 0;
            private long timeLastLeaves03 = 0;
            private long timeLastLeaves04 = 0;
            private long timeLastLeaves05 = 0;
            private long timeLastLeaves06 = 0;

            private void updateWallpaper(Canvas c) {
                long nowTime = System.currentTimeMillis();
                if (AutumnEngine.this.slideBackground && nowTime - AutumnEngine.this.timeChangeBackground > ((long) (AutumnEngine.this.timeBackground * 1000))) {
                    AutumnEngine.this.numBackground = AutumnEngine.this.random.nextInt(9) + 1;
                }
                if (AutumnEngine.this.backgroundImage == null || AutumnEngine.this.numBackgroundOld != AutumnEngine.this.numBackground) {
                    AutumnEngine.this.cWidth = c.getWidth();
                    AutumnEngine.this.cHeight = c.getHeight();
                    Options options = new Options();
                    options.inScaled = false;
                    if (AutumnEngine.this.cWidth > AutumnEngine.this.cHeight) {
                        AutumnEngine.this.touchWidth = 200.0f;
                    } else {
                        AutumnEngine.this.touchWidth = 160.0f;
                    }
                    switch (AutumnEngine.this.numBackground) {
                        case 1:
                            if (AutumnEngine.this.cWidth < AutumnEngine.this.cHeight) {
                                if (AutumnEngine.this.cHeight >= 500) {
                                    AutumnEngine.this.backgroundImage = BitmapFactory.decodeResource(AutumnServicePro.this.getResources(), R.drawable.autumn_wallpaper_1_small, options);
                                    break;
                                }
                                AutumnEngine.this.backgroundImage = BitmapFactory.decodeResource(AutumnServicePro.this.getResources(), R.drawable.autumn_wallpaper_1_240_360, options);
                                break;
                            }
                            AutumnEngine.this.backgroundImage = BitmapFactory.decodeResource(AutumnServicePro.this.getResources(), R.drawable.autumn_wallpaper_1, options);
                            break;
                    }
                    if (AutumnEngine.this.cWidth < AutumnEngine.this.cHeight && AutumnEngine.this.cHeight < 500) {
                        AutumnEngine.this.factorB = 0.3f;
                    }
                    if (AutumnEngine.this.cWidth > AutumnEngine.this.cHeight) {
                        AutumnEngine.this.factorB = 0.6f;
                    }
                    AutumnEngine.this.bgWidth = AutumnEngine.this.backgroundImage.getWidth();
                    AutumnEngine.this.bgHeight = AutumnEngine.this.backgroundImage.getHeight();
                    AutumnEngine.this.factor = ((float) AutumnEngine.this.cHeight) / ((float) AutumnEngine.this.bgHeight);
                    AutumnEngine.this.newWidth = (int) (((float) AutumnEngine.this.bgWidth) * AutumnEngine.this.factor);
                    AutumnEngine.this.leftWidth = (float) ((AutumnEngine.this.cWidth - AutumnEngine.this.newWidth) / 2);
                    if (AutumnEngine.this.newWidth < AutumnEngine.this.cWidth) {
                        AutumnEngine.this.newWidth = AutumnEngine.this.cWidth;
                        AutumnEngine.this.leftWidth = 0.0f;
                    }
                    AutumnEngine.this.backgroundImage = Bitmap.createScaledBitmap(AutumnEngine.this.backgroundImage, AutumnEngine.this.newWidth, AutumnEngine.this.cHeight, true);
                    AutumnEngine.this.numBackgroundOld = AutumnEngine.this.numBackground;
                    AutumnEngine.this.timeChangeBackground = nowTime;
                    AutumnEngine autumnEngine = AutumnEngine.this;
                    autumnEngine.factor *= AutumnEngine.this.factorB;
                    AutumnEngine.this.touchWidth = (AutumnEngine.this.touchWidth * AutumnEngine.this.factor) * 1.5f;
                }
                c.drawBitmap(AutumnEngine.this.backgroundImage, AutumnEngine.this.leftWidth, 0.0f, null);
                if (AutumnEngine.this.numBubble < AutumnEngine.this.numBubbleOld) {
                    AutumnEngine.this.motionBubble.clear();
                }
                if (AutumnEngine.this.numLeaves01 < AutumnEngine.this.numLeaves01Old) {
                    AutumnEngine.this.leaves01.clear();
                }
                if (AutumnEngine.this.numLeaves02 < AutumnEngine.this.numLeaves02Old) {
                    AutumnEngine.this.leaves02.clear();
                }
                if (AutumnEngine.this.numLeaves03 < AutumnEngine.this.numLeaves03Old) {
                    AutumnEngine.this.leaves03.clear();
                }
                if (AutumnEngine.this.numLeaves04 < AutumnEngine.this.numLeaves04Old) {
                    AutumnEngine.this.leaves04.clear();
                }
                if (AutumnEngine.this.numLeaves05 < AutumnEngine.this.numLeaves05Old) {
                    AutumnEngine.this.leaves05.clear();
                }
                if (AutumnEngine.this.numLeaves06 < AutumnEngine.this.numLeaves06Old) {
                    AutumnEngine.this.leaves06.clear();
                }
                AutumnEngine.this.numBubbleOld = AutumnEngine.this.numBubble;
                AutumnEngine.this.numLeaves01Old = AutumnEngine.this.numLeaves01;
                AutumnEngine.this.numLeaves02Old = AutumnEngine.this.numLeaves02;
                AutumnEngine.this.numLeaves03Old = AutumnEngine.this.numLeaves03;
                AutumnEngine.this.numLeaves04Old = AutumnEngine.this.numLeaves04;
                AutumnEngine.this.numLeaves05Old = AutumnEngine.this.numLeaves05;
                AutumnEngine.this.numLeaves06Old = AutumnEngine.this.numLeaves06;
                if (AutumnEngine.this.leaves01.size() < AutumnEngine.this.numLeaves01 && nowTime - this.timeLastLeaves01 > this.numTimeAddLeaves01) {
                    AutumnEngine.this.leaves01.add(new Leaves01(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves01 = nowTime;
                }
                if (AutumnEngine.this.leaves02.size() < AutumnEngine.this.numLeaves02 && nowTime - this.timeLastLeaves02 > this.numTimeAddLeaves02) {
                    AutumnEngine.this.leaves02.add(new Leaves02(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves02 = nowTime;
                }
                if (AutumnEngine.this.leaves03.size() < AutumnEngine.this.numLeaves03 && nowTime - this.timeLastLeaves03 > this.numTimeAddLeaves03) {
                    AutumnEngine.this.leaves03.add(new Leaves03(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves03 = nowTime;
                }
                if (AutumnEngine.this.leaves04.size() < AutumnEngine.this.numLeaves04 && nowTime - this.timeLastLeaves04 > this.numTimeAddLeaves04) {
                    AutumnEngine.this.leaves04.add(new Leaves04(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves04 = nowTime;
                }
                if (AutumnEngine.this.leaves05.size() < AutumnEngine.this.numLeaves05 && nowTime - this.timeLastLeaves05 > this.numTimeAddLeaves05) {
                    AutumnEngine.this.leaves05.add(new Leaves05(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves05 = nowTime;
                }
                if (AutumnEngine.this.leaves06.size() < AutumnEngine.this.numLeaves06 && nowTime - this.timeLastLeaves06 > this.numTimeAddLeaves06) {
                    AutumnEngine.this.leaves06.add(new Leaves06(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight));
                    this.timeLastLeaves06 = nowTime;
                }
                if (AutumnEngine.this.alowBubble && AutumnEngine.this.motionBubble.size() < AutumnEngine.this.numBubble && nowTime - this.timeLastBubble > this.numTimeAddBubble) {
                    AutumnEngine.this.motionBubble.add(new MotionBubble(AutumnEngine.this.context, AutumnEngine.this.cWidth, AutumnEngine.this.cHeight, AutumnEngine.this.motionBubble.size() + 1, AutumnEngine.this.factor * 1.5f));
                    this.timeLastBubble = nowTime;
                }
                if (AutumnEngine.this.touchTime > 0) {
                    AutumnEngine.this.touchTime--;
                } else {
                    AutumnEngine.this.wind = 0;
                }
                if (AutumnEngine.this.firstFactorBubble) {
                    float factorBubble = AutumnEngine.this.factor * 1.5f;
                    AutumnEngine.this.bubbleMax = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMax);
                    AutumnEngine.this.bubbleMaxBroken1 = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMaxBroken1);
                    AutumnEngine.this.bubbleMaxBroken2 = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMaxBroken2);
                    AutumnEngine.this.bubbleMin = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMin);
                    AutumnEngine.this.bubbleMinBroken1 = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMinBroken1);
                    AutumnEngine.this.bubbleMinBroken2 = MotionObject.creatFromScale(factorBubble, AutumnEngine.this.bubbleMinBroken2);
                    AutumnEngine.this.firstFactorBubble = false;
                }
                if (AutumnEngine.this.firstFactorLeaves) {
                    float factorLeaves = AutumnEngine.this.factor;
                    AutumnEngine.this.leavesBitmap01 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap01);
                    AutumnEngine.this.leavesBitmap02 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap02);
                    AutumnEngine.this.leavesBitmap03 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap03);
                    AutumnEngine.this.leavesBitmap04 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap04);
                    AutumnEngine.this.leavesBitmap05 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap05);
                    AutumnEngine.this.leavesBitmap06 = MotionObject.creatFromScale(factorLeaves, AutumnEngine.this.leavesBitmap06);
                    AutumnEngine.this.leaves01X = (float) (AutumnEngine.this.leavesBitmap01.getWidth() / 2);
                    AutumnEngine.this.leaves01Y = (float) (AutumnEngine.this.leavesBitmap01.getHeight() / 2);
                    AutumnEngine.this.leaves02X = (float) (AutumnEngine.this.leavesBitmap02.getWidth() / 2);
                    AutumnEngine.this.leaves02Y = (float) (AutumnEngine.this.leavesBitmap02.getHeight() / 2);
                    AutumnEngine.this.leaves03X = (float) (AutumnEngine.this.leavesBitmap03.getWidth() / 2);
                    AutumnEngine.this.leaves03Y = (float) (AutumnEngine.this.leavesBitmap03.getHeight() / 2);
                    AutumnEngine.this.leaves04X = (float) (AutumnEngine.this.leavesBitmap04.getWidth() / 2);
                    AutumnEngine.this.leaves04Y = (float) (AutumnEngine.this.leavesBitmap04.getHeight() / 2);
                    AutumnEngine.this.leaves05X = (float) (AutumnEngine.this.leavesBitmap05.getWidth() / 2);
                    AutumnEngine.this.leaves05Y = (float) (AutumnEngine.this.leavesBitmap05.getHeight() / 2);
                    AutumnEngine.this.leaves06X = (float) (AutumnEngine.this.leavesBitmap06.getWidth() / 2);
                    AutumnEngine.this.leaves06Y = (float) (AutumnEngine.this.leavesBitmap06.getHeight() / 2);
                    AutumnEngine.this.firstFactorLeaves = false;
                }
                if (!(AutumnEngine.this.alowBubble || AutumnEngine.this.motionBubble.size() == 0)) {
                    AutumnEngine.this.motionBubble.clear();
                }
                for (Leaves01 imageOb : AutumnEngine.this.leaves01) {
                    imageOb.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb.rotate, AutumnEngine.this.leaves01X, AutumnEngine.this.leaves01Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb.f7x, imageOb.f8y);
                    if (imageOb.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap01, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (Leaves02 imageOb2 : AutumnEngine.this.leaves02) {
                    imageOb2.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb2.rotate, AutumnEngine.this.leaves02X, AutumnEngine.this.leaves02Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb2.f9x, imageOb2.f10y);
                    if (imageOb2.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap02, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (Leaves03 imageOb3 : AutumnEngine.this.leaves03) {
                    imageOb3.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb3.rotate, AutumnEngine.this.leaves03X, AutumnEngine.this.leaves03Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb3.f11x, imageOb3.f12y);
                    if (imageOb3.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap03, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (Leaves04 imageOb4 : AutumnEngine.this.leaves04) {
                    imageOb4.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb4.rotate, AutumnEngine.this.leaves04X, AutumnEngine.this.leaves04Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb4.f13x, imageOb4.f14y);
                    if (imageOb4.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap04, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (Leaves05 imageOb5 : AutumnEngine.this.leaves05) {
                    imageOb5.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb5.rotate, AutumnEngine.this.leaves05X, AutumnEngine.this.leaves05Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb5.f15x, imageOb5.f16y);
                    if (imageOb5.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap05, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (Leaves06 imageOb6 : AutumnEngine.this.leaves06) {
                    imageOb6.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    AutumnEngine.this.localMatrix.reset();
                    AutumnEngine.this.localMatrix.setRotate((float) imageOb6.rotate, AutumnEngine.this.leaves06X, AutumnEngine.this.leaves06Y);
                    AutumnEngine.this.localMatrix.postTranslate(imageOb6.f17x, imageOb6.f18y);
                    if (imageOb6.visible) {
                        c.drawBitmap(AutumnEngine.this.leavesBitmap06, AutumnEngine.this.localMatrix, null);
                    }
                }
                for (MotionBubble imageOb7 : AutumnEngine.this.motionBubble) {
                    imageOb7.updateTouch(AutumnEngine.this.wind, AutumnEngine.this.touchWidth, AutumnEngine.this.touchTime, AutumnEngine.this.xTouch, AutumnEngine.this.yTouch);
                    if (imageOb7.timeBroken != 0) {
                        if (imageOb7.timeBroken == 14 && AutumnEngine.this.soundBubble) {
                            if (imageOb7.sizeMax) {
                                AutumnEngine.this.soundBubblePlay.play(AutumnEngine.this.sBubble2, 1.0f, 1.0f, 0, 0, 1.0f);
                            } else {
                                AutumnEngine.this.soundBubblePlay.play(AutumnEngine.this.sBubble1, 1.0f, 1.0f, 0, 0, 1.0f);
                            }
                        }
                        if (imageOb7.timeBroken > 7) {
                            if (imageOb7.sizeMax) {
                                AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMaxBroken1;
                            } else {
                                AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMinBroken1;
                            }
                        } else if (imageOb7.sizeMax) {
                            AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMaxBroken2;
                        } else {
                            AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMinBroken2;
                        }
                        c.drawBitmap(AutumnEngine.this.bubbleBitmap, imageOb7.f19x, imageOb7.f20y, null);
                    } else if (imageOb7.visible) {
                        if (imageOb7.sizeMax) {
                            AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMax;
                        } else {
                            AutumnEngine.this.bubbleBitmap = AutumnEngine.this.bubbleMin;
                        }
                        c.drawBitmap(AutumnEngine.this.bubbleBitmap, imageOb7.f19x, imageOb7.f20y, null);
                    }
                }
            }

            public void run() {
                while (isRunning()) {
                    Canvas canvas = null;
                    try {
                        canvas = AutumnEngine.this.holder.lockCanvas();
                        synchronized (AutumnEngine.this.holder) {
                            updateWallpaper(canvas);
                        }
                        if (canvas != null) {
                            AutumnEngine.this.holder.unlockCanvasAndPost(canvas);
                        }
                    } catch (IllegalArgumentException e) {
                        try {
                            e.printStackTrace();
                            if (canvas != null) {
                                AutumnEngine.this.holder.unlockCanvasAndPost(canvas);
                            }
                        } catch (Throwable th) {
                            if (canvas != null) {
                                AutumnEngine.this.holder.unlockCanvasAndPost(canvas);
                            }
                        }
                    } catch (NullPointerException e2) {
                        e2.printStackTrace();
                        if (canvas != null) {
                            AutumnEngine.this.holder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

            public boolean isRunning() {
                return this.isRunning;
            }

            public void stopRunning() {
                this.isRunning = false;
            }

            public void startRunning() {
                this.isRunning = true;
            }

            public boolean isLimited() {
                return this.isLimited;
            }

            public void stopLimited() {
                this.isLimited = false;
            }
        }

        public AutumnEngine() {
            super();
            this.context = AutumnServicePro.this.getBaseContext();
            this.numBackground = 1;
            this.slideBackground = false;
            this.timeBackground = 20;
            this.alowBubble = true;
            this.imageBubble = 1;
            this.numBubble = 15;
            this.soundBubble = true;
            Options options = new Options();
            options.inScaled = false;
            this.bubbleMax = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_80_1, options);
            this.bubbleMaxBroken1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_broken_80_1, options);
            this.bubbleMaxBroken2 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_broken_80_2, options);
            this.bubbleMin = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_60_1, options);
            this.bubbleMinBroken1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_broken_60_1, options);
            this.bubbleMinBroken2 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble_broken_60_2, options);
            this.numLeaves01 = 20;
            this.numLeaves02 = 20;
            this.numLeaves03 = 20;
            this.numLeaves04 = 20;
            this.numLeaves05 = 20;
            this.numLeaves06 = 20;
            this.leavesBitmap01 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_11, options);
            this.leavesBitmap02 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_12, options);
            this.leavesBitmap03 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_13, options);
            this.leavesBitmap04 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_14, options);
            this.leavesBitmap05 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_15, options);
            this.leavesBitmap06 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.leaves_16, options);
            this.sBubble2 = this.soundBubblePlay.load(this.context, R.raw.bubble2, 1);
            this.sBubble1 = this.soundBubblePlay.load(this.context, R.raw.bubble, 1);
        }

        public void onDestroy() {
            this.thread.stopRunning();
            System.gc();
            super.onDestroy();
        }

        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            super.onVisibilityChanged(visible);
            if (this.visible) {
                this.thread = new AutumnThread();
                this.thread.start();
                return;
            }
            this.thread.stopRunning();
        }

        public void onSurfaceCreated(SurfaceHolder holders) {
            this.holder = holders;
        }

        public void onSurfaceDestroyed(SurfaceHolder holders) {
            boolean retry = true;
            this.visible = false;
            this.thread.stopRunning();
            while (retry) {
                try {
                    this.thread.join();
                    super.onSurfaceDestroyed(holders);
                    this.leaves01.clear();
                    this.leaves02.clear();
                    this.leaves03.clear();
                    this.leaves04.clear();
                    this.leaves05.clear();
                    this.leaves06.clear();
                    this.motionBubble.clear();
                    this.backgroundImage.recycle();
                    this.leavesBitmap01.recycle();
                    this.leavesBitmap02.recycle();
                    this.leavesBitmap03.recycle();
                    this.leavesBitmap04.recycle();
                    this.leavesBitmap05.recycle();
                    this.leavesBitmap06.recycle();
                    this.bubbleBitmap.recycle();
                    this.bubbleMax.recycle();
                    this.bubbleMin.recycle();
                    this.bubbleMaxBroken1.recycle();
                    this.bubbleMaxBroken2.recycle();
                    this.bubbleMinBroken1.recycle();
                    this.bubbleMinBroken2.recycle();
                    retry = false;
                    System.gc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception localException) {
                    localException.printStackTrace();
                }
            }
        }

        public void onTouchEvent(MotionEvent ev) {
            if (ev != null) {
                int action = ev.getAction();
                this.mActivePointerId = ev.getPointerId(0);
                int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                switch (action & 255) {
                    case 0:
                        this.xTouch = ev.getX(pointerIndex);
                        this.yTouch = ev.getY(pointerIndex);
                        this.touchTime = 10;
                        this.mLastTouchX = ev.getX(pointerIndex);
                        return;
                    case 1:
                        this.xTouch = ev.getX(pointerIndex);
                        this.yTouch = ev.getY(pointerIndex);
                        this.touchTime = 10;
                        return;
                    case 2:
                        float x = ev.getX(pointerIndex);
                        this.xTouch = ev.getX(pointerIndex);
                        this.yTouch = ev.getY(pointerIndex);
                        this.touchTime = 10;
                        float dx = x - this.mLastTouchX;
                        if (dx > this.touchWidth) {
                            this.wind += 2;
                            return;
                        } else if (this.touchWidth + dx < 0.0f) {
                            this.wind -= 2;
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    public Engine onCreateEngine() {
        return new AutumnEngine();
    }
}
