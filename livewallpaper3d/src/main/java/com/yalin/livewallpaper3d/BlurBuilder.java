package com.yalin.livewallpaper3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Blake on 12/31/2015.
 */
public class BlurBuilder
{
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 9.5f;

    public static Bitmap blur(Context context, Bitmap image, float blurRadius, float scale) {
        int width = Math.round(image.getWidth() * scale);
        int height = Math.round(image.getHeight() * scale);
//        int width = image.getWidth();
//        int height = image.getHeight();

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        if(blurRadius != 0)
        {
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(blurRadius);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }
        outputBitmap = Bitmap.createScaledBitmap(outputBitmap, image.getWidth(), image.getHeight(), false);

        return outputBitmap;
    }
}
