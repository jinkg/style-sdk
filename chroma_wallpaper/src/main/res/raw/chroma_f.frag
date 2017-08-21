precision mediump float;

uniform sampler2D u_ColorSwath;
uniform sampler2D u_Noise;

uniform vec4 u_Touch;

varying float v_noise0_sample;
varying float v_noise1_sample;

varying vec2 v_Position;  // [0.0, 1.0]
varying float v_TopCurve;
varying float v_BottomCurve;
varying float v_Vignette;

void main() {
   
     float TouchMultiplier = 1.0;
     if(u_Touch.x != -1.0) {
     	TouchMultiplier += .01 / abs(u_Touch.x - v_Position.x);
     if(u_Touch.y != -1.0) {
     	TouchMultiplier += .01 / abs(u_Touch.y - v_Position.x);
     if(u_Touch.z != -1.0) {
     	TouchMultiplier += .01 / abs(u_Touch.z - v_Position.x);
     if(u_Touch.w != -1.0)
     	TouchMultiplier += .01 / abs(u_Touch.w - v_Position.x);
     } } }
	
	float noise0 = texture2D(u_Noise, vec2(v_Position.x, v_noise0_sample)).r;
	float noise1 = texture2D(u_Noise, vec2(v_Position.x, v_noise1_sample)).b;
	float combinedNoise = .24 + noise0 * noise1;
	
	float value = 10.0 * (v_TopCurve-v_Position.y) * (v_Position.y-v_BottomCurve) * v_Vignette;
	value = clamp(value, 0.0, 1.0);

	vec4 color = texture2D(u_ColorSwath, v_Position);
	
	gl_FragColor = min(TouchMultiplier * combinedNoise * value, 1.0) * color;
 
}
