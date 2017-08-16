/*******************************************************************************
 * Copyright 2015 Cypher Cove, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.yalin.wallpaper.doublehelix.lwp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;

/**Draws a textured quad that fills the entire screen and fades out over a given time.*/
public class FullScreenFader implements Disposable{

    private Mesh mesh;
    private ShaderProgram shader;

    int u_color;

    private float delay;
    private float fadeTime;
    private float elapsed;

    private final float[] vertices ={
            -1,-1,0,
            1,-1,0,
            1,1,0,
            -1,1,0
    };

    private final Color color;

    public FullScreenFader(float delay, float fadeTime, Color color){
        this.delay = delay;
        this.fadeTime = fadeTime;

        mesh=new Mesh(true, 4, 0,
                new VertexAttribute(Usage.Position, 3,"a_position"));

        mesh.setVertices(vertices);
        this.color = color;
    }

    public FullScreenFader(float delay, float fadeTime){
        this(delay, fadeTime, new Color(Color.BLACK));
    }

    public void createShader(){
        String vertexShaderSrc =  "attribute vec4 a_position;    \n" +
                "void main()                  \n" +
                "{                            \n" +
                "   gl_Position = a_position;  \n" +
                "}                            \n";
        String fragmentShaderSrc = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "uniform vec4 u_color;    \n" +
                "void main()                                  \n" +
                "{                                            \n" +
                "  gl_FragColor =  u_color;\n" +
                "}                                            \n";

        shader=new ShaderProgram(vertexShaderSrc,fragmentShaderSrc);

        u_color = shader.getUniformLocation("u_color");
    }

    public void render(float deltaTime){
        if (elapsed >= fadeTime)
            return;

        if (delay > 0){
            delay -= deltaTime;
        }

        GL20 gl = Gdx.gl20;
        gl.glEnable(GL20.GL_BLEND);
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        color.a = delay > 0 ? 1f : 1f - Interpolation.fade.apply( elapsed / fadeTime);

        if (shader==null)
            createShader();
        shader.begin();
        shader.setUniformf(u_color, color);
        mesh.render(shader, GL20.GL_TRIANGLE_FAN);

        shader.end();

        if (delay <= 0)
            elapsed += deltaTime;
    }

    public void dispose(){
        if (shader!=null)
            shader.dispose();
    }

}
