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

#define t iGlobalTime
mat2 m(float a){float c=cos(a), s=sin(a);return mat2(c,-s,s,c);}
float map(vec3 p){
    p.xz*= m(t*0.4);p.xy*= m(t*0.3);
    vec3 q = p*2.+t*1.;
    return length(p+vec3(sin(t*0.7)))*log(length(p)+1.) + sin(q.x+sin(q.z+sin(q.y)))*0.5 - 1.;
}


void main()
{
	vec2 p = -(a_pos+vec2(u_offset,0.0))*0.5 - vec2(0.2, 0.25);
	//vec2 p = a_pos;
    vec3 cl = vec3(0.0);
    float d = 2.5;
    for(int i=0; i <= 5; i++)
    {
		vec3 p3 = vec3(0.0, 0.0, 5.0) + normalize(vec3(p, -1.0)) * d;
        float rz = map(p3);
		float f =  clamp((rz - map(p3 + 0.1)) * 0.5, -0.1, 1.0);
        vec3 l = vec3(0.1, 0.3, 0.4) + vec3(5., 2.5, 3.)*f;
        cl = cl * l + (1.0 - smoothstep(0.0 , 2.5, rz)) * 0.7 * l;
		d += min(rz, 1.0);
	}
    v_col = vec4(cl, 1.0);

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
