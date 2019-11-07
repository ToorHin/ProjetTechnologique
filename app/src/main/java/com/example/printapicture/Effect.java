package com.example.printapicture;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

public class Effect {

    // ------ shade of gray ------ //
    static String toGray(Bitmap bmp){
        long startTime = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int [] pixels = new int [w * h];

        bmp.getPixels(pixels, 0, w, 0,0,w,h);
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                int index = y * w + x;
                int c = pixels[index];
                int r = Color.red(c);
                int g = Color.green(c);
                int b = Color.blue(c);

                int moy = (int) (0.3 * r + 0.59 * g + 0.11 *b );
                pixels [index] = Color.rgb(moy,moy,moy);
            }
        }
        bmp.setPixels(pixels,0,w,0,0,w,h);
        long endTime = System.currentTimeMillis();
        return Long.toString(endTime - startTime);
    }

    // ------ Only the red color ------ //
    static String justInRed (Bitmap bmp){
        long startTime = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int [] pixels = new int [w * h];
        bmp.getPixels(pixels, 0, w, 0,0,w,h);
        for(int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = y * w + x;
                int c = pixels[index];
                int r = Color.red(c);
                int g = Color.green(c);
                int b = Color.blue(c);

                float[] hsv = new float[3];  //init variable HSV
                HSV.RGBToHSV(r, g, b, hsv);

                float hue = hsv[0]; //color
                float sat = hsv[1]; //saturation
                float val = hsv[2]; //light

                if(hsv[0] <= 10 || hsv[0] >= 355){
                    pixels[index] = HSV.HSVToRGB(new float[]{hue, sat, val});
                }
                else {
                    int moy = (int) (0.3 * r + 0.59 * g + 0.11 *b );
                    pixels [index] = Color.rgb(moy,moy,moy);
                }
            }
        }
        bmp.setPixels(pixels,0,w,0,0,w,h);
        long endTime = System.currentTimeMillis();
        return Long.toString(endTime - startTime);
    }

    // ------ apply a random color on the picture ------ //
    static String colorize (Bitmap bmp){
        long startTime = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int [] pixels = new int [w * h];
        bmp.getPixels(pixels, 0, w, 0,0,w,h);

        Random rand = new Random();
        float color = rand.nextFloat() * 360;   //random color

        for(int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = y * w + x;
                int c = pixels[index];
                int r = Color.red(c);
                int g = Color.green(c);
                int b = Color.blue(c);

                float[] hsv = new float[3];  //init variable HSV
                HSV.RGBToHSV(r, g, b, hsv);

                hsv[0] = color;
                float hue = hsv[0]; //color
                float sat = hsv[1]; //saturation
                float val = hsv[2]; //light

                pixels[index] = HSV.HSVToRGB(new float[]{hue, sat, val});
            }
        }
        bmp.setPixels(pixels,0,w,0,0,w,h);
        long endTime = System.currentTimeMillis();
        return Long.toString(endTime - startTime);
    }

    // ------ algorithm of LUT ------ //
    // ------ use in contrast ------ //
    static float[] LUTrgb(int min, int max){
        float[] LUT = new float[256];
        for( int i = 0; i < 256 ; i++){
            LUT[i] = (255 * ( i - min ))/(max - min);
        }
        return LUT;
    }

    // ------ balanced the contrast of the picture ------ //
    static String contrast(Bitmap bmp){
        long startTime = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] Pixels = new int[w * h];
        bmp.getPixels(Pixels, 0, w, 0, 0, w, h);

        float[] histoR = Histogram.createHistogram(bmp, "red");
        float[] histoG = Histogram.createHistogram(bmp, "green");
        float[] histoB = Histogram.createHistogram(bmp, "blue");

        int minR = Histogram.minInHisto(histoR);
        int maxR = Histogram.maxInHisto(histoR);
        int minG = Histogram.minInHisto(histoG);
        int maxG = Histogram.maxInHisto(histoG);
        int minB = Histogram.minInHisto(histoB);
        int maxB = Histogram.maxInHisto(histoB);

        float[] LUTr = LUTrgb(minR, maxR);
        float[] LUTg = LUTrgb(minG, maxG);
        float[] LUTb = LUTrgb(minB, maxB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = Pixels[y * w + x];
                int r = (int) LUTr[Color.red(c)];
                int g = (int) LUTg[Color.green(c)];
                int b = (int) LUTb[Color.blue(c)];
                Pixels[y * w + x] = Color.rgb(r, g, b);
            }
        }
        bmp.setPixels(Pixels, 0, w, 0, 0, w, h);
        long endTime = System.currentTimeMillis();
        return Long.toString(endTime - startTime);
    }

}
