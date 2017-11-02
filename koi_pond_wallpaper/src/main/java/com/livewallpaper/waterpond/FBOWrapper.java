package com.livewallpaper.waterpond;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.livewallpaper.utils.GLLimit;

public class FBOWrapper {
    private FrameBuffer fbo = null;
    protected final float height;
    private boolean inited = false;
    protected final float width;

    public FBOWrapper(float width, float height) {
        GLLimit gpuLimits = GLLimit.instance();
        float maxColorBufferSizeAllowed = (float) Math.min(gpuLimits.maxRenderbufferSize, gpuLimits.maxTextureSize);
        float fboMaxSize = Math.max(width, height);
        if (fboMaxSize > maxColorBufferSizeAllowed) {
            float scale = maxColorBufferSizeAllowed / fboMaxSize;
            width = (float) Math.round(width * scale);
            height = (float) Math.round(scale * height);
        }
        this.width = width;
        this.height = height;
    }

    public void dispose() {
        if (this.fbo != null) {
            this.fbo.dispose();
            this.fbo = null;
        }
    }

    public void create(Format format) {
        if (this.fbo != null) {
            this.fbo.dispose();
        }
        this.fbo = new FrameBuffer(format, (int) this.width, (int) this.height, true);
        this.inited = true;
    }

    public final void begin() {
        if (this.inited) {
            this.fbo.begin();
        }
    }

    public final void end() {
        if (this.inited) {
            this.fbo.end();
        }
    }

    public final void reset() {
        this.inited = false;
        if (this.fbo != null) {
            this.fbo.dispose();
            this.fbo = null;
        }
    }

    public final Texture getColorBufferTexture() {
        if (this.fbo == null) {
            return null;
        }
        return this.fbo.getColorBufferTexture();
    }
}
