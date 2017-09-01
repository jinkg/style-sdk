package com.yalin.wallpaper.electric_plasma.gl;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.EGLContextFactory;
import android.opengl.GLSurfaceView.EGLWindowSurfaceFactory;
import android.opengl.GLSurfaceView.GLWrapper;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class NewGLWallpaperService extends WallpaperService {

    public class GLEngine extends Engine {
        public static final int RENDERMODE_CONTINUOUSLY = 1;
        public static final int RENDERMODE_WHEN_DIRTY = 0;
        private int debugFlags;
        private Object lock = new Object();
        private GLSurfaceView mGLSurfaceView = null;
        private List<Runnable> pendingOperations = new ArrayList<>();
        private int renderMode;

        public GLEngine() {
            super();
        }

        public void setGLWrapper(final GLWrapper glWrapper) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setGLWrapper(glWrapper);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setGLWrapper(glWrapper);
                        }
                    });
                }
            }
        }

        public void setDebugFlags(final int debugFlags) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setDebugFlags(debugFlags);
                } else {
                    this.debugFlags = debugFlags;
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setDebugFlags(debugFlags);
                        }
                    });
                }
            }
        }

        public int getDebugFlags() {
            int debugFlags;
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    debugFlags = this.mGLSurfaceView.getDebugFlags();
                } else {
                    debugFlags = this.debugFlags;
                }
            }
            return debugFlags;
        }

        public void setRenderer(final Renderer renderer) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setRenderer(renderer);
                    if (!isVisible()) {
                        this.mGLSurfaceView.onPause();
                    }
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setRenderer(renderer);
                        }
                    });
                }
            }
        }

        public void queueEvent(final Runnable r) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.queueEvent(r);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.queueEvent(r);
                        }
                    });
                }
            }
        }

        public void setEGLContextFactory(final EGLContextFactory factory) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setEGLContextFactory(factory);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLContextFactory(factory);
                        }
                    });
                }
            }
        }

        public void setEGLWindowSurfaceFactory(final EGLWindowSurfaceFactory factory) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setEGLWindowSurfaceFactory(factory);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLWindowSurfaceFactory(factory);
                        }
                    });
                }
            }
        }

        public void setEGLConfigChooser(final EGLConfigChooser configChooser) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setEGLConfigChooser(configChooser);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLConfigChooser(configChooser);
                        }
                    });
                }
            }
        }

        public void setEGLConfigChooser(final boolean needDepth) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setEGLConfigChooser(needDepth);
                } else {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLConfigChooser(needDepth);
                        }
                    });
                }
            }
        }

        public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setEGLConfigChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize);
                } else {
                    final int i = redSize;
                    final int i2 = greenSize;
                    final int i3 = blueSize;
                    final int i4 = alphaSize;
                    final int i5 = depthSize;
                    final int i6 = stencilSize;
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLConfigChooser(i, i2, i3, i4, i5, i6);
                        }
                    });
                }
            }
        }

        public void setEGLContextClientVersion(final int version) {
            synchronized (this.lock) {
                try {
                    Method method = GLSurfaceView.class.getMethod("setEGLContextClientVersion", new Class[]{Integer.TYPE});
                    if (this.mGLSurfaceView != null) {
                        try {
                            method.invoke(this.mGLSurfaceView, new Object[]{Integer.valueOf(version)});
                        } catch (IllegalAccessException e) {
                            return;
                        } catch (InvocationTargetException e2) {
                            return;
                        }
                    }
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setEGLContextClientVersion(version);
                        }
                    });
                } catch (NoSuchMethodException e3) {
                }
            }
        }

        public void setRenderMode(final int renderMode) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.setRenderMode(renderMode);
                } else {
                    this.renderMode = renderMode;
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.setRenderMode(renderMode);
                        }
                    });
                }
            }
        }

        public int getRenderMode() {
            int renderMode;
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    renderMode = this.mGLSurfaceView.getRenderMode();
                } else {
                    renderMode = this.renderMode;
                }
            }
            return renderMode;
        }

        public void requestRender() {
            if (this.mGLSurfaceView != null) {
                this.mGLSurfaceView.requestRender();
            }
        }

        public void onVisibilityChanged(final boolean visible) {
            super.onVisibilityChanged(visible);
            synchronized (this.lock) {
                if (this.mGLSurfaceView == null) {
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            if (visible) {
                                GLEngine.this.mGLSurfaceView.onResume();
                            } else {
                                GLEngine.this.mGLSurfaceView.onPause();
                            }
                        }
                    });
                } else if (visible) {
                    this.mGLSurfaceView.onResume();
                } else {
                    this.mGLSurfaceView.onPause();
                }
            }
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.surfaceChanged(holder, format, width, height);
                } else {
                    final SurfaceHolder surfaceHolder = holder;
                    final int i = format;
                    final int i2 = width;
                    final int i3 = height;
                    this.pendingOperations.add(new Runnable() {
                        public void run() {
                            GLEngine.this.onSurfaceChanged(surfaceHolder, i, i2, i3);
                        }
                    });
                }
            }
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView == null) {
                    this.mGLSurfaceView = new GLSurfaceView(NewGLWallpaperService.this) {
                        public SurfaceHolder getHolder() {
                            return GLEngine.this.getSurfaceHolder();
                        }
                    };
                    for (Runnable pendingOperation : this.pendingOperations) {
                        pendingOperation.run();
                    }
                    this.pendingOperations.clear();
                }
                this.mGLSurfaceView.surfaceCreated(holder);
            }
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            synchronized (this.lock) {
                if (this.mGLSurfaceView != null) {
                    this.mGLSurfaceView.surfaceDestroyed(holder);
                }
            }
        }
    }
}
