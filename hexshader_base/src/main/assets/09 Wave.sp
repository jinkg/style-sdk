// vertex shader

#ifdef GL_ES
//precision mediump float;
#endif

#define PI 3.1415926535897932384626433832795
#define SQRT2 1.41421356237

uniform mediump float u_size;
uniform highp float iGlobalTime;
uniform mediump vec2 iResolution;
uniform mediump float u_offset;

attribute highp vec2 a_pos;
attribute lowp vec4 a_col;

varying lowp vec4 v_col;

highp float rand(vec2 co)
{
    highp float a = 12.9898;
    highp float b = 78.233;
    highp float c = 43758.5453;
    highp float dt= dot(co.xy ,vec2(a,b));
    highp float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}

void main()
{

	vec2 pos = a_pos+vec2(u_offset,0.0);
	vec3 color = vec3(0);
	
	color.g += 1. - pow(abs(sin(pos.x * 100. + iGlobalTime * 0.5) * abs(sin(iGlobalTime*0.1)) + 4. - pos.y), 0.1);
	color.b += 1. - pow(abs(cos(cos(pos.x) * 2. - iGlobalTime * 0.5) * cos(iGlobalTime*0.1) - (pos.y)), 0.1);
	color.r += 1. - pow(abs(cos(sin(pos.x) * 2. - iGlobalTime * 0.5) * sin(iGlobalTime*0.1) - (pos.y)), 0.3);

	color += color.r + color.g + color.b * 6. * (1. + mod(rand(vec2(iGlobalTime*0.1, iGlobalTime*0.1)), 0.01));

	v_col = vec4(color*0.7, 1.0);
	
	
	//v_col *= a_col;
	gl_PointSize = u_size;
	gl_Position =  vec4(a_pos.x, a_pos.y, 1.0, 1.0);
}

====
// fragment shader
uniform lowp sampler2D u_texture;
varying lowp vec4 v_col;

void main()
{
	gl_FragColor = v_col * texture2D(u_texture, gl_PointCoord);
}
