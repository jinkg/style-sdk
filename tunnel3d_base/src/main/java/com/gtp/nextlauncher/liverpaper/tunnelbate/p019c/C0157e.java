package com.gtp.nextlauncher.liverpaper.tunnelbate.p019c;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.C0185f;
import com.gtp.nextlauncher.liverpaper.tunnelbate.opengl.C0197r;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: LoadUtil2 */
public class C0157e {
    public static C0185f m924a(String str, Resources resources, Context context) {
        ArrayList<Float> arrayList = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList();
        ArrayList<Float> arrayList3 = new ArrayList();
        HashMap hashMap = new HashMap();
        ArrayList<Float> arrayList4 = new ArrayList();
        ArrayList<Float> arrayList5 = new ArrayList();
        float f = 1.0E7f;
        float f2 = -1.0E7f;
        float f3 = 1.0E7f;
        float f4 = -1.0E7f;
        float f5 = 1.0E7f;
        float f6 = -1.0E7f;
        ArrayList<Float> arrayList6 = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resources.getAssets().open(str)));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                float parseFloat = 0;
                float parseFloat2 = 0;
                float max = 0;
                float min = 0;
                float min2 = 0;
                String[] split = readLine.split("[ ]+");
                if (split[0].trim().equals("v")) {
                    parseFloat = Float.parseFloat(split[1]);
                    parseFloat2 = Float.parseFloat(split[2]);
                    float parseFloat3 = Float.parseFloat(split[3]);
                    arrayList.add(parseFloat);
                    arrayList.add(parseFloat2);
                    arrayList.add(parseFloat3);
                    f = Math.min(f, parseFloat);
                    max = Math.max(f2, parseFloat);
                    min = Math.min(f3, parseFloat2);
                    parseFloat2 = Math.max(f4, parseFloat2);
                    min2 = Math.min(f5, parseFloat3);
                    parseFloat = Math.max(f6, parseFloat3);
                    f6 = min2;
                    min2 = parseFloat2;
                    parseFloat2 = min;
                    min = max;
                    max = f;
                } else if (!split[0].trim().equals("vn")) {
                    if (split[0].trim().equals("vt")) {
                        arrayList4.add(Float.parseFloat(split[1]));
                        arrayList4.add(Float.parseFloat(split[2]));
                        parseFloat = f6;
                        min2 = f4;
                        parseFloat2 = f3;
                        min = f2;
                        max = f;
                        f6 = f5;
                    } else {
                        if (split[0].trim().equals("f")) {
                            int[] iArr = new int[3];
                            iArr[0] = Integer.parseInt(split[1].split("/")[0]) - 1;
                            min = arrayList.get(iArr[0] * 3);
                            max = arrayList.get((iArr[0] * 3) + 1);
                            parseFloat = (Float) arrayList.get((iArr[0] * 3) + 2);
                            arrayList3.add(min);
                            arrayList3.add(Float.valueOf(max));
                            arrayList3.add(Float.valueOf(parseFloat));
                            iArr[1] = Integer.parseInt(split[2].split("/")[0]) - 1;
                            min = ((Float) arrayList.get(iArr[1] * 3)).floatValue();
                            max = ((Float) arrayList.get((iArr[1] * 3) + 1)).floatValue();
                            parseFloat = ((Float) arrayList.get((iArr[1] * 3) + 2)).floatValue();
                            arrayList3.add(Float.valueOf(min));
                            arrayList3.add(Float.valueOf(max));
                            arrayList3.add(Float.valueOf(parseFloat));
                            iArr[2] = Integer.parseInt(split[3].split("/")[0]) - 1;
                            min = ((Float) arrayList.get(iArr[2] * 3)).floatValue();
                            max = ((Float) arrayList.get((iArr[2] * 3) + 1)).floatValue();
                            parseFloat = ((Float) arrayList.get((iArr[2] * 3) + 2)).floatValue();
                            arrayList3.add(Float.valueOf(min));
                            arrayList3.add(Float.valueOf(max));
                            arrayList3.add(Float.valueOf(parseFloat));
                            arrayList2.add(Integer.valueOf(iArr[0]));
                            arrayList2.add(Integer.valueOf(iArr[1]));
                            arrayList2.add(Integer.valueOf(iArr[2]));
                            int parseInt = Integer.parseInt(split[1].split("/")[1]) - 1;
                            arrayList5.add(arrayList4.get(parseInt * 2));
                            arrayList5.add(arrayList4.get((parseInt * 2) + 1));
                            parseInt = Integer.parseInt(split[2].split("/")[1]) - 1;
                            arrayList5.add(arrayList4.get(parseInt * 2));
                            arrayList5.add(arrayList4.get((parseInt * 2) + 1));
                            parseInt = Integer.parseInt(split[3].split("/")[1]) - 1;
                            arrayList5.add(arrayList4.get(parseInt * 2));
                            arrayList5.add(arrayList4.get((parseInt * 2) + 1));
                        }
                        parseFloat = f6;
                        min2 = f4;
                        parseFloat2 = f3;
                        min = f2;
                        max = f;
                        f6 = f5;
                    }
                }
                f5 = f6;
                f4 = min2;
                f3 = parseFloat2;
                f2 = min;
                f = max;
                f6 = parseFloat;
            }
            int size = arrayList3.size();
            float[] fArr = new float[size];
            for (int i = 0; i < size; i++) {
                fArr[i] = ((Float) arrayList3.get(i)).floatValue();
            }
            float[] fArr2 = new float[(arrayList2.size() * 3)];
            int size2 = arrayList5.size();
            float[] fArr3 = new float[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                fArr3[i2] = ((Float) arrayList5.get(i2)).floatValue();
            }
            return new C0185f(context, fArr, fArr3, fArr2, new C0197r((f2 + f) / 2.0f, (f4 + f3) / 2.0f, (f6 + f5) / 2.0f));
        } catch (Exception e) {
            Log.d("load error", "load error");
            e.printStackTrace();
            return null;
        }
    }
}
