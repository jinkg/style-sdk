package com.yalin.wallpaper.galaxy.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;
import java.util.Iterator;

public class C0364b implements GroupStrategy, Disposable {
    Pool<Array<Decal>> f812a;
    Array<Array<Decal>> f813b;
    ObjectMap<DecalMaterial, Array<Decal>> f814c;
    Camera f815d;
    ShaderProgram f816e;
    private final Comparator<Decal> f817f = new C03612();

    @Override
    public ShaderProgram getGroupShader(int group) {
        return this.f816e;
    }

    @Override
    public int decideGroup(Decal decal) {
        return decal.getMaterial().isOpaque() ? 0 : 1;
    }

    @Override
    public void beforeGroup(int group, Array<Decal> contents) {
        if (group == 1) {
            Gdx.gl.glEnable(3042);
            contents.sort(this.f817f);
            Gdx.gl.glDisable(2929);
            return;
        }
        int i2 = contents.size;
        for (int i3 = 0; i3 < i2; i3++) {
            Decal obj = contents.get(i3);
            Array<Decal> c0911a2 = this.f814c.get(obj.getMaterial());
            if (c0911a2 == null) {
                c0911a2 = this.f812a.obtain();
                c0911a2.clear();
                this.f813b.add(c0911a2);
                this.f814c.put(obj.getMaterial(), c0911a2);
            }
            c0911a2.add(obj);
        }
        contents.clear();
        Iterator<Array<Decal>> a = this.f814c.values().iterator();
        while (a.hasNext()) {
            contents.addAll(a.next());
        }
        this.f814c.clear();
        this.f812a.freeAll(this.f813b);
        this.f813b.clear();
    }

    @Override
    public void afterGroup(int group) {
        if (group == 1) {
            Gdx.gl.glDisable(3042);
        }
    }

    @Override
    public void beforeGroups() {
        Gdx.gl.glEnable(2929);
        this.f816e.begin();
        this.f816e.setUniformMatrix("u_projectionViewMatrix", this.f815d.combined);
        this.f816e.setUniformi("u_texture", 0);
    }

    @Override
    public void afterGroups() {
        this.f816e.end();
        Gdx.gl.glDisable(2929);
    }

    @Override
    public void dispose() {
        if (this.f816e != null) {
            this.f816e.dispose();
        }
    }

    class C03612 implements Comparator<Decal> {
        C03612() {
        }

        @Override
        public int compare(Decal c0438a, Decal c0438a2) {
            return (int) Math.signum(f815d.position.dst(c0438a2.getPosition()) - f815d.position.dst(c0438a.getPosition()));
        }
    }

    public C0364b(Camera c0851a) {
        this.f812a = new Pool<Array<Decal>>(16) {
            @Override
            protected Array<Decal> newObject() {
                return new Array<>();
            }
        };
        this.f813b = new Array<>();
        this.f814c = new ObjectMap<>();
        this.f815d = c0851a;
        m1596d();
    }


    private void m1596d() {
        this.f816e = new ShaderProgram("attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}");
        if (!this.f816e.isCompiled()) {
            throw new IllegalArgumentException("couldn't compile shader: " + this.f816e.getLog());
        }
    }
}
