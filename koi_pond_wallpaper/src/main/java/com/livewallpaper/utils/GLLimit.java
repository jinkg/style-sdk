package com.livewallpaper.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class GLLimit {
    private static GLLimit instance = null;
    public final int depthBits;
    public final int maxRenderbufferSize;
    public final int maxTextureImageUnits;
    public final int maxTextureSize;
    public final int maxVertexAttribs;
    public final int maxVertexTextureImageUnits;
    public final int stencilBits;

    private GLLimit() {
        ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(512);
        localByteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer localIntBuffer = localByteBuffer.asIntBuffer();
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, localIntBuffer);
        this.maxTextureSize = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_MAX_RENDERBUFFER_SIZE, localIntBuffer);
        this.maxRenderbufferSize = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_DEPTH_BITS, localIntBuffer);
        this.depthBits = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_STENCIL_BITS, localIntBuffer);
        this.stencilBits = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_MAX_VERTEX_ATTRIBS, localIntBuffer);
        this.maxVertexAttribs = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, localIntBuffer);
        this.maxVertexTextureImageUnits = localIntBuffer.get(0);
        localIntBuffer.rewind();
        Gdx.gl20.glGetIntegerv(GL20.GL_MAX_TEXTURE_IMAGE_UNITS, localIntBuffer);
        this.maxTextureImageUnits = localIntBuffer.get(0);
        Gdx.gl20.glGetError();
    }

    public static GLLimit instance() {
        if (instance == null) {
            instance = new GLLimit();
        }
        return instance;
    }
}
