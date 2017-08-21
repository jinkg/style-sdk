precision highp float;

attribute vec4 a_Position;

uniform highp int u_Time;

varying float v_noise0_sample;
varying float v_noise1_sample;

varying vec2 v_Position;
varying float v_TopCurve;
varying float v_BottomCurve;
varying float v_Vignette;

void main() {
    gl_Position = a_Position;
    v_Position = (a_Position.xy+1.0)/2.0;
    v_TopCurve = .86 + .06*sin(v_Position.x+float(u_Time)/157.3) - .04*sin((v_Position.x+float(u_Time) / 104.0)*2.1);
    v_BottomCurve = .20 + .12*sin((v_Position.x+float(u_Time)/230.3)*1.4) - .06*sin((v_Position.x+float(u_Time) / 204.0)*.8);
    v_Vignette = 1.0 - pow(abs(a_Position.x), 2.0);
    v_noise0_sample = fract(float(u_Time) / 313.0);
    v_noise1_sample = fract(float(u_Time) / 458.0);
}
