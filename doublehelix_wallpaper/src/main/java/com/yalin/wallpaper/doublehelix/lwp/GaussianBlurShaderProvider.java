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

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.IntMap;

/**
 * Create one of these per app to provide shaders for multiple GaussianBlur objects.
 * Created by Darren on 10/5/2015.
 */
public class GaussianBlurShaderProvider {

    private static class UniqueShader {
        ShaderProgram shaderProgram;
        int refCount = 1;
        public UniqueShader (ShaderProgram shaderProgram) {
            this.shaderProgram = shaderProgram;
        }
    }

    private final IntMap<UniqueShader> blurPassShaderPrograms = new IntMap<UniqueShader>(5);

    ShaderProgram obtainBlurPassShaderProgram(int maxRadius){
        UniqueShader uniqueShader = blurPassShaderPrograms.get(maxRadius);
        if (uniqueShader != null){
            uniqueShader.refCount++;
            return uniqueShader.shaderProgram;
        }

        String prefix = "#define RADIUS " + maxRadius + "\n";

        String vertexShaderSrc =
                prefix +
                        "attribute vec4 a_position;\n" +
                        "attribute vec4 a_color;\n" +
                        "attribute vec2 a_texCoord0;\n" +
                        "\n" +
                        "uniform mat4 u_projTrans;\n" +
                        "uniform vec2 u_size;\n" +
                        "uniform vec4 u_offsets;\n" +
                        "\n" +
                        "varying vec2 v_texCoords[RADIUS+2];\n" +
                        "\n" +
                        "\n" +
                        "void main()\n" +
                        "{\n" +
                        "v_texCoords[0] = a_texCoord0.xy;\n" +
                        "v_texCoords[1] = a_texCoord0.xy + u_size * u_offsets[0];\n" +
                        "v_texCoords[2] = a_texCoord0.xy - u_size * u_offsets[0];\n" +
                        "#if (RADIUS > 2)\n" +
                        "v_texCoords[3] = a_texCoord0.xy + u_size * u_offsets[1];\n" +
                        "v_texCoords[4] = a_texCoord0.xy - u_size * u_offsets[1];\n" +
                        "#endif\n" +
                        "#if (RADIUS > 4)\n" +
                        "v_texCoords[5] = a_texCoord0.xy + u_size * u_offsets[2];\n" +
                        "v_texCoords[6] = a_texCoord0.xy - u_size * u_offsets[2];\n" +
                        "#endif\n" +
                        "#if (RADIUS > 6)\n" +
                        "v_texCoords[7] = a_texCoord0.xy + u_size * u_offsets[3];\n" +
                        "v_texCoords[8] = a_texCoord0.xy - u_size * u_offsets[3];\n" +
                        "#endif\n" +
                        "gl_Position =  u_projTrans * a_position;\n" +
                        "}";

        String fragmentShaderSrc =
                prefix +
                        "#ifdef GL_ES\n" +
                        "\t#define LOWP lowp\n" +
                        "\tprecision mediump float;\n" +
                        "#else\n" +
                        "\t#define LOWP \n" +
                        "#endif\n" +
                        "\n" +
                        "varying vec2 v_texCoords[RADIUS+2];\n" +
                        "\n" +
                        "uniform sampler2D u_texture;\n" +
                        "uniform float u_weightAtCenter;\n" +
                        "uniform vec4 u_weights;\n" +
                        "\n" +
                        "void main()\n" +
                        "{\n" +
                        "vec4 blurSum = texture2D(u_texture, v_texCoords[0]) * u_weightAtCenter;\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[1]) * u_weights[0];\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[2]) * u_weights[0];\n" +
                        "#if (RADIUS > 2)\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[3]) * u_weights[1];\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[4]) * u_weights[1];\n" +
                        "#endif\n" +
                        "#if (RADIUS > 4)\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[5]) * u_weights[2];\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[6]) * u_weights[2];\n" +
                        "#endif\n" +
                        "#if (RADIUS > 6)\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[7]) * u_weights[3];\n" +
                        "blurSum += texture2D(u_texture, v_texCoords[8]) * u_weights[3];\n" +
                        "#endif\n" +
                        "gl_FragColor = blurSum;\n" +
                        "}";

        ShaderProgram shaderProgram = new ShaderProgram(vertexShaderSrc, fragmentShaderSrc);
        uniqueShader = new UniqueShader(shaderProgram);
        blurPassShaderPrograms.put(maxRadius, uniqueShader);
        return shaderProgram;
    }

    boolean disposeShader(ShaderProgram shaderProgram){
        if (shaderProgram == null)
            return false;

        for (IntMap.Entry<UniqueShader> entry : blurPassShaderPrograms.entries()){
            UniqueShader uniqueShader = entry.value;
            if (uniqueShader.shaderProgram == shaderProgram){
                uniqueShader.refCount--;
                if (uniqueShader.refCount < 1){
                    shaderProgram.dispose();
                    blurPassShaderPrograms.remove(entry.key);
                    return true;
                } else {
                    return false;
                }
            }
        }

        //not found!
        shaderProgram.dispose();
        return true;
    }
}
