package com.yalin.wallpaper.super_hexagon;

import android.util.Log;

import com.yalin.wallpaper.super_hexagon.Mesh.PrimitiveType;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HexagonRenderer implements GLWallpaperService.Renderer {
	private HexagonService service;

	private float mod_perspective = 1;
	private float mod_zoom = 1;
	private float mod_pulse = 0;
	private float mod_rotation = 0;
	
	private float rotation_speed = 0;
	private int rotation_timer = -1;
	
	private float perspective_speed = 0;
	private int perspective_timer = -1;
	
	private int zoom_timer = -1;
	private float zoom_speed = 0;
	
	private int pulse_timer = 0;

    private float cursor_pos = 0;
	
	private int w;
	private int h;
	private float rel;
	
	private Mesh bg;
	private Mesh hexagonO;
	private Mesh hexagonI;
    private Mesh wall;
    private Mesh cursor;

    private ArrayList<Wall> walls = new ArrayList<Wall>();
	
	private float color1r,color1g,color1b;
	private float color2r,color2g,color2b;
	private float color3r,color3g,color3b;

	private int color_timer = 0;
	private boolean swapStripes = false;
    private int hInc = 0;           // Variables just for Hexagonest mode
    private float[] hCols =   {10,82,0,17,99,0,69,249,17,
                               24,1,83,34,0,99,109,15,249,
                               81,11,1,99,18,1,249,69,16,
                               0,78,82,1,89,99,23,211,249,
                               78,4,79,94,4,91,239,25,217,
                               82,76,0,99,96,1,244,255,0};

	public HexagonRenderer(HexagonService s) {
		service = s;
	}

	public void onDrawFrame(GL10 gl) {		
		// Logic
		color_timer++;
		if(service.prefs_colors!=7 && color_timer>=120) color_timer=0;
		if (service.prefs_colors==1) {			// Hexagon
			color1r=c(105,92);		color1g=c(29,74);		color1b=c(6,8);
			color2r=c(72,61);		color2g=c(20,50);		color2b=c(6,5);
			color3r=c(221,220);		color3g=c(44,218);		color3b=c(54,35);
		} else if (service.prefs_colors==2) {	// Hyper Hexagon
			color1r=c(6,1);			color1g=c(6,61);		color1b=c(70,69);
			color2r=c(9,2);			color2g=c(9,90);		color2b=c(105,102);
			color3r=c(69,5);		color3g=c(87,169);		color3b=c(189,178);
		} else if (service.prefs_colors==3) {	// Unique Hexagon
            color1r=70f/255f;       color1g=7f/255f;		color1b=c(15,7);
            color2r=104f/255f;		color2g=12f/255f;	    color2b=c(21,7);
            color3r=c(202,225);		color3g=c(61,82);		color3b=c(70,88);
		} else if (service.prefs_colors==4) {	// Hexagoner
			color1r=0;				color1g=0;				color1b=0;
			color2r=c(5,38);		color2g=c(37,20);		color2b=c(16,0);
			color3r=c(33,245);		color3g=c(239,133);		color3b=c(112,0);
			if (color_timer%30==0) swapStripes =!swapStripes;
		} else if (service.prefs_colors==5) {	// Hyper Hexagoner
			color1r=1;				color1g=1;				color1b=1;
			color2r=c(253,217);		color2g=c(215,234);		color2b=c(236,254);
			color3r=c(242,19);		color3g=c(6,118);		color3b=c(134,247);
			if (color_timer%30==0) swapStripes =!swapStripes;
		} else if (service.prefs_colors==6) {	// Unique Hexagoner
            color1r=c(254,203);		color1g=c(1,27);		color1b=c(128,154);
            color2r=c(253,217);		color2g=c(65,73);		color2b=c(113,142);
            color3r=c(253,241);		color3g=c(192,197);		color3b=c(223,230);
			if (color_timer%30==0) swapStripes =!swapStripes;
		} else if (service.prefs_colors==7) {	// Hexagonest
            if (color_timer<=30) {
                if (hInc==0) {
                    color1r=ch(hCols[45],hCols[0]);
                    color1g=ch(hCols[46],hCols[1]);
                    color1b=ch(hCols[47],hCols[2]);
                    color2r=ch(hCols[48],hCols[3]);
                    color2g=ch(hCols[49],hCols[4]);
                    color2b=ch(hCols[50],hCols[5]);
                    color3r=ch(hCols[51],hCols[6]);
                    color3g=ch(hCols[52],hCols[7]);
                    color3b=ch(hCols[53],hCols[8]);
                } else {
                    color1r=ch(hCols[(hInc-1)*9],hCols[hInc*9]);
                    color1g=ch(hCols[(hInc-1)*9+1],hCols[hInc*9+1]);
                    color1b=ch(hCols[(hInc-1)*9+2],hCols[hInc*9+2]);
                    color2r=ch(hCols[(hInc-1)*9+3],hCols[hInc*9+3]);
                    color2g=ch(hCols[(hInc-1)*9+4],hCols[hInc*9+4]);
                    color2b=ch(hCols[(hInc-1)*9+5],hCols[hInc*9+5]);
                    color3r=ch(hCols[(hInc-1)*9+6],hCols[hInc*9+6]);
                    color3g=ch(hCols[(hInc-1)*9+7],hCols[hInc*9+7]);
                    color3b=ch(hCols[(hInc-1)*9+8],hCols[hInc*9+8]);
                }
            }
			if (color_timer>=180) {
                color_timer=0;
                hInc++;
                if (hInc>5) hInc=0;
            }
            if (color_timer%30==0) swapStripes =!swapStripes;
		} else if (service.prefs_colors==8) {	// Hyper Hexagonest
			color1r=color1g=color1b=c(80,141);
			color2r=color2g=color2b=c(101,178);
			color3r=color3g=color3b=c(239,252);
			if (color_timer%30==0) swapStripes =!swapStripes;
		} else if (service.prefs_colors==9) {	// The End
			color1r=color1g=color1b=color2r=color2g=color2b=0;
			color3r=color3g=color3b=1;
		}
		
		if (service.prefs_rotation==-1) {
			if (rotation_timer<0 && Math.random()<0.01) {
				if (rotation_speed<0) rotation_speed=(float)Math.random()+0.25f;
				else rotation_speed=-(float)Math.random()-0.25f;
				rotation_timer=100+(int)(Math.random()*50);
			}
			rotation_timer--;
			mod_rotation+=rotation_speed;
		} else if (service.prefs_rotation==1) mod_rotation-=0.5;
		else if (service.prefs_rotation==2) mod_rotation+=0.5;
		else mod_rotation=0;
		if (mod_rotation>360) mod_rotation-=360;
		if (mod_rotation<0) mod_rotation+=360;
		
		if (service.prefs_perspective==-1) {
			if ((perspective_timer<0 && Math.random()<0.01) || mod_perspective>=1 || mod_perspective<=0.75) {
				if (mod_perspective>1) mod_perspective=1;
				else if (mod_perspective<0.75f) mod_perspective=0.75f;
				
				if (perspective_speed<0) perspective_speed=(float)Math.random()*0.001f;
				else perspective_speed=(float)Math.random()*-0.001f;
				perspective_timer=100+(int)(Math.random()*50);
			}
			perspective_timer--;
			mod_perspective+=perspective_speed;
		} else if (service.prefs_perspective==0) mod_perspective=1;
		else if (service.prefs_perspective==1) mod_perspective=0.875f;
		else if (service.prefs_perspective==2) mod_perspective=0.75f;
		
		if (service.prefs_zoom==-1) {
			if ((zoom_timer<0 && Math.random()<0.01) || mod_zoom>=1.5 || mod_zoom<=0.75) {
				if (mod_zoom>1.5) mod_zoom=1.5f;
				else if (mod_zoom<0.75f) mod_zoom=0.75f;
				
				if (zoom_speed<0) zoom_speed=(float)Math.random()*0.005f;
				else zoom_speed=(float)Math.random()*-0.005f;
				zoom_timer=100+(int)(Math.random()*50);
			}
			zoom_timer--;
			mod_zoom+=zoom_speed;
		} else if (service.prefs_zoom==0) mod_zoom=1.5f;
		else if (service.prefs_zoom==1) mod_zoom=0.75f;
		
		if (mod_pulse>0) mod_pulse-=0.01f;
		if (service.prefs_pulse>0) {
			pulse_timer--;
			if (pulse_timer<=0) {
				mod_pulse=0.1f;
				if (service.prefs_pulse==1) pulse_timer=40;
				else if (service.prefs_pulse==2) pulse_timer=80;
				else if (service.prefs_pulse==3) pulse_timer=160;
			}
		}

        if (service.prefs_walls>0) {
            if (mod_pulse==0.1f && (service.prefs_walls>1 || Math.round(Math.random())>0.5)) {
                int wl = (int) Math.floor(Math.random() * 6);
                walls.add(new Wall(wl));
                if (Math.round(Math.random())>0.8) {
                    walls.add(new Wall(wl+1));
                    if (Math.round(Math.random())>0.8) {
                        walls.add(new Wall(wl+2));
                    }
                }
            }
            ArrayList<Wall> dangerWalls = new ArrayList<Wall>();
            for (Wall w : walls) {
                if (w.dist<1 && w.dist>0.05) {
                    if (dangerWalls.size()==0) dangerWalls.add(w);
                    else {
                        if (w.dist<dangerWalls.get(0).dist) {
                            dangerWalls.clear();
                            dangerWalls.add(w);
                        } else if (w.dist==dangerWalls.get(0).dist) {
                            dangerWalls.add(w);
                        }
                    }
                }
            }

            if (service.prefs_cursor>0) {
                if (dangerWalls.size()==1) {
                    if (dangerWalls.get(0).dodged==false) {
                        int danger = dangerWalls.get(0).lane*60+30;
                        if (Math.abs(cursor_pos-danger)<30+Math.random()*20) {
                            if (cursor_pos<danger) cursor_pos-=7;
                            else cursor_pos+=7;
                        } else {
                            danger+=360;
                            if (Math.abs(cursor_pos-danger)<30+Math.random()*20) {
                                if (cursor_pos<danger) cursor_pos-=7;
                                else cursor_pos+=7;
                            } else {
                                danger-=720;
                                if (Math.abs(cursor_pos-danger)<60+Math.random()*30) {
                                    if (cursor_pos<danger) cursor_pos-=7;
                                    else cursor_pos+=7;
                                } else {
                                    dangerWalls.get(0).dodged=true;
                                }
                            }
                        }
                    }
                } else if (dangerWalls.size()==2) {
                    if (dangerWalls.get(0).dodged==false) {
                        int lanes[] = new int[2];
                        lanes[0] = dangerWalls.get(0).lane;
                        lanes[1] = dangerWalls.get(1).lane;
                        if (lanes[0]==0 || lanes[1]==0) {
                            if (lanes[0]==5) lanes[0]=-1;
                            else if (lanes[1]==5) lanes[1]=-1;
                        }
                        int danger = (int)(((float)(lanes[0]+lanes[1])/2.0)*60+30);
                        if (Math.abs(cursor_pos-danger)<60+Math.random()*30) {
                            if (cursor_pos<danger) cursor_pos-=7;
                            else cursor_pos+=7;
                        } else {
                            danger+=360;
                            if (Math.abs(cursor_pos-danger)<60+Math.random()*30) {
                                if (cursor_pos<danger) cursor_pos-=7;
                                else cursor_pos+=7;
                            } else {
                                danger-=720;
                                if (Math.abs(cursor_pos-danger)<60+Math.random()*30) {
                                    if (cursor_pos<danger) cursor_pos-=7;
                                    else cursor_pos+=7;
                                } else {
                                    dangerWalls.get(0).dodged=dangerWalls.get(1).dodged=true;
                                }
                            }
                        }
                    }
                } else if (dangerWalls.size()==3) {
                    if (dangerWalls.get(0).dodged==false) {
                        int lanes[] = new int[3];
                        lanes[0] = dangerWalls.get(0).lane;
                        lanes[1] = dangerWalls.get(1).lane;
                        lanes[2] = dangerWalls.get(2).lane;
                        if (lanes[0]==0 || lanes[1]==0 || lanes[2]==0) {
                            if (lanes[0]>3) lanes[0]-=6;
                            if (lanes[1]>3) lanes[1]-=6;
                            if (lanes[2]>3) lanes[2]-=6;
                        }
                        int danger = (int)(((float)(lanes[0]+lanes[1]+lanes[2])/3.0)*60+30);
                        if (Math.abs(cursor_pos-danger)<90+Math.random()*40) {
                            if (cursor_pos<danger) cursor_pos-=7;
                            else cursor_pos+=7;
                        } else {
                            danger+=360;
                            if (Math.abs(cursor_pos-danger)<90+Math.random()*40) {
                                if (cursor_pos<danger) cursor_pos-=7;
                                else cursor_pos+=7;
                            } else {
                                danger-=720;
                                if (Math.abs(cursor_pos-danger)<90+Math.random()*30) {
                                    if (cursor_pos<danger) cursor_pos-=7;
                                    else cursor_pos+=7;
                                } else {
                                    dangerWalls.get(0).dodged=dangerWalls.get(1).dodged=dangerWalls.get(2).dodged=true;
                                }
                            }
                        }
                    }
                }
                if (cursor_pos>360) cursor_pos-=360;
                if (cursor_pos<0) cursor_pos+=360;
            }
        }

		// Drawing
		gl.glViewport( 0, 0, w, h);
		if (swapStripes) gl.glClearColor(color2r, color2g, color2b, 1f);
		else gl.glClearColor(color1r, color1g, color1b, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glPushMatrix();
			gl.glScalef(mod_zoom,rel*mod_perspective*mod_zoom,1);
			gl.glRotatef(mod_rotation,0,0,1);
			
			if (swapStripes) gl.glColor4f(color1r, color1g, color1b, 1.0f);
			else gl.glColor4f(color2r, color2g, color2b, 1.0f);
			bg.render(PrimitiveType.Triangles);

            gl.glColor4f(color3r, color3g, color3b, 1.0f);
            float[] p = new float[2];
            ArrayList<Wall> removeWalls = new ArrayList<Wall>();
            for (Wall w : walls) {
                if (w.dist<=0) removeWalls.add(w);
                else {
                    wall.reset();
                    p = getLinePoint(w.dist*5f,w.lane);
                    wall.vertex(p[0], p[1], 0);
                    p = getLinePoint(w.dist*5f-0.1f,w.lane);
                    wall.vertex(p[0], p[1], 0);
                    p = getLinePoint(w.dist*5f,w.lane+1);
                    wall.vertex(p[0], p[1], 0);
                    p = getLinePoint(w.dist*5f-0.1f,w.lane+1);
                    wall.vertex(p[0], p[1], 0);
                    wall.render(PrimitiveType.TriangleStrip);
                    if (service.prefs_walls==3) w.dist-=0.005;
                    else w.dist-=0.002;
                }
            }
            for (Wall w : removeWalls) walls.remove(w);
            removeWalls.clear();
			
			gl.glPushMatrix();
				gl.glScalef(1+mod_pulse,1+mod_pulse,1);
				hexagonO.render(PrimitiveType.TriangleStrip);
				gl.glColor4f(color1r, color1g, color1b, 1.0f);
				hexagonI.render(PrimitiveType.TriangleStrip);
                gl.glPushMatrix();
                    gl.glRotatef(cursor_pos,0,0,1);
                    gl.glTranslatef(0.23f,0,0);
                    //gl.glScalef(cursor_pos/360f,cursor_pos/360f,0);
                    gl.glColor4f(color3r, color3g, color3b, 1.0f);
                    cursor.render(PrimitiveType.TriangleStrip);
                gl.glPopMatrix();
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	public float c(float a, float b) {
		if (color_timer==0) return a/255f;
		else if (color_timer==60) return b/255f;
		float t = Math.abs((float)(color_timer-60)/60f);
		t = b+(a-b)*t;
		return t/255f;
	}
    public float ch(float a, float b) {
        if (color_timer==0) return a/255f;
        else if (color_timer==30) return b/255f;
        float t = Math.abs((float)(color_timer-30)/30f);
        t = b+(a-b)*t;
        return t/255f;
    }
	
	public float[] getLinePoint(float dist, int line) {
		// line: starting straight right (0), counter-clockwise
        while (line>5) line-=6;
		float[] p = new float[2];
		switch (line) {
			case 0:
				p[0] = dist*(float)Math.cos(0);
				p[1] = 0*(float)Math.sin(0);
				break;
			case 1:
				p[0] = dist*(float)Math.cos((float)Math.PI/3f);
				p[1] = dist*(float)Math.sin((float)Math.PI/3f);
				break;
			case 2:
				p[0] = dist*(float)Math.cos(2*(float)Math.PI/3f);
				p[1] = dist*(float)Math.sin(2*(float)Math.PI/3f);
				break;
			case 3:
				p[0] = dist*(float)Math.cos(Math.PI);
				p[1] = 0*(float)Math.sin(Math.PI);
				break;
			case 4:
				p[0] = dist*(float)Math.cos(Math.PI+(float)Math.PI/3f);
				p[1] = dist*(float)Math.sin(Math.PI+(float)Math.PI/3f);
				break;
			case 5:
				p[0] = dist*(float)Math.cos(Math.PI+2*(float)Math.PI/3f);
				p[1] = dist*(float)Math.sin(Math.PI+2*(float)Math.PI/3f);
				break;
		}
		return p;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glLoadIdentity();

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		w = width;
		h = height;
		rel = (float)w/(float)h;
		Log.d("Size",w+","+h+";"+rel);
		
		bg = new Mesh( gl, 9, false, false, false );
		float[] p;
		bg.vertex(0, 0, 0);
		p = getLinePoint(5,0);
		bg.vertex(p[0], p[1], 0);
		p = getLinePoint(5,1);
		bg.vertex(p[0], p[1], 0);
		bg.vertex(0, 0, 0);
		p = getLinePoint(5,2);
		bg.vertex(p[0], p[1], 0);
		p = getLinePoint(5,3);
		bg.vertex(p[0], p[1], 0);
		bg.vertex(0, 0, 0);
		p = getLinePoint(5,4);
		bg.vertex(p[0], p[1], 0);
		p = getLinePoint(5,5);
		bg.vertex(p[0], p[1], 0);
		
		hexagonO = new Mesh( gl, 6, false, false, false );
		p = getLinePoint(0.2f,0);
		hexagonO.vertex(p[0], p[1], 0);
		p = getLinePoint(0.2f,1);
		hexagonO.vertex(p[0], p[1], 0);
		p = getLinePoint(0.2f,5);
		hexagonO.vertex(p[0], p[1], 0);
		p = getLinePoint(0.2f,2);
		hexagonO.vertex(p[0], p[1], 0);
		p = getLinePoint(0.2f,4);
		hexagonO.vertex(p[0], p[1], 0);
		p = getLinePoint(0.2f,3);
		hexagonO.vertex(p[0], p[1], 0);
		
		hexagonI = new Mesh( gl, 6, false, false, false );
		p = getLinePoint(0.19f,0);
		hexagonI.vertex(p[0], p[1], 0);
		p = getLinePoint(0.19f,1);
		hexagonI.vertex(p[0], p[1], 0);
		p = getLinePoint(0.19f,5);
		hexagonI.vertex(p[0], p[1], 0);
		p = getLinePoint(0.19f,2);
		hexagonI.vertex(p[0], p[1], 0);
		p = getLinePoint(0.19f,4);
		hexagonI.vertex(p[0], p[1], 0);
		p = getLinePoint(0.19f,3);
		hexagonI.vertex(p[0], p[1], 0);

        cursor = new Mesh( gl, 3, false, false, false );
        cursor.vertex(-0.017f, -0.022f, 0);
        cursor.vertex(-0.017f, 0.022f, 0);
        cursor.vertex(0.017f, 0, 0);
        cursor_pos = (int)(Math.random()*360);

        wall = new Mesh( gl, 4, false, false, false );
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearDepthf(1f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
	    gl.glShadeModel(GL10.GL_FLAT);
	    gl.glDisable(GL10.GL_DEPTH_TEST);
	    gl.glDisable(GL10.GL_DITHER);
	    gl.glDisable(GL10.GL_LIGHTING);
	    
	    gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

	    gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
	            GL10.GL_MODULATE);
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    gl.glViewport(0, 0, w, h);
	    gl.glMatrixMode( GL10.GL_PROJECTION );
	    gl.glLoadIdentity();
	    //GLU.gluOrtho2D(gl, -ratio, ratio, -1, 1);
	}

	/**
	 * Called when the engine is destroyed. Do any necessary clean up because
	 * at this point your renderer instance is now done for.
	 */
	public void release() {
	}
}
