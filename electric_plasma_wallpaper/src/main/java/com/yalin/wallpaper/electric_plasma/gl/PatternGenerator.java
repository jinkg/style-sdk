package com.yalin.wallpaper.electric_plasma.gl;

import java.lang.reflect.Array;

public class PatternGenerator {
    private static /* synthetic */ int[] $SWITCH_TABLE$mjp$android$wallpaper$plasma$gl$Symmetry;

    static /* synthetic */ int[] $SWITCH_TABLE$mjp$android$wallpaper$plasma$gl$Symmetry() {
        int[] iArr = $SWITCH_TABLE$mjp$android$wallpaper$plasma$gl$Symmetry;
        if (iArr == null) {
            iArr = new int[Symmetry.values().length];
            try {
                iArr[Symmetry.Horizontal.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Symmetry.HorizontalVertical.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Symmetry.None.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$mjp$android$wallpaper$plasma$gl$Symmetry = iArr;
        }
        return iArr;
    }

    public static void randomAdd(float[][] target, float randomscale, Symmetry symmetry) {
        int y;
        switch ($SWITCH_TABLE$mjp$android$wallpaper$plasma$gl$Symmetry()[symmetry.ordinal()]) {
            case 1:
                for (y = 0; y < target.length; y++) {
                    for (int x = 0; x < target[y].length; x++) {
                        target[y][x] = target[y][x] + ((((float) Math.random()) - 0.5f) * randomscale);
                    }
                }
                return;
            case 2:
                for (float[] row : target) {
                    symmetricalRandomAddRow(row, randomscale);
                }
                return;
            case 3:
                int half = (target.length / 2) + (target.length % 2);
                for (y = 0; y < half; y++) {
                    symmetricalRandomAddRow(target[y], randomscale);
                    target[(target.length - y) - 1] = (float[]) target[y].clone();
                }
                return;
            default:
                return;
        }
    }

    private static void symmetricalRandomAddRow(float[] row, float randomscale) {
        int half = (row.length / 2) + (row.length % 2);
        for (int x = 0; x < half; x++) {
            float value = row[x] + ((((float) Math.random()) - 0.5f) * randomscale);
            row[x] = value;
            row[(row.length - x) - 1] = value;
        }
    }

    public static void blur(float[][] target) {
        int w = target[0].length;
        int h = target.length;
        for (int y = 0; y < h; y++) {
            float[] row = (float[]) target[y].clone();
            float[] up = (float[]) target[((y - 1) + h) % h].clone();
            float[] down = (float[]) target[(y + 1) % h].clone();
            for (int x = 0; x < w; x++) {
                target[y][x] = ((((((row[x] + up[x]) + down[x]) * 2.0f) + ((row[(x + 1) % w] + row[((x - 1) + w) % w]) * 2.0f)) + (up[(x + 1) % w] + up[((x - 1) + w) % w])) + (down[(x + 1) % w] + down[((x - 1) + w) % w])) / 14.0f;
            }
        }
    }

    public static float[][] expand2D(int w, int h, float[][] source) {
        float[][] target = (float[][]) Array.newInstance(Float.TYPE, new int[]{h, w});
        int hs = source.length;
        int ws = source[0].length;
        for (int y = 0; y < h; y++) {
            int ys = (y * hs) / h;
            float yp = ((((float) y) * ((float) hs)) / ((float) h)) - ((float) ys);
            float[] row = source[ys];
            float[] down = source[(ys + 1) % hs];
            for (int x = 0; x < w; x++) {
                int xs = (x * ws) / w;
                float xp = ((((float) x) * ((float) ws)) / ((float) w)) - ((float) xs);
                target[y][x] = ((1.0f - yp) * ((row[xs] * (1.0f - xp)) + (row[(xs + 1) % ws] * xp))) + (((down[xs] * (1.0f - xp)) + (down[(xs + 1) % ws] * xp)) * yp);
            }
        }
        return target;
    }

    static byte[][] makeData(int width, int height, int max, int complexityWidth, int complexityHeight, Symmetry symmetry, int smoothness) {
        int i;
        float[][] raw = (float[][]) Array.newInstance(Float.TYPE, new int[]{complexityHeight, complexityWidth});
        randomAdd(raw, 32.0f, symmetry);
        for (i = 0; i <= 4; i++) {
            raw = expand2D(raw[0].length * 2, raw.length * 2, raw);
            blur(raw);
            randomAdd(raw, (float) (16 >> i), symmetry);
        }
        for (i = 0; i <= smoothness; i++) {
            blur(raw);
        }
        raw = expand2D(width, height, raw);
        blur(raw);
        byte[][] data = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{height, width});
        int scale = (int) (Math.sqrt((double) max) * 2.0d);
        for (int y = 0; y < height; y++) {
            float[] rawrow = raw[y];
            byte[] datarow = data[y];
            for (int x = 0; x < width; x++) {
                datarow[x] = (byte) ((((int) ((rawrow[x] + ((float) (max * 2))) * ((float) scale))) % max) & 255);
            }
        }
        return data;
    }
}
