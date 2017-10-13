#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying float v_light;
varying vec2 v_texCoords;
uniform sampler2D u_texture0;

void main() {
	gl_FragColor = texture2D(u_texture0, v_texCoords.xy);
}