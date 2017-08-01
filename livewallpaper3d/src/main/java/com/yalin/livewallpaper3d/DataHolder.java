package com.yalin.livewallpaper3d;

import java.util.Random;

/**
 * Created by Blake on 8/19/2015.
 */
public class DataHolder
{

    public DataHolder()
    {
    }

    private float opacity = 1.0f;

    public float getOpacity()
    {
        return opacity;
    }

    public void setOpacity(float newOpacity)
    {
        if (newOpacity > 1)
            opacity = 1;
        else if (opacity < 0)
            opacity = 0;
        else
            opacity = newOpacity;
        updateGirlColor();
    }

    public void updateGirlColor()
    {
        normalColorGirl = new float[]
                {1f, 1f, 1f, opacity,
                        1f, 1f, 1f, opacity,
                        1f, 1f, 1f, opacity,
                        1f, 1f, 1f, opacity};

        nightColorGirl = new float[]
                {0.17f, 0.27f, 0.27f, opacity,
                        0.17f, 0.27f, 0.63921568627f, opacity,
                        0.17f, 0.27f, 0.63921568627f, opacity,
                        0.17f, 0.27f, 0.63921568627f, opacity};

        dawnColorGirl = new float[]
                {
                        0.247f, 0.275f, 0.443f, opacity,
                        0.247f, 0.275f, 0.443f, opacity,
                        0.247f, 0.275f, 0.443f, opacity,
                        0.247f, 0.275f, 0.443f, opacity
                };
        sunsetColorGirl = new float[]
                {
                        0.929f, 0.586f, 0.551f, opacity,
                        0.929f, 0.586f, 0.551f, opacity,
                        0.929f, 0.586f, 0.551f, opacity,
                        0.929f, 0.586f, 0.551f, opacity
                };
    }

    public static short indices[] = {0, 1, 2, 0, 2, 3};

    public short[] getCloudIndices()
    {
        short[] mIndices = new short[30 * 6];
        int last = 0;
        for (int i = 0; i < 30; i++)
        {
            // We need to set the new indices for the new quad
            mIndices[(i * 6) + 0] = (short) (last + 0);
            mIndices[(i * 6) + 1] = (short) (last + 1);
            mIndices[(i * 6) + 2] = (short) (last + 2);
            mIndices[(i * 6) + 3] = (short) (last + 0);
            mIndices[(i * 6) + 4] = (short) (last + 2);
            mIndices[(i * 6) + 5] = (short) (last + 3);

            // Our indices are connected to the vertices so we need to keep them
            // in the correct order.
            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
            last = last + 4;
        }
        return mIndices;
    }

    public float[][] getCloudVertices(int number, int size)
    {
        Random rnd = new Random();

        // Our collection of vertices
        float[][] vertices = new float[number][12];
        int swp;
        int shp;
        // Create the vertex data
        for (int i = 0; i < number; i++)
        {
            float minX = -5.0f;
            float maxX = 1.8f;
            float minY = -1.3f;
            float maxY = 3.4f;
            float offset_x = 0.0f;//rnd.nextFloat() * (maxX - minX) + minX;
            float offset_y = rnd.nextFloat() * (maxY - minY) + minY;

            // Create the 2D parts of our 3D vertices, others are default 0.0f
            vertices[i][0] = offset_x;
            vertices[i][1] = offset_y + 1.0f;
            vertices[i][2] = 2.0f;
            vertices[i][3] = offset_x;
            vertices[i][4] = offset_y;
            vertices[i][5] = 2.0f;
            vertices[i][6] = offset_x + 1.0f;
            vertices[i][7] = offset_y;
            vertices[i][8] = 2.0f;
            vertices[i][9] = offset_x + 1.0f;
            vertices[i][10] = offset_y + 1.0f;
            vertices[i][11] = 2.0f;
        }
        return vertices;
    }

    //Sprite posisitions
    public float[] tableVertices = new float[]{
            -4.5f, -0.8f, -2.0f,   // top left
            -4.5f, -1.3f, -2.0f,   // bottom left
            0.7f, -1.3f, -2.0f,   // bottom right
            0.7f, -0.8f, -2.0f}; // top right

    public float[] roomVertices = new float[]{
//    -5.7f,  3.2f, 2.0f,   // top left
//            -5.7f, -2.0f, 2.0f,   // bottom left
//            2.1f, -2.0f, 2.0f,   // bottom right
//            2.1f,  3.2f, 2.0f }; // top right

            3.2f, 3.1f, 2.0f, //top right
            3.2f, -2.0f, 2.0f,   //bottom right
            -7.0f, -2.0f, 2.0f,   // bottom left
            -7.0f, 3.1f, 2.0f};   // top left

    public float[] buildingVertices = new float[]{
            4.3f, 4.7f, 4.0f,   // top left
            4.3f, -2.0f, 4.0f,   // bottom left
            0.0f, -2.0f, 4.0f,   // bottom right
            0.0f, 4.7f, 4.0f}; // top right

    public float[] girlMidStanding = new float[]{
            -1.04f, 1.1f, 0.0f,   // top left
            -1.04f, -1.2f, 0.0f,   // bottom left
            0.37f, -1.2f, 0.0f,   // bottom right
            0.37f, 1.1f, 0.0f}; // top right

    public float[] girlBackSitting = new float[]{
            -1.24f, 2.3f, 1.9999f,   // top left
            -1.24f, -0.75f, 1.9999f,   // bottom left
            0.87f, -0.75f, 1.9999f,   // bottom right
            0.87f, 2.3f, 1.9999f}; // top right

    public float[] girlFrontReading = new float[]{
            0.32f, 0.8f, -2.0f,   // top right
            0.32f, -1.0f, -2.0f,   // bottom right
            -0.85f, -1.0f, -2.0f,   // bottom left
            -0.85f, 0.8f, -2.0f}; // top left

    public float[] girlSwordVertices = new float[]{
            -0.435f, 0.0f, 0.0f,   // top left
            -0.435f, -1.435f, 0.0f,   // bottom left
            0.435f, -1.435f, 0.0f,   // bottom right
            0.435f, 0.0f, 0.0f}; // top right

    public float[] cityVertices = new float[]{
            -3.0f, 2.4f, 2.0f,   // top left
            -3.0f, -2.3f, 2.0f,   // bottom left
            1.8f, -2.3f, 2.0f,   // bottom right
            1.8f, 2.4f, 2.0f}; // top right

    public float[] skyVertices = new float[]{
            -5.0f, 3.4f, 2.0f,   // top left
            -5.0f, -1.3f, 2.0f,   // bottom left
            1.8f, -1.3f, 2.0f,   // bottom right
            1.8f, 3.4f, 2.0f}; // top right


    /*
    Colors
     */

    //Texture colors
    public float[] normalColor = new float[]{
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f
    };

    public float[] nightColor = new float[]{
            0.37f, 0.47f, 0.83921568627f, 1.0f,
            0.37f, 0.47f, 0.83921568627f, 1.0f,
            0.37f, 0.47f, 0.83921568627f, 1.0f,
            0.37f, 0.47f, 0.83921568627f, 1.0f
    };

    public float[] dawnColor = new float[]{
            0.247f, 0.275f, 0.443f, 1.0f,
            0.247f, 0.275f, 0.443f, 1.0f,
            0.247f, 0.275f, 0.443f, 1.0f,
            0.247f, 0.275f, 0.443f, 1.0f
    };
    public float[] sunsetColor = new float[]{
            0.929f, 0.586f, 0.551f, 1.0f,
            0.929f, 0.586f, 0.551f, 1.0f,
            0.929f, 0.586f, 0.551f, 1.0f,
            0.929f, 0.586f, 0.551f, 1.0f
    };

    /*
    Girl Colors
     */
    public float[] normalColorGirl = new float[]
            {
                    1f, 1f, 1f, opacity,
                    1f, 1f, 1f, opacity,
                    1f, 1f, 1f, opacity,
                    1f, 1f, 1f, opacity
            };
    public float[] sunsetColorGirl = new float[]
            {
                    0.929f, 0.586f, 0.551f, opacity,
                    0.929f, 0.586f, 0.551f, opacity,
                    0.929f, 0.586f, 0.551f, opacity,
                    0.929f, 0.586f, 0.551f, opacity
            };
    public float[] dawnColorGirl = new float[]
            {
                    0.247f, 0.275f, 0.443f, opacity,
                    0.247f, 0.275f, 0.443f, opacity,
                    0.247f, 0.275f, 0.443f, opacity,
                    0.247f, 0.275f, 0.443f, opacity
            };
    public float[] nightColorGirl = new float[]
            {
                    0.17f, 0.27f, 0.63921568627f, opacity,
                    0.17f, 0.27f, 0.63921568627f, opacity,
                    0.17f, 0.27f, 0.63921568627f, opacity,
                    0.17f, 0.27f, 0.63921568627f, opacity
            };
    public float[][] girlColorArray = {
            dawnColorGirl,
            normalColorGirl,
            sunsetColorGirl,
            nightColorGirl};

    /*
    Sky Colors
     */
    public float[] skyColorNormal = new float[]{
            0.035f, 0.216f, 0.631f, 1.0f,
            0.035f, 0.216f, 0.831f, 1.0f,
            0.9f, 0.9f, 0.9f, 1.0f,
            0.035f, 0.216f, 0.631f, 1.0f};

    public float[] skyColorNight = new float[]{
            0.0f, 0.0f, 0.0f, 1.0f,
            0.008f, 0.0043f, 0.12f, 1.0f,
            0.016f, 0.0086f, 0.24f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f};

    public float[] skyColorSunset = new float[]{
            0.059f, 0.361f, .953f, 1.0f,
            1.0f, 0.376f, .075f, 1.0f,
            1.0f, 0.286f, 0.0f, 1.0f,
            0.059f, 0.361f, .953f, 1.0f};

    public float[] skyColorDawn = new float[]{
            0.176f, 0.40f, 0.776f, 1.0f,
            1.0f, 0.812f, 0.243f, 1.0f,
            1.0f, 0.812f, 0.243f, 1.0f,
            0.176f, 0.40f, 0.776f, 1.0f};

    public float[][] skyColorArray = {skyColorDawn,
            skyColorNormal,
            skyColorSunset,
            skyColorNight};


    public float[] getCloudColors()
    {
        float[] colors = new float[30 * 4 * 4];
        for (int i = 0; i < colors.length ; i++)
        {
            colors[i] = 1.0f;
        }
        return colors;
    }

}
