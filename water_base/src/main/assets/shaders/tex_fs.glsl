#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture0;

void main() {
	vec4 texColor = texture2D(u_texture0, v_texCoords);
	gl_FragColor = texColor;
}