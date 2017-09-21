// vertex shader
// http://glslsandbox.com/e#27946.0

#ifdef GL_ES
//precision highp float;
#endif

uniform mediump float u_size;
uniform mediump vec2 iResolution;
uniform highp float iGlobalTime;
uniform mediump float u_offset;

attribute mediump vec2 a_pos;
attribute lowp vec4 a_col;

varying lowp vec4 v_col;

vec2 hash(vec2 p)
{
    mat2 m = mat2(  13.85, 47.77,
                    99.41, 88.48
                );

    return fract(sin(m*p) * 46738.29);
}

float voronoi(vec2 p)
{
    vec2 g = floor(p);
    vec2 f = fract(p);

    float distanceToClosestFeaturePoint = 1.0;
    for(int y = -1; y <= 1; y++)
    {
        for(int x = -1; x <= 1; x++)
        {
            vec2 latticePoint = vec2(x, y);
            float currentDistance = distance(latticePoint + hash(g+latticePoint), f);
            distanceToClosestFeaturePoint = min(distanceToClosestFeaturePoint, currentDistance);
        }
    }

    return distanceToClosestFeaturePoint;
}

void main()
{
    vec2 uv = a_pos+vec2(u_offset,0.0);
    uv.x *= iResolution.x/iResolution.y;

    float offset = voronoi(uv*10.0 + vec2(iGlobalTime));
    float t = 1.0/abs(((uv.x + sin(uv.y + iGlobalTime)) + offset) * 30.0);

    float r = voronoi( uv * 1.0 ) * (15.0+5.0*sin(iGlobalTime));
    vec3 finalColor = vec3(12.0 * uv.y, 2.0, 1.0 * r) * t;
    
    v_col = vec4(finalColor, 1.0 );
	
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
