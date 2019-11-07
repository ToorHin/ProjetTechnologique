package com.example.printapicture;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Histogram {

    // ------ create and fill a histogram in function of the color in parameter ------ //
    static float[] createHistogram(Bitmap image, String color) {
        float[] histogram = new float[256];

        //initialize
        for (int i = 0; i < 256; i++) {
            histogram[i] = 0;
        }

        int w = image.getWidth();
        int h = image.getHeight();

        int[] pixels = new int[w * h];
        image.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int p = pixels[i];
                int value = 0;

                switch (color) {
                    case "red":
                        value = Color.red(p);
                        break;
                    case "green":
                        value = Color.green(p);
                        break;
                    case "blue":
                        value = Color.blue(p);
                        break;
                }
                histogram[value]++;
            }
        }
        return histogram;
    }

    // ------ used in the algorithm of LUT ------ //
    static int maxInHisto(float[] histogram){
        for (int i = 255; i >= 0; i--) {
            if (histogram[i] > 10) {
                return i;
            }
        }
        return 0;
    }

    static int minInHisto(float[] histogram){
        for (int i = 0; i < 256; i++) {
            if (histogram[i] > 10) {
                return i;
            }
        }
        return 0;
    }

    // ------ rezise the histogram ------ //
    // ------ before : max value -> width * height ------ //
    // ------ after : max value -> 100 ------ //
    // ------ so the max height of the picture of the histogram is 100px ------ //
    // ------ used in bitmapHistogram ------ //
    static float[] resizeHistogram(float[] histogram){
        float max = 0;
        for ( int i = 0; i < 256 ; i++){
            if (max < histogram[i]) {
                max = histogram[i];
            }
        }
        for (int i = 0; i < 256 ; i++){
            histogram[i] = (histogram[i] / max) * 100;
        }
        return histogram;
    }

    // ------ create the tab of pixel for the picture of the histogram ------ //
    // ------ take the color in parameter ------ //
    // ------ used in bitmapHistogram ------ //
    static int[] pixelsHistogram(float[] histo, String color, int width, int height){
        int[] pixels = new int[width * height];
        for(int i = 0; i < width * height; i++){
            pixels[i] = Color.WHITE;
        }
        for(int x = 0; x < 256; x++) {
            int y = 1;
            while (y < histo[x]) {
                int i = ((width * height) - width) - (y * width) + x;
                switch (color) {
                    case "red":
                        pixels[i] = Color.RED;
                        break;
                    case "green":
                        pixels[i] = Color.GREEN;
                        break;
                    case "blue":
                        pixels[i] = Color.BLUE;
                        break;
                }
                y++;
            }
        }
        return pixels;
    }

    // ------ set the pixels of the histograms in the 3 pictures ------ //
    static void bitmapHistogram(Bitmap bmp, Bitmap histoR, Bitmap histoG, Bitmap histoB){
        int width = 255;
        int height = 100;

        float[] tabR = createHistogram(bmp,  "red");
        float[] tabG = createHistogram(bmp,  "green");
        float[] tabB = createHistogram(bmp,  "blue");

        tabR = resizeHistogram(tabR);
        tabG = resizeHistogram(tabG);
        tabB = resizeHistogram(tabB);

        int[] pixelsR = pixelsHistogram( tabR , "red", width, height);
        int[] pixelsG = pixelsHistogram( tabG, "green", width, height);
        int[] pixelsB = pixelsHistogram( tabB, "blue", width, height);

        histoR.setPixels(pixelsR, 0, width, 0, 0, width, height);
        histoG.setPixels(pixelsG, 0, width, 0, 0, width, height);
        histoB.setPixels(pixelsB, 0, width, 0, 0, width, height);

    }
}
