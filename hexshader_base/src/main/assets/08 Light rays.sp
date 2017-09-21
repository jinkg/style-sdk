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

float rayStrength(vec2 raySource, vec2 rayRefDirection, vec2 coord, float seedA, float seedB, float speed)
{
	vec2 sourceToCoord = coord - raySource;
	float cosAngle = dot(normalize(sourceToCoord), rayRefDirection);
	
	return clamp(
		(0.45 + 0.15 * sin(cosAngle * seedA + iGlobalTime * speed)) +
		(0.3 + 0.2 * cos(-cosAngle * seedB + iGlobalTime * speed)),
		0.0, 1.0) *
		clamp((iResolution.x - length(sourceToCoord)) / iResolution.x, 0.5, 1.0);
}


void main()
{
 	vec2 uv = a_pos*0.5 + 0.5;
	uv.y = 1.0 - uv.y;
	vec2 coord = vec2(uv.x*iResolution.x + u_offset*iResolution.x, (uv.y-0.25)*iResolution.y);
	coord += vec2(iResolution.x*sin(iGlobalTime*0.2), iResolution.x*cos(iGlobalTime*0.01))*0.3;
	
	// Set the parameters of the sun rays
	vec2 rayPos1 = vec2(iResolution.x * 0.7, iResolution.y * -0.4);
	vec2 rayRefDir1 = normalize(vec2(1.0, -0.116));
	float raySeedA1 = 36.2214;
	float raySeedB1 = 21.11349;
	float raySpeed1 = 1.5;
	
	vec2 rayPos2 = vec2(iResolution.x * 0.8, iResolution.y * -0.6);
	vec2 rayRefDir2 = normalize(vec2(1.0, 0.241));
	const float raySeedA2 = 22.39910;
	const float raySeedB2 = 18.0234;
	const float raySpeed2 = 1.1;
	
	// Calculate the colour of the sun rays on the current fragment
	vec4 rays1 =
		vec4(1.0) *
		rayStrength(rayPos1, rayRefDir1, coord, raySeedA1, raySeedB1, raySpeed1);
	 
	vec4 rays2 =
		vec4(1.0) *
		rayStrength(rayPos2, rayRefDir2, coord, raySeedA2, raySeedB2, raySpeed2);
	
	v_col = rays1 * 0.7 + rays2 * 0.5;
	
	// Attenuate brightness towards the bottom, simulating light-loss due to depth.
	// Give the whole thing a blue-green tinge as well.
	float brightness = 1.0 - (coord.y / iResolution.y);
	v_col.x *= 0.1 + (brightness * 0.8);
	v_col.y *= 0.3 + (brightness * 0.6);
	v_col.z *= 0.5 + (brightness * 0.5);

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
