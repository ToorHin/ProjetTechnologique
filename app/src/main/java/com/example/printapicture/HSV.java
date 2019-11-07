package com.example.printapicture;

import android.graphics.Color;

public class HSV {

    static void RGBToHSV(int r, int g, int b, float[] hsv){
        float r2 = r/255.f;
        float g2 = g/255.f;
        float b2 = b/255.f;

        float rgbmax = Math.max(r2,g2);
        float cmax = Math.max(rgbmax,b2);

        float rgbmin = Math.min(r2,g2);
        float cmin = Math.min(rgbmin,b2);

        float d = cmax - cmin;

        float h = 0;

        if(d == 0){
            h = 0;
        }
        else if(cmax == r2){
            h = 60*(((g2-b2)%6)/d);
        }
        else if(cmax == g2){
            h = 60*(((b2-r2)/d)+2);
        }
        else if(cmax == b2){
            h = 60*(((r2-g2)/d)+4);
        }

        float s = 0;

        if(cmax == 0){
            s = 0;
        }
        else{
            s = d/cmax;
        }

        hsv[0] = h;
        hsv[1]= s;
        hsv[2] = cmax;
    }

    static int HSVToRGB(float[] hsv){
        float c = hsv[2] * hsv[1];

        float x = c * (1-Math.abs((hsv[0]/60)%2 -1));

        float m = hsv[2] - c;

        float r2 = 0;
        float g2 = 0;
        float b2 = 0;

        if(hsv[0] >= 0f && hsv[0] < 60f){
            r2 = c;
            g2 = x;
            b2 = 0;
        }
        else if(hsv[0] >= 60f && hsv[0] < 120f){
            r2 = x;
            g2 = c;
            b2 = 0;
        }
        else if(hsv[0] >= 120f && hsv[0] < 180f){
            r2 = 0;
            g2 = c;
            b2 = x;
        }
        else if(hsv[0] >= 180f && hsv[0] < 240f){
            r2 = 0;
            g2 = x;
            b2 = c;
        }
        else if(hsv[0] >= 240f && hsv[0] < 300f){
            r2 = x;
            g2 = 0;
            b2 = c;
        }
        else if(hsv[0] >= 300f && hsv[0] < 360f){
            r2 = c;
            g2 = 0;
            b2 = x;
        }
        return Color.rgb((int) ((r2+m)*255),(int) ((g2+m)*255),(int) ((b2+m)*255));
    }

}
