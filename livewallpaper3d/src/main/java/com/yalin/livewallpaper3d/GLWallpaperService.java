package com.yalin.livewallpaper3d;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by User on 8/13/2015.
 * Post apocalyptic live wallpaper Checklist

 The overall idea is to have an android live wallpaper with a bunch of images that form a scene (post apocalyptic in this case) and creates
 the (cool) illusion of a 3d environment by using the parallax effect.

 I will need a blurred out foreground layer which is basically the top of a table with various tools, a midground layer, which is the
 subject and the room itself, another layer further back, which will serve as the city backdrop outside of the room, and a room
 which will serve as the sky.

 Things I'd like to add:

 /Done! dynamic lighting so that the scene will be of different brightnesses and hues depending on the time of day.

 different objects, positions, and activities depending on the time of day (sleeping, eating, scavenging, reading etc.).

 animations for various objects. Blowing hair, flickering fires, twinkling stars, moving clouds etc.

 /Done! Settings page is a must.

 options for people without homescreens that do offsets changed.

 /Done! camera translations based on the gyroscope/acclerometer

 weather changes based on user preference or real life weather data

 donation option

 /Done! fix wallpaper not responding when it is chosen twice

 /Done! link to artists deviantart page
 */
public abstract class GLWallpaperService extends WallpaperService{

    protected SensorManager sensorManager;
    protected Sensor accelerometer;
    private long lastUpdate;
    public static float x;
    public static float y;
    public static float lastX;
    boolean rendererSet = false;

  public class GLEngine extends Engine implements SensorEventListener{
      @Override
      public void onSensorChanged(SensorEvent event)
      {
//          Log.d("Sensor info", "Sensor X: " + x + " Sensor Y: " + y);
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy)
      {

      }

      class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context)
            {
                super(context);
            }

            public SurfaceHolder getHolder()
            {
                return getSurfaceHolder();
            }

            public void onDestroy()
            {
                super.onDetachedFromWindow();
            }
        }

      protected WallpaperGLSurfaceView glSurfaceView;
      private boolean rendererHasBeenSet;
      public GLRenderer renderer;

      @Override
      public void onCreate(SurfaceHolder surfaceHolder)
      {
          super.onCreate(surfaceHolder);

          glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);

          Log.d("onCreate", "was called");
          Log.d("glSurfaceView", "glSurfaceView = " + glSurfaceView.toString());

          //set up the accelerometer
          sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
          if(sensorManager.getSensorList(Sensor.TYPE_GRAVITY).size() > 0)
          {
              sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                      SensorManager.SENSOR_DELAY_GAME);
          }
          accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
          lastUpdate = System.currentTimeMillis();

      }

      String previousTexture;

      long startTime, endTime;
      @Override
      public void onVisibilityChanged(boolean visible)
      {
          super.onVisibilityChanged(visible);
              if (visible)//resume
              {
                  glSurfaceView.onResume();
                  glSurfaceView.queueEvent(new Runnable()
                  {
                      public void run()
                      {
                          endTime = System.currentTimeMillis();
                          long elapsed = endTime - startTime;
                          renderer.refreshColors();
                          if(elapsed > 2000)
                          {
//                          renderer.setOpacity(0);
                              String newTexture = renderer.preferences.getString("texture_model", "1")+
                                      renderer.preferences.getString("camera_blur", "none");

//                          if(!newTexture.equals(previousTexture))
//                          {
                              renderer.sceneSetter.shuffleScene();
                              renderer.refreshTexture(DataCodes.GIRL);
                              renderer.refreshVertices(DataCodes.GIRL);
                              previousTexture = newTexture;
//                          }
                          }
                      }
                  });
                  sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_GAME);
                  Log.d("onResume", "was called on " + glSurfaceView.toString());
              } else //pause
              {
                  if(rendererHasBeenSet)
                  {
                      startTime = System.currentTimeMillis();
                      renderer.setOpacity(0);
                      glSurfaceView.requestRender();
                      glSurfaceView.onPause();
                      sensorManager.unregisterListener(this);
                      Log.d("onPause", "was called on " + glSurfaceView.toString());
              }
          }
      }

      @Override
      /*
      destroys the surface
       */
      public void onDestroy()
      {
          super.onDestroy();
          glSurfaceView.onDestroy();
          Log.d("onDestroy", "was called on " + glSurfaceView.toString());
      }

      @Override
      public void onSurfaceDestroyed(SurfaceHolder holder)
      {
          super.onSurfaceDestroyed(holder);
      }

      /*
      sets the renderer for the surface view and sets this.renderer to the input renderer
       */
      protected void setRenderer(GLSurfaceView.Renderer renderer)
      {
          this.renderer = (GLRenderer) renderer;
          glSurfaceView.setRenderer(renderer);
          rendererHasBeenSet = true;
          Log.d("setRenderer", "the set renderer is " + this.renderer.toString());
      }

      protected void setPreserveEGLContextOnPause(boolean preserve)
      {
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
          {
              glSurfaceView.setPreserveEGLContextOnPause(preserve);
          }
      }

      protected void setEGLContextClientVersion(int version)
      {
          glSurfaceView.setEGLContextClientVersion(version);
      }

      protected void setEGLConfigChooser(GLSurfaceView.EGLConfigChooser configChooser)
      {
          glSurfaceView.setEGLConfigChooser(configChooser);
      }
    }
}
