package com.yalin.livewallpaper3d;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.Random;

/**
 * Created by Blake on 9/19/2015.
 */
public class SceneSetter
{
    private int blurSetting;
    Random randomGenerator;
    private int girlTextureChoice;
    private String girlTextureArray[] = {"1", "2", "3", "4", "5", "6"};


    private int textureIndexTable, textureIndexCity, textureIndexBuilding, textureIndexSky;


    DataHolder dataHolder = new DataHolder();
    SharedPreferences preferences;
    Context context;
    private float[] currentGirlVertices;
    private float girlOffset;
    TimeTracker timeTracker;
    private int lastDayHour;

    public SceneSetter(SharedPreferences nPreferences, Context nContext)
    {
        this.preferences = nPreferences;
        this.context = nContext;
        randomGenerator = new Random();
        shuffleScene();
        Log.d("Scene Setter", "Constructor. girlTextureChoice: " + girlTextureChoice);
        timeTracker = new TimeTracker();

    }

    public boolean shuffleScene()
    {
        String choice = preferences.getString("texture_model", "3");
            if(choice.equals("0"))
            {
                shuffleGirl();
                return true;
            }
        return false;
    }

    public float[] getSpriteVertices(int sprite)
    {
        if(sprite == DataCodes.TABLE)
        {
            return dataHolder.tableVertices;
        }
        else if(sprite == DataCodes.GIRL)
        {
            String choice = preferences.getString("texture_model", "3");
            if(choice.equals("0"))
                choice = girlTextureArray[girlTextureChoice];
            switch(choice)
            {
                case("1"):
                    return dataHolder.girlFrontReading;
                case("2"):
                    return dataHolder.girlFrontReading;
                case("3"):
                    return dataHolder.girlMidStanding;
                case("4"):
                    return dataHolder.girlMidStanding;
                case("5"):
                    return dataHolder.girlBackSitting;
                case("6"):
                    return dataHolder.girlBackSitting;
            }
        }
        else if(sprite == DataCodes.BUILDING)
        {
            return dataHolder.buildingVertices;
        }
        else if (sprite == DataCodes.CITY)
        {
            return dataHolder.cityVertices;
        }
        else if (sprite == DataCodes.SKY)
        {
            return dataHolder.skyVertices;
        }
//        else if(sprite == DataCodes.CLOUDS)
//        {
//            return dataHolder.getCloudVertices();
//        }
        else //it's the room
        {
            return dataHolder.roomVertices;
        }
        return null;
    }

    public void setNewScene()
    {

    }


    public float getGirlOffset()
    {
        return girlOffset;
    }

    public float[] getSpriteColor(int sprite)
    {
        int choice = Integer.parseInt(preferences.getString("set_time", "30"));
        if(choice == DataCodes.AUTOMATIC)
        {
            choice = timeTracker.getDayHour();
            lastDayHour = choice;
            Log.d("Scene Setter: ", "color chosen from automatic: " + choice);
        }
        if(sprite == DataCodes.GIRL)
        {
            if(choice == DataCodes.DAY)
                return dataHolder.normalColorGirl;
            else if(choice == DataCodes.SUNSET)
                return dataHolder.sunsetColorGirl;
            else if(choice == DataCodes.NIGHT)
                return dataHolder.nightColorGirl;
            else if(choice == DataCodes.DAWN)
                return dataHolder.dawnColorGirl;
                return dataHolder.normalColor;
        }
        else if(sprite == DataCodes.SKY)
        {
            Log.d("Scene Setter: ", "Sky colors chosen");
            if(choice == DataCodes.DAY)
                return dataHolder.skyColorNormal;
            else if(choice == DataCodes.SUNSET)
                return dataHolder.skyColorSunset;
            else if(choice == DataCodes.NIGHT)
                return dataHolder.skyColorNight;
            else if(choice == DataCodes.DAWN)
                return dataHolder.skyColorDawn;

            return dataHolder.skyColorNormal;
        }
        else if(sprite == DataCodes.CLOUDS)
        {
            return dataHolder.getCloudColors();
        }
//        else if(sprite == DataCodes.ROOM)
//        {
//            if(choice == DataCodes.NIGHT)
//            {
//                return dataHolder.nightColor;
//            }
//            return dataHolder.normalColor;
//        }
//        else if(sprite== DataCodes.CITY)
//        {
//            if(choice == DataCodes.NIGHT)
//            {
//                return dataHolder.nightColor;
//            }
//            return dataHolder.normalColor;
//        }
//        else if(sprite == DataCodes.BUILDING)
//        {
//            if(choice == DataCodes.NIGHT)
//            {
//                return dataHolder.nightColor;
//            }
//            return dataHolder.normalColor;
//
//        }
        else //it's an object in the room
        {
            if(choice == DataCodes.DAY)
                return dataHolder.normalColor;
            else if(choice == DataCodes.SUNSET)
                return dataHolder.sunsetColor;
            else if(choice == DataCodes.NIGHT)
                return dataHolder.nightColor;
            else if(choice == DataCodes.DAWN)
                return dataHolder.dawnColor;

            return dataHolder.normalColor;
        }
    }

    public boolean needsColorRefresh()
    {
        int currentTime = timeTracker.getDayHour();
        if(currentTime != lastDayHour)
            return true;
        return false;
    }
    public int getGirlRender()
    {
        if(preferences.getBoolean("remove_layer", true))
        {
            return 0;
        }
        String choice = preferences.getString("texture_model", "1");
        switch(choice)
        {
            case("1"):
                return 1;
            case("2"):
                return 2;
            case("3"):
                return 3;
        }
        return 1;
    }

    public void setOpacity(float newOpacity)
    {
        float temp = newOpacity;
        if(temp > 1.0f)
            temp = 1.0f;
        if(temp < 0.0f)
            temp = 0.0f;
        dataHolder.setOpacity(temp);
    }

    public float getOpacity()
    {
        return dataHolder.getOpacity();
    }

    public void setBlur(String blurString)
    {
        Log.d("Scene Setter", "blur string: " + blurString);
        if(blurString.equals("none"))
        {
            blurSetting = DataCodes.BLUR_NONE;
            Log.d("Scene Setter", "no blur chosen");
        }
        else if(blurString.equals("low"))
        {
            blurSetting = DataCodes.BLUR_LOW;
            Log.d("Scene Setter", "low blur chosen");
        }
        else if(blurString.equals("high"))
        {
            blurSetting = DataCodes.BLUR_HIGH;
            Log.d("Scene Setter", "high blur chosen");
        }
    }

    public Bitmap getTexture(int sprite)
    {
        Bitmap bmp = null; //BitmapFactory.decodeResource(context.getResources(), R.drawable.girlmidsword);
        if(sprite == DataCodes.GIRL)
        {
            String choice = preferences.getString("texture_model", "3");
            if(choice.equals("0"))
                choice = girlTextureArray[girlTextureChoice];
            switch(choice)
            {
                case("1"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    if(blurSetting != DataCodes.BLUR_NONE)
                    {
                        if(blurSetting == DataCodes.BLUR_LOW)
                            bmp = BlurBuilder.blur(context, bmp, 9.5f, 0.8f);
                        else
                            bmp = BlurBuilder.blur(context, bmp, 13.5f, 0.5f);
                    }
                    girlOffset = 1.0f;
                    break;
                case("2"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    if(blurSetting != DataCodes.BLUR_NONE)
                    {
                        if(blurSetting == DataCodes.BLUR_LOW)
                            bmp = BlurBuilder.blur(context, bmp, 9.5f, 0.8f);
                        else
                            bmp = BlurBuilder.blur(context, bmp, 13.5f, 0.5f);
                    }
                    girlOffset = 1.0f;
                    break;
                case("3"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    if (preferences.getString("camera_blur", "none").equals("high"))
                    {
                        bmp = BlurBuilder.blur(context, bmp, 7.5f, 0.9f);
                        Log.d("Scene Setter", "sword blurry image chosen");
                    }
                    else
                        Log.d("Scene Setter", "sword non blurry image chosen");
                    girlOffset = 0.98f;
                    break;
                case("4"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    if (preferences.getString("camera_blur", "none").equals("high"))
                    {
                        bmp = BlurBuilder.blur(context, bmp, 7.5f, 0.9f);
                        Log.d("Scene Setter", "headphone blurry image chosen");
                    }else
                        Log.d("Scene Setter", "headphone non blurry image chosen");
                    girlOffset = 0.98f;
                    break;
                case("5"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    girlOffset = 0.95f;
                    break;
                case("6"):
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
                    girlOffset = 0.95f;
                    break;
            }
        }
        else if (sprite == DataCodes.TABLE)
        {
            Log.d("Scene Setter" , "table was called");
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
//            if(blurSetting != BLUR_NONE)
//            {
//                if(blurSetting == BLUR_LOW)
//                    bmp = BlurBuilder.blur(context, bmp, 9.5f, 0.8f);
//                else
//                    bmp = BlurBuilder.blur(context, bmp, 13.5f, 0.5f);
//            }
        }
        else if (sprite == DataCodes.ROOM)
        {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
        }
        else if (sprite == DataCodes.BUILDING)
        {
            Log.d("Scene Setter" , "table was called");
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
//            if(blurSetting != BLUR_NONE)
//            {
//                if(blurSetting == BLUR_LOW)
//                    bmp = BlurBuilder.blur(context, bmp, 9.5f, 0.8f);
//                else
//                    bmp = BlurBuilder.blur(context, bmp, 13.5f, 0.5f);
//            }
        }
        else if (sprite == DataCodes.CITY)
        {
            Log.d("Scene Setter" , "city was called");
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
//            if(blurSetting != BLUR_NONE)
//            {
//                if(blurSetting == BLUR_LOW)
//                    bmp = BlurBuilder.blur(context, bmp, 11.5f, 0.7f);
//                else
//                    bmp = BlurBuilder.blur(context, bmp, 15.5f, 0.4f);
//            }
        }
        else if (sprite == DataCodes.SKY)
        {
            Log.d("Scene Setter" , "sky was called");
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
//            if(blurSetting != BLUR_NONE)
//            {
//                if(blurSetting == BLUR_LOW)
//                    bmp = BlurBuilder.blur(context, bmp, 11.5f, 0.7f);
//                else
//                    bmp = BlurBuilder.blur(context, bmp, 15.5f, 0.4f);
//            }
        }
        else if(sprite == DataCodes.CLOUDS)
        {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
        }

        return bmp;
    }
    public int getTextureIndex(int texture)
    {
        if(texture == DataCodes.CITY)
        {
            if(preferences.getString("camera_blur", "none").equals("none"))
            {
                return 3;
            }
            return 13;
        }
        else if(texture == DataCodes.BUILDING)
        {
            if(preferences.getString("camera_blur", "none").equals("none"))
            {
                return 7;
            }
            return 17;
        }
        else if(texture == DataCodes.SKY)
        {
            if(preferences.getString("camera_blur", "none").equals("none"))
            {
                return 4;
            }
            return 14;
        }
        else if(texture == DataCodes.TABLE)
        {
            if(preferences.getString("camera_blur", "none").equals("none"))
            {
                return 2;
            }
            return 12;
        }
        else if(texture == DataCodes.CLOUDS)
        {
            if(preferences.getString("camera_blur", "none").equals("none"))
            {
                return 8;
            }
            return 18;

        }
        return 0;
    }
    private void shuffleGirl()
    {
        Log.d("Scene Setter" , "girl was shuffled. chose: " + girlTextureChoice);
        int choice = randomGenerator.nextInt(5);
        if(choice == girlTextureChoice)
        {
            if (choice == 5)
                choice = 5 - randomGenerator.nextInt(3) + 1;
            else
                choice++;
        }
        girlTextureChoice = choice;
        Log.d("Scene Setter" , "girl was shuffled. chose: " + girlTextureChoice);
    }
}
