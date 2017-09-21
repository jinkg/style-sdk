// vertex shader

#ifdef GL_ES
//precision mediump float;
#endif

uniform mediump float u_size;
uniform highp float iGlobalTime;
uniform mediump vec2 iResolution;
uniform mediump float u_offset;

attribute mediump vec2 a_pos;
attribute lowp vec4 a_col;

varying lowp vec4 v_col;

#define TAU 6.28318530718
#define MAX_ITER 5

void main()
{
	float time = iGlobalTime * .5+23.0;
    // uv should be the 0-1 uv of texture...
	vec2 uv = (a_pos+ vec2(u_offset,0.0))*0.5+0.5;
    
#ifdef SHOW_TILING
	vec2 p = mod(uv*TAU*2.0, TAU)-250.0;
#else
    vec2 p = mod(uv*TAU, TAU)-250.0;
#endif
	vec2 i = vec2(p);
	float c = 1.0;
	float inten = .005;

	for (int n = 0; n < MAX_ITER; n++) 
	{
		float t = time * (1.0 - (3.5 / float(n+1)));
		i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
		c += 1.0/length(vec2(p.x / (sin(i.x+t)/inten),p.y / (cos(i.y+t)/inten)));
	}
	c /= float(MAX_ITER);
	c = 1.17-pow(c, 1.4);
	vec3 colour = vec3(pow(abs(c), 8.0));
    colour = clamp(colour + vec3(0.0, 0.35, 0.5), 0.0, 1.0);
    

	#ifdef SHOW_TILING
	// Flash tile borders...
	vec2 pixel = 2.0 / iResolution.xy;
	uv *= 2.0;

	float f = floor(mod(iGlobalTime*.5, 2.0)); 	// Flash value.
	vec2 first = step(pixel, uv) * f;		   	// Rule out first screen pixels and flash.
	uv  = step(fract(uv), pixel);				// Add one line of pixels per tile.
	colour = mix(colour, vec3(1.0, 1.0, 0.0), (uv.x + uv.y) * first.x * first.y); // Yellow line
	
	#endif
	v_col = vec4(colour, 1.0);
	
	v_col *= vec4(0.7,0.7,0.7,1.0);
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
